package com.panghu.housemanage.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.common.util.PHResp;
import com.panghu.housemanage.common.util.RequestHandleUtils;
import com.panghu.housemanage.pojo.vo.ReceiptVo;
import com.panghu.housemanage.service.ReceiptService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;


@Controller
@RequestMapping("/receipt")
public class ReceiptController {
    @Autowired
    ReceiptService receiptService;

    @GetMapping("/page")
    public String getPage(@RequestParam(required = false, value = "memberSearch") String memberSearch, Model model){
        model.addAttribute("memberSearch", memberSearch);
        return "receipt_list";
    }

    @GetMapping
    @ResponseBody
    public PHResp<Map<String, Object>> getData(HttpServletRequest request) throws Exception {
        // 通过前端参数构建分页对象page
        Page<ReceiptVo> page = RequestHandleUtils.getPage(request);
        // 通过前端参数构建查询实体po
        ReceiptVo receiptVo = RequestHandleUtils.buildPoEntity(request, ReceiptVo.class);
        // 把分页对象page和查询实体po传到service层，查询结果返回封装成Page对象
        IPage<ReceiptVo> pageResult = receiptService.pageQueryReceipt(page, receiptVo);
        // 获取查询总数和记录，构建返回前端的Map对象
        return RequestHandleUtils.successPageResult(pageResult);
    }
}
