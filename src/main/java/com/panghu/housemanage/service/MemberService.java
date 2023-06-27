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

    int batchDelete(Long[] ids);

    int insertMember(MemberPo memberPo);

    int updateMemberInfo(MemberPo memberPo);
}
