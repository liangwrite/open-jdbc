package cn.fomer.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 2019-06-11 摇号随机序列
 * 
 * 
 */
public class RandomSet {
	//
	int index;
	List<Integer> list= new ArrayList<>();
	public RandomSet(int size)
	{
		this.index= size;
		do
		{
			Integer r= new Random().nextInt(size);
			if(!list.contains(r))
			{
				list.add(r);
			}
		}
		while(list.size()<size);
		
		System.out.println(list.toString());

	}
	
	public void reset(int size)
	{
		this.index= size;
		do
		{
			Integer r= new Random().nextInt(size);
			if(!list.contains(r))
			{
				list.add(r);
			}
		}
		while(list.size()<size);
		
		System.out.println(list.toString());
		
	}
	
	/**
	 * 
	 * 2019-06-11 ȡ��һ����
	 *
	 */
	public int remove()
	{
		if(index>0) return list.get(--index);
		
		return list.get(0);
	}
	
	/**
	 * 
	 * 2019-06-11 ��ʣ������
	 *
	 */
	public int getSize()
	{
		return index+1;
	}
	
	public boolean isLast()
	{
		return index==0?true:false;
	}

	public static void main(String[] args) {
		//
		RandomSet randomList= new RandomSet(10);
		for(int i=0;!randomList.isLast();i++)
		{
			System.out.print(randomList.remove()+",");
		}
	}

}
