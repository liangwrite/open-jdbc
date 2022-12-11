package cn.fomer.common.qrcode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Hashtable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;


/**
 * 2017-06-13
 * 
 * 
 */
public class QRCode {
	public static void create(String code,String fileName) throws Exception
	{
		int width = 300; /* px */
		int height = 300;
		Hashtable<EncodeHintType,String> hashtable = new Hashtable<EncodeHintType,String>();
		hashtable.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		BitMatrix bitMatrix 
			= new MultiFormatWriter().encode(code, BarcodeFormat.QR_CODE, width, height, hashtable);
		
		OutputStream outputStream
			= new FileOutputStream(fileName);
		MatrixToImageWriter.writeToStream(bitMatrix, "png", outputStream);
		outputStream.close();
		
		File file= new File(fileName);		
	}

}
