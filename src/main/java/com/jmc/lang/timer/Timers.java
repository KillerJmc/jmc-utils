package com.jmc.lang.timer;

import com.jmc.lang.extend.Tries;

/**
 * 计时器类
 * @since 1.0
 * @author Jmc
 */
@SuppressWarnings("unused")
public class Timers {
    /**
     * 秒计时器
     * @param r 代码块
     * @param name 计时模块名称
     */
    public static void secondTimer(Tries.RunnableThrowsE r, String name) {
        printTimer(name, nano(r), 9);
    }

    /**
     * 秒计时器
     * @param r 代码块
     */
    public static void secondTimer(Tries.RunnableThrowsE r) {
        secondTimer(r, "");
    }

    /**
     * 毫秒计时器
     * @param r 代码块
     * @param name 计时模块名称
     */
    public static void milliTimer(Tries.RunnableThrowsE r, String name) {
        printTimer(name, nano(r), 6);
    }

    /**
     * 毫秒计时器
     * @param r 代码块
     */
    public static void milliTimer(Tries.RunnableThrowsE r) {
        milliTimer(r, "");
    }

    /**
     * 纳秒计时器
     * @param r 代码块
     * @param name 计时模块名称
     */
    public static void nanoTimer(Tries.RunnableThrowsE r, String name) {
        printTimer(name, nano(r), 1);
    }

    /**
     * 纳秒计时器
     * @param r 代码块
     */
    public static void nanoTimer(Tries.RunnableThrowsE r) {
        nanoTimer(r, "");
    }

    /**
     * 返回模块的纳秒计时值
     * @param r 代码块
     * @return 计时的纳秒值
     */
    private static long nano(Tries.RunnableThrowsE r) {
        long startTime = System.nanoTime();
        Tries.tryThis(r);
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
