/**
 * 
 */
package cn.fomer.jdbc.service.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import cn.fomer.jdbc.api.FieldService;
import cn.fomer.jdbc.entity.ColumnTypeEnum;
import cn.fomer.jdbc.entity.DbTypeEnum;
import cn.fomer.jdbc.service.MetaDataService;

import lombok.Getter;

/**
 * 2021-04-14 通过meta数据获取表名、字段等信息（由于max不支持show tables等语句）
 * 
 */
@Getter
public class MetaDataServiceImpl implements MetaDataService {
	
	
	DataSource dataSource;
	
	
	String url;
	DbTypeEnum dbType;
	boolean supportBatchUpdate;
	
	public MetaDataServiceImpl(DataSource ds)
	{
		this.dataSource= ds;
		
		init();
	}
	
	
	
	/**
	 * 2021-04-17 通过重建一个连接，获取数据源的信息
	 */
	void init() {
		// TODO Auto-generated method stub
		Connection connection = null;
		try
		{
			connection = dataSource.getConnection();
			DatabaseMetaData metaData = connection.getMetaData();

			try
			{
				//hive不支持
				this.url= metaData.getURL();
			}
			catch(Exception e) 
			{ 
				//e.printStackTrace();
				System.err.println("警告：当前数据库不支持metaData.getURL()");
			}
			
			
			try
			{
				//批量更新
				this.supportBatchUpdate= metaData.supportsBatchUpdates();
			}
			catch(Exception e) { e.printStackTrace();}			

			this.dbType= parseDBType(metaData);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(connection!=null) 
			{
				try { connection.close();} catch(Exception e) { System.err.println("关闭connection时发生了异常！");}
			}
		}
	}
	
	
	@Override
	public List<String> getTableList() {
		// TODO Auto-generated method stub
		
		Connection connection = null;
		try
		{
			connection = dataSource.getConnection();
			DatabaseMetaData metaData = connection.getMetaData();
			
			ResultSet tableResultSet = metaData.getTables(null, null, null, new String[]{"TABLE", "USER"});
			ResultSetReaderImpl tableReader= new ResultSetReaderImpl(tableResultSet);
			//mysql
			if(getDbType()==DbTypeEnum.MySQL)
			{
				List<String> tableList = (List)tableReader.singleColumn("TABLE_NAME");
				return tableList;
			}
			if(getDbType()==DbTypeEnum.MaxCompute)
			{
				List<String> tableList = (List)tableReader.singleColumn("TABLE_NAME");
				return tableList;
			}
			
			
			return (List)tableReader.singleColumn();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
			return Collections.emptyList();
		}
		finally
		{
			if(connection!=null) 
			{
				try { connection.close();} catch(Exception e) { System.err.println("关闭connection时发生了异常！");}
			}
		}

	}

