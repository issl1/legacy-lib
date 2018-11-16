package com.nokor.efinance.accounting;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.criterion.Restrictions;

import com.nokor.ersys.finance.accounting.model.JournalEvent;
import com.nokor.ersys.finance.accounting.model.JournalEventAccount;
import com.nokor.ersys.finance.accounting.service.AccountRestriction;

/**
 * @author bunlong.taing
 */
public class TestCreateScriptJournalEventAccount extends BaseAccountingTestCase {
	
	private static final String SOURCE_FOLDER = "/home/bunlong.taing/Desktop/";
	private static final String SOURCE_FILE_NAME = SOURCE_FOLDER + "Receive_Mapping_COA.xlsx";
	private static final String DES_FILE_NAME = SOURCE_FOLDER + "Receive_Mapping_COA.sql";
	
	private static final String JOURNAL_EVENT_ACCOUNT_TEMPLATE = "INSERT INTO tu_journal_event_account(jou_eve_acc_id, "
			+ "jou_eve_id, acc_id, jou_eve_acc_is_debit, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)"
			+ "\n\tVALUES";

	/**
	 * @see com.nokor.frmk.testing.BaseTestCase#isRequiredAuhentication()
	 */
	@Override
	protected boolean isRequiredAuhentication() {
		return false;
	}
	
	/**
	 * @see com.nokor.frmk.testing.BaseTestCase#setAuthentication()
	 */
	@Override
	protected void setAuthentication() {
		login = "admin";
		password = "admin@EFIN";
	}
	
	/**
	 */
	public void testMain() {
		// !! DO NOT FORGET
		// Copy from ChartOfAccount.xlsx the 2 columns (Account_Code/Old_Account_Code) to the source file in the "AccountsCodes" tab)
		loadDataFromExcel();
	}
	
	/**
	 * @return
	 */
	private static void loadDataFromExcel() {
		try {
			FileInputStream fis = new FileInputStream(SOURCE_FILE_NAME);
			Workbook workbook = new XSSFWorkbook(fis);

			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			
			Map<String, String> codeMapping = accountCodeOldNewMapping(workbook.getSheet("AccountsCodes").iterator());
			createJournalEventAccountScript(rowIterator, codeMapping);
			
			fis.close();
			workbook.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param rowIterator
	 * @param codeMapping
	 */
	private static void createJournalEventAccountScript(Iterator<Row> rowIterator, Map<String, String> codeMapping) {
		List<JournalEventAccount> journalEventAccounts = new ArrayList<>();
		long journalEventAccountId = 1l;
		
		// skip the first row: Heading
		rowIterator.next();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			JournalEventAccount journalEventAccount = createJournalEventAccount(row, codeMapping);
			journalEventAccount.setId(journalEventAccountId++);
			journalEventAccounts.add(journalEventAccount);
		}
		generateJournalEventAccountScript(journalEventAccounts);
	}
	
	/**
	 * @param journalEventAccounts
	 */
	private static void generateJournalEventAccountScript(List<JournalEventAccount> journalEventAccounts) {
		StringBuffer content = new StringBuffer(JOURNAL_EVENT_ACCOUNT_TEMPLATE);
		// JournalEventAccount script
		for (int i = 0; i < journalEventAccounts.size(); i++) {
			JournalEventAccount journalEventAccount = journalEventAccounts.get(i);
			content.append("\n")
				.append((journalEventAccount.getAccount() != null ? "" : "-- "))
				.append("\t(")
				.append(journalEventAccount.getId() + ", ")
				.append((journalEventAccount.getEvent() != null ? journalEventAccount.getEvent().getId() : "null") + ", ")
				.append((journalEventAccount.getAccount() != null ? journalEventAccount.getAccount().getId() : "null") + ", ")
				.append(journalEventAccount.getIsDebit().toString() + ", ")
				.append(StringUtils.leftPad("1, now(), 'admin', now(), 'admin')", 20))
				.append((i < journalEventAccounts.size() - 1) ? "," : ";");
		}
		logger.debug("finish journal event account script");
		writeToFile(content.toString(), DES_FILE_NAME);
	}
	
	/**
	 * @param row
	 * @param codeMapping
	 * @return
	 */
	private static JournalEventAccount createJournalEventAccount(Row row, Map<String, String> codeMapping) {
		String eventCode = getString(row.getCell(1));
		boolean isDebit = "Debit".equals(getString(row.getCell(2)));
		String accountCode = getString(row.getCell(3));
		
		JournalEventAccount journalEventAccount = new JournalEventAccount();
		journalEventAccount.setEvent(ACCOUNTING_SRV.getByCode(JournalEvent.class, eventCode));
		AccountRestriction restrictions = new AccountRestriction();
		restrictions.addCriterion(Restrictions.eq("code", codeMapping.get(accountCode)));
		journalEventAccount.setAccount(ACCOUNTING_SRV.getFirst(restrictions));
		journalEventAccount.setIsDebit(isDebit);
		return journalEventAccount;
	}
	
	/**
	 * @param rowIterator
	 * @return
	 */
	private static Map<String, String> accountCodeOldNewMapping(Iterator<Row> rowIterator) {
		Map<String, String> codeMapping = new HashMap<>();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			codeMapping.put(getString(row.getCell(1)), getString(row.getCell(0)));
		}
		return codeMapping;
	}
	
}
