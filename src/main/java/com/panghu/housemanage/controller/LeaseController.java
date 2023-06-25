package com.panghu.housemanage.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.common.util.PHResp;
import com.panghu.housemanage.common.util.RequestHandleUtil;
import com.panghu.housemanage.pojo.po.LeasePo;
import com.panghu.housemanage.pojo.po.MemberPo;
import com.panghu.housemanage.pojo.vo.MemberVo;
import com.panghu.housemanage.service.LeaseService;
import com.panghu.housemanage.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @GetMapping
    @ResponseBody
    public PHResp<List<LeasePo>> queryLeaseByMemberId(@RequestParam("memberId") Long memberId) {
        List<LeasePo> leasePos = leaseService.queryLeaseByMemberId(memberId);
        return PHResp.success(leasePos);
    }
}