	@Override
	public List<FieldService> getAllField(String tableName) 
	{
		// TODO Auto-generated method stub
		List<FieldService> columnList= new ArrayList<FieldService>();
		
		Connection connection = null;
		try
		{
			connection = dataSource.getConnection();
			DatabaseMetaData metaData = connection.getMetaData();
			
			ResultSet columns = metaData.getColumns(null, null, tableName, null);
			
			//
			
			
			
			
			
			ResultSetReaderImpl columnReader= new ResultSetReaderImpl(columns);
			List<Map<String, Object>> rowList = columnReader.mapList();
			System.out.println(new Gson().toJson(rowList));
			if(dbType==DbTypeEnum.MaxCompute)
			{
				for(int i=0;i<rowList.size();i++)
				{
					FieldService fieldDetail= new FieldServiceImpl(null);
					columnList.add(fieldDetail);
				
					String type_name= String.valueOf(columnReader.get(i, "TYPE_NAME"));
					String nullable= String.valueOf(columnReader.get(i, "NULLABLE"));
					ColumnTypeEnum columnTypeEnum = ColumnTypeEnum.parse(type_name, DbTypeEnum.MaxCompute);
					
					
					fieldDetail.setCode(String.valueOf(columnReader.get(i, "COLUMN_NAME")));
					fieldDetail.setNullable("1".equals(nullable)?true:false);
					fieldDetail.setColumnType(columnTypeEnum);
					
				}
				
				
				//2.maxcompute partition field
				//data_province,STRING
				
				
			}
			if(dbType==DbTypeEnum.SQLite)
			{
				for(int i=0;i<rowList.size();i++)
				{
					FieldService fieldDetail= new FieldServiceImpl(null);
					columnList.add(fieldDetail);
					
					//0.
					Integer nullable= (Integer)columnReader.get(i, "NULLABLE");
					String DATA_TYPE= String.valueOf(columnReader.get(i, "DATA_TYPE")); //"4"
					String TYPE_NAME= String.valueOf(columnReader.get(i, "TYPE_NAME")); //"INTEGER"
					Integer ORDINAL_POSITION= (Integer)columnReader.get(i, "ORDINAL_POSITION");
					
					
					//1.
					fieldDetail.setCode(String.valueOf(columnReader.get(i, "COLUMN_NAME")));
					fieldDetail.setNullable((nullable==0)?false:true);
					fieldDetail.setColumnType(ColumnTypeEnum.parse(DATA_TYPE, dbType));
					fieldDetail.setOrdinalPosition(ORDINAL_POSITION);
					columnList.add(fieldDetail);
				}
				
				
				//2.maxcompute partition field
				//data_province,STRING
				
				
			}
						
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return Collections.emptyList();
		}
		finally
		{
			if(connection!=null) 
			{
				try { connection.close();} catch(Exception e) { System.err.println("关闭connection时发生了异常！");}
			}
		}
		
		return columnList;
	}

	@Override
	public List<String> getDbList() {
		// TODO Auto-generated method stub
		
		Connection connection = null;
		try
		{
			connection = dataSource.getConnection();
			DatabaseMetaData metaData = connection.getMetaData();
			
			ResultSet dbs = metaData.getCatalogs();
			ResultSetReaderImpl dbsReader= new ResultSetReaderImpl(dbs);
			return (List)dbsReader.singleColumn();
			
		}
		catch(Exception e)
		{
			return Collections.emptyList();
		}
		finally
		{
			if(connection!=null) 
			{
				try { connection.close();} catch(Exception e) { System.err.println("关闭connection时发生了异常！");}
			}
		}

	}
	

	DbTypeEnum parseDBType(DatabaseMetaData metaData) throws SQLException {
		// TODO Auto-generated method stub
		
		String databaseProductName = metaData.getDatabaseProductName(); //""

		if(StringUtils.isEmpty(databaseProductName))
		{
			return DbTypeEnum.Unkown;
		}
		if(databaseProductName.equalsIgnoreCase("MySQL")) //"MySQL"
		{
			return DbTypeEnum.MySQL;
		}
		else if(databaseProductName!=null&&databaseProductName.contains("MaxCompute")) //"MaxCompute/ODPS"
		{
			return DbTypeEnum.MaxCompute;
		}
		else if(databaseProductName!=null&&databaseProductName.contains("Hive")) //"Apache Hive"
		{
			return DbTypeEnum.Hive;
		}
		else if(databaseProductName!=null&&databaseProductName.equalsIgnoreCase("Oracle")) //"Oracle"
		{
			return DbTypeEnum.Oracle;
		}
		else if(databaseProductName!=null&&databaseProductName.equalsIgnoreCase("SQLite")) //"SQLite"
		{
			return DbTypeEnum.SQLite;
		}

		return DbTypeEnum.Unkown;
	}
	
	

	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		Connection connection = null;
		try
		{
			connection = dataSource.getConnection();
			DatabaseMetaData metaData = connection.getMetaData();
			
			String url = metaData.getURL();

			
			return url;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
			return null;
		}
		finally
		{
			if(connection!=null) 
			{
				try { connection.close();} catch(Exception e) { System.err.println("关闭connection时发生了异常！");}
			}
		}
	}



}
