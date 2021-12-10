package com.jmc.lang.primitive;

/**
 * boolean拓展
 * @since 1.0
 * @author Jmc
 */
@SuppressWarnings("unused")
public class Bools {

    private Bools() {}

    /**
     * 判断一个数字是否在两数之间
     * @param t 被判断的数字
     * @param startInclusive 第一个数（包含边界）
     * @param endInclusive 第二个数（包含边界）
     * @return 这个数是否在两数之间
     */
    public static boolean in(double t, double startInclusive, double endInclusive) {
        return t >= startInclusive && t <= endInclusive;
    }
}
