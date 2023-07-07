package com.panghu.housemanage.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.common.util.CommonUtils;
import com.panghu.housemanage.common.util.DateTimeUtils;
import com.panghu.housemanage.common.util.RequestHandleUtils;
import com.panghu.housemanage.dao.LeaseMapper;
import com.panghu.housemanage.dao.MemberMapper;
import com.panghu.housemanage.pojo.po.LeasePo;
import com.panghu.housemanage.pojo.po.MemberPo;
import com.panghu.housemanage.pojo.vo.LeaseVo;
import com.panghu.housemanage.service.LeaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;

@Service
public class LeaseServiceImpl implements LeaseService {
    @Autowired
    LeaseMapper leaseMapper;
    @Autowired
    MemberMapper memberMapper;


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
    @Transactional
    public void insertLease(LeaseVo leaseVo) {
        // 判断是否需要创建租客记录
        if (ObjectUtils.isEmpty(leaseVo.getMemberId())) {
            MemberPo memberPo = MemberPo.builder().name(leaseVo.getMemberName()).tel(leaseVo.getTel()).sex(leaseVo.getSex()).idCard(leaseVo.getIdCard()).roomId(leaseVo.getRoomId()).build();
            memberMapper.insert(memberPo);
            leaseVo.setMemberId(memberPo.getId());
        }
        // 数据模型转换
        LeasePo leasePo = RequestHandleUtils.<LeaseVo, LeasePo> modelDTOTrans(Collections.singletonList(leaseVo)).get(0);
        // 生成业务单号
        String leaseNumber = genLeaseNumber(leaseVo);
        leasePo.setNumber(leaseNumber);
        // 新增租客记录
        leaseMapper.insert(leasePo);
    }

    @Override
    public void updateBatch(List<LeasePo> poList) {
        poList.forEach(leasePo -> leaseMapper.updateById(leasePo));
    }

    @Override
    public void batchDelete(Long[] ids) {
        LambdaUpdateWrapper<LeasePo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(LeasePo::getIsDelete, 1).in(LeasePo::getId, ids);
        leaseMapper.update(null, updateWrapper);
    }

    private String genLeaseNumber(LeaseVo leaseVo) {
        char firstLetter = leaseVo.getLeaseType().name().charAt(0);
        String roomNo = leaseVo.getRoomNo();
        String initials = CommonUtils.getInitials(leaseVo.getMemberName());
        String yyyyMMdd = DateTimeUtils.formatDate(leaseVo.getStartDate(), "yyyyMMdd");
        return String.join("-", String.valueOf(firstLetter)+roomNo, initials+yyyyMMdd);
    }
}
