package cn.fomer.common.mail;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;

import javax.mail.Multipart;

import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;


public class LPop3Server 
{
	public LPop3Server(String pop3Host, String username, String password) throws Exception
	{
		Store tmpstore= null;
		Folder tmpfolder= null;
		
		try
		{
			Properties props = new Properties(); 
			props.setProperty("mail.store.protocol", "pop3");
			props.setProperty("mail.pop3.host", pop3Host); /* pop3.163.com */  
			Session session=Session.getInstance(props);  
			Store store = session.getStore("pop3");
			tmpstore= store;
			session.setDebug(true);  
			
			store.connect(pop3Host, username, password);  
			Folder folder = store.getFolder("INBOX"); /* �ռ��� */ 
			tmpfolder= folder;
			
			folder.open(Folder.READ_ONLY);  
			
			Message[] listMessage = folder.getMessages();
			
			
			for (int i=0;i< listMessage.length; i++) 
			{
				
				Message message= listMessage[i];				
				String subject= message.getSubject(); /* ���� */
				Date sentDate= message.getSentDate();
				String fromMail= message.getFrom()[0].toString(); /* �����г��˼��ַ����ˣ�������Ҫ�ֶ���ȡ�ʼ���ַ */
				/*
				 * "fomer.cn" <fomercn@163.com>
				 * =?utf-8?B?Zm9tZXI=?= <fomer@fomer.cn>
				 * fomer@fomer.cn
				 * fomer@fomer.cn
				 * */
				System.out.print(subject+ "------>");
				System.out.println(fromMail);
				
				String contentType= message.getContentType(); /*  */
				/* 
				 * "text/html; charset=UTF-8"
				 * "multipart/alternative; 	boundary="__=_Part_Boundary_004_029012.000209"" ����仯��
				 * "multipart/mixed;  	boundary="----=_Part_184116_184134809.1487079939222"" ����仯��
				 * "multipart/related;  	boundary="----=_Part_189251_1054017644.1487083334559"" ����仯��
				 * 				 
				 */
				
				Object content= message.getContent(); /* �ʼ����ݷ�Ϊ����: String ��  MimeMultipart*/				
				
				/*
				 * 
				 * "class java.lang.String"��
				 * "class javax.mail.internet.MimeMultipart" ���� �ʼ����в�ͼ������ ��ĩβ��� 
				 * 
				 * */
				
				if(true)
				{

					
					System.out.println("return");
					continue;					
				}
				
				
				if(content.getClass().equals(String.class)) { String text= (String)content;}				
				
				else if(content.getClass().equals(MimeMultipart.class))
				{
					
					Multipart multipart= (Multipart)content;
					/* 
					 * �ʼ���Ϊ��������: ����������� 
					 * ÿ����ͼ��һ��
					 * ÿ��������һ��
					 * �����ʼ�ĩβ���ӹ����һ��
					 * 
					 * */					
					int partLength= multipart.getCount(); /* �ȽϺ�ʱ ������Ӧ�������ظ������������һ�㶼������ */
					
					for(int n=0;n<partLength;n++)
					{						
						Part part= multipart.getBodyPart(n);
						String fileName= part.getFileName(); /* ��ͼ���ļ��� �� �������ļ��� */
						
						String disposition= part.getDisposition();
						/*
						 * null() ����������ּ���ÿ���ʼ�����
						 * Part.ATTACHMENT ("attachment") ���� 
						 * Part.INLINE ("inline") ����
						 * 
						 * */
						
						if(disposition!=null)
							if(disposition.equals(Part.ATTACHMENT)) /* ���總�� */
							{								
								//save(part.getInputStream(), fileName);
							}
							else if(disposition.equals(Part.INLINE)) /* �����ڲ���ͼ */
							{								
								//save(part.getInputStream(), fileName);
							}
						
						
					}
					
				}
							
			}  		
			
			folder.close(false);
			store.close();
		}
		
		catch(Exception e)
		{
			if(tmpfolder.isOpen()) 
			{
				System.out.println();
				System.out.println("LPop3Server.LPop3Server()");
				System.err.println("tmpfolder.isOpen()==true");
				tmpfolder.close(false);
			}
			if(tmpstore.isConnected()) 
			{
				System.out.println();
				System.out.println("LPop3Server.LPop3Server()");
				System.err.println("tmpstore.isConnected()==true");
				tmpstore.close();
			}
			System.err.println(e.toString());
		}
		System.out.println("..over..");
	}
	
	
	void save(InputStream inputStream, String fileName)
	{
		try
		{
			
			System.out.println("��ʼ���� "+fileName+"..."+ inputStream.available()+"");
			if(fileName.contains("?")) fileName= fileName.replace("?", "@");
			FileOutputStream outputStream= new FileOutputStream(new File("D:\\Users\\Liang\\Desktop\\attach\\"+fileName));			
			byte[] buffer= new byte[1024*100]; //100KB
			int length= 0;
			while((length= inputStream.read(buffer, 0, buffer.length))!=-1)
			{
				outputStream.write(buffer, 0, length);
			}
			outputStream.flush();
			outputStream.close();
		}
		catch(Exception e)
		{
			System.out.println("LPop3Server.save()");
			System.err.println(e.toString());
		}
	}

}
