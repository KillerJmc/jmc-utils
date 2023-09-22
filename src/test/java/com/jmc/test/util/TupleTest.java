package com.jmc.test.util;

import com.jmc.util.Tuple;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class TupleTest {
    @Test
    public void simpleTest() {
        var tuple = Tuple.of(666, "Jmc", '男');
        // 获取元组元素个数
        Assert.assertEquals(3, tuple.size());

        // 获取元组的元素
        int id = tuple.get(1);
        String name = tuple.get(2);
        char gender = tuple.get(3);
        Assert.assertEquals(666, id);
        Assert.assertEquals("Jmc", name);
        Assert.assertEquals('男', gender);
    }

    @Test
    public void namedTest() {
        int id = 666;
        var name = "Jmc";
        char gender = '男';

        var tuple = Tuple.fromNamed(Map.of(
                "id", id,
                "name", name,
                "gender", gender
        ));
        // 获取元组元素个数
        Assert.assertEquals(3, tuple.size());

        // 通过元素名获取元组的元素
        int id1 = tuple.get("id");
        var name1 = tuple.<String>get("name");
        char gender1 = tuple.get("gender");

        Assert.assertEquals(id, id1);
        Assert.assertEquals(name, name1);
        Assert.assertEquals(gender, gender1);

        // 仍然可以通过下标获取元素
        Assert.assertEquals(id, (int) tuple.get(1));
        Assert.assertEquals(name, tuple.get(2));
        Assert.assertEquals((Object) gender, tuple.get(3));
    }
}
