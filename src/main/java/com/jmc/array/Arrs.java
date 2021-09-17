package com.jmc.array;

import com.jmc.lang.extend.Rand;
import com.jmc.lang.extend.Tries;
import com.jmc.util.Compare;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * 数组增强类
 * @since 1.0.0
 * @author Jmc
 */
@SuppressWarnings("unused")
public class Arrs {
    /**
     * 获得指定类型的初始化完成的数组
     * @param c 指定类型的Class对象
     * @param len 数组长度
     * @param <T> 指定的类型
     * @return 初始化完的数组
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] newInstance(Class<T> c, int len) {
        return Tries.tryReturnsT(() -> {
            var a = (T[]) Array.newInstance(c, len);
            var ctr = c.getConstructor();

            for (int i = 0; i < a.length; i++) {
                a[i] = ctr.newInstance();
            }
            return a;
        });
    }

    /**
     * 包装基本数据类型数组为可排序对象数组
     * @param a 原数组
     * @param componentType 结果数组的元素类型对应的Class类
     * @param <T> 结果数组的元素类型，必须是可排序的类型
     * @return 结果数组
     */
    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> T[] box(Object a, Class<T> componentType) {
        if (a instanceof int[] a0) {
            return (T[]) Arrays.stream(a0).boxed().toArray(Integer[]::new);
        } else if (a instanceof long[] a0) {
            return (T[]) Arrays.stream(a0).boxed().toArray(Long[]::new);
        } else if (a instanceof double[] a0) {
            return (T[]) Arrays.stream(a0).boxed().toArray(Double[]::new);
        } else if (a instanceof byte[] a0) {
            var result = (T[]) Array.newInstance(componentType, a0.length);
            for (int i = 0; i < a0.length; i++) {
                result[i] = (T) (Object) a0[i];
            }
            return result;
        } else if (a instanceof char[] a0) {
            var result = (T[]) Array.newInstance(componentType, a0.length);
            for (int i = 0; i < a0.length; i++) {
                result[i] = (T) (Object) a0[i];
            }
            return result;
        } else if (a instanceof short[] a0) {
            var result = (T[]) Array.newInstance(componentType, a0.length);
            for (int i = 0; i < a0.length; i++) {
                result[i] = (T) (Object) a0[i];
            }
            return result;
        } else if (a instanceof float[] a0) {
            var result = (T[]) Array.newInstance(componentType, a0.length);
            for (int i = 0; i < a0.length; i++) {
                result[i] = (T) (Object) a0[i];
            }
            return result;
        } else if (a instanceof boolean[] a0) {
            var result = (T[]) Array.newInstance(componentType, a0.length);
            for (int i = 0; i < a0.length; i++) {
                result[i] = (T) (Object) a0[i];
            }
            return result;
        } else if (a instanceof Comparable<?>[] a0){
            return (T[]) a0;
        } else {
            throw new IllegalArgumentException("Invalid array type!");
        }
    }

    /**
     * 生成各不相同的随机数
     * @param min 最小值
     * @param max 最大值
     * @param n 生成个数
     * @return 生成结果的数组
     */
    public static int[] getDiffRandArr(int min, int max, int n){
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
     */
    public static int[] getRandArr(int min, int max, int n){
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
     * @param a 数组名
     * @param idx1 第一个元素对应的下标
     * @param idx2 第二个元素对应的下标
     * @param <T> 数组元素
     */
    public static <T> void swap(T[] a, int idx1, int idx2) {
        var tmp = a[idx1];
        a[idx1] = a[idx2];
        a[idx2] = tmp;
    }

    /**
     * 交换元素
     * @param a 整形数组
     * @param idx1 第一个元素对应的下标
     * @param idx2 第二个元素对应的下标
     */
    public static void swap(int[] a, int idx1, int idx2) {
        var tmp = a[idx1];
        a[idx1] = a[idx2];
        a[idx2] = tmp;
    }

    /**
     * 交换元素
     * @param a 长整形数组
     * @param idx1 第一个元素对应的下标
     * @param idx2 第二个元素对应的下标
     */
    public static void swap(long[] a, int idx1, int idx2) {
        var tmp = a[idx1];
        a[idx1] = a[idx2];
        a[idx2] = tmp;
    }

    /**
     * 交换元素
     * @param a 双精度浮点数数组
     * @param idx1 第一个元素对应的下标
     * @param idx2 第二个元素对应的下标
     */
    public static void swap(double[] a, int idx1, int idx2) {
        var tmp = a[idx1];
        a[idx1] = a[idx2];
        a[idx2] = tmp;
    }
}
