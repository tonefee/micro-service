package com.fukun.commons.util.convert;

import java.lang.annotation.Annotation;

/**
 * 对象属性转换上层接口
 *
 * @author tangyifei
 * @since 2019年5月22日16:52:48 PM
 */
public interface BeanFieldConverter<A extends Annotation, T> {

    void initialize(A ann);

    boolean isNeedConvert(T field);

    T convert(T field);
}
