package com.fukun.stock.web;

import com.fukun.commons.web.annotations.ResponseResult;
import com.fukun.stock.service.stock.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 库存控制层
 *
 * @author tangyifei
 * @since 2019-5-24 15:48:53
 */
@ResponseResult
@RestController("StockController")
@RequestMapping("/stocks")
public class StockController {

    @Resource
    private StockService stockService;

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public int reduceStock(@RequestParam("stockId") String stockId) {
        return stockService.reduceStock(stockId);
    }

}
