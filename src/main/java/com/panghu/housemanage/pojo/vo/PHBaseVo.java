package com.panghu.housemanage.pojo.vo;

import lombok.Getter;

/**
 * VO基类，所有ResponseBody对象继承此类
 */
@Getter
public abstract class PHBaseVo {
    /** 行id */
    public Long rowId;
}
