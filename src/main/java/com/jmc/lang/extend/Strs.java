package com.jmc.lang.extend;

import java.util.ArrayList;
import java.util.List;

/**
 * String类扩展
 * <p> 时间:
 * <pre>
 *      2020.3.11   加入重复方法
 *      2020.3.24   添加高级重复（以吊状图精简代码为灵感开发了此功能）
 *      2020.3.26   添加or系列方法
 *      2020.4.1    添加orEquals系列方法和substrings系列方法
 *      2020.4.10   添加removeAll方法
 *      2020.4.12   1.添加getLines和getLine方法
 *                  2.removeAll方法改为StringBuilder编辑提高效率
 *      2020.5.1    开创stream模式
 *      2020.6.20   解决stream模式方法名称歧义
 *      2020.8.6    添加encrypt和decrypt方法（之后改名为xor：2021.1.30补充）
 *      2020.8.30   添加linesToStr方法
 *      2020.9.5    添加collectAll方法
 *      2020.11.18  添加format方法
 *      2021.1.22   修改substrings方法，并改名为sub
 *      2021.1.30   移除stream模式，删除getLine系列，linesToStr，format方法
 *      2021.6.16   将sub方法分成subInclusive和subExclusive方法
 *      2021.6.20   让sub系列方法在找不到开始或结束字符串时返回原字符串本身
 *      2021.8.2    添加capitalize方法
 * </pre>
 * @since 1.0.0
 * @author Jmc
 */
@SuppressWarnings("unused")
public class Strs
{
    /**
     * 获取字符串的子串
     * @param src 原字符串
     * @param start 开始的子串（不包含）
     * @return 结果子串
     */
    public static String subExclusive(String src, String start) {
        var startIdx = src.indexOf(start);
        return startIdx == -1 ? src : src.substring(startIdx + start.length());
    }

    /**
     * 获取字符串的子串
     * @param src 原字符串
     * @param start 开始的子串（不包含）
     * @param end 结束的子串（不包含）
     * @return 结果子串
     */
    public static String subExclusive(String src, String start, String end) {
        var startIdx = src.indexOf(start);
        var endIdx = src.indexOf(end);
        return startIdx == -1 || endIdx == -1 ? src : src.substring(startIdx + start.length(), endIdx);
    }

    /**
     * 获取字符串的子串
     * @param src 原字符串
     * @param start 开始的子串（包含）
     * @return 结果子串
     */
    public static String subInclusive(String src, String start) {
        var startIdx = src.indexOf(start);
        return startIdx == -1 ? src : src.substring(startIdx);
    }

    /**
     * 获取字符串的子串
     * @param src 原字符串
     * @param start 开始的子串（包含）
     * @param end 结束的子串（包含）
     * @return 结果子串
     */
    public static String subInclusive(String src, String start, String end) {
        int startIdx = src.indexOf(start);
        int endIdx = src.indexOf(end, startIdx);

        return startIdx == -1 || endIdx == -1 ? src :  src.substring(startIdx, endIdx + end.length());
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
