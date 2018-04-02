package com.how2java.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.how2java.pojo.Category;
import com.how2java.pojo.Product;
import com.how2java.service.CategoryService;
import com.how2java.service.ProductService;
import com.how2java.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class ProductController {
    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    @RequestMapping("admin_product_list")
    public String list(Model model, int cid, Page page){
        PageHelper.offsetPage(page.getStart(), page.getCount());
        Category c = categoryService.get(cid);
        List<Product> ps = productService.list(cid);
        for (Product p : ps){
            productService.setFirstImage(p);
        }
        int total = (int)new PageInfo<>(ps).getTotal();
        page.setTotal(total);
        page.setParam("&cid=" + cid);
        model.addAttribute("ps", ps);
        model.addAttribute("page", page);
        model.addAttribute("c", c);
        return "admin/listProduct";
    }

    @RequestMapping("admin_product_add")
    public String add(Product product){
        productService.add(product);
        return "redirect:/admin_product_list?cid=" + product.getCid();
    }

    @RequestMapping("admin_product_delete")
    public String delete(int id){
        Product p = productService.get(id);
        productService.delete(id);
        return "redirect:/admin_product_list?cid=" + p.getCid();
    }
    @RequestMapping("admin_product_edit")
    public String edit(Model model, int id){
        Product p = productService.get(id);
        Category c = categoryService.get(id);
        p.setCategory(c);
        model.addAttribute("p", p);
        model.addAttribute("c", c);
        return "admin/editProduct";
    }

    @RequestMapping("admin_product_update")
    public String update(Product product){
        productService.update(product);
        return "redirect:/admin_product_list?cid=" + product.getCid();
    }

}
