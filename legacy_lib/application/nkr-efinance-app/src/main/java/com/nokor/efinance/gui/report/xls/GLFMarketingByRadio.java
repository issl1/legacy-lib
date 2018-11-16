package com.nokor.efinance.gui.report.xls;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.quotation.QuotationService;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.efinance.glf.statistic.model.Statistic3HoursVisitor;
import com.nokor.efinance.tools.report.Report;
import com.nokor.efinance.tools.report.XLSAbstractReportExtractor;
import com.nokor.frmk.security.context.SecApplicationContextHolder;

/**
 * @author sok.vina
 */
public class GLFMarketingByRadio extends XLSAbstractReportExtractor implements Report, GLFApplicantFields, QuotationEntityField {

	protected QuotationService quotationService = (QuotationService) SecApplicationContextHolder.getContext().getBean("quotationService");
	protected EntityService entityService = (EntityService) SecApplicationContextHolder.getContext().getBean("entityService");

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

	public GLFMarketingByRadio() {

	}

	@Override
	public String generate(ReportParameter reportParameter) throws Exception {	
		Map<String, Object> parameters = reportParameter.getParameters();
		Date date = (Date) parameters.get("startDate");
		
		BaseRestrictions<Dealer> restrictions = new BaseRestrictions<Dealer>(Dealer.class);		
		restrictions.addAssociation("dealerAddresses", "deaAddress", JoinType.INNER_JOIN);
		restrictions.addAssociation("deaAddress.address", "address", JoinType.INNER_JOIN);
		restrictions.addOrder(Order.desc("address.province.id"));
		List<Dealer> dealers = entityService.list(restrictions);
		
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
		sheet.setColumnWidth(0, 3000);
		sheet.setColumnWidth(1, 9000);
		sheet.setColumnWidth(2, 8000);
		sheet.setColumnWidth(3, 8000);
		sheet.setColumnWidth(4, 3000);
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
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
		createCell(headerRow, 0, "Percentage of Customers know GLF By Radio", 16, true, false, false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,false);

		int iRow = 0;
		int iCol = 0;
		iCol = iCol + 2;
		iCol = 0;
		iRow = iRow + 1;
		if (date == null) {
			date = DateUtils.today();
		}
		iRow = DataTable(sheet, iRow, style, dealers, date);

//		Row tmpRowEnd = sheet.createRow(iRow++);
//		tmpRowEnd.setRowStyle(style);

		iRow = iRow + 1;

		String fileName = writeXLSData("Marketing_By_Radio" + DateUtils.getDateLabel(DateUtils.today(), "yyyyMMddHHmmssSSS") + ".xlsx");

		return fileName;
	}

