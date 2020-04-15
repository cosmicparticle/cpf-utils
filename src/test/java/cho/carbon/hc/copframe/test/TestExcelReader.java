package cho.carbon.hc.copframe.test;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Test;

import cho.carbon.hc.copframe.utils.TreeTable;
import cho.carbon.hc.copframe.utils.excel.ExcelReader;
import cho.carbon.hc.copframe.utils.excel.SheetReader;
import cho.carbon.hc.copframe.utils.excel.poi.PoiExcelReader;

public class TestExcelReader {
	Logger logger = LoggerFactory.getLogger(TestExcelReader.class);

//	@Test
	public void readTest() {
		File file = new File("d://1.xls");
		ExcelReader reader = PoiExcelReader.createReader(file);
		SheetReader sheet = reader.getSheet(0);
		TreeTable<String> table = new TreeTable();
		sheet.iterateRow(() -> {
			return (row) -> {
				return !row.getCell(0).isEmpty();
			};
		}, (row, e) -> {
			table.put(row.getRowNum(), 0, row.getString(0));
			System.out.println(row.getRowNum());
		});
	}
}
