package cho.carbon.hc.copframe.utils.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultElement;

import cho.carbon.hc.copframe.utils.FormatUtils;
import cho.carbon.hc.copframe.utils.TextUtils;
/**
 * 
 * <p>Title: Dom4jNode</p>
 * <p>Description: </p><p>
 * XmlNode的Dom4j的实现类
 * </p>
 * @author Copperfield Zhang
 * @date 2017年3月9日 下午9:16:30
 */
public class Dom4jNode implements XmlNode{

	private final Element _thisElement;
	
	private List<Dom4jNode> elements = new ArrayList<Dom4jNode>();
	
	Dom4jNode parent;
	
	static final SAXReader reader = new SAXReader();
	
	static Element toRoot(InputStream inp) throws XMLException {
		Document document;
		try {
			document = reader.read(inp);
		} catch (DocumentException | NullPointerException e) {
			throw new XMLException(e);
		}
		return document.getRootElement();
	}
	
	static InputStream toInputStram(String xmlStr, String charset) throws UnsupportedEncodingException {
		return new ByteArrayInputStream(xmlStr.getBytes(FormatUtils.coalesce(charset, "utf-8")));
	}
	
	/**
	 * 直接创建一个空的xml元素
	 */
	public Dom4jNode() {
		this(new DefaultElement("xml"));
	}
	
	public Dom4jNode(Element element) {
		this(element, null);
	}
	
	Dom4jNode(Element element, Dom4jNode parent) {
		_thisElement = element;
		this.parent = parent;
		@SuppressWarnings("unchecked")
		List<Element> children = element.elements();
		if(children != null && children.size() > 0){
			for (Element child : children) {
				Dom4jNode childNode = new Dom4jNode(child, this);
				elements.add(childNode);
			}
		}
	}
	
	public Dom4jNode(String xmlStr) throws XMLException, UnsupportedEncodingException {
		this(xmlStr, null);
	}
	
	public Dom4jNode(String xmlStr, String charset) throws XMLException, UnsupportedEncodingException{
		this(toRoot(toInputStram(xmlStr, charset)));
	}
	
	public Dom4jNode(InputStream inp) throws IOException, XMLException {
		this(toRoot(inp));
	}

	@Override
	public String getTagName() {
		return _thisElement.getName();
	}

	@Override
	public String getAttribute(String attrName) {
		return getAttribute(attrName, false);
	}
	
	@Override
	public String getStrictAttribute(String attrName)
			throws ValueUnprovidedExcepetion {
		return getAttribute(attrName, true);
	}
	
	public String getAttribute(String attrName, boolean strict) throws ValueUnprovidedExcepetion{
		String value = _thisElement.attributeValue(attrName);
		if(value == null && strict){
			throw new ValueUnprovidedExcepetion(this, attrName);
		}
		return value;
	}
	
	@Override
	public String getText() {
		return _thisElement.getTextTrim();
	}
	
	@Override
	public String getStrictText() throws ValueUnprovidedExcepetion {
		String text = getText();
		if(TextUtils.hasText(text)){
			return text;
		}
		throw new ValueUnprovidedExcepetion(this.getPath() + "的内容不能为空");
	}
	
	@Override
	public XmlNode getFirstElement(String tagName) {
		if(tagName != null && !tagName.isEmpty()){
			for (Dom4jNode ele : elements) {
				if(tagName.equals(ele.getTagName())){
					return ele;
				}
			}
		}
		return null;
	}
	@Override
	public XmlNode getStrictFirstElement(String tagName)
			throws ValueUnprovidedExcepetion {
		XmlNode ele = getFirstElement(tagName);
		if(ele != null){
			return ele;
		}
		throw new ValueUnprovidedExcepetion(this, tagName, true);
	}

	@Override
	public XmlNode getFirstElement(String attrName, String attrValue) {
		if(attrName != null && attrName != null){
			for (Dom4jNode ele : elements) {
				if(attrValue.equals(ele.getAttribute(attrName))){
					return ele;
				}
			}
		}
		return null;
	}
	
	@Override
	public List<XmlNode> getElements(String attrName, String attrValue) {
		List<XmlNode> result = new ArrayList<XmlNode>();
		if(attrName != null && attrName != null){
			for (Dom4jNode ele : elements) {
				if(attrValue.equals(ele.getAttribute(attrName))){
					result.add(ele);
				}
			}
		}
		return result;
	}

	@Override
	public List<XmlNode> getElements() {
		return new ArrayList<XmlNode>(elements);
	}

	@Override
	public List<XmlNode> getElements(String tagName) {
		List<XmlNode> result = new ArrayList<XmlNode>();
		if(tagName != null && !tagName.isEmpty()){
			for (Dom4jNode ele : elements) {
				if(tagName.equals(ele.getTagName())){
					result.add(ele);
				}
			}
		}
		return result;
	}

	@Override
	public XmlNode getParent() {
		return parent;
	}
	
	@Override
	public String asXML() {
		return _thisElement.asXML();
	}
	
	@Override
	public String getFirstElementText(String tagName) {
		XmlNode node = getFirstElement(tagName);
		if(node != null){
			return node.getText();
		}
		return null;
	}
	
	@Override
	public XmlNode addNode(String tagName) {
		Element ele = _thisElement.addElement(tagName);
		Dom4jNode node = new Dom4jNode(ele, this);
		elements.add(node);
		return node;
	}
	
	@Override
	public XmlNode addNodeWithCDATA(String tagName, String cdata) {
		XmlNode node = addNode(tagName);
		node.addCDATA(cdata);
		return node;
	}
	
	@Override
	public XmlNode setText(String text) {
		_thisElement.setText(text);
		return this;
	}
	
	@Override
	public XmlNode addCDATA(String cdata) {
		_thisElement.addCDATA(cdata);
		return this;
	}
	
	@Override
	public XmlNode setCDATA(String cdata) {
		this.empty().addCDATA(cdata);
		return this;
	}
	
	@Override
	public XmlNode empty() {
		_thisElement.clearContent();
		return this;
	}
	
	@Override
	public XmlNode setAttribute(String attrName, String attrValue) {
		_thisElement.addAttribute(attrName, attrValue);
		return this;
	}
	
	@Override
	public LinkedHashMap<String, String> toTagTextMap() {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		this.getElements().forEach((ele) -> {
			if(!map.containsKey(ele.getTagName())){
				map.put(ele.getTagName(), ele.getText());
			}
		});
		return map;
	}
	
	@Override
	public String toString() {
		return asXML();
	}
	
	@Override
	public String getPath(){
		return _thisElement.getPath();
	}

}
