package cn.fomer.jdbc.service.impl;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.fomer.common.html.HtmlHelper;
import cn.fomer.common.util.ObjectHelper;
import cn.fomer.common.util.StringHelper;
import cn.fomer.jdbc.entity.Error;
import cn.fomer.jdbc.service.ResultSetReader;

import lombok.Data;
import lombok.Getter;

/**
 * 自定义查询结果
 * 201901 oracle.sql.CLOB 类型会立即读取完，以String类型保存
 * 自带关闭
 *
 */
@Getter
public class ResultSetReaderImpl implements ResultSetReader
{
	
	
	/**
	 * 2021-04-14
	 * @throws Exception 
	 */
	public static ResultSetReaderImpl getInstance(ResultSet resultSet)
	{
		try
		{
			return new ResultSetReaderImpl(resultSet);
		}
		catch(Exception e)
		{
			Error.throwError(e);
			return null;
		}
		
	}

	
	
	
	ResultSet resultSet;
	List<String> columnNameList= new ArrayList<String>(10);
	List<Class<?>> clumnClassList= new ArrayList<Class<?>>(10);
	List<List<Object>> rowList= new ArrayList<List<Object>>(10);
	
	int size;
	
	/**
	 * 每列JDBC类型
	 * 自带关闭
	 * @throws IOException 
	 */
	public ResultSetReaderImpl(ResultSet resultSet)
	{		
		this.resultSet= resultSet;	
		
		try
		{
			//1.先读取列信息
			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();	
			
			for(int column=0;column<columnCount;column++)
			{				
				columnNameList.add(metaData.getColumnName(column+1)); /* metaData 从 1开始 */	
				
				
				//占位
				clumnClassList.add(null);
			}
			

			
			
			//2.获取数据
			while(resultSet.next())
			{			
				List<Object> objectList= new ArrayList<Object>();
				for (int i = 0; i < columnNameList.size(); i++) 
				{
					
					String columnName= columnNameList.get(i);
					Object columnValue= resultSet.getObject(columnName);
					if(clumnClassList.get(i)==null&&columnValue!=null) 
					{
						clumnClassList.set(i, columnValue.getClass());
					}
					
					//CLOB oracle
					if(columnValue instanceof ByteArrayInputStream)
					{
						String text= null;
						
						//CLOB clob= (oracle.sql.CLOB)value;
						//InputStream inputStream= clob.getStream();
						byte[] buf= new byte[1024];
						InputStream inputStream= new ByteArrayInputStream(buf);
						byte[] buffer= new byte[inputStream.available()];
						try
						{ 
							int len = inputStream.read(buffer);
							text= new String(buf, 0, len, "utf-8");
						}
						catch(Exception e) { e.printStackTrace();}
						
						columnValue= text;
					}
					objectList.add(columnValue);
				}
				
				rowList.add(objectList);
			}
			
			
			size= rowList.size();
				
		}
		catch(Exception e)
		{ 
			e.printStackTrace(); 
		}
		finally
		{
			if(resultSet!=null) try { resultSet.close();} catch(Exception e1) { System.err.println("resultSet关闭失败！");} 
		}
	}
	
	
	
	



	/**
	 * 查询结果生成html <table></table>
	 */
	public String toHtml(boolean css)
	{	
		StringBuilder strBuilder= new StringBuilder();
		
		
		//1.table <table>
		String tmpText= "<table cellspacing='0' cellpadding='0' style='' >"
				.replace("style=''", css?"style='border-collapse:collapse; font-size:12px; '":"");
		
		strBuilder.append(tmpText);		
				
		
		//2.1 列名 header <tr><th></th></tr> 
		strBuilder.append("<tr>");
		tmpText= "<th style='' >columnName</th>"
				.replace("style=''", css?"style='border:1px solid black;'":"");
		for(String columnName:columnNameList) strBuilder.append(tmpText.replace("columnName", columnName.toLowerCase()));
		strBuilder.append("</tr>");
		
		
		
		//2.1 SQL类型 header <tr><th></th></tr>
		/*
		strBuilder.append("<tr>");
		tmpText= "<th style='' >columnName</th>"
				.replace("style=''", css?"style='border:1px solid black;'":"");
		for(Class clazz:clumnClassList) strBuilder.append(tmpText.replace("columnName", clazz==null?"": clazz.getSimpleName()));
		strBuilder.append("</tr>");
		
		//2.2 java类型 header <tr><th></th></tr>
		strBuilder.append("<tr>");
		tmpText= "<th style='' >columnName</th>"
				.replace("style=''", css?"style='border:1px solid black;'":"");
		for(Class clazz:clumnClassList) strBuilder.append(tmpText.replace("columnName", clazz==null?"": clazz.getSimpleName()));
		strBuilder.append("</tr>");
		*/
		
		
		//3 行数据 <tr><td></td></tr>
		for(int i=0; i<rowList.size(); i++)
		{
			//
			List<Object> valueList=rowList.get(i);
			
			strBuilder.append("<tr>");
			for(Object value: valueList)
			{
				String finalValue= null;
				if(ObjectHelper.isByteArray(value)) {
					byte[] byteValue= (byte[])value;
					
					try {
						finalValue = new String(byteValue, "utf-8");
					}
					catch(Exception e) {
						System.err.println("byte[] 转 String 失败！");
					}
				}
				if(finalValue==null&&value!=null) {
					finalValue= value.toString();
				}
				
				tmpText= "<td style='' >";
				strBuilder.append(tmpText.replace("style=''", css?"style='border:1px solid black;'":""));
				try{ strBuilder.append(StringHelper.substring(HtmlHelper.toSafeHtml(finalValue==null?"":finalValue), 100));}
				catch(Exception e){ strBuilder.append("error");}
				strBuilder.append("</td>");
			}
			strBuilder.append("</tr>");			
		}
		
		
		//4.</table>
		strBuilder.append("</table>");
		return strBuilder.toString();				
	}	
	


