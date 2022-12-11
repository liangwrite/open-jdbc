package cn.fomer.common.util;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.google.gson.Gson;
import cn.fomer.common.util.ip.IpVO;

/**
 * 2019-09-09 获取本机所有ip
 * 情况：
 * 1.有多个网卡
 * 2.每个网卡有多个ip4地址
 * 3.每个网卡还有多个ip6地址
 * 4.有的网卡连接了网线，但是也可能没有分配到ip地址
 * 
 */
public class Ip {

	static List<IpVO> ipList= new ArrayList<>();

	static
	{
		try{ init();}catch(Exception e){ e.printStackTrace();}
	}
	

	/**
	 * 
	 * 2019-09-09 获取ip
	 *
	 */
	static void init() throws UnknownHostException, SocketException {

		//1.获取所有网卡数量
		//2.获取每个网卡的ip4、ip6地址
		Enumeration<NetworkInterface> interface_enumeration= NetworkInterface.getNetworkInterfaces();
		while(interface_enumeration.hasMoreElements()) 
		{
			
			NetworkInterface networkInterface= interface_enumeration.nextElement();
			
			IpVO vo= new IpVO();
			vo.setName(networkInterface.getName()); //"lo"、"ppp0"、"net1"、"net2"、
			vo.setDisplayName(networkInterface.getDisplayName()); //"Intel(R) Ethernet Connection (3) I218-V"、"Realtek RTL8723BE Wireless LAN 802.11n PCI-E NIC"
			
			if("lo".equals(vo.getName())) continue; //"lo"回环网卡
			ipList.add(vo);
			
			//遍历该网卡的每个ip
			Enumeration<InetAddress> inetAddress_enumeration= networkInterface.getInetAddresses();
			while(inetAddress_enumeration.hasMoreElements())
			{
				InetAddress inetAddress= inetAddress_enumeration.nextElement();
				
				if(inetAddress instanceof Inet4Address)
				{
					vo.getIp4List().add(inetAddress.getHostAddress());
				}
				if(inetAddress instanceof Inet6Address)
				{
					vo.getIp6List().add(inetAddress.getHostAddress());
				}
			}
		}
	}
	
	
	/**
	 * 2019-09-09 获取网卡数量
	 * 
	 */
	public static int getCardNum()
	{
		return ipList.size();
	}
	/**
	 * 2019-09-09 获取所有ip4地址
	 * 
	 */
	public static List<String> getIp4List()
	{
		List<String> ip4= new ArrayList<>();
		for(IpVO vo:ipList)
		{
			ip4.addAll(vo.getIp4List());
		}
		return ip4;
	}
	/**
	 * 2019-09-09 获取所有ip6地址
	 * 
	 */
	public static List<String> getIp6List()
	{
		List<String> ip6= new ArrayList<>();
		for(IpVO vo:ipList)
		{
			ip6.addAll(vo.getIp6List());
		}
		return ip6;
	}
	
	public static void main(String[] args) {
		//System.out.println(new Gson().toJson(getIp4List()));
		System.out.println(new Gson().toJson(ipList));
	}
	
}
