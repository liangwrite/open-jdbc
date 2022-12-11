package cn.fomer.common.office;

import java.util.List;

/**
 * 2020-05-18 把一个业务对象转换为excel一行
 *
 */
public interface ExcelConverter<T> {
	
	/**
	 * 把一个业务对象转换为excel一行
	 * 2020-05-18
	 * @param T 业务对象
	 * @param i 行号
	 */
	List<String[]> convert(T vo, int i);
	
}
