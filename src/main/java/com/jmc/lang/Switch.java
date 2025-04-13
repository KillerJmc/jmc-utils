package com.jmc.lang;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 增强版switch：匹配值不限制是常量，并支持复杂的匹配条件
 * @apiNote <pre>{@code
 * // 使用Switch进行复杂条件匹配
 * int x = 10;
 * String res = Switch.match(x)
 *     .when((Integer t) -> t > 10, () -> "t大于10！")
 *     .when((Integer t) -> t < 10, () -> "t小于10！")
 *     .when((Integer t) -> t == 10, () -> "t等于10！")
 *     .orElse(null);
 * Assert.assertEquals("t等于10！", res);
 *
 * // 使用Switch匹配复杂对象
 * record Student(String name) {}
 *
 * var stuJmc = new Student("Jmc");
 * var stuJack = new Student("Jack");
 * var stuLucy = new Student("Lucy");
 *
 * String res = Switch.match(() -> new Student("Jmc"))
 *     .when((Student s) -> Objects.equals(s.name(), stuJack.name()), "是Student Jack")
 *     .when((Student s) -> Objects.equals(s.name(), stuJmc.name()), "是Student Jmc")
 *     .when((Student s) -> Objects.equals(s.name(), stuLucy.name()), "是Student Lucy")
 *     .orElseThrow(() -> new NoSuchElementException("匹配学生失败"));
 *
 * Assert.assertEquals("是Student Jmc", res);
 *
 * // 支持类型匹配
 * Object obj = "123";
 * String res = Switch.match(obj)
 *     .whenType(Integer.class, i -> "是Integer.class，值是：" + i)
 *     .whenType(String.class, s -> "是String.class，长度是：" + s.length())
 *     .whenType(Double.class, d -> "是Double.class，值是：" + d)
 *     .orElseThrow(() -> new NoSuchElementException("匹配类型失败"));
 *
 * Assert.assertEquals("是String.class，长度是：3", res);
 * }</pre>
 * @since 3.9
 * @author Jmc
 */
public class Switch {
    /**
     * 匹配值
     */
    private Object matchObj;

    /**
     * 返回值对象：将会在获取结果后返回
     */
    private Object returnObj;

    /**
     * 禁止直接实例化
     */
    private Switch() {}

    /**
     * 抛出异常的函数
     * @param <T> 函数参数类型
     * @param <R> 返回值类型
     */
    public interface CheckedFunction<T, R> {
        R apply(T t) throws Throwable;
    }

    /**
     * 获取Switch实例
     * @param matchObj 匹配对象
     * @return Switch实例对象
     */
    public static Switch match(Object matchObj) {
        var instance = new Switch();
        instance.matchObj = matchObj;
        return instance;
    }

    /**
     * 获取Switch实例
     * @param matchObjSupplier 匹配对象获取函数
     * @return Switch实例对象
     */
    public static Switch match(Tries.ReturnedThrowable<Object> matchObjSupplier) {
        var instance = new Switch();
        instance.matchObj = Tries.tryReturnsT(matchObjSupplier);
        return instance;
    }

    /**
     * Switch匹配函数：匹配对象和匹配值相等就记录返回值
     * @param matchObj 匹配对象
     * @param returnObj 返回值对象
     * @return Switch对象本身
     */
    public Switch when(Object matchObj, Object returnObj) {
        if (Objects.equals(matchObj, this.matchObj)) {
            this.returnObj = returnObj;
        }
        return this;
    }

    /**
     * Switch匹配函数：匹配对象和匹配值相等就执行函数记录返回值
     * @param matchObj 匹配对象
     * @param action 返回值函数
     * @return Switch对象本身
     */
    public Switch when(Object matchObj, Tries.ReturnedThrowable<Object> action) {
        if (Objects.equals(matchObj, this.matchObj)) {
            this.returnObj = Tries.tryReturnsT(action);
        }
        return this;
    }

