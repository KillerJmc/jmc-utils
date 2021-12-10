package com.jmc.lang.extend;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

/**
 * try增强类
 * @since 1.0
 * @author Jmc
 */
@SuppressWarnings("unused")
public class Tries {

    private Tries() {}

    /**
     * 执行需要被try包含的代码块，直接打印异常
     * @param r 代码块
     */
    public static void tryThis(RunnableThrowsE r) {
        try {
            r.run();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 执行需要被try包含的代码块并处理异常
     * @param r 代码块
     * @param exceptionHandler 异常处理器
     * @since 1.5
     */
    public static void tryHandlesE(RunnableThrowsE r, Consumer<Throwable> exceptionHandler) {
        try {
            r.run();
        } catch (Throwable e) {
            exceptionHandler.accept(e);
        }
    }


    /**
     * 可抛出异常的代码块接口
     */
    public interface RunnableThrowsE {
        /**
         * 执行方法
         * @throws Throwable 抛出的异常
         */
        void run() throws Throwable;
    }

    /**
     * 执行需要被try包含的代码块并返回结果，直接打印异常
     * @param c 代码块
     * @param <T> 返回结果类型
     * @return 结果
     */
    public static <T> T tryReturnsT(Callable<T> c) {
        try {
            return c.call();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 执行需要被try包含的代码块并返回结果和处理异常
     * @param c 代码块
     * @param exceptionHandler 异常处理器
     * @param <T> 返回结果类型
     * @param <E> 异常类型
     * @return 结果
     * @since 1.5
     */
    @SuppressWarnings("unchecked")
    public static <T, E extends Throwable> T tryReturnsT(Callable<T> c, Consumer<E> exceptionHandler) {
        try {
            return c.call();
        } catch (Throwable e) {
            exceptionHandler.accept((E) e);
        }
        return null;
    }

    /**
     * 把抛出异常的Consumer转化为普通的Consumer，并直接抛出异常
     * @param c 抛出异常的Consumer
     * @param <T> 被消耗的对象
     * @return 普通的Consumer
     * @since 1.5
     */
    public static <T> Consumer<T> throwsE(ConsumerThrowsE<T> c) {
        return t -> Tries.tryThis(() -> c.accept(t));
    }

    /**
     * 可抛出异常的Consumer
     * @param <T> 被消耗的对象
     * @since 1.5
     */
    public interface ConsumerThrowsE<T> {
        /**
         * 消耗方法
         * @param t 消耗对象
         * @throws Throwable 抛出的异常
         */
        void accept(T t) throws Throwable;
    }
}
