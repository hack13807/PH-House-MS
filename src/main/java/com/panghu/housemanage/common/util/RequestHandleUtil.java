package com.panghu.housemanage.common.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.pojo.po.MemberPo;
import com.panghu.housemanage.pojo.po.PHBasePo;
import com.panghu.housemanage.pojo.vo.PHBaseVo;
import jakarta.servlet.http.HttpServletRequest;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller处理请求工具类
 */
public class RequestHandleUtil {

    private RequestHandleUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> Page<T> getPage(HttpServletRequest request) {
        int offset = Integer.parseInt(request.getParameter("offset") == null ? "0" : request.getParameter("offset"));
        int limit = Integer.parseInt(request.getParameter("limit") == null ? "10" : request.getParameter("limit"));
        return new Page<>(offset / limit + 1L, limit);
    }

    public static PHResp<Map<String, Object>> successPageResult(IPage<? extends PHBaseVo> pageResult) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("total", pageResult.getTotal());
        resultMap.put("rows", pageResult.getRecords());
        return PHResp.success(resultMap);
    }

    public static PHResp<String> successResult() {
        return PHResp.success();
    }

    public static Map<String, Object> errorResult(Exception e) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", 300);
        resultMap.put("msg", e.toString());
        return resultMap;
    }

    /**
     * 建立订单实体
     *
     * @param request 请求
     * @param clazz   clazz
     * @return {@link T}
     * @throws Exception 异常
     */
    public static <T> T buildPoEntity(HttpServletRequest request, Class<? extends PHBasePo> clazz) throws Exception {
// 实例化类对象
        T instance = (T) clazz.newInstance();

        // 获取所有属性
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            // 获取属性名
            String fieldName = field.getName();

            // 从请求参数中获取对应的值
            String value = request.getParameter(fieldName);
            if (value == null) {
                continue;
            }

            // 将值转换为与属性类型相符的类型，并设置属性值
            Class<?> fieldType = field.getType();
            Object fieldValue = convertStringToType(value, fieldType);
            field.setAccessible(true);
            field.set(instance, fieldValue);
        }

        return instance;
    }

    private static Object convertStringToType(String str, Class<?> fieldType) {
        if (fieldType.equals(String.class)) {
            return str;
        } else if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
            return Integer.valueOf(str);
        } else if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
            return Long.valueOf(str);
        } else if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
            return Boolean.valueOf(str);
        } else {
            throw new IllegalArgumentException("Unsupported field type: " + fieldType.getName());
        }
    }
}
