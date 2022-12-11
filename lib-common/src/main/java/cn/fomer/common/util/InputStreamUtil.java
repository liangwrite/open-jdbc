package cn.fomer.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 2019-01-22
 * 
 * 
 */
public class InputStreamUtil {
	
	public static byte[] ReadAll(InputStream inputStream) throws IOException
	{
		ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream(10*1024);
		byte[] buffer= new byte[1*1024];
		int len= 0;
		while((len=inputStream.read(buffer))!=-1)
		{
			byteArrayOutputStream.write(buffer, 0, len);
			
		}
		
		return byteArrayOutputStream.toByteArray();
	}
	
	public static String ReadAllAsString(InputStream inputStream) throws IOException
	{
		byte[] data= ReadAll(inputStream);
		String text= new String(data,"utf-8");
		return text;
	}
	
}
