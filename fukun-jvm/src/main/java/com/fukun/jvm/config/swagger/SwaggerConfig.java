package com.fukun.jvm.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    @Bean
    public Docket demo1ApiDocket() {
        return new Docket(DocumentationType.SWAGGER_12)
//                .enable(!EnvironmentEnum.isProdEnv(env))
                .groupName("JVM")
                .apiInfo(new ApiInfoBuilder().title("JVM").description("java虚拟机的相关的性能测试").build())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.fukun.jvm.web"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("富坤集团API")
                .description("实现jvm的相关的性能测试")
                .version("1.0")
                .build();
    }
}
