package cn.fomer.jdbc.datasource;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import cn.fomer.jdbc.entity.DbTypeEnum;
import cn.fomer.jdbc.entity.Dialect;
import cn.fomer.jdbc.entity.NotSupportError;
import cn.fomer.jdbc.entity.PostGresqlConfig;
import cn.fomer.jdbc.service.DataSourceSimple;

/**
 * 2018-11-02 简易数据源
 * 
 * 
 */
public class DataSourceSimpleImpl extends DataSourceSimple 
{
	//
	//1.传进来的

	
	
	//
	static String getUrl(DbTypeEnum dbType)
	{
		Map<DbTypeEnum, String> mapConnectUrl = new HashMap<DbTypeEnum, String>();
		mapConnectUrl.clear();
		
		String dbUrlSQLServer = "jdbc:sqlserver://127.0.0.1:1433; DatabaseName=dbName";
		//String dbUrlMySQL = "jdbc:mysql://127.0.0.1:3306/dbName?useSSL=false";
		String dbUrlMySQL = "jdbc:mysql://127.0.0.1:3306/dbName?useSSL=false&serverTimezone=UTC";
		String dbUrlOracle = "jdbc:oracle:thin:@127.0.0.1:1521:dbName";
		String dbUrlDB2 = "jdbc:db2://127.0.0.1:50000/dbName";
		String dbUrlSQLite = "jdbc:sqlite:dbName.db"; //"jdbc:sqlite:E:/workspace/testdatabase.db";
		String dbUrlDM = "jdbc:dm://192.168.50.9:5236/dbName";
		String dbUrlHive = "jdbc:hive2://127.0.0.1:10000/dbName";
		String dbUrlMax = "jdbc:odps:http://service.cn.maxcompute.aliyun.com/api?project=dbName";
		//String dbUrlPostgreSQL = "jdbc:postgresql://127.0.0.1:5432/dbName";
		String dbUrlPostgreSQL = "jdbc:postgresql://127.0.0.1:5432/dbName?currentSchema=schemaName";
		
		mapConnectUrl.put(DbTypeEnum.MySQL, dbUrlMySQL);
		mapConnectUrl.put(DbTypeEnum.SQLServer, dbUrlSQLServer);
		mapConnectUrl.put(DbTypeEnum.Oracle, dbUrlOracle);
		mapConnectUrl.put(DbTypeEnum.DB2, dbUrlDB2);
		mapConnectUrl.put(DbTypeEnum.SQLite, dbUrlSQLite);
		mapConnectUrl.put(DbTypeEnum.DM, dbUrlDM);
		mapConnectUrl.put(DbTypeEnum.Hive, dbUrlHive);
		mapConnectUrl.put(DbTypeEnum.MaxCompute, dbUrlMax);		
		mapConnectUrl.put(DbTypeEnum.PostgreSQL, dbUrlPostgreSQL);		

		return mapConnectUrl.get(dbType);
	}
	
	static String getDriverClass(DbTypeEnum dbType)
	{
		Map<DbTypeEnum, String> mapClassName = new HashMap<DbTypeEnum, String>();
		mapClassName.clear();
		
		//String driverMysql = "com.mysql.jdbc.Driver";
		String driverMysql = "com.mysql.cj.jdbc.Driver";
		String driverSqlserver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		String driverOracle = "oracle.jdbc.driver.OracleDriver";
		String driverDB2 = "com.ibm.db2.jcc.DB2Driver";
		String driverSQLite = "org.sqlite.JDBC";
		String driverDM = "dm.jdbc.driver.DmDriver";
		String driverHive = "org.apache.hive.jdbc.HiveDriver";
		String driverMax = "com.aliyun.odps.jdbc.OdpsDriver";
		String driverPostgreSQL = "org.postgresql.Driver";
		
		mapClassName.put(DbTypeEnum.MySQL, driverMysql);
		mapClassName.put(DbTypeEnum.SQLServer, driverSqlserver);
		mapClassName.put(DbTypeEnum.Oracle, driverOracle);
		mapClassName.put(DbTypeEnum.DB2, driverDB2);
		mapClassName.put(DbTypeEnum.SQLite, driverSQLite);
		mapClassName.put(DbTypeEnum.DM, driverDM);
		mapClassName.put(DbTypeEnum.Hive, driverHive);
		mapClassName.put(DbTypeEnum.MaxCompute, driverMax);		
		mapClassName.put(DbTypeEnum.PostgreSQL, driverPostgreSQL);		
		mapClassName.put(DbTypeEnum.Kingbase, "com.kingbase8.Driver");		
		
		return mapClassName.get(dbType);
	}
	
