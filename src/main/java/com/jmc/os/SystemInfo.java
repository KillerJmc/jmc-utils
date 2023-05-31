package com.jmc.os;

/**
 * 系统信息类
 * @since 3.5
 * @author Jmc
 */
public class SystemInfo {
    /**
     * 系统类型
     */
    public static final Type TYPE;

    static {
        // 系统名称的环境变量名称
        var osNamePropKey = "os.name";
        // 系统名称的环境变量值
        var osNameValue = System.getProperty(osNamePropKey);

        // 通过判断是否包含标记名称来确定系统类型
        if (osNameValue.contains(Type.WINDOWS.SYMBOL_NAME)) {
            TYPE = Type.WINDOWS;
        } else if (osNameValue.contains(Type.LINUX.SYMBOL_NAME)) {
            TYPE = Type.LINUX;
        } else {
            TYPE = Type.UNKNOWN;
        }
    }

    /**
     * 系统类型枚举
     */
    public enum Type {
        /**
         * Windows
         */
        WINDOWS("Windows"),

        /**
         * Linux
         */
        LINUX("Linux"),

        /**
         * 未知类型
         */
        UNKNOWN(null);

        /**
         * 在os.name系统变量中的标记名称
         */
        final String SYMBOL_NAME;

        Type(String symbolName) { SYMBOL_NAME = symbolName; }
    }
}
