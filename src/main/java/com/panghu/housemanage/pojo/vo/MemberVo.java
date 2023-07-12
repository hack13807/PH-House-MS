package com.panghu.housemanage.pojo.vo;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.panghu.housemanage.common.enumeration.MemberSexEnum;
import com.panghu.housemanage.common.enumeration.MemberStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class MemberVo extends PHBaseVo {
    private Long roomId;
    private String memberName;
    private String roomNo;
    private List<String> roomNos;
    private String tel;
    private String idCard;
    @EnumValue
    private MemberSexEnum sex;
    @EnumValue
    private MemberStatusEnum memberStatus;
    private String roomDesc;

    /*页面参数*/
    private String voStatus;
}
