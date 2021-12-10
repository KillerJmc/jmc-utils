package com.jmc.lang.extend;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Map;

/**
 * Object类扩展
 * @since 1.0
 * @author Jmc
 */
@SuppressWarnings("unused")
public class Objs {

	private Objs() {}

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
	private static boolean nullOrEmpty(Object... objs) {
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

