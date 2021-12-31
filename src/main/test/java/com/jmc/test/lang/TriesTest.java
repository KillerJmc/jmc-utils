package com.jmc.test.lang;

import com.jmc.lang.Tries;
import org.junit.Test;

import java.util.stream.Stream;

public class TriesTest {
    @Test
    public void StreamTest() {
        Stream.generate(() -> (Runnable) () -> System.out.println(666))
                .limit(3)
                .map(Thread::new)
                .peek(Thread::start)
                // 简写抛出Stream中的异常
                .forEach(Tries.throwsE(Thread::join));
    }

    @Test
    public void normalTest() {
        var t = new Thread(() -> System.out.println(777));
        t.start();
        // 简写直接抛出异常
        Tries.tryThis(t::join);
    }

    @Test
    public void ReturnTest() {
        // 有返回值的形式，直接抛出异常
        var c = Tries.tryReturnsT(() -> Class.forName("java.lang.String"));
        System.out.println(c);
    }

    @Test
    public void handleExceptionTest() {
        Tries.tryHandlesE(() -> { throw new RuntimeException("异常啦！"); }, e -> {
            // 处理异常
            System.out.println("发生异常e：" + e.getMessage());
        });
    }
}
