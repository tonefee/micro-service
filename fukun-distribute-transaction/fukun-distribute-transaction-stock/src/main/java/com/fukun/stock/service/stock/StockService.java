package com.fukun.stock.service.stock;

import com.fukun.commons.service.CrudService;
import com.fukun.stock.model.po.StockPO;

/**
 * 库存服务接口
 *
 * @author tangyifei
 * @since 2019-5-24 15:32:20
 */
public interface StockService extends CrudService<StockPO, String> {

    /**
     * 减少库存
     *
     * @param stockId 库存主键
     * @return 影响的行数
     */
    void reduceStock(String stockId);

}
