package com.jmc.lang.extend;

import java.util.Random;

/**
 * 产生指定随机数
 * @since 1.0.0
 * @author Jmc
 */
@SuppressWarnings("unused")
public class Rand
{
    /**
     * 随机数类
     */
    private static final Random RAND = new Random(System.currentTimeMillis());

    /**
     * 产生指定范围的随机数
     * @param minInclusive 最小值（包含边界）
     * @param maxInclusive 最大值（包含边界）
     * @return 产生的随机数
     */
    public static int nextInt(int minInclusive, int maxInclusive) {
        if (maxInclusive < minInclusive) {
            return -1;
        }
        return RAND.nextInt(maxInclusive - minInclusive + 1) + minInclusive;
    }
}
