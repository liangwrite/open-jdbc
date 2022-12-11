package cn.fomer.jdbc.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import cn.fomer.jdbc.api.DataBaseService;
import cn.fomer.jdbc.api.TableService;
import cn.fomer.jdbc.clone.service.impl.BiggestTable;
import cn.fomer.jdbc.clone.service.impl.SQLSummary;
import cn.fomer.jdbc.clone.service.impl.TableSummary;
import cn.fomer.jdbc.entity.Item;
import cn.fomer.jdbc.service.SummaryService;

/**
 * 2021-12-10
 * 
 */
public class SummaryServiceImpl implements SummaryService {

	DataBaseService db;
	public SummaryServiceImpl(DataBaseService db) {
		this.db= db;
	}
	
	@Override
	public void toHtmlFile(int top, String path)  {
		// TODO Auto-generated method stub
		String html = this.toHtml(top);
		File toFile= new File(path);
		try {
			if(!toFile.exists()) toFile.createNewFile();
			FileWriter fileWriter= new FileWriter(toFile, true);
			fileWriter.write(html);
			fileWriter.flush();
			fileWriter.close();			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	

	
	volatile int running_thread= 0;
	
	/**
	 * 2018-11-03 使用多线程查询
	 * 
	 */
	SQLSummary queryInThread(final Integer top,final int maxThread)
	{
		
		long start= System.currentTimeMillis();
		
		final SQLSummary vo= new SQLSummary(top);
		vo.setDbVersion(db.getDataBaseVersion());
		vo.setDbType(db.getDbType().toString());
		vo.setTableList(Collections.synchronizedList(new ArrayList<TableSummary>())); //线程安全的List
		List<TableService> tableList = db.getTableList();
		
		final Lock lock= new ReentrantLock(true);
		final Condition mainCondition= lock.newCondition();
		for(final TableService table:tableList)
		{
			TableSummary summaryTable= new TableSummary();
			summaryTable.setTable(table.getName());
			summaryTable.setCount(0);
			vo.getTableList().add(summaryTable);
			//SummaryQuery query= new SummaryQuery(summaryTable, table, top);
			
			//
			Runnable runnable= new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try
					{
//						summaryTable.setRows(0);
						
						try {
							summaryTable.setCount(table.count());
							summaryTable.setRows(table.topInfo(top));
						}
						catch(Exception e) {
							System.err.println(String.format("%s 查询时出错！%s", table.getName(), String.valueOf(e.getMessage())));
						}

						//summaryTable.rows.setResultSet(null);
					}
					catch(Exception e)
					{ 
						e.printStackTrace();
					}
					
					
					lock.lock();
					running_thread--;
					mainCondition.signal();
					lock.unlock();
					
				}
			};
			
			Thread thread1= new Thread(runnable);
			
			// 判断剩余线程够用
			lock.lock();
			if(running_thread>=maxThread)
			{
				// 
				try{ mainCondition.await();}catch(Exception e){}
			}
			thread1.start();
			running_thread++;
			lock.unlock();
			
		} // 循环
		
		
		
		
		
		
		//等待所有线程退出
		while(true)
		{
			lock.lock();
			if(running_thread==0) break;
			
			try{ mainCondition.await();}catch(Exception e){ e.printStackTrace();}
			lock.unlock();	
		}
		
		//3 排序 （由于多线程完成时间不一致，对table按照数量+名称排序）
		for(int i=0;i<vo.getTableList().size()-1;i++)
		{
			TableSummary iTable= vo.getTableList().get(i);
			if(iTable.getCount()==null) iTable.setCount(0);
			
			
			for(int j=i+1;j<vo.getTableList().size();j++)
			{
				
				TableSummary jTable= vo.getTableList().get(j);
				if(jTable.getCount()==null) jTable.setCount(0);
				
				
				//1 比较
				if(iTable.getCount()>jTable.getCount()) {
					continue;
				}
				else if(iTable.getCount()==jTable.getCount()) {
					if(iTable.getTable().compareTo(jTable.getTable())<0) {
						continue;
					}
				}
					
				
					
				//3 对调
				vo.getTableList().set(i, jTable);
				vo.getTableList().set(j, iTable);
				iTable= jTable;
			}
			
		}
		
		
		//4 检查
		System.out.println();
		for(int i=0;i<vo.getTableList().size();i++) {
			//if(i==4)
			//System.out.print(vo.getTableList().get(i).getTable()+", ");
		}
		//System.out.println();
		vo.setMillisecond(System.currentTimeMillis()-start);
		//vo.sqlCount= getCount("");
		return vo;
	}

