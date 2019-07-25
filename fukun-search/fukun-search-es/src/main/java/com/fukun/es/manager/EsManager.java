package com.fukun.es.manager;

import com.fukun.commons.util.StringUtil;
import com.fukun.es.query.EsQuery;
import com.fukun.es.util.EsUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * es的相关逻辑层
 *
 * @author tangyifei
 * @date 2019年7月25日11:03:25
 */
@Service
@Slf4j
public class EsManager {

    @Resource
    private RestHighLevelClient client;

    @Resource
    private EsUtil esUtil;

    /**
     * 根据多个查询条件并集查找相关的文档（复合查询）
     *
     * @param esQuery es查询参数对象
     * @return 查询结果对象
     * @throws Exception 可能出现的异常
     */
    public List<Map<String, Object>> queryDocumentByAndCondition(EsQuery esQuery) throws Exception {
        String index = esQuery.getIndex();
        if (StringUtil.isEmpty(index)) {
            if (log.isInfoEnabled()) {
                log.info("es相关的索引名称不能为空！");
            }
        }
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        // 合并查询参数，查询参数以and方式在es中进行查询
        Map<String, Object> queryMap = esQuery.getQueryMap();
        if (MapUtils.isNotEmpty(queryMap)) {
            String key;
            Object value;
            for(Map.Entry<String, Object> entry : queryMap.entrySet()) {
                key = entry.getKey();
                value = entry.getValue();
                boolQueryBuilder.must(QueryBuilders.matchQuery(key, value));
            }
        }

        // 排序
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        String sortParam = esQuery.getSort();
        if (StringUtil.isNotEmpty(sortParam)) {
            searchSourceBuilder.sort(SortBuilders.fieldSort(sortParam).order(SortOrder.DESC));
        }

        // 分页
        Integer page = esQuery.getPage();
        Integer pageSize = esQuery.getPageSize();
        if (null != page && null != pageSize) {
            searchSourceBuilder.from(page).size(pageSize).query(boolQueryBuilder);
        }

        SearchRequest request = new SearchRequest(index);
        request.searchType(SearchType.DEFAULT).source(searchSourceBuilder);

        // 获取结果
        SearchHit[] searchHitArray = client.search(request, RequestOptions.DEFAULT).getHits().getHits();
        if (ArrayUtils.isNotEmpty(searchHitArray)) {
            int len = searchHitArray.length;
            List<Map<String, Object>> list = Lists.newLinkedList();
            SearchHit searchHit;
            for(int i = 0; i < len; i++) {
                searchHit = searchHitArray[i];
                list.add(searchHit.getSourceAsMap());
            }
            return list;
        }
        return null;
    }

    /**
     * 批量添加文档操作
     *
     * @param index 索引名称
     * @param list  文档列表
     */
    public void addBatchDocument(String index, List<Map<String, Object>> list) {
        esUtil.addBatchDocument(index, list);
    }

    /**
     * 批量删除文档操作
     *
     * @param index 索引名称
     * @param list  文档id列表
     */
    public void delBatchDocument(String index, List<String> list) {
        esUtil.delBatchDocument(index, list);
    }

    /**
     * 批量更新文档操作
     *
     * @param index 索引名称
     * @param list  文档列表
     */
    public void updateBatchDocument(String index, List<Map<String, Object>> list) {
        esUtil.updateBatchDocument(index, list);
    }


}
