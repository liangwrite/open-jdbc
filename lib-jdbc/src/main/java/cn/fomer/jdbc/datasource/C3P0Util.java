package cn.fomer.jdbc.datasource;

import javax.sql.ConnectionPoolDataSource;

import cn.fomer.jdbc.entity.DbTypeEnum;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 2018-11-02
 * 
 * 
 */
public class C3P0Util 
{

	
	/**
	 * 2018-11-03 Don't forget close.
	 * 
	 */
	public static ComboPooledDataSource CreateDataSource(String ip, Integer port, String username, String password, String dbName, DbTypeEnum dbType)
	{
		long from= System.currentTimeMillis();
		System.out.print(C3P0Util.class.getSimpleName()+": initial...");
		ComboPooledDataSource ds = new ComboPooledDataSource();
		try
		{
			ds.setDriverClass(DataSourceSimpleImpl.getDriverClass(dbType));
		}
		catch(Exception e){ e.printStackTrace(); return null;}
		//ds.setJdbcUrl(DataSourceFactory.g(ip, port, dbName, dbType));
		ds.setUser(username);
		ds.setPassword(password);
		ds.setInitialPoolSize(100); //20
		ds.setMinPoolSize(100); //10
		ds.setMaxPoolSize(200); //40
		ds.setAcquireIncrement(20); //number of create conn once
		ds.setMaxIdleTime(5*60); //if long than this, will be distroy	
		ds.setIdleConnectionTestPeriod(2*60); //every 10 minute check conn
		ConnectionPoolDataSource pool= ds.getConnectionPoolDataSource();
		System.out.println(C3P0Util.class.getSimpleName()+": initial...OK in "+(System.currentTimeMillis()-from)/1000+" seconds.");
		
		//状态
		//int num= ds.getNumIdleConnections(); //当前可用链接数量
		
		return ds;
	}
}
