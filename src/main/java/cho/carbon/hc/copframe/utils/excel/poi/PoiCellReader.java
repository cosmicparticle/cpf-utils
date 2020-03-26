package cho.carbon.hc.copframe.utils.excel.poi;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

import cho.carbon.hc.copframe.utils.FormatUtils;
import cho.carbon.hc.copframe.utils.excel.CellTypeUtils;
import cho.carbon.hc.copframe.utils.excel.ExcelCellReader;
import cho.carbon.hc.copframe.utils.excel.ExcelException;

public class PoiCellReader implements ExcelCellReader {
	private Cell cell;
	private DateFormat defaultDateFormat = new SimpleDateFormat("yyyy年MM月dd日");

	public PoiCellReader(Cell cell) {
		this.cell = cell;
	}

	public boolean isEmpty() {
		String str = this.getString();
		return str == null || str.isEmpty();
	}

	private String getStringWithBlank(Cell cell) {
		if (cell == null) {
			return null;
		} else {
			int cellType = cell.getCellType();
			switch (cellType) {
				case 0 :
					if (CellTypeUtils.isCellDateFormatted(cell)) {
						return this.defaultDateFormat.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
					}

					return FormatUtils.toString(FormatUtils.toLong(cell.getNumericCellValue()));
				case 1 :
					return cell.getStringCellValue();
				case 2 :
					FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper()
							.createFormulaEvaluator();
					CellValue cellValue = evaluator.evaluate(cell);
					switch (cellValue.getCellType()) {
						case 0 :
							return FormatUtils.toString(FormatUtils.toLong(cellValue.getNumberValue()));
						case 1 :
							return cellValue.getStringValue();
						default :
							return null;
					}
				default :
					return null;
			}
		}
	}

	private String removeBlank(String str) {
		return str != null ? str.replace(" ", "").replace("　", "") : str;
	}

	public String getString() {
		return this.removeBlank(this.getStringWithBlank());
	}

	public String getStringWithBlank() {
		return this.getStringWithBlank(this.cell);
	}

	public Date getDate(DateFormat... dateFormats) {
		String value = this.getString();
		Date date = null;
		if (value != null && !value.isEmpty()) {
			if (dateFormats.length > 0) {
				boolean hasParsed = false;
				DateFormat[] var8 = dateFormats;
				int var7 = dateFormats.length;
				int var6 = 0;

				while (var6 < var7) {
					DateFormat df = var8[var6];

					try {
						date = df.parse(value);
						hasParsed = true;
						break;
					} catch (ParseException var11) {
						++var6;
					}
				}

				if (hasParsed) {
					throw new ExcelException("传入的所有日期格式都不能成功转换时间字符串[" + value + "]");
				}
			} else {
				try {
					date = this.defaultDateFormat.parse(value);
				} catch (ParseException var10) {
					throw new ExcelException("默认日期个是无法转换日期字符串", var10);
				}
			}

			return date;
		} else {
			return null;
		}
	}

	public Long getDateLong(DateFormat... dateFormats) {
		Date date = this.getDate(dateFormats);
		return date != null ? date.getTime() : null;
	}

	public Integer getInt() {
		String value = this.getString();
		return FormatUtils.toInteger(value);
	}

	public Long getLong() {
		String value = this.getString();
		return FormatUtils.toLong(value);
	}

	public int getColumnIndex() {
		return this.cell.getColumnIndex();
	}

	public int getRowNum() {
		return this.cell.getRowIndex() + 1;
	}
}