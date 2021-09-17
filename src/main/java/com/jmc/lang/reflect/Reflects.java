package com.jmc.lang.reflect;

import com.jmc.lang.extend.Tries;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 反射增强类
 * @since 1.0.0
 * @author Jmc
 */
@SuppressWarnings({"unchecked", "unused"})
public class Reflects {
    /**
     * 执行指定的方法（不支持参数存在基本数据类型的方法）
     * @param instance 类的实例
     * @param methodName 方法名称
     * @param args 方法参数
     * @return 方法返回值
     */
    public static <T> T invokeMethod(Object instance, String methodName, Object... args) {
        return (T) Tries.tryReturnsT(() -> getMethod(instance, methodName,
                Arrays.stream(args).map(Object::getClass).toArray(Class[]::new)).invoke(instance, args));
    }

    /**
     * 获取指定的方法
     * @param instance 类的实例
     * @param methodName 方法名称
     * @param parameterTypes 参数类型
     * @return 方法对象
     */
    public static Method getMethod(Object instance, String methodName, Class<?>... parameterTypes) {
        return Tries.tryReturnsT(() -> {
            Method m = instance.getClass().getDeclaredMethod(methodName, parameterTypes);
            m.setAccessible(true);
            return m;
        });
    }

    /**
     * 获取类中指定名称的成员变量
     * @param instance 类的实例
     * @param fieldName 成员变量名称
     * @return 指定的成员变量
     */
    public static <T> T getField(Object instance, String fieldName) {
        return (T) Tries.tryReturnsT(() -> {
            Field f = instance.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            return f.get(instance);
        });
    }

    /**
     * 将一个对象实例写入byte数组
     * @param o 对象实例
     * @return 结果byte数组
     */
    public static byte[] outObj(Object o) {
        var out = new ByteArrayOutputStream();

        try (var oos = new ObjectOutputStream(out)) {
            oos.writeObject(o);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }

    /**
     * 从byte数组中读取一个对象实例
     * @param bs byte数组
     * @param <T> 对象类型
     * @return 读取的对象
     */
    public static <T> T readObj(byte[] bs) {
        try (var in = new ObjectInputStream(new ByteArrayInputStream(bs))) {
            return (T) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
