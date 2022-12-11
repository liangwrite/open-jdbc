package cn.fomer.jdbc.entity;

import java.util.HashMap;
import java.util.Map;

import cn.fomer.common.service.Xml;
import cn.fomer.common.service.impl.XmlImpl;





/**
 * 20180729 Support many sql function
 * 
 *
 */
/**
 * 2021-03-15 SQL方言
 * 
 */
public class Dialect 
{
	Xml mysqlXml= new XmlImpl("dialect/mysql.xml");
	Xml oracleXml= new XmlImpl("dialect/oracle.xml");
	Xml kingbaseXml= new XmlImpl("dialect/kingbase.xml");
	Xml postgresXml= new XmlImpl("dialect/postgres.xml");
	
	DbTypeEnum dbType;
	
	
	
	
	
	
	Map<DbTypeEnum, String> mapQueryDatabase= new HashMap<DbTypeEnum, String>();
	
	
	Map<DbTypeEnum, String> mapQueryFieldOfDB= new HashMap<DbTypeEnum, String>();	
	Map<DbTypeEnum, String> mapQueryVersion= new HashMap<DbTypeEnum, String>();
	Map<DbTypeEnum, String> mapQueryCharset= new HashMap<DbTypeEnum, String>();
	Map<DbTypeEnum, String> mapQueryView= new HashMap<DbTypeEnum, String>();
	Map<DbTypeEnum, String> mapQueryProcedure= new HashMap<DbTypeEnum, String>();
	Map<DbTypeEnum, String> mapQueryProcText= new HashMap<DbTypeEnum, String>();
	Map<DbTypeEnum, String> mapQueryTotalFieldType= new HashMap<DbTypeEnum, String>();
	Map<DbTypeEnum, String> mapQuerySqlOfCreateTable= new HashMap<DbTypeEnum, String>();
	Map<DbTypeEnum, String> mapQueryIndex= new HashMap<DbTypeEnum, String>();
	Map<DbTypeEnum, String> mapQueryTrigger= new HashMap<DbTypeEnum, String>();
	Map<String, String> mapSpecialOfOracle= new HashMap<String, String>();
	
	{
		
		//查询版本号
		mapQueryVersion.put(DbTypeEnum.SQLServer, "select @@version;");
		mapQueryVersion.put(DbTypeEnum.Oracle, "select BANNER from v$version");
		mapQueryVersion.put(DbTypeEnum.MySQL, "select VERSION()");

		
		//查看Charset		
		mapQueryCharset.put(DbTypeEnum.Oracle, "select userenv('language') from dual;");
		
		//查询所有数据库
		mapQueryDatabase.put(DbTypeEnum.MySQL, "show databases;");
		mapQueryDatabase.put(DbTypeEnum.SQLServer, 
				"use master;select name from sys.databases where name not in ('master','msdb','tempdb','model');");
		mapQueryDatabase.put(DbTypeEnum.Oracle, "select name from v$database");
		mapQueryDatabase.put(DbTypeEnum.DB2, "list db directory"); /* DB2 Not Support with SQL */
		
		

		
		
		

		
		//查询视图
		mapQueryView.put(DbTypeEnum.Oracle, "select VIEW_NAME,TEXT from user_views");
		
		//查询存储过程
		mapQueryProcedure.put(DbTypeEnum.Oracle, "select distinct name from user_source");
		
		//查询存储过程内容
		mapQueryProcText.put(DbTypeEnum.Oracle, "select  text from user_source where name ='PROCEDURE_NAME'");
		
		//查看数据库用户表使用的所有字段类型
		mapQueryTotalFieldType.put(DbTypeEnum.Oracle, "select DATA_TYPE,count(DATA_TYPE) from user_tab_columns group by DATA_TYPE order by count(DATA_TYPE) desc");
		
		
		//查询建表语句
		mapQuerySqlOfCreateTable.put(DbTypeEnum.Oracle, "SELECT DBMS_METADATA.GET_DDL('TABLE','#TABLE') FROM DUAL");
		mapQuerySqlOfCreateTable.put(DbTypeEnum.MySQL, "show create table #TABLE");
		
		//查询索引
		mapQueryIndex.put(DbTypeEnum.MySQL, "show index from #TABLE");
		
		//查询触发器
		mapQueryTrigger.put(DbTypeEnum.MySQL, "select * from information_schema.triggers where TRIGGER_SCHEMA='#DATABASE' and event_object_table='#TABLE'");
		
		
		//oracle的特殊语法
		//mapSpecialOfOracle.put("oracle_getTableComment", oracleXml.getById("oracle_getTableComment").getNode().getTextContent());
		
	}
	


	public Dialect(DbTypeEnum dbType)
	{
		if(dbType==null)
		{
			//throw new SQLException("方言不能为null");
			System.err.println(getClass().getSimpleName()+": 错误 - 数据库类型不能为NULL");
		}
		
		this.dbType= dbType;
		
	}
	
	
	/**
	 * 2018-11-03 Some DB not support, example Oracle.
	 * 
	 */
	@Deprecated
	public String getAllDbNames(){ return mapQueryDatabase.get(dbType); }
	public String getDBNameByUrl(String url) {
		if(dbType==DbTypeEnum.MySQL)
		{
			//jdbc:mysql://123.139.58.106:51000/online_mia_verifier?zautoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false
			
			int end= url.indexOf("?");
			if(end==-1) end= url.length();
			int start= url.substring(0,end).lastIndexOf("/");
			return url.substring(start+1, end);			
		}
		return null;
	}
	
	
	
