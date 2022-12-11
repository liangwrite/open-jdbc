package liang.http;



import java.io.IOException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;


/**
 * 2018-10-10 JDK1.6 陕西移动
 * 
 * 
 */
public class CommonHttpClientUtil {
	
	public static void main(String[] args) throws IOException {
		//
		String url= "http://192.144.167.220/OpenServiceAPI.asmx/ConfirmData?jsondata={\"LabId\":\"46307448-FA1E-455E-8880-0D26F8395BAC\",\"Rfid\":[\"201901110001\",\"201901110002\"],\"XJRName\":\"我是巡检人\",\"Memo\":\"我来巡检\",\"CreateTime\":\"2019/1/10 20:00:16\",\"XJRTel\":\"13221212121\",\"LabJDRName\":\"我是实验室接待人\"}";
		String text= CommonHttpClientUtil.doGet(null, url, "");
		System.out.println(text);
	}
	public static String doGet(Map<String, String> headers, String url, String queryString) throws IOException {
		String response = null;
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod(url);
		if (headers != null && headers.size() > 0) {
			Iterator i$ = headers.keySet().iterator();

			while (i$.hasNext()) {
				String key = (String) i$.next();
				method.setRequestHeader(key, (String) headers.get(key));
			}
		}

		try {
			if (queryString!=null&&queryString.trim().length()>0) {
				method.setQueryString(queryString);
			}

			client.executeMethod(method);
			if (method.getStatusCode() == 200) {
				response = method.getResponseBodyAsString();
			}
		} catch (URIException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			method.releaseConnection();
		}

		return response;
	}
	

	public static String doPostWithNameValue(String url, Map<String, String> nameValuePaireMap) throws IOException {
		//ResultVO<String> resultVO= new ResultVO();
		
		String response = null;
		HttpClient client = new HttpClient();
		client.setConnectionTimeout(5000);
		client.setTimeout(45000);
		PostMethod postMethod = new PostMethod(url);

		
		Set<Entry<String, String>> set= nameValuePaireMap.entrySet();		
		
		String queryString= "";
		for(Entry<String, String> entry:set)
		{
			queryString+= entry.getKey()+"="+URLEncoder.encode(entry.getValue(),"utf-8")+"&";
		}

		if(queryString.endsWith("&"))
		{
			queryString= queryString.substring(0, queryString.length()-1);
		}
		
			
		
		StringRequestEntity requestEntity= new StringRequestEntity(queryString,"application/x-www-form-urlencoded","UTF-8");

		
		postMethod.setRequestEntity(requestEntity);

		
		
		try 
		{
			
			int code= client.executeMethod(postMethod);
			response = postMethod.getResponseBodyAsString();
		
			System.out.println(CommonHttpClientUtil.class.getSimpleName()+": StatusCode="+postMethod.getStatusCode());
			if (postMethod.getStatusCode() == 200)
			{
				

			}
			//resultVO.setSuccess(true);
		} catch (IOException e) 
		{
			e.printStackTrace();
			//resultVO.setMessage(e.toString());
			throw e;
		} finally {
			postMethod.releaseConnection();
		}

		return response;
	}

	
}