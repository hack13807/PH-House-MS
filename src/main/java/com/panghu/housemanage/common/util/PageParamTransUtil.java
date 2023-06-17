package com.panghu.housemanage.common.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.common.constant.PageControlConstant;
import com.panghu.housemanage.pojo.condition.MemberCondition;
import com.panghu.housemanage.pojo.condition.PHBaseCondition;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public class PageParamTransUtil {

    public static <T> Page<T> getPage(HttpServletRequest request) {
        long offset = Long.parseLong(request.getParameter("offset"));
        long limit = Long.parseLong(request.getParameter("limit"));
        return new Page<>(offset, limit);
    }
}
