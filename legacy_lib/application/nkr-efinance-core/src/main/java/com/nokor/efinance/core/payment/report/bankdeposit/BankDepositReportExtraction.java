package com.nokor.efinance.core.payment.report.bankdeposit;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.payment.model.BankDeposit;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.tools.report.Report;
import com.nokor.efinance.tools.report.XLSAbstractReportExtractor;
import com.nokor.efinance.core.quotation.QuotationService;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.frmk.security.context.SecApplicationContextHolder;

/**
 * @author vina.sok
 */
public class BankDepositReportExtraction extends XLSAbstractReportExtractor implements Report {

	protected QuotationService quotationService = (QuotationService) SecApplicationContextHolder.getContext().getBean("quotationService");
	protected EntityService entityService = (EntityService) SecApplicationContextHolder.getContext().getBean("entityService");

	private static Map<String, CellStyle> styles = null;
	
	public static String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";
	/** Background color format */
	static short BG_BLUE = IndexedColors.DARK_BLUE.getIndex();// IndexedColors.DARK_TEAL.getIndex();
	static short BG_RED = IndexedColors.RED.getIndex();
	static short BG_CYAN = IndexedColors.LIGHT_TURQUOISE.getIndex();
	static short BG_YELLOW = IndexedColors.LIGHT_YELLOW.getIndex();
	static short BG_GREY22 = 22;
	static short BG_GREY = IndexedColors.GREY_25_PERCENT.getIndex();
	static short BG_LIGHT_BLUE = IndexedColors.LIGHT_BLUE.getIndex();
	static short BG_WHITE = IndexedColors.WHITE.getIndex();
	static short BG_GREEN = IndexedColors.GREEN.getIndex();

	/** Font color */
	static short FC_WHITE = IndexedColors.WHITE.getIndex();
	static short FC_BLACK = IndexedColors.BLACK.getIndex();
	static short FC_BLUE = 48;
	static short FC_GREY = IndexedColors.GREY_80_PERCENT.getIndex();
	static short FC_GREEN = IndexedColors.GREEN.getIndex();

	public BankDepositReportExtraction() {

	}

	@Override
	public String generate(ReportParameter reportParameter) throws Exception {	
		Map<String, Object> parameters = reportParameter.getParameters();
		BankDeposit bankDeposit = (BankDeposit) parameters.get("bankDeposit");
		//BankDeposit bankDeposit = entityService.getById(BankDeposit.class, bankDepositID);
		Dealer dealer = bankDeposit.getDealer();
		List<Payment> payments = bankDeposit.getPayments();
		
		createWorkbook(null);
		XSSFSheet sheet = wb.createSheet();
		sheet.lockDeleteColumns();
		sheet.lockDeleteRows();
		sheet.lockFormatCells();
		sheet.lockFormatColumns();
		sheet.lockFormatRows();
		sheet.lockInsertColumns();
		sheet.lockInsertRows();
		CellStyle style = wb.createCellStyle();
		styles = new HashMap<String, CellStyle>();
		createStyles();
		sheet.setColumnWidth(0, 5000);
		sheet.setColumnWidth(1, 5000);
		sheet.setColumnWidth(2, 6000);
		sheet.setColumnWidth(3, 6000);
		sheet.setColumnWidth(4, 5000);
		sheet.setColumnWidth(5, 5000);
		sheet.setZoom(7, 10);
		final PrintSetup printSetup = sheet.getPrintSetup();

		printSetup.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);

		printSetup.setScale((short) 75);

