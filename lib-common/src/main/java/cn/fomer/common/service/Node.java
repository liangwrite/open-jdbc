/**
 * 
 */
package cn.fomer.common.service;


/**
 * 
 *
 * @author Liang
 * @email liang@163.com
 * @date 2022-07-12
 */
public interface Node {

	String getId();
	org.w3c.dom.Node getNode();
	

	/**
	 * 说明
	 * @date 202207
	 */	
	String text() ;	
}
