package com.jmc.math;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 数学增强类
 * @since 1.0
 * @author Jmc
 */
public class Maths {

    private Maths() {}

    /**
     * 埃式筛法计算质数，时间复杂度O(N * loglogN)
     * @param max 最大值（若也为质数，结果中也会包括）
     * @return 结果集合
     */
    public static List<Integer> getPrimes(int max) {
        var res = new ArrayList<Integer>();
        boolean[] nonPrime = new boolean[max + 1];

        // i > sqrt(n)之后i x i > n，无需计算
        for (int i = 2; i * i <= max; i++) {
            if (!nonPrime[i]) {
                /*
                   i x 2, i x 3, i x 4..., i x (i - 1) -- [*]
                   因为合数均被质数覆盖，即4 x i, 6 x i均分别被2 x i, 3 x i覆盖过
                   所以[*]均被 2 x i, 3 x i, 5 x i, 7 x i, ... , (i - 1) x i覆盖过
                   因此从i x i开始计算，而不是i + i
                 */
                for (int j = i * i; j <= max; j += i) {
                    // 质数的倍数(> 1)都是合数
                    nonPrime[j] = true;
                }
            }
        }

        for (int i = 2; i <= max; i++) {
            if (!nonPrime[i]) {
                res.add(i);
            }
        }
        return res;
    }

    /**
     * 判断是否是质数，时间复杂度O(sqrt(N))
     * @param probablePrime 被判断的数
     * @return 该数是否为质数
     */
    public static boolean isPrime(long probablePrime) {
        int firstPrime = 2;
        if (probablePrime == firstPrime) {
            return true;
        }
        if (probablePrime < firstPrime || (probablePrime & 1) == 0) {
            return false;
        }

        // 开根号减少时间复杂度
        double sqrtDouble = Math.sqrt(probablePrime);

        // 向下取整
        long sqrt = (long) sqrtDouble;

        // 特殊情况减少时间复杂度
        if (sqrt == sqrtDouble) {
            return false;
        }

        // 前面已经判断过2，且偶数不用判断
        int step = 2;
        for (long p = 3; p <= sqrt; p += step) {
            // 排除部分数以减少时间复杂度
            var exclude = p > 7 && (p % 3 == 0 || p % 5 == 0 || p % 7 == 0);
            if (exclude) {
                continue;
            }
            if (probablePrime % p == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 计算阶乘
     * @param n 底数
     * @return 计算结果
     */
    public static BigInteger factorial(int n) {
        if (n == 0 || n == 1) {
            return BigInteger.ONE;
        }

        var a = new BigInteger[n];
        long tmp = 1;
        int p = 0;

        for (int i = 2; i <= n; i++) {
            if (tmp < Long.MAX_VALUE / i) {
                tmp *= i;
            } else {
                a[p++] = BigInteger.valueOf(tmp);
                tmp = i;
            }
        }
        a[p++] = BigInteger.valueOf(tmp);

        var optional = Arrays.stream(a, 0, p)
                                                .parallel()
                                                .reduce(BigInteger::multiply);
        return optional.orElse(null);
    }

    /**
     * 计算阶乘近似值
     * @param n 底数
     * @return 阶乘近似结果的字符串
     */
    public static String approxFactorial(int n) {
        double d = 1;
        long pow = factorialLength(n) - 1;

        while (n > 1) {
            d = d * n--;

            d /= d >= 10000000000L        ?        10000000000L        :
                 d >= 1000000000          ?        1000000000          :
                 d >= 100000000           ?        100000000           :
                 d >= 10000000            ?        10000000            :
                 d >= 1000000             ?        1000000             :
                 d >= 100000              ?        100000              :
                 d >= 10000               ?        10000               :
                 d >= 1000                ?        1000                :
                 d >= 100                 ?        100                 :
                 d >= 10                  ?        10                  :                  1;
        }

        return (int) d + " * 10 ^ " + pow;
    }


    /**
     * 用斯特林公式计算阶乘结果的位数
     * @param n 底数
     * @return 阶乘结果的位数
     */
    private static long factorialLength(int n) {
        // lg n! = lg 2Πn / 2 + n lg n / e
        return (long) (Math.log10(2 * Math.PI * n) / 2 + n * Math.log10(n / Math.E)) + 1;
    }
}
