package com.jmc.test.lang;

import com.jmc.lang.Run;
import com.jmc.os.SystemInfo;
import org.junit.Test;

public class RunTest {
    @Test
    public void execTest() {
        String cmd = null, cmd2 = null;

        switch (SystemInfo.TYPE) {
            case WINDOWS -> {
                cmd = "cmd /c echo 666";
                cmd2 = "cmd /c echo 777";
            }

            case LINUX -> {
                cmd = "echo 666";
                cmd2 = "echo 777";
            }

            case UNKNOWN -> throw new RuntimeException("Unknown System Type!");
        }

        // 执行命令并打印结果
        Run.exec(cmd);

        // 执行命令并将结果放进字符串
        var res = Run.execToStr(cmd2);
        System.out.println(res);
    }
}
