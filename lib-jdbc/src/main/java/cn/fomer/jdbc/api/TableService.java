package cn.fomer.jdbc.api;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.fomer.jdbc.entity.PKTypeEnum;
import cn.fomer.jdbc.service.impl.ResultSetReaderImpl;

/**
 * 2018-11-15 表相关操作
 * 
 * 
 */
public interface TableService
{
	String getName();
	String getComment();
	String getJavaCamelName();
	DataBaseService getDataBase();
	void updateComment(String comment);
	
	//void mapJavaEntity();
	TemplateService getTemplateService();
	

	/**
	 * 说明 从另外一个数据库
	 * @date 2022-07
	 */
	void createFieldFromDiffrentDB(FieldService src);
	
	/**
	 * 202011 根据主键删除
	 */
	int deleteById(Object id);
	
	
	//int deleteWhere(String where);
	
	
	/**
	 * 202103 删除表
	 */
	boolean drop();
	
	/**
	 * 202103 尝试删除表（不会抛出异常）
	 */	
	boolean tryDrop();
	
	/**
	 * 获取主键（只支持单主键）
	 */
	List<FieldService> getPrimaryKey();
	
	
	
	/**
	 * 2021-04-14 不分区大小写
	 */
	public Boolean existField(String name);
	
	
	/**
	 * 20210401
	 */
	public FieldService getField(String name);
	
	/**
	 * 2018-11-03 Oracle 返回都是大写
	 * 202103 hive的分区字段会显示两遍/max的分区字段只会显示一遍
	 * <p>maxcompute比较特殊，只能通过metadata的方式</p>
	 */
	public List<FieldService> getFieldList();
	
	
	public String getDDL();
	
	
	/**
	 * 获取前 n 条记录
	 */
	List<Map<String, Object>> top(int top);
	
	
	/**
	 * 获取前 n 条记录（查询summary时）
	 */
	ResultSetReaderImpl topInfo(int top);
	
	
	/**
	 * 查询表所有数据
	 */
	List<Map<String, Object>> all();
	int count();
	
	
	/**
	 * 2021-04-23 允许条件
	 */
	int count(String where);
	
	
	/**
	 * 清空表数据
	 */
	int clear();
	
	Map<String, Object> selectByPk(Object pk);
	List<Map<String, Object>> selectByWhere(String where);
	
	/**
	 * 202011 根据主键更新，无主键时报错。只有当dataMap中有的key，才会进行更新（自动判断主键）
	 * 
	 */	
	int updateByMap(Map<String, Object> dataMap);
	
	
	/**
	 * 2021-03-15 （必须定义了主键）
	 * 
	 */
	int updateByMap(List<Map<String, Object>> mapList);
	
	
	/**
	 * 202104
	 * @param mapList
	 * @param id
	 * @return
	 */
	Entry<Integer, String> updateByMapNoId(List<Map<String, Object>> mapList, String id);
	
	
	/**
	 * 202103
	 * @param targetCode 要更细的列名 
	 */
	int updateByMap(List<Map<String, Object>> mapList,String targetCode);
	
	
	/**
	 * 2018-12-10 只有当dataMap中有的key，才会进行更新（必须定义了主键）
	 * 2018-12-10 将JSON格式数据写入表
	 * 201803 hive要求拼全字段，即使为null的字段也必须出现
	 * 步骤：
	 * 1.检查数据
	 * 2.拼接SQL语句
	 * @param dataMap 该数据不一定和表结构一致
	 * 
	 */	
	int insertByMap(Map<String, Object> dataMap);
	
	/**
	 * 2021-03-15 向同一张表里批量插入<br /> 
	 * （必须定义了主键）<br />
	 * hive拼接values(),(); 因为hive不支持addBatch<br />
	 * maxcompute必须使用格式 INSERT INTO table VALUES (?, ?, ?)
	 */
	int insertByMap(List<Map<String, Object>> mapList);
	
	
	String getDbName();
	
	
	/**
	 * 202012 获取主键类型
	 */
	PKTypeEnum getPKType();
	
	
	/**
	 * 202103 无数据 支持oracle/hive/maxcompute
	 * 克隆表结构到其他表名
	 * 如果源表是分区表，目标表变为普通表
	 */
	boolean createTableAsTo(String totable);
	

	
	/**
	 * 2021-04-23 <br />
	 * 支持hive/maxcompute<br />
	 * 不完全支持oracle，相当于createTableAsTo<br />
	 */
	boolean createTableLikeTo(String totable);
	
	
	/**
	 * 2021-09-14 
	 */
	boolean hasFieldsIn(TableService another);
	
	
	/**
	 * 2022-01-25<br />
	 * SYS_MENU 菜单<br />
	 * id:菜单:Varchar:String:Bigdecimal 
	 * 
	 */
	String summaryText();
	
	/**
	 * 2022-01-25
	 */
	String summaryHtml();
	
}
