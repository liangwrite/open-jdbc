package cn.fomer.jdbc.service.impl;

import cn.fomer.common.entity.CamelName;
import cn.fomer.jdbc.api.ColumnService;
import cn.fomer.jdbc.api.TableService;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 20180928 字段属性
 * 
 * */
 public class ColumnServiceImpl extends ColumnService {
	
	
	 public ColumnServiceImpl(TableService table) {
		// TODO Auto-generated constructor stub
		 super(table);
	}
	 
	/**
	 * 驼峰形式（非原生字段）
	 */
	//String columnNameCamel;
	
	public String getCodeCamel() {
		return CamelName.toCamelForField(code);
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		// this.setIsPrimaryKey(isPrimaryKey);
		// this.setPrimaryKey(true);
		return code;
	}
	
	public String getJavaType() {
		// TODO Auto-generated method stub
		// this.setIsPrimaryKey(isPrimaryKey);
		// this.setPrimaryKey(true);
		if(columnType!=null) {
			return columnType.getJavaType().getSimpleName();
		}
		else {
			return Object.class.getSimpleName();
		}
	}


	@Override
	public void updateComment(String comment) {
		// TODO Auto-generated method stub
		final String SQL= "comment on column COLUMN_NAME is 'COMMENT'";
		String tempSQL= SQL.replace("COLUMN_NAME", this.code).replace("COMMENT", comment);
		int success = this.getTable().getDataBase().executeUpdate(tempSQL);
	}
}
