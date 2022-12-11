package cn.fomer.common.office;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

/**
 * excel
 *
 * @author Liang
 * @email liang@163.com
 * @date 2022-11-17
 */
public interface IExcel {
	

	IExcelSheet getSheet();
	IExcelSheet getSheet(int i);
	
	//void output(HttpServletResponse response);
	//void output(HttpServletResponse response, File file);
	//void output(HttpServletResponse response, String fileName);
	
	
    /**
     * 2022-02
     * 
     * 1.如果某行没有任何输入，该行返回null
     * 2.如果某列没有输入值返回，该列值为null
     * 
     */
	List<String[]> read();
	List<String[]> read(int sheetNo);
	
	void close();
	void save();
	
	
	/**
	 * 保存到一个临时文件
	 */
	File saveTemp();
}
