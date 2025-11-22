package com.jmc.lang;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * String类扩展
 * @since 1.0
 * @author Jmc
 */
public class Strs {

    private Strs() {}

    /**
     * 通用字符串常量类
     * @since 3.9
     */
    public static final class Const {

        private Const() {}

        // region 最常用的符号
        /**
         * 空字符串
         */
        public static final String EMPTY = Whitespace.EMPTY;

        /**
         * 空格
         */
        public static final String SPACE = Whitespace.SPACE;

        /**
         * 制表符
         */
        public static final String TAB = Whitespace.TAB;

        /**
         * 换行符(LF)
         */
        public static final String LF = Whitespace.LF;

        /**
         * 单引号
         */
        public static final String SINGLE_QUOTE = Symbols.SINGLE_QUOTE;

        /**
         * 双引号
         */
        public static final String DOUBLE_QUOTE = Symbols.DOUBLE_QUOTE;

        /**
         * 正斜杠
         */
        public static final String SLASH = Symbols.SLASH;

        /**
         * 反斜杠
         */
        public static final String BACKSLASH = Symbols.BACKSLASH;

        /**
         * 点号
         */
        public static final String DOT = Symbols.DOT;

        /**
         * 逗号
         */
        public static final String COMMA = Symbols.COMMA;

        /**
         * 冒号
         */
        public static final String COLON = Symbols.COLON;

        /**
         * 下划线
         */
        public static final String UNDERSCORE = Symbols.UNDERSCORE;

        /**
         * & 符号
         */
        public static final String AMP = Symbols.AMP;

        /**
         * % 符号
         */
        public static final String PERCENT = Symbols.PERCENT;

        // endregion

        /**
         * 常用空白与控制字符
         */
        public static final class Whitespace {
            private Whitespace() {}

            /**
             * 空字符串
             */
            public static final String EMPTY = "";

            /**
             * 空格
             */
            public static final String SPACE = " ";

            /**
             * 制表符
             */
            public static final String TAB = "\t";

            /**
             * 换行符(LF)
             */
            public static final String LF = "\n";

            /**
             * 回车符(CR)
             */
            public static final String CR = "\r";

            /**
             * 回车换行符(CRLF)
             */
            public static final String CRLF = "\r\n";
        }

        /**
         * 常用标点和符号
         */
        public static final class Symbols {
            private Symbols() {}

            /**
             * 单引号
             */
            public static final String SINGLE_QUOTE = "'";

            /**
             * 双引号
             */
            public static final String DOUBLE_QUOTE = "\"";

            /**
             * 正斜杠
             */
            public static final String SLASH = "/";

            /**
             * 反斜杠
             */
            public static final String BACKSLASH = "\\";

            /**
             * 点号
             */
            public static final String DOT = ".";

            /**
             * 逗号
             */
            public static final String COMMA = ",";

            /**
             * 冒号
             */
            public static final String COLON = ":";

            /**
             * 分号
             */
            public static final String SEMICOLON = ";";

            /**
             * 问号
             */
            public static final String QUESTION = "?";

            /**
             * 感叹号
             */
            public static final String EXCLAMATION = "!";

            /**
             * 反引号
             */
            public static final String BACKTICK = "`";

            /**
             * 管道符
             */
            public static final String PIPE = "|";

            /**
             * 下划线
             */
            public static final String UNDERSCORE = "_";

            /**
             * 破折号/连字符
             */
            public static final String DASH = "-";

            /**
             * @ 符号
             */
            public static final String AT = "@";

            /**
             * # 符号
             */
            public static final String HASH = "#";

            /**
             * & 符号
             */
            public static final String AMP = "&";

            /**
             * % 符号
             */
            public static final String PERCENT = "%";

            /**
             * * 符号
             */
            public static final String STAR = "*";

            /**
             * ~ 符号
             */
            public static final String TILDE = "~";

            /**
             * ^ 符号
             */
            public static final String CARET = "^";

            /**
             * $ 符号
             */
            public static final String DOLLAR = "$";
        }

        /**
         * 常用括号符号
         */
        public static final class Brackets {
            private Brackets() {}

            /**
             * 左圆括号
             */
            public static final String LPAREN = "(";

            /**
             * 右圆括号
             */
            public static final String RPAREN = ")";

            /**
             * 左方括号
             */
            public static final String LBRACKET = "[";

            /**
             * 右方括号
             */
            public static final String RBRACKET = "]";

            /**
             * 左花括号
             */
            public static final String LBRACE = "{";

            /**
             * 右花括号
             */
            public static final String RBRACE = "}";

            /**
             * 左尖括号
             */
            public static final String LANGLE = "<";

            /**
             * 右尖括号
             */
            public static final String RANGLE = ">";
        }

        /**
         * 常用数学与逻辑操作符
         */
        public static final class Operators {
            private Operators() {}

            /**
             * 加号
             */
            public static final String PLUS = "+";

            /**
             * 减号
             */
            public static final String MINUS = "-";

            /**
             * 乘号
             */
            public static final String MULTIPLY = "*";

            /**
             * 除号
             */
            public static final String DIVIDE = "/";

            /**
             * 取模
             */
            public static final String MOD = "%";

            /**
             * 赋值符号
             */
            public static final String ASSIGN = "=";

            /**
             * 等于
             */
            public static final String EQ = "==";

            /**
             * 不等于
             */
            public static final String NE = "!=";

            /**
             * 大于
             */
            public static final String GT = ">";

            /**
             * 小于
             */
            public static final String LT = "<";

            /**
             * 大于等于
             */
            public static final String GE = ">=";

            /**
             * 小于等于
             */
            public static final String LE = "<=";

            /**
             * 逻辑与
             */
            public static final String AND = "&&";

            /**
             * 逻辑或
             */
            public static final String OR = "||";

            /**
             * Java lambda 箭头
             */
            public static final String ARROW = "->";

            /**
             * Java 方法引用 ::
             */
            public static final String DOUBLE_COLON = "::";
        }

        /**
         * 系统路径相关常量
         */
        public static final class Path {
            private Path() {}

            /**
             * 文件分隔符
             */
            public static final String FILE_SEPARATOR = File.separator;

            /**
             * 路径分隔符
             */
            public static final String PATH_SEPARATOR = File.pathSeparator;

            /**
             * 当前目录
             */
            public static final String CURRENT_DIR = ".";

            /**
             * 父目录
             */
            public static final String PARENT_DIR = "..";
        }

        /**
         * URL和网络相关常量
         */
        public static final class Url {
            private Url() {}

            /**
             * HTTP 协议
             */
            public static final String HTTP = "http";

            /**
             * HTTPS 协议
             */
            public static final String HTTPS = "https";

            /**
             * 协议分隔符 ://
             */
            public static final String COLON_SLASH_SLASH = "://";

            /**
             * URL 参数前缀 ?
             */
            public static final String PARAM_PREFIX = "?";

            /**
             * URL 参数等号 =
             */
            public static final String PARAM_EQUALS = "=";

            /**
             * URL 参数连接符 &
             */
            public static final String PARAM_AND = "&";
        }

        /**
         * 常用转义字符
         */
        public static final class Escape {
            private Escape() {}

            /**
             * 反斜杠 \
             */
            public static final String ESCAPE = "\\";

            /**
             * 双引号转义 \"
             */
            public static final String ESC_QUOTE = "\\\"";

            /**
             * 单引号转义 \'
             */
            public static final String ESC_SINGLE_QUOTE = "\\'";

            /**
             * 点号转义 \.
             */
            public static final String ESC_DOT = "\\.";

            /**
             * 星号转义 \*
             */
            public static final String ESC_STAR = "\\*";
        }

        /**
         * 常用文件扩展名
         */
        public static final class FileExt {
            private FileExt() {}

            /**
             * java文件 .java
             */
            public static final String JAVA = ".java";

            /**
             * class文件 .class
             */
            public static final String CLASS = ".class";

            /**
             * jar文件 .jar
             */
            public static final String JAR = ".jar";

            /**
             * 文本文件 .txt
             */
            public static final String TXT = ".txt";

            /**
             * JSON 文件 .json
             */
            public static final String JSON = ".json";

            /**
             * XML 文件 .xml
             */
            public static final String XML = ".xml";

            /**
             * YAML 文件 .yml
             */
            public static final String YAML = ".yml";

            /**
             * 属性文件 .properties
             */
            public static final String PROPERTIES = ".properties";

