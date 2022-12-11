package cn.fomer.common.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 2020-06-14 下载工具类
 *
 */
public class DownloadHelper {
	
	public static void download(HttpServletResponse response, File srcFile) throws IOException {
		//
		download(response, srcFile, srcFile.getName());
		
	}
	/**
	 * 
	 * @param suggestFileName 如果是中文必须编码
	 * @date 2020-06-14
	 * 
	 */
	public static void download(HttpServletResponse response, File srcFile, String suggestFileName) throws IOException
	{
		if(srcFile!=null)
		{
            if(srcFile.exists()&&srcFile.isFile())
            {
            	FileInputStream inputStream= new FileInputStream(srcFile);
                BufferedInputStream bufferedInputStream= new BufferedInputStream(inputStream);//（放到缓冲流里面）
                ServletOutputStream servletOutputStream = response.getOutputStream();//（获取文件输出IO流）
                BufferedOutputStream bufferedOutputStream=new BufferedOutputStream(servletOutputStream);
                
                //header
                if(suggestFileName==null||"".equals(suggestFileName)) suggestFileName= srcFile.getName();
                String iso8859Name= new String( suggestFileName.getBytes("gb2312"), "ISO8859-1" ); //（不能用utf-8）
                
                response.setHeader("Access-Control-Expose-Headers", "content-disposition,content-length"); //（axios跨域获取头信息）
                response.setContentType(getContentType(suggestFileName)); //（文件流）
                response.setHeader("content-disposition", "attachment;filename="+iso8859Name); //（设置头部信息）
                response.setHeader("content-length", Long.toString(srcFile.length())); //（显示附件大小）
                
                int len= 0;
                byte[] buffer = new byte[100*1024];
                //开始向网络传输文件流
                while ((len= bufferedInputStream.read(buffer, 0, buffer.length))!= -1) 
                {
                    bufferedOutputStream.write(buffer, 0, len);
                }
                bufferedOutputStream.flush();//这里一定要调用flush()方法
                
                inputStream.close();
                bufferedInputStream.close();
                servletOutputStream.close();
                bufferedOutputStream.close();
            }
        }
	}
	
	
	static String getContentType(String fileName) {
		//
		String lowerFileName= fileName.toLowerCase();
		if(lowerFileName.endsWith("xls") || lowerFileName.endsWith("xlsx")) {
			return IContentType.APPLICATION_EXCEL;
		}
			
		
		return IContentType.APPLICATION_STREAM;
	}

}
