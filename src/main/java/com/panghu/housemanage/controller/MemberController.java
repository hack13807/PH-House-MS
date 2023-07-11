package com.panghu.housemanage.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.common.enumeration.PHExceptionCodeEnum;
import com.panghu.housemanage.common.exception.PHServiceException;
import com.panghu.housemanage.common.util.PHResp;
import com.panghu.housemanage.common.util.RequestHandleUtils;
import com.panghu.housemanage.pojo.po.MemberPo;
import com.panghu.housemanage.pojo.vo.MemberVo;
import com.panghu.housemanage.service.MemberService;
import com.panghu.housemanage.service.RoomService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    public String getPage(@RequestParam(required = false, value = "roomSearch") String roomSearch, Model model){
        model.addAttribute("roomSearch", roomSearch);
        return "member_list";
    }

    @GetMapping
    @ResponseBody
    public PHResp<Map<String, Object>> getData(HttpServletRequest request) throws Exception {
        // 通过前端参数构建分页对象page
        Page<MemberVo> page = RequestHandleUtils.getPage(request);
        // 通过前端参数构建查询实体po
        MemberVo memberVo = RequestHandleUtils.buildPoEntity(request, MemberVo.class);
        // 把分页对象page和查询实体po传到service层，查询结果返回封装成Page对象
        IPage<MemberVo> pageResult = memberService.pageQueryMember(page, memberVo);
        // 获取查询总数和记录，构建返回前端的Map对象
        return RequestHandleUtils.successPageResult(pageResult);
    }

    @GetMapping("/queryMember")
    @ResponseBody
    public PHResp<List<MemberPo>> queryMember(String id)  {
        List<MemberPo> memberPos = memberService.queryMember(Map.of("id", id));
        return PHResp.success(memberPos);
    }
    @GetMapping("/getByRoomId")
    @ResponseBody
    public PHResp<List<MemberVo>> getByRoomId(@RequestParam("roomId") Long roomId)  {
        List<MemberVo> memberVos = memberService.getByRoomId(Map.of("roomId", roomId));
        return PHResp.success(memberVos);
    }
    @GetMapping("/isTerminate")
    @ResponseBody
    public PHResp<String> isTerminate(Long[] ids)  {
        List<MemberPo> rentingList = memberService.isTerminate(ids);
        if (CollectionUtils.isEmpty(rentingList)) {
            return PHResp.success();
        }
        String memberNames = String.join(", ", rentingList.stream().map(MemberPo::getName).toList());
        throw new PHServiceException(PHExceptionCodeEnum.MEMBER_RENTING, memberNames);
    }

    @GetMapping("/cache")
    @ResponseBody
    public PHResp<List<MemberPo>> getCache()  {
        List<MemberPo> memberList = memberService.getAllMember();
        return PHResp.success(memberList);
    }

    @DeleteMapping
    @ResponseBody
    public PHResp<String> deleteData(@RequestBody Long[] ids) {
        memberService.batchDelete(ids);
        return PHResp.success();
    }

    @PutMapping
    @ResponseBody
    public PHResp<String> update(@RequestBody List<MemberVo> volist) {
        List<MemberPo> memberList = RequestHandleUtils.memberDTOTrans(volist);
        memberService.updateBatch(memberList);
        return PHResp.success();
    }

    @PostMapping
    @ResponseBody
    @Transactional
    public PHResp<String> insert(@RequestBody MemberVo memberVo) {
        MemberPo memberPo = RequestHandleUtils.memberDTOTrans(Collections.singletonList(memberVo)).get(0);
        // 验重
        MemberPo member = memberService.checkUnique(memberPo);
        if (member != null) {
            throw new PHServiceException(PHExceptionCodeEnum.UNIQUE_MEMBER, null);
        }
        // 新增租客记录
        memberService.insertMember(memberPo);
        return PHResp.success();
    }

}
