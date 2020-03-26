package cho.carbon.hc.copframe.utils.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;

public class CellTypeUtils {
	public static boolean isCellDateFormatted(Cell cell) {
		if (cell == null) {
			return false;
		} else {
			boolean bDate = false;
			double d = cell.getNumericCellValue();
			if (DateUtil.isValidExcelDate(d)) {
				CellStyle style = cell.getCellStyle();
				if (style == null) {
					return false;
				}

				int i = style.getDataFormat();
				String f = style.getDataFormatString();
				bDate = isADateFormat(i, f);
			}

			return bDate;
		}
	}

	public static boolean isADateFormat(int formatIndex, String formatString) {
		String fs = formatString.replaceAll("[\"|']", "").replaceAll("[日|秒]", "").replaceAll("[年|月]", "-")
				.replaceAll("[时|分]", ":");
		return DateUtil.isADateFormat(formatIndex, fs);
	}

	public static void main(String[] args) {
		String pa = "yyyy\"年\"mm\"月\"dd\"日\"";
		System.out.println(isADateFormat(0, pa));
	}
}