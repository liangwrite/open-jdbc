package cn.fomer.jdbc.api;

import java.util.List;
import java.util.Map.Entry;

import javax.sql.DataSource;

import cn.fomer.jdbc.service.impl.ResultSetReaderImpl;

/**
 * 2019-08-06 最基本的sql操作
 */
public interface CrudService {

	//
	DataSource getDataSource();
	
	
	//
	ResultSetReaderImpl executeQuery(String text,Object ...param);
	ResultSetReaderImpl[] executeQueryMultiResponse(String text,Object ...param);
	int executeUpdate(String text,Object ...param);
	
	
	
	
	/**
	 * 2021-04-15 一次执行多条不同的语句
	 */
	int executeUpdateBatch(String... text);
	
	
	/**
	 * <p>202103 一次执行相同的命令，适用于批量插入/更新(性能大幅领先单个语句执行)</p>
	 * <p>
	 * e.g. update PbUser set password = ?
	 * <br>支持hive</br>
	 * </p>
	 * 
	 */
	int executeUpdateBatch(String sql, List<Object[]> varList);
	
	
	void setEnableLog(boolean b);
	void log(Object o,String text);
	

}
