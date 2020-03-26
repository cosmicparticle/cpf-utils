package cho.carbon.hc.copframe.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.CharUtils;

/**
 * 
 * <p>Title:StringUtils</p>
 * <p>Description:
 * 	整合部分常用字符串处理函数的工具类
 * </p>
 * @author 张荣波
 * @date 2014年11月10日 下午4:42:00
 */
public class TextUtils {
	
	
	public static boolean hasText(String str){
		return str != null && !str.isEmpty(); 
	}
	
	/**
	 * 将字符串首字符转换成大写，当首字母不是字母的时候返回原字符串
	 * 这个函数可以用于组合类的get,set函数名
	 * @param str 要转换的字符串
	 * @return 转换后的字符串，其首字母大写
	 */
	public static String upCaseFirstChar(String str){
		synchronized (str) {
			char[] chs = str.toCharArray();
			if (CharUtils.isAsciiAlpha(chs[0])) {
				chs[0] = String.valueOf(chs[0]).toUpperCase().toCharArray()[0];
			}
			return String.valueOf(chs);
		}
	}
	/**
	 * 将字符串首字符转换成小写，当首字母不是字母的时候返回原字符串
	 * 这个函数可以用于还原类的get,set函数名
	 * @param str 要转换的字符串
	 * @return 转换后的字符串，其首字母大写
	 */
	public static String lowCaseFirstChar(String str){
		char[] chs = str.toCharArray();
		if (CharUtils.isAsciiAlpha(chs[0])) {
			chs[0] = String.valueOf(chs[0]).toLowerCase().toCharArray()[0];
		}
		return String.valueOf(chs);
	}
	/**
	 * 将str的所有字母转为大写
	 * @param str
	 * @return
	 */
	public static String toUpperCase(String str){
		if(str != null){
			return str.toUpperCase();
		}
		return null;
	}
	/**
	 * 将str的字母转为小写
	 * @param str
	 * @return
	 */
	public static String toLowCase(String str){
		if(str != null){
			return str.toLowerCase();
		}
		return null;
	}
	
	 /**
	  * 去除字符串首尾的特定字符序列
	  * @param str 要修整的字符串
	  * @param regex 检查的字符序列
	  * @return 新字符串
	  */
	 public static String trim(String str, String...regex){
		if(str == null){
			return null;
		}
		String ret = new String(str);
		if(regex.length == 0){
			return ret.trim();
		}
		Boolean hasChanged = true;
		while(hasChanged){
			hasChanged = false;
			for (String r : regex) {
				if(ret.startsWith(r)){
					ret = ret.substring(r.length());
					hasChanged = true;
				}
				if(ret.endsWith(r)){
					ret = ret.substring(0, ret.lastIndexOf(r));
					hasChanged = true;
				}
			}
		}
		return ret;
	 }
	 /**
	  * 将字符串根据分隔符分割为数组
	  * @param toSplit
	  * @param regex
	  * @param limit 可选参数，限制元素个数用
	  * @return 传入的字符串为空时返回null
	  */
	 public static String[] splitToArray(String toSplit, String regex, Integer...limit){
		 if(toSplit != null){
			 if(limit.length == 0){
				 return toSplit.split(regex);
			 }else{
				 return toSplit.split(regex, limit[0]);
			 }
		 }
		 return null;
	 }
	 /**
	  * 判断字符串能否转成integer
	  * @param string
	  * @return
	  */
	 public static Boolean isInteger(String string){
		 try {
			Integer.valueOf(string);
			return true;
		} catch (Exception e) {
			return false;
		}
	 }
	 private static String alphabetRegex = "^[A-Za-z]+$";
	 /**
	  * 判断一个字符串是不是全是字母
	  * @param string
	  * @return
	  */
	 public static Boolean isAlphabet(String string){
		 if(string != null){
			 return string.matches(alphabetRegex);
		 }
		 return false;
	 }
	 
