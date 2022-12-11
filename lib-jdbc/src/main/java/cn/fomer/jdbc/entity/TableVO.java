package cn.fomer.jdbc.entity;

import java.util.ArrayList;
import java.util.List;

import cn.fomer.jdbc.service.impl.FieldServiceImpl;

/**
 * @author Liang
 * 20180928
 *
 */
public class TableVO {
	String name;
	
	List<FieldServiceImpl> fields;
	List<String> columns;
	List<Object> rows;
	
	public TableVO(String name)
	{
		this.name= name;
	}
	
	public List<FieldServiceImpl> getFields()
	{
		return null;
	}
	
	
}
