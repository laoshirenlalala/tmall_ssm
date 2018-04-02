package com.how2java.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.how2java.pojo.Category;
import com.how2java.service.CategoryService;
import com.how2java.util.ImageUtil;
import com.how2java.util.Page;
import com.how2java.util.UploadedImageFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @RequestMapping("admin_category_list")
    public String list(Model model, Page page){
        PageHelper.offsetPage(page.getStart(), page.getCount());
        List<Category> cs= categoryService.list();
        int total = (int) new PageInfo<>(cs).getTotal();
        page.setTotal(total);
        model.addAttribute("cs", cs);
        model.addAttribute("page", page);
        return "admin/listCategory";
    }
    @RequestMapping("admin_category_add")
    public String add(Category category, HttpSession session, UploadedImageFile uiif){
        categoryService.add(category);
        File path = new File(session.getServletContext().getRealPath("img/category"));
        File file = new File(path, category.getId() + ".jpg");
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        try {
            uiif.getImage().transferTo(file);
            BufferedImage image = ImageUtil.change2jpg(file);
            ImageIO.write(image, "jpg", file);
        }catch(IOException e){
            e.getStackTrace();
        }
        return "redirect:/admin_category_list";
    }
    @RequestMapping("admin_category_delete")
    public String delete(int id, HttpSession session){
        categoryService.delete(id);
        File path = new File(session.getServletContext().getRealPath("img/category"));
        File image = new File(path, id+".jpg");
        image.delete();
        return "redirect:/admin_category_list";
    }
    @RequestMapping("admin_category_edit")
    public String edit(int id,Model model) throws IOException {
        Category c= categoryService.get(id);
        model.addAttribute("c", c);
        return "admin/editCategory";
    }
    @RequestMapping("admin_category_update")
    public String update(Category category, UploadedImageFile uiif, HttpSession session){
        categoryService.update(category);
        /*File path = new File(session.getServletContext().getRealPath("img/category"));
        File fileOld = new File(path, category.getId() + ".jpg");
        fileOld.delete();
        File fileNew = new File(path, category.getId() + ".jpg");
        try{
            uiif.getImage().transferTo(fileNew);
            BufferedImage image = ImageUtil.change2jpg(fileNew);
            ImageIO.write(image, ".jsp", fileNew);
        }catch(IOException e){
            e.getStackTrace();
        }*/
        MultipartFile image = uiif.getImage();
        if (null != image && !image.isEmpty()){
            try{
                File path = new File(session.getServletContext().getRealPath("img/category"));
                File file = new File(path, category.getId() + ".jpg");
                image.transferTo(file);
                BufferedImage bi = ImageUtil.change2jpg(file);
                ImageIO.write(bi, ".jpg", file);
            }catch(IOException e){
                e.getStackTrace();
            }
        }
        return "redirect:/admin_category_list";
    }
}
