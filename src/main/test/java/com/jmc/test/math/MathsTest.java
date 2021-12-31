package com.jmc.test.math;

import com.jmc.math.Maths;
import org.junit.Assert;
import org.junit.Test;

public class MathsTest {
    @Test
    public void primeTest() {
        // 判断一个数是不是质数
        Assert.assertTrue(Maths.isPrime(23));

        // 获取100以内所有质数
        Maths.getPrimes(100).forEach(t -> System.out.print(t + ", "));
        System.out.println();
    }

    @Test
    public void factorialTest() {
        // 计算100的阶乘
        System.out.println(Maths.factorial(100));

        // 估算100的阶乘
        System.out.println(Maths.approxFactorial(100));
    }
}
