package com.fukun.commons.util.annotations;

import java.lang.annotation.*;

/**
 * 标记在对象/集合属性上
 *
 * @author tangyifei
 * @since 2019-5-22 16:45:35 PM
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Convert {

}