	 /**
	  * 二次解码，编码为UTF-8
	  * @param string
	  * @return
	  */
	 public static String dblDecode(String string){
		 return dblDecode(string, "UTF-8");
	 }
	 /**
	  * 二次解码，编码为code
	  * @param string
	  * @param code
	  * @return
	  */
	 public static String dblDecode(String string, String code){
		 try {
			return URLDecoder.decode(URLDecoder.decode(string, code), code);
		} catch (Exception e) {
			return string;
		}
	 }
	 /**
	  * 转码函数
	  * @param string
	  * @param oriCode
	  * @param targetCode
	  * @return
	  */
	 public static String transcoding(String string, String oriCode, String targetCode){
		 try {
			String oriStr = new String(string.getBytes(), oriCode);
			String targetStr = new String(oriStr.getBytes(), targetCode);
			return targetStr;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return string;
		}
		 
	 }
	 /**
	  * 判断某个字符串是否是数字，如果是整数或者小数，那么就返回true
	  * @param str
	  * @return
	  */
	 public static Boolean isNumeric(String str){
		 String regexInteger = "^-?\\d+$";
		 String regexDouble = "^(-?\\d+)(\\.\\d+)?$";
		 if(!str.matches(regexInteger)){
			 return str.matches(regexDouble);
		 }
		 return true;
	 }
	 
	 /**
	  * 将某个数组转换成字符串，以特定分隔符分割
	  * @param array
	  * @param spliter
	  * @return
	  */
	 public static String toString(Object[] array, String spliter){
		 String ret = "";
		 if(array != null && TextUtils.hasText(spliter)){
			 for (Object object : array) {
				ret += FormatUtils.toString(object) + spliter;
			}
			 ret = TextUtils.trim(ret, spliter);
		 }
		 return ret;
	 }

	 private static Pattern lastNumberPattern = Pattern.compile("^(.*\\D)?(\\d+)(.*)$", Pattern.DOTALL);
	 private static Pattern endNumberPattern= Pattern.compile("^(.*\\D)?(\\d+)$", Pattern.DOTALL);
	 
	 /**
	  * 如果字符串是以数字结尾的，那么就取出这几个数字
	  * 否则就返回null
	  * @param string
	  * @return
	  */
	 public static String getEndNumber(String string){
		 if(string != null){
			 Matcher matcher = endNumberPattern.matcher(string);
			 if(matcher.matches()){
				 return matcher.group(2);
			 }
		 }
		 return null;
	 }
	 
	 /*
	  * 获取字符串最后的几位数字
	  */
	 public static String getLastNumber(String string){
		 String ret = "";
		 if(string != null){
			 Matcher matcher = lastNumberPattern.matcher(string);
			 if(matcher.matches()){
				 return matcher.group(2) ;
			 }
		 }
		 return ret;
	 }
	 
	 /**
	  * 判断如果是以其中任何一个字符串为后缀，那么返回正确
	  * 不传入后缀则为false
	  * @param source
	  * @param suffix
	  * @return
	  */
	 public static Boolean endWith(String source, String...suffix){
		 for (String string : suffix) {
			if(source.endsWith(string)){
				return true;
			}
		}
		 return false;
	 }
	 private static String CHARSTR;
	 private static char[] CHARS;
	 private static Map<Character, Integer> CHARS_MAP;
	
	 static{
		 CHARSTR = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		 CHARS = CHARSTR.toCharArray();
		 CHARS_MAP = new HashMap<Character, Integer>();
		 for (int i = 0; i < CHARS.length; i++) {
			 CHARS_MAP.put(CHARS[i], i);
		 }
	 }
	
	
	 /**
	  * 获取随机的uuid，长度为length，位数为radix
	  * @param length
	  * @param radix
	  * @return
	  */
	 public static synchronized String uuid(Integer length, Integer radix){
		 String ret = "";
		 if(length != null){
			 char[] uuid = new char[length];
			 for(int i = 0 ; i < length; i++){
				 uuid[i] = CHARS[(int)(Math.random() * radix)];
			 }
			 ret = String.valueOf(uuid);
		 }
		 return ret;
	 }
	 /**
	  * 获取长度为32，位数为16的随机uuid
	  * @return
	  */
	 public static String uuid(){
		 return uuid(32, 16);
	 }

	 public static String convert(int digit){
		int radix = CHARS.length;
		int d = digit / radix;
		StringBuffer buffer = new StringBuffer();
		do {
			int t = digit % radix;
			d = digit / radix;
			char c = CHARS[t];
			buffer.insert(0, c);
			digit = d;
		} while (d > 0);
		return buffer.toString();
	 }
	 
