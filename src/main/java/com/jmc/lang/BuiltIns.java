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
     * Prints a boolean value.
     * @param b the boolean to print
     * @see PrintStream#print(boolean)
     */
    public static void print(boolean b) {
        System.out.print(b);
    }

    /**
     * Prints a character.
     * @param c the character to print
     * @see PrintStream#print(char)
     */
    public static void print(char c) {
        System.out.print(c);
    }

    /**
     * Prints an integer.
     * @param i the integer to print
     * @see PrintStream#print(int)
     */
    public static void print(int i) {
        System.out.print(i);
    }

    /**
     * Prints a long integer.
     * @param l the long integer to print
     * @see PrintStream#print(long)
     */
    public static void print(long l) {
        System.out.print(l);
    }

    /**
     * Prints a floating-point number.
     * @param f the float to print
     * @see PrintStream#print(float)
     */
    public static void print(float f) {
        System.out.print(f);
    }

    /**
     * Prints a double-precision floating-point number.
     * @param d the double to print
     * @see PrintStream#print(double)
     */
    public static void print(double d) {
        System.out.print(d);
    }

    /**
     * Prints an array of characters.
     * @param s the array of characters to print
     * @see PrintStream#print(char[])
     */
    public static void print(char[] s) {
        System.out.print(s);
    }

    /**
     * Prints a string.
     * @param s the string to print
     * @see PrintStream#print(String)
     */
    public static void print(String s) {
        System.out.print(s);
    }

    /**
     * Prints an object.
     * @param obj the object to print
     * @see PrintStream#print(Object)
     */
    public static void print(Object obj) {
        System.out.print(obj);
    }

    /**
     * Terminates the current line by writing the line separator string.
     * @see PrintStream#println()
     */
    public static void println() {
        System.out.println();
    }

    /**
     * Prints a boolean value and then terminate the line.
     * @param x the boolean to print
     * @see PrintStream#println(boolean)
     */
    public static void println(boolean x) {
        System.out.println(x);
    }

    /**
     * Prints a character and then terminate the line.
     * @param x the character to print
     * @see PrintStream#println(char)
     */
    public static void println(char x) {
        System.out.println(x);
    }

    /**
     * Prints an integer and then terminate the line.
     * @param x the integer to print
     * @see PrintStream#println(int)
     */
    public static void println(int x) {
        System.out.println(x);
    }

    /**
     * Prints a long integer and then terminate the line.
     * @param x the long integer to print
     * @see PrintStream#println(long)
     */
    public static void println(long x) {
        System.out.println(x);
    }

    /**
     * Prints a floating-point number and then terminate the line.
     * @param x the float to print
     * @see PrintStream#println(float)
     */
    public static void println(float x) {
        System.out.println(x);
    }

    /**
     * Prints a double-precision floating-point number and then terminate the line.
     * @param x the double to print
     * @see PrintStream#println(double)
     */
    public static void println(double x) {
        System.out.println(x);
    }

    /**
     * Prints an array of characters and then terminate the line.
     * @param x the array of characters to print
     * @see PrintStream#println(char[])
     */
    public static void println(char[] x) {
        System.out.println(x);
    }

    /**
     * Prints a string and then terminate the line.
     * @param x the string to print
     * @see PrintStream#println(String)
     */
    public static void println(String x) {
        System.out.println(x);
    }

    /**
     * Prints an object and then terminate the line.
     * @param x the object to print
     * @see PrintStream#println(Object)
     */
    public static void println(Object x) {
        System.out.println(x);
    }

    /**
     * Formats a string according to a format string and writes the formatted string to the standard output stream,
     * using the specified arguments.
     * @param format a format string
     * @param args   arguments referenced by the format specifiers in the format string
     * @return the PrintStream object itself
     * @see PrintStream#printf(String, Object...)
     */
    public static PrintStream printf(String format, Object... args) {
        return System.out.printf(format, args);
    }

    /**
     * Formats a string according to a format string and the specified locale, then writes the formatted string to the
     * standard output stream, using the specified arguments.
     * @param l      the locale to apply during formatting. If {@code l} is {@code null}, no localization is applied.
     * @param format a format string
     * @param args   arguments referenced by the format specifiers in the format string
     * @return the PrintStream object itself
     * @see PrintStream#printf(Locale, String, Object...)
     */
    public static PrintStream printf(Locale l, String format, Object... args) {
        return System.out.printf(l, format, args);
    }

    // endregion

    // region math

    /**
     * Returns the correctly rounded positive square root of a double value.
     * @param a a value
     * @return the positive square root of {@code a}
     * @see Math#sqrt(double)
     */
    public static double sqrt(double a) {
        return Math.sqrt(a);
    }

    /**
     * Returns the value of the first argument raised to the power of the second argument.
     * @param a the base
     * @param b the exponent
     * @return the value {@code a}<sup>{@code b}</sup>.
     * @see Math#pow(double, double)
     */
    public static double pow(double a, double b) {
        return Math.pow(a, b);
    }

    /**
     * Returns the closest {@code int} to the argument, with ties rounding to positive infinity.
     * @param a a floating-point value to be rounded to an integer.
     * @return the value of the argument rounded to the nearest {@code int} value.
     * @see Math#round(float)
     */
    public static int round(float a) {
        return Math.round(a);
    }

    /**
     * Returns the closest {@code long} to the argument, with ties rounding to positive infinity.
     * @param a a floating-point value to be rounded to a {@code long}.
     * @return the value of the argument rounded to the nearest {@code long} value.
     * @see Math#round(double)
     */
    public static long round(double a) {
        return Math.round(a);
    }

    /**
     * Returns the absolute value of an {@code int} value.
     * @param a the argument whose absolute value is to be determined
     * @return the absolute value of {@code a}.
     * @see Math#abs(int)
     */
    public static int abs(int a) {
        return Math.abs(a);
    }

    /**
     * Returns the absolute value of a {@code long} value.
     * @param a the argument whose absolute value is to be determined
     * @return the absolute value of {@code a}.
     * @see Math#abs(long)
     */
    public static long abs(long a) {
        return Math.abs(a);
    }

    /**
     * Returns the greater of two {@code int} values.
     * @param a an {@code int}
     * @param b another {@code int}
     * @return the larger of {@code a} and {@code b}.
     * @see Math#max(int, int)
     */
    public static int max(int a, int b) {
        return Math.max(a, b);
    }

    /**
     * Returns the greater of two {@code long} values.
     * @param a a {@code long}
     * @param b another {@code long}
     * @return the larger of {@code a} and {@code b}.
     * @see Math#max(long, long)
     */
    public static long max(long a, long b) {
        return Math.max(a, b);
    }

    /**
     * Returns the greater of two {@code float} values.
     * @param a a {@code float}
     * @param b another {@code float}
     * @return the larger of {@code a} and {@code b}.
     * @see Math#max(float, float)
     */
    public static float max(float a, float b) {
        return Math.max(a, b);
    }

    /**
     * Returns the greater of two {@code double} values.
     * @param a a {@code double}
     * @param b another {@code double}
     * @return the larger of {@code a} and {@code b}.
     * @see Math#max(double, double)
     */
    public static double max(double a, double b) {
        return Math.max(a, b);
    }

    /**
     * Returns the smaller of two {@code int} values.
     * @param a an {@code int}
     * @param b another {@code int}
     * @return the smaller of {@code a} and {@code b}.
     * @see Math#min(int, int)
     */
    public static int min(int a, int b) {
        return Math.min(a, b);
    }

    /**
     * Returns the smaller of two {@code long} values.
     * @param a a {@code long}
     * @param b another {@code long}
     * @return the smaller of {@code a} and {@code b}.
     * @see Math#min(long, long)
     */
    public static long min(long a, long b) {
        return Math.min(a, b);
    }

    /**
     * Returns the smaller of two {@code float} values.
     * @param a a {@code float}
     * @param b another {@code float}
     * @return the smaller of {@code a} and {@code b}.
     * @see Math#min(float, float)
     */
    public static float min(float a, float b) {
        return Math.min(a, b);
    }

    /**
     * Returns the smaller of two {@code double} values.
     * @param a a {@code double}
     * @param b another {@code double}
     * @return the smaller of {@code a} and {@code b}.
     * @see Math#min(double, double)
     */
    public static double min(double a, double b) {
        return Math.min(a, b);
    }

    // endregion

}
