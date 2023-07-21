package com.panghu.housemanage.pojo.vo;

import lombok.*;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class LeaseMemberRelVo extends PHBaseVo {
    private Long leaseId;
    private Long memberId;
}
