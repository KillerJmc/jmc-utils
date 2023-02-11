package com.jmc.lang;

import com.jmc.math.exp.ExpUtils;

import java.util.Map;
import java.util.Stack;

/**
 * 运算符重载
 * @since 2.0
 * @author Jmc
 * @param <T> 具有运算符重载的类型
 */
public interface Operator<T> {
    // region operators

    /**
     * 重载运算符 +
     * @param other 另一个元素
     * @return 运算结果
     */
    default T plus(T other) {
        throw new UnsupportedOperationException("operator+ (binary)");
    }

    /**
     * 重载运算符 -
     * @param other 另一个元素
     * @return 运算结果
     */
    default T minus(T other) {
        throw new UnsupportedOperationException("operator- (binary)");
    }

    /**
     * 重载运算符 *
     * @param other 另一个元素
     * @return 运算结果
     */
    default T times(T other) {
        throw new UnsupportedOperationException("operator* (binary)");
    }

    /**
     * 重载运算符 /
     * @param other 另一个元素
     * @return 运算结果
     */
    default T div(T other) {
        throw new UnsupportedOperationException("operator/ (binary)");
    }

    /**
     * 重载运算符 %
     * @param other 另一个元素
     * @return 运算结果
     */
    default T mod(T other) {
        throw new UnsupportedOperationException("operator% (binary)");
    }

    /**
     * 重载运算符 +=
     * @param other 另一个元素
     * @return 运算结果
     */
    default T plusAssign(T other) {
        throw new UnsupportedOperationException("operator+= (binary)");
    }

    /**
     * 重载运算符 -=
     * @param other 另一个元素
     * @return 运算结果
     */
    default T minusAssign(T other) {
        throw new UnsupportedOperationException("operator-= (binary)");
    }

    /**
     * 重载运算符 *=
     * @param other 另一个元素
     * @return 运算结果
     */
    default T timesAssign(T other) {
        throw new UnsupportedOperationException("operator*= (binary)");
    }

    /**
     * 重载运算符 /=
     * @param other 另一个元素
     * @return 运算结果
     */
    default T divAssign(T other) {
        throw new UnsupportedOperationException("operator/= (binary)");
    }

    /**
     * 重载运算符 %=
     * @param other 另一个元素
     * @return 运算结果
     */
    default T modAssign(T other) {
        throw new UnsupportedOperationException("operator%= (binary)");
    }

    /**
     * 重载运算符 ++（前置）
     * @return 运算结果
     */
    default T incPre() {
        throw new UnsupportedOperationException("operator++ (unary pre)");
    }

    /**
     * 重载运算符 --（前置）
     * @return 运算结果
     */
    default T decPre() {
        throw new UnsupportedOperationException("operator-- (unary pre)");
    }

    /**
     * 重载运算符 ++（后置）
     * @return 运算结果
     */
    default T incPost() {
        throw new UnsupportedOperationException("operator++ (unary post)");
    }

    /**
     * 重载运算符 --（后置）
     * @return 运算结果
     */
    default T decPost() {
        throw new UnsupportedOperationException("operator-- (unary post)");
    }

    /**
     * 重载运算符 +（正号）
     * @return 运算结果
     */
    default T unaryPlus() {
        throw new UnsupportedOperationException("operator+ (unary pre)");
    }

    /**
     * 重载运算符 -（负号）
     * @return 运算结果
     */
    default T unaryMinus() {
        throw new UnsupportedOperationException("operator- (unary pre)");
    }

    /**
     * 重载运算符 &amp;&amp;
     * @param other 另一个元素
     * @return 运算结果
     */
    default boolean and(T other) {
        throw new UnsupportedOperationException("operator&& (binary)");
    }

    /**
     * 重载运算符 ||
     * @param other 另一个元素
     * @return 运算结果
     */
    default boolean or(T other) {
        throw new UnsupportedOperationException("operator|| (binary)");
    }

    /**
     * 重载运算符 !
     * @return 运算结果
     */
    default boolean not() {
        throw new UnsupportedOperationException("operator! (unary pre)");
    }

