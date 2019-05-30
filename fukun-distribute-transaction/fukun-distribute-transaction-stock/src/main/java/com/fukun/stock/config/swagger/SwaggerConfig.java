package com.fukun.stock.config.swagger;

import com.fukun.commons.enums.EnvironmentEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
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
                .groupName("stock")
                .apiInfo(new ApiInfoBuilder().title("stock").description("分布式事务库存模块").build())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.fukun.stock.web"))
                .paths(PathSelectors.any())
                .build();
    }

}
