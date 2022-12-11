package cn.fomer.common.json.gson;

import java.lang.reflect.Type;
import java.sql.Date;
import java.text.SimpleDateFormat;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class SqlDateJsonSerializer implements JsonSerializer<Date>, JsonDeserializer<Date>
{

	@Override
	public JsonElement serialize(Date date, Type arg1, JsonSerializationContext arg2) {
		// TODO Auto-generated method stub
		//if(n==null) return 
		SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return new JsonPrimitive(simpleDateFormat.format(date));
	}
	
	@Override
	public Date deserialize(JsonElement jsonElement, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
		// TODO Auto-generated method stub
		Date sqlDate= null;
		String text= jsonElement.getAsString();
		if(jsonElement==null) return null;
		
		try
		{
			SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm");
			java.util.Date date= simpleDateFormat.parse(text);
			sqlDate= new Date(date.getTime());
			
		}
		catch(Exception e){}

		return sqlDate;
	}
	

}
