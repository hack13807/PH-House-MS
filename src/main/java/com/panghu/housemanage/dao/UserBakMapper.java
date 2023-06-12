package com.panghu.housemanage.dao;

import com.panghu.housemanage.pojo.UserBak;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserBakMapper {
    //通过username和passwd 验证用户 reader
    UserBak checkUser(@Param("user") UserBak user);
    UserBak checkManager(@Param("user") UserBak user);
}
