package com.panghu.housemanage.controller;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.common.util.PHResp;
import com.panghu.housemanage.common.util.RequestHandleUtil;
import com.panghu.housemanage.pojo.po.LeasePo;
import com.panghu.housemanage.pojo.po.MemberPo;
import com.panghu.housemanage.pojo.vo.LeaseVo;
import com.panghu.housemanage.pojo.vo.MemberVo;
import com.panghu.housemanage.service.LeaseService;
import com.panghu.housemanage.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 租赁控制器
 *
 * @author PangHu
 * @date 2023/06/25
 */
@Controller
@RequestMapping("/lease")
public class LeaseController {
    @Autowired
    LeaseService leaseService;
    @Autowired
    MemberService memberService;

    @GetMapping("/page")
    public String getPage(HttpServletRequest request){
        return "lease_list";
    }

    @GetMapping("getByMemberId")
    @ResponseBody
    public PHResp<List<LeaseVo>> getOne(@RequestParam("memberId") Long memberId) {
        List<LeaseVo> leaseVos = leaseService.queryLeaseByMemberId(memberId);
        return PHResp.success(leaseVos);
    }

    @GetMapping
    @ResponseBody
    public PHResp<Map<String, Object>> getData(HttpServletRequest request) throws Exception {
        // 通过前端参数构建分页对象page
        Page<LeaseVo> page = RequestHandleUtil.getPage(request);
        // 通过前端参数构建查询实体po
        LeaseVo leaseVo = RequestHandleUtil.buildPoEntity(request, LeaseVo.class);
        // 把分页对象page和查询实体po传到service层，查询结果返回封装成Page对象
        IPage<LeaseVo> pageResult = leaseService.pageQueryLease(page, leaseVo);
        // 获取查询总数和记录，构建返回前端的Map对象
        return RequestHandleUtil.successPageResult(pageResult);
    }

    @PostMapping
    @ResponseBody
    @Transactional
    public PHResp<String> insert(@RequestBody LeaseVo leaseVo) {
        // TODO 流水号！！
        Long memberId = leaseVo.getMemberId();
        if (ObjectUtils.isEmpty(memberId)) {
            MemberPo memberPo = MemberPo.builder().name(leaseVo.getMemberName()).tel(leaseVo.getTel()).sex(leaseVo.getSex()).idCard(leaseVo.getIdCard()).roomId(leaseVo.getRoomId()).build();
            memberService.insertMember(memberPo);
            leaseVo.setMemberId(memberPo.getId());
        }
        LeasePo leasePo = RequestHandleUtil.<LeaseVo, LeasePo> modelDTOTrans(Collections.singletonList(leaseVo)).get(0);
        // // 新增租客记录
        leaseService.insertLease(leasePo);
        return PHResp.success();
    }
}
