package com.nokor.efinance.accounting;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.nokor.ersys.finance.accounting.model.JournalEvent;

/**
 * @author bunlong.taing
 */
public class TestCreateScriptJournalEvent extends BaseAccountingTestCase {
	
	private static final String SOURCE_FOLDER = "/home/bunlong.taing/Desktop/";
	private static final String SOURCE_FILE_NAME = SOURCE_FOLDER + "Receipt_Code.xlsx";
	private static final String DES_FILE_NAME = SOURCE_FOLDER + "Receipt_Code.sql";
	
	private static final String JOURNAL_ID = "5";
	private static final String JOURNAL_EVENT_TEMPLATE = "INSERT INTO tu_journal_event(jou_eve_id, jou_id, jou_eve_code, "
			+ "jou_eve_desc, jou_eve_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)"
			+ "\n\tVALUES";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// !! NOTE
		// Run Java Application, not JUnit test
		loadDataFromExcel();
	}
	
	/**
	 */
	private static void loadDataFromExcel() {
		try {
			FileInputStream fis = new FileInputStream(SOURCE_FILE_NAME);
			Workbook workbook = new XSSFWorkbook(fis);
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			createJournalEventScript(rowIterator);
			fis.close();
			workbook.close();
		} catch (FileNotFoundException e) {
			logger.debug("Errors: File not found", e);
		} catch (IOException e) {
			logger.debug("Errors: IOException", e);
		}
	}
	
	/**
	 * @param rowIterator
	 */
	private static void createJournalEventScript(Iterator<Row> rowIterator) {
		List<JournalEvent> journalEvents = new ArrayList<JournalEvent>();
		long journalEventId = 1l;
		
		// skip the first row: Heading
		rowIterator.next();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			JournalEvent journalEvent = createJournalEvent(row);
			journalEvent.setId(journalEventId++);
			journalEvents.add(journalEvent);
		}
		generateJournalScript(journalEvents);
	}
	
	/**
	 * @param journalEvents
	 */
	private static void generateJournalScript(List<JournalEvent> journalEvents) {
		StringBuffer content = new StringBuffer(JOURNAL_EVENT_TEMPLATE);
		// JournalEntry script
		for (int i = 0; i < journalEvents.size(); i++) {
			JournalEvent journalEvent = journalEvents.get(i);
			content.append("\n\t(")
					.append(journalEvent.getId() + ", " + JOURNAL_ID + ", '")
					.append(journalEvent.getCode() + "', '")
					.append(journalEvent.getDesc() + "', '")
					.append(journalEvent.getDescEn() + "', ")
					.append(StringUtils.leftPad("1, 1, now(), 'admin', now(), 'admin')", 100))
					.append((i < journalEvents.size() - 1) ? "," : ";");
		}
		logger.debug("Finish generate journal script");
		writeToFile(content.toString(), DES_FILE_NAME);
	}
	
	/**
	 * Create Journal Event
	 * @param row
	 * @return
	 */
	private static JournalEvent createJournalEvent(Row row) {
		JournalEvent journalEvent = new JournalEvent();
		journalEvent.setCode(getString(row.getCell(0)));
		journalEvent.setDesc(getString(row.getCell(1)));
		journalEvent.setDescEn(getString(row.getCell(2)));
		return journalEvent;
	}

}
