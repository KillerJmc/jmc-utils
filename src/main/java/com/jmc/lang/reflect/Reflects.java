package com.jmc.lang.reflect;

import com.jmc.io.Files;
import com.jmc.lang.Objs;
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

    // region info

    /**
     * 获取类中指定名称的成员变量
     * @param c 类的Class对象
     * @param fieldName 成员变量名称
     * @return 指定的成员变量
     * @apiNote <pre>{@code
     * class Student {
     *     private String name;
     * }
     *
     * // 获取Student类的name属性
     * Field nameField = Reflects.getField(Student.class, "name");
     * }</pre>
     * @since 1.5
     */
    public static Field getField(Class<?> c, String fieldName) {
        illegalAccessCheck(c);

        return Tries.tryReturnsT(() -> {
            Field f = c.getDeclaredField(fieldName);
            f.setAccessible(true);
            return f;
        }, e -> {
            if (e instanceof InaccessibleObjectException) {
                var packageName = Strs.subExclusive(e.getMessage(), "opens ", "\"");
                // Unable to make field %s accessible: module %s does not "opens %s" to unnamed module @%s
                throw new RuntimeException("获取静态字段失败，模块 %s 并不向你开放！".formatted(packageName));
            } else {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 获取指定的方法
     * @param c 类的Class对象
     * @param methodName 方法名称
     * @param parameterTypes 参数类型
     * @return 方法对象
     * @apiNote <pre>{@code
     * class Student {
     *     private Long id;
     *     private String name;
     *     void print(Long id, String name) {}
     * }
     *
     * // 获取Student类的print方法（需要指定方法名称和所有参数类型）
     * Method printMethod = Reflects.getMethod(Student.class, "print", Long.class, String.class);
     * }</pre>
     * @since 1.5
     */
    public static Method getMethod(Class<?> c, String methodName, Class<?>... parameterTypes) {
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
                throw new RuntimeException(e);
            }
        });
    }

    // endregion

    // region invoke

    /**
     * 动态创建一个类实例
     * @param c 类的Class对象
     * @param args 类的构造器参数
     * @return 构建的类实例
     * @param <T> 类对象的类型
     * @apiNote <pre>{@code
     * // 创建一个StringBuilder对象实例（需要传入构造器参数）
     * var instance = Reflects.newInstance(StringBuilder.class, "ABC");
     * }</pre>
     * @since 2.7
     */
    public static <T> T newInstance(Class<T> c, Object... args) {
        illegalAccessCheck(c);

        var argTypes = Arrays.stream(args)
                .map(Object::getClass)
                .toArray(Class[]::new);

        return Tries.tryReturnsT(() -> {
            var ctor = c.getDeclaredConstructor(argTypes);
            ctor.setAccessible(true);
            return ctor.newInstance(args);
        });
    }

    /**
     * 获取类中成员变量的值
     * @param instance 类的实例
     * @param fieldName 成员变量名称
     * @return 成员变量的值
     * @param <T> 类的类型
     * @apiNote <pre>{@code
     * class Student {
     *     private Long id;
     *     public Student(Long id) { this.id = id; }
     * }
     *
     * // 创建对象
     * var stu = new Student(3);
     *
     * // 获取其成员变量id的值（需要指定变量类型Long）
     * Long idValue = Reflects.getFieldValue(stu, "id");
     * }</pre>
     * @since 2.7
     */
    public static <T> T getFieldValue(Object instance, String fieldName) {
        return (T) Tries.tryReturnsT(() -> getField(instance.getClass(), fieldName).get(instance));
    }

    /**
     * 获取类中静态成员变量的值
     * @param c 类的Class对象
     * @param fieldName 静态成员变量名称
     * @return 静态成员变量的值
     * @param <T> 类的类型
     * @apiNote <pre>{@code
     * class Student {
     *     private static final Long id = 3;
     * }
     *
     * // 获取其静态变量id的值（需要指定变量类型Long）
     * Long idValue = Reflects.getFieldValue(Student.class, "id");
     * }</pre>
     * @since 2.7
     */
    public static <T> T getFieldValue(Class<?> c, String fieldName) {
        return (T) Tries.tryReturnsT(() -> getField(c, fieldName).get(null));
    }

    /**
     * 执行指定的方法（不支持参数存在基本数据类型的方法）
     * @param instance 类的实例
     * @param methodName 方法名称
     * @param args 方法参数
     * @param <R> 返回值类型
     * @return 方法返回值
     * @apiNote <pre>{@code
     * class Student {
     *     private Long id;
     *     private String name;
     *     String setName(String name) {
     *         var oldName = this.name;
     *         this.name = name;
     *         return oldName;
     *     }
     * }
     *
     * // 创建对象
     * var stu = new Student(1, "Jmc");
     *
     * // 执行setName方法（需要传入参数）并获取返回值（需要指定返回值类型String）
     * String oldName = Reflects.invokeMethod(stu, "setName", "Lucy");
     * }</pre>
     */
    public static <R> R invokeMethod(Object instance, String methodName, Object... args) {
        return (R) Tries.tryReturnsT(() -> getMethod(instance.getClass(), methodName,
                Arrays.stream(args).map(Object::getClass).toArray(Class[]::new)).invoke(instance, args));
    }

    /**
     * 执行指定的静态方法（不支持参数存在基本数据类型的方法）
     * @param c 类的Class对象
     * @param methodName 方法名称
     * @param args 方法参数
     * @param <R> 返回值类型
     * @return 方法返回值
     * @apiNote <pre>{@code
     * class Student {
     *     private Long id;
     *     private String name;
     *     static String info(Long id) { return ...; }
     * }
     *
     * // 执行静态方法info（需要传入参数）并获取返回值（需要指定返回值类型String）
     * String info = Reflects.invokeMethod(Student.class, "info", 3);
     * }</pre>
     * @since 1.5
     */
    public static <R> R invokeMethod(Class<?> c, String methodName, Object... args) {
        return (R) Tries.tryReturnsT(() -> getMethod(c, methodName,
                Arrays.stream(args).map(Object::getClass).toArray(Class[]::new)).invoke(null, args));
    }

    // endregion

    // region classloader

    /**
     * 从class文件加载Class对象
     * @param className 类名
     * @param classFileBytes class文件的byte数组
     * @return Class对象
     * @apiNote <pre>{@code
     * // 从classFileBytes字节数组中加载类名为com.jmc.Student的Class对象
     * Class<?> studentClass = Reflects.loadClass("com.jmc.Student", classFileBytes);
     * }</pre>
     * @since 3.2
     */
    public static Class<?> loadClass(String className, byte[] classFileBytes) {
        Objs.throwsIfNullOrEmpty(classFileBytes, className);

        var classLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                // 第一次调用从byte数组直接定义Class对象
                if (name.equals(className)) {
                    return defineClass(name, classFileBytes, 0, classFileBytes.length);
                }

                // 其他调用（由native方法调用，获取目标类的父类Class等）
                // 直接通过默认委派机制获取
                return super.loadClass(name);
            }
        };

        return Tries.tryReturnsT(() -> classLoader.loadClass(className));
    }

    /**
     * 从class文件加载Class对象
     * @param className 类名
     * @param classFilePath class文件路径
     * @return Class对象
     * @apiNote <pre>{@code
     * // 从Student.class文件中加载类名为com.jmc.Student的Class对象
     * Class<?> studentClass = Reflects.loadClass("com.jmc.Student", "/path/to/Student.class");
     * }</pre>
     * @since 3.2
     */
    public static Class<?> loadClass(String className, String classFilePath) {
        return loadClass(className, Files.readToBytes(classFilePath));
    }

    /**
     * 从jar文件加载Class对象
     * @param className 类名
     * @param jarFilePath jar文件路径
     * @return Class对象
     * @apiNote <pre>{@code
     * // 从test.jar文件中加载类名为com.jmc.Student的Class对象
     * Class<?> studentClass = Reflects.loadClassInJar("com.jmc.Student", "/path/to/test.jar");
     * }</pre>
     * @since 3.2
     */
    public static Class<?> loadClassInJar(String className, String jarFilePath) {
        Objs.throwsIfNullOrEmpty(className, jarFilePath);

        var classFileRelativePath = className.replace(".", "/")
                .concat(".class");

        var classFileURL = Tries.tryReturnsT(() ->
                new URL("jar:file:///%s!/%s".formatted(jarFilePath, classFileRelativePath))
        );

        try (var in = classFileURL.openStream()) {
            var bs = in.readAllBytes();
            return loadClass(className, bs);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // endregion

    // region serialization

    /**
     * 将一个对象实例写入byte数组
     * @param o 对象实例
     * @return 结果byte数组
     * @apiNote <pre>{@code
     * // 将String对象序列化到byte数组
     * byte[] bytes = Reflects.outObj("666");
     * }</pre>
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
     * @apiNote <pre>{@code
     * // 从byte数组（bytes）读取String对象（需要指定对象类型String）
     * String obj = Reflects.readObj(bytes);
     * }</pre>
     */
    public static <T> T readObj(byte[] bs) {
        try (var in = new ObjectInputStream(new ByteArrayInputStream(bs))) {
            return (T) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // endregion

    // region classpath

    /**
     * 判断类是否在jar包中
     * @param c 类的Class对象
     * @return 类是否在jar包中
     * @apiNote <pre>{@code
     * class Student {}
     * // 判断Student类是否在jar中
     * boolean res = Reflects.isClassInJar(Student.class);
     * }</pre>
     * @since 2.6
     */
    public static boolean isClassInJar(Class<?> c) {
        return getClassPath(c)
                .map(URL::getProtocol)
                .map("jar"::equals)
                .orElseThrow(() -> new RuntimeException("找不到类加载路径"));
    }

    /**
     * 获取jar的绝对路径
     * @param jarClass jar中的任意一个Class
     * @return jar的绝对路径
     * @apiNote <pre>{@code
     * class Student {}
     * // 获取Student类的jar的系统绝对路径
     * String path = Reflects.getJarPath(Student.class);
     * }</pre>
     * @since 3.0
     */
    public static String getJarPath(Class<?> jarClass) {
        if (!isClassInJar(jarClass)) {
            throw new RuntimeException("类 " + jarClass.getName() + " 并不在jar中！");
        }

        // 类加载路径 file:///path/xxx.jar!/
        var classPath = getClassPath(jarClass).orElseThrow().getPath();

        // 提取path/xxx.jar
        return Strs.subExclusive(classPath, "file:///", "!/");
    }

    /**
     * 获取指定类的类加载路径
     * @param c 类的Class对象
     * @return 类加载路径
     * @since 2.6
     * @apiNote <pre>{@code
     * class Student {}
     * // 获取Student类的类加载路径
     * URL url = Reflects.getClassPath(Student.class).orElseThrow();
     * }</pre>
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
     * @apiNote <pre>{@code
     * // 获取Reflect类路径的/com/jmc文件夹下的一级文件和文件夹
     * // 这个方法对jar、非jar都适用
     * Reflects.listResources(Reflects.class, "/com/jmc")
     *         .forEach(urlInfo -> {
     *             // 获取文件/文件夹url
     *             URL url = urlInfo.getUrl();
     *             // 获取文件/文件夹名称
     *             String name = urlInfo.getName();
     *             // 是否为文件
     *             boolean isFile = urlInfo.isFile();
     *         });
     * }</pre>
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

    // endregion
}
