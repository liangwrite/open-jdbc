package cn.fomer.common.office.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;  

  
/**
 * 2019-05-26 Excel 读取
 * 
 * 
 */
class ExcelReader {
	//
	
	ExcelImpl excel;
	
	ExcelReader(ExcelImpl excel) {
		this.excel= excel;
	}
	
	
	static String[] EMPTY_ROW= new String[] {};
    //
    public static void main(String[] args) {
		//
    	List<String[]> rowList= null;
    	try
    	{
    		//rowList= ExcelReader.Read(new File("G:\\import\\import.xls"));
    		//System.out.println("rowList="+new Gson().toJson(rowList));
    	}
    	catch(Exception e){ e.printStackTrace();}
    	System.out.println("over");
	}
      

    
    
    
    File getTempDirecotry() {
    	String tmp= System.getProperty("java.io.tmpdir");
    	String path= String.format("%s%s", tmp, UUID.randomUUID().toString());
    	File directory= new File(path);
    	directory.mkdir();
    	return directory;
    }
    
    File moveToTmp(MultipartFile file) {
    	File dest= new File(this.getTempDirecotry(), file.getOriginalFilename());
    	try {
    		file.transferTo(dest);
    		return dest;
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
    }
    
    
    
    
    
    void open() throws IOException {
    	//
    	/*
        //0 判断版本
        String fileName = this.excel.file.getName().toLowerCase();
        
        
        int version= 0;
        if(fileName.endsWith(".xls"))
        {  
        	version= 2003;
        }
        else if(fileName.endsWith(".xlsx"))
        {  
        	version= 2007;
        }  
        
        if(version==0)
        {  
            throw new RuntimeException(fileName + "不是excel文件");  
        } 
        

        
        
        //1 打开
        this.excel.fileInputStream= new FileInputStream(this.excel.file);
        if(version==2003)
        { 
        	this.excel.workbook = new HSSFWorkbook(this.excel.fileInputStream);
        }
        else if(version==2007)
        {  
        	this.excel.workbook = new XSSFWorkbook(this.excel.fileInputStream);  
        } 
        */
        
        
        //
        this.excel.workbook= WorkbookFactory.create(this.excel.file);
    }
    
    /**
     * 2019-05-26
     * 
     * 0 如果某行是空行，该行返回[]（而不是null）
     * 1 每个单元格都返回字符串类型（而不是实际类型）
     * 2 如果某列没有输入值返回，该列值为null
     * 
     */
    List<String[]> read(int sheetNo){  
    	//读取Workbook
        try 
        {  
            //0 获取workbook
           
            
            
            //1 遍历sheet（创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回）
            List<String[]> rowList = new ArrayList<String[]>();  
            int maxSheet= this.excel.workbook.getNumberOfSheets(); //共有多少个sheet
            for(int sheetNum = 0;sheetNum == sheetNo; sheetNum++)
            {  
                //
                Sheet sheet = this.excel.workbook.getSheetAt(sheetNum);  
                if(sheet == null) //（空sheet）
                {  
                    continue;  
                }  
                
                //2 遍历行
                int lastRowNo = sheet.getLastRowNum(); //（第几行是最后一行）  
                for(int r = 0; r <= lastRowNo; r++)
                {  
                    //
                    Row row = sheet.getRow(r);  
                    if(row == null)
                    {  
                    	rowList.add(EMPTY_ROW); //（空行） 
                        continue;  
                    }  
                    
                    //3 遍历列
                    short lastCellNum = row.getLastCellNum();
                    String[] cellArray = new String[lastCellNum];  
                    for(int cellNum = 0; cellNum < lastCellNum;cellNum++)
                    {  
                        Cell cell = row.getCell(cellNum);  
                        cellArray[cellNum] = getCellValue(cell);  
                    }  
                    rowList.add(cellArray);  
                }  
            }  

            return rowList;              
        } 
        catch (Exception e) 
        {  
        	e.printStackTrace();
        	
        	return Collections.EMPTY_LIST;
        }
        finally
        {

        	try {
            	//if(this.excel.workbook!=null){ this.excel.workbook.close();}
            	//if( this.excel.fileInputStream!=null){ this.excel.fileInputStream.close();}          		
        	}
        	catch(Exception e) {
        		e.printStackTrace();
        	}
        }
    }  
    
    static String getCellValue(Cell cell)
    {  
        if(cell == null)
        {  
            return null;  
        }  
        
        /*
        //把数字当成String来读，避免出现1读成1.0的情况  
        if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
        {  
            cell.setCellType(Cell.CELL_TYPE_STRING);  
        }  
        //判断数据的类型 
        
        switch (cell.getCellType())
        {  
            case Cell.CELL_TYPE_NUMERIC: //数字  
                cellValue = String.valueOf(cell.getNumericCellValue());  
                break;  
            case Cell.CELL_TYPE_STRING: //字符串  
                cellValue = String.valueOf(cell.getStringCellValue());  
                break;  
            case Cell.CELL_TYPE_BOOLEAN: //Boolean  
                cellValue = String.valueOf(cell.getBooleanCellValue());  
                break;  
            case Cell.CELL_TYPE_FORMULA: //公式  
                cellValue = String.valueOf(cell.getCellFormula());  
                break;  
            case Cell.CELL_TYPE_BLANK: //空值   
                cellValue = "";  
                break;  
            case Cell.CELL_TYPE_ERROR: //故障  
                cellValue = "非法字符";  
                break;  
            default:  
                cellValue = "未知类型";  
                break;  
        }  
        */
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue();  
    }  
}