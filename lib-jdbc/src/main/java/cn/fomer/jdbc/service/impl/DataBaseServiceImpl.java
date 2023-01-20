package cn.fomer.jdbc.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.fomer.jdbc.api.DataBaseService;
import cn.fomer.jdbc.api.ColumnService;
import cn.fomer.jdbc.api.HugeQueryService;
import cn.fomer.jdbc.api.TableService;
import cn.fomer.jdbc.clone.service.impl.SQLFactory;
import cn.fomer.jdbc.entity.ColumnTypeEnum;
import cn.fomer.jdbc.entity.DbTypeEnum;
import cn.fomer.jdbc.entity.Dialect;
import cn.fomer.jdbc.service.DataSourceSimple;
import cn.fomer.jdbc.service.MetaDataService;
import cn.fomer.jdbc.service.SummaryService;

import lombok.Getter;
import lombok.Setter;

/**
 * 2018-11-03 强大的数据库操作对象
 * 2020-11 分离出了Table对象
 * 
 * 
 */
@Setter
@Getter
public class DataBaseServiceImpl extends DataBaseService
{


	
	/**
	 * @param ds
	 */
	public DataBaseServiceImpl(DataSourceSimple ds) {
		super(ds, false);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param ds
	 */
	public DataBaseServiceImpl(DataSourceSimple ds, boolean readonly) {
		// TODO Auto-generated constructor stub
		super(ds, readonly);
	}




	

	
//	public DataBaseImpl(DataSource ds, String dbName, DbTypeEnum dbTypeEnum)
//	{
//		super(ds, false);
//		this.dbName= dbName;
//		this.dbType= dbTypeEnum;	
//		this.dialect= new Dialect(dbTypeEnum);
//		
//	}

//	public DataBaseImpl(DataSource ds, String dbName, DbTypeEnum dbTypeEnum, boolean readonly)
//	{
//		super(ds,readonly);
//		this.dbName= dbName;
//		this.dbType= dbTypeEnum;	
//		this.dialect= new Dialect(dbTypeEnum);
//		this.schemaName= ds.getSchemaName();
//		
//	}
	
	
	/**
	 * 202104
	 */
//	public static DataBase newInstance(DataSource ds)
//	{
//		return newInstance(ds, false);
//	}
	
	
	/**
	 * 202104
	 */
//	public static DataBase newInstance(DataSource ds, boolean readonly)
//	{
//		return new DataBaseImpl(ds, ds.getDbName(), ds.geDbType(), readonly);
//	}
	
	
	/**
	 * 202104
	 */
//	public static DataBase newInstance(DataSource ds, String dbName, DbTypeEnum dbTypeEnum)
//	{
//		return new DataBaseImpl(ds, dbName, dbTypeEnum, false);
//	}
	

	
	/**
	 * 2018-11-03 some db not support
	 * 
	 */
	@Deprecated
	public List<String> getAllDataBase()
	{
		
		String text= new Dialect(dbType).getAllDbNames();
		//may be not support 
		if(text==null)
		{
			return new ArrayList<String>();
		}
		ResultSetReaderImpl sqlResult= executeQuery(text, new ArrayList<Object>());
		List<String> listDbName= new ArrayList<String>();		
		if(sqlResult==null)
		{
			listDbName.add("查询所有数据库失败！"); return listDbName;
		}
		
		List<Map<String, Object>> listMap= sqlResult.mapList();
		
		if(listMap!=null)
			for(int i=0;i<listMap.size();i++)
			{
				Map<String, Object> map= listMap.get(i);
				for(String key:map.keySet())
				{
					listDbName.add((String)map.get(key));
					break;
				}
				
			}
		return listDbName;
	}



	@Override
	public List<TableService> getTableList()
	{
		//1.max不支持sql查询列
		if(dbType==DbTypeEnum.MaxCompute)
		{
			List<TableService> tableList= new ArrayList<TableService>();
			List<String> textList = getMetaDataService().getTableList();
			for(String tableName: textList)
			{
				tableList.add(this.getTable(tableName));
			}
			return tableList;
		}
		

		//2 pgsql 通过schema
		String text= null;
		if(dbType==DbTypeEnum.PostgreSQL) {
			text= dialect.getTableList(schemaName);
		}
		else {
			text= dialect.getTableList(dbName);
		}
		
		
		
		
		//3
		ResultSetReaderImpl sqlResult= executeQuery(text);
		List<Map<String, Object>> listMap= sqlResult.mapList();
		List<TableService> tableList= new ArrayList<TableService>();
		if(listMap!=null)
			for(int i=0;i<listMap.size();i++)
			{
				Map<String, Object> map= listMap.get(i);
				for(String key:map.keySet())
				{
					//(String)map.get(key)
					String table_name= (String)map.get(key);
					tableList.add(this.getTable(table_name));
					break;
				}
				
			}
		return tableList;
	}

	/**
	 * 2018-11-03
	 * 
	 */
	public List<String> getAllTypeOfDB()
	{
		return null;
	}

	

	
	/**
	 * 2018-11-03
	 * 
	 */
	@Override
	public String getDataBaseVersion()
	{
		try
		{
			String text= dialect.getDbVersion();
			ResultSetReaderImpl result= executeQuery(text);
			String version= "";
			if(dbType==DbTypeEnum.Oracle)
			{
				for(Map<String, Object> row: result.mapList())
				{
					version+= row.get(result.getColumnNameList().get(0));
				}
				
			}
			else
			{
				version= (String)result.singleRowSingleColumn();
			}
			return version;
		}
		catch(Exception e)
		{
			//e.printStackTrace();
			
			System.err.println(getClass().getSimpleName()+": 获取版本号失败！");
		}
		
		return "unkown version";		
	}
	
	/**
	 * 2018-11-03
	 * 
	 */
	public List<String> getViews()
	{
		
		return null;
	}


	
	
	
	@Override
	public int dropTable(String table) throws SQLException
	{
		String text= "drop table "+table;
		int n= executeUpdate(text);
		return n;
	}
	
	/**
	 * 2019-08-06 即使不存在也不会报异常
	 */
	public int dropTableOfSilent(String table)
	{
		try
		{
			return dropTable(table);
		}
		catch(Exception e)
		{
			return 0;
		}
	}

	@Override
	public List<String> getViewList() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 2018-11-03
	 */
	@Override
	public List<String> getProcedureList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getFunList() {
		// TODO Auto-generated method stub
		return null;
	}





	/* (non-Javadoc)
	 * @see liang.jdbc.service.SuperSql#clearTables()
	 */
	@Override
	public List<Map<String, Integer>> clearAllTables(List<String> notList) {
		// TODO Auto-generated method stub
		List<Map<String, Integer>> mapList= new ArrayList<>();
		List<TableService> tableList = getTableList();
		for(TableService table:tableList)
		{
			if(notList.indexOf(table)==-1)
			{
				int count = table.clear();
				if(count>0) mapList.add(Collections.singletonMap(table.getName(), count));
			}
		}
		
		return mapList;
	}


	@Override
	public LinkedHashMap<String, Integer> getTopCount() {
		// TODO Auto-generated method stub
		class Paire{
			String table;
			Integer count;
		}
		
		List<TableService> tableList = getTableList();
		List<Paire> mapList= new ArrayList<>();
		for(TableService table:tableList)
		{
			Paire p= new Paire();
			try
			{
				p.count = table.count();
			}
			catch(Exception e) { continue;}
			p.table= table.getName();
			mapList.add(p);
		}
		
		mapList.sort(new Comparator<Paire>() {
			@Override
			public int compare(Paire o1, Paire o2) {
				// TODO Auto-generated method stub
				return o1.count==o2.count?0:(o1.count<o2.count?1:-1);
			}
		});
		
		LinkedHashMap<String, Integer> linkedHashMap= new LinkedHashMap<String, Integer>();
		for(Paire p:mapList)
		{
			linkedHashMap.put(p.table, p.count);
		}
		return linkedHashMap;
	}


	
	@Override
	public TableService getTable(String tableName)
	{
		return SQLFactory.getTableService(this, tableName);
	}


	@Override
	public List<String> getFieldTypeOfDB() {
		// TODO Auto-generated method stub
		String sql = dialect.getFieldsOfDb(dbName);
		List<String> typeList = (List)executeQuery(sql).singleColumn();
		return typeList;
	}
	
	/**
	 * 2019-01-15 根据字段类型，查找表
	 * 
	 */
	public List<String> getTableByFieldType(ColumnTypeEnum fieldType)
	{
		String text= dialect.getTableByFieldType(dbName, fieldType);
		List<String> tableList= (List)executeQuery(text).singleColumn();
		return tableList;
	}
	

	/**
	 * 2018-12-03 判断是否有表
	 * 
	 */
	public boolean hasTable(String table)
	{
		return existsTable(table);
	}
	

	@Override
	public boolean existsTable(String table) {
		// TODO Auto-generated method stub
		List<TableService> allTable = getTableList();
		for(TableService s:allTable)
		{
			if(s.getName().equalsIgnoreCase(table)) return true;
		}
		return false;
	}
	
	

	@Override
	public HugeQueryService hugeQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SummaryService getSummaryService() {
		// TODO Auto-generated method stub
		SummaryService summaryService= new SummaryServiceImpl(this);
		return summaryService;
	}

	@Override
	public Dialect getDialect() {
		// TODO Auto-generated method stub
		return dialect;
	}


	@Override
	public void cleanCache() {
		// TODO Auto-generated method stub
		this.getTableFieldCache().clear();
	}

	@Override
	public boolean tryConnnect() {
		// TODO Auto-generated method stub
		if(this.dataSource instanceof DataSourceSimple) {
			DataSourceSimple ds= (DataSourceSimple)this.dataSource;
			return ds.tryConnnect();
		}
		
		throw new RuntimeException("Not Support tryConnnect()!");
	}

	@Override
	public void createTableFromDiffrentDB(TableService src) {
		// TODO Auto-generated method stub
		
	}





	@Override
	public boolean isEnableLog() {
		// TODO Auto-generated method stub
		return false;
	}






	@Override
	public MetaDataService getMetaDataService() {
		// TODO Auto-generated method stub
		return null;
	}


}