    /**
     * 重载运算符 ==
     * @param other 另一个元素
     * @return 运算结果
     */
    default boolean eq(T other) {
        throw new UnsupportedOperationException("operator== (binary)");
    }

    /**
     * 重载运算符 !=
     * @param other 另一个元素
     * @return 运算结果
     */
    default boolean notEq(T other) {
        throw new UnsupportedOperationException("operator!= (binary)");
    }

    /**
     * 重载运算符 &lt;
     * @param other 另一个元素
     * @return 运算结果
     */
    default boolean lessThan(T other) {
        throw new UnsupportedOperationException("operator< (binary)");
    }

    /**
     * 重载运算符 >
     * @param other 另一个元素
     * @return 运算结果
     */
    default boolean greaterThan(T other) {
        throw new UnsupportedOperationException("operator> (binary)");
    }

    /**
     * 重载运算符 &lt;=
     * @param other 另一个元素
     * @return 运算结果
     */
    default boolean lessEq(T other) {
        throw new UnsupportedOperationException("operator<= (binary)");
    }

    /**
     * 重载运算符 >=
     * @param other 另一个元素
     * @return 运算结果
     */
    default boolean greaterEq(T other) {
        throw new UnsupportedOperationException("operator>= (binary)");
    }

    /**
     * 重载运算符 ~
     * @return 运算结果
     */
    default T bitReverse() {
        throw new UnsupportedOperationException("operator~ (unary pre)");
    }

    /**
     * 重载运算符 &amp;
     * @param other 另一个元素
     * @return 运算结果
     */
    default T bitAnd(T other) {
        throw new UnsupportedOperationException("operator& (binary)");
    }

    /**
     * 重载运算符 |
     * @param other 另一个元素
     * @return 运算结果
     */
    default T bitOr(T other) {
        throw new UnsupportedOperationException("operator| (binary)");
    }

    /**
     * 重载运算符 ^
     * @param other 另一个元素
     * @return 运算结果
     */
    default T bitXor(T other) {
        throw new UnsupportedOperationException("operator^ (binary)");
    }

    /**
     * 重载运算符 &lt;&lt;
     * @param other 另一个元素
     * @return 运算结果
     */
    default T shl(T other) {
        throw new UnsupportedOperationException("operator<< (binary)");
    }

    /**
     * 重载运算符 &gt;&gt;
     * @param other 另一个元素
     * @return 运算结果
     */
    default T shr(T other) {
        throw new UnsupportedOperationException("operator>> (binary)");
    }

    /**
     * 重载运算符 &gt;&gt;&gt;
     * @param other 另一个元素
     * @return 运算结果
     */
    default T uShr(T other) {
        throw new UnsupportedOperationException("operator>>> (binary)");
    }

    /**
     * 重载运算符 &amp;=
     * @param other 另一个元素
     * @return 运算结果
     */
    default T bitAndAssign(T other) {
        throw new UnsupportedOperationException("operator&= (binary)");
    }

    /**
     * 重载运算符 |=
     * @param other 另一个元素
     * @return 运算结果
     */
    default T bitOrAssign(T other) {
        throw new UnsupportedOperationException("operator|= (binary)");
    }

    /**
     * 重载运算符 ^=
     * @param other 另一个元素
     * @return 运算结果
     */
    default T bitXorAssign(T other) {
        throw new UnsupportedOperationException("operator^= (binary)");
    }

    /**
     * 重载运算符 &lt;&lt;=
     * @param other 另一个元素
     * @return 运算结果
     */
    default T shlAssign(T other) {
        throw new UnsupportedOperationException("operator<<= (binary)");
    }

    /**
     * 重载运算符 &gt;&gt;=
     * @param other 另一个元素
     * @return 运算结果
     */
    default T shrAssign(T other) {
        throw new UnsupportedOperationException("operator>>= (binary)");
    }

    /**
     * 重载运算符 &gt;&gt;&gt;=
     * @param other 另一个元素
     * @return 运算结果
     */
    default T uShrAssign(T other) {
        throw new UnsupportedOperationException("operator>>>= (binary)");
    }

    // endregion

    // region calculate

    /**
     * 表达式运算（只支持二元运算）
     * @param exp 算数表达式（?为占位符）
     * @param args 参数
     * @param <T> 具有运算符重载的类
     * @return 表达式运算结果
     */
    @SafeVarargs
    static <T extends Operator<T>> T calc(String exp, T... args) {
        exp = placeholder2argsIdx(exp, args);

        // 运算符优先级
        var priority = Map.ofEntries(
                Map.entry("+=", 1),  Map.entry("-=", 1),  Map.entry("*=", 1),   Map.entry("/=", 1),
                Map.entry("%=", 1),  Map.entry("&=", 1),  Map.entry("|=", 1),   Map.entry("^=", 1),
                Map.entry("<<=", 1), Map.entry(">>=", 1), Map.entry(">>>=", 1),
                Map.entry("&", 2),   Map.entry("|", 2),   Map.entry("^", 2),    Map.entry("<<", 2),
                Map.entry(">>", 2),  Map.entry(">>>", 2),
                Map.entry("+", 3),   Map.entry("-", 3),
                Map.entry("*", 4),   Map.entry("/", 4),   Map.entry("%", 4)
        );

        // 构建表达式计算器并返回计算结果
        return ExpUtils.calculator(
                priority,
                s -> args[Integer.parseInt(s)],
                Operator::calc
        ).calc(exp);
    }

    /**
     * 二元运算
     * @param a 元素a
     * @param opr 运算符opr
     * @param b 元素b
     * @param <T> 具有运算符重载的类
     * @return a opr b
     */
    static <T extends Operator<T>> T calc(T a, String opr, T b) {
        return switch (opr) {
            case "+" -> a.plus(b);
            case "-" -> a.minus(b);
            case "*" -> a.times(b);
            case "/" -> a.div(b);
            case "%" -> a.mod(b);
            case "&" -> a.bitAnd(b);
            case "|" -> a.bitOr(b);
            case "^" -> a.bitXor(b);
            case "<<" -> a.shl(b);
            case ">>" -> a.shr(b);
            case ">>>" -> a.uShr(b);
            case "+=" -> a.plusAssign(b);
            case "-=" -> a.minusAssign(b);
            case "*=" -> a.timesAssign(b);
            case "/=" -> a.divAssign(b);
            case "%=" -> a.modAssign(b);
            case "&=" -> a.bitAndAssign(b);
            case "|=" -> a.bitOrAssign(b);
            case "^=" -> a.bitXorAssign(b);
            case "<<=" -> a.shlAssign(b);
            case ">>=" -> a.shrAssign(b);
            case ">>>=" -> a.uShrAssign(b);
            default -> throw new UnsupportedOperationException("operator" + opr + " (binary)");
        };
    }

    /**
     * 一元前置运算
     * @param opr 前置运算符
     * @param a 元素a
     * @param <T> 具有运算符重载的类
     * @return opr a
     */
    static <T extends Operator<T>> T calc(String opr, T a) {
        return switch (opr) {
            case "+" -> a.unaryPlus();
            case "-" -> a.unaryMinus();
            case "~" -> a.bitReverse();
            case "++" -> a.incPre();
            case "--" -> a.decPre();
            default -> throw new UnsupportedOperationException("operator" + opr + " (unary pre)");
        };
    }

    /**
     * 一元后置运算
     * @param opr 后置运算符
     * @param a 元素a
     * @param <T> 具有运算符重载的类
     * @return a opr
     */
    static <T extends Operator<T>> T calc(T a, String opr) {
        return switch (opr) {
            case "++" -> a.incPost();
            case "--" -> a.decPost();
            default -> throw new UnsupportedOperationException("operator" + opr + " (unary post)");
        };
    }

