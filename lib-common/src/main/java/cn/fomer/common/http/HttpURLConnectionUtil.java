package cn.fomer.common.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.bcel.generic.I2F;

import com.google.gson.Gson;
import com.mchange.io.FileUtils;

/**
 * 2019-05-12 JDK自带，不需要外部包
 * 
 * 
 */
public class HttpURLConnectionUtil
{
	//static
	final static int bufferSize=10*1024;	
	static int connect_timeout=1000*10;	
	static int read_timeout=1000*20;
	
	public IaOnTransferringListener onUpingListener;
	public IaOnTransferringListener onDowningListener;
	//private boolean success=false;
	
	URLConnection urlConnection;
	
	static
	{
		try
		{
			//信任所有SSL证书
			//trustAllSSL();
		}
		catch(Exception e){ e.printStackTrace();}
	}
	/**
	 * 2019-04-10
	 * @throws Exception 
	 * 
	 */
	public static void main(String[] args) throws Exception {
		//
		get("http://192.168.10.10:8040/log/info?message=java");
		
	}

	/**
	 * 2019-05-18 信任所有https证书
	 * 
	 */
	static void trustAllSSL() throws KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException
	{
		//
		TrustManager[] trustManagers = {new X509TrustManager() {

			@Override
			public void checkClientTrusted(X509Certificate[] var1, String var2) throws CertificateException {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void checkServerTrusted(X509Certificate[] var1, String var2) throws CertificateException {
				// TODO Auto-generated method stub
				
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				// TODO Auto-generated method stub
				return null;
			} }};
		
		//SSLContext sslcontext = SSLContext.getInstance("SSL", "SunJSSE");//第一个参数为协议,第二个参数为提供者(可以缺省)
		SSLContext sslcontext = SSLContext.getInstance("SSL");//在android平台加第二个参数会报错
		sslcontext.init(null, trustManagers, new SecureRandom());
		HostnameVerifier hostnameVerifier = new HostnameVerifier() {
			public boolean verify(String s, SSLSession sslsession)
			{
				return true;
			}
		};
		
		HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
		HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
		
	}
	
	/**
	 * 
	 * 2018-01-01 data is what to send, url is the jsp page that will recieve data
	 *
	 */
	public byte[] upload(String destUrl,byte[] data)
	{			
		byte[] buffer=new byte[bufferSize];
		
		try
		{		
			//1 ready
			URL url=new URL(destUrl);	//set request		
			HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
			httpURLConnection.setReadTimeout(read_timeout);
			httpURLConnection.setConnectTimeout(connect_timeout);
			httpURLConnection.setChunkedStreamingMode(bufferSize);//to control progress
			
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("enctype","multipart/form-data");			
			
			//2 upload
			OutputStream outputStream= httpURLConnection.getOutputStream();	
			ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(data);
			
			int nTotal=data.length,nHasUp=0,nHasDown=0,n=0;
			while((n=byteArrayInputStream.read(buffer))>0)
			{
				outputStream.write(buffer,0,n);
				outputStream.flush();
			
				nHasUp+=n;
				if(onUpingListener!=null)
				{
					onUpingListener.onTransferring(nHasUp);
					onUpingListener.onTransferring(1f*nHasUp/nTotal);						
				}
			}
			outputStream.close();	
			byteArrayInputStream.close();
			
			//3 download
			InputStream inputStream=httpURLConnection.getInputStream();	//recieve
			ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream(bufferSize);

			nTotal=0; nHasDown=0; 
			while((n=inputStream.read(buffer))!=-1)	
			{
				byteArrayOutputStream.write(buffer,0,n);

				nHasDown+=n;
				if(onDowningListener!=null)
				{ 
					
					onDowningListener.onTransferring(nHasDown);
					//onDowningListener.onTransferring(percent)
				}
			}			
			inputStream.close();
			byte[] array=byteArrayOutputStream.toByteArray();
			return array;
		}
		catch(Exception e)
		{ e.printStackTrace(); return new byte[0]; }

		
	}	
	
	public interface IaOnTransferringListener
	{
		public void onTransferring(float percent); 
		public void onTransferring(int hasComplete);
	}

	/**
	 * 
	 * 2019-05-12 支持http或https
	 *
	 */
	public static void download(String url,OutputStream out) throws Exception 
	{
		//
		
		HttpURLConnection httpUrlConnection= (HttpURLConnection)new URL(url).openConnection();;
        try 
		{
            //打开连接
            httpUrlConnection.setConnectTimeout(connect_timeout);
            httpUrlConnection.setReadTimeout(read_timeout);
			httpUrlConnection.setRequestMethod("GET");
			httpUrlConnection.setInstanceFollowRedirects(false);
			
			long full= 30765984;
			httpUrlConnection.setRequestProperty("Range", "bytes="+(full-3)+"-"+(full-1));//设置头信息属性,拿到指定大小的输入流
            httpUrlConnection.connect();
            
            
            
            //206 支持多线程下载
            int responseCode= httpUrlConnection.getResponseCode();
            if(206 == responseCode)
            {
            	System.out.println("succport thread.");
            }
            if(200 == responseCode||206 == responseCode)
            {
                //得到输入流
                InputStream inputStream = null;
                try
                {
                	inputStream= httpUrlConnection.getInputStream();
                }
                catch(Exception e)
                { 
                	e.printStackTrace();
                	return;
                }
                
                
                
                
                
                byte[] buffer = new byte[10*1024];
                int len = 0;
                while(-1 != (len = inputStream.read(buffer)))
                {
                    out.write(buffer,0,len);
                    System.out.println("HttpURLConnectionUtil.download()="+len);
                }
                inputStream.close();
                out.close();
            }
            else
            {
            	
            	throw new Exception("ResponseCode == "+responseCode);
            }
        }
        finally
        {
        	//
        	httpUrlConnection.disconnect();
        	out.close();
        }
	}

	public String post(String url, Map<String, String> data) throws Exception 
	{
		// TODO Auto-generated method stub	
		//通用参数
		String content_type= "application/x-www-form-urlencoded; charset=UTF-8";
		//if(formType==HttpFormType.JSON) content_type= "application/json; charset=UTF-8";
		//else if(formType==HttpFormType.FILE) content_type= "multipart/form-data; charset=UTF-8";
		
		//Cookie
		CookieManager cookieManager= new CookieManager();
		//cookieManager.
		
		//请求
		try
		{		
			//1 ready
			HttpURLConnection httpURLConnection=(HttpURLConnection)new URL(url).openConnection();
			httpURLConnection.setReadTimeout(read_timeout);
			httpURLConnection.setConnectTimeout(connect_timeout);
			httpURLConnection.setChunkedStreamingMode(bufferSize);//to control progress
			
			httpURLConnection.setDoInput(true); //post必须
			httpURLConnection.setDoOutput(true); //post必须
			
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Content-Type", content_type);
			//httpURLConnection.setRequestProperty("Cookie", "uvc=8%7C20; loc=MDAwMDBBU0NOQkoyMTk5Mjk2NDAwMTAwMDBDSA==");
			
			//2 upload
			OutputStream outputStream= httpURLConnection.getOutputStream();	
			String body= "";
			for(Entry<String, String> entry:data.entrySet())
			{
				body+= entry.getKey()+"="+URLEncoder.encode(entry.getValue(),"utf-8")+"&";
			}
			if(body.endsWith("&"))
			{
				body= body.substring(0, body.length()-1);
			}
					
			ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(body.getBytes("utf-8"));
			
			int n= 0;
			byte[] buffer=new byte[bufferSize];
			while((n=byteArrayInputStream.read(buffer))>0)
			{
				outputStream.write(buffer,0,n);
				outputStream.flush();
			}
			outputStream.close();	
			byteArrayInputStream.close();
			
			//3 download
			InputStream inputStream= null;
			try
			{
				inputStream=httpURLConnection.getInputStream();	//recieve
			}
			catch(Exception e)
			{
				e.printStackTrace();
				
			}
			ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream(bufferSize);

			while((n=inputStream.read(buffer))!=-1)	
			{
				byteArrayOutputStream.write(buffer,0,n);


			}			
			inputStream.close();
			byte[] array=byteArrayOutputStream.toByteArray();
			byteArrayOutputStream.close();
			//success=true;
			String response= new String(array,"utf-8");
			return response;
		}
		catch(Exception e)
		{ 
			e.printStackTrace(); 
			return ""; 
		}

	}

	public static String get(String url)
	{
		// TODO Auto-generated method stub	
		//通用参数
		String content_type= "application/x-www-form-urlencoded; charset=UTF-8";
		
		//请求
		try
		{		
			//1 ready
			HttpURLConnection httpURLConnection=(HttpURLConnection)new URL(url).openConnection();
			httpURLConnection.setReadTimeout(read_timeout);
			httpURLConnection.setConnectTimeout(connect_timeout);
			httpURLConnection.setChunkedStreamingMode(bufferSize);//to control progress
			
			httpURLConnection.setDoInput(true); //post必须
			httpURLConnection.setDoOutput(true); //post必须
			
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setRequestProperty("Content-Type", content_type);
			if(httpURLConnection.getResponseCode()==200)
			{
				byte[] buffer= new byte[1024];
				int n= 0;
				InputStream inputStream= httpURLConnection.getInputStream();
				n= inputStream.read(buffer);
				inputStream.close();
				return new String(buffer,0,n);
			}
			
			return Integer.toString(httpURLConnection.getResponseCode());
		}
		catch(Exception e)
		{ 
			e.printStackTrace(); 
			return e.toString(); 
		}
		
	}
	

	

}//class
