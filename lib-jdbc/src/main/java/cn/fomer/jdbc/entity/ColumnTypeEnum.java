package cn.fomer.jdbc.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.management.RuntimeErrorException;

import lombok.Getter;

/**
 * 2019-01-08 字段类型
 * 
 * 
 */
@Getter
public enum ColumnTypeEnum
{
	//["bit","char","date","datetime","decimal","smallint","varchar"]

	MYSQL_CHAR("CHAR",DbTypeEnum.MySQL, String.class), //mysql
	MYSQL_DATE("DATE",DbTypeEnum.MySQL, Date.class), //mysql
	MYSQL_DATETIME("DATETIME",DbTypeEnum.MySQL, Date.class), //mysql
	MYSQL_DECIMAL("DECIMAL",DbTypeEnum.MySQL, BigDecimal.class), //mysql
	MYSQL_INT("INT",DbTypeEnum.MySQL, Integer.class), //mysql
	MYSQL_VARCHAR("VARCHAR",DbTypeEnum.MySQL, String.class), //mysql
	MYSQL_BIT("BIT",DbTypeEnum.MySQL, Boolean.class), //mysql
	MYSQL_SMALLINT("SMALLINT",DbTypeEnum.MySQL, Integer.class), //mysql
	
	
	/**** 202104以下都是新加的 ****/
	ORACLE_DATE("DATE",DbTypeEnum.Oracle, Date.class), //
	ORACLE_VARCHAR2("VARCHAR2",DbTypeEnum.Oracle, String.class),
	ORACLE_NVARCHAR2("NVARCHAR2",DbTypeEnum.Oracle, String.class),
	ORACLE_NUMBER("NUMBER",DbTypeEnum.Oracle, String.class),
	ORACLE_TIMESTAMP6("TIMESTAMP6",DbTypeEnum.Oracle, String.class),
	ORACLE_BINARY_DOUBLE("BINARY_DOUBLE",DbTypeEnum.Oracle, String.class),
	ORACLE_NCHAR("NCHAR",DbTypeEnum.Oracle, String.class),
	ORACLE_FLOAT("FLOAT",DbTypeEnum.Oracle, String.class),
	ORACLE_CLOB("CLOB",DbTypeEnum.Oracle, String.class),
	ORACLE_RAW("RAW",DbTypeEnum.Oracle, String.class),
	
	
	
	/* 202104 */
	MAX_INT("INT",DbTypeEnum.MaxCompute, String.class),
	MAX_STRING("STRING",DbTypeEnum.MaxCompute, String.class),
	
	
	/* 202108 */
	POSTGRESSQL_INTEGER("integer",DbTypeEnum.PostgreSQL, Integer.class),
	POSTGRESSQL_CHARACTER("character varying",DbTypeEnum.PostgreSQL, String.class),
	POSTGRESSQL_TIMESTAMP("timestamp without time zone",DbTypeEnum.PostgreSQL, Date.class),
	POSTGRESSQL_TEXT("text",DbTypeEnum.PostgreSQL, String.class),
	POSTGRESSQL_BIGINT("bigint",DbTypeEnum.PostgreSQL, Integer.class),
	POSTGRESSQL_SMALLINT("smallint",DbTypeEnum.PostgreSQL, Integer.class),
	POSTGRESSQL_NUMERIC("numeric",DbTypeEnum.PostgreSQL, Double.class),
	POSTGRESSQL_VARCHAR("varchar",DbTypeEnum.PostgreSQL, String.class),
	
	
	/**** 202109 ****/
	SQLITE_INTEGER("4",DbTypeEnum.SQLite, Integer.class), // "12"	
	SQLITE_STRING("12",DbTypeEnum.SQLite, String.class), // "VARCHAR(2000)"
	
	
	/**** 202201 ****/
	KINGBASE_INTEGER("int4",DbTypeEnum.Kingbase, Integer.class), // "12"	
	KINGBASE_STRING("varchar",DbTypeEnum.Kingbase, String.class), // "VARCHAR(2000)"
	
	
	
	UNKOWN("",DbTypeEnum.Unkown, String.class),
	;
	
	
	String typeName;
	DbTypeEnum dataBaseType;
	Class javaType;
	ColumnTypeEnum(String typeName, DbTypeEnum dataBaseType, Class javaType)
	{
		this.typeName= typeName;
		this.dataBaseType= dataBaseType;
		this.javaType= javaType;
	}
	
	
	public static ColumnTypeEnum parse(String s, DbTypeEnum dbType)
	{
		for(ColumnTypeEnum typeEnum: ColumnTypeEnum.values())
		{
			if(typeEnum.dataBaseType== dbType&& typeEnum.typeName.equalsIgnoreCase(s))
			{
				return typeEnum;
			}
		}
		
		throw new RuntimeException(String.format("%s is unkown column type.", s));
		//return UNKOWN;
	}
	
	/**
	 * 2021-04-14 是否是String类型
	 */
	public boolean isString()
	{
		if(this==ORACLE_VARCHAR2)
		{
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		Object object= valueOf("DATE"); //区分大小写
		System.out.println(object);
		
	}
}
