package com.fukun.es.util;

import com.fukun.commons.util.CollectionUtil;
import com.fukun.commons.util.StringUtil;
import com.fukun.es.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.concurrent.atomic.LongAdder;

/**
 * 创建es工具类
 *
 * @author tangyifei
 * @date 2019年7月24日17:26:26
 */
@Component
@Slf4j
public class EsUtil {

    LongAdder la = new LongAdder();

    @Resource
    private RestHighLevelClient client;

    /**
     * 批量插入文档
     *
     * @param index 索引名称
     * @param list  文档列表
     */
    public void addBatchDocument(String index, List<Map<String, Object>> list) {
        if (StringUtil.isEmpty(index)) {
            if (log.isInfoEnabled()) {
                log.info("索引不能为空");
            }
            return;
        }
        if (CollectionUtil.isNotEmpty(list)) {
            la.reset();
            // 异步方法不会阻塞并立即返回
            ActionListener<BulkResponse> addBatchListener = new ActionListener<BulkResponse>() {
                @Override
                public void onResponse(BulkResponse bulkResponse) {
                    //如果执行成功，则调用onResponse方法;
                    if (log.isInfoEnabled()) {
                        log.info("添加{}个文档成功的结果：{}", la.intValue(), bulkResponse.status());
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    //如果执行失败，则调用 onFailure 方法;
                    if (log.isInfoEnabled()) {
                        log.error("添加{}个文档失败的相关异常：{}", la.intValue(), e);
                    }
                }
            };
            //批量操作请求：
            BulkRequest bulkRequest = new BulkRequest();
            Map<String, Object> map;
            Object object;
            String id = null;
            if (list instanceof RandomAccess) {
                int size = list.size();
                for(int i = 0; i < size; i++) {
                    map = list.get(i);
                    object = map.get(Constants.ES_DOC_ID);
                    if (null != object) {
                        id = (String) object;
                    }
                    la.add(1);
                    bulkRequest.add(new IndexRequest(index).id(id).source(map));
                }

            } else {
                for(Iterator<Map<String, Object>> iterator = list.iterator(); iterator.hasNext(); ) {
                    map = iterator.next();
                    object = map.get(Constants.ES_DOC_ID);
                    if (null != object) {
                        id = (String) object;
                    }
                    la.add(1);
                    bulkRequest.add(new IndexRequest(index).id(id).source(map));
                }
            }
            // 同步操作
            // BulkResponse r = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            // 异步操作
            client.bulkAsync(bulkRequest, RequestOptions.DEFAULT, addBatchListener);
        }
    }

    /**
     * 批量更新文档
     *
     * @param index 索引名称
     * @param list  文档列表
     */
    public void updateBatchDocument(String index, List<Map<String, Object>> list) {
        if (StringUtil.isEmpty(index)) {
            if (log.isInfoEnabled()) {
                log.info("索引不能为空");
            }
            return;
        }
        if (CollectionUtil.isNotEmpty(list)) {
            la.reset();
            // 异步方法不会阻塞并立即返回
            ActionListener<BulkResponse> updateBatchListener = new ActionListener<BulkResponse>() {
                @Override
                public void onResponse(BulkResponse bulkResponse) {
                    //如果执行成功，则调用onResponse方法;
                    if (log.isInfoEnabled()) {
                        log.info("更新{}个文档成功的结果：{}", la.intValue(), bulkResponse.status());
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    //如果执行失败，则调用 onFailure 方法;
                    if (log.isInfoEnabled()) {
                        log.error("更新{}个文档失败的相关异常：{}", la.intValue(), e);
                    }
                }
            };
            //批量操作请求：
            BulkRequest bulkRequest = new BulkRequest();
            Map<String, Object> map;
            Object object;
            String id = null;
            if (list instanceof RandomAccess) {
                int size = list.size();
                for(int i = 0; i < size; i++) {
                    map = list.get(i);
                    object = map.get(Constants.ES_DOC_ID);
                    if (null != object) {
                        id = (String) object;
                    }
                    bulkRequest.add(new UpdateRequest(index, id).doc(map));
                    la.add(1);
                }

            } else {
                for(Iterator<Map<String, Object>> iterator = list.iterator(); iterator.hasNext(); ) {
                    map = iterator.next();
                    object = map.get(Constants.ES_DOC_ID);
                    if (null != object) {
                        id = (String) object;
                    }
                    bulkRequest.add(new UpdateRequest(index, id).doc(map));
                    la.add(1);
                }
            }
            // 同步操作
            // BulkResponse r = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            // 异步操作
            client.bulkAsync(bulkRequest, RequestOptions.DEFAULT, updateBatchListener);
        }
    }

    /**
     * 批量删除文档
     *
     * @param index 索引名称
     * @param list  文档id列表
     */
    public void delBatchDocument(String index, List<String> list) {
        if (StringUtil.isEmpty(index)) {
            if (log.isInfoEnabled()) {
                log.info("索引不能为空");
            }
            return;
        }
        if (CollectionUtil.isNotEmpty(list)) {
            la.reset();
            // 异步方法不会阻塞并立即返回
            ActionListener<BulkResponse> deleteBatchListener = new ActionListener<BulkResponse>() {
                @Override
                public void onResponse(BulkResponse bulkResponse) {
                    //如果执行成功，则调用onResponse方法;
                    if (log.isInfoEnabled()) {
                        log.info("删除{}个文档成功的结果：{}", la.intValue(), bulkResponse.status());
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    //如果执行失败，则调用 onFailure 方法;
                    if (log.isInfoEnabled()) {
                        log.error("删除{}个文档失败的相关异常：{}", la.intValue(), e);
                    }
                }
            };
            //批量操作请求：
            BulkRequest bulkRequest = new BulkRequest();
            String id;
            if (list instanceof RandomAccess) {
                int size = list.size();
                for(int i = 0; i < size; i++) {
                    id = list.get(i);
                    bulkRequest.add(new DeleteRequest(index, id));
                    la.add(1);
                }

            } else {
                for(Iterator<String> iterator = list.iterator(); iterator.hasNext(); ) {
                    id = iterator.next();
                    bulkRequest.add(new DeleteRequest(index, id));
                    la.add(1);
                }
            }
            // 同步操作
            // BulkResponse r = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            // 异步操作
            client.bulkAsync(bulkRequest, RequestOptions.DEFAULT, deleteBatchListener);
        }
    }

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
        if (StringUtil.isEmpty(index)) {
            if (log.isInfoEnabled()) {
                log.info("索引不能为空");
            }
            return;
        }
        XContentBuilder builder = XContentFactory.jsonBuilder().startObject();
        String key;
        Object value;
        for(Map.Entry<String, Object> entry : map.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            builder.field(key, value);
        }
        builder.endObject();
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
        IndexRequest request = new IndexRequest(index);
        // 下面是同步操作
        // IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        // return response.toString();
        // 下面是异步操作
        request.id(id).opType("create").source(builder);
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
        if (StringUtil.isEmpty(index)) {
            if (log.isInfoEnabled()) {
                log.info("索引不能为空");
            }
            return;
        }
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
        DeleteRequest request = new DeleteRequest(index, id);
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
        if (StringUtil.isEmpty(index)) {
            if (log.isInfoEnabled()) {
                log.info("索引不能为空");
            }
            return;
        }
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
        UpdateRequest request = new UpdateRequest(index, id);
        request.doc(map);
        client.updateAsync(request, RequestOptions.DEFAULT, updateListener);
    }

}
