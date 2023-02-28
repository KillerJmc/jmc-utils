package com.jmc.test.math.exp;

import com.jmc.math.exp.ExpUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ExpUtilsTest {
    @Test
    public void infixAndSuffixTest() {
        Assert.assertEquals(List.of("(", "-1", "+", "2.5", ")", ">>", "366"), ExpUtils.splitInfix("(-1 + 2.5) >> 366"));
        Assert.assertEquals(List.of("1", "2", "+", "5", "*"), ExpUtils.suffix("(1 + 2) * 5", ExpUtils.DEFAULT_PRIORITY));
    }

    @Test
    public void calculatorTest() {
        var calculator = ExpUtils.defaultCalculator();
        var res = calculator.calc("(1 + 2) * 4");
        Assert.assertEquals(12.0, res, 0.0);
    }
}
