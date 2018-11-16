package com.nokor.efinance.gui.report.xls;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.efinance.tools.report.Report;
import com.nokor.efinance.tools.report.XLSAbstractReportExtractor;
import com.nokor.frmk.security.context.SecApplicationContextHolder;

/**
 * @author sok.vina
 */
public class GLFIncomingInstall extends XLSAbstractReportExtractor implements
		Report, GLFIncomingInstallFields {

	EntityService entityService = (EntityService) SecApplicationContextHolder
			.getContext().getBean("entityService");

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

	public GLFIncomingInstall() {

	}

	@Override
	public String generate(ReportParameter reportParameter) throws Exception {

		Map<String, Object> parameters = reportParameter.getParameters();
		Dealer dealer = (Dealer) parameters.get("dealer");
		Date startDateInstallment = (Date) parameters.get("startDate");
		Date endDateInstallment = (Date) parameters.get("endDate");

		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<Cashflow>(
				Cashflow.class);
		restrictions.addAssociation("contract", "cont", JoinType.INNER_JOIN);
		if (dealer != null) {
			restrictions.addCriterion(Restrictions.eq("cont.dealer.id",
					dealer.getId()));
		}
		if (startDateInstallment != null) {
			restrictions.addCriterion(Restrictions.ge("installmentDate",
					DateUtils.getDateAtBeginningOfDay(startDateInstallment)));
		}
		if (endDateInstallment != null) {
			restrictions.addCriterion(Restrictions.le("installmentDate",
					DateUtils.getDateAtEndOfDay(endDateInstallment)));
		}
		restrictions.addOrder(Order.desc("installmentDate"));
		
		List<Cashflow> cashflows = entityService.list(restrictions);

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
		sheet.setColumnWidth(1, 6000);
		sheet.setColumnWidth(2, 6000);
		sheet.setColumnWidth(3, 6000);
		sheet.setColumnWidth(4, 6000);
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

		final Row headerRow = sheet.createRow(2);
		createCell(headerRow, 2, INCOMING_INSTALL_TITTLE, 14, true, false,
				CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK);

		int iRow = 4;
		int iCol = 0;
		iCol = iCol + 2;
		iCol = 0;

		iRow = cashflowTable(sheet, iRow, style, cashflows);

		Row tmpRowEnd = sheet.createRow(iRow++);
		tmpRowEnd.setRowStyle(style);

		iRow = iRow + 1;

		String fileName = writeXLSData("IncomingInstallment_"
				+ DateUtils.getDateLabel(DateUtils.today(), "yyyyMMddHHmmssSSS")
				+ ".xlsx");

		return fileName;
	}

	private int cashflowTable(final Sheet sheet, int iRow,
			final CellStyle style, List<Cashflow> cashflows) throws Exception {
		/* Create total data header */
		int iCol = 0;
		Row tmpRow = sheet.createRow(iRow++);
		createCell(tmpRow, iCol++, CONTRACT, 12, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);

		createCell(tmpRow, iCol++, INSTALLMENT_DATE, 12, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);

		createCell(tmpRow, iCol++, PRICIPLE, 12, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);

		createCell(tmpRow, iCol++, INTEREST, 12, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);

		createCell(tmpRow, iCol++, OUN_STANDING, 12, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);

		String contract = "0";
		Date installmentDate = null;
		String priciple = "";
		String interest = "";
		String ounStanding = "";

		Long nextContractId = null;
		Date nextInstallmentDate = null;
		int nbRows = cashflows.size();
		for (int i = 0; i < nbRows; i++) {
			//tmpRow = sheet.createRow(iRow++);
			Cashflow cashflow = cashflows.get(i);

			if (cashflow.getCashflowType().equals(ECashflowType.CAP)) {
				priciple = cashflow.getTiInstallmentAmount().toString();
			}

			if (cashflow.getCashflowType().equals(ECashflowType.IAP)) {

				interest = cashflow.getTiInstallmentAmount().toString();
			}
			if (i == nbRows - 1) {
				nextContractId = null;
				nextInstallmentDate = null;
			} else {
				nextContractId = cashflows.get(i + 1).getContract().getId();
				nextInstallmentDate = cashflows.get(i + 1).getInstallmentDate();
			}
			if (cashflow.getContract().getId() != nextContractId || !cashflow.getInstallmentDate().equals(nextInstallmentDate)) {
				tmpRow = sheet.createRow(iRow++);
				contract = cashflow.getContract().getId().toString();
				installmentDate = cashflow.getInstallmentDate();
				ounStanding = "N/A";

				iCol = 0;
				createNumericCell(tmpRow, iCol++, contract, true,
						CellStyle.ALIGN_CENTER, 12, false, true, BG_WHITE,
						FC_BLACK);
				createDateCell(tmpRow, iCol++, installmentDate,
						true, CellStyle.ALIGN_LEFT, 12, false, true, BG_WHITE,
						FC_BLACK);
				createNumericCell(tmpRow, iCol++, priciple, true,
						CellStyle.ALIGN_RIGHT, 12, false, true, BG_WHITE,
						FC_BLACK);

				createNumericCell(tmpRow, iCol++, interest, true,
						CellStyle.ALIGN_RIGHT, 12, false, true, BG_WHITE,
						FC_BLACK);
				createNumericCell(tmpRow, iCol++, ounStanding, true,
						CellStyle.ALIGN_RIGHT, 12, false, true, BG_WHITE,
						FC_BLACK);
				//iRow = iRow + 1;

			}
		}

		return iRow;

	}
	protected Cell createDateCell(final Row row, final int iCol,
			final Date value, final boolean hasBorder, final short alignment,
			final int fontsize, final boolean isBold, final boolean setBgColor,
			final short bgColor, final short fontColor) {

		final Cell cell = row.createCell(iCol);

		final Font itemRightFont = wb.createFont();
		itemRightFont.setFontHeightInPoints((short) fontsize);
		itemRightFont.setFontName("Khmer OS Battambang");
		itemRightFont.setColor(fontColor);
		if (isBold) {
			itemRightFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		}
		final CellStyle style = wb.createCellStyle();
		style.setFont(itemRightFont);
		style.setLocked(true);
		
		if (setBgColor) {
			style.setFillForegroundColor(bgColor);
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		}
		if (hasBorder) {
			style.setBorderTop(CellStyle.BORDER_DOTTED);
			style.setBorderLeft(CellStyle.BORDER_DOTTED);
			style.setBorderRight(CellStyle.BORDER_DOTTED);
			style.setBorderBottom(CellStyle.BORDER_DOTTED);
		}
		style.setAlignment(alignment);
		cell.setCellValue((value == null ? "" : getDateLabel(value)));
		cell.setCellStyle(style);
		return cell;
	}
	public String getDateLabel(final Date date) {
		return getDateLabel(date, DEFAULT_DATE_FORMAT);
	}
	protected Cell createCell(final Row row, final int iCol,
			final String value, final CellStyle style) {
		final Cell cell = row.createCell(iCol);
		cell.setCellValue((value == null ? "" : value));
		cell.setCellStyle(style);
		return cell;
	}

	protected Cell createCell(final Row row, final int iCol,
			final String value, final int fontsize, final boolean isBold,
			final boolean hasBorder, final short alignment,
			final boolean setBgColor, final short bgColor, final short fonCorlor) {
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
			style.setBorderTop(CellStyle.BORDER_DOTTED);
			style.setBorderLeft(CellStyle.BORDER_DOTTED);
			style.setBorderRight(CellStyle.BORDER_DOTTED);
			style.setBorderBottom(CellStyle.BORDER_DOTTED);
		}
		if (setBgColor) {
			style.setFillForegroundColor(bgColor);
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		}
		cell.setCellValue((value == null ? "" : value));
		cell.setCellStyle(style);
		return cell;
	}

	private Cell createNumericCell(final Row row, final int iCol,
			final Object value, final boolean hasBorder, final short alignment,
			final int fontsize, final boolean isBold, final boolean setBgColor,
			final short bgColor, final short fontColor) {

		final Cell cell = row.createCell(iCol);
		final Font itemRightFont = wb.createFont();
		itemRightFont.setFontHeightInPoints((short) fontsize);
		itemRightFont.setFontName("Khmer OS Battambang");
		itemRightFont.setColor(fontColor);
		if (isBold) {
			itemRightFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		}
		final CellStyle style = wb.createCellStyle();
		style.setFont(itemRightFont);
		style.setLocked(true);

		if (setBgColor) {
			style.setFillForegroundColor(bgColor);
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		}
		if (hasBorder) {
			style.setBorderTop(CellStyle.BORDER_DOTTED);
			style.setBorderLeft(CellStyle.BORDER_DOTTED);
			style.setBorderRight(CellStyle.BORDER_DOTTED);
			style.setBorderBottom(CellStyle.BORDER_DOTTED);
		}

		style.setAlignment(alignment);
		if (value == null) {
			cell.setCellValue("");
		} else if (value instanceof Integer) {
			cell.setCellValue(Integer.valueOf(value.toString()));
		} else if (value instanceof Long) {
			cell.setCellValue(Long.valueOf(value.toString()));
		} else if (value instanceof String) {
			cell.setCellValue(value.toString());
		}
		cell.setCellStyle(style);
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

		return styles;
	}

	protected CellStyle getCellStyle(final String styleName) {
		return styles.get(styleName);
	}

}
