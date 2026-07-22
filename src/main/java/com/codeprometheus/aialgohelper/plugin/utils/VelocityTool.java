package com.codeprometheus.aialgohelper.plugin.utils;

import com.github.promeg.pinyinhelper.Pinyin;
import com.codeprometheus.aialgohelper.plugin.model.CodeTypeEnum;
import com.codeprometheus.aialgohelper.plugin.model.Config;
import com.codeprometheus.aialgohelper.plugin.model.Constant;
import com.codeprometheus.aialgohelper.plugin.setting.PersistentConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

/**
 * Static tool class exposed to code templates as $velocityTool / $vt.<br>
 * 提供给代码模板的静态工具类（$velocityTool / $vt）。<br>
 * Historically this class extended StringUtils; the commonly used string helpers are
 * now exposed directly below so existing templates keep working.
 *
 * @author shuzijun
 */
public class VelocityTool {

    private static String[] numsAry = {"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};

    /**
     * Fill 0 on the left to reach a fixed length <br>
     * 在左侧填充0达到固定长度length
     *
     * @param s
     * @param length
     * @return
     */
    public static String leftPadZeros(String s, int length) {
        if (s.length() >= length) {
            return s;
        }
        int nPads = length - s.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nPads; ++i) {
            sb.append('0');
        }
        sb.append(s);
        return sb.toString();
    }

    /**
     * Convert characters to camel case (initial letter capitalized) <br>
     * 转换字符为驼峰样式（开头字母大写）
     *
     * @param underscoreName
     * @return
     */
    public static String camelCaseName(String underscoreName) {

        if (StringUtils.isNotBlank(underscoreName)) {
            underscoreName = underscoreName.replace(" ", "_");
            StringBuilder result = new StringBuilder();
            if (StringUtils.isNumeric(underscoreName.substring(0, 1))) {
                underscoreName = numsAry[Integer.valueOf(underscoreName.substring(0, 1))] + "-" + underscoreName.substring(1);
            }
            boolean first = true;
            boolean flag = false;
            for (int i = 0; i < underscoreName.length(); i++) {
                char ch = underscoreName.charAt(i);
                if ('_' == ch || '-' == ch) {
                    flag = true;
                } else {
                    if (flag || first) {
                        result.append(Character.toUpperCase(ch));
                        flag = false;
                        first = false;
                    } else {
                        result.append(ch);
                    }
                }
            }
            return result.toString();
        } else {
            return underscoreName;
        }
    }

    /**
     * Convert characters to camel case (lower case at the beginning) <br>
     * 转换字符为小驼峰样式（开头字母小写）
     *
     * @param underscoreName
     * @return
     */
    public static String smallCamelCaseName(String underscoreName) {

        if (StringUtils.isNotBlank(underscoreName)) {
            underscoreName = underscoreName.replace(" ", "_");
            StringBuilder result = new StringBuilder();
            if (StringUtils.isNumeric(underscoreName.substring(0, 1))) {
                underscoreName = numsAry[Integer.valueOf(underscoreName.substring(0, 1))] + "-" + underscoreName.substring(1);
            }
            boolean first = false;
            boolean flag = false;
            for (int i = 0; i < underscoreName.length(); i++) {
                char ch = underscoreName.charAt(i);
                if ('_' == ch || '-' == ch) {
                    flag = true;
                } else {
                    if (flag || first) {
                        result.append(Character.toUpperCase(ch));
                        flag = false;
                        first = false;
                    } else {
                        result.append(ch);
                    }
                }
            }
            return result.toString();
        } else {
            return underscoreName;
        }
    }

    /**
     * Convert characters to snake style <br>
     * 转换字符为蛇形样式
     *
     * @param underscoreName
     * @return
     */

    public static String snakeCaseName(String underscoreName) {

        if (StringUtils.isNotBlank(underscoreName)) {
            underscoreName = underscoreName.replace(" ", "_");
            StringBuilder result = new StringBuilder();
            for (int i = 0, j = underscoreName.length(); i < j; i++) {
                char ch = underscoreName.charAt(i);
                if ('_' == ch || '-' == ch) {
                    if (i + 1 < j) {
                        result.append("_").append(Character.toLowerCase(underscoreName.charAt(i + 1)));
                        i = i + 1;
                    }
                } else if (Character.isUpperCase(ch)) {
                    result.append("_").append(Character.toLowerCase(underscoreName.charAt(i)));
                } else {
                    result.append(ch);
                }
            }
            return result.toString();
        } else {
            return underscoreName;
        }
    }

