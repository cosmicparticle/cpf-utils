package cho.carbon.hc.copframe.utils;

public class Assert {
	public static void notNull(Object obj){
		if(obj == null){
			throw new IllegalArgumentException("参数不能为null");
		}
	}
}
