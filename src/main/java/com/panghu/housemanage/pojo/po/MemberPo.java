package com.panghu.housemanage.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
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

    /**
     * 页面参数
     */
    @TableField(exist = false)
    private Long memberId;
    @TableField(exist = false)
    private String memberName;
    @TableField(exist = false)
    private String roomNo;
    @TableField(exist = false)
    private String memberStatus;
    @TableField(exist = false)
    private String roomDesc;
}
