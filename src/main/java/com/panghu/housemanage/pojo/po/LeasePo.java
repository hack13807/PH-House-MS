package com.panghu.housemanage.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.panghu.housemanage.pojo.vo.MemberVo;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 租赁po
 *
 * @author PangHu
 * @date 2023/06/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_lease")
public class LeasePo extends PHBasePo{
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Integer leaseType;
    private Integer unit;
    private BigDecimal rentAmount;
    private Date startDate;
    private Date endDate;
    private Long roomId;
//    private Long memberId;
    private String number;
    private Integer effective;
    @TableField("isdelete")
    private Integer isDelete;

    /**
     * 页面参数
     */
    @TableField(exist = false)
    private Long leaseId;
    @TableField(exist = false)
    private String memberName;
    @TableField(exist = false)
    private String roomNo;
    @TableField(exist = false)
    private List<MemberVo> members;
    @TableField(exist = false)
    private String roomDesc;
    @TableField(exist = false)
    private String voUnit;
}
