package com.jmc.net;

import com.jmc.lang.Tries;
import com.jmc.lang.ref.Pointer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 统一返回数据实体类型
 * @apiNote <pre>{@code
 * // 返回请求成功并携带数据666
 * // 前端收到json：{ "code": 200, "message": null, "data": 666 }
 * R<Integer> succeed2 = R.ok(666);
 *
 * // 返回请求失败并携带信息"Error"
 * // 前端收到json：{ "code": 500, "message": "Error", "data": null }
 * R<Void> error2 = R.error("Error");
 *
 * // 返回请求失败错误码10001并携带错误信息"Error"
 * // 前端收到json：{ "code": 10001, "message": "Error", "data": null }
 * R<Void> error3 = R.error(10001).msg("Error").build();
 *
 * }</pre>
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
     * 私有构造器
     */
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
         * @param <T> 返回数据类型
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
     * @param <T> 返回数据类型
     * @return 数据类
     * @since 2.3
     */
    public static <T> R<T> ok(T data) {
        var rBuilder = new RBuilder();
        rBuilder.code = HttpStatus.OK;
        return rBuilder.data(data);
    }

    /**
     * 返回带有指定错误码，状态为错误的数据类构造器
     * @param code 指定的错误码
     * @return 数据类构造器
     * @since 3.9
     */
    public static RBuilder error(int code) {
        var rBuilder = new RBuilder();
        rBuilder.code = code;
        return rBuilder;
    }

    /**
     * 返回带有默认错误码，状态为错误的数据类构造器
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
     * @param <T> 返回数据类型
     * @return 数据类
     * @since 2.3
     */
    public static <T> R<T> error(String errorMsg) {
        var rBuilder = new RBuilder();
        rBuilder.code = HttpStatus.ERROR;
        return rBuilder.msg(errorMsg).build();
    }

    /**
     * 判断某次请求是否成功
     * @return 请求是否成功
     * @since 3.9
     */
    public boolean succeed() {
        return this.code == HttpStatus.OK;
    }
}
