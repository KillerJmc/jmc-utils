package com.jmc.util;

import java.util.Random;

/**
 * 产生指定随机数
 * @since 1.0
 * @author Jmc
 */
public class Rand {
    /**
     * 随机数类
     */
    private static final Random RAND = new Random(System.currentTimeMillis());

    private Rand() {}

    /**
     * 产生范围为整个int的随机数
     * @return 产生的随机数
     * @apiNote <pre>{@code
     * // 产生一个int范围内的随机数
     * var res = Rand.nextInt();
     * }</pre>
     * @since 2.1
     */
    public static int nextInt() {
        return RAND.nextInt();
    }

    /**
     * 产生指定范围的随机数
     * @param minInclusive 最小值（大于0，包含边界，小于int最大值）
     * @param maxInclusive 最大值（大于0，包含边界，小于int最大值）
     * @return 产生的随机数
     * @apiNote <pre>{@code
     * // 产生一个范围是[3, 6]的随机数
     * var res = Rand.nextInt(3, 6);
     * }</pre>
     */
    public static int nextInt(int minInclusive, int maxInclusive) {
        if (minInclusive < 0 || maxInclusive < 0 || maxInclusive < minInclusive) {
            throw new RuntimeException("最小值和最大值需大于0 且 最大值必须大于等于最小值");
        }

        if (maxInclusive == Integer.MAX_VALUE) {
            throw new RuntimeException("最大值只能为int的最大值减一");
        }

        return RAND.nextInt(maxInclusive - minInclusive + 1) + minInclusive;
    }

    /**
     * 产生指定范围的随机数，最小值默认为0
     * @param maxInclusive 最大值（大于0，包含边界，小于int最大值）
     * @return 产生的随机数
     * @apiNote <pre>{@code
     * // 产生一个范围是[0, 6]的随机数
     * var res = Rand.nextInt(6);
     * }</pre>
     * @since 1.5
     */
    public static int nextInt(int maxInclusive) {
        if (maxInclusive < 0) {
            throw new RuntimeException("最大值需大于0");
        }

        if (maxInclusive == Integer.MAX_VALUE) {
            throw new RuntimeException("最大值只能为int的最大值减一");
        }

        return RAND.nextInt(maxInclusive + 1);
    }
}
