package com.fukun.stock.web;

import com.fukun.commons.web.annotations.ResponseResult;
import com.fukun.stock.service.stock.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
@Slf4j
public class StockController {

    @Resource
    private StockService stockService;

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public int reduceStock() {
        return stockService.reduceStock("1");
    }

}
