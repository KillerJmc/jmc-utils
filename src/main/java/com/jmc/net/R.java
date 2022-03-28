package com.jmc.net;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 统一返回数据实体类型
 * @since 1.6
 * @author Jmc
 * @param <T> 返回数据类型
 */
@SuppressWarnings("unused")
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

    private R() {}

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
     * 返回状态为错误的数据类构造器
     * @return 数据类构造器
     */
    public static RBuilder error() {
        var rBuilder = new RBuilder();
        rBuilder.code = HttpStatus.ERROR;
        return rBuilder;
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
     * 检查请求是否成功并获取返回数据
     * @return 返回数据
     * @since 2.2
     */
    public T get() {
        // 如果请求失败直接抛出异常，并打印错误信息
        if (this.code != HttpStatus.OK) {
            throw new RuntimeException("Request failed: " + this.message);
        }
        return this.data;
    }
}
