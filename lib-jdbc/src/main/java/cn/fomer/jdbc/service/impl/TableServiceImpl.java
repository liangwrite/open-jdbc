package cn.fomer.jdbc.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;

import cn.fomer.common.entity.EntryImpl;
import cn.fomer.jdbc.api.DataBaseService;
import cn.fomer.jdbc.api.FieldService;
import cn.fomer.jdbc.api.MySQLTable;
import cn.fomer.jdbc.api.OracleTable;
import cn.fomer.jdbc.api.TableService;
import cn.fomer.jdbc.api.TemplateService;
import cn.fomer.jdbc.component.JdbcMap;
import cn.fomer.jdbc.entity.ColumnTypeEnum;
import cn.fomer.jdbc.entity.DbTypeEnum;
import cn.fomer.jdbc.entity.Error;
import cn.fomer.jdbc.entity.PKTypeEnum;
import cn.fomer.jdbc.service.MetaDataService;
import lombok.Data;


/**
 * 2018-11-15 表
 * 
 * 
 */
@Data
public abstract class TableServiceImpl implements TableService
{
	protected DataBaseService dataBase;
	protected final String name;
	String javaName;
	TemplateService templateService;
	
	
	BatchInsertServiceImpl batchInsertService;
	
	
	OracleTable oracleTable;
	MySQLTable mysqlTable;
	KingbaseTableImpl kingbaseTable;
	TableServicePgSQLImpl postgresTable;
	TableServiceImpl(DataBaseService db,String tableName)
	{
		this.dataBase= db;
		this.name= tableName;
		//this.javaName= CamelName.toCamelForClass(tableName);
		
		this.batchInsertService= new BatchInsertServiceImpl(this);
		this.templateService= new TemplateServiceImpl(this);
		
	}
	
	
	/**
	 * 2018-12-14
	 * 
	 */
	@Override
	public Boolean existField(String name)
	{
		return getField(name)==null?false:true;
	}

	
	@Override
	public FieldService getField(String name) {
		// TODO Auto-generated method stub
		List<FieldService> allField = getFieldList();
		for(FieldService detail: allField)
		{
			if(detail.getCode().equalsIgnoreCase(name)) return detail;
		}
		
		return null;
	}
	
	
	@Override
	public List<FieldService> getFieldList()
	{
		//0 从缓存读取
		List<FieldService> fieldList= new ArrayList<FieldService>();;
		
		//1.从缓存中读取
		for(String tableString:this.getDataBase().getTableFieldCache().keySet())
		{
			if(name.equalsIgnoreCase(tableString))
			{
				fieldList = this.getDataBase().getTableFieldCache().get(tableString);
				return fieldList;
			}				
		}	
		
		
		return null;
		
		
		
		/*
		
		if(dataBase.getDbType()==DbTypeEnum.MaxCompute)
		{
			fieldList = dataBase.getMetaDataService().getFieldList(tableName);
			return fieldList;
		}
		
		
		
		
		
		
		//System.out.println("SQLSuperUtil.getFields(): \r\n"+JacksonUtil.toJson(result.listRow));
		//System.out.println(new Gson().toJson(mapList));
		
		//Set<String> hiveKeySet= new HashSet<String>(); //hive防止由于分区字段存在导致重复字段
		
		
		
		for(int i=0;i<len;i++)
		{
			
			
			else if(dataBase.getDbType()==DbTypeEnum.MySQL) //column_name,data_type
			{
				fieldDetail.setCode((String)row.get("COLUMN_NAME"));
				fieldDetail.setNullable(("NO").equals((String)row.get("IS_NULLABLE"))?false:true);
				fieldDetail.setColumnType(ColumnTypeEnum.parse((String)row.get("DATA_TYPE"),dataBase.getDbType()));
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
			else if(dataBase.getDbType()==DbTypeEnum.Hive) //column_name,data_type
			{
				
				
				//{col_name=id, data_type=int, comment=}
				fieldDetail.setCode((String)row.get("col_name"));
				fieldDetail.setColumnType(ColumnTypeEnum.parse((String)row.get("data_type"),dataBase.getDbType()));
				fieldDetail.setComment((String)row.get("comment"));
				
				
				//如果遇到空字符串，说明后面是分区字段信息，属于重复
				if(fieldDetail.getCode().length()==0)
				{
					fieldList.remove(fieldList.size()-1);
					
					//标记
					for(FieldDetail f: fieldList)
					{
						if(f.getCode().equals(fieldDetail.getCode()))
						{
							f.setIsPartitionField(true);
						}
						else
						{
							f.setIsPartitionField(false);
						}
					}
					break;
				}

			}
			else if(dataBase.getDbType()==DbTypeEnum.PostgreSQL) //column_name,data_type
			{
				fieldDetail.setCode((String)row.get("column_name"));
				fieldDetail.setNullable(("NO").equals((String)row.get("is_nullable"))?false:true);
				fieldDetail.setColumnType(ColumnTypeEnum.parse((String)row.get("data_type"),dataBase.getDbType()));
			}			
			else if(dataBase.getDbType()==DbTypeEnum.MaxCompute) //column_name,data_type
			{
				//table_schema	table_name	column_name	ordinal_position	column_default	is_nullable	data_type	column_comment	is_partition_key
				parseForMaxcomputeSQL(fieldDetail, row);
			}
			else {
				
			}
				
		}
		*/
		

	}
	
	
	
