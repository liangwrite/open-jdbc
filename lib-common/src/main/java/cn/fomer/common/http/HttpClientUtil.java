package cn.fomer.common.http;


import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


/**
 * 2019-10-31 根据httpclient4.5.2
 *
 */
public class HttpClientUtil
{
	public int connect_timeout=1000*10; /* 链接超时 */
	public int socket_timeout=1000*10; //
	public int read_timeout=1000*20;
	public Exception exception;
	public int StatusCode= -1; //每次完成网络请求，都会获取一个响应码
	byte[] buffer= new byte[1024*100];
	int buffer_len= 0;
	
	public static void main(String[] args) throws Exception {
		//
		String url= "http://www.winrar.com.cn/images/521.jpg";
		HttpClientUtil http= new HttpClientUtil();
		//String response= http.jsonPost(url, IHttp.data);
		String response= http.get(url);
		System.out.println(response);
	}
	public String get(String url)
	{		
		try
		{		
			
			HttpClient httpClient= HttpClients.createDefault();
			RequestConfig requestConfig= RequestConfig.custom()
					.setConnectTimeout(connect_timeout)
					.setSocketTimeout(socket_timeout)
					.setConnectionRequestTimeout(read_timeout)					
					.build();			
			
			HttpGet httpGet= new HttpGet(url);
			httpGet.setConfig(requestConfig);
			
			HttpResponse httpResponse= httpClient.execute(httpGet);
			StatusCode=httpResponse.getStatusLine().getStatusCode();
			if(StatusCode==HttpStatus.SC_OK){} else{} /* HttpStatus.SC_OK:请求成功，一切正常; 404:请求的资源不可用; */			
			String html=EntityUtils.toString(httpResponse.getEntity());
			return html;		
		}
		catch (Exception e) { this.exception= e; e.printStackTrace(); return null;} 		
	}	 
	
	
	public String post(String url, Map<String, String> header, Map<String, ?> data) throws Exception
	{		
		//
		List<NameValuePair> nameValuePairList= new ArrayList<NameValuePair>();
		if(data!=null)
		{
			for(Entry<String, ?> entry:data.entrySet())
			{
				NameValuePair nameValuePair= new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue()));
				nameValuePairList.add(nameValuePair);
			}
		}
		
		try
		{
			HttpClient httpClient= HttpClients.createDefault();
			
			RequestConfig requestConfig= RequestConfig.custom()
					.setSocketTimeout(connect_timeout)
					.setConnectTimeout(connect_timeout)
					.setConnectionRequestTimeout(read_timeout)					
					.build();			

			HttpPost httpPost= new HttpPost(url);	
			
			httpPost.setConfig(requestConfig);
			
			//
			if(header!=null)
			{
				for(String key:header.keySet())
				{
					httpPost.addHeader(key, header.get(key));
				}
			}
			
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList,"utf-8"));
			HttpResponse response = httpClient.execute(httpPost);
			StatusCode=response.getStatusLine().getStatusCode();
			if(StatusCode ==HttpStatus.SC_OK){} else{}	/* HttpStatus.SC_OK:请求成功，一切正常; 404:请求的资源不可用; */ 
			String html = EntityUtils.toString(response.getEntity());				
			return html;			
		}
		catch (Exception e) { this.exception= e; e.printStackTrace(); return null;} 
	}

	
	public void download(String url,String path) throws Exception
	{		
		//
		
		try
		{
			HttpClient httpClient= HttpClients.createDefault();
			
			RequestConfig requestConfig= RequestConfig.custom()
					.setSocketTimeout(connect_timeout)
					.setConnectTimeout(connect_timeout)
					.setConnectionRequestTimeout(read_timeout)					
					.build();			

			HttpPost httpPost= new HttpPost(url);	
			
			httpPost.setConfig(requestConfig);
			
			//
			HttpResponse response = httpClient.execute(httpPost);
			StatusCode=response.getStatusLine().getStatusCode();
			if(StatusCode ==HttpStatus.SC_OK){} else{}	/* HttpStatus.SC_OK:请求成功，一切正常; 404:请求的资源不可用; */ 
			InputStream inputStream = response.getEntity().getContent();
			FileOutputStream fileOutputStream= new FileOutputStream("");
			while((buffer_len=inputStream.read(buffer))!=-1)
			{
				fileOutputStream.write(buffer,0,buffer_len);
			}
			fileOutputStream.close();
			fileOutputStream.close();
			
		}
		catch (Exception e) { this.exception= e; e.printStackTrace(); } 
	}


	public String jsonPost(String url, String json, Map<String, String> header) throws Exception {
		// TODO Auto-generated method stub
		HttpClient httpclient= HttpClients.createDefault();
        HttpPost httpPost= new HttpPost(url);
        
        
		if(header!=null)
		{
			for(String key:header.keySet())
			{
				httpPost.addHeader(key, header.get(key));
			}
		}   
		
        try 
        {
            StringEntity stringEntity = new StringEntity(json,"UTF-8");
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");//发送json数据需要设置contentType
            httpPost.setEntity(stringEntity);
            
            
            
            HttpResponse httpResponse = httpclient.execute(httpPost);
            if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
            {
                String result = EntityUtils.toString(httpResponse.getEntity());// 返回json格式：
                return result;
            }
            else
            {
            	throw new Exception("statusCode = "+httpResponse.getStatusLine().getStatusCode());
            }
        } 
        catch(Exception e) {
        	e.printStackTrace();
        	throw e;
        }
	}
		
}
