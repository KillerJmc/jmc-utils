package com.jmc.math.exp;

import com.jmc.lang.Strs;
import com.jmc.lang.ref.Func;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.Function;

/**
 * 表达式工具类
 * @since 2.0
 * @author Jmc
 */
public class ExpUtils {

    private ExpUtils() {}

    /**
     * 默认运算符优先级
     */
    public static final Map<String, Integer> DEFAULT_PRIORITY = Map.of(
            "+", 1,
            "-", 1,
            "*", 2,
            "/", 2,
            "%", 2
    );

    /**
     * 把中缀表达式中的数字与运算符分离开（如果是一元运算符需要加括号）
     * @param infixExp 中缀表达式
     * @return 分离得到的字符串数组（中缀表达式）
     */
    public static List<String> splitInfix(String infixExp) {
        // 去除表达式空格
        infixExp = infixExp.replace(" ", "");
        char[] cs = infixExp.toCharArray();

        var res = new ArrayList<String>();

        for (int i = 0; i < cs.length; i++) {
            // 是否为负数
            var isNeg = false;

            // 判断负号（只能在表达式最前面 或者 其前面一个是为不为右括号的非数字字符，即不能是这种情况：(1+2)-3）
            if (cs[i] == '-' && (i == 0 || (!Character.isDigit(cs[i - 1])) && cs[i - 1] != ')')) {
                // 标记
                isNeg = true;
                // 跳过负号字符
                i++;
            }

            // 收集数字
            if (Character.isDigit(cs[i])) {
                for (int j = i + 1; j <= cs.length; j++) {
                    // 读到字符串末尾 或者 下一位不再是小数点或数字
                    if (j == cs.length || (!Character.isDigit(cs[j]) && cs[j] != '.')) {
                        // 加入读到的数字，如果是负数就加上之前的符号
                        res.add(infixExp.substring(isNeg ? i - 1 : i, j));
                        // 改变i的位置
                        i = j - 1;
                        break;
                    }
                }
                continue;
            }

            // 收集括号
            if (cs[i] == '(' || cs[i] == ')') {
                res.add(String.valueOf(cs[i]));
                continue;
            }

            // 收集其他运算符
            for (int j = i + 1; j <= cs.length; j++) {
                // 读到字符串末尾 或者 下一个是数字或者括号（右括号存在于后置一元运算符的情况：2+(3!)）
                if (j == cs.length || Character.isDigit(cs[j]) || cs[j] == '(' || cs[j] == ')') {
                    // 读完了一个运算符
                    var operator = infixExp.substring(i, j);
                    // 加入该运算符
                    res.add(operator);
                    // 改变i的位置
                    i = j - 1;
                    break;
                }
            }
        }
        return res;
    }

    /**
     * 把中缀表达式转换为后缀表达式
     * @param splitInfix 中缀表达式分离数字和运算符后的集合
     * @param priority 二元运算符优先级（比如{(+, 1), (/, 2), (**, 2)}）
     * @return 后缀表达式
     */
    public static List<String> suffix(List<String> splitInfix, Map<String, Integer> priority) {
        // 辅助栈
        var stack = new Stack<String>();
        var res = new ArrayList<String>();

        for (var s : splitInfix) {
            // 如果是数字
            if (Strs.isNum(s)) {
                res.add(s);
                continue;
            }

            // 左括号直接放入
            if ("(".equals(s)) {
                stack.push(s);
                continue;
            }

            // 遇到右括号
            if (")".equals(s)) {
                // 弹出到左括号的所有运算符
                while (!"(".equals(stack.peek())) {
                    res.add(stack.pop());
                }
                // 去除左括号
                stack.pop();
                continue;
            }

            // 未知运算符抛出异常
            if (priority.get(s) == null) {
                throw new UnsupportedOperationException("operator" + s);
            }

            // 如果当前运算符优先级不大于栈顶运算符且栈顶不是左括号
            while (!stack.isEmpty() && !"(".equals(stack.peek()) && !(priority.get(s) > priority.get(stack.peek()))) {
                // 不断出栈
                res.add(stack.pop());
            }
            // 把运算符放入栈
            stack.push(s);
        }

        // 剩余符号依次弹出
        while (!stack.isEmpty()) {
            res.add(stack.pop());
        }

        return res;
    }

    /**
     * 把中缀表达式转换为后缀表达式
     * @param infixExp 中缀表达式
     * @param priority 运算符优先级（比如{(+, 1), (/, 2), (**, 2)}）
     * @return 后缀表达式
     */
    public static List<String> suffix(String infixExp, Map<String, Integer> priority) {
        return suffix(splitInfix(infixExp), priority);
    }

    /**
     * 表达式计算器
     * @param <T> 表达式中数字类型
     */
    public interface Calculator<T> {
        /**
         * 计算方法
         * @param infixExp 中缀表达式
         * @return 计算结果
         */
        T calc(String infixExp);
    }

    /**
     * 构造一个表达式计算器
     * @param priority 二元运算符优先级
     * @param str2Number 表达式中字符串数字转化成目标类型数字的函数（比如字符串 -> Double，字符串 -> BigInteger）
     * @param calc 运算方法（调用时分别传入数字a，运算符opr，数字b）
     * @param <T> 表达式中数字类型
     * @return 表达式计算器
     */
    public static <T> Calculator<T> calculator(Map<String, Integer> priority,
                                               Function<String, T> str2Number,
                                               Func.Object3<T, String, T, T> calc)
    {
        return infixExp -> {
            var suffix = suffix(infixExp, priority);

            var stack = new Stack<T>();

            for (var s : suffix) {
                // 如果是数字
                if (Strs.isNum(s)) {
                    stack.push(str2Number.apply(s));
                    continue;
                }

                T b = stack.pop(), a = stack.pop();

                stack.push(calc.invoke(a, s, b));
            }
            return stack.pop();
        };
    }

    /**
     * 默认的表达式计算器
     * @return 默认的表达式计算器
     */
    public static Calculator<Double> defaultCalculator() {
        return ExpUtils.calculator(
                DEFAULT_PRIORITY,
                Double::valueOf,
                (a, opr, b) -> switch (opr) {
                    case "+" -> a + b;
                    case "-" -> a - b;
                    case "*" -> a * b;
                    case "/" -> a / b;
                    case "%" -> a % b;
                    default -> throw new UnsupportedOperationException("operator" + opr);
                }
        );
    }
}