	 /**
	 * 
	 * @param num
	 * @param carry
	 * @param length
	 * @return
	 */
	public static String convert(long num, Integer carry, int length) {
		Integer division = carry;
		if(carry == null || carry > CHARS.length){
			division = CHARS.length;
		}
		StringBuffer buffer = new StringBuffer();
		long div = num;
		int mod = 0;
		do {
			mod = (int) (div % division);
			buffer.insert(0, CHARS[mod]);
			div = div / division;
		} while (div > 0);
		while(buffer.length() < length){
			buffer.insert(0, "0");
		}
		return buffer.toString();
	}
	 
	/**
	 * 将压缩后的字符串还原为原始的十进制数字
	 * @param code 字符串，只能由大小写字母和数字构成
	 * @param carry 进制，最大值为62
	 * @return 还原后的十进制数字
	 * @see #convert(long, Integer, int)
	 */
	public static Long parse(String code, int carry){
		if(code != null && code.matches("^[0-9A-Za-z]+$")){
			if(carry > CHARS.length){
				carry = CHARS.length;
			}
			char[] cs = code.toCharArray();
			long sum = 0;
			for (int i = 0; i < cs.length; i++) {
				sum += CHARSTR.indexOf(cs[i]) * Math.pow(carry, cs.length - i - 1);
			}
			return sum;
		}
		return null;
	}
	
	
	public static void main(String[] args) {
		//将编码转换为十进制数字
		Long c = parse("_4WB", 62);
		//计算该数字代表的日期
		Temporal t = ChronoUnit.DAYS.addTo((new Date(0l)).toInstant(), c);
		System.out.println(Date.from(Instant.from(t)));
	}
	 
	 /**
	  * 将一个字符串集合通过逗号分隔，放到一个字符串内
	  * @param collection
	  * @return
	  */
	 public static String join(String[] list){
		 String ret = "";
		 for (String ele : list) {
			ret += ele + ",";
		 }
		 return TextUtils.trim(ret, ",");
	 }
	 
	 /**
	  * 去掉字符串的所有空格，包括全角和半角的空格
	  * @param str
	  * @return
	  */
	 public static String removeBlank(String str){
		 if(str != null){
			 return str.replace(" ", "").replace("　", "");
		 }
		 return str;
	 }
	 
	/**
/**
	 * 根据正则表达式截取源字符串中对应的片段，如果不存在这个片段，那么就返回null
	 * @param source 要截取的源字符串
	 * @param pattern 用于截取的正则表达式
	 * @param index 匹配的子串索引
	 * @param group 返回匹配的组号
	 * @return 如果匹配成功，并且组号对应的片段存在，那么就返回这个片段，如果不匹配，或者组号对应的片段不存在，那么就返回null
	 */
	public static String getString(String source, Pattern pattern, int index,  int group){
		Matcher matcher = pattern.matcher(source);
		String ret = null;
		while(matcher.find() && index -- >= 0 ){
			try {
				ret =  matcher.group(group);
			} catch (IndexOutOfBoundsException  e) {
			}
		}
		return ret;
	}
	/**
	 * 根据正则表达式截取源字符串中对应的片段，如果不存在这个片段，那么就返回null
	 * @param source 要截取的源字符串
	 * @param pattern 用于截取的正则表达式
	 * @return 如果全匹配成功，那么就返回这个片段，如果不匹配，那么就返回null
	 */
	public static String getString(String source, Pattern pattern){
		return getString(source, pattern, 0, 0);
	}
	/**
	 * 根据正则表达式截取源字符串中对应的片段，如果不存在这个片段，那么就返回null
	 * @param source 要截取的源字符串
	 * @param pattern 用于截取的正则表达式
	 * @param group 返回匹配的组号
	 * @return 如果全匹配成功，那么就返回这个片段，如果不匹配，那么就返回null
	 */
	public static String getString(String source, Pattern pattern, int group){
		return getString(source, pattern, 0, group);
	}
	
	static Pattern SCIENTIFIC = Pattern.compile("^((-?\\d+.?\\d*)[Ee]{1}(-?\\d+))$");
	public static boolean isScientific(String str){
		Matcher matcher = SCIENTIFIC.matcher(str);
		if(matcher.matches()){
			return true;
		}
		return false;
	}
	
	
	/**
	 * 将Java字符串转换成JSON格式的字符串
	 * @param str
	 * @return
	 */
	public static String escapeToStringJSONElement(String str){
		String ret = "";
		if(str != null){
			ret = str;
			ret = ret.replaceAll("\\\\", "\\\\\\\\");
			ret = ret.replaceAll("\"", "\\\\\"");
			ret = ret.replaceAll("\'", "\\\\\'");
		}
		return ret;
	}
	
