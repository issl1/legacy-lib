package com.nokor.efinance.accounting;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.apache.poi.ss.usermodel.Cell;

import com.nokor.ersys.finance.accounting.tools.helper.ErsysAccountingAppServicesHelper;
import com.nokor.frmk.testing.BaseTestCase;

/**
 * @author bunlong.taing
 */
public class BaseAccountingTestCase extends BaseTestCase implements ErsysAccountingAppServicesHelper {
	
	/**
	 * @param content
	 * @param dest
	 */
	protected static void writeToFile(String content, String dest) {
		try {
			Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dest), "utf-8"));
			writer.write(content);
			writer.close();
			logger.debug("Write complete: " + dest);
		} catch (UnsupportedEncodingException e) {
			logger.debug("Error: not supported encoding", e);
		} catch (FileNotFoundException e) {
			logger.debug("Error: file not found", e);
		} catch (IOException e) {
			logger.debug("Error: IOException", e);
		}
	}
	
	/**
	 * @param cell
	 * @return
	 */
	protected static String getString(Cell cell) {
		if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
			return String.valueOf((long) cell.getNumericCellValue());
		}
		return cell.getStringCellValue();
	}

}
