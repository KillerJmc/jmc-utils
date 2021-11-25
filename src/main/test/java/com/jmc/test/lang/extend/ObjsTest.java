package com.jmc.test.lang.extend;

import com.jmc.lang.extend.Objs;
import org.junit.Test;

import java.util.ArrayList;

public class ObjsTest {
    @Test
    @SuppressWarnings("all")
    public void test() {
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
            Objs.throwsIfNullOrEmpty("obj", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
