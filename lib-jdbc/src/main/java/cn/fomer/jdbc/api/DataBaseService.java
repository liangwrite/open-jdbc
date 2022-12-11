package cn.fomer.jdbc.api;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.fomer.jdbc.entity.ColumnTypeEnum;
import cn.fomer.jdbc.entity.DbTypeEnum;
import cn.fomer.jdbc.entity.Dialect;
import cn.fomer.jdbc.service.DataSourceSimple;
import cn.fomer.jdbc.service.MetaDataService;
import cn.fomer.jdbc.service.SummaryService;
import cn.fomer.jdbc.service.impl.CrudServiceImpl;

import lombok.Getter;
import lombok.Setter;

/**
 * 2018-12-14 高级数据库操作
 * 2020-11 分离出了Table对象
 * 2022-07 将CloneService融入了Table
 * 
 */
@Setter
@Getter
public abstract class DataBaseService extends CrudServiceImpl {
	
	protected String dbName;
	protected String schemaName;
	protected DbTypeEnum dbType;
	protected Dialect dialect;
	
	//表结构缓存
	protected Map<String, List<FieldService>> tableFieldCache= new ConcurrentHashMap<>();
	
	public DataBaseService(DataSourceSimple ds, boolean readonly)
	{
		super(ds, readonly);
		this.dbName= ds.getDbName();
		this.schemaName= ds.getSchemaName();
		this.dbType= ds.getDbType();
		this.dialect= ds.getDialect();
	}


	
	//基本
	public abstract Dialect getDialect();
	public abstract boolean isEnableLog();
	public abstract MetaDataService getMetaDataService();
	
	

	
	//SQLSummary summary(int top, int thread);
	/* 数据库相关 */
	public abstract List<String> getAllDataBase();
	public abstract String getDataBaseVersion();
	
	public abstract List<String> getViewList();
	public abstract List<String> getProcedureList();
	public abstract List<String> getFunList();
	
	
	public abstract List<String> getAllTypeOfDB();
	
	
	/* 表相关 */
	public abstract boolean hasTable(String table);
	public abstract List<TableService> getTableList();
	public abstract TableService getTable(String tableName);
	
	
	
	

	
	
	/* 数据 */
	public abstract List<Map<String, Integer>> clearAllTables(List<String> notList);
	
	/** 获取表数据量并且按照从高到低排序 */
	public abstract LinkedHashMap<String, Integer> getTopCount();
	
	public abstract List<String> getTableByFieldType(ColumnTypeEnum fieldType);
	public abstract List<String> getFieldTypeOfDB();
	
	/**
	 * 202103 表是否存在(不分区大小写)
	 */
	public abstract boolean existsTable(String table);
	
	public abstract int dropTable(String table) throws Exception;
	
	public abstract HugeQueryService hugeQuery();
	
	
	public abstract SummaryService getSummaryService();
	
	/**
	 * 说明
	 * @date 2022-07
	 */
	public abstract void createTableFromDiffrentDB(TableService src);
	
	
	/**
	 * 2022-01-25 <br />
	 * 默认表结构、表注释、字段注释、字段JDBC类型都会进行缓存。所以如果在程序运行期间，修改了表结构，就需要重新调用此方法。
	 * 
	 */
	public abstract void cleanCache();
	
	/**
	 * 说明
	 * @date 2022-07
	 */
	public abstract boolean tryConnnect();
}
