[中文](README.zh.md) &nbsp; | &nbsp; English

# jmc-utils

## Description

Providing a great number of utility classes to simplify the daily development. ([API Docs](https://killerjmc.github.io/jmc-utils/docs))

| Feature                                              | Descripton                                                   |
| :--------------------------------------------------- | :----------------------------------------------------------- |
| **File Operations (Files)**                          | Operations on files: copy, delete, compress, search, file encoding, etc. |
| **Operator Overloading (Operator)**                  | Provides operator overloading for classes!                   |
| **Default Arguments (@DefaultArg)**                  | Support for specifying default arguments in constructor methods, member methods and static methods. |
| **Function Reference (Func)**                        | Bind any method reference (supports **binding** parameters at the same time), and can be called for execution. |
| **Pointer Type (Pointer)**                           | Turn any type (including **basic data type**) into a pointer which can be passed to the method. |
| **Generic Array (Array)**                            | Implement general operations on arrays of any type (including arrays of **basic data types**). |
| **High-precision Expression Calculation (ExactExp)** | Provides **BigDecimal** level high-precision expression calculations. |
| **Reflection Enhancement (Reflects)**                | Provides useful methods for reflection, class loading, serialization, and the classpath. |
| **Runtime Agent Loading (JavaAgent)**                | Provides the runtime loading method of **Java Agent**.       |
| **Exception Handling Enhancement (Tries)**           | Handle exceptions more gracefully, **avoid** writing try blocks. |
| **String Enhancement (Strs)**                        | Provides a huge of string operation methods.                 |
| **Command Execution (Run)**                          | Simplify command line execution.                             |
| **Front-end and Back-end Interaction (R)**           | Provides a common return data entity type.                   |
| **Mathematical Operations (Maths)**                  | Provides prime number arithmetic and factorial arithmetic.   |
| **Binary Conversion (Binary)**                       | Provides methods for the conversion between binary strings and numbers. |
| **Time Conversion (Time)**                           | Provides methods for the conversion between **LocalDateTime**, **millisecond value**, **formatted time string**. |
| **Random Number Generation (Rand)**                  | Provides a method for generating random numbers in a specified range. |
| **Running Timers (Timers)**                          | Provides **second**, **millisecond**, **nanosecond** level code running timers. |
| **Tuple Sequence (Tuple)**                           | Provides a sequence combining values of **different types**. |
| **Builtin Functions (Builtins)**                     | Provides a serials static builtin functions to simplify method invocation in **JDK21**. |



## Usage

+ Gradle

  1. Add repository

     ```groovy
     maven { url "https://killerjmc.github.io/jmc-utils/repo" }
     ```

  2. Add dependency

     ```groovy
     implementation "com.jmc:jmc-utils:latestVersion"
     ```


+ Maven

  1. Add repository

     ```xml
     <repository>
          <id>jmc-utils-repo</id>
          <url>https://killerjmc.github.io/jmc-utils/repo</url>
     </repository>
     ```

  2. Add dependency

     ```xml
     <dependency>
         <groupId>com.jmc</groupId>
         <artifactId>jmc-utils</artifactId>
         <version>latest-version</version>
     </dependency>
     ```
