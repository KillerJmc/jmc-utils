package com.jmc.lang.reflect;

import com.jmc.lang.extend.Strs;
import com.jmc.lang.extend.Tries;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/**
 * 反射增强类
 * @since 1.0
 * @author Jmc
 */
@SuppressWarnings({"unchecked", "unused"})
public class Reflects {

    private Reflects() {}

    /**
     * 非法访问检测
     * @param c 被反射的类
     * @since 1.9
     */
    private static void illegalAccessCheck(Class<?> c) {
        if (c.getPackageName().startsWith("com.jmc")) {
            throw new RuntimeException(new IllegalAccessException("你不能反射jmc.utils模块中的类！"));
        }
    }

    /**
     * 执行指定的方法（不支持参数存在基本数据类型的方法）
     * @param instance 类的实例
     * @param methodName 方法名称
     * @param args 方法参数
     * @param <R> 返回值类型
     * @return 方法返回值
     */
    public static <R> R invokeMethod(Object instance, String methodName, Object... args) {
        return (R) Tries.tryReturnsT(() -> getMethod(instance, methodName,
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
        illegalAccessCheck(instance.getClass());

        return Tries.tryReturnsT(() -> {
            Method m = instance.getClass().getDeclaredMethod(methodName, parameterTypes);
            m.setAccessible(true);
            return m;
        }, e -> {
            if (e instanceof InaccessibleObjectException) {
                // Unable to make method %s accessible: module %s does not "opens %s" to unnamed module @%s
                var packageName = Strs.subExclusive(e.getMessage(), "opens ", "\"");
                throw new RuntimeException("获取方法失败，模块 %s 并不向你开放！".formatted(packageName));
            } else {
                e.printStackTrace();
            }
        });
    }


    /**
     * 获取类中指定名称的成员变量
     * @param instance 类的实例
     * @param fieldName 成员变量名称
     * @param <T> 成员变量类型
     * @return 指定的成员变量
     */
    public static <T> T getField(Object instance, String fieldName) {
        illegalAccessCheck(instance.getClass());

        return (T) Tries.tryReturnsT(() -> {
            Field f = instance.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            return f.get(instance);
        }, e -> {
            if (e instanceof InaccessibleObjectException) {
                var packageName = Strs.subExclusive(e.getMessage(), "opens ", "\"");
                // Unable to make field %s accessible: module %s does not "opens %s" to unnamed module @%s
                throw new RuntimeException("获取字段失败，模块 %s 并不向你开放！".formatted(packageName));
            } else {
                e.printStackTrace();
            }
        });
    }

    /**
     * 执行指定的静态方法（不支持参数存在基本数据类型的方法）
     * @param c 类对象
     * @param methodName 方法名称
     * @param args 方法参数
     * @param <R> 返回值类型
     * @return 方法返回值
     * @since 1.5
     */
    public static <R> R invokeStaticMethod(Class<?> c, String methodName, Object... args) {
        return (R) Tries.tryReturnsT(() -> getStaticMethod(c, methodName,
                Arrays.stream(args).map(Object::getClass).toArray(Class[]::new)).invoke(null, args));
    }

    /**
     * 获取指定的静态方法
     * @param c 类对象
     * @param methodName 方法名称
     * @param parameterTypes 参数类型
     * @return 方法对象
     * @since 1.5
     */
    public static Method getStaticMethod(Class<?> c, String methodName, Class<?>... parameterTypes) {
        illegalAccessCheck(c);

        return Tries.tryReturnsT(() -> {
            Method m = c.getDeclaredMethod(methodName, parameterTypes);
            m.setAccessible(true);
            return m;
        }, e -> {
            if (e instanceof InaccessibleObjectException) {
                // Unable to make method %s accessible: module %s does not "opens %s" to unnamed module @%s
                var packageName = Strs.subExclusive(e.getMessage(), "opens ", "\"");
                throw new RuntimeException("获取静态方法失败，模块 %s 并不向你开放！".formatted(packageName));
            } else {
                e.printStackTrace();
            }
        });
    }

    /**
     * 获取类中指定名称的静态成员变量
     * @param c 类对象
     * @param fieldName 成员变量名称
     * @param <T> 成员变量类型
     * @return 指定的成员变量
     * @since 1.5
     */
    public static <T> T getStaticField(Class<?> c, String fieldName) {
        illegalAccessCheck(c);

        return (T) Tries.tryReturnsT(() -> {
            Field f = c.getDeclaredField(fieldName);
            f.setAccessible(true);
            return f.get(null);
        }, e -> {
            if (e instanceof InaccessibleObjectException) {
                var packageName = Strs.subExclusive(e.getMessage(), "opens ", "\"");
                // Unable to make field %s accessible: module %s does not "opens %s" to unnamed module @%s
                throw new RuntimeException("获取静态字段失败，模块 %s 并不向你开放！".formatted(packageName));
            } else {
                e.printStackTrace();
            }
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
