package cn.fomer.common.smb;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jcifs.smb.SmbFile;

/**
 * 
 * 
 * 2017-11-17
 * 访问共享文件夹
 * url= "smb://Liang:0@192.168.0.200/f/";
 * 
 * 
 * 
 * */
public class SmbUtil {
	StringBuffer logBuffer= new StringBuffer();
	String rootUrl; 
	public SmbVO currentSmbPO= new SmbVO(); //遍历位置

	
	
	public int fileCount=0,directoryCount=0;
	public List<SmbVO> fileList= new ArrayList<SmbVO>();
	public List<SmbVO> directoryList= new ArrayList<SmbVO>();
	public List<SmbVO> errorList= new ArrayList<SmbVO>();

	boolean test;
	/** 递归遍历的结束开关 */
	private boolean isStoping= false;
	public SmbUtil(String url)
	{
		this.rootUrl= url;
	}
	
	public boolean tryConnect()
	{
		try
		{
			//"smb://Liang:0@192.168.0.200/f/"
			SmbFile smbFile= new SmbFile(rootUrl);
			smbFile.connect(); //IOException
			return true;
		}
		catch(Exception e){ }
		return false;
	}
	
	
	/**
	 * 
	 * 遍历所有文件。会重置文件总数
	 * 
	 * */
	public void searchAll(int depth,IScanListener listener)
	{
		isStoping= false; //初始化开关
		
		if(tryConnect())
		{
			
			directoryList.clear();
			fileList.clear();
			errorList.clear();
						
			try
			{
				SmbFile rootSmbFile= new SmbFile(rootUrl);
				SmbVO rootSmpPO= SmbVO.Create(rootSmbFile);
				rootSmpPO.id= 0; //数据库Id
				rootSmpPO.depth= 0; //根目录
				rootSmpPO.parentId= 0; //自己的父路径还是自己
				
				directoryList.add(rootSmpPO);
				++directoryCount;
				
				logBuffer.setLength(0); //log
				fileCount=0;directoryCount=0;
				
				eachSmbFile(rootSmpPO,depth,new IntegerObject(0),listener);
				
				
				
			}
			catch(Exception e)
			{
				System.out.println("扫描异常结束");
				e.printStackTrace();
			}
			
		}
	}
	
	
	
	/**
	 * 
	 * 2017-11-19 递归遍历文件夹
	 * 1 递归遍历
	 * 2 给PO设置主键
	 * 3 设置父子关系
	 * @param depth 可选。表示当前层级是第几层，首次调用建议给0。（ 0-n：0表示你给的路径是个文件。1：表示你给的路径下的文件或文件夹，依次类推***）
	 * @param stopDepth 用于浅搜索。一旦搜索到stopDepth层后，就停止。（比如 stopDepth=1时，只搜索给定文件夹下的这层，不再递归 ）
	 * @param index 数据库主键索引。从1开始
	 * @param IListen 
	 * 
	 * */	
	private void eachSmbFile(SmbVO parentSmbPO,int stopDepth,IntegerObject index,IScanListener scanListener)
	{
		currentSmbPO= parentSmbPO; //报告当前扫描路径信息
		//System.out.println(fatherSmbPO.getPath());
		if(isStoping)
		{
			return; //不要重置开关，等再次调用searchAll（）时
		}
		
		if(parentSmbPO.depth==null) parentSmbPO.depth= 0; //一般给定的路径为0层，子层为1层...
		if(index==null) index= new IntegerObject(0); //传递0，表示记录从1开始
		if(parentSmbPO.depth>stopDepth) return;
		

		scanListener.onNextSearch(directoryList,fileList);
		
		try
		{
			if(parentSmbPO.smbFile.isFile()) 
			{
				//fileList.add(fatherSmbPO);				
				return;
			}
			
			
			else if(parentSmbPO.getIsDirectory())
			{
				SmbFile[] childrenSmbFileArray= null;
				try
				{
					childrenSmbFileArray= parentSmbPO.smbFile.listFiles(); //错误：1.directory must end with '/'; 2.Access is denied.; 3.Invalid operation for IPC service					
				}
				catch(Exception e)
				{
					e.printStackTrace();
					System.err.println(getClass().getName()+":读取文件列表失败 "+parentSmbPO.getUrl().toString());
					errorList.add(parentSmbPO);		
				}
				
				if(childrenSmbFileArray==null) return;

		
				//遍历此文件夹下面的文件及子文件
				for(SmbFile childSmbFile:childrenSmbFileArray)
				{
					if(isStoping)
					{
						//不要重置开关，等再次调用searchAll（）时
						//为了最快结束扫描，第二个结束点。
						break; 
					}
					
					SmbVO childPO= SmbVO.Create(childSmbFile);
					
					childPO.depth= parentSmbPO.depth+1;
					childPO.id= index.increase().getInt();
					
					childPO.parentId= parentSmbPO.id;

					Boolean isFile= null;
					try
					{ isFile= childSmbFile.isFile();}
					catch(Exception e)
					{
						e.printStackTrace();
						System.out.println("无法判断是否是文件");
					}
					
					Boolean isDirectory= null;
					try
					{ isDirectory= childSmbFile.isDirectory();}
					catch(Exception e)
					{
						e.printStackTrace();
						System.out.println("无法判断是否是文件夹");
					}
					
					

					if(isFile) 
					{
						
						fileList.add(childPO);	
						++fileCount;
						continue;
					}
					else if(isDirectory)
					{
						
						directoryList.add(childPO);
						eachSmbFile(childPO,stopDepth,index,scanListener);
						++directoryCount;
						continue;
					}		
					else
					{
						System.out.println("发现一个既不是文件夹也不是文件的类型");
						continue;
					}
					
				}	


				
			}
			
		}
		catch(Exception e)
		{
			System.out.println("获取属性时异常：");
			errorList.add(parentSmbPO);
			e.printStackTrace();
		}
	
	}
	
