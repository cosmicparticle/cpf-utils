package cho.carbon.hc.copframe.utils.text;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 
 * <p>Title: TextHandler</p>
 * <p>Description: </p><p>
 * 文本处理工具，可以用于替换文本和拼接文本
 * </p>
 * @author Copperfield Zhang
 * @date 2017年5月25日 下午4:39:59
 */
public class TextHandler {
	private StringBuffer buffer;
	private final String indicator = "#";
	private final String wrapStart = "\\{";
	private final String wrapEnd = "\\}";
	private Map<String, String> parameter = new HashMap<String, String>();
	
	public TextHandler(String source) {
		this.buffer = new StringBuffer(source);
	}
	/**
	 * 设置文本中的参数
	 * @param key
	 * @param value
	 * @return
	 */
	public TextHandler setParameter(String key, Object value){
		parameter.put(key, String.valueOf(value));
		return this;
	}
	/**
	 * 添加文本
	 * @param text
	 * @return
	 */
	public TextHandler appendText(String text){
		buffer.append(text);
		return this;
	}
	
	/**
	 * 去除文本前后的字符
	 * @param chars
	 * @return
	 */
	public TextHandler trim(String chars){
		int charsLength = chars.length();
		while(buffer.indexOf(chars) == 0){
			buffer.delete(0, charsLength);
		}
		while(true){
			int lastIndex = buffer.lastIndexOf(chars);
			if(lastIndex == buffer.length() - charsLength){
				buffer.delete(lastIndex, lastIndex + charsLength);
			}else{
				break;
			}
		}
		return this;
	}
	
	/**
	 * 将参数放到文本中，并返回文本
	 * @param ignoreUnset 是否忽略没有设置的参数。如果参数没有设定的情况下，该值为false时，那么会抛出异常，如果为true，那么会将参数的占据的位置设置为空
	 * @return
	 * @throws ParameterUnsetException
	 */
	public String getText(boolean ignoreUnset) throws ParameterUnsetException{
		Pattern patter = Pattern.compile(indicator + wrapStart + "\\s*([\\d\\w_\\$]+)\\s*" + wrapEnd);
		Matcher matcher = patter.matcher(buffer.toString());
		StringBuffer result = new StringBuffer();
		
		while(matcher.find()){
			String key = matcher.group(1);
			String value = parameter.get(key);
			if(value != null){
				matcher.appendReplacement(result, value);
			}else{
				if(ignoreUnset){
					matcher.appendReplacement(result, "");
				}else{
					throw new ParameterUnsetException(key);
				}
			}
		}
		matcher.appendTail(result);
		return result.toString();
	}

	public String getText() {
		return getText(false);
	}
}
