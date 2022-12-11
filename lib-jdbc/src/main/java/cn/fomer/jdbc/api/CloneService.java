package cn.fomer.jdbc.api;

/**
 * 2018-11-03 数据库之间数据迁移
 * 
 */
public interface CloneService 
{
	/* 其它 */
	Boolean support();
	
	
	/* 数据库 */
	void cloneView();
	void cloneFunOfOracle();
	void cloneProcedure();
	
	/* 表 */
	//void cloneTable(String db,String table); //克隆表
	void cloneTableField(TableService srcTable, TableService toTable); //表字段
	boolean cloneTableStruct(String table); //克隆表结构
	
	int cloneTableData(String table,int top); //测试用，赋值前几条数据
	
	
	
	void cloneTableComment(TableService srcTable, TableService toTable);
	boolean cloneTableSequenceInOracle(String table); //Oracle
	boolean cloneTableConstraint(String table);
	boolean cloneTableIndex(String table);
	boolean cloneTableTrigger(String table);
	
	
	
	/**
	 * 202012 只负责插入数据 <br />
	 * 0.判断两边表字段是否一致 <br />
	 * 1.把目标表清空 <br />
	 * 2.把数据复制过去 <br />
	 */
	int cloneTableData(TableService srcTable,TableService toTable); //表数据	
	
	
}
