package com.jmc.net;

/**
 * Http状态码的常量类
 * @since 1.6
 * @author Jmc
 */
public interface HttpStatus {
    /**
     * 成功返回网页
     */
    int OK = 200;

    /**
     * 禁止访问
     */
    int FORBIDDEN = 403;

    /**
     * 访问的页面不存在
     */
    int NOT_FOUND = 404;

    /**
     * 服务器内部错误
     */
    int ERROR = 500;
}
