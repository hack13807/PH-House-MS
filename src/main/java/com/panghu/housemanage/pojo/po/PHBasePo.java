package com.panghu.housemanage.pojo.po;

import lombok.Data;

import java.util.Date;

/**
 * phbase阿宝
 * PO基类，所有ResponseBody对象继承此类
 *
 * @author PangHu
 * @date 2023/06/25
 */
@Data
public abstract class PHBasePo {
    private int offset;
    private int limit;
    private Date createTime;
    private Date lastUpdateTime;
    private long createUserId;
    private long lastUpdateUserId;
}
