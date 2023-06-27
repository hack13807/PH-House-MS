package com.panghu.housemanage.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_room")
public class RoomPo extends PHBasePo {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String number;
    private String description;
    private String status;
    private String memberId;

    /**
     * 页面参数
     */
    @TableField(exist = false)
    private String memberName;
    @TableField(exist = false)
    private String roomNo;
    @TableField(exist = false)
    private String roomStatus;
    @TableField(exist = false)
    private String roomDesc;
}
