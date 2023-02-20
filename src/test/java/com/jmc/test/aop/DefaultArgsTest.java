package com.jmc.test.aop;

import com.jmc.aop.DefaultArg;
import com.jmc.aop.DefaultArgTransfer;
import com.jmc.aop.DefaultArgsFeature;
import lombok.Getter;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class DefaultArgsTest {
    @Test
    public void ctorTest() {
        // 启动默认参数特性！
        DefaultArgsFeature.enable();

        var testCtor = new TestCtor(null);
        Assert.assertEquals(666L, testCtor.getData());
    }

    // 构造方法的默认参数
    private static class TestCtor {
        @Getter
        @SuppressWarnings("all")
        private long data;

        public TestCtor(@DefaultArg("666") Long data) {
            this.data = data;
        }
    }

    @Test
    public void numberTest() {
        // 启动默认参数特性！
        DefaultArgsFeature.enable();

        Assert.assertEquals(7L, Calc.add(null, null));
        Assert.assertEquals(11L, Calc.add(5L, 6L));

        Assert.assertEquals(4L, Calc.sub(null, null));
        Assert.assertEquals(1L, Calc.sub(BigInteger.valueOf(5L), BigInteger.valueOf(4L)));
    }

    private static class Calc {
        // 基本数据类型
        private static long add(@DefaultArg("3") Long a, @DefaultArg("4") Long b) {
            return a + b;
        }

        // 特殊数字类型
        private static long sub(@DefaultArg("9") BigInteger a, @DefaultArg("5") BigInteger b) {
            return a.subtract(b).longValueExact();
        }
    }

    @Test
    public void complexTypeTest() {
        // 启动默认参数特性！
        DefaultArgsFeature.enable();

        Assert.assertEquals("UTF-8", ComplexTest.getCharsetName(null));
        Assert.assertEquals("UTF-16", ComplexTest.getCharsetName(StandardCharsets.UTF_16));
    }

    private static class ComplexTest {
        // 复杂类型需要指定转换类
        private static String getCharsetName(
                @DefaultArg(value = "UTF-8", transferClass = StringToCharset.class) Charset c) {
            return c.displayName();
        }

        // 转化类
        private static class StringToCharset extends DefaultArgTransfer<Charset> {
            @Override
            public Charset transfer(String defaultArg) { return Charset.forName(defaultArg); }
        }
    }
}
