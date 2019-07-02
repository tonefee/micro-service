package com.fukun.order.config.web;

import com.fukun.commons.web.interceptor.ResponseResultInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置
 * <p>
 * 对于spring-boot2.0来说，如果继承WebMvcConfigurationSupport之后，Spring Boot有关MVC的自动配置就不生效了,除非你自己对代码把控的相当的好，在继承类中重写了一系列有关WebMVC的配置，否则可能就会遇到静态资源访问不到，返回数据不成功这些一系列问题了<br/>
 * Spring Boot2.0是基于Java8的，Java8有个重大的改变就是接口中可以有default方法，而default方法是不需要强制实现的。上述的WebMvcConfigurerAdapter类就是实现了WebMvcConfigurer这个接口，所以我们不需要继承WebMvcConfigurerAdapter类，可以直接实现WebMvcConfigurer接口，用法与继承这个适配类是一样的<br/>
 * 区别就是继承 WebMvcConfigurationSupport 会使Spring Boot关于WebMVC的自动配置失效，需要自己去实现全部关于WebMVC的配置，而实现WebMvcConfigurer接口的话，Spring Boot的自动配置不会失效，可以有选择的实现关于WebMVC的配置。
 * </p>
 *
 * @author tangyifei
 * @since 2019-5-24 14:50:45
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    /**
     * 注册接口响应体控制拦截器相关的bean
     *
     * @return 接口响应体控制拦截器对象
     */
    @Bean
    public ResponseResultInterceptor responseResultInterceptor() {
        return new ResponseResultInterceptor();
    }

    /**
     * 注册拦截器
     *
     * @param registry 拦截器注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //响应结果控制拦截
        registry.addInterceptor(responseResultInterceptor());
    }

    /**
     * 静态资源映射
     * 如果继承WebMvcConfigurationSupport，那么就不要在application.propertie文件中加入spring.mvc.static-path-pattern = /sh/static/**
     * 添加配置来实现静态资源的映射
     *
     * @param registry 资源映射注册器
     */
//    @Override
//    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/sh/static/**").addResourceLocations("classpath:/static/");
//        super.addResourceHandlers(registry);
//    }

}
