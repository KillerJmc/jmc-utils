package com.jmc.lang;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 增强版switch，优点如下：<br>
 * 1. 匹配值不限制是常量，可匹配自定义对象（针对JDK版本 < 21） <br>
 * 2. 支持复杂的匹配条件（Predicate） <br>
 *
 * @apiNote <pre>{@code
 * // 使用Switch进行复杂条件匹配
 *  String res = Switch.of(obj)
 *      .caseObj("你好", s -> "是字符串，值为：" + s)
 *      .caseType(Integer.class, i -> "是整数，值为：" + i)
 *      .caseType(Employee.class, emp -> "是职工对象，职工姓名：" + stu.getName())
 *      .caseWhen((Student stu) -> stu.getAge() < 18, stu -> "是学生对象，年龄小于18，学生信息：" + stu)
 *      .caseNull("为空对象")
 *      .orElse("没命中默认值");
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

    // region construct

    /**
     * 获取Switch实例
     * @param matchObj 匹配对象
     * @return Switch实例对象
     */
    public static Switch of(Object matchObj) {
        var instance = new Switch();
        instance.matchObj = matchObj;
        return instance;
    }

    /**
     * 获取Switch实例
     * @param matchObjSupplier 匹配对象获取函数
     * @return Switch实例对象
     */
    public static Switch of(FunctionalInterfaces.CheckedSupplier<Object> matchObjSupplier) {
        var instance = new Switch();
        if (Objects.nonNull(matchObjSupplier)) {
            instance.matchObj = Tries.tryGet(matchObjSupplier);
        }
        return instance;
    }

    // endregion

    // region match by object

    /**
     * Switch匹配函数：匹配对象和匹配值相等就记录返回值
     * @param matchObj 匹配对象
     * @param returnObj 返回值对象
     * @return Switch对象本身
     */
    public Switch caseObj(Object matchObj, Object returnObj) {
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
    public <T> Switch caseObj(T matchObj, FunctionalInterfaces.CheckedFunction<T, Object> action) {
        if (Objects.equals(matchObj, this.matchObj)) {
            this.returnObj = Tries.tryGet(() -> action.apply(matchObj));
        }
        return this;
    }

    /**
     * Switch匹配函数：匹配对象和匹配值相等就执行函数
     * @param matchObj 匹配对象
     * @param action 返回值函数
     * @return Switch对象本身
     */
    public Switch caseObj(Object matchObj, FunctionalInterfaces.CheckedRunnable action) {
        if (Objects.equals(matchObj, this.matchObj)) {
            Tries.tryRun(action);
            // 赋值一个空对象
            this.returnObj = new Object();
        }
        return this;
    }

    // endregion

    // region match by type

    /**
     * Switch匹配函数：类型匹配就记录返回值
     * @param matchType 匹配类型
     * @param returnObj 返回值对象
     * @return Switch对象本身
     * @param <T> 匹配的类型
     */
    public <T> Switch caseType(Class<T> matchType, Object returnObj) {
        if (Objects.nonNull(this.matchObj) && this.matchObj.getClass().isAssignableFrom(matchType)) {
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
    public <T> Switch caseType(Class<T> matchType, FunctionalInterfaces.CheckedFunction<T, Object> function) {
        if (Objects.nonNull(this.matchObj) && this.matchObj.getClass().isAssignableFrom(matchType)) {
            this.returnObj = Tries.tryGet(() -> function.apply((T) this.matchObj));
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
    public <T> Switch caseType(Class<T> matchType, FunctionalInterfaces.CheckedRunnable action) {
        if (Objects.nonNull(this.matchObj) && this.matchObj.getClass().isAssignableFrom(matchType)) {
            Tries.tryRun(action);
            // 赋值一个空对象
            this.returnObj = new Object();
        }
        return this;
    }

    // endregion

    // region match by condition

    /**
     * Switch匹配函数：匹配断言函数为true就记录返回值
     * @param matchFunc 匹配断言函数
     * @param returnObj 返回值对象
     * @return Switch对象本身
     * @param <T> 校验对象类型
     */
    @SuppressWarnings("unchecked")
    public <T> Switch caseWhen(Predicate<T> matchFunc, Object returnObj) {
        Tries.tryRunOrCapture(() -> {
            if (Objects.nonNull(this.matchObj) && matchFunc.test((T) this.matchObj)) {
                this.returnObj = returnObj;
            }
        }).ifPresent(ex -> {
            // 只忽略转化异常
            if (!(ex instanceof ClassCastException)) {
                throw new RuntimeException(ex);
            }
        });

        return this;
    }

    /**
     * Switch匹配函数：匹配断言函数为true就执行函数记录返回值
     * @param matchFunc 匹配断言函数
     * @param function 返回值函数
     * @return Switch对象本身
     */
    @SuppressWarnings("unchecked")
    public <T> Switch caseWhen(Predicate<T> matchFunc, FunctionalInterfaces.CheckedFunction<T, Object> function) {
        Tries.tryRunOrCapture(() -> {
            if (Objects.nonNull(this.matchObj) && matchFunc.test((T) this.matchObj)) {
                this.returnObj = Tries.tryGet(() -> function.apply((T) this.matchObj));
            }
        }).ifPresent(ex -> {
            // 只忽略转化异常
            if (!(ex instanceof ClassCastException)) {
                throw new RuntimeException(ex);
            }
        });
        return this;
    }

    /**
     * Switch匹配函数：匹配断言函数为true就执行函数
     * @param matchFunc 匹配断言函数
     * @param action 执行函数
     * @return Switch对象本身
     */
    @SuppressWarnings("unchecked")
    public <T> Switch caseWhen(Predicate<T> matchFunc, FunctionalInterfaces.CheckedRunnable action) {
        Tries.tryRunOrCapture(() -> {
            if (Objects.nonNull(this.matchObj) && matchFunc.test((T) this.matchObj)) {
                Tries.tryRun(action);
                // 赋值一个空对象
                this.returnObj = new Object();
            }
        }).ifPresent(ex -> {
            // 只忽略转化异常
            if (!(ex instanceof ClassCastException)) {
                throw new RuntimeException(ex);
            }
        });
        return this;
    }

    // endregion

    // region match null

    /**
     * Switch匹配函数：匹配对象为null就记录返回值
     * @param returnObj 返回值对象
     * @return Switch对象本身
     */
    public Switch caseNull(Object returnObj) {
        if (Objects.isNull(this.matchObj)) {
            this.returnObj = returnObj;
        }
        return this;
    }


    /**
     * Switch匹配函数：匹配对象为null就执行函数
     * @param action 返回值函数
     * @return Switch对象本身
     */
    public Switch caseNull(FunctionalInterfaces.CheckedRunnable action) {
        if (Objects.isNull(this.matchObj)) {
            Tries.tryRun(action);
            // 赋值一个空对象
            this.returnObj = new Object();
        }
        return this;
    }

    // endregion

    // region terminal operations

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
    public <R> R orElse(FunctionalInterfaces.CheckedSupplier<R> supplier) {
        R result = getResult();
        return result != null ? result : Tries.tryGet(supplier);
    }

    /**
     * Switch返回函数：返回匹配结果，如果匹配不上就执行代码片段
     * @param action 备选值
     * @param <R> 匹配结果类型
     */
    public <R> void orElseRun(FunctionalInterfaces.CheckedRunnable action) {
        if (getResult() == null) {
            Tries.tryRun(action);
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
     */
    public <R> R orElseThrow(Supplier<? extends RuntimeException> exceptionSupplier) {
        R result = getResult();
        if (result == null) {
            throw exceptionSupplier.get();
        }
        return result;
    }

    // endregion

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
