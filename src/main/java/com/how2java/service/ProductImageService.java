package com.how2java.service;

import com.how2java.mapper.ProductImageMapper;
import com.how2java.pojo.ProductImage;
import com.how2java.pojo.ProductImageExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductImageService {
    @Autowired
    ProductImageMapper productImageMapper;

    public List<ProductImage> list(int pid, String type){
        ProductImageExample example = new ProductImageExample();
        example.createCriteria().andPidEqualTo(pid)
                                .andTypeEqualTo(type);
        example.setOrderByClause("id desc");
        return productImageMapper.selectByExample(example);
    }

    public void add(ProductImage productImage){
        productImageMapper.insert(productImage);
    }

    public void delete(int id){
        productImageMapper.deleteByPrimaryKey(id);
    }

    public ProductImage get(int id){
        return productImageMapper.selectByPrimaryKey(id);
    }
}