		// Setup the Page margins - Left, Right, Top and Bottom
		sheet.setMargin(Sheet.LeftMargin, 0.25);
		sheet.setMargin(Sheet.RightMargin, 0.25);
		sheet.setMargin(Sheet.TopMargin, 0.25);
		sheet.setMargin(Sheet.BottomMargin, 0.25);
		sheet.setMargin(Sheet.HeaderMargin, 0.25);
		sheet.setMargin(Sheet.FooterMargin, 0.25);
		style.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.BIG_SPOTS);
		style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		Row headerRow = sheet.createRow(0);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
		createCell(headerRow, 0, "áž‡áŸ†ážšáž¶áž”áž‡áž¼áž“ážŠáŸ†ážŽáž¶áž„áž›áž€áŸ‹áž˜áŸ‰áž¼áž�áž¼ Honda", 16, true, false, false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,false);
		headerRow = sheet.createRow(1);
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5));
		createCell(headerRow, 0, dealer.getNameEn() , 16, true, false, false, CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,false);
		headerRow = sheet.createRow(2);
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 5));
		String description1 = "ážŸáž¼áž˜áž˜áŸ�áž�áŸ’áž�áž¶ážŠáž¶áž€áŸ‹áž”áŸ’ážšáž¶áž€áŸ‹ážŠáŸ‚áž›áž”áŸ’ážšáž˜áž¼áž›áž”áž¶áž“áž–áž¸áž¢áž�áž·áž�áž·áž‡áž“ážšáž”ážŸáŸ‹ áž€áŸ’ážšáž»áž˜áž áŸŠáž»áž“â€‹ GL FINANCE PLC áž€áŸ’áž“áž»áž„áž‚ážŽáž“áž¸ážšáž”ážŸáŸ‹áž™áž¾áž„áž�áŸ’áž‰áž»áŸ†áž›áŸ�áž�";
		createCell(headerRow, 0, description1 , 14, false, false, false, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK,false);
		headerRow = sheet.createRow(3);
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 5));
		String description2 = "001-0002093028 áž“áŸ…áž€áŸ’áž“áž»áž„áž’áž“áž¶áž‚áž¶ážšáž€áž¶ážŽáž¶ážŒáž¸áž™áŸ‰áž¶ ážŸáŸ†ážšáž¶áž”áŸ‹áž”áŸ’ážšáž¶áž€áŸ‹ážŠáŸ‚áž›áž”áŸ’ážšáž˜áž¼áž›áž”áž¶áž“áž–áž¸áž¢áž�áž·áž�áž·áž‡áž“â€‹áž“áŸ…áž…áž“áŸ’áž›áŸ„áŸ‡â€‹áž…áž¶áž”áŸ‹áž–áž¸áž�áŸ’áž„áŸƒáž‘áž¸";
		createCell(headerRow, 0, description2 , 14, false, false, false, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK,false);
		headerRow = sheet.createRow(4);
		sheet.addMergedRegion(new CellRangeAddress(4, 4, 0, 5));
		String description3 = DateUtils.getDateLabel(payments.get(0).getPaymentDate(), DateUtils.FORMAT_DDMMYYYY_SLASH)
				+ "  ážŠáž›áŸ‹  " + DateUtils.getDateLabel(payments.get(payments.size()-1).getPaymentDate(), DateUtils.FORMAT_DDMMYYYY_SLASH) 
				+ "  áž…áŸ†áž“áž½áž“áž‘áž¹áž€áž”áŸ’ážšáž¶áž€áŸ‹ážŸážšáž»áž”   " + AmountUtils.format(getTotalPayment(payments)) ;
		createCell(headerRow, 0, description3 , 14, false, false, false, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK,false);
		headerRow = sheet.createRow(5);
		sheet.addMergedRegion(new CellRangeAddress(5, 5, 0, 5));
		String description4 = "ážŸáž¼áž˜áž›áŸ„áž€áž›áŸ„áž€ážŸáŸ’ážšáž¸áž˜áŸ�áž�áŸ’áž�áž¶ážŠáž¶áž€áŸ‹áž”áŸ’ážšáž¶áž€áŸ‹ážŠáŸ‚áž›áž”áŸ’ážšáž˜áž¼áž›áž”áž¶áž“áž“áŸ…áž˜áž»áž“áž�áŸ’áž„áŸƒáž‘áž¸  " + DateUtils.getDateLabel(bankDeposit.getRequestDepositDate(), DateUtils.FORMAT_DDMMYYYY_SLASH);
		createCell(headerRow, 0, description4 , 14, false, false, false, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK,false);

		int iRow = 6;
		iRow = iRow + 1;
		iRow = dataTable(sheet, iRow, style, bankDeposit);

