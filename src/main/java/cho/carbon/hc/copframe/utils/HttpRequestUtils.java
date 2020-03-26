package cho.carbon.hc.copframe.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cho.carbon.hc.copframe.utils.xml.Dom4jNode;
import cho.carbon.hc.copframe.utils.xml.XMLException;
import cho.carbon.hc.copframe.utils.xml.XmlNode;
/**
 * 
 * <p>Title: HttpRequestUtils</p>
 * <p>Description: </p><p>
 * http请求工具类
 * </p>
 * @author Copperfield Zhang
 * @date 2017年3月9日 下午9:15:54
 */
public class HttpRequestUtils {
	
	static Logger logger = Logger.getLogger(HttpRequestUtils.class);
	
	/**
	 * 用于获取当前请求的完整url，包括get方式提交的参数
	 * @param request
	 * @param withoutPort 是否要去去除端口
	 * @return
	 */
	public static String getCompleteURL(HttpServletRequest request){
		return getCompleteURL(request, true);
	}
	
	/**
	 * 用于获取当前请求的完整url，包括get方式提交的参数
	 * @param request
	 * @param withoutPort 是否要去去除端口
	 * @return
	 */
	public static String getCompleteURL(HttpServletRequest request, boolean withoutPort){
		String requestURL = request.getRequestURL().toString();
		if(withoutPort){
			Pattern pattern =  Pattern.compile("^(\\w+://)?[^/]+:(\\d+)/?(.*)$");
			Matcher matcher = pattern.matcher(requestURL);
			if(matcher.matches()){
				String port = matcher.group(2);
				requestURL = requestURL.replaceFirst(":" + port, "");
			}
		}
		String requestQuery = request.getQueryString(),
			url = requestURL + (TextUtils.hasText(requestQuery) ? ("?" + requestQuery) : "")
			;
		return url;
	}
	
	public static String getURI(HttpServletRequest req) {
		String reqURI = req.getRequestURI(),
				contextPath = req.getContextPath();
		if(reqURI.startsWith(contextPath)){
			return reqURI.substring(contextPath.length(), reqURI.length() );
		}else{
			logger.error("获得uri失败，requestURI为[" + reqURI + "],contextPath为[" + contextPath + "]，无法截取");
			return reqURI;
		}
	}
	
	/**
	 * 将request中parameter数据转到attribute中
	 * @param request
	 */
	public static void restoreParametersToAttribute(HttpServletRequest request){
		Enumeration<String> names = request.getParameterNames();
		while(names.hasMoreElements()){
			String name = names.nextElement();
			String[] parameters = request.getParameterValues(name);
			if(parameters != null){
				if(parameters.length >= 1){
					request.setAttribute(name, parameters[0]);
				}
			}
		}
	}
	
	/**
	 * 发送http的post请求。post请求只能通过正文来添加参数值
	 * @param url 请求地址
	 * @param contentType 请求正文类型
	 * @param content 请求正文
	 * @return
	 */
	public static String post(String url, String contentType, String content){
		try {
			//打开TCP连接
			URL urlObject = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
			//设置POST请求的必要参数
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			//设置正文类型
			connection.setRequestProperty("Content-type", contentType);
			//正文编码为utf8
			connection.setRequestProperty("charset", "utf-8");
			//写入正文
			OutputStream oStream = connection.getOutputStream();
			logger.debug(content);
			oStream.write(content.getBytes("utf-8"));
			oStream.flush();
			oStream.close();
			//提交请求并且获得返回数据
			InputStream iStream = connection.getInputStream();
			
			BufferedReader bReader = new BufferedReader(new InputStreamReader(iStream, "utf-8"));
			
			StringBuffer buffer = new StringBuffer();
			String line;
			while((line = bReader.readLine()) != null){
				buffer.append(line);
			}
			return buffer.toString();
		} catch (MalformedURLException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
		return null;
	}
	
	/**
	 * post发送请求，并且返回数据为JSON数据
	 * @param url 请求地址
	 * @param contentType 正文类型
	 * @param content 请求正文
	 * @return
	 */
	public static JSONObject postAndReturnJson(String url, String contentType, String content){
		String result = post(url, contentType, content);
		if(result != null){
			return JSON.parseObject(result);
		}
		return null;
	}
	/**
	 * post发送请求，并且返回数据为JSON
	 * @param url 请求地址
	 * @param parameters 请求参数
	 * @return
	 */
	public static JSONObject postAndReturnJson(String url, Map<String, String> parameters){
		StringBuffer content = new StringBuffer();
		if(parameters != null){
			parameters.forEach((key, value) -> {
				content.append(key + "=" + value + "&");
			});
		}
		return postAndReturnJson(url, "utf-8", content.toString());
	}
	/**
	 * post发送请求，请求内容为json，响应的内容也是json
	 * @param url
	 * @param jo
	 * @return
	 */
	public static JSONObject postJsonAndReturnJson(String url, JSONObject jo) {
		if(jo != null){
			return postAndReturnJson(url, "utf-8", jo.toString());
		}
		return null;
	}
	
	/**
	 * 以xml报文的方式发起请求到url，并且返回XmlNode
	 * @param url
	 * @param xml
	 * @return
	 */
	public static XmlNode postXMLAndReturnXML(String url, XmlNode xml){
		String result = post(url, "text/xml", xml.asXML());
		try {
			return new Dom4jNode(result);
		} catch (XMLException | UnsupportedEncodingException e) {
			logger.error("解析xml时发生错误" + result, e);
		}
		return null;
	}

	public static boolean isAjaxRequest(HttpServletRequest reqeust) {
		String ajaxHeader = reqeust.getHeader("X-Requested-With");  
        return "XMLHttpRequest".equalsIgnoreCase(ajaxHeader);
	}

	
	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
	}
	       
	/**
	 * 根据当前请求获得项目的磁盘路径
	 * @param request
	 * @param path
	 * @return
	 */
	public static String getRealPath(HttpServletRequest request, String path){
		return request.getServletContext().getRealPath(path);
	}

	public static Map<String, String> convertParamMap(HttpServletRequest request) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		request.getParameterMap().forEach((paramName, paramValues)->{
			if(paramValues != null && paramValues.length > 0) {
				map.put(paramName, paramValues[0]);
			}
		});
		return map;
	}
	
	
}