	private int DataTable(final Sheet sheet, int iRow, final CellStyle style, List<Dealer> dealers, Date date) throws Exception {
		/* Create total data header */
		Format formatter = new SimpleDateFormat("MMMM");  
		String month = formatter.format(date);
		int iCol = 0;
		Row tmpRow = sheet.createRow(iRow++);
		
		createCell(tmpRow, iCol++, "No.", 12, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		createCell(tmpRow, iCol++, "POS Name", 12, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		createCell(tmpRow, iCol++, "Amount Visitor in " + month , 12, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		createCell(tmpRow, iCol++, "Amount of Visitor's Radio", 12, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		createCell(tmpRow, iCol++, "%", 12, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);		
		
		if (dealers != null && !dealers.isEmpty()) {
			int nbRows = dealers.size();
			int visitorInMonth = 0;
			int visitorByRadio = 0;
			int index = 0;
			for (int i = 0; i < nbRows; i++) {
				Dealer dealer = dealers.get(i);
				BaseRestrictions<Statistic3HoursVisitor> restrictions = new BaseRestrictions<Statistic3HoursVisitor>(Statistic3HoursVisitor.class);		
				restrictions.addCriterion(Restrictions.eq("dealer.id", dealer.getId()));
				restrictions.addCriterion(Restrictions.ge("createDate", DateUtils.getDateAtBeginningOfMonth(date)));
				restrictions.addCriterion(Restrictions.le("createDate", DateUtils.getDateAtEndOfMonth(date)));
				List<Statistic3HoursVisitor> statisticVisitors = entityService.list(restrictions);
				if (statisticVisitors != null && !statisticVisitors.isEmpty()) {
					for (Statistic3HoursVisitor statisticVisitor : statisticVisitors) {
						visitorInMonth += statisticVisitor.getNumberVisitorCompany11();
						visitorInMonth += statisticVisitor.getNumberVisitorCompany14();
						visitorInMonth += statisticVisitor.getNumberVisitorCompany17();
//						if (statisticVisitor.getWayOfKnowing() != null) {
//							if (statisticVisitor.getWayOfKnowing().getCode() != null) {
//								if (statisticVisitor.getWayOfKnowing().getCode().equals("Radio")
//										|| statisticVisitor.getWayOfKnowing().getCode().equals("radio")) {
//									visitorByRadio += statisticVisitor.getNumberVisitorCompany();
//								}	
//							}
//						}
					}
				}
				
				if (i == nbRows - 1 || dealers.get(i).getDealerAddresses().get(0).getAddress().getProvince().getId() != dealers.get(i + 1).getDealerAddresses().get(0).getAddress().getProvince().getId()) {
					tmpRow = sheet.createRow(iRow++);
					iCol = 0;
					index++;
					createNumericCell(tmpRow, iCol++, index, true,
							CellStyle.ALIGN_CENTER, 14, false, true, BG_WHITE, FC_BLACK);
					createNumericCell(tmpRow, iCol++, dealers.get(i).getDealerAddresses().get(0).getAddress().getProvince().getDescEn(), true,
							CellStyle.ALIGN_CENTER, 14, false, true, BG_WHITE, FC_BLACK);
					createNumericCell(tmpRow, iCol++, visitorInMonth, true,
							CellStyle.ALIGN_CENTER, 14, false, true, BG_WHITE, FC_BLACK);
					createNumericCell(tmpRow, iCol++, visitorByRadio, true,
							CellStyle.ALIGN_CENTER, 14, false, true, BG_WHITE, FC_BLACK);
					createNumericCell(tmpRow, iCol++, visitorByRadio * 100 / (visitorInMonth == 0 ? 1 : visitorInMonth)+"%", true,
							CellStyle.ALIGN_CENTER, 14, false, true, BG_WHITE, FC_BLACK);
					visitorInMonth = 0;
					visitorByRadio = 0;
				}
				
			}

			iRow = iRow + 1;
		}

		return iRow;

	}

    /**
     * 
     * @param dt
     * @param nbHours
     * @return
     */
    public static Date getAddBeginningOfHours(Date dt, int nbHours) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.set(Calendar.HOUR_OF_DAY, nbHours);
        cal.set(Calendar.MINUTE, 00);
        cal.set(Calendar.SECOND, 00);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
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
		if (value instanceof Double) {
            cell.setCellValue((Double) value);
            cell.setCellStyle(styles.get(AMOUNT));
            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        }
		cell.setCellStyle(styles.get("ALIGN_CENTER"));	
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
		
		final Font itemFont = wb.createFont();
		itemFont.setFontHeightInPoints((short) 14);
		itemFont.setFontName("Khmer OS Battambang");
		style = wb.createCellStyle();
		style.setFont(itemFont);
		styles.put(BODY, style);
		
	  	DataFormat format = wb.createDataFormat();
    	style = wb.createCellStyle();
    	style.setAlignment(CellStyle.ALIGN_RIGHT);
    	style.setFont(itemFont);
    	style.setDataFormat(format.getFormat("#,##0.00"));
    	styles.put(AMOUNT, style);
    	
    	style = wb.createCellStyle();
    	style.setAlignment(CellStyle.ALIGN_CENTER);
    	style.setFont(itemFont);
    	styles.put("ALIGN_CENTER", style);

		return styles;
	}

	protected CellStyle getCellStyle(final String styleName) {
		return styles.get(styleName);
	}
}

