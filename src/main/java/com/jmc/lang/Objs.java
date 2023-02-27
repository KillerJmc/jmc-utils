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
	 * @apiNote <pre>{@code
	 * var s = "666";
	 * // 判断字符串s是否与"555"或者"666"相同
	 * boolean res = Objs.orEquals(s, "555", "666");
	 * }</pre>
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
	 * @apiNote <pre>{@code
	 * String s = null;
	 * var l = List.of();
	 * // 当字符串s或者集合l为空（空字符串，空集合，null）的时候抛出空指针异常
	 * // 此时会抛出异常（集合l为空）
	 * Objs.throwsIfNullOrEmpty(s, l);
	 * }</pre>
	 */
	public static void throwsIfNullOrEmpty(Object... objs) {
		throwsIfNullOrEmpty("", objs);
	}

	/**
	 * 如果传入对象为空（包含字符串或集合为空），则抛出NPE
	 * @param message 异常提示信息
	 * @param objs 传入的对象
	 * @apiNote <pre>{@code
	 * String s = null;
	 * var l = List.of();
	 * // 当字符串s或者集合l为空（空字符串，空集合，null）的时候抛出空指针异常
	 * // 此时会抛出异常（集合l为空），并附带异常信息
	 * Objs.throwsIfNullOrEmpty("字符串或集合为空！", s, l);
	 * }</pre>
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
	 * @apiNote <pre>{@code
	 * String s = null;
	 * var l = List.of();
	 * // 判断对象是否为空（空字符串，空集合，null）
	 * // 此时集合l为空集合，返回false
	 * boolean res = Objs.nullOrEmpty(s, l);
	 * }</pre>
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

