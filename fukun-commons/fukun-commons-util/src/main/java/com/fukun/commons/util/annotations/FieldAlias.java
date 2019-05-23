package com.fukun.commons.util.annotations;


import java.lang.annotation.*;

/**
 * 别名注解 用来为类的字段添加别名（备注：可重复注解，也可以为一个别名指定多个源类）
 *
 * @author tangyifei
 * @since 2019-5-22 16:46:28 PM
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(FieldAlias.FieldAliases.class)
public @interface FieldAlias {

    String value();

    Class<?>[] sourceClass() default {};

    /**
     * 别名注解复数
     *
     * @author tangyifei
     * @since 2019-5-22 16:47:54 PM
     */
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface FieldAliases {

        FieldAlias[] value();

    }
}
