package com.jmc.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 默认参数注解
 * @apiNote <pre>{@code
 * // 开启默认参数特性（必须在使用类加载前调用！）
 * DefaultArgsFeature.enable();
 *
 * // 在另一个类中定义默认参数方法
 * class Other {
 *     static long add(@DefaultArg("3") Long a, @DefaultArg("4") Long b) {
 *         return a + b;
 *     }
 * }
 *
 * // 调用默认参数方法，结果是7
 * Assert.assertEquals(7, Other.add(null, null));
 * }</pre>
 * @since 3.0
 * @author Jmc
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface DefaultArg {
    /**
     * 默认参数的值
     * @return 默认参数值
     */
    String value();

    /**
     * 如果默认参数是非数字类型（不包括String） <br>
     * 需要传入转换默认参数（字符串）到指定参数类型的转换类
     * @return 转换类的Class对象
     * @see DefaultArgTransfer
     */
    Class<? extends DefaultArgTransfer> transferClass() default DefaultArgTransfer.class;
}
