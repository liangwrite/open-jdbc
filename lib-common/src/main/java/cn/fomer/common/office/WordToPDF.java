/*package cn.fomer.common.office;

import java.io.File;
import java.io.OutputStream;

import org.docx4j.Docx4J;
import org.docx4j.convert.out.FOSettings;
import org.docx4j.fonts.IdentityPlusMapper;
import org.docx4j.fonts.Mapper;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.org.apache.poi.util.IOUtils;


*//**
 * 2017-12-05
 * 
 * 
 *//*
public class WordToPDF {
	
	public static void convert(String docxPath,String pdfPath)
	{

		OutputStream os = null;
		try
		{
			WordprocessingMLPackage mlPackage = WordprocessingMLPackage.load(new File(docxPath));
			Mapper fontMapper = new IdentityPlusMapper();
			fontMapper.put("华文行楷", PhysicalFonts.get("STXingkai"));
			fontMapper.put("华文仿宋", PhysicalFonts.get("STFangsong"));
			fontMapper.put("隶书", PhysicalFonts.get("LiSu"));
			fontMapper.put("Calibri", PhysicalFonts.get("Calibri"));
			mlPackage.setFontMapper(fontMapper);

			os = new java.io.FileOutputStream(pdfPath);

			FOSettings foSettings = Docx4J.createFOSettings();
			foSettings.setWmlPackage(mlPackage);
			Docx4J.toFO(foSettings, os, Docx4J.FLAG_EXPORT_PREFER_XSL);

		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		}
		finally 
		{
			IOUtils.closeQuietly(os);
		}
		System.out.println("OVER");
	
	}

}
*/