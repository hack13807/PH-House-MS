package com.panghu.housemanage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.pojo.po.MemberPo;
import com.panghu.housemanage.pojo.vo.MemberVo;
import com.panghu.housemanage.pojo.vo.PHBaseVo;

import java.util.List;
import java.util.Map;

public interface MemberService {
     IPage<MemberVo> pageQueryMember(Page<MemberVo> page, PHBaseVo vo);

    void batchDelete(Long[] ids);

    void insertMember(MemberPo memberPo);

    void updateMemberInfo(MemberPo memberPo);

    List<MemberPo> queryMemberByRoomId(Long[] ids);

    void updateBatch(List<MemberPo> list, Integer optType);

    List<MemberPo> isTerminate(Long[] ids);

    List<MemberPo> getAllMember();

    List<MemberPo> queryMember(Map<String, Object> params);
}
