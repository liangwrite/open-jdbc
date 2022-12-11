package cn.fomer.jdbc.demo;

import cn.fomer.jdbc.api.CloneService;
import cn.fomer.jdbc.api.DataBaseService;
import cn.fomer.jdbc.api.TableService;
import cn.fomer.jdbc.clone.service.impl.CloneServiceImpl;
import cn.fomer.jdbc.datasource.DataSourceSimpleImpl;
import cn.fomer.jdbc.service.DataSourceSimple;
import cn.fomer.jdbc.service.impl.DataBaseServiceImpl;

/**
 * 2021-09-14
 * 
 */
public class CloneDemoXnhm {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Result result= CloneFactory.newInstance(src, to).copyTableData("aaa");
		DataSourceSimple srcDs = DataSourceSimpleImpl.newInstancePostgreSQL("postgres", "123456", "192.168.128.128", 5432, "wit");
		DataSourceSimple toDs = DataSourceSimpleImpl.newInstancePostgreSQL("postgres", "123456", "192.168.128.250", 9905, "wit");
		
		DataBaseService srcDB= new DataBaseServiceImpl(srcDs, true);
		DataBaseService toDB= new DataBaseServiceImpl(toDs );
		
		
		//String[] tableArray= {"dd_channel","dd_model","dd_model_item"};
		String[] tableArray= {"dd_model_item"};
		CloneService cloneService= new CloneServiceImpl(srcDB, toDB);
		srcDB.setEnableLog(false);
		toDB.setEnableLog(false);
		
		
		for(String table: tableArray)
		{
			TableService srcTable = srcDB.getTable(table);
			TableService toTable = toDB.getTable(table);
			
			
			int count = cloneService.cloneTableData(srcTable, toTable);				
		}


		System.out.println("quit!");
	}

}
