package com.jmc.math.exp;

import com.jmc.lang.Strs;
import com.jmc.math.Maths;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * 精准计算表达式的结果
 * @since 1.0
 * @author Jmc
 */
@SuppressWarnings("unused")
public class ExactExp {

    private ExactExp() {}

    /**
     * 除法精度
     */
    private static final int DIVIDE_SCALE = 34;

    /**
     * 运算符优先级
     */
    private static final Map<String, Integer> PRIORITY = Map.of(
        "+", 1,
        "-", 1,
        "*", 2,
        "/", 2,
        "%", 2,
        "^", 3,
        "**", 3,
        "√", 3,
        "!", 3
    );

    /**
     * 计算后缀表达式
     * @param suffixExp 后缀表达式
     * @return 计算结果
     */
    private static BigDecimal calculate(List<String> suffixExp) {
        Stack<BigDecimal> stack = new Stack<>();

        for (String s : suffixExp) {
            // 如果是数字
            if (Strs.isNum(s)) {
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
                case "^", "**" -> a.pow(b.intValueExact());
                case "√" -> a.sqrt(MathContext.DECIMAL128);
                case "!" -> new BigDecimal(Maths.factorial(a.intValueExact()));
                default -> throw new UnsupportedOperationException("operator" + s);
            });
        }
        return stack.pop();
    }

    /**
     * 将表达式的计算结果转换为精度16的BigDecimal
     * @param infixExp 表达式
     * @return 精度为16的BigDecimal
     */
    public static BigDecimal getResult(String infixExp) {
        BigDecimal result = getResult(infixExp, 16);

        boolean hasDecimal = result.toString().charAt(result.toString().indexOf(".") + 1) != '0';
        return hasDecimal ? result : getResult(infixExp, 0);
    }

    /**
     * 将表达式的计算结果转换为指定精度的BigDecimal
     * @param infixExp 中缀表达式
     * @param scale 精度
     * @return 指定精度的BigDecimal
     */
    public static BigDecimal getResult(String infixExp, int scale) {
        var suffixExp = ExpUtils.suffix(infixExp, PRIORITY);
        return calculate(suffixExp).setScale(scale, RoundingMode.HALF_UP);
    }
}
