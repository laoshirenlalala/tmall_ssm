package com.how2java.service;

import com.how2java.mapper.UserMapper;
import com.how2java.pojo.User;
import com.how2java.pojo.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    public User get(int id){
        return userMapper.selectByPrimaryKey(id);
    }

    public List<User> list(){
        UserExample example = new UserExample();
        example.setOrderByClause("id desc");
        return userMapper.selectByExample(example);
    }

    public boolean isExist(String name){
        UserExample example = new UserExample();
        example.createCriteria().andNameEqualTo(name);
        List<User> user = userMapper.selectByExample(example);
        if (user.isEmpty())
            return false;
        else
            return  true;
    }

    public void add(User user){
        userMapper.insert(user);
    }

    public User get(String name, String password){
        UserExample example = new UserExample();
        example.createCriteria().andNameEqualTo(name)
                                .andPasswordEqualTo(password);
        List<User> user = userMapper.selectByExample(example);
        if (user.isEmpty())
            return null;
        return user.get(0);
    }
}
