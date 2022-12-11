package cn.fomer.jdbc.demo;

import cn.fomer.common.util.SystemHelper;
import cn.fomer.jdbc.api.DataBaseService;
import cn.fomer.jdbc.datasource.DataSourceSimpleImpl;
import cn.fomer.jdbc.service.DataSourceSimple;
import cn.fomer.jdbc.service.impl.DataBaseServiceImpl;

/**
 * 2022-08
 * 
 */
public class DemoLezao {


	
	public static void main(String[] args) {
		//
		DataSourceSimple srcDs = DataSourceSimpleImpl.newInstanceMySQL("root", "123456", "localhost", "ruoyi-official");
		
		//(DataSource ds, String dbName, DbTypeEnum dbTypeEnum)
		//public static SuperDB newInstance(DataSource ds, String dbName, DbTypeEnum dbTypeEnum)
		DataBaseService db= new DataBaseServiceImpl(srcDs);
		String path = SystemHelper.getDesktop("1.html").getPath();
		db.getSummaryService().toHtmlFile(10, path);
		System.out.println(path);
		System.out.println("over");
		
	}
}
