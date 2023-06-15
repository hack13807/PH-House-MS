package com.panghu.housemanage.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("t_member")
public class MemberVo {
    @TableId(value = "id",type = IdType.AUTO)//指定自增策略
    private Long memberId;
    private Long roomId;
    private String memberName;
    private String roomNo;
    private String phone;
    private String idCard;
    private String memberStatus;
    private String roomDesc;
}
