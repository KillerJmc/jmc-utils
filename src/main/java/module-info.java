
/**
 * jmc.utils module
 * @since 1.9
 */
module jmc.utils {
    requires java.logging;
    requires static lombok;

    exports com.jmc.array;
    exports com.jmc.decompile;
    exports com.jmc.io;
    exports com.jmc.lang.extend;
    exports com.jmc.lang.math;
    exports com.jmc.lang.primitive;
    exports com.jmc.lang.rand;
    exports com.jmc.lang.reflect;
    exports com.jmc.lang.time;
    exports com.jmc.lang.timer;
    exports com.jmc.net;
    exports com.jmc.process;
    exports com.jmc.ref;
    exports com.jmc.util;
}