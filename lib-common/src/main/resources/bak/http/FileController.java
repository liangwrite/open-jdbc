package cn.fomer.common.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.gson.Gson;

/**
 * 2020-06-08
 * spring.http.multipart.maxFileSize=40Mb
 * spring.http.multipart.maxRequestSize=100Mb
 * 
 */
@Controller
@RequestMapping("/file")
public class FileController {
	
	
	/**
	 * 2020-06-10 上传多文件和表单
	 */
	@CrossOrigin
	@RequestMapping("/upload")
	@ResponseBody
	//public Object upload(MultipartFile multipartFile)
	public Object upload(HttpServletRequest request)
	{
		//
		System.out.println(">>>>>>>uploading...");
		MultipartHttpServletRequest multiRequest= (MultipartHttpServletRequest)request;
		
		//0
		Enumeration headers = multiRequest.getHeaderNames();
		
		//1.表单 (Url和表单变量)
		Map varMap = multiRequest.getParameterMap();
		System.out.println("varMap:"+new Gson().toJson(varMap));
		
		//2.文件
		Map<String, MultipartFile> fileMap = multiRequest.getFileMap();
		
		Map map= new HashMap<>();
		for(String filename:fileMap.keySet())
		{
			System.out.println("file:"+filename);
			Map<String, Object> result = uploadSingle(fileMap.get(filename));
			
			map= result;
		}
		
		return map;
	}
	
	/**
	 * 2020-06-10 上传单文件和参数
	 */
	@CrossOrigin
	@RequestMapping("/uploadSingle")
	@ResponseBody	
	public Map<String, Object> uploadSingle(MultipartFile multipartFile)
	{
		//
		System.out.println("single...");
		Map<String, Object> data= new HashMap<>();
		data.put("success", false);
		
		//默认10M，然后再次判断
		if((int)multipartFile.getSize()>20*1024*1024)
		{
			data.put("message", "上传的文件不能超过20M！");
			return data;
		}
		
		try
		{
			String OriginalName= multipartFile.getOriginalFilename(); //TortoiseSVN-1.10.5.28651-x64-svn-1.10.6.msi
			String contentType = multipartFile.getContentType(); //application/octet-stream
			String name = multipartFile.getName(); //file1(无用信息)
			long fileSize= multipartFile.getSize();
			
			File target= new File("G:/11/"+UUID.randomUUID().toString());
			multipartFile.transferTo(target);
			
			
			data.put("contentType", contentType);
			data.put("OriginalName", OriginalName);
			data.put("name", name);
			data.put("fileSize", fileSize);
			data.put("target", target.getCanonicalPath());
			data.put("success", true);
			System.out.println("上传成功！"+target.getCanonicalPath());
			return data;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
			data.put("message", e.toString());
			System.out.println("上传异常！"+e.toString());
			return data;
		}
		
	}
	
	@CrossOrigin
	@RequestMapping("/download")
	//@ResponseBody
	//public Object download(File srcExcelPath, String suggestFileName) throws IOException
	public void download() throws IOException
	{
		File srcExcelPath=new File("G:/11/TortoiseSVN-1.10.5.28651-x64-svn-1.10.6.msi");
		String suggestFileName= null;
		
		ServletRequestAttributes servletRequestAttributes= (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		HttpServletResponse response = servletRequestAttributes.getResponse();		
		
		Download.download(srcExcelPath, suggestFileName, response);
		
	}
}
