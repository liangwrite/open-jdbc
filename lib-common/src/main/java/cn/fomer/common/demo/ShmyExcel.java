/**
 * 
 */
package cn.fomer.common.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import cn.fomer.common.http.HttpClientUtil;
import cn.fomer.common.office.IExcel;
import cn.fomer.common.office.impl.ExcelImpl;

/**
 * 2022-04-21 上海脉云
 * 
 */
public class ShmyExcel {

	public static void main(String[] args) throws Exception {
		//0
		int rowStart=2;
		int rowEnd=18;
		int columnOne=1;
		int columnTwo=3;
		int columnThree= 4;
		String dictIdShebei= "3e3cef68a43011ecbd6ac4346b4b418e";
		String dictIdPeijian= "94155258a42711ecbd6ac4346b4b418e";
		String dictIdHaocai= "39c24df6a42811ecbd6ac4346b4b418e";
		String[] dictList= {dictIdShebei, dictIdPeijian, dictIdHaocai};
		
		List<List<Integer>> borderList= new ArrayList<>();
		borderList.add(Arrays.asList(2,12));
		borderList.add(Arrays.asList(13,17));
		borderList.add(Arrays.asList(18,18));
		
		
		
		//1
		IExcel excel= new ExcelImpl("E:/Users/Liang/Desktop/data.xlsx");
		List<String[]> rowList = excel.read();
		
		MultilevelAddVO root= new MultilevelAddVO();
		//0
		for(int level0=0; level0<borderList.size(); level0++) {
			
			String parentId= "0";
			String dictId= dictList[level0];

			
			//1
			List<Integer> border0 = borderList.get(level0);
			for(int level1Row=border0.get(0);level1Row<=border0.get(border0.size()-1);level1Row++) {
				String[] row= rowList.get(level1Row-1);
				String name= row[columnTwo-1];

				MultilevelAddVO level1Vo= new MultilevelAddVO();
				level1Vo.setDictId(dictId);
				level1Vo.setLevel(1);
				level1Vo.setParentId("0");
				level1Vo.setName(name);
				String level1Id = "";
				try {
					level1Id= save(level1Vo);
				}
				catch(Exception e) {
					System.err.println("level1Row="+level1Row);
					
					throw e;
				}
				
				
				//2
				String level3nameString= row[columnThree-1];
				String[] level3nameArray = level3nameString.split("、");
				for(int i=0;i<level3nameArray.length;i++) {
					String name3= level3nameArray[i];
					MultilevelAddVO level2Vo= new MultilevelAddVO();
					level2Vo.setDictId(dictId);
					level2Vo.setLevel(2);
					level2Vo.setParentId(level1Id);
					level2Vo.setName(name3);
					
					String level2Id = "";
					try {
						level2Id= save(level2Vo);
					}
					catch(Exception e) {
						System.err.println("level1Row="+level1Row);
						System.err.println("第几个分词="+i);
						
						throw e;
					}					
				}
			}
			
			
		}
		
		System.out.println("success");
		
	}
	static String token= "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsic3lzdGVtIiwiZGF0YXZpc2F1bHNlcnZpY2UiLCJsb2ciLCJkYXRhZWFzZS1zZXJ2aWNlIiwiZHluZm9ybWRic2VydmljZSIsInVzZXIiLCJkZW1vIiwiZ2NvcCIsImticyIsImZpbmFuY2UiLCJkeW5yZXBvcnRzZXJ2aWNlIl0sInVzZXJfbmFtZSI6IntcInRlbmFudF9pZFwiOlwiMDAwMDAwXCIsXCJ1c2VyX2lkXCI6XCJkNjU2ZmE0N2FjN2JkZTdlN2VhOWJiMjM0YmU1YjRmMVwiLFwicm9sZV9pZFwiOlwiYTM2NDk3MThjZGQyN2Q1OGIwMDYzNDBjMGQ2NTEzYjgsYzZiYzBhOTFmMDgzNmY0OGM2ZDcwMjJlNGU3NDZlN2MsZTA3OTllMTFiNDZmNGY3ZjRlOTFkMjNmOWMyNWQzNDZcIixcInVzZXJfbmFtZVwiOlwi6LaF57qn566h55CG5ZGYXCIsXCJhY2NvdW50XCI6XCJjaGFvamkwMDFcIn0iLCJzY29wZSI6WyJST0xFX0FETUlOIiwiUk9MRV9VU0VSIiwiUk9MRV9BUEkiXSwiZXhwIjoxNjUwNTgwMTAxLCJhdXRob3JpdGllcyI6WyJ1c2VyX3JvbGVAZDY1NmZhNDdhYzdiZGU3ZTdlYTliYjIzNGJlNWI0ZjEiLCJhZG1pbmlzdHJhdG9yIiwiZGVmYXVsdF82MTg2NzhmYi1jNzVmLTRjNWUtOTU4NS1kODVhZjE5YTYwY2MiXSwianRpIjoiZGQ4MjMwYWMtMDg1YS00ODE3LTllZjItMDIwNDIyOThlOWQ4IiwiY2xpZW50X2lkIjoic3pqal9jbGllbnQifQ.zcQsDq7r6I-i4mdvOe5sfhN_2VQIG2M-vNy3dsz5z3Q";
	static String url= "http://localhost:18106/V3.0/multileveldict/save";
	static Gson gson= new Gson();
	
	static String save(MultilevelAddVO level1Vo) throws Exception {
		//
		Map<String, String> header= new HashMap<String, String>();
		header.put("Authorization", "bearer "+token);
		header.put("Content-Type", "application/json;charset=UTF-8");
		
		//
		HttpClientUtil http= new HttpClientUtil();
		String post = http.jsonPost(url, gson.toJson(level1Vo), header);
		R r = gson.fromJson(post, R.class);
		if(r.isSuccess()) {
			return r.getData().toString();
		}
		
		System.err.println(gson.toJson(level1Vo));
		System.err.println(post);
		throw new RuntimeException("异常终止！");
	}
	
	
}
