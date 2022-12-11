/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.fomer.jdbc.service.impl;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import cn.fomer.jdbc.api.CrudService;
import cn.fomer.jdbc.service.MetaDataService;

import lombok.Getter;







/**
 * 
 * @company GINFO
 * @description 数据库
 * @author Liang
 * @作者 Liang
 * @date 2014-5-24 下午6:37:50
 * @version 1.1
 */
@Getter
public class CrudServiceImpl implements CrudService {	
	
	protected MetaDataService metaDataService;
	protected DataSource dataSource;	// create connection by this
	
	/** 如果只读，将不会执行update方法 */
	boolean readonly;
	
	protected boolean enableLog= false;
	int count;
	
	/**
	 * @param ds
	 * @param readonly 只读
	 */
	public CrudServiceImpl(DataSource ds,boolean readonly)
	{
		this.dataSource= ds;
		this.readonly= readonly;
	}
	
	
	public CrudServiceImpl(DataSource ds) 
	{ this(ds, false);}
	
	/**
	 * 2019-08-06 e.g. select * from PbUser where username = ?
	 * @param top 
	 */
	public ResultSetReaderImpl[] executeQueryMultiResponse(String sql,Object ...pArray) 
	{
		++count;
		
		log(this, getClass().getSimpleName()+": "+sql);
		
		if(pArray!=null)
		{
			log(this, getClass().getSimpleName()+": param="+(pArray==null?"null":pArray.toString()));
		}
			
		
		Connection connection = null; // 用来在异常时关闭连接

		//ResultSetReaderImpl ResultSetReaderImpl= null;
		PreparedStatement preparedStatement=null;
		
		
		//List<ResultSet> listResultSet= new ArrayList<>();
		List<ResultSetReaderImpl> listResultSetReaderImpl= new ArrayList<ResultSetReaderImpl>();
		try 
		{	
			connection = dataSource.getConnection();
			
			
			preparedStatement = connection.prepareStatement(sql); // 捕获异常SQLException	
			
			//同时开启fetchSize和maxRows参数时，取最小作为limit来executeQuery
			Integer top= null;
			if(top!=null) preparedStatement.setMaxRows(top);
			//preparedStatement.setFetchSize(1);
//			preparedStatement.setMaxFieldSize(max);
			if(pArray!=null)
				for(int i=0;i<pArray.length;i++)		
				{
					preparedStatement.setObject(i+1, pArray[i]);    //从1开始 且 不能 作为表名
				}
				
			//System.out.println("SqlBase.executeQuery() "+text+ "["+param.toString()+"]");
			//CloneTest.log("远程查询开始...");
			ResultSet resultSet= preparedStatement.executeQuery(); //第一结果集（ 捕获异常SQLException）	
			//CloneTest.log("开始读取数据...");
			//listResultSet.add(resultSet);
			listResultSetReaderImpl.add(new ResultSetReaderImpl(resultSet));
			//CloneTest.log("完成");
			
			
			//第二、第三及其它结果集
			//在条件判断之后，之前的ResultSet会被关闭。因此只能及时使用
			while(preparedStatement.getMoreResults()) 
			{
				ResultSet resultSetTemp= preparedStatement.getResultSet();

				//listResultSet.add(resultSetTemp);
				listResultSetReaderImpl.add(new ResultSetReaderImpl(resultSetTemp));
			}
			

			
			//resultSet.close();
			//preparedStatement.close();
			//connection.close(); // 捕获异常SQLException

			ResultSetReaderImpl[] arr= new ResultSetReaderImpl[listResultSetReaderImpl.size()];
			
			return listResultSetReaderImpl.toArray(arr);
		}
		catch (Exception e)
		{
			e.printStackTrace(); 
			//System.err.println(getClass().getSimpleName()+": ["+e.toString()+"] text="+text);
		} 
		finally
		{
			for(ResultSetReaderImpl r:listResultSetReaderImpl)
			{
				try 
				{ 					
					//if(r.resultSet!=null) r.resultSet.close();					
				} 
				catch(Exception e){}				
			}

			try { if(preparedStatement!=null) preparedStatement.close();} catch(Exception e){}
			try { if(connection!=null) connection.close(); } catch(Exception e){}			
		}		
		return null;
	}
	
	
	/**
	 * 2021-10-11  select * from PbUser where username = ? <br />
	 */
	public ResultSet huageQuery(String text,Object ...pArray) 
	{
		Connection connection = null; // 用来在异常时关闭连接
		
		PreparedStatement preparedStatement=null;
		
		List<ResultSetReaderImpl> listResultSetReaderImpl= new ArrayList<ResultSetReaderImpl>();
		try 
		{	
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(text); // 捕获异常SQLException	
			
			preparedStatement.setFetchSize(10000);
			
			for(int i=1;i<pArray.length;i++)		
			{
				preparedStatement.setObject(i, pArray[i-1]);    //从1开始 且 不能 作为表名
			}
			
			ResultSet resultSet= preparedStatement.executeQuery(); //第一结果集（ 捕获异常SQLException）	
			
			return resultSet;
			
		}
		catch (Exception e)
		{
			e.printStackTrace(); 
		} 
		finally
		{
			for(ResultSetReaderImpl r:listResultSetReaderImpl)
			{
				try 
				{ 					
					//if(r.resultSet!=null) r.resultSet.close();					
				} 
				catch(Exception e){}				
			}
			
			try { if(preparedStatement!=null) preparedStatement.close();} catch(Exception e){}
			try { if(connection!=null) connection.close(); } catch(Exception e){}			
		}		
		return null;
	}
	

