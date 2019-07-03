package com.fukun.demo.config.swagger;

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
                .groupName("DEMO1")
                .apiInfo(new ApiInfoBuilder().title("DEMO1").description("通用Mapper和分页PageHelper的使用演示").build())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.fukun.demo.web.demo1"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket demo2ApiDocket() {
        return new Docket(DocumentationType.SWAGGER_12)
                .enable(!EnvironmentEnum.isProdEnv(env))
                .groupName("DEMO2")
                .apiInfo(new ApiInfoBuilder().title("DEMO2").description("Spring Boot项目通用功能代码示例演示").build())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.fukun.demo.web.demo2"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket demo3ApiDocket() {
        return new Docket(DocumentationType.SWAGGER_12)
                .enable(!EnvironmentEnum.isProdEnv(env))
                .groupName("DEMO3")
                .apiInfo(new ApiInfoBuilder().title("DEMO3").description("登录功能设计和如果利用Spring自定义参数解析器注入已登录用户信息等功能").build())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.fukun.demo.web.demo3"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket demo4ApiDocket() {
        return new Docket(DocumentationType.SWAGGER_12)
                .enable(!EnvironmentEnum.isProdEnv(env))
                .groupName("DEMO4")
                .apiInfo(new ApiInfoBuilder().title("DEMO4").description("登录功能设计和如果利用Spring自定义参数解析器注入已登录用户信息等功能").build())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.fukun.demo.web.demo4"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket demo5ApiDocket() {
        return new Docket(DocumentationType.SWAGGER_12)
                .enable(!EnvironmentEnum.isProdEnv(env))
                .groupName("DEMO5")
                .apiInfo(new ApiInfoBuilder().title("DEMO5").description("登录功能设计和如果利用Spring自定义参数解析器注入已登录用户信息等功能").build())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.fukun.demo.web.demo5"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket demo6ApiDocket() {
        return new Docket(DocumentationType.SWAGGER_12)
                .enable(!EnvironmentEnum.isProdEnv(env))
                .groupName("DEMO6")
                .apiInfo(new ApiInfoBuilder().title("DEMO6").description("登录功能设计和如果利用Spring自定义参数解析器注入已登录用户信息等功能").build())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.fukun.demo.web.demo6"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket demo7ApiDocket() {
        return new Docket(DocumentationType.SWAGGER_12)
                .enable(!EnvironmentEnum.isProdEnv(env))
                .groupName("DEMO7")
                .apiInfo(new ApiInfoBuilder().title("DEMO7").description("线程池的使用").build())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.fukun.demo.web.demo7"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("富坤集团API")
                .description("接口写得好，联调通的早")
                .version("1.0")
                .build();
    }
}
