package com.jmc.lang.ref;

import com.jmc.lang.Tries;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * 函数引用 <br><br>
 * 可将方法作为参数传入另一个方法以便调用，且支持直接传入lambda <br>
 * （参数均必须小于等于7个，支持传入基本数据类型）
 * @apiNote <pre>{@code
 * // add方法
 * public int add(int a, int b) {
 *     return a + b;
 * }
 *
 * // 执行指定add方法
 * // 这里的泛型Integer指的是方法返回值类型
 * public void invokeAdd(Func<Integer> addFunc, int a, int b) {
 *     // 执行函数指针并获取返回值
 *     int res = addFunc.invoke(a, b);
 *     System.out.println(res);
 * }
 *
 * public void test() {
 *     // 绑定一个方法作为函数指针
 *     var addFunc = Func.of(this::add);
 *     // 将函数指针传入方法
 *     invokeAdd(addFunc, 2, 3);
 *
 *     // 绑定一个方法和其中一个参数作为偏函数指针
 *     var bindFunc = Func.partial(this::add, 3);
 *     // 传入剩余参数，执行这个函数指针并获得返回值（7）
 *     int res2 = bindFunc.invoke(4);
 *
 *     // 绑定一个lambda作为函数指针
 *     var lambdaFunc = Func.of((String a, String b) -> a + b);
 *     // 执行这个函数指针并获得返回值（"12"）
 *     String res3 = lambdaFunc.invoke("1", "2");
 *
 *     // 绑定一个纯基本数据类型的lambda作为函数指针（需要补充泛型）
 *     var numberLambdaFunc = Func.<Long>of((a, b) -> a - b);
 *     // 执行这个函数指针并获得返回值（3）
 *     long res4 = numberLambdaFunc.invoke(7L, 4L);
 * }
 * }</pre>
 * @since 1.0
 * @author Jmc
 * @param <R> 返回值类型
 */
@SuppressWarnings({"unchecked", "unused"})
public abstract class Func<R> {

    private Func() {}

    /**
     * 执行方法
     * @param args 参数
     * @return 返回值
     */
    public abstract R invoke(Object... args);

    /**
     * 使用反射调用执行方法
     * @param funcInterfaceObj 承载函数的接口对象
     * @param args 参数
     * @return 返回值
     * @since 3.8
     */
    protected R invokeUsingReflection(Object funcInterfaceObj, Object... args) {
        var invokeMethodName = "invoke";

        return (R) Tries.tryReturnsT(() -> {
            var funcClass = funcInterfaceObj.getClass();
            var methods = funcClass.getDeclaredMethods();
            for (var method : methods) {
                if (method.getName().equals(invokeMethodName)) {
                    method.setAccessible(true);
                    return method.invoke(funcInterfaceObj, args);
                }
            }
            throw new RuntimeException("没有找到执行方法：" + invokeMethodName);
        });
    }

    /**
     * 检查调用方法的参数个数是否匹配
     * @param realSize 实际参数个数
     * @param assertSize 需要参数个数
     */
    protected static void checkInvokeParams(int realSize, int assertSize) {
        if (realSize != assertSize) {
            throw new IllegalArgumentException("调用参数个数不匹配，需要%d个，实际提供了%d个".formatted(assertSize, realSize));
        }
    }

    /**
     * 检查partial方法的参数列表是否合法
     * @param args    参数列表
     * @param maxArgSize 最大参数个数
     * @return 参数列表
     * @since 3.8
     */
    private static Object[] checkPartialParams(Object[] args, int maxArgSize) {
        if (args == null) {
            args = new Object[0];
        }
        if (args.length > maxArgSize) {
            throw new IllegalArgumentException("调用参数过多，最多%d个，实际提供了%d个"
                    .formatted(maxArgSize, args.length));
        }
        return args;
    }

    /**
     * 返回一个函数引用实例
     * @param m lambda表达式或方法引用
     * @return 函数引用实例
     */
    public static Func<Void> of(Void0 m) {
        return new Func<>() {
            @Override
            public Void invoke(Object... args) {
                checkInvokeParams(args.length, 0);
                m.invoke();
                return null;
            }
        };
    }

    /**
     * 返回一个函数引用实例
     * @param m lambda表达式或方法引用
     * @param <T> 参数类型
     * @return 函数引用实例
     */
    public static <T> Func<Void> of(Void1<T> m) {
        return new Func<>() {
            @Override
            public Void invoke(Object... args) {
                checkInvokeParams(args.length, 1);
                m.invoke((T) args[0]);
                return null;
            }
        };
    }

