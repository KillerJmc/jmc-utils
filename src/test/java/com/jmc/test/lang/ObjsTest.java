package com.jmc.test.lang;

import com.jmc.lang.Objs;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class ObjsTest {
    @Test
    @SuppressWarnings("all")
    public void throwTest() {
        try {
            // 空对象，无信息提示
            Objs.throwsIfNullOrEmpty(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // 空字符串
            Objs.throwsIfNullOrEmpty("s", "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // 空列表
            Objs.throwsIfNullOrEmpty("list", new ArrayList<String>());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // 空对象
            Object o = null;
            Objs.throwsIfNullOrEmpty("obj", o);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // 直接传入null
            Objs.throwsIfNullOrEmpty("obj", (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void orTest() {
        Assert.assertTrue(Objs.orEquals('b', 'a', 'b', 'c'));
        Assert.assertFalse(Objs.orEquals("666", "777", "888"));
    }
}
