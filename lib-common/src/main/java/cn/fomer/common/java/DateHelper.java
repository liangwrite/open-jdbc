package cn.fomer.common.java;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 2021-12-18
 * 
 */
public class DateHelper {

	static SimpleDateFormat simpleDateFormat= new SimpleDateFormat();
	public static Date getLastYear() {
		return null;
	}
	
	public static String toFormate(String formate) {
		SimpleDateFormat simpleDateFormat= new SimpleDateFormat(formate);
		return simpleDateFormat.format(new Date());
	}
}
