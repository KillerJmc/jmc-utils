package com.jmc.test.lang.math;

import com.jmc.lang.extend.Outs;
import com.jmc.lang.math.ExactExp;
import com.jmc.lang.timer.Timers;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class ExactExpTest {
    @Test
    public void test() {
        // 计算n!
        int n = 10000;

        // 计时
        Timers.milliTimer(() -> useExp(n), "useExp");

        // 分隔符
        Outs.newLine();

        // 计时
        Timers.milliTimer(() -> useNormal(n), "useNormal");
    }

    // 未使用表达式操作复杂
    private void useNormal(int n) {
        // n! ~ √(2πn) * (n/e)^n
        var result2 = new BigDecimal(2).multiply(new BigDecimal(Double.valueOf(Math.PI).toString()))
                .multiply(new BigDecimal(n)).sqrt(MathContext.DECIMAL128)
                .multiply(new BigDecimal(n).divide(new BigDecimal(Double.valueOf(Math.E).toString()), 34, RoundingMode.HALF_UP)
                        .pow(n)).setScale(0, RoundingMode.HALF_UP);

        System.out.println(result2);
    }

    // 使用表达式更加简单
    private void useExp(int n) {
        // n! ~ √(2πn) * (n/e)^n
        String exp = "√(2 * %.16f * %d) * (%d / %.16f) ^ %d".formatted(Math.PI, n, n, Math.E, n);
        var result = ExactExp.getResult(exp, 0);

        System.out.println(result);
    }
}

