package com.jmc.test.array;

import com.jmc.array.Array;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class ArrayTest {
    @Test
    public void testSwap() {
        int[] a = { 1, 2, 3, 4, 5 };
        var arr = Array.of(a);

        swap(arr, 1, 3);

        // 交换后Array发生改变
        Assert.assertEquals("[ 1, 4, 3, 2, 5 ]", arr.toString());

        // 交换后原数组也会发生变化
        Assert.assertEquals("[1, 4, 3, 2, 5]", Arrays.toString(a));
    }

    // 这个方法可以对任意类型的数组（特别指包括基本数据类型）进行swap操作！
    @SuppressWarnings("all")
    private <T> void swap(Array<T> arr, int idx1, int idx2) {
        T t = arr.get(idx1);
        arr.set(idx1, arr.get(idx2));
        arr.set(idx2, t);
    }

    @Test
    public void testPrint() {
        var arr = Array.of(1, 2, 3, 4, 5);
        var arr2 = Array.of(6.0, 7.0, 8.0, 9.0, 10.0);
        var arr3 = Array.of("abcde".toCharArray());

        printArray(arr);
        printArray2(arr2);
        System.out.println(arr3);
    }

    private <T> void printArray(Array<T> arr) {
        System.out.print("[ ");
        for (T t : arr) {
            System.out.print(t + ", ");
        }
        System.out.println("\b\b ]");
    }

    private <T> void printArray2(Array<T> arr) {
        System.out.print("[ ");
        for (int i = 0; i < arr.len(); i++) {
            System.out.print(arr.get(i) + ", ");
        }
        System.out.println("\b\b ]");
    }
}
