package cn.fomer.jdbc.api;

import java.io.Closeable;

import cn.fomer.jdbc.service.impl.ResultSetReaderImpl;

/**
 * 2021-10-11 海量数据查询
 * 
 */
public interface HugeQueryService{

	ResultSetReaderImpl next(int length);
	
	
	void close();
}
