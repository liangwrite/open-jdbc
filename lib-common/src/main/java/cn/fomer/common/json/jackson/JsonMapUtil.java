package cn.fomer.common.json.jackson;

import java.util.List;
import java.util.Map;

/**
 * 2018-04-19 分析复杂由JSON转为的Map
 * 
 * 
 * */
public class JsonMapUtil {
	
	Object o;
	
	
	/**
	 * 2018-04-19 规定了起点，也就是允许Map
	 * 
	 * 
	 * */
	public static JsonMapUtil getInstance(Map map)
	{		
		return new JsonMapUtil(map);
	}
	
	/**
	 * 2018-04-19 规定了起点，也就是允许Map
	 * 
	 * 
	 * */
	public static JsonMapUtil getInstance(List json)
	{		
		return new JsonMapUtil(json);
	}
	
	

	private JsonMapUtil(Object o)
	{		
		this.o= o;		
	}
	
	/**
	 * 2018-04-19 支持源数据是Map 或者 List
	 * map.get("name")
	 * list.get(0)
	 * 
	 * */
	public JsonMapUtil get(Object key)
	{
		if(o instanceof Map)
		{
			Object v= ((Map)o).get(key);
			return new JsonMapUtil(v);			
		}
		else if(o instanceof List)
		{
			if(key instanceof Integer)
			{
				Object v= ((List)o).get((Integer)key);
				return new JsonMapUtil(v);	
			}
		}
		
		
		
		return null;
	}
	
	/**
	 * 2018-04-19 将字符串["2018-04-17 07:18", "2018-04-17 13:10", 3.66, "hour", "病假", "请假类型"]转JSON数组
	 * 注意：前提条件是源类型必须是字符串类型
	 * 尚未完成，因为不希望将Jackon代码混入进来。
	 * 
	 * */
	@Deprecated
	public JsonMapUtil asList()
	{
		if(o instanceof String)
		{
			
		}
		
		
		return null;
	}
	
	public Object value()
	{
		return o;
	}
	 
	
	/**
	 * 2018-04-21
	 * 
	 * */
	public JsonMapUtil fromJson()
	{
		if(o instanceof String)
		{
			try
			{
				String json= (String)o;
				
				List list= new JacksonUtil().fromJsonAsList(json, Map.class);
				
				Map<String, Object> map= new JacksonUtil().fromJSONAsMap(json);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			
		}
		return null;
		
	}

}
