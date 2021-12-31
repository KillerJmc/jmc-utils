package com.jmc.test.time;

import com.jmc.time.Time;
import org.junit.Test;

import java.time.LocalDateTime;

public class TimeTest {
    @Test
    public void test() {
        // 获取当前时间字符串
        System.out.println(Time.now());

        // 把时间转化为毫秒值
        System.out.println(Time.toMilli(LocalDateTime.now()));

        // 把毫秒值转化为时间
        System.out.println(Time.ofMilli(System.currentTimeMillis()));

        // 按格式表示当前时间
        System.out.println(Time.now("yyyy年MM月dd日 HH时mm分"));
    }
}
