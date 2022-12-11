package cn.fomer.common.springcloud;

import java.util.List;

/**
 * 2020-07-06 
 *
 */
public interface ServiceUtil {

	
	/**
	 * 2020-07-06 返回 "web-service"
	 */
	List<String> getServiceList();
	
	
	/**
	 * 2020-07-06 返回 "192.168.50.83:8080"
	 */
	List<String> getInstanceList(String serviceId);
	
	
	
}
