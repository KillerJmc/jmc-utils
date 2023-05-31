package com.jmc.lang.reflect;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * 反编译类
 * @since 1.0
 * @author Jmc
 */
public class ClassDecompile {

    private ClassDecompile() {}

    /**
     * 反编译类
     * @param c Class
     * @return 反编译输出的字符串
     * @apiNote <pre>{@code
     * // 反编译String类
     * String res = ClassDecompile.decompile(String.class);
     * }</pre>
     */
    public static String decompile(Class<?> c) {
        // 储存结果
        var res = new StringBuilder();

        // 符号的字符串
        String blank = " ", emptyStr = "",
               newLine = "\n", tab = "\t",
               comma = ",", semicolon = ";",
               leftBracket = "(", rightBracket = ")",
               leftBrace = "{", rightBrace = "}" ;

        // 如果字符串非空就返回target字符串
        BiFunction<String, String, String> ifExistsThen =
                (s, target) -> !s.isEmpty() ? target : emptyStr;

        // 继承标记
        var extendsSymbol = "extends";
        // 实现标记
        var implSymbol = "implements";

        // 类修饰符
        var classModifier = Modifier.toString(c.getModifiers());
        // 如果不是接口就加上class标识
        var classSymbol = c.isInterface() ? emptyStr : "class";
        // 类名
        var className = c.getSimpleName();
        // 父类名
        var superClassName = c.getSuperclass() == null ? emptyStr : c.getSuperclass().getSimpleName();
        // 类实现的所有接口
        var implInterfaces = Arrays.stream(c.getInterfaces())
                .map(Class::getSimpleName)
                .collect(Collectors.joining(comma + blank));

        // 类成员变量描述
        var fieldsStr = Arrays.stream(c.getDeclaredFields())
                .map(f -> {
                    var modifierStr = Modifier.toString(f.getModifiers());
                    var fieldTypeName = f.getType().getSimpleName();
                    var fieldName = f.getName();
                    // "\t" + 变量可见修饰符 + 变量类型 + 变量名称 + ";"
                    return tab + ifExistsThen.apply(modifierStr, modifierStr + blank)
                            + fieldTypeName + blank + fieldName + semicolon;
                })
                .collect(Collectors.joining(newLine));

        // 类构造方法描述
        var ctorsStr = Arrays.stream(c.getDeclaredConstructors())
                        .map(ctor -> {
                            var modifierStr = Modifier.toString(ctor.getModifiers());
                            var paramsStr = Arrays.stream(ctor.getParameters())
                                    .map(param -> {
                                       var paramTypeName = param.getType().getSimpleName();
                                       var paramName = param.getName();
                                       return paramTypeName + blank + paramName;
                                    })
                                    .collect(Collectors.joining(comma + blank));

                            // "\t" + 可见修饰符 + 类名 + "(" + 参数 + ")" + "{}"
                            return tab + ifExistsThen.apply(modifierStr, modifierStr + blank)
                                    + className + leftBracket + paramsStr + rightBracket
                                    + blank + leftBrace + rightBrace;
                        })
                        .collect(Collectors.joining(newLine));

        // 类成员方法描述
        var methodsStr = Arrays.stream(c.getDeclaredMethods())
                .map(m -> {
                    var modifierStr = Modifier.toString(m.getModifiers());
                    var returnTypeName = m.getReturnType().getSimpleName();
                    var methodName = m.getName();
                    var paramsStr = Arrays.stream(m.getParameters())
                            .map(param -> {
                                var paramTypeName = param.getType().getSimpleName();
                                var paramName = param.getName();
                                return paramTypeName + blank + paramName;
                            })
                            .collect(Collectors.joining(comma + blank));

                    // "\t" + 可见修饰符 + 返回值类型 + 方法名 + "(" + 参数 + ")" + "{}"
                    return tab + ifExistsThen.apply(modifierStr, modifierStr + blank)
                            + returnTypeName + blank + methodName + leftBracket + paramsStr + rightBracket
                            + blank + leftBrace + rightBrace;
                }).collect(Collectors.joining(newLine));

        return res
                // 类修饰符
                .append(ifExistsThen.apply(classModifier, classModifier + blank))
                // "class" + 类名
                .append(ifExistsThen.apply(classSymbol, classSymbol + blank)).append(className).append(blank)
                // "extends" + 父类名
                .append(ifExistsThen.apply(superClassName, extendsSymbol + blank + superClassName + blank))
                // "implements" + 所有实现的接口名
                .append(ifExistsThen.apply(implInterfaces, implSymbol + blank + implInterfaces + blank))
                // "{" + 换行
                .append(leftBrace).append(newLine)
                // 类成员变量
                .append(ifExistsThen.apply(fieldsStr, fieldsStr + newLine + newLine))
                // 构造方法
                .append(ifExistsThen.apply(ctorsStr, ctorsStr + newLine + newLine))
                // 成员方法
                .append(ifExistsThen.apply(methodsStr, methodsStr + newLine))
                // "}"
                .append(rightBrace)
                .toString();
    }

    /**
     * 反编译类
     * @param className 被反编译的类的类名
     * @return 反编译输出的字符串
     * @throws Exception 异常
     * @apiNote <pre>{@code
     * // 反编译String类
     * String res = ClassDecompile.decompile("java.lang.String");
     * }</pre>
     */
    public static String decompile(String className) throws Exception {
        return decompile(Class.forName(className));
    }
}
