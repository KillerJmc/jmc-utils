package com.jmc.array;

import java.util.Iterator;

/**
 * 通用数组类 <br><br>
 * <pre><b>
 * 特点：
 *   1. 可配合泛型作为任意类型数组传入方法（特别指基本数据类型）
 *      从而对所有类型的数组进行通用操作。（比如通用swap）
 *   2. 绑定数组后所有操作对原数组都有效。
 *
 * 效率：
 *   仅在获取（包括遍历）和设置值时对基本数据类型进行包装。
 *   基本类型数组访问速度慢10倍左右，对象数组访问速度不变。
 * </b></pre>
 * @apiNote <pre>{@code
 * <T> void swap(Array<T> arr, int idx1, int idx2) {
 *     T t = arr.get(idx1);
 *     arr.set(idx1, arr.get(idx2));
 *     arr.set(idx2, t);
 * }
 *
 * int[] a = { 1, 2, 3, 4, 5 };
 * var arr = Array.of(a);
 *
 * // 交换后原数组a和通用数组arr都是：[ 1, 4, 3, 2, 5 ]
 * swap(arr, 1, 3);
 *
 * }</pre>
 * @since 1.1
 * @author Jmc
 * @param <T> 数组元素类型
 */
@SuppressWarnings({"all", "unchecked"})
public abstract class Array<T> implements Iterable<T> {
    /**
     * 私有构造器
     */
    private Array() {}

    /**
     * 获得数组中指定下标的元素
     * @param pos 下标
     * @return 对应下标的元素
     */
    public abstract T get(int pos);

    /**
     * 在指定下标处插入元素
     * @param pos 下标
     * @param t 要插入的元素
     */
    public abstract void set(int pos, T t);

    /**
     * 返回数组长度
     * @return 数组长度
     */
    public abstract int len();

    /**
     * 获得原来的数组
     * @param <Arr> 数组类型
     * @return 原来的数组
     */
    public abstract <Arr> Arr toArray();

    /**
     * 把通用数组转化成字符串
     * @return 结果字符串
     * @since 1.8
     */
    @Override
    public String toString() {
        if (len() == 0) {
            return "[]";
        }

        var leftBracket = "[ ";
        var rightBracket = " ]";
        var comma = ", ";

        var res = new StringBuilder(leftBracket);
        for (var t : this) {
            res.append(t).append(comma);
        }

        // 删除最后一个", "
        res.delete(res.length() - 2, res.length());
        res.append(rightBracket);
        return res.toString();
    }

    /**
     * 返回通用数组
     * @param a byte类型数组
     * @return 通用数组
     */
    public static Array<Byte> of(byte... a) {
        return new Array<>() {
            @Override
            public Byte get(int pos) {
                return a[pos];
            }

            @Override
            public void set(int pos, Byte e) {
                a[pos] = e;
            }

            @Override
            public int len() {
                return a.length;
            }

            @Override
            public byte[] toArray() {
                return a;
            }

            @Override
            public Iterator<Byte> iterator() {
                return new Iterator<>() {
                    private int pos = 0;

                    @Override
                    public boolean hasNext() {
                        return pos < len();
                    }

                    @Override
                    public Byte next() {
                        return a[pos++];
                    }
                };
            }
        };
    }

    /**
     * 返回通用数组
     * @param a char类型数组
     * @return 通用数组
     */
    public static Array<Character> of(char... a) {
        return new Array<>() {
            @Override
            public Character get(int pos) {
                return a[pos];
            }

            @Override
            public void set(int pos, Character e) {
                a[pos] = e;
            }

            @Override
            public int len() {
                return a.length;
            }

            @Override
            public char[] toArray() {
                return a;
            }

            @Override
            public Iterator<Character> iterator() {
                return new Iterator<>() {
                    private int pos = 0;

                    @Override
                    public boolean hasNext() {
                        return pos < len();
                    }

                    @Override
                    public Character next() {
                        return a[pos++];
                    }
                };
            }
        };
    }

    /**
     * 返回通用数组
     * @param a boolean类型数组
     * @return 通用数组
     */
    public static Array<Boolean> of(boolean... a) {
        return new Array<>() {
            @Override
            public Boolean get(int pos) {
                return a[pos];
            }

            @Override
            public void set(int pos, Boolean e) {
                a[pos] = e;
            }

            @Override
            public int len() {
                return a.length;
            }

            @Override
            public boolean[] toArray() {
                return a;
            }

            @Override
            public Iterator<Boolean> iterator() {
                return new Iterator<>() {
                    private int pos = 0;

                    @Override
                    public boolean hasNext() {
                        return pos < len();
                    }

                    @Override
                    public Boolean next() {
                        return a[pos++];
                    }
                };
            }
        };
    }

