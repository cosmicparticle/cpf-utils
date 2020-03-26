package cho.carbon.hc.copframe.utils.xml;

public class ValueUnprovidedExcepetion extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8961944207241670070L;

	public ValueUnprovidedExcepetion(String msg) {
		super(msg);
	}

	public ValueUnprovidedExcepetion(XmlNode node, String attrName) {
		this(node.getPath() + "没有找到属性[" + attrName + "]");
	}

	public ValueUnprovidedExcepetion(XmlNode node, String tagName,
			boolean isEle) {
		this(node.getPath() + "没有找到子标签[" + tagName + "]");
	}
	
	
}
