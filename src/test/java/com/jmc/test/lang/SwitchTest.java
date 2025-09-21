package com.jmc.test.lang;

import com.jmc.lang.Switch;
import com.jmc.lang.ref.Func;
import com.jmc.lang.ref.Pointer;
import org.junit.Assert;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Objects;

public class SwitchTest {
    @Test
    public void caseGet() {
        Func<String> f = Func.of((Object obj) ->
                Switch.of(obj)
                        .caseObj("caseObj返回对象条件", "caseObj返回对象")
                        .caseObj("caseObj返回对象条件2", s -> s + "!")
                        .caseType(Integer.class, "caseType整数")
                        .caseType(Long.class, l -> "caseType长整数" + l)
                        .caseWhen((Double d) -> d > 3, "caseWhen浮点数大于3")
                        .caseWhen((Double d) -> d < 3, d -> "caseWhen浮点数小于3，是" + d)
                        .caseNull("caseNull")
                        .caseNull(() -> "caseNull提供对象")
                        .orElse("没命中")
        );

        Assert.assertEquals("caseObj返回对象", f.invoke("caseObj返回对象条件"));
        Assert.assertEquals("caseObj返回对象条件2!", f.invoke("caseObj返回对象条件2"));
        Assert.assertEquals("caseType整数", f.invoke(1));
        Assert.assertEquals("caseType长整数666", f.invoke(666L));
        Assert.assertEquals("caseWhen浮点数大于3", f.invoke(10.0));
        Assert.assertEquals("caseWhen浮点数小于3，是1", f.invoke(1.0));
        Assert.assertEquals("caseNull", f.invoke((Object) null));
        Assert.assertEquals("没命中", f.invoke("你好呀"));
    }

    @Test
    public void caseGetNullSupplierOrElseGet() {

    }

    @Test
    public void caseGetOrElseThrow() {

    }

    @Test
    public void caseGetOrElseThrowCustom() {

    }

