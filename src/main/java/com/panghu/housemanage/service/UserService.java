package com.panghu.housemanage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.pojo.po.User;
import com.panghu.housemanage.pojo.vo.UserVo;

import java.util.Map;

public interface UserService {
    User queryUser(User user); //登录

    IPage<UserVo> listUser(Page<UserVo> page, Map<String, Object> params);//查询用户列表

    //IPage<MemberVo> pageQueryMember(Page<MemberVo> page, Map<String, Object> params);
}
