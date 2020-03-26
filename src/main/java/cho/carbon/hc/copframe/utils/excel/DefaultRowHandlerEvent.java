package cho.carbon.hc.copframe.utils.excel;

public class DefaultRowHandlerEvent implements RowHandlerEvent {
	private boolean breakFlag = false;

	public boolean isBreak() {
		return this.breakFlag;
	}

	public void setTotalProgress(int rowCount) {
	}

	public void setCurrentProgress(int prevRowNum) {
	}
}