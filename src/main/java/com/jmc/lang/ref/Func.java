package com.jmc.lang.ref;

/**
 * 函数引用 <br><br>
 * 可将方法作为参数传入另一个方法以便调用，且支持直接传入lambda <br>
 * （参数均必须小于等于7个，支持传入基本数据类型）<br>
 * 提供了of和bind方法
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
 *     // 绑定一个方法和参数作为函数指针
 *     var bindFunc = Func.partial(this::add, 3, 4);
 *     // 执行这个函数指针并指定返回值（7）
 *     int res2 = bindFunc.invoke();
 *
 *     // 绑定一个lambda作为函数指针
 *     var lambdaFunc = Func.of((String a, String b) -> a + b);
 *     // 执行这个函数指针并指定返回值（"12"）
 *     String res3 = lambdaFunc.invoke("1", "2");
 *
 *     // 绑定一个纯基本数据类型的lambda作为函数指针（需要补充泛型）
 *     var numberLambdaFunc = Func.<Long>of((a, b) -> a - b);
 *     // 执行这个函数指针并获取返回值（3）
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
     * 抽象的执行方法
     * @param args 参数
     * @return 返回值
     */
    public abstract R invoke(Object... args);

    /**
     * 检查参数个数是否匹配
     * @param realSize 实际参数个数
     * @param assertSize 需要参数个数
     */
    private static void checkParams(int realSize, int assertSize) {
        if (realSize != assertSize) {
            throw new IllegalArgumentException("调用参数个数不匹配，需要%d个，实际提供了%d个".formatted(assertSize, realSize));
        }
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
                checkParams(args.length, 0);
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
                checkParams(args.length, 1);
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
                checkParams(args.length, 2);
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
                checkParams(args.length, 3);
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
                checkParams(args.length, 4);
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
                checkParams(args.length, 5);
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
                checkParams(args.length, 6);
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
                checkParams(args.length, 7);
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
                checkParams(args.length, 0);
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
                checkParams(args.length, 1);
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
                checkParams(args.length, 2);
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
                checkParams(args.length, 3);
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
                checkParams(args.length, 4);
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
                checkParams(args.length, 5);
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
                checkParams(args.length, 6);
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
                checkParams(args.length, 7);
                return m.invoke((T) args[0], (U) args[1], (V) args[2], (W) args[3], (X) args[4], (Y) args[5], (Z) args[6]);
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
                checkParams(args.length, 1);
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
                checkParams(args.length, 2);
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
                checkParams(args.length, 3);
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
                checkParams(args.length, 4);
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
                checkParams(args.length, 5);
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
                checkParams(args.length, 6);
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
                checkParams(args.length, 7);
                return m.invoke((T) args[0], (T) args[1], (T) args[2], (T) args[3], (T) args[4], (T) args[5], (T) args[6]);
            }
        };
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块
     * @param t 参数
     * @param <T> 参数类型
     * @return 函数引用实例
     */
    public static <T> Func<Void> partial(Void1<T> m, T t) {
        return new Func<>() {
            @Override
            public Void invoke(Object... args) {
                checkParams(args.length, 0);
                m.invoke(t);
                return null;
            }
        };
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块
     * @param t 参数1
     * @param u 参数2
     * @param <T> 参数1类型
     * @param <U> 参数2类型
     * @return 函数引用实例
     */
    public static <T, U> Func<Void> partial(Void2<T, U> m, T t, U u) {
        return new Func<>() {
            @Override
            public Void invoke(Object... args) {
                checkParams(args.length, 0);
                m.invoke(t, u);
                return null;
            }
        };
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块
     * @param t 参数1
     * @param u 参数2
     * @param v 参数3
     * @param <T> 参数1类型
     * @param <U> 参数2类型
     * @param <V> 参数3类型
     * @return 函数引用实例
     */
    public static <T, U, V> Func<Void> partial(Void3<T, U, V> m, T t, U u, V v) {
        return new Func<>() {
            @Override
            public Void invoke(Object... args) {
                checkParams(args.length, 0);
                m.invoke(t, u, v);
                return null;
            }
        };
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块
     * @param t 参数1
     * @param u 参数2
     * @param v 参数3
     * @param w 参数4
     * @param <T> 参数1类型
     * @param <U> 参数2类型
     * @param <V> 参数3类型
     * @param <W> 参数4类型
     * @return 函数引用实例
     */
    public static <T, U, V, W> Func<Void> partial(Void4<T, U, V, W> m, T t, U u, V v, W w) {
        return new Func<>() {
            @Override
            public Void invoke(Object... args) {
                checkParams(args.length, 0);
                m.invoke(t, u, v, w);
                return null;
            }
        };
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块
     * @param t 参数1
     * @param u 参数2
     * @param v 参数3
     * @param w 参数4
     * @param x 参数5
     * @param <T> 参数1类型
     * @param <U> 参数2类型
     * @param <V> 参数3类型
     * @param <W> 参数4类型
     * @param <X> 参数5类型
     * @return 函数引用实例
     */
    public static <T, U, V, W, X> Func<Void> partial(Void5<T, U, V, W, X> m, T t, U u, V v, W w, X x) {
        return new Func<>() {
            @Override
            public Void invoke(Object... args) {
                checkParams(args.length, 0);
                m.invoke(t, u, v, w, x);
                return null;
            }
        };
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块
     * @param t 参数1
     * @param u 参数2
     * @param v 参数3
     * @param w 参数4
     * @param x 参数5
     * @param y 参数6
     * @param <T> 参数1类型
     * @param <U> 参数2类型
     * @param <V> 参数3类型
     * @param <W> 参数4类型
     * @param <X> 参数5类型
     * @param <Y> 参数6类型
     * @return 函数引用实例
     */
    public static <T, U, V, W, X, Y> Func<Void> partial(Void6<T, U, V, W, X, Y> m, T t, U u, V v, W w, X x, Y y) {
        return new Func<>() {
            @Override
            public Void invoke(Object... args) {
                checkParams(args.length, 0);
                m.invoke(t, u, v, w, x, y);
                return null;
            }
        };
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块
     * @param t 参数1
     * @param u 参数2
     * @param v 参数3
     * @param w 参数4
     * @param x 参数5
     * @param y 参数6
     * @param z 参数7
     * @param <T> 参数1类型
     * @param <U> 参数2类型
     * @param <V> 参数3类型
     * @param <W> 参数4类型
     * @param <X> 参数5类型
     * @param <Y> 参数6类型
     * @param <Z> 参数7类型
     * @return 函数引用实例
     */
    public static <T, U, V, W, X, Y, Z> Func<Void> partial(Void7<T, U, V, W, X, Y, Z> m, T t, U u, V v, W w, X x, Y y, Z z) {
        return new Func<>() {
            @Override
            public Void invoke(Object... args) {
                checkParams(args.length, 0);
                m.invoke(t, u, v, w, x, y, z);
                return null;
            }
        };
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块
     * @param t 参数
     * @param <T> 参数类型
     * @param <R> 返回值类型
     * @return 函数引用实例
     */
    public static <T, R> Func<R> partial(Object1<T, R> m, T t) {
        return new Func<>() {
            @Override
            public R invoke(Object... args) {
                checkParams(args.length, 0);
                return m.invoke(t);
            }
        };
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块
     * @param t 参数1
     * @param u 参数2
     * @param <T> 参数1类型
     * @param <U> 参数2类型
     * @param <R> 返回值类型
     * @return 函数引用实例
     */
    public static <T, U, R> Func<R> partial(Object2<T, U, R> m, T t, U u) {
        return new Func<>() {
            @Override
            public R invoke(Object... args) {
                checkParams(args.length, 0);
                return m.invoke(t, u);
            }
        };
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块
     * @param t 参数1
     * @param u 参数2
     * @param v 参数3
     * @param <T> 参数1类型
     * @param <U> 参数2类型
     * @param <V> 参数3类型
     * @param <R> 返回值类型
     * @return 函数引用实例
     */
    public static <T, U, V, R> Func<R> partial(Object3<T, U, V, R> m, T t, U u, V v) {
        return new Func<>() {
            @Override
            public R invoke(Object... args) {
                checkParams(args.length, 0);
                return m.invoke(t, u, v);
            }
        };
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块
     * @param t 参数1
     * @param u 参数2
     * @param v 参数3
     * @param w 参数4
     * @param <T> 参数1类型
     * @param <U> 参数2类型
     * @param <V> 参数3类型
     * @param <W> 参数4类型
     * @param <R> 返回值类型
     * @return 函数引用实例
     */
    public static <T, U, V, W, R> Func<R> partial(Object4<T, U, V, W, R> m, T t, U u, V v, W w) {
        return new Func<>() {
            @Override
            public R invoke(Object... args) {
                checkParams(args.length, 0);
                return m.invoke(t, u, v, w);
            }
        };
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块
     * @param t 参数1
     * @param u 参数2
     * @param v 参数3
     * @param w 参数4
     * @param x 参数5
     * @param <T> 参数1类型
     * @param <U> 参数2类型
     * @param <V> 参数3类型
     * @param <W> 参数4类型
     * @param <X> 参数5类型
     * @param <R> 返回值类型
     * @return 函数引用实例
     */
    public static <T, U, V, W, X, R> Func<R> partial(Object5<T, U, V, W, X, R> m, T t, U u, V v, W w, X x) {
        return new Func<>() {
            @Override
            public R invoke(Object... args) {
                checkParams(args.length, 0);
                return m.invoke(t, u, v, w, x);
            }
        };
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块
     * @param t 参数1
     * @param u 参数2
     * @param v 参数3
     * @param w 参数4
     * @param x 参数5
     * @param y 参数6
     * @param <T> 参数1类型
     * @param <U> 参数2类型
     * @param <V> 参数3类型
     * @param <W> 参数4类型
     * @param <X> 参数5类型
     * @param <Y> 参数6类型
     * @param <R> 返回值类型
     * @return 函数引用实例
     */
    public static <T, U, V, W, X, Y, R> Func<R> partial(Object6<T, U, V, W, X, Y, R> m, T t, U u, V v, W w, X x, Y y) {
        return new Func<>() {
            @Override
            public R invoke(Object... args) {
                checkParams(args.length, 0);
                return m.invoke(t, u, v, w, x, y);
            }
        };
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块
     * @param t 参数1
     * @param u 参数2
     * @param v 参数3
     * @param w 参数4
     * @param x 参数5
     * @param y 参数6
     * @param z 参数7
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
    public static <T, U, V, W, X, Y, Z, R> Func<R> partial(Object7<T, U, V, W, X, Y, Z, R> m, T t, U u, V v, W w, X x, Y y, Z z) {
        return new Func<>() {
            @Override
            public R invoke(Object... args) {
                checkParams(args.length, 0);
                return m.invoke(t, u, v, w, x, y, z);
            }
        };
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块（含数字参数的）
     * @param t 数字参数
     * @param <T> 数字类型
     * @return 函数引用实例
     * @since 1.4
     */
    public static <T extends Number> Func<T> partial(Number1<T> m, T t) {
        return new Func<>() {
            @Override
            public T invoke(Object... args) {
                checkParams(args.length, 0);
                return m.invoke(t);
            }
        };
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块（含数字参数的）
     * @param t1 数字参数1
     * @param t2 数字参数2
     * @param <T> 数字类型
     * @return 函数引用实例
     * @since 1.4
     */
    public static <T extends Number> Func<T> partial(Number2<T> m, T t1, T t2) {
        return new Func<>() {
            @Override
            public T invoke(Object... args) {
                checkParams(args.length, 0);
                return m.invoke(t1, t2);
            }
        };
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块（含数字参数的）
     * @param t1 数字参数1
     * @param t2 数字参数2
     * @param t3 数字参数3
     * @param <T> 数字类型
     * @return 函数引用实例
     * @since 1.4
     */
    public static <T extends Number> Func<T> partial(Number3<T> m, T t1, T t2, T t3) {
        return new Func<>() {
            @Override
            public T invoke(Object... args) {
                checkParams(args.length, 0);
                return m.invoke(t1, t2, t3);
            }
        };
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块（含数字参数的）
     * @param t1 数字参数1
     * @param t2 数字参数2
     * @param t3 数字参数3
     * @param t4 数字参数4
     * @param <T> 数字类型
     * @return 函数引用实例
     * @since 1.4
     */
    public static <T extends Number> Func<T> partial(Number4<T> m, T t1, T t2, T t3, T t4) {
        return new Func<>() {
            @Override
            public T invoke(Object... args) {
                checkParams(args.length, 0);
                return m.invoke(t1, t2, t3, t4);
            }
        };
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块（含数字参数的）
     * @param t1 数字参数1
     * @param t2 数字参数2
     * @param t3 数字参数3
     * @param t4 数字参数4
     * @param t5 数字参数5
     * @param <T> 数字类型
     * @return 函数引用实例
     * @since 1.4
     */
    public static <T extends Number> Func<T> partial(Number5<T> m, T t1, T t2, T t3, T t4, T t5) {
        return new Func<>() {
            @Override
            public T invoke(Object... args) {
                checkParams(args.length, 0);
                return m.invoke(t1, t2, t3, t4, t5);
            }
        };
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块（含数字参数的）
     * @param t1 数字参数1
     * @param t2 数字参数2
     * @param t3 数字参数3
     * @param t4 数字参数4
     * @param t5 数字参数5
     * @param t6 数字参数6
     * @param <T> 数字类型
     * @return 函数引用实例
     * @since 1.4
     */
    public static <T extends Number> Func<T> partial(Number6<T> m, T t1, T t2, T t3, T t4, T t5, T t6) {
        return new Func<>() {
            @Override
            public T invoke(Object... args) {
                checkParams(args.length, 0);
                return m.invoke(t1, t2, t3, t4, t5, t6);
            }
        };
    }

    /**
     * 返回一个绑定部分或者所有参数的函数引用实例
     * @param m 方法引用或代码块（含数字参数的）
     * @param t1 数字参数1
     * @param t2 数字参数2
     * @param t3 数字参数3
     * @param t4 数字参数4
     * @param t5 数字参数5
     * @param t6 数字参数6
     * @param t7 数字参数7
     * @param <T> 数字类型
     * @return 函数引用实例
     * @since 1.4
     */
    public static <T extends Number> Func<T> partial(Number7<T> m, T t1, T t2, T t3, T t4, T t5, T t6, T t7) {
        return new Func<>() {
            @Override
            public T invoke(Object... args) {
                checkParams(args.length, 0);
                return m.invoke(t1, t2, t3, t4, t5, t6, t7);
            }
        };
    }

    /**
     * 无接收参数，无返回值的函数引用或代码块的接口
     */
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

