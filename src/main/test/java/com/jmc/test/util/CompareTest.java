package com.jmc.test.util;

import com.jmc.array.Array;
import com.jmc.util.Compare;
import org.junit.Assert;
import org.junit.Test;

public class CompareTest {
    @Test
    public void normalTest() {
        // 判断两个元素相等
        Assert.assertTrue(Compare.eq(3, 3));

        // 判断4大于3
        Assert.assertTrue(Compare.gt(4, 3));

        // 判断4小于3
        Assert.assertFalse(Compare.lt(4, 3));
    }

    @Test
    public void ArrayTest() {
        var a = Array.of(1, 2, 3);
        Assert.assertTrue(Compare.gt(a, 2, 0));

        var a2 = Array.of(1.0, 2.0, 3.0);
        Assert.assertFalse(Compare.lt(a, 2, 0));
    }
}
