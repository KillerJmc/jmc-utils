package com.jmc.test.lang;

import com.jmc.lang.Threads;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class ThreadsTest {
    @Test
    public void test() {
        System.out.println(1);
        Threads.sleep(TimeUnit.MILLISECONDS, 500);
        System.out.println(2);
        Threads.sleep(500);
    }
}
