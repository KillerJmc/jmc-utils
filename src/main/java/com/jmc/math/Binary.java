package com.jmc.math;

import com.jmc.lang.Objs;
import com.jmc.lang.Strs;

/**
 * 二进制工具类
 * @since 1.0
 * @author Jmc
 */
public class Binary {

    private Binary() {}

    /**
     * 将long转成二进制字符串
     * @param x 长整数
     * @param toTrueForm 是否转换成原码形式
     * @return 结果字符串
     */
    public static String toString(long x, boolean toTrueForm) {
        // 求补码
        // 大于等于0的数以及最小值的原码和补码均相同，直接返回补码
        // 这里把最小值归到补码来处理是为了防止最小值的负数超出Long的范围
        if (!toTrueForm || (x >= 0 || x == Long.MIN_VALUE)) {
            // Long和Integer的toBinaryString返回的是补码形式
            String s = Long.toBinaryString(x);
            // 高位补0
            return "0".repeat(Long.SIZE - s.length()) + s;
        } else {
            // 求x相反数（正数）的原码
            String s = Long.toBinaryString(-x);
            // 1 加上 高位补0 加上 原码
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
        // 原理同 toString(long x, boolean toTrueForm)
        if (!toTrueForm || (x >= 0 || x == Integer.MIN_VALUE)) {
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
        // 原理同 toString(long x, boolean toTrueForm)
        if (!toTrueForm || (x >= 0 || x == Byte.MIN_VALUE)) {
            String s = toString((int) x, false);
            // 取最后8位
            return s.substring(s.length() - Byte.SIZE);
        } else {
            // 获取相反数的原码
            String s = toString(-(int) x, false);
            // 1 加上 相反数原码的后7位
            return "1" + s.substring(s.length() - Byte.SIZE + 1);
        }
    }

    /**
     * 转换二进制字符串为长整数
     * @param binaryString 二进制字符串
     * @param isTrueForm 是否是原码形式
     * @return 结果长整数
     */
    public static long toLong(String binaryString, boolean isTrueForm) {
        // 检查参数合法性
        if (Objs.nullOrEmpty(binaryString) || binaryString.length() > Long.SIZE || !Strs.isNum(binaryString)) {
            throw new IllegalArgumentException("二进制字符串不合法！");
        }

        // Long, Integer的最小值的二进制字符串放入parse方法会报错，所以特殊处理
        if (binaryString.equals(toString(Long.MIN_VALUE, true))) {
            return Long.MIN_VALUE;
        }

        // Long, Integer的parse方法参数必须为原码形式
        // 如果是负数的补码形式就先完全取反后加一获得其正数再加负号
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
        // 原理同 toLong(String binaryString, boolean isTrueForm)
        if (Objs.nullOrEmpty(binaryString) || binaryString.length() > Integer.SIZE || !Strs.isNum(binaryString)) {
            throw new IllegalArgumentException("二进制字符串不合法！");
        }

        if (binaryString.equals(toString(Integer.MIN_VALUE, true))) {
            return Integer.MIN_VALUE;
        }

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
        // 原理同 toLong(String binaryString, boolean isTrueForm)
        if (Objs.nullOrEmpty(binaryString) || binaryString.length() > Byte.SIZE || !Strs.isNum(binaryString)) {
            throw new IllegalArgumentException("二进制字符串不合法！");
        }

        if (binaryString.equals(toString(Byte.MIN_VALUE, true))) {
            return Byte.MIN_VALUE;
        }

        return !isTrueForm && binaryString.length() == Byte.SIZE && binaryString.charAt(0) == '1' ?
            (byte) -(toInt(Strs.swap(binaryString, "0", "1"), true) + 1):
            (byte) toInt(binaryString, true);
    }

}
