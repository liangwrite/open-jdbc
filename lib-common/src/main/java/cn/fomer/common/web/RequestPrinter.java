package cn.fomer.common.web;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;

/**
 * 2019-06-17 打印request中的参数
 * 
 * 
 */
public class RequestPrinter{
	
	static long count= 0;
	public static String toText(HttpServletRequest request){
		// TODO Auto-generated method stub
		final String unsupport_message= "不支持参数打印";
		try
		{
			//
			boolean isPost= "post".equalsIgnoreCase(request.getMethod());
			String url= "";
			String srcIp= "";
			String header= "";
			String param= "";
			String text= "";
			
			srcIp= request.getRemoteAddr()+":"+request.getRemotePort();
			if("get".equalsIgnoreCase(request.getMethod()))
			{
				url= request.getRequestURL().toString()+((request.getQueryString()==null)?"":("?"+request.getQueryString()));
				param= convert(request.getParameterMap());
			}
			else if("post".equalsIgnoreCase(request.getMethod()))
			{
				url= request.getRequestURL().toString();
				if(request.getContentType()==null)
				{
					param= " "+unsupport_message;
				}
				else if(request.getContentType().toLowerCase().contains("application/json"))
				{
					param= " "+unsupport_message;
					request.getInputStream();
				}
				else if(request.getContentType().toLowerCase().contains("application/x-www-form-urlencoded"))
				{
					param= convert(request.getParameterMap());
				}
				else
				{
					param= request.getContentType()+" "+unsupport_message;
				}
				
			}
			
			Enumeration<String> enumeration= request.getHeaderNames();
			while(enumeration.hasMoreElements())
			{
				String key= enumeration.nextElement();
				String value= request.getHeader(key);
				header+= key+"="+value+";";
			}

			
			if(isPost)
			{
				text+= "body: "+(param);
			}
			
			
			text+= "head: Authorization="+request.getHeader("Authorization")+"<br />";
			text+= request.getMethod()+": "+ url+"<br />";
			return text;
			

		}
		catch(Exception e)
		{
			e.printStackTrace();
			
			return null;
		}
	}

	//如果
	private static String convert(Map<String, String[]> map)
	{
		Map<String, Object> paramMap= new HashMap<>();
		for(String key:map.keySet())
		{
			String[] value= map.get(key);
			if(value==null||value.length==0)
			{
				paramMap.put(key, null);
			}
			else
			{
				if(value.length==1)
				{
					paramMap.put(key, value[0]);
				}
				else
				{
					paramMap.put(key, value);
				}
				
			}
			
		}
		return new Gson().toJson(paramMap);
	}
	

}
