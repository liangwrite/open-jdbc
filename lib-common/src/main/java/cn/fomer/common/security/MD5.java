package cn.fomer.common.security;

import java.security.MessageDigest;

public class MD5 
{
	/* 32 */
	public static String encode(String s)
	{
		MessageDigest md5 = null;
		try { md5 = MessageDigest.getInstance("MD5"); } catch (Exception e) { return "";}
		
		char[] charArray = s.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer sBuffer = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int n = ((int) md5Bytes[i]) & 0xff;
			if (n < 16)
				sBuffer.append("0");
			sBuffer.append(Integer.toHexString(n));
		}
		return sBuffer.toString();
	}

}
