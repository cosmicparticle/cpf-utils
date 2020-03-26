package cho.carbon.hc.copframe.utils.excel.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cho.carbon.hc.copframe.utils.excel.ExcelException;
import cho.carbon.hc.copframe.utils.excel.ExcelReader;
import cho.carbon.hc.copframe.utils.excel.ExcelVersion;
import cho.carbon.hc.copframe.utils.excel.SheetReader;

public class PoiExcelReader implements ExcelReader {
	private Workbook workbook;
	private final ExcelVersion version;
	static Logger logger = Logger.getLogger(PoiExcelReader.class);

	PoiExcelReader(ExcelVersion version) {
		this.version = version;
	}

	public static ExcelReader createReader(File file) {
		if (file.isFile() && file.exists()) {
			try {
				return createReader((String) file.getName(), new FileInputStream(file));
			} catch (FileNotFoundException var2) {
				throw new ExcelException("Excel文件没有找到");
			}
		} else {
			throw new ExcelException("没有找到文件" + file);
		}
	}

	public static ExcelReader createReader(ExcelVersion version, InputStream inputStream) {
		if (version != null && inputStream != null) {
			ExcelReader reader = new PoiExcelReader(version);
			reader.read(inputStream);
			return reader;
		} else {
			throw new ExcelException("参数不能为空");
		}
	}

	public static ExcelReader createReader(String fileName, InputStream inputStream) {
		PoiExcelReader reader = null;
		if (ExcelVersion.XLS.matchFileName(fileName)) {
			reader = new PoiExcelReader(ExcelVersion.XLS);
		} else if (ExcelVersion.XLSX.matchFileName(fileName)) {
			reader = new PoiExcelReader(ExcelVersion.XLSX);
		}

		if (reader != null) {
			try {
				reader.read(inputStream);
				return reader;
			} catch (ExcelException var4) {
				throw new ExcelException("读取excel数据失败", var4);
			}
		} else {
			throw new ExcelException("根据文件名" + fileName + "没能匹配到对应的Excel版本");
		}
	}

	public void read(InputStream inputStream) throws ExcelException {
		try {
			if (this.version == ExcelVersion.XLS) {
				this.workbook = new HSSFWorkbook(inputStream);
			} else {
				this.workbook = new XSSFWorkbook(inputStream);
			}

		} catch (IOException var3) {
			throw new ExcelException("读取输入流时发生错误", var3);
		}
	}

	public boolean hasRead() {
		return this.workbook != null;
	}

	public SheetReader getSheet(int index) {
		return new PoiSheetReader(this.workbook.getSheetAt(index));
	}

	public SheetReader getSheet(String sheetName) {
		return new PoiSheetReader(this.workbook.getSheet(sheetName));
	}

	public String[] getSheetNames() {
		int sheetCount = this.getSheetCount();
		String[] names = new String[sheetCount];

		for (int i = 0; i < sheetCount; ++i) {
			names[i] = this.workbook.getSheetName(i);
		}

		return names;
	}

	public int getSheetCount() {
		return this.workbook.getNumberOfSheets();
	}
}