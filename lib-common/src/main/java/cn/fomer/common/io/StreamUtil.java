package cn.fomer.common.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 2020-08-23
 *
 */
public class StreamUtil {

	public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException
	{
		byte[] buffer=new byte[1*1024*1024];
		int len= 0;
		while((len=inputStream.read(buffer))!=-1)
		{
			outputStream.write(buffer,0,len);
		}
		inputStream.close();
	}
}
