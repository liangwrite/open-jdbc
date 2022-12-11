package cn.fomer.common.http;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.gson.Gson;
import cn.fomer.common.util.Md5Util;

/**
 * 2020-06-08
 * spring.http.multipart.maxFileSize=40Mb 不起作用
 * spring.http.multipart.maxRequestSize=100Mb
 * 
 */
@Controller
@RequestMapping("/file")
public class FileController {
	
	final File folder= new File("D:\\");
	
	public FileController()
	{
		System.out.println("FileController被扫描到了！");
	}
	
	@CrossOrigin
	@RequestMapping("/info")
	@ResponseBody
	public Object info()
	{
		//
		return Collections.singletonMap("message", "success");
	}
	
	
	/**
	 * 2020-06-10 支持多文件和表单
	 */
	@CrossOrigin
	@RequestMapping("/upload")
	@ResponseBody
	public Object upload(HttpServletRequest request)
	{
		//
		MultipartHttpServletRequest multipartHttpServletRequest= (MultipartHttpServletRequest)request;
		
		//0.获取header信息
		Enumeration headers = multipartHttpServletRequest.getHeaderNames();
		while(headers.hasMoreElements())
		{
			Object key = headers.nextElement();
			String value = multipartHttpServletRequest.getHeader(key.toString());
			System.out.println(key+" = "+value);
		}
		
		//1.获取表单字段 (Url和表单变量)
		Map formMap = multipartHttpServletRequest.getParameterMap();
		System.out.println("varMap:"+new Gson().toJson(formMap));
		
		//2.获取文件
		Map<String, MultipartFile> fileMap = multipartHttpServletRequest.getFileMap();
		
		java.util.List<Map<String, Object>> maplList= new ArrayList<Map<String,Object>>();
		for(String filename:fileMap.keySet())
		{
			System.out.println("file:"+filename);
			MultipartFile multipartFile = fileMap.get(filename);
			if(multipartFile.getSize()==0) continue;
			Map<String, Object> result = saveFile(multipartFile);
			
			maplList.add(result);
		}
		
		return maplList;
	}
	
	/**
	 * 2020-06-10 上传单文件和参数
	 */
	@CrossOrigin
	@RequestMapping("/uploadSingle")
	@ResponseBody	
	public Map<String, Object> saveFile(MultipartFile multipartFile)
	{
		//
		Map<String, Object> data= new LinkedHashMap<>();
		data.put("success", false);
		
		//默认10M，然后再次判断
		int MAX_SIZE= 20*1024*1024;
		if((int)multipartFile.getSize()>MAX_SIZE)
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
			
			File target= new File(folder, UUID.randomUUID().toString());
			multipartFile.transferTo(target);
			System.out.println(target.getAbsolutePath());
			
			data.put("contentType", contentType);
			data.put("OriginalName", OriginalName);
			data.put("name", name);
			data.put("fileSize", fileSize);
			data.put("md5", Md5Util.Encode(target));
			data.put("server_path", target.getCanonicalPath());
			data.put("success", true);
			return data;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
			data.put("message", e.toString());
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
		
		DownloadHelper.download(response, srcExcelPath, suggestFileName);
		
	}
	
	@CrossOrigin
	@RequestMapping("/downloadOrJson")
	public void downloadOrJson(HttpServletResponse response, String id) throws IOException 
	{
		File srcExcelPath=new File("G:\\export\\empty.xls11.xlsx");
		
		
		if(StringUtils.isEmpty("id")) {
			response.setContentType("");
			response.getWriter().write(new Gson().toJson(Collections.singletonMap("message", "id 不能为空！")));
		}
		DownloadHelper.download(response, srcExcelPath, "1.xlsx");
		
	}
	
}
