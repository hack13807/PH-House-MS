package com.panghu.housemanage.pojo.vo;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.panghu.housemanage.common.enumeration.RoomStatusEnum;
import lombok.*;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class RoomVo  extends PHBaseVo {
    private String roomNo;
    private String roomDesc;
    @EnumValue
    private RoomStatusEnum roomStatus;
    private String memberName;
}
