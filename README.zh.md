中文 &nbsp; | &nbsp; [English](README.md)

# jmc-utils

## 介绍

提供大量的工具类，简化日常开发。（[API文档](https://killerjmc.github.io/jmc-utils/docs)）

| 特性                             | 介绍                                                         |
| :------------------------------- | :----------------------------------------------------------- |
| **文件操作（Files）**            | 封装对文件的操作：复制、删除、压缩、查找、文件编码等。       |
| **运算符重载（Operator）**       | 提供类级别的运算符重载！                                     |
| **默认参数（@DefaultArg）**      | 支持指定构造方法、成员方法和静态方法级别的默认参数。         |
| **函数引用（Func）**             | 绑定任意方法引用（支持同时**绑定**参数），并可调用执行。     |
| **指针（Pointer）**              | 将任意类型（包括**基本数据类型**）变成指针传入方法。         |
| **通用数组（Array）**            | 实现对任意类型数组的通用操作（包括**基本数据类型**数组）。   |
| **高精度表达式计算（ExactExp）** | 提供**BigDecimal**级别的高精度表达式计算。                   |
| **反射增强（Reflects）**         | 提供反射、类加载、序列化和类路径的操作方法。                 |
| **运行时Agent加载（JavaAgent)**  | 提供**Java Agent**的运行时加载方法。                         |
| **异常处理增强（Tries）**        | 更加优雅的处理异常，**避免**写try块。                        |
| **字符串增强（Strs）**           | 提供一系列字符串增强方法。                                   |
| **命令执行（Run）**              | 简化命令行执行命令。                                         |
| **前后端交互（R）**              | 提供通用返回数据实体类型。                                   |
| **数学运算（Maths）**            | 提供质数算法和阶乘算法。                                     |
| **二进制转换（Binary）**         | 提供二进制字符串到数字的相互转换的方法。                     |
| **时间转换（Time）**             | 提供**LocalDateTime**、**毫秒值**、**格式化时间字符串**三者间的转换。 |
| **随机数生成（Rand）**           | 提供产生指定范围随机数的方法。                               |
| **计时器（Timers）**             | 提供**秒**、**毫秒**、**纳秒**级别的代码运行计时器。         |
| **元组（Tuple）**                | 提供一个存放**不同类型值**的序列。                           |




## 用法

+ Gradle

  1. 添加仓库

     ```groovy
     maven { url "https://killerjmc.github.io/jmc-utils/repo" }
     ```

  2. 添加依赖

     ```groovy
     implementation "com.jmc:jmc-utils:latestVersion"
     ```


+ Maven

  1. 添加仓库

     ```xml
     <repository>
         <id>jmc-utils-repo</id>
         <url>https://killerjmc.github.io/jmc-utils/repo</url>
     </repository>
     ```

  2. 添加依赖

     ```xml
     <dependency>
         <groupId>com.jmc</groupId>
         <artifactId>jmc-utils</artifactId>
         <version>latest-version</version>
     </dependency>
     ```
