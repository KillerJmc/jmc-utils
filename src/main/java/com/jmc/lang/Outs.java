package com.jmc.lang;

/**
 * 输出增强类
 * @since 1.0
 * @author Jmc
 */
public class Outs {

    private Outs() {}

    /**
     * 输出分割行
     * @apiNote <pre>{@code
     * System.out.println("a = 3");
     * // 输出分割行
     * Outs.newLine();
     * System.out.println("b = 4");
     * }</pre>
     */
    public static void newLine() {
        System.out.println("--------------------------------------------------------------------------");
    }

    /**
     * 在代码块的输出部分前后插入分割行
     * @param r 代码块
     * @apiNote <pre>{@code
     * Outs.newLine(() -> {
     *     // 在输出666前后都加上分割行
     *     System.out.println("666");
     * });
     * }</pre>
     */
    public static void newLine(Tries.RunnableThrowsE r) {
        newLine();
        Tries.tryThis(r);
        newLine();
    }
}
