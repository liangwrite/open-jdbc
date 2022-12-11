package cn.fomer.common.springcloud;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.fomer.common.io.StreamUtil;

/**
 * 2020-08-23
 * 
 *
 */
public class JarUtil {

	
	/**
	 * 2020-08-23
	 * 
	 * @param resourceFile json/xx.json
	 * @return
	 */
	public static String ReadResourceFile(String resourceFile)
	{
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceFile);
		ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
		try
		{
			StreamUtil.copy(inputStream, byteArrayOutputStream);
			inputStream.close();
			String text= new String(byteArrayOutputStream.toByteArray(), "utf-8");
			return text;
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		
		return null;
	}
}
