package com.nokor.efinance;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.criterion.Restrictions;

import com.nokor.ersys.finance.accounting.model.Account;
import com.nokor.ersys.finance.accounting.model.AccountCategory;
import com.nokor.ersys.finance.accounting.model.ECategoryRoot;
import com.nokor.ersys.finance.accounting.model.JournalEvent;
import com.nokor.ersys.finance.accounting.model.JournalEventAccount;
import com.nokor.ersys.finance.accounting.service.AccountRestriction;
import com.nokor.ersys.finance.accounting.tools.helper.ErsysAccountingAppServicesHelper;
import com.nokor.frmk.testing.BaseTestCase;

/**
 * @author bunlong.taing
 */
public class TestAccountCategory extends BaseTestCase implements ErsysAccountingAppServicesHelper {
	
	private static final String SOURCE_FOLDER = "R:/WORK/SVN-NKR-SOLUTIONS/nkr-efinance-th/trunk/doc/From Customer/Module Accounting/parameters/";
	private static final String SOURCE_FILE_NAME = SOURCE_FOLDER + "Receive_Mapping_COA.xlsx";
	private static final String DES_FILE_NAME = SOURCE_FOLDER + "Receive_Mapping_COA.sql";
	
	private static final String ACCOUNT_TEMPLATE = "-- Account\n"
			+ "INSERT INTO tu_account(acc_id, acc_cat_id, acc_start_balance, acc_code, acc_other_info, "
			+ "acc_name, acc_name_en, acc_reference, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)"
			+ "\n\tVALUES";
	private static final String ACC_CAT_TEMPLATE = "-- AccountCategory\n"
			+ "INSERT INTO tu_account_category(acc_cat_id, acc_cat_code, acc_cat_name, acc_cat_en,"
			+ "acc_cat_desc, acc_cat_desc_en, root, parent_acc_cat_id,"
			+ "sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)"
			+ "\n\tVALUES";
	private static final String JOURNAL_EVENT_TEMPLATE = "INSERT INTO tu_journal_event(jou_eve_id, jou_id, jou_eve_code, "
			+ "jou_eve_desc, jou_eve_desc_en, sort_index, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)"
			+ "\n\tVALUES";
	private static final String JOURNAL_EVENT_ACCOUNT_TEMPLATE = "INSERT INTO tu_journal_event_account(jou_eve_acc_id, "
			+ "jou_eve_id, acc_id, jou_eve_acc_is_debit, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)"
			+ "\n\tVALUES";
	
	private static Map<String, AccountCategory> categories;
//	private static List<Account> accounts;
	private static long categoryId;
//	private static long accountId;
	
	@Override
	protected boolean isRequiredAuhentication() {
		return false;
	}
	
	@Override
	protected void setAuthentication() {
		login = "admin";
		password = "admin@EFIN";
	}
	
	public static void main(String[] args) {
		loadDataFromExcel();
	}

