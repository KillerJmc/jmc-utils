package com.jmc.lang;

import java.io.PrintStream;
import java.util.Locale;

/**
 * Java内置函数
 * @apiNote <pre>{@code
 * import static com.jmc.lang.Builtins.*;
 *
 * // In JDK 21
 * void main() {
 *     print(666);
 *     println();
 *     printf("%d + %d = %d\n", 3, 4, 3 + 4);
 *
 *     println(sqrt(9));
 *     println(pow(2, 5));
 *     println(round(3.4f));
 *     println(round(4.7d));
 *     println(abs(-3));
 *     println(max(3, 4));
 *     println(min(-1, 2));
 * }
 * }</pre>
 * @since 3.8
 * @author Jmc
 */
@SuppressWarnings("unused")
public class BuiltIns {
    /**
     * 私有构造器
     */
    private BuiltIns() {}

    // region print

    /**
     * @see PrintStream#print(boolean)
     */
    public static void print(boolean b) {
        System.out.print(b);
    }

    /**
     * @see PrintStream#print(char)
     */
    public static void print(char c) {
        System.out.print(c);
    }

    /**
     * @see PrintStream#print(int)
     */
    public static void print(int i) {
        System.out.print(i);
    }

    /**
     * @see PrintStream#print(long)
     */
    public static void print(long l) {
        System.out.print(l);
    }

    /**
     * @see PrintStream#print(float)
     */
    public static void print(float f) {
        System.out.print(f);
    }

    /**
     * @see PrintStream#print(double)
     */
    public static void print(double d) {
        System.out.print(d);
    }

    /**
     * @see PrintStream#print(char[])
     */
    public static void print(char[] s) {
        System.out.print(s);
    }

    /**
     * @see PrintStream#print(String)
     */
    public static void print(String s) {
        System.out.print(s);
    }

    /**
     * @see PrintStream#print(Object)
     */
    public static void print(Object obj) {
        System.out.print(obj);
    }

    /**
     * @see PrintStream#println()
     */
    public static void println() {
        System.out.println();
    }

    /**
     * @see PrintStream#println(boolean)
     */
    public static void println(boolean x) {
        System.out.println(x);
    }

    /**
     * @see PrintStream#println(char)
     */
    public static void println(char x) {
        System.out.println(x);
    }

    /**
     * @see PrintStream#println(int)
     */
    public static void println(int x) {
        System.out.println(x);
    }

    /**
     * @see PrintStream#println(long)
     */
    public static void println(long x) {
        System.out.println(x);
    }

    /**
     * @see PrintStream#println(float)
     */
    public static void println(float x) {
        System.out.println(x);
    }

    /**
     * @see PrintStream#println(double)
     */
    public static void println(double x) {
        System.out.println(x);
    }

    /**
     * @see PrintStream#println(char[])
     */
    public static void println(char[] x) {
        System.out.println(x);
    }

    /**
     * @see PrintStream#println(String)
     */
    public static void println(String x) {
        System.out.println(x);
    }

    /**
     * @see PrintStream#println(Object)
     */
    public static void println(Object x) {
        System.out.println(x);
    }

    /**
     * @see PrintStream#printf(String, Object...)
     */
    public static PrintStream printf(String format, Object ... args) {
        return System.out.printf(format, args);
    }

    /**
     * @see PrintStream#printf(Locale, String, Object...)
     */
    public static PrintStream printf(Locale l, String format, Object ... args) {
        return System.out.printf(l, format, args);
    }

    // endregion

    // region math

    /**
     * @see Math#sqrt(double)
     */
    public static double sqrt(double a) {
        return Math.sqrt(a);
    }

    /**
     * @see Math#pow(double, double)
     */
    public static double pow(double a, double b) {
        return Math.pow(a, b);
    }

    /**
     * @see Math#round(float)
     */
    public static int round(float a) {
        return Math.round(a);
    }

    /**
     * @see Math#round(double)
     */
    public static long round(double a) {
        return Math.round(a);
    }

    /**
     * @see Math#abs(int)
     */
    public static int abs(int a) {
        return Math.abs(a);
    }

    /**
     * @see Math#abs(long)
     */
    public static long abs(long a) {
        return Math.abs(a);
    }

    /**
     * @see Math#max(int, int)
     */
    public static int max(int a, int b) {
        return Math.max(a, b);
    }

    /**
     * @see Math#max(long, long)
     */
    public static long max(long a, long b) {
        return Math.max(a, b);
    }

    /**
     * @see Math#max(float, float)
     */
    public static float max(float a, float b) {
        return Math.max(a, b);
    }

    /**
     * @see Math#max(double, double)
     */
    public static double max(double a, double b) {
        return Math.max(a, b);
    }

    /**
     * @see Math#min(int, int)
     */
    public static int min(int a, int b) {
        return Math.min(a, b);
    }

    /**
     * @see Math#min(long, long)
     */
    public static long min(long a, long b) {
        return Math.min(a, b);
    }

    /**
     * @see Math#min(float, float)
     */
    public static float min(float a, float b) {
        return Math.min(a, b);
    }

    /**
     * @see Math#min(double, double)
     */
    public static double min(double a, double b) {
        return Math.min(a, b);
    }

    // endregion
}
