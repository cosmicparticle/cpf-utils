package cho.carbon.hc.copframe.utils.excel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SheetHeader {
	private ExcelRow headerRow;
	private Map<String, Integer> cnameIndexMap = new HashMap();
	Logger logger = LoggerFactory.getLogger(SheetHeader.class);

	public SheetHeader(ExcelRow headerRow) {
		this.headerRow = headerRow;
	}

	public int getColumnIndexByCname(String headerCname) {
		if (this.cnameIndexMap.containsKey(headerCname)) {
			return (Integer) this.cnameIndexMap.get(headerCname);
		} else {
			List<String> headerList = this.headerRow.cellTextList();
			int index = headerList.indexOf(headerCname);
			this.cnameIndexMap.put(headerCname, index);
			return index;
		}
	}

	public void handleCells(ExcelRow row, String[] typeintParam, Consumer<ExcelCellReader> cellHandler) {
		String[] var7 = typeintParam;
		int var6 = typeintParam.length;

		for (int var5 = 0; var5 < var6; ++var5) {
			String cname = var7[var5];
			int index = this.getColumnIndexByCname(cname);
			if (index >= 0) {
				ExcelCellReader cell = row.getCell(index);
				if (cell != null) {
					try {
						cellHandler.accept(cell);
					} catch (ExcelException var11) {
						this.logger.error("æŠ›å‡ºExcelException", var11);
						if (var11.forBreak()) {
							break;
						}
					} catch (Exception var12) {
						this.logger.error("处理单元时发生错误", var12);
					}
				}
			}
		}

	}
}
