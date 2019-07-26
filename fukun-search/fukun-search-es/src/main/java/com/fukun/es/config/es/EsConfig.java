package com.fukun.es.config.es;

import com.google.common.collect.Lists;
import lombok.Setter;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.StringTokenizer;

/**
 * 添加es的配置
 *
 * @author tangyifei
 * @date 2019年7月24日16:28:30
 */
@Configuration
@ConfigurationProperties(prefix = "es")
@Setter
public class EsConfig {

    private static ArrayList<HttpHost> hostList = null;

    private String hosts;

    private Integer port;

    private String schema;

    private Integer connectTimeOut;

    private Integer socketTimeOut;

    private Integer connectionRequestTimeOut;

    private Integer maxConnectNum;

    private Integer maxConnectPerRoute;

    @PostConstruct
    public void setEsLinkInfo() {
        StringTokenizer st = new StringTokenizer(hosts, ",");
        LinkedList<String> list = Lists.newLinkedList();
        while (st.hasMoreElements()) {
            list.add(st.nextToken());
        }
        int size = list.size();
        hostList = new ArrayList<>(size);
        for(String hostStr : list) {
            hostList.add(new HttpHost(hostStr, port, schema));
        }
    }

    @Bean("client")
    public RestHighLevelClient getTransportClient() {
        RestClientBuilder builder = RestClient.builder(hostList.toArray(new HttpHost[0]));
        // 异步httpclient连接延时配置
        builder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(connectTimeOut);
            requestConfigBuilder.setSocketTimeout(socketTimeOut);
            requestConfigBuilder.setConnectionRequestTimeout(connectionRequestTimeOut);
            return requestConfigBuilder;
        });
        // 异步httpclient连接数配置
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setMaxConnTotal(maxConnectNum);
            httpClientBuilder.setMaxConnPerRoute(maxConnectPerRoute);
            return httpClientBuilder;
        });
        RestHighLevelClient client = new RestHighLevelClient(builder);
        return client;
//        RestHighLevelClient client = new RestHighLevelClient(
//                RestClient.builder(
//                        new HttpHost(host, port, schema)
//                        //这里如果要用client去访问其他节点，就添加进去
//                        //new HttpHost("localhost", 9200, http)
//                )
//        );
//        return client;
    }

}
