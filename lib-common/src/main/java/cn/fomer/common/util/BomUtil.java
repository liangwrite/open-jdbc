package cn.fomer.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.BOMInputStream;

/**
 * 2020-02-09 移除Bom头（依赖 commons-io）
 *
 */
public class BomUtil {

	
	/**
	 * @param f 文件或文件夹均可
	 * 2020-02-09
	 */
	public static void removeBom(File f) throws Exception {
		// TODO Auto-generated method stub
	
		FileDucer fileDucer= new FileDucer() {
			
			@Override
			public void ifFile(File f) throws IOException {
				// TODO Auto-generated method stub
				removeFile(f);
//				System.out.println(f.getAbsolutePath());
			}
		};
		
		if(f!=null)
			if(f.isFile())
				removeFile(f);
			else 
				listFolder(new File("E:\\develop_Java1\\SpringCloud_Blog"), fileDucer);
	}
	
	public interface FileDucer
	{
		public void ifFile(File f) throws Exception;
	}
	
	
	/**
	 * @param f 必须是文件，可能含有bom
	 * 2020-02-09 需要先读入内存，才能删除
	 */
	private static void removeFile(File f) throws IOException
	{
		//仅适用于UTF8 BOM
		ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream((int)f.length());
		FileUtils.copyFile(f, byteArrayOutputStream);
		
		
		//
		ByteArrayInputStream byteArrayInputStream= new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
		//这个流读出来就自动去掉bom了，但是bomIn.available()仍然是文件大小
		BOMInputStream bomIn = new BOMInputStream(byteArrayInputStream); 
		//
		if(bomIn.hasBOM())
		{
			System.out.println("has bom");
		}

		//如果文件包含bom，那么读取的流已经没有bom，否则不改变。
		//这里覆盖
		FileUtils.copyInputStreamToFile(bomIn, f); 
		bomIn.close(); //
	}
	
	private static void listFolder(File folder,FileDucer fileDucer) throws Exception
	{
		File[] listFiles = folder.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				// TODO Auto-generated method stub
				
				return (pathname.isDirectory()||pathname.getName().endsWith(".java"))?true:false;
			}
		});
		
		for(File f:listFiles)
		{
			if(f.isFile()) fileDucer.ifFile(f);
			else
			{
				listFolder(f, fileDucer);
			}
		}
	}



}
