package com.jmc.util;

import com.jmc.lang.extend.Strs;

/**
 * 二进制工具类
 * @since 1.0
 * @author Jmc
 */
@SuppressWarnings("unused")
public class Binary {
    /**
     * 将long转成二进制字符串
     * @param x 长整数
     * @param toTrueForm 是否转换成原码形式
     * @return 结果字符串
     */
    public static String toString(long x, boolean toTrueForm) {
        // long, int, byte最小值比较特殊，直接返回补码即可
        if (x >= 0 || x == Long.MIN_VALUE || !toTrueForm) {
            // long, int, byte的toBinaryString返回的是补码形式
            String s = Long.toBinaryString(x);
            return "0".repeat(Long.SIZE - s.length()) + s;
        } else {
            String s = Long.toBinaryString(-x);
            return "1" + "0".repeat(Long.SIZE - s.length() - 1) + s;
        }
    }

    /**
     * 将int转成二进制字符串
     * @param x 整数
     * @param toTrueForm 是否转换成原码形式
     * @return 结果字符串
     */
    public static String toString(int x, boolean toTrueForm) {
        if (x >= 0 || x == Integer.MIN_VALUE || !toTrueForm) {
            String s = Integer.toBinaryString(x);
            return "0".repeat(Integer.SIZE - s.length()) + s;
        } else {
            String s = Integer.toBinaryString(-x);
            return "1" + "0".repeat(Integer.SIZE - s.length() - 1) + s;
        }
    }

    /**
     * 将byte转成二进制字符串
     * @param x 字节类型（byte）
     * @param toTrueForm 是否转换成原码形式
     * @return 结果字符串
     */
    public static String toString(byte x, boolean toTrueForm) {
        if (x >= 0 || x == Byte.MIN_VALUE || !toTrueForm) {
            String s = toString((int) x | 256, false);
            return s.substring(s.length() - 8);
        } else {
            String s = toString(-x, false);
            return "1" + "0".repeat(Byte.SIZE - s.length() - 1) + s;
        }
    }

    /**
     * 转换二进制字符串为长整数
     * @param binaryString 二进制字符串
     * @param isTrueForm 是否是原码形式
     * @return 结果长整数
     */
    public static long toLong(String binaryString, boolean isTrueForm) {
        // long, int, byte的最小值比较特殊，直接返回
        if (binaryString.equals(toString(Long.MIN_VALUE, true))) return Long.MIN_VALUE;

        // long, int的parse方法参数必须为原码形式
        // 如果不是原码形式就先取反后加一获得其正数再加负号
        return !isTrueForm && binaryString.length() == Long.SIZE && binaryString.charAt(0) == '1' ?
            -(Long.parseLong(Strs.swap(binaryString, "0", "1"), 2) + 1) :
            Long.parseLong(binaryString, 2);
    }

    /**
     * 转换二进制字符串为整数
     * @param binaryString 二进制字符串
     * @param isTrueForm 是否是原码形式
     * @return 结果整数
     */
    public static int toInt(String binaryString, boolean isTrueForm) {
        if (binaryString.equals(toString(Integer.MIN_VALUE, true))) return Integer.MIN_VALUE;

        return !isTrueForm && binaryString.length() == Integer.SIZE && binaryString.charAt(0) == '1' ?
                -(Integer.parseInt(Strs.swap(binaryString, "0", "1"), 2) + 1) :
                Integer.parseInt(binaryString, 2);
    }

    /**
     * 转换二进制字符串为字节
     * @param binaryString 二进制字符串
     * @param isTrueForm 是否是原码形式
     * @return 结果字节
     */
    public static byte toByte(String binaryString, boolean isTrueForm) {
        if (binaryString.equals("10000000")) return Byte.MIN_VALUE;

        return !isTrueForm && binaryString.length() == Byte.SIZE && binaryString.charAt(0) == '1' ?
            (byte) -(toInt(Strs.swap(binaryString, "0", "1"), true) + 1):
            (byte) toInt(binaryString, true);
    }

}
