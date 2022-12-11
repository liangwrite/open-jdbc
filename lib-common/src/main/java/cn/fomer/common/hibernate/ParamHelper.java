package cn.fomer.common.hibernate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 2018-12-14 帮助生成参数（借鉴PagerHelper）
 * 
 * 
 */
public class ParamHelper
{

	Map<String, Object> map= new HashMap<String, Object>();
	
	public static ParamHelper newParam()
	{
		return new ParamHelper();
	}
	
	public ParamHelper add(String name,Object value)
	{
		map.put(name, value);
		return this;
	}
	
	public Map<String, Object> toMap()
	{
		return map;
	}
	
	private void main()
	{
		ParamHelper.newParam().add("start", new Date()).add("end", new Date()).toMap();
	}
}
