package com.how2java.controller;

import com.how2java.pojo.Category;
import com.how2java.pojo.Product;
import com.how2java.pojo.Property;
import com.how2java.pojo.PropertyValue;
import com.how2java.service.CategoryService;
import com.how2java.service.ProductService;
import com.how2java.service.PropertyService;
import com.how2java.service.PropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class PropertyValueController {
    @Autowired
    PropertyValueService propertyValueService;

    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    PropertyService propertyService;

    @RequestMapping("admin_propertyValue_edit")
    public String list(int pid, Model model){
        Product p = productService.get(pid);
        Category c = categoryService.get(p.getCid());
        p.setCategory(c);
        propertyValueService.init(p);
        List<PropertyValue> pvs = propertyValueService.list(pid);
        model.addAttribute("pvs",pvs);
        model.addAttribute("p", p);
        return "admin/editPropertyValue";
    }

    @RequestMapping("admin_propertyValue_update")
    @ResponseBody
    public String update(PropertyValue propertyValue){
        propertyValueService.update(propertyValue);
        return "success";
    }
}
