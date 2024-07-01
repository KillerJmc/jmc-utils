package com.jmc.test.lang.ref;

import com.jmc.lang.ref.Func;
import org.junit.Assert;
import org.junit.Test;

public class FuncTest {
    @Test
    public void funcTest() {
        // 绑定方法引用到Func
        var sum = Func.of(Integer::sum);
        var res = sum.invoke(1, 2);
        Assert.assertEquals(res, Integer.valueOf(3));

        // 绑定纯数值的lambda表达式
        var mul = Func.<Integer>of((a, b) -> a * b);
        Assert.assertEquals(mul.invoke(3, 4), Integer.valueOf(12));

        // 绑定普通lambda表达式
        var concat = Func.of((String a, Integer b) -> a + b);
        Assert.assertEquals(concat.invoke("a", 3), "a3");
    }

    @Test
    public void partialTest() {
        // 绑定部分参数获取方法引用
        var threeAddFunc = Func.partial(Integer::sum, 3);
        Assert.assertEquals(threeAddFunc.invoke(4), Integer.valueOf(7));

        // 绑定部分参数获取普通lambda偏函数
        var jmcAddStrFunc = Func.partial((String a, String b) -> a + b, "Jmc");
        Assert.assertEquals(jmcAddStrFunc.invoke(" NB!"), "Jmc NB!");

        // 绑定部分参数获取纯数值lambda偏函数
        var threeMulFunc = Func.<Integer>partial((a, b) -> a * b, 3);
        Assert.assertEquals(threeMulFunc.invoke(4), Integer.valueOf(12));
    }
}
