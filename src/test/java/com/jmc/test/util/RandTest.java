package com.jmc.test.util;

import com.jmc.util.Rand;
import org.junit.Test;

public class RandTest {
    @Test
    public void test() {
        // 生成一个int范围内的随机数
        System.out.println(Rand.nextInt());

        // 生成一个100以内的随机数
        System.out.println(Rand.nextInt(100));

        // 生成一个1到10的随机数
        System.out.println(Rand.nextInt(1, 10));
    }
}
