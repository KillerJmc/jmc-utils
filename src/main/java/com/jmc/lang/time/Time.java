package com.jmc.lang.time;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

/**
 * 时间类
 * @since 1.0
 * @author Jmc
 */
@SuppressWarnings("unused")
public class Time {

    private Time() {}

    /**
     * 默认格式
     */
    private static final String DEFAULT_FORMAT = "yyyy-M-d HH:mm:ss";

    /**
     * 获取当前时间
     * @return 默认格式的当前时间字符串
     */
    public static String now() {
        return now(DEFAULT_FORMAT);
    }

    /**
     * 获取当前时间
     * @param format 指定的时间格式
     * @return 指定格式的当前时间字符串
     */
    public static String now(String format) {
        return LocalDateTime.now().format(getFormatter(format));
    }

    /**
     * 用新纪元时间到现在的毫秒值获取时间
     * @param epochMilli 新纪元时间到现在的毫秒值
     * @return 默认格式的时间结果
     */
    public static String ofMilli(long epochMilli) {
        return ofMilli(epochMilli, DEFAULT_FORMAT);
    }

    /**
     * 用新纪元时间到现在的毫秒值获取时间
     * @param epochMilli 新纪元时间到现在的毫秒值
     * @param format 指定的时间格式
     * @return 指定格式的时间结果
     */
    public static String ofMilli(long epochMilli, String format) {
        return ofMilliToLocalDateTime(epochMilli).format(getFormatter(format));
    }

    /**
     * 用新纪元时间到现在的毫秒值获取时间
     * @param epochMilli 新纪元时间到现在的毫秒值
     * @return LocalDateTime对象
     * @since 1.3
     */
    public static LocalDateTime ofMilliToLocalDateTime(long epochMilli) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneId.systemDefault());
    }

    /**
     * 将指定格式的字符串转化为新纪元时间到指定时间的毫秒值
     * @param timeWithDefaultFormat 指定的时间字符串（使用默认的时间格式）
     * @return 新纪元时间到指定时间的毫秒值
     */
    public static long toMilli(String timeWithDefaultFormat) {
        return toMilli(timeWithDefaultFormat, DEFAULT_FORMAT);
    }

    /**
     * 将指定格式的字符串转化为新纪元时间到指定时间的毫秒值
     * @param format 指定的时间格式
     * @param time 指定的时间字符串
     * @return 新纪元时间到指定时间的毫秒值
     */
    public static long toMilli(String time, String format) {
        return toMilli(LocalDateTime.parse(time, getFormatter(format)));
    }

    /**
     * 将LocalDateTime对象转化为新纪元时间到指定时间的毫秒值
     * @param ldt LocalDateTime对象
     * @return 新纪元时间到指定时间的毫秒值
     * @since 1.3
     */
    public static long toMilli(LocalDateTime ldt) {
        // 东8区
        return ldt.toInstant(ZoneOffset.ofHours(+8)).toEpochMilli();
    }

    /**
     * 获取时间格式化类：默认的必须写年月日和小时，现在我们可以突破这个限制
     * @param format 指定的时间格式
     * @return 结果时间格式类（默认年份1970，月份和日期为1，小时为0）
     */
    private static DateTimeFormatter getFormatter(String format) {
        var formatter0 = DateTimeFormatter.ofPattern(format);
        return new DateTimeFormatterBuilder()
                .append(formatter0)
                .parseDefaulting(ChronoField.YEAR_OF_ERA, 1970)
                .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
                .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .toFormatter();
    }
}

