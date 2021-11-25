package com.jmc.test.lang.reflect;

import com.jmc.lang.reflect.Reflects;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ReflectsTest {
    @Test
    public void objTest() {
        var bytes = Reflects.outObj("666");
        System.out.println((String) Reflects.readObj(bytes));
    }

    @Test
    public void invokeTest() {
        // 执行静态方法
        List<?> l = Reflects.invokeStaticMethod(List.class, "of");
        System.out.println(l);

        var s = "     abc     ";
        // 执行成员方法
        String res = Reflects.invokeMethod(s, "trim");
        System.out.println("|" + res + "|");
    }
}
