package cn.fomer.common.web;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 2020-06-01
 *
 */
public class RequestUtil {

	/**
	 * 2020-06-01 所有value不看做数组
	 */
	public static Map<String, String> paramMap(HttpServletRequest request)
	{
		Enumeration<String> enumeration = request.getParameterNames();
		
		Map<String, String> varMap= new HashMap<>();
		while(enumeration.hasMoreElements())
		{
			String key = enumeration.nextElement();
			String value = request.getParameter(key);
			varMap.put(key, value);
		}
		
		return varMap;
	}
}
