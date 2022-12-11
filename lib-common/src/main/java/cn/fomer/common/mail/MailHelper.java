package cn.fomer.common.mail;

/**
 * 2019-05-10
 * 
 * 
 */
public interface MailHelper {

	//
	MailHelper setMyAccount(String smtpServer,String email,String password);
	MailHelper setToAccount(String email);
	boolean send(String title,String message) throws Exception;
}


