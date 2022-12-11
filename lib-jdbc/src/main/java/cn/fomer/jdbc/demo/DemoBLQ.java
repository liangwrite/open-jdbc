package cn.fomer.jdbc.demo;

import java.util.List;

import cn.fomer.jdbc.api.DataBaseService;
import cn.fomer.jdbc.api.TableService;
import cn.fomer.jdbc.datasource.DataSourceSimpleImpl;
import cn.fomer.jdbc.service.DataSourceSimple;
import cn.fomer.jdbc.service.impl.DataBaseServiceImpl;

/**
 * 2022-01 避雷器
 * 
 */
public class DemoBLQ {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//
		DataSourceSimple srcDs =
		DataSourceSimpleImpl.newInstance("jdbc:kingbase8://47.108.88.232:54321/mes?currentSchema=mes_business", "com.kingbase8.Driver", "system", "mes", "mes_business");
		//(DataSource ds, String dbName, DbTypeEnum dbTypeEnum)
		//public static SuperDB newInstance(DataSource ds, String dbName, DbTypeEnum dbTypeEnum)
		DataBaseService db= new DataBaseServiceImpl(srcDs);
		//db.setEnableLog(false);
		List<TableService> tableList = db.getTableList();
		db.getTableList().forEach(table->{
			System.out.println(table.getName());
		});
		
		//Table table = db.getTable("eq_equipment_maintain");
		//TemplateService templateService = table.getTemplateService();
		
		//templateService.setAuthor("Liang");
		//templateService.mapEntity();
		//templateService.mapMapper();
		

		//List<FieldDetail> fieldList = table.getFieldList();
		//System.out.println(new Gson().toJson(fieldList));

		
	}
	

}
