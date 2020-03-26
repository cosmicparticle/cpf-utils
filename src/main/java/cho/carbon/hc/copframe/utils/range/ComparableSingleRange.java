package cho.carbon.hc.copframe.utils.range;


public class ComparableSingleRange<T extends Comparable<T>> implements Range<T>{

	private T begin;
	private T end;
	private boolean beginExcl = false;
	private boolean endExcl = false;
	
	public ComparableSingleRange(T begin, T end) {
		super();
		this.begin = begin;
		this.end = end;
	}
	public T getBegin() {
		return begin;
	}
	public void setBegin(T begin) {
		this.begin = begin;
	}
	public T getEnd() {
		return end;
	}
	public void setEnd(T end) {
		this.end = end;
	}
	
	@Override
	public boolean inRange(T point) {
		if(point == null){
			throw new IllegalArgumentException("参数不能为NULL");
		}
		boolean beginSuit = begin == null || point.compareTo(begin) < 0 || (!beginExcl && point.compareTo(begin) == 0),
				endSuit = end == null || point.compareTo(end) > 0 || (!endExcl && point.compareTo(end) == 0);
		return beginSuit && endSuit;
	}
	public boolean excludeBegin() {
		return beginExcl;
	}
	public void excludeBegin(boolean isExclude) {
		this.beginExcl = isExclude;
	}
	public boolean excludeEnd() {
		return endExcl;
	}
	public void excludeEnd(boolean isExclude) {
		this.endExcl = isExclude;
	}
}