    /**
     * 返回一个函数引用实例
     * @param m lambda表达式或方法引用
     * @param <T> 参数1类型
     * @param <U> 参数2类型
     * @return 函数引用实例
     */
    public static <T, U> Func<Void> of(Void2<T, U> m) {
        return new Func<>() {
            @Override
            public Void invoke(Object... args) {
                checkInvokeParams(args.length, 2);
                m.invoke((T) args[0], (U) args[1]);
                return null;
            }
        };
    }

    /**
     * 返回一个函数引用实例
     * @param m lambda表达式或方法引用
     * @param <T> 参数1类型
     * @param <U> 参数2类型
     * @param <V> 参数3类型
     * @return 函数引用实例
     */
    public static <T, U, V> Func<Void> of(Void3<T, U, V> m) {
        return new Func<>() {
            @Override
            public Void invoke(Object... args) {
                checkInvokeParams(args.length, 3);
                m.invoke((T) args[0], (U) args[1], (V) args[2]);
                return null;
            }
        };
    }

    /**
     * 返回一个函数引用实例
     * @param m lambda表达式或方法引用
     * @param <T> 参数1类型
     * @param <U> 参数2类型
     * @param <V> 参数3类型
     * @param <W> 参数4类型
     * @return 函数引用实例
     */
    public static <T, U, V, W> Func<Void> of(Void4<T, U, V, W> m) {
        return new Func<>() {
            @Override
            public Void invoke(Object... args) {
                checkInvokeParams(args.length, 4);
                m.invoke((T) args[0], (U) args[1], (V) args[2], (W) args[3]);
                return null;
            }
        };
    }

    /**
     * 返回一个函数引用实例
     * @param m lambda表达式或方法引用
     * @param <T> 参数1类型
     * @param <U> 参数2类型
     * @param <V> 参数3类型
     * @param <W> 参数4类型
     * @param <X> 参数5类型
     * @return 函数引用实例
     */
    public static <T, U, V, W, X> Func<Void> of(Void5<T, U, V, W, X> m) {
        return new Func<>() {
            @Override
            public Void invoke(Object... args) {
                checkInvokeParams(args.length, 5);
                m.invoke((T) args[0], (U) args[1], (V) args[2], (W) args[3], (X) args[4]);
                return null;
            }
        };
    }

    /**
     * 返回一个函数引用实例
     * @param m lambda表达式或方法引用
     * @param <T> 参数1类型
     * @param <U> 参数2类型
     * @param <V> 参数3类型
     * @param <W> 参数4类型
     * @param <X> 参数5类型
     * @param <Y> 参数6类型
     * @return 函数引用实例
     */
    public static <T, U, V, W, X, Y> Func<Void> of(Void6<T, U, V, W, X, Y> m) {
        return new Func<>() {
            @Override
            public Void invoke(Object... args) {
                checkInvokeParams(args.length, 6);
                m.invoke((T) args[0], (U) args[1], (V) args[2], (W) args[3], (X) args[4], (Y) args[5]);
                return null;
            }
        };
    }

    /**
     * 返回一个函数引用实例
     * @param m lambda表达式或方法引用
     * @param <T> 参数1类型
     * @param <U> 参数2类型
     * @param <V> 参数3类型
     * @param <W> 参数4类型
     * @param <X> 参数5类型
     * @param <Y> 参数6类型
     * @param <Z> 参数7类型
     * @return 函数引用实例
     */
    public static <T, U, V, W, X, Y, Z> Func<Void> of(Void7<T, U, V, W, X, Y, Z> m) {
        return new Func<>() {
            @Override
            public Void invoke(Object... args) {
                checkInvokeParams(args.length, 7);
                m.invoke((T) args[0], (U) args[1], (V) args[2], (W) args[3], (X) args[4], (Y) args[5], (Z) args[6]);
                return null;
            }
        };
    }

    /**
     * 返回一个函数引用实例
     * @param m lambda表达式或方法引用
     * @param <R> 返回值类型
     * @return 函数引用实例
     */
    public static <R> Func<R> of(Object0<R> m) {
        return new Func<>() {
            @Override
            public R invoke(Object... args) {
                checkInvokeParams(args.length, 0);
                return m.invoke();
            }
        };
    }

