/**
 * 
 */
package cn.fomer.common.io;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 字符串工具
 *
 * @author Liang
 * @email liang@163.com
 * @date 2022-07-14
 */
public class StringHelper {

	/**
	 * 说明 逐行
	 * @throws IOException 
	 * @date 2022-07
	 */
	public static List<String> readLine(String text) throws IOException{
		//0
		ByteArrayInputStream byteArrayInputStream= new ByteArrayInputStream(text.getBytes());
		BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(byteArrayInputStream));
		
		//1
		String line= null;
		List<String> lineList= new ArrayList<>();
		while((line=bufferedReader.readLine())!=null) {
			lineList.add(line);
		}
		
		//2
		byteArrayInputStream.close();
		
		return lineList;
	}
}
