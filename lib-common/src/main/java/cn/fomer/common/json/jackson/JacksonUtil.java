package cn.fomer.common.json.jackson;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

//import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import cn.fomer.common.json.JSON;


/**
 * 
 * 2017-12-09 jackson-databind-*.jar version must be higher than jackson-databind-2.4.6.1.jar
 * 
 * */
public class JacksonUtil implements JSON {
	static ObjectMapper objectMapper= new ObjectMapper();
	static
	{
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		
		//序列化时，如果对象没有get方法，默认抛出异常
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		//当json出现的属性，而对象没有该字段时，是否抛出异常
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	
	/**
	 * 2017-12-01
	 * 
	 * json中的子对象变为Map
	 * 
	 * */
	@Override
	public Map fromJSONAsMap(String json)
	{
		
		//
		Map map= null;
		try
		{
			map= objectMapper.readValue(json, Map.class);
			return map;
		}
		catch(Exception e)
		{ 
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}

	}
	
	
	/**
	 * 2017-12-13
	 * */
	public <T> List<T> fromJsonAsList(String json,Class<T> childType)
	{
		//
		JavaType javaType= objectMapper.getTypeFactory().constructParametricType(List.class, childType); 
		
		try{
			List<T> list= objectMapper.readValue(json, javaType);
			return list;
		}
		catch(Exception e)
		{ 
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	
	

	/**
	 * 2018-06-04
	 * 
	 * 
	 * */
	@Override
	public boolean isJSON(String s)
	{
		//会报错
		if(s!=null)
		{
			//会报错
			if(!"".equals(s))
			{
				ObjectMapper objectMapper= new ObjectMapper();
				JsonNode rootJsonNode= null;
				try
				{
					rootJsonNode= objectMapper.readTree(s); //1.不能为null; 2.不能为""; 3.可以为"123";
					
				}
				catch(Exception e)
				{
					//e.printStackTrace();
					
					return false;
				}
				boolean isOk= rootJsonNode.isContainerNode();
				return isOk;
			}
			
		}
		return false;
		
	}

	@Override
	public String toJSON(Object obj) {
		// TODO Auto-generated method stub
		if(obj==null) return null; //否则返回"null";
		
		try
		{
			String json= objectMapper.writeValueAsString(obj);
			return json;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;	
	}

	@Override
	public <T> T fromJSON(String json, Class<T> clazz) {
		// TODO Auto-generated method stub
		T t= null;
		try
		{ 
			t= objectMapper.readValue(json, clazz);
		}
		catch(Exception e)
		{
			e.printStackTrace();			
		}
		return t;
	}

	@Override
	public <T> List<T> fromJSONAsList(String json, Class<T> clazz) {
		// TODO Auto-generated method stub
		return null;
	}


}