    /**
     * Switch匹配函数：匹配对象和匹配值相等就执行函数
     * @param matchObj 匹配对象
     * @param action 返回值函数
     * @return Switch对象本身
     */
    public Switch when(Object matchObj, Tries.RunnableThrowsE action) {
        if (Objects.equals(matchObj, this.matchObj)) {
            Tries.tryThis(action);
            // 赋值一个空对象
            this.returnObj = new Object();
        }
        return this;
    }

    /**
     * Switch匹配函数：匹配断言函数为true就记录返回值
     * @param matchFunc 匹配断言函数
     * @param returnObj 返回值对象
     * @return Switch对象本身
     * @param <T> 校验对象类型
     */
    @SuppressWarnings("unchecked")
    public <T> Switch when(Predicate<T> matchFunc, Object returnObj) {
        if (matchFunc.test((T) this.matchObj)) {
            this.returnObj = returnObj;
        }
        return this;
    }

    /**
     * Switch匹配函数：匹配断言函数为true就执行函数记录返回值
     * @param matchFunc 匹配断言函数
     * @param function 返回值函数
     * @return Switch对象本身
     */
    @SuppressWarnings("unchecked")
    public <T> Switch when(Predicate<T> matchFunc, CheckedFunction<T, Object> function) {
        if (matchFunc.test((T) this.matchObj)) {
            this.returnObj = Tries.tryReturnsT(() -> function.apply((T) this.matchObj));
        }
        return this;
    }

    /**
     * Switch匹配函数：匹配断言函数为true就执行函数
     * @param matchFunc 匹配断言函数
     * @param action 执行函数
     * @return Switch对象本身
     */
    @SuppressWarnings("unchecked")
    public <T> Switch when(Predicate<T> matchFunc, Tries.RunnableThrowsE action) {
        if (matchFunc.test((T) this.matchObj)) {
            Tries.tryThis(action);
            // 赋值一个空对象
            this.returnObj = new Object();
        }
        return this;
    }

    /**
     * Switch匹配函数：类型匹配就记录返回值
     * @param matchType 匹配类型
     * @param returnObj 返回值对象
     * @return Switch对象本身
     * @param <T> 匹配的类型
     */
    public <T> Switch whenType(Class<T> matchType, Object returnObj) {
        if (this.matchObj.getClass().isAssignableFrom(matchType)) {
            this.returnObj = returnObj;
        }
        return this;
    }

    /**
     * Switch匹配函数：类型匹配就执行函数记录返回值
     * @param matchType 匹配类型
     * @param function 返回值函数
     * @return Switch对象本身
     * @param <T> 匹配的类型
     */
    @SuppressWarnings("unchecked")
    public <T> Switch whenType(Class<T> matchType, CheckedFunction<T, Object> function) {
        if (this.matchObj.getClass().isAssignableFrom(matchType)) {
            this.returnObj = Tries.tryReturnsT(() -> function.apply((T) this.matchObj));
        }
        return this;
    }

    /**
     * Switch匹配函数：类型匹配就执行函数
     * @param matchType 匹配类型
     * @param action 返回值函数
     * @return Switch对象本身
     * @param <T> 匹配的类型
     */
    public <T> Switch whenType(Class<T> matchType, Tries.RunnableThrowsE action) {
        if (this.matchObj.getClass().isAssignableFrom(matchType)) {
            Tries.tryThis(action);
            // 赋值一个空对象
            this.returnObj = new Object();
        }
        return this;
    }

    /**
     * Switch匹配函数：匹配对象为null就记录返回值
     * @param returnObj 返回值对象
     * @return Switch对象本身
     */
    public Switch whenNull(Object returnObj) {
        if (Objects.isNull(this.matchObj)) {
            this.returnObj = returnObj;
        }
        return this;
    }

    /**
     * Switch匹配函数：匹配对象为null就执行函数记录返回值
     * @param action 返回值函数
     * @return Switch对象本身
     */
    public Switch whenNull(Tries.ReturnedThrowable<Object> action) {
        if (Objects.isNull(this.matchObj)) {
            this.returnObj = Tries.tryReturnsT(action);
        }
        return this;
    }

