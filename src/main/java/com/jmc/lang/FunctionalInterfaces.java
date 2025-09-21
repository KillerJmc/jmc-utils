package com.jmc.lang;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * 函数式接口合集
 * @since 3.9
 * @author Jmc
 */
public class FunctionalInterfaces {
    /**
     * 禁止实例化
     */
    private FunctionalInterfaces() {}

    /**
     * 可抛出异常的Runnable
     */
    @FunctionalInterface
    public interface CheckedRunnable {
        /**
         * 执行方法
         * @throws Throwable 抛出的异常
         */
        void run() throws Throwable;
    }

    /**
     * 抛出异常的Consumer
     * @param <T> 函数参数类型
     */
    @FunctionalInterface
    public interface CheckedConsumer<T> {
        /**
         * 给定参数进行消费
         * @param t 输入的参数
         * @throws Throwable 抛出异常
         */
        void accept(T t) throws Throwable;
    }

    /**
     * 抛出异常的Supplier
     * @param <T> 返回值类型
     */
    @FunctionalInterface
    public interface CheckedSupplier<T> {
        /**
         * 获取一个结果
         * @return 获取的结果
         * @throws Throwable 抛出的异常
         */
        T get() throws Throwable;
    }

    /**
     * 抛出异常的Function
     * @param <T> 函数参数类型
     * @param <R> 返回值类型
     */
    @FunctionalInterface
    public interface CheckedFunction<T, R> {
        /**
         * 传入参数返回函数转化结果
         * @param t 传入参数
         * @return 函数转化结果
         * @throws Throwable 抛出异常
         */
        R apply(T t) throws Throwable;
    }

    /**
     * 抛出异常的Predicate
     * @param <T> 函数参数类型
     */
    @FunctionalInterface
    public interface CheckedPredicate<T> {
        /**
         * 对给定参数执行断言
         * @param t 输入的参数
         * @return 输入的参数是否满足条件（布尔值）
         * @throws Throwable 抛出异常
         */
        boolean test(T t) throws Throwable;
    }
}
