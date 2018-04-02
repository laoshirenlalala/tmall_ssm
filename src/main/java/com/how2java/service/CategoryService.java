package com.how2java.service;

import com.how2java.mapper.CategoryMapper;
import com.how2java.pojo.Category;
import com.how2java.pojo.CategoryExample;
import com.how2java.pojo.Product;
import com.how2java.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    ProductService productService;

    public List<Category> list(){
        CategoryExample example = new CategoryExample();
        example.setOrderByClause("id desc");
        return categoryMapper.selectByExample(example);
    }
    public void add(Category category){
        categoryMapper.insert(category);
    }
    public void delete(int id){
        categoryMapper.deleteByPrimaryKey(id);
    }
    public Category get(int id){
        return categoryMapper.selectByPrimaryKey(id);
    }
    public void update(Category category){
        categoryMapper.updateByPrimaryKey(category);
    }

    public void fillProducts(Category category){
        List<Product> products = productService.list(category.getId());
        for (Product p : products)
            productService.setFirstImage(p);
        category.setProducts(products);
    }

    public List<List<Product>> fillProductsByRow(Category categry){
        List<Product> products = categry.getProducts();
        List<List<Product>> productsByRow = new ArrayList<>();
        for (int i= 0; i < products.size(); i++){
            int num = 8;
            int size = i + num;
            size = size > products.size() ? products.size() : size;
            List<Product> productsOfEachRow = products.subList(i, size);
            productsByRow.add(productsOfEachRow);
        }
        categry.setProductsByRow(productsByRow);
        return productsByRow;
    }
}
