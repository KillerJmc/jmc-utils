package com.jmc.test.lang;

import com.jmc.lang.Strs;
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
                        .orElse("没命中")
        );

        Assert.assertEquals("caseObj返回对象", f.invoke("caseObj返回对象条件"));
        Assert.assertEquals("caseObj返回对象条件2!", f.invoke("caseObj返回对象条件2"));
        Assert.assertEquals("caseType整数", f.invoke(1));
        Assert.assertEquals("caseType长整数666", f.invoke(666L));
        Assert.assertEquals("caseWhen浮点数大于3", f.invoke(10.0));
        Assert.assertEquals("caseWhen浮点数小于3，是1.0", f.invoke(1.0));
        Assert.assertEquals("caseNull", f.invoke((Object) null));
        Assert.assertEquals("没命中", f.invoke("你好呀"));
    }

    @Test
    public void caseRun() {
        var strResPtr = Pointer.of(Strs.Const.EMPTY);
        Func<Void> f = Func.of((Object obj) ->
                Switch.of(() -> obj)
                        .caseObj("caseObj执行方法", () -> strResPtr.reset("caseObj执行方法"))
                        .caseType(Integer.class, () -> strResPtr.reset("caseType执行方法"))
                        .caseWhen((Double d) -> d > 3, () -> strResPtr.reset("caseWhen执行方法，浮点数大于3"))
                        .caseNull(() -> strResPtr.reset("caseNull"))
                        .orElseRun(() -> strResPtr.reset("没命中"))
        );

        f.invoke("caseObj执行方法");
        Assert.assertEquals("caseObj执行方法", strResPtr.get());
        f.invoke(2);
        Assert.assertEquals("caseType执行方法", strResPtr.get());
        f.invoke(6.0);
        Assert.assertEquals("caseWhen执行方法，浮点数大于3", strResPtr.get());
        f.invoke((Object) null);
        Assert.assertEquals("caseNull", strResPtr.get());
        f.invoke("你好呀");
        Assert.assertEquals("没命中", strResPtr.get());
    }

    @Test
    public void caseOrElseGetSupplier() {
        Object obj = 666;
        String res = Switch.of(() -> obj).caseObj(1, "匹配上了").orElse(() -> "没匹配上");
        Assert.assertEquals("没匹配上", res);
    }

    @Test
    public void caseGetOrElseThrow() {
        Object obj = 666;
        Assert.assertThrows(
                RuntimeException.class,
                () -> Switch.of(() -> obj).caseObj(1, "匹配上了").orElseThrow(RuntimeException::new)
        );
    }

    @Test
    public void caseGetOrElseThrowCustom() {
        Object obj = 666;
        Assert.assertThrows(
                NoSuchElementException.class,
                () -> Switch.of(obj).orElseThrow()
        );
    }

    @Test
    public void objTest() {
        record Employee(String name) {}
        record Student(String name, int age) {}

        Func<String> f = Func.of((Object obj) ->
                Switch.of(obj)
                        .caseObj("你好", s -> "是字符串，值为：" + s)
                        .caseType(Integer.class, i -> "是整数，值为：" + i)
                        .caseType(Employee.class, emp -> "是职工对象，职工姓名：" + emp.name())
                        .caseWhen((Student stu) -> stu.age() < 18, stu -> "是学生对象，年龄小于18，学生信息：" + stu)
                        .caseNull("为空对象")
                        .orElse("没命中默认值")
        );

        Assert.assertEquals("是字符串，值为：你好", f.invoke("你好"));
        Assert.assertEquals("是整数，值为：42", f.invoke(42));

        Employee emp = new Employee("张三");
        Assert.assertEquals("是职工对象，职工姓名：张三", f.invoke(emp));

        Student stu1 = new Student("Jmc", 16);
        Assert.assertEquals("是学生对象，年龄小于18，学生信息：" + stu1, f.invoke(stu1));

        Student stu2 = new Student("Jack", 20);
        Assert.assertEquals("没命中默认值", f.invoke(stu2));

        Assert.assertEquals("为空对象", f.invoke((Object) null));
        Assert.assertEquals("没命中默认值", f.invoke(3.14));
    }
}
