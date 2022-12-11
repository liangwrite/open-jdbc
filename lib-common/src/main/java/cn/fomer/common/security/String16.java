package cn.fomer.common.security;

/**
 * 2019-05-15
 * 
 * 
 */
public class String16 {
	
	static String toString16(byte[] data)
	{
		//
	    char[] chars = "0123456789ABCDEF".toCharArray();
	    StringBuilder sb = new StringBuilder("");
	    int bit;
	    for (int i = 0; i < data.length; i++) 
	    {
	        bit = (data[i] & 0x0f0) >> 4;
	        sb.append(chars[bit]);
	        bit = data[i] & 0x0f;
	        sb.append(chars[bit]);
	    }
		return sb.toString();
	}
	static byte[] fromString16(String s16)
	{
		//
	    String str = "0123456789ABCDEF";
	    char[] hexs = s16.toCharArray();
	    byte[] bytes = new byte[s16.length() / 2];
	    int n;
	    for (int i = 0; i < bytes.length; i++) {
	        n = str.indexOf(hexs[2 * i]) * 16;
	        n += str.indexOf(hexs[2 * i + 1]);
	        bytes[i] = (byte) (n & 0xff);
	    }
	    return bytes;
	}

}
