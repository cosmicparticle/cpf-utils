package cho.carbon.hc.copframe.utils.excel;

import java.util.function.Function;

public interface IterateRule {
	default int getBegin() {
		return 2;
	}

	default int getMaxIgnoreRows() {
		return 1;
	}

	Function<ExcelRow, Boolean> getIgnoredRowChecker();

	default int getHeaderRowNo() {
		return 1;
	}
}