            /**
             * CSV 文件 .csv
             */
            public static final String CSV = ".csv";

            /**
             * 日志文件 .log
             */
            public static final String LOG = ".log";
        }

        /**
         * JSON相关常量
         */
        public static final class Json {
            private Json() {}

            /**
             * 布尔值 true
             */
            public static final String TRUE = "true";

            /**
             * 布尔值 false
             */
            public static final String FALSE = "false";

            /**
             * null 值
             */
            public static final String NULL = "null";

            /**
             * 空 JSON 对象 {}
             */
            public static final String EMPTY_OBJECT = "{}";

            /**
             * 空 JSON 数组 []
             */
            public static final String EMPTY_ARRAY = "[]";
        }
    }

    /**
     * 判断一个字符串是否由数字构成（正数，负数或小数）
     * @param src 字符串
     * @return 判断结果
     * @apiNote <pre>{@code
     * // 判断是否是数字（true）
     * var isNum = Strs.isNum("123");
     * // true
     * isNum = Strs.isNum("12.34");
     * // true
     * isNum = Strs.isNum("-234.789");
     * // false
     * isNum = Strs.isNum("123aa45");
     * }</pre>
     * @since 2.0
     */
    public static boolean isNum(String src) {
        // 如果src是空白的，返回假
        if (src.isBlank()) {
            return false;
        }

        // 如果只有一个字符
        if (src.length() == 1) {
            // 这个字符必须是数字
            return Character.isDigit(src.charAt(0));
        }

        // 把字符串正负号削减掉
        // 如果第一个字符不是数字
        if (!Character.isDigit(src.charAt(0))) {
            // 如果不是正负号，返回假
            if (!Objs.orEquals(src.charAt(0), '+', '-')) {
                return false;
            }
            // 去掉第一个正负号
            src = src.substring(1);

            // 如果此时第一个字符不是数字，返回假
            if (!Character.isDigit(src.charAt(0))) {
                return false;
            }
        }

        // 把字符串削减到小数点后第一位
        // 如果此时第一个字符是0
        if (src.charAt(0) == '0') {
            // 如果字符串读完了，返回真
            if (src.length() == 1) {
                return true;
            }

            // 否则第二个字符一定要是小数点
            if (src.charAt(1) != '.') {
                return false;
            }

            // 去掉0和小数点
            src = src.substring(2);
        } else {
            var hasDot = false;
            // 如果此时第一个字符是非0的数字
            for (int i = 0; i < src.length(); i++) {
                char c = src.charAt(i);
                // 如果当前字符不是数字
                if (!Character.isDigit(c)) {
                    // 如果当前字符是小数点
                    if (c == '.') {
                        // 截断字符串到小数点后
                        src = src.substring(i + 1);
                        // 标记存在小数点
                        hasDot = true;
                        break;
                    }
                    // 不是小数点或数字，返回假
                    return false;
                }
            }

            // 如果不包含小数点，返回真
            if (!hasDot) {
                return true;
            }
        }

        // 小数点后有且只有1至多个数字
        return !src.isBlank() && src.chars().mapToObj(t -> (char) t).allMatch(Character::isDigit);
    }

    /**
     * 获取字符串的子串
     * @param src 原字符串
     * @param start 开始的子串（不包含）
     * @param end 结束的子串（不包含）
     * @param fromIdx 从哪个下标开始查找
     * @return 结果子串
     * @since 1.7
     * @apiNote <pre>{@code
     * var s = "zxxx123yyy"
     * // 获取s中xxx和yyy之间的字串，从下标1开始匹配（结果：123）
     * var res = Strs.subExclusive(s, "xxx", "yyy", 1);
     * }</pre>
     */
    public static String subExclusive(String src, String start, String end, int fromIdx) {
        var startIdx = src.indexOf(start, fromIdx);

        var endIdx = src.indexOf(end, startIdx + start.length());
        return startIdx == -1 || endIdx == -1 ? src : src.substring(startIdx + start.length(), endIdx);
    }

