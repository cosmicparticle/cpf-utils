package cho.carbon.hc.copframe.utils.excel;

import java.text.DateFormat;
import java.util.List;

public interface ExcelRow {
	ExcelCellReader getCell(int var1);

	String getString(String var1);

	String getString(int var1);

	String getStringWithBlank(String var1);

	String getStringWithBlank(int var1);

	Long getDateLong(int var1, DateFormat... var2);

	Long getDateLong(String var1, DateFormat... var2);

	Integer getInt(Integer var1);

	Integer getInt(String var1);

	int getRowNum();

	List<String> cellTextList();

	ExcelRow setValue(int var1, String var2);

	ExcelRow resetValue(int var1);

	ExcelRow resetAllValue();

	static int toColumnIndex(String position) throws ExcelException {
		int index = 0;
		if (position != null && position.matches("^[A-Za-z]*$")) {
			char[] chars = position.toUpperCase().toCharArray();

			for (int i = chars.length - 1; i >= 0; --i) {
				index = (int) ((double) index + (double) (chars[i] - 65 + (i < chars.length - 1 ? 1 : 0))
						* Math.pow(26.0D, (double) (chars.length - i - 1)));
			}

			return index;
		} else {
			throw new ExcelException("传入的字符串必须是由英文字母组成");
		}
	}

	static String toColumnPosition(int index) throws ExcelException {
		if (index < 0) {
			throw new ExcelException("传入的索引必须大于等于0");
		} else {
			String ret = "";
			byte A = 65;

			do {
				int cur = index % 26;
				char c = (char) (A + cur);
				ret = c + ret;
				index /= 26;
			} while (index >= 26);

			if (index != 0) {
				ret = (char) (A + index - 1) + ret;
			}

			return ret;
		}
	}
}