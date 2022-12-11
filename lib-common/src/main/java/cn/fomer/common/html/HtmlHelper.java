package cn.fomer.common.html;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.html.HTMLMapElement;

/**
 * 2017-11-04
 * 
 * 
 * */
public class HtmlHelper 
{
	static Map<String, String> HtmlMap= new HashMap<String, String>();
	static
	{
		HtmlMap.put("<", "&lt;");
		HtmlMap.put(">", "&gt;");
		HtmlMap.put("\"", "&quot;");
		//HtmlMap.put("&", "&amp;");
	}
	
	/**
	 * html转义
	 * 
	 * 
	 * */
	public static String toSafeHtml(String source)
	{		
		if(source!=null)
		{
			for(Entry<String, String> entry:HtmlMap.entrySet())
			{
				source=source.replace(entry.getKey(), entry.getValue());	//替换所有
			}
		}		
		return source;
	}
	
	public static String fromHtmlString(String source)
	{		
		if(source!=null)
		{
			for(Entry<String, String> entry:HtmlMap.entrySet())
			{
				source=source.replace(entry.getValue(), entry.getKey());	//替换所有
			}
		}		
		return source;
	}

	public static String toSafeJavascript(String source)
	{		
		if(source!=null)
		{
			source=source.replace("\\", "\\\\");	// \	-	\\
			source=source.replace("'", "\\\'");	//'	 -	\'
			source=source.replace("\"", "\\\"");	//"	-	\"			
			source=source.replace("\r\n", "\\r\\n");	//\r\n	-	\\r\\n
			source=source.replace("\n", "\\n");	//\r\n	-	\\r\\n
		}
		return source;
	}
	
	
	

}
