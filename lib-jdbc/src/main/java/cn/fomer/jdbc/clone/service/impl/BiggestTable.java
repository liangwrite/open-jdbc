package cn.fomer.jdbc.clone.service.impl;

import java.util.ArrayList;
import java.util.List;

import cn.fomer.jdbc.entity.Item;

import lombok.Getter;

/**
 * 20180928 用于保存最大的几个表名
 * 
 * 
 * */
@Getter
public class BiggestTable
{
	int top= 10;
	
	List<Item> list= new ArrayList<Item>();
	
	
	public BiggestTable(int top)
	{
		this.top= top;
	}
	
	/**
	 * 有并列的不要（大 -》 小）
	 * 
	 * */
	public void add(String tname,int count)
	{
		Item newItem= new Item(tname, count);
		for(int i=0;i<list.size();i++)
		{
			Item oldItem= list.get(i);
			if(newItem.count>oldItem.count)
			{
				//每次插入时，移除最后一个
				list.add(i, newItem);
				if(list.size()>top)
				{
					//list.remove(list.size()-1);						
				}
				return;					
			}
			
		}
		
		list.add(newItem);
	}
	

}
