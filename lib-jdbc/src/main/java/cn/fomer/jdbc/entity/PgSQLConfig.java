/**
 * 
 */
package cn.fomer.jdbc.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 
 *
 * @author Liang
 * @email liang@163.com
 * @date 2022-07-14
 */
@Data
@Accessors(chain = true)
public class PgSQLConfig {

	String dbName;
	String schemaName="public";
	String username;
	String password;
	String ip;
	int port= 5432;
}
