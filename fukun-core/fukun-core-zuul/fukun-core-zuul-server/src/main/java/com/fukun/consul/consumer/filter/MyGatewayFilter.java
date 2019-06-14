package com.fukun.consul.consumer.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 网关过滤器，对请求进行拦截与过滤
 *
 * @author tangyifei
 * @since 2019-6-13 15:14:27
 * @since jdk1.8
 */
@Slf4j
@Component
public class MyGatewayFilter extends ZuulFilter implements FallbackProvider {

    /**
     * 当我们的某一个服务崩溃时，网关要负责进行回调
     * ServiceId，如果需要所有调用都支持回退，则 return "*" 或 return null，
     * 单个支持回调只需要加入application.name 例如 return "consul-service-producer"
     *
     * @return 回调类型
     */
    @Override
    public String getRoute() {
        return "*";
    }

    /**
     * 如果请求服务失败，则返回指定的信息给调用者
     *
     * @param route 路由
     * @param cause 异常
     * @return
     */
    @Override
    @SneakyThrows
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {

        return new ClientHttpResponse() {

            /**
             * 网关向 api 服务请求失败了，但是消费者客户端向网关发起的请求是成功的，
             * 不应该把 api 的 404,500 等问题抛给客户端
             * 网关和 api 服务集群对于客户端来说是黑盒
             * @return 正常的http状态
             * @throws IOException IO异常
             */
            @Override
            public HttpStatus getStatusCode() {
                return HttpStatus.OK;
            }

            @Override
            public int getRawStatusCode() {
                return HttpStatus.OK.value();
            }

            @Override
            public String getStatusText() {
                return HttpStatus.OK.getReasonPhrase();
            }

            @Override
            public void close() {

            }

            @Override
            public InputStream getBody() throws IOException {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> map = new HashMap<>(1 << 2);
                map.put("status", 200);
                map.put("message", "无法连接，请检查您的网络");
                return new ByteArrayInputStream(objectMapper.writeValueAsString(map).getBytes("UTF-8"));
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                // 和 getBody 中的内容编码一致
                headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
                return headers;
            }
        };
    }

    /**
     * 4种过滤器类型,
     * 　　pre；可以在请求被路由之前调用,
     * 　　route：在路由请求时候被调用,
     * 　　post：在route和error过滤器之后被调用,
     * 　　error：处理请求时发生错误时被调用
     *
     * @return 过滤器类型
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 优先级为0，数字越大，优先级越低
     *
     * @return 优先级
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 是否执行该过滤器，此处为true，说明需要过滤
     *
     * @return 是否应当过滤
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤相关的业务代码
     *
     * @return 过滤后的响应
     */
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        log.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));
        // 获取token参数
        Object accessToken = request.getParameter("token");
        if (accessToken == null) {
            log.warn("token is empty");
            // 过滤该请求, 不对其进行路由
            ctx.setSendZuulResponse(false);
            // 返回错误码
            ctx.setResponseStatusCode(401);
            // 返回错误内容
            ctx.setResponseBody("token is null!");
            // Zuul还未对返回数据做处理
            return null;
        }
        log.info("ok");
        return null;
    }
}
