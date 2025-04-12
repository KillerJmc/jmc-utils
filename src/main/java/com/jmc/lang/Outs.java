package com.jmc.lang;

import lombok.AllArgsConstructor;

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
    public static void newLine(Tries.CheckedRunnable r) {
        newLine();
        Tries.tryRun(r);
        newLine();
    }

    /**
     * 控制台字体颜色枚举
     * @author Jmc
     * @since 3.3
     */
    @AllArgsConstructor
    public enum Color {
        /**
         * 黑色
         */
        BLACK("\033[30m"),

        /**
         * 红色
         */
        RED("\033[31m"),

        /**
         * 绿色
         */
        GREEN("\033[32m"),

        /**
         * 黄色
         */
        YELLOW("\033[33m"),

        /**
         * 蓝色
         */
        BLUE("\033[34m"),

        /**
         * 白色
         */
        WHITE("\033[37m"),

        /**
         * 恢复默认颜色
         */
        RESET("\033[0m");

        /**
         * 每种颜色对应的ANSI码
         */
        public final String ANSI_CODE;
    }

    /**
     * 打印颜色字符串（有换行）
     * @param s 指定的字符串
     * @param color 字体颜色
     * @since 3.3
     * @apiNote <pre>{@code
     * Outs.printColorStr("黑色", Outs.Color.BLACK);
     * Outs.printColorStr("红色", Outs.Color.RED);
     * Outs.printColorStr("绿色", Outs.Color.GREEN);
     * Outs.printColorStr("黄色", Outs.Color.YELLOW);
     * Outs.printColorStr("蓝色", Outs.Color.BLUE);
     * Outs.printColorStr("白色", Outs.Color.WHITE);
     * }</pre>
     */
    public static void printColorStr(String s, Color color) {
        // ANSI颜色字符串
        var colorStr = color.ANSI_CODE + s + Color.RESET.ANSI_CODE;

        // 判断系统是否为Windows
        var isWindows = System.getProperty("os.name").contains("Windows");

        // 如果不是Windows系统或者不是使用控制台，直接打印即可
        if (!isWindows || System.console() == null) {
            System.out.println(colorStr);
            return;
        }

        // 在java中直接打印ANSI颜色字符串在Windows CMD中会显示异常，因此执行时必须完全绕开java输出
        Tries.tryRun(() -> new ProcessBuilder("cmd", "/c", "echo", colorStr)
                // 将标准输出和错误输出都重定向到控制台，绕开java输出
                .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                .redirectError(ProcessBuilder.Redirect.INHERIT)
                .start()
                .waitFor()
        );
    }
}
