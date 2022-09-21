package com.jmc.test.net;

import com.jmc.lang.Tries;
import com.jmc.net.HttpStatus;
import com.jmc.net.R;
import org.junit.Assert;
import org.junit.Test;

public class RTest {
    @Test
    public void okTest() throws Exception {
        // 用于网络流（直接返回给前端）
        var r = R.ok()
                .msg("一切正常")
                .data(666);

        // 没有打印内容，保护其中信息
        System.out.println(r);

        // 正常获取和打印对象
        var data = r.getData();
        System.out.println(data);
    }

    @Test
    public void okWithData() throws Exception {
        // 请求成功
        var r = R.ok("成功");
        // 打印包含数据
        System.out.println("data: " + r.getData());
    }

    @Test
    public void errTest() {
        // 请求失败
        var r = R.error()
                .msg("参数欠缺！")
                .build();

        // 获取数据并进行异常处理
        var data = Tries.tryReturnsT(r::getData, System.err::println);

        // 返回数据应该为空
        Assert.assertNull(data);
    }

    @Test
    public void errWithMsg() {
        // 请求失败
        var r = R.error("错误信息");
        // 获取错误信息
        var errorMsg = r.getMessage();
        System.err.println(errorMsg);
    }

    @Test
    public void notFoundAndForbiddenTest() {
        // 找不到页面
        var notFoundR = R.notFound().build();
        Assert.assertEquals(notFoundR.getCode(), HttpStatus.NOT_FOUND);

        // 禁止访问
        var forbiddenR = R.forbidden().build();
        Assert.assertEquals(forbiddenR.getCode(), HttpStatus.FORBIDDEN);
    }

    @Test
    public void handleError() {
        // 请求成功
        var r = R.ok()
                .data(new Object());

        // 如果请求失败
        if (r.failed()) {
            // 在这可以返回这个R，承载错误信息
            return;
        }

        // 继续处理数据
        var data = r.getDataUnchecked();
        Assert.assertNotNull(data);
    }
}
