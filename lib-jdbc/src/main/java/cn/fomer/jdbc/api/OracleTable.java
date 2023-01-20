/**
 * 
 */
package cn.fomer.jdbc.api;

import java.util.List;
import java.util.Map;

import cn.fomer.jdbc.service.impl.ColumnServiceImpl;

/**
 * 2022-01-13
 * 
 */
public interface OracleTable 
{
	
	/**
	 * 2022-01-23 查找主键
	 */
	String getIdField();
	
	
	
	/**
	 * 2018-11-03 Oracle 返回都是大写
	 * 202103 hive的分区字段会显示两遍/max的分区字段只会显示一遍
	 * <p>maxcompute比较特殊，只能通过metadata的方式</p>
	 */
	List<ColumnService> getColumnList();
}
