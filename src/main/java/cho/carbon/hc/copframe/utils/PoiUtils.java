package cho.carbon.hc.copframe.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * 
 * @author trx
 */
public final class PoiUtils {

	public PoiUtils() {
	}

	public static void copySheets(Sheet source, Sheet target) {
		copySheets(source, target, true);
	}
	
	public static void copySheets(Sheet source, Sheet target,
			boolean copyStyle) {
		TreeSet<Integer> rownums = new TreeSet<>();
		for (int i = source.getFirstRowNum(); i <= source.getLastRowNum(); i++) {
			rownums.add(i);
		}
		copySheets(source, target, rownums, true, copyStyle);
	}
	
	public static void copySheets(Sheet source, Sheet target, SortedSet<Integer> rownums, boolean retainRownum, boolean copyStyle) {
		int maxColumnNum = 0;
		Map<Integer, CellStyle> styleMap = (copyStyle) ? new HashMap<Integer, CellStyle>()
				: null;
		int index = 0;
		for (int rownum : rownums) {
			Row srcRow = source.getRow(rownum);
			Row destRow = target.createRow(retainRownum? rownum: index++);
			if (srcRow != null) {
				PoiUtils.copyRow(source, target, srcRow, destRow,
						styleMap);
				if (srcRow.getLastCellNum() > maxColumnNum) {
					maxColumnNum = srcRow.getLastCellNum();
				}
			}
		}
		for (int i = 0; i <= maxColumnNum; i++) {    //设置列宽
			target.setColumnWidth(i, source.getColumnWidth(i));
		}
	}

	
	
	

	/**
	 * 复制并合并单元格
	 * @param newSheet
	 * @param sheet
	 * @param copyStyle
	 */
	public static void copyRow(Sheet srcSheet, Sheet destSheet,
			Row srcRow, Row destRow,
			Map<Integer, CellStyle> styleMap) {
		Set<CellRangeAddressWrapper> mergedRegions = new TreeSet<CellRangeAddressWrapper>();
		destRow.setHeight(srcRow.getHeight());
		int deltaRows = destRow.getRowNum() - srcRow.getRowNum(); //如果copy到另一个sheet的起始行数不同
		for (int j = srcRow.getFirstCellNum(); j <= srcRow.getLastCellNum(); j++) {
			Cell oldCell = srcRow.getCell(j); // old cell
			Cell newCell = destRow.getCell(j); // new cell
			if (oldCell != null) {
				if (newCell == null) {
					newCell = destRow.createCell(j);
				}
				copyCell(oldCell, newCell, styleMap);
				CellRangeAddress mergedRegion = getMergedRegion(srcSheet,
						srcRow.getRowNum(), (short) oldCell.getColumnIndex());
				if (mergedRegion != null) {
					CellRangeAddress newMergedRegion = new CellRangeAddress(
							mergedRegion.getFirstRow() + deltaRows,
							mergedRegion.getLastRow() + deltaRows, mergedRegion
									.getFirstColumn(), mergedRegion
									.getLastColumn());
					CellRangeAddressWrapper wrapper = new CellRangeAddressWrapper(
							newMergedRegion);
					if (isNewMergedRegion(wrapper, mergedRegions)) {
						mergedRegions.add(wrapper);
						destSheet.addMergedRegion(wrapper.range);
					}
				}
			}
		}
	}

	/**
	 * 把原来的Sheet中cell（列）的样式和数据类型复制到新的sheet的cell（列）中
	 * 
	 * @param oldCell
	 * @param newCell
	 * @param styleMap
	 */
	public static void copyCell(Cell oldCell, Cell newCell,
			Map<Integer, CellStyle> styleMap) {
		if (styleMap != null) {
			if (oldCell.getSheet().getWorkbook() == newCell.getSheet()
					.getWorkbook()) {
				newCell.setCellStyle(oldCell.getCellStyle());
			} else {
				int stHashCode = oldCell.getCellStyle().hashCode();
				CellStyle newCellStyle = styleMap.get(stHashCode);
				if (newCellStyle == null) {
					newCellStyle = newCell.getSheet().getWorkbook()
							.createCellStyle();
					newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
					styleMap.put(stHashCode, newCellStyle);
				}
				newCell.setCellStyle(newCellStyle);
			}
		}
		if(CellType.STRING == oldCell.getCellTypeEnum()) {
			newCell.setCellValue(oldCell.getStringCellValue());
		}else if(CellType.NUMERIC == oldCell.getCellTypeEnum()) {
			newCell.setCellValue(oldCell.getNumericCellValue());
		}else if(CellType.BLANK == oldCell.getCellTypeEnum()) {
			newCell.setCellType(CellType.BLANK);
		}else if(CellType.BOOLEAN == oldCell.getCellTypeEnum()) {
			newCell.setCellValue(oldCell.getBooleanCellValue());
		}else if(CellType.ERROR == oldCell.getCellTypeEnum()) {
			newCell.setCellErrorValue(oldCell.getErrorCellValue());
		}else if(CellType.FORMULA == oldCell.getCellTypeEnum()) {
			newCell.setCellFormula(oldCell.getCellFormula());
		}

	}

	// 获取merge对象
	public static CellRangeAddress getMergedRegion(Sheet sheet, int rowNum,
			short cellNum) {
		for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
			CellRangeAddress merged = sheet.getMergedRegion(i);
			if (merged.isInRange(rowNum, cellNum)) {
				return merged;
			}
		}
		return null;
	}

	private static boolean isNewMergedRegion(
			CellRangeAddressWrapper newMergedRegion,
			Set<CellRangeAddressWrapper> mergedRegions) {
		boolean bool = mergedRegions.contains(newMergedRegion);
		return !bool;
	}
	
	
	

}