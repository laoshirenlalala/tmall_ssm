package com.how2java.service;

import com.how2java.mapper.ReviewMapper;
import com.how2java.mapper.UserMapper;
import com.how2java.pojo.Review;
import com.how2java.pojo.ReviewExample;
import com.how2java.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    @Autowired
    ReviewMapper reviewMapper;

    @Autowired
    UserService userService;

    public List<Review> list(int pid){
        ReviewExample example = new ReviewExample();
        example.createCriteria().andPidEqualTo(pid);
        example.setOrderByClause("id desc");
        List<Review> reviews = reviewMapper.selectByExample(example);
        for (Review review : reviews)
            reviewSetUser(review);
        return reviews;
    }

    public void reviewSetUser(Review review){
        User user = userService.get(review.getUid());
        review.setUser(user);
    }
}
