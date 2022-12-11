package cn.fomer.common.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import cn.fomer.common.service.Xml;

import lombok.Getter;

/**
 * 2019-01-18 JDK自带
 * 
 * 
 */
@Getter
public class XmlImpl implements Xml {
	
	File file;
	Map<String,cn.fomer.common.service.Node> cacheMap= new HashMap<>();
	
	Document document;
	Element rootElement;
	
	public XmlImpl(Class<?> clzz, String shortFileName)
	{
		try
		{
			analysis(clzz, shortFileName);
		}
		catch(Exception e)
		{
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public XmlImpl(String resourcePath)
	{
		try
		{
			analysis(resourcePath);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public XmlImpl(File f)
	{
		this.file= f;
		try
		{
			if(f.exists()) {
				FileInputStream inputStream= new FileInputStream(f);
				analysis(inputStream);
				inputStream.close();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 2022-01
	 */
	void analysis(String shortFileName) throws IOException{
		//ClassPathResource resource= new ClassPathResource(shortFileName);
		//InputStream inputStream = resource.getInputStream();
		InputStream inputStream = null;
		try {
			inputStream = Thread.currentThread().getContextClassLoader().getResource(shortFileName).openStream();
		}
		catch(Exception e) {
			e.printStackTrace();
			System.err.println("可能文件不存在！"+shortFileName);
		}
		

		try {
			analysis(inputStream);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			inputStream.close();
		}
	}
	
	void analysis(Class<?> clazz, String shortFileName) throws ParserConfigurationException, SAXException, IOException{

		InputStream inputStream = clazz.getResourceAsStream(shortFileName);		
		try {
			analysis(inputStream);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			inputStream.close();
		}
	}
	
	/**
	 * 2021-03-15
	 * @param clazz 文件所在包
	 * @params shortFileName 短文件名称
	 */
	void analysis(InputStream inputStream) throws ParserConfigurationException, SAXException, IOException
	{
		DocumentBuilderFactory documentBuilderFactory= DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder= documentBuilderFactory.newDocumentBuilder();
		this.document= documentBuilder.parse(inputStream);
		inputStream.close();
		
		
		//

		
		//
		this.rootElement = document.getDocumentElement();
		NodeList nodeList = this.rootElement.getChildNodes(); //这里会有很杂的项
		
		for(int i=0;i<nodeList.getLength();i++)
		{
			Node node = nodeList.item(i);
			/*
			System.out.println(String.format("Node%s NodeType=%s NodeName=%s Class=%s", 
					Integer.toString(i), XmlImpl.convertDictType(node.getNodeType()), node.getNodeName()
					,
					node.getClass().getName())
			);
			*/
			//System.out.println(String.format("\t%s=[%s]", Integer.toString(i), node.getTextContent()));
			String nodeName = node.getNodeName(); //"select
			String nodeValue = node.getNodeValue(); //只有当类型为文本节点时才有意义
			short nodeType = node.getNodeType();
			
			if(node.getNodeType()==Node.ELEMENT_NODE)
			{
				Element element= (Element)node;
				String id = element.getAttribute("id");
				
				
				if(id!=null)
				{
					cacheMap.put(id, new cn.fomer.common.service.impl.NodeImpl(element));
				}
			}
			
		}		
	}
	
	static String convertDictType(short n) {
		switch(n) {
		case 1:
			return "ELEMENT_NODE";
		case 3:
			return "TEXT_NODE    ";
		case 8:
			return "COMMENT_NODE";
		default:
			return Short.toString(n);
		}
	}
	public cn.fomer.common.service.Node getByProp(String id, String val)
	{
		if("id".equals(id))
		{
			return getById(val);
		}
		
		return null;
	}
	


	@Override
	public Map<String, cn.fomer.common.service.Node> getAllNode() {
		// TODO Auto-generated method stub
		return this.cacheMap;
	}

	@Override
	public boolean existNode(String nodeId) {
		// TODO Auto-generated method stub
		if(cacheMap.containsKey(nodeId)) {
			return true;
		}
		return false;
	}

	@Override
	public void add(cn.fomer.common.service.Node n) {
		// TODO Auto-generated method stub
		Node srcNode = n.getNode();
		Element srcElement= (Element)srcNode;
		
		Node newNode = this.rootElement.getOwnerDocument().importNode(srcElement, true);
		Node appendChild = this.rootElement.appendChild(newNode);
	}
	
	void cloneNode(Node srcNode) {
		//属性
		NamedNodeMap attrMap = srcNode.getAttributes();
		for(int i=0;i<attrMap.getLength();i++) {
			Node item = attrMap.item(i);
			System.out.println(item.getNodeName()+", "+item.getNodeValue());
		}
		
		//子节点
	}
	
	void cloneNodeDeep(Node srcNode) {
		//属性
		NamedNodeMap attrMap = srcNode.getAttributes();
		for(int i=0;i<attrMap.getLength();i++) {
			Node item = attrMap.item(i);
			System.out.println(item.getNodeName()+", "+item.getNodeValue());
		}
		
		//子节点
	}
	

	@Override
	public void addLine() {
		// TODO Auto-generated method stub
		Text newTextNode = this.rootElement.getOwnerDocument().createTextNode("\n");
		//newTextNode.setTextContent("    ");
		this.rootElement.appendChild(newTextNode);
	}
	
	@Override
	public void addCommit(String text) {
		// TODO Auto-generated method stub
		Comment newComment = this.rootElement.getOwnerDocument().createComment("复制");
		this.rootElement.appendChild(newComment);
	}
	
	@Override
	public void saveAs(String path){
		// TODO Auto-generated method stub
		String s= "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">";
//		DocumentType createDocumentType = this.document.getImplementation().createDocumentType("", "", "");
		//this.document.insertBefore(createCDATASection, this.rootElement);
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "-//mybatis.org//DTD Mapper 3.0//EN");

			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "http://mybatis.org/dtd/mybatis-3-mapper.dtd");
			transformer.transform(new DOMSource(this.document), new StreamResult(path));
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}

	}

	@Override
	public void save() {
		// TODO Auto-generated method stub
		this.saveAs(file.getPath());
	}
	
	
	public cn.fomer.common.service.Node getById(String id)
	{
		/*
		Node element = cacheMap.get(id).getNode();
		if(element!=null&&element.getFirstChild().getNodeType()==Node.TEXT_NODE)
		{
			cn.fomer.common.service.Node node = new cn.fomer.common.service.impl.NodeImpl(element.getFirstChild());
			return node;
		}
		return null;
		*/
		return cacheMap.get(id);
	}	




	
	/**
	 * 
	 * 2021-03-12 节点下的文本也是一个节点 node.getFirstChild().getNodeValue()
	 */
	/*
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException
	{
		XmlReader reader= new XmlReader(Dialect.class, "dialect.xml");
		String searchById = reader.id("oracle_queryField");
		System.out.println(searchById);
	}
	*/
}
