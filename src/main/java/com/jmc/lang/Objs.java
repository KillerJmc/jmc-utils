package com.jmc.lang;

import java.util.Collection;
import java.util.Map;

/**
 * Object类扩展
 * @since 1.0
 * @author Jmc
 */
public class Objs {

	private Objs() {}

	/**
	 * 判断一个对象是否与给定对象列表中任意一个相等
	 * @param t 一个对象
	 * @param others 给定对象列表
	 * @param <T> 对象类型
	 * @return 判断结果
	 * @since 2.0
	 */
	@SafeVarargs
	public static <T> boolean orEquals(T t, T... others) {
		for (var other : others) {
			if (t.equals(other)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 如果传入对象为空（包含字符串或集合为空），则抛出NPE
	 * @param objs 传入的对象
	 */
	public static void throwsIfNullOrEmpty(Object... objs) {
		throwsIfNullOrEmpty("", objs);
	}

	/**
	 * 如果传入对象为空（包含字符串或集合为空），则抛出NPE
	 * @param message 异常提示信息
	 * @param objs 传入的对象
	 */
	public static void throwsIfNullOrEmpty(String message, Object... objs) {
		if (nullOrEmpty(objs)) {
			throw new NullPointerException(message);
		}
	}

	/**
	 * 判断传入对象是否存在null值（包含字符串或集合为空）
	 * @param objs 传入对象
	 * @return 是否存在null值（包含字符串或集合为空）
	 */
	public static boolean nullOrEmpty(Object... objs) {
		if (objs == null) {
			return true;
		}

		for (Object o : objs) {
			if (o == null) {
				return true;
			} else if (o instanceof String s) {
				if (s.isBlank()) {
					return true;
				}
			} else if (o instanceof Collection<?> c) {
				if (c.isEmpty()) {
					return true;
				}
			} else if (o instanceof Map<?, ?> m) {
				if (m.isEmpty()) {
					return true;
				}
			}
		}

		return false;
	}
}

