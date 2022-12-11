package cn.fomer.jdbc.datasource;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * 2021-09-14
 * 
 */
public class SQLiteDataSource {
	String connectUrl;
	
	SQLiteDataSource(File file){
		try
		{
			String path = file.getCanonicalPath();
			connectUrl = "jdbc:sqlite:dbName.db".replace("dbName.db", path);				
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	
	}
	
	public Connection getConnection()
	{
		//2.创建连接
		//System.out.println("创建连接...");
		try
		{
			String className= "org.sqlite.JDBC";
			Class.forName(className).newInstance(); // 捕获异常ClassNotFoundException
			Connection connection = DriverManager.getConnection(connectUrl); // 捕获异常SQLException
			
			return connection;
		} catch (Exception e)
		{
			e.printStackTrace();
			
			throw new RuntimeException(e.getMessage());
		}
	}	
}
