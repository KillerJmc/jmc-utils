package com.jmc.test.lang;

import com.jmc.lang.Run;
import org.junit.Test;

public class RunTest {
    @Test
    public void test() {
        // 执行命令并打印结果
        Run.exec("cmd /c echo 666");
    }
}
