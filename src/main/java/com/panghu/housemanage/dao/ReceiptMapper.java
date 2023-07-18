package com.panghu.housemanage.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.pojo.po.ReceiptPo;
import com.panghu.housemanage.pojo.vo.LeaseVo;
import com.panghu.housemanage.pojo.vo.PHBaseVo;
import com.panghu.housemanage.pojo.vo.ReceiptVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReceiptMapper extends BaseMapper<ReceiptPo> {

    Page<ReceiptVo> pageQueryReceipt(Page<ReceiptVo> page, @Param("receiptVo") PHBaseVo vo);

}
