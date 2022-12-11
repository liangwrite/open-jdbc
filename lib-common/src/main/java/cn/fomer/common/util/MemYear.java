package cn.fomer.common.util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;

/**
 * 
 * 1.每年12个月0-11、
 * 2.每月（最多横跨）6个星期1-6、7天1-7 
 * 3.每星期按照西方习惯顺序是：星期天、星期一、...、星期六
 * 
 * 
 * */
public class MemYear 
{
	private static Map<Integer, MemYear> containerMap= new HashMap<Integer, MemYear>();
	synchronized public static MemYear getInstance(int year)
	{
		if(containerMap.containsKey(year))
		{
			return containerMap.get(year);		
		}
		
		MemYear newYear= new MemYear(year);
		containerMap.put(year, newYear);
		
		return newYear;
	}
	
	private Integer year;
	
	/**
	 * 内存中的日历
	 * 12个月
	 * 6个星期
	 * 7天
	 * 
	 * */
	private Integer[][][] arr= new Integer[12][6][7]; 
	
	public Integer[][][] getData()
	{ 
		return arr;
	}
	
	/** 禁止外部调用 */
	private MemYear(int year)
	{
		this.year= year;
		
		/* 2017-01-01 0:0:0.000 */		
		/* 月份从0开始 */
		for(int m=0;m<12;m++)
		{
			int day=1; /* 日期从1开始 */			
			Calendar first_day_per_month= Calendar.getInstance(); //每个月的第一天			
			first_day_per_month.set(year, m, 1, 0, 0, 0); //"2017-01-01 08:00:00.0"
			first_day_per_month.set(Calendar.MILLISECOND, 0); //去毫秒
			
			int maxDays_of_month= first_day_per_month.getActualMaximum(Calendar.DAY_OF_MONTH); /* 当月一共有多少天 */
			boolean isLastDayOfMonth= false; //指示当月是否已经遍历完
			
			for(int week=1;week<=6;week++) /* 日历最多显示6行 */
			{					
				int weekday=1; //每星期一般从1开始[1,7]
				if(week==1) weekday= first_day_per_month.get(Calendar.DAY_OF_WEEK);//但是第一周不遵守这个规律				
				for(;weekday<=7;weekday++)
				{
					arr[m][week-1][weekday-1]= day;
					day++;
					if(day>maxDays_of_month) { isLastDayOfMonth= true; break;}
				}
				if(isLastDayOfMonth) break; //如果已经月末了，
			}			
		}
		
	}
	/** 用途：获取某月第几周第几天 是几号（如果没有返回？） */
	public Integer getDay(int month, int week, int weekday) /* you should use—— m:[1,12]; week:[1,6]; weekday:[1,7] */
	{ 		
		return arr[month][week-1][weekday-1];
	}
	
	/** 用途：获取某月第几周第几天的日期 */
	public Date getDate(int month, int week, int weekday) /* you should use—— m:[1,12]; week:[1,6]; weekday:[1,7] */
	{ 
		Integer day= getDay(month, week, weekday);
		return new Date(year,month,day);
	}	
	
}

