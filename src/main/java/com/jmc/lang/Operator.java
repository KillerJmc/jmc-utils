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

        return ExpUtils.calculator(
                priority,
                s -> args[Integer.parseInt(s)],
                Operator::calc
        ).calc(exp);
    }


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
            case "+" -> a.operatorPlus(b);
            case "-" -> a.operatorMinus(b);
            case "*" -> a.operatorTimes(b);
            case "/" -> a.operatorDiv(b);
            case "%" -> a.operatorMod(b);
            case "&" -> a.operatorBitAnd(b);
            case "|" -> a.operatorBitOr(b);
            case "^" -> a.operatorBitXor(b);
            case "<<" -> a.operatorShl(b);
            case ">>" -> a.operatorShr(b);
            case ">>>" -> a.operatorUShr(b);
            case "+=" -> a.operatorPlusEq(b);
            case "-=" -> a.operatorMinusEq(b);
            case "*=" -> a.operatorTimesEq(b);
            case "/=" -> a.operatorDivEq(b);
            case "%=" -> a.operatorModEq(b);
            case "&=" -> a.operatorBitAndEq(b);
            case "|=" -> a.operatorBitOrEq(b);
            case "^=" -> a.operatorBitXorEq(b);
            case "<<=" -> a.operatorShlEq(b);
            case ">>=" -> a.operatorShrEq(b);
            case ">>>=" -> a.operatorUShrEq(b);
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
            case "+" -> a.operatorPos();
            case "-" -> a.operatorNeg();
            case "~" -> a.operatorBitReverse();
            case "++" -> a.operatorIncPre();
            case "--" -> a.operatorDecPre();
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
            case "++" -> a.operatorIncPost();
            case "--" -> a.operatorDecPost();
            default -> throw new UnsupportedOperationException("operator" + opr + " (unary post)");
        };
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
            case "==" -> a.operatorDoubleEq(b);
            case "!=" -> a.operatorNotEq(b);
            case "<" -> a.operatorLT(b);
            case ">" -> a.operatorGT(b);
            case "<=" -> a.operatorLE(b);
            case ">=" -> a.operatorGE(b);
            case "||" -> a.operatorOr(b);
            case "&&" -> a.operatorAnd(b);
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
            return a.operatorNot();
        }
        throw new UnsupportedOperationException("operator" + opr + " (unary pre)");
    }

    /**
     * 重载运算符 +
     * @param other 另一个元素
     * @return 运算结果
     */
    default T operatorPlus(T other) {
        throw new UnsupportedOperationException("operator+ (binary)");
    }

    /**
     * 重载运算符 -
     * @param other 另一个元素
     * @return 运算结果
     */
    default T operatorMinus(T other) {
        throw new UnsupportedOperationException("operator- (binary)");
    }

    /**
     * 重载运算符 *
     * @param other 另一个元素
     * @return 运算结果
     */
    default T operatorTimes(T other) {
        throw new UnsupportedOperationException("operator* (binary)");
    }

    /**
     * 重载运算符 /
     * @param other 另一个元素
     * @return 运算结果
     */
    default T operatorDiv(T other) {
        throw new UnsupportedOperationException("operator/ (binary)");
    }

    /**
     * 重载运算符 %
     * @param other 另一个元素
     * @return 运算结果
     */
    default T operatorMod(T other) {
        throw new UnsupportedOperationException("operator% (binary)");
    }

    /**
     * 重载运算符 &amp;
     * @param other 另一个元素
     * @return 运算结果
     */
    default T operatorBitAnd(T other) {
        throw new UnsupportedOperationException("operator& (binary)");
    }

    /**
     * 重载运算符 |
     * @param other 另一个元素
     * @return 运算结果
     */
    default T operatorBitOr(T other) {
        throw new UnsupportedOperationException("operator| (binary)");
    }

    /**
     * 重载运算符 ^
     * @param other 另一个元素
     * @return 运算结果
     */
    default T operatorBitXor(T other) {
        throw new UnsupportedOperationException("operator^ (binary)");
    }

    /**
     * 重载运算符 &lt;&lt;
     * @param other 另一个元素
     * @return 运算结果
     */
    default T operatorShl(T other) {
        throw new UnsupportedOperationException("operator<< (binary)");
    }

    /**
     * 重载运算符 &gt;&gt;
     * @param other 另一个元素
     * @return 运算结果
     */
    default T operatorShr(T other) {
        throw new UnsupportedOperationException("operator>> (binary)");
    }

    /**
     * 重载运算符 &gt;&gt;&gt;
     * @param other 另一个元素
     * @return 运算结果
     */
    default T operatorUShr(T other) {
        throw new UnsupportedOperationException("operator>>> (binary)");
    }

    /**
     * 重载运算符 +=
     * @param other 另一个元素
     * @return 运算结果
     */
    default T operatorPlusEq(T other) {
        throw new UnsupportedOperationException("operator+= (binary)");
    }

    /**
     * 重载运算符 -=
     * @param other 另一个元素
     * @return 运算结果
     */
    default T operatorMinusEq(T other) {
        throw new UnsupportedOperationException("operator-= (binary)");
    }

    /**
     * 重载运算符 *=
     * @param other 另一个元素
     * @return 运算结果
     */
    default T operatorTimesEq(T other) {
        throw new UnsupportedOperationException("operator*= (binary)");
    }

    /**
     * 重载运算符 /=
     * @param other 另一个元素
     * @return 运算结果
     */
    default T operatorDivEq(T other) {
        throw new UnsupportedOperationException("operator/= (binary)");
    }

    /**
     * 重载运算符 %=
     * @param other 另一个元素
     * @return 运算结果
     */
    default T operatorModEq(T other) {
        throw new UnsupportedOperationException("operator%= (binary)");
    }

    /**
     * 重载运算符 &amp;=
     * @param other 另一个元素
     * @return 运算结果
     */
    default T operatorBitAndEq(T other) {
        throw new UnsupportedOperationException("operator&= (binary)");
    }

    /**
     * 重载运算符 |=
     * @param other 另一个元素
     * @return 运算结果
     */
    default T operatorBitOrEq(T other) {
        throw new UnsupportedOperationException("operator|= (binary)");
    }

    /**
     * 重载运算符 ^=
     * @param other 另一个元素
     * @return 运算结果
     */
    default T operatorBitXorEq(T other) {
        throw new UnsupportedOperationException("operator^= (binary)");
    }

    /**
     * 重载运算符 &lt;&lt;=
     * @param other 另一个元素
     * @return 运算结果
     */
    default T operatorShlEq(T other) {
        throw new UnsupportedOperationException("operator<<= (binary)");
    }

    /**
     * 重载运算符 &gt;&gt;=
     * @param other 另一个元素
     * @return 运算结果
     */
    default T operatorShrEq(T other) {
        throw new UnsupportedOperationException("operator>>= (binary)");
    }

    /**
     * 重载运算符 &gt;&gt;&gt;=
     * @param other 另一个元素
     * @return 运算结果
     */
    default T operatorUShrEq(T other) {
        throw new UnsupportedOperationException("operator>>>= (binary)");
    }

    /**
     * 重载运算符 +（正号）
     * @return 运算结果
     */
    default T operatorPos() {
        throw new UnsupportedOperationException("operator+ (unary pre)");
    }

    /**
     * 重载运算符 -（负号）
     * @return 运算结果
     */
    default T operatorNeg() {
        throw new UnsupportedOperationException("operator- (unary pre)");
    }

    /**
     * 重载运算符 ~
     * @return 运算结果
     */
    default T operatorBitReverse() {
        throw new UnsupportedOperationException("operator~ (unary pre)");
    }

    /**
     * 重载运算符 ++（前置）
     * @return 运算结果
     */
    default T operatorIncPre() {
        throw new UnsupportedOperationException("operator++ (unary pre)");
    }

    /**
     * 重载运算符 --（前置）
     * @return 运算结果
     */
    default T operatorDecPre() {
        throw new UnsupportedOperationException("operator-- (unary pre)");
    }

    /**
     * 重载运算符 ++（后置）
     * @return 运算结果
     */
    default T operatorIncPost() {
        throw new UnsupportedOperationException("operator++ (unary post)");
    }

    /**
     * 重载运算符 --（后置）
     * @return 运算结果
     */
    default T operatorDecPost() {
        throw new UnsupportedOperationException("operator-- (unary post)");
    }

    /**
     * 重载运算符 ==
     * @param other 另一个元素
     * @return 运算结果
     */
    default boolean operatorDoubleEq(T other) {
        throw new UnsupportedOperationException("operator== (binary)");
    }

    /**
     * 重载运算符 !=
     * @param other 另一个元素
     * @return 运算结果
     */
    default boolean operatorNotEq(T other) {
        throw new UnsupportedOperationException("operator!= (binary)");
    }

    /**
     * 重载运算符 &lt;
     * @param other 另一个元素
     * @return 运算结果
     */
    default boolean operatorLT(T other) {
        throw new UnsupportedOperationException("operator< (binary)");
    }

    /**
     * 重载运算符 >
     * @param other 另一个元素
     * @return 运算结果
     */
    default boolean operatorGT(T other) {
        throw new UnsupportedOperationException("operator> (binary)");
    }

    /**
     * 重载运算符 &lt;=
     * @param other 另一个元素
     * @return 运算结果
     */
    default boolean operatorLE(T other) {
        throw new UnsupportedOperationException("operator<= (binary)");
    }

    /**
     * 重载运算符 >=
     * @param other 另一个元素
     * @return 运算结果
     */
    default boolean operatorGE(T other) {
        throw new UnsupportedOperationException("operator>= (binary)");
    }

    /**
     * 重载运算符 &amp;&amp;（不推荐）
     * @param other 另一个元素
     * @return 运算结果
     */
    default boolean operatorAnd(T other) {
        throw new UnsupportedOperationException("operator&& (binary)");
    }

    /**
     * 重载运算符 ||（不推荐）
     * @param other 另一个元素
     * @return 运算结果
     */
    default boolean operatorOr(T other) {
        throw new UnsupportedOperationException("operator|| (binary)");
    }

    /**
     * 重载运算符 !
     * @return 运算结果
     */
    default boolean operatorNot() {
        throw new UnsupportedOperationException("operator! (unary pre)");
    }
}
