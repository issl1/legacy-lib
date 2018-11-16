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

import com.nokor.ersys.finance.accounting.model.Account;

/**
 * @author bunlong.taing
 */
public class TestCreateScriptAccount extends BaseAccountingTestCase {
	
	private static final String SOURCE_FOLDER = "/home/bunlong.taing/Desktop/";
	private static final String SOURCE_FILE_NAME = SOURCE_FOLDER + "ChartOfAccount.xlsx";
	private static final String DES_FILE_NAME = SOURCE_FOLDER + "ChartOfAccount.sql";
	
	private static final String ACCOUNT_TEMPLATE = "-- Account\n"
			+ "INSERT INTO tu_account(acc_id, acc_cat_id, acc_start_balance, acc_code, acc_other_info, "
			+ "acc_name, acc_name_en, acc_reference, sta_rec_id, dt_cre, usr_cre, dt_upd, usr_upd)"
			+ "\n\tVALUES";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// !! NOTE
		// Run Java Application, not JUnit test
		
		// And AccountCategory (acc_cat_id), have to manually add
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
			createAccountScript(rowIterator);
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
	private static void createAccountScript(Iterator<Row> rowIterator) {
		List<Account> accounts = new ArrayList<Account>();
		long accountId = 1l;
		
		// skip the first row: Heading
		rowIterator.next();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			Account account = createAccount(row);
			account.setId(accountId++);
			accounts.add(account);
		}
		generateAccountScript(accounts);
	}
	
	/**
	 * @param accounts
	 */
	private static void generateAccountScript(List<Account> accounts) {
		StringBuffer content = new StringBuffer(ACCOUNT_TEMPLATE);
		// Account script
		for (int i = 0; i < accounts.size(); i++) {
			Account account = accounts.get(i);
			content.append("\n\t(")
					.append(account.getId() + ", " + "null, 0, '")
					.append(account.getCode() + "', '")
					.append(account.getOtherInfo() + "', '")
					.append(account.getName().replace("'", "''") + "', '")
					.append(account.getNameEn().replace("'", "''") + "', ")
					.append(StringUtils.leftPad("null, 1, now(), 'admin', now(), 'admin')", 100))
					.append((i < accounts.size() - 1) ? "," : ";");
		}
		logger.debug("Finish generate account script");
		writeToFile(content.toString(), DES_FILE_NAME);
	}
	
	/**
	 * @param row
	 * @return
	 */
	private static Account createAccount(Row row) {
		Account account = new Account();
		account.setCode(getString(row.getCell(0)));
		account.setOtherInfo(getString(row.getCell(1)));
		account.setName(getString(row.getCell(2)));
		account.setNameEn(getString(row.getCell(3)));
		return account;
	}

}
