package com.panghu.housemanage.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.pojo.po.MemberPo;
import com.panghu.housemanage.pojo.vo.MemberVo;
import com.panghu.housemanage.pojo.vo.PHBaseVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface MemberMapper extends BaseMapper<MemberPo> {
    Page<MemberVo> pageQueryMember(Page<MemberVo> page, @Param("memberVo") PHBaseVo vo);

    List<Map<Object, Object>> countMemberByRoomId(@Param("ids") List<Long> ids);
}
