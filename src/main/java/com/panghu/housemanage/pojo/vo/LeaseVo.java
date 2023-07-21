package com.panghu.housemanage.pojo.vo;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.panghu.housemanage.common.enumeration.EffectiveEnum;
import com.panghu.housemanage.common.enumeration.LeaseTypeEnum;
import com.panghu.housemanage.common.enumeration.MemberSexEnum;
import com.panghu.housemanage.common.enumeration.MemberStatusEnum;
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
public class LeaseVo extends PHBaseVo {
    private Long leaseId;
    @EnumValue
    private LeaseTypeEnum leaseType;
    private Integer unit;
    private BigDecimal rentAmount;
    private Date startDate;
    private Date endDate;
    private String voUnit;
    private String leaseNumber;
    private EffectiveEnum effective;

    /**
     * 页面参数
     */
    private String voLeaseType;
    private String voEffective;
    private String memberName;
    private List<String> memberNames;
    private List<MemberVo> members;
    private List<String> roomNos;
    private Long roomId;
    private String roomNo;
    private Long memberId;
    private String tel;
    private Integer sex;
    private String idCard;
}
