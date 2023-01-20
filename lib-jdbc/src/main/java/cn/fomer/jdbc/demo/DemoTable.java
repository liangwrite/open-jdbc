package cn.fomer.jdbc.demo;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import cn.fomer.jdbc.api.DataBaseService;
import cn.fomer.jdbc.datasource.DataSourceSimpleImpl;
import cn.fomer.jdbc.entity.DbTypeEnum;
import cn.fomer.jdbc.service.DataSourceSimple;
import cn.fomer.jdbc.service.impl.DataBaseServiceImpl;
import cn.fomer.jdbc.service.impl.ColumnServiceImpl;
import cn.fomer.jdbc.service.impl.ResultSetReaderImpl;

/**
 * 2021-08-18
 * 
 */
public class DemoTable {


	
	public static void main(String[] args) throws IOException {
		//
		DataSourceSimple dataSource = DataSourceSimpleImpl.newInstancePostgreSQL("postgres", "123456", "192.168.128.128", 5432, "wit");
		
		//(DataSource ds, String dbName, DbTypeEnum dbTypeEnum)
		//public static SuperDB newInstance(DataSource ds, String dbName, DbTypeEnum dbTypeEnum)
		DataBaseService db= new DataBaseServiceImpl(dataSource);
		
		
		//
		String html = db.getSummaryService().toHtml(10);
		FileUtils.writeStringToFile(new File("E:\\Users\\Liang\\Desktop\\1.html"), html);
		System.out.println(html);
	}
}
