package cn.fomer.jdbc.component;

import java.util.Map;
import java.util.Map.Entry;

/**
 * 2020-11-28 根据表里一个的字段，在map中查找有没有值
 *
 */
public class JdbcMap {

	Map<String, Object> dataMap;
	public JdbcMap(Map<String, Object> dataMap)
	{
		this.dataMap= dataMap;
	}

	
	/**
	 * 202011 根据表里一个的字段，在map中查找有没有值
	 */
	public Entry<String, Object> getFieldEntry(String field)
	{
		for(Entry<String, Object> entry: dataMap.entrySet())
		{
			if(field.equalsIgnoreCase(entry.getKey()))
			{
				return entry;
			}
		}
		
		return null;
	}
}
