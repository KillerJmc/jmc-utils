package com.jmc.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 元组类
 * @since 3.7
 * @author Jmc
 */
public class Tuple {
    /**
     * 元组初始下标值
     */
    private static final int initialIdx = 1;

    /**
     * 元组数据（名称 -> 数据）
     */
    private final Map<String, Object> data = new HashMap<>();

    /**
     * 元素个数
     */
    private int size;

    private Tuple() {}

    /**
     * 从一系列值构建元组
     * @param data 一系列值（类型可以不同）
     * @return 元组对象
     * @apiNote <pre>{@code
     * // 创建一个元组
     * var tuple = Tuple.of(1, "Jack", '男');
     * }</pre>
     */
    public static Tuple of(Object... data) {
        var instance = new Tuple();
        // 设置元素个数
        instance.size = data.length;

        // 从初始下标开始将值依次放入到元组数据中
        var idx = new AtomicInteger(initialIdx);
        Arrays.stream(data).forEach(e -> instance.data.put(String.valueOf(idx.getAndIncrement()), e));
        return instance;
    }

    /**
     * 从一系列有命名的值构建元组
     * @param nameToDataMap 命名的值的Map集合
     * @return 元组对象
     * @apiNote <pre>{@code
     * // 数据
     * int id = 1;
     * String name = "Jack";
     * char gender = '男';
     *
     * // 构建存放有命名的值的元组
     * var tuple = Tuple.fromNamed(Map.of(
     *         "id", id,
     *         "name", name,
     *         "gender", gender
     * ));
     * }</pre>
     */
    public static Tuple fromNamed(Map<String, Object> nameToDataMap) {
        // 构建含数字下标的元组
        var instance = Tuple.of(nameToDataMap.values().toArray());

        // 加入可用命名作为下标
        instance.data.putAll(nameToDataMap);
        return instance;
    }

    /**
     * 通过下标获取元素（第一个元素下标是{@value #initialIdx}）
     * @param index 元素的下标
     * @return 对应的元素
     * @param <T> 获取的元素类型
     * @apiNote <pre>{@code
     * // 构建元组
     * var tuple = Tuple.of(666, "Jack");
     *
     * // 获取第二个元素
     * int id = tuple.get(2);
     * }</pre>
     */
    @SuppressWarnings("unchecked")
    public <T> T get(int index) {
        return (T) data.get(String.valueOf(index));
    }

    /**
     * 通过元素名称获取元素
     * @param name 元素的名称
     * @return 对应的元素
     * @param <T> 获取的元素类型
     * @apiNote <pre>{@code
     * // 数据
     * int id = 1;
     * String name = "Jack";
     * char gender = '男';
     *
     * // 构建存放有命名的值的元组
     * var tuple = Tuple.fromNamed(Map.of(
     *         "id", id,
     *         "name", name,
     *         "gender", gender
     * ));
     *
     * // 通过值名称“gender”获取值
     * char gender1 = tuple.get("gender");
     * }</pre>
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
        return (T) data.get(name);
    }

    /**
     * 获取元组的元素个数
     * @return 元组的元素个数
     * @apiNote <pre>{@code
     * // 构建元组
     * var tuple = Tuple.of(666, "Jack");
     *
     * // 获取元组的元素个数：2
     * int size = tuple.size();
     * }</pre>
     */
    public int size() {
        return size;
    }
}
