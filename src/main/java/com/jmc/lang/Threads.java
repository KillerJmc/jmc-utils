package com.jmc.lang;

import java.util.concurrent.TimeUnit;

/**
 * 线程增强类
 * @since 1.5
 * @author Jmc
 */
public class Threads {

    private Threads() {}

    /**
     * 线程以毫秒为单位睡眠一段时间，直接抛出中断异常
     * @param millis 休眠毫秒值
     * @apiNote <pre>{@code
     * // 让当前线程睡眠3ms
     * Threads.sleep(3);
     * }</pre>
     */
    public static void sleep(long millis) {
        Tries.tryRun(() -> Thread.sleep(millis));
    }

    /**
     * 线程以特定时间单位休眠一段时间，直接抛出中断异常
     * @param timeout 休眠时间
     * @param unit 时间单位
     * @apiNote <pre>{@code
     * // 让当前线程睡眠3分钟
     * Threads.sleep(3, TimeUnit.MINUTES);
     * }</pre>
     */
    public static void sleep(long timeout, TimeUnit unit) {
        Tries.tryRun(() -> unit.sleep(timeout));
    }
}
