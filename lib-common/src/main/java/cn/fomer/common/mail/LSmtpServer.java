package cn.fomer.common.mail;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class LSmtpServer {
	
	String smtpHost;
	String myMail;
	String username, password;

	public LSmtpServer(String smtpHost, String username, String password, String myMail) 
	{
		this.smtpHost= smtpHost;
		this.username= username; this.password= password;
		this.myMail= myMail;		
	}
		
	/* ֻ�ܷ���һ����: ���ѷ����л᲻���м�¼����Ҫ��������������ˣ� */
	public boolean sendMail(String ToMail, String title, String htmlContent)
	{ return sendMail(ToMail, title, htmlContent, new ArrayList<String>(), new ArrayList<String>()); }
	
	/* ֧�ֶ��˳��͡����� : ���ѷ����л᲻���м�¼����Ҫ��������������ˣ� */
	public boolean sendMail(String ToMail, String title, String htmlContent, List<String> CCMailList, List<String> BCCMailList)
	{			
		//String ToMail= "";
		//String subject= "�����ʼ�"+ new Timestamp(System.currentTimeMillis()).toString();
		//String htmlContent= "<h1 align=\"center\">��ð�</h1><a href='abc' target='_blank'>abc</a>";		
		//List<String> CCMailList= new ArrayList<>(); /* �������б� */
		//List<String> BCCMailList= new ArrayList<>(); /* �������б� */				
		
		try
		{
	        Properties props = new Properties();         	        
	        props.setProperty("mail.debug", "false"); /* ����bug����: �����������Ϣ */
	        props.setProperty("mail.smtp.auth", "true"); /* ���ͷ�������Ҫ�����֤: ���ϼ����������䶼Ҫ�˺���֤ */
	        props.setProperty("mail.host", smtpHost); /* �����ʼ�������������   */
	        props.setProperty("mail.transport.protocol", "smtp"); /* �����ʼ�Э������   */
	        
	        Session session = Session.getInstance(props); /* ���û�����Ϣ */
	        Message msg = new MimeMessage(session); /* �����ʼ����� */  
	        msg.setSubject(title); /* �ʼ����� */	        
	        msg.setContent(htmlContent, "text/html; charset=UTF-8"); /* �ʼ�����: ֧��html��ǩ */
	        
	        msg.setFrom(new InternetAddress(myMail)); /* ������: ��ʹ */
	        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(ToMail)); /* �ռ��� */
	        for(String mail: CCMailList)
	        	msg.addRecipient(Message.RecipientType.CC, new InternetAddress(mail)); /* ���͵� */
	        for(String mail: BCCMailList)
	        	msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(mail)); /* ���͵� */
	        
	        Transport transport = session.getTransport(); /*  */
	        transport.connect(username, password); /* �����ʼ�������: �����������˺����루�쳣: ����������δ֪��������Ȩʧ�ܡ��� */
	        transport.sendMessage(msg, msg.getAllRecipients()); /* �ռ��� */
	        transport.close();  
	        return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
			return false;
		}
	}
}

