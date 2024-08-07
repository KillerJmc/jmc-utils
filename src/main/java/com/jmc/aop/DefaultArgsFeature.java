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
     * 私有构造器
     */
    private DefaultArgsFeature() {}

    /**
     * 开启默认参数特性（@DefaultArg） <br>
     * 基于Aspectj LTW，会拦截正在加载中的类进行增强 <br>
     * 注意：请勿在调用此开启方法的类方法上使用@DefaultArg，因为该类已经加载完而无法被增强！
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
