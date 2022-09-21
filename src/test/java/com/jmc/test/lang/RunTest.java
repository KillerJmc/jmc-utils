package com.jmc.test.lang;

import com.jmc.lang.Run;
import org.junit.Test;

public class RunTest {
    @Test
    public void test() {
        // 执行命令并打印结果
        Run.exec("cmd /c echo 666");

        // 执行命令并将结果放进字符串
        var res = Run.execToStr("cmd /c echo 777");
        System.out.println(res);
    }
}
