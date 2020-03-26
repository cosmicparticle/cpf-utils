package cho.carbon.hc.copframe.utils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
/**
 * 
 * <p>Title: FormatUtils</p>
 * <p>Description: </p><p>
 * 格式化工具类
 * </p>
 * @author Copperfield Zhang
 * @date 2016年3月13日 下午1:49:08
 */
public class FormatUtils {
	/**
	 * 将对象转换成Integer<br/>
	 * 机制是将o.toString转换成Double，再获取Double对象的intValue()
	 * 如果转换失败，就返回null
	 * @param o
	 * @return
	 */
	public static Integer toInteger(Object o){
		try {
			return Double.valueOf(o.toString()).intValue();
		} catch (Exception e) {
		}
		return null;
	}
	/**
	 * 将对象转换成Long</br>
	 * 机制是将o.toString()转换成Double，在将Doule转换成BigDecimal，最后获得BigDecimal的longValue
	 * @param o
	 * @return 如果转换有误，返回null
	 */
	public static Long toLong(Object o){
		try {
			return BigDecimal.valueOf(toDouble(o)).longValue();
		} catch (Exception e) {
		}
		return null;
	}
	/**
	 * 直接获得o的toString
	 * @param o
	 * @return 如果转换有误，返回null
	 */
	public static String toString(Object o){
		try {
			return o.toString();
		} catch (Exception e) {
		}
		return null;
	}
	/**
	 * 获得整数的BigDecimal
	 * @param o
	 * @return 如果转换有误，返回null
	 */
	public static BigDecimal toBigDecimal(Object o){
		try {
			return BigDecimal.valueOf(toLong(o));
		} catch (Exception e) {
		}
		return null;
	}
	/**
	 * 将对象转换成Double
	 * @param o
	 * @return 如果转换有误，返回null
	 */
	public static Double toDouble(Object o){
		try {
			return Double.valueOf(o.toString());
		} catch (Exception e) {
		}
		return null;
	}
	/**
	 * 将对象转换成布尔型
	 * 如果对象是字符串类型，就根据Boolean.value()方法转换，
	 * 只有传入"true"的时候才为真,
	 * 如果传入其它类型的对象，则仅当对象不为空时返回真
	 * @param o
	 * @return 如果转换有误，返回false
	 */
	public static Boolean toBoolean(Object o){
		try {
			if(o != null){
				if(o instanceof String){
					return Boolean.valueOf(o.toString());
				}else{
					return true;
				}
			}
		} catch (Exception e) {}
		return false;
	}
	
	public static LinkedHashMap<Object, Object> toMap(Object[] objs){
		if(objs != null){
			LinkedHashMap<Object, Object> ret = new LinkedHashMap<Object, Object>();
			for (Object obj : objs) {
				ret.put(obj, obj);
			}
			return ret;
		}
		return null;
	}
	
	public static LinkedHashMap<Object, Object> toMap(Collection<Object> objs){
		if(objs != null){
			return toMap(objs.toArray(new Object[objs.size()]));
		}
		return null;
	}
	
	/**
	 * 整合各个方法，将value转换成其他类型的对象
	 * @param formatClass
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T toClass(Class<T> formatClass, Object value){
		if(formatClass != null && value != null && formatClass.isAssignableFrom(value.getClass())) {
			return (T) value;
		}else {
			if(String.class.equals(formatClass)){
				return (T) toString(value);
			}else if(Integer.class == formatClass || Integer.TYPE == formatClass){
				return (T) toInteger(value);
			}else if(Long.class == formatClass || Long.TYPE == formatClass){
				return (T) toLong(value);
			}else if(Double.class == formatClass || Double.TYPE == formatClass){
				return (T) toDouble(value);
			}else if(BigDecimal.class == formatClass){
				return (T) toBigDecimal(value);
			}else if(Boolean.class == formatClass || Boolean.TYPE == formatClass){
				return (T) toBoolean(value);
			}else if(Date.class.isAssignableFrom(formatClass)){
				return (T) value;
			}
		}
		return null;
	}
	
	/**
	 * 取第一个非null的对象
	 * @param objects
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@SafeVarargs
	public static <T> T coalesce(T...objects){
		return (T) coalesceWhole(objects);
	}
	
	/**
	 * 返回第一个非null的对象
	 * @param objects
	 * @return
	 */
	public static Object coalesceWhole(Object...objects){
		if(objects.length > 0){
			for (Object object : objects) {
				if(object != null){
					return object;
				}
			}
		}
		return null;
	}
	
	public static String join(Object array, String split) {
		if(array instanceof String) {
			return (String) array;
		}
		if(array != null) {
			StringBuffer buffer = new StringBuffer();
			if(array instanceof Collection) {
				((Collection<?>) array).forEach(e->buffer.append(toString(e) + split));
			}else if(array.getClass().isArray()) {
				for (Object e : (Object[])array) {
					buffer.append(toString(e) + split);
				}
			}
			if(buffer.length() > 0) {
				buffer.delete(buffer.length() - split.length(), buffer.length());
			}
			return buffer.toString();
		}
		return null;
	}
	
}
