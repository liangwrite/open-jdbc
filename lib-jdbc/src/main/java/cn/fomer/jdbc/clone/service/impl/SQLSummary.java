package cn.fomer.jdbc.clone.service.impl;

import java.util.List;

import cn.fomer.jdbc.entity.FuncVO;
import cn.fomer.jdbc.entity.ProcedureVO;
import cn.fomer.jdbc.entity.ViewVO;

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
public class SQLSummary {
	long millisecond; //查询耗时
	int sqlCount; //查询了多少次
	int top;
	String dbVersion;
	String dbType;
	List<TableSummary> tableList;
	BiggestTable biggestTable;
	
	List<ViewVO> viewList;
	List<ProcedureVO> procedureList;
	List<FuncVO> funList;
	List<String> fieldTypeList;
	
	public SQLSummary(int top){ this.top= top;}
}
