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

/**
 * 租客Mapper接口
 * 
 * @author PangHu
 */
@Mapper
public interface MemberMapper extends BaseMapper<MemberPo> {
    /**
     * 分页查询租客信息
     *
     * @param page 分页参数
     * @param vo 查询条件
     * @return 分页结果
     */
    Page<MemberVo> pageQueryMember(Page<MemberVo> page, @Param("memberVo") MemberVo vo);

    /**
     * 统计房间中的租客数量
     *
     * @param ids 房间ID列表
     * @return 统计结果
     */
    List<Map<String, Object>> countMemberByRoomId(@Param("ids") List<Long> ids);

    /**
     * 根据房间ID获取租客信息
     *
     * @param params 查询参数，包含roomId
     * @return 租客列表
     */
    List<MemberVo> getByRoomId(Map<String, Object> params);

    /**
     * 根据租约ID查询租客信息
     *
     * @param leaseId 租约ID
     * @return 租客列表
     */
    List<MemberPo> selectByLeaseId(@Param("leaseId") Long leaseId);

    /**
     * 根据租约ID获取租客信息
     *
     * @param leaseId 租约ID
     * @return 租客视图对象列表
     */
    List<MemberVo> getRentMemberByLeaseId(@Param("leaseId") Long leaseId);
}
