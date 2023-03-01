package com.jmc.test.vm;

import com.jmc.lang.reflect.Reflects;
import com.jmc.lang.vm.JavaAgent;
import org.junit.Test;

public class JavaAgentTest {
    @Test
    public void test() throws ClassNotFoundException {
        // 尝试将aspectj agent加载到当前JVM
        JavaAgent.loadToSelf(
                Reflects.getJarPath(Class.forName("org.aspectj.weaver.tools.WeavingClassLoader"))
        );
    }
}
