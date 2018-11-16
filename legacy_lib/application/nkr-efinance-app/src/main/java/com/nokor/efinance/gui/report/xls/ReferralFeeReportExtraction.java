package com.nokor.efinance.gui.report.xls;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.service.ContractService;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.efinance.tools.report.Report;
import com.nokor.efinance.tools.report.XLSAbstractReportExtractor;
import com.nokor.frmk.security.context.SecApplicationContextHolder;

/**
 * @author sok.vina
 */
public class ReferralFeeReportExtraction extends XLSAbstractReportExtractor implements Report, ReferalFeeFields, QuotationEntityField {

	private ContractService contractService = (ContractService) SecApplicationContextHolder.getContext().getBean("contractService");
	private Map<String, CellStyle> styles = null;
	
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

	/** */
	public ReferralFeeReportExtraction() {

	}

	/**
	 * @see com.nokor.efinance.tools.report.Report#generate(com.nokor.efinance.core.shared.report.ReportParameter)
	 */
	@Override
	public String generate(ReportParameter reportParameter) throws Exception {
		
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<Contract>(Contract.class);
		Map<String, Object> parameters = reportParameter.getParameters();
		Dealer dealer = (Dealer) parameters.get(DEALER);
		String contractReference = (String) parameters.get("contractReference");
		Date startDate = (Date) parameters.get("startDate");
		Date endDate = (Date) parameters.get("endDate");
		
		if (dealer != null) {
			restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));
		}
		if (StringUtils.isNotEmpty(contractReference)) {
			
			restrictions.addCriterion(Restrictions.like(REFERENCE, contractReference, MatchMode.ANYWHERE));	
		}
		if (startDate != null) {       
			restrictions.addCriterion(Restrictions.ge("startDate", DateUtils.getDateAtBeginningOfDay(startDate)));
		}
		if (endDate != null) {
			restrictions.addCriterion(Restrictions.le("startDate", DateUtils.getDateAtEndOfDay(endDate)));
		}
		restrictions.addOrder(Order.asc(DEALER + "." + ID));
		restrictions.addOrder(Order.asc("startDate"));
		List<Contract> contracts = contractService.list(restrictions);

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
		for (int i = 1; i < 26; i++) {
			sheet.setColumnWidth(i, 6000);	
		}
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

