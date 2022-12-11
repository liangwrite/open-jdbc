package cn.fomer.jdbc;

import cn.fomer.jdbc.api.TableService;

/**
 * 2018-11-03
 * 
 */
public interface JdbcClone 
{
	/* 其它 */
	Boolean support();
	
	
	/* 数据库相关 */
	void cloneView();
	void cloneFunOfOracle();
	void cloneProcedure();
	
	/* 表相关 */
	void cloneTable(String db,String table); //克隆表
	void cloneTableField(String table); //表字段
	boolean cloneTableStruct(String table); //克隆表结构
	
	void cloneTableData(String table,int top); //测试用，赋值前几条数据
	void cloneTableComment(String table);
	boolean cloneTableSequenceInOracle(String table); //Oracle
	boolean cloneTableConstraint(String table);
	boolean cloneTableIndex(String table);
	boolean cloneTableTrigger(String table);
	
	
	
	/**
	 * 202012 只负责插入数据
	 */
	int cloneTableData(TableService srcTable,TableService toTable); //表数据	
	
	

	
	
}
