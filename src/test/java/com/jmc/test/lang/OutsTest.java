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
        Outs.printColorStr("Black", Outs.Color.BLACK);
        Outs.printColorStr("Red", Outs.Color.RED);
        Outs.printColorStr("Green", Outs.Color.GREEN);
        Outs.printColorStr("Yellow", Outs.Color.YELLOW);
        Outs.printColorStr("Blue", Outs.Color.BLUE);
        Outs.printColorStr("White", Outs.Color.WHITE);
    }
}
