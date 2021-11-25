package com.jmc.test.lang.timer;

import com.jmc.lang.timer.Timers;
import org.junit.Test;

public class TimersTest {
    @Test
    public void test() {
        // 秒计时器
        Timers.secondTimer(() -> System.out.println("666"), "test1");

        // 毫秒计时器
        Timers.milliTimer(() -> System.out.println("777"), "test2");

        // 纳秒计时器
        Timers.nanoTimer(() -> System.out.println("888"), "test3");
    }
}
