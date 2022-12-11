package cn.fomer.common.json.fastjson;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.fomer.common.json.JSON;


/**
 * 2018-12-10
 * 
 * 
 */
public class FastJSONUtil implements JSON
{
	public static void main(String[] args) {
		//
		FastJSONUtil json= new FastJSONUtil();
		
		Date date= new Date();
		System.out.println(json.toJSON(date));
	}

	@Override
	public String toJSON(Object obj) {
		// TODO Auto-generated method stub
		
		return com.alibaba.fastjson.JSON.toJSONString(obj);
	}

	@Override
	public <T> T fromJSON(String s, Class<T> clazz) {
		// TODO Auto-generated method stub
		T t= com.alibaba.fastjson.JSON.parseObject(s, clazz);
		return t;
	}


	@Override
	public <T> List<T> fromJSONAsList(String s, Class<T> element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isJSON(String s) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map fromJSONAsMap(String json) {
		// TODO Auto-generated method stub
		return null;
	}

}
