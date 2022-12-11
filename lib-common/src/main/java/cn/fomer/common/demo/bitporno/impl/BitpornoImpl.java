package cn.fomer.common.demo.bitporno.impl;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.fomer.common.demo.bitporno.Bitporno;
import cn.fomer.common.demo.bitporno.BitpornoPage;

/**
 * @author Liang 2019-06-07
 *
 */
public class BitpornoImpl implements Bitporno {
	//
	Map<Integer, BitpornoPage> pageMap= new HashMap<>();
	Map<Integer, String> urlMap= new HashMap<>();
	
	int maxPage= 0;
	
	
	BitpornoPage currentBitpornoPage;
	public BitpornoImpl(String keyword) throws IOException
	{
		//
		
		String queryUrl= null;
		if(keyword==null||keyword.trim().length()==0)
		{
			queryUrl= baseUrl;
		}
		else
		{
			queryUrl= baseSearchUrl+URLEncoder.encode(keyword.trim(),"utf-8");
		}
		if(log) System.out.println("keyword="+keyword);
		//
		BitpornoPage bitpornoPage= new BitpornoPageImpl(queryUrl);
		this.maxPage= bitpornoPage.getTotalPage();
		this.urlMap.putAll(((BitpornoImpl)bitpornoPage).urlMap);
	}

	

	@Override
	public int getMaxPage() {
		// TODO Auto-generated method stub
		Hashtable<String, String> s;
		Dictionary<String, Object> a;
		Map<String, Object> syncMap= new ConcurrentHashMap<>();
		return maxPage;
	}

	@Override
	public BitpornoPage goPageNo(int pageNo) {
		// TODO Auto-generated method stub
		//String queryUrl= pageMap.get(pageNo);
		//http(queryUrl);
		//已经打开过
		BitpornoPage one= pageMap.get(pageNo);
		if(one!=null)
		{
			currentBitpornoPage= one;
		}
		else
		{
		}
		return currentBitpornoPage;
	}


	public String getQueryUrl() {
		return baseSearchUrl;
	}


	@Override
	public BitpornoPage prevPage() {
		// TODO Auto-generated method stub
		if(currentBitpornoPage.getPageNo()==1) return currentBitpornoPage;
		//之前已经缓存过了
		BitpornoPage prev= pageMap.get(currentBitpornoPage.getPageNo()-1);
		if(prev!=null) return prev;
		//没有缓存过
		return null;
	}


	@Override
	public BitpornoPage nextPage() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public int getPageNo() {
		// TODO Auto-generated method stub
		return currentBitpornoPage.getPageNo();
	}




}
