package com.panghu.housemanage.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.dao.MemberMapper;
import com.panghu.housemanage.pojo.vo.MemberVo;
import com.panghu.housemanage.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    MemberMapper memberMapper;

    @Override
    public List<MemberVo> queryMember(Map<String, Object> params) {
        return memberMapper.queryMember(params);
    }

    @Override
    public IPage<MemberVo> pageQueryMember(Page<MemberVo> page, Map<String, Object> params) {
        return memberMapper.pageQueryMember(page, params);
    }

}
