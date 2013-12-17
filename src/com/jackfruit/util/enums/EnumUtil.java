package com.jackfruit.util.enums;

import java.util.List;

public class EnumUtil {
	/**
	 * 根据枚举index返回枚举元素，index从0开始
	 * @param <T> 
	 * 			枚举类型
	 * @param values
	 * 			枚举元素输入
	 * @param index
	 * 			从0开始的index
	 * @return	
	 * 		枚举元素
	 */
	public static <T extends Enum<T>> T valueOf(List<T> values, int index) {
		T value = null;
		try {
			value = values.get(index);
		} catch (Exception e) {
			return null;
		}
		return value;
	}
}
