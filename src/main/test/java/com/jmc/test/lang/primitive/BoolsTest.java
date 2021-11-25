package com.jmc.test.lang.primitive;

import com.jmc.lang.primitive.Bools;
import org.junit.Assert;
import org.junit.Test;

public class BoolsTest {
    @Test
    public void test() {
        Assert.assertTrue(Bools.in(5, 1, 10));
    }
}
