package cn.fomer.common.json.jackson;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;


public class JacksonDeserializerForDate extends JsonDeserializer<Date> 
{	

	@Override
	public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
			throws IOException, JsonProcessingException {
		// TODO Auto-generated method stub
		SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date= null;          
        try 
        { date= format.parse(jsonParser.getText());} 
        catch(ParseException e){ e.printStackTrace();}  
        return date;  
	}		
	
}