    /**
     * 获取字符串的子串
     * @param src 原字符串
     * @param start 开始的子串（不包含）
     * @param fromIdx 从哪个下标开始查找
     * @return 结果子串
     * @since 1.7
     * @apiNote <pre>{@code
     * var s = "zxxx123"
     * // 获取s中xxx之后的字串，从下标1开始匹配（结果：123）
     * var res = Strs.subExclusive(s, "xxx", 1);
     * }</pre>
     */
    public static String subExclusive(String src, String start, int fromIdx) {
        var startIdx = src.indexOf(start, fromIdx);
        return startIdx == -1 ? src : src.substring(startIdx + start.length());
    }

    /**
     * 获取字符串的子串
     * @param src 原字符串
     * @param start 开始的子串（不包含）
     * @param end 结束的子串（不包含）
     * @return 结果子串
     * @apiNote <pre>{@code
     * var s = "xxx123yyy"
     * // 获取s中xxx和yyy之间的子串（结果：123）
     * var res = Strs.subExclusive(s, "xxx", "yyy");
     * }</pre>
     */
    public static String subExclusive(String src, String start, String end) {
        return subExclusive(src, start, end, 0);
    }

    /**
     * 获取字符串的子串
     * @param src 原字符串
     * @param start 开始的子串（不包含）
     * @return 结果子串
     * @apiNote <pre>{@code
     * var s = "xxx123"
     * // 获取s中xxx后面的子串（结果：123）
     * var res = Strs.subExclusive(s, "xxx");
     * }</pre>
     */
    public static String subExclusive(String src, String start) {
        return subExclusive(src, start, 0);
    }

    /**
     * 获取字符串的子串
     * @param src 原字符串
     * @param start 开始的子串（包含）
     * @param end 结束的子串（包含）
     * @param fromIdx 从哪个下标开始查找
     * @return 结果子串
     * @apiNote <pre>{@code
     * var s = "zx123y"
     * // 获取s中x和y之间的字串（包括x和y），从下标1开始匹配（结果：x123y）
     * var res = Strs.subInclusive(s, "x", "y", 1);
     * }</pre>
     * @since 1.7
     */
    public static String subInclusive(String src, String start, String end, int fromIdx) {
        int startIdx = src.indexOf(start, fromIdx);
        int endIdx = src.indexOf(end, startIdx + start.length());

        return startIdx == -1 || endIdx == -1 ? src : src.substring(startIdx, endIdx + end.length());
    }

    /**
     * 获取字符串的子串
     * @param src 原字符串
     * @param start 开始的子串（包含）
     * @param fromIdx 从哪个下标开始查找
     * @return 结果子串
     * @apiNote <pre>{@code
     * var s = "zxxx123"
     * // 获取s中xxx之后的字串（包括xxx），从下标1开始匹配（结果：xxx123）
     * var res = Strs.subInclusive(s, "xxx", 1);
     * }</pre>
     * @since 1.7
     */
    public static String subInclusive(String src, String start, int fromIdx) {
        var startIdx = src.indexOf(start, fromIdx);
        return startIdx == -1 ? src : src.substring(startIdx);
    }

    /**
     * 获取字符串的子串
     * @param src 原字符串
     * @param start 开始的子串（包含）
     * @param end 结束的子串（包含）
     * @return 结果子串
     * @apiNote <pre>{@code
     * var s = "x12y3z"
     * // 获取s中x和y之间的子串（包括x和y）（结果：x12y）
     * var res = Strs.subInclusive(s, "x", "y");
     * }</pre>
     */
    public static String subInclusive(String src, String start, String end) {
        return subInclusive(src, start, end, 0);
    }

    /**
     * 获取字符串的子串
     * @param src 原字符串
     * @param start 开始的子串（包含）
     * @return 结果子串
     * @apiNote <pre>{@code
     * var s = "xxy123"
     * // 获取s中y后面的子串（包括y）（结果：y123）
     * var res = Strs.subInclusive(s, "y");
     * }</pre>
     */
    public static String subInclusive(String src, String start) {
        return subInclusive(src, start, 0);
    }

