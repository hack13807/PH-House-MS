package com.panghu.housemanage.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.dao.UserMapper;
import com.panghu.housemanage.pojo.po.User;
import com.panghu.housemanage.pojo.vo.UserVo;
import com.panghu.housemanage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired(required = false)
    UserMapper userMapper;

    @Override
    public User queryUser(User user) {
        return userMapper.queryUser(user);
    }

    @Override
    public IPage<UserVo> listUser(Page<UserVo> page, Map<String, Object> params) {
        return userMapper.listUser(page, params);
    }

}