	/**
	 * 2021-05-04 通过SQL的方式，新建的表获取不到
	 */
	void parseForMaxcomputeSQL(FieldService fieldDetail, Map<String, Object> row)
	{
		fieldDetail.setSchemaName((String)row.get("table_schema"));
		fieldDetail.setTableName((String)row.get("table_name"));
		fieldDetail.setCode((String)row.get("column_name"));
		fieldDetail.setOrdinalPosition(((Long)row.get("ordinal_position")).intValue());
		fieldDetail.setDefaultVal(row.get("column_default"));
		fieldDetail.setNullable((Boolean)row.get("is_nullable"));
		fieldDetail.setComment((String)row.get("column_comment"));
		fieldDetail.setColumnType(ColumnTypeEnum.parse((String)row.get("data_type"),dataBase.getDbType()));
		fieldDetail.setIsPartitionField((Boolean)row.get("is_partition_key"));
		
		//2.无需对分区字段去重	
		
	}

	@Override
	public List<FieldService> getPrimaryKey() {
		// TODO Auto-generated method stub
		List<FieldService> fieldList = getFieldList();
		List<FieldService> pkList= new ArrayList<FieldService>();
		for(FieldService f:fieldList)
		{
			if(f.getIsPrimaryKey()) pkList.add(f);
		}
		return pkList;
	}	

	@Override
	public String getDDL()
	{
		String sql= dataBase.getDialect().getTableDDL(name);
		String ddl= null;
		if(dataBase.getDbType()==DbTypeEnum.Oracle)
		{
			ddl= (String)dataBase.executeQuery(sql).singleRowSingleColumn();
			return ddl;		
		}
		if(dataBase.getDbType()==DbTypeEnum.MySQL)
		{
			Map<String, Object> result= dataBase.executeQuery(sql).singleRow();
			ddl= (String)result.get("Create Table");
			return ddl;
		}
		
		throw new RuntimeException("unsupport "+dataBase.getDbType().toString());
	}
	


	/**
	 *
	 */
	public int count(String filterWhere)
	{
		
		final String SQL= "select count(*) from TABLE";
		final String ORACLE_SQL= "select count(*) from \"TABLE\"";

		String tempSQL= SQL.replace("TABLE", name);
		
		/*
		if(dataBase.getDbType()==DbTypeEnum.Oracle) {
			tempSQL= ORACLE_SQL.replace("TABLE", name);
		}
		*/
		
		if(StringUtils.isEmpty(filterWhere))
		{
			
		}
		else
		{
			tempSQL+= " where "+filterWhere;			
		}
		ResultSetReaderImpl sqlResult= dataBase.executeQuery(tempSQL);
		Object count= sqlResult.singleRowSingleColumn();
		return Integer.valueOf(count.toString());
	}
	
	
	/**
	 * 2018-11-03
	 * 
	 */	
	public int count()
	{
		return count("");
	}

	/**
	 * 2018-11-03
	 */
	@Override
	public List<Map<String, Object>> top(int top)
	{
		String topSQL = dataBase.getDialect().getTopRow(name, top);
		return dataBase.executeQuery(topSQL).mapList();
	}
	
