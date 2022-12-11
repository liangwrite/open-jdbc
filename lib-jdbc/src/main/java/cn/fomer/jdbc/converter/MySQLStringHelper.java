/**
 * 
 */
package cn.fomer.jdbc.converter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import cn.fomer.common.io.StringHelper;

/**
 * 
 *
 * @author Liang
 * @email liang@163.com
 * @date 2022-07-14
 */
public class MySQLStringHelper {
	/*
	CREATE TABLE `blade_code` (
			  `id` bigint(20) NOT NULL COMMENT '主键',
			  `datasource_id` bigint(20) DEFAULT NULL COMMENT '数据源主键',
			  `service_name` varchar(64) DEFAULT NULL COMMENT '服务名称',
			  `code_name` varchar(64) DEFAULT NULL COMMENT '模块名称',
			  `table_name` varchar(64) DEFAULT NULL COMMENT '表名',
			  `table_prefix` varchar(64) DEFAULT NULL COMMENT '表前缀',
			  `pk_name` varchar(32) DEFAULT NULL COMMENT '主键名',
			  `package_name` varchar(500) DEFAULT NULL COMMENT '后端包名',
			  `base_mode` int(2) DEFAULT NULL COMMENT '基础业务模式',
			  `wrap_mode` int(2) DEFAULT NULL COMMENT '包装器模式',
			  `api_path` varchar(2000) DEFAULT NULL COMMENT '后端路径',
			  `web_path` varchar(2000) DEFAULT NULL COMMENT '前端路径',
			  `is_deleted` int(2) DEFAULT '0' COMMENT '是否已删除',
			  PRIMARY KEY (`id`) USING BTREE
			) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='代码生成表'	
	*/
	public static String formatePgsql(String ddl) {
		List<String> lineList= null;
		try { lineList= StringHelper.readLine(ddl);}
		catch(Exception e) {
			e.printStackTrace();
			return "";
		}
		
		
		StringBuilder sb= new StringBuilder();
		for(String line: lineList) {
			String lower= line.toLowerCase();
			int start = lower.lastIndexOf(" comment ");
			int end = line.lastIndexOf(",");
			if(start!=-1 && end!=-1) {
				String newLine= line.substring(0, start)+",";
				sb.append(newLine+"\r\n");
			}
			else {
				// 不要 "ENGINE="
				if(lower.contains("engine=")) {
					String newLine= line.substring(0, lower.lastIndexOf("engine="));
					sb.append(newLine+"\r\n");
				}
				else {
					sb.append(line+"\r\n");
				}
			}
		}
		
		return sb.toString();
	}
	
}
