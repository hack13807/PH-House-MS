package com.panghu.housemanage.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.common.enumeration.PHExceptionCodeEnum;
import com.panghu.housemanage.common.exception.PHServiceException;
import com.panghu.housemanage.common.util.PHResp;
import com.panghu.housemanage.common.util.RequestHandleUtils;
import com.panghu.housemanage.dao.MemberMapper;
import com.panghu.housemanage.pojo.po.MemberPo;
import com.panghu.housemanage.pojo.vo.MemberVo;
import com.panghu.housemanage.service.MemberService;
import com.panghu.housemanage.service.RoomService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 租客控制器
 *
 * @author PangHu
 * @date 2023/06/18
 */
@Slf4j
@Controller
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private MemberService memberService;
    
    @Autowired
    private MemberMapper memberMapper;
    
    @Autowired
    private RoomService roomService;

    /**
     * 获取租客管理页面
     */
    @GetMapping("/page")
    public String getPage(
            @RequestParam(required = false, value = "roomSearch") String roomSearch, 
            @RequestParam(required = false, value = "memberSearch") String memberSearch, 
            Model model) {
        model.addAttribute("roomSearch", roomSearch);
        model.addAttribute("memberSearch", memberSearch);
        return "member_list";
    }

    /**
     * 获取租客列表数据
     */
    @GetMapping
    @ResponseBody
    public PHResp<Map<String, Object>> getData(HttpServletRequest request) {
        try {
            // 通过前端参数构建分页对象page
            Page<MemberVo> page = RequestHandleUtils.getPage(request);
            // 通过前端参数构建查询实体
            MemberVo memberVo = RequestHandleUtils.buildPoEntity(request, MemberVo.class);
            
            log.info("查询租客列表：原始参数={}", request.getParameterMap());
            log.info("查询租客列表构建的VO对象：memberName={}, roomNo={}, voStatus={}", 
                    memberVo.getMemberName(), memberVo.getRoomNo(), memberVo.getVoStatus());
            
            // 把分页对象page和查询实体传到service层
            IPage<MemberVo> pageResult = memberService.pageQueryMember(page, memberVo);
            
            log.info("查询租客列表结果：总数={}, 当前页={}", pageResult.getTotal(), pageResult.getCurrent());
            
            // 获取查询总数和记录，构建返回前端的Map对象
            return RequestHandleUtils.successPageResult(pageResult);
        } catch (Exception e) {
            log.error("获取租客数据异常", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据租约ID查询租客
     */
    @GetMapping("/queryMemberByLeaseId")
    @ResponseBody
    public PHResp<List<MemberPo>> queryMemberByLeaseId(@RequestParam String id) {
        try {
            log.info("根据租约ID查询租客: leaseId={}", id);
            List<MemberPo> memberPos = memberService.queryMember(Map.of("leaseId", id));
            return PHResp.success(memberPos);
        } catch (Exception e) {
            log.error("根据租约ID查询租客异常", e);
            throw e;
        }
    }

    /**
     * 根据ID获取租客信息
     */
    @GetMapping("/getById")
    @ResponseBody
    public PHResp<MemberPo> getById(@RequestParam String id) {
        try {
            log.info("根据ID查询租客: id={}", id);
            MemberPo memberPo = memberMapper.selectById(id);
            return PHResp.success(memberPo);
        } catch (Exception e) {
            log.error("根据ID查询租客异常", e);
            throw e;
        }
    }
    
    /**
     * 根据房间ID查询租客
     */
    @GetMapping("/getByRoomId")
    @ResponseBody
    public PHResp<List<MemberVo>> getByRoomId(@RequestParam("roomId") Long roomId) {
        try {
            log.info("根据房间ID查询租客: roomId={}", roomId);
            List<MemberVo> memberVos = memberService.getByRoomId(Map.of("roomId", roomId));
            return PHResp.success(memberVos);
        } catch (Exception e) {
            log.error("根据房间ID查询租客异常", e);
             throw e;
        }
    }
    
    /**
     * 检查租客是否可以终止合同
     */
    @GetMapping("/isTerminate")
    @ResponseBody
    public PHResp<String> isTerminate(@RequestParam Long[] ids) {
        try {
            log.info("检查租客是否可终止合同: ids={}", (Object) ids);
            List<MemberPo> rentingList = memberService.isTerminate(ids);
            if (CollectionUtils.isEmpty(rentingList)) {
                return PHResp.success();
            }
            String memberNames = String.join(", ", rentingList.stream().map(MemberPo::getName).toList());
            throw new PHServiceException(PHExceptionCodeEnum.MEMBER_RENTING, memberNames);
        } catch (PHServiceException e) {
            log.info("租客终止检查结果: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("检查租客终止状态异常", e);
            throw e;
        }
    }

    /**
     * 获取租客缓存数据
     */
    @GetMapping("/cache")
    @ResponseBody
    public PHResp<List<MemberPo>> getCache() {
        try {
            log.info("获取租客缓存数据");
            List<MemberPo> memberList = memberService.getAllMember();
            return PHResp.success(memberList);
        } catch (Exception e) {
            log.error("获取租客缓存数据异常", e);
            throw e;
        }
    }

    /**
     * 删除租客
     */
    @DeleteMapping
    @ResponseBody
    public PHResp<String> deleteData(@RequestBody Long[] ids) {
        try {
            log.info("删除租客: ids={}", (Object) ids);
            memberService.batchDelete(ids);
            return PHResp.success();
        } catch (Exception e) {
            log.error("删除租客异常", e);
            throw e;
        }
    }

    /**
     * 更新租客信息
     */
    @PutMapping
    @ResponseBody
    public PHResp<String> update(@RequestBody List<MemberVo> volist) {
        try {
            log.info("更新租客信息: count={}", volist.size());
            List<MemberPo> memberList = RequestHandleUtils.memberDTOTrans(volist);
            memberService.updateBatch(memberList);
            return PHResp.success();
        } catch (Exception e) {
            log.error("更新租客信息异常", e);
            throw e;
        }
    }

    /**
     * 新增租客
     */
    @PostMapping
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public PHResp<String> insert(@RequestBody MemberVo memberVo) {
        try {
            log.info("新增租客: name={}, tel={}", memberVo.getMemberName(), memberVo.getTel());
            MemberPo memberPo = RequestHandleUtils.memberDTOTrans(Collections.singletonList(memberVo)).get(0);
            
            // 验证身份证号唯一性
            MemberPo existingMember = memberService.checkUnique(memberPo);
            if (existingMember != null) {
                throw new PHServiceException(PHExceptionCodeEnum.UNIQUE_MEMBER, null);
            }
            
            // 新增租客记录
            memberService.insertMember(memberPo);
            return PHResp.success();
        } catch (PHServiceException e) {
            log.info("新增租客业务异常: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("新增租客异常", e);
            throw e;
        }
    }
}
