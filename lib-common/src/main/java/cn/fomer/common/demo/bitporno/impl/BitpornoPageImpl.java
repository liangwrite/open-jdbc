package cn.fomer.common.demo.bitporno.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.fomer.common.demo.bitporno.Bitporno;
import cn.fomer.common.demo.bitporno.BitpornoPage;
import cn.fomer.common.demo.bitporno.entity.ItemVO;


/**
 * 2019-05-12
 * 
 * 
 */
public class BitpornoPageImpl implements BitpornoPage {
	//
	Map<Integer, String> pageMap= new HashMap<Integer, String>();
	List<ItemVO> itemList= new ArrayList<>();
	int pageNo;
	public BitpornoPageImpl(String queryUrl) throws IOException 
	{
		// TODO Auto-generated constructor stub
		if(Bitporno.log) System.out.println("queryUrl="+queryUrl);
		Document doc= Jsoup.connect(queryUrl).validateTLSCertificates(false).timeout(60*1000).get(); //忽视SSL错误

		Elements elementCollection= doc.select("img.small_thumbnail");	


		for(int i=0;i<elementCollection.size();i++) 
		{
			Element imgElement= elementCollection.get(i);
			
			String thumbnailImg= imgElement.attr("src");
			
			Element parentElement= imgElement.parent();
			String href= parentElement.attr("href");
			String pageUrl= "https://www.bitporno.com/"+href;
			
			if(Bitporno.log) System.out.println(i+" "+pageUrl);
			//VideoPage videoPage= new VideoPageImpl(pageUrl, thumbnailImg);	
			//itemList.add(videoPage);
			ItemVO itemVO= new ItemVO();
			itemVO.image= thumbnailImg;
			itemVO.pageUrl= pageUrl;
			itemList.add(itemVO);
		}
		
		//分页信息
		Elements pageElementCollection= doc.select("a.pages");
		for(Element a:pageElementCollection)
		{
			
			Integer pageNo= null;
			try{ pageNo= Integer.valueOf(a.text());} catch(Exception e){ continue;}
			String url= Bitporno.baseUrl+ a.attr("href");
			pageMap.put(pageNo, url);
			if(a.hasClass("pages-active"))
			{
				this.pageNo= pageNo;
			}
		}
		
	}



	@Override
	public int getTotalPage() {
		// TODO Auto-generated method stub
		return itemList.size();
	}

	@Override
	public List<ItemVO> getItemList() {
		// TODO Auto-generated method stub
		return itemList;
	}
	//

	@Override
	public String getImage(int n) {
		// TODO Auto-generated method stub
		return itemList.get(n).image;
	}



	@Override
	public void openPage(int n) throws IOException {
		// TODO Auto-generated method stub
		itemList.get(n).videoPage= new BitpornoVideoPageImpl(itemList.get(n).pageUrl, itemList.get(n).image);
	}







	@Override
	public int getPageNo() {
		// TODO Auto-generated method stub
		return pageNo;
	}


}
