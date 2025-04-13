package com.jmc.test.lang;

import com.jmc.lang.Switch;
import org.junit.Assert;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Objects;

public class SwitchTest {
    @Test
    public void matchExecSwitchTest() {
        Class<?> clazz = String.class;
        // 会打印“是String.class”
        Switch.match(clazz)
                .when(Integer.class, () -> System.out.println("是Integer.class"))
                .when(String.class, () -> System.out.println("是String.class"))
                .when(Double.class, () -> System.out.println("是Double.class"))
                .orElseRun(() -> System.out.println("兜底逻辑"));

        // 会打印“兜底逻辑”
        Switch.match(clazz)
                .when(Integer.class, () -> System.out.println("是Integer.class"))
                .orElseRun(() -> System.out.println("兜底逻辑"));
    }

    @Test
    public void matchReturnSwitchTest() {
        Class<?> clazz = String.class;

        // 会返回“是String.class”
        String res = Switch.match(clazz)
                .when(Integer.class, "是Integer.class")
                .when(String.class, "是String.class")
                .when(Double.class, "是Double.class")
                .orElseThrow();
        System.out.println(res);
        Assert.assertEquals("是String.class", res);

        // 匹配不上，会抛出NoSuchElementException异常
        Assert.assertThrows(
                NoSuchElementException.class,
                () -> Switch.match(clazz)
                        .when(Integer.class, "是Integer.class")
                        .orElseThrow()
        );
    }

    @Test
    public void matchReturnSupplierSwitchTest() {
        // 会返回“是String.class”
        String res = Switch.match(() -> String.class)
                .when(Integer.class, () -> "是Integer.class")
                .when(String.class, () -> "是String.class")
                .when(Double.class, () -> "是Double.class")
                .orElseThrow(() -> new IllegalArgumentException("新异常"));
        System.out.println(res);
        Assert.assertEquals("是String.class", res);

        // 匹配不上，会抛出转化后的IllegalArgumentException异常
        Assert.assertThrows(
                IllegalArgumentException.class,
                () -> Switch.match(() -> String.class)
                        .when(Integer.class, () -> "是Integer.class")
                        .orElseThrow(() -> new IllegalArgumentException("新异常"))
        );
    }

    @Test
    public void typeMatchExecSwitchTest() {
        Object obj = "123";

        // 会打印“obj是String类型”
        Switch.match(obj)
                .whenType(Integer.class, () -> System.out.println("obj是Integer类型"))
                .whenType(String.class, () -> System.out.println("obj是String类型"))
                .whenType(Double.class, () -> System.out.println("obj是Double类型"))
                .orElseRun(() -> System.out.println("兜底逻辑"));

        // 会打印“兜底逻辑”
        Switch.match(obj)
                .when(Integer.class, () -> System.out.println("obj是Integer类型"))
                .orElseRun(() -> System.out.println("兜底逻辑"));
    }

    @Test
    public void matchTypeReturnSwitchTest() {
        Object obj = "123";

        // 会返回“obj是String类型”
        String res = Switch.match(obj)
                .whenType(Integer.class, "obj是Integer类型")
                .whenType(String.class, "obj是String类型")
                .whenType(Double.class, "obj是Double类型")
                .orElseThrow();
        System.out.println(res);
        Assert.assertEquals("obj是String类型", res);

        // 匹配不上，会抛出NoSuchElementException异常
        Assert.assertThrows(
                NoSuchElementException.class,
                () -> Switch.match(obj)
                        .when(Integer.class, "obj是Integer类型")
                        .orElseThrow()
        );
    }

    @Test
    public void matchTypeReturnSupplierSwitchTest() {
        // 会返回“是String.class，长度是：3”
        String res = Switch.match(() -> "123")
                .whenType(Integer.class, i -> "是Integer.class，值是：" + i)
                .whenType(String.class, s -> "是String.class，长度是：" + s.length())
                .whenType(Double.class, d -> "是Double.class，值是：" + d)
                .orElseThrow(() -> new IllegalArgumentException("新异常"));
        System.out.println(res);
        Assert.assertEquals("是String.class，长度是：3", res);

        // 匹配不上，会抛出转化后的IllegalArgumentException异常
        Assert.assertThrows(
                IllegalArgumentException.class,
                () -> Switch.match(() -> String.class)
                        .when(Integer.class, () -> "是Integer.class")
                        .orElseThrow(() -> new IllegalArgumentException("新异常"))
        );
    }

    @Test
    public void judgeExecSwitchTest() {
        int x = 10;
        // 会打印“t等于10！”
        Switch.match(x)
                .when((Integer t) -> t > 10, () -> System.out.println("t大于10！"))
                .when((Integer t) -> t < 10, () -> System.out.println("t小于10！"))
                .when((Integer t) -> t == 10, () -> System.out.println("t等于10！"));
    }

    @Test
    public void judgeExecSwitchSpecialTest() {
        Class<?> clazz = String.class;
        // 会打印“是String.class”
        Switch.match(clazz)
                .when((Class<?> c) -> c.getName().equals(Integer.class.getName()), () -> System.out.println("是Integer.class"))
                .when((Class<?> c) -> c.getName().equals(String.class.getName()), () -> System.out.println("是String.class"))
                .when((Class<?> c) -> c.getName().equals(Double.class.getName()), () -> System.out.println("是Double.class"));
    }

    @Test
    public void judgeReturnSwitchTest() {
        int x = 10;
        // 会返回“t等于10！”
        String res = Switch.match(x)
                .when((Integer t) -> t > 10, "t大于10！")
                .when((Integer t) -> t < 10, "t小于10！")
                .when((Integer t) -> t == 10, "t等于10！")
                .orElse(null);
        System.out.println(res);
        Assert.assertEquals("t等于10！", res);

        // 会走到兜底，返回“兜底结果”
        res = Switch.match(x)
                .when((Integer t) -> t > 10, "t大于10！")
                .orElse("兜底结果");
        System.out.println(res);
        Assert.assertEquals("兜底结果", res);
    }

    @Test
    public void judgeReturnSupplierSwitchTest() {
        int x = 10;
        // 会返回“t等于10！”
        String res = Switch.match(x)
                .when((Integer t) -> t > 10, t -> "t大于10！")
                .when((Integer t) -> t < 10, t -> "t小于10！")
                .when((Integer t) -> t == 10, t -> "t等于10！")
                .orElse(null);
        System.out.println(res);
        Assert.assertEquals("t等于10！", res);

        // 会走到兜底，返回“兜底结果”
        res = Switch.match(x)
                .when((Integer t) -> t > 10, t -> "t大于10！")
                .orElseGet(() -> "兜底结果");
        System.out.println(res);
        Assert.assertEquals("兜底结果", res);
    }

    @Test
    public void judgeReturnSupplierComplexObjectSwitchTest() {
        record Student(String name) {}

        var stuJmc = new Student("Jmc");
        var stuJack = new Student("Jack");
        var stuLucy = new Student("Lucy");

        String res = Switch.match(() -> new Student("Jmc"))
                .when((Student s) -> Objects.equals(s.name(), stuJack.name()), "是Student Jack")
                .when((Student s) -> Objects.equals(s.name(), stuJmc.name()), "是Student Jmc")
                .when((Student s) -> Objects.equals(s.name(), stuLucy.name()), "是Student Lucy")
                .orElseThrow(() -> new NoSuchElementException("匹配学生失败"));

        System.out.println(res);
        Assert.assertEquals("是Student Jmc", res);
    }
}
