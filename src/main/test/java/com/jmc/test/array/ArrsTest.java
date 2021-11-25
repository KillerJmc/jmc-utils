package com.jmc.test.array;

import com.jmc.array.Array;
import com.jmc.array.Arrs;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class ArrsTest {
    @Test
    @SuppressWarnings("all")
    public void testCreate() {
        var a = Arrs.newInstance(ArrayList.class, 6);
        var arr = Array.of(a);
        // 每个元素都是初始化好的
        arr.forEach(l -> System.out.println(l.size()));
    }

    @Test
    public void testSwap() {
        int[] a = {1, 2, 3, 4, 5};
        var arr = Array.of(a);

        Arrs.swap(arr, 1, 3);

        System.out.println(arr);
        // 原数组也会发生变化
        System.out.println(Arrays.toString(a));
    }

    @Test
    public void testRand() {
        // 可能含重复元素的随机数组
        var a = Arrs.getRandArr(1, 10, 10);
        System.out.println(Arrays.toString(a));

        // 不含重复元素的随机数组
        a = Arrs.getDiffRandArr(1, 10, 10);
        System.out.println(Arrays.toString(a));
    }
}
