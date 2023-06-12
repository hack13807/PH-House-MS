package com.panghu.housemanage.dao;

import com.panghu.housemanage.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User queryUser(@Param("user") User user);
}
