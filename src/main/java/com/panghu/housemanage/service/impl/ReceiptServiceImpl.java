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
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class ReceiptServiceImpl implements ReceiptService {
    @Autowired
    ReceiptMapper receiptMapper;

    @Override
    public IPage<ReceiptVo> pageQueryReceipt(Page<ReceiptVo> page, ReceiptVo receiptVo) {
        preprocess(receiptVo);
        return receiptMapper.pageQueryReceipt(page, receiptVo);
    }

    private void preprocess(ReceiptVo vo) {
        // 处理多选复选框款项类型的值
        String voReceiptType = vo.getVoReceiptType();
        if (!ObjectUtils.isEmpty(voReceiptType)) {
            String[] split = voReceiptType.split(",");
            List<String> receiptTypeList = Arrays.stream(split).toList();
            vo.setReceiptTypes(receiptTypeList);
        }
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
