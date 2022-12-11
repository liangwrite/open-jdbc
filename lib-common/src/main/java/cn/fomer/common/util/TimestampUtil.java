package cn.fomer.common.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimestampUtil 
{
	/* 应该从高匹配到低匹配反着来 */
	private static String[] arrStandardFormat= new String[]
	{
		"yyyy-MM-dd HH:mm:ss.SSS","yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm","yyyy-MM-dd"
	};

	public static Timestamp parse(String text)
	{
		System.out.println("TimestampUtil.parse():text:"+text);
		for(int i=0;i<arrStandardFormat.length;i++)
		{
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(arrStandardFormat[i]);  //年一定为小写 yyyy
			try
			{
				Date date= simpleDateFormat.parse(text);	
				Timestamp toTime= new Timestamp(date.getTime());				
				return toTime;
			}
			catch(Exception parseEx)
			{ 				
				continue;				
			}
		}
		System.err.println("TimestampUtil.parse() Failed:"+text);
		return null;		
	}

}