    /**
     * Get the current time. <br>
     * 获取当前时间
     *
     * @return
     */
    public static String date() {
        return date("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * Get the current time. <br>
     * 获取当前时间
     *
     * @return
     */
    public static String date(String format) {
        return DateFormatUtils.format(new Date(), format);
    }

    /**
     * Get start tag <br>
     * 获取开始标记
     *
     * @return
     */
    public static String SUBMIT_REGION_BEGIN() {
        Config config = PersistentConfig.getInstance().getInitConfig();
        String codeType = config.getCodeType();
        CodeTypeEnum codeTypeEnum = CodeTypeEnum.getCodeTypeEnum(codeType);
        return codeTypeEnum.getComment() + Constant.SUBMIT_REGION_BEGIN;
    }

    /**
     * Get eng tag <br>
     * 获取结束标记
     *
     * @return
     */
    public static String SUBMIT_REGION_END() {
        Config config = PersistentConfig.getInstance().getInitConfig();
        String codeType = config.getCodeType();
        CodeTypeEnum codeTypeEnum = CodeTypeEnum.getCodeTypeEnum(codeType);
        return codeTypeEnum.getComment() + Constant.SUBMIT_REGION_END;
    }

    /**
     * Convert Chinese characters to Pinyin and remove all spaces<br>
     * 将汉字转为为拼音并去除所有空格
     *
     * @param str
     * @return
     */
    public static String toPinyinAndTrims(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        str = str.replace(" ", "");
        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (Pinyin.isChinese(c)) {
                String pinYin = Pinyin.toPinyin(c);
                sb.append(camelCaseName(pinYin.toLowerCase()));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    // ------------------------------------------------------------------
    // String helpers kept for template compatibility. VelocityTool used to
    // extend StringUtils, so custom templates may call these directly.
    // ------------------------------------------------------------------

    public static boolean isBlank(CharSequence cs) {
        return StringUtils.isBlank(cs);
    }

    public static boolean isNotBlank(CharSequence cs) {
        return StringUtils.isNotBlank(cs);
    }

    public static boolean isEmpty(CharSequence cs) {
        return StringUtils.isEmpty(cs);
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return StringUtils.isNotEmpty(cs);
    }

    public static boolean isNumeric(CharSequence cs) {
        return StringUtils.isNumeric(cs);
    }

    public static boolean isAlpha(CharSequence cs) {
        return StringUtils.isAlpha(cs);
    }

    public static boolean isAlphanumeric(CharSequence cs) {
        return StringUtils.isAlphanumeric(cs);
    }

    public static String upperCase(String str) {
        return StringUtils.upperCase(str);
    }

    public static String lowerCase(String str) {
        return StringUtils.lowerCase(str);
    }

    public static String capitalize(String str) {
        return StringUtils.capitalize(str);
    }

    public static String uncapitalize(String str) {
        return StringUtils.uncapitalize(str);
    }

    public static String swapCase(String str) {
        return StringUtils.swapCase(str);
    }

    public static String trim(String str) {
        return StringUtils.trim(str);
    }

    public static String strip(String str) {
        return StringUtils.strip(str);
    }

    public static String deleteWhitespace(String str) {
        return StringUtils.deleteWhitespace(str);
    }

    public static String reverse(String str) {
        return StringUtils.reverse(str);
    }

    public static String repeat(String str, int repeat) {
        return StringUtils.repeat(str, repeat);
    }

    public static String abbreviate(String str, int maxWidth) {
        return StringUtils.abbreviate(str, maxWidth);
    }

    public static String defaultString(String str) {
        return StringUtils.defaultString(str);
    }

    public static String leftPad(String str, int size) {
        return StringUtils.leftPad(str, size);
    }

    public static String leftPad(String str, int size, String padStr) {
        return StringUtils.leftPad(str, size, padStr);
    }

    public static String rightPad(String str, int size) {
        return StringUtils.rightPad(str, size);
    }

    public static String rightPad(String str, int size, String padStr) {
        return StringUtils.rightPad(str, size, padStr);
    }

    public static String substring(String str, int start) {
        return StringUtils.substring(str, start);
    }

    public static String substring(String str, int start, int end) {
        return StringUtils.substring(str, start, end);
    }

    public static String left(String str, int len) {
        return StringUtils.left(str, len);
    }

    public static String right(String str, int len) {
        return StringUtils.right(str, len);
    }

    public static String mid(String str, int pos, int len) {
        return StringUtils.mid(str, pos, len);
    }

    public static String substringBefore(String str, String separator) {
        return StringUtils.substringBefore(str, separator);
    }

    public static String substringAfter(String str, String separator) {
        return StringUtils.substringAfter(str, separator);
    }

    public static String substringBeforeLast(String str, String separator) {
        return StringUtils.substringBeforeLast(str, separator);
    }

    public static String substringAfterLast(String str, String separator) {
        return StringUtils.substringAfterLast(str, separator);
    }

    public static String[] split(String str) {
        return StringUtils.split(str);
    }

    public static String[] split(String str, String separatorChars) {
        return StringUtils.split(str, separatorChars);
    }

    public static int countMatches(CharSequence str, CharSequence sub) {
        return StringUtils.countMatches(str, sub);
    }

    // The methods below mirror StringUtils semantics (null-safe, literal matching)
    // without calling the StringUtils overloads deprecated in commons-lang3 3.18+.

    public static String replace(String text, String searchString, String replacement) {
        if (text == null || StringUtils.isEmpty(searchString) || replacement == null) {
            return text;
        }
        return text.replace(searchString, replacement);
    }

    public static String remove(String str, String toRemove) {
        return replace(str, toRemove, "");
    }

    public static String removeStart(String str, String toRemove) {
        if (str == null || StringUtils.isEmpty(toRemove)) {
            return str;
        }
        return str.startsWith(toRemove) ? str.substring(toRemove.length()) : str;
    }

    public static String removeEnd(String str, String toRemove) {
        if (str == null || StringUtils.isEmpty(toRemove)) {
            return str;
        }
        return str.endsWith(toRemove) ? str.substring(0, str.length() - toRemove.length()) : str;
    }

    public static boolean contains(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        return str.contains(searchStr);
    }

    public static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        int len = searchStr.length();
        int max = str.length() - len;
        for (int i = 0; i <= max; i++) {
            if (str.regionMatches(true, i, searchStr, 0, len)) {
                return true;
            }
        }
        return false;
    }

    public static boolean startsWith(String str, String prefix) {
        if (str == null || prefix == null) {
            return str == null && prefix == null;
        }
        return str.startsWith(prefix);
    }

    public static boolean endsWith(String str, String suffix) {
        if (str == null || suffix == null) {
            return str == null && suffix == null;
        }
        return str.endsWith(suffix);
    }

    public static boolean equalsIgnoreCase(String str1, String str2) {
        if (str1 == null || str2 == null) {
            return str1 == str2;
        }
        return str1.equalsIgnoreCase(str2);
    }
}
