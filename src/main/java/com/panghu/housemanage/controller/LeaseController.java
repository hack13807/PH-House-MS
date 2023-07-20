package com.panghu.housemanage.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.common.util.PHResp;
import com.panghu.housemanage.common.util.RequestHandleUtils;
import com.panghu.housemanage.pojo.vo.LeaseVo;
import com.panghu.housemanage.service.LeaseService;
import com.panghu.housemanage.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
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
    @Autowired
    MemberService memberService;

    @GetMapping("/page")
    public String getPage(@RequestParam(required = false, value = "memberSearch") String memberSearch, Model model){
        model.addAttribute("memberSearch", memberSearch);
        return "lease_list";
    }

    @GetMapping("getByMemberId")
    @ResponseBody
    public PHResp<List<LeaseVo>> getByMemberId(@RequestParam("memberId") Long memberId) {
        List<LeaseVo> leaseVos = leaseService.queryLeaseByMemberId(memberId);
        return PHResp.success(leaseVos);
    }

    @GetMapping("getByRoomId")
    @ResponseBody
    public PHResp<List<LeaseVo>> getByRoomId(@RequestParam("roomId") Long roomId) {
        List<LeaseVo> leaseVos = leaseService.queryLeaseByRoomId(roomId);
        return PHResp.success(leaseVos);
    }

    @GetMapping
    @ResponseBody
    public PHResp<Map<String, Object>> getData(HttpServletRequest request) throws Exception {
        // 通过前端参数构建分页对象page
        Page<LeaseVo> page = RequestHandleUtils.getPage(request);
        // 通过前端参数构建查询实体po
        LeaseVo leaseVo = RequestHandleUtils.buildPoEntity(request, LeaseVo.class);
        // 把分页对象page和查询实体po传到service层，查询结果返回封装成Page对象
        IPage<LeaseVo> pageResult = leaseService.pageQueryLease(page, leaseVo);
        // 获取查询总数和记录，构建返回前端的Map对象
        return RequestHandleUtils.successPageResult(pageResult);
    }

    @PostMapping
    @ResponseBody
    public PHResp<String> insert(@RequestBody LeaseVo leaseVo) {
        // 新增租约
        leaseService.insertLease(leaseVo);
        return PHResp.success();
    }

    @PutMapping
    @ResponseBody
    @Transactional
    public PHResp<String> update(@RequestBody List<LeaseVo> voList) {
        leaseService.updateBatch(voList);
        return PHResp.success();
    }

    @DeleteMapping
    @ResponseBody
    public PHResp<String> deleteData(@RequestBody Long[] ids) {
        leaseService.batchDelete(ids);
        return PHResp.success();
    }
}
