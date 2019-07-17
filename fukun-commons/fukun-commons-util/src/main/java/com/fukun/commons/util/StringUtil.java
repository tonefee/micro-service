package com.fukun.commons.util;

/**
 * 字符串操作工具类
 *
 * @author tangyifei
 * @since 2019-5-22 16:33:32 PM
 */
public class StringUtil {

    /**
     * 判断传入的字符串是否为空串
     */
    public static boolean isEmpty(String str) {
        return str == null || ("".equals(str.trim()));
    }

    /**
     * 判断传入的字符串是否不为空串
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 判断传入的是否存在空字符串
     */
    public static boolean isAnyBlank(CharSequence... css) {
        if (css == null || css.length == 0) {
            return true;
        }

        for(CharSequence cs : css) {
            if (isBlank(cs)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for(int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * 子字符串出现的个数
     */
    public static int getSubStrCount(String str, String subStr) {
        int count = 0;
        int index = 0;
        while ((index = str.indexOf(subStr, index)) != -1) {
            index = index + subStr.length();
            count++;
        }
        return count;
    }


    /**
     * 替换字符串
     */
    public static String replace(String inString, String oldPattern, String newPattern) {
        if (isNotEmpty(inString) && isNotEmpty(oldPattern) && newPattern != null) {
            int index = inString.indexOf(oldPattern);
            if (index == -1) {
                return inString;
            } else {
                int capacity = inString.length();
                if (newPattern.length() > oldPattern.length()) {
                    capacity += 16;
                }

                StringBuilder sb = new StringBuilder(capacity);
                int pos = 0;

                for(int patLen = oldPattern.length(); index >= 0; index = inString.indexOf(oldPattern, pos)) {
                    sb.append(inString.substring(pos, index));
                    sb.append(newPattern);
                    pos = index + patLen;
                }

                sb.append(inString.substring(pos));
                return sb.toString();
            }
        } else {
            return inString;
        }
    }

    /**
     * 格式化字符串（替换符为%s）
     */
    public static String formatIfArgs(String format, Object... args) {
        if (isEmpty(format)) {
            return format;
        }

        return (args == null || args.length == 0) ? String.format(format.replaceAll("%([^n])", "%%$1")) : String.format(format, args);
    }

    /**
     * 格式化字符串(替换符自己指定)
     */
    public static String formatIfArgs(String format, String replaceOperator, Object... args) {
        if (isEmpty(format) || isEmpty(replaceOperator)) {
            return format;
        }

        format = replace(format, replaceOperator, "%s");
        return formatIfArgs(format, args);
    }

    /**
     * 在循环中截取字符串
     * 如果在循环中截取字符串，最好创建一个新的字符串对象，这样String对象的默认substring截取字符串函数中的包级别的私有构造函数截取的字符串对象没有被引用，就有可能被垃圾回收，节省内存空间
     * 如果在循环中不这样做的话，可能会导致OOM的发生
     * 如果不在循环中截取字符串，那么就使用String对象的substring方法，因为一旦方法执行完毕，该对象就没有地方被引用，当GCf发生后，就会被回收
     */
    public static String subStr(String sourceStr, int beginIndex, int endIndex) {
        if (isNotEmpty(sourceStr)) {
            return new String(sourceStr.substring(beginIndex, endIndex));
        }
        return null;
    }

}
