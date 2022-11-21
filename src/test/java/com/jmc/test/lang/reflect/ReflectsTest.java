package com.jmc.test.lang.reflect;

import com.jmc.lang.Outs;
import com.jmc.lang.Strs;
import com.jmc.lang.Tries;
import com.jmc.lang.reflect.Reflects;
import org.junit.Assert;
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

    @Test
    public void judgeClassInJarTest() {
        Assert.assertFalse(Reflects.isClassInJar(this.getClass()));
        Assert.assertTrue(Reflects.isClassInJar(Test.class));
    }

    @Test
    public void classPathTest() {
        // 内置类找不到类加载路径
        Reflects.getClassPath(String.class).ifPresentOrElse(
                System.out::println,
                () -> System.err.println("找不到类路径")
        );

        // 获取非在jar内的类路径
        Reflects.getClassPath(this.getClass()).ifPresentOrElse(
                System.out::println,
                () -> { throw new RuntimeException("找不到类路径"); }
        );

        // 获取在jar内的类路径
        Reflects.getClassPath(Test.class).ifPresentOrElse(
                System.out::println,
                () -> { throw new RuntimeException("找不到类路径"); }
        );
    }

    @Test
    public void listResourceTest() {
        // 普通类路径（非jar路径）
        Reflects.listResources(ReflectsTest.class, "/com/jmc/test")
                .forEach(System.out::println);

        Outs.newLine();

        // jar路径
        Reflects.listResources(Test.class, "/org/junit")
                .forEach(System.out::println);
    }
}
