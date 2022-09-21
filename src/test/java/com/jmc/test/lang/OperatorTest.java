package com.jmc.test.lang;

import com.jmc.lang.Operator;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

public class OperatorTest {
    @Test
    public void expOprTest() {
        Int a = new Int(3), b = new Int(4), c = new Int(5), d = new Int(6), e = new Int(-1),
            f = new Int(7), g = new Int(2);

        // (a <= d - a) && (a && e)) || ((a * b > c + d) && (a * b == c))
        var flag = Operator.cmp("(? <= ? - ? && (? && ?)) || ((? * ? > ? + ?) && (? * ? == ?))",
                a, d, a, a, e, a, b, c, d, a, b, c);
        Assert.assertFalse(flag);

        // g + (a - g) * (d & f) / (g | b) ^ g << c >>> g >> f % b
        var res = Operator.calc("? + (? - ?) * (? & ?) / (? | ?) ^ ? << ? >>> ? >> ? % ?",
                g, a, g, d, f, g, b, g, c, g, f, b);
        Assert.assertEquals(res, new Int(1));

        Operator.calc("? *= ? + ?", a, b, c);
        Assert.assertEquals(a, new Int(27));
    }

    @Test
    public void binaryOprTest() {
        Int a = new Int(3), b = new Int(4);

        // c = a + b
        Int c = Operator.calc(a, "+", b);
        Assert.assertEquals(c, new Int(7));

        // c = a - b
        c = Operator.calc(a, "-", b);
        Assert.assertEquals(c, new Int(-1));

        // c = a * b
        c = Operator.calc(a, "*", b);
        Assert.assertEquals(c, new Int(12));

        // c = a / b
        c = Operator.calc(a, "/", b);
        Assert.assertEquals(c, new Int(0));

        // c = a % b
        c = Operator.calc(a, "%", b);
        Assert.assertEquals(c, new Int(3));

        // c = a & b
        c = Operator.calc(a, "&", b);
        Assert.assertEquals(c, new Int(3 & 4));

        // c = a | b
        c = Operator.calc(a, "|", b);
        Assert.assertEquals(c, new Int(3 | 4));

        // c = a ^ b
        c = Operator.calc(a, "^", b);
        Assert.assertEquals(c, new Int(3 ^ 4));

        // c = a << b
        c = Operator.calc(a, "<<", b);
        Assert.assertEquals(c, new Int(3 << 4));

        // c = a >> b
        c = Operator.calc(a, ">>", b);
        Assert.assertEquals(c, new Int(3 >> 4));

        // c = a >>> b
        c = Operator.calc(a, ">>>", b);
        Assert.assertEquals(c, new Int(3 >>> 4));

        // a += b
        Operator.calc(a, "+=", b);
        Assert.assertEquals(a, new Int(7));

        // a -= b
        Operator.calc(a, "-=", b);
        Assert.assertEquals(a, new Int(3));

        // a *= b
        Operator.calc(a, "*=", b);
        Assert.assertEquals(a, new Int(12));

        // a /= b
        Operator.calc(a, "/=", b);
        Assert.assertEquals(a, new Int(3));

        // a %= b
        Operator.calc(a, "%=", b);
        Assert.assertEquals(a, new Int(3));

        // a &= b
        Operator.calc(a, "&=", b);
        Assert.assertEquals(a, new Int(3 & 4));

        // a |= b
        Operator.calc(a, "|=", b);
        Assert.assertEquals(a, new Int((3 & 4) | 4));

        // a ^= b
        Operator.calc(a, "^=", b);
        Assert.assertEquals(a, new Int((3 & 4 | 4) ^ 4));

        // a <<= b
        Operator.calc(a, "<<=", b);
        Assert.assertEquals(a, new Int(((3 & 4 | 4) ^ 4) << 4));

        // a >>= b
        Operator.calc(a, ">>=", b);
        Assert.assertEquals(a, new Int((((3 & 4 | 4) ^ 4) << 4) >> 4));

        // a >>>= b
        Operator.calc(a, ">>>=", b);
        Assert.assertEquals(a, new Int((((3 & 4 | 4) ^ 4) << 4) >>> 4));
    }

    @Test
    public void unaryOprTest() {
        Int a = new Int(3);

        // b = +a
        Int b = Operator.calc("+", a);
        Assert.assertEquals(b, new Int(3));

        // b = -a
        b = Operator.calc("-", a);
        Assert.assertEquals(b, new Int(-3));

        // b = ~a
        b = Operator.calc("~", a);
        Assert.assertEquals(b, new Int(~3));

        Operator.calc("~", a);

        // b = ++a
        b = Operator.calc("++", a);
        Assert.assertEquals(b, new Int(4));

        // b = a++
        b = Operator.calc(a, "++");
        Assert.assertEquals(b, new Int(4));

        // b = --a
        b = Operator.calc("--", a);
        Assert.assertEquals(b, new Int(4));

        // b = a--
        b = Operator.calc(a, "--");
        Assert.assertEquals(b, new Int(4));
    }

