package com.panghu.housemanage.pojo.vo;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.panghu.housemanage.common.enumeration.LeaseTypeEnum;
import com.panghu.housemanage.common.enumeration.MemberSexEnum;
import com.panghu.housemanage.common.enumeration.MemberStatusEnum;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class LeaseVo extends PHBaseVo {
    private Long leaseId;
    @EnumValue
    private LeaseTypeEnum leaseType;
    private Integer unit;
    private BigDecimal rent;
    private Date startDate;
    private Date endDate;
}
