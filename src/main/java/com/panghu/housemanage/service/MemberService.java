package com.panghu.housemanage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.pojo.po.MemberPo;
import com.panghu.housemanage.pojo.vo.MemberVo;
import com.panghu.housemanage.pojo.po.PHBasePo;

import java.util.List;
import java.util.Map;

public interface MemberService {
     IPage<MemberVo> pageQueryMember(Page<MemberVo> page, PHBasePo po);

    void batchDelete(Long[] ids);

    void insertMember(MemberPo memberPo);

    void updateMemberInfo(MemberPo memberPo);

    List<MemberPo> queryMemberByRoomId(Long[] ids);

    void batchUpdate(List<MemberPo> list);
}
