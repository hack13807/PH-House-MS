package com.panghu.housemanage.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.common.enumeration.LeaseTypeEnum;
import com.panghu.housemanage.dao.LeaseMapper;
import com.panghu.housemanage.pojo.po.LeasePo;
import com.panghu.housemanage.pojo.vo.LeaseVo;
import com.panghu.housemanage.service.LeaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class LeaseServiceImpl implements LeaseService {
    @Autowired
    LeaseMapper leaseMapper;


    @Override
    public List<LeaseVo> queryLeaseByMemberId(Long memberId) {
        List<LeaseVo> leaseVos = leaseMapper.queryLeaseByMemberId(memberId);
        leaseVos.forEach(vo -> vo.setVoUnit(vo.getUnit()+ vo.getLeaseType().getUnit()));
        return leaseVos;
    }

    @Override
    public IPage<LeaseVo> pageQueryLease(Page<LeaseVo> page, LeaseVo leaseVo) {
        Page<LeaseVo> leaseVoPage = leaseMapper.pageQueryLease(page, leaseVo);
        leaseVoPage.getRecords().forEach(vo -> vo.setVoUnit(vo.getUnit()+vo.getLeaseType().getUnit()));
        return leaseVoPage;
    }

    @Override
    public void insertLease(LeasePo leasePo) {
        leaseMapper.insert(leasePo);
    }
}
