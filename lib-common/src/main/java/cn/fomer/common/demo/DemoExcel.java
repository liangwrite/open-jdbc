package cn.fomer.common.demo;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import cn.fomer.common.office.IExcel;
import cn.fomer.common.office.impl.ExcelImpl;
import cn.fomer.common.util.PinyinHelper;

/**
 * 类说明
 *
 * @author Liang
 * @email liang@163.com
 * @date 2022-11-23
 */
public class DemoExcel {

	static final String TEMPLATE= "@ApiModelProperty(\"%s\")\r\nString %s;";
	public static void main(String[] args) throws IOException {
		//
		Set<String> columnSet= new HashSet<>();
		IExcel excel =new ExcelImpl("G:\\test\\wellcomplete.xls");
		List<String[]> rowList = excel.read();
		
		for(int c=0;c<200;c++) {
			if(rowList.get(4).length-1<c) continue;
			String columnChinese= String.format("%s", getTop(rowList, c, 4).replace("\n", ""));
			
			
			String columnComment= format(columnChinese);
			String english= PinyinHelper.getAllFirstLetter(columnComment);

			String columnCode= calc(columnSet, english);

			
			System.out.println();
			System.out.println(String.format(TEMPLATE, columnComment, columnCode));
			
			
			columnSet.add(columnCode);
			excel.getSheet().write(5, c, columnCode);
		}
		System.out.println(excel.saveTemp().getCanonicalPath());
		excel.close();
	}
	
	static String calc(Set<String> set, String key) {
		if(!set.contains(key)) {
			return key;
		}
		else {
			return calc(set, key+"1");
		}
	}
	
	
	static String format(String s) {
		int small= -1;
		if((small=s.indexOf("("))>-1) {
			return s.substring(0, small);
		}
		
		return s;
	}
	
	
	static String getTop(List<String[]> rowList, int c, int top) {
		String s= rowList.get(top)[c];
		if(StringUtils.isEmpty(s)) {
			return getTop(rowList, c, top-1);
		}
		
		String parent= "";
		if(top==4 && StringUtils.isNotEmpty((parent=rowList.get(top-1)[c]))) {
			//return parent+s;
		}
		return s;
	}
}
