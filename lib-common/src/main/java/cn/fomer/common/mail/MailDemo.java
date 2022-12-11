package cn.fomer.common.mail;

/**
 * 2019-05-10
 * 
 * 
 */
public class MailDemo {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		MailHelper mail= new MailHelperImpl();
		mail.setMyAccount("smtp.yunyou.top", "pachong@fomer.cn", "kbz2018");
		mail.setToAccount("498847886@qq.com");
		mail.send("测试邮件","test mail");

	}

}