    /**
     * 返回一个函数引用实例
     * @param m lambda表达式或方法引用
     * @param <T> 参数类型
     * @param <R> 返回值类型
     * @return 函数引用实例
     */
    public static <T, R> Func<R> of(Object1<T, R> m) {
        return new Func<>() {
            @Override
            public R invoke(Object... args) {
                checkInvokeParams(args.length, 1);
                return m.invoke((T) args[0]);
            }
        };
    }

    /**
     * 返回一个函数引用实例
     * @param m lambda表达式或方法引用
     * @param <T> 参数1类型
     * @param <U> 参数2类型
     * @param <R> 返回值类型
     * @return 函数引用实例
     */
    public static <T, U, R> Func<R> of(Object2<T, U, R> m) {
        return new Func<>() {
            @Override
            public R invoke(Object... args) {
                checkInvokeParams(args.length, 2);
                return m.invoke((T) args[0], (U) args[1]);
            }
        };
    }

    /**
     * 返回一个函数引用实例
     * @param m lambda表达式或方法引用
     * @param <T> 参数1类型
     * @param <U> 参数2类型
     * @param <V> 参数3类型
     * @param <R> 返回值类型
     * @return 函数引用实例
     */
    public static <T, U, V, R> Func<R> of(Object3<T, U, V, R> m) {
        return new Func<>() {
            @Override
            public R invoke(Object... args) {
                checkInvokeParams(args.length, 3);
                return m.invoke((T) args[0], (U) args[1], (V) args[2]);
            }
        };
    }

    /**
     * 返回一个函数引用实例
     * @param m lambda表达式或方法引用
     * @param <T> 参数1类型
     * @param <U> 参数2类型
     * @param <V> 参数3类型
     * @param <W> 参数4类型
     * @param <R> 返回值类型
     * @return 函数引用实例
     */
    public static <T, U, V, W, R> Func<R> of(Object4<T, U, V, W, R> m) {
        return new Func<>() {
            @Override
            public R invoke(Object... args) {
                checkInvokeParams(args.length, 4);
                return m.invoke((T) args[0], (U) args[1], (V) args[2], (W) args[3]);
            }
        };
    }

    /**
     * 返回一个函数引用实例
     * @param m lambda表达式或方法引用
     * @param <T> 参数1类型
     * @param <U> 参数2类型
     * @param <V> 参数3类型
     * @param <W> 参数4类型
     * @param <X> 参数5类型
     * @param <R> 返回值类型
     * @return 函数引用实例
     */
    public static <T, U, V, W, X, R> Func<R> of(Object5<T, U, V, W, X, R> m) {
        return new Func<>() {
            @Override
            public R invoke(Object... args) {
                checkInvokeParams(args.length, 5);
                return m.invoke((T) args[0], (U) args[1], (V) args[2], (W) args[3], (X) args[4]);
            }
        };
    }

    /**
     * 返回一个函数引用实例
     * @param m lambda表达式或方法引用
     * @param <T> 参数1类型
     * @param <U> 参数2类型
     * @param <V> 参数3类型
     * @param <W> 参数4类型
     * @param <X> 参数5类型
     * @param <Y> 参数6类型
     * @param <R> 返回值类型
     * @return 函数引用实例
     */
    public static <T, U, V, W, X, Y, R> Func<R> of(Object6<T, U, V, W, X, Y, R> m) {
        return new Func<>() {
            @Override
            public R invoke(Object... args) {
                checkInvokeParams(args.length, 6);
                return m.invoke((T) args[0], (U) args[1], (V) args[2], (W) args[3], (X) args[4], (Y) args[5]);
            }
        };
    }

    /**
     * 返回一个函数引用实例
     * @param m lambda表达式或方法引用
     * @param <T> 参数1类型
     * @param <U> 参数2类型
     * @param <V> 参数3类型
     * @param <W> 参数4类型
     * @param <X> 参数5类型
     * @param <Y> 参数6类型
     * @param <Z> 参数7类型
     * @param <R> 返回值类型
     * @return 函数引用实例
     */
    public static <T, U, V, W, X, Y, Z, R> Func<R> of(Object7<T, U, V, W, X, Y, Z, R> m) {
        return new Func<>() {
            @Override
            public R invoke(Object... args) {
                checkInvokeParams(args.length, 7);
                return m.invoke((T) args[0], (U) args[1], (V) args[2],
                        (W) args[3], (X) args[4], (Y) args[5], (Z) args[6]);
            }
        };
    }

