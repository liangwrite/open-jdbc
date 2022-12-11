package cn.fomer.common.smb;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import javax.persistence.Column;
import javax.persistence.Id;

import jcifs.smb.SmbFile;



public class SmbVO {
	
	public transient SmbFile smbFile;
	@Id
	//@GeneratedValue(strategy=GenerationType.IDENTITY) 不要自动创建
	//Integer mainId;
	public Integer id;
	



	public Integer depth;
	

	@Column(length=100)
	public String name; //"CentOS5.iso"
	public Long length; //4674615296 （文件夹大小：0）	 
	
	public Timestamp createTime; //1427163708696
	public Timestamp lastModified; //1427163708696
	
	@Column(length=500)
	public String path; //"smb://Liang:0@192.168.0.200/f/CentOS5.iso"
	@Column(length=500)
	public String parent; //"smb://Liang:0@192.168.0.200/f/"
	public Boolean isDirectory; //false
	
	public Integer parentId;
	
	public SmbVO(){}
	
	
	public static SmbVO Create(SmbFile smbFile)
	{
		return Property(smbFile);
	}
	
	
	private static SmbVO Property(SmbFile smbFile)
	{		
		SmbVO smbPO= new SmbVO();
		smbPO.smbFile= smbFile;
		try
		{
			smbPO.name= smbFile.getName();
			smbPO.createTime= new Timestamp(smbFile.createTime());
			smbPO.path= smbFile.getPath();
			smbPO.isDirectory= smbFile.isDirectory();
			smbPO.lastModified= new Timestamp(smbFile.lastModified());
			
			smbPO.parent= smbFile.getParent();
			
			smbPO.length= smbFile.length(); //错误：Access is denied.
		}
		catch(Exception e)
		{ 			
			e.printStackTrace();
			System.out.println("读取属性出错"+smbPO.getPath());
		}
		return smbPO;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUrl() {
		//smb://Liang:0@192.168.0.200/f/android-x86-4.4-r5.iso
		//file://///192.168.0.200/f/eav4.2.71.2x64.rar	
		if(path!=null)
		{
			if(path.contains("@"))
			{
				return ("//"+path.split("@")[1]).replace("/", "\\");
			}
			else
			{
				return ("//"+path).replace("/", "\\");
			}
			
		}
		return path;
	}
	
	public String getParentUrl() {
		//smb://Liang:0@192.168.0.200/f/android-x86-4.4-r5.iso
		//file://///192.168.0.200/f/eav4.2.71.2x64.rar	

			
		//System.out.println(parent);
		if(parent!=null)
		{
			if(parent.contains("@"))
			{
				return ("//"+parent.split("@")[1]).replace("/", "\\");
			}
			else
			{
				return ("//"+parent).replace("/", "\\");
			}
			
		}
		return parent;
	}
	
	

	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	
	//手动创建
	public Boolean getIsDirectory() {
		return isDirectory;
	}
	
	public void setIsDirectory(Boolean isDirectory) {
		this.isDirectory = isDirectory;
	}

	public Long getLength() {
		return length;
	}
	
	//手动创建
	public String getLengthString() {
		long B= 1;
		long KB= 1*1024;
		long MB= 1*1024*1024;
		long GB= 1*1024*MB;
		
		DecimalFormat format0= new DecimalFormat("0");
		DecimalFormat format2= new DecimalFormat("0.00");

		
		
		if(length>900*MB)
		{
			return format2.format(1d*length/GB)+"G";
		}
		else if(length>100*KB)
		{
			return format2.format(1d*length/MB)+"M";
		}
		else if(length>10*KB)
		{	
			return format0.format(1d*length/KB)+"K";
		}
		else if(length==0)
		{
			return "";
		}
		else
		{
			return 1*length/B+"B";
		}

	}
	
	
	public void setLength(Long length) {
		this.length = length;
	}

	public SmbFile getSmbFile() {
		return smbFile;
	}

	public void setSmbFile(SmbFile smbFile) {
		this.smbFile = smbFile;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}
	
	public String getCreateTimeString() {
		SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return dateFormat.format(createTime);
	}
	

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getLastModified() {
		return lastModified;
	}

	public String getLastModifiedString() {
		SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return dateFormat.format(lastModified);
	}
	
	public void setLastModified(Timestamp lastModified) {
		this.lastModified = lastModified;
	}

	public Integer getDepth() {
		return depth;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

}
