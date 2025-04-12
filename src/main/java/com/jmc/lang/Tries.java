package com.jmc.lang;

import lombok.NonNull;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * try增强类
 * @since 1.0
 * @author Jmc
 */
public class Tries {

    private Tries() {}

    /**
     * 可抛出异常的Runnable
     */
    public interface CheckedRunnable {
        /**
         * 执行方法
         * @throws Throwable 抛出的异常
         */
        void run() throws Throwable;
    }

    /**
     * 可抛出Throwable异常的Supplier
     * @param <T> 返回值类型
     * @since 2.9
     */
    public interface CheckedSupplier<T> {
        /**
         * 执行方法
         * @return 方法的返回值
         * @throws Throwable 抛出的异常
         */
        T call() throws Throwable;
    }

    /**
     * 可抛出异常的Consumer
     * @param <T> 被消耗的对象
     * @since 1.5
     */
    @FunctionalInterface
    public interface CheckedConsumer<T> {
        /**
         * 消耗方法
         * @param t 消耗对象
         * @throws Throwable 抛出的异常
         */
        void accept(T t) throws Throwable;
    }

    /**
     * 执行需要被try包含的代码块，直接抛出运行时异常
     * @param r 代码块
     * @apiNote <pre>{@code
     * // 让当前线程睡眠3ms，并直接抛出运行时异常（无需捕获）
     * Tries.tryRun(() -> Thread.sleep(3));
     * }</pre>
     */
    public static void tryRun(@NonNull CheckedRunnable r) {
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
     * Tries.tryRunOrHandle(() -> Thread.sleep(3), Throwable::printStackTrace);
     * }</pre>
     * @since 1.5
     */
    public static void tryRunOrHandle(@NonNull CheckedRunnable r,
                                      @NonNull Consumer<Throwable> exceptionHandler) {
        try {
            r.run();
        } catch (Throwable e) {
            exceptionHandler.accept(e);
        }
    }

    /**
     * 执行需要被try包含的代码块并转化异常抛出
     * @param r 代码块
     * @param exceptionConverter 异常处理器
     * @apiNote <pre>{@code
     * // 让当前线程睡眠3ms，并转化异常为业务异常（是运行时异常）并抛出
     * Tries.tryRunOrThrow(() -> Thread.sleep(3), e -> new BusinessException(e));
     * }</pre>
     * @since 3.9
     */
    public static void tryRunOrThrow(@NonNull CheckedRunnable r,
                                     @NonNull Function<Throwable, ? extends RuntimeException> exceptionConverter) {
        try {
            r.run();
        } catch (Throwable e) {
            var convertedException = exceptionConverter.apply(e);
            throw Objects.isNull(convertedException) ? new RuntimeException(e) : convertedException;
        }
    }

    /**
     * 执行需要被try包含的代码块并获取可能的异常
     * @param r 代码块
     * @return 异常对象
     * @apiNote <pre>{@code
     * // 让当前线程睡眠3ms，并返回异常
     * var optionalE = Tries.tryRunOrCapture(() -> Thread.sleep(3));
     * // 处理异常（进行打印）
     * optionalE.ifPresent(Throwable::printStackTrace);
     * }</pre>
     * @since 2.4
     */
    public static Optional<Throwable> tryRunOrCapture(@NonNull CheckedRunnable r) {
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
     * var class = Tries.tryGet(() -> Class.forName("java.lang.String"));
     * }</pre>
     */
    public static <T> T tryGet(@NonNull CheckedSupplier<T> c) {
        try {
            return c.call();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 执行需要被try包含的代码块并返回结果和消费异常
     * @param c 代码块
     * @param exceptionHandler 异常处理器
     * @param <T> 返回结果类型
     * @return 结果
     * @apiNote <pre>{@code
     * // 获取String的Class对象，如果有异常直接打印出来
     * var clz = Tries.tryGetOrHandle(
     *         () -> Class.forName("java.lang.String"),
     *         Throwable::printStackTrace
     * );
     * }</pre>
     * @since 1.5
     */
    public static <T> T tryGetOrHandle(@NonNull CheckedSupplier<T> c,
                                       @NonNull Consumer<Throwable> exceptionHandler) {
        try {
            return c.call();
        } catch (Throwable e) {
            exceptionHandler.accept(e);
        }
        return null;
    }

    /**
     * 执行需要被try包含的代码块并返回结果，如果有异常进行转化为运行时异常重新抛出
     * @param c 代码块
     * @param exceptionConverter 异常转换器，把原始异常转化为运行时异常重新抛出
     * @param <T> 返回结果类型
     * @return 结果
     * @apiNote <pre>{@code
     * // 获取String的Class对象，如果有异常就转化为业务异常（是运行时异常）抛出
     * var clz = Tries.tryGetOrThrow(
     *         () -> Class.forName("java.lang.String"),
     *         e -> new BusinessException("获取Class对象失败，原因：" + e.getMessage())
     * );
     * }</pre>
     * @since 3.9
     */
    public static <T> T tryGetOrThrow(@NonNull CheckedSupplier<T> c,
                                      @NonNull Function<Throwable, ? extends RuntimeException> exceptionConverter) {
        try {
            return c.call();
        } catch (Throwable e) {
            var convertedException = exceptionConverter.apply(e);
            throw Objects.isNull(convertedException) ? new RuntimeException(e) : convertedException;
        }
    }

    /**
     * 执行需要被try包含的代码块并返回Optional结果，如果有异常进行转化为运行时异常重新抛出
     * @param c 代码块
     * @param exceptionConverter 异常转换器，把原始异常转化为运行时异常重新抛出
     * @param <T> 返回结果类型
     * @return 结果Optional
     * @apiNote <pre>{@code
     * // 通过接口获取用户信息，出现异常就转化为业务异常（是运行时异常）抛出
     * var user = Tries.tryGetOptionalOrThrow(
     *         () -> UserClient.getUserInfo(...),
     *         e -> new BusinessException("获取用户信息失败，原因：" + e.getMessage())
     * );
     *
     * // 对Optional结果进行再次处理
     * if (userInfo.isPresent()) {
     *     // 进一步的操作（激活用户）
     *     userService.activateUser(user);
     * } else {
     *     log.warn("获取到用户信息为空，本次不做激活操作");
     * }
     * }</pre>
     * @since 3.9
     */
    public static <T> Optional<T> tryGetOptionalOrThrow(@NonNull CheckedSupplier<T> c,
                                                        @NonNull Function<Throwable, ? extends RuntimeException> exceptionConverter) {
        try {
            return Optional.ofNullable(c.call());
        } catch (Throwable e) {
            var convertedException = exceptionConverter.apply(e);
            throw Objects.isNull(convertedException) ? new RuntimeException(e) : convertedException;
        }
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
     *       .forEach(Tries.unchecked(Thread::join));
     * }</pre>
     * @since 1.5
     */
    public static <T> Consumer<T> unchecked(@NonNull CheckedConsumer<T> c) {
        return t -> Tries.tryRun(() -> c.accept(t));
    }
}
