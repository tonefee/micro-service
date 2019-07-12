package com.fukun.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import io.micrometer.core.instrument.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Optional;

/**
 * 自定义过滤器，需要继承ZuulFilter的类，并覆盖其中的4个方法
 * 比如：我们可以定制一种STATIC类型的过滤器，直接在Zuul中生成响应，
 * 而不将请求转发到后端的微服务。
 * 网关过滤器，对请求进行拦截与过滤
 *
 * @author tangyifei
 * @since 2019-6-13 15:14:27
 * @since jdk1.8
 */
@Slf4j
@Component
public class ZuulPostFilter extends ZuulFilter {

    /**
     * 4种过滤器类型,
     * 　　pre；可以在请求被路由之前调用，可利用这种过滤器实现身份验证、在集群中选择请求的微服务、记录调试信息等。
     * 　　routing：在路由请求时候被调用，这种过滤器将请求路由到微服务。这种过滤器用于构建发送给微服务的请求，并使用Apache HttpClient或Netfilx Ribbon请求微服务。
     * 　　post：在route和error过滤器之后被调用，这种过滤器在路由到微服务以后执行。这种过滤器可用来为响应添加标准的HTTP Header、收集统计信息和指标、将响应从微服务发送给客户端等。
     * 　　error：处理请求时发生错误时被调用，在其他阶段发生错误时执行该过滤器。
     *
     * @return 过滤器类型
     */
    @Override
    public String filterType() {
        return "post";
    }

    /**
     * 优先级为1，数字越大，优先级越低，越后执行
     *
     * @return 优先级
     */
    @Override
    public int filterOrder() {
        return 1;
    }

    /**
     * 是否执行该过滤器，此处为true，说明需要过滤并执行，为false，表示不执行
     *
     * @return 是否应当过滤
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤微服务端的响应内容
     *
     * @return 过滤后的响应
     */
    @Override
    public Object run() {
        InputStream stream = RequestContext.getCurrentContext().getResponseDataStream();
        if (null != stream) {
            String body = IOUtils.toString(stream);
            if (log.isInfoEnabled()) {
                log.info("RESPONSE:: > {}", body);
            }
            RequestContext.getCurrentContext().setResponseBody(body);
        }
        return Optional.empty();
    }
}
