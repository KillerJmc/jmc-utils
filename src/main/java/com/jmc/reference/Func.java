package com.jmc.reference;

/**
 * 函数引用 <br><br>
 * 可将方法作为参数传入另一个方法以便调用，且支持直接传入lambda <br>
 * （参数均必须小于等于7个，支持传入基本数据类型）<br>
 * 提供了of和bind方法
 * @since 1.0
 * @author Jmc
 * @param <R> 返回值类型
 */
@SuppressWarnings({"unchecked", "unused"})
public abstract class Func<R> {
    private Func() {
        // 工具类不能被实例化
    }

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
        if (realSize != assertSize)
            throw new IllegalArgumentException("调用参数个数不匹配，需要%d个，实际提供了%d个".formatted(assertSize, realSize));
    }

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

    public static <R> Func<R> of(Object0<R> m) {
        return new Func<>() {
            @Override
            public R invoke(Object... args) {
                checkParams(args.length, 0);
                return m.invoke();
            }
        };
    }

    public static <T, R> Func<R> of(Object1<T, R> m) {
        return new Func<>() {
            @Override
            public R invoke(Object... args) {
                checkParams(args.length, 1);
                return m.invoke((T) args[0]);
            }
        };
    }

    public static <T, U, R> Func<R> of(Object2<T, U, R> m) {
        return new Func<>() {
            @Override
            public R invoke(Object... args) {
                checkParams(args.length, 2);
                return m.invoke((T) args[0], (U) args[1]);
            }
        };
    }

    public static <T, U, V, R> Func<R> of(Object3<T, U, V, R> m) {
        return new Func<>() {
            @Override
            public R invoke(Object... args) {
                checkParams(args.length, 3);
                return m.invoke((T) args[0], (U) args[1], (V) args[2]);
            }
        };
    }

    public static <T, U, V, W, R> Func<R> of(Object4<T, U, V, W, R> m) {
        return new Func<>() {
            @Override
            public R invoke(Object... args) {
                checkParams(args.length, 4);
                return m.invoke((T) args[0], (U) args[1], (V) args[2], (W) args[3]);
            }
        };
    }

    public static <T, U, V, W, X, R> Func<R> of(Object5<T, U, V, W, X, R> m) {
        return new Func<>() {
            @Override
            public R invoke(Object... args) {
                checkParams(args.length, 5);
                return m.invoke((T) args[0], (U) args[1], (V) args[2], (W) args[3], (X) args[4]);
            }
        };
    }

    public static <T, U, V, W, X, Y, R> Func<R> of(Object6<T, U, V, W, X, Y, R> m) {
        return new Func<>() {
            @Override
            public R invoke(Object... args) {
                checkParams(args.length, 6);
                return m.invoke((T) args[0], (U) args[1], (V) args[2], (W) args[3], (X) args[4], (Y) args[5]);
            }
        };
    }

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
     * @since 1.4
     */
    public static <T> Func<T> of(Number1<T> m) {
        return new Func<>() {
            @Override
            public T invoke(Object... args) {
                checkParams(args.length, 1);
                return m.invoke((T) args[0]);
            }
        };
    }

    /**
     * @since 1.4
     */
    public static <T> Func<T> of(Number2<T> m) {
        return new Func<>() {
            @Override
            public T invoke(Object... args) {
                checkParams(args.length, 2);
                return m.invoke((T) args[0], (T) args[1]);
            }
        };
    }

    /**
     * @since 1.4
     */
    public static <T> Func<T> of(Number3<T> m) {
        return new Func<>() {
            @Override
            public T invoke(Object... args) {
                checkParams(args.length, 3);
                return m.invoke((T) args[0], (T) args[1], (T) args[2]);
            }
        };
    }

    /**
     * @since 1.4
     */
    public static <T> Func<T> of(Number4<T> m) {
        return new Func<>() {
            @Override
            public T invoke(Object... args) {
                checkParams(args.length, 4);
                return m.invoke((T) args[0], (T) args[1], (T) args[2], (T) args[3]);
            }
        };
    }

    /**
     * @since 1.4
     */
    public static <T> Func<T> of(Number5<T> m) {
        return new Func<>() {
            @Override
            public T invoke(Object... args) {
                checkParams(args.length, 5);
                return m.invoke((T) args[0], (T) args[1], (T) args[2], (T) args[3], (T) args[4]);
            }
        };
    }

    /**
     * @since 1.4
     */
    public static <T> Func<T> of(Number6<T> m) {
        return new Func<>() {
            @Override
            public T invoke(Object... args) {
                checkParams(args.length, 6);
                return m.invoke((T) args[0], (T) args[1], (T) args[2], (T) args[3], (T) args[4], (T) args[5]);
            }
        };
    }

    /**
     * @since 1.4
     */
    public static <T> Func<T> of(Number7<T> m) {
        return new Func<>() {
            @Override
            public T invoke(Object... args) {
                checkParams(args.length, 7);
                return m.invoke((T) args[0], (T) args[1], (T) args[2], (T) args[3], (T) args[4], (T) args[5], (T) args[6]);
            }
        };
    }

    public static Func<Void> bind(Void0 m) {
        return new Func<>() {
            @Override
            public Void invoke(Object... args) {
                checkParams(args.length, 0);
                m.invoke();
                return null;
            }
        };
    }

    public static <T> Func<Void> bind(Void1<T> m, T t) {
        return new Func<>() {
            @Override
            public Void invoke(Object... args) {
                checkParams(args.length, 0);
                m.invoke(t);
                return null;
            }
        };
    }

    public static <T, U> Func<Void> bind(Void2<T, U> m, T t, U u) {
        return new Func<>() {
            @Override
            public Void invoke(Object... args) {
                checkParams(args.length, 0);
                m.invoke(t, u);
                return null;
            }
        };
    }

    public static <T, U, V> Func<Void> bind(Void3<T, U, V> m, T t, U u, V v) {
        return new Func<>() {
            @Override
            public Void invoke(Object... args) {
                checkParams(args.length, 0);
                m.invoke(t, u, v);
                return null;
            }
        };
    }

    public static <T, U, V, W> Func<Void> bind(Void4<T, U, V, W> m, T t, U u, V v, W w) {
        return new Func<>() {
            @Override
            public Void invoke(Object... args) {
                checkParams(args.length, 0);
                m.invoke(t, u, v, w);
                return null;
            }
        };
    }

    public static <T, U, V, W, X> Func<Void> bind(Void5<T, U, V, W, X> m, T t, U u, V v, W w, X x) {
        return new Func<>() {
            @Override
            public Void invoke(Object... args) {
                checkParams(args.length, 0);
                m.invoke(t, u, v, w, x);
                return null;
            }
        };
    }

    public static <T, U, V, W, X, Y> Func<Void> bind(Void6<T, U, V, W, X, Y> m, T t, U u, V v, W w, X x, Y y) {
        return new Func<>() {
            @Override
            public Void invoke(Object... args) {
                checkParams(args.length, 0);
                m.invoke(t, u, v, w, x, y);
                return null;
            }
        };
    }

    public static <T, U, V, W, X, Y, Z> Func<Void> bind(Void7<T, U, V, W, X, Y, Z> m, T t, U u, V v, W w, X x, Y y, Z z) {
        return new Func<>() {
            @Override
            public Void invoke(Object... args) {
                checkParams(args.length, 0);
                m.invoke(t, u, v, w, x, y, z);
                return null;
            }
        };
    }

    public static <R> Func<R> bind(Object0<R> m) {
        return new Func<>() {
            @Override
            public R invoke(Object... args) {
                checkParams(args.length, 0);
                return m.invoke();
            }
        };
    }

    public static <T, R> Func<R> bind(Object1<T, R> m, T t) {
        return new Func<>() {
            @Override
            public R invoke(Object... args) {
                checkParams(args.length, 0);
                return m.invoke(t);
            }
        };
    }

    public static <T, U, R> Func<R> bind(Object2<T, U, R> m, T t, U u) {
        return new Func<>() {
            @Override
            public R invoke(Object... args) {
                checkParams(args.length, 0);
                return m.invoke(t, u);
            }
        };
    }

    public static <T, U, V, R> Func<R> bind(Object3<T, U, V, R> m, T t, U u, V v) {
        return new Func<>() {
            @Override
            public R invoke(Object... args) {
                checkParams(args.length, 0);
                return m.invoke(t, u, v);
            }
        };
    }

    public static <T, U, V, W, R> Func<R> bind(Object4<T, U, V, W, R> m, T t, U u, V v, W w) {
        return new Func<>() {
            @Override
            public R invoke(Object... args) {
                checkParams(args.length, 0);
                return m.invoke(t, u, v, w);
            }
        };
    }

    public static <T, U, V, W, X, R> Func<R> bind(Object5<T, U, V, W, X, R> m, T t, U u, V v, W w, X x) {
        return new Func<>() {
            @Override
            public R invoke(Object... args) {
                checkParams(args.length, 0);
                return m.invoke(t, u, v, w, x);
            }
        };
    }

    public static <T, U, V, W, X, Y, R> Func<R> bind(Object6<T, U, V, W, X, Y, R> m, T t, U u, V v, W w, X x, Y y) {
        return new Func<>() {
            @Override
            public R invoke(Object... args) {
                checkParams(args.length, 0);
                return m.invoke(t, u, v, w, x, y);
            }
        };
    }

    public static <T, U, V, W, X, Y, Z, R> Func<R> bind(Object7<T, U, V, W, X, Y, Z, R> m, T t, U u, V v, W w, X x, Y y, Z z) {
        return new Func<>() {
            @Override
            public R invoke(Object... args) {
                checkParams(args.length, 0);
                return m.invoke(t, u, v, w, x, y, z);
            }
        };
    }

    /**
     * @since 1.4
     */
    public static <T> Func<T> bind(Number0<T> m) {
        return new Func<>() {
            @Override
            public T invoke(Object... args) {
                checkParams(args.length, 0);
                return m.invoke();
            }
        };
    }

    /**
     * @since 1.4
     */
    public static <T> Func<T> bind(Number1<T> m, T t) {
        return new Func<>() {
            @Override
            public T invoke(Object... args) {
                checkParams(args.length, 1);
                return m.invoke(t);
            }
        };
    }

    /**
     * @since 1.4
     */
    public static <T> Func<T> bind(Number2<T> m, T t1, T t2) {
        return new Func<>() {
            @Override
            public T invoke(Object... args) {
                checkParams(args.length, 2);
                return m.invoke(t1, t2);
            }
        };
    }

    /**
     * @since 1.4
     */
    public static <T> Func<T> bind(Number3<T> m, T t1, T t2, T t3) {
        return new Func<>() {
            @Override
            public T invoke(Object... args) {
                checkParams(args.length, 3);
                return m.invoke(t1, t2, t3);
            }
        };
    }

    /**
     * @since 1.4
     */
    public static <T> Func<T> bind(Number4<T> m, T t1, T t2, T t3, T t4) {
        return new Func<>() {
            @Override
            public T invoke(Object... args) {
                checkParams(args.length, 4);
                return m.invoke(t1, t2, t3, t4);
            }
        };
    }

    /**
     * @since 1.4
     */
    public static <T> Func<T> bind(Number5<T> m, T t1, T t2, T t3, T t4, T t5) {
        return new Func<>() {
            @Override
            public T invoke(Object... args) {
                checkParams(args.length, 5);
                return m.invoke(t1, t2, t3, t4, t5);
            }
        };
    }

    /**
     * @since 1.4
     */
    public static <T> Func<T> bind(Number6<T> m, T t1, T t2, T t3, T t4, T t5, T t6) {
        return new Func<>() {
            @Override
            public T invoke(Object... args) {
                checkParams(args.length, 6);
                return m.invoke(t1, t2, t3, t4, t5, t6);
            }
        };
    }

    /**
     * @since 1.4
     */
    public static <T> Func<T> bind(Number7<T> m, T t1, T t2, T t3, T t4, T t5, T t6, T t7) {
        return new Func<>() {
            @Override
            public T invoke(Object... args) {
                checkParams(args.length, 7);
                return m.invoke(t1, t2, t3, t4, t5, t6, t7);
            }
        };
    }

    public interface Void0 {
        void invoke();
    }

    public interface Void1<T> {
        void invoke(T a);
    }

    public interface Void2<T, U> {
        void invoke(T t, U u);
    }

    public interface Void3<T, U, V> {
        void invoke(T t, U u, V v);
    }

    public interface Void4<T, U, V, W> {
        void invoke(T t, U u, V v, W w);
    }

    public interface Void5<T, U, V, W, X> {
        void invoke(T t, U u, V v, W w, X x);
    }

    public interface Void6<T, U, V, W, X, Y> {
        void invoke(T t, U u, V v, W w, X x, Y y);
    }

    public interface Void7<T, U, V, W, X, Y, Z> {
        void invoke(T t, U u, V v, W w, X x, Y y, Z z);
    }

    public interface Object0<R> {
        R invoke();
    }

    public interface Object1<T, R> {
        R invoke(T t);
    }

    public interface Object2<T, U, R> {
        R invoke(T t, U u);
    }

    public interface Object3<T, U, V, R> {
        R invoke(T t, U u, V v);
    }

    public interface Object4<T, U, V, W, R> {
        R invoke(T t, U u, V v, W w);
    }

    public interface Object5<T, U, V, W, X, R> {
        R invoke(T t, U u, V v, W w, X x);
    }

    public interface Object6<T, U, V, W, X, Y, R> {
        R invoke(T t, U u, V v, W w, X x, Y y);
    }

    public interface Object7<T, U, V, W, X, Y, Z, R> {
        R invoke(T t, U u, V v, W w, X x, Y y, Z z);
    }

    /**
     * @since 1.4
     */
    public interface Number0<T> {
        T invoke();
    }

    /**
     * @since 1.4
     */
    public interface Number1<T> {
        T invoke(T t);
    }

    /**
     * @since 1.4
     */
    public interface Number2<T> {
        T invoke(T t1, T t2);
    }

    /**
     * @since 1.4
     */
    public interface Number3<T> {
        T invoke(T t1, T t2, T t3);
    }

    /**
     * @since 1.4
     */
    public interface Number4<T> {
        T invoke(T t1, T t2, T t3, T t4);
    }

    /**
     * @since 1.4
     */
    public interface Number5<T> {
        T invoke(T t1, T t2, T t3, T t4, T t5);
    }

    /**
     * @since 1.4
     */
    public interface Number6<T> {
        T invoke(T t1, T t2, T t3, T t4, T t5, T t6);
    }

    /**
     * @since 1.4
     */
    public interface Number7<T> {
        T invoke(T t1, T t2, T t3, T t4, T t5, T t6, T t7);
    }
}

