package com.fukun.consumer.config.swagger;

import com.fukun.commons.enums.EnvironmentEnum;
import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * swagger相关的配置
 *
 * @author tangyifei
 * @since 2019-5-24 14:36:28
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Autowired
    private Environment env;

    @Bean
    public Docket demo1ApiDocket() {
        return new Docket(DocumentationType.SWAGGER_12)
                .enable(!EnvironmentEnum.isProdEnv(env))
                .groupName("RABBITMQ_CONSUMER")
                .apiInfo(new ApiInfoBuilder().title("RABBITMQ_CONSUMER").description("消息消费方（消费端）").build())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.fukun.consumer.web"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("富坤集团API")
                .description("实现可靠消息最终一致性")
                .version("1.0")
                .build();
    }
}
