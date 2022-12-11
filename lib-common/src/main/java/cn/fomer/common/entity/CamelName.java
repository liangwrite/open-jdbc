package cn.fomer.common.entity;

/**
 * 2021-08-19 驼峰命名
 * 
 */
public class CamelName {


	
	/**
	 * 会自动去掉前缀
	 */
	public static String toCamelForClass(String columnName) {
		String name= columnName;
		if(columnName.contains("_")) {
			name= columnName.substring(columnName.indexOf("_")+1);
		}
		return toCamel(name, true);
	}
	/**
	 * 会自动去掉前缀
	 */
	public static String toCamelForController(String columnName) {
		String name= columnName;
		if(columnName.contains("_")) {
			name= columnName.substring(columnName.indexOf("_")+1);
		}
		return toCamel(name, false);
	}
	public static String toCamelForField(String columnName) {
		return toCamel(columnName, false);
	}
	
	static String toCamel(String columnName, boolean shouldFirstLetterUpper)
	{
		String data= "";
		
		
		//
		String[] partList = columnName.toLowerCase().split("_");
		
		boolean isFirstPart= true;
		for(String part: partList)
		{
			boolean isFirstChar= true;
			for(char c: part.toLowerCase().toCharArray())
			{
				Character newC= c;
				
				//1 第一段 全部小写
				if(isFirstPart)
				{
					//（条件生效）
					if(shouldFirstLetterUpper&&isFirstChar) {
						newC= Character.toUpperCase(c);
					}
					
				}
				else {
					//2 非第一段 首字母大写，其余小写
					if(isFirstChar)
					{
						newC= Character.toUpperCase(c);
					}
				}
				
				

				
				
				
				//
				data+= newC;
				
				
				//
				isFirstChar= false;
			}
			
			isFirstPart= false;
		}
		
		
		
		return data;
	}	
}
