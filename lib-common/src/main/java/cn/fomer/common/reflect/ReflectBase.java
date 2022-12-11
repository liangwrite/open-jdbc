package cn.fomer.common.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * 2018-01-05 最基础的
 * 
 * 
 */
public class ReflectBase 
{	
	protected Class<?> c;	
	
	public ReflectBase(Class<?> c) { this.c= c;}
	
	/**
	 * 2018-01-05
	 * 
	 * */
	public static Object getFieldValue(Object srcObj,String fieldName)
	{
		if(srcObj!=null&&(fieldName!=null&&fieldName.length()>0))
		{
			
			for(Field field:srcObj.getClass().getDeclaredFields())
			{
				//需要的字段
				if(fieldName.equals(field.getName()))
				{
					//非公共字段
					if(!Modifier.isPublic(field.getModifiers()))
					{						
						field.setAccessible(true);
					}
					
					Object value= null;
					try
					{
						value= field.get(srcObj);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					return value;
				}
				
			}
		}
		return null;
	}
	
	public Object getFieldValue(String fieldName, Object srcObj)
	{		
		Field[] arrField= c.getDeclaredFields();
		for(Field field:arrField)
		{
			field.setAccessible(true); /* 避免private触发异常 */
			if(field.getName().equals(fieldName))
			{
				try{ return field.get(srcObj);}catch (Exception e) {e.printStackTrace();}
				return null;
			}
		}
		return null;
	}
	
	public List<String> getInterfaceInfo()
	{		
		List<String> listInterface= new ArrayList<String>();
		Class<?>[] arrClass= c.getInterfaces();
		
		for(Class<?> c:arrClass)
		{
			listInterface.add(c.getName());
		}
		return listInterface;		
	}
	
		
	
	public List<String> getFieldInfo()
	{
		List<String> listField= new ArrayList<String>();
		Field[] arrField= c.getDeclaredFields();
		for(Field field:arrField)
		{
			String modifier= Modifier.toString(field.getModifiers());
			String type= field.getType().getName();
			String name= field.getName();
			listField.add(modifier+" "+type+" "+name);
		}
		return listField;
	}
	
	public List<String> getConstructorInfo()
	{
		List<String> listConstructor= new ArrayList<String>();
		Constructor<?>[] arrConstructor= c.getDeclaredConstructors(); /* 构造函数 */
		for(Constructor<?> constructor:arrConstructor)
		{
			String text= "";
			Class<?>[] arrParam= constructor.getParameterTypes();
			String modifier= Modifier.toString(constructor.getModifiers());
			text+=modifier+" "+c.getSimpleName()+"(";
			for(Class<?> pClass:arrParam)
			{
				String type= pClass.getName();
				text+= type+", ";
			}
			if(text.endsWith(", ")) text= text.substring(0, text.length()-2);
			text+=")";
			listConstructor.add(text);
		}
		return listConstructor;
	}
	
	public List<String> getMethodInfo()
	{
		List<String> listMethod= new ArrayList<String>();
		Method[] arrMethod= c.getDeclaredMethods(); //c.getMethods();		
		
		for(Method method:arrMethod)
		{			
			String text= "";
			String modifier= Modifier.toString(method.getModifiers());
			Class<?>[] arrParam= method.getParameterTypes();
			String returnType= method.getReturnType().toString();
			text+=modifier+" "+returnType+" "+method.getName()+"(";
			for(Class<?> pClass:arrParam)
			{
				String type= pClass.getName();
				text+= type+", ";
			}
			if(text.endsWith(", ")) text= text.substring(0, text.length()-2);
			text+=")";
			listMethod.add(text);
		}
		return listMethod;		
	}
	
	public String getReport()
	{
		List<String> listInterface= getInterfaceInfo();
		List<String> listField= getFieldInfo();
		List<String> listConstructor= getConstructorInfo();
		List<String> listMethod= getMethodInfo();		
		
		String modifier= Modifier.toString(c.getModifiers());		
		String text= "/* "+c.getName()+" */\r\n";
		text+= modifier+(c.isInterface()?" interface ":" class ")+c.getSimpleName();		
		
		if(c.getSuperclass()!=Object.class&&c.getSuperclass()!=null) /* if c.isInterface() c.getSuperclass() will be null  */
		{
			text+= " extends "+c.getSuperclass().getName();			
		}
		
		if(listInterface.size()>0) 
		{
			text+= " implements";
			for(String ia:listInterface)
			{
				text+=" "+ ia+",";
			}
			text= text.substring(0, text.length()-1);
		}
		text+= "\r\n";
		text+= "{\r\n";
		//text+= "Field:";
		//text+= "\r\n";
		for(String s:listField)
		{
			text+= "\t"+s+"\r\n";		
		}
		//text+= "Constructor:";
		text+= "\r\n";
		for(String s:listConstructor)
		{
			text+= "\t"+s+"\r\n";		
		}
		//text+= "Method:";
		text+= "\r\n";
		for(String s:listMethod)
		{
			text+= "\t"+s+"\r\n";
		}
		text+="}";
		return text;		
	}
	
	public String getHtmlReport()
	{ 
		String tabStr= "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
		return getReport().replace("\r\n", "<br />").replace("\t", tabStr); 
	}
	
	
	/**
	 * 2017-12-31
	 * 
	 * */
	public static boolean hasField(Class<?> clazz,String name)
	{
		
		
		if(clazz!=null&&name!=null)
		{
			List<String> fieldList= getFieldList(clazz);
			name= name.toLowerCase();
			for(String fieldName:fieldList)
			{
				if(name.equals(fieldName.toLowerCase())) return true;
			}			
		}		
		return false;
	}
	
	
	/**
	 * 2017-12-31
	 * 
	 * */
	public static List<String> getFieldList(Class<?> clazz)
	{
		
		ArrayList<String> list= new ArrayList<String>();
		
		if(clazz!=null)
		{
			Field[] arr= clazz.getDeclaredFields();			
			
			for(Field field:arr)
			{
				list.add(field.getName());
			}			
		}
		
		return list;
	}
	
	/**
	 * 2017-12-31
	 * 获取某public字段。
	 * 
	 * 
	 * */
	public static Field getField(Class<?> clazz,String name)
	{
		if(clazz!=null&&name!=null)
		{
			Field field;
			try
			{
				field= clazz.getField(name);
				return field;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	/** 
	 * 2017-12-31
	 * 设置public字段
	 * 
	 * 
	 * */
	public static void setFieldValue(Object obj,String fieldName,Object value)
	{
		if(obj!=null&&fieldName!=null)
		{			
			Field field= getField(obj.getClass(), fieldName);
			if(field!=null)
			{	
				try
				{
					field.set(obj, value);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}


}
