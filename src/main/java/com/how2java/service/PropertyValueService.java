package com.how2java.service;



import com.how2java.mapper.PropertyValueMapper;
import com.how2java.pojo.Product;
import com.how2java.pojo.Property;
import com.how2java.pojo.PropertyValue;
import com.how2java.pojo.PropertyValueExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyValueService {
    @Autowired
    PropertyValueMapper propertyValueMapper;

    @Autowired
    PropertyService propertyService;

    public List<PropertyValue> list(int pid){
        PropertyValueExample example = new PropertyValueExample();
        example.createCriteria().andPidEqualTo(pid);
        List<PropertyValue> result = propertyValueMapper.selectByExample(example);
        for (PropertyValue pv : result)
            pv.setProperty(propertyService.get(pv.getPtid()));
        return result;
    }

    public PropertyValue get(int pid, int ptid) {
        PropertyValueExample example = new PropertyValueExample();
        example.createCriteria().andPidEqualTo(pid)
                                .andPtidEqualTo(ptid);
        List<PropertyValue> pvs= propertyValueMapper.selectByExample(example);
        if(pvs.isEmpty())
            return null;
        return pvs.get(0);
    }

    public void init(Product product){
        int cid = product.getCid();
        List<Property> pts = propertyService.list(cid);
        for (Property pt : pts){
            PropertyValue pv = get(product.getId(), pt.getId());
            if (null == pv){
                pv = new PropertyValue();
                pv.setPid(product.getId());
                pv.setPtid(pt.getId());
                propertyValueMapper.insert(pv);
            }
        }
    }

    public void update(PropertyValue propertyValue){
        propertyValueMapper.updateByPrimaryKeySelective(propertyValue);
    }
}