	/**
	 * 2018-11-03
	 * 
	 */
	public ResultSetReaderImpl executeQuery(String sql,Object ...pArray)
	{
		return executeQueryMultiResponse(sql, pArray)[0];
	}
	
	/* e.g. update PbUser set password = ? */
	public int executeUpdate(String sql,Object ...pArray)
	{
		try
		{
			++count;
			log(this, getClass().getSimpleName()+": "+sql);
			
			if(pArray!=null&&pArray.length!=0)
			{
				log(this, getClass().getSimpleName()+": param="+(pArray==null?"null":pArray.toString()));
			}
			
			if(readonly)
			{
				log(this, getClass().getSimpleName()+": I'm sorry that this sql server is readonly.");
				return 0;
			}
			
			
			
			Connection connection = null;
			PreparedStatement preparedStatement=null;
			try 
			{			
				connection= dataSource.getConnection();
				preparedStatement = connection.prepareStatement(sql); // 捕获异常SQLException	
				if(pArray!=null)
					for(int i=0;i<pArray.length;i++)
					{
						preparedStatement.setObject(i+1, pArray[i]);    //从1开始 且 不能 作为表名
					}
				
				int n = preparedStatement.executeUpdate(); // 捕获异常SQLException
				preparedStatement.close();
				connection.close(); // 捕获异常SQLException
				return n;
			} 
			finally
			{				
				try{ if(preparedStatement!=null) preparedStatement.close();}catch(Exception e){}
				try{ if(connection!=null) connection.close();}catch(Exception e){}
			}		
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
			System.err.println(String.format("SQL: %s", sql));
			throw new RuntimeException("执行异常!");
		}
	}
	
	/**
	 * 2017-12-12 测试连接
	 * 
	 * */
	public boolean tryConnection()
	{
		try
		{
			Connection conn= dataSource.getConnection();
			conn.close();
			return true;			
		}
		catch(Exception e)
		{ 
			//e.printStackTrace();
			return false;
		}
	}
	

	@Override
	public void setEnableLog(boolean b) {
		// TODO Auto-generated method stub
		this.enableLog= b;
	}


	@Override
	public void log(Object o, String text) {
		// TODO Auto-generated method stub
		if(enableLog)
		{
			System.out.println(o.getClass().getName()+": "+text);
		}
	}



	@Override
	public int executeUpdateBatch(String sql, List<Object[]> varList) {
		// TODO Auto-generated method stub
		try
		{
			//1.是否支持批量
			//if(!metaDataService.isSupportBatchUpdate())
			{
				//Error.throwError("这个数据源不支持批量执行命令！");
			}

			
			//2.
			++count;
			
			
			if(readonly)
			{
				log(this, getClass().getSimpleName()+": I'm sorry that this sql server is readonly.");
				return 0;
			}

			
			Connection connection = null;
			PreparedStatement prepareStatement=null;
			try 
			{			
				connection= dataSource.getConnection();
				prepareStatement = connection.prepareStatement(sql); // 捕获异常SQLException
				
				
				int total= 0;
				for(Object[] row: varList)
				{
					for(int col=0;col<row.length;col++)
					{
						prepareStatement.setObject(col+1, row[col]);
					}
					
					boolean n = prepareStatement.execute(); // 捕获异常SQLException		
					
					total+= 1;
				}

				
				prepareStatement.close();
				connection.close(); // 捕获异常SQLException
				
				
				return total;
			} 
			finally
			{				
				try{ if(prepareStatement!=null) prepareStatement.close();}catch(Exception e){ System.err.println("关闭statement时发生了异常！");}
				try{ if(connection!=null) connection.close();}catch(Exception e){ System.err.println("关闭connection时发生了异常！");}
			}		
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException("执行异常!");
		}
		
	}



	@Override
	public int executeUpdateBatch(String... arr) {
		// TODO Auto-generated method stub
		try
		{
			//1.是否支持批量
			//if(!metaDataService.isSupportBatchUpdate())
			{
				//Error.throwError("这个数据源不支持批量执行命令！");
			}

			
			//2.
			++count;
			log(this, getClass().getSimpleName()+": "+arr);
			
			
			if(readonly)
			{
				log(this, getClass().getSimpleName()+": I'm sorry that this sql server is readonly.");
				return 0;
			}

			
			Connection connection = null;
			Statement statement=null;
			try 
			{			
				connection= dataSource.getConnection();
				statement = connection.createStatement(); // 捕获异常SQLException
				for(String sql: arr)
				{
					statement.addBatch(sql);
				}
				
				int[] n = statement.executeBatch(); // 捕获异常SQLException
				statement.close();
				connection.close(); // 捕获异常SQLException
				
				int total= 0;
				for(int i: n) total+= i;
				return total;
			} 
			finally
			{				
				try{ if(statement!=null) statement.close();}catch(Exception e){ System.err.println("关闭statement时发生了异常！");}
				try{ if(connection!=null) connection.close();}catch(Exception e){ System.err.println("关闭connection时发生了异常！");}
			}		
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException("执行异常!");
		}
		
	}


	
	
}//class

