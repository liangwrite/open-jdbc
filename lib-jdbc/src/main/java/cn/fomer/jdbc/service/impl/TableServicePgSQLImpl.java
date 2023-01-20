/**
 * 
 */
package cn.fomer.jdbc.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import cn.fomer.jdbc.api.DataBaseService;
import cn.fomer.jdbc.api.ColumnService;
import cn.fomer.jdbc.api.PostgresTable;
import cn.fomer.jdbc.entity.ColumnTypeEnum;
import cn.fomer.jdbc.service.MetaDataService;

/**
 * 2022-01-21
 * 
 */
public class TableServicePgSQLImpl extends TableServiceImpl implements PostgresTable {
	
	public TableServicePgSQLImpl(DataBaseService db,String tableName) {
		super(db, tableName);
	}

	@Override
	public List<ColumnService> getColumnList() {
		// TODO Auto-generated method stub
		String sql=  this.getDataBase().getDialect().getFieldsForPgSQL(this.getDataBase().getSchemaName(),this.getName());
		
		List<ColumnService> fieldList= new ArrayList<ColumnService>();
		if(sql==null) //不支持时 
		{
			MetaDataService metaDataService= new MetaDataServiceImpl(this.getDataBase().getDataSource());
			fieldList= metaDataService.getAllField(this.getName());
			return fieldList;
		}	
		
		
		
		Map<String, String> fieldCommentMap = getColumnComment();
		ResultSetReaderImpl res = this.getDataBase().executeQuery(sql);
		List<Map<String, Object>> mapList = res.mapList();
		for(int i=0;i<mapList.size();i++) {
			ColumnService fieldDetail= new FieldServicePgSQLImpl(this);
			fieldList.add(fieldDetail);
			
			
			Map<String, Object> row= mapList.get(i);
			
			fieldDetail.setCode((String)row.get("column_name"));
			fieldDetail.setComment(fieldCommentMap.get(fieldDetail.getCode()));
			fieldDetail.setNullable("YES".equals(row.get("columnn_nullable")));
			fieldDetail.setType((String)row.get("column_type"));
			fieldDetail.setOrdinalPosition((Integer)row.get("column_no"));
			fieldDetail.setColumnType(ColumnTypeEnum.parse(fieldDetail.getType(), this.dataBase.getDbType()));
		}
		return fieldList;
	}

	@Override
	public String getComment() {
		// TODO Auto-generated method stub
		String sql = this.getDataBase().getDialect().getTableComment(this.getDataBase().getDbName(), this.getName());
		String comment = (String)this.getDataBase().executeQuery(sql).singleRowSingleColumn();
		return comment;
	}

	public Map<String, String> getColumnComment() {
		// TODO Auto-generated method stub
		String sql = this.getDataBase().getDialect().getColumnCommentForPgSQL(this.getDataBase().getSchemaName(), this.getName());
		Map<String, String> dictMap = this.getDataBase().executeQuery(sql).getDictMap();
		return dictMap;
	}
	
	
	@Override
	public void updateComment(String comment) {
		// TODO Auto-generated method stub
		if(StringUtils.isNotEmpty(comment)) {
			
			final String SQL= "comment on table TABLE_NAME is 'COMMENT'";
			String tempSQL= SQL.replace("TABLE_NAME", this.name).replace("COMMENT", comment);
			int success = this.dataBase.executeUpdate(tempSQL);
		}
	}

}
