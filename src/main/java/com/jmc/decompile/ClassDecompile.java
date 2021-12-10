package com.jmc.decompile;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 反编译类
 * @since 1.0
 * @author Jmc
 */
@SuppressWarnings("unused")
public class ClassDecompile {

    private ClassDecompile() {}

    /**
     * 反编译类
     * @param className Class名称
     * @return 反编译输出的字符串
     * @throws Exception 异常
     */
    public static String decompile(String className) throws Exception {
        //实现类
        Class<?> c = Class.forName(className);

        //实现接口数组,参数数组,构造方法数组和方法数组
        Class<?>[] ins = c.getInterfaces();
        Field[] fs = c.getDeclaredFields();
        Constructor<?>[] cts = c.getConstructors();
        Method[] md = c.getDeclaredMethods();

        //用StringBuffer储存结果
        StringBuilder sb = new StringBuilder();

        //如果类修饰符不存在就不加空格
        String classModifier = "";
        if (c.getModifiers() == 0) {
            sb.append("class ").append(c.getSimpleName());
        } else {
            classModifier = Modifier.toString(c.getModifiers());
            if (classModifier.contains("interface")) {
                sb.append(classModifier.replace("abstract ", "")).append(" ").append(c.getSimpleName());
            } else {
                sb.append(classModifier).append(" class ").append(c.getSimpleName());
            }
        }
        //若不止继承Object则添加父类
        Class<?> superClass = c.getSuperclass();
        if (superClass != null && !"java.lang.Object".equals(superClass.getName())) {
            sb.append(" extends ").append(c.getSuperclass().getSimpleName());
        }
        //添加实现的接口
        if (ins.length != 0) {
            sb.append(" implements ");
            int count = 0;
            for (Class<?> in: ins) {
                //若为最后一个就不加逗号
                if (count == ins.length - 1) {
                    sb.append(in.getSimpleName());
                } else {
                    sb.append(in.getSimpleName()).append(", ");
                }
                count++;
            }
        }
        //加左大括号并换行
        sb.append(" {\n");

        //添加类中参数
        for (Field field : fs) {
            //每行添加tab
            sb.append("\t");
            //如果参数存在修饰符就添加
            if (field.getModifiers() != 0) {
                sb.append(Modifier.toString(field.getModifiers())).append(" ");
            }
            //添加参数属性(int, String等)
            sb.append(field.getType().getSimpleName()).append(" ");
            //添加参数名并分号换行结束
            sb.append(field.getName()).append(";\n");
        }
        //换行
        sb.append("\n");

        //添加构造方法
        for (Constructor<?> ct : cts) {
            //每行添加tab
            sb.append("\t");
            //如果存在修饰符就添加
            if (ct.getModifiers() != 0){
                sb.append(Modifier.toString(ct.getModifiers())).append(" ");
            }
            //添加构造方法名
            sb.append(c.getSimpleName()).append("(");
            //传入方法参数数组
            Class<?>[] cs = ct.getParameterTypes();
            //如果没有参数就结束
            if (cs.length == 0) {
                sb.append(") {}\n\n");
            } else {
                //计数的int
                int count = 0;
                //遍历方法参数类型数组
                for (Class<?> mdParaType : cs) {
                    //添加参数类型
                    sb.append(mdParaType.getSimpleName()).append(" ");

                    //添加参数名称
                    sb.append("p").append(count + 1);

                    //如果是最后一个参数
                    if (count == cs.length - 1) {
                        //不加逗号换行结束
                        sb.append(") {}\n\n");
                    } else {
                        //加逗号
                        sb.append(", ");
                    }
                    //计数器+1
                    count++;
                }
            }
        }

        //添加方法
        for (Method method : md) {
            //每行添加tab
            sb.append("\t");
            //如果方法存在修饰符就添加
            if (method.getModifiers() != 0){
                String methodModifier = Modifier.toString(method.getModifiers());
                if (classModifier.contains("interface")) {
                    methodModifier = methodModifier.replace(" abstract", "");
                }
                sb.append(methodModifier).append(" ");
            }           
            //添加返回值类型
            sb.append(method.getReturnType().getSimpleName()).append(" ");
            //添加方法名
            sb.append(method.getName()).append("(");
            //传入方法参数数组
            Class<?>[] cs = method.getParameterTypes();
            //如果没有参数就结束
            if (cs.length == 0) {
                sb.append(") {}\n\n");
            } else {
                //计数的int
                int count = 0;
                //遍历方法参数类型数组
                for (Class<?> mdParaType : cs) {
                    //添加参数类型
                    sb.append(mdParaType.getSimpleName()).append(" ");

                    //添加参数名称
                    sb.append("p").append(count + 1);

                    //如果是最后一个参数
                    if (count == cs.length - 1) {       
                        //不加逗号换行结束
                        sb.append(") {}\n\n");
                    } else {
                        //加逗号
                        sb.append(", ");
                    }
                    //计数器+1
                    count++;
                }
            }            
        }

        //删除最后一个换行符
        sb.delete(sb.length() - 1, sb.length());
        //添加右大括号
        sb.append("}");
        //输出StringBuffer
        return sb.toString();
    }

    /**
     * 反编译类
     * @param c 被反编译的类
     * @return 反编译输出的字符串
     * @throws Exception 异常
     */
    public static String decompile(Class<?> c) throws Exception {
        return decompile(c.getName());
    }
}