    /**
     * 替换多个旧子串为新子串
     * @param src 原字符串
     * @param newStr 新子串
     * @param oldStrs 旧子串
     * @return 结果字符串
     * @apiNote <pre>{@code
     * var s = "abccddeeff";
     * // 将s字符串中的cc和ee替换成!（结果：aa!dd!ff）
     * var res = Strs.orReplace(s, "!", "cc", "ee");
     * }</pre>
     */
    public static String orReplace(String src, String newStr, String... oldStrs) {
        Objs.throwsIfNullOrEmpty(src);

        if (newStr == null) {
            newStr = "";
        }

        for (String oldChar : oldStrs) {
            src = src.replace(oldChar, newStr);
        }

        return src;
    }

    /**
     * 判断多个字符串中是否存在至少一个字符串与提供的字符串相等
     * @param src 提供的字符串
     * @param strs 多个字符串
     * @return 是否存在相等
     * @apiNote <pre>{@code
     * // 判断字符串"abc"是否与"a"或"b"或"abc"完全相等（结果：true）
     * var flag = Strs.orEquals("abc", "a", "b", "abc");
     * }</pre>
     */
    public static boolean orEquals(String src, String... strs) {
        Objs.throwsIfNullOrEmpty(src);

        for (String s : strs) {
            if (src.equals(s)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断字符串中是否包含任意提供的子串
     * @param src 字符串
     * @param contains 提供的子串
     * @return 是否包含任意子串
     * @apiNote <pre>{@code
     * // 判断字符串"aabbccdddee"是否包含"ff"或"cc"（结果：true）
     * var flag = Strs.orContains("aabbccdddee", "ff", "cc");
     * }</pre>
     */
    public static boolean orContains(String src, String... contains) {
        Objs.throwsIfNullOrEmpty(src);

        for (String s : contains) {
            if (src.contains(s)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断字符串是否以提供的任意子串开始
     * @param src 字符串
     * @param strs 提供的子串
     * @return 是否以提供的任意子串开始
     * @apiNote <pre>{@code
     * // 判断字符串"aabbccdddee"是否以"bb"或"dd"开始（结果：false）
     * var flag = Strs.orStartsWith("aabbccdddee", "bb", "dd");
     * }</pre>
     */
    public static boolean orStartsWith(String src, String... strs) {
        Objs.throwsIfNullOrEmpty(src);

        for (String s : strs) {
            if (src.startsWith(s)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断字符串是否以提供的任意子串结束
     * @param src 字符串
     * @param strs 提供的子串
     * @return 是否以提供的任意子串结束
     * @apiNote <pre>{@code
     * // 判断字符串"aabbccdddee"是否以"ff"或"ee"结束（结果：true）
     * var flag = Strs.orEndsWith("aabbccdddee", "ff", "ee");
     * }</pre>
     */
    public static boolean orEndsWith(String src, String... strs) {
        Objs.throwsIfNullOrEmpty(src);

        for (String s : strs) {
            if (src.endsWith(s)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 用多个分隔符分割字符串
     * @param src 字符串
     * @param splits 1个或多个分隔符
     * @return 分割后字符串列表
     * @apiNote <pre>{@code
     * // 将字符串"a!c?d"使用"?"和"!"作为分隔符分割成多个字符串（结果：["a", "c", "d"]）
     * var flag = Strs.orSplit("a!c?d", "?", "!");
     * }</pre>
     * @since 2.4
     */
    public static List<String> orSplit(String src, String... splits) {
        var res = new ArrayList<String>();

        /*
            例子：11uu22ttt33uuu44vvv55, 其中tt,uu和vvv是分隔符
            第一次循环：left = 0,  right = 2,   splitLen = 2(uu),  res = [11]
            第二次循环：left = 4,  right = 6,   splitLen = 2(tt),  res = [11, 22]
            第三次循环：left = 8,  right = 11,  splitLen = 2(uu),  res = [11, 22, t33]
            第四次循环：left = 13, right = 16,  splitLen = 3(vvv), res = [11, 22, t33, u44]
            第五次循环：left = 19, right = 无限, splitLen = -1, res = [11, 22, t33, u44, 55]
         */

        // 左边边界
        int left = 0;
        while (true) {
            // 右边边界
            int right = Integer.MAX_VALUE;
            // 右边边界右边的分隔符长度
            int splitLen = -1;

            // 找出最小右边边界
            for (var split : splits) {
                int startIdx;
                if ((startIdx = src.indexOf(split, left)) != -1) {
                    if (startIdx < right) {
                        right = startIdx;
                        splitLen = split.length();
                    }
                }
            }

            // 如果不存在右边边界，说明后面没有分隔符了，把后面全部放进去后结束循环
            if (right == Integer.MAX_VALUE) {
                res.add(src.substring(left));
                break;
            }

            // 添加结果子串
            res.add(src.substring(left, right));

            // 更新左边边界
            left = right + splitLen;
        }

        return res;
    }

    /**
     * 将字符串首字母大写
     * @param src 字符串
     * @return 结果字符串
     * @apiNote <pre>{@code
     * // 将字符串"jmc"首字母大写（结果：Jmc）
     * var res = Strs.capitalize("jmc");
     * }</pre>
     */
    public static String capitalize(String src) {
        return (src == null || src.length() == 0) ? src :
                src.length() == 1 ? src.toUpperCase() : Character.toUpperCase(src.charAt(0)) + src.substring(1);
    }

    /**
     * 异或字符串
     * @param src 原字符串
     * @param key 私钥
     * @return 结果字符串
     * @apiNote <pre>{@code
     * // 将字符串"abc"用私钥34进行异或获得密文（结果：C@A）
     * var encrypt = Strs.xor("abc", 34);
     *
     * // 将密文再进行异或获得原文
     * var decrypt = Strs.xor(encrypt, 34);
     * Assert.assertEquals("abc", decrypt);
     * }</pre>
     */
    public static String xor(String src, byte key) {
        byte[] bs = src.getBytes();
        for (int i = 0; i < bs.length; i++) {
            bs[i] ^= key;
        }
        return new String(bs);
    }

    /**
     * 交换字符串中的两个子串
     * @param src 字符串
     * @param subA 子串a
     * @param subB 子串b
     * @return 结果字符串
     * @apiNote <pre>{@code
     * var s = "a c a c";
     * // 把字符串s中的a和c调换位置（结果是：c a c a）
     * var res = Strs.swap(s, "a", "c");
     * }</pre>
     */
    public static String swap(String src, String subA, String subB) {
        return subA.equals(subB) ? src :
                src.replace(subA, "" + Character.MAX_VALUE)
                   .replace(subB, subA)
                   .replace("" + Character.MAX_VALUE, subB);
    }

    /**
     * 收集所有以开始字符串开始，结束字符串结束的子串
     * @param src 字符串
     * @param start 开始字符串
     * @param end 结束字符串
     * @param includesBoundary 是否包含边界（开始和结束字符串）
     * @return 结果列表
     * @apiNote <pre>{@code
     * var s = "<a><b><c>";
     * // 收集所有以"<"开头，">"结尾的子串（不包括边界"<"和">"）（结果：["a", "b", "c"]）
     * var res = Strs.collectAll(s, "<", ">", false);
     * }</pre>
     */
    public static List<String> collectAll(String src, String start, String end, boolean includesBoundary) {
        StringBuilder sb = new StringBuilder(src);
        List<String> rList = new ArrayList<>();
        while (true) {
            int startIdx = sb.indexOf(start);
            int endIdx = sb.indexOf(end, startIdx);
            if (startIdx != -1 && endIdx != -1) {
                if (includesBoundary) {
                    rList.add(sb.substring(startIdx, endIdx + end.length()));
                } else {
                    rList.add(sb.substring(startIdx + end.length(), endIdx));
                }
                sb.delete(startIdx, endIdx + end.length());
            } else {
                break;
            }
        }
        return rList;
    }

    /**
     * 删除所有以开始字符串开始，结束字符串结束的子串
     * @param src 字符串
     * @param start 开始字符串
     * @param end 结束字符串
     * @return 结果字符串
     * @apiNote <pre>{@code
     * var s = "<a>?<b>!<c>";
     * // 去除所有以<开头，>结尾的子串（结果："?!"）
     * var res = Strs.removeAll(s, "<", ">");
     * }</pre>
     */
    public static String removeAll(String src, String start, String end) {
        StringBuilder sb = new StringBuilder(src);
        while (true) {
            int startIdx = sb.indexOf(start);
            int endIdx = sb.indexOf(end, startIdx);
            if (startIdx != -1 && endIdx != -1) {
                sb.delete(startIdx, endIdx + end.length());
            } else {
                break;
            }
        }
        return sb.toString();
    }
}
