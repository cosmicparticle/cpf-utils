package cho.carbon.hc.copframe.utils;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class JsonUtils {
	public static JSONObject convertToStringKeyMap(Map<? extends Object, ? extends Object> map) {
		if(map != null) {
			JSONObject jo = new JSONObject();
			map.forEach((key, val)->{
				jo.put(String.valueOf(key), val);
			});
			return jo;
		}
		return null;
	}
}
