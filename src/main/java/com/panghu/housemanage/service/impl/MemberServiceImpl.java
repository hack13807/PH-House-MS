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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 租客服务实现类
 * 
 * @author PangHu
 */
@Slf4j
@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    MemberMapper memberMapper;

    @Override
    public IPage<MemberVo> pageQueryMember(Page<MemberVo> page, MemberVo vo) {
        try {
            preprocess(vo);
            // 添加日志记录租客查询参数
            log.info("查询租客，参数：{}", vo);
            IPage<MemberVo> resultPage = memberMapper.pageQueryMember(page, vo);
            log.info("租客查询结果数：{}", resultPage.getTotal());
            return resultPage;
        } catch (Exception e) {
            log.error("查询租客异常", e);
            throw e;
        }
    }

    /**
     * 预处理查询参数
     */
    private void preprocess(MemberVo vo) {
        if (vo == null) {
            return;
        }
        
        String roomNo = vo.getRoomNo();
        if (StringUtils.hasText(roomNo) && (roomNo.contains(",") || roomNo.contains("，"))) {
            roomNo = roomNo.replace("，", ",");
            vo.setRoomNos(Arrays.asList(roomNo.split(",")));
            vo.setRoomNo(null);
            vo.setVoStatus("-1");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(Long[] ids) {
        if (ids == null || ids.length == 0) {
            return;
        }
        
        try {
            LambdaUpdateWrapper<MemberPo> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(MemberPo::getIsDelete, 1).in(MemberPo::getId, (Object[])ids);
            int rows = memberMapper.update(null, updateWrapper);
            log.info("批量删除租客成功，数量：{}", rows);
        } catch (Exception e) {
            log.error("批量删除租客异常", e);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertMember(MemberPo memberPo) {
        try {
            if (memberPo.getIsDelete() == null) {
                memberPo.setIsDelete(0);
            }
            int rows = memberMapper.insert(memberPo);
            log.info("新增租客成功，ID：{}", memberPo.getId());
        } catch (Exception e) {
            log.error("新增租客异常", e);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMemberInfo(MemberPo memberPo) {
        try {
            int rows = memberMapper.updateById(memberPo);
            log.info("更新租客成功，ID：{}", memberPo.getId());
        } catch (Exception e) {
            log.error("更新租客异常", e);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBatch(List<MemberPo> memberList) {
        if (memberList == null || memberList.isEmpty()) {
            return;
        }
        
        try {
            // 更新租客信息
            for (MemberPo memberPo : memberList) {
                // 验重
                MemberPo member = checkUnique(memberPo);
                if (member != null) {
                    throw new PHServiceException(PHExceptionCodeEnum.UNIQUE_MEMBER, null);
                }
                memberMapper.updateById(memberPo);
            }
            log.info("批量更新租客成功，数量：{}", memberList.size());
        } catch (PHServiceException e) {
            log.error("批量更新租客业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("批量更新租客异常", e);
            throw e;
        }
    }

    @Override
    public List<MemberPo> isTerminate(Long[] ids) {
        if (ids == null || ids.length == 0) {
            return Collections.emptyList();
        }
        
        try {
            List<MemberPo> memberPos = memberMapper.selectBatchIds(Arrays.asList(ids));
            return memberPos.stream().filter(memberPo -> memberPo.getStatus() == 1).toList();
        } catch (Exception e) {
            log.error("查询租客状态异常", e);
            throw e;
        }
    }

    @Override
    public List<MemberPo> getAllMember() {
        try {
            QueryWrapper<MemberPo> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("id", "idcard", "name", "tel", "sex").eq("isdelete", 0);
            return memberMapper.selectList(queryWrapper);
        } catch (Exception e) {
            log.error("获取所有租客异常", e);
            throw e;
        }
    }

    @Override
    public List<MemberPo> queryMember(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return Collections.emptyList();
        }
        
        try {
            // 根据不同属性值拼接查询条件
            if (params.containsKey("leaseId")) {
                return memberMapper.selectByLeaseId(Long.parseLong(params.get("leaseId").toString()));
            } else {
                QueryWrapper<MemberPo> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("isdelete", 0);
                
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
        } catch (Exception e) {
            log.error("查询租客异常", e);
            throw e;
        }
    }

    @Override
    public List<MemberVo> getByRoomId(Map<String, Object> params) {
        if (params == null || !params.containsKey("roomId")) {
            return Collections.emptyList();
        }
        
        try {
            return memberMapper.getByRoomId(params);
        } catch (Exception e) {
            log.error("根据房间ID查询租客异常", e);
            throw e;
        }
    }

    @Override
    public MemberPo checkUnique(MemberPo memberPo) {
        if (memberPo == null || !StringUtils.hasText(memberPo.getIdCard())) {
            return null;
        }
        
        boolean checkNeeded = false;
        
        if (memberPo.getId() == null) {
            // 新增操作，需要检查
            checkNeeded = true;
        } else {
            // 更新操作，检查原始数据是否变更
            MemberPo currentMember = memberMapper.selectById(memberPo.getId());
            if (currentMember != null && !currentMember.getIdCard().equals(memberPo.getIdCard())) {
                checkNeeded = true;
            }
        }
        
        if (checkNeeded) {
            QueryWrapper<MemberPo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("idcard", memberPo.getIdCard()).eq("isdelete", 0);
            if (memberPo.getId() != null) {
                queryWrapper.ne("id", memberPo.getId()); // 排除自身
            }
            
            List<MemberPo> memberPos = memberMapper.selectList(queryWrapper);
            return memberPos.isEmpty() ? null : memberPos.get(0);
        }
        
        return null;
    }
}
