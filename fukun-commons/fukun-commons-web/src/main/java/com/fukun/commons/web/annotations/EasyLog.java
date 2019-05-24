package com.fukun.commons.web.annotations;

import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.AsyncConfigurationSelector;

import java.lang.annotation.*;

/**
 * 日志打印
 *
 * @author tangyifei
 * @since 2019-5-23 16:56:18 PM
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({AsyncConfigurationSelector.class})
public @interface EasyLog {

    /**
     * 是否关闭日志输出功能
     */
    boolean enable() default true;

    /**
     * 是否输出执行时间
     */
    boolean duration() default true;

    /**
     * 是否只在debug模式下输出日志
     */
    boolean response() default true;
}
