package com.jmc.lang;

import com.jmc.io.Files;
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
    public static void newLine(Tries.RunnableThrowsE r) {
        newLine();
        Tries.tryThis(r);
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
        private final String ANSI_CODE;
    }

    /**
     * 打印颜色字符串
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

        // 创建临时文件，并将颜色字符串存入其中
        var tempFilePath = Files.createTempFile();
        Files.out(colorStr, tempFilePath);

        // 构建打印颜色字符串的命令，具体逻辑是读取临时文件内容并打印到控制台
        // 这样做的原因是Windows终端无法直接识别命令中的ESC(\033)
        var printColorStrCmd = "cmd /c type " + tempFilePath;

        // 执行打印命令，直接将输出结果重定向到控制台
        Run.execOnConsole(printColorStrCmd);

        // 删除临时文件
        Files.delete(tempFilePath);
    }
}
