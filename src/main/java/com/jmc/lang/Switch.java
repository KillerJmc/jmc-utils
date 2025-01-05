package com.jmc.lang;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 增强版switch：匹配值不限制是常量，并支持复杂的匹配条件
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
     * 返回值函数：将会在获取结果时被调用并返回
     */
    private Callable<Object> returnAction;

    /**
     * 禁止直接实例化
     */
    private Switch() {}

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
     * 检查是否是非法匹配：匹配到多个条件
     * @param isAddingReturnObj 是否正在添加返回值对象
     * @param isAddingReturnAction 是否正在添加返回值函数
     */
    private void checkIllegalMatch(boolean isAddingReturnObj, boolean isAddingReturnAction) {
        var exception = new IllegalStateException("Switch: 匹配到了多个条件！");

        // 返回值对象和返回值函数此时均不为空，就匹配到多个条件了
        if (this.returnObj != null && this.returnAction != null) {
            throw exception;
        }

        // 正在添加返回值对象但此时它不为空，就匹配到多个条件了
        if (isAddingReturnObj && this.returnObj != null) {
            throw exception;
        }

        // 正在添加返回值函数但此时它不为空，就匹配到多个条件了
        if (isAddingReturnAction && this.returnAction != null) {
            throw exception;
        }
    }

    /**
     * 获取Switch匹配结果
     * @return Switch匹配结果
     * @param <R> 匹配结果类型
     */
    @SuppressWarnings("unchecked")
    private <R> R getResult() {
        checkIllegalMatch(false, false);
        if (this.returnObj != null) {
            return (R) this.returnObj;
        }
        if (this.returnAction != null) {
            return (R) Tries.tryReturnsT(this.returnAction::call);
        }
        return null;
    }

    /**
     * Switch匹配函数：匹配对象和匹配值相等就记录返回值
     * @param matchObj 匹配对象
     * @param returnObj 返回值对象
     * @return Switch对象本身
     */
    public Switch when(Object matchObj, Object returnObj) {
        if (Objects.equals(matchObj, this.matchObj)) {
            checkIllegalMatch(true, false);
            this.returnObj = returnObj;
        }
        return this;
    }

    /**
     * Switch匹配函数：匹配对象和匹配值相等就记录返回值函数
     * @param matchObj 匹配对象
     * @param action 返回值函数
     * @return Switch对象本身
     */
    public Switch when(Object matchObj, Callable<Object> action) {
        if (Objects.equals(matchObj, this.matchObj)) {
            checkIllegalMatch(false, true);
            this.returnAction = action;
        }
        return this;
    }

    /**
     * Switch匹配函数：匹配断言函数为true就记录返回值
     * @param matchFunc 匹配断言函数
     * @param returnObj 返回值对象
     * @return Switch对象本身
     */
    @SuppressWarnings("unchecked")
    public <T> Switch when(Predicate<T> matchFunc, Object returnObj) {
        if (matchFunc.test((T) this.matchObj)) {
            checkIllegalMatch(true, false);
            this.returnObj = returnObj;
        }
        return this;
    }

    /**
     * Switch匹配函数：匹配断言函数为true就记录返回值函数
     * @param matchFunc 匹配断言函数
     * @param action 返回值函数
     * @return Switch对象本身
     */
    @SuppressWarnings("unchecked")
    public <T> Switch when(Predicate<T> matchFunc, Callable<Object> action) {
        if (matchFunc.test((T) this.matchObj)) {
            checkIllegalMatch(false, true);
            this.returnAction = action;
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
    public <R> R orElseGet(Supplier<R> supplier) {
        R result = getResult();
        return result != null ? result : supplier.get();
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
    public <R, E extends Throwable> R orElseThrow(Supplier<E> exceptionSupplier) throws E {
        R result = getResult();
        if (result == null) {
            throw exceptionSupplier.get();
        }
        return result;
    }
}
