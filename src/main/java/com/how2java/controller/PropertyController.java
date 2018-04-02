package com.how2java.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.how2java.pojo.Category;
import com.how2java.pojo.Property;
import com.how2java.service.CategoryService;
import com.how2java.service.PropertyService;
import com.how2java.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class PropertyController {
    @Autowired
    PropertyService propertyService;

    @Autowired
    CategoryService categoryService;

    @RequestMapping("admin_property_list")
    public String list(int cid, Model model, Page page){
        PageHelper.offsetPage(page.getStart(), page.getCount());
        List<Property> ps = propertyService.list(cid);
        Category category = categoryService.get(cid);
        int total = (int)new PageInfo<>(ps).getTotal();
        page.setTotal(total);
        page.setParam("&cid=" + cid);
        model.addAttribute("ps", ps);
        model.addAttribute("c", category);
        model.addAttribute("page", page);
        return "admin/listProperty";
    }
    @RequestMapping("admin_property_add")
    public String add(Property property){
        propertyService.add(property);
        return "redirect:/admin_property_list?cid=" + property.getCid();
    }

    @RequestMapping("admin_property_delete")
    public String delete(int id){
        Property property = propertyService.get(id);
        propertyService.delete(property.getId());
        return "redirect:admin_property_list?cid=" + property.getCid();
    }

    @RequestMapping("admin_property_edit")
    public String edit(Model model, int id){
        Property p = propertyService.get(id);
        Category c = categoryService.get(p.getCid());
        p.setCategory(c);
        model.addAttribute("p", p);
        return "admin/editProperty";
    }

    @RequestMapping("admin_property_update")
    public String update(Property property){
        propertyService.update(property);
        return "redirect:/admin_property_list?cid=" + property.getCid();
    }
}