    @Test
    public void caseRun() {

    }

//    @Test
//    public void caseTypeAndRunTest() {
//        var strPtr = Pointer.empty();
//
//        Class<?> clazz = String.class;
//        // 会打印“是String.class”
//        Switch.of(clazz)
//                .caseType(Integer.class, () -> strPtr.reset("是Integer.class"))
//                .caseType(String.class, () -> strPtr.reset("是String.class"))
//                .caseType(Double.class, () -> strPtr.reset("是Double.class"))
//                .orElseRun(() -> strPtr.reset("兜底逻辑"));
//        Assert.assertEquals("是String.class", strPtr.get());
//        strPtr.reset(null);
//
//        // 会打印“兜底逻辑”
//        Switch.of(clazz)
//                .caseType(Integer.class, () -> strPtr.reset("是Integer.class"))
//                .orElseRun(() -> strPtr.reset("兜底逻辑"));
//        Assert.assertEquals("兜底逻辑", strPtr.get());
//
//    }
//
//    @Test
//    public void caseTypeAndGetTest() {
//        Class<?> clazz = String.class;
//
//        // 会返回“是String.class”
//        String res = Switch.of(clazz)
//                .caseType(Integer.class, "是Integer.class")
//                .caseType(String.class, "是String.class")
//                .caseType(Double.class, "是Double.class")
//                .orElseThrow();
//        Assert.assertEquals("是String.class", res);
//
//        // 匹配不上，会抛出NoSuchElementException异常
//        Assert.assertThrows(
//                NoSuchElementException.class,
//                () -> Switch.of(clazz)
//                        .caseType(Integer.class, "是Integer.class")
//                        .orElseThrow()
//        );
//    }
//
//    @Test
//    public void caseTypeAndGetThenElseThrowTest() {
//        // 会返回“是String.class”
//        String res = Switch.of(() -> String.class)
//                .caseType(Integer.class, i -> "是Integer.class")
//                .caseType(String.class, s -> "是String.class")
//                .caseType(Double.class, d -> "是Double.class")
//                .orElseThrow(() -> new IllegalArgumentException("新异常"));
//        Assert.assertEquals("是String.class", res);
//
//        // 匹配不上，会抛出转化后的IllegalArgumentException异常
//        Assert.assertThrows(
//                IllegalArgumentException.class,
//                () -> Switch.of(() -> String.class)
//                        .caseType(Integer.class, i -> "是Integer.class")
//                        .orElseThrow(() -> new IllegalArgumentException("新异常"))
//        );
//    }
//
//    @Test
//    public void caseTypeAndGetThenRunTest() {
//        var strPtr = Pointer.empty();
//        Object obj = "123";
//
//        // 会打印“obj是String类型”
//        Switch.of(obj)
//                .caseType(Integer.class, () -> strPtr.reset("obj是Integer类型"))
//                .caseType(String.class, () -> strPtr.reset("obj是String类型"))
//                .caseType(Double.class, () -> strPtr.reset("obj是Double类型"))
//                .orElseRun(() -> strPtr.reset("兜底逻辑"));
//        Assert.assertEquals("obj是String类型", strPtr.get());
//        strPtr.reset(null);
//
//        // 会打印“兜底逻辑”
//        Switch.of(obj)
//                .caseType(Integer.class, () -> strPtr.reset("obj是Integer类型"))
//                .orElseRun(() -> strPtr.reset("兜底逻辑"));
//        Assert.assertEquals("兜底逻辑", strPtr.get());
//    }
//
//    @Test
//    public void matchTypeReturnSwitchTest() {
//        Object obj = "123";
//
//        // 会返回“obj是String类型”
//        String res = Switch.of(obj)
//                .caseType(Integer.class, "obj是Integer类型")
//                .caseType(String.class, "obj是String类型")
//                .caseType(Double.class, "obj是Double类型")
//                .orElseThrow();
//        strPtr.reset(res);
//        Assert.assertEquals("obj是String类型", res);
//
//        // 匹配不上，会抛出NoSuchElementException异常
//        Assert.assertThrows(
//                NoSuchElementException.class,
//                () -> Switch.of(obj)
//                        .caseType(Integer.class, "obj是Integer类型")
//                        .orElseThrow()
//        );
//    }
//
//    @Test
//    public void matchTypeReturnSupplierSwitchTest() {
//        // 会返回“是String.class，长度是：3”
//        String res = Switch.of(() -> "123")
//                .caseType(Integer.class, i -> "是Integer.class，值是：" + i)
//                .caseType(String.class, s -> "是String.class，长度是：" + s.length())
//                .caseType(Double.class, d -> "是Double.class，值是：" + d)
//                .orElseThrow(() -> new IllegalArgumentException("新异常"));
//        strPtr.reset(res);
//        Assert.assertEquals("是String.class，长度是：3", res);
//
//        // 匹配不上，会抛出转化后的IllegalArgumentException异常
//        Assert.assertThrows(
//                IllegalArgumentException.class,
//                () -> Switch.of(() -> String.class)
//                        .caseType(Integer.class, i -> "是Integer.class")
//                        .orElseThrow(() -> new IllegalArgumentException("新异常"))
//        );
//    }
//
//    @Test
//    public void judgeExecSwitchTest() {
//        int x = 10;
//        // 会打印“t等于10！”
//        Switch.of(x)
//                .caseWhen((Integer t) -> t > 10, () -> strPtr.reset("t大于10！"))
//                .caseWhen((Integer t) -> t < 10, () -> strPtr.reset("t小于10！"))
//                .caseWhen((Integer t) -> t == 10, () -> strPtr.reset("t等于10！"));
//    }
//
//    @Test
//    public void judgeExecSwitchSpecialTest() {
//        Class<?> clazz = String.class;
//        // 会打印“是String.class”
//        Switch.of(clazz)
//                .caseWhen((Class<?> c) -> c == Integer.class, () -> strPtr.reset("是Integer.class"))
//                .caseWhen((Class<?> c) -> c == String.class, () -> strPtr.reset("是String.class"))
//                .caseWhen((Class<?> c) -> c == Double.class, () -> strPtr.reset("是Double.class"));
//    }
//
//    @Test
//    public void judgeReturnSwitchTest() {
//        int x = 10;
//        // 会返回“t等于10！”
//        String res = Switch.of(x)
//                .caseWhen((Integer t) -> t > 10, "t大于10！")
//                .caseWhen((Integer t) -> t < 10, "t小于10！")
//                .caseWhen((Integer t) -> t == 10, "t等于10！")
//                .orElse(null);
//        strPtr.reset(res);
//        Assert.assertEquals("t等于10！", res);
//
//        // 会走到兜底，返回“兜底结果”
//        res = Switch.of(x)
//                .caseWhen((Integer t) -> t > 10, "t大于10！")
//                .orElse("兜底结果");
//        strPtr.reset(res);
//        Assert.assertEquals("兜底结果", res);
//    }
//
//    @Test
//    public void judgeReturnSupplierSwitchTest() {
//        int x = 10;
//        // 会返回“t等于10！”
//        String res = Switch.of(x)
//                .caseWhen((Integer t) -> t > 10, t -> "t大于10！")
//                .caseWhen((Integer t) -> t < 10, t -> "t小于10！")
//                .caseWhen((Integer t) -> t == 10, t -> "t等于10！")
//                .orElse(null);
//        strPtr.reset(res);
//        Assert.assertEquals("t等于10！", res);
//
//        // 会走到兜底，返回“兜底结果”
//        res = Switch.of(x)
//                .caseWhen((Integer t) -> t > 10, t -> "t大于10！")
//                .orElse(() -> "兜底结果");
//        strPtr.reset(res);
//        Assert.assertEquals("兜底结果", res);
//    }
//
//    @Test
//    public void judgeReturnSupplierComplexObjectSwitchTest() {
//        record Student(String name) {}
//
//        var stuJmc = new Student("Jmc");
//        var stuJack = new Student("Jack");
//        var stuLucy = new Student("Lucy");
//
//        String res = Switch.of(() -> new Student("Jmc"))
//                .caseWhen((Student s) -> Objects.equals(s.name(), stuJack.name()), "是Student Jack")
//                .caseWhen((Student s) -> Objects.equals(s.name(), stuJmc.name()), "是Student Jmc")
//                .caseWhen((Student s) -> Objects.equals(s.name(), stuLucy.name()), "是Student Lucy")
//                .orElseThrow(() -> new NoSuchElementException("匹配学生失败"));
//
//        strPtr.reset(res);
//        Assert.assertEquals("是Student Jmc", res);
//    }
//
//    @Test
//    public void test() {
//        Switch.of(123)
//                .caseType(String.class, s -> { strPtr.reset("String: " + s); })
//                .caseType(Integer.class, i -> { strPtr.reset("int: " + i); });
//    }
//
//    @Test
//    public void matchNullSwitchTest() {
//        // 测试null、nonNull 6个方法
//
//        Object obj = null;
//        // 会返回“是String.class，长度是：3”
//        String res = Switch.of(obj)
//                .caseType(Integer.class, i -> "是Integer.class，值是：" + i)
//                .caseType(String.class, s -> "是String.class，长度是：" + s.length())
//                .caseType(Double.class, d -> "是Double.class，值是：" + d)
//                .caseNull(obj)
//                .orElseThrow(() -> new IllegalArgumentException("新异常"));
//        strPtr.reset(res);
//        Assert.assertEquals("是String.class，长度是：3", res);
//
//        // 匹配不上，会抛出转化后的IllegalArgumentException异常
//        Assert.assertThrows(
//                IllegalArgumentException.class,
//                () -> Switch.of(() -> String.class)
//                        .caseType(Integer.class, i -> "是Integer.class")
//                        .orElseThrow(() -> new IllegalArgumentException("新异常"))
//        );
//    }
}
