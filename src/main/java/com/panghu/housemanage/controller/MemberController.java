package com.panghu.housemanage.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.common.enumeration.RoomStatusEnum;
import com.panghu.housemanage.common.util.PHResp;
import com.panghu.housemanage.common.util.RequestHandleUtil;
import com.panghu.housemanage.pojo.po.MemberPo;
import com.panghu.housemanage.pojo.vo.MemberVo;
import com.panghu.housemanage.service.MemberService;
import com.panghu.housemanage.service.RoomService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 租客控制器
 *
 * @author PangHu
 * @date 2023/06/18
 */
@Controller
@RequestMapping("/member")
public class MemberController {
    @Autowired
    MemberService memberService;
    @Autowired
    RoomService roomService;

    @GetMapping("/page")
    public String getPage(HttpServletRequest request){
        return "member_list";
    }

    @GetMapping
    @ResponseBody
    public PHResp<Map<String, Object>> getData(HttpServletRequest request) throws Exception {
        // 通过前端参数构建分页对象page
        Page<MemberVo> page = RequestHandleUtil.getPage(request);
        // 通过前端参数构建查询实体po
        MemberVo memberVo = RequestHandleUtil.buildPoEntity(request, MemberVo.class);
        // 把分页对象page和查询实体po传到service层，查询结果返回封装成Page对象
        IPage<MemberVo> pageResult = memberService.pageQueryMember(page, memberVo);
        // 获取查询总数和记录，构建返回前端的Map对象
        return RequestHandleUtil.successPageResult(pageResult);
    }

    @DeleteMapping
    @ResponseBody
    public PHResp<String> deleteData(@RequestBody Long[] ids) {
        memberService.batchDelete(ids);
        return PHResp.success();
    }

    @PutMapping
    @ResponseBody
    @Transactional
    public PHResp<String> update(@RequestBody List<MemberVo> volist) {
        List<MemberPo> memberList = RequestHandleUtil.memberDTOTrans(volist);
        // 更新房间状态 TODO bug先查询当前房间，和编辑后的房间做对比
        roomService.updateRoomStatus(memberList.stream().map(MemberPo::getRoomId).collect(Collectors.toSet()), RoomStatusEnum.UNUSED);
        // 更新租客信息
        memberService.updateBatch(memberList);
        return PHResp.success();
    }

    @PostMapping
    @ResponseBody
    public PHResp<String> insert(@RequestBody MemberVo memberVo) {
        MemberPo memberPo = RequestHandleUtil.memberDTOTrans(Collections.singletonList(memberVo)).get(0);
        memberService.insertMember(memberPo);
        // TODO 更新房间状态
        // 更新房间状态
        roomService.updateRoomStatus(Set.of(memberVo.getRoomId()), RoomStatusEnum.INUSE);
        return PHResp.success();
    }

}