	/**
	 * 比较两个String
	 * @param a
	 * @param b
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static int compare(Comparable a, Comparable b){
		return (a == null && b == null ? 0 : a == null && b != null ? -1
				: a != null && b == null ? 1 : a.compareTo(b));
	}
	
	static Pattern mobilePattern = Pattern.compile("^[1][3,4,5,8][0-9]{9}$");
	static Pattern phonePattern1 = Pattern.compile("^[0][1-9][2,3]-[0-9]{5,10}");
	static Pattern phonePattern2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");
	public static boolean isContactNumber(String contactNumber){
		if(contactNumber != null){
			if(mobilePattern.matcher(contactNumber).matches()
					|| phonePattern1.matcher(contactNumber).matches()
					|| phonePattern2.matcher(contactNumber).matches()){
				return true;
			}
		}
		return false;
	}
	public static String toString(String[] ranges) {
		return null;
	}
	
	
	
	
	/**
	 * md5加密
	 * @param string
	 * @return
	 */
	public static String md5Encode(String string, String c) {
		;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			Charset charset = null;
			if(c != null && Charset.isSupported(c)){
				charset = Charset.forName(c);
			}else{
				charset = Charset.defaultCharset();
			}
			md.update(string.getBytes(charset));
			byte[] b = md.digest();
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < b.length; i++) {
	           String hex = Integer.toHexString(b[i] & 0xFF);
	           if (hex.length() == 1) {
	              hex = '0' + hex;
	           }
	           buffer.append(hex);
	       }
			return buffer.toString();
		} catch (NoSuchAlgorithmException e) {
		}
		return null;
	}

	/**
	 * sha1加密
	 * @param upperCase
	 * @return
	 */
	public static String sha1Encode(String upperCase) {
		SHA1 sha1 = new SHA1();
		return sha1.Digest(upperCase);
	}
	
	
	public static long decode(String code, int carry){
		if(carry > CHARS.length){
			carry = CHARS.length;
		}
		long result = 0;
		char[] codeArray = code.toCharArray();
		long power = carry; 
		for (int i = 0; i < codeArray.length; i++) {
			char c = codeArray[codeArray.length - i - 1];
			int cur = CHARS_MAP.get(c);
			result += cur * power;
			power *= carry;
		}
		return result;
	}
	public static String prependZeros(long num, int maxLength){
		String.valueOf(num);
		char[] cs = new char[maxLength];
		for (int i = 0; i < cs.length; i++) {
			cs[i] = '0';
		}
		String pattern = new String(cs);
		DecimalFormat df = new DecimalFormat(pattern);
		return df.format(num);
	}

	public static String formatFloat(float number, String pattern) {
		DecimalFormat format = new DecimalFormat(pattern);
		return format.format(number);
	}

	public static String readAsString(InputStream inp) throws IOException {
		StringBuffer xmlStr = new StringBuffer();
		InputStreamReader reader = new InputStreamReader(inp);
		char[] cbuf = new char[1000];
		while(reader.read(cbuf) != -1) {
			xmlStr.append(cbuf);
		}
		reader.close();
		return xmlStr.toString();
	}
	
	public static <T, R extends Collection<T>> R split(
			String toSplit, 
			String spliter, 
			Supplier<R> containerSuppilier, 
			Function<String, T> func){
		R container = containerSuppilier.get();
		if(hasText(toSplit)) {
			String[] array = toSplit.split(spliter);
			for (String e : array) {
				container.add(func.apply(e));
			}
		}
		return container;
	}

	public static Set<Long> splitToLongSet(String toSplit, String spliter) {
		return split(toSplit, spliter, LinkedHashSet::new, a->Long.valueOf(a));
	}

	public static Set<String> split(String toSplit, String spliter) {
		return split(toSplit, spliter, LinkedHashSet::new, a->a);
	}

	public static Set<Integer> splitToIntegerSet(String toSplit, String spliter) {
		return split(toSplit, spliter, LinkedHashSet::new, a->Integer.valueOf(a));
	}
	
}