	/**
	 * 
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
			
//			createAccountCategoryScript(rowIterator);
//			createAccountScript(rowIterator);
//			createJournalEntryScript(rowIterator);
			
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
	
	// ============================================================= Journal Event Account
	
	private static void createJournalEventAccountScript(Iterator<Row> rowIterator, Map<String, String> codeMapping) {
		List<JournalEventAccount> journalEventAccounts = new ArrayList<>();
		long journalEventAccountId = 100l;
		
		// skip the first row: heading
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
	 * 
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
		System.out.println("finish journal event account script");
		writeToFile(content.toString(), DES_FILE_NAME);
	}
	
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
	
	private static Map<String, String> accountCodeOldNewMapping(Iterator<Row> rowIterator) {
		Map<String, String> codeMapping = new HashMap<>();
		// For the header row;
		Row rowHeader = rowIterator.next();
		logger.info("***********Row header [" + rowHeader.getCell(0) + "]**************");
		
		// For the data rows
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			codeMapping.put(getString(row.getCell(1)), getString(row.getCell(0)));
		}
		return codeMapping;
	}
	
	// ============================================================= Journal Entry
	
	private static void createJournalEntryScript(Iterator<Row> rowIterator) {
		List<JournalEvent> journalEvents = new ArrayList<JournalEvent>();
		long journalEventId = 55l;
		
		// skip the first row: heading
		rowIterator.next();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			JournalEvent journalEvent = createJournalEvent(row);
			journalEvent.setId(journalEventId++);
			journalEvents.add(journalEvent);
		}
		generateJournalScript(journalEvents);
	}
	
	private static void generateJournalScript(List<JournalEvent> journalEvents) {
		StringBuffer content = new StringBuffer(JOURNAL_EVENT_TEMPLATE);
		// JournalEntry script
		for (int i = 0; i < journalEvents.size(); i++) {
			JournalEvent journalEvent = journalEvents.get(i);
			content.append("\n\t(")
					.append(journalEvent.getId() + ", " + "4, '")
					.append(journalEvent.getCode() + "', '")
					.append(journalEvent.getDesc() + "', '")
					.append(journalEvent.getDescEn() + "', ")
					.append(StringUtils.leftPad("1, 1, now(), 'admin', now(), 'admin')", 100))
					.append((i < journalEvents.size() - 1) ? "," : ";");
		}
		System.out.println("finish journal script");
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
	
	// ==================================================================== Account
	
	/**
	 * @param rowIterator
	 */
	private static void createAccountScript(Iterator<Row> rowIterator) {
		List<Account> accounts = new ArrayList<Account>();
		long accountId = 1l;
		
		// skip the first row: heading
		rowIterator.next();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			Account account = createAccount(row);
			account.setId(accountId++);
			accounts.add(account);
		}
		generateAccountScript(accounts);
	}
	
	private static void generateAccountScript(List<Account> accounts) {
		StringBuffer content = new StringBuffer(ACCOUNT_TEMPLATE);
		// Account script
		for (int i = 0; i < accounts.size(); i++) {
			Account account = accounts.get(i);
			content.append("\n\t(")
					.append(account.getId() + ", " + "1, 0, '")
					.append(account.getCode() + "', '")
					.append(account.getOtherInfo() + "', '")
					.append(account.getName().replace("'", "''") + "', '")
					.append(account.getNameEn().replace("'", "''") + "', ")
					.append(StringUtils.leftPad("null, 1, now(), 'admin', now(), 'admin')", 100))
					.append((i < accounts.size() - 1) ? "," : ";");
		}
		System.out.println("finish account script");
		writeToFile(content.toString(), DES_FILE_NAME);
	}
	
	private static Account createAccount(Row row) {
		Account account = new Account();
		account.setCode(getString(row.getCell(0)));
		account.setOtherInfo(getString(row.getCell(1)));
		account.setName(getString(row.getCell(2)));
		account.setNameEn(getString(row.getCell(3)));
		return account;
	}
	
	// =============================================================== AccountCategory
	
	/**
	 * Create Account category Script
	 * @param rowIterator
	 */
	private static void createAccountCategoryScript(Iterator<Row> rowIterator) {
		categories = new HashMap<String, AccountCategory>();
		categoryId = 1l;
		
		// skip the first row: heading
		rowIterator.next();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			AccountCategory category = createAccountCategory(row);
			if (categories.get(category.getCode()) == null) {
				category.setId(categoryId++);
				categories.put(category.getCode(), category);
			}
		}
		generateAccountCategoryScript();
	}
	
	/**
	 */
	private static void generateAccountCategoryScript() {
			// Account script
//			for (int i = 0; i < accounts.size(); i++) {
//				Account account = accounts.get(i);
//				String accountSql = "\n\t(" +
//						account.getId() + ", " +
//						account.getCode() + ", " +
//						account.getName() + ", " +
//						account.getNameEn() + ", " +
//						account.getStartingBalance() + ", " +
//						account.getCategory().getId() + ", " +
//						"1, 1, now(), 'admin', now(), 'admin')";
//				writer.append(accountSql);
//				if (i < accounts.size() - 1) {
//					writer.append(",");
//				} else {
//					writer.append(";");
//				}
//			}
	}
	
	/**
	 * @param row
	 * @return
	 */
	private static AccountCategory createAccountCategory(Row row) {
		AccountCategory category = new AccountCategory();
		category.setCode(getString(row.getCell(0)).substring(0, 2) + "0000");
		category.setName(getString(row.getCell(5)));
		category.setNameEn(getString(row.getCell(5)));
		category.setRoot(getRoot(getString(row.getCell(0))));
		return category;
	}
	
	/**
	 * @param code
	 * @return
	 */
	private static ECategoryRoot getRoot(String code) {
		if (StringUtils.isEmpty(code)) {
			return null;
		}
		code = code.substring(0, 1);
		
		if ("1".equals(code)) {
			return ECategoryRoot.ASSETS;
		} else if ("2".equals(code)) {
			return ECategoryRoot.LIABILITIES;
		} else if ("3".equals(code)) {
			return ECategoryRoot.EQUITIES;
		} else if ("4".equals(code)) {
			return ECategoryRoot.INCOMES;
		} else if ("5".equals(code)) {
			return ECategoryRoot.EXPENSES;
		}
		return null;
	}
	
	// ============================================================ 
	
	private static void writeToFile(String content, String dest) {
		try {
			Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dest), "utf-8"));
			writer.write(content);
			writer.close();
			System.out.println("Write complete: " + dest);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param cell
	 * @return
	 */
	private static String getString(Cell cell) {
		if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
			return String.valueOf((long) cell.getNumericCellValue());
		}
		return cell.getStringCellValue();
	}

}
