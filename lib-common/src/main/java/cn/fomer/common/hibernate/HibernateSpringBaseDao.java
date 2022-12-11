package cn.fomer.common.hibernate;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.c3p0.internal.C3P0ConnectionProvider;
import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.mchange.v2.c3p0.PoolBackedDataSource;


/**
 * 2018-12-01 仅适用于spring环境
 * 
 * 
 */
public abstract class HibernateSpringBaseDao 
{
	
	boolean isShowLog= true;

	/**
	 * 20181201 可以支持Spring和非Spring两种环境
	 * 可能的情况：
	 * 1.sessionFactory.getCurrentSession(); //spring
	 * 2.sessionFactory.openSession(); //非spring
	 * 
	 * */
	public abstract SessionFactory getSessionFactory();
	
	/**
	 * 2017-12-10 
	 * 
	 * 
	 * */
	public Object merge(Object obj)
	{
		
		Session session= getSessionFactory().getCurrentSession();
		return session.merge(obj);	
	}
	
	/**
	 * 
	 * 2017-12-16 脱离持久化（）
	 * 
	 * */
	public void evict(Object obj)
	{
		Session session= getSessionFactory().getCurrentSession();
		session.evict(obj);		
	}
	
	
	public void save(Object obj)
	{
		Session session= getSessionFactory().getCurrentSession();
		Serializable serializable= session.save(obj);		
	}
	
	public void update(Object obj)
	{
		Session session= getSessionFactory().getCurrentSession();
		session.update(obj);
	
	}
	
	public void update(List<?> list)
	{
		Session session= getSessionFactory().getCurrentSession();
		for(Object obj:list)
		{
			session.update(obj);
		}
	}
	
	
	
	public void saveOrUpdate(Object obj)
	{
		Session session= getSessionFactory().getCurrentSession();
		session.saveOrUpdate(obj); //会给实体加上id
	
	}
	
	
	/**
	 * 
	 * 2017-11-21
	 * 
	 * */
	public void save(List<?> list)
	{
		if(isShowLog) System.out.println(getClass().getSimpleName()+": 准备批量写入"+list.size()+"条数据");
		Session session= getSessionFactory().getCurrentSession();
		for(Object obj:list)
		{
			session.save(obj); //在这里捕捉不到异常。只有事务提交才会有。
		}		
	}
	
	
	public void delete(Object obj)
	{
		Session session= getSessionFactory().getCurrentSession();
		session.delete(obj);	

	}
	
