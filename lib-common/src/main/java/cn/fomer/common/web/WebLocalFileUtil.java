package cn.fomer.common.web;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.stereotype.Component;

/**
 * 2018-04-28
 * 
 * */
@Component
public class WebLocalFileUtil {
	
	static ServletContext application;

	/** 数据结构：map.put(relativePath, content); */
	static Map<String, String> cacheMap= new HashMap<String, String>();
	static boolean isEnableCache= true;
	
	synchronized public static void Init(ServletContext servletContext)
	{
		if(application==null)
			application= servletContext;
			
	}
	
	public static void EnableCache(boolean isEnable)
	{
		WebLocalFileUtil.isEnableCache= isEnable;
	}
	
	
	/**
	 * 2018-04-28 网站启动后首次读取，必定需要读取物理文件。再次读取时，才会根据 isEnableCache 选择缓存还是物理文件。
	 * 
	 * 
	 * */
	public static String ReadPath(String relativePath)
	{
		
		System.out.println("WebLocalFileUtil: "+relativePath);
		if(isEnableCache)
		{
			//有没有缓存过
			if(cacheMap.containsKey(relativePath))
			{
				return ReadCache(relativePath);
			}			
		}
		
		return ReadRelativePath(relativePath, null);		
	}
	
	
	/**
	 * 2018-04-28 从硬盘读取物理文件，并且缓存。
	 * @param relativePath 示例 "/WEB-INF/sql/dd_kaoqin_report.txt"
	 * 
	 * 
	 * */
	public static String ReadRelativePath(String relativePath,ServletContext application)
	{
		String absolutePath= application.getRealPath(relativePath);
		String context= ReadAbsolutePath(absolutePath);
		return context;		
	}
	
	
	/**
	 * 2018-11-15 读取绝对路径下文件内容
	 * 
	 */
	public static String ReadAbsolutePath(String absolutePath)
	{		
		File file= new File(absolutePath);
		try
		{
			if(file.exists())
			{
				String content= "";
				byte[] buffer= new byte[10*1024]; //10KB
				FileInputStream fileInputStream= new FileInputStream(file);
				int len= fileInputStream.read(buffer);
				//content= new String(buffer, 0, len, "UTF-8");
				content= new String(buffer, 0, len, "UTF-8");
				
				putCache(absolutePath, content); //不要忘记缓存
				return content;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		putCache(absolutePath, null); //不要忘记存入缓存
		return null;		
	}
	
	/**
	 * 2018-04-28
	 * 
	 * */
	public static String ReadCache(String relativePath)
	{
		//
		return cacheMap.get(relativePath);
		
	}
	
	/**
	 * 2018-06-03
	 * 
	 * */
	static void putCache(String relativePath,String content)
	{
		//
		cacheMap.put(relativePath, content);
		
	}

}
