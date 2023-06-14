package com.panghu.housemanage.pojo.vo;

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
public class MemberVo {
    private Long memberId;
    private Long roomId;
    private String memberName;
    private String roomNo;
    private String phone;
    private String idCard;
    private String memberStatus;
    private String roomDesc;
}
