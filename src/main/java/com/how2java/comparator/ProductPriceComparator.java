package com.how2java.comparator;

import com.how2java.pojo.Product;

import java.util.Comparator;

public class ProductPriceComparator implements Comparator<Product> {
    public int compare(Product p1, Product p2){
        return (int) (p1.getPromotePrice() - p2.getPromotePrice());
    }
}
