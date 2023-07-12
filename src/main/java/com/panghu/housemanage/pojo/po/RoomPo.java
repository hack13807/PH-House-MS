package com.panghu.housemanage.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_room")
public class RoomPo extends PHBasePo {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String number;
    private String description;
    private Integer status;
    private Integer enable;
}
