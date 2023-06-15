package com.panghu.housemanage.common.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.common.constant.PageControlConstant;
import com.panghu.housemanage.pojo.condition.MemberCondition;
import com.panghu.housemanage.pojo.condition.PHBaseCondition;

import java.util.Optional;

public class PageParamTransUtil {

    public static <T> Page<T> getPage(PHBaseCondition condition) {
        PHBaseCondition init = Optional.ofNullable(condition).orElse(PHBaseCondition.builder().pn(1).build());
        return new Page<>(init.getPn(), PageControlConstant.PAGENUM);
    }
}
