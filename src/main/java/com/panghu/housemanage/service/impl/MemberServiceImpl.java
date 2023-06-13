package com.panghu.housemanage.service.impl;

import com.panghu.housemanage.dao.MemberMapper;
import com.panghu.housemanage.pojo.po.Member;
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
    public List<Member> queryMember(Map<String, Object> params) {
        return memberMapper.queryMember(params);
    }

}
