package com.jmc.test.lang;

import com.jmc.lang.Outs;
import com.jmc.lang.Strs;
import org.junit.Assert;
import org.junit.Test;

public class StrsTest {
    @Test
    public void capitalizeTest() {
        System.out.println(Strs.capitalize("stu"));
    }

    @Test
    public void orTest() {
        var s = "abcdefg";
        Assert.assertTrue(Strs.orContains(s, "p", "a"));
        Assert.assertFalse(Strs.orStartsWith(s, "23", "2", "ii"));
        Assert.assertFalse(Strs.orEndsWith(s, "t", "u"));
        Assert.assertTrue(Strs.orEquals(s, "a", "ab", "abcdefg"));
        System.out.println(Strs.orReplace(s, "!", "f", "bc"));
    }

    @Test
    public void swapTest() {
        var s = "a c a c";
        // 把s中的a和c调换位置
        System.out.println(Strs.swap(s, "a", "c"));
    }

    @Test
    public void subTest() {
        var s = "abcdefg";
        System.out.println(Strs.subExclusive(s, "a", "fg"));
        System.out.println(Strs.subInclusive(s, "cd", "f"));
    }

    @Test
    public void removeAndCollectTest() {
        var s = "<abc><lef><ajk><ghi>";
        // 收集所有以<开头，>结尾的子串（不包括边界），并输出
        Strs.collectAll(s, "<", ">", false).forEach(System.out::println);

        // 输出分割线
        Outs.newLine();

        // 删除字符串中所有以<a开头，>结尾的子串，打印结果字符串
        System.out.println(Strs.removeAll(s, "<a", ">"));

    }

    @Test
    public void xorTest() {
        var s = "abc";

        byte key = 34;
        var encrypt = Strs.xor(s, key);
        System.out.println(encrypt);

        var decrypt = Strs.xor(encrypt, key);
        Assert.assertEquals(decrypt, s);
    }

    @Test
    public void isNumTest() {
        Assert.assertTrue(Strs.isNum("0"));
        Assert.assertTrue(Strs.isNum("+0"));
        Assert.assertTrue(Strs.isNum("-0"));
        Assert.assertTrue(Strs.isNum("234789283497"));
        Assert.assertTrue(Strs.isNum("-2323234"));
        Assert.assertTrue(Strs.isNum("324.7"));
        Assert.assertTrue(Strs.isNum("-123.1234"));
        Assert.assertTrue(Strs.isNum("+1.000000"));
        Assert.assertTrue(Strs.isNum("+0.000000"));

        Assert.assertFalse(Strs.isNum(""));
        Assert.assertFalse(Strs.isNum(" \n\r "));
        Assert.assertFalse(Strs.isNum("   "));
        Assert.assertFalse(Strs.isNum("+"));
        Assert.assertFalse(Strs.isNum("-"));
        Assert.assertFalse(Strs.isNum("234.234.2"));
        Assert.assertFalse(Strs.isNum("3-4"));
        Assert.assertFalse(Strs.isNum("142a234"));
        Assert.assertFalse(Strs.isNum("..3"));
        Assert.assertFalse(Strs.isNum("010"));
        Assert.assertFalse(Strs.isNum("+010"));
        Assert.assertFalse(Strs.isNum("+0 10"));
        Assert.assertFalse(Strs.isNum("+234.3224w"));
        Assert.assertFalse(Strs.isNum("+0."));
        Assert.assertFalse(Strs.isNum("0."));
    }
}
