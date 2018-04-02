package com.how2java.comparator;

import com.how2java.pojo.Product;

import java.util.Comparator;

public class ProductDateComparator implements Comparator<Product> {
    public int compare(Product p1, Product p2){
        return p1.getCreateDate().compareTo(p2.getCreateDate());
    }
}
