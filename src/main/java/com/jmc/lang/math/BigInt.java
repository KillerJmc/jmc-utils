package com.jmc.lang.math;

import java.util.Arrays;

/**
 * 大整数
 * @since 1.0.0
 * @author Jmc
 */
@SuppressWarnings("unused")
public class BigInt {
    /**
     * 存储大整数的数组
     */
    private final int[] data;

    /**
     * 是否为正数
     */
    private final boolean isPositive;

    /**
     * 用字符串构造大整数
     * @param s 字符串
     */
    public BigInt(String s) {
        char[] cs = s.toCharArray();
        this.isPositive = cs[0] != '-';
        this.data = new int[isPositive ? s.length() : s.length() - 1];
        if (isPositive) {
            for (int i = 0; i < data.length; i++) {
                data[i] = cs[i] - '0';
            }
        } else {
            for (int i = 0; i < data.length; i++) {
                data[i] = cs[i + 1] - '0';
            }
        }
    }

    /**
     * 用存储大整数的数组和是否为正数的布尔值构造大整数
     * @param data 存储大整数的数组
     * @param isPositive 是否为正数
     */
    private BigInt(int[] data, boolean isPositive) {
        this.data = data;
        this.isPositive = isPositive;
    }

    /**
     * 用long类型整数构造大整数
     * @param l long类型整数
     * @return 大整数
     */
    public static BigInt valueOf(long l) {
        if (l == 0) {
            return new BigInt(new int[] {0}, true);
        }
        boolean isPositive = l > 0;
        int[] result = new int[19];
        int p = result.length - 1;

        while (l != 0) {
            result[p--] = isPositive ? (int) (l % 10) : (int) (-l % 10);
            l /= 10;
        }

        if (p != -1) {
            result = Arrays.copyOfRange(result, p + 1, result.length);
        }

        return new BigInt(result, isPositive);
    }

    /**
     * 加法
     * @param other 另一个加数
     * @return 新的结果对象
     */
    public BigInt add(BigInt other) {
        return this.isPositive() && other.isPositive() ? new BigInt(add(this.data, other.data), true) :
            !this.isPositive() && !other.isPositive() ? new BigInt(add(this.data, other.data), false) :
            this.isPositive() ? sub(data, other.data) : sub(other.data, data);
    }

    /**
     * 数组层面的加法
     * @param data1 数组1
     * @param data2 数组2
     * @return 结果数组
     */
    private int[] add(int[] data1, int[] data2) {
        int[] longer = data1.length > data2.length ? data1 : data2,
              shorter = longer == data1 ? data2 : data1,
              result = new int[longer.length + 1];

        int carry = 0, p = result.length - 1, pL = longer.length - 1, pS = shorter.length - 1;

        while (p != 0) {
            int n = (pS >= 0 ? longer[pL--] + shorter[pS--] : longer[pL--]) + carry;
            result[p--] = n % 10;
            carry = n / 10 % 10;
        }

        // 若超过最大加数的最高位还有余数（如99 + 1 -> 仍有余数1）
        if (carry > 0) {
            result[0] = carry;
        } else {
            result = Arrays.copyOfRange(result, 1, result.length);
        }

        return result;
    }

    /**
     * 减法
     * @param other 减数
     * @return 新的结果对象
     */
    public BigInt sub(BigInt other) {
        return this.isPositive() && other.isPositive() ? sub(data, other.data) :
            !this.isPositive() && !other.isPositive() ? sub(other.data, data) :
            this.isPositive() ? new BigInt(add(data, other.data), true) :
            new BigInt(add(data, other.data), false);
    }

