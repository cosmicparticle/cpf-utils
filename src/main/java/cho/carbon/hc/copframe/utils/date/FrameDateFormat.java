package cho.carbon.hc.copframe.utils.date;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 * 
 * <p>Title: FrameDateFormat</p>
 * <p>Description: </p><p>
 * 框架级的日期格式接口
 * </p>
 * @author Copperfield Zhang
 * @date 2016年3月13日 下午2:26:42
 */
public interface FrameDateFormat {

	/**
	 * 使用特定格式的字符串来格式化时间对象。
	 * 如果传入的pattern是一个错误的格式，那么最后会返回一个null值
	 * @param date 要进行格式化的日期对象
	 * @param pattern 日期格式
	 * @return 如果格式错误，那么就返回一个null值
	 * @see {@link #format(Date, DateFormat)}
	 */
	public String format(Date date, String pattern);

	/**
	 * 自定义日期格式化
	 * @param date 要进行格式化的日期对象
	 * @param format 日期格式对象,可以是{@link SimpleDateFormat}对象
	 * @return 如果传入的format对象为null,那么返回一个null
	 */
	public String format(Date date, DateFormat format);

	/**
	 * 将Date对象格式化按照默认日期格式来进行格式化
	 * @param date
	 * @return
	 */
	public String formatDate(Date date);

	/**
	 * 将Date对象按照默认时间格式来格式化
	 * @param date
	 * @return
	 */
	public String formatDateTime(Date date);

	/**
	 * 将Date对象按照默认日期时间格式来格式化
	 * @param date
	 * @return
	 */
	public String formatTime(Date date);

	/**
	 * 将字符串按照当前的日期时间格式转换为时间对象<br/>
	 * <h1>转化步骤为</h1>
	 * <ul>
	 * 	<li>按照默认时间格式来转换，如果无法解析，那么用时间格式来解析</li>
	 * 	<li>如果默认时间格式无法解析，那么按照日期时间格式来解析</li>
	 * 	<li>如果默认日期时间格式无法解析，那么最后返回null</li>
	 * <ul>
	 * <h1>
	 * 由此可见，无论这个字符串是日期还是时间或者是日期时间，只要是根据默认格式，
	 * 那么就可以将其解析为日期对象
	 * </h1>
	 * @param dateStr
	 * @return
	 */
	public Date parse(String dateStr);

	/**
	 * 将字符串按照特定日期格式化对象来解析成日期对象 
	 * 如果传入的日期对象为null，那么最后返回的对象也是null
	 * @param dateStr 要进行格式化的时间字符串
	 * @param format 日期格式化对象{@link SimpleDateFormat}
	 * @return
	 */
	public Date parse(String dateStr, DateFormat format);

	/**
	 * 将字符串按照特定日期格式来解析成日期对象
	 * 传入的日期格式有误，那么最后返回的对象也为null
	 * @param dateStr 要进行解析的时间字符串
	 * @param formatStr 解析格式字符串，会构造成{@link SimpleDateFormat}对象
	 * @return 
	 * @see {@link #parse(String, DateFormat)}
	 */
	public Date parse(String dateStr, String formatStr);
	/**
	 * 从字符串中分割时间范围
	 * @param dateRange 包含时间范围的字符串
	 * @param spliter 分割开始时间和结束时间的字符串
	 * @return 一个数组，包含两个元素，第一个是开始时间，第二个时间结束时间。
	 * 无论字符串是否包含两个时间，都会返回长度为2的数组，但是元素可能为null
	 */
	Date[] splitDateRange(String dateRange, String spliter);
	/**
	 * 用符号“~”来分割开始时间和结束时间的字符串
	 * @param dateRange 时间范围字符串 
	 * @return 
	 * @see DateUtils#splitDateRange(String, String)
	 */
	Date[] splitDateRange(String dateRange);
	/**
	 * 获得某天的零点时间对象
	 * @return
	 */
	Date getTheDayZero(Date theDay);
	/**
	 * 根据日期，获得增加指定天数后的日期对象
	 * @param datetime 初始时间
	 * @param incDay 增加的天数，可以为负数
	 * @return 返回计算后的日期时间对象，注意只有日期改变，时间不变
	 */
	Date incDay(Date datetime, int incDay);
	/**
	 * 判断两个时间是否在同一天
	 * @param date
	 * @param theDay
	 * @return
	 */
	boolean inDay(Date date, Date theDay);
	
	/**
	 * 
	 * @param base 某一天为基础，计算该周的星期几
	 * @param startDayOfWeek 该周的星期几，以这天为起始{@link Calendar#MONDAY}...
	 * @param dayAddition 以startDayOfWeek为初始，向后的天数。例如startDayOfWeek为星期一，dayAddition为2时，则计算出来的是星期三
	 * @param HH 小时（24小时）
	 * @param mm 分钟
	 * @param ss 秒
	 */
	Date getTheDayOfWeek(Date base,int startDayOfWeek, int dayAddition, int HH, int mm, int ss);

	/**
	 * @param startDayOfWeek 该周的星期几，以这天为起始{@link Calendar#MONDAY}...
	 * @param dayAddition 以startDayOfWeek为初始，向后的天数。例如startDayOfWeek为星期一，dayAddition为2时，则计算出来的是星期三
	 * @return
	 * @see #getTheDayOfWeek(int, int, int, int, int)
	 */
	Date getTheDayOfWeek(int startDayOfWeek, int dayAddition);
	
	/**
	 * 获得日期参数所在周的时间范围
	 * @param date
	 * @param weekStart 每周的第一天是哪天， 默认{@link Calendar#MONDAY}
	 * @return
	 */
	Date[] getTheWeekRange(Date date, Integer weekStart);

	
}