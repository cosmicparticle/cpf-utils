package cho.carbon.hc.copframe.utils.excel.poi;

import java.util.TreeSet;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import cho.carbon.hc.copframe.utils.excel.CheckOptions;
import cho.carbon.hc.copframe.utils.excel.CheckResult;
import cho.carbon.hc.copframe.utils.excel.DefaultRowHandlerEvent;
import cho.carbon.hc.copframe.utils.excel.ExcelCellReader;
import cho.carbon.hc.copframe.utils.excel.ExcelException;
import cho.carbon.hc.copframe.utils.excel.ExcelRow;
import cho.carbon.hc.copframe.utils.excel.IterateRule;
import cho.carbon.hc.copframe.utils.excel.RowHandler;
import cho.carbon.hc.copframe.utils.excel.RowHandlerEvent;
import cho.carbon.hc.copframe.utils.excel.SheetReader;

public class PoiSheetReader implements SheetReader {
	private Sheet sheet;
	Logger logger = LoggerFactory.getLogger(PoiSheetReader.class);

	public PoiSheetReader(Sheet sheet) {
		if (sheet != null) {
			this.sheet = sheet;
		} else {
			throw new ExcelException("sheet为null");
		}
	}

	private ExcelRow buildRow(Row row) {
		return new PoiExcelRow(row);
	}

	public ExcelRow getRow(int rowNum) {
		if (rowNum > 0) {
			Row row = this.sheet.getRow(rowNum - 1);
			if (row != null) {
				return this.buildRow(row);
			}
		}

		return null;
	}

	public int getRowCount() {
		return this.sheet.getLastRowNum() + 1;
	}

	public int getRowCount(int markColumnIndex, int beginRowNo) {

		IterateRule rule = new IterateRule() {
			@Override
			public Function<ExcelRow, Boolean> getIgnoredRowChecker() {
				return (row) -> {
					ExcelCellReader cell = row.getCell(markColumnIndex);
					return !cell.isEmpty();
				};
			}

			public int getBegin() {
				return beginRowNo;
			}
		};

		Counter counter = new Counter();
		this.iterateRow(rule, (row, e) -> {
			counter.increase();
		});
		return counter.getCount();
	}

	public CheckResult check(CheckOptions options) {
		CheckResult result = new CheckResult(true, "�?测完�?");
		return result;
	}

	public int iterateRow(IterateRule rule, RowHandler handler) {
		int beginNum = rule.getBegin();
		int readRowCount = 0;
		Row cRow = this.sheet.getRow(beginNum - 1);
		int prevRowNum = beginNum - 1;
		TreeSet<Integer> ignoredRowNums = new TreeSet();
		TreeSet<Integer> handledRownums = new TreeSet();
		RowHandlerEvent event = new DefaultRowHandlerEvent();
		event.setTotalProgress(this.getRowCount());

		do {
			event.setCurrentProgress(prevRowNum);
			int nextRownum = this.getNextRowNum(cRow, rule);
			if (nextRownum > 0) {
				ExcelRow row = this.buildRow(cRow);
				int gap = row.getRowNum() - prevRowNum;
				readRowCount += gap;

				while (gap > 1) {
					--gap;
					ignoredRowNums.add(prevRowNum + gap);
				}

				try {
					handler.execute(row, event);
					handledRownums.add(row.getRowNum());
					if (event.isBreak()) {
						break;
					}
				} catch (Exception var14) {
					this.logger.error(var14.toString());
				}
			}

			int absNextRowNum = Math.abs(nextRownum);
			if (absNextRowNum <= 1) {
				break;
			}

			cRow = this.sheet.getRow(absNextRowNum - 1);
		} while (cRow != null);

		this.logger.debug("遍历结束");
		return readRowCount;
	}

	private int getNextRowNum(Row currentRow, IterateRule rule) {
		ExcelRow row;
		try {
			row = this.buildRow(currentRow);
		} catch (ExcelException var9) {
			return 0;
		}

		boolean currentOk = (Boolean) rule.getIgnoredRowChecker().apply(row);
		Row nextRow = currentRow.getSheet().getRow(currentRow.getRowNum() + 1);

		for (int var6 = rule.getMaxIgnoreRows(); nextRow != null
				&& var6-- >= 0; nextRow = nextRow.getSheet().getRow(nextRow.getRowNum() + 1)) {
			ExcelRow nextERow;
			try {
				nextERow = this.buildRow(nextRow);
			} catch (ExcelException var10) {
				break;
			}

			boolean nextRowOk = (Boolean) rule.getIgnoredRowChecker().apply(nextERow);
			if (nextRowOk) {
				return nextERow.getRowNum();
			}
		}

		return currentOk ? 1 : -1;
	}


	public class Counter {
		int count;

		Counter() {
			this.count = 0;
		}

		public int increase() {
			return ++this.count;
		}

		public int decrease() {
			return --this.count;
		}

		public int getCount() {
			return this.count;
		}
	}
}
