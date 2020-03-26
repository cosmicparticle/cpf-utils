package cho.carbon.hc.copframe.utils.excel;

public class CheckResult {
	private boolean isSuc = false;
	private String msg;
	private String reason;

	public CheckResult(boolean defaultResult, String defaultMsg) {
		this.isSuc = defaultResult;
		this.msg = defaultMsg;
	}

	public boolean isSuc() {
		return this.isSuc;
	}

	public CheckResult isSuc(boolean isSuc) {
		this.isSuc = isSuc;
		return this;
	}

	public String getMsg() {
		return this.msg;
	}

	public CheckResult setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	public CheckResult setResult(boolean isSuc, String reason) {
		this.isSuc = isSuc;
		this.reason = reason;
		return this;
	}

	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}