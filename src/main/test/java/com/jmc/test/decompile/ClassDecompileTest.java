package com.jmc.test.decompile;

import com.jmc.decompile.ClassDecompile;
import org.junit.Test;

public class ClassDecompileTest {
    @Test
    public void test() throws Exception {
        @SuppressWarnings("unused")
        class Stu {
            int id;
            String name;
            String password;
        }
        System.out.println(ClassDecompile.decompile(Stu.class));
        System.out.println(ClassDecompile.decompile("java.lang.String"));
    }
}
