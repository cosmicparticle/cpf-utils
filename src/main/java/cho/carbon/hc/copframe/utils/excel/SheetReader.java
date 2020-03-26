package cho.carbon.hc.copframe.utils.excel;

public interface SheetReader {
	ExcelRow getRow(int var1);

	int getRowCount();

	int getRowCount(int var1, int var2);

	CheckResult check(CheckOptions var1);

	int iterateRow(IterateRule var1, RowHandler var2);
}