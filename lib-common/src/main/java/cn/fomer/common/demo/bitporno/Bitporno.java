package cn.fomer.common.demo.bitporno;

import java.io.IOException;

/**
 * @author Liang 2019-06-07
 *
 */
public interface Bitporno {
	//
	BitpornoPage prevPage();
	BitpornoPage nextPage();
	BitpornoPage goPageNo(int pageNo);
	int getMaxPage();
	int getPageNo();
	
	String baseUrl= "https://www.bitporno.com"; //末尾不要加/
	String baseSearchUrl= "https://www.bitporno.com/?q=";
	boolean log= true;
}
