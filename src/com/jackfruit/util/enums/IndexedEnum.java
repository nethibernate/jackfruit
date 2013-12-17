package com.jackfruit.util.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 从0开始的可索引枚举接口定义，实现此接口的枚举各个元素的index
 * 可以不连续，但此接口的实现类多为稀疏数组结构，保持index连续
 * 可以节省空间
 * @author yaguang.xiao
 *
 */
public interface IndexedEnum {
	/**
	 * 获取该枚举的索引值
	 * @return 返回>=0的索引值
	 */
	public abstract int getIndex();
	
	public static class IndexedEnumUtil {
		/** 索引警戒上限，发现超过此值得索引可能存在较大的空间浪费 */
		private static final int WORNNING_MAX_INDEX = 1000;
		
		public static <E extends IndexedEnum> List<E> toIndexes(E[] enums) {
			int maxIndex = Integer.MIN_VALUE;
			int curIdx = 0;
			// 找到最大index，此值+1就是结果list的size
			for(E enm : enums) {
				curIdx = enm.getIndex();
				assert curIdx >= 0;
//				throw new Exception("枚举索引不能为负，index:" + curIdx + ",type:" + enums.getClass());
				if(curIdx > maxIndex) {
					maxIndex = curIdx;
				}
			}
			if(maxIndex >= WORNNING_MAX_INDEX) {
				System.out.println("警告：枚举类" + enums.getClass().getComponentType().getName()
							+ "中有索引超过" + WORNNING_MAX_INDEX + "的索引，如果有很多索引空缺，可能会造成空间浪费");
			}
			List<E> instances = new ArrayList<E>(maxIndex + 1);
			// 先全用null填充
			for(int i = 0;i < maxIndex + 1;i ++) {
				instances.add(null);
			}
			for(E enm : enums) {
				curIdx = enm.getIndex();
				assert instances.get(curIdx) == null;
//				throw new Exception("枚举重有重复的index type=" + enums.getClass().getComponentType().getName());
				instances.set(curIdx, enm);
			}
			return instances;
		}
	}
}
