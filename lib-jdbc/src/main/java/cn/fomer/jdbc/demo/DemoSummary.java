package cn.fomer.jdbc.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.mapred.FileOutputCommitter;

import com.google.gson.Gson;
import cn.fomer.jdbc.api.DataBaseService;
import cn.fomer.jdbc.datasource.DataSourceSimpleImpl;
import cn.fomer.jdbc.entity.DbTypeEnum;
import cn.fomer.jdbc.service.DataSourceSimple;
import cn.fomer.jdbc.service.SummaryService;
import cn.fomer.jdbc.service.impl.DataBaseServiceImpl;
import cn.fomer.jdbc.service.impl.FieldServiceImpl;
import cn.fomer.jdbc.service.impl.ResultSetReaderImpl;
import cn.fomer.common.util.SystemHelper;

/**
 * 2021-08-18
 * 
 */
public class DemoSummary {


	
	public static void main(String[] args) throws IOException {
		//
		DataSourceSimple srcDs = DataSourceSimpleImpl.newInstanceOracle("mes", "mes", "47.108.88.232", "ORCL");
		DataBaseService db= new DataBaseServiceImpl(srcDs);
		SummaryService summaryService = db.getSummaryService();
		summaryService.toHtmlFile(20, SystemHelper.getDesktop("summary.html").getCanonicalPath());
		System.out.println("完成.");
		
	}
}
