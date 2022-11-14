package com.jmc.test.net;

import com.jmc.lang.Objs;
import com.jmc.lang.Outs;
import com.jmc.lang.Threads;
import com.jmc.lang.Tries;
import com.jmc.lang.ref.Func;
import com.jmc.net.HttpStatus;
import com.jmc.net.R;
import org.junit.Assert;
import org.junit.Test;

public class RTest {
    @Test
    public void okTest() {
        // 用于网络流（直接返回给前端）
        var r = R.ok()
                .msg("一切正常")
                .data(666);

        // 没有打印内容，保护其中信息
        System.out.println(r);

        // 正常获取和打印对象
        var data = r.getData();
        System.out.println(data);
    }

    @Test
    public void okWithData() {
        // 请求成功
        var r = R.ok("成功");
        // 打印包含数据
        System.out.println("data: " + r.getData());
    }

    @Test
    public void errTest() {
        // 请求失败
        var r = R.error()
                .msg("参数欠缺！")
                .build();

        // 获取数据并进行异常处理
        var data = Tries.tryReturnsT(r::getData, System.err::println);

        // 返回数据应该为空
        Assert.assertNull(data);
    }

    @Test
    public void errWithMsg() {
        // 请求失败
        var r = R.error("错误信息");
        // 获取错误信息
        var errorMsg = r.getMessage();
        System.err.println(errorMsg);
    }

    @Test
    public void notFoundAndForbiddenTest() {
        // 找不到页面
        var notFoundR = R.notFound().build();
        Assert.assertEquals(notFoundR.getCode(), HttpStatus.NOT_FOUND);

        // 禁止访问
        var forbiddenR = R.forbidden().build();
        Assert.assertEquals(forbiddenR.getCode(), HttpStatus.FORBIDDEN);
    }

    @Test
    public void handleError() {
        // 请求成功
        var r = R.ok()
                .data(new Object());

        // 如果请求失败
        if (r.failed()) {
            // 在这可以返回这个R，承载错误信息
            return;
        }

        // 继续处理数据
        var data = r.getData();
        Assert.assertNotNull(data);
    }

    private record Student(String name, String password) {}

    @Test
    public void streamTest() {
        var testRegister = Func.of((Student s) -> {
            // 调用注册方法
            var res = register(s);
            if (res.failed()) {
                System.err.println("前端返回错误信息: " + res.getMessage());
                // 防止System.out和System.err混用导致顺序错乱
                Threads.sleep(1);
            } else {
                System.out.println("前端返回成功数据: " + res.getData());
            }
            Outs.newLine();
        });

        // 测试不同的注册请求
        testRegister.invoke(new Student("", ""));
        testRegister.invoke(new Student("Jmc", "123"));
        testRegister.invoke(new Student("Lucy", "123?"));
        testRegister.invoke(new Student("Lucy", "123"));
        testRegister.invoke(new Student("Jenny", "123"));
    }

    private R<String> register(Student s) {
        return R.stream()
                // 检查学生姓名和密码是否为空
                .check(() -> checkEmpty(s))
                // 学生姓名是否重复
                .check(() -> existName(s.name))
                // 检查密码是否非法
                .check(s.password.endsWith("?"), "密码非法")
                // 执行插入学生对象操作
                .exec(() -> insert(s))
                // 返回结果数据
                .build(() -> getResult(s));
    }

    private void checkEmpty(Student s) throws Exception {
        if (Objs.nullOrEmpty(s.name, s.password)) {
            throw new Exception("姓名或密码为空");
        }
    }

    private void existName(String name) throws Exception {
        if (name.equals("Jmc")) {
            throw new Exception("姓名重复");
        }
    }

    private void insert(Student s) {
        System.out.println("insert: 插入学生记录 -> " + s);
    }

    private String getResult(Student s) throws Exception {
        if ("Lucy".equals(s.name)) {
            throw new Exception("获取结果出错：名字不能为Lucy（莫须有罪名，模拟构建结果出错情况）");
        }
        return "注册成功，姓名为：" + s.name;
    }
}
