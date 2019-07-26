package com.fukun.es.web;

import com.fukun.commons.web.annotations.ResponseResult;
import com.fukun.es.constant.Constants;
import com.fukun.es.manager.EsManager;
import com.fukun.es.query.EsQuery;
import com.fukun.es.util.EsUtil;
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

    @Resource
    private EsUtil esUtil;

    @ApiOperation(value = "根据条件从es检索相关的文档", httpMethod = "POST", notes = "根据条件从es检索相关的文档")
    @PostMapping("/_query")
    public List<Map<String, Object>> queryDocumentByQueryCondition(@RequestBody EsQuery esQuery) throws Exception {
        return esManager.queryDocumentByAndCondition(esQuery);
    }

    @ApiOperation(value = "批量添加文档", httpMethod = "POST", notes = "批量添加文档")
    @PostMapping("/{index}/_madd")
    public int addBatchDocument(@PathVariable String index, @RequestBody List<Map<String, Object>> list) {
        return esUtil.addBatchDocument(index, list);
    }

    @ApiOperation(value = "添加文档", httpMethod = "POST", notes = "添加文档")
    @PostMapping("/{index}/{id}")
    public String addDocument(@PathVariable String index, @PathVariable String id, @RequestBody Map<String, Object> map) throws Exception {
        map.put(Constants.ES_DOC_ID, id);
        return esUtil.addDocument(index, id, map);
    }

    @ApiOperation(value = "批量删除文档", httpMethod = "DELETE", notes = "批量删除文档")
    @DeleteMapping("/{index}/_mdelete")
    public int delBatchDocument(@PathVariable String index, @RequestBody List<String> list) {
        return esUtil.delBatchDocument(index, list);
    }

    @ApiOperation(value = "删除文档", httpMethod = "DELETE", notes = "删除文档")
    @DeleteMapping("/{index}/{id}")
    public String delDocument(@PathVariable String index, @PathVariable String id) throws Exception {
        return esUtil.deleteDocument(index, id);
    }

    @ApiOperation(value = "批量更新文档", httpMethod = "PUT", notes = "批量更新文档")
    @PutMapping("/{index}/_mupdate")
    public int updateBatchDocument(@PathVariable String index, @RequestBody List<Map<String, Object>> list) {
        return esUtil.updateBatchDocument(index, list);
    }

    @ApiOperation(value = "更新文档", httpMethod = "PUT", notes = "更新文档")
    @PutMapping("/{index}/{id}")
    public String updateDocument(@PathVariable String index, @PathVariable String id, @RequestBody Map<String, Object> map) throws Exception {
        map.put(Constants.ES_DOC_ID, id);
        return esUtil.updateDocument(index, id, map);
    }

}
