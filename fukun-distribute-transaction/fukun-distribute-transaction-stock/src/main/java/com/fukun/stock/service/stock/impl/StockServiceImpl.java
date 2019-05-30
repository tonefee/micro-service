package com.fukun.stock.service.stock.impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fukun.commons.service.impl.BaseMySqlCrudServiceImpl;
import com.fukun.stock.mapper.StockMapper;
import com.fukun.stock.model.po.StockPO;
import com.fukun.stock.service.stock.StockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 库存服务接口实现
 *
 * @author tangyifei
 * @since 2019-5-24 15:33:14
 */
@Service
public class StockServiceImpl extends BaseMySqlCrudServiceImpl<StockPO, String> implements StockService {

    private final static String TRANSACTION_MANAGER = "testTransactionManager";

    @Resource
    private StockMapper stockMapper;

    @Override
    @LcnTransaction
    @Transactional(value = TRANSACTION_MANAGER, rollbackFor = Exception.class)
    public int reduceStock(String stockId) {
        return stockMapper.reduceStock(stockId);
    }
}