	/**
	 * 
	 * 2017-11-19 生成一个SQLite
	 * @throws IOException 
	 * 
	 * 
	 */
	public static DataSourceSimple newInstanceSQLite(File file)
	{
		//0
		String path = null;
		try
		{
			path = file.getCanonicalPath();
		}
		catch(Exception e) {}
		
		
		//1
		DataSourceSimpleImpl dataSource= new DataSourceSimpleImpl();
		dataSource.connectUrl= "jdbc:sqlite:dbName.db".replace("dbName.db", path);
		dataSource.dbType= DbTypeEnum.SQLite;
		
		return dataSource;
	}
	
	/**
	 * 2021-08-18
	 */
	public static DataSourceSimple newInstancePostgreSQL(String username, String password, String ip, int port, String dbName)
	{
		//String dbUrlPostgreSQL = "jdbc:postgresql://127.0.0.1:5432/dbName?currentSchema=schemaName";
		String url= getUrl(DbTypeEnum.PostgreSQL)
				.replace("127.0.0.1:5432", ip+":"+port)
				.replace("dbName", dbName)
				.replace("schemaName", "public")
				;
		DataSourceSimpleImpl dataSource = new DataSourceSimpleImpl(username, password, url, dbName);
		
		return dataSource;
	}
	
	/**
	 * 2022-07
	 */
	public static DataSourceSimple newInstancePostgreSQL(String username, String password, String ip, int port, String dbName, String schemaName)
	{
		//String dbUrlPostgreSQL = "jdbc:postgresql://127.0.0.1:5432/dbName?currentSchema=schemaName";
		String url= getUrl(DbTypeEnum.PostgreSQL)
				.replace("127.0.0.1:5432", ip+":"+port)
				.replace("dbName", dbName)
				.replace("schemaName", schemaName)
				;
		DataSourceSimpleImpl dataSource = new DataSourceSimpleImpl(username, password, url, dbName, schemaName);
		
		
		PostGresqlConfig config= new PostGresqlConfig();
		config.setUsername(username);
		config.setPassword(password);
		config.setIp(ip);
		config.setPort(port);
		config.setDbName(dbName);
		config.setSchemaName(schemaName);
		
		return dataSource;
	}

	
	/**
	 * 202104
	 */
	public static DataSourceSimple newInstanceMaxCompute(String username, String password, String dbName)
	{

		//"jdbc:odps:http://service.cn.maxcompute.aliyun.com/api?project=dbName";
		String url= getUrl(DbTypeEnum.MaxCompute)
				.replace("dbName", dbName)
				;
		
		DataSourceSimpleImpl dataSource = new DataSourceSimpleImpl(username, password, url, dbName);
		return dataSource;
	}
	
	static String replaceText(String url)
	{
		return url;
	}
	
	/**
	 * 202104
	 */
	public static DataSourceSimple newInstanceOracle(String username, String password, String ip, String dbName)
	{
		//jdbc:oracle:thin:@127.0.0.1:1521:dbName
		String url= getUrl(DbTypeEnum.Oracle)
				.replace("127.0.0.1", ip)
				.replace("dbName", dbName)
				;
		DataSourceSimpleImpl dataSource = new DataSourceSimpleImpl(username, password, url, dbName);
		return dataSource;
	}
	
