package cn.fomer.jdbc.service;

import javax.sql.DataSource;

import cn.fomer.jdbc.entity.DbTypeEnum;
import cn.fomer.jdbc.entity.Dialect;

import lombok.Getter;
import lombok.Setter;

/**
 * 2020-04-30 对数据源的扩展
 *
 */
@Getter
@Setter
public abstract class DataSourceSimple implements javax.sql.DataSource {

	protected String connectUrl;
	protected String username;
	protected String password;
	protected String dbName;
	protected String schemaName;
	protected DbTypeEnum dbType;
	protected String connectString;
	protected Dialect dialect;
	protected DataSource ds;
	
	public abstract DbTypeEnum geDbType();
	public abstract String getDbName();
	
	public abstract String getConnectString();
	

	public abstract boolean tryConnnect();
	
}