	/* 
	 * @see liang.jdbc.service.SuperSql#clearTable()
	 */
	@Override
	public int clear() {
		// TODO Auto-generated method stub
		return dataBase.executeUpdate("delete from "+name);
	}


	@Override
	public int deleteById(Object id) {
		// TODO Auto-generated method stub
		if(getPKType()==PKTypeEnum.ONE)
		{
			String sql= "delete from "+name+" where "+getPrimaryKey().get(0).getCode()+"=?";
			int n = dataBase.executeUpdate(sql, id);
			return n;			
		}
		
		return 0;
	}


	@Override
	public Map<String, Object> selectByPk(Object pk) {
		// TODO Auto-generated method stub
		if(getPKType()==PKTypeEnum.ONE)
		{
			String sql= "select * from "+name+" where "+getPrimaryKey().get(0).getCode()+"=?";
			ResultSetReaderImpl result = dataBase.executeQuery(sql, pk);
			if(result.mapList().size()>0)
			{
				return result.mapList().get(0);
			}			
		}

		return null;
	}


	/**
	 * 202011 获取字段兼容的值。比如datetime字段，而且值为long，那么应该转换为Date
	 */
	private Object asCompatibleValue(FieldService fieldVO,Object value)
	{
		Object compatible= value;
		if(dataBase.getDbType()==DbTypeEnum.MySQL)
		{
			if(fieldVO.getColumnType()==ColumnTypeEnum.MYSQL_DATETIME)
			{
				if(value instanceof Long)
				{
					Date d= new Date((Long)value);
					compatible= d;
				}
			}
		}
		
		return compatible;
	}
	@Override
	public String getDbName() {
		// TODO Auto-generated method stub
		return dataBase.getDbName();
	}


	@Override
	public List<Map<String, Object>> selectByWhere(String where) {
		// TODO Auto-generated method stub
		String sql= "select * from "+name+" where "+where;
		ResultSetReaderImpl executeQuery = dataBase.executeQuery(sql);
		return executeQuery.mapList();
	}


	@Override
	public PKTypeEnum getPKType() {
		// TODO Auto-generated method stub
		List<FieldService> fieldList = getFieldList();

		int pkCount= 0;
		for(FieldService f:fieldList)
		{
			if(f.getIsPrimaryKey()) pkCount++;
		}
		
		if(pkCount==0) return PKTypeEnum.NONE;
		else if(pkCount==1) return PKTypeEnum.ONE;
		else if(pkCount>1) return PKTypeEnum.UNION;
		return null;
	}


