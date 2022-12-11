package cn.fomer.common.json.gson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import cn.fomer.common.json.JSON;



/**
 * 2019-05-16
 * 
 * 
 */
public class GsonUtil implements JSON
{

	static Gson gson;
	static
	{
		gson= new GsonBuilder()
				.registerTypeAdapter(Integer.class, new IntegerJsonSerializer())
				.registerTypeAdapter(java.sql.Date.class, new SqlDateJsonSerializer())
				.registerTypeAdapter(java.util.Date.class, new DateJsonSerializer())
				.create();
	}
	

	@Override
	public String toJSON(Object obj) {
		// TODO Auto-generated method stub
		return gson.toJson(obj);
	}

	@Override
	public <T> T fromJSON(String s, Class<T> clazz) {
		// TODO Auto-generated method stub
		return gson.fromJson(s, clazz);
	}

	
	@Override
	public <T> List<T> fromJSONAsList(String s, Class<T> childClazz) {
		// TODO Auto-generated method stub
		JsonParser parser=new JsonParser();
		JsonElement rootElement= parser.parse(s);
		
		if(rootElement.isJsonArray()) 			
		{
			JsonArray array= rootElement.getAsJsonArray();
			List<T> entityList=new ArrayList<T>();		
			Iterator<JsonElement> it= array.iterator();
			while(it.hasNext())
			{
				JsonElement jsonElement=it.next();
				T ob= null;
				try
				{ 
					ob=gson.fromJson(jsonElement, childClazz);
				}
				catch(Exception e)
				{
					throw new RuntimeException(e.toString());
				}
				
				entityList.add(ob);
			}
			
			return entityList;
			
			
		}
		return null;
	}



	@Override
	public boolean isJSON(String s) {
		// TODO Auto-generated method stub
		JsonParser parser=new JsonParser();
		try
		{
			JsonElement jsonElement = parser.parse(s);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}


	@Override
	public Map fromJSONAsMap(String json) {
		// TODO Auto-generated method stub
		return null;
	}

}
