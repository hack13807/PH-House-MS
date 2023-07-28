package com.panghu.housemanage.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.pojo.po.MemberPo;
import com.panghu.housemanage.pojo.vo.MemberVo;
import com.panghu.housemanage.pojo.vo.PHBaseVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MemberMapper extends BaseMapper<MemberPo> {
    Page<MemberVo> pageQueryMember(Page<MemberVo> page, @Param("memberVo") PHBaseVo vo);

    List<Map<Object, Object>> countMemberByRoomId(@Param("ids") List<Long> ids);

    List<MemberVo> getByRoomId(Map<String, Object> params);

    List<MemberPo> selectByLeaseId(@Param("leaseId") Long leaseId);

    List<MemberVo> getRentMemberByLeaseId(@Param("leaseId") Long leaseId);
}
