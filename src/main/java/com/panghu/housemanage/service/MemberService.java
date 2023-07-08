package com.panghu.housemanage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.pojo.po.MemberPo;
import com.panghu.housemanage.pojo.vo.MemberVo;
import com.panghu.housemanage.pojo.vo.PHBaseVo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface MemberService {
     IPage<MemberVo> pageQueryMember(Page<MemberVo> page, MemberVo vo);

    void batchDelete(Long[] ids);

    void insertMember(MemberPo memberPo);

    void updateMemberInfo(MemberPo memberPo);


    @Transactional
    void updateBatch(List<MemberPo> memberList);

    List<MemberPo> isTerminate(Long[] ids);

    List<MemberPo> getAllMember();

    List<MemberPo> queryMember(Map<String, Object> params);
}
