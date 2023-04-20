package com.jmc.test.lang;

import com.jmc.lang.Operator;
import org.junit.Assert;
import org.junit.Test;

import java.util.Objects;

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

    static class Int implements Operator<Int> {
        int value;

        public Int(int value) { this.value = value; }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Int i) {
                return value == i.value;
            }
            return false;
        }

        @Override
        public int hashCode() { return Objects.hash(value); }

        @Override
        public Int plus(Int other) {
            return new Int(this.value + other.value);
        }

        @Override
        public Int minus(Int other) {
            return new Int(this.value - other.value);
        }

        @Override
        public Int times(Int other) {
            return new Int(this.value * other.value);
        }

        @Override
        public Int div(Int other) {
            return new Int(this.value / other.value);
        }

        @Override
        public Int mod(Int other) {
            return new Int(this.value % other.value);
        }

        @Override
        public Int unaryPlus() {
            return new Int(+this.value);
        }

        @Override
        public Int unaryMinus() {
            return new Int(-this.value);
        }

        @Override
        public Int incPre() {
            this.value++;
            return this;
        }

        @Override
        public Int incPost() {
            return new Int(this.value++);
        }

        @Override
        public Int decPre() {
            this.value--;
            return this;
        }

        @Override
        public Int decPost() {
            return new Int(this.value--);
        }

        @Override
        public Int plusAssign(Int other) {
            this.value += other.value;
            return this;
        }

        @Override
        public Int minusAssign(Int other) {
            this.value -= other.value;
            return this;
        }

        @Override
        public Int timesAssign(Int other) {
            this.value *= other.value;
            return this;
        }

        @Override
        public Int divAssign(Int other) {
            this.value /= other.value;
            return this;
        }

        @Override
        public Int modAssign(Int other) {
            this.value %= other.value;
            return this;
        }

        @Override
        public boolean eq(Int other) {
            return this.equals(other);
        }

        @Override
        public boolean notEq(Int other) {
            return !this.equals(other);
        }

        @Override
        public boolean lessThan(Int other) {
            return this.value < other.value;
        }

        @Override
        public boolean greaterThan(Int other) {
            return this.value > other.value;
        }

        @Override
        public boolean lessEq(Int other) {
            return this.value <= other.value;
        }

        @Override
        public boolean greaterEq(Int other) {
            return this.value >= other.value;
        }

        @Override
        public boolean and(Int other) {
            return this.value > 0 && other.value > 0;
        }

        @Override
        public boolean or(Int other) {
            return this.value > 0 || other.value > 0;
        }

        @Override
        public boolean not() {
            return !(this.value > 0);
        }

        @Override
        public Int bitAnd(Int other) {
            return new Int(this.value & other.value);
        }

        @Override
        public Int bitOr(Int other) {
            return new Int(this.value | other.value);

        }

        @Override
        public Int bitXor(Int other) {
            return new Int(this.value ^ other.value);
        }

        @Override
        public Int shl(Int other) {
            return new Int(this.value << other.value);
        }

        @Override
        public Int shr(Int other) {
            return new Int(this.value >> other.value);
        }

        @Override
        public Int uShr(Int other) {
            return new Int(this.value >>> other.value);
        }

        @Override
        public Int bitAndAssign(Int other) {
            this.value &= other.value;
            return this;
        }

        @Override
        public Int bitOrAssign(Int other) {
            this.value |= other.value;
            return this;
        }

        @Override
        public Int bitXorAssign(Int other) {
            this.value ^= other.value;
            return this;
        }

        @Override
        public Int shlAssign(Int other) {
            this.value <<= other.value;
            return this;
        }

        @Override
        public Int shrAssign(Int other) {
            this.value >>= other.value;
            return this;
        }

        @Override
        public Int uShrAssign(Int other) {
            this.value >>>= other.value;
            return this;
        }

        @Override
        public Int bitReverse() {
            this.value = ~this.value;
            return this;
        }
    }
}
