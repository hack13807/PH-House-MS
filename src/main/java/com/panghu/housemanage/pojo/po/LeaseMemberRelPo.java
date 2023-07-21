package com.panghu.housemanage.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_lease_member_ral")
public class LeaseMemberRelPo extends PHBasePo {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long leaseId;
    private Long memberId;
}
