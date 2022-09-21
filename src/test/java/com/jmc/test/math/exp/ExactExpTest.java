package com.jmc.test.math.exp;

import com.jmc.math.exp.ExactExp;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class ExactExpTest {
    @Test
    public void simpleTest() {
        Assert.assertEquals(ExactExp.getResult("3!"), BigDecimal.valueOf(6));
        Assert.assertEquals(ExactExp.getResult("1 + ((3 + 2)!) + (2!) + 5"), BigDecimal.valueOf(128));
        Assert.assertEquals(ExactExp.getResult("6 / (8 - 3!)"), BigDecimal.valueOf(3));

        Assert.assertEquals(ExactExp.getResult("√4"), BigDecimal.valueOf(2));
        Assert.assertEquals(ExactExp.getResult("1 + (√(5 + 20)) + (√4) + 2"), BigDecimal.valueOf(10));
        Assert.assertEquals(ExactExp.getResult("2 * (√4 + 2)"), BigDecimal.valueOf(8));

        Assert.assertEquals(ExactExp.getResult("3 ^ 2"), BigDecimal.valueOf(9));
        Assert.assertEquals(ExactExp.getResult("3 ** 2 - 4 * 2"), BigDecimal.valueOf(1));
    }

    @Test
    public void complicatedTest() {
        // 计算n!
        int n = 10000;

        Assert.assertEquals(useExp(n), useNormal(n));
    }

    // 未使用表达式操作复杂
    private BigDecimal useNormal(int n) {
        // n! ~ √(2πn) * (n/e)^n
        var res = new BigDecimal(2).multiply(new BigDecimal(Double.valueOf(Math.PI).toString()))
                .multiply(new BigDecimal(n)).sqrt(MathContext.DECIMAL128)
                .multiply(new BigDecimal(n).divide(new BigDecimal(Double.valueOf(Math.E).toString()), 34, RoundingMode.HALF_UP)
                        .pow(n)).setScale(0, RoundingMode.HALF_UP);

        System.out.println(res);
        return res;
    }

    // 使用表达式更加简单
    private BigDecimal useExp(int n) {
        // n! ~ √(2πn) * (n/e)^n
        String exp = "√(2 * %.16f * %d) * (%d / %.16f) ^ %d".formatted(Math.PI, n, n, Math.E, n);
        var res = ExactExp.getResult(exp, 0);

        System.out.println(res);
        return res;
    }
}

