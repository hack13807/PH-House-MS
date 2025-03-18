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
            // 详细记录查询参数
            log.info("查询租客，参数：memberName={}, roomNo={}, roomNos={}, voStatus={}", 
                    vo != null ? vo.getMemberName() : null,
                    vo != null ? vo.getRoomNo() : null,
                    vo != null ? vo.getRoomNos() : null,
                    vo != null ? vo.getVoStatus() : null);
            
            // 增加SQL调试信息
            log.info("开始执行pageQueryMember SQL...");
            IPage<MemberVo> resultPage = memberMapper.pageQueryMember(page, vo);
            log.info("租客查询完成，结果数：{}", resultPage.getTotal());
            
            // 打印查询结果中的几条记录以便调试
            if (resultPage.getRecords() != null && !resultPage.getRecords().isEmpty()) {
                log.info("查询结果示例：第一条记录 - name={}, id={}, roomNo={}", 
                        resultPage.getRecords().get(0).getMemberName(),
                        resultPage.getRecords().get(0).getRowId(),
                        resultPage.getRecords().get(0).getRoomNo());
            } else {
                log.warn("查询结果为空");
            }
            
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
        
        // 状态转换处理，前端传入的状态与数据库状态映射
        String voStatus = vo.getVoStatus();
        if (StringUtils.hasText(voStatus)) {
            log.info("状态参数预处理前：voStatus={}", voStatus);
            
            // 如果前端没有明确指定状态过滤（或传入的是全部），则默认不过滤status
            if (voStatus == null || voStatus.isEmpty() || "-1".equals(voStatus) || "0".equals(voStatus)) {
                log.info("查询全部状态租客，移除status过滤条件");
                vo.setVoStatus("-1"); // 设置为-1表示不过滤
            }
            // 前端传1表示查询空闲状态(status=0)，传2表示查询在租状态(status=1)
            else if ("1".equals(voStatus)) {
                vo.setVoStatus("0"); // 空闲状态
                log.info("查询空闲状态，已修正voStatus=0");
            } else if ("2".equals(voStatus)) {
                vo.setVoStatus("1"); // 在租状态
                log.info("查询在租状态，已修正voStatus=1");
            }
        } else {
            // 如果状态参数为空，默认查询所有非禁用状态
            log.info("状态参数为空，默认查询所有状态");
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
