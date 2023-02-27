package com.jmc.lang.ref;

import java.util.function.Function;

/**
 * 指针
 * @param <T> 指针储存类型
 * @apiNote <pre>{@code
 * public void test() {
 *     // 声明一个指针，并绑定一个初始值
 *     var p = Pointer.of(1);
 *
 *     // 获取指针内的值（1）
 *     var value = p.get();
 *
 *     // 获取指针值的类型（class java.lang.Integer）
 *     var type = p.type();
 *
 *     // 重新设置指针里的值为666
 *     p.reset(666);
 *
 *     // 将指针传入方法中，执行完值为667
 *     changeValue(p);
 *
 *     // 直接打印指针（Pointer { java.lang.Integer@29b }）
 *     // 打印的是指针的“地址”（hashcode）
 *     System.out.println(p);
 * }
 *
 * // 传入一个指针并改变值
 * void changeValue(Pointer<Integer> p) {
 *     // 使指针指向的值 + 1
 *     p.update(t -> ++t);
 * }
 * }</pre>
 * @since 1.7
 * @author Jmc
 */
public class Pointer<T> {
    /**
     * 指针指向值
     */
    private T value;

    private Pointer() {}

    /**
     * 获得一个指针
     * @param initValue 指向的初始值
     * @param <T> 初始值类型
     * @return 指针
     * @apiNote <pre>{@code
     * // 创建一个指针，指定初始值为3
     * var ptr = Pointer.of(3);
     * }</pre>
     */
    public static <T> Pointer<T> of(T initValue) {
        var ptr = new Pointer<T>();
        ptr.value = initValue;
        return ptr;
    }

    /**
     * 获得一个具有类型的空指针
     * @param <T> 初始值类型
     * @return 具有类型的空指针
     * @apiNote <pre>{@code
     * // 创建一个空指针，类型为Integer
     * var intPtr = Pointer.<Integer>empty();
     * }</pre>
     * @since 2.3
     */
    public static <T> Pointer<T> empty() {
        return new Pointer<>();
    }

    /**
     * 获取指针指向的值
     * @return 指向的值
     * @apiNote <pre>{@code
     * // 定义指针
     * var ptr = Pointer.of(3);
     * // 获取指针指向的值（3）
     * var value = ptr.get();
     * }</pre>
     */
    public T get() {
        return value;
    }

    /**
     * 更新指针指向的值
     * @param updateFunc 更新函数
     * @apiNote <pre>{@code
     * // 定义指针
     * var ptr = Pointer.of(3);
     * // 更新指针指向的值，将其+1
     * ptr.update(i -> i + 1);
     * }</pre>
     */
    public void update(Function<T, T> updateFunc) {
        this.value = updateFunc.apply(value);
    }

    /**
     * 让指针指向一个新值
     * @param newValue 新值
     * @return 旧值
     * @apiNote <pre>{@code
     * // 定义指针
     * var ptr = Pointer.of(3);
     * // 使指针指向一个新的值：4
     * ptr.reset(4);
     * }</pre>
     */
    public T reset(T newValue) {
        T oldValue = value;
        this.value = newValue;
        return oldValue;
    }

    /**
     * 让指针指向一个新值（无类型检查）
     * @param newValue 新值
     * @return 旧值
     * @apiNote <pre>{@code
     * // 定义指针
     * var ptr = Pointer.of(3);
     * // 使指针指向一个新的值并不加类型检查
     * ptr.resetUnchecked(4);
     * }</pre>
     */
    @SuppressWarnings("unchecked")
    public T resetUnchecked(Object newValue) {
        T oldValue = value;
        this.value = (T) newValue;
        return oldValue;
    }

    /**
     * 获取指针指向的值的类型
     * @return 值的类型
     * @apiNote <pre>{@code
     * // 定义指针
     * var ptr = Pointer.of(3);
     * // 获取指针指向值类型（class java.lang.Integer）
     * var classType = ptr.getType();
     * }</pre>
     */
    @SuppressWarnings("unchecked")
    public Class<T> type() {
        return (Class<T>) value.getClass();
    }

    /**
     * 打印指针类型和指向的地址
     * @return 指针类型和指向的地址
     * @apiNote <pre>{@code
     * // 定义指针
     * var ptr = Pointer.of(3);
     * // 获取指针类型和指向的地址（Pointer { java.lang.Integer@29a }）
     * var toString = ptr.toString();
     * }</pre>
     */
    @Override
    public String toString() {
        return "Pointer { " + value.getClass().getName() + "@" + Integer.toHexString(value.hashCode()) + " }";
    }
}
