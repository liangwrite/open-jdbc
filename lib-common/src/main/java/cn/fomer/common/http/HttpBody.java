package cn.fomer.common.http;

import java.net.URLEncoder;
import java.util.Map;

import com.alibaba.fastjson.JSON;


/**
 * 2019-05-18
 * 
 * 
 */
public class HttpBody {
	//
	public static HttpBody createFormBody(Map<String, String> formMap)
	{
		HttpBody httpBody= new HttpBody();
		httpBody.formType= HttpFormType.FORM;
		String body= "";
		for(Map.Entry<String, String> entry:formMap.entrySet())
		{
			try{ body+= entry.getKey()+"="+URLEncoder.encode(entry.getValue(),"utf-8")+"&";}
			catch(Exception e){ e.printStackTrace();}
		}
		body= body.substring(0,body.length()-1); //移除末尾&
		httpBody.body= body;
		return httpBody;
	}
	
	HttpFormType formType;
	String body;
	public static HttpBody createJSONBody(Map<String, Object> jsonMap,JSON jsonObj)
	{
		HttpBody httpBody= new HttpBody();
		httpBody.formType= HttpFormType.JSON;
		String body= "";
		//body= jsonObj.toJSON(jsonMap);
		httpBody.body= body;
		return httpBody;
	}
	
	private HttpBody(){}
	
	
	public String getBody(){ return body;}

	
}
