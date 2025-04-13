package com.jmc.test.time;

import com.jmc.time.Time;
import org.junit.Test;

import java.time.LocalDateTime;

public class TimeTest {
    @Test
    public void test() {
        // 获取当前时间
        System.out.println(Time.of(LocalDateTime.now()));

        // 把时间转化为毫秒值
        System.out.println(Time.of(LocalDateTime.now()).toMilli());

        // 把毫秒值转化为时间
        System.out.println(Time.of(System.currentTimeMillis()));

        // 按格式表示当前时间
        System.out.println(Time.of(LocalDateTime.now()).toString("yyyy年M月d日 H时m分"));

        // 输入有格式的时间并转成LocalDateTime
        System.out.println(Time.of("yyyy.M.d", "2001.8.9").toLocalDateTime());

        // 输入有格式的时间并转成LocalDateTime（使用时间类的默认格式）
        System.out.println(Time.of("2001-08-09 12:00:00").toLocalDateTime());
    }
}
