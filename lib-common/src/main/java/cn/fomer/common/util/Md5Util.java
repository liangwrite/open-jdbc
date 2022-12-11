package cn.fomer.common.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;


/**
 * 2018-03-16 来源【陕西华氏SPD+网络】
 * 
 * */ 
public class Md5Util
{
	//十六位数字字符串
	private static final String HEX_NUMS_STR = "0123456789ABCDEF";
	//md5字符串长度
	private static final Integer SALT_LENGTH = 12;

	/** 
	* @Title: hexStringToByte 
	* @Description: 将16进制字符串转换成字节数组
	* @author ginfo003 
	* @date 2014-3-21 上午11:18:10 
	* @version v1.0.9 
	* @param hex
	* 			16进制字符串
	* @return
	*        byte[] 字节数组
	*/ 
	public static byte[] hexStringToByte(String hex)
	{
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] hexChars = hex.toCharArray();
		for (int i = 0; i < len; i++)
		{
			int pos = i * 2;
			result[i] = (byte) (HEX_NUMS_STR.indexOf(hexChars[pos]) << 4 | HEX_NUMS_STR.indexOf(hexChars[pos + 1]));
		}
		return result;
	}

	/** 
	* @Title: byteToHexString 
	* @Description: 将指定byte数组转换成16进制字符串
	* @author ginfo003 
	* @date 2014-3-21 上午11:18:33 
	* @version v1.0.9 
	* @param b
	* 			指定byte数组
	* @return
	*        String 16进制字符串
	*/ 
	public static String byteToHexString(byte[] b)
	{
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < b.length; i++)
		{
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1)
			{
				hex = '0' + hex;
			}
			hexString.append(hex.toUpperCase());
		}
		return hexString.toString();
	}

	/** 
	* @Title: validPassword 
	* @Description:  验证口令是否合法
	* @author ginfo003 
	* @date 2014-3-21 上午11:19:03 
	* @version v1.0.9 
	* @param password
	* 			算法口令的数据
	* @param passwordInDb
	* 			16进制字符串格式口令
	* @return
	* @throws NoSuchAlgorithmException
	* @throws UnsupportedEncodingException
	*        boolean 
	*/ 
	public static boolean validPassword(String password, String passwordInDb) throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		// 将16进制字符串格式口令转换成字节数组
		byte[] pwdInDb = hexStringToByte(passwordInDb);
		// 声明盐变量
		byte[] salt = new byte[SALT_LENGTH];
		// 将盐从数据库中保存的口令字节数组中提取出来
		System.arraycopy(pwdInDb, 0, salt, 0, SALT_LENGTH);
		// 创建消息摘要对象
		MessageDigest md = MessageDigest.getInstance("MD5");
		// 将盐数据传入消息摘要对象
		md.update(salt);
		// 将口令的数据传给消息摘要对象
		md.update(password.getBytes("UTF-8"));
		// 生成输入口令的消息摘要
		byte[] digest = md.digest();
		// 声明一个保存数据库中口令消息摘要的变量
		byte[] digestInDb = new byte[pwdInDb.length - SALT_LENGTH];
		// 取得数据库中口令的消息摘要
		System.arraycopy(pwdInDb, SALT_LENGTH, digestInDb, 0, digestInDb.length);
		// 比较根据输入口令生成的消息摘要和数据库中消息摘要是否相同
		if (Arrays.equals(digest, digestInDb))
		{
			// 口令正确返回口令匹配消息
			return true;
		}
		else
		{
			// 口令不正确返回口令不匹配消息
			return false;
		}
	}

	/** 
	* @Title: getEncryptedPwd 
	* @Description: 获得加密后的16进制形式口令
	* @author ginfo003 
	* @date 2014-3-21 上午11:20:46 
	* @version v1.0.9 
	* @param text
	* 			算法口令数据
	* @return
	* @throws NoSuchAlgorithmException
	* @throws UnsupportedEncodingException
	*        String 
	*/ 
	public static String Encode(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		// 声明加密后的口令数组变量
		byte[] buffer = null;
		// 随机数生成器
		SecureRandom secureRandom = new SecureRandom();
		// 声明盐数组变量
		byte[] salt = new byte[SALT_LENGTH];
		// 将随机数放入盐变量中
		secureRandom.nextBytes(salt);

		// 声明消息摘要对象
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		// 将盐数据传入消息摘要对象
		md5.update(salt);
		// 将口令的数据传给消息摘要对象
		md5.update(text.getBytes("UTF-8"));
		// 获得消息摘要的字节数组
		byte[] digest = md5.digest();

		// 因为要在口令的字节数组中存放盐，所以加上盐的字节长度
		buffer = new byte[digest.length + SALT_LENGTH];
		// 将盐的字节拷贝到生成的加密口令字节数组的前12个字节，以便在验证口令时取出盐
		System.arraycopy(salt, 0, buffer, 0, SALT_LENGTH);
		// 将消息摘要拷贝到加密口令字节数组从第13个字节开始的字节
		System.arraycopy(digest, 0, buffer, SALT_LENGTH, digest.length);
		// 将字节数组格式加密后的口令转化为16进制字符串格式的口令

		return byteToHexString(buffer);
	}
	
	public static void main(String[] args) {
		try
		{
			System.out.println("A669D439704B72FB6CF1558AB0DD9981\r\n"+Encode(new File("E:\\eclipse-4.6.3-win32-x86_64.zip")));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static String Encode(File file) throws IOException
	{
	    
		// 缓冲区大小（这个可以抽出一个参数）		
		DigestInputStream digestInputStream = null;

		try 
		{
			// 拿到一个MD5转换器（同样，这里可以换成SHA1）
			MessageDigest md5 = MessageDigest.getInstance("MD5");

			// 使用DigestInputStream
			digestInputStream = new DigestInputStream(new FileInputStream(file), md5);
			
			// read的过程中进行MD5处理，直到读完文件			
			byte[] buffer = new byte[10*1024];
			
			//注意：这步必须有
			while ((digestInputStream.read(buffer)) != -1){} 
			// 获取最终的MessageDigest
			md5 = digestInputStream.getMessageDigest();

			// 拿到结果，也是字节数组，包含16个元素

			byte[] resultByteArray = md5.digest();

			// 同样，把字节数组转换成字符串
			return byteToHexString(resultByteArray);

		} 
		catch(NoSuchAlgorithmException e) 
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			try { digestInputStream.close();} catch (Exception e) { e.printStackTrace();}
		} 
	}
}
