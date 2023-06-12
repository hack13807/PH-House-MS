package com.panghu.housemanage.service;

import com.panghu.housemanage.dao.UserMapper;
import com.panghu.housemanage.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    @Autowired(required = false)
    UserMapper userMapper;

    @Override
    public User queryUser(User user){
        return  userMapper.queryUser(user);
    };
}