//		Row tmpRowEnd = sheet.createRow(iRow++);
//		tmpRowEnd.setRowStyle(style);

		iRow = iRow + 1;

		String fileName = writeXLSData("BankDeposit_" + DateUtils.getDateLabel(DateUtils.today(), "yyyyMMddHHmmss") + ".xlsx");

		return fileName;
	}
	/**
	 * 
	 * @param payments
	 * @return
	 */
	private double getTotalPayment(List<Payment> payments) {
		double totalPayment = 0d;
		for (Payment payment : payments) {
			totalPayment += payment.getTiPaidAmount();
		}
		return totalPayment;
	}
	
	private int dataTable(final Sheet sheet, int iRow, final CellStyle style, BankDeposit bankDeposit) throws Exception {
		/* Create total data header */
		int iCol = 0;
		Row tmpRow = sheet.createRow(iRow++);
		createCell(tmpRow, iCol++, "áž€áž¶ážšáž·áž™áž”ážšáž·áž…áŸ’áž†áŸ�áž‘áž”áž„áŸ‹áž”áŸ’ážšáž¶áž€áŸ‹", 12, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, iCol - 1, iCol++));
		createCell(tmpRow, iCol-1, "", 12, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		createCell(tmpRow, iCol++, "ážˆáŸ’áž˜áŸ„áŸ‡ážŠáŸ†ážŽáž¶áž„áž˜áŸ‰áž¼áž�áž¼áž›áž€áŸ‹áž áž»áž„ážŠáž¶", 12, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, iCol - 1, iCol++));
		createCell(tmpRow, iCol-1, "", 12, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		createCell(tmpRow, iCol++, "áž‘áž¹áž€áž”áŸ’ážšáž¶áž€áŸ‹áž�áŸ’ážšáž¼ážœáž”áž„áŸ‹", 12, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, iCol - 1, iCol++));
		createCell(tmpRow, iCol-1, "", 12, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);

		// Calculate total amount per day
		Map<Date,List<Payment>> totalAmountPerDays = new TreeMap<>();
		for (Payment payment : bankDeposit.getPayments()) {
			if (totalAmountPerDays.containsKey(DateUtils.getDateAtBeginningOfDay(payment.getPaymentDate()))) {
				totalAmountPerDays.get(DateUtils.getDateAtBeginningOfDay(payment.getPaymentDate())).add(payment);
			} else {
				List<Payment> pList = new ArrayList<>();
				pList.add(payment);
				totalAmountPerDays.put(DateUtils.getDateAtBeginningOfDay(payment.getPaymentDate()), pList);
			}
		}
		
		for (Date key : totalAmountPerDays.keySet()) {
			createRowPerDay (totalAmountPerDays.get(key), sheet, iRow++, iCol);
		}
		iRow = iRow + 2;
		tmpRow = sheet.createRow(iRow++);
		iCol = 0;
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_CENTER, 14, false, true, BG_WHITE, FC_BLACK);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, iCol - 1, iCol++));
		createCell(tmpRow, iCol++, "Total", 12, true, true, false,
				CellStyle.ALIGN_RIGHT, true, BG_WHITE, FC_BLACK, false);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, iCol - 1, iCol++));
		createCell(tmpRow, iCol-1, "", 12, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		createCell(tmpRow, iCol++, AmountUtils.format(getTotalPayment(bankDeposit.getPayments())), 12, true, true, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, false);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, iCol - 1, iCol++));
		createCell(tmpRow, iCol-1, "", 12, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		createCell(tmpRow, iCol-1, "", 12, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		if (bankDeposit.getPayments().size() < 5) {
			iRow = iRow + 5;
		} else {
			iRow = iRow + 2;
		}
		tmpRow = sheet.createRow(iRow++);
		iCol = 2;
		String description5 = "ážŸáž¼áž˜áž›áŸ„áž€/áž›áŸ„áž€ážŸáŸ’ážšáž¸áž˜áŸ�áž�áŸ’áž�áž¶áž“áž¼ážœážŸáŸ�áž…áž€áŸ’áž�áž¸áž‚áŸ„ážšáž–áž¢áŸ†áž–áž¸áž™áž¾áž„áž�áŸ’áž‰áž»áŸ†áŸ”";
		createCell(tmpRow, iCol++, description5 , 14, false, false, false, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK,false);
		
		iRow = iRow + 5;
		tmpRow = sheet.createRow(iRow++);
		iCol = 3;
		String description6 = "áž—áŸ’áž“áŸ†áž–áŸ�áž‰ áž�áŸ’áž„áŸƒáž‘áž¸â€¦â€¦â€¦â€¦áž�áŸ‚â€¦â€¦â€¦â€¦.áž†áŸ’áž“áž¶áŸ†â€¦â€¦â€¦â€¦â€¦.";
		createCell(tmpRow, iCol++, description6 , 14, false, false, false, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK,false);
		tmpRow = sheet.createRow(iRow++);
		iCol = 4;
		String description7 = "áž”áŸ’ážšáž’áž¶áž“áž áž·ážšáž‰áŸ’áž‰ážœáž�áŸ’áž�áž»";
		createCell(tmpRow, iCol++, description7 , 14, true, false, false, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK,false);
		return iRow;

	}
	
	/**
	 * Create a row of payment
	 * @param detail list of payment
	 * @param sheet The sheet
	 * @param iRow The Row to be created
	 * @param iCol The started Column to be created
	 */
	protected void createRowPerDay (List<Payment> detail, Sheet sheet, int iRow, int iCol) {
		double totalAmount = 0d;
		if (detail != null && !detail.isEmpty()) {
			Payment payment = detail.get(0);
			for (Payment p : detail) {
				totalAmount += p.getTiPaidAmount();
			}
			Row tmpRow = sheet.createRow(iRow++);
			iCol = 0;
			createCell(tmpRow, iCol++, DateUtils.getDateLabel(payment.getPaymentDate(), DateUtils.FORMAT_DDMMYYYY_SLASH), 12, false, true, false,
					CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, false);
			sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, iCol - 1, iCol++));
			createCell(tmpRow, iCol-1, "", 12, false, true, false,
					CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
			createCell(tmpRow, iCol++, payment.getDealer().getNameEn(), 12, false, true, false,
					CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, false);
			sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, iCol - 1, iCol++));
			createCell(tmpRow, iCol-1, "", 12, false, true, false,
					CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
			createCell(tmpRow, iCol++, AmountUtils.format(totalAmount), 12, false, true, false,
					CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, false);
			sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, iCol - 1, iCol++));
			createCell(tmpRow, iCol-1, "", 12, false, true, false,
					CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		}
	}
	
	protected Cell createCell(final Row row, final int iCol, final String value, final CellStyle style) {
		final Cell cell = row.createCell(iCol);
		cell.setCellValue((value == null ? "" : value));
		cell.setCellStyle(style);
		return cell;
	}

	protected Cell createCell(final Row row, final int iCol,
			final String value, final int fontsize, final boolean isBold,
			final boolean hasBorder, final boolean leftRight, final short alignment,
			final boolean setBgColor, final short bgColor, final short fonCorlor, boolean wrapText) {
	
		final Cell cell = row.createCell(iCol);
		final Font itemFont = wb.createFont();
		itemFont.setFontHeightInPoints((short) fontsize);
		if (isBold) {
			itemFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		}
		itemFont.setFontName("Khmer OS Battambang");

		final CellStyle style = wb.createCellStyle();
		style.setAlignment(alignment);
		style.setFont(itemFont);
		style.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.BIG_SPOTS);
		style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setLocked(true);
		itemFont.setColor(fonCorlor);
		style.setFont(itemFont);
		if (hasBorder) {
			style.setBorderTop(CellStyle.BORDER_THIN);
			style.setBorderLeft(CellStyle.BORDER_THIN);
			style.setBorderRight(CellStyle.BORDER_THIN);
			style.setBorderBottom(CellStyle.BORDER_THIN);
			style.setBottomBorderColor(FC_BLACK);
		}
		if (leftRight) {
			style.setBorderLeft(CellStyle.BORDER_THIN);
			style.setBorderRight(CellStyle.BORDER_THIN);
		}
		if (setBgColor) {
			style.setFillForegroundColor(bgColor);
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		}
		if (wrapText) {
			style.setWrapText(wrapText);
		}
		cell.setCellValue((value == null ? "" : value));
		cell.setCellStyle(style);
		return cell;
	}
	
	protected Cell createCellBorderBottomAndRight(final Row row, final int iCol,
			final String value, final int fontsize, final boolean isBold,
			final boolean hasBorder, final boolean leftRight, final short alignment,
			final boolean setBgColor, final short bgColor, final short fonCorlor, boolean wrapText) {
	
		final Cell cell = row.createCell(iCol);
		final Font itemFont = wb.createFont();
		itemFont.setFontHeightInPoints((short) fontsize);
		if (isBold) {
			itemFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		}
		itemFont.setFontName("Khmer OS Battambang");

		final CellStyle style = wb.createCellStyle();
		style.setAlignment(alignment);
		style.setFont(itemFont);
		style.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.BIG_SPOTS);
		style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setLocked(true);
		itemFont.setColor(fonCorlor);
		style.setFont(itemFont);
		if (hasBorder) {
			style.setBorderRight(CellStyle.BORDER_THIN);
			style.setBorderBottom(CellStyle.BORDER_THIN);
			style.setBottomBorderColor(FC_BLACK);
		}
		if (leftRight) {
			style.setBorderLeft(CellStyle.BORDER_THIN);
			style.setBorderRight(CellStyle.BORDER_THIN);
		}
		if (setBgColor) {
			style.setFillForegroundColor(bgColor);
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		}
		if (wrapText) {
			style.setWrapText(wrapText);
		}
		cell.setCellValue((value == null ? "" : value));
		cell.setCellStyle(style);
		return cell;
	}
	
	private Cell createNumericCell(final Row row, final int iCol,
			final Object value, final boolean isBold1, final short alignment,
			final int fontsize, final boolean isBold, final boolean setBgColor,
			final short bgColor, final short fontColor) {

		final Cell cell = row.createCell(iCol);
		if (isBold1) {
			final Font itemFont = wb.createFont();
			itemFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		}
		if (value == null) {
			cell.setCellValue("");
		} else if (value instanceof Integer) {
			cell.setCellValue(Integer.valueOf(value.toString()));
		} else if (value instanceof Long) {
			cell.setCellValue(Long.valueOf(value.toString()));
		} else if (value instanceof String) {
			cell.setCellValue(value.toString());
		}
		if (value instanceof Double) {
            cell.setCellValue((Double) value);
            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        } else {
        	cell.setCellStyle(styles.get("BODY"));	
        }
		return cell;
	}

	public String getDateLabel(final Date date, final String formatPattern) {
		if (date != null && formatPattern != null) {
			return DateFormatUtils.format(date, formatPattern);
		}
		return null;
	}

	private Map<String, CellStyle> createStyles() {
		CellStyle style;
		style = wb.createCellStyle();
		style.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.BORDER_DOUBLE);
		style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderLeft(CellStyle.BORDER_MEDIUM);
		style.setBorderTop(CellStyle.BORDER_MEDIUM);
		style.setLocked(true);
		styles.put("TOP_LEFT_BORDER", style);

		style = wb.createCellStyle();
		style.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.BORDER_DOUBLE);
		style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderTop(CellStyle.BORDER_MEDIUM);
		style.setLocked(true);
		styles.put("TOP_BORDER", style);

		style = wb.createCellStyle();
		style.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.BORDER_DOUBLE);
		style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderLeft(CellStyle.BORDER_MEDIUM);
		style.setLocked(true);
		styles.put("LEFT_BORDER", style);

		style = wb.createCellStyle();
		style.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.BORDER_DOUBLE);
		style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderRight(CellStyle.BORDER_MEDIUM);
		style.setLocked(true);
		styles.put("RIGHT_BORDER", style);

		style = wb.createCellStyle();
		style.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.BORDER_DOUBLE);
		style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(CellStyle.BORDER_MEDIUM);
		style.setLocked(true);
		styles.put("BUTTOM_BORDER", style);

		style = wb.createCellStyle();
		style.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.BORDER_DOUBLE);
		style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderRight(CellStyle.BORDER_MEDIUM);
		style.setBorderTop(CellStyle.BORDER_MEDIUM);
		style.setLocked(true);
		styles.put("TOP_RIGHT_BORDER", style);

		style = wb.createCellStyle();
		style.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.BORDER_DOUBLE);
		style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderRight(CellStyle.BORDER_MEDIUM);
		style.setBorderBottom(CellStyle.BORDER_MEDIUM);
		style.setLocked(true);
		styles.put("BUTTOM_RIGHT_BORDER", style);

		style = wb.createCellStyle();
		style.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.BORDER_DOUBLE);
		style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderLeft(CellStyle.BORDER_MEDIUM);
		style.setBorderBottom(CellStyle.BORDER_MEDIUM);
		style.setLocked(true);
		styles.put("BUTTOM_LEFT_BORDER", style);

		style = wb.createCellStyle();
		style.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
		style.setFillPattern(HSSFCellStyle.BORDER_DOUBLE);
		style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setLocked(true);
		styles.put("HEADER", style);
		
		final Font itemFont = wb.createFont();
		itemFont.setFontHeightInPoints((short) 14);
		itemFont.setFontName("Khmer OS Battambang");
		style = wb.createCellStyle();
		style.setFont(itemFont);
		styles.put("BODY", style);
		
	  	DataFormat format = wb.createDataFormat();
    	style = wb.createCellStyle();
    	style.setAlignment(CellStyle.ALIGN_RIGHT);
    	style.setFont(itemFont);
    	style.setDataFormat(format.getFormat("#,##0.00"));
    	styles.put("AMOUNT", style);

		return styles;
	}

	protected CellStyle getCellStyle(final String styleName) {
		return styles.get(styleName);
	}
}
