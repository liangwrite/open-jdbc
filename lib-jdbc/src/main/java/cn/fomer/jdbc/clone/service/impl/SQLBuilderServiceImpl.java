package cn.fomer.jdbc.clone.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import cn.fomer.common.entity.ResultVO;
import cn.fomer.jdbc.api.DataBaseService;
import cn.fomer.jdbc.api.ColumnService;
import cn.fomer.jdbc.api.TableService;
import cn.fomer.jdbc.clone.service.ISQLBuilderService;
import cn.fomer.jdbc.entity.ColumnTypeEnum;

public class SQLBuilderServiceImpl implements ISQLBuilderService
{
	protected DataBaseService database;

	public SQLBuilderServiceImpl(DataBaseService database) 
	{
		// TODO Auto-generated constructor stub
		this.database= database;
	}
	
	
	@Override
	public ResultVO getTableDDL(TableService table) {
		// TODO Auto-generated method stub
		ResultVO resultVO= new ResultVO();
		final String SQL="create table TABLE_NAME(id int)";
		
	
		List<ColumnService> srcList = table.getColumnList();
		String contentPart= "";
		for(int i=0;i<srcList.size();i++)
		{
			ColumnService fieldVO= srcList.get(i);
			String create_field_string= this.createFieldSQL(fieldVO);
			if(StringUtils.isEmpty(create_field_string))
			{
				//log(table, fieldVO.getName(), fieldVO.getType(), "没有创建语句！");
				resultVO.setSuccess(false);
				resultVO.setMessage("");
				return resultVO;					
			}
			contentPart+= create_field_string;
			
			if(i!=srcList.size()-1)
			{
				contentPart+= ",";
			}
			
		}
		
		String tempSQL= SQL.replace("TABLE_NAME", table.getName()).replace("id int", contentPart);
		return ResultVO.success(tempSQL);
	}
	
	public Set<ColumnTypeEnum> unsupportCollection= new HashSet<ColumnTypeEnum>();
	
	@Override
	public String createFieldSQL(ColumnService fieldVO)
	{

		String text= "";
		if(fieldVO.getColumnType()==ColumnTypeEnum.ORACLE_VARCHAR2)
		{
			text= fieldVO.getCode()+" varchar2("+fieldVO.getCharLength()+")";
		}		
		else if(fieldVO.getColumnType()==ColumnTypeEnum.ORACLE_NUMBER)
		{
			text= fieldVO.getCode()+" number("+fieldVO.getNumlength()+")";
		}
		else if(fieldVO.getColumnType()==ColumnTypeEnum.ORACLE_DATE)
		{
			text= fieldVO.getCode()+" date";
		}
		
		else if(fieldVO.getColumnType()==ColumnTypeEnum.ORACLE_NVARCHAR2)
		{
			text= fieldVO.getCode()+" nvarchar2("+fieldVO.getCharLength()+")";			
		}
		
		else if(fieldVO.getColumnType()==ColumnTypeEnum.ORACLE_TIMESTAMP6)
		{
			text= fieldVO.getCode()+" TIMESTAMP(6)";
		}
		else if(fieldVO.getColumnType()==ColumnTypeEnum.ORACLE_BINARY_DOUBLE)
		{
			text= fieldVO.getCode()+" binary_double";
		}
		else if(fieldVO.getColumnType()==ColumnTypeEnum.ORACLE_NCHAR)
		{
			text= fieldVO.getCode()+" nchar";
		}
		else if(fieldVO.getColumnType()==ColumnTypeEnum.MYSQL_CHAR)
		{
			text= fieldVO.getCode()+" char";
		}
		else if(fieldVO.getColumnType()==ColumnTypeEnum.ORACLE_FLOAT)
		{
			text= fieldVO.getCode()+" float";
		}
		else if(fieldVO.getColumnType()==ColumnTypeEnum.ORACLE_CLOB)
		{
			text= fieldVO.getCode()+" clob";
		}
		else if(fieldVO.getColumnType()==ColumnTypeEnum.ORACLE_RAW)
		{
			text= fieldVO.getCode()+" raw("+fieldVO.getNumlength()+")";
		}
		else if(fieldVO.getColumnType()==ColumnTypeEnum.ORACLE_BINARY_DOUBLE)
		{
			text= fieldVO.getCode()+" binary_double";
		}

		
		else
		{
			unsupportCollection.add(fieldVO.getColumnType());
			return null;
		}

		if(fieldVO.getNullable())
		{
			text+= " not null";
		}
		return text;
	}
	
	public String buildInsert(Map<String, Object> vo,String table)
	{
		StringBuilder sb= new StringBuilder();
		
		sb.append("insert into tb_table(id,code,name) values('','','')");
		List<String> fieldList= new ArrayList<String>();
		for(String key:vo.keySet())
		{

			
		}
		return null;
	}
	
	void log(String table,ColumnService fieldVO,String message)
	{
		String sample= "TABLE.FIELD int 失败原因！";
		String text= sample
				.replace("TABLE", table)
				.replace("FIELD", fieldVO.getCode())
				.replace("int", fieldVO.getColumnType().toString())
				.replace("失败原因", message)
				;
				
		System.out.println(text);
	}

}
