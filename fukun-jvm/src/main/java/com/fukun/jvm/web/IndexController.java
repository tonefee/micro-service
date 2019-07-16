package com.fukun.jvm.web;

import com.fukun.commons.web.annotations.ResponseResult;
import com.fukun.jvm.model.ApplicationMemReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * jvm性能测试
 *
 * @author tangyifei
 * @date 2019年7月15日17:47:42
 */
@ResponseResult
@RestController("indexController")
@RequestMapping("/bug")
@Api(value = "jvm", tags = {"jvm"})
public class IndexController {

    private final static int _1M = 1024 * 1024;

    @ApiOperation(value = "性能测试", httpMethod = "POST", notes = "性能测试")
    @PostMapping("/allocationMem")
    public void allocationMem(@RequestBody ApplicationMemReq applicationMemReq) {
        int size = applicationMemReq.getNum();
        for(int i = 0; i < size; i++) {
            // 创建对象，cpu都会有一定的消耗
            byte[] mem = new byte[applicationMemReq.getMem() * _1M];
        }

    }

}
