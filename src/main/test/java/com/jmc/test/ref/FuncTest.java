package com.jmc.test.ref;

import com.jmc.ref.Func;
import org.junit.Test;

public class FuncTest {
    @Test
    public void funcTest() {
        // 绑定方法引用到Func
        var sum = Func.of(Integer::sum);

        // 执行方法获取结果
        var res = sum.invoke(1, 2);

        System.out.println(res);

        // 绑定纯数值的lambda表达式
        var mul = Func.<Integer>of((a, b) -> a * b);
        System.out.println(mul.invoke(3, 4));

        // 绑定普通lambda表达式
        var concat = Func.of((String a, Integer b) -> a + b);
        System.out.println(concat.invoke("a", 3));
    }

    @Test
    public void bindTest() {
        // 绑定方法引用并绑定参数
        var sum = Func.bind(Integer::sum, 3, 4);
        System.out.println(sum.invoke());
    }
}
