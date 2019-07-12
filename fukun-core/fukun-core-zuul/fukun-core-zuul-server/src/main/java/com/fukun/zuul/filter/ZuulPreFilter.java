package com.fukun.zuul.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import io.micrometer.core.instrument.util.IOUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
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
public class ZuulPreFilter extends ZuulFilter implements FallbackProvider {

    private static final String GET_METHOD = "GET";

    /**
     * 熔断拦截哪个服务，告诉Zuul它是负责哪个route定义的熔断。
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
     * 定制返回内容
     * 而fallbackResponse方法则是告诉 Zuul 断路出现时，它会提供一个什么返回值来处理请求。
     * 如果请求服务失败，则返回指定的信息给调用者
     * 注意：Zuul 目前只支持服务级别的熔断，不支持具体到某个URL进行熔断。
     *
     * @param route 路由
     * @param cause 异常
     * @return
     */
    @Override
    @SneakyThrows
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {

        // 当服务出现异常时，打印异常信息，用于定位错误，并返回”The service is unavailable.”。
        // 分别开启fukun-core-consul-producer1和fukun-core-consul-producer2，使用zuul访问时，
        // 关闭某一台服务，比如fukun-core-consul-producer2，
        // 这时会交替出现hello consul1和The service is unavailable.
        // 这就说明已经启用了熔断机制
        if (cause != null && cause.getCause() != null) {
            String reason = cause.getCause().getMessage();
            if (log.isInfoEnabled()) {
                log.info("Exception {}", reason);
            }
        }

        /**
         * 当我们的后端服务出现异常的时候，我们不希望将异常抛出给最外层，
         * 我们不希望将异常抛出给最外层，期望服务可以自动进行一降级。
         * Zuul给我们提供了这样的支持。当某个服务出现异常时，直接返回我们预设的信息。
         */
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
     * 　　pre；可以在请求被路由之前调用，可利用这种过滤器实现身份验证、在集群中选择请求的微服务、记录调试信息等。
     * 　　routing：在路由请求时候被调用，这种过滤器将请求路由到微服务。这种过滤器用于构建发送给微服务的请求，并使用Apache HttpClient或Netfilx Ribbon请求微服务。
     * 　　post：在route和error过滤器之后被调用，这种过滤器在路由到微服务以后执行。这种过滤器可用来为响应添加标准的HTTP Header、收集统计信息和指标、将响应从微服务发送给客户端等。
     * 　　error：处理请求时发生错误时被调用，在其他阶段发生错误时执行该过滤器。
     *
     * @return 过滤器类型
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 优先级为0，数字越大，优先级越低，越后执行
     *
     * @return 优先级
     */
    @Override
    public int filterOrder() {
        return 0;
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
     * 过滤相关的业务代码，filter需要执行的具体操作
     * 在实际使用中我们可以结合shiro、oauth2.0等技术去做鉴权、验证。
     *
     * @return 过滤后的响应
     */
    @Override
    public Object run() {
        //Zull获取请求信息
        final RequestContext ctx = RequestContext.getCurrentContext();
        // 开启zuul的调试模式
        ctx.setDebugRouting(true);
        ctx.setDebugRequest(true);
        HttpServletRequest request = ctx.getRequest();
        if (log.isInfoEnabled()) {
            log.info("REQUEST:: {} {}:{}", request.getScheme(), request.getRemoteAddr(), request.getRemotePort());
        }
        StringBuilder params = new StringBuilder("?");
        // 获取URL参数
        Enumeration<String> names = request.getParameterNames();
        if (request.getMethod().equals(GET_METHOD)) {
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                params.append(name);
                params.append("=");
                params.append(request.getParameter(name));
                params.append("&");
            }
        }

        if (params.length() > 0) {
            params.delete(params.length() - 1, params.length());
        }

        if (log.isInfoEnabled()) {
            log.info("REQUEST:: > {} {} {} {}", request.getMethod(), request.getRequestURL(), params, request.getProtocol());
        }

        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String name = headers.nextElement();
            String value = request.getHeader(name);
            if (log.isInfoEnabled()) {
                log.info("REQUEST:: > {}:{}", name, value);
            }
        }

        // 获取请求体参数
        if (!ctx.isChunkedRequestBody()) {
            ServletInputStream inp;
            try {
                inp = ctx.getRequest().getInputStream();
                String body;
                if (null != inp) {
                    body = IOUtils.toString(inp);
                    if (log.isInfoEnabled()) {
                        log.info("REQUEST:: > {}", body);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (log.isInfoEnabled()) {
                    log.error("出现IO异常！");
                }
            }
        }

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
            return Optional.empty();
        }
        log.info("ok");
        return Optional.empty();
    }
}
