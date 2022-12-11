/**
 * 
 */
package cn.fomer.jdbc.service;

import java.util.List;

import cn.fomer.jdbc.api.FieldService;
import cn.fomer.jdbc.entity.DbTypeEnum;

/**
 * 2021-04-14 通过meta数据获取表名、字段等信息（由于max不支持show tables等语句）
 * 
 */
public interface MetaDataService {
	
	
	/**
	 * 2021-04-14
	 * hive会把所有数据库的表查到
	 */
	List<String> getTableList();
	
	
	
	/**
	 * 2021-04-14
	 * <p>只支持maxcompute</p>
	 * <p>
	 * maxcompute 新建的表通过SQL可能查不到分区字段
	 * </>
	 */
	List<FieldService> getAllField(String tableName);
	
	/**
	 * 202104
	 */
	List<String> getDbList();
	
	
	/**
	 * 2021-04-14
	 */
	DbTypeEnum getDbType();
	
	
	/**
	 * 2021-04-14
	 * <p>MySQL/Oracle支持</p>
	 * <p>Hive会报错</p>
	 * <p>MaxCompute会把数据库过滤掉 http://service.cn.maxcompute.aliyun.com/api</p>
	 */
	String getUrl();
	
	boolean isSupportBatchUpdate();
}
