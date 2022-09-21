package com.jmc.util;

import com.jmc.array.Array;

/**
 * 比较工具类
 * @since 1.0
 * @author Jmc
 */
public class Compare {

    private Compare() {}

    /**
     * 比较是否大于
     * @param o1 第一个元素
     * @param o2 第二个元素
     * @param <O> 可排序元素
     * @return 第一个元素是否大于第二个元素
     */
    public static <O extends Comparable<O>> boolean gt(O o1, O o2) {
        return o1.compareTo(o2) > 0;
    }

    /**
     * 比较是否大于或等于
     * @param o1 第一个元素
     * @param o2 第二个元素
     * @param <O> 可排序元素
     * @return 第一个元素是否大于或等于第二个元素
     */
    public static <O extends Comparable<O>> boolean ge(O o1, O o2) {
        return o1.compareTo(o2) >= 0;
    }

    /**
     * 比较是否小于
     * @param o1 第一个元素
     * @param o2 第二个元素
     * @param <O> 可排序元素
     * @return 第一个元素是否小于第二个元素
     */
    public static <O extends Comparable<O>> boolean lt(O o1, O o2) {
        return o1.compareTo(o2) < 0;
    }

    /**
     * 比较是否小于或等于
     * @param o1 第一个元素
     * @param o2 第二个元素
     * @param <O> 可排序元素
     * @return 第一个元素是否小于第二个元素
     */
    public static <O extends Comparable<O>> boolean le(O o1, O o2) {
        return o1.compareTo(o2) < 0;
    }

    /**
     * 比较是否等于
     * @param o1 第一个元素
     * @param o2 第二个元素
     * @param <O> 可排序元素
     * @return 第一个元素是否大于第二个元素
     */
    public static <O extends Comparable<O>> boolean eq(O o1, O o2) {
        return o1.compareTo(o2) == 0;
    }

    /**
     * 比较是否大于
     * @param a 可排序通用数组
     * @param idx1 第一个元素的下标
     * @param idx2 第二个元素的下标
     * @param <T> 可排序元素类型
     * @return 第一个元素是否大于第二个元素
     * @since 1.2
     */
    public static <T extends Comparable<T>> boolean gt(Array<T> a, int idx1, int idx2) {
        return Compare.gt(a.get(idx1), a.get(idx2));
    }

    /**
     * 比较是否大于或等于
     * @param a 可排序通用数组
     * @param idx1 第一个元素的下标
     * @param idx2 第二个元素的下标
     * @param <T> 可排序元素类型
     * @return 第一个元素是否大于或等于第二个元素
     * @since 1.2
     */
    public static <T extends Comparable<T>> boolean ge(Array<T> a, int idx1, int idx2) {
        return Compare.ge(a.get(idx1), a.get(idx2));
    }

    /**
     * 比较是否小于
     * @param a 可排序通用数组
     * @param idx1 第一个元素的下标
     * @param idx2 第二个元素的下标
     * @param <T> 可排序元素类型
     * @return 第一个元素是否小于第二个元素
     * @since 1.2
     */
    public static <T extends Comparable<T>> boolean lt(Array<T> a, int idx1, int idx2) {
        return Compare.lt(a.get(idx1), a.get(idx2));
    }

    /**
     * 比较是否小于或等于
     * @param a 可排序通用数组
     * @param idx1 第一个元素的下标
     * @param idx2 第二个元素的下标
     * @param <T> 可排序元素类型
     * @return 第一个元素是否小于或等于第二个元素
     * @since 1.2
     */
    public static <T extends Comparable<T>> boolean le(Array<T> a, int idx1, int idx2) {
        return Compare.le(a.get(idx1), a.get(idx2));
    }

    /**
     * 比较是否等于
     * @param a 可排序通用数组
     * @param idx1 第一个元素的下标
     * @param idx2 第二个元素的下标
     * @param <T> 可排序元素类型
     * @return 第一个元素是否等于第二个元素
     * @since 1.2
     */
    public static <T extends Comparable<T>> boolean eq(Array<T> a, int idx1, int idx2) {
        return Compare.eq(a.get(idx1), a.get(idx2));
    }
}
