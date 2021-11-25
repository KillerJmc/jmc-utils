package com.jmc.test.lang.rand;

import com.jmc.lang.rand.Rand;
import org.junit.Test;

public class RandTest {
    @Test
    public void test() {
        // 生成100以内的一个随机数
        System.out.println(Rand.nextInt(100));

        // 生成1到10的一个随机数
        System.out.println(Rand.nextInt(1, 10));
    }
}
