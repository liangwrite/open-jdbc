/**
 * 
 */
package cn.fomer.jdbc.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.fomer.jdbc.api.DataBaseService;
import cn.fomer.jdbc.api.FieldService;
import cn.fomer.jdbc.api.PostgresTable;
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
	public List<FieldService> getFieldList() {
		// TODO Auto-generated method stub
		String sql=  this.getDataBase().getDialect().getFields(this.getDataBase().getSchemaName(),this.getName());
		
		List<FieldService> fieldList= new ArrayList<FieldService>();
		if(sql==null) //不支持时 
		{
			MetaDataService metaDataService= new MetaDataServiceImpl(this.getDataBase().getDataSource());
			fieldList= metaDataService.getAllField(this.getName());
			return fieldList;
		}	
		
		
		
		Map<String, String> fieldCommentMap = getFieldComment();
		ResultSetReaderImpl res = this.getDataBase().executeQuery(sql);
		List<Map<String, Object>> mapList = res.mapList();
		int len= mapList.size();
		for(int i=0;i<len;i++) {
			FieldService fieldDetail= new FieldServicePgSQLImpl(this);
			fieldList.add(fieldDetail);
			
			
			Map<String, Object> row= mapList.get(i);
			
			fieldDetail.setCode((String)row.get("column_name"));
			fieldDetail.setComment(fieldCommentMap.get(fieldDetail.getCode()));
			fieldDetail.setNullable("YES".equals(row.get("is_nullable")));
			fieldDetail.setType((String)row.get("udt_name"));
			fieldDetail.setOrdinalPosition((Integer)row.get("ordinal_position"));			
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

	public Map<String, String> getFieldComment() {
		// TODO Auto-generated method stub
		String sql = this.getDataBase().getDialect().getFieldComment(this.getDataBase().getDbName(), this.getName());
		Map<String, String> dictMap = this.getDataBase().executeQuery(sql).getDictMap();
		return dictMap;
	}
	
	
	@Override
	public void updateComment(String comment) {
		// TODO Auto-generated method stub
		final String SQL= "comment on table TABLE_NAME is 'COMMENT'";
		String tempSQL= SQL.replace("TABLE_NAME", this.name).replace("COMMENT", comment);
		int success = this.db.executeUpdate(tempSQL);
	}

}
