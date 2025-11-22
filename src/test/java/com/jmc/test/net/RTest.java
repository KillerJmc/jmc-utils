package com.jmc.test.net;

import com.jmc.lang.Objs;
import com.jmc.lang.Outs;
import com.jmc.lang.Threads;
import com.jmc.lang.Tries;
import com.jmc.lang.ref.Func;
import com.jmc.net.HttpStatus;
import com.jmc.net.R;
import org.junit.Assert;
import org.junit.Test;

public class RTest {
    @Test
    public void okTest() {
        // 用于网络流（直接返回给前端）
        var r = R.ok().msg("一切正常").data(666);

        // 没有打印内容，保护其中信息
        System.out.println(r);

        // 正常获取和打印对象
        var data = r.getData();
        System.out.println(data);
        Assert.assertEquals((Integer) 666, data);
    }

    @Test
    public void errTest() {
        // 请求失败
        var r = R.error().msg("参数欠缺！").build();

        // 获取数据并进行异常处理
        var data = Tries.tryGetOrHandle(r::getData, System.err::println);

        // 返回数据应该为空
        Assert.assertNull(data);
    }

    @Test
    public void errWithCodeTest() {
        // 请求失败
        var r = R.error(300).msg("参数欠缺！").build();

        Assert.assertEquals(300, r.getCode());
    }

    @Test
    public void errWithMsgTest() {
        // 请求失败
        var r = R.error("错误信息");
        // 获取错误信息
        var errorMsg = r.getMessage();
        System.err.println(errorMsg);
        Assert.assertEquals("错误信息", errorMsg);
    }

    @Test
    public void succeedJudgeTest() {
        // 请求成功
        var r = R.ok(new Object());

        // 如果请求成功
        if (r.succeed()) {
            // 继续处理数据
            var data = r.getData();
            Assert.assertNotNull(data);
        }
    }
}
