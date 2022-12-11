package cn.fomer.common.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 2018-01-07
 * */
public class StringHelper {
	
	public static boolean isNumber(String s)
	{
		try {
			Long.parseLong(s);
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	public static String ifNull(String s, String defaultString)
	{
		if(s==null) {
			return defaultString;
		}
		
		return s;
	}
	
	/**
	 * 202209 安全截取
	 */
	public static String substring(String s, int len)
	{
		if(s!=null) {
			if(s.length()>=len) {
				return s.substring(0, len);
			}
		}
		
		return s;
		
	}

}
