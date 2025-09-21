package com.jmc.util;

import com.jmc.lang.FunctionalInterfaces;
import com.jmc.lang.Tries;

/**
 * 计时器类
 * @since 1.0
 * @author Jmc
 */
public class Timers {

    private Timers() {}

    /**
     * 秒计时器
     * @param r 代码块
     * @param name 计时模块名称
     * @apiNote <pre>{@code
     * // 执行一个代码块，命名为test，进行秒计时（打印：test耗时：0秒）
     * Timers.secondTimer(() -> {
     *     System.out.println("666")
     * }, "test");
     * }</pre>
     */
    public static void secondTimer(FunctionalInterfaces.CheckedRunnable r, String name) {
        printTimer(name, nano(r), 9);
    }

    /**
     * 秒计时器
     * @param r 代码块
     * @apiNote <pre>{@code
     * // 执行一个代码块，进行秒计时（打印：耗时：0秒）
     * Timers.secondTimer(() -> {
     *     System.out.println("666")
     * });
     * }</pre>
     */
    public static void secondTimer(FunctionalInterfaces.CheckedRunnable r) {
        secondTimer(r, "");
    }

    /**
     * 毫秒计时器
     * @param r 代码块
     * @param name 计时模块名称
     * @apiNote <pre>{@code
     * // 执行一个代码块，命名为test，进行毫秒计时（打印：test耗时：0毫秒）
     * Timers.milliTimer(() -> {
     *     System.out.println("666")
     * }, "test");
     * }</pre>
     */
    public static void milliTimer(FunctionalInterfaces.CheckedRunnable r, String name) {
        printTimer(name, nano(r), 6);
    }

    /**
     * 毫秒计时器
     * @param r 代码块
     * @apiNote <pre>{@code
     * // 执行一个代码块，命名为test，进行毫秒计时（打印：耗时：0毫秒）
     * Timers.milliTimer(() -> {
     *     System.out.println("666")
     * });
     * }</pre>
     */
    public static void milliTimer(FunctionalInterfaces.CheckedRunnable r) {
        milliTimer(r, "");
    }

    /**
     * 纳秒计时器
     * @param r 代码块
     * @param name 计时模块名称
     * @apiNote <pre>{@code
     * // 执行一个代码块，命名为test，进行纳秒计时（打印：test耗时：0纳秒）
     * Timers.nanoTimer(() -> {
     *     System.out.println("666")
     * }, "test");
     * }</pre>
     */
    public static void nanoTimer(FunctionalInterfaces.CheckedRunnable r, String name) {
        printTimer(name, nano(r), 1);
    }

    /**
     * 纳秒计时器
     * @param r 代码块
     * @apiNote <pre>{@code
     * // 执行一个代码块，进行纳秒计时（打印：耗时：0纳秒）
     * Timers.nanoTimer(() -> {
     *     System.out.println("666")
     * });
     * }</pre>
     */
    public static void nanoTimer(FunctionalInterfaces.CheckedRunnable r) {
        nanoTimer(r, "");
    }

    /**
     * 返回模块的纳秒计时值
     * @param r 代码块
     * @return 计时的纳秒值
     */
    private static long nano(FunctionalInterfaces.CheckedRunnable r) {
        long startTime = System.nanoTime();
        Tries.tryRun(r);
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    /**
     * 打印耗时结果
     * @param name 计时模块名称
     * @param nano 计时纳秒值
     * @param pow 和纳秒相差的数量级
     */
    private static void printTimer(String name, long nano, double pow) {
        System.out.printf("%s耗时：%d%s秒\n",
                name,
                (long) (nano / Math.pow(10, pow)),
                pow == 1 ? "纳" : pow == 6 ? "毫" : "");
    }
}
