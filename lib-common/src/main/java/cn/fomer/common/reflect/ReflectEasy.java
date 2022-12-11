package cn.fomer.common.reflect;

/**
 * 2019-04-18 用于方便的取属性
 * 
 * 
 */
public class ReflectEasy {

	//
	Object srcObj;
	
	private ReflectEasy(Object o)
	{
		if(o==null)
		{
			//throw new RuntimeException(NullPointerException.class.getName());
		}
		this.srcObj= o;
	}
	
	
	public static ReflectEasy newInstance(Object o)
	{
		return new ReflectEasy(o);
	}
	//
	/**
	 * 2019-04-18 获取属性
	 * 
	 */
	public ReflectEasy getProperty(String property)
	{
		//
		Object propertyObj= ReflectBase.getFieldValue(srcObj, property);
		
		ReflectEasy newReflectObject= ReflectEasy.newInstance(propertyObj);
		return newReflectObject;
	}
	
	/**
	 * 2019-04-18 获取实际值
	 * 
	 */
	@SuppressWarnings("unchecked")
	public <T> T getValue()
	{
		return (T)srcObj;
	}
}
