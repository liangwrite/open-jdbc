package cn.fomer.common.json;

import java.util.List;
import java.util.Map;

/**
 * 2018-12-10 JSON(JavaScript Object Notation, JS 对象简谱) 是一种轻量级的数据交换格式
 * 
 * 
 */
public interface JSON 
{
	//
	public String toJSON(Object obj);
	public <T> T fromJSON(String json,Class<T> clazz);	
	public <T> List<T> fromJSONAsList(String json,Class<T> element);
	public Map fromJSONAsMap(String json);
	public boolean isJSON(String s);
}
