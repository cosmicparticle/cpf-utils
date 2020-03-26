package cho.carbon.hc.copframe.utils;

public class TableEntry<S> {
	private int x;
	private int y;
	private S value;
	
	
	public TableEntry(int x, int y, S value) {
		super();
		this.x = x;
		this.y = y;
		this.value = value;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public S getValue() {
		return value;
	}
}
