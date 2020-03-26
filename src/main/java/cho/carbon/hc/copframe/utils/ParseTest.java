package cho.carbon.hc.copframe.utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Date;

public class ParseTest {
	private static String CHARSTR = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private static char[] CHARS = CHARSTR.toCharArray();
	/**
	 * 将压缩后的字符串还原为原始的十进制数字
	 * @param code 字符串，只能由大小写字母和数字构成
	 * @param carry 进制，最大值为62
	 * @return 还原后的十进制数字，转换失败时返回null
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
		Long c = parse("4WB", 62);
		if(c != null){
			//计算该数字代表的日期
			Temporal t = ChronoUnit.DAYS.addTo((new Date(0l)).toInstant(), c);
			System.out.println(Date.from(Instant.from(t)));
		}
	}
}
