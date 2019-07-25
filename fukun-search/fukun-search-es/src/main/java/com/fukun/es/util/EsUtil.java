package com.fukun.es.util;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
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
    public void addDocument(String index, String id, Map<String, Object> map) throws Exception {
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
        // 异步方法不会阻塞并立即返回
        ActionListener<IndexResponse> addListener = new ActionListener<IndexResponse>() {
            @Override
            public void onResponse(IndexResponse indexResponse) {
                //如果执行成功，则调用onResponse方法;
                if (log.isInfoEnabled()) {
                    log.info("添加成功的结果：{}", indexResponse.toString());
                }
            }

            @Override
            public void onFailure(Exception e) {
                //如果执行失败，则调用 onFailure 方法;
                if (log.isInfoEnabled()) {
                    log.error("添加失败的相关异常：{}", e);
                }
            }
        };
        // 下面是同步操作
        // IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        // return response.toString();
        // 下面是异步操作
        client.indexAsync(request, RequestOptions.DEFAULT, addListener);
    }

    /**
     * 删除文档
     *
     * @param index 索引名称
     * @param id    文档id
     * @return 响应
     * @throws Exception 异常
     */
    public void deleteDocument(String index, String id) {
        DeleteRequest request = new DeleteRequest(index, id);
        ActionListener<DeleteResponse> deleteListener = new ActionListener<DeleteResponse>() {
            @Override
            public void onResponse(DeleteResponse deleteResponse) {
                //如果执行成功，则调用onResponse方法;
                if (log.isInfoEnabled()) {
                    log.info("删除成功的结果：{}", deleteResponse.toString());
                }
            }

            @Override
            public void onFailure(Exception e) {
                //如果执行失败，则调用 onFailure 方法;
                if (log.isInfoEnabled()) {
                    log.error("删除失败的相关异常：{}", e);
                }
            }
        };
        // 同步操作
        // DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
        // return response.toString();
        // 异步操作
        client.deleteAsync(request, RequestOptions.DEFAULT, deleteListener);
    }

    /**
     * 更新文档
     *
     * @param index 索引名称
     * @param id    文档id
     * @return 响应
     * @throws Exception 异常
     */
    public void updateDocument(String index, String id, Map<String, Object> map) {
        UpdateRequest request = new UpdateRequest(index, id);
        request.doc(map);
        ActionListener<UpdateResponse> updateListener = new ActionListener<UpdateResponse>() {
            @Override
            public void onResponse(UpdateResponse updateResponse) {
                //如果执行成功，则调用onResponse方法;
                if (log.isInfoEnabled()) {
                    log.info("更新成功的结果：{}", updateResponse.toString());
                }
            }

            @Override
            public void onFailure(Exception e) {
                //如果执行失败，则调用 onFailure 方法;
                if (log.isInfoEnabled()) {
                    log.error("更新失败的相关异常：{}", e);
                }
            }
        };
        client.updateAsync(request, RequestOptions.DEFAULT, updateListener);
    }

}
