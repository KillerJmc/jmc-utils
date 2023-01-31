package com.jmc.lang.reflect;

import com.jmc.lang.Strs;
import com.jmc.lang.Tries;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.jar.JarFile;
import java.util.stream.Stream;

/**
 * 反射增强类
 * @since 1.0
 * @author Jmc
 */
@SuppressWarnings("unchecked")
public class Reflects {
    private Reflects() {}

    /**
     * 非法访问检测
     * @param c 被反射的类
     * @since 1.9
     */
    private static void illegalAccessCheck(Class<?> c) {
        // Class必须非空
        if (c == null) {
            throw new NullPointerException("待检查的Class为空！");
        }

        // 获取Class对应的类路径
        var classPath = getClassPath(c).map(URL::getPath).orElse("");

        // 不能反射本模块的Class
        if (classPath.contains("jmc-utils")) {
            throw new RuntimeException(new IllegalAccessException("你不能反射jmc-utils模块中的类！"));
        }
    }

    /**
     * 动态创建一个类实例
     * @param c 类的Class对象
     * @param args 类的构造器参数
     * @return 构建的类实例
     * @param <T> 类对象的类型
     * @since 2.7
     */
    public static <T> T newInstance(Class<T> c, Object... args) {
        illegalAccessCheck(c);

        var argTypes = Arrays.stream(args)
                .map(Object::getClass)
                .toArray(Class[]::new);

        return Tries.tryReturnsT(() ->
                c.getDeclaredConstructor(argTypes).newInstance(args)
        );
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
     * @param c 类的Class对象
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
     * @param c 类的Class对象
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
     * @param c 类的Class对象
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

    /**
     * 判断类是否在jar包中
     * @param c 类的Class对象
     * @return 类是否在jar包中
     * @since 2.6
     */
    public static boolean isClassInJar(Class<?> c) {
        return getClassPath(c)
                .map(URL::getProtocol)
                .map("jar"::equals)
                .orElseThrow(() -> new RuntimeException("找不到类加载路径"));
    }

    /**
     * 获取指定类的类加载路径
     * @param c 类的Class对象
     * @return 类加载路径
     * @since 2.6
     */
    public static Optional<URL> getClassPath(Class<?> c) {
        return Optional.of(c)
                .map(Class::getProtectionDomain)
                .map(ProtectionDomain::getCodeSource)
                .map(CodeSource::getLocation)
                .map(classPath -> {
                    // 类加载路径
                    var classPathStr = classPath.getPath();

                    // 如果对应的是jar路径
                    if (classPathStr.endsWith(".jar")) {
                        // 返回标准的java jar URL
                        return Tries.tryReturnsT(() ->
                                // 替换 /D:/xxx.jar -> jar:file:///D:/xxx.jar!/
                                new URL("jar:file://%s!/".formatted(classPathStr)
                        ));
                    }
                    // 否则直接返回类路径
                    return classPath;
                });
    }

    /**
     * URL信息类
     * @since 2.6
     */
    @AllArgsConstructor(staticName = "of", access = AccessLevel.PRIVATE)
    @Getter
    @ToString
    public static class URLInfo {
        /**
         * URL对象
         */
        private URL url;

        /**
         * 文件/文件夹名称
         */
        private String name;

        /**
         * 是否为文件
         */
        private boolean isFile;
    }

    /**
     * 获取类路径下指定路径的一级文件/文件夹的URL信息列表
     * @param c 类的Class对象
     * @param path 指定的路径
     * @return 一级文件/文件夹的URL信息对象列表
     * @since 2.6
     */
    public static List<URLInfo> listResources(Class<?> c, String path) {
        // 获取类加载路径
        var classPath = getClassPath(c).orElseThrow(() -> new RuntimeException("找不到类加载路径"));

        // 处理jar路径
        if ("jar".equals(classPath.getProtocol())) {
            // 处理得到jar文件路径（jar:file:///path!/ -> path）
            var jarFilePath = Strs.subExclusive(classPath.toString(), "jar:file:///", "!/");

            // 读取jar文件
            try (var jarFile = new JarFile(jarFilePath)) {
                // 将用户给出的路径去掉开头的 /（如果有），如/a/b -> a/b
                path = path.startsWith("/") ? path.substring(1) : path;

                // 给用户的路径末尾加上 /（如果没有），a/b -> a/b/
                var rootPath = path.endsWith("/") ? path : path + "/";

                // 遍历jar文件中的entry
                return jarFile.stream()
                        // 去掉根目录本身
                        .filter(entry -> !entry.getName().equals(rootPath))
                        // 选择根目录下的所有文件/文件夹
                        .filter(entry -> entry.getName().startsWith(rootPath))
                        // 选择根目录下的一级文件/文件夹
                        .filter(entry -> {
                            // 获取entry相对根目录的路径，如rootPath/b/c -> b/c
                            var relativePath = Strs.subExclusive(entry.getName(), rootPath);
                            // 如果没有找到/或者找到的第一个/为最后一个字符，说明是一级文件/文件夹。如a.txt和b/
                            return !relativePath.contains("/") || relativePath.indexOf("/") == relativePath.length() - 1;
                        })
                        .map(entry -> {
                            // entry名称
                            var entryName = entry.getName();

                            // 通过拼接类路径和文件/文件夹名来构建URL
                            var url = Tries.tryReturnsT(() -> new URL(classPath, entryName));
                            // 获取文件/文件夹名
                            var name = entry.isDirectory() ? Strs.subExclusive(entryName, rootPath, "/")
                                    : Strs.subExclusive(entryName, rootPath);
                            // 获取是否为文件的标识
                            var isFile = !entry.isDirectory();

                            // 返回构建的URL信息对象
                            return URLInfo.of(url, name, isFile);
                        })
                        .toList();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        // 处理普通类路径
        // 通过File类的listFiles方法获取目录文件列表
        return Optional.ofNullable(new File(classPath.getPath() + File.separator + path).listFiles())
                // 转化为Stream
                .map(Arrays::stream)
                // 将文件列表流化为URL信息列表流
                .map(stream -> stream.map(file -> {
                    // 通过加上”file:///“前缀将文件地址转化为URL
                    var url = Tries.tryReturnsT(() -> new URL("file:///" + file.getAbsolutePath()));
                    // 获取文件名
                    var name = file.getName();
                    // 获取是否为文件的标识
                    var isFile = file.isFile();

                    // 返回构建的URL信息对象
                    return URLInfo.of(url, name, isFile);
                }))
                .map(Stream::toList)
                .orElse(List.of());
    }
}
