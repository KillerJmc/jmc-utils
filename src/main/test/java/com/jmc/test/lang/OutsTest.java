package com.jmc.test.lang;

import com.jmc.lang.Outs;
import org.junit.Test;

public class OutsTest {
    @Test
    public void test() {
        // 输出分割线
        Outs.newLine();

        // 代码块开始和结束都输出分割线
        Outs.newLine(() -> {
            System.out.println(666);
            System.out.println(777);
            System.out.println(888);
        });
    }
}
