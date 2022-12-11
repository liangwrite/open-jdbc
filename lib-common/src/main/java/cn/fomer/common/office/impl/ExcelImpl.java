package cn.fomer.common.office.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import cn.fomer.common.office.IExcel;
import cn.fomer.common.office.IExcelSheet;
import lombok.Getter;


/**
 * 类说明
 *
 * @author Liang
 * @email liang@163.com
 * @date 2022-11-17
 */
@Getter
public class ExcelImpl implements IExcel {
	//外部
	File file;
	FileInputStream fileInputStream;
	
	//内部
	ExcelReader reader;
	ExcelWriter writer;
	Workbook workbook;
	Sheet[] poiSheet= new Sheet[10];
	IExcelSheet[] iExcelSheet= new IExcelSheet[10];
	public ExcelImpl(String path) {
		//
		this.reader= new ExcelReader(this);
		this.writer= new ExcelWriter(this);
		
		//
		this.file= new File(path);
		
		
		//
		try {
			this.reader.open();
		}
		catch (Exception e) {
			e.printStackTrace();
			this.close();
		}
		
		
	}
	
	public ExcelImpl(MultipartFile file) {
		//
		this.reader= new ExcelReader(this);
		this.writer= new ExcelWriter(this);
		
		//
		this.file = reader.moveToTmp(file);
		
		
		//
		try {
			this.reader.open();
		}
		catch (Exception e) {
			e.printStackTrace();
			this.close();
		}
		
	}
	
	/*
	void init() throws IOException {
		//0
		this.reader= new ExcelReader(this);
		this.writer= new ExcelWriter(this);
		
		//1 打开
		this.reader.open();
	}
	*/
	
	@Override
	public IExcelSheet getSheet() {
		//
		return this.getSheet(0);
	}

	@Override
	public IExcelSheet getSheet(int i) {
		//
		if(this.poiSheet[i]==null) {
			this.poiSheet[i]= this.workbook.getSheetAt(i);
			this.iExcelSheet[i]= new ExcelSheetImpl(this, i);
		}
		return this.iExcelSheet[i];
	}

	/*
	@Override
	public void output(HttpServletResponse response, String fileName) {
		//
		try {
			writer.outputStream(this.file, fileName);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	*/
	
	/*
	@Override
	public void output(HttpServletResponse response) {
		//
		this.output(response, this.file.getName());
	}
	*/
	
	

	@Override
	public List<String[]> read() {
		//
		return reader.read(0);
	}
	
	@Override
	public List<String[]> read(int sheetNo) {
		//
		return reader.read(sheetNo);
	}

	
	void createFileIfNotExist(String path) {
		//
		
		
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub
		try {
			OutputStream outputStream= new FileOutputStream(this.file);
			this.workbook.write(outputStream);
			outputStream.flush();
			outputStream.close();
			this.close();
		}
		catch(Exception e) {
			e.printStackTrace();
			
			this.close();
		}		
	}

	
	@Override
	public File saveTemp() {
		//
		try {
			File destFile= new File(this.reader.getTempDirecotry(), this.file.getName());
			//destFile.createNewFile();
			OutputStream outputStream= new FileOutputStream(destFile);
			this.workbook.write(outputStream);
			outputStream.flush();
			outputStream.close();
			this.close();
			return destFile;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public void close() {
		//
		try {
	        if(this.fileInputStream!=null) {
	        	this.fileInputStream.close();
	        }
	        
	        if(this.workbook!=null) {
	        	this.workbook.close();
	        }
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}



}
