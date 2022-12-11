package cn.fomer.common.java;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 2021-08-18
 * Map map= MapBuilder.newInstance().put("total",12).put("child", itemList).toMap();
 * 
 */
public class MapBuilder {
	
	Map<String, Object> map= new LinkedHashMap<String, Object>();

	public static MapBuilder newInstance()
	{
		return new MapBuilder();
	}
	
	
	public MapBuilder put(String key, Object value)
	{
		map.put(key, value);
		return this;
	}
	
	public Map<String, Object> toMap()
	{
		return map;
	}
}
