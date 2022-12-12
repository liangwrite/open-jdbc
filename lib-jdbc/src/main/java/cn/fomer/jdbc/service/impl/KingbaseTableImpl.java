/**
 * 
 */
package cn.fomer.jdbc.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.fomer.jdbc.api.DataBaseService;
import cn.fomer.jdbc.api.FieldService;
import cn.fomer.jdbc.api.KingbaseTable;
import cn.fomer.jdbc.service.MetaDataService;

/**
 * 2022-01-21 金仓数据库
 * 
 */
public class KingbaseTableImpl extends TableServiceImpl implements KingbaseTable {
	
	public KingbaseTableImpl(DataBaseService db,String tableName) {
		super(db, tableName);
	}

	@Override
	public List<FieldService> getFieldList() {
		// TODO Auto-generated method stub
		String sql= super.getDataBase().getDialect().getFields(super.getDataBase().getDbName() ,this.getName());
		
		List<FieldService> fieldList= new ArrayList<FieldService>();
		if(sql==null) //不支持时 
		{
			MetaDataService metaDataService= new MetaDataServiceImpl(super.getDataBase().getDataSource());
			fieldList= metaDataService.getAllField(this.getName());
			return fieldList;
		}	
		
		
		
		ResultSetReaderImpl resutl = super.getDataBase().executeQuery(sql);
		List<Map<String, Object>> mapList = resutl.mapList();
		int len= mapList.size();
		for(int i=0;i<len;i++) {
			FieldService fieldDetail= new FieldServiceImpl(this);
			fieldList.add(fieldDetail);
			
			
			Map<String, Object> row= mapList.get(i);
			
			fieldDetail.setComment((String)row.get("description"));
			fieldDetail.setCode((String)row.get("column_name"));
			fieldDetail.setNullable("YES".equals(row.get("is_nullable")));
			fieldDetail.setType((String)row.get("udt_name"));
			fieldDetail.setOrdinalPosition((Integer)row.get("ordinal_position"));			
		}
		return fieldList;
	}

	@Override
	public void updateComment(String comment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getPageCount() {
		// TODO Auto-generated method stub
		return 0;
	}
}
