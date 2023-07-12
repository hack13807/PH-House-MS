package com.panghu.housemanage.pojo.vo;

import lombok.Data;
import lombok.Getter;

/**
 * VO基类，所有ResponseBody对象继承此类
 */
@Data
public abstract class PHBaseVo {
    /** 行id */
    public Long rowId;

    private String optType;
}
