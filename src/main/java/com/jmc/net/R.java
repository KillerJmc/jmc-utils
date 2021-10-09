package com.jmc.net;

import lombok.Getter;

/**
 * 统一返回数据类型
 * @since 1.6
 * @author Jmc
 */
@SuppressWarnings("unused")
@Getter
public class R {
    /**
     * 返回状态码
     */
    private int code;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 返回对象
     */
    private Object data;

    private R() {}

    /**
     * 返回状态为成功的数据类
     * @return 数据类
     * @since 1.6
     */
    public static R ok() {
        R r = new R();
        r.code = HttpStatus.OK;
        return r;
    }

    /**
     * 返回状态为禁止访问的数据类
     * @return 数据类
     * @since 1.6
     */
    public static R forbidden() {
        R r = new R();
        r.code = HttpStatus.FORBIDDEN;
        return r;
    }

    /**
     * 返回状态为页面不存在的数据类
     * @return 数据类
     * @since 1.6
     */
    public static R notFound() {
        R r = new R();
        r.code = HttpStatus.NOT_FOUND;
        return r;
    }

    /**
     * 返回状态为错误的数据类
     * @return 数据类
     * @since 1.6
     */
    public static R error() {
        R r = new R();
        r.code = HttpStatus.ERROR;
        return r;
    }

    /**
     * 指定数据类的返回信息
     * @param message 返回信息
     * @return 数据类本身
     * @since 1.6
     */
    public R msg(String message) {
        this.message = message;
        return this;
    }

    /**
     * 指定数据类的返回对象
     * @param data 返回对象
     * @return 数据类本身
     * @since 1.6
     */
    public R data(Object data) {
        this.data = data;
        return this;
    }
}
