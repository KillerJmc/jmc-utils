package com.jmc.net;

import com.jmc.lang.Tries;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * 统一返回数据实体类型
 * @since 1.6
 * @author Jmc
 * @param <T> 返回数据类型
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
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
     * 常用Json转换器包名列表
     * @since 2.5
     */
    private static final List<String> JSON_CONVERTER_PACKAGE_NAMES = List.of(
            "com.fasterxml.jackson",
            "com.alibaba.fastjson"
    );

    /**
     * 判断是否正在进行Json转换
     * @return 结果布尔值
     * @since 2.5
     */
    private boolean isJsonConverting() {
        return Arrays.stream(Thread.currentThread().getStackTrace())
                .map(StackTraceElement::getClassName)
                .anyMatch(className ->
                        JSON_CONVERTER_PACKAGE_NAMES
                                .stream()
                                .anyMatch(className::contains)
                );
    }

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
     * 检查请求是否成功并获取返回数据 <br>
     * 如果请求失败就抛出异常信息
     * @return 返回数据
     * @since 2.5
     */
    public T getData() {
        // 如果正在进行Json转换，不检查请求是否成功，直接返回数据
        if (isJsonConverting()) {
            return data;
        }

        // 如果是用户在调用，请求失败直接抛出异常，并打印错误信息
        if (failed()) {
            throw new RuntimeException("Request failed: " + this.message);
        }

        return data;
    }

    /**
     * 获取R的流式高级构建对象
     * @return R构建流
     * @since 2.5
     */
    public static RStream stream() {
        return new RStream();
    }

    /**
     * R的流式高级构建对象
     * @since 2.5
     */
    public static class RStream {
        private RStream() {}

        /**
         * 存放中间操作函数的集合
         */
        private final List<Tries.RunnableThrowsE> OPERATIONS = new ArrayList<>();

        /**
         * 运行检查函数
         * @param checkFunc 负责检查对象有效性的函数
         * @return 本对象实例
         */
        public RStream check(Tries.RunnableThrowsE checkFunc) {
            OPERATIONS.add(checkFunc);
            return this;
        }

        /**
         * 执行布尔值检查
         * @param reject 是否拒绝请求
         * @param errorMsg 拒绝请求时的错误信息
         * @return 本对象实例
         */
        public RStream check(boolean reject, String errorMsg) {
            OPERATIONS.add(() -> {
                if (reject) {
                    throw new Exception(errorMsg);
                }
            });
            return this;
        }

        /**
         * 运行功能函数
         * @param execFunc 执行功能的函数
         * @return 本对象实例
         */
        public RStream exec(Tries.RunnableThrowsE execFunc) {
            OPERATIONS.add(execFunc);
            return this;
        }

        /**
         * 构建无返回数据的R实例
         * @return R实例
         */
        public R<Void> build() {
            return invokeAndGetErrorMsg()
                    .<R<Void>>map(R::error)
                    .orElse(R.ok().build());
        }

        /**
         * 构建带返回数据的R实例
         * @param data 返回数据
         * @return R实例
         */
        public <T> R<T> build(T data) {
            return invokeAndGetErrorMsg()
                    .<R<T>>map(R::error)
                    .orElse(R.ok(data));
        }

        /**
         * 构建带返回数据的R实例
         * @param dataSupplier 返回数据提供函数
         * @return R实例
         */
        public <T> R<T> build(Supplier<T> dataSupplier) {
            return invokeAndGetErrorMsg()
                    .<R<T>>map(R::error)
                    .orElseGet(() -> R.ok(dataSupplier.get()));
        }

        /**
         * 执行所有中间操作，当操作抛出异常信息时中断并返回异常信息
         * @return 导致中断的异常信息
         */
        private Optional<String> invokeAndGetErrorMsg() {
            for (var operation : OPERATIONS) {
                var errorMsg = Tries.tryReturnsE(operation).map(Throwable::getMessage);
                if (errorMsg.isPresent()) {
                    return errorMsg;
                }
            }
            return Optional.empty();
        }
    }
}
