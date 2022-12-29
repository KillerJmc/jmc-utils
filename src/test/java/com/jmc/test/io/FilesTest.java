package com.jmc.test.io;

import com.jmc.io.Files;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;


public class FilesTest {
    // 获取类目录下的文件全路径
    private String getFilePath(String fileName) {
        return "src/test/java/"
                + FilesTest.class.getPackageName().replace(".", "/")
                + "/"
                + fileName;
    }

    @Test
    public void readAndOutTest() {
        var path = getFilePath("a.txt");

        // 输出字符串到文件（默认非追加方式）
        Files.out("hello!", path);

        // 以追加模式输入
        Files.out("hello!", path, true);

        // 把文件内容读取到字符串
        System.out.println(Files.read(path));

        // 删除文件
        Files.delete(path);
    }

    @Test
    public void linesTest() {
        var s = """
        abc
        def
        gih
        jkl
        nmo
        opr
        stu
        vwx
        yz""";
        var path = getFilePath("a.txt");
        Files.out(s, path);

        Files.lines(path)
                .forEach(t -> System.out.printf("->%s<-\n", t));

        Files.delete(path);
    }

    @Test
    public void copyTest() {
        // 在./test文件夹中创建文件
        var path = getFilePath("test/a.txt");

        Files.out("hello", path);

        // 把 test/a.txt 复制到 .
        Files.copy(path, getFilePath(""));

        // 查看结果
        System.out.println(Files.read(getFilePath("a.txt")));

        Files.delete(getFilePath("a.txt"), getFilePath("test"));
    }

    @Test
    public void moveAndRenameTest() {
        var path = getFilePath("test/a.txt");
        // 在./test文件夹中创建新文件
        Files.createFile(path);

        // 把文件重命名为b.txt
        Files.rename(path, "b.txt");
        Assert.assertFalse(Files.exists(path));

        var newPath = getFilePath("test/b.txt");
        // 成功重命名
        Assert.assertTrue(Files.exists(newPath));

        // 把 test/b.txt 移动到 .
        Files.move(newPath, getFilePath(""));

        // 原文件被移动，不存在
        Assert.assertFalse(Files.exists(newPath));
        // 成功移动
        Assert.assertTrue(Files.exists(getFilePath("b.txt")));

        Files.delete(getFilePath("b.txt"), getFilePath("test"));
    }

    @Test
    public void zipAndUnzipTest() {
        var path = getFilePath("a.txt");
        Files.out("666", path);

        // 以非储存模式压缩到.
        Files.zip(path, false);
        Files.delete(path);

        var zipPath = getFilePath("a.txt.zip");
        Files.unzip(zipPath);
        System.out.println(Files.read(path));

        Files.delete(path, zipPath);
    }

    @Test
    public void fileAddrTest() {
        // 显示文件/文件夹信息
        System.out.println(Files.fileInfo("."));

        // 把字节长度变为人类可读的长度
        System.out.println(Files.lengthFormatter(100000));

        var path = getFilePath("a.txt");
        Files.out("666", path);

        // 获取文件编码
        System.out.println(Files.getEncoding(path).orElseThrow());

        // 设置文件编码
        Files.setEncoding(path, StandardCharsets.UTF_8, StandardCharsets.ISO_8859_1);
        System.out.println(Files.read(path));

        Files.delete(path);
    }

    @Test
    public void findTest() {
        // 找到所有.下的.java文件并输出其中3个
        Files.findAll(".", f -> f.getName().endsWith(".java"))
                .files()
                .stream()
                .limit(3)
                .forEach(System.out::println);

        // 找到所有.下的含有java的文件/文件夹并打印仔细信息
        System.out.println(Files.findInfo(".", "java"));

        // 找出.下的任意一个.java文件
        System.out.println(Files.findAny(".", ".java"));
    }

    @Test
    public void treeTest() {
        // 输出深度为3，文件最小为30kb的.文件夹的文件树
        System.out.println(Files.tree(".", 3, 0.03));

        // 输出全文件树
        System.out.println(Files.wholeTree("."));

        // 输出单层文件树
        System.out.println(Files.singleTree("."));

        // 输出普通文件树
        System.out.println(Files.normalTree("."));
    }
}
