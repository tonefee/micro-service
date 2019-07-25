package com.fukun.es.config.swagger;

import com.fukun.commons.enums.EnvironmentEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.Resource;

/**
 * swagger相关的配置
 *
 * @author tangyifei
 * @since 2019-5-24 14:36:28
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Resource
    private Environment env;

    @Bean
    public Docket demo1ApiDocket() {
        return new Docket(DocumentationType.SWAGGER_12)
                .enable(!EnvironmentEnum.isProdEnv(env))
                .groupName("ES_PRODUCER")
                .apiInfo(new ApiInfoBuilder().title("ES_PRODUCER").description("数据库数据同步到es").build())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.fukun.es.web"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("富坤集团API")
                .description("数据同步到es并创建索引便于搜索")
                .version("1.0")
                .build();
    }
}
