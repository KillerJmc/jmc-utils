package com.jmc.test.os;

import com.jmc.os.SystemInfo;
import org.junit.Test;

public class SystemInfoTest {
    @Test
    public void test() {
        // 打印系统类型
        System.out.println(SystemInfo.TYPE);
    }
}
