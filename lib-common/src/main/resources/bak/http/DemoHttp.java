package cn.fomer.common.http;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 2019-06-27
 * 
 * 
 */
public class DemoHttp {

	public static void main(String[] args) {
		//
		String baofeng= "http://fastsoft.onlinedown.net/down/Baofeng16-9.03.0801.1111.exe";
		String mp4= "https://www94.bitporno.com/87/0/SeTqpMoZaqZ8LBhnXAVKZA/1572501472/190901/931G6KPVDSYX9PNOUNQOG.mp4";
		try
		{
			HttpURLConnectionUtil.download(mp4, new FileOutputStream("e:/1.exe"));
		}
		catch (Exception e)
		{ 
			e.printStackTrace(); 
		} 
		
		System.out.println("over");
		
	}
	
	public static void https() {
		// TODO Auto-generated method stub
		
		HttpClientUtil http= new HttpClientUtil();
		try
		{
			Map<String, String> data= new HashMap<>();
			Map<String, String> header= new HashMap<>();
			header.put("Authorization", "Bearer 1fb230aa-ff8e-4a08-bfaa-5f3c1fc5cff8");
			
			String url= "http://localhost:8040/order/find";
			String json= http.post(url,header,data );
			System.out.println(json);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
			e.printStackTrace();
		}

	}

}
