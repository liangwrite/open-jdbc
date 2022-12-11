package cn.fomer.common.json.gson;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class IntegerJsonSerializer implements JsonSerializer<Integer>, JsonDeserializer<Integer>
{

	@Override
	public JsonElement serialize(Integer n, Type arg1, JsonSerializationContext arg2) {
		// TODO Auto-generated method stub
		//if(n==null) return 
		return new JsonPrimitive(n);
	}
	
	@Override
	public Integer deserialize(JsonElement jsonElement, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
		// TODO Auto-generated method stub
		Integer n= null;
		String text= jsonElement.getAsString();
		if(jsonElement==null) return null;
		
		try
		{
			n= Integer.valueOf(text);
		}
		catch(Exception e){}

		return n;
	}
	

}