	/**
	 * 
	 * 2017-11-14 Liang
	 * 
	 * 
	 * */
	public String toText()
	{
		StringBuilder strBuilder= new StringBuilder();
		
		//1.表头
		for(String columnName:columnNameList)
		{
			strBuilder.append(columnName+"\t");
		}
		strBuilder.append("\r\n");
		
		
		//2.内容
		for(List<Object> valueList: rowList)
		{
			for(Object columnValue: valueList)
			{
				strBuilder.append(String.valueOf(columnValue)+"\t");
			}
			strBuilder.append("\r\n");
		}
		
		//3.类型
		for(Class<?> clazz:clumnClassList)
		{
			strBuilder.append((clazz==null?"null":clazz.getSimpleName())+"\t");
		}
		strBuilder.append("\r\n");

		return strBuilder.toString();				
	}


	/**
	 * 20181010 仅适用于1行1列，比如count(*)
	 * 
	 * */
	public Object singleRowSingleColumn() {
		// TODO Auto-generated method stub
		if(rowList.size()>0&&columnNameList.size()>0)
		{
			return rowList.get(0).get(0);
		}
		return null;
	}	
	

	/**
	 * @return 20181205 仅适用于仅有1行或0行
	 * 
	 */
	public Map<String, Object> singleRow()
	{
		Map<String, Object> rowMap= new LinkedHashMap<String, Object>();
		if(rowList.size()>0) 
		{
			for(int i=0;i<columnNameList.size();i++)
			{
				List<Object> firstRow= rowList.get(0);
				rowMap.put(columnNameList.get(i), firstRow.get(i));
			}
			
			return rowMap;
		}
		
		return null;
	}

	
	/**
	 * 2019-01-15 返回查询结果第n列合集（从0开始）
	 * column 第几列
	 * 
	 */
	public List<Object> singleColumn(int columnIndex)
	{
		List<Object> result= new ArrayList<Object>();
		for(int i=0; i<rowList.size(); i++)
		{
			result.add(rowList.get(i).get(columnIndex));
		}
		
		return result;
	}
	
	
	/**
	 * 202104
	 * 
	 */
	public List<Object> singleColumn(String columnName)
	{
		
		//1.确定是那一列
		int index= -1;
		for(int i=0; i<columnNameList.size(); i++)
		{
			if(columnNameList.get(i).equalsIgnoreCase(columnName))
			{
				index= i;
				break;
			}
		}
		
		if(index==-1)
		{
			Error.throwError("找不到当前列！columnName="+columnName);
		}
		
		
		
		//2.拼值
		List<Object> valueList= new ArrayList<Object>();
		for(int i=0; i<rowList.size(); i++)
		{
			List<Object> objectList = rowList.get(i);
			valueList.add(objectList.get(index));
		}

		return valueList;
	}


	
	
	/**
	 * 202104 只有一列或只取第一列
	 * 
	 */
	public List<Object> singleColumn()
	{
		return singleColumn(0);
	}
	
	
	/**
	 * 2021-04-14
	 */
	public List<Map<String, Object>> mapList()
	{
		List<Map<String, Object>> mapList= new ArrayList<Map<String,Object>>();
		for(int row= 0;row<rowList.size();row++)
		{
			List<Object> valueList= rowList.get(row);
			
			Map<String, Object> rowMap= new LinkedHashMap<String, Object>();
			for(int col= 0;col<columnNameList.size();col++)
			{
				String key= columnNameList.get(col);
				Object value= valueList.get(col);
				rowMap.put(columnNameList.get(col), valueList.get(col));
			}
			
			mapList.add(rowMap);
		}
		
		return mapList;
	}
	
	/**
	 * 2022-03 将（code, name）转换为map
	 */
	public Map<String, String> getDictMap()
	{
		Map<String, String> map= new HashMap<>();
		
		for(int row= 0;row<rowList.size();row++)
		{
			String key = (String)rowList.get(row).get(0);
			String value = (String)rowList.get(row).get(1);
			map.put(key, value);
		}
		
		return map;
	}
	
	
	/**
	 * 2021-04-14
	 */
	public int size()
	{
		return rowList.size();
	}
	
	
	/**
	 * 2021-04-14
	 */
	public Object get(int row, String columnName)
	{
		int indexOf = columnNameList.indexOf(columnName);
		if(indexOf!=-1)
		{
			return rowList.get(row).get(indexOf);
		}
		
		Error.throwError("字段不存在！");
		return null;
	}
}