    /**
     * 返回一个函数引用实例
     * @param m 参数为单个数字的代码块或方法引用
     * @param <T> 数字类型
     * @return 函数引用实例
     * @since 1.4
     */
    public static <T extends Number> Func<T> of(Number1<T> m) {
        return new Func<>() {
            @Override
            public T invoke(Object... args) {
                checkInvokeParams(args.length, 1);
                return m.invoke((T) args[0]);
            }
        };
    }

    /**
     * 返回一个函数引用实例
     * @param m 参数为2个数字的代码块或方法引用
     * @param <T> 数字类型
     * @return 函数引用实例
     * @since 1.4
     */
    public static <T extends Number> Func<T> of(Number2<T> m) {
        return new Func<>() {
            @Override
            public T invoke(Object... args) {
                checkInvokeParams(args.length, 2);
                return m.invoke((T) args[0], (T) args[1]);
            }
        };
    }

    /**
     * 返回一个函数引用实例
     * @param m 参数为3个数字的代码块或方法引用
     * @param <T> 数字类型
     * @return 函数引用实例
     * @since 1.4
     */
    public static <T extends Number> Func<T> of(Number3<T> m) {
        return new Func<>() {
            @Override
            public T invoke(Object... args) {
                checkInvokeParams(args.length, 3);
                return m.invoke((T) args[0], (T) args[1], (T) args[2]);
            }
        };
    }

    /**
     * 返回一个函数引用实例
     * @param m 参数为4个数字的代码块或方法引用
     * @param <T> 数字类型
     * @return 函数引用实例
     * @since 1.4
     */
    public static <T extends Number> Func<T> of(Number4<T> m) {
        return new Func<>() {
            @Override
            public T invoke(Object... args) {
                checkInvokeParams(args.length, 4);
                return m.invoke((T) args[0], (T) args[1], (T) args[2], (T) args[3]);
            }
        };
    }

    /**
     * 返回一个函数引用实例
     * @param m 参数为5个数字的代码块或方法引用
     * @param <T> 数字类型
     * @return 函数引用实例
     * @since 1.4
     */
    public static <T extends Number> Func<T> of(Number5<T> m) {
        return new Func<>() {
            @Override
            public T invoke(Object... args) {
                checkInvokeParams(args.length, 5);
                return m.invoke((T) args[0], (T) args[1], (T) args[2], (T) args[3], (T) args[4]);
            }
        };
    }

    /**
     * 返回一个函数引用实例
     * @param m 参数为6个数字的代码块或方法引用
     * @param <T> 数字类型
     * @return 函数引用实例
     * @since 1.4
     */
    public static <T extends Number> Func<T> of(Number6<T> m) {
        return new Func<>() {
            @Override
            public T invoke(Object... args) {
                checkInvokeParams(args.length, 6);
                return m.invoke((T) args[0], (T) args[1], (T) args[2], (T) args[3], (T) args[4], (T) args[5]);
            }
        };
    }

    /**
     * 返回一个函数引用实例
     * @param m 参数为7个数字的代码块或方法引用
     * @param <T> 数字类型
     * @return 函数引用实例
     * @since 1.4
     */
    public static <T extends Number> Func<T> of(Number7<T> m) {
        return new Func<>() {
            @Override
            public T invoke(Object... args) {
                checkInvokeParams(args.length, 7);
                return m.invoke((T) args[0], (T) args[1], (T) args[2],
                        (T) args[3], (T) args[4], (T) args[5], (T) args[6]);
            }
        };
    }

