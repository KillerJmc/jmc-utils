package com.jmc.test.math;

import com.jmc.math.Binary;
import org.junit.Test;

public class BinaryTest {
    @Test
    public void test() {
        // 输出-18的二进制原码字符串
        System.out.println(Binary.toString(-18, true));

        // 输出-18的二进制补码字符串
        System.out.println(Binary.toString(-18, false));

        // 把二进制原码字符串转化为int
        System.out.println(Binary.toInt("101010", true));
    }
}
