package com.jmc.lang;

import java.util.ArrayList;
import java.util.List;

/**
 * String类扩展
 * @since 1.0
 * @author Jmc
 */
@SuppressWarnings("unused")
public class Strs {

    private Strs() {}

    /**
     * 判断一个字符串是否由数字构成（正数，负数或小数）
     * @param src 字符串
     * @return 判断结果
     * @since 2.0
     */
    public static boolean isNum(String src) {
        // 如果src是空白的，返回假
        if (src.isBlank()) {
            return false;
        }

        // 如果只有一个字符
        if (src.length() == 1) {
            // 这个字符必须是数字
            return Character.isDigit(src.charAt(0));
        }

        // 把字符串正负号削减掉
        // 如果第一个字符不是数字
        if (!Character.isDigit(src.charAt(0))) {
            // 如果不是正负号，返回假
            if (!Objs.orEquals(src.charAt(0), '+', '-')) {
                return false;
            }
            // 去掉第一个正负号
            src = src.substring(1);

            // 如果此时第一个字符不是数字，返回假
            if (!Character.isDigit(src.charAt(0))) {
                return false;
            }
        }

        // 把字符串削减到小数点后第一位
        // 如果此时第一个字符是0
        if (src.charAt(0) == '0') {
            // 如果字符串读完了，返回真
            if (src.length() == 1) {
                return true;
            }

            // 否则第二个字符一定要是小数点
            if (src.charAt(1) != '.') {
                return false;
            }

            // 去掉0和小数点
            src = src.substring(2);
        } else {
            var hasDot = false;
            // 如果此时第一个字符是非0的数字
            for (int i = 0; i < src.length(); i++) {
                char c = src.charAt(i);
                // 如果当前字符不是数字
                if (!Character.isDigit(c)) {
                    // 如果当前字符是小数点
                    if (c == '.') {
                        // 截断字符串到小数点后
                        src = src.substring(i + 1);
                        // 标记存在小数点
                        hasDot = true;
                        break;
                    }
                    // 不是小数点或数字，返回假
                    return false;
                }
            }

            // 如果不包含小数点，返回真
            if (!hasDot) {
                return true;
            }
        }

        // 小数点后有且只有1至多个数字
        return !src.isBlank() && src.chars().mapToObj(t -> (char) t).allMatch(Character::isDigit);
    }

    /**
     * 获取字符串的子串
     * @param src 原字符串
     * @param start 开始的子串（不包含）
     * @param fromIdx 从哪个下标开始查找
     * @return 结果子串
     * @since 1.7
     */
    public static String subExclusive(String src, String start, int fromIdx) {
        var startIdx = src.indexOf(start, fromIdx);
        return startIdx == -1 ? src : src.substring(startIdx + start.length());
    }

    /**
     * 获取字符串的子串
     * @param src 原字符串
     * @param start 开始的子串（不包含）
     * @return 结果子串
     */
    public static String subExclusive(String src, String start) {
        return subExclusive(src, start, 0);
    }

    /**
     * 获取字符串的子串
     * @param src 原字符串
     * @param start 开始的子串（不包含）
     * @param end 结束的子串（不包含）
     * @param fromIdx 从哪个下标开始查找
     * @return 结果子串
     * @since 1.7
     */
    public static String subExclusive(String src, String start, String end, int fromIdx) {
        var startIdx = src.indexOf(start, fromIdx);
        var endIdx = src.indexOf(end, startIdx + start.length());
        return startIdx == -1 || endIdx == -1 ? src : src.substring(startIdx + start.length(), endIdx);
    }

    /**
     * 获取字符串的子串
     * @param src 原字符串
     * @param start 开始的子串（不包含）
     * @param end 结束的子串（不包含）
     * @return 结果子串
     */
    public static String subExclusive(String src, String start, String end) {
        return subExclusive(src, start, end, 0);
    }


    /**
     * 获取字符串的子串
     * @param src 原字符串
     * @param start 开始的子串（包含）
     * @param fromIdx 从哪个下标开始查找
     * @return 结果子串
     * @since 1.7
     */
    public static String subInclusive(String src, String start, int fromIdx) {
        var startIdx = src.indexOf(start, fromIdx);
        return startIdx == -1 ? src : src.substring(startIdx);
    }

    /**
     * 获取字符串的子串
     * @param src 原字符串
     * @param start 开始的子串（包含）
     * @return 结果子串
     */
    public static String subInclusive(String src, String start) {
        return subInclusive(src, start, 0);
    }

    /**
     * 获取字符串的子串
     * @param src 原字符串
     * @param start 开始的子串（包含）
     * @param end 结束的子串（包含）
     * @param fromIdx 从哪个下标开始查找
     * @return 结果子串
     * @since 1.7
     */
    public static String subInclusive(String src, String start, String end, int fromIdx) {
        int startIdx = src.indexOf(start, fromIdx);
        int endIdx = src.indexOf(end, startIdx + start.length());

        return startIdx == -1 || endIdx == -1 ? src : src.substring(startIdx, endIdx + end.length());
    }

