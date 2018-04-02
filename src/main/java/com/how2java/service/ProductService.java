package com.how2java.service;

import com.how2java.mapper.ProductMapper;
import com.how2java.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductMapper productMapper;

    @Autowired
    ProductImageService productImageService;

    @Autowired
    ReviewService reviewService;

    @Autowired
    OrderItemService orderItemService;

    public List<Product> list(int cid){
        ProductExample example = new ProductExample();
        example.createCriteria().andCidEqualTo(cid);
        example.setOrderByClause("id desc");
        return productMapper.selectByExample(example);
    }

    public List<Product> list(String keyword){
        ProductExample example = new ProductExample();
        example.createCriteria().andNameLike("%" + keyword + "%");
        List<Product> ps = productMapper.selectByExample(example);
        for (Product p : ps) {
            setFirstImage(p);
            setSaleCount(p);
            setReviewCount(p);
        }
        return ps;
    }

    public void add(Product product){
        productMapper.insert(product);
    }

    public void delete(int id){
        productMapper.deleteByPrimaryKey(id);
    }

    public Product get(int id){
        return productMapper.selectByPrimaryKey(id);
    }

    public void update(Product product){
        productMapper.updateByPrimaryKey(product);
    }

    public void setFirstImage(Product p){
        List<ProductImage> pis = productImageService.list(p.getId(), "type_single");
        if(!pis.isEmpty()){
            ProductImage piFirst = pis.get(0);
            p.setFirstImage(piFirst);
        }
    }

    public void fillProductSingleImages(Product product){
        List<ProductImage> productSingleImages = productImageService.list(product.getId(), "type_single");
        product.setProductSingleImages(productSingleImages);
    }

    public void fillProductDetailImages(Product product){
        List<ProductImage> productDetailImages = productImageService.list(product.getId(), "type_detail");
        product.setProductDetailImages(productDetailImages);
    }

    public void setReviewCount(Product product){
        List<Review> reviews = reviewService.list(product.getId());
        if (reviews.isEmpty())
            product.setReviewCount(0);
        else
            product.setReviewCount(reviews.size());
    }

    public void setSaleCount(Product product){
        int saleCount = orderItemService.saleCount(product);
        product.setSaleCount(saleCount);
    }
}