    /**
     * Switch匹配函数：匹配对象为null就执行函数
     * @param action 返回值函数
     * @return Switch对象本身
     */
    public Switch whenNull(Tries.RunnableThrowsE action) {
        if (Objects.isNull(this.matchObj)) {
            Tries.tryThis(action);
            // 赋值一个空对象
            this.returnObj = new Object();
        }
        return this;
    }

    /**
     * Switch匹配函数：匹配对象不为null就记录返回值
     * @param returnObj 返回值对象
     * @return Switch对象本身
     */
    public Switch whenNonNull(Object returnObj) {
        if (Objects.isNull(this.matchObj)) {
            this.returnObj = returnObj;
        }
        return this;
    }

    /**
     * Switch匹配函数：匹配对象不为null就执行函数记录返回值
     * @param action 返回值函数
     * @return Switch对象本身
     */
    public Switch whenNonNull(Tries.ReturnedThrowable<Object> action) {
        if (Objects.nonNull(this.matchObj)) {
            this.returnObj = Tries.tryReturnsT(action);
        }
        return this;
    }

    /**
     * Switch匹配函数：匹配对象不为null就执行函数
     * @param action 返回值函数
     * @return Switch对象本身
     */
    public Switch whenNonNull(Tries.RunnableThrowsE action) {
        if (Objects.nonNull(this.matchObj)) {
            Tries.tryThis(action);
            // 赋值一个空对象
            this.returnObj = new Object();
        }
        return this;
    }

    /**
     * Switch返回函数：返回匹配结果，如果匹配不上就返回备选值
     * @param other 备选值
     * @return 返回匹配结果，如果匹配不上就返回备选值
     * @param <R> 匹配结果类型
     */
    public <R> R orElse(R other) {
        R result = getResult();
        return result != null ? result : other;
    }

    /**
     * Switch返回函数：返回匹配结果，如果匹配不上就返回备选值
     * @param supplier 备选值获取函数
     * @return 返回匹配结果，如果匹配不上就返回备选值
     * @param <R> 匹配结果类型
     */
    public <R> R orElseGet(Tries.ReturnedThrowable<R> supplier) {
        R result = getResult();
        return result != null ? result : Tries.tryReturnsT(supplier);
    }

    /**
     * Switch返回函数：返回匹配结果，如果匹配不上就执行代码片段
     * @param action 备选值
     * @param <R> 匹配结果类型
     */
    public <R> void orElseRun(Tries.RunnableThrowsE action) {
        if (getResult() == null) {
            Tries.tryThis(action);
        }
    }

    /**
     * Switch返回函数：返回匹配结果，如果匹配不上就抛出异常
     * @return 返回匹配结果，如果匹配不上就抛出异常
     * @param <R> 匹配结果类型
     */
    public <R> R orElseThrow() {
        R result = getResult();
        if (result == null) {
            throw new NoSuchElementException("Switch: 目标值是：%s，没有命中Switch！".formatted(this.matchObj));
        }
        return result;
    }

    /**
     * Switch返回函数：返回匹配结果，如果匹配不上就抛出异常
     * @param exceptionSupplier 异常获取函数
     * @return 返回匹配结果，如果匹配不上就抛出异常
     * @param <R> 匹配结果类型
     * @param <E> 异常类型
     */
    public <R> R orElseThrow(Supplier<? extends RuntimeException> exceptionSupplier) {
        R result = getResult();
        if (result == null) {
            throw exceptionSupplier.get();
        }
        return result;
    }

    /**
     * 获取Switch匹配结果
     * @return Switch匹配结果
     * @param <R> 匹配结果类型
     */
    @SuppressWarnings("unchecked")
    private <R> R getResult() {
        if (this.returnObj != null) {
            return (R) this.returnObj;
        }
        return null;
    }
}
