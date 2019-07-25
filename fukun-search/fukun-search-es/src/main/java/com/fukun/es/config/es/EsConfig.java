package com.fukun.es.config.es;

import lombok.Setter;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    private String host;

    private Integer port;

    private String http;

    @Bean("client")
    public RestHighLevelClient getTransportClient() {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(host, port, http)
                        //这里如果要用client去访问其他节点，就添加进去
                        //new HttpHost("localhost", 9200, http)
                )
        );
        return client;
    }

}
