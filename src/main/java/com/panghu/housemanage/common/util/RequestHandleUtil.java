package com.panghu.housemanage.common.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.pojo.po.PHBasePo;
import com.panghu.housemanage.pojo.vo.PHBaseVo;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller处理请求工具类
 */
public class RequestHandleUtil {

    private RequestHandleUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> Page<T> getPage(PHBasePo po) {
        int offset = po.getOffset();
        int limit = po.getLimit();
        return new Page<>(offset, limit);
    }

    public static Map<String, Object> successResult(IPage<? extends PHBaseVo> pageResult) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("total", pageResult.getTotal());
        resultMap.put("rows", pageResult.getRecords());
        return resultMap;
    }

    public static Map<String, Object> successResult() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", 200);
        return resultMap;
    }

    public static Map<String, Object> errorResult(Exception e) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", 300);
        resultMap.put("msg", e.toString());
        return resultMap;
    }
}
