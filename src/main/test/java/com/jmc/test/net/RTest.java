package com.jmc.test.net;

import com.jmc.net.R;
import org.junit.Test;

public class RTest {
    @Test
    public void test() {
        // 用于网络流（直接返回给前端）
        var r = R.ok()
                .data(666)
                .msg("一切正常");

        // 没有打印内容，保护其中信息
        System.out.println(r);
    }
}
