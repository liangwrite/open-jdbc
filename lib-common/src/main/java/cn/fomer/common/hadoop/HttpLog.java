package cn.fomer.common.hadoop;

import java.lang.management.ManagementFactory;
import java.net.URLEncoder;

import cn.fomer.common.http.HttpURLConnectionUtil;


/**
 * 2019-11-17
 *
 */
public class HttpLog {
	
	public static void main(String[] args) {
		http("haha");
	}

	static String url= "http://192.168.10.10:8040/log/info?message=";
	public static void http(String message)
	{
		int pId= Integer.valueOf(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]).intValue();
		long tId= Thread.currentThread().getId();

		String body= "PID/TID="+pId+"/"+tId+" "+message;
		HttpURLConnectionUtil.get(url+URLEncoder.encode(body));
	}
}
