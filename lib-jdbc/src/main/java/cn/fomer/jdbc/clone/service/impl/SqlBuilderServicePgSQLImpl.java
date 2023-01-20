package cn.fomer.jdbc.clone.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import cn.fomer.common.entity.ResultVO;
import cn.fomer.jdbc.api.DataBaseService;
import cn.fomer.jdbc.api.ColumnService;
import cn.fomer.jdbc.api.TableService;
import cn.fomer.jdbc.clone.service.ISQLBuilderService;
import cn.fomer.jdbc.entity.ColumnTypeEnum;
import cn.fomer.jdbc.entity.DbTypeEnum;

/**
 * 类说明
 *
 * @author Liang
 * @email liang@163.com
 * @date 2022-12-08
 */
public class SqlBuilderServicePgSQLImpl extends SQLBuilderServiceImpl implements ISQLBuilderService {

	
	public SqlBuilderServicePgSQLImpl(DataBaseService database) {
		// TODO Auto-generated constructor stub
		super(database);
	}
	

	@Override
	public String createFieldSQL(ColumnService srcFieldVO) {
		// TODO Auto-generated method stub
		String text= "";
		if(srcFieldVO.getDbType()==DbTypeEnum.Oracle) {
			final String SQL_String= "column_code varchar(2)";
			final String SQL_NCHAR= "column_code char(2)";
			final String SQL_NUMERIC= "column_code numeric";
			final String SQL_DATE= "column_code date";
			final String SQL_FLOAT= "column_code float";
			
			if(srcFieldVO.getColumnType()==ColumnTypeEnum.ORACLE_NVARCHAR2 || srcFieldVO.getColumnType()==ColumnTypeEnum.ORACLE_VARCHAR2) {
				text= SQL_String.replace("2", srcFieldVO.getCharLength().toString()).replace("column_code", srcFieldVO.getCode());
			}
			if(srcFieldVO.getColumnType()==ColumnTypeEnum.ORACLE_NUMBER) {
				text= SQL_NUMERIC.replace("column_code", srcFieldVO.getCode());
			}
			if(srcFieldVO.getColumnType()==ColumnTypeEnum.ORACLE_DATE) {
				text= SQL_DATE.replace("column_code", srcFieldVO.getCode());
			}
			if(srcFieldVO.getColumnType()==ColumnTypeEnum.ORACLE_FLOAT) {
				text= SQL_FLOAT.replace("column_code", srcFieldVO.getCode());
			}
			if(srcFieldVO.getColumnType()==ColumnTypeEnum.ORACLE_NCHAR) {
				text= SQL_NCHAR.replace("2", srcFieldVO.getCharLength().toString()).replace("column_code", srcFieldVO.getCode());
			}
			
			if(StringUtils.isEmpty(text)) {
				throw new RuntimeException(String.format("column_name=%s, column_type=%s, column_ori=%s is not support!", 
					srcFieldVO.getCode(), 
					srcFieldVO.getColumnType().toString(),
					srcFieldVO.getColumnType().getTypeName()
				));
			}
			
			
			return text;
		}
		
		
		throw new RuntimeException(String.format("db type %s is not support!", srcFieldVO.getDbType().toString()));
	}

}
