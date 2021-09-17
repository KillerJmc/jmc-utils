package com.jmc.lang.extend;

import java.util.concurrent.Callable;

/**
 * try增强类
 * @since 1.0.0
 * @author Jmc
 */
@SuppressWarnings("unused")
public class Tries {
    /**
     * 执行需要被try包含的代码块
     * @param r 代码块
     */
    public static void tryThis(RunnableThrowsException r) {
        try {
            r.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行需要被try包含的代码块并返回结果
     * @param c 代码块
     * @param <T> 返回结果类型
     * @return 结果
     */
    public static<T> T tryReturnsT(Callable<T> c) {
        try {
            return c.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 可抛出异常的代码块
     */
    public interface RunnableThrowsException {
        void run() throws Exception;
    }
}
