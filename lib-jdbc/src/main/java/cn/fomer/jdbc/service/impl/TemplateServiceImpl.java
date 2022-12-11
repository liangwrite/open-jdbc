/**
 * 
 */
package cn.fomer.jdbc.service.impl;

import java.util.List;

import cn.fomer.common.java.DateHelper;
import cn.fomer.common.service.Xml;
import cn.fomer.common.service.impl.XmlImpl;
import cn.fomer.common.util.StringHelper;
import cn.fomer.jdbc.api.FieldService;
import cn.fomer.jdbc.api.TableService;
import cn.fomer.jdbc.api.TemplateService;
import cn.fomer.jdbc.entity.ColumnTypeEnum;
import lombok.Setter;

/**
 * 2022-01-20 创建模板
 * 
 */
@Setter
public class TemplateServiceImpl implements TemplateService {

	TableService table;
	
	String author;
	
	/**
	 * 
	 */
	public TemplateServiceImpl(TableService table) {
		// TODO Auto-generated constructor stub
		this.table= table;
	}
	
	
	
	@Override
	public void mapEntity() {
		// TODO Auto-generated method stub
		
		//System.out.println(tableName);
		//System.out.println(getJavaName());
		
		
		List<FieldService> fieldList = table.getFieldList();
		
		
		Xml xml= new XmlImpl(getClass(), "XD.xml");
		String classText = xml.getById("CLASS").getNode().getTextContent();
		
		String idText = xml.getById("ID").getNode().getTextContent();
		String fieldText = xml.getById("FIELD").getNode().getTextContent();
		String dateText = xml.getById("DATE_FIELD").getNode().getTextContent();
		
		StringBuffer buffer= new StringBuffer();
	
		
		
		//1 fieldList
		fieldList.forEach(field->{
			

			if(field.getIsPrimaryKey()) {
				buffer.append(
					idText
						.replace("{comments}", StringHelper.ifNull(field.getComment(), ""))
						.replace("{field}", field.getCode())
						.replace("{class}", field.getJavaType())
						.replace("{camel}", field.getCodeCamel())
				);
			}
			else if(field.getColumnType()==ColumnTypeEnum.ORACLE_DATE) {
				buffer.append(
					dateText
						.replace("{comments}", StringHelper.ifNull(field.getComment(), ""))
						.replace("{field}", field.getCode())
						.replace("{class}", field.getJavaType())
						.replace("{camel}", field.getCodeCamel())
						);
			}
			else {
				buffer.append(
					fieldText
						.replace("{comments}", StringHelper.ifNull(field.getComment(), ""))
						.replace("{field}", field.getCode())
						.replace("{class}", StringHelper.ifNull(field.getJavaType(), "??"))
						.replace("{camel}", field.getCodeCamel())
						);
			}

			
		});

		
		//2 class
		String outString= 
		(
			classText
				.replace("{tablename}", table.getName())
				.replace("{tablecomment}", StringHelper.ifNull(table.getComment(), ""))
				.replace("{author}", "Liang")
				.replace("{classname}", table.getJavaCamelName())
				.replace("{date}", DateHelper.toFormate("yyyy-MM-dd"))
				.replace("{fieldList}", buffer.toString())
		);	
		
		System.out.println(outString);
				
	}
	

	@Override
	public void mapMapper() {
		// TODO Auto-generated method stub
		List<FieldService> fieldList = table.getFieldList();
		for(FieldService fieldDetail: fieldList) {
			String text= "<result property=\"{field}\" column=\"{column}\"/>";
			
			System.out.println(
				text
					.replace("{field}", fieldDetail.getCodeCamel())
					.replace("{column}", fieldDetail.getCode())
			);
		}
	}

	@Override
	public void mapService() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mapController() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mapMapperXml() {
		// TODO Auto-generated method stub
		
	}

}
