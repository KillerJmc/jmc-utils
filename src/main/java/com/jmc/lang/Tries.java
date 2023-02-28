package com.jmc.lang;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * try增强类
 * @since 1.0
 * @author Jmc
 */
public class Tries {

    private Tries() {}

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
     * 可抛出异常的带返回值的代码块接口
     * @param <T> 返回值类型
     * @since 2.9
     */
    public interface ReturnedThrowable<T> {
        /**
         * 执行方法
         * @return 方法的返回值
         * @throws Throwable 抛出的异常
         */
        T call() throws Throwable;
    }

    /**
     * 执行需要被try包含的代码块，直接抛出运行时异常
     * @param r 代码块
     * @apiNote <pre>{@code
     * // 让当前线程睡眠3ms，并直接抛出运行时异常（无需捕获）
     * Tries.tryThis(() -> Thread.sleep(3));
     * }</pre>
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
     * @apiNote <pre>{@code
     * // 让当前线程睡眠3ms，并处理异常（进行打印）
     * Tries.tryHandlesE(() -> Thread.sleep(3), Throwable::printStackTrace);
     * }</pre>
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
     * 执行需要被try包含的代码块并返回异常
     * @param r 代码块
     * @return 异常对象
     * @apiNote <pre>{@code
     * // 让当前线程睡眠3ms，并返回异常
     * var optionalE = Tries.tryHandlesE(() -> Thread.sleep(3));
     * // 处理异常（进行打印）
     * optionalE.ifPresent(Throwable::printStackTrace);
     * }</pre>
     * @since 2.4
     */
    public static Optional<Throwable> tryReturnsE(RunnableThrowsE r) {
        try {
            r.run();
        } catch (Throwable e) {
            return Optional.of(e);
        }
        return Optional.empty();
    }

    /**
     * 执行需要被try包含的代码块并返回结果，直接抛出运行时异常
     * @param c 代码块
     * @param <T> 返回结果类型
     * @return 结果
     * @apiNote <pre>{@code
     * // 获取String的Class对象，直接抛出运行时异常（无需捕获）
     * var class = Tries.tryReturnsT(() -> Class.forName("java.lang.String"));
     * }</pre>
     */
    public static <T> T tryReturnsT(ReturnedThrowable<T> c) {
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
     * @apiNote <pre>{@code
     * // 获取String的Class对象，如果有异常直接打印出来
     * var class = Tries.tryReturnsT(
     *         () -> Class.forName("java.lang.String"),
     *         Throwable::printStackTrace
     * );
     * }</pre>
     * @since 1.5
     */
    @SuppressWarnings("unchecked")
    public static <T, E extends Throwable> T tryReturnsT(ReturnedThrowable<T> c, Consumer<E> exceptionHandler) {
        try {
            return c.call();
        } catch (Throwable e) {
            exceptionHandler.accept((E) e);
        }
        return null;
    }

    /**
     * 把抛出异常的Consumer转化为普通的Consumer，并直接抛出运行时异常
     * @param c 抛出异常的Consumer
     * @param <T> 被消耗的对象
     * @return 普通的Consumer
     * @apiNote <pre>{@code
     * // 定义3个线程，启动并阻塞等待结束
     * Stream.generate(() -> (Runnable) () -> System.out.println(666))
     *       .limit(3)
     *       .map(Thread::new)
     *       .peek(Thread::start)
     *       // 简写抛出Stream中的异常
     *       .forEach(Tries.throwsE(Thread::join));
     * }</pre>
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
    @FunctionalInterface
    public interface ConsumerThrowsE<T> {
        /**
         * 消耗方法
         * @param t 消耗对象
         * @throws Throwable 抛出的异常
         */
        void accept(T t) throws Throwable;
    }
}
