package com.jmc.lang.primitive;

import com.jmc.lang.extend.Objs;

/**
 * 字符增强类
 * @since 1.0
 * @author Jmc
 */
public class Chars {

    private Chars() {}

    /**
     * 判断多个字符中是否存在至少一个字符与提供的字符相等
     * @param c 提供的字符
     * @param cs 多个字符
     * @return 是否存在相等
     */
    public static boolean orEquals(char c, char... cs) {
        Objs.throwsIfNullOrEmpty(c);

        for (var t : cs) if (t == c) return true;

        return false;
    }
}
