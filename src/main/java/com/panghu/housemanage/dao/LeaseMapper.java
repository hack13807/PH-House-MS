package com.panghu.housemanage.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.pojo.po.LeasePo;
import com.panghu.housemanage.pojo.vo.LeaseVo;
import com.panghu.housemanage.pojo.vo.MemberVo;
import com.panghu.housemanage.pojo.vo.PHBaseVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface LeaseMapper extends BaseMapper<LeasePo> {
    List<LeaseVo> queryLeaseByMemberId(@Param("memberId") Long memberId);

    Page<LeaseVo> pageQueryLease(Page<LeaseVo> page, @Param("leaseVo") PHBaseVo vo);

    List<LeaseVo> queryLeaseByRoomId(@Param("roomId") Long roomId);

    List<LeaseVo> queryLeaseAndMemberByRoomId(@Param("roomId") Long roomId);

    List<LeaseVo> checkUnique(Map<String, Object> params);

}
