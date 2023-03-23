package com.jmc.test.lang;

import com.jmc.lang.Outs;
import org.junit.Test;

public class OutsTest {
    @Test
    public void newLineTest() {
        // 输出分割线
        Outs.newLine();

        // 代码块开始和结束都输出分割线
        Outs.newLine(() -> {
            System.out.println(666);
            System.out.println(777);
            System.out.println(888);
        });
    }

    @Test
    public void printColorStrTest() {
        Outs.printColorStr("黑色", Outs.Color.BLACK);
        Outs.printColorStr("红色", Outs.Color.RED);
        Outs.printColorStr("绿色", Outs.Color.GREEN);
        Outs.printColorStr("黄色", Outs.Color.YELLOW);
        Outs.printColorStr("蓝色", Outs.Color.BLUE);
        Outs.printColorStr("白色", Outs.Color.WHITE);
    }
}
