package com.fukun;

import com.fukun.commons.util.JsonUtil;
import com.fukun.es.DataSynToEsApplication;
import com.fukun.es.model.News;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.*;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ES操作相关的单元测试
 * 温馨提示：下面的单元测试请从上往下按照顺序执行
 *
 * @author tangyifei
 * @date 2019年7月27日10:45:14
 */
@SpringBootTest(classes = {DataSynToEsApplication.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class EsTests {

    private static final String INDEX = "news";

    @Resource
    private RestHighLevelClient rhlClient;

    @Resource
    private RestClient restClient;

    // 温馨提示：下面的单元测试请从上往下按照顺序执行

    // ********************************** 对索引的相关操作开始 ******************************

    /**
     * 添加索引
     *
     * @throws Exception 可能抛出的异常
     */
    @Test
    public void addIndex() throws Exception {
        Request request = new Request("PUT", INDEX);
        Response response = restClient.performRequest(request);
        System.out.println(response);
    }

    /**
     * 检查索引是否存在
     *
     * @throws Exception 可能抛出的异常
     */
    @Test
    public void checkIndexExist() throws Exception {
        Request request = new Request("HEAD", INDEX);
        Response response = restClient.performRequest(request);
        Assert.assertEquals("OK", response.getStatusLine().getReasonPhrase());
    }

    /**
     * 为索引创建映射结构
     *
     * @throws Exception 可能抛出的异常
     */
    @Test
    public void createMappingTest() throws Exception {
        // 借助indexRequest的json拼接工具
        IndexRequest indexRequest = new IndexRequest();
        XContentBuilder builder = JsonXContent.contentBuilder()
                .startObject()

                .startObject("properties")

                .startObject("id")
                .field("type", "keyword")
                .endObject()

                .startObject("title")
                .field("type", "text")
                .field("analyzer", "ik_max_word")
                .endObject()

                .startObject("tag")
                .field("type", "keyword")
                .endObject()

                .startObject("publishTime")
                .field("type", "date")
                .endObject()

                .endObject()

                .endObject();
        indexRequest.source(builder);
        // 生成json字符串
        String source = indexRequest.source().utf8ToString();
        HttpEntity entity = new NStringEntity(source, ContentType.APPLICATION_JSON);
        // 使用RestClient进行操作 而非rhlClient
        Request request = new Request("PUT", "/" + INDEX + "/_mapping");
        request.setEntity(entity);
        Response response = restClient.performRequest(request);
        System.out.println(response);
    }

    /**
     * 删除索引
     *
     * @throws Exception 可能抛出的异常
     */
    @Test
    public void deleteIndex() throws Exception {
        Request request = new Request("DELETE", INDEX);
        Response response = restClient.performRequest(request);
        System.out.println(response);
    }

    // 由于使用RestHighLevelClient（后称为rhlClient）时，进行Index操作，所有IndexRequest都会校验Index，type，source，contentType不为空。
    // 通过restClient绕过rhlClient的空类型检测。所以如果还有其他对索引的需求，也可以尝试使用IndexRequest的Json拼接工具，拼接好Json字符串后，使用restClient发出请求

    // ********************************** 对索引的相关操作结束 ******************************

    // ********************************** 对索引中的文档的相关操作开始 ******************************

    /**
     * 单个创建索引
     *
     * @throws Exception 可能抛出的异常
     */
    @Test
    public void addTest() throws Exception {
        String id = "6";
        IndexRequest indexRequest = new IndexRequest(INDEX);
        indexRequest.id(id);
        News news = new News();
        news.setId(id);
        news.setTitle("江苏南京进入高温模式，局部气温36度");
        news.setTag("气象");
        news.setPublishTime("2019-07-27T23:59:30Z");
        String source = JsonUtil.object2Json(news);
        indexRequest.source(source, XContentType.JSON);
        rhlClient.index(indexRequest, RequestOptions.DEFAULT);
    }

    /**
     * 向索引中批量插入文档数据
     *
     * @throws Exception 可能抛出的异常
     */
    @Test
    public void batchAddTest() throws Exception {
        BulkRequest bulkRequest = new BulkRequest();
        List<IndexRequest> requests = generateRequests();
        for(IndexRequest indexRequest : requests) {
            bulkRequest.add(indexRequest);
        }
        rhlClient.bulk(bulkRequest, RequestOptions.DEFAULT);
    }

    /**
     * 更新文档
     *
     * @throws Exception 可能抛出的异常
     */
    @Test
    public void updateTest() throws Exception {
        UpdateRequest updateRequest = new UpdateRequest(INDEX, "1");
        Map<String, Object> map = new HashMap<>(1 << 1);
        map.put("tag", "军事论坛");
        updateRequest.doc(map);
        rhlClient.update(updateRequest, RequestOptions.DEFAULT);
    }

    /**
     * 查询目标：2018年1月26日早八点到晚八点关于费德勒的前十条体育新闻的标题
     *
     * @throws Exception 可能抛出的异常
     */
    @Test
    public void queryTest() throws Exception {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(0);
        sourceBuilder.size(10);
        sourceBuilder.fetchSource(new String[]{"title"}, new String[]{});
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("title", "费德勒");
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("tag", "体育");
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("publishTime");
        rangeQueryBuilder.gte("2018-01-26T08:00:00Z");
        rangeQueryBuilder.lte("2018-01-26T20:00:00Z");
        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        boolBuilder.must(matchQueryBuilder);
        boolBuilder.must(termQueryBuilder);
        boolBuilder.must(rangeQueryBuilder);
        sourceBuilder.query(boolBuilder);
        SearchRequest searchRequest = new SearchRequest(INDEX);
        //searchRequest.types(type);
        searchRequest.source(sourceBuilder);
        SearchResponse response = rhlClient.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(response);
    }

    /**
     * 根据文档的id删除文档
     *
     * @throws Exception 可能抛出的异常
     */
    @Test
    public void delete() throws Exception {
        DeleteRequest deleteRequest = new DeleteRequest(INDEX, "1");
        DeleteResponse response = rhlClient.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println(response);
    }

    /**
     * 删除title包含费德勒的新闻
     * 使用 RestHighLevelClient 根据查询条件删除文档，先查询出满足条件的文档，再根据文档ID依次删除，即发送了两次请求。
     * 就删除操作而言，RestHighLevelClient所能做的还不够完善，因此要联系RestClient的灵活性才能实现我们想要的功能。
     *
     * @throws Exception 可能抛出的异常
     */
//    @Test
//    public void deleteByQueryWithRestHighLevelClient() throws Exception {
//        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//        sourceBuilder.timeout(new TimeValue(2, TimeUnit.SECONDS));
//        TermQueryBuilder termQueryBuilder1 = QueryBuilders.termQuery("title.keyword", "费德勒");
//        sourceBuilder.query(termQueryBuilder1);
//        SearchRequest searchRequest = new SearchRequest(INDEX);
//        searchRequest.source(sourceBuilder);
//        SearchResponse response = rhlClient.search(searchRequest, RequestOptions.DEFAULT);
//        SearchHits hits = response.getHits();
//        // 先根据条件查询出符合查询条件的文档id
//        List<String> docIds = new ArrayList<>(hits.getHits().length);
//        for(SearchHit hit : hits) {
//            docIds.add(hit.getId());
//        }
//
//        BulkRequest bulkRequest = new BulkRequest();
//        // 再根据查询条件查询出的文档id来删除文档
//        for(String id : docIds) {
//            DeleteRequest deleteRequest = new DeleteRequest(INDEX, id);
//            bulkRequest.add(deleteRequest);
//        }
//        rhlClient.bulk(bulkRequest, RequestOptions.DEFAULT);
//    }

    /**
     * 删除 tag 包含体育的新闻
     * 使用 RestClient 直接以http的形式调用ES服务执行根据条件执行删除操作。
     * 优点就在于不需要发起两次HTTP连接，节省时间
     *
     * @throws Exception 可能抛出的异常
     */
    @Test
    public void deleteByQueryWithRestClient() throws Exception {
        String endPoint = "/" + INDEX + "/_delete_by_query";
        String source = generateQueryString();
        HttpEntity entity = new NStringEntity(source, ContentType.APPLICATION_JSON);
        Request request = new Request("POST", endPoint);
        request.setEntity(entity);
        restClient.performRequest(request);
    }

    private String generateQueryString() throws Exception {
        IndexRequest indexRequest = new IndexRequest();
        XContentBuilder builder;
        builder = JsonXContent.contentBuilder()
                .startObject()
                  .startObject("query")
                     .startObject("bool")
                            .startArray("must")
                              .startObject()
                                .startObject("term")
                                    .startObject("tag")
                                    .field("value", "体育")
                                    .endObject()
                                .endObject()
                              .endObject()
                            .endArray()
                     .endObject()
                  .endObject()
                .endObject();
        indexRequest.source(builder);
        String source = indexRequest.source().utf8ToString();
        return source;
    }

    private List<IndexRequest> generateRequests() {
        List<IndexRequest> requests = new ArrayList<>(1 << 2);
        requests.add(generateNewsRequest("1", "中印边防军于拉达克举行会晤 强调维护边境和平", "军事", "2018-01-27T08:34:00Z"));
        requests.add(generateNewsRequest("2", "费德勒收郑泫退赛礼 进决赛战西里奇", "体育", "2018-01-26T14:34:00Z"));
        requests.add(generateNewsRequest("3", "欧文否认拿动手术威胁骑士 兴奋全明星联手詹皇", "体育", "2018-01-26T08:34:00Z"));
        requests.add(generateNewsRequest("4", "皇马官方通告拉莫斯伊斯科伤情 将缺阵西甲关键战", "体育", "2018-01-26T20:34:00Z"));
        return requests;
    }

    private IndexRequest generateNewsRequest(String id, String title, String tag, String publishTime) {
        IndexRequest indexRequest = new IndexRequest(INDEX);
        indexRequest.id(id);
        News news = new News();
        news.setId(id);
        news.setTitle(title);
        news.setTag(tag);
        news.setPublishTime(publishTime);
        String source = JsonUtil.object2Json(news);
        indexRequest.source(source, XContentType.JSON);
        return indexRequest;
    }

    // ********************************** 对索引中的文档的相关操作结束 ******************************


}