	/**
	 * 2018-01-13
	 * 
	 * */
	public void delete(List<?> entityList)
	{
		Session session= getSessionFactory().getCurrentSession();
		for(Object entity:entityList) session.delete(entity);
		
	}
	
	
	/**
	 * 
	 * 
	 * 
	 * */
	public int delete(Class<?> clazz,Serializable id)
	{

		
		Session session= getSessionFactory().getCurrentSession();
		Object obj= findById(clazz, id);
		if(obj==null) return 0;
		
		session.delete(obj); //删除null会有报错		
		return 1;
	}
	
	
	/**
	 * 2017-12-22
	 * 删除一组数据
	 * 
	 * */
	public int delete(Class<?> clazz,List<Integer> IDS)
	{
		//校验
		for(Integer id:IDS)
		{
			if(id==null)
			{
				return -1;
			}
		}
		
		
		for(Integer id:IDS)
		{
			delete(clazz,id);
		}
		return IDS.size();		
	}

	
	

	
	/**
	 * 
	 * 2017-12-02 
	 * 2018-01-08
	 * @param orderMap 排序参数（true：升序，false: 降序）
	 * 
	 * */
	public <T> List<T> findByObject(T obj,Map<T, Boolean> orderMap)
	{	


		String logStr= "";
		Map<String, Object> paramMap= new HashMap<String, Object>();
		if(obj!=null)
		{
			Class<?> clazz= obj.getClass();
			Field[] arr= clazz.getDeclaredFields();
			for(Field field:arr)
			{				
				String fieldName= field.getName();
				Object fieldValue= null;
				if(!Modifier.isPublic(field.getModifiers()))
				{
					field.setAccessible(true);
				}
				try
				{					
					fieldValue= field.get(obj);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				if(fieldValue!=null)
				{
					paramMap.put(fieldName, fieldValue);
					logStr+= fieldName+":"+fieldValue.toString()+"\t";
				}
				
			}
		}
		
		String hql= "from "+obj.getClass().getName()+" "; //
		
		
		hql+= "where ";
		
		for(String fieldName:paramMap.keySet())
		{
			hql+= fieldName+" = "+":"+fieldName+" and ";
		}
		
		hql+= " 1=1";			

		
		if(orderMap!=null&&orderMap.size()>0)
		{			
			for(Entry<T, Boolean> entry:orderMap.entrySet())
			{
				Boolean asc= entry.getValue();
				String fieldName= getFirstNotNullFieldName(entry.getKey());
				
				if(fieldName!=null&&asc!=null)
				{
					//自动拼接了参数的情况下（上面必须留结尾）
					if(hql.endsWith(" 1=1"))
					{
						hql+= " order by ";
					}
					//已经拼接了排序字段的情况下 
					else if(hql.endsWith(" asc")||hql.endsWith("desc")) 
					{
						hql+= ",";
					}
					else
					{
						//没有拼接参数或排序字段时
					}
					
					
					hql+= " "+fieldName+" "+(asc?"asc":"desc");
				}
				
			}
		}
		if(isShowLog) System.out.println(getClass().getSimpleName()+" - Create HQL: "+hql);
		return (List<T>)queryByHql(hql, paramMap);
		
	}
	
	/**
	 * 2018-01-08 找出对象中第一个非null字段
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * 
	 * */
	private String getFirstNotNullFieldName(Object entity)
	{
		if(entity!=null)
		{
			Field[] fields= entity.getClass().getDeclaredFields();
			for(Field f:fields)
			{
				if(!Modifier.isPublic(f.getModifiers()))
				{
					f.setAccessible(true);					
				}
				
				Object value= null;
				try
				{
					value= f.get(entity);
				}
				catch(Exception e)
				{
					e.printStackTrace();
					continue;
				}
				
				if(value!=null)
				{
					return f.getName();
				}
			}
		}
		
		return null;		
	}
	
	/**
	 * 
	 * 2017-12-02 
	 * 
	 * 
	 * */
	public <T> List<T> findByObject(T obj)
	{
		return findByObject(obj,null);
	}

	
	
	public <T> List<T> findByObjectLikeQuery(Object obj)
	{
		String logStr= "";
		Map<String, Object> paramMap= new HashMap<String, Object>();
		if(obj!=null)
		{
			Class<?> clazz= obj.getClass();
			Field[] arr= clazz.getDeclaredFields();
			for(Field field:arr)
			{				
				String fieldName= field.getName();
				Object fieldValue= null;
				if(!Modifier.isPublic(field.getModifiers()))
				{
					field.setAccessible(true);
				}
				
				try{ fieldValue= field.get(obj);} catch(Exception e){ e.printStackTrace();}
				
				if(fieldValue!=null)
				{
					paramMap.put(fieldName, "%"+fieldValue+"%");
					logStr+= fieldName+":"+paramMap.get(fieldName)+"\t";
				}
				
			}
		}
		
		String hql= "from "+obj.getClass().getName();
		if(paramMap.size()>0)
		{
			hql+= " where ";
			
			for(String fieldName:paramMap.keySet())
			{
				hql+= fieldName+" like "+":"+fieldName+" and ";
			}
			
			hql+= " 1=1";			
		}		
		if(isShowLog) System.out.println(getClass().getSimpleName()+" - Create HQL: "+hql);
		//System.out.println(getClass().getName()+" - Build HQL param: "+logStr); //后面还会报参数
		return (List<T>)queryByHql(hql, paramMap);
		
		
	}
	
	public <T> T findById(Class<T> clazz,Serializable id)
	{
		Session session= getSessionFactory().getCurrentSession();
		
		if(isShowLog) System.out.println(getClass().getSimpleName()+" 参数："+id);
		
		@SuppressWarnings("unchecked")
		T t= (T)session.get(clazz, id);
		
		return t;
	}

	
	/**
	 * 2018-05-10 返回结果T类型：1.实体; 2.Object[] 3.Object
	 * @param paramMap "from UserPO "
	 * 
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public <T> List<T> queryByHql(String hql,Map<String, Object> paramMap)
	{
		return (List<T>)queryByHql(hql,paramMap,null,null);
	}
	
	/**
	 * 
	 * 
	 * @param PageNo 页码从0开始（改为private，避免baseDao提示多余的方法）
	 * 
	 * */
	private List<?> queryByHql(String hql,Map<String, Object> paramMap,Integer PageSize,Integer PageNo)
	{
	
		Session session= getSessionFactory().getCurrentSession();
		//Session session= getSessionFactory().getCurrentSession().openSession();
		Query query= session.createQuery(hql);
		if(paramMap!=null)
		{
			String text= "";
			for(String name:paramMap.keySet())
			{
				query.setParameter(name, paramMap.get(name));
				text+= name+":"+paramMap.get(name)+";";
			}
			
			if(isShowLog) System.out.println(getClass().getSimpleName()+" 参数："+text);
		}
		
		
		/* 分页代码 */		
		if(PageSize!=null&&PageNo!=null)
		{	
			if(PageNo!=null&&PageNo>=0)
			{
				query.setFirstResult(getPageStartId(PageSize, PageNo));
				query.setMaxResults(PageSize);				
			}
		}
		
		

		
		List<?> list= query.list();
		return list;		
	}
	
	/**
	 * 
	 * 2017-11-22
	 * 
	 * 查询结果返回分页信息
	 * @param PageNo 从0开始
	 * 
	 * */
	public Page queryByHqlWithPage(String hql,Map<String, Object> paramMap,Integer PageSize,Integer PageNo)
	{
		Page page= new Page();
		
		if(PageNo==null) PageNo= 0; //
		
		String countHql= createCountHql(hql);
		if(countHql!=null)
		{
			Long Total_Size= null;
			try{ Total_Size= (Long)queryByHql(countHql, paramMap).get(0);}
			catch(Exception e)
			{ 
				if(isShowLog) System.out.println("自动生成COUNT查询语句 - 执行：出错！");
				e.printStackTrace();
			}
			if(Total_Size!=null)
			{
				int n= Total_Size.intValue();
				int PageCount= 0;
				if(n%PageSize==0) PageCount= n/PageSize;
				else PageCount= n/PageSize+1;	
				page.setDataSize(Total_Size.intValue());
				page.setPageCount(PageCount);				
			}
		}
		
		page.setList(queryByHql(hql, paramMap, PageSize, PageNo));
		return page;
	}

	
	/**
	 * 
	 * @param PageSize
	 * @param pageNo 从0开始
	 * 
	 * 
	 * */
	private int getPageStartId(Integer PageSize,Integer pageNo)
	{
		int first=0;
		if(pageNo==0) 
		{
			first=0;
		}
		else
		{
			first= PageSize*pageNo;
		}
		if(isShowLog) System.out.println("PageStart:"+first);
		return first;
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> findAll(Class<T> clazz)
	{
		return (List<T>)queryByHql("from "+clazz.getName(), new HashMap<String, Object>());
	}
	

	public int deleteAll(Class<?> clazz)
	{
		String hql= "delete from "+clazz.getName();
		//return executeUpdateByHql(hql);
		return updateByHql(hql, new HashMap<String,Object>());
	}


	/**
	 * 
	 * 2017-11-22
	 * @param selectHql 查询语句
	 * 
	 * "from UserPO where id = ?"
	 * "select id from UserPO where id = ?"
	 * "select id from UserPO where id = ? order by isDirectory desc, name asc"
	 * " select id from UserPO where id = ? order by isDirectory desc, name asc"
	 * 
	 * */
	public String createCountHql(final String selectHql)
	{
		if(isShowLog) System.out.println(getClass().getSimpleName()+"：自动生成HQL_COUNT查询语句 - 原句："+selectHql);
		final String small_sql= selectHql.trim().toLowerCase();
		//加头
		String count_hql= null;
		if(small_sql.startsWith("from ")) 
		{
			count_hql= "select count(*) "+selectHql;
		}
		if(small_sql.startsWith("select ")&&small_sql.contains(" from "))
		{
			int fromIndex= small_sql.indexOf(" from ");
			count_hql= "select count(*) "+selectHql.substring(fromIndex, small_sql.length());
		}
		
		//去尾
		if(count_hql!=null)
		{
			if(small_sql.contains(" order by "))
			{
				int orderbyIndex= count_hql.toLowerCase().indexOf(" order by ");
				count_hql= count_hql.substring(0, orderbyIndex);
			}
			
			
			
			if(isShowLog) System.out.println(getClass().getSimpleName()+"：自动生成HQL_COUNT查询语句 - 新句："+count_hql);
			return count_hql;
		}
		
		if(isShowLog) System.err.println(getClass().getSimpleName()+"：自动生成HQL_COUNT查询语句：null");
		return null;
	}
	
	/**
	 * 
	 * 2017-11-22
	 * @param selectSql 查询语句
	 * select COUNT(*) as _count from dd_user
	 * 
	 * */
	public String createCountSql(String selectSql)
	{


		if(isShowLog) System.out.println(getClass().getSimpleName()+"：自动生成SQL_COUNT查询语句 - 原句："+selectSql);
		final String small_sql= selectSql.trim().toLowerCase();
		//加头
		String count_sql= null;

		if(small_sql.contains(" from "))
		{
			int fromIndex= small_sql.indexOf(" from ");
			count_sql= "select count(*) as _count "+selectSql.substring(fromIndex, small_sql.length());
		}
		
		//删除 "order by:
		if(count_sql!=null)
		{
			if(small_sql.contains(" order by "))
			{
				int orderbyIndex= count_sql.toLowerCase().indexOf(" order by ");
				count_sql= count_sql.substring(0, orderbyIndex);
			}
			
			
			
			if(isShowLog) System.out.println(getClass().getSimpleName()+"：自动生成SQL_COUNT查询语句 - 新句："+count_sql);
			return count_sql;
		}
		
		if(isShowLog) System.err.println(getClass().getSimpleName()+"：自动生成SQL_COUNT查询语句：null");
		return null;
		
	}
	
	
	public int updateByHql(String hql,Map<String, Object> paramMap)
	{
		Session session= getSessionFactory().getCurrentSession();
		Query query= session.createQuery(hql);
		
		if(paramMap!=null)
		{
			String text= "";
			for(String name:paramMap.keySet())
			{
				query.setParameter(name, paramMap.get(name));
				text+= name+":"+paramMap.get(name)+";";
			}
			if(isShowLog) System.out.println(getClass().getSimpleName()+" 参数："+text);
		}
		
		
		int n= query.executeUpdate();		
		
		return n;
		
	}
	
	public int updateBySql(String sql,Map<String, Object> paramMap)
	{
		Session session= getSessionFactory().getCurrentSession();
		SQLQuery sqlQuery= session.createSQLQuery(sql);
		
		if(paramMap!=null)
		{
			String text= "";
			for(String name:paramMap.keySet())
			{
				sqlQuery.setParameter(name, paramMap.get(name));
				text+= name+":"+paramMap.get(name)+";";
			}
			if(isShowLog) System.out.println(getClass().getSimpleName()+" 参数："+text);
		}
		
		int n= sqlQuery.executeUpdate();
		return n;
	}
	
	public List<Map<String, Object>> queryBySql(String sql,Map<String, Object> paramMap)
	{
		return queryBySql(sql,paramMap,null,null);
	}
	
	
	/**
	 * 
	 * 2017-11-21（改为private，避免baseDao提示多余的方法）
	 * 
	 * 
	 * */
	private List<Map<String, Object>> queryBySql(String sql,Map<String, Object> paramMap,Integer PageSize,Integer PageNo)
	{


		Session session= getSessionFactory().getCurrentSession();
		SQLQuery sqlQuery= session.createSQLQuery(sql);
		//sqlQuery.uniqueResult();
		if(paramMap!=null)
		{
			String log_text= "";
			for(String name:paramMap.keySet())
			{
				sqlQuery.setParameter(name, paramMap.get(name));
				log_text+= name+":"+paramMap.get(name)+";";
			}
			if(isShowLog) System.out.println(getClass().getSimpleName()+" 参数："+log_text);
		}
		
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);


		
		/* 分页代码 */
		boolean log_hasPageInfo= false; //测试
		if(PageSize!=null&&PageNo!=null)
		{	
			if(PageNo!=null&&PageNo>=0)
			{				
				sqlQuery.setFirstResult(getPageStartId(PageSize, PageNo));
				sqlQuery.setMaxResults(PageSize);	
				
				log_hasPageInfo= true;
			}
		}
		
		if(isShowLog) System.out.println(getClass().getSimpleName()+": 分页状态-"+log_hasPageInfo);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> mapList= sqlQuery.list();





		return mapList;
	}

	
	
	/**
	 * 2018-05-10
	 * 
	 * 
	 * */
	public Page queryBySqlWithPage(String sql,Map<String, Object> paramMap,Integer PageSize,Integer PageNo)
	{

		if(PageNo==null) PageNo= 0; //
		
		List<Map<String, Object>> list= queryBySql(sql, paramMap, PageSize, PageNo);
		
		String count_sql= createCountSql(sql);
		List<Map<String, Object>> mapList= queryBySql(count_sql, paramMap);
		Integer _count= (Integer)mapList.get(0).get("_count");
		
		
		Page page= new Page();
		page.setList(list);
		
		if(_count!=null)
		{
			int n= _count.intValue();
			int PageCount= 0;
			if(n%PageSize==0) PageCount= n/PageSize;
			else PageCount= n/PageSize+1;	
			page.setDataSize(_count.intValue());
			page.setPageCount(PageCount);				
		}
		
		
		
		return page;
	}
	
	
	/**
	 * 
	 * 2017-11-17
	 * 从SessionFactory通过反射获取数据源
	 * 
	 * 
	 * */
	public static DataSource getDataSource(SessionFactory sessionFactory)
	{
		SessionFactoryImpl sessionFactoryImpl= (SessionFactoryImpl)sessionFactory;
		org.hibernate.engine.jdbc.connections.spi.ConnectionProvider connectionProvider= sessionFactoryImpl.getConnectionProvider();
		if(connectionProvider instanceof DatasourceConnectionProviderImpl)
		{
			DatasourceConnectionProviderImpl datasourceConnectionProviderImpl= (DatasourceConnectionProviderImpl)connectionProvider;
			return datasourceConnectionProviderImpl.getDataSource();
		}
		
		else if(connectionProvider instanceof C3P0ConnectionProvider)
		{
			C3P0ConnectionProvider c3P0ConnectionProvider= (C3P0ConnectionProvider)sessionFactoryImpl.getConnectionProvider();
			/* 通过反射获取私有字段"ds" */
			Field[] arrField= C3P0ConnectionProvider.class.getDeclaredFields();
			for(Field field:arrField)
			{
				field.setAccessible(true);
				if(field.getName().equals("ds"))
				{
					try
					{
						PoolBackedDataSource poolBackedDataSource= (PoolBackedDataSource)field.get(c3P0ConnectionProvider);
						return poolBackedDataSource;
					}
					catch(Exception e){ e.printStackTrace(); }
				}
			}
			
		}
		 
	
		return null;
	}
	

	
	/**
	 * 2017-12-10 Spring手动事务回滚（Spring专属）
	 * 
	 * */
	public void rollback()
	{
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();		
	}
	
	
	/**
	 * 2018-01-07
	 * public <T> T findById(Class<T> clazz,Serializable id)
	 * */
	public <T> List<T> findByIdList(Class<T> clazz,List<Integer> primarykeyList)
	{
		List<T> entityList= new ArrayList<T>();
		for(Integer id:primarykeyList)
		{
			T entity= findById(clazz, id);
			if(entity!=null) entityList.add(entity);
		}
		
		return entityList;
	}
	
	

	
	/**
	 * 
	 * 内部静态类
	 * 
	 * 
	 * */
	public class Page
	{
		/** 本次查询结果 */
		List list; //不带泛型，更容易被转换
		/** 不分页时数据数量 */
		Integer DataSize;
		/** 分页数量 */
		Integer PageCount;
		
		public Page(){}
		
		public Page(Integer pageCount,Integer dataSize, List list) 
		{
			// TODO Auto-generated constructor stub
			this.PageCount= pageCount;
			this.DataSize= dataSize;
			
			this.list= list;
		}
		public Integer getDataSize() {
			return DataSize;
		}
		public void setDataSize(Integer dataSize) {
			DataSize = dataSize;
		}
		
		public Integer getPageCount() {
			return PageCount;
		}
		public void setPageCount(Integer pageCount) {
			PageCount = pageCount;
		}

		public <T> List<T> getList() {
			return list;
		}

		public void setList(List list) {
			this.list = list;
		}
	}

	
	
	
}