    /**
     * 返回通用数组
     * @param a short类型数组
     * @return 通用数组
     */
    public static Array<Short> of(short... a) {
        return new Array<>() {
            @Override
            public Short get(int pos) {
                return a[pos];
            }

            @Override
            public void set(int pos, Short e) {
                a[pos] = e;
            }

            @Override
            public int len() {
                return a.length;
            }

            @Override
            public short[] toArray() {
                return a;
            }

            @Override
            public Iterator<Short> iterator() {
                return new Iterator<>() {
                    private int pos = 0;

                    @Override
                    public boolean hasNext() {
                        return pos < len();
                    }

                    @Override
                    public Short next() {
                        return a[pos++];
                    }
                };
            }
        };
    }

    /**
     * 返回通用数组
     * @param a int类型数组
     * @return 通用数组
     */
    public static Array<Integer> of(int... a) {
        return new Array<>() {
            @Override
            public Integer get(int pos) {
                return a[pos];
            }

            @Override
            public void set(int pos, Integer e) {
                a[pos] = e;
            }

            @Override
            public int len() {
                return a.length;
            }

            @Override
            public int[] toArray() {
                return a;
            }

            @Override
            public Iterator<Integer> iterator() {
                return new Iterator<>() {
                    private int pos = 0;

                    @Override
                    public boolean hasNext() {
                        return pos < len();
                    }

                    @Override
                    public Integer next() {
                        return a[pos++];
                    }
                };
            }
        };
    }

    /**
     * 返回通用数组
     * @param a long类型数组
     * @return 通用数组
     */
    public static Array<Long> of(long... a) {
        return new Array<>() {
            @Override
            public Long get(int pos) {
                return a[pos];
            }

            @Override
            public void set(int pos, Long e) {
                a[pos] = e;
            }

            @Override
            public int len() {
                return a.length;
            }

            @Override
            public long[] toArray() {
                return a;
            }

            @Override
            public Iterator<Long> iterator() {
                return new Iterator<>() {
                    private int pos = 0;

                    @Override
                    public boolean hasNext() {
                        return pos < len();
                    }

                    @Override
                    public Long next() {
                        return a[pos++];
                    }
                };
            }
        };
    }

    /**
     * 返回通用数组
     * @param a float类型数组
     * @return 通用数组
     */
    public static Array<Float> of(float... a) {
        return new Array<>() {
            @Override
            public Float get(int pos) {
                return a[pos];
            }

            @Override
            public void set(int pos, Float e) {
                a[pos] = e;
            }

            @Override
            public int len() {
                return a.length;
            }

            @Override
            public float[] toArray() {
                return a;
            }

            @Override
            public Iterator<Float> iterator() {
                return new Iterator<>() {
                    private int pos = 0;

                    @Override
                    public boolean hasNext() {
                        return pos < len();
                    }

                    @Override
                    public Float next() {
                        return a[pos++];
                    }
                };
            }
        };
    }

    /**
     * 返回通用数组
     * @param a double类型数组
     * @return 通用数组
     */
    public static Array<Double> of(double... a) {
        return new Array<>() {
            @Override
            public Double get(int pos) {
                return a[pos];
            }

            @Override
            public void set(int pos, Double e) {
                a[pos] = e;
            }

            @Override
            public int len() {
                return a.length;
            }

            @Override
            public double[] toArray() {
                return a;
            }

            @Override
            public Iterator<Double> iterator() {
                return new Iterator<>() {
                    private int pos = 0;

                    @Override
                    public boolean hasNext() {
                        return pos < len();
                    }

                    @Override
                    public Double next() {
                        return a[pos++];
                    }
                };
            }
        };
    }

    /**
     * 返回通用数组
     * @param a T类型数组
     * @param <T> 数组元素类型
     * @return 通用数组
     */
    public static <T> Array<T> of(T[] a) {
        return new Array<>() {
            @Override
            public T get(int pos) {
                return a[pos];
            }

            @Override
            public void set(int pos, T e) {
                a[pos] = e;
            }

            @Override
            public int len() {
                return a.length;
            }

            @Override
            public T[] toArray() {
                return a;
            }

            @Override
            public Iterator<T> iterator() {
                return new Iterator<>() {
                    private int pos = 0;

                    @Override
                    public boolean hasNext() {
                        return pos < len();
                    }

                    @Override
                    public T next() {
                        return a[pos++];
                    }
                };
            }
        };
    }
}
