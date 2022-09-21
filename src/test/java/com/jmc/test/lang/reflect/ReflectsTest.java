package com.jmc.test.lang.reflect;

import com.jmc.lang.Strs;
import com.jmc.lang.Tries;
import com.jmc.lang.reflect.Reflects;
import org.junit.Test;

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

    @Test
    public void getFieldAndFailedTest() {
        Tries.tryHandlesE(() -> Reflects.getStaticField(String.class, "COMPACT_STRINGS"),
                System.err::println);
        Tries.tryHandlesE(() -> Reflects.getField("", "coder"),
                System.err::println);
    }

    @Test
    public void illegalAccessTest() {
        Tries.tryHandlesE(() -> Reflects.getStaticMethod(Strs.class, "isNum"),
                System.err::println);
    }
}
