package com.how2java.service;

import com.how2java.mapper.PropertyMapper;
import com.how2java.pojo.Property;
import com.how2java.pojo.PropertyExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyService {
    @Autowired
    PropertyMapper propertyMapper;
    public List<Property> list(int cid){
        PropertyExample example = new PropertyExample();
        example.createCriteria().andCidEqualTo(cid);
        example.setOrderByClause("id desc");
        return propertyMapper.selectByExample(example);
    }

    public void add(Property property){
        propertyMapper.insert(property);
    }

    public void delete(int id){
        propertyMapper.deleteByPrimaryKey(id);
    }

    public Property get(int id){
        return propertyMapper.selectByPrimaryKey(id);
    }

    public void update(Property property){
        propertyMapper.updateByPrimaryKeySelective(property);
    }
}
