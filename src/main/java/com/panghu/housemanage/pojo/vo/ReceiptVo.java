package com.panghu.housemanage.pojo.vo;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.panghu.housemanage.common.enumeration.EffectiveEnum;
import com.panghu.housemanage.common.enumeration.LeaseTypeEnum;
import com.panghu.housemanage.common.enumeration.PayTypeEnum;
import com.panghu.housemanage.common.enumeration.ReceiptTypeEnum;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ReceiptVo extends PHBaseVo {
    private Long receiptId;
    @EnumValue
    private ReceiptTypeEnum receiptType;
    @EnumValue
    private PayTypeEnum payType;
    private String receiptNumber;
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
    private String voEffective;
    private String voReceiptType;
    private String searchKey;
    private List<String> receiptTypes;
}
