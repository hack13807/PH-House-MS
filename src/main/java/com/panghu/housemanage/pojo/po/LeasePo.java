package com.panghu.housemanage.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 租赁po
 *
 * @author PangHu
 * @date 2023/06/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_lease")
public class LeasePo extends PHBasePo{
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Integer leaseType;
    private Integer unit;
    private BigDecimal rent;
    private Date startDate;
    private Date endDate;
    private Long roomId;
    private Long memberId;

    /**
     * 页面参数
     */
    @TableField(exist = false)
    private Long leaseId;
    @TableField(exist = false)
    private String memberName;
    @TableField(exist = false)
    private String roomNo;
    @TableField(exist = false)
    private String memberStatus;
    @TableField(exist = false)
    private String roomDesc;
}
