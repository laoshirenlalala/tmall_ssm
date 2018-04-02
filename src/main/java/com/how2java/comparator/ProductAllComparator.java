package com.how2java.comparator;

import com.how2java.pojo.Product;

import java.util.Comparator;

public class ProductAllComparator implements Comparator<Product> {
    public int compare(Product p1, Product p2){
        return p2.getSaleCount() * p2.getReviewCount() - p1.getSaleCount() * p1.getReviewCount();
    }
}
