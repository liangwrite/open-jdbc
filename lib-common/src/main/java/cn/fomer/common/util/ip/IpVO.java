package cn.fomer.common.util.ip;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * 2019-09-09 表示网卡信息
 * 
 * 
 */
public class IpVO {

	String name;
	String displayName;
	List<String> ip4List;
	List<String> ip6List;
	
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDisplayName() {
		return displayName;
	}


	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}


	public List<String> getIp4List() {
		return ip4List;
	}


	public void setIp4List(List<String> ip4List) {
		this.ip4List = ip4List;
	}


	public List<String> getIp6List() {
		return ip6List;
	}


	public void setIp6List(List<String> ip6List) {
		this.ip6List = ip6List;
	}


	public IpVO()
	{
		ip4List= new ArrayList<>();
		ip6List= new ArrayList<>();
	}
}
