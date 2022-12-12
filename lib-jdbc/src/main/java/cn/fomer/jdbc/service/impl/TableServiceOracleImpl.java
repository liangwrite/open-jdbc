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
import cn.fomer.jdbc.api.OracleTable;
import cn.fomer.jdbc.clone.service.impl.FieldServiceOracleImpl;
import cn.fomer.jdbc.entity.ColumnTypeEnum;
import cn.fomer.jdbc.service.MetaDataService;

/**
 * 2021-03-15
 * 
 */
public class TableServiceOracleImpl extends TableServiceImpl implements OracleTable {
	
	/**
	 * @param db
	 * @param tableName
	 */
	public TableServiceOracleImpl(DataBaseService db,String tableName) {
		// TODO Auto-generated constructor stub
		super(db, tableName);
	}

	@Override
	public List<FieldService> getFieldList() {
		// TODO Auto-generated method stub
		List<FieldService> cacheFieldList = super.getFieldList();
		if(cacheFieldList!=null) {
			return cacheFieldList;
		}
		
		//
		String sql= this.dataBase.getDialect().getFields(this.dataBase.getDbName(), this.name);
		String id= null; //this.getPrimaryKey();
		List<FieldService> fieldList= new ArrayList<FieldService>();
		if(sql==null) //不支持时 
		{
			MetaDataService metaDataService= new MetaDataServiceImpl(this.dataBase.getDataSource());
			fieldList= metaDataService.getAllField(this.name);
			return fieldList;
		}	
		
		
		
		ResultSetReaderImpl result = this.dataBase.executeQuery(sql);
		List<Map<String, Object>> mapList = result.mapList();
		int len= mapList.size();
		
		for(int i=0;i<len;i++) {
			FieldService fieldDetail= new FieldServiceOracleImpl(this);
			fieldList.add(fieldDetail);
			fieldDetail.setDbType(this.dataBase.getDbType());
			
			Map<String, Object> row= mapList.get(i);
			
			fieldDetail.setId(((BigDecimal)row.get("ID")).intValue());
			fieldDetail.setCode((String)row.get("NAME"));
			//fieldDetail.setCodeCamel(CamelName.toCamelForField(fieldDetail.getCode()));
			fieldDetail.setComment((String)row.get("COMMENTS"));
			fieldDetail.setColumnType(ColumnTypeEnum.parse((String)row.get("TYPE"), this.dataBase.getDbType()));
			fieldDetail.setNumlength(row.get("NUMLENGTH")==null?null:((BigDecimal)row.get("NUMLENGTH")).intValue());
			fieldDetail.setCharLength(row.get("CHARLENGTH")==null?null:((BigDecimal)row.get("CHARLENGTH")).intValue());
			fieldDetail.setNullable("Y".equals(row.get("NULLABLE"))?true:false);
			fieldDetail.setDefaultVal(row.get("DEFAULTVAL"));
			fieldDetail.setPrecision(row.get("PRECISON")==null?null:((BigDecimal)row.get("PRECISON")).intValue());
			
			
			fieldDetail.setIsPrimaryKey(id!=null&&id.equalsIgnoreCase(fieldDetail.getCode()));	
			
			
		}
		return fieldList;
	}

	@Override
	public void updateComment(String comment) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public List<FieldService> getPrimaryKey() {
		// TODO Auto-generated method stub
		String sql = this.getDataBase().getDialect().getPrimaryKeyForOracle(this.name);
		ResultSetReaderImpl result = this.getDataBase().executeQuery(sql);
			
		//return (String)result.singleRowSingleColumn();
		return null;
	}

	@Override
	public String getIdField() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int getPageCount() {
		// TODO Auto-generated method stub
		int count = this.count();
		int pageCount= count/this.PAGE_SIZE;
		if(count%this.PAGE_SIZE>0) {
			pageCount++;
		}
		return pageCount;
	}
	
	@Override
	public List<Map<String, Object>> getPage(int pageNo) {
		// TODO Auto-generated method stub
		String sql = this.getDataBase().getDialect().getRow(this.name, pageNo*this.PAGE_SIZE, (pageNo+1)*this.PAGE_SIZE);
		List<Map<String, Object>> mapList = this.getDataBase().executeQuery(sql).mapList();
		return mapList;
	}

}
