package cho.carbon.hc.copframe.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 
 * <p>Title: SerializeUtils</p>
 * <p>Description: </p><p>
 * 序列化工具类
 * </p>
 * @author Copperfield Zhang
 * @date 2017年7月19日 上午10:20:13
 */
public class SerializeUtils {
	
	/**
	 * 序列化对象，将Java对象转换为字节数组
	 * @param obj
	 * @return 转换失败返回null
	 */
	public static byte[] serialize(Serializable obj){
		ByteArrayOutputStream bo = new ByteArrayOutputStream();  
		ObjectOutputStream oo = null;
		try {
			oo = new ObjectOutputStream(bo);
			oo.writeObject(obj);
			return bo.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				oo.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				bo.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 反序列化字节数组，将其转换成Java对象
	 * @param bytes
	 * @return 转换失败时返回null
	 */
	public static Serializable deserialize(byte[] bytes){
		ByteArrayInputStream bi = new ByteArrayInputStream(bytes);  
	    ObjectInputStream oi = null;
		try {
			oi = new ObjectInputStream(bi);
			Serializable obj = (Serializable) oi.readObject();
			return obj;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				oi.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				bi.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
