package cn.fomer.jdbc.clone.service.impl;

import cn.fomer.jdbc.service.impl.ResultSetReaderImpl;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 2018-11-03
 * 
 * 
 */
@Getter
@Setter
public class TableSummary{
	String table;
	
	Integer count;
	ResultSetReaderImpl rows;
	
}
