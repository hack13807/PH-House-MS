package com.panghu.housemanage.pojo.vo;

import lombok.Data;

import java.util.Date;

/**
 * VO基类，所有ResponseBody对象继承此类
 */
@Data
public abstract class PHBaseVo {
    /** 行id */
    public Long rowId;

    private String optType;

    private Date createTime;
    private Date lastUpdateTime;
    private String createUser;
    private String lastUpdateUser;
}