	boolean hasRepeat(SmbVO newPO)
	{
		List<SmbVO> list= getListAll();
		for(SmbVO tempPO:list)
		{
			//if(tempPO.id==newPO.id)
			if(tempPO.id!=null&&newPO.id!=null&&(tempPO.id.intValue()==newPO.id.intValue()))
			{
				System.out.println("发现重复项："+"");
				return true;
			}
		}
		return false;
	}
	
	
	public List<SmbVO> getListAll()
	{
		List<SmbVO> list= new ArrayList<SmbVO>();
		for(SmbVO smbPO:directoryList)
		{
			list.add(smbPO);
		}
		for(SmbVO smbPO:fileList)
		{
			list.add(smbPO);
		}
		return list;
	}
	
	/**
	 * 
	 * 
	 * */
	public void clean()
	{
		directoryList.clear(); //不影响统计总数
		fileList.clear();
	}
	
	public void stop()
	{
		isStoping= true;
	}
	
	public String getUrl()
	{
		return this.rootUrl;
	}
	
	
	public interface IScanListener
	{		
		public void onNextSearch(List<SmbVO> directoryList,List<SmbVO> fileList);		
	}
	
	class IntegerObject
	{
		int n=0;
		public IntegerObject(int n)
		{
			this.n= n;
		}
		public IntegerObject increase()
		{
			++n;
			return this;
		}
		public IntegerObject reduce()
		{
			--n;
			return this;
		}
		
		public int getInt()
		{
			return n;
		}
	}
	
	
	/**
	 * 2017-12-23 
	 * 
	 * */
	public byte[] DownLoad()
	{
		try
		{
			SmbFile smbFile= new SmbFile(rootUrl);
			InputStream inputStream= smbFile.getInputStream();
			byte[] buffer= new byte[1*1024*1024];
			int len= -1;
			
			ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream(buffer.length*2);
			while((len=inputStream.read(buffer))!=-1)
			{
				byteArrayOutputStream.write(buffer,0,len);
			}
			
			inputStream.close();
			
			byte[] data= byteArrayOutputStream.toByteArray();
			
			byteArrayOutputStream.close();
			return data;
		}
		catch(Exception e){ e.printStackTrace();}
		return null;
	}
	
	/**
	 * 
	 * 
	 * 
	 * */
	public void SaveTo(String filePath)
	{
		byte[] data= DownLoad();
		
		File destFile= new File(filePath);
		if(destFile.exists()) destFile.delete();
		
		File destDirectory= new File(destFile.getParent());
		if(!destDirectory.exists()) destDirectory.mkdirs();
		
		try
		{
			FileOutputStream fileOutputStream= new FileOutputStream(destFile);
			
			fileOutputStream.write(data);
			fileOutputStream.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	
		
		
		
	}

}
