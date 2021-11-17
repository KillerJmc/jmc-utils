package com.jmc.reference;

import java.util.function.Function;

/**
 * 指针
 * @param <T> 指针储存类型
 * @since 1.7
 * @author Jmc
 */
@SuppressWarnings("unused")
public class Pointer<T> {
    /**
     * 指针指向值
     */
    private T value;

    private Pointer() {}

    /**
     * 获得一个指针
     * @param initValue 指向的初始值
     * @return 指针
     */
    public static <T> Pointer<T> of(T initValue) {
        var ptr = new Pointer<T>();
        ptr.value = initValue;
        return ptr;
    }

    /**
     * 获取指针指向的值
     * @return 指向的值
     */
    public T get() {
        return value;
    }

    /**
     * 更新指针指向的值
     * @param updateFunc 更新函数
     */
    public void update(Function<T, T> updateFunc) {
        this.value = updateFunc.apply(value);
    }

    /**
     * 让指针指向一个新值
     * @param newValue 新值
     * @return 旧值
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
     */
    @SuppressWarnings("unchecked")
    public Class<T> type() {
        return (Class<T>) value.getClass();
    }

    /**
     * 打印指针类型和指向的地址
     * @return 指针类型和指向的地址
     */
    @Override
    public String toString() {
        return "Pointer { " + value.getClass().getName() + "@" + Integer.toHexString(value.hashCode()) + " }";
    }
}
