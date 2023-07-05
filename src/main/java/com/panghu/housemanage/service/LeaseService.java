package com.panghu.housemanage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.pojo.po.LeasePo;
import com.panghu.housemanage.pojo.vo.LeaseVo;

import java.util.List;

public interface LeaseService {

    List<LeaseVo> queryLeaseByMemberId(Long memberId);

    IPage<LeaseVo> pageQueryLease(Page<LeaseVo> page, LeaseVo leaseVo);

    void insertLease(LeasePo leasePo);
}
