package com.panghu.housemanage.common.util;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 请求统一响应类
 *
 * @author PangHu
 * @date 2023/06/24
 */
@Data
@AllArgsConstructor
public class PHResp<T> {
    private int code;
    private String msg;
    private T data;

    public static <T> PHResp<T> success() {
        return new PHResp<>(200, "success", null);
    }
    public static <T> PHResp<T> success(T data) {
        return new PHResp<>(200, "success", data);
    }
    public static <T> PHResp<T> success(String msg, T data) {
        return new PHResp<>(200, msg, data);
    }
    public static <T> PHResp<T> error(int code, String msg) {
        return new PHResp<>(code, msg, null);
    }
}
