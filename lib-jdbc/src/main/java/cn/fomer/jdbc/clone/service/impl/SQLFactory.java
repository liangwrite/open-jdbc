package cn.fomer.jdbc.clone.service.impl;

import cn.fomer.jdbc.api.DataBaseService;
import cn.fomer.jdbc.api.ColumnService;
import cn.fomer.jdbc.api.TableService;
import cn.fomer.jdbc.clone.service.ISQLBuilderService;
import cn.fomer.jdbc.entity.DbTypeEnum;
import cn.fomer.jdbc.service.impl.ColumnServiceImpl;
import cn.fomer.jdbc.service.impl.FieldServicePgSQLImpl;
import cn.fomer.jdbc.service.impl.TableServiceOracleImpl;
import cn.fomer.jdbc.service.impl.TableServicePgSQLImpl;

/**
 * 类说明
 *
 * @author Liang
 * @email liang@163.com
 * @date 2022-12-08
 */
public class SQLFactory {

	public static ISQLBuilderService getSQLBuilder(DataBaseService db) {
		if(db.getDbType()==DbTypeEnum.PostgreSQL) {
			return new SqlBuilderServicePgSQLImpl(db);
		}
		
		return null;
	}
	
	public static TableService getTableService(DataBaseService db, String name) {
		if(db.getDbType()==DbTypeEnum.PostgreSQL) {
			return new TableServicePgSQLImpl(db, name);
		}
		if(db.getDbType()==DbTypeEnum.Oracle) {
			return new TableServiceOracleImpl(db, name);
		}
		
		return null;
	}
	
	
	public static ColumnService getFieldService(TableService table) {
		if(table.getDataBase().getDbType()==DbTypeEnum.PostgreSQL) {
			return new FieldServicePgSQLImpl(table);
		}
		
		
		return new ColumnServiceImpl(table);
	}
}
