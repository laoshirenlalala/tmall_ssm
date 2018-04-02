package com.how2java.controller;

import com.how2java.comparator.*;
import com.how2java.pojo.*;
import com.how2java.service.*;
import com.sun.deploy.net.HttpUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class ForeController {
    @Autowired
    CategoryService categoryService;

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @Autowired
    ReviewService reviewService;

    @Autowired
    PropertyValueService propertyValueService;

    @Autowired
    OrderItemService orderItemService;

    @Autowired
    OrderService orderService;

   @RequestMapping("forehome")
    public String home(Model model){
        List<Category> cs = categoryService.list();
        for (Category c : cs){
            categoryService.fillProducts(c);
            categoryService.fillProductsByRow(c);
        }
        model.addAttribute("cs", cs);
        return "fore/home";
    }

    @RequestMapping("foreregister")
    public String register(Model model,User user){
        String name = user.getName();
        name = HtmlUtils.htmlEscape(name);
        if (userService.isExist(name)){
            String m = "用户名已存在";
            model.addAttribute("msg", m);
            model.addAttribute("user", null);
            return "fore/register";
        }
        user.setName(name);
        userService.add(user);
        return "redirect:registerSuccessPage";
    }

    @RequestMapping("forelogin")
    public String login(User user, Model model, HttpSession session){
        String name = user.getName();
        String password = user.getPassword();
        HtmlUtils.htmlEscape(name);
        user = userService.get(name, password);
        if (null == user){
            String m = "账号密码错误";
            model.addAttribute("msg", m);
            return "fore/login";
        }
        session.setAttribute("user", user);
        return "redirect:forehome";
    }
    @RequestMapping("forelogout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:forehome";
    }

    @RequestMapping("foreproduct")
    public String foreProduct(int pid, Model model){
        Product p  = productService.get(pid);
        Category c = categoryService.get(p.getCid());
        p.setCategory(c);
        productService.setFirstImage(p);
        productService.fillProductSingleImages(p);
        productService.fillProductDetailImages(p);
        productService.setSaleCount(p);
        productService.setReviewCount(p);
        List<Review> reviews = reviewService.list(pid);
        List<PropertyValue> pvs = propertyValueService.list(pid);
        model.addAttribute("p", p);
        model.addAttribute("pvs", pvs);
        model.addAttribute("reviews", reviews);
        return "fore/product";
    }

    @RequestMapping("forecheckLogin")
    @ResponseBody
    public String checkLogin(HttpSession session){
        User user = (User)session.getAttribute("user");
        if (null == user)
            return "fail";
        return "success";
    }

    @RequestMapping("foreloginAjax")
    @ResponseBody
    public String loginAjax(Model model, User user, HttpSession session){
        String name = user.getName();
        String password = user.getPassword();
        name = HtmlUtils.htmlEscape(name);
        user = userService.get(name, password);
        if (null == user)
            return "fail";
        session.setAttribute("user", user);
        return "success";
    }

    @RequestMapping("forecategory")
    public String foreCategory(int cid, Model model, String sort){
        Category c = categoryService.get(cid);
        categoryService.fillProducts(c);
        List<Product> products = c.getProducts();
        if(null!=sort){
            switch(sort){
                case "all":
                    Collections.sort(products,  new ProductAllComparator());
                    break;
                case "review":
                    Collections.sort(products, new ProductReviewComparator());
                    break;
                case "date":
                    Collections.sort(products, new ProductDateComparator());
                    break;
                case "saleCount":
                    Collections.sort(products, new ProductSalecountComparator());
                    break;
                case "price":
                    Collections.sort(products, new ProductPriceComparator());
                    break;
            }
        }
        for (Product p : products){
            productService.setReviewCount(p);
            productService.setSaleCount(p);
        }
        c.setProducts(products);
        model.addAttribute("c", c);
        return "fore/foreCategory";
    }

    @RequestMapping("foresearch")
    public String foresearch(String keyword, Model model){
        List<Product> ps = productService.list(keyword);
        model.addAttribute("ps", ps);
        model.addAttribute("keyword", keyword);
        return "fore/searchResult";
    }

    @RequestMapping("forebuyone")
    public String foreBuyOne(int pid, int num, HttpSession session){
        Product p = productService.get(pid);
        User user = (User) session.getAttribute("user");
        int uid = user.getId();
        int oiid = 0;
        List<OrderItem> ois = orderItemService.listAndOidIsNull(pid, uid);
        if (ois.isEmpty()){
            OrderItem orderItem = new OrderItem();
            orderItem.setPid(pid);
            orderItem.setUid(uid);
            orderItem.setNumber(num);
            orderItemService.add(orderItem);
            oiid = orderItem.getId();
            return "redirect:forebuy?oiid=" + oiid;
        }
        OrderItem oi = ois.get(0);
        oi.setNumber(oi.getNumber() + num);
        orderItemService.update(oi);
        oiid = oi.getId();
        return "redirect:forebuy?oiid=" + oiid;
    }

    @RequestMapping("forebuy")
    public String foreBuy(Model model, HttpSession session, String[] oiid){
        List<OrderItem> ois = new ArrayList<>();
        float total = 0;
        for (String id : oiid){
            int i = Integer.parseInt(id);
            OrderItem orderItem = orderItemService.get(i);
            orderItemService.setProduct(orderItem);
            total += orderItem.getProduct().getPromotePrice() * orderItem.getNumber();
            ois.add(orderItem);
        }
        session.setAttribute("ois", ois);
        model.addAttribute("total",total);
        return "fore/buy";
    }

    @RequestMapping("foreaddCart")
    @ResponseBody
    public String foreAddCart(int pid, int num, HttpSession session){
        Product p = productService.get(pid);
        User user = (User) session.getAttribute("user");
        int uid = user.getId();
        int oiid = 0;
        List<OrderItem> ois = orderItemService.listAndOidIsNull(pid, uid);
        if (ois.isEmpty()){
            OrderItem orderItem = new OrderItem();
            orderItem.setPid(pid);
            orderItem.setUid(uid);
            orderItem.setNumber(num);
            orderItemService.add(orderItem);
            oiid = orderItem.getId();
            return "success";
        }
        OrderItem oi = ois.get(0);
        oi.setNumber(oi.getNumber() + num);
        orderItemService.update(oi);
        oiid = oi.getId();
        return "success";
    }

    @RequestMapping("forecart")
    public String foreCart(Model model, HttpSession session){
        User user = (User) session.getAttribute("user");
        if (null == user)
            return "fore/login";
        List<OrderItem> ois = orderItemService.listByUid(user.getId());
        model.addAttribute("ois", ois);
        return "fore/cart";
    }

    @RequestMapping("forechangeOrderItem")
    @ResponseBody
    public String foreChanggeOrderItem(int pid, int number, HttpSession session){
        User user = (User) session.getAttribute("user");
        List<OrderItem> ois = orderItemService.listAndOidIsNull(pid, user.getId());
        OrderItem oi = ois.get(0);
        oi.setNumber(number);
        orderItemService.update(oi);
        return "success";
    }

    @RequestMapping("foredeleteOrderItem")
    @ResponseBody
    public String foreDeleteOrderItem(int oiid){
        orderItemService.deleteOrderItem(oiid);
        return "success";
    }

    @RequestMapping("forecreateOrder")
    public String foreCreateOrder(Model model, HttpSession session, Order order){
        User user = (User) session.getAttribute("user");
        List<OrderItem> ois = (List) session.getAttribute("ois");
        order.setUid(user.getId());
        String orderCode = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date())
                + RandomUtils.nextInt(10000);
        order.setOrderCode(orderCode);
        order.setStatus("waitPay");
        order.setCreateDate(new Date());
        orderService.add(order);
        float total = 0;
        for (OrderItem oi : ois) {
            total += oi.getProduct().getPromotePrice() * oi.getNumber();
            oi.setOid(order.getId());
            orderItemService.update(oi);
        }
        return "redirect:forealipay?oid=" + order.getId() + "&total=" + total;
    }

    @RequestMapping("forepayed")
    public String forePayer(int oid, float total, Model model){
        Order order = orderService.get(oid);
        order.setPayDate(new Date());
        order.setStatus("waitDelivery");
        orderService.update(order);
        model.addAttribute("o", order);
        return "fore/payed";
    }

    @RequestMapping("forebought")
    public String foreBought(Model model, HttpSession session){
        User user = (User) session.getAttribute("user");
        List<Order> os = orderService.listByUid(user.getId());
        model.addAttribute("os", os);
        return "fore/bought";
    }

    @RequestMapping("foreconfirmPay")
    public String foreConfirmPay(int oid, Model model){
        Order order = orderService.get(oid);
        orderItemService.fill(order);
        model.addAttribute("o", order);
        return "fore/confirmPay";
    }

    @RequestMapping("foreorderConfirmed")
    public String foreOrderConfirmed(int oid){
        Order order = orderService.get(oid);
        order.setStatus("waitReview");
        order.setConfirmDate(new Date());
        orderService.update(order);
        return "fore/orderConfirmed";
    }

    @RequestMapping("foredeleteOrder")
    @ResponseBody
    public String foreDeleteOrder(int oid){
        Order order = orderService.get(oid);
        order.setStatus("delete");
        orderService.update(order);
        return "success";
    }
}
