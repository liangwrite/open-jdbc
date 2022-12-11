/**
 * 
 */
package cn.fomer.common.service.impl;

import org.w3c.dom.Element;

import cn.fomer.common.service.Node;

import lombok.Getter;

/**
 * Xml 解析器
 *
 * @author Liang
 * @email liang@163.com
 * @date 2022-07-12
 */
@Getter
public class NodeImpl implements Node {
	org.w3c.dom.Node node;
	
	public NodeImpl(org.w3c.dom.Node node) {
		this.node= node;
	}
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		Element element= (Element)node;
		String id = element.getAttribute("id");		
		return id;
	}
	
	@Override
	public String text() {
		// TODO Auto-generated method stub
		if(this.node!=null) {
			return this.node.getTextContent();
		}
		return "";
		/*
		if(this.node!=null&&this.node.getFirstChild().getNodeType()==org.w3c.dom.Node.TEXT_NODE)
		{
			cn.fomer.common.service.Node node = new cn.fomer.common.service.impl.NodeImpl(this.node.getFirstChild());
			return node;
		}
		return null;
		*/
	}

}
