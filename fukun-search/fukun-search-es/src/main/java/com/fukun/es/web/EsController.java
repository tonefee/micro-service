package com.fukun.es.web;

import com.fukun.commons.web.annotations.ResponseResult;
import com.fukun.es.manager.EsManager;
import com.fukun.es.query.EsQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value = "批量添加文档", httpMethod = "POST", notes = "批量添加文档")
    @PostMapping("/{index}/_madd")
    public void addBatchDocument(@PathVariable String index, @RequestBody List<Map<String, Object>> list) {
        esManager.addBatchDocument(index, list);
    }

    @ApiOperation(value = "批量删除文档", httpMethod = "DELETE", notes = "批量删除文档")
    @DeleteMapping("/{index}/_mdelete")
    public void delBatchDocument(@PathVariable String index, @RequestBody List<String> list) {
        esManager.delBatchDocument(index, list);
    }

    @ApiOperation(value = "批量更新文档", httpMethod = "PUT", notes = "批量更新文档")
    @PutMapping("/{index}/_mupdate")
    public void updateBatchDocument(@PathVariable String index, @RequestBody List<Map<String, Object>> list) {
        esManager.updateBatchDocument(index, list);
    }

}
