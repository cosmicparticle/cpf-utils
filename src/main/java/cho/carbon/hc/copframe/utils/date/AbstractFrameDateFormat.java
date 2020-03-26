package cho.carbon.hc.copframe.utils.date;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 * 
 * <p>Title: AbstractFrameFormat</p>
 * <p>Description: </p><p>
 * 框架默认日期时间格式工具类。
 * 通过集成这个抽象类可以实现不同格式的日期时间格式
 * </p>
 * @author Copperfield Zhang
 * @date 2016年3月13日 下午2:32:48
 */
public abstract class AbstractFrameDateFormat implements FrameDateFormat{
	//默认日期格式化对象
	protected DateFormat dateFormat;
	//默认时间格式化对象
	protected DateFormat timeFormat;
	//默认日期时间格式化对象
	protected DateFormat dateTimeFormat;
	
	protected final long ONE_DAY_LENGTH;
	/**
	 * 自定义格式化对象
	 * @param dateFormat
	 * @param timeFormat
	 * @param dateTimeFormat
	 */
	protected AbstractFrameDateFormat(DateFormat dateFormat, DateFormat timeFormat,
			DateFormat dateTimeFormat) {
		super();
		this.dateFormat = dateFormat;
		this.timeFormat = timeFormat;
		this.dateTimeFormat = dateTimeFormat;
		Calendar cal = Calendar.getInstance();
		long tm = cal.getTimeInMillis();
		cal.add(Calendar.DATE, 1);
		ONE_DAY_LENGTH = cal.getTimeInMillis() - tm;
	}

	@Override
	public String format(Date date, String pattern) {
		//通过传入的字符串来构造一个format对象
		DateFormat format  = null;
		try {
			format = new SimpleDateFormat(pattern);
		} catch (Exception e) {
		}
		return this.format(date, format);
	}

	@Override
	public String format(Date date, DateFormat format) {
		if(date != null && format != null){
			try {
				return format.format(date);
			} catch (Exception e) {
			}
		}
		return null;
	}

	@Override
	public String formatDate(Date date) {
		return this.format(date, this.dateFormat);
	}

	@Override
	public String formatDateTime(Date date) {
		return this.format(date, this.dateTimeFormat);
	}

	@Override
	public String formatTime(Date date) {
		return this.format(date, this.timeFormat);
	}

	@Override
	public Date parse(String dateStr) {
		Date result = null;
		if(dateStr != null){
			try {
				result = this.dateTimeFormat.parse(dateStr);
			} catch (Exception e) {
				try {
					result = this.dateFormat.parse(dateStr);
				} catch (Exception e1) {
					try {
						result = this.timeFormat.parse(dateStr);
					} catch (ParseException e2) {
					}
				}
			}
		}
		return result;
	}

	@Override
	public Date parse(String dateStr, DateFormat format) {
		if(dateStr != null && format != null){
			try {
				return format.parse(dateStr);
			} catch (ParseException e) {
			}
		}
		return null;
	}

	@Override
	public Date parse(String dateStr, String formatStr) {
		DateFormat format = null;
		try {
			format = new SimpleDateFormat(formatStr);
		} catch (Exception e) {
		}
		return this.parse(dateStr, format);
	}


	@Override
	public Date[] splitDateRange(String dateRange, String spliter){
		Date[] result = new Date[2];
		if(dateRange != null){
			String[] strs = dateRange.split(spliter, 2);
			try {
				result[0] = dateTimeFormat.parse(strs[0]);
			} catch (ParseException e) {
				try {
					result[0] = dateFormat.parse(strs[0]);
				} catch (ParseException e1) {
				}
			}
			if(strs.length > 1){
				try {
					result[1] = dateTimeFormat.parse(strs[1]);
				} catch (ParseException e) {
					try {
						result[1] = dateFormat.parse(strs[1]);
					} catch (ParseException e1) {
					}
				}
			}
		}
		return result;
	}

	@Override
	public Date[] splitDateRange(String dateRange){
		return splitDateRange(dateRange, "~");
	}
	

	@Override
	public Date getTheDayZero(Date theDay){
		if(theDay != null){
			Calendar c = Calendar.getInstance();
			c.setTime(theDay);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			return c.getTime();
		}
		return null;
	}
	
	@Override
	public Date incDay(Date datetime, int incDay){
		Calendar cal = Calendar.getInstance();
		cal.setTime(datetime);
		cal.add(Calendar.DATE, incDay);
		return cal.getTime();
	}
	
	@Override
	public boolean inDay(Date date, Date theDay) {
		Date zero = getTheDayZero(theDay);
		long sub = date.getTime() - zero.getTime();
		if(sub <= ONE_DAY_LENGTH){
			return true;
		}
		return false;
	}
	
	
	@Override
	public Date getTheDayOfWeek(Date base, int startDayOfWeek, int dayAddition, int HH,
			int mm, int ss) {
		return getTheDayOfWeek(base, startDayOfWeek, dayAddition, HH, mm, ss, 0);
	}
	
	public Date getTheDayOfWeek(Date base, int startDayOfWeek, int dayAddition, int HH,
			int mm, int ss, int mss) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(base);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		if(dayOfWeek == Calendar.SUNDAY) {
			dayOfWeek = Calendar.SUNDAY + 7;
		}
		int sub = startDayOfWeek - dayOfWeek;
		cal.add(Calendar.DAY_OF_WEEK, sub);
		
		cal.add(Calendar.DATE, dayAddition);
		cal.set(Calendar.HOUR_OF_DAY, HH);
		cal.set(Calendar.MINUTE, mm);
		cal.set(Calendar.SECOND, ss);
		cal.set(Calendar.MILLISECOND, mss);
		return cal.getTime();
	}
	
	@Override
	public Date getTheDayOfWeek(int startDayOfWeek, int dayAddition) {
		return getTheDayOfWeek(new Date(), startDayOfWeek, dayAddition, 0, 0, 0);
	}
	
	@Override
	public Date[] getTheWeekRange(Date date, Integer weekStart) {
		if(weekStart == null) weekStart = Calendar.MONDAY;
		Date[] result = new Date[2];
		
		result[0] = getTheDayOfWeek(date, weekStart, 0, 0, 0, 0);
		result[1] = getTheDayOfWeek(date, weekStart, 6, 23, 59, 59, 0);
		return result;
	}
	
	
}