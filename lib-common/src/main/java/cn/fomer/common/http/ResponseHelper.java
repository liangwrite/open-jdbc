package cn.fomer.common.http;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

/**
 * Response 工具类
 *
 * @author Liang
 * @email liang@163.com
 * @date 2022-11-19
 */
public class ResponseHelper {

	public static void writeJson(HttpServletResponse response, String s) throws IOException {
		//
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(s);
	}
	
	
	public static void writeFile(HttpServletResponse response, File file) throws IOException {
		//
		DownloadHelper.download(response, file);
	}
	
	public static void writeFile(HttpServletResponse response, File file, String suggestFileName) throws IOException {
		//
		DownloadHelper.download(response, file, suggestFileName);
	}
}
