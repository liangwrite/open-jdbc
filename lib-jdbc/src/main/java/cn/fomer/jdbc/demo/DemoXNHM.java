package cn.fomer.jdbc.demo;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import cn.fomer.jdbc.api.DataBaseService;
import cn.fomer.jdbc.api.ColumnService;
import cn.fomer.jdbc.datasource.DataSourceSimpleImpl;
import cn.fomer.jdbc.entity.DbTypeEnum;
import cn.fomer.jdbc.service.DataSourceSimple;
import cn.fomer.jdbc.service.impl.DataBaseServiceImpl;
import cn.fomer.jdbc.service.impl.ResultSetReaderImpl;

/**
 * 2021-08-18
 * 
 */
public class DemoXNHM {


	
	public static void main(String[] args) {
		//
		DataSourceSimple srcDs = DataSourceSimpleImpl.newInstancePostgreSQL("postgres", "123456", "192.168.128.250", 9905, "wit-prod");
		
		//(DataSource ds, String dbName, DbTypeEnum dbTypeEnum)
		//public static SuperDB newInstance(DataSource ds, String dbName, DbTypeEnum dbTypeEnum)
		DataBaseService db= new DataBaseServiceImpl(srcDs);
		
		
		//
		int channelId= 66;
		String areaLjzc= "610118111234";
		List<String> notField= Arrays.asList("zhen", "xian");
		
		//
		List<ColumnService> fieldList = db.getTable("dd_content_attr").getColumnList();
		for(ColumnService f: fieldList)
		{
			//
			String columnName = f.getCode();
			

			
			//
			System.out.println("String "+columnName+"= \""+columnName+"\";");
		}
		
		System.out.println("...........");
		
		//2.生成 FieldKeys
		String sql= "select * from dd_model_item where del_flag=0 and model_id=(select model_id from dd_channel where id=?) order by field";
		ResultSetReaderImpl fieldResult = db.executeQuery(sql, channelId);
		for(int row= 0; row<fieldResult.getRowList().size(); row++)
		{
			List<Object> rowData= fieldResult.getRowList().get(row);
			System.out.println();
			System.out.println("/** {备注} */".replace("{备注}", rowData.get(3).toString()));
			System.out.println("String "+rowData.get(2)+"= \""+rowData.get(2)+"\"; ");
		}
		
		
		//3.生成 JSON
		Map<String, Object> map= new LinkedHashMap<String, Object>();
		for(int row= 0; row<fieldResult.getRowList().size(); row++)
		{
			List<Object> rowData= fieldResult.getRowList().get(row);
			String fieldName= (String)rowData.get(2);
			//不要"zhen" "xian"
			if(!notField.contains(fieldName))
				map.put((String)rowData.get(2), (String)rowData.get(3));
		}	
		map.put("cun", areaLjzc);
		System.out.println(new Gson().toJson(map));
	}
}
