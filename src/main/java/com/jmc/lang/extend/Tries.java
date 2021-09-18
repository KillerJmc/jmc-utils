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
    /**
     * 执行需要被try包含的代码块，直接打印异常
     * @param r 代码块
     */
    public static void tryThis(RunnableThrowsE r) {
        try {
            r.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行需要被try包含的代码块并处理异常
     * @param r 代码块
     * @param exceptionHandler 异常处理器
     * @since 1.5
     */
    public static void tryHandlesE(RunnableThrowsE r, Consumer<Exception> exceptionHandler) {
        try {
            r.run();
        } catch (Exception e) {
            exceptionHandler.accept(e);
        }
    }


    /**
     * 可抛出异常的代码块接口
     */
    public interface RunnableThrowsE {
        void run() throws Exception;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 执行需要被try包含的代码块并返回结果和处理异常
     * @param c 代码块
     * @param exceptionHandler 异常处理器
     * @param <T> 返回结果类型
     * @return 结果
     * @since 1.5
     */
    @SuppressWarnings("unchecked")
    public static <T, E extends Exception> T tryReturnsT(Callable<T> c, Consumer<E> exceptionHandler) {
        try {
            return c.call();
        } catch (Exception e) {
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
        void accept(T t) throws Exception;
    }
}
