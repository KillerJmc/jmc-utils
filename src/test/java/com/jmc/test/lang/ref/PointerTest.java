package com.jmc.test.lang.ref;

import com.jmc.lang.ref.Pointer;
import org.junit.Assert;
import org.junit.Test;

public class PointerTest {
    @Test
    public void simpleTest() {
        // 声明一个指针，并绑定一个初始值
        var p = Pointer.of(1);

        // 打印指针内的值
        System.out.println(p.get());

        // 打印指针值的类型
        System.out.println(p.type());

        // 重新设置指针里的值
        p.reset(666);
        System.out.println(p.get());

        changeValue(p);
        System.out.println(p.get());

        // 直接打印指针
        System.out.println(p);
    }

    private void changeValue(Pointer<Integer> p) {
        // 使指针指向的值 + 1
        p.update(t -> ++t);
    }

    @Test
    public void emptyTest() {
        // 声明一个空指针
        Pointer<String> p = Pointer.empty();

        // 指向的值应该为null
        Assert.assertNull(p.get());

        // 不检查类型修改指向值
        p.resetUnchecked("data");

        // 获取并打印值
        System.out.println(p.get());
    }
}
