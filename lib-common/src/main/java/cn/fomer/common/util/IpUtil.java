package cn.fomer.common.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.docx4j.model.datastorage.XPathEnhancerParser.main_return;


/**
 * @author Liang
 * 20170701 08:00
 *
 */
public class IpUtil {
    public static long toLong(String ipStr) 
    {  
    	if(isIp(ipStr)!=true) return 0;
    		
        String[] arr = ipStr.split("\\."); //如果用“.”作为分隔的话，必须是如此写法：[192,168,0,1]  
       
        long result = 0;  
        for (int i = 0; i < arr.length; i++) {  
       
            int power = 3 - i;  
            int ip = Integer.parseInt(arr[i]); //192  
            result += ip * Math.pow(256, power);         
        }         
        return result;        
    }  	
    
    public static boolean isIp(String ipStr)
    {
    	String regex= "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
    	if(ipStr!=null&&ipStr.matches(regex)==true) return true;
    	return false;
    }
    
    public static String toIp2(String ip)
    {

		String bin_ip= "";


		if(isIp(ip))
		{

			String[] arr_ip= ip.split("\\.");
			 
			for(String sn:arr_ip)
			{
				Integer n= null;
				try
				{
					n= Integer.valueOf(sn);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
				String binaryString= Integer.toBinaryString(n);
				binaryString= String.format("%8s", binaryString).replace(" ", "0");
				bin_ip+= binaryString;
				bin_ip+= ".";
			}
			
			if(bin_ip.endsWith("."))
			{
				bin_ip= bin_ip.substring(0, bin_ip.length()-1);
			}
		}
		
		return bin_ip;
    	
    }
    
    public static String local() throws UnknownHostException
    {
    	String ip = Inet4Address.getLocalHost().getHostAddress();
    	return ip;
    }
    
    
    public static void main(String[] args) throws UnknownHostException {
		System.out.println(local());
	}

}