    /**
     * 获取偏函数的调用对象
     * @param funcInterfaceObj 承载函数的接口对象
     * @param partialArgs 偏函数的前置参数
     * @param maxArgSize 函数最多参数个数
     * @return 偏函数调用对象
     * @param <R> 返回值类型
     * @since 3.8
     */
    private static <R> Func<R> getPartialFunc(Object funcInterfaceObj, Object[] partialArgs, int maxArgSize) {
        var validPartialArgs = checkPartialParams(partialArgs, maxArgSize);

        return new Func<>() {
            @Override
            public R invoke(Object... args) {
                checkInvokeParams(args.length, maxArgSize - validPartialArgs.length);
                var completedArgs = Stream.concat(Arrays.stream(validPartialArgs), Arrays.stream(args))
                        .toArray(Object[]::new);
                return invokeUsingReflection(funcInterfaceObj, completedArgs);
            }
        };
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块
     * @param args 参数列表
     * @return 函数引用实例
     * @since 3.8
     */
    public static Func<Void> partial(Void1<?> m, Object... args) {
        return getPartialFunc(m, args, 1);
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块
     * @param args 参数列表
     * @return 函数引用实例
     * @since 3.8
     */
    public static Func<Void> partial(Void2<?, ?> m, Object... args) {
        return getPartialFunc(m, args, 2);
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块
     * @param args 参数列表
     * @return 函数引用实例
     * @since 3.8
     */
    public static Func<Void> partial(Void3<?, ?, ?> m, Object... args) {
        return getPartialFunc(m, args, 3);
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块
     * @param args 参数列表
     * @return 函数引用实例
     * @since 3.8
     */
    public static Func<Void> partial(Void4<?, ?, ?, ?> m, Object... args) {
        return getPartialFunc(m, args, 4);
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块
     * @param args 参数列表
     * @return 函数引用实例
     * @since 3.8
     */
    public static Func<Void> partial(Void5<?, ?, ?, ?, ?> m, Object... args) {
        return getPartialFunc(m, args, 5);
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块
     * @param args 参数列表
     * @return 函数引用实例
     * @since 3.8
     */
    public static Func<Void> partial(Void6<?, ?, ?, ?, ?, ?> m, Object... args) {
        return getPartialFunc(m, args, 6);
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块
     * @param args 参数列表
     * @return 函数引用实例
     * @since 3.8
     */
    public static Func<Void> partial(Void7<?, ?, ?, ?, ?, ?, ?> m, Object... args) {
        return getPartialFunc(m, args, 7);
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块
     * @param args 参数列表
     * @param <T> 参数1类型
     * @param <R> 返回值类型
     * @return 函数引用实例
     * @since 3.8
     */
    public static <T, R> Func<R> partial(Object1<T, R> m, Object... args) {
        return getPartialFunc(m, args, 1);
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块
     * @param args 参数列表
     * @param <T> 参数1类型
     * @param <U> 参数2类型
     * @param <R> 返回值类型
     * @return 函数引用实例
     * @since 3.8
     */
    public static <T, U, R> Func<R> partial(Object2<T, U, R> m, Object... args) {
        return getPartialFunc(m, args, 2);
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块
     * @param args 参数列表
     * @param <T> 参数1类型
     * @param <U> 参数2类型
     * @param <V> 参数3类型
     * @param <R> 返回值类型
     * @return 函数引用实例
     * @since 3.8
     */
    public static <T, U, V, R> Func<R> partial(Object3<T, U, V, R> m, Object... args) {
        return getPartialFunc(m, args, 3);
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块
     * @param args 参数列表
     * @param <T> 参数1类型
     * @param <U> 参数2类型
     * @param <V> 参数3类型
     * @param <W> 参数4类型
     * @param <R> 返回值类型
     * @return 函数引用实例
     * @since 3.8
     */
    public static <T, U, V, W, R> Func<R> partial(Object4<T, U, V, W, R> m, Object... args) {
        return getPartialFunc(m, args, 4);
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块
     * @param args 参数列表
     * @param <R> 返回值类型
     * @param <T> 参数1类型
     * @param <U> 参数2类型
     * @param <V> 参数3类型
     * @param <W> 参数4类型
     * @param <X> 参数5类型
     * @return 函数引用实例
     * @since 3.8
     */
    public static <T, U, V, W, X, R> Func<R> partial(Object5<T, U, V, W, X, R> m, Object... args) {
        return getPartialFunc(m, args, 5);
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块
     * @param args 参数列表
     * @param <T> 参数1类型
     * @param <U> 参数2类型
     * @param <V> 参数3类型
     * @param <W> 参数4类型
     * @param <X> 参数5类型
     * @param <Y> 参数6类型
     * @param <R> 返回值类型
     * @return 函数引用实例
     * @since 3.8
     */
    public static <T, U, V, W, X, Y, R> Func<R> partial(Object6<T, U, V, W, X, Y, R> m, Object... args) {
        return getPartialFunc(m, args, 6);
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块
     * @param args 参数列表
     * @param <T> 参数1类型
     * @param <U> 参数2类型
     * @param <V> 参数3类型
     * @param <W> 参数4类型
     * @param <X> 参数5类型
     * @param <Y> 参数6类型
     * @param <Z> 参数6类型
     * @param <R> 返回值类型
     * @return 函数引用实例
     * @since 3.8
     */
    public static <T, U, V, W, X, Y, Z, R> Func<R> partial(Object7<T, U, V, W, X, Y, Z, R> m, Object... args) {
        return getPartialFunc(m, args, 7);
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块（含数字参数的）
     * @param args 参数列表
     * @param <T> 数字类型
     * @return 函数引用实例
     * @since 3.8
     */
    public static <T extends Number> Func<T> partial(Number1<T> m, T... args) {
        return getPartialFunc(m, args, 1);
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块（含数字参数的）
     * @param args 参数列表
     * @param <T> 数字类型
     * @return 函数引用实例
     * @since 3.8
     */
    public static <T extends Number> Func<T> partial(Number2<T> m, T... args) {
        return getPartialFunc(m, args, 2);
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块（含数字参数的）
     * @param args 参数列表
     * @param <T> 数字类型
     * @return 函数引用实例
     * @since 3.8
     */
    public static <T extends Number> Func<T> partial(Number3<T> m, T... args) {
        return getPartialFunc(m, args, 3);
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块（含数字参数的）
     * @param args 参数列表
     * @param <T> 数字类型
     * @return 函数引用实例
     * @since 3.8
     */
    public static <T extends Number> Func<T> partial(Number4<T> m, T... args) {
        return getPartialFunc(m, args, 4);
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块（含数字参数的）
     * @param args 参数列表
     * @param <T> 数字类型
     * @return 函数引用实例
     * @since 3.8
     */
    public static <T extends Number> Func<T> partial(Number5<T> m, T... args) {
        return getPartialFunc(m, args, 5);
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块（含数字参数的）
     * @param args 参数列表
     * @param <T> 数字类型
     * @return 函数引用实例
     * @since 3.8
     */
    public static <T extends Number> Func<T> partial(Number6<T> m, T... args) {
        return getPartialFunc(m, args, 6);
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块（含数字参数的）
     * @param args 参数列表
     * @param <T> 数字类型
     * @return 函数引用实例
     * @since 3.8
     */
    public static <T extends Number> Func<T> partial(Number7<T> m, T... args) {
        return getPartialFunc(m, args, 7);
    }

    /**
     * 无接收参数，无返回值的函数引用或代码块的接口
     */
    @FunctionalInterface
    public interface Void0 {
        /**
         * 执行方法
         */
        void invoke();
    }

    /**
     * 接收1个参数，无返回值的函数引用或代码块的接口
     * @param <T> 参数类型
     */
    @FunctionalInterface
    public interface Void1<T> {
        /**
         * 执行方法
         * @param t 参数
         */
        void invoke(T t);
    }

    /**
     * 接收2个参数，无返回值的函数引用或代码块的接口
     * @param <T> 参数类型1
     * @param <U> 参数类型2
     */
    @FunctionalInterface
    public interface Void2<T, U> {
        /**
         * 执行方法
         * @param t 参数1
         * @param u 参数2
         */
        void invoke(T t, U u);
    }

    /**
     * 接收3个参数，无返回值的函数引用或代码块的接口
     * @param <T> 参数类型1
     * @param <U> 参数类型2
     * @param <V> 参数类型3
     */
    @FunctionalInterface
    public interface Void3<T, U, V> {
        /**
         * 执行方法
         * @param t 参数1
         * @param u 参数2
         * @param v 参数3
         */
        void invoke(T t, U u, V v);
    }

    /**
     * 接收4个参数，无返回值的函数引用或代码块的接口
     * @param <T> 参数类型1
     * @param <U> 参数类型2
     * @param <V> 参数类型3
     * @param <W> 参数类型4
     */
    @FunctionalInterface
    public interface Void4<T, U, V, W> {
        /**
         * 执行方法
         * @param t 参数1
         * @param u 参数2
         * @param v 参数3
         * @param w 参数4
         */
        void invoke(T t, U u, V v, W w);
    }

    /**
     * 接收5个参数，无返回值的函数引用或代码块的接口
     * @param <T> 参数类型1
     * @param <U> 参数类型2
     * @param <V> 参数类型3
     * @param <W> 参数类型4
     * @param <X> 参数类型5
     */
    @FunctionalInterface
    public interface Void5<T, U, V, W, X> {
        /**
         * 执行方法
         * @param t 参数1
         * @param u 参数2
         * @param v 参数3
         * @param w 参数4
         * @param x 参数5
         */
        void invoke(T t, U u, V v, W w, X x);
    }

    /**
     * 接收6个参数，无返回值的函数引用或代码块的接口
     * @param <T> 参数类型1
     * @param <U> 参数类型2
     * @param <V> 参数类型3
     * @param <W> 参数类型4
     * @param <X> 参数类型5
     * @param <Y> 参数类型6
     */
    @FunctionalInterface
    public interface Void6<T, U, V, W, X, Y> {
        /**
         * 执行方法
         * @param t 参数1
         * @param u 参数2
         * @param v 参数3
         * @param w 参数4
         * @param x 参数5
         * @param y 参数6
         */
        void invoke(T t, U u, V v, W w, X x, Y y);
    }

    /**
     * 接收7个参数，无返回值的函数引用或代码块的接口
     * @param <T> 参数类型1
     * @param <U> 参数类型2
     * @param <V> 参数类型3
     * @param <W> 参数类型4
     * @param <X> 参数类型5
     * @param <Y> 参数类型6
     * @param <Z> 参数类型7
     */
    @FunctionalInterface
    public interface Void7<T, U, V, W, X, Y, Z> {
        /**
         * 执行方法
         * @param t 参数1
         * @param u 参数2
         * @param v 参数3
         * @param w 参数4
         * @param x 参数5
         * @param y 参数6
         * @param z 参数7
         */
        void invoke(T t, U u, V v, W w, X x, Y y, Z z);
    }

    /**
     * 无接收参数，有返回值的函数引用或代码块的接口
     * @param <R> 返回值类型
     */
    @FunctionalInterface
    public interface Object0<R> {
        /**
         * 执行方法
         * @return 返回值
         */
        R invoke();
    }

    /**
     * 接收1个参数，有返回值的函数引用或代码块的接口
     * @param <T> 参数类型
     * @param <R> 返回值类型
     */
    @FunctionalInterface
    public interface Object1<T, R> {
        /**
         * 执行方法
         * @param t 参数
         * @return 返回值
         */
        R invoke(T t);
    }

    /**
     * 接收2个参数，有返回值的函数引用或代码块的接口
     * @param <T> 参数类型1
     * @param <U> 参数类型2
     * @param <R> 返回值类型
     */
    @FunctionalInterface
    public interface Object2<T, U, R> {
        /**
         * 执行方法
         * @param t 参数1
         * @param u 参数2
         * @return 返回值
         */
        R invoke(T t, U u);
    }

    /**
     * 接收3个参数，有返回值的函数引用或代码块的接口
     * @param <T> 参数类型1
     * @param <U> 参数类型2
     * @param <V> 参数类型3
     * @param <R> 返回值类型
     */
    @FunctionalInterface
    public interface Object3<T, U, V, R> {
        /**
         * 执行方法
         * @param t 参数1
         * @param u 参数2
         * @param v 参数3
         * @return 返回值
         */
        R invoke(T t, U u, V v);
    }

    /**
     * 接收4个参数，有返回值的函数引用或代码块的接口
     * @param <T> 参数类型1
     * @param <U> 参数类型2
     * @param <V> 参数类型3
     * @param <W> 参数类型4
     * @param <R> 返回值类型
     */
    @FunctionalInterface
    public interface Object4<T, U, V, W, R> {
        /**
         * 执行方法
         * @param t 参数1
         * @param u 参数2
         * @param v 参数3
         * @param w 参数4
         * @return 返回值
         */
        R invoke(T t, U u, V v, W w);
    }

    /**
     * 接收5个参数，有返回值的函数引用或代码块的接口
     * @param <T> 参数类型1
     * @param <U> 参数类型2
     * @param <V> 参数类型3
     * @param <W> 参数类型4
     * @param <X> 参数类型5
     * @param <R> 返回值类型
     */
    @FunctionalInterface
    public interface Object5<T, U, V, W, X, R> {
        /**
         * 执行方法
         * @param t 参数1
         * @param u 参数2
         * @param v 参数3
         * @param w 参数4
         * @param x 参数5
         * @return 返回值
         */
        R invoke(T t, U u, V v, W w, X x);
    }

    /**
     * 接收6个参数，有返回值的函数引用或代码块的接口
     * @param <T> 参数类型1
     * @param <U> 参数类型2
     * @param <V> 参数类型3
     * @param <W> 参数类型4
     * @param <X> 参数类型5
     * @param <Y> 参数类型6
     * @param <R> 返回值类型
     */
    @FunctionalInterface
    public interface Object6<T, U, V, W, X, Y, R> {
        /**
         * 执行方法
         * @param t 参数1
         * @param u 参数2
         * @param v 参数3
         * @param w 参数4
         * @param x 参数5
         * @param y 参数6
         * @return 返回值
         */
        R invoke(T t, U u, V v, W w, X x, Y y);
    }

    /**
     * 接收7个参数，有返回值的函数引用或代码块的接口
     * @param <T> 参数类型1
     * @param <U> 参数类型2
     * @param <V> 参数类型3
     * @param <W> 参数类型4
     * @param <X> 参数类型5
     * @param <Y> 参数类型6
     * @param <Z> 参数类型7
     * @param <R> 返回值类型
     */
    @FunctionalInterface
    public interface Object7<T, U, V, W, X, Y, Z, R> {
        /**
         * 执行方法
         * @param t 参数1
         * @param u 参数2
         * @param v 参数3
         * @param w 参数4
         * @param x 参数5
         * @param y 参数6
         * @param z 参数7
         * @return 返回值
         */
        R invoke(T t, U u, V v, W w, X x, Y y, Z z);
    }

    /**
     * 接收1个数字参数，返回数字的函数引用或代码块的接口
     * @param <T> 参数类型和返回值类型
     * @since 1.4
     */
    @FunctionalInterface
    public interface Number1<T extends Number> {
        /**
         * 执行方法
         * @param t 参数
         * @return 返回值
         */
        T invoke(T t);
    }

    /**
     * 接收2个数字参数，返回数字的函数引用或代码块的接口
     * @param <T> 参数类型和返回值类型
     * @since 1.4
     */
    @FunctionalInterface
    public interface Number2<T extends Number> {
        /**
         * 执行方法
         * @param t1 参数1
         * @param t2 参数2
         * @return 返回值
         */
        T invoke(T t1, T t2);
    }

    /**
     * 接收3个数字参数，返回数字的函数引用或代码块的接口
     * @param <T> 参数类型和返回值类型
     * @since 1.4
     */
    @FunctionalInterface
    public interface Number3<T extends Number> {
        /**
         * 执行方法
         * @param t1 参数1
         * @param t2 参数2
         * @param t3 参数3
         * @return 返回值
         */
        T invoke(T t1, T t2, T t3);
    }

    /**
     * 接收4个数字参数，返回数字的函数引用或代码块的接口
     * @param <T> 参数类型和返回值类型
     * @since 1.4
     */
    @FunctionalInterface
    public interface Number4<T extends Number> {
        /**
         * 执行方法
         * @param t1 参数1
         * @param t2 参数2
         * @param t3 参数3
         * @param t4 参数4
         * @return 返回值
         */
        T invoke(T t1, T t2, T t3, T t4);
    }

    /**
     * 接收5个数字参数，返回数字的函数引用或代码块的接口
     * @param <T> 参数类型和返回值类型
     * @since 1.4
     */
    @FunctionalInterface
    public interface Number5<T extends Number> {
        /**
         * 执行方法
         * @param t1 参数1
         * @param t2 参数2
         * @param t3 参数3
         * @param t4 参数4
         * @param t5 参数5
         * @return 返回值
         */
        T invoke(T t1, T t2, T t3, T t4, T t5);
    }

    /**
     * 接收6个数字参数，返回数字的函数引用或代码块的接口
     * @param <T> 参数类型和返回值类型
     * @since 1.4
     */
    @FunctionalInterface
    public interface Number6<T extends Number> {
        /**
         * 执行方法
         * @param t1 参数1
         * @param t2 参数2
         * @param t3 参数3
         * @param t4 参数4
         * @param t5 参数5
         * @param t6 参数6
         * @return 返回值
         */
        T invoke(T t1, T t2, T t3, T t4, T t5, T t6);
    }

    /**
     * 接收7个数字参数，返回数字的函数引用或代码块的接口
     * @param <T> 参数类型和返回值类型
     * @since 1.4
     */
    @FunctionalInterface
    public interface Number7<T extends Number> {
        /**
         * 执行方法
         * @param t1 参数1
         * @param t2 参数2
         * @param t3 参数3
         * @param t4 参数4
         * @param t5 参数5
         * @param t6 参数6
         * @param t7 参数7
         * @return 返回值
         */
        T invoke(T t1, T t2, T t3, T t4, T t5, T t6, T t7);
    }
}

