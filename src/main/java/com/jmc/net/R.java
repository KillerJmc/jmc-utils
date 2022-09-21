package com.jmc.net;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 统一返回数据实体类型
 * @since 1.6
 * @author Jmc
 * @param <T> 返回数据类型
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class R<T> {
    /**
     * 返回状态码
     */
    private int code;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    /**
     * Spring Json转化类的包名
     */
    private static final String SPRING_JSON_CONVERTER_PACKAGE_NAME = "org.springframework.http.converter.json";

    /**
     * 数据类的构造器
     * @since 2.2
     */
    public static class RBuilder {
        /**
         * 返回状态码
         */
        private int code;

        /**
         * 返回信息
         */
        private String message;

        private RBuilder() {}

        /**
         * 指定数据类构造器的返回信息
         * @param message 返回信息
         * @return 数据类的构造器本身
         */
        public RBuilder msg(String message) {
            this.message = message;
            return this;
        }

        /**
         * 通过包装数据构造数据类的对象
         * @param data 包装数据
         * @return 数据类的实例对象
         */
        public <T> R<T> data(T data) {
            return new R<>(code, message, data);
        }

        /**
         * 构造数据类的对象
         * @param <T> 返回数据类型
         * @return 数据类的实例对象
         */
        public <T> R<T> build() {
            return new R<>(code, message, null);
        }
    }

    /**
     * 返回状态为成功的数据类构造器
     * @return 数据类构造器
     */
    public static RBuilder ok() {
        var rBuilder = new RBuilder();
        rBuilder.code = HttpStatus.OK;
        return rBuilder;
    }

    /**
     * 返回包含数据，状态为成功的数据类
     * @param data 返回数据
     * @return 数据类
     * @since 2.3
     */
    public static <T> R<T> ok(T data) {
        var rBuilder = new RBuilder();
        rBuilder.code = HttpStatus.OK;
        return rBuilder.data(data);
    }

    /**
     * 返回状态为错误的数据类构造器
     * @return 数据类构造器
     */
    public static RBuilder error() {
        var rBuilder = new RBuilder();
        rBuilder.code = HttpStatus.ERROR;
        return rBuilder;
    }

    /**
     * 返回包含错误信息，状态为错误的数据类
     * @param errorMsg 错误信息
     * @return 数据类
     * @since 2.3
     */
    public static <T> R<T> error(String errorMsg) {
        var rBuilder = new RBuilder();
        rBuilder.code = HttpStatus.ERROR;
        return rBuilder.msg(errorMsg).build();
    }

    /**
     * 返回状态为禁止访问的数据类构造器
     * @return 数据类构造器
     */
    public static RBuilder forbidden() {
        var RBuilder = new RBuilder();
        RBuilder.code = HttpStatus.FORBIDDEN;
        return RBuilder;
    }

    /**
     * 返回状态为页面不存在的数据类构造器
     * @return 数据类构造器
     */
    public static RBuilder notFound() {
        var rBuilder = new RBuilder();
        rBuilder.code = HttpStatus.NOT_FOUND;
        return rBuilder;
    }

    /**
     * 判断某次请求是否失败
     * @return 请求是否失败
     * @since 2.3
     */
    public boolean failed() {
        return this.code != HttpStatus.OK;
    }

    /**
     * 判断Spring是否正在进行Json转换
     * @return 结果布尔值
     * @since 2.4
     */
    private boolean isSpringJsonConverting() {
        return Arrays.stream(Thread.currentThread().getStackTrace())
                .anyMatch(stack -> stack.getClassName().contains(SPRING_JSON_CONVERTER_PACKAGE_NAME));
    }

    /**
     * 检查请求是否成功并获取返回数据
     * @return 返回数据
     * @since 2.4
     */
    public T getData() throws Exception {
        // 如果是Spring正在进行Json转换，不检查请求是否成功，直接返回数据
        if (isSpringJsonConverting()) {
            return getDataUnchecked();
        }

        // 如果是用户在调用，请求失败直接抛出异常，并打印错误信息
        if (failed()) {
            throw new Exception("Request failed: " + this.message);
        }

        return data;
    }

    /**
     * 不检查请求是否成功直接获取返回数据
     * @return 返回数据
     * @since 2.4
     */
    public T getDataUnchecked() {
        return data;
    }
}
