package cn.fomer.jdbc.service.impl;

import org.apache.commons.lang.StringUtils;

import cn.fomer.jdbc.api.TableService;

/**
 * 类说明
 *
 * @author Liang
 * @email liang@163.com
 * @date 2022-12-09
 */
public class FieldServicePgSQLImpl extends FieldServiceImpl {

	public FieldServicePgSQLImpl(TableService table) {
		// TODO Auto-generated constructor stub
		super(table);
	}
	
	@Override
	public void updateComment(String comment) {
		// TODO Auto-generated method stub
		if(StringUtils.isEmpty(comment)) {
			return;
		}
		
		//
		final String SQL= "comment on column SCHEMA.TABLE.COLUMN is 'COMMENT'";
		String tempSQL= SQL
			.replace("SCHEMA", this.getTable().getDataBase().getSchemaName())
			.replace("TABLE", this.getTable().getName())
			.replace("COLUMN", this.code)
			.replace("COMMENT", comment)
			;
		
		int success = this.getTable().getDataBase().executeUpdate(tempSQL);
	}
}
