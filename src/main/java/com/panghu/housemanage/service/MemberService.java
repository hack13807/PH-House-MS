package com.panghu.housemanage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.pojo.vo.MemberVo;

import java.util.List;
import java.util.Map;

public interface MemberService {
     List<MemberVo> queryMember(Map<String, Object> params);

     IPage<MemberVo> pageQueryMember(Page<MemberVo> page, Map<String, Object> params);
}
