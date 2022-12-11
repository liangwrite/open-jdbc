package cn.fomer.common.demo.bitporno.impl;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.fomer.common.demo.bitporno.Bitporno;
import cn.fomer.common.demo.bitporno.BitpornoVideoPage;
import cn.fomer.common.demo.bitporno.entity.ItemVO;

/**
 * @author Liang 2019-06-07
 *
 */
public class BitpornoVideoPageImpl implements BitpornoVideoPage {
	//
	String videoPageUrl;
	String img;
	
	//
	String posterUrl;
	String map4Url;
	public BitpornoVideoPageImpl(String videoPageUrl,String image) throws IOException
	{
		if(Bitporno.log) System.out.println("videoPageUrl="+videoPageUrl);
		
		this.videoPageUrl= videoPageUrl;
		this.img= image;
		
		//
		Document doc= Jsoup.connect(videoPageUrl).validateTLSCertificates(false).get(); //忽视SSL错误
		Elements elementCollection= doc.select("video#videojs");
		Element videoElement= elementCollection.get(0);
		Element sourceElement= videoElement.children().select("source").get(0);
		
		posterUrl= videoElement.attr("poster");
		map4Url= sourceElement.attr("src");
		
		System.out.println("poster="+posterUrl+" mp4="+map4Url);
	}
	@Override
	public String getMp4() {
		// TODO Auto-generated method stub
		return this.map4Url;
	}
	@Override
	public String getPostImage() {
		// TODO Auto-generated method stub
		return this.posterUrl;
	}
	
	
}
