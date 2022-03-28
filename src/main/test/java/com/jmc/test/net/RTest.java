package com.jmc.test.net;

import com.jmc.lang.Tries;
import com.jmc.net.R;
import org.junit.Assert;
import org.junit.Test;

public class RTest {
    @Test
    public void okTest() {
        // 用于网络流（直接返回给前端）
        var r = R.ok()
                .msg("一切正常")
                .data(666);

        // 没有打印内容，保护其中信息
        System.out.println(r);

        // 正常获取和打印对象
        var data = r.get();
        System.out.println(data);
    }

    @Test
    public void errTest() {
        // 请求失败
        var r = R.forbidden()
                .msg("参数欠缺！")
                .build();

        // 获取数据并进行异常处理
        var data = Tries.tryReturnsT(r::get, System.err::println);

        // 返回数据应该为空
        Assert.assertNull(data);
    }
}
