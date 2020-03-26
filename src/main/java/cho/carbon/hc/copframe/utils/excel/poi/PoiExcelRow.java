package cho.carbon.hc.copframe.utils.excel.poi;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Function;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import cho.carbon.hc.copframe.utils.excel.ExcelCellReader;
import cho.carbon.hc.copframe.utils.excel.ExcelException;
import cho.carbon.hc.copframe.utils.excel.ExcelRow;

public class PoiExcelRow implements ExcelRow {
	private Row row;
	private TreeMap<Integer, String> coverValueMap = new TreeMap();

	public PoiExcelRow(Row row) {
		if (row != null) {
			this.row = row;
		} else {
			throw new ExcelException("传入的Row对象不能为null");
		}
	}

	public ExcelCellReader getCell(int cellIndex) {
		return new PoiCellReader(this.row.getCell(cellIndex));
	}

	public int getRowNum() {
		return this.row.getRowNum() + 1;
	}

	public List<String> cellTextList() {
		ArrayList<String> list = new ArrayList();
		Iterator it = this.row.cellIterator();

		while (it.hasNext()) {
			Cell cell = (Cell) it.next();
			if (cell != null) {
				String value = this.getString(cell.getColumnIndex());
				list.add(value);
			} else {
				list.add((String) null);
			}
		}

		return list;
	}

	public ExcelRow setValue(int columnIndex, String value) {
		this.coverValueMap.put(columnIndex, value);
		return this;
	}

	public ExcelRow resetValue(int index) {
		this.coverValueMap.remove(index);
		return this;
	}

	public ExcelRow resetAllValue() {
		this.coverValueMap.clear();
		return this;
	}

	private String select(int columnIndex, Function<ExcelCellReader, String> func) {
		if (this.coverValueMap.containsKey(columnIndex)) {
			return (String) this.coverValueMap.get(columnIndex);
		} else {
			ExcelCellReader cell = this.getCell(columnIndex);
			return cell != null ? (String) func.apply(cell) : null;
		}
	}

	public String getString(String columnPosition) {
		return this.getString(ExcelRow.toColumnIndex(columnPosition));
	}

	public String getString(int columnIndex) {
		return this.select(columnIndex, (cell) -> {
			return cell.getString();
		});
	}

	public String getStringWithBlank(String columnPosition) {
		return this.getStringWithBlank(ExcelRow.toColumnIndex(columnPosition));
	}

	public String getStringWithBlank(int columnIndex) {
		return this.select(columnIndex, (cell) -> {
			return cell.getStringWithBlank();
		});
	}

	public Long getDateLong(int columnIndex, DateFormat... dateFormats) {
		ExcelCellReader cell = this.getCell(columnIndex);
		return cell != null ? cell.getDateLong(dateFormats) : null;
	}

	public Long getDateLong(String columnPosition, DateFormat... dateFormats) {
		return this.getDateLong(ExcelRow.toColumnIndex(columnPosition), dateFormats);
	}

	public Integer getInt(Integer columnIndex) {
		ExcelCellReader cell = this.getCell(columnIndex);
		return cell != null ? cell.getInt() : null;
	}

	public Integer getInt(String columnPosition) {
		return this.getInt(ExcelRow.toColumnIndex(columnPosition));
	}
}