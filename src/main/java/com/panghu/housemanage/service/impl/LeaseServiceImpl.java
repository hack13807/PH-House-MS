package com.panghu.housemanage.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.dao.LeaseMapper;
import com.panghu.housemanage.dao.MemberMapper;
import com.panghu.housemanage.pojo.po.LeasePo;
import com.panghu.housemanage.pojo.po.PHBasePo;
import com.panghu.housemanage.pojo.vo.MemberVo;
import com.panghu.housemanage.service.LeaseService;
import com.panghu.housemanage.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class LeaseServiceImpl implements LeaseService {
    @Autowired
    LeaseMapper leaseMapper;


    @Override
    public List<LeasePo> queryLeaseByMemberId(Long memberId) {
        return leaseMapper.queryLeaseByMemberId(memberId);
    }
}
