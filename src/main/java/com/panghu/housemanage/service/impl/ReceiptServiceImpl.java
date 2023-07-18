package com.panghu.housemanage.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.dao.ReceiptMapper;
import com.panghu.housemanage.pojo.po.ReceiptPo;
import com.panghu.housemanage.pojo.vo.ReceiptVo;
import com.panghu.housemanage.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceiptServiceImpl implements ReceiptService {
    @Autowired
    ReceiptMapper receiptMapper;

    @Override
    public IPage<ReceiptVo> pageQueryReceipt(Page<ReceiptVo> page, ReceiptVo receiptVo) {
        preprocess(receiptVo);
        Page<ReceiptVo> receiptVoPage = receiptMapper.pageQueryReceipt(page, receiptVo);
        return receiptVoPage;
    }

    private void preprocess(ReceiptVo vo) {
    }

    @Override
    public void insertReceipt(ReceiptVo leaseVo) {
    }


    @Override
    public void updateBatch(List<ReceiptVo> voList) {
    }


    @Override
    public void batchDelete(Long[] ids) {
        LambdaUpdateWrapper<ReceiptPo> updateWrapper = new LambdaUpdateWrapper<>();
        receiptMapper.update(null, updateWrapper);
    }

}
