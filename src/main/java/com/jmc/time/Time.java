package com.jmc.time;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

/**
 * 时间类 <br>
 * 提供LocalDateTime、Millis、时间格式字符串之间的相互转换
 * @apiNote <pre>{@code
 * // 从LocalDateTime创建Time并转换为毫秒值
 * var milli = Time.of(LocalDateTime.now()).toMilli();
 *
 * // 从毫秒值创建Time并转换为指定格式的时间字符串
 * var timeStr = Time.of(System.currentTimeMillis()).toString("yyyy年M月d日 H时m分");
 *
 * // 从指定格式的时间字符串创建Time并转成LocalDateTime
 * var localDateTime = Time.of("yyyy.M.d", "2001.8.9").toLocalDateTime();
 * }</pre>
 * @since 1.0
 * @author Jmc
 */
public class Time {

    private Time() {}

    /**
     * 储存的时间
     * @since 2.1
     */
    public LocalDateTime ldt;

    /**
     * 默认时间格式
     */
    public static final String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 通过LocalDateTime创建实例
     * @param ldt LocalDateTime对象
     * @return 实例
     * @apiNote <pre>{@code
     * // 从LocalDateTime创建一个Time实例
     * var time = Time.of(LocalDateTime.now());
     * }</pre>
     * @since 2.1
     */
    public static Time of(LocalDateTime ldt) {
        var instance = new Time();
        instance.ldt = ldt;
        return instance;
    }

    /**
     * 通过新纪元时间到现在的毫秒值创建实例
     * @param milli 新纪元时间到现在的毫秒值
     * @return 实例
     * @apiNote <pre>{@code
     * // 从新纪元时间到现在的毫秒值创建一个Time实例
     * var time = Time.of(System.currentTimeMillis());
     * }</pre>
     * @since 2.1
     */
    public static Time of(long milli) {
        return of(LocalDateTime.ofInstant(Instant.ofEpochMilli(milli), ZoneId.systemDefault()));
    }

    /**
     * 通过带有格式的时间字符串创建实例，使用类中的默认时间格式
     * @param time 时间字符串
     * @return 实例
     * @apiNote <pre>{@code
     * // 从格式字符串和时间字符串创建一个Time实例
     * var time = Time.of("2001-08-09");
     * }</pre>
     * @since 3.9
     */
    public static Time of(String time) {
        return of(DEFAULT_TIME_FORMAT, time);
    }

    /**
     * 通过带有格式的时间字符串创建实例
     * @param format 指定时间格式
     * @param time 时间字符串
     * @return 实例
     * @apiNote <pre>{@code
     * // 从格式字符串和时间字符串创建一个Time实例
     * var time = Time.of("yyyy.MM.dd", "2001.08.09");
     * }</pre>
     * @since 2.1
     */
    public static Time of(String format, String time) {
        return of(LocalDateTime.parse(time, getFormatter(format)));
    }

    /**
     * 转换为LocalDateTime
     * @return 转换结果对象
     * @apiNote <pre>{@code
     * // 将Time转换为LocalDateTime
     * var ldt = time.toLocalDateTime();
     * }</pre>
     * @since 2.1
     */
    public LocalDateTime toLocalDateTime() {
        return ldt;
    }

    /**
     * 转化为新纪元时间到现在的毫秒值
     * @return 新纪元时间到现在的毫秒值
     * @apiNote <pre>{@code
     * // 将Time转换为新纪元时间到现在的毫秒值
     * var ldt = time.toMilli();
     * }</pre>
     * @since 2.1
     */
    public long toMilli() {
        return ldt.toInstant(ZoneOffset.ofHours(+8)).toEpochMilli();
    }

    /**
     * 转化为 {@link #DEFAULT_TIME_FORMAT 默认时间格式} 字符串
     * @return 默认时间格式字符串
     * @apiNote <pre>{@code
     * // 将Time转化为默认时间格式字符串（例子："2001-8-9 12:00:00"）
     * var res = time.toString();
     * }</pre>
     * @since 2.1
     */
    @Override
    public String toString() {
        return ldt.format(getFormatter(DEFAULT_TIME_FORMAT));
    }

    /**
     * 转化为指定时间格式的字符串
     * @param format 指定的时间格式
     * @return 定时间格式的字符串
     * @apiNote <pre>{@code
     * // 将Time转化为指定时间格式字符串（例子："2001-8-9"）
     * var res = time.toString("yyyy-M-d");
     * }</pre>
     * @since 2.1
     */
    public String toString(String format) {
        return ldt.format(getFormatter(format));
    }

    /**
     * 获取时间格式化类：默认的必须写年月日和小时，现在我们可以突破这个限制
     * @param format 指定的时间格式
     * @return 结果时间格式类（默认年份1970，月份和日期为1，小时为0）
     * @apiNote <pre>{@code
     * // 从时间格式字符串创建DateTimeFormatter
     * DateTimeFormatter formatter = Time.getFormatter("yyyy-MM-dd");
     * }</pre>
     */
    public static DateTimeFormatter getFormatter(String format) {
        return new DateTimeFormatterBuilder()
                .append(DateTimeFormatter.ofPattern(format))
                .parseDefaulting(ChronoField.YEAR_OF_ERA, 1970)
                .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
                .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .toFormatter();
    }
}

