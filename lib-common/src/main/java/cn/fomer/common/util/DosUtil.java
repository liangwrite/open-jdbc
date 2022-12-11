package cn.fomer.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 2017-12-23
 * 
 * 
 * */
public class DosUtil {
	
	public static void main(String[] args) throws IOException {
		
		//execute("ping 127.0.0.1 -n 10");
		execute("cd \\");
		
	}
	
	public static String execute(String commond) throws IOException
	{
		return execute(commond, "utf-8");
	}
	public static String execute(String commond,String character) throws IOException
	{		

		Runtime runtime= Runtime.getRuntime();
		Process process= runtime.exec(commond); //IOException,"系统找不到指定的文件。"、"Cannot run program "ifconfig""、"java.lang.ArrayIndexOutOfBoundsException"
		InputStream inputStream= process.getInputStream();
		ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
		
		byte[] buffer= new byte[1*200*1024]; //0.2M缓存
		int len=0;
		while((len=inputStream.read(buffer))!=-1) //IOException
		{
			String tmp_text= new String(buffer,0,len,"gbk");
			System.out.println("tmp_text: "+tmp_text);
			byteArrayOutputStream.write(buffer, 0, len);			
		}
		
		inputStream.close(); //IOException
		
		String text= new String(byteArrayOutputStream.toByteArray(),character);
		byteArrayOutputStream.close(); //IOException
		
		return text;

	}
	
}
