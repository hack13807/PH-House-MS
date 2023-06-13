package com.panghu.housemanage.service;

import com.panghu.housemanage.pojo.po.Member;

import java.util.List;
import java.util.Map;

public interface MemberService {
     List<Member> queryMember(Map<String, Object> params);
}
