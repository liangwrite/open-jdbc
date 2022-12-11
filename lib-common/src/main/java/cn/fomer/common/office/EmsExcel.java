package cn.fomer.common.office;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.core.io.ClassPathResource;

import cn.fomer.common.http.ResponseHelper;
import cn.fomer.common.office.impl.ExcelWriter;
import cn.fomer.common.springmvc.SpringHelper;


/**
 * 2020-05-18 excel导出
 *
 */
public class EmsExcel<T> {
	
	
	public static final int current = 1;
	public static final int size = Integer.MAX_VALUE;
	
	List<String> headerList= new ArrayList<>();
	ExcelConverter<T> converter;
	
	List<T> voList;
	boolean isTemplate;
	int start=0;
	File templateFile;
	private EmsExcel(List<T> dataList)
	{
		this.voList= dataList;
	}
	
	public static <T> EmsExcel<T> newInstance(List<T> dataList)
	{
		return new EmsExcel<>(dataList);
	}
	public EmsExcel<T> useTemplate(String filepath)
	{
		isTemplate= true;
		try
		{
			templateFile= new ClassPathResource(filepath).getFile();
						
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException("找不到该模板："+filepath);
		}
		if(!templateFile.exists()) throw new RuntimeException("找不到该模板："+filepath);
		
		return this;
	}
	public EmsExcel<T> row(ExcelConverter<T> c)
	{
		this.converter= c;
		return this;
	}
	public EmsExcel<T> headers(String ...headers)
	{
		if(headers.length>0) headerList= Arrays.asList(headers);
		return this;
	}
	public EmsExcel<T> startRow(int n)
	{
		if(n>0) start= n;
		return this;
	}
	
	public static class ExcelRow
	{
		private List<Object> objectList= new ArrayList<>();
		public List<Object> getData()
		{
			return objectList;
		}
		public ExcelRow column(Object ...o)
		{
			//for(Object )
			objectList = Arrays.asList(o);
			return this;
			
		}
	}

	
	/**
	 * 
	 * 2020-05-18
	 * @param list 查询的信息
	 * @param filename 比如"用户"，后面不要加后缀.xlsx
	 * @param converter 把一个对象转换为excel的一行，次序代表显示顺序
	 * 
	 */
	public void export(String filename)
	{
		//
		List<List<? extends Object>> rowList= new ArrayList<>();
		
		
		if(!isTemplate)
			if(headerList!=null) rowList.add(headerList);
		
		for(int i=0;i<voList.size();i++)
		{
			T t= voList.get(i);
			try
			{
				//（void convert(T vo, int i, ExcelRow row);）
				List<String[]> rowData = converter.convert(t,i);
				rowList.add(rowData);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				
				throw new RuntimeException("对象转换失败！"+e.toString());
			}
			
		}
		File excel = ExcelWriter.createExcel(rowList,isTemplate?templateFile:null,start);
		
		try
		{
			//ExcelWriter.outputStream(excel, filename);
			//ExcelWriter.outputStream(excel, filename);
			ResponseHelper.writeFile(SpringHelper.getResponse(), excel, filename);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException("写入流失败！");
		}
	}
	
	
}
