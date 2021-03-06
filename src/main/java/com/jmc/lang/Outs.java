package com.jmc.lang;

/**
 * 输出增强类
 * @since 1.0
 * @author Jmc
 */
@SuppressWarnings("unused")
public class Outs {

    private Outs() {}

    /**
     * 输出分隔符
     */
    public static void newLine() {
        System.out.println("--------------------------------------------------------------------------");
    }

    /**
     * 在代码块的输出部分前后插入分隔符
     * @param r 代码块
     */
    public static void newLine(Tries.RunnableThrowsE r) {
        newLine();
        Tries.tryThis(r);
        newLine();
    }
}
