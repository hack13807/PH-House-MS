package com.panghu.housemanage.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.pojo.po.User;
import com.panghu.housemanage.pojo.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface UserMapper {
    User queryUser(@Param("user") User user);

    IPage<UserVo> listUser(Page<UserVo> page, @Param("params") Map<String, Object> params);
}