	/**
	 * 2021-10-08 参数不区分大小写
	 * 
	 * 如果是pgsql请传SCHEMA_NAME
	 */
	public String getTableList(String dbName)
	{ 
		//DB_NAME
		String sql = getDialectXml().getById("table_list").getNode().getTextContent();
		if(sql!=null) {
			if(!(this.dbType==DbTypeEnum.PostgreSQL)) {
				sql= sql.replace("DB_NAME", dbName);
			}		
			else {
				sql= sql.replace("SCHEMA_NAME", dbName);
			}
			
		}

		return sql;
		//0
		//查询所有表
		/**
		 * 要求：
		 * 1.单列（列名无所谓）
		 * 2.一定要排除系统表
		 *
		 */
		/*
		Map<DbTypeEnum, String> mapQueryTable= new HashMap<DbTypeEnum, String>();
		mapQueryTable.put(DbTypeEnum.MySQL, "show tables;");		
		mapQueryTable.put(DbTypeEnum.SQLServer, "select name from sysobjects where xtype='U';");
		mapQueryTable.put(DbTypeEnum.DB2, "select name from sysibm.systables where type='T' and creator='DB2ADMIN'");
		mapQueryTable.put(DbTypeEnum.Hive, "show tables");
		
Map<DbTypeEnum, String> mapQueryFieldOfTable= new HashMap<DbTypeEnum, String>();	
		//查询字段(column_name COLUMN_COMMENT)
		mapQueryFieldOfTable.put(DbTypeEnum.DB2, "SELECT colname,typename,length FROM SYSCAT.COLUMNS WHERE TABNAME='T_USERS' order by colno");
		//mapQueryField.put(DBType.Oracle, "select COLUMN_ID id,COLUMN_NAME name,DATA_TYPE type,DATA_LENGTH numlength,DATA_PRECISION precison,NULLABLE nullable,CHAR_LENGTH charlength,DATA_DEFAULT defaultVal from user_tab_columns where table_name = upper('TABLE_NAME') order by column_id");

		mapQueryFieldOfTable.put(DbTypeEnum.Hive, "desc TABLE_NAME");
		//(必须要有顺序 - 分区字段在后)
		mapQueryFieldOfTable.put(DbTypeEnum.MaxCompute, "select * from Information_Schema.COLUMNS where lower(table_name) = lower('TABLE_NAME')  order by is_partition_key asc, ordinal_position asc limit 500"); //必须要有顺序
		
		
		//1
		String string = mapQueryTable.get(dbType);
		return string;
		*/
	}
	

	public String getTopRow(String tableName, int top)
	{ 
		//0
		Map<DbTypeEnum, String> mapQueryTopRecord= new HashMap<DbTypeEnum, String>();	
		//查询前多少个记录
		mapQueryTopRecord.put(DbTypeEnum.MySQL, "select * from tableName limit 0,10;");
		mapQueryTopRecord.put(DbTypeEnum.SQLServer, "select top 10 * from tableName;");
		mapQueryTopRecord.put(DbTypeEnum.Oracle, "select * from \"tableName\" where rownum < 10"); /* Oracle末尾不能加分号 */
		mapQueryTopRecord.put(DbTypeEnum.DB2, "select * from tableName fetch first 10 rows only"); /* Oracle末尾不能加分号 */
		mapQueryTopRecord.put(DbTypeEnum.PostgreSQL, "select * from tableName limit 10 offset 0"); /* Oracle末尾不能加分号 */
		
		
		return mapQueryTopRecord.get(dbType)
				.replace("tableName", tableName)
				.replace("10", ""+((dbType==DbTypeEnum.Oracle)?(top+1):top));
		
		
	}
	public String getDbVersion(){ return mapQueryVersion.get(dbType); }	
	

	
	public String getViews(){ return mapQueryView.get(dbType); }
	public String getProcs(){ return mapQueryProcedure.get(dbType); }
	public String getProcText(){ return mapQueryProcText.get(dbType); }
	public String getTotalUserFieldType(){ return mapQueryTotalFieldType.get(dbType); }
	public String getTableDDL(String table)
	{
		String text= mapQuerySqlOfCreateTable.get(dbType);
		String sql= text.replace("#TABLE", table);
		return sql;
	}
	
	/**
	 * 2019-08-05
	 */
	public String getIndex(String table)
	{
		String text= mapQuerySqlOfCreateTable.get(dbType);
		String sql= text.replace("TABLE_NAME", table);
		return sql;
	}
	
