package com.jmc.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 默认参数注解
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
     */
    Class<? extends DefaultArgTransfer> transferClass() default DefaultArgTransfer.class;
}
