package cn.fomer.common.util;

import java.io.File;

import javax.swing.filechooser.FileSystemView;

/**
 * 2021-12-10
 * 
 */
public class SystemHelper {

	
	/**
	 * 2021-12-10 获取桌面路径
	 */
	public static File getDesktop() {
		FileSystemView fileSystemView = FileSystemView.getFileSystemView();
		File desktop= fileSystemView.getHomeDirectory();   
		System.out.println(desktop.getPath());
		return desktop;
	}
	
	public static File getDesktop(String filename) {
		return new File(getDesktop(), filename);
	}
	
	/**
	 * 2021-12-10
	 */
	public static void main(String[] args) {
		System.out.println(getDesktop("aa.txt"));;
	}
	
}
