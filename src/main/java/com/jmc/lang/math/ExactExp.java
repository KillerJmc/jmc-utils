package com.jmc.lang.math;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * 精准计算表达式的结果
 * @since 1.0.0
 * @author Jmc
 */
@SuppressWarnings("unused")
public class ExactExp {
    /**
     * 除法精度
     */
    private static final int DIVIDE_SCALE = 34;

    /**
     * 符号优先级
     */
    private static final Map<String, Integer> PRIORITY = Map.ofEntries(
        Map.entry("+", 1),
        Map.entry("-", 1),
        Map.entry("*", 2),
        Map.entry("/", 2),
        Map.entry("%", 2),
        Map.entry("^", 3),
        Map.entry("√", 3),
        Map.entry("!", 3),
        Map.entry("(", 0),
        Map.entry(")", 0),
        Map.entry("#", 0)
    );

    /**
     * 将字符串数字与符号一一分离
     * @param exp 表达式
     * @return 分离得到的字符串数组（中缀表达式）
     */
    private static String[] split(String exp) {
        char[] cs = exp.replace(" ", "").replace("**", "^").toCharArray();
        List<String> list = new ArrayList<>();
        StringBuilder t = new StringBuilder();

        for (int i = 0; i < cs.length; i++) {
            // 判断负数
            if (cs[i] == '-' && (i == 0 || !Character.isDigit(cs[i - 1]))) {
                t.append(cs[i]);
                continue;
            }

            // 读完一个数
            boolean numberEnds = !Character.isDigit(cs[i]) && cs[i] != '.';
            if (numberEnds) {
                if (t.length() > 0) {
                    list.add(t.toString());
                    t.delete(0, t.length());
                }

                // 结果加上这个操作符
                list.add(String.valueOf(cs[i]));
                continue;
            }

            // 存放数字
            t.append(cs[i]);
        }
        list.add(t.toString());
        list.add("#");

        return list.toArray(new String[0]);
    }

    /**
     * 转换中缀表达式为后缀表达式
     * @param infixExp 中缀表达式
     * @return 后缀表达式
     */
    private static String[] transToSuffixExp(String[] infixExp) {
        Stack<String> s1 = new Stack<>();
        s1.push("#");
        Stack<String> s2 = new Stack<>();

        for (String s : infixExp) {
            if (s.length() == 0) {
                continue;
            }

            // 如果是正数或负数
            if (Character.isDigit(s.charAt(0)) || (s.length() > 1 && s.charAt(0) == '-')) {
                s2.push(s);
                continue;
            }

            if (PRIORITY.get(s) > 0) {
                while (PRIORITY.get(s) <= PRIORITY.get(s1.peek())) {
                    s2.push(s1.pop());
                }
                s1.push(s);
                continue;
            }

            if ("(".equals(s)) {
                s1.push(s);
                continue;
            }

            if (")".equals(s)) {
                while (!"(".equals(s1.peek())) {
                    s2.push(s1.pop());
                }
                s1.pop();
                continue;
            }

            if ("#".equals(s)) {
                while (s1.size() > 1) {
                    s2.push(s1.pop());
                }
            }
        }

        return s2.toArray(new String[0]);
    }

    /**
     * 计算后缀表达式
     * @param suffixExp 后缀表达式
     * @return 计算结果
     */
    private static BigDecimal calculate(String[] suffixExp) {
        Stack<BigDecimal> stack = new Stack<>();

        for (String s : suffixExp) {
            // 如果是正数或者负数
            if (Character.isDigit(s.charAt(0)) || (s.length() > 1 && s.charAt(0) == '-')) {
                stack.push(new BigDecimal(s));
                continue;
            }

            BigDecimal b = !"√".equals(s) && !"!".equals(s) ? stack.pop() : null;
            BigDecimal a = stack.pop();

            stack.push(switch(s) {
                case "+" -> a.add(b);
                case "-" -> a.subtract(b);
                case "*" -> a.multiply(b);
                case "/" -> a.divide(b, DIVIDE_SCALE, RoundingMode.HALF_UP);
                case "%" -> new BigDecimal(a.toBigIntegerExact().mod(b.toBigIntegerExact()));
                case "^" -> a.pow(b.intValueExact());
                case "√" -> a.sqrt(MathContext.DECIMAL128);
                case "!" -> new BigDecimal(Maths.factorial(a.intValueExact()));
                default -> throw new IllegalStateException("Unexpected value: " + s);
            });
        }
        return stack.pop();
    }

    /**
     * 将表达式的计算结果转换为精度16的BigDecimal
     * @param exp 表达式
     * @return 精度为16的BigDecimal
     */
    public static BigDecimal getResult(String exp) {
        BigDecimal result = getResult(exp, 16);

        boolean hasDecimal = result.toString().charAt(result.toString().indexOf(".") + 1) != '0';
        return hasDecimal ? result : getResult(exp, 0);
    }

    /**
     * 将表达式的计算结果转换为指定精度的BigDecimal
     * @param exp 表达式
     * @param scale 精度
     * @return 指定精度的BigDecimal
     */
    public static BigDecimal getResult(String exp, int scale) {
        String[] infixExp = split(exp);
        String[] suffixExp = transToSuffixExp(infixExp);
        return calculate(suffixExp).setScale(scale, RoundingMode.HALF_UP);
    }
}
