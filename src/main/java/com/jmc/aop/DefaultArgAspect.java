package com.jmc.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.ConstructorSignature;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * 使用AspectJ实现的默认参数切面（@DefaultArg）
 * @since 3.0
 * @author Jmc
 */
@Aspect
public class DefaultArgAspect {
    /**
     * 用于拦截成员方法和静态方法的切点
     */
    @Pointcut("execution(* *(.., @DefaultArg (*), ..))")
    public void onMethod() {}

    /**
     * 用于拦截构造方法的切点
     */
    @Pointcut("execution(*.new(.., @DefaultArg (*), ..))")
    public void onConstructor() {}

    /**
     * 默认参数AOP处理
     * @param joinPoint 切点
     * @return 方法返回值
     * @throws Throwable 异常
     */
    @Around("onMethod() || onConstructor()")
    public Object defaultArg(ProceedingJoinPoint joinPoint) throws Throwable {
        // 方法签名
        var signature = joinPoint.getSignature();
        // 方法参数
        var args = joinPoint.getArgs();
        // 方法参数类型列表
        Parameter[] params;
        // 方法注解列表
        Annotation[][] annotations;

        // 分别解决是成员/静态方法和构造方法的情况
        if (signature instanceof MethodSignature methodSignature) {
            var method = methodSignature.getMethod();
            params = method.getParameters();
            annotations = method.getParameterAnnotations();
        } else if (signature instanceof ConstructorSignature ctorSignature) {
            var ctor = ctorSignature.getConstructor();
            params = ctor.getParameters();
            annotations = ctor.getParameterAnnotations();
        } else {
            throw new Error("不应该执行到这里！");
        }

        // 默认参数类型转换（包括数字类型，布尔类型和字符串）的函数集合（类型Class名称 -> 转换函数）
        var defaultTransferFunctionMap = new HashMap<String, Function<String, Object>>(Map.of(
                Byte.class.getName(), Byte::valueOf,
                Short.class.getName(), Short::valueOf,
                Integer.class.getName(), Integer::valueOf,
                Long.class.getName(), Long::valueOf,
                Float.class.getName(), Float::valueOf,
                Double.class.getName(), Double::valueOf,
                Character.class.getName(), s -> s.charAt(0),
                Boolean.class.getName(), Boolean::valueOf,
                BigInteger.class.getName(), BigInteger::new,
                BigDecimal.class.getName(), BigDecimal::new
        ));
        defaultTransferFunctionMap.put(String.class.getName(), s -> s);

        // 只有参数为null并且存在@DefaultArg注解时才填充默认参数
        for (int i = 0; i < args.length; i++) {
            // 当参数为null
            if (args[i] == null) {
                // 遍历参数上的每一个注解
                for (var anno : annotations[i]) {
                    var paramClassName = params[i].getType().getName();
                    var annoClassName = anno.annotationType().getName();

                    // 当注解为@DefaultArg注解
                    if (DefaultArg.class.getName().equals(annoClassName)) {
                        var defaultArgStrValue = ((DefaultArg) anno).value();

                        // 如果参数类型在默认参数转换函数集合中能找到，那就直接转换并退出循环
                        if (defaultTransferFunctionMap.containsKey(paramClassName)) {
                            // 进行默认参数类型转换并填充进对应参数
                            args[i] = defaultTransferFunctionMap.get(paramClassName).apply(defaultArgStrValue);
                            break;
                        }

                        // 否则尝试读取用户定义的默认参数类型转换类
                        var transferClass = ((DefaultArg) anno).transferClass();

                        var ctor = transferClass.getDeclaredConstructor();
                        ctor.setAccessible(true);

                        // 进行默认参数类型转换并填充进对应参数
                        args[i] = ctor.newInstance().transfer(defaultArgStrValue);
                        break;
                    }
                }
            }
        }

        // 执行方法并返回结果
        return joinPoint.proceed(args);
    }
}