    /**
     * 获取字符串的子串
     * @param src 原字符串
     * @param start 开始的子串（包含）
     * @param end 结束的子串（包含）
     * @return 结果子串
     */
    public static String subInclusive(String src, String start, String end) {
        return subInclusive(src, start, end, 0);
    }

    /**
     * 替换多个旧子串为新子串
     * @param src 原字符串
     * @param newStr 新子串
     * @param oldStrs 旧子串
     * @return 结果字符串
     */
    public static String orReplace(String src, String newStr, String... oldStrs) {
        Objs.throwsIfNullOrEmpty(src, newStr);

        for (String oldChar : oldStrs) src = src.replace(oldChar, newStr);

        return src;
    }

    /**
     * 判断多个字符串中是否存在至少一个字符串与提供的字符串相等
     * @param src 提供的字符串
     * @param strs 多个字符串
     * @return 是否存在相等
     */
    public static boolean orEquals(String src, String... strs) {
        Objs.throwsIfNullOrEmpty(src);

        for (String s : strs) if (src.equals(s)) return true;

        return false;
    }

    /**
     * 判断字符串中是否包含任意提供的子串
     * @param src 字符串
     * @param contains 提供的子串
     * @return 是否包含任意子串
     */
    public static boolean orContains(String src, String... contains) {
        Objs.throwsIfNullOrEmpty(src);

        for (String s : contains) if (src.contains(s)) return true;

        return false;
    }

    /**
     * 判断字符串是否以提供的任意子串开始
     * @param src 字符串
     * @param strs 提供的子串
     * @return 是否以提供的任意子串开始
     */
    public static boolean orStartsWith(String src, String... strs) {
        Objs.throwsIfNullOrEmpty(src);

        for (String s : strs) if (src.startsWith(s)) return true;

        return false;
    }

    /**
     * 判断字符串是否以提供的任意子串结束
     * @param src 字符串
     * @param strs 提供的子串
     * @return 是否以提供的任意子串结束
     */
    public static boolean orEndsWith(String src, String... strs) {
        Objs.throwsIfNullOrEmpty(src);

        for (String s : strs) if (src.endsWith(s)) return true;

        return false;
    }

    /**
     * 将字符串首字母大写
     * @param src 字符串
     * @return 结果字符串
     */
    public static String capitalize(String src) {
        return (src == null || src.length() == 0) ? src :
                src.length() == 1 ? src.toUpperCase() : Character.toUpperCase(src.charAt(0)) + src.substring(1);
    }

    /**
     * 异或字符串
     * @param src 原字符串
     * @param key 私钥
     * @return 结果字符串
     */
    public static String xor(String src, byte key) {
        byte[] bs = src.getBytes();
        for (int i = 0; i < bs.length; i++) {
            bs[i] ^= key;
        }
        return new String(bs);
    }

    /**
     * 交换字符串中的两个子串
     * @param src 字符串
     * @param subA 子串a
     * @param subB 子串b
     * @return 结果字符串
     */
    public static String swap(String src, String subA, String subB) {
        return subA.equals(subB) ? src :
                src.replace(subA, "" + Character.MAX_VALUE)
                   .replace(subB, subA)
                   .replace("" + Character.MAX_VALUE, subB);
    }

    /**
     * 收集所有以开始字符串开始，结束字符串结束的子串
     * @param src 字符串
     * @param start 开始字符串
     * @param end 结束字符串
     * @param includesBoundary 是否包含边界（开始和结束字符串）
     * @return 结果列表
     */
    public static List<String> collectAll(String src, String start, String end, boolean includesBoundary) {
        StringBuilder sb = new StringBuilder(src);
        List<String> rList = new ArrayList<>();
        while (true) {
            int startIdx = sb.indexOf(start);
            int endIdx = sb.indexOf(end, startIdx);
            if (startIdx != -1 && endIdx != -1) {
                if (includesBoundary) {
                    rList.add(sb.substring(startIdx, endIdx + end.length()));
                } else {
                    rList.add(sb.substring(startIdx + end.length(), endIdx));
                }
                sb.delete(startIdx, endIdx + end.length());
            } else {
                break;
            }
        }
        return rList;
    }

    /**
     * 删除所有以开始字符串开始，结束字符串结束的子串
     * @param src 字符串
     * @param start 开始字符串
     * @param end 结束字符串
     * @return 结果字符串
     */
    public static String removeAll(String src, String start, String end) {
        StringBuilder sb = new StringBuilder(src);
        while (true) {
            int startIdx = sb.indexOf(start);
            int endIdx = sb.indexOf(end, startIdx);
            if (startIdx != -1 && endIdx != -1) {
                sb.delete(startIdx, endIdx + end.length());
            } else {
                break;
            }
        }
        return sb.toString();
    }
}
