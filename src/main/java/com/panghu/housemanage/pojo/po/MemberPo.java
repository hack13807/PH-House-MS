package com.panghu.housemanage.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import com.panghu.housemanage.common.enumeration.MemberSexEnum;
import lombok.*;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_member")
public class MemberPo extends PHBasePo{
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String name;
    private Integer sex;
    private String tel;
    @TableField("idcard")
    private String idCard;
    private Long roomId;
    private Integer status;
    @TableField("isdelete")
    private Integer isDelete;
}
