/**
 * 
 */
package cn.fomer.jdbc.api;

/**
 * 2022-01-20 创建各种模板
 * 
 */
public interface TemplateService {
	
	/**
	 * 映射java实体
	 */
	void mapEntity();
	void mapMapper();
	void mapService();
	void mapController();
	void mapMapperXml();
	
	
	void setAuthor(String name);

	
}
