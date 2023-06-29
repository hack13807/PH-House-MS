package com.panghu.housemanage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.dao.MemberMapper;
import com.panghu.housemanage.pojo.po.MemberPo;
import com.panghu.housemanage.pojo.po.PHBasePo;
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
    public IPage<MemberVo> pageQueryMember(Page<MemberVo> page, PHBasePo po) {
        return memberMapper.pageQueryMember(page, po);
    }

    @Override
    public void batchDelete(Long[] ids) {
        LambdaUpdateWrapper<MemberPo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(MemberPo::getStatus, 0).in(MemberPo::getId, ids);
        memberMapper.update(null, updateWrapper);
    }

    @Override
    public void insertMember(MemberPo memberPo) {
        memberMapper.insert(memberPo);
    }

    @Override
    public void updateMemberInfo(MemberPo memberPo) {
        memberMapper.updateById(memberPo);
    }

    @Override
    public List<MemberPo> queryMemberByRoomId(Long[] ids) {
        QueryWrapper<MemberPo> wrapper = new QueryWrapper<>();
        wrapper.in("room_id", ids);
        return memberMapper.selectList(wrapper);
    }

    @Override
    public void batchUpdate(List<MemberPo> list) {
        // memberMapper.up(memberPo);
    }

}