    @Test
    public void cmpOprTest() {
        Int a = new Int(3), b = new Int(-4);

        var flag = Operator.cmp(a, "==", b);
        Assert.assertFalse(flag);

        flag = Operator.cmp(a, "!=", b);
        Assert.assertTrue(flag);

        flag = Operator.cmp(a, "<", b);
        Assert.assertFalse(flag);

        flag = Operator.cmp(a, ">", b);
        Assert.assertTrue(flag);

        flag = Operator.cmp(a, "<=", b);
        Assert.assertFalse(flag);

        flag = Operator.cmp(a, ">=", b);
        Assert.assertTrue(flag);

        flag = Operator.cmp(a, "&&", b);
        Assert.assertFalse(flag);

        flag = Operator.cmp(a, "||", b);
        Assert.assertTrue(flag);

        flag = Operator.cmp("!", b);
        Assert.assertTrue(flag);
    }

    @Data
    @AllArgsConstructor
    static class Int implements Operator<Int> {
        int value;

        @Override
        public Int operatorPlus(Int other) {
            return new Int(this.value + other.value);
        }

        @Override
        public Int operatorMinus(Int other) {
            return new Int(this.value - other.value);
        }

        @Override
        public Int operatorTimes(Int other) {
            return new Int(this.value * other.value);
        }

        @Override
        public Int operatorDiv(Int other) {
            return new Int(this.value / other.value);
        }

        @Override
        public Int operatorMod(Int other) {
            return new Int(this.value % other.value);
        }

        @Override
        public Int operatorPos() {
            return new Int(+this.value);
        }

        @Override
        public Int operatorNeg() {
            return new Int(-this.value);
        }

        @Override
        public Int operatorIncPre() {
            this.value++;
            return this;
        }

        @Override
        public Int operatorIncPost() {
            return new Int(this.value++);
        }

        @Override
        public Int operatorDecPre() {
            this.value--;
            return this;
        }

        @Override
        public Int operatorDecPost() {
            return new Int(this.value--);
        }

        @Override
        public Int operatorPlusEq(Int other) {
            this.value += other.value;
            return this;
        }

        @Override
        public Int operatorMinusEq(Int other) {
            this.value -= other.value;
            return this;
        }

        @Override
        public Int operatorTimesEq(Int other) {
            this.value *= other.value;
            return this;
        }

        @Override
        public Int operatorDivEq(Int other) {
            this.value /= other.value;
            return this;
        }

        @Override
        public Int operatorModEq(Int other) {
            this.value %= other.value;
            return this;
        }

        @Override
        public boolean operatorDoubleEq(Int other) {
            return this.equals(other);
        }

        @Override
        public boolean operatorNotEq(Int other) {
            return !this.equals(other);
        }

        @Override
        public boolean operatorLT(Int other) {
            return this.value < other.value;
        }

        @Override
        public boolean operatorGT(Int other) {
            return this.value > other.value;
        }

        @Override
        public boolean operatorLE(Int other) {
            return this.value <= other.value;
        }

        @Override
        public boolean operatorGE(Int other) {
            return this.value >= other.value;
        }

        @Override
        public boolean operatorAnd(Int other) {
            return this.value > 0 && other.value > 0;
        }

        @Override
        public boolean operatorOr(Int other) {
            return this.value > 0 || other.value > 0;
        }

        @Override
        public boolean operatorNot() {
            return !(this.value > 0);
        }

        @Override
        public Int operatorBitAnd(Int other) {
            return new Int(this.value & other.value);
        }

        @Override
        public Int operatorBitOr(Int other) {
            return new Int(this.value | other.value);

        }

        @Override
        public Int operatorBitXor(Int other) {
            return new Int(this.value ^ other.value);
        }

        @Override
        public Int operatorShl(Int other) {
            return new Int(this.value << other.value);
        }

        @Override
        public Int operatorShr(Int other) {
            return new Int(this.value >> other.value);
        }

        @Override
        public Int operatorUShr(Int other) {
            return new Int(this.value >>> other.value);
        }

        @Override
        public Int operatorBitAndEq(Int other) {
            this.value &= other.value;
            return this;
        }

        @Override
        public Int operatorBitOrEq(Int other) {
            this.value |= other.value;
            return this;
        }

        @Override
        public Int operatorBitXorEq(Int other) {
            this.value ^= other.value;
            return this;
        }

        @Override
        public Int operatorShlEq(Int other) {
            this.value <<= other.value;
            return this;
        }

        @Override
        public Int operatorShrEq(Int other) {
            this.value >>= other.value;
            return this;
        }

        @Override
        public Int operatorUShrEq(Int other) {
            this.value >>>= other.value;
            return this;
        }

        @Override
        public Int operatorBitReverse() {
            this.value = ~this.value;
            return this;
        }
    }
}
