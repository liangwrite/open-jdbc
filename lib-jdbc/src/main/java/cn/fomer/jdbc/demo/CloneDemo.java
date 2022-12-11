package cn.fomer.jdbc.demo;

import java.io.File;
import java.util.List;

import com.google.gson.Gson;
import cn.fomer.jdbc.api.CloneService;
import cn.fomer.jdbc.api.DataBaseService;
import cn.fomer.jdbc.api.FieldService;
import cn.fomer.jdbc.api.TableService;
import cn.fomer.jdbc.clone.service.impl.CloneServiceImpl;
import cn.fomer.jdbc.datasource.DataSourceSimpleImpl;
import cn.fomer.jdbc.entity.DbTypeEnum;
import cn.fomer.jdbc.service.DataSourceSimple;
import cn.fomer.jdbc.service.impl.DataBaseServiceImpl;

/**
 * 2021-09-14
 * 
 */
public class CloneDemo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Result result= CloneFactory.newInstance(src, to).copyTableData("aaa");
		DataSourceSimple src= DataSourceSimpleImpl.newInstanceSQLite(new File("G:/upload/windows.db"));
		DataSourceSimple dest= DataSourceSimpleImpl.newInstanceSQLite(new File("G:/upload/linux.db"));
		
		DataBaseService srcDB= new DataBaseServiceImpl(src );
		DataBaseService toDB= new DataBaseServiceImpl(dest );
		
		TableService srcTable = srcDB.getTable("md_books");
		TableService toTable = toDB.getTable("md_books");
		//toTable.compareFieldWith(srcTable);
		if(true) return;
		
		List<FieldService> fieldList = toDB.getTable("md_books").getFieldList();
		System.out.println(new Gson().toJson(fieldList));
		
		
		//if(true) return;
		CloneService cloneService= new CloneServiceImpl(srcDB, toDB);
		int count = cloneService.cloneTableData(srcDB.getTable("md_books"), toDB.getTable("md_books"));	

		System.out.println("quit!");
	}

}
