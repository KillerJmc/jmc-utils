package com.jmc.aop;

/**
 * 默认参数转换类 <br>
 * 用于将默认参数（字符串类型）转换成对应参数类型 <br>
 * 使用时需要继承该类并重写transfer方法
 * @apiNote <pre>{@code
 * // 复杂类型需要指定转换类
 * static String getCharsetName(@DefaultArg(value = "UTF-8", transferClass = StringToCharset.class) Charset c) {
 *     return c.displayName();
 * }
 *
 * // 转化类
 * static class StringToCharset extends DefaultArgTransfer<Charset> {
 *     @Override
 *     public Charset transfer(String defaultArg) {
 *         return Charset.forName(defaultArg);
 *     }
 * }
 * }</pre>
 * @since 3.0
 * @author Jmc
 * @param <T> 对应参数转换结果的类型
 */
public class DefaultArgTransfer<T> {
    /**
     * 参数转换方法
     * @param defaultArg 注入的默认参数
     * @return 对应参数类型的转换结果
     */
    public T transfer(String defaultArg) {
        throw new UnsupportedOperationException("No implementation found for transfer method!");
    }
}
