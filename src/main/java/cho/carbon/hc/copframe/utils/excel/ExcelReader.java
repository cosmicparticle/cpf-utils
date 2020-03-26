package cho.carbon.hc.copframe.utils.excel;

import java.io.InputStream;

public interface ExcelReader {
	void read(InputStream var1) throws ExcelException;

	boolean hasRead();

	SheetReader getSheet(int var1);

	SheetReader getSheet(String var1);

	String[] getSheetNames();

	int getSheetCount();
}