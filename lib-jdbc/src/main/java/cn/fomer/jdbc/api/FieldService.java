/**
 * 
 */
package cn.fomer.jdbc.api;

import cn.fomer.jdbc.entity.ColumnTypeEnum;
import cn.fomer.jdbc.entity.DbTypeEnum;
import lombok.Data;

/**
 * 2022-01-25
 * 
 */
@Data
public abstract class FieldService {
	protected Integer id;
	protected String code;
	protected String type;
	protected ColumnTypeEnum columnType;
	protected Integer numlength;
	protected Integer charLength;
	protected Integer precision;	
	protected Integer ordinalPosition;
	protected Boolean nullable;
	protected Object defaultVal;
	protected String comment;
	protected DbTypeEnum dbType;
	protected Boolean isPrimaryKey= false;
	protected Boolean isPartitionField;
	protected String schemaName;
	protected String tableName;
	
	protected TableService table;
	
	public FieldService(TableService table) {
		this.table= table;
	}
	
	abstract public void updateComment(String comment);
	public abstract String getJavaType();
	public abstract String getCodeCamel();
}
