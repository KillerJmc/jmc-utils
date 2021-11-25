package com.jmc.test.lang.math;

import com.jmc.lang.math.BigInt;
import org.junit.Test;

public class BigIntTest {
    @Test
    public void test() {
        var i = BigInt.valueOf(Long.MAX_VALUE);
        var res = i.mul(i);
        System.out.println(res);
    }
}
