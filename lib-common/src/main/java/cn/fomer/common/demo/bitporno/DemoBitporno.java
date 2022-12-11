package cn.fomer.common.demo.bitporno;

import cn.fomer.common.demo.bitporno.impl.BitpornoImpl;

/**
 * @author Liang 2019-06-07
 *
 */
public class DemoBitporno {
	
	//
	public static void main(String[] args) throws Exception {
		//
		Bitporno bitporno= new BitpornoImpl(null);
		for(int i=0;i<bitporno.getMaxPage();i++)
		{
			System.out.println("第"+(i+1)+"页: ");
			
		}
	}

}
