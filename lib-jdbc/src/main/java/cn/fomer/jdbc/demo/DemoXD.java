package cn.fomer.jdbc.demo;

import cn.fomer.jdbc.api.DataBaseService;
import cn.fomer.jdbc.api.TableService;
import cn.fomer.jdbc.datasource.DataSourceSimpleImpl;
import cn.fomer.jdbc.service.DataSourceSimple;
import cn.fomer.jdbc.service.impl.DataBaseServiceImpl;

/**
 * 2021-12-20 西电mes
 * 
 */
public class DemoXD {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//
		DataSourceSimple srcDs = DataSourceSimpleImpl.newInstanceOracle("mes", "mes", "127.0.0.1", "ORCL");	
		DataBaseService dataBaseService= new DataBaseServiceImpl(srcDs);
		
		TableService table = dataBaseService.getTable("MM_STORAGE_STOCK_IN");
		table.getTemplateService().mapEntity();

		table.getFieldList().forEach(field->{
			//System.out.println(field.getCode()+" -> "+ field.getJavaType());
		});
		
		
		//String summaryText = table.summaryText();
		//System.out.println(summaryText);
	}
	

}
