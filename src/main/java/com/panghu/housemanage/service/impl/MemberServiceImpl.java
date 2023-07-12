package com.panghu.housemanage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.common.enumeration.PHExceptionCodeEnum;
import com.panghu.housemanage.common.exception.PHServiceException;
import com.panghu.housemanage.dao.MemberMapper;
import com.panghu.housemanage.pojo.po.MemberPo;
import com.panghu.housemanage.pojo.po.RoomPo;
import com.panghu.housemanage.pojo.vo.MemberVo;
import com.panghu.housemanage.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    MemberMapper memberMapper;

    @Override
    public IPage<MemberVo> pageQueryMember(Page<MemberVo> page, MemberVo vo) {
        preprocess(vo);
        return memberMapper.pageQueryMember(page, vo);
    }

    private void preprocess(MemberVo vo) {
        String roomNo = vo.getRoomNo();
        if (roomNo != null && (roomNo.contains(",") || roomNo.contains("，"))) {
            roomNo = roomNo.replace("，", ",");
            vo.setRoomNos(Arrays.asList(roomNo.split(",")));
            vo.setRoomNo(null);
            vo.setVoStatus("-1");
        }
    }

    @Override
    public void batchDelete(Long[] ids) {
        LambdaUpdateWrapper<MemberPo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(MemberPo::getIsDelete, 1).in(MemberPo::getId, ids);
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
    @Transactional
    public void updateBatch(List<MemberPo> memberList) {
        // 更新租客信息
        memberList.forEach(memberPo -> {
            // 验重
            MemberPo member = checkUnique(memberPo);
            if (member != null) {
                throw new PHServiceException(PHExceptionCodeEnum.UNIQUE_MEMBER, null);
            }
            memberMapper.updateById(memberPo);
        });
    }

    @Override
    public List<MemberPo> isTerminate(Long[] ids) {
        List<MemberPo> memberPos = memberMapper.selectBatchIds(Arrays.asList(ids));
        return memberPos.stream().filter(memberPo -> memberPo.getStatus() == 1).toList();
    }

    @Override
    public List<MemberPo> getAllMember() {
        QueryWrapper<MemberPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "idcard", "name", "tel", "sex").ne("isdelete", 1);
        return memberMapper.selectList(queryWrapper);
    }

    @Override
    public List<MemberPo> queryMember(Map<String, Object> params) {
        QueryWrapper<MemberPo> queryWrapper = new QueryWrapper<>();

        // 根据不同属性值拼接查询条件
        if (params.containsKey("id")) {
            Long id = Long.parseLong(params.get("id").toString());
            queryWrapper.eq("id", id);
        }
        if (params.containsKey("name")) {
            String name = (String) params.get("name");
            queryWrapper.eq("name", name);
        }
        if (params.containsKey("sex")) {
            Integer sex = (Integer) params.get("sex");
            queryWrapper.eq("sex", sex);
        }
        if (params.containsKey("tel")) {
            String tel = (String) params.get("tel");
            queryWrapper.eq("tel", tel);
        }
        return memberMapper.selectList(queryWrapper);
    }

    @Override
    public List<MemberVo> getByRoomId(Map<String, Object> params) {
        return memberMapper.getByRoomId(params);
    }

    @Override
    public MemberPo checkUnique(MemberPo memberPo) {
        MemberPo currentMember = memberMapper.selectById(memberPo.getId()); // 根据房间ID获取原始数据
        // 判断是否与原始数据中的房间号一致
        if (!currentMember.getIdCard().equals(memberPo.getIdCard())) {
            QueryWrapper<MemberPo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("idcard", memberPo.getIdCard()).eq("isdelete", 0);
            List<MemberPo> memberPos = memberMapper.selectList(queryWrapper);
            return memberPos.isEmpty() ? null : memberPos.get(0);
        }
        return null;
    }
}
