package cn.fomer.common.http;

import java.io.InputStream;
import java.util.Map;

/**
 * 2018-11-01
 * 
 * 
 */
public interface Http 
{
	public String get(String url) throws Exception;
	public String get(String url,Map<String, String> header, Map<String, String> variable) throws Exception;
	
	public String post(String url) throws Exception;
	public String post(String url,Map<String, String> header, Map<String, String> variable) throws Exception;
	
	public String jsonPost(String url) throws Exception;
	public String jsonPost(String url,Map<String, String> header, Map<String, Object> variable) throws Exception;
	
	public InputStream download(String url) throws Exception;
	
	public final int GET= 0;
	public final int POST= 1;
	public final int FORM= 0;
	public final int JSON= 1;
}
