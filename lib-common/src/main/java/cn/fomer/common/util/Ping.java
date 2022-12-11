package cn.fomer.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 2019-02-10
 * 
 * 
 */
public class Ping {
	
	public static void main(String[] args) throws InterruptedException {
		//
		Ping ping= new Ping();
		List<String> list = ping.syncOnLine("192.168.1", 2, 254);
		System.out.println(list.toString());
		System.out.println("over");
	}
	
	public boolean log= false;
	final int max_thread_count= 40;
	
	final int max_ip_per_thread= (254%max_thread_count==0)?(254/max_thread_count):(254/max_thread_count+1);
	
	/**
	 * 2019-02-10  多线程方式
	 * @param ipPart 192.168.1
	 * @param start 2
	 * @param end 254
	 * 
	 */
	public List<String> syncOnLine(final String ipPart,Integer start, Integer end) throws InterruptedException
	{
		//
		final List<String> onList= Collections.synchronizedList(new ArrayList<String>(10));
		
		//1.需要多少线程
		int count= end-start+1;
		int max_thread= (count%max_ip_per_thread==0)?count/max_ip_per_thread:count/max_ip_per_thread+1;
		
		final CountDownLatch countDownLatch= new CountDownLatch(max_thread);

		//2.ip分段检测
		for(int i=start;;)
		{
			//
			final int tmpStart= i;
			i+= max_ip_per_thread;
			
			boolean quit= false;
			if(i>end)
			{
				i= end;
				quit= true;
			}
			else if(i==end)
			{
				quit= true;
			}
			
			final int tmpEnd= i;
			Thread thread= new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try
					{
						List<String> partList = isOnLine(ipPart, tmpStart, tmpEnd-1);
						onList.addAll(partList);	
						
						
						//countDownLatch.await();
						
						System.out.println(new DecimalFormat("00").format(Thread.currentThread().getId())+" quit.");
					}
					catch(Exception e){ e.printStackTrace();}
					finally
					{
						countDownLatch.countDown();
					}
					
				}
			});
			
			thread.start();
			if(quit) break;
		}
		
		countDownLatch.await();
		System.out.println("主线程退出("+max_thread+"个子线程)...");

		Collections.sort(onList); //多个线程返回
		return onList;
		
	}
	
	
	/**
	 * 2019-02-10
	 * @param ipPart e.g. 192.168.0
	 * @throws IOException 
	 * 
	 */
	public List<String> isOnLine(String ipPart,Integer start, Integer end)
	{
		List<String> ipList= new ArrayList<String>();
		for(int i=start;i<=end;i++)
		{
			String ip= ipPart+"."+i;

			boolean isLive= false;
			try{ isLive= isOnLine(ip);}catch(Exception e){ e.printStackTrace();}
			if(isLive) ipList.add(ip);

		}
		
		
		return ipList;
	}
	
	
	/**
	 * 
	 * @param ip eg 192.168.137.2
	 * @throws IOException 
	 * */
	boolean isOnLine(String ip) throws IOException
	{
		
		Runtime runtime= Runtime.getRuntime();
		final String commond= "ping :IP -n 1 -w 100";		
		String cmd= commond.replace(":IP", ip);
		
		Process process= runtime.exec(cmd); //IOException,"系统找不到指定的文件。"
		InputStream inputStream= process.getInputStream();
		ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
		
		byte[] buffer= new byte[1*200*1024]; //0.2M缓存
		int len=0;
		while((len=inputStream.read(buffer))!=-1) //IOException
		{
			byteArrayOutputStream.write(buffer, 0, len);			
		}
		
		inputStream.close(); //IOException
		
		String text= new String(byteArrayOutputStream.toByteArray());
		byteArrayOutputStream.close(); //IOException
		if(log) System.out.print(cmd);
		if(text.contains("回复"))
		{

			if(log) System.out.println(" —— OK");
			return true;
		}
		else
		{
			if(log) System.out.println();
		}

		return false;
		
	}
	
}
