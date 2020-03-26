package cho.carbon.hc.copframe.utils.excel;

import java.text.DateFormat;
import java.util.Date;

public interface ExcelCellReader {
	boolean isEmpty();

	String getStringWithBlank();

	String getString();

	Long getDateLong(DateFormat... var1);

	Date getDate(DateFormat... var1);

	Integer getInt();

	Long getLong();

	int getColumnIndex();

	int getRowNum();
}