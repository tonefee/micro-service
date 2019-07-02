package com.fukun.order.client.hystrix;

import com.fukun.commons.enums.BusinessExceptionEnum;
import com.fukun.commons.exceptions.BusinessException;
import com.fukun.order.client.StockClient;
import org.springframework.stereotype.Component;

@Component
public class StockHystrix implements StockClient {

    @Override
    public void reduceStock() {
        throw new BusinessException(BusinessExceptionEnum.REMOTE_ACCESS_ERROR.getResultCode());
    }
}
