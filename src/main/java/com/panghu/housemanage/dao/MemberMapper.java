package com.panghu.housemanage.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.pojo.po.Member;
import com.panghu.housemanage.pojo.vo.MemberVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MemberMapper extends BaseMapper<MemberVo> {
    List<MemberVo> queryMember(Map<String, Object> params);


    Page<MemberVo> pageQueryMember(Page<MemberVo> page, @Param("params")Map<String, Object> params);
}
