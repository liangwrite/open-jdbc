package cn.fomer.jdbc.clone.service.impl;

import java.util.List;
import java.util.Map;

import cn.fomer.common.entity.ResultVO;
import cn.fomer.jdbc.api.CloneService;
import cn.fomer.jdbc.api.DataBaseService;
import cn.fomer.jdbc.api.ColumnService;
import cn.fomer.jdbc.api.TableService;
import cn.fomer.jdbc.clone.service.ISQLBuilderService;
import cn.fomer.jdbc.entity.DbTypeEnum;

/**
 * 2018-11-08
 * 
 * 
 */
public class CloneServiceImpl implements CloneService
{
	DataBaseService srcDB;
	DataBaseService toDB;
	public ISQLBuilderService toSqlBuilder;
	
	
	public CloneServiceImpl(DataBaseService srcDB,DataBaseService tarDB)
	{
		// TODO Auto-generated constructor stub
		this.srcDB= srcDB;
		this.toDB= tarDB;
		this.toSqlBuilder= SQLFactory.getSQLBuilder(toDB);
	}

	@Override
	public void cloneTableField(TableService srcTable, TableService toTable) {
		// TODO Auto-generated method stub
		System.out.println(String.format("准备克隆表 %s -> %s...", srcTable.getName(), toTable.getName()));
		
		//1 同类型
		if(srcDB.getDbType()==toDB.getDbType())
		{
			if(srcDB.getDbType()==DbTypeEnum.MySQL)
			{
				String ddl = srcTable.getDDL();
				try
				{
					int executeUpdate = toDB.executeUpdate(ddl);
					return;
				}
				catch(Exception e){ e.printStackTrace();}
			}
		}
		
		//2 不同类型
		System.out.println(String.format("正在查询表结构...", ""));
		ResultVO res= toSqlBuilder.getTableDDL(srcTable);
		if(res.getSuccess()!=true)
		{
			//System.err.println(JacksonUtil.toJson(createTableResult));
			return;
		}
		//System.out.println(createTableResult.getData());
		try
		{
			System.out.println(String.format("正在创建表...", ""));
			if(toDB.hasTable(srcTable.getName())) {
				throw new RuntimeException(String.format("table %s already exist!", toTable.getName()));
			}
			toDB.executeUpdate((String)res.getData());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		

		//2.表注释 
		System.out.println(String.format("正在添加表注释...", ""));
		cloneTableComment(srcTable, toTable);
		
		//3 字段注释
		System.out.println(String.format("正在添加字段注释...", ""));
		List<ColumnService> srcFieldList = srcTable.getColumnList();
		for(ColumnService srcField: srcFieldList) {
			toTable.getField(srcField.getCode()).updateComment(srcField.getComment());
		}
		
		//
		//int result= dstSQL.executeUpdate(createTableSQL);
	}




	@Override
	public void cloneView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean cloneTableSequenceInOracle(String table) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean cloneTableConstraint(String table) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void cloneFunOfOracle() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cloneProcedure() {
		// TODO Auto-generated method stub
		
	}


//	@Override
	public void cloneTableData(String db,String tableName, int top) {
		// TODO Auto-generated method stub
		if(srcDB.getDbType()==toDB.getDbType())
		{
			List<Map<String, Object>> rowList = srcDB.getTable(tableName).top(top);
			toDB.getTable(tableName).insertByMap(rowList);
		}
		
	}

	@Override
	public Boolean support() {
		// TODO Auto-generated method stub
		
		return false;
	}

	@Override
	public void cloneTableComment(TableService srcTable, TableService toTable) {
		// TODO Auto-generated method stub
		//List<FieldVO> fieldList= srcSQL.getFields(table);
		toTable.updateComment(srcTable.getComment());
	}

	@Override
	public boolean cloneTableIndex(String table) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean cloneTableTrigger(String table) {
		// TODO Auto-generated method stub
		return false;
	}

	//@Override
	public void cloneTable(TableService srcTable, TableService toTable) {
		// TODO Auto-generated method stub
		cloneTableField(srcTable, toTable);
		//cloneTableTrigger(srcTable, toTable);
		//cloneTableConstraint(srcTable, toTable);
		//cloneTableData(db,table);
	}

	/* (non-Javadoc)
	 * @see liang.jdbc.clone.ia.CloneService#cloneTableStruct(java.lang.String)
	 */
	@Override
	public boolean cloneTableStruct(String table) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see liang.jdbc.clone.ia.CloneService#cloneTableData(java.lang.String, int)
	 */
	@Override
	public int cloneTableData(String table, int top) {
		// TODO Auto-generated method stub
		
		
		return -1;
	}

	@Override
	public int cloneTableData(TableService srcTable, TableService toTable) {
		// TODO Auto-generated method stub
		
		//-1
		int count_all= 0;
		
		//0.
		//System.out.println("1.清空目标表...");
		//toTable.clear();
		
		//1.2.一共要复制1000条数据...
		count_all=srcTable.count();
		System.out.println(String.format("开始写入数据（%d条）...", count_all));
		
		//3.已完成 57% (23/1000)
		//int n = toTable.insertByMap(srcTable.all());

		
		//List<Map<String, Object>> mapList = srcTable.all();
		int pageCount = srcTable.getPageCount();
		for(int p=0;p<pageCount;p++) {
			
			toTable.insertByMap(srcTable.getPage(p));
			int now= (p+1)*TableService.PAGE_SIZE;
			System.out.println(String.format("已经写入%d/%d条数据...", now<count_all?now:count_all, count_all));
		}
		//toTable.insertByMap(srcTable.all());
		
	
		//4.已完成
		return count_all;
	}


}
