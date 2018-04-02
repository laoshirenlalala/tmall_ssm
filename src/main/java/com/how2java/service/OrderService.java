package com.how2java.service;

import com.how2java.mapper.OrderMapper;
import com.how2java.pojo.Order;
import com.how2java.pojo.OrderExample;
import com.how2java.pojo.OrderItem;
import com.how2java.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderMapper orderMapper;

    @Autowired
    OrderItemService orderItemService;

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    public List<Order> list(){
        OrderExample example = new OrderExample();
        example.setOrderByClause("id desc");
        List<Order> os = orderMapper.selectByExample(example);
        for (Order o : os){
          orderItemService.fill(o);
          setUser(o);
        }
        return os;
    }

    public void setUser(Order order){
        User user = userService.get(order.getUid());
        order.setUser(user);
    }

    public void add(Order order){
        orderMapper.insert(order);
    }

    @Transactional(propagation= Propagation.REQUIRED,rollbackForClassName="Exception")

    public float add(Order o, List<OrderItem> ois) {
        float total = 0;
        add(o);

        if(false)
            throw new RuntimeException();

        for (OrderItem oi: ois) {
            oi.setOid(o.getId());
            orderItemService.update(oi);
            total+=oi.getProduct().getPromotePrice()*oi.getNumber();
        }
        return total;
    }

    public Order get(int id){
        return orderMapper.selectByPrimaryKey(id);
    }

    public void update(Order order){
        orderMapper.updateByPrimaryKey(order);
    }

    public List<Order> listByUid(int uid){
        OrderExample example = new OrderExample();
        example.createCriteria().andUidEqualTo(uid);
        example.setOrderByClause("id desc");
        List<Order> os = orderMapper.selectByExample(example);
        for (Order o : os)
            orderItemService.fill(o);
        return os;
    }
}