    /**
     * 把表达式中的占位符替换为参数数组下标
     * @param exp 原来的表达式
     * @param args 参数数组
     * @param <T> 具有运算符重载的类
     * @return 替换后的表达式
     */
    private static <T extends Operator<T>> String placeholder2argsIdx(String exp, T[] args) {
        var sb = new StringBuilder(exp);
        int actualArgAmount = 0;

        // 把占位符?替换为从0开始的数组下标
        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) == '?') {
                sb.deleteCharAt(i);
                sb.insert(i, actualArgAmount++);
            }
        }
        exp = sb.toString();

        // 判断参数个数是否匹配
        if (actualArgAmount != args.length) {
            throw new IllegalArgumentException("Wrong arg amount: %d, expected: %d".formatted(args.length, actualArgAmount));
        }
        return exp;
    }

    // endregion

    // region compare

    /**
     * 表达式布尔运算（只支持二元运算）
     * @param exp 算数表达式
     * @param args 参数
     * @param <T> 具有运算符重载的类
     * @return 表达式结果布尔值
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    static <T extends Operator<T>> boolean cmp(String exp, T... args) {
        exp = placeholder2argsIdx(exp, args);

        // 运算符优先级
        var priority = Map.ofEntries(
                Map.entry("+=", 1),  Map.entry("-=", 1),  Map.entry("*=", 1),   Map.entry("/=", 1),
                Map.entry("%=", 1),  Map.entry("&=", 1),  Map.entry("|=", 1),   Map.entry("^=", 1),
                Map.entry("<<=", 1), Map.entry(">>=", 1), Map.entry(">>>=", 1),
                Map.entry("&&", 2),  Map.entry("||", 2),
                Map.entry("&", 3),   Map.entry("|", 3),   Map.entry("^", 3),
                Map.entry("==", 4),  Map.entry("!=", 4),
                Map.entry("<", 5),   Map.entry("<=", 5),  Map.entry(">", 5),    Map.entry(">=", 5),
                Map.entry("<<", 6),  Map.entry(">>", 6),  Map.entry(">>>", 6),
                Map.entry("+", 7),   Map.entry("-", 7),
                Map.entry("*", 8),   Map.entry("/", 8),   Map.entry("%", 8)
        );

        var suffix = ExpUtils.suffix(exp, priority);

        var stack = new Stack<>();
        for (var s : suffix) {
            // 如果是下标
            if (Strs.isNum(s)) {
                // 存入下标对应的T
                stack.push(args[Integer.parseInt(s)]);
                continue;
            }

            Object b = stack.pop(), a = stack.pop();

            // 如果是运算符
            if (a instanceof Operator t1 && b instanceof Operator t2) {
                // 如果是比较符
                if (Strs.orEquals(s, "<", "<=", ">", ">=", "&&", "||", "==", "!=")) {
                    // 计算并存入布尔值
                    stack.push(cmp(t1, s, t2));
                } else {
                    // 计算并存入T
                    stack.push(calc(t1, s, t2));
                }
                continue;
            }

            // 如果是布尔值
            if (a instanceof Boolean b1 && b instanceof Boolean b2) {
                // 计算并存入布尔值
                stack.push("&&".equals(s) ? b1 && b2 : b1 || b2);
            }
        }

        // 返回栈顶元素
        return (boolean) stack.pop();
    }

    /**
     * 二元布尔值运算
     * @param a 元素a
     * @param opr 运算符
     * @param b 元素b
     * @param <T> 具有运算符重载的类
     * @return a opr b
     */
    static <T extends Operator<T>> boolean cmp(T a, String opr, T b) {
        return switch (opr) {
            case "==" -> a.eq(b);
            case "!=" -> a.notEq(b);
            case "<" -> a.lessThan(b);
            case ">" -> a.greaterThan(b);
            case "<=" -> a.lessEq(b);
            case ">=" -> a.greaterEq(b);
            case "||" -> a.or(b);
            case "&&" -> a.and(b);
            default -> throw new UnsupportedOperationException("operator" + opr + " (binary)");
        };
    }

    /**
     * 一元前置布尔值运算
     * @param opr 前置运算符
     * @param a 元素a
     * @param <T> 具有运算符重载的类
     * @return opr a
     */
    static <T extends Operator<T>> boolean cmp(String opr, T a) {
        if ("!".equals(opr)) {
            return a.not();
        }
        throw new UnsupportedOperationException("operator" + opr + " (unary pre)");
    }

    // endregion
}
