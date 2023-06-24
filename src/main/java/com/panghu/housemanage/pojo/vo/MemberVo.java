package com.panghu.housemanage.pojo.vo;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.panghu.housemanage.common.enumeration.MemberStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class MemberVo extends PHBaseVo {
    private Long memberId;
    private Long roomId;
    private String memberName;
    private String roomNo;
    private String tel;
    private String idCard;
    @EnumValue
    private MemberStatusEnum memberStatus;
    private String roomDesc;
}
