package com.panghu.housemanage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.pojo.vo.LeaseVo;
import com.panghu.housemanage.pojo.vo.ReceiptVo;

import java.util.List;

public interface ReceiptService {

    // List<LeaseVo> queryLeaseByMemberId(Long memberId);

    IPage<ReceiptVo> pageQueryReceipt(Page<ReceiptVo> page, ReceiptVo receiptVo);

    void insertReceipt(ReceiptVo receiptVo);

    void updateBatch(List<ReceiptVo> voList);

    void batchDelete(Long[] ids);

}
