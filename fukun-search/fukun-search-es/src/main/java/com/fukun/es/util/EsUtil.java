package com.fukun.es.util;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 创建es工具类
 *
 * @author tangyifei
 * @date 2019年7月24日17:26:26
 */
@Component
@Slf4j
public class EsUtil {

    @Resource
    private RestHighLevelClient client;

    /**
     * 创建索引
     *
     * @param index 索引的名字
     * @param id    文档的id
     * @param map   文档内容
     * @return 响应
     * @throws Exception 异常
     */
    public String addDocument(String index, String id, Map<String, Object> map) throws Exception {
        XContentBuilder builder = XContentFactory.jsonBuilder().startObject();
        String key;
        Object value;
        for(Map.Entry<String, Object> entry : map.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            builder.field(key, value);
        }
        builder.endObject();
        IndexRequest request = new IndexRequest(index);
        request.id(id).opType("create").source(builder);
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        return response.toString();
    }

    /**
     * 删除文档
     *
     * @param index 索引名称
     * @param id    文档id
     * @return 响应
     * @throws Exception 异常
     */
    public String deleteDocument(String index, String id) throws Exception {
        DeleteRequest request = new DeleteRequest(index, id);
        DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
        return response.toString();
    }

    /**
     * 更新文档
     *
     * @param index 索引名称
     * @param id    文档id
     * @return 响应
     * @throws Exception 异常
     */
    public String updateDocument(String index, String id, Map<String, Object> map) throws Exception {
        UpdateRequest request = new UpdateRequest(index, id);
        request.doc(map);
        return client.update(request, RequestOptions.DEFAULT).toString();
    }

}
