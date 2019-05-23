package com.fukun.commons.util.annotations;

import com.fukun.commons.util.AsciiUtils;
import com.fukun.commons.util.convert.BeanFieldConverter;

import java.lang.annotation.*;

/**
 * 半角替全角
 *
 * @author tangyifei
 * @since 2019-5-22 16:49:38 PM
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FullToHalf {

    /**
     * 转换器
     */
    class Converter implements BeanFieldConverter<FullToHalf, String> {

        @Override
        public void initialize(FullToHalf ann) {

        }

        @Override
        public boolean isNeedConvert(String field) {
            return AsciiUtils.existFullChar(field);
        }

        @Override
        public String convert(String field) {
            return AsciiUtils.full2Half(field);
        }
    }
}
