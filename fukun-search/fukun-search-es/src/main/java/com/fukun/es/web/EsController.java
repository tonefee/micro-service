package com.fukun.es.web;

import com.fukun.commons.web.annotations.ResponseResult;
import com.fukun.es.manager.EsManager;
import com.fukun.es.query.EsQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Es相关的操作
 *
 * @author tangyifei
 * @date 2019年7月25日10:41:36
 */
@ResponseResult
@RestController("EsController")
@RequestMapping("/es")
@Slf4j
@Api(value = "fukun-search-es", tags = {"fukun-search-es"})
public class EsController {

    @Resource
    private EsManager esManager;

    @ApiOperation(value = "根据条件从es检索相关的文档", httpMethod = "POST", notes = "根据条件从es检索相关的文档")
    @PostMapping("/_query")
    public List<Map<String, Object>> queryDocumentByQueryCondition(@RequestBody EsQuery esQuery) throws Exception {
        return esManager.queryDocumentByAndCondition(esQuery);
    }

}
