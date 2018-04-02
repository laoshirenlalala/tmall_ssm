package com.how2java.service;

import com.how2java.mapper.OrderItemMapper;
import com.how2java.pojo.Order;
import com.how2java.pojo.OrderItem;
import com.how2java.pojo.OrderItemExample;
import com.how2java.pojo.Product;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService {
    @Autowired
    OrderItemMapper orderItemMapper;

    @Autowired
    ProductService productService;

    public List<OrderItem> list(int oid){
        OrderItemExample example = new OrderItemExample();
        example.createCriteria().andOidEqualTo(oid);
        example.setOrderByClause("id desc");
        List<OrderItem> ois = orderItemMapper.selectByExample(example);
        if (!ois.isEmpty()){
            for (OrderItem oi : ois){
                setProduct(oi);
            }
        }
        return ois;
    }

    public void setProduct(OrderItem orderItem){
        Product product = productService.get(orderItem.getPid());
        productService.setFirstImage(product);
        orderItem.setProduct(product);
    }

    public void fill(Order order){
        List<OrderItem> ois = list(order.getId());
        order.setOrderItems(ois);
        float total = 0;
        int totalNumber = 0;
        for (OrderItem oi : ois){
            total += oi.getNumber() * oi.getProduct().getPromotePrice();
            totalNumber += oi.getNumber();
        }
        order.setTotal(total);
        order.setTotalNumber(totalNumber);
    }

    public int saleCount(Product product){
        OrderItemExample example = new OrderItemExample();
        example.createCriteria().andPidEqualTo(product.getId());
        List<OrderItem> ois = orderItemMapper.selectByExample(example);
        int count = 0;
        if (!ois.isEmpty()){
            for (OrderItem oi : ois)
                count += oi.getNumber();
            return  count;
        }
        return 0;
    }

    public List<OrderItem> listAndOidIsNull(int pid, int uid){
        OrderItemExample example = new OrderItemExample();
        example.createCriteria().andPidEqualTo(pid)
                                .andUidEqualTo(uid)
                                .andOidIsNull();
        return orderItemMapper.selectByExample(example);
    }

    public void add(OrderItem orderItem){
        orderItemMapper.insert(orderItem);
    }

    public void update(OrderItem orderItem){
        orderItemMapper.updateByPrimaryKey(orderItem);
    }

    public OrderItem get(int id){
        return orderItemMapper.selectByPrimaryKey(id);
    }

    public List<OrderItem> listByUid(int uid){
        OrderItemExample example = new OrderItemExample();
        example.createCriteria().andUidEqualTo(uid)
                                .andOidIsNull();
        example.setOrderByClause("id desc");
        List<OrderItem> ois = orderItemMapper.selectByExample(example);
        for (OrderItem oi : ois)
            setProduct(oi);
        return  ois;
    }

    public void deleteOrderItem(int id){
        orderItemMapper.deleteByPrimaryKey(id);
    }
}
