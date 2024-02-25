package com.jmc.aop;

import com.jmc.lang.Tries;
import com.jmc.lang.reflect.Reflects;
import com.jmc.lang.vm.JavaAgent;

/**
 * 默认参数特性配置类
 * @since 3.0
 * @see DefaultArg
 * @author Jmc
 */
public class DefaultArgsFeature {
    /**
     * 开启默认参数特性（@DefaultArg） <br>
     * 基于Aspectj LTW <br>
     * 注意：请勿在使用特性的类中调用，请在其被JVM加载前调用！
     */
    public static void enable() {
        // 尝试获取aspectj weaver的jar中的一个class
        var aspectJWeaverClassName = "org.aspectj.weaver.tools.WeavingClassLoader";
        var aspectjWeaverJarClass = Tries.tryReturnsT(() -> Class.forName(aspectJWeaverClassName));

        // 获取aspectj weaver的jar路径
        var aspectjAgentJarPath = Reflects.getJarPath(aspectjWeaverJarClass);

        // 加载Java Agent到当前JVM
        JavaAgent.loadToSelf(aspectjAgentJarPath);
    }
}
