package cn.fomer.common.service;

import java.io.File;
import java.util.Map;


/**
 * Xml 解析器（JDK自带）
 * 
 * @date 2019-01-18
 */
public interface Xml {
	
	File getFile();
	/**
	 * 说明
	 * @date 202207
	 */
	Map<String, cn.fomer.common.service.Node> getAllNode();
	
	cn.fomer.common.service.Node getById(String id);
	
	/**
	 * 说明
	 * @date 202207
	 */
	boolean existNode(String nodeId) ;
	
	cn.fomer.common.service.Node getByProp(String id, String val);
	
	/**
	 * 说明
	 * @date 202207
	 */	
	void add(cn.fomer.common.service.Node n) ;
	

	
	/**
	 * 说明
	 * @date 202207
	 */	
	void addLine() ;
	
	/**
	 * 说明
	 * @date 202207
	 */	
	void addCommit(String text);
	
	/**
	 * 说明
	 * @date 202207
	 */	
	void saveAs(String path);
	
	/**
	 * 说明
	 * @date 202207
	 */	
	void save();
}
