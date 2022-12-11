package cn.fomer.common.office.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PushbackInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * 2018-03-05 Liang via POI.
 * 2018-04-03 这个缺少读取的功能
 * 
 * 
 * */
public class ExcelWriter {

	ExcelImpl excel;
	
	ExcelWriter(ExcelImpl excel) {
		this.excel= excel;
	}
	
	/**
	 * 
	 * 2020-05-18 保存单sheet
	 * @param start 从哪一行开始写
	 */
	public static File createExcel(List<List<? extends Object>> rowList,File templateFile,int start)
	{
		Map<String, List<List<? extends Object>>> sheetMap= new HashMap<>();
		sheetMap.put("Sheet1", rowList);
		return createExcel(sheetMap,templateFile,start);
	}
	
	/**
	 * 
	 * 2017-10-30 保存多sheet
	 * public static String createExcel(Map<String, List<List<?>>> sheetMap) 
	 * return file real path "C:\Users\Liang\AppData\Local\Temp\7157f13c-4878-40a8-aef8-d12021183fb6.xlsx"
	 * 
	 * @param templateFile 如果是不是null，表示使用模板
	 * @param startIndex 从文件哪一行开始写
	 * 
	 * */
	public static File createExcel(Map<String, List<List<? extends Object>>> sheetMap,File templateFile,int startIndex) 	
	{		
		//判断模板
		File outputFile= randomFile();
		if(templateFile!=null)
		{
			try{
				//FileHelper.Copy(templateFile.getAbsoluteFile().getPath(), outputFile.getAbsoluteFile().getPath());
			}catch(Exception e) { e.printStackTrace();}
		}
		
		//
		XSSFWorkbook workBook= null;
		if(templateFile!=null)
		{
			try
			{
				//编辑
				workBook = new XSSFWorkbook(new FileInputStream(outputFile));
			}
			catch(Exception e) 
			{ 
				e.printStackTrace(); 
				if(workBook!=null) try { workBook.close();}catch(Exception e1) {}				
				
				throw new RuntimeException("打开模板失败！"+e.toString());
				
			}
		}
		else
			workBook= new XSSFWorkbook(); //新建
		
		for(String sheetName:sheetMap.keySet())
		{
			//每个Sheet
			List<List<? extends Object>> dataList= sheetMap.get(sheetName);
			XSSFSheet xssfSheet= null;
			//if(s==0) sheet= book.getSheetAt(0); else
			
			//编辑
			xssfSheet= workBook.getSheet(sheetName);
			
			//新建
			if(xssfSheet==null) {
				xssfSheet= workBook.createSheet(sheetName);
			}
			
			
			//4 列自适应
			//（确定有多少列，每列有多少长度）
			int maxColumnCount= 0;
			for(List<? extends Object> objectList: dataList) {
				if(objectList!=null&&objectList.size()>maxColumnCount) {
					maxColumnCount= objectList.size();
				}
			}
			for (int col = 0; col < maxColumnCount; col++) {
				int maxLength = xssfSheet.getDefaultColumnWidth();
				for (List<? extends Object> objectList : dataList) {
					if (objectList != null && objectList.size() > col) {
						Object colValue= objectList.get(col);
						if(colValue!=null) {
							if(colValue.toString().length()>maxLength) {
								maxLength= colValue.toString().length();
							}
						}

					}
				}
				
				xssfSheet.setColumnWidth(col, maxLength*2*260);
				System.out.println(String.format("cols=%s, width=%s", Integer.toString(col), Integer.toString(maxLength)));
			}
			
				
			//
			int dataIndex=-1;
			for(int rowIndex=0;dataIndex<dataList.size();rowIndex++)
			{
				//跳过空行
				if(rowIndex<startIndex) continue;
				
				//
				dataIndex++;
				if(dataIndex>=dataList.size()) break;
				
				//填充一行
				XSSFRow xssfRow = xssfSheet.getRow(rowIndex); //编辑行
				if(xssfRow==null)
					xssfRow = xssfSheet.createRow(rowIndex); //新建行
					
				List<? extends Object> rowData= dataList.get(dataIndex);
				//这一行传null，表示不写数据，跳过该行
				if(rowData==null) continue;
				
				for(int column=0;column<rowData.size();column++)
				{
					Object value= rowData.get(column);
					XSSFCell cell= xssfRow.getCell(column); //编辑
					if(cell==null)
						cell= xssfRow.createCell(column); //新建
						
					cell.setCellValue(value==null?"":value.toString()); //转String
				}				
				
			}		
			
		}
		
		try
		{
			FileOutputStream fileOutputStream= new FileOutputStream(outputFile);
			workBook.write(fileOutputStream);
			
			fileOutputStream.flush();
			fileOutputStream.close();
			return outputFile;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException("生成时发生异常"+e.getMessage());
		}
		
		
	}
	
	
	/**
	 * 一个临时文件（不会重复）
	 * 2020-05-18
	 */
	private static File randomFile()
	{
		
		String tmpPath= System.getProperty("java.io.tmpdir");
		File parent= new File(tmpPath);
		File excelFile= new File(parent, UUID.randomUUID().toString());
		System.out.println("ExcelWriter.randomFile() "+excelFile.getAbsolutePath());
		return excelFile;
	}
	
	void test() {
		//
		PushbackInputStream m;
	}
	
}
