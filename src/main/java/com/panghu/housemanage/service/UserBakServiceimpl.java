package com.panghu.housemanage.service;

import com.panghu.housemanage.dao.UserBakMapper;
import com.panghu.housemanage.pojo.UserBak;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserBakServiceimpl implements UserBakService {
    @Autowired(required = false)
    UserBakMapper userMapper;

    @Override
    public UserBak checkUser(UserBak user) {
        return userMapper.checkUser(user);
    }

    @Override
    public UserBak checkManager(UserBak user) {
        return userMapper.checkManager(user);
    }
}