	/**
	 * 2019-08-05
	 */
	public String getTrigger(String db,String table)
	{
		String text= mapQueryTrigger.get(dbType);
		String sql= text.replace("#DATABASE", db).replace("#TABLE", table);
		return sql;
	}
	
	
	/**
	 * 202012 查询数据库用了哪些类型的字段
	 */
	public String getFieldsOfDb(String dbname)
	{
		//SELECT DATA_TYPE FROM information_schema.columns WHERE table_schema = 'mia_center' group by DATA_TYPE
		mapQueryFieldOfDB.put(DbTypeEnum.MySQL, "SELECT DATA_TYPE FROM information_schema.columns WHERE table_schema = '#DB_NAME' group by DATA_TYPE order by DATA_TYPE");
		return mapQueryFieldOfDB.get(dbType).replace("#DB_NAME", dbname);
	}
	
	/**
	 * 202012 查询某个类型的字段在哪些表中
	 */	
	public String getTableByFieldType(String dbname,ColumnTypeEnum fieldTypeEnum)
	{
		//SELECT DATA_TYPE FROM information_schema.columns WHERE table_schema = 'mia_center' group by DATA_TYPE
		Map<DbTypeEnum, String> map= new HashMap<DbTypeEnum, String>();
		map.put(DbTypeEnum.Oracle, "select distinct TABLE_NAME from user_tab_columns where DATA_TYPE = upper('#FIELD_TYPE') order by table_name");
		map.put(DbTypeEnum.MySQL, "SELECT TABLE_NAME FROM information_schema.columns WHERE table_schema = '#DB_NAME' and data_type='#FIELD_TYPE' group by table_name order by TABLE_NAME");
		
		String sql= map.get(dbType)
				.replace("#DB_NAME", dbname)
				.replace("#FIELD_TYPE", fieldTypeEnum.toString());
		return sql;
	}
	
	
	
	/**
	 * 2021-03-15 获取oracle的主键
	 * @param table 忽略大小写
	 */
	public String getPrimaryKeyForOracle(String table)
	{
		return getDialectXml().getById("selectId").getNode().getTextContent().replace("TABLE_NAME", table);
	}
	
	/**
	 * 2022-01 获取oracle的字段注释
	 * @param table 忽略大小写
	 */
	/*
	public String getFieldCommentForOracle(String table)
	{
		
		return mapSpecialOfOracle.get("oracle_getFieldComment").replace("YOUR_TABLE", table);
	}
	
	*/
	
	/**
	 * 2022-03
	 */
	public String getFieldComment(String dbName, String table)
	{
		String sql= getDialectXml().getById("field_comment").getNode().getTextContent();
		return sql
				.replace("DB_NAME", dbName)
				.replace("TABLE_NAME", table);
	}	
	
	/**
	 * 2022-01 获取oracle的字段注释
	 * @param table 忽略大小写
	 */
	public String getTableComment(String dbName, String table)
	{
		String key= "table_comment";

		String sql= getDialectXml().getById(key).getNode().getTextContent();
		return sql
					.replace("DB_NAME", dbName)
					.replace("TABLE_NAME", table); //mapSpecialOfOracle.get("oracle_getTableComment").replace("YOUR_TABLE", table);
	}
	
	public String getFields(String dbName, String tableName)
	{ 
		String key= "field_list";
		String sql= getDialectXml().getById(key).getNode().getTextContent();
		
		if(sql==null) return null;
		
		sql= sql.replace("TABLE_NAME", tableName);
		sql= sql.replace("DB_NAME", dbName);
		return sql;
	}	
	
	
	/**
	 * 2022-01
	 */
	public Xml getDialectXml() {
		
		switch(dbType) {
			case MySQL:
				return mysqlXml;
			case Oracle:
				return oracleXml;
			case Kingbase:
				return kingbaseXml;
			case PostgreSQL:
				return postgresXml;
		}
		
		throw NotSupportError.throwError("不支持的数据库类型！"+dbType);
	}
	
	/**
	 * 2021-03-15 获取oracle的主键
	 * @param table 忽略大小写
	 */
	public String isPrimaryKeyForOracle(String table, String field)
	{
		return mapSpecialOfOracle.get("oracle_isPrimaryKey")
				.replace("YOUR_TABLE", table)
				.replace("YOUR_FIELD", field)
				;
	}
	
	/**
	 * 2021-03-21 分页语句
	 * @param pageSize 
	 * @param pageNo 第几页，从1开始
	 */
	public String getPageSQL(String table, int pageSize, int pageNo)
	{
		if(dbType==DbTypeEnum.Oracle)
		{
			String sqlTemplate= "select * from (select ROWNUM i,t.* from #{tableName} t) v where v.i > #{startIndex} and v.i <= #{endIndex}";
			int endIndex= pageSize*pageNo;
			int startIndex= pageSize*(pageNo-1);
			 String sql = sqlTemplate
					 .replace("#{tableName}", table)
					 .replace("#{startIndex}", Integer.toString(startIndex)).replace("#{endIndex}", Integer.toString(endIndex));
			return sql;			
		}

		return null;
	}
	
	
	/** 
	 * 202103 删除表
	 */
	public String getDropTableSQL(String table)
	{
		String sql= "drop table {TABLE}";
		sql= sql.replace("{TABLE}", table);
		return sql;
	}
	
	
}
