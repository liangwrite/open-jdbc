package cn.fomer.jdbc.service;

import java.io.File;

/**
 * 2021-12-10
 * 
 */
public interface SummaryService {

	/**
	 * 2021-12-10
	 */
	void toHtmlFile(int top, String file);
	
	String toHtml(int top);
	String toHtml();
	
	/**/
	//public String summaryToHtml(int top,boolean css);
	
	String toText(int top);
	String toText();	
}
