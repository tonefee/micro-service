package com.fukun.commons.util.annotations;

import com.fukun.commons.util.convert.BeanFieldConverter;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.*;

/**
 * 字符串去两边空格
 *
 * @author tangyifei
 * @since 2019-5-22 16:51:47 PM
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Trim {

    /**
     * 转换器
     */
    class Converter implements BeanFieldConverter<Trim, String> {

        @Override
        public void initialize(Trim ann) {

        }

        @Override
        public boolean isNeedConvert(String field) {
            if (StringUtils.isEmpty(field)) {
                return false;
            }
            return field.startsWith(" ") || field.endsWith(" ");
        }

        @Override
        public String convert(String field) {
            return field.trim();
        }
    }

}