	/**
	 * 202104
	 */
	public static DataSourceSimple newInstanceKingbase(String username, String password, String ip, String dbName)
	{
		//jdbc:oracle:thin:@127.0.0.1:1521:dbName
		String url= getUrl(DbTypeEnum.Oracle)
				.replace("127.0.0.1", ip)
				.replace("dbName", dbName)
				;
		DataSourceSimpleImpl dataSource = new DataSourceSimpleImpl(username, password, url, dbName);
		return dataSource;
	}
	
	
	/**
	 * 202104
	 * "192.168.15.17", 10000, "Tom", "123456", "zlgk", DBType.Hive);
	 */
	public static DataSourceSimple newInstanceHive(String username, String password, String ip, int port,String dbName)
	{
		//"jdbc:hive2://127.0.0.1:10000/dbName";
		String url= getUrl(DbTypeEnum.Hive)
				.replace("127.0.0.1:10000", ip+":"+port)
				.replace("dbName", dbName)
				;
		DataSourceSimpleImpl dataSource = new DataSourceSimpleImpl(username, password, url, dbName);
		
		return dataSource;
	}
	
	/**
	 * 202104
	 * "192.168.15.17", 10000, "Tom", "123456", "zlgk", DBType.Hive);
	 */
	public static DataSourceSimple newInstanceHive(String username, String password, String ip, String dbName)
	{
		//
		return newInstanceHive(username, password, ip, 10000, dbName);
	}
	
	/**
	 * 202104
	 */
	public static DataSourceSimple newInstanceMySQL(String username, String password, String ip, String dbName)
	{
		return newInstanceMySQL(username, password, ip, 3306, dbName);
	}
	
	/**
	 * 202104
	 */
	public static DataSourceSimple newInstanceMySQL(String username, String password, String ip, int port, String dbName)
	{
		//"jdbc:mysql://127.0.0.1:3306/dbName?useSSL=false";
		String url= getUrl(DbTypeEnum.MySQL)
				.replace("127.0.0.1:3306", ip+":"+port)
				.replace("dbName", dbName)
				;
		DataSourceSimpleImpl dataSource = new DataSourceSimpleImpl(username, password, url, dbName);
		
		
		//以下代码功能：替换驱动中的ip、port
		//"jdbc:hive2://127.0.0.1:10000/dbName";
//		String connectUrl = null;
//
//		
//		if(port==null)
//		{
//			//只替换ip，使用默认端口
//			connectUrl = mapConnectUrl.get(dbType).replace("127.0.0.1", ip).replace("dbName", dbName);
//		}
//		else
//		{
//		
//			//整体替换字符串 127.0.0.1:10000
//			Pattern pattern = Pattern.compile("127.0.0.1:\\d*"); //从源链接字符串中查找将替换的
//			Matcher matcher = pattern.matcher(url);			
//			if(matcher.find())
//			{
//				matcher.groupCount();
//				String src_ip_port= matcher.group(); //"127.0.0.1:1521"
//				connectUrl = url.get(dbType).replace(src_ip_port, ip+":"+port).replace("dbName", dbName);
//			}
//			else
//			{
//				//找不到就不用替换了，目前只有SQLite没有
//			}	
//		}
		return dataSource;
	}
	
	/**
	 * 202104
	 */
	public static DataSourceSimple newInstanceDB2(String username, String password, String dbName)
	{
		String url= getUrl(DbTypeEnum.MaxCompute);
		DataSourceSimpleImpl dataSource = new DataSourceSimpleImpl(username, password, url, dbName);
		
		return dataSource;
	}
	
	
	/**
	 * 2022-01-20
	 */
	public static DataSourceSimple newInstance(String jdbcUrl, String driver, String username, String password, String dbName)
	{
		DataSourceSimpleImpl dataSource = new DataSourceSimpleImpl(username, password, jdbcUrl, dbName);
		return dataSource;
	}
	
	/**
	 * 2022-08-31
	 */
	public static DataSourceSimple newInstance(DataSource ds, DbTypeEnum dbType, String dbName)
	{
		DataSourceSimpleImpl dataSource = new DataSourceSimpleImpl(ds, dbType, dbName);
		return dataSource;
	}
	
	DataSourceSimpleImpl(DataSource ds, DbTypeEnum dbType, String dbName)
	{
		this.ds= ds;
		this.dbType= dbType;
		this.dbName= dbName;
		
		this.dialect= new Dialect(dbType);
	}
	