    /**
     * 数组层面的减法
     * @param data1 数组1
     * @param data2 数组2
     * @return 结果对象
     */
    private BigInt sub(int[] data1, int[] data2) {
        if (isZero(data1)) {
            return new BigInt(data2, false);
        }
        if (isZero(data2)) {
            return new BigInt(data1, true);
        }
        if (data1.length < data2.length) {
            return BigInt.valueOf(0).sub(sub(data2, data1));
        }

        int[] result = new int[data1.length];

        int carry = 0, p = result.length - 1, p1 = data1.length - 1, p2 = data2.length - 1;

        while (p != -1) {
            int n = carry + (p2 >= 0 ? data1[p1--] - data2[p2--] : data1[p1--]);
            carry = 0;
            if (n < 0) {
                carry = -1;
                n += 10;
            }
            result[p--] = n;
        }

        if (carry == -1) {
            return BigInt.valueOf(0).sub(sub(data2, data1));
        }

        // 去除数值前的0值
        if (result[0] == 0) {
            int countZero = 0;
            for (int t : result) {
                if (t == 0) {
                    countZero++;
                } else {
                    break;
                }
            }

            if (countZero == result.length) {
                return BigInt.valueOf(0);
            }

            result = Arrays.copyOfRange(result, countZero, result.length);
        }

        return new BigInt(result, true);
    }

    /**
     * 乘法
     * @param other 另一个乘数
     * @return 新的结果对象
     */
    public BigInt mul(BigInt other) {
        return (this.isPositive() && !other.isPositive()) || (!this.isPositive() && other.isPositive()) ?
            new BigInt(mul(data, other.data), false) : new BigInt(mul(data, other.data), true);
    }

    /**
     * 数组层面的乘法
     * @param data1 数组1
     * @param data2 数组2
     * @return 结果数组
     */
    private int[] mul(int[] data1, int[] data2) {
        int[] result = new int[data1.length + data2.length];

        for (int p1 = data1.length - 1; p1 >= 0; p1--) {
            for (int p2 = data2.length - 1; p2 >= 0; p2--) {
                int tmp = data1[p1] * data2[p2] + result[p1 + p2 + 1];
                result[p1 + p2 + 1] = tmp % 10;
                result[p1 + p2] += tmp / 10;
            }
        }

        return result[0] != 0 ? result : Arrays.copyOfRange(result, 1, result.length);
    }

    /**
     * 除法
     * @param other 另一个乘数
     * @return 新的结果对象
     */
    public BigInt div(BigInt other) {
        int[] result = div(data, other.data);

        if (this.isPositive() == other.isPositive()) {
            return new BigInt(result, true);
        } else {
            return isZero(result) ? BigInt.valueOf(0) : new BigInt(result, false);
        }
    }

    /**
     * 数组层面的除法
     * @param data1 数组1
     * @param data2 数组2
     * @return 结果数组
     */
    private int[] div(int[] data1, int[] data2) {
        if (isZero(data2)) {
            throw new ArithmeticException("BigInt divide by zero!");
        }
        if (data1.length < data2.length) {
            return new int[] {0};
        }

        int multiple = data1.length - data2.length;
        int[] result = new int[data1.length - data2.length + 1];

        data2 = Arrays.copyOf(data2, data2.length + multiple);
        BigInt remainder = new BigInt(data1, true);

        while (multiple >= 0) {
            var tmp = sub(remainder.data, data2);
            if (!tmp.isPositive() || isZero(tmp.data)) {
                data2 = Arrays.copyOf(data2, data2.length - 1);
                multiple--;
                continue;
            }
            remainder = tmp;
            result[result.length - multiple - 1]++;
        }

        return result;
    }

    /**
     * 返回是否为正数
     * @return 是否为正数
     */
    public boolean isPositive() {
        return isPositive;
    }

    /**
     * 返回存储大整数的数组是否表示大整数零
     * @param result 存储大整数的数组
     * @return 是否表示零
     */
    private boolean isZero(int[] result) {
        return result.length == 1 && result[0] == 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (!this.isPositive()) {
            sb.append("-");
        }
        for (int t : data) {
            sb.append(t);
        }
        return sb.toString();
    }
}