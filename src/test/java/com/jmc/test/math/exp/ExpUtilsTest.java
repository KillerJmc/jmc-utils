package com.jmc.test.math.exp;

import com.jmc.math.exp.ExpUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ExpUtilsTest {
    @Test
    public void infixAndSuffixTest() {
        Assert.assertEquals(ExpUtils.splitInfix("(-1 + 2.5) >> 366"), List.of("(", "-1", "+", "2.5", ")", ">>", "366"));
        Assert.assertEquals(ExpUtils.suffix("(1 + 2) * 5", ExpUtils.DEFAULT_PRIORITY), List.of("1", "2", "+", "5", "*"));
    }

    @Test
    public void calculatorTest() {
        var calculator = ExpUtils.defaultCalculator();
        var res = calculator.calc("(1 + 2) * 4");
        Assert.assertEquals((Object) res, 12.0);
    }
}
