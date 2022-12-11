package cn.fomer.jdbc.demo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import cn.fomer.jdbc.api.CloneService;
import cn.fomer.jdbc.api.DataBaseService;
import cn.fomer.jdbc.api.TableService;
import cn.fomer.jdbc.clone.service.impl.CloneServiceImpl;
import cn.fomer.jdbc.datasource.DataSourceSimpleImpl;
import cn.fomer.jdbc.service.DataSourceSimple;
import cn.fomer.jdbc.service.impl.DataBaseServiceImpl;

/**
 * 类说明
 *
 * @author Liang
 * @email liang@163.com
 * @date 2022-12-08
 */
public class DemoCloneOraclePgSQL {
	public static void main(String[] args) {
		//
		DataSourceSimple datasourceOracle= 
			DataSourceSimpleImpl.newInstanceOracle("chqjtgczl_dev", "chqjtgczl_dev", "192.168.123.115", "orcl"); 
		
		//String username, String password, String ip, int port, String dbName, String schemaName
		DataSourceSimple datasourcePgSQL= 
				DataSourceSimpleImpl.newInstancePostgreSQL("shaleoil", "Sharewin88323650", "192.168.123.125", 5432, "shaleoil", "ods_jing_tong_ku"); 
		
		DataBaseService oracle= new DataBaseServiceImpl(datasourceOracle, true);
		DataBaseService pgsql= new DataBaseServiceImpl(datasourcePgSQL);
		
		
		CloneService cloneService= new CloneServiceImpl(oracle, pgsql);
		
		List<String> tableList = Arrays.asList("T_SETTLEMENT", "T_SETTLEMENT_WELL", "T_WELLINFO_GROUP", "T_SETTLEMENT_WELL_SKZY", "T_SETTLEMENT_WELL_CJ", "T_WELLINFO_GROUP_CL", "DATA_DEFINITION");
		
		String table= tableList.get(6);
		TableService srcTable = oracle.getTable(table);
		TableService toTable = pgsql.getTable(table);
		cloneService.cloneTableField(srcTable, toTable);
		cloneService.cloneTableData(srcTable, toTable);
		
		
		System.out.println("main is over!");
	}

}
