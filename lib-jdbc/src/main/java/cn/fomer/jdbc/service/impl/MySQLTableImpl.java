/**
 * 
 */
package cn.fomer.jdbc.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.fomer.jdbc.api.DataBaseService;
import cn.fomer.jdbc.api.FieldService;
import cn.fomer.jdbc.api.MySQLTable;
import cn.fomer.jdbc.api.TableService;
import cn.fomer.jdbc.entity.ColumnTypeEnum;
import cn.fomer.jdbc.entity.DbTypeEnum;
import cn.fomer.jdbc.service.MetaDataService;

/**
 * 2021-03-15
 * 
 */
public class MySQLTableImpl extends TableServiceImpl implements MySQLTable {
	
	/**
	 * @param db
	 * @param tableName
	 */
	public MySQLTableImpl(DataBaseService db,String tableName) {
		// TODO Auto-generated constructor stub
		super(db, tableName);
	}


	@Override
	public List<FieldService> getFieldList() {
		// TODO Auto-generated method stub
		String sql= this.getDataBase().getDialect().getFields(this.getDataBase().getDbName(), this.getName());
		//String id= getIdField();
		List<FieldService> fieldList= new ArrayList<FieldService>();
		if(sql==null) //不支持时 
		{
			MetaDataService metaDataService= new MetaDataServiceImpl(this.getDataBase().getDataSource());
			fieldList= metaDataService.getAllField(this.getName());
			return fieldList;
		}	
		
		
		
		ResultSetReaderImpl resutl = this.getDataBase().executeQuery(sql);
		List<Map<String, Object>> mapList = resutl.mapList();
		int len= mapList.size();
		for(int i=0;i<len;i++) {
			FieldService fieldDetail= new FieldServiceImpl(this);
			fieldList.add(fieldDetail);
			
			
			Map<String, Object> row= mapList.get(i);
			fieldDetail.setCode((String)row.get("COLUMN_NAME"));
			fieldDetail.setNullable(("NO").equals((String)row.get("IS_NULLABLE"))?false:true);
			fieldDetail.setType((String)row.get("DATA_TYPE"));
			fieldDetail.setColumnType(ColumnTypeEnum.parse((String)row.get("DATA_TYPE"),this.getDataBase().getDbType()));
			fieldDetail.setComment((String)row.get("COLUMN_COMMENT"));
			if("PRI".equals((String)row.get("COLUMN_KEY")))
			{
				fieldDetail.setIsPrimaryKey(true);
			}
			else
			{
				fieldDetail.setIsPrimaryKey(false);
			}			
			
		}
		return fieldList;
	}


	@Override
	public void updateComment(String comment) {
		// TODO Auto-generated method stub
		
	}
	
	

}
