package cho.carbon.hc.copframe.utils.excel;

public class ExcelException extends RuntimeException {
	private static final long serialVersionUID = 6251780074632678008L;
	private boolean forBreak = false;

	public ExcelException(String msg) {
		super(msg);
	}

	public ExcelException(String msg, Throwable e) {
		super(msg, e);
	}

	public ExcelException(boolean forBreak, String msg) {
		super(msg);
		this.forBreak = forBreak;
	}

	public boolean forBreak() {
		return this.forBreak;
	}
}