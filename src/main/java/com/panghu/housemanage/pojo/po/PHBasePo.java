package com.panghu.housemanage.pojo.po;

import com.baomidou.mybatisplus.annotation.TableField;
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
    @TableField(exist = false)
    private int offset;
    @TableField(exist = false)
    private int limit;
    @TableField(exist = false)
    private Date createTime;
    @TableField(exist = false)
    private Date lastUpdateTime;
    @TableField(exist = false)
    private long createUserId;
    @TableField(exist = false)
    private long lastUpdateUserId;
}