	/**
	 * 生成简要报告
	 * 函数每次执行估计创建250次连接
	 * 建议 top <= 5;  
	 * 
	 * */
	String toHtml(SQLSummary summary, boolean css)
	{	
		
		String HTML= "<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title>PLACE_HOLDER_TITLE</title><!-- style --></head><body></body></html>";		
		HTML= HTML
				.replace("PLACE_HOLDER_TITLE", "jdbc: "+db.getDbName())
				.replace("<!-- style -->", "<style type=\"text/css\">body{ font-size:12px;} td,th{ padding: 0px 2px 0px 2px; min-width: 30px; white-space: nowrap;}</style>");
		
		List<TableService> tableList = db.getTableList();
		String s= "";
		for(int i=0;i<tableList.size();i++)
		{
			s+= i+"."+tableList.get(i).getName()+" ";
		}
		
		//1 概要
		String text= "<h1>数据库概要</h1>";
		text+= "<strong>查询统计:</strong>&nbsp;"+summary.getMillisecond()+"毫秒&nbsp;&nbsp;"+summary.getSqlCount()+"次"+"<br />";
		text+= "<strong>DataBaseType:</strong>&nbsp;"+db.getDbType().toString()+"<br />";
		text+= "<strong>DataBaseVersion:</strong>&nbsp;"+summary.getDbVersion()+"<br />";		
		text+= "<Strong>DataBaseList: [DATA]</Strong><br /><br />".replace("[DATA]", ""); 
		text+= "<Strong>TableList: </Strong>size=Num<br /><br />".replace("Num", ""+summary.getTableList().size());;
		text+= "[DATA]<br /><br />".replace("[DATA]", s);		
		
	
		
		//2 top 10
		int n= 10;
		BiggestTable biggestTable= new BiggestTable(n);
		String biggestTable_placeHolder= "<strong>【数据量最大TOP10】</strong><br />".replace("TOP10", "TOP"+n);
		text+= biggestTable_placeHolder;
		
		
		//3 表详情
		for(int i=0;i<summary.getTableList().size();i++)
		{		
			//准备数据
			TableSummary summaryTable= summary.getTableList().get(i);
			

			if(summaryTable.getCount()!=null)
			{
				biggestTable.add(summaryTable.getTable(), summaryTable.getCount());
			}
			
			db.log(this, summaryTable.getTable()+" - "+summaryTable.getCount());
			
			
			//3.1 表名
			text+= "<div style=''>tableName</div>"					
					.replace("style=''", css?"style='margin-top:20px; margin-bottom:2px; font-weight:bold;'":"")
					.replace("tableName", (i+1)+".&nbsp;"+summaryTable.getTable());
			
		
			//3.2 显示表数量
			text+= "<div style=''>count=Num</div>"
					.replace("Num", ""+ summaryTable.getCount())
					.replace("style=''", css?"style='font-size:12px; margin-bottom:2px;'":"");
			
			
			//3.3 拼接表详情
			if(summaryTable.getRows()==null)
			{
				text+= "<div>ERROR</div>";
			}
			else
			{
				text+= summaryTable.getRows().toHtml(css);
			}
			
			
		}
		
		//拼接记录前10
		List<Item> itemList= biggestTable.getList();
		String biggistHTML= "";
		
		for(int i=0;i<n;i++)
		{
			if(itemList.get(i).count>0) {
				
				Item item= i<itemList.size()?itemList.get(i):null;
				biggistHTML+= "top #I: #TABLE(#COUNT)<br />"
						.replace("#I", (i+1)+"")
						.replace("#TABLE", item==null?"":itemList.get(i).tname)
						.replace("(#COUNT)", item==null?"":"("+itemList.get(i).count+")")
						;
			}
			
		}
		text= text.replace(biggestTable_placeHolder, biggestTable_placeHolder+biggistHTML);
		
		//拼接BODY
		text=HTML.replace("<body></body>", "<body>"+text+"</body>");
		
		
		//System.out.println(getClass().getSimpleName()+": time - "+(System.currentTimeMillis()-start)/1000+"s");
		return text;
	}

	@Override
	public String toHtml(int top) {
		// TODO Auto-generated method stub
		int thread= 4;
		SQLSummary sqlSummary= queryInThread(top, thread);
		return toHtml(sqlSummary, true);
	}

	@Override
	public String toHtml() {
		// TODO Auto-generated method stub
		return this.toHtml(5);
	}

	@Override
	public String toText(int top) {
		// TODO Auto-generated method stub
		//int thread= 4;
		//SQLSummary sqlSummary= queryInThread(4, thread);
		//return this.summaryToHtml(top, false);
		return null;
	}

	@Override
	public String toText() {
		// TODO Auto-generated method stub
		return this.toText(10);
	}

}
