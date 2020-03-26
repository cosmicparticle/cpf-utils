package cho.carbon.hc.copframe.utils.excel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum ExcelVersion {
	XLS("xls"), XLSX("xlsx");

	private String suffix;

	private ExcelVersion(String suffix) {
		this.suffix = suffix;
	}

	public boolean matchFileName(String fileName) {
		Pattern pattern = Pattern.compile(".+\\." + this.suffix + "$");
		Matcher matcher = pattern.matcher(fileName);
		return matcher.matches();
	}
}