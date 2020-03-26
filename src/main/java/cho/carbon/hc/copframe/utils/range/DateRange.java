package cho.carbon.hc.copframe.utils.range;

import java.util.Date;

public class DateRange extends ComparableSingleRange<Date>{

	public DateRange(Date begin, Date end) {
		super(begin, end);
	}
	
	public ComparableSingleRange<Long> toLongRange(){
		Date begin = getBegin(),
				end = getEnd();
		return new ComparableSingleRange<Long>(begin == null? null: begin.getTime(), end == null? null: end.getTime());
	}
}
