package com.fukun.es.config.es;

import com.fukun.es.factory.ESClientSpringFactory;
import com.google.common.collect.Lists;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.StringTokenizer;

/**
 * 添加es的配置
 *
 * @author tangyifei
 * @date 2019年7月26日17:12:09
 */
@Configuration
@ComponentScan(basePackageClasses = ESClientSpringFactory.class)
public class ElasticSearchConfig {

    @Value("${es.hosts}")
    private String hosts;

    @Value("${es.port}")
    private int port;

    @Value("${es.schema}")
    private String schema;

    @Value("${es.connectTimeOut}")
    private int connectTimeOut;

    @Value("${es.socketTimeOut}")
    private int socketTimeOut;

    @Value("${es.connectionRequestTimeOut}")
    private int connectionRequestTimeOut;

    @Value("${es.maxConnectPerRoute}")
    private int maxConnectPerRoute;

    @Value("${es.maxConnectNum}")
    private int maxConnectNum;

    @Bean
    public ArrayList<HttpHost> httpHost() {
        StringTokenizer st = new StringTokenizer(hosts, ",");
        LinkedList<String> list = Lists.newLinkedList();
        while (st.hasMoreElements()) {
            list.add(st.nextToken());
        }
        int size = list.size();
        ArrayList<HttpHost> hostList = new ArrayList<>(size);
        for(String hostStr : list) {
            hostList.add(new HttpHost(hostStr, port, schema));
        }
        return hostList;
    }

    @Bean(initMethod = "init", destroyMethod = "close")
    public ESClientSpringFactory getFactory() {
        return ESClientSpringFactory.build(httpHost(), connectTimeOut, socketTimeOut,
                connectionRequestTimeOut, maxConnectNum, maxConnectPerRoute);
    }

    @Bean
    @Scope("singleton")
    public RestClient getRestClient() {
        return getFactory().getClient();
    }

    @Bean
    @Scope("singleton")
    public RestHighLevelClient getRHLClient() {
        return getFactory().getRhlClient();
    }
}
