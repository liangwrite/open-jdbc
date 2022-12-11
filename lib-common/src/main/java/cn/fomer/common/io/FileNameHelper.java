package cn.fomer.common.io;

import java.nio.file.Path;

import lombok.Getter;

/**
 * 2021-10-26
 * 
 */
@Getter
public class FileNameHelper {

	static String[] pictureList= {".jpg", ".png", ".jpeg"};
	static String[] videoList= {".mp4"};
	
	String path;
	
	String name;
	String extenString;
	
	
	public static FileNameHelper newInstance(String path) {
		return new FileNameHelper(path);
	}
	
	
	FileNameHelper(String path) {
		this.path= path;
		
		if(path.contains(".")){
			this.extenString= path.substring(path.indexOf(".")+1);
		}
		
		
	}
	
	
	
	public boolean isPicture() {
		for(String exten: pictureList) {
			if(exten.equalsIgnoreCase(extenString))
				return true;
		}
			
		
		return false;
	}
	
	
	public String getShortNameWithoutExt() {
		String s= path.replace("/", "\\");
		if(path.contains(".")) {
			s= s.substring(0, path.indexOf("."));
		}
		if(path.contains("\\")) {
			s= s.substring(s.lastIndexOf("\\"));
		}
		return s;
	}
	
}