	@Override
	public List<Map<String, Object>> all() {
		// TODO Auto-generated method stub
		String sql= "select * from "+name;
		ResultSetReaderImpl result = dataBase.executeQuery(sql);
		return result.mapList();
	}
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name;
	}


	@Override
	public int updateByMap(Map<String, Object> dataMap)
	{
		//
		JdbcMap jdbcMap= new JdbcMap(dataMap);
	
		if(getPrimaryKey().size()==0)
		{
			
			System.err.println("没有主键！");
			return 0;
		}
		FieldService pk= getPrimaryKey().get(0);
		String pkName= pk.getCode();
		Object pkValue= jdbcMap.getFieldEntry(pkName).getValue();
		
		
		//拼SQL
		String sql= "update "+getName()+" set ";
		List<Object> pArray= new ArrayList<>();
		
		boolean found= false;
		for(int i=0;i<getFieldList().size();i++)
		{
			FieldService fieldDetail= getFieldList().get(i);
			if(fieldDetail.getIsPrimaryKey()) continue; //不能拼主键
	
			Entry<String, Object> pair = jdbcMap.getFieldEntry(fieldDetail.getCode());
			
			if(pair!=null)
			{
				Object value= pair.getValue();
				sql+= fieldDetail.getCode()+"=?,";
				Object compatibleValue = asCompatibleValue(fieldDetail, value);
				pArray.add(asCompatibleValue(fieldDetail, compatibleValue));
				
			}
	
		}
		
		if(sql.endsWith(",")) sql= sql.substring(0, sql.length()-1);
	
		sql+= " where "+pkName+"=?";
		pArray.add(asCompatibleValue(pk, pkValue));
	
		if(dataBase.isEnableLog())
		{
			System.out.println(sql);
			System.out.println(new Gson().toJson(pArray));			
		}

		dataBase.executeUpdate(sql, pArray.toArray());
		
		return 0;
	}
	
	/**
	 * 202103 必须有主键才支持update操作
	 */	
	public Entry<String, List<Object[]>> updateByMapBatch(List<Map<String, Object>> mapList)
	{
		return updateByMapBatch(mapList, null);
	}
	
	/**
	 * 202103 
	 * 如果id!=null时 认为id是主键
	 * 如果id==null时 取表定义为主键
	 * 否则 报错
	 * 
	 */
	public Entry<String, List<Object[]>> updateByMapBatch(List<Map<String, Object>> mapList, String id)
	{
		//1.主键检查
		FieldService idfFieldDetail= null;
		
		if(StringUtils.isEmpty(id))
		{
			if(getPrimaryKey().size()==0)
			{
				throw new RuntimeException("没有找到{TABLE}表的主键，不支持update操作！".replace("{TABLE}", name));
			}
			else
			{
				idfFieldDetail= getPrimaryKey().get(0);
			}
		}
		else
		{
			idfFieldDetail= getField(id);
			if(idfFieldDetail==null) 
			{
				throw new RuntimeException("在{TABLE}表中，您指定的字段{ID}不存在，不支持update操作！"
						.replace("{TABLE}", name)
						.replace("{ID}", id)
						
						);
			}
		}

		
		//2.
		List<Object[]> varList= new ArrayList<Object[]>();
		
		
		//
		for(Map<String, Object> rowMap: mapList)
		{
			//拼SQL
			List<Object> objectList= new ArrayList<>();
			

			//
			JdbcMap jdbcMap= new JdbcMap(rowMap);
			Object idValue= jdbcMap.getFieldEntry(idfFieldDetail.getCode()).getValue();

			
			for(int i=0;i<getFieldList().size();i++)
			{
				FieldService fieldDetail= getFieldList().get(i);
				if(fieldDetail.getIsPrimaryKey()) continue; //不能拼主键
				
				Entry<String, Object> fieldValueEntry= jdbcMap.getFieldEntry(fieldDetail.getCode());
				
				
				
				Object value= fieldValueEntry==null?null:fieldValueEntry.getValue();
				
				boolean fullSQL= true;
				
				//是否全量
				//if(fullSQL||fieldValueEntry!=null) 
				{
					Object compatibleValue = asCompatibleValue(fieldDetail, value);
					objectList.add(compatibleValue);
					
				}
				
			}
			
			
			objectList.add(asCompatibleValue(idfFieldDetail, idValue));
			
			if(dataBase.isEnableLog())
			{
				System.out.println(new Gson().toJson(objectList));			
			}
			
			
			
			varList.add(objectList.toArray());
		}
		
		
		//统一语句
		String sql= createFullUpdateSQL(idfFieldDetail);
		Entry<String, List<Object[]>> result= new EntryImpl<>(sql, varList);
		
		
		return result;
	}
	
	
	/**
	 * 202103 生成一个全字段的更新SQL（必须定义了主键）（仅用于批量更新，而且是内部调用）
	 * idfFieldDetail 是指定的主键
	 */
	String createFullUpdateSQL(FieldService idFieldDetail)
	{
		//
		
		//拼SQL
		String sql= "update "+getName()+" set ";
		List<Object> pArray= new ArrayList<>();
		
		for(int i=0;i<getFieldList().size();i++)
		{
			FieldService field= getFieldList().get(i);
			if(field.getIsPrimaryKey()) continue; //不能拼主键
			
			
			//全量
			sql+= field.getCode()+"=?,";
		}
		
		if(sql.endsWith(",")) sql= sql.substring(0, sql.length()-1);
		sql+= " where "+idFieldDetail.getCode()+"=?";
		
		if(dataBase.isEnableLog())
		{
			System.out.println(sql);
		}
		
		
		
		return sql;
	}
	

	@Override
	public int updateByMap(List<Map<String, Object>> mapList) {
		// TODO Auto-generated method stub
		
		Entry<String, List<Object[]>> batch = updateByMapBatch(mapList);
		if(dataBase.isEnableLog())
		{
			System.out.println("批量更新语句："+batch.getKey());
		}
		
		
		int n = dataBase.executeUpdateBatch(batch.getKey(), batch.getValue());
		
		
		
		return n;
	}

	@Override
	public Entry<Integer, String> updateByMapNoId(List<Map<String, Object>> mapList, String id) {
		// TODO Auto-generated method stub
		
		Entry<String, List<Object[]>> batch = updateByMapBatch(mapList, id);
		if(dataBase.isEnableLog())
		{
			System.out.println("批量更新语句："+batch.getKey());
		}
		
		
		int n = dataBase.executeUpdateBatch(batch.getKey(), batch.getValue());
		
		
		
		return new EntryImpl(n, batch.getKey());
	}



	@Override
	public int insertByMap(Map<String, Object> dataMap)
	{
		
		TableService t= dataBase.getTable(name);
		List<FieldService> fieldList= t.getFieldList();
		
		Entry<String, List<Object>> entry = insertByMapSQL(dataMap, fieldList, true);
		
		int result= 0;
		try
		{
			result= dataBase.executeUpdate(entry.getKey(), entry.getValue().toArray());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
			throw new RuntimeException("动态写入后台报错！");
		}
		
		return result;
		
	}
	
	
	
	String insertByMapFullHeadSQLFor(List<FieldService> fieldList)
	{
		
		String keytext= "insert into "+name+ " (";
		String valuetext= " values(";
		
		for(int i=0;i<fieldList.size();i++)
		{
			FieldService fieldVO= fieldList.get(i);	
			
			//拼接
			keytext+= fieldVO.getCode()+",";
			valuetext+= "?,";
		}
		
		keytext= keytext.substring(0, keytext.length()-1); //最后一个字段拼完不加逗号
		valuetext= valuetext.substring(0, valuetext.length()-1);
		
		keytext+= ")";
		valuetext+= ")";
		
		String full= keytext+valuetext;
		
		dataBase.log(this, "插入语句"+full);
		
		return full;		
	}
	
	
	
	/**
	 * 2021-05-04
	 * @param showFieldList
	 */
	Entry<String, List<Object>> insertByMapSQL(Map<String, Object> dataMap, List<FieldService> fieldList, boolean showFieldList)
	{
		
		JdbcMap jdbcMap= new JdbcMap(dataMap);
		
		
		String keytext= "insert into "+name+ " (";
		String valuetext= " values(";
		
		
		List<Object> varList= new ArrayList<Object>();
		
		int found_total= 0; //本次一共插入多少个字段(有值时)
		
		
		for(int i=0;i<fieldList.size();i++)
		{
			FieldService fieldVO= fieldList.get(i);	
			Object value= null;
			
			boolean hasFoundValue= false;
			
			Entry<String, Object> fieldEntry = jdbcMap.getFieldEntry(fieldVO.getCode());
			
			if(fieldEntry!=null){
				hasFoundValue= true;
				++found_total;
				
				value= dataMap.get(fieldEntry.getKey());
			}
			
			//校验（主键/必填字段不能为空）
			if(fieldVO.getNullable()!=null&&fieldVO.getNullable()!=true)
			{
				
			}
			if(fieldVO.getNullable()!=null&&!fieldVO.getNullable()&&(value==null||(dataBase.getDbType()==DbTypeEnum.Oracle&&"".equals(value))))
			{
				String message= "该字段定义要求不能为空！"+name+"."+fieldVO.getCode();
				System.err.println(new Gson().toJson(fieldEntry));
				System.err.println(message);
				throw new RuntimeException(message);
			}
			
			if(!hasFoundValue)
			{
				//hive语法要求拼全字段
				if(dataBase.getDbType()==DbTypeEnum.Hive)
				{
					found_total++;
				}
				else
				{
					continue;
				}
			}
			
			//格式转换
			value= asCompatibleValue(fieldVO,value);
			
			
			//拼接
			keytext+= fieldVO.getCode()+",";
			valuetext+= "?,";
			
			varList.add(value);
			
		}
		
		if(found_total==0)
		{
			throw new RuntimeException("所有字段都确定不了值，动态插入失败！");
		}
		
		keytext= keytext.substring(0, keytext.length()-1); //最后一个字段拼完不加逗号
		valuetext= valuetext.substring(0, valuetext.length()-1);
		
		keytext+= ")";
		valuetext+= ")";
		
		String full= keytext+valuetext;
		
		
		//dataBase.log(this, "前台变量"+JSON.toJSONString(dataMap));
		//dataBase.log(this, "插入语句"+full);
		//dataBase.log(this, "动态变量"+JSON.toJSONString(varList));
		
		Entry<String, List<Object>> entry= new EntryImpl(full, varList);
		return entry;
		
	}
	


	@Override
	public int insertByMap(List<Map<String, Object>> mapList) {
		// TODO Auto-generated method stub
		if(mapList.size()==0) return 0;
		
		//hive有专用方法
		if(dataBase.getDbType()==DbTypeEnum.Hive||dataBase.getDbType()==DbTypeEnum.MaxCompute)
		{
			int n = insertByMapForHive(mapList);
			return n;
		}

		
		//1.获取表结构
		List<FieldService> fieldList= getFieldList();
		if(fieldList==null) //不支持表结构
		{
			//
			MetaDataService metaDataService= new MetaDataServiceImpl(getDataBase().getDataSource());
			fieldList = metaDataService.getAllField(name);
			
			return -1;
		}
		
		//2
		String sql= null;
		List<Object[]> list= new ArrayList<>();
		for(int r=0;r<mapList.size();r++)
		{
			//
			Map<String, Object> rowMap= mapList.get(r);
			
			//maxcompute语法禁止显示字段列表
			boolean showFieldList= dataBase.getDbType()==DbTypeEnum.MaxCompute?false:true;
			Entry<String, List<Object>> rowEntry = insertByMapSQL(rowMap, fieldList, showFieldList);
			
			
			if(sql==null) sql= rowEntry.getKey();
			list.add(rowEntry.getValue().toArray());
		}
		
		//生成语句和参数
		
		//执行
		try
		{
			int n= dataBase.executeUpdateBatch(sql, list);
			return n;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException("批量插入失败！");
		}
	}
	
	
	/**
	 * 2021-04-15 因为hive的jdbc不支持批量插入，所以采用拼多个value的方式
	 */
	public int insertByMapForHive(List<Map<String, Object>> mapList) {
		// TODO Auto-generated method stub
		//int[] executeUpdateBatch(String sql, List<Object[]> varList) throws Exception;
		//insert into t_user values(1,'Tom'),(2,'Tom'),(3,'Tom');

		String sql= "insert into {TABLE} values	"
				.replace("{TABLE}", name)
				;
		
		for(Map<String, Object> rowMap: mapList)
		{
			JdbcMap jdbcMap= new JdbcMap(rowMap);
			
			String part= "(";
			for(FieldService fieldDetail:getFieldList())
			{
				Entry<String, Object> valueEntry = jdbcMap.getFieldEntry(fieldDetail.getCode());
				Object v= valueEntry.getValue();
				String vString= "";
				if(v==null)
				{
					vString= "null";
				}
				//else if(v instanceof Integer || v instanceof Long || v instanceof Double || v instanceof BigDecimal || v instanceof BigInteger)
				else if(v instanceof Number)
				{
					vString= v.toString();
				}
				else
				{
					vString= "'"+v.toString()+"'";
				}
				part+= vString+",";
			}
			part= part.substring(0,part.length()-1);
			part+= "),";
			sql+= part;
		}
		
		sql= sql.substring(0,sql.length()-1);
		
		
		
		//生成语句和参数
		//System.out.println("批量插入的SQL: "+sql);
		
		//执行
		if(dataBase.isEnableLog())
		{
			System.out.println("批量插入的SQL: "+sql);
		}
		try
		{
			int n= dataBase.executeUpdate(sql);
			return n;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException("批量插入失败！");
		}
	}


	
	
	/**
	 * 2021-05-04 不能列全字段 INSERT INTO table VALUES (?, ?, ?)
ds
	 */
	public int insertByMapForMaxCompute(List<Map<String, Object>> mapList) {
		// TODO Auto-generated method stub
		//int[] executeUpdateBatch(String sql, List<Object[]> varList) throws Exception;
		//insert into t_user values(1,'Tom'),(2,'Tom'),(3,'Tom');
		
		String sql= "insert into {TABLE} values	"
				.replace("{TABLE}", name)
				;
		
		for(Map<String, Object> rowMap: mapList)
		{
			JdbcMap jdbcMap= new JdbcMap(rowMap);
			
			String part= "(";
			for(FieldService fieldDetail:getFieldList())
			{
				Entry<String, Object> valueEntry = jdbcMap.getFieldEntry(fieldDetail.getCode());
				Object v= valueEntry.getValue();
				String vString= "";
				if(v==null)
				{
					vString= "null";
				}
				//else if(v instanceof Integer || v instanceof Long || v instanceof Double || v instanceof BigDecimal || v instanceof BigInteger)
				else if(v instanceof Number)
				{
					vString= v.toString();
				}
				else
				{
					vString= "'"+v.toString()+"'";
				}
				part+= vString+",";
			}
			part= part.substring(0,part.length()-1);
			part+= "),";
			sql+= part;
		}
		
		sql= sql.substring(0,sql.length()-1);
		
		
		
		//生成语句和参数
		//System.out.println("批量插入的SQL: "+sql);
		
		//执行
		if(dataBase.isEnableLog())
		{
			System.out.println("批量插入的SQL: "+sql);
		}
		try
		{
			int n= dataBase.executeUpdate(sql);
			return n;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException("批量插入失败！");
		}
	}
	
	/**
	 * 2021-04-15 因为maxcompute的批量插入，有格式要求INSERT INTO table VALUES (?, ?, ?)
	 */
	public int insertByMapViaValuesForMaxcompute(List<Map<String, Object>> mapList) {
		// TODO Auto-generated method stub
		//int[] executeUpdateBatch(String sql, List<Object[]> varList) throws Exception;
		//insert into t_user values(1,'Tom'),(2,'Tom'),(3,'Tom');
		
		String sql= "insert into {TABLE} values	"
				.replace("{TABLE}", name)
				;
		
		for(Map<String, Object> rowMap: mapList)
		{
			JdbcMap jdbcMap= new JdbcMap(rowMap);
			
			String part= "(";
			for(FieldService fieldDetail:getFieldList())
			{
				Entry<String, Object> valueEntry = jdbcMap.getFieldEntry(fieldDetail.getCode());
				Object v= valueEntry.getValue();
				String vString= "";
				if(v==null)
				{
					vString= "null";
				}
				//else if(v instanceof Integer || v instanceof Long || v instanceof Double || v instanceof BigDecimal || v instanceof BigInteger)
				else if(v instanceof Number)
				{
					vString= v.toString();
				}
				else
				{
					vString= "'"+v.toString()+"'";
				}
				part+= vString+",";
			}
			part= part.substring(0,part.length()-1);
			part+= "),";
			sql+= part;
		}
		
		sql= sql.substring(0,sql.length()-1);
		
		
		
		//生成语句和参数
		//System.out.println("批量插入的SQL: "+sql);
		
		//执行
		if(dataBase.isEnableLog())
		{
			System.out.println("批量插入的SQL: "+sql);
		}
		try
		{
			int n= dataBase.executeUpdate(sql);
			return n;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException("批量插入失败！");
		}
	}


	@Override
	public int updateByMap(List<Map<String, Object>> mapList, String targetCode) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public boolean drop() {
		// TODO Auto-generated method stub
		String dropTableSQL = dataBase.getDialect().getDropTableSQL(name);
		int n = dataBase.executeUpdate(dropTableSQL);
		return true;
	}
	
	
	@Override
	public boolean tryDrop() {
		// TODO Auto-generated method stub
		try
		{
			return drop();
		}
		catch(Exception e)
		{
			return false;
		}

	}


	@Override
	public boolean createTableAsTo(String targetTable) {
		// TODO Auto-generated method stub
		int executeUpdate= 0;
		
		if(dataBase.getDbType()==DbTypeEnum.Oracle)
		{
			String sql= "create table {TARGET_TABLE} as SELECT *  FROM {SOURCE_TABLE} WHERE 1=2";
			sql= sql
					.replace("{SOURCE_TABLE}", name)
					.replace("{TARGET_TABLE}", targetTable)
					;
			//if(dataBase.isEnableLog()) 
			{
				System.out.println(sql);
			}
			
			
			executeUpdate = dataBase.executeUpdate(sql);
		}
		if(dataBase.getDbType()==DbTypeEnum.Hive||dataBase.getDbType()==DbTypeEnum.MaxCompute)
		{
			
			String sql= "create table if not exists {TARGET_TABLE} as select * from {SOURCE_TABLE} WHERE 1=2";
			sql= sql
					.replace("{SOURCE_TABLE}", name)
					.replace("{TARGET_TABLE}", targetTable)
					;
			if(dataBase.isEnableLog()) 
			{
				System.out.println(sql);
			}
			
			
			executeUpdate = dataBase.executeUpdate(sql);
		}
		
		return executeUpdate>0?true:false;
	}


	@Override
	public boolean createTableLikeTo(String totable) {
		// TODO Auto-generated method stub
		//create table if not exists t_user_bak like t_user
		if(dataBase.getDbType()==DbTypeEnum.Oracle)
		{
			boolean success = createTableAsTo(totable);
			return success;
		}
		else if(dataBase.getDbType()==DbTypeEnum.Hive||dataBase.getDbType()==DbTypeEnum.MaxCompute)
		{
			String exeCString= "create table if not exists {TO_TABLE} like {SOURCE_TABLE}"
					.replace("{TO_TABLE}", totable)
					.replace("{SOURCE_TABLE}", name)
					;
			
			int result = dataBase.executeUpdate(exeCString);
			
			return result>0?true:false;
			
		}
		else
		{
			Error.throwError("不支持！");
		}
		return false;
	}


	@Override
	public boolean hasFieldsIn(TableService another) {
		// TODO Auto-generated method stub
		String text= new String();
		List<FieldService> srcFieldList = getFieldList();
		List<FieldService> toFieldList = another.getFieldList();
		for(FieldService src: srcFieldList)
		{
			boolean found= false;
			for(FieldService to: toFieldList)
			{
				if(src.getCode().equals(to.getCode()))
				{
					found= true;
					break;
				}
			}
			
			
			if(found) 
			{
				//text+=(src.getCode()+ " found \r\n");
			}
			else
			{
				text+=(src.getCode()+ " not found \r\n");
			}
		}
		
		System.out.println(text);
		return false;
	}


	@Override
	public ResultSetReaderImpl topInfo(int top) {
		// TODO Auto-generated method stub
		System.out.println(name);
		String topSQL = dataBase.getDialect().getTopRow(name, top);
		return dataBase.executeQuery(topSQL);
	}


	@Override
	public DataBaseService getDataBase() {
		// TODO Auto-generated method stub
		return dataBase;
	}


	@Override
	public String getComment() {
		// TODO Auto-generated method stub
		String sql = getDataBase().getDialect().getTableComment(dataBase.getDbName(), name);
		String comment= (String)getDataBase().executeQuery(sql).singleRowSingleColumn();
		return comment;
	}


	@Override
	public TemplateService getTemplateService() {
		// TODO Auto-generated method stub
		return templateService;
	}


	@Override
	public String summaryText() {
		// TODO Auto-generated method stub
		StringBuilder sBuilder= new StringBuilder();
		String row0= "{table} {table_comment}".replace("{table}", name).replace("{table_comment}", getComment());
		sBuilder.append(row0);
		for(FieldService field: getFieldList()) {
			String rowN= "{field}:{field_comment}:{db_type}:{jdbc_type}"
				.replace("{field}", field.getCode())	
				.replace("{field_comment}", field.getComment())	
				.replace("{db_type}", field.getJavaType())	
				.replace("{jdbc_type}", "")	
				;
			sBuilder.append(rowN);
			
		}
		
		return sBuilder.toString();
	}


	@Override
	public String summaryHtml() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}


	@Override
	public String getJavaCamelName() {
		// TODO Auto-generated method stub
		return javaName;
	}



	@Override
	public void createFieldFromDiffrentDB(FieldService src) {
		// TODO Auto-generated method stub
		
	}



}
