package cn.fomer.common.io;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import cn.fomer.common.util.BomUtil;



/**
 * 2018-12-11
 * 
 * 
 */
public class FileHelper {
	
	/**
	 * 202207 递归查询子文件
	 */
	public static List<File> iteratorFiles(String path){
		return null;
	}
	/**
	 * 202207 递归查询子文件
	 */
	public static List<File> iteratorFiles(String path, FileFilter filter){
		List<File> fileList= new ArrayList<>();
		File root= new File(path);
		if(root.isDirectory()) {
			iteratorFiles(root, filter, fileList);
		}
		return fileList;
	}
	
	static void iteratorFiles(File file, FileFilter filter, List<File> fileList){
		for(File child: file.listFiles(filter)) {
			if(child.isFile()) {
				fileList.add(child);
			}
			else if(child.isDirectory()) {
				iteratorFiles(child, filter, fileList);
			}
		}
		
	}
	
	
	
	/**
	 * 2021-10-29 清除空行
	 */ 
	public static void cleanEmptyLine(String src, String to) {

		BufferedReader bufferedReader;
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream("E:\\Users\\Liang\\Documents\\Files\\兴农惠民\\工作\\草稿3.txt"), StandardCharsets.UTF_8);
			bufferedReader = new BufferedReader(inputStreamReader);
			String line = bufferedReader.readLine();
			
			FileOutputStream fileOutputStream= new FileOutputStream("E:\\Users\\Liang\\Documents\\Files\\兴农惠民\\工作\\草稿3.out.txt");
			while ((line = bufferedReader.readLine()) != null) {
				if(StringUtils.isEmpty(line)) continue;
				
				fileOutputStream.write((line+"\r\n").getBytes());
				System.out.println(line);
			}
			// 不需要fileOutputStream.close();
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean Copy(String src,String tar)
	{
		File srcFile= new File(src);
		File tarFile= new File(tar);
		if(srcFile.exists())
		{
			FileInputStream fileInputStream= null;
			FileOutputStream fileOutputStream= null;
			try
			{
				fileInputStream= new FileInputStream(srcFile);
				
				File targetFolder= tarFile.getParentFile();
				if(!targetFolder.exists()) {
					targetFolder.mkdirs();
				}
				fileOutputStream= new FileOutputStream(tarFile);
				byte[] buffer= new byte[1*1024*1024];
				int len= 0;
				while((len=fileInputStream.read(buffer))!=-1)
				{
					fileOutputStream.write(buffer, 0, len);					
				}
				fileInputStream.close();
				fileOutputStream.flush();
				fileOutputStream.close();
				return true;
			}
			catch(Exception e) 
			{
				e.printStackTrace();
				if(fileInputStream!=null)
				{
					try{ fileInputStream.close();}catch(Exception ex){}
				}
				if(fileOutputStream!=null)
				{
					try{ fileOutputStream.close();}catch(Exception ex){}
				}
			}
		}
		return false;
	
	}
	
	/**
	 * 2020-12-25
	 */
	public static Map<String, String> folder(File file)
	{
		Map<String, String> map= new HashMap();		
		if(file.isDirectory())
		{
			File[] listFiles = file.listFiles(f->f.isFile());
			for(File f:listFiles)
			{
				String s = readString(f);
				map.put(f.getName(), s);
			}
;
		}
		return map;
	}
	
	
	public static byte[] readByte(File file)
	{
		
		
		int buffer_size= 1*100*1024;
		//if(filePath!=null)
		{
			byte[] data= null;
			if(file.exists()&&file.isFile())
			{
				FileInputStream fileInputStream= null;
				ByteArrayOutputStream byteArrayOutputStream= null;
				try
				{
					fileInputStream= new FileInputStream(file);	
					byteArrayOutputStream= new ByteArrayOutputStream(buffer_size);
					byte[] buffer= new byte[buffer_size];
					int len= 0;
					while((len=fileInputStream.read(buffer))!=-1)
					{
						byteArrayOutputStream.write(buffer,0,len);					
					}
					
					fileInputStream.close();
					data= byteArrayOutputStream.toByteArray();
					byteArrayOutputStream.close();
					
					return data;
					
				}
				catch(Exception e)
				{
					e.printStackTrace();
					if(fileInputStream!=null)
						try{ fileInputStream.close();}catch(Exception ex){}
					if(byteArrayOutputStream!=null)
						try{ byteArrayOutputStream.close();}catch(Exception ex){}
					
					throw new RuntimeException(e.toString());
				}
			}
			else
			{
				throw new RuntimeException("The file not found or file is a folder.");
			}
			
		}
	}

	/**
	 * 2018-12-11
	 * 
	 */
	public static String readString(File file)
	{

		byte[] data= readByte(file);
		

		try
		{
			String text= new String(data,"utf-8"); //throws IOException
			return text;			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
			throw new RuntimeException(e.toString());
		}
	}

	/**
	 * 2019-04-11 复制Blog时，会带烦人的行号
	 * @throws IOException 
	 * 
	 */
	public String removeHeader(File file) throws IOException
	{
		//
		String text= readString(file);
		BufferedReader bufferedReader= new BufferedReader(null);
		bufferedReader.readLine();
		
		return null;
	}
	
	public static boolean write(String text,File toFile)
	{
		if(text==null) return true;
		
		try
		{
			FileOutputStream fileOutputStream= new FileOutputStream(toFile);
			fileOutputStream.write(text.getBytes("utf-8"));
			fileOutputStream.close();
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
			return false;
		}
	}
	
	public interface FileDucer
	{
		public void ifFile(File f) throws Exception;
	}
	
	
	/**
	 * 
	 * 2020-02-09
	 * @param folder 必须是文件夹
	 * 
	 */
	public static void listFolder(File folder,FileDucer fileDucer) throws Exception
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

	
	/**
	 * 2020-02-09
	 */
	public static void removeBom(File f)
	{
		try
		{
			BomUtil.removeBom(f);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 随机创建一个文件
	 * @date 2022-02-16
	 */
	public static File random() {
		String tempFilePath= System.getProperty("java.io.tmpdir")+UUID.randomUUID().toString(); //"C:\Users\ADMINI~1\AppData\Local\Temp\"
		return new File(tempFilePath);
	}
	
}
