/**
 * jmc.utils模块 <br>
 * 提供大量的工具类，简化日常开发。
 */
module jmc.utils {
    requires java.logging;
    requires jdk.attach;
    requires org.aspectj.weaver;
    requires static lombok;

    exports com.jmc.aop;
    exports com.jmc.array;
    exports com.jmc.io;
    exports com.jmc.lang;
    exports com.jmc.lang.ref;
    exports com.jmc.lang.reflect;
    exports com.jmc.lang.vm;
    exports com.jmc.math;
    exports com.jmc.math.exp;
    exports com.jmc.net;
    exports com.jmc.time;
    exports com.jmc.util;
}
