package com.jmc.array;

import com.jmc.lang.Tries;
import com.jmc.util.Rand;

import java.util.Arrays;

/**
 * 数组增强类
 * @since 1.0
 * @author Jmc
 */
public class Arrs {

    private Arrs() {}

    /**
     * 获得指定类型的初始化完成的数组
     * @param c 指定类型的Class对象
     * @param len 数组长度
     * @param <T> 指定的类型
     * @return 初始化完的数组
     * @apiNote <pre>{@code
     * // 初始化一个ArrayList数组
     * var a = Arrs.newInstance(ArrayList.class, 6);
     * // 每个元素都已经初始化完成，可直接使用
     * a[0].add(1);
     * }</pre>
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] newInstance(Class<T> c, int len) {
        return Tries.tryGet(() -> {
            var a = (T[]) java.lang.reflect.Array.newInstance(c, len);
            var ctr = c.getConstructor();

            for (int i = 0; i < a.length; i++) {
                a[i] = ctr.newInstance();
            }
            return a;
        });
    }

    /**
     * 生成各不相同的随机数
     * @param min 最小值
     * @param max 最大值
     * @param n 生成个数
     * @return 生成结果的数组
     * @apiNote <pre>{@code
     * // 生成不含重复元素的随机数组（一共10个元素，每个元素范围是[1, 10]）
     * var a = Arrs.getDiffRandArr(1, 10, 10);
     * }</pre>
     */
    public static int[] getDiffRandArr(int min, int max, int n) {
        // 如果参数不合法就返回
        if (max < min || n > max - min + 1) {
            return null;
        }

        // rand数组
        int[] rand = new int[n];

        // 已填入数字个数
        int amount = 0;

        // 如果最小值为0就填充数组(排除默认值0的干扰)
        if (min == 0) {
            Arrays.fill(rand, Integer.MAX_VALUE);
        }

        // 当已填入的数字个数小于用户指定的个数
        outer : while (amount < n) {
            int result = Rand.nextInt(min, max);

            // 遍历数组
            for (int oldNum : rand) {
                if (result == oldNum) {
                    continue outer;
                }
            }

            // 如果为新数字就记录
            rand[amount++] = result;
        }

        return rand;
    }

    /**
     * 获取随机数组
     * @param min 最小值
     * @param max 最大值
     * @param n 数组长度
     * @return 结果数组
     * @apiNote <pre>{@code
     * // 生成随机数组（一共10个元素，每个元素范围是[1, 10]）
     * var a = Arrs.getRandArr(1, 10, 10);
     * }</pre>
     */
    public static int[] getRandArr(int min, int max, int n) {
        // 如果参数不合法就返回
        if (max < min) {
            return null;
        }

        // rand数组
        int[] rand = new int[n];

        for (int i = 0; i < rand.length; i++) {
            rand[i] = Rand.nextInt(min, max);
        }

        return rand;
    }

    /**
     * 交换元素
     * @param a 通用数组
     * @param idx1 第一个元素对应的下标
     * @param idx2 第二个元素对应的下标
     * @param <T> 数组元素类型
     * @apiNote <pre>{@code
     * int[] a = {1, 2, 3, 4, 5};
     * var arr = Array.of(a);
     *
     * // 用于对通用数组元素的交换
     * // 交换下标1和下标3的元素
     * Arrs.swap(arr, 1, 3);
     * }</pre>
     * @since 1.1
     * @see Array
     */
    public static <T> void swap(Array<T> a, int idx1, int idx2) {
        T tmp = a.get(idx1);
        a.set(idx1, a.get(idx2));
        a.set(idx2, tmp);
    }
}
