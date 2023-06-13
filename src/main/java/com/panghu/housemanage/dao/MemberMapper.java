package com.panghu.housemanage.dao;

import com.panghu.housemanage.pojo.po.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MemberMapper {
    List<Member> queryMember(Map<String, Object> params);

    int insertBatch(List<Member> list);
}
