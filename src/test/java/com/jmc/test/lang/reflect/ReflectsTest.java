package com.jmc.test.lang.reflect;

import com.jmc.lang.Outs;
import com.jmc.lang.Strs;
import com.jmc.lang.Tries;
import com.jmc.lang.reflect.Reflects;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class ReflectsTest {
    @Test
    public void instanceTest() {
        // 构建对象
        var sb = Reflects.newInstance(StringBuilder.class, "ABC");

        // 调用append方法
        Reflects.invokeMethod(sb, "append", "DEF");
        // 调用toString方法
        String res = Reflects.invokeMethod(sb, "toString");

        Assert.assertEquals("ABCDEF", res);
    }

    @Test
    public void invokeTest() {
        // 执行静态方法
        List<?> l = Reflects.invokeMethod(List.class, "of");
        Assert.assertEquals("[]", l.toString());

        var s = "     abc     ";
        // 执行成员方法
        String res = Reflects.invokeMethod(s, "trim");
        Assert.assertEquals("abc", res);
    }

    @Test
    public void moduleNotOpenTest() {
        Tries.tryHandlesE(() -> Reflects.getFieldValue(String.class, "COMPACT_STRINGS"),
                System.err::println);
        Tries.tryHandlesE(() -> Reflects.getFieldValue("", "value"),
                System.err::println);
    }

    @Test
    public void illegalAccessTest() {
        Tries.tryHandlesE(() -> Reflects.getMethod(Strs.class, "isNum"),
                System.err::println);
    }

    @Test
    public void serializeTest() {
        var bytes = Reflects.outObj("666");
        String obj = Reflects.readObj(bytes);
        Assert.assertEquals("666", obj);
    }

    @Test
    public void getJarPathTest() {
        var jarPath = Reflects.getJarPath(Test.class);
        var jarFile = new File(jarPath);
        Assert.assertTrue(jarFile.exists() && jarFile.getName().endsWith(".jar"));
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
