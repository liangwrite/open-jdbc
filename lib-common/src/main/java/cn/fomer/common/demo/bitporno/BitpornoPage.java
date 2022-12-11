package cn.fomer.common.demo.bitporno;

import java.io.IOException;
import java.util.List;

import cn.fomer.common.demo.bitporno.entity.ItemVO;

/**
 * 2019-05-12
 * 
 * 
 */
public interface BitpornoPage {
	//
	int getTotalPage();
	List<ItemVO> getItemList();
	String getImage(int n);
	void openPage(int n) throws IOException;

	int getPageNo();
}
