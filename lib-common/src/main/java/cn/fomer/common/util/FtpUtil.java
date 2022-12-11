package cn.fomer.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/**
 * 2019-01-03 来自新东网云店项目
 * 
 * 
 */
public class FtpUtil {
	  
    private static String encoding = System.getProperty("file.encoding");  
      
    FTPClient ftpclient;  
  
    /** ftp服务器地址 */  
    private String host;  
    /** ftp 端口号 默认21 */  
    private int port = 21;  
    /** ftp服务器用户名 */  
    private String username;  
    /** ftp服务器密码 */  
    private String password;  
    /** ftp远程目录   以文件分隔符结尾 */  
    private String remoteDir;  
    /** 本地存储目录 */  
    private String localDir;  
    /** 文件路径通配符 默认列出所有*/  
    private String regEx = "*";  
    /** 指定要下载的文件名 */  
    private String downloadFileName;  
  
    /** 
     * 设置连接属性 
     *  
     * @param host 
     * @param username 
     * @param password 
     * @return 
     */  
    public FtpUtil(String host, String username, String password) {  
    	this(host, 21, username, password);
    }  
  
    /** 
     * 设置连接属性 
     *  
     * @param host 
     * @param port 
     * @param username 
     * @param password 
     */  
    public FtpUtil(String host, int port, String username,String password) {  
        this.host = host;  
        this.port = port;  
        this.username = username;  
        this.password = password;  
    }  
  
    /** 
     * 连接FTP服务器 
     */  
    private FtpUtil connectServer() {  
        ftpclient = new FTPClient();  
        //设置超时时间  
        ftpclient.setConnectTimeout(30000);  
        try {  
            // 1、连接服务器  
            if(!ftpclient.isConnected()){  
                // 如果采用默认端口，可以使用client.connect(host)的方式直接连接FTP服务器  
                ftpclient.connect(host, port);  
                // 登录  
                ftpclient.login(username, password);  
                // 获取ftp登录应答码  
                int reply = ftpclient.getReplyCode();  
                // 验证是否登陆成功  
                if (!FTPReply.isPositiveCompletion(reply)) {  
                    logger("未连接到FTP，用户名或密码错误。");  
                    ftpclient.disconnect();  
                    throw new RuntimeException("未连接到FTP，用户名或密码错误。");  
                } else {  
                    logger("FTP连接成功。IP:"+host +"PORT:" +port);  
                }  
                // 2、设置连接属性  
                ftpclient.setControlEncoding(encoding);  
                // 设置以二进制方式传输  
                ftpclient.setFileType(FTPClient.BINARY_FILE_TYPE);    
                ftpclient.enterLocalPassiveMode();  
            }  
        } catch (SocketException e) {  
            try {  
                ftpclient.disconnect();  
            } catch (IOException e1) {  
            }  
            logger("连接FTP服务器失败" + e.getMessage());  
            throw new RuntimeException("连接FTP服务器失败" + e.getMessage());  
        } catch (IOException e) {  
        }  
        return this;  
    }  
      
  
    /** 
     * 下载文件 
     */  
    public List<File> download(){  
          
        List<File> files = null;  
          
        this.connectServer();  
        InputStream is = null;  
        File downloadFile = null;  
        try {  
            // 1、设置远程FTP目录  
            ftpclient.changeWorkingDirectory(remoteDir);  
            logger("切换至工作目录【" + remoteDir + "】");  
            // 2、读取远程文件  
            FTPFile[] ftpFiles = ftpclient.listFiles(regEx);  
            if(ftpFiles.length==0) {  
                logger("文件数为0，没有可下载的文件！");  
                return null;  
            }  
            logger("准备下载" + ftpFiles.length + "个文件");  
            // 3、保存文件到本地  
            for (FTPFile file : ftpFiles) {  
                //如果有指定下载的文件  
                if(StringUtils.isNotBlank(downloadFileName) && !file.getName().equals(downloadFileName)){  
                    continue;  
                }  
                if(files == null) files = new ArrayList<File>();  
                is = ftpclient.retrieveFileStream(file.getName());  
                if(is==null) throw new RuntimeException("下载失败，检查文件是否存在");  
                downloadFile = new File(localDir + file.getName());  
                FileOutputStream fos = FileUtils.openOutputStream(downloadFile);  
                IOUtils.copy(is, fos);  
                ftpclient.completePendingCommand();  
                IOUtils.closeQuietly(is);  
                IOUtils.closeQuietly(fos);  
                  
                /* 
                //另外一种方式，供参考 
                OutputStream is = new FileOutputStream(localFile); 
                ftpClient.retrieveFile(ff.getName(), is); 
                is.close(); 
                */  
                  
                files.add(downloadFile);  
            }  
            logger("文件下载成功,下载文件路径：" + localDir);  
            return files;  
        } catch (IOException e) {  
            logger("下载文件失败" + e.getMessage());  
            throw new RuntimeException("下载文件失败" + e.getMessage());  
        }  
    }  
      
    /** 
     * 下载文件 
     * @param localDir 
     * @param remoteDir 
     */  
    public List<File> download(String remoteDir,String localDir){  
        this.remoteDir = remoteDir;  
        this.localDir = localDir;  
        return this.download();  
    }  
    /** 
     * 下载文件 
     * @param remoteDir 
     * @param regEx 文件通配符 
     * @param localDir 
     * @return 
     */  
    public List<File> download(String remoteDir,String regEx,String localDir){  
        this.remoteDir = remoteDir;  
        this.localDir = localDir;  
        this.regEx = regEx;  
        return this.download();  
    }  
      
