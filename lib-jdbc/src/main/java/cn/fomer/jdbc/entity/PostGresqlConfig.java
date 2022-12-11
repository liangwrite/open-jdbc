/**
 * 
 */
package cn.fomer.jdbc.entity;

import lombok.Data;

/**
 * 
 *
 * @author Liang
 * @email liang@163.com
 * @date 2022-07-14
 */
@Data
public class PostGresqlConfig {

	String dbName;
	String schemaName="public";
	String username;
	String password;
	String ip;
	int port= 5432;
}
