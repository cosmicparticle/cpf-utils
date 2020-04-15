package cho.carbon.hc.copframe.utils.xml;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cho.carbon.hc.copframe.utils.FormatUtils;

/**
 * 
 * <p>Title: XMLConverter</p>
 * <p>Description: </p><p>
 * 将对象的字段转换成XmlNode对象，当前只能转换一层，无法嵌套转换
 * </p>
 * @author Copperfield Zhang
 * @date 2017�?3�?9�? 下午9:17:17
 * @since 2017�?3�?9�? 下午9:17:17
 * @param <T>
 */
public class XMLConverter<T> {
	
	Logger logger = LoggerFactory.getLogger(XMLConverter.class);
	
	public XmlNode doConvert(T obj) throws XMLException{
		return doConvert(obj, new XMLConvertConfig());
	}
	
	/**
	 * 
	 * @param obj
	 * @param ignoreRequired
	 * @param ignoreExceedLength
	 * @return
	 * @throws XMLException
	 */
	public XmlNode doConvert(T obj, XMLConvertConfig config) throws XMLException{
		XmlNode node = new Dom4jNode();
		Class<?> objClass = obj.getClass();
		//获得�?有字�?
		Map<String, Field> fieldMap = getAllFieldMap(objClass);
		StringBuffer nullFields = new StringBuffer();
		StringBuffer exceedLengthFields = new StringBuffer();
		fieldMap.forEach((key, field) -> {
			XMLTag tagAnno = field.getDeclaredAnnotation(XMLTag.class);
			if(tagAnno != null && tagAnno.ignored()){
				return;
			}
			String fieldName = field.getName();
			Method getter = getGetter(fieldName, objClass);
			if(getter != null){
				Object fieldValue = null;
				try {
					fieldValue = getter.invoke(obj);
				} catch (Exception e1) {
				}
				String value = FormatUtils.toString(fieldValue);
				String tagName = key;
				boolean error = false;
				if(tagAnno != null){
					if(!config.ignoreRequred(fieldName) && tagAnno.required() && value == null){
						nullFields.append(fieldName + ",");
						error = true;
					}
					if(!config.isIgnoreExceedLength() && tagAnno.lengthLimit() > 0 && value != null && value.length() > tagAnno.lengthLimit()){
						exceedLengthFields.append(fieldName + ",");
						error = true;
					}
				}
				if(!error){
					if(value == null){
						node.addNode(tagName);
					}else{
						node.addNodeWithCDATA(tagName, value);
					}
				}
			}
		});
		String errMsg = "";
		if(nullFields.length() > 0){
			errMsg += "必需字段[" + nullFields + "]为null";
		}
		if(exceedLengthFields.length() > 0){
			errMsg += "字段[" + exceedLengthFields + "]的长度超出规�?";
		}
		if(!errMsg.isEmpty()){
			throw new XMLException(errMsg);
		}
		return node;
	}
	
	Map<String, Field> allFieldMap = null;
	/**
	 * 遍历类的�?有字段，包括超类的字�?
	 * 返回�?个map。map的key是字段的tagName
	 * @param clazz
	 * @return
	 */
	Map<String, Field> getAllFieldMap(Class<?> clazz){
		if(allFieldMap == null){
			Map<String, Field> fieldNameMap = new LinkedHashMap<String, Field>();
			Map<String, Field> tagNameMap = new LinkedHashMap<String, Field>();
			Class<?> cClass = clazz;
			while(cClass != Object.class){
				Field[] fields = cClass.getDeclaredFields();
				for (Field field : fields) {
					String fieldName = field.getName();
					if(!fieldNameMap.containsKey(fieldName)){
						String key = fieldName;
						XMLTag tag = field.getDeclaredAnnotation(XMLTag.class);
						if(tag != null && !tag.tagName().isEmpty()){
							key = tag.tagName();
						}
						fieldNameMap.put(fieldName, field);
						tagNameMap.put(key, field);
					}
				}
				cClass = cClass.getSuperclass();
			}
			allFieldMap = tagNameMap;
		}
		return allFieldMap;
	}
	
	
	
	private Map<String, Method> setterMap = new HashMap<String, Method>();
	private Map<String, Method> getterMap = new HashMap<String, Method>();
	
	private Method getGetter(String fieldName, Class<?> baseClass){
		Method method = getterMap.get(fieldName);
		Class<?> cClass = baseClass;
		if(method == null){
			while(cClass != Object.class){
					try {
						method = cClass.getDeclaredMethod("get" + upcaseFirst(fieldName));
					} catch (NoSuchMethodException | SecurityException e) {}
					if(method != null){
						getterMap.put(fieldName, method);
					}else{
						try {
							method = cClass.getDeclaredMethod("is" + upcaseFirst(fieldName));
						} catch (NoSuchMethodException | SecurityException e) {}
					}
				cClass = cClass.getSuperclass();
			}
		}
		return method;
	}
	
	private Method getSetter(Field field, Class<?> baseClass){
		String fieldName = field.getName();
		Method method = setterMap.get(fieldName);
		Class<?> cClass = baseClass;
		if(method == null){
			while(cClass != Object.class){
				try {
					method = cClass.getDeclaredMethod("set" + upcaseFirst(fieldName), field.getType());
					if(method != null){
						setterMap.put(fieldName, method);
					}
				} catch (NoSuchMethodException | SecurityException e) {
				}
				cClass = cClass.getSuperclass();
			}
		}
		return method;
	}
	
	
	private String upcaseFirst(String fieldName) {
		return fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
	}

	/**
	 * 
	 * @param returnXML
	 * @param source
	 * @return
	 */
	public T parse(XmlNode returnXML, T source) {
		if(returnXML != null){
			Class<?> objClass = source.getClass();
			List<XmlNode> children = returnXML.getElements();
			children.forEach((child) -> {
				String tagName = child.getTagName();
				Field field = findField(tagName, objClass);
				if(field != null){
					Method setter = getSetter(field, objClass);
					if(setter != null){
						Class<?> fieldType = field.getType();
						String tagText = child.getText();
						if(tagText != null){
							Object val = FormatUtils.toClass(fieldType, tagText);
							if(val != null){
								try {
									setter.invoke(source, val);
								} catch (Exception e) {}
							}
						}
						
					}
				}
			});
			return source;
		}
		return null;
	}


	private Field findField(String tagName, Class<?> clazz) {
		return getAllFieldMap(clazz).get(tagName);
	}


	

}