//		final Row headerRow = sheet.createRow(2);
//		createCell(headerRow, 6, APPLICANT_TITTLE, 14, true, false, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK);

		int iRow = 0;

		iRow = dataTable(sheet, iRow, style, contracts);

		Row tmpRowEnd = sheet.createRow(iRow++ + 1);
		tmpRowEnd.setRowStyle(style);

		iRow = iRow + 2;

		String fileName = writeXLSData("CBC_Report_Extraction_" + DateUtils.getDateLabel(DateUtils.today(), "yyyyMMddHHmmss") + ".xlsx");

		return fileName;
	}

	/**
	 * 
	 * @param sheet
	 * @param iRow
	 * @param style
	 * @param contracts
	 * @return
	 * @throws Exception
	 */
	private int dataTable(final Sheet sheet, int iRow, final CellStyle style, List<Contract> contracts) throws Exception {
		/* Create total data header */

		int iCol = 0;
		Row tmpRow = sheet.createRow(iRow++);
		createCell(tmpRow, iCol++, "No.", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "Lid No.", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "Lessee Name", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "Tel", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "Dealer name", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);		
		createCell(tmpRow, iCol++, "Start month", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "End month", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "Date of Contract", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "First due date", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "Motor Price", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "Term", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "Rate", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "IRR /Year", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "IRR/Month", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, " ", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "NF", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "Total Int.", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "Installment", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "Commission fee", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "WHT on First Portion", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "Total Commission Expense", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "First Portion", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "RA1", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "RA2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
				createCell(tmpRow, iCol++, "EIR", 14, false, false,
						CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		int nbRows = 0;
		if (!contracts.isEmpty() || contracts != null) {
			nbRows = contracts.size();
		}
		int customerId = 0;
		for (int i = 0; i < nbRows; i++) {
			customerId++;
			Contract contract = contracts.get(i);
			Quotation quotation = contract.getQuotation();
			iRow = generateRow(sheet, contract, quotation, iRow, customerId);
		}
		return iRow; 
	}
	
	/**
	 * 
	 * @param sheet
	 * @param contract
	 * @param quo
	 * @param iRow
	 * @param customerId
	 * @return
	 */
	private int generateRow(final Sheet sheet, Contract contract, Quotation quo, int iRow, int customerId) {
		Quotation quotation = quo;
		int iCol = 0;
		Row tmpRow = sheet.createRow(iRow++);
		createNumericCell(tmpRow, iCol++, (customerId), true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, getDefaultString(contract.getReference()), true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, contract.getApplicant().getIndividual().getLastNameEn() + " " +contract.getApplicant().getIndividual().getFirstNameEn(), true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		// TODO YLY
		// createNumericCell(tmpRow, iCol++, getDefaultString(contract.getApplicant().getMobilePhone()), true,
		//		CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, contract.getDealer().getNameEn(), true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, getDateLabel(contract.getStartDate()), true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, getDateLabel(contract.getEndDate()), true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, getDateLabel(contract.getStartDate()), true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, getDateLabel(quotation.getFirstDueDate()), true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, AmountUtils.format(contract.getAsset().getTiAssetPrice()), true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, quotation.getTerm(), true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "" +contract.getFinancialProduct().getPeriodicInterestRate(), true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, AmountUtils.format(contract.getIrrRate()*12), true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		
		createNumericCell(tmpRow, iCol++, AmountUtils.format(contract.getIrrRate()), true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, AmountUtils.format(contract.getAdvancePaymentPercentage()), true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, AmountUtils.format(contract.getAsset().getTiAssetPrice() - contract.getTiAdvancePaymentAmount()), true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, AmountUtils.format(contractService.getTotalInterest(contract.getId()).getTiAmount()), true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, AmountUtils.format(quotation.getTiInstallmentAmount()), true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "commissionfee", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		return iRow;
	}

	/**
	 * 
	 * @param row
	 * @param iCol
	 * @param value
	 * @param fontsize
	 * @param isBold
	 * @param hasBorder
	 * @param alignment
	 * @param setBgColor
	 * @param bgColor
	 * @param fonCorlor
	 * @return
	 */
	protected Cell createCell(final Row row, final int iCol,
			final String value, final int fontsize, final boolean isBold,
			final boolean hasBorder, final short alignment,
			final boolean setBgColor, final short bgColor, final short fonCorlor) {
		final Cell cell = row.createCell(iCol);
		
		cell.setCellValue((value == null ? "" : value));
		cell.setCellStyle(styles.get(BODY));
		return cell;
	}

	/**
	 * 
	 * @param row
	 * @param iCol
	 * @param value
	 * @param hasBorder
	 * @param alignment
	 * @param fontsize
	 * @param isBold
	 * @param setBgColor
	 * @param bgColor
	 * @param fontColor
	 * @return
	 */
	private Cell createNumericCell(final Row row, final int iCol,
			final Object value, final boolean hasBorder, final short alignment,
			final int fontsize, final boolean isBold, final boolean setBgColor,
			final short bgColor, final short fontColor) {

		final Cell cell = row.createCell(iCol);
		if (value == null) {
			cell.setCellValue("");
		} else if (value instanceof Integer) {
			cell.setCellValue(Integer.valueOf(value.toString()));
		} else if (value instanceof Long) {
			cell.setCellValue(Long.valueOf(value.toString()));
		} else if (value instanceof String) {
			cell.setCellValue(value.toString());
		}
		cell.setCellStyle(styles.get(BODY));
		return cell;
	}

	/**
	 * 
	 * @param date
	 * @param formatPattern
	 * @return
	 */
	public String getDateLabel(final Date date, final String formatPattern) {
		if (date != null && formatPattern != null) {
			return DateFormatUtils.format(date, formatPattern);
		}
		return null;
	}

	/**
	 * 
	 * @return
	 */
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
		styles.put(TOP_LEFT_BORDER, style);

		style = wb.createCellStyle();
		style.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.BORDER_DOUBLE);
		style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderTop(CellStyle.BORDER_MEDIUM);
		style.setLocked(true);
		styles.put(TOP_BORDER, style);

		style = wb.createCellStyle();
		style.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.BORDER_DOUBLE);
		style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderLeft(CellStyle.BORDER_MEDIUM);
		style.setLocked(true);
		styles.put(LEFT_BORDER, style);

		style = wb.createCellStyle();
		style.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.BORDER_DOUBLE);
		style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderRight(CellStyle.BORDER_MEDIUM);
		style.setLocked(true);
		styles.put(RIGHT_BORDER, style);

		style = wb.createCellStyle();
		style.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.BORDER_DOUBLE);
		style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(CellStyle.BORDER_MEDIUM);
		style.setLocked(true);
		styles.put(BUTTOM_BORDER, style);

		style = wb.createCellStyle();
		style.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.BORDER_DOUBLE);
		style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderRight(CellStyle.BORDER_MEDIUM);
		style.setBorderTop(CellStyle.BORDER_MEDIUM);
		style.setLocked(true);
		styles.put(TOP_RIGHT_BORDER, style);

		style = wb.createCellStyle();
		style.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.BORDER_DOUBLE);
		style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderRight(CellStyle.BORDER_MEDIUM);
		style.setBorderBottom(CellStyle.BORDER_MEDIUM);
		style.setLocked(true);
		styles.put(BUTTOM_RIGHT_BORDER, style);

		style = wb.createCellStyle();
		style.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.BORDER_DOUBLE);
		style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderLeft(CellStyle.BORDER_MEDIUM);
		style.setBorderBottom(CellStyle.BORDER_MEDIUM);
		style.setLocked(true);
		styles.put(BUTTOM_LEFT_BORDER, style);

		style = wb.createCellStyle();
		style.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
		style.setFillPattern(HSSFCellStyle.BORDER_DOUBLE);
		style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setLocked(true);
		styles.put(HEADER, style);
		
		final Font itemFont = wb.createFont();
		itemFont.setFontHeightInPoints((short) 14);
		itemFont.setFontName("Khmer OS Battambang");
		style = wb.createCellStyle();
		style.setFont(itemFont);
		styles.put(BODY, style);

		return styles;
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	public String getDateLabel(final Date date) {
		return getDateLabel(date, DEFAULT_DATE_FORMAT);
	}
	
	/** */
	protected CellStyle getCellStyle(final String styleName) {
		return styles.get(styleName);
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private String getDefaultString(String value) {
		String strValue = "";
		if (StringUtils.isNotEmpty(value)) {
			strValue = value;
		}
		return strValue;
	}
}
