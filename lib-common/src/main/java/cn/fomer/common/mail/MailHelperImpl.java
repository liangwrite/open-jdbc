package cn.fomer.common.mail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;

/**
 * 2019-05-10
 * 
 * 
 */
public class MailHelperImpl implements MailHelper{
	LSmtpServer lSmtpServer;
	String toAccount;
	
	@Override
	public MailHelper setMyAccount(String smtpServer, String email, String password) {
		// TODO Auto-generated method stub
		lSmtpServer= new LSmtpServer(smtpServer, email, password, email);
		return this;
	}

	@Override
	public MailHelper setToAccount(String email) {
		// TODO Auto-generated method stub
		toAccount= email;
		return this;
	}

	@Override
	public boolean send(String title,String message) throws Exception {
		// TODO Auto-generated method stub
		return lSmtpServer.sendMail(toAccount, title, message);
	}
	//
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		MailHelper mail= new MailHelperImpl();
		mail.setMyAccount("smtp.yunyou.top", "pachong@fomer.cn", "kbz2018");
		mail.setToAccount("498847886@qq.com");
		mail.send("通知邮件","爬虫不工作了！因为XX原因！");

	}

	public static void main1(String[] args) {
		File srcFile= new File("E:\\Users\\Liang\\Documents\\Files\\李亮亮\\笔记\\mail.in.txt");
		File toFile= new File("E:\\Users\\Liang\\Documents\\Files\\李亮亮\\笔记\\mail.out.txt");
		MailHelperImpl.cleanEmpty(srcFile, toFile);
	}
	
	public static void cleanEmpty(File srcFile, File toFile) {
		String csn = Charset.defaultCharset().name(); //获取默认字体


		System.out.println(csn);
		BufferedReader bufferedReader;
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(srcFile), StandardCharsets.UTF_8);
			bufferedReader = new BufferedReader(inputStreamReader);
			String line = null;
			
			
			int i=-1;
			FileOutputStream fileOutputStream= new FileOutputStream(toFile);
			while ((line = bufferedReader.readLine()) != null) {
				++i;
				
				
				//1.
				if(StringUtils.isEmpty(line)) continue;
				
				
				//2.
				if(line.startsWith("//") && i>0) {
					fileOutputStream.write(("\r\n\r\n"+line+"\r\n").getBytes());
					continue;
				}
				
				
				//2.
				if(line.startsWith(">>") && i>0) {
					fileOutputStream.write(("\r\n"+line+"\r\n").getBytes());
					continue;
				}
					
					
					
				//
				fileOutputStream.write((line+"\r\n").getBytes());
				
				

				System.out.println(line);
			}
			fileOutputStream.close();
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
}
