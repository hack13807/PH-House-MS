package com.panghu.housemanage.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import com.panghu.housemanage.common.enumeration.PayTypeEnum;
import com.panghu.housemanage.common.enumeration.ReceiptTypeEnum;
import lombok.*;

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
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_receipt")
public class ReceiptPo extends PHBasePo{
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Integer receiptType;
    private Integer payType;
    private String number;
    private String leaseNumber;
    private String roomNo;
    private String memberName;
    private String memberIdcard;
    private String memberTel;
    private BigDecimal amount;
    private Date bizDate;

    /**
     * 页面参数
     */
    // @TableField(exist = false)
    // private Long leaseId;
    // @TableField(exist = false)
    // private String memberStatus;
    // @TableField(exist = false)
    // private String roomDesc;
    // @TableField(exist = false)
    // private String voUnit;
}
