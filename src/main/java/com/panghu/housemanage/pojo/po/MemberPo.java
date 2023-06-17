package com.panghu.housemanage.pojo.po;

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
public class MemberPo extends PHBasePo{
    private Long memberId;
    private Long roomId;
    private String memberName;
    private String roomNo;
    private String tel;
    private String idCard;
    private String memberStatus;
    private String roomDesc;
}