    /** 
     * 下载文件 
     * @param downloadFileName 指定要下载的文件名称 
     * @return 
     */  
    public List<File> download(String downloadFileName){  
        this.downloadFileName = downloadFileName;  
        return this.download();  
    }  
      
    /** 
     * 上传文件 
     * @param files 
     */  
    public void upload(List<File> files){  
          
        OutputStream os = null;  
        try {  
            // 2、取本地文件  
            if(files == null || files.size()==0) {  
                logger("文件数为0，没有找到可上传的文件");  
                return;  
            }  
            logger("准备上传" + files.size() + "个文件");  
            // 3、上传到FTP服务器  
            for(File file : files){  
                this.connectServer();  
                // 1、设置远程FTP目录  
                ftpclient.changeWorkingDirectory(remoteDir);  
                logger("切换至工作目录【" + remoteDir + "】");  
                os = ftpclient.storeFileStream(file.getName());  
                if(os== null) throw new RuntimeException("上传失败，请检查是否有上传权限");  
                IOUtils.copy(new FileInputStream(file), os);  
                IOUtils.closeQuietly(os);  
            }  
            logger("文件上传成功,上传文件路径：" + remoteDir);  
        } catch (IOException e) {  
            logger("上传文件失败" + e.getMessage());  
            throw new RuntimeException("上传文件失败" + e.getMessage());  
        }  
    }  
      
    public OutputStream getOutputStream(String fileName){  
        OutputStream os = null;  
        this.connectServer();  
        // 1、设置远程FTP目录  
        try {  
            ftpclient.changeWorkingDirectory(remoteDir);  
            logger("切换至工作目录【" + remoteDir + "】");  
            os = ftpclient.storeFileStream(fileName);  
            if(os== null) throw new RuntimeException("服务器上创建文件对象失败");  
            return os;  
        } catch (IOException e) {  
            logger("服务器上创建文件对象失败" + e.getMessage());  
            throw new RuntimeException("服务器上创建文件对象失败" + e.getMessage());  
        }  
    }  
    /** 
     * 上传文件 
     * @param files 上传的文件 
     * @param remoteDir 
     */  
    public void upload(List<File> files,String remoteDir){  
        this.remoteDir = remoteDir;  
        this.upload(files);  
    }  
      
    /** 
     * 上传文件 
     * @param file 
     */  
    public void upload(File file){  
        List<File> files = new ArrayList<File>();  
        files.add(file);  
        upload(files);  
    }  
      
    /** 
     * 判断文件在FTP上是否存在 
     * @param fileName 
     * @return 
     */  
    public boolean isFileExist(String fileName) {  
          
        boolean result = false;  
        this.connectServer();  
        try {  
            // 1、设置远程FTP目录  
            ftpclient.changeWorkingDirectory(remoteDir);  
            logger("切换至工作目录【" + remoteDir + "】");  
            // 2、读取远程文件  
            FTPFile[] ftpFiles = ftpclient.listFiles(regEx);  
            if(ftpFiles.length==0) {  
                logger("文件数为0，没有可下载的文件！");  
                return result;  
            }  
            // 3、检查文件是否存在  
            for (FTPFile file : ftpFiles) {  
                if(file.getName().equals(fileName)){  
                    result = true;  
                    break;  
                }  
            }  
        } catch (Exception e) {  
            logger("检查文件是否存在失败" + e.getMessage());  
            throw new RuntimeException("检查文件是否存在失败" + e.getMessage());  
        }  
          
        return result;  
    }  
  
     /** 
     * 关闭连接 
     */  
    public void closeConnect() {  
        try {  
            ftpclient.disconnect();  
            logger(" 关闭FTP连接!!! ");  
        } catch (IOException e) {  
            logger(" 关闭FTP连接失败!!! ",e);  
        }  
    }  
    public String getRemoteDir() {  
        return remoteDir;  
    }  
  
    public void setRemoteDir(String remoteDir) {  
        this.remoteDir = remoteDir;  
    }  
  
    public String getLocalPath() {  
        return localDir;  
    }  
  
    public void setLocalPath(String localPath) {  
        this.localDir = localPath;  
    }  
  
    public String getDownloadFileName() {  
        return downloadFileName;  
    }  
  
    public void setDownloadFileName(String downloadFileName) {  
        this.downloadFileName = downloadFileName;  
    }  
      
    @Override  
    public String toString() {  
        return "FTPUtil [host=" + host + ", port=" + port + ", username="  
                + username + ", password=" + password + "]";  
    }  
    
    public static void main(String[] args) {
		FtpUtil ftpUtil = new FtpUtil("127.0.0.1", 21, "xdw", "xdw#2017");
		ftpUtil.connectServer();
		
		try
		{			
			
			FTPFile[] arr= ftpUtil.ftpclient.listFiles("");
			SimpleDateFormat dateFormat= new SimpleDateFormat("yyyyMMdd HH:mm:ss");
			for(FTPFile ftpFile:arr)
			{
				System.out.println(ftpFile.getName()+"\t"+ftpFile.getSize()+"\t"+dateFormat.format(ftpFile.getTimestamp().getTime()));
			}
			System.out.println(arr.length);
			System.out.println("OK!");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try{ ftpUtil.closeConnect();}catch(Exception err){ System.err.println("关闭失败！"+err.toString());}
		}

		
		/*
		ftpUtil.setLocalPath("D:\\");
		ftpUtil.setRemoteDir("/"); //"/mnt/hd1/itlulife/apps/"
		ftpUtil.setDownloadFileName("nohup.out");
		List<File> download = ftpUtil.download();
		for (File file : download) {
			System.out.println(file.getPath()+file.separator+file.getName());
		}
		*/
		
		
	}

    void logger(String message)
    {
    	
    }
    void logger(String message, Exception e)
    {
    	
    }
}