	DataSourceSimpleImpl(String username, String password, String jdbcUrl, String dbName)
	{
		this(username, password, jdbcUrl, dbName, null);
	}
	
	DataSourceSimpleImpl(String username, String password, String jdbcUrl, String dbName, String schemaName)
	{
		this.username= username;
		this.password= password;
		this.dbType= analysisDbType(jdbcUrl);
		this.connectUrl= jdbcUrl;
		this.dbName= dbName;
		this.schemaName= schemaName;
		
		this.dialect= new Dialect(dbType);
	}
	
	
	//仅仅用于SQLite
	/**
	 * 仅仅用于SQLite
	 */
	DataSourceSimpleImpl(){
		
	}




	@Override
	public Connection getConnection()
	{
		//

		//2.创建连接
		//System.out.println("创建连接...");
		try
		{
			//0
			if(this.ds!=null) {
				return ds.getConnection();
			}
			
			//1.
			String className= getDriverClass(dbType);
			Class.forName(className).newInstance(); // 捕获异常ClassNotFoundException
			
			
			Connection connection = null;
			if(dbType==DbTypeEnum.SQLite)
			{
				connection= DriverManager.getConnection(connectUrl); // 捕获异常SQLException
			}
			else
			{
				connection= DriverManager.getConnection(connectUrl, username, password); // 捕获异常SQLException
			}
			return connection;
		} catch (Exception e)
		{
			e.printStackTrace();
			
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLoginTimeout() throws SQLException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}

	
	
	/* (non-Javadoc)
	 * @see javax.sql.CommonDataSource#getParentLogger()
	 * 在1.6没有，但是1.7+有，所以不要加 @Override
	 * 
	 */
	public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}




	/* (non-Javadoc)
	 * @see liang.jdbc.service.impl.DataSourceExt#geDbType()
	 */
	@Override
	public DbTypeEnum geDbType() {
		// TODO Auto-generated method stub
		return dbType;
	}


	@Override
	public String getDbName() {
		// TODO Auto-generated method stub
		return dbName;
	}

	@Override
	public String getConnectString() {
		// TODO Auto-generated method stub
		return null;
	}

	DbTypeEnum analysisDbType(String jdbcUrl) {
		// TODO Auto-generated method stub
		String dbUrlSQLServer = "jdbc:sqlserver://127.0.0.1:1433; DatabaseName=dbName";
		String dbUrlMySQL = "jdbc:mysql://127.0.0.1:3306/dbName?useSSL=false";
		String dbUrlOracle = "jdbc:oracle:thin:@127.0.0.1:1521:dbName";
		String dbUrlDB2 = "jdbc:db2://127.0.0.1:50000/dbName";
		String dbUrlSQLite = "jdbc:sqlite:dbName.db"; //"jdbc:sqlite:E:/workspace/testdatabase.db";
		String dbUrlDM = "jdbc:dm://192.168.50.9:5236/dbName";
		String dbUrlHive = "jdbc:hive2://127.0.0.1:10000/dbName";
		String dbUrlMaxCompute = "jdbc:odps:http://service.cn.maxcompute.aliyun.com/api?project=dbName";
		String dbUrlPostgreSQL = "jdbc:postgresql://127.0.0.1:5432/dbName";
		
		if(jdbcUrl.startsWith("jdbc:odps:")) {
			return DbTypeEnum.MaxCompute;
		}
		if(jdbcUrl.startsWith("jdbc:postgresql://")) {
			return DbTypeEnum.PostgreSQL;
		}
		if(jdbcUrl.startsWith("jdbc:mysql://")) {
			return DbTypeEnum.MySQL;
		}
		if(jdbcUrl.startsWith("jdbc:oracle:thin:@")) {
			return DbTypeEnum.Oracle;
		}
		if(jdbcUrl.startsWith("jdbc:hive2://")) {
			return DbTypeEnum.Hive;
		}
		
		throw NotSupportError.throwError("不支持的数据库！");
	}

	@Override
	public boolean tryConnnect() {
		// TODO Auto-generated method stub
		try {
			Connection connection = this.getConnection();
			connection.close();
			return true;
		}
		catch(Exception e) {
		}

		return false;
	}
}
