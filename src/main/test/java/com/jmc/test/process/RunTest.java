package com.jmc.test.process;

import com.jmc.process.Run;
import org.junit.Test;

import java.io.IOException;

public class RunTest {
    @Test
    public void test() {
        // 执行命令并打印结果
        Run.exec("cmd /c echo 666");
    }
}
