package com.nokor.efinance.gui.report.xls;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.joda.time.DateTime;
import org.joda.time.Months;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.quotation.QuotationService;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.conf.AppConfig;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.efinance.core.shared.util.DateFilterUtil;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.efinance.glf.statistic.model.Statistic3HoursVisitor;
import com.nokor.efinance.tools.report.Report;
import com.nokor.efinance.tools.report.XLSAbstractReportExtractor;
import com.nokor.frmk.security.context.SecApplicationContextHolder;

/**
 * Weekly report
 * @author uhout.cheng
 * @author buntha.chea (Modified)
 * @since 16/12/2014
 */
public class GLFWeeklyReport extends XLSAbstractReportExtractor implements
		Report, GLFApplicantFields, QuotationEntityField {

	protected QuotationService quotationService = (QuotationService) SecApplicationContextHolder
			.getContext().getBean("quotationService");

	private Map<String, CellStyle> styles = null;

	public static String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";
	
	static short BG_WHITE = IndexedColors.WHITE.getIndex();
	static short FC_BLACK = IndexedColors.BLACK.getIndex();

	private long totalHondaVisitor;
	private long totalGLFVisitor;
	private long totalNumApply;
	private long totalNumcontract;
	
	private Date startDate;
	private Date endDate;
	
	/**
	 * Generate the excel
	 * @see com.nokor.efinance.tools.report.Report#generate(com.nokor.efinance.core.shared.report.ReportParameter)
	 */
	@Override
	public String generate(ReportParameter reportParameter) throws Exception {
		Map<String, Object> parameters = reportParameter.getParameters();
		Date startMonth = (Date) parameters.get("startDateValue");
		Date endMonth = (Date) parameters.get("endDateValue");
		Dealer dealer = (Dealer) parameters.get("dealer");
		EDealerType dealerType = (EDealerType) parameters.get("dealerType");
		
		startDate = (Date) parameters.get("startDateValue");
		endDate = (Date) parameters.get("endDateValue");
		
		//Start Year,Month,Day
	    int syear = DateUtils.getYear(startMonth);
	    int sMonth =  DateUtils.getMonth(startMonth);
	    int sDay = DateUtils.getDay(DateUtils.getDateAtBeginningOfMonth(startMonth));
	    
	    //End Year,Month,Day
	    int eYear = DateUtils.getYear(endMonth);
	    int eMonth = DateUtils.getMonth(endMonth);
	    int eDay = DateUtils.getDay(DateUtils.getDateAtEndOfMonth(endMonth));
		
		
		DateTime start = new DateTime().withDate(syear,sMonth,sDay);
		DateTime end = new DateTime().withDate(eYear,eMonth,eDay);
		
		//Calculate number of month from startMonth to endMonth 
		Months mt = Months.monthsBetween(start, end);
	    int monthDiff = mt.getMonths() + 1;

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
		sheet.setColumnWidth(0, 2900);
		sheet.setColumnWidth(1, 3000);
		sheet.setColumnWidth(2, 5300);
		sheet.setColumnWidth(3, 3200);
		sheet.setColumnWidth(4, 3000);
		sheet.setColumnWidth(5, 4750);
		sheet.setColumnWidth(6, 3000);
		sheet.setColumnWidth(7, 3900);
		sheet.setColumnWidth(8, 3500);
		sheet.setColumnWidth(9, 3000);
		sheet.setColumnWidth(10, 4500);
		sheet.setColumnWidth(11, 4300);
		sheet.setZoom(9, 10);
		final PrintSetup printSetup = sheet.getPrintSetup();

		printSetup.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);

		printSetup.setScale((short) 58);
		
		// Setup the Page margins - Left, Right, Top and Bottom
		sheet.setMargin(Sheet.LeftMargin, 0);
		sheet.setMargin(Sheet.RightMargin, 0);
		sheet.setMargin(Sheet.TopMargin, 0.25);
		sheet.setMargin(Sheet.BottomMargin, 0.25);
		sheet.setMargin(Sheet.HeaderMargin, 0.25);
		sheet.setMargin(Sheet.FooterMargin, 0.25);
		style.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.BIG_SPOTS);
		style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		
		int iRow = 2;
		
		if (startMonth == null) {
			startMonth = DateUtils.getDateAtBeginningOfMonth();
		} else {
			startMonth = DateUtils.getDateAtBeginningOfMonth(startMonth);
		}
		
		if (startDate == null) {
			startDate = DateUtils.getDateAtBeginningOfMonth();
		} 
		if (endDate == null) {
			endDate = DateUtils.getDateAtBeginningOfMonth();
		}
		
		for (int i = 0; i < monthDiff; i++) {
			Date month = DateUtils.addMonthsDate(startMonth, i);
			if (i != 0 && i % 6 == 0) {
				sheet.setRowBreak(iRow);
			}
			iRow = dataTable(sheet, iRow, month, dealer, dealerType, i);
			iRow = iRow + 6;
		}
		String fileName = writeXLSData("Weekly_Report"+ DateUtils.getDateLabel(DateUtils.today(), "yyyyMMddHHmmssSSS")+ ".xlsx");

		return fileName;
	}
	
	/**
	 * 
	 * @param sheet
	 * @param iRow
	 * @param selectStartDate
	 * @param dealer
	 * @param dealerType
	 * @param i
	 * @return
	 * @throws Exception
	 */
	private int dataTable(final Sheet sheet, int iRow, Date selectStartDate, Dealer dealer, EDealerType dealerType, int i)
			throws Exception {
		if (i == 0) {
			Row headerRow = sheet.createRow(iRow - 1);
			// Set Logo GLF
			Drawing drawing = sheet.createDrawingPatriarch();
			ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, iRow, iRow + 2, iRow);
			String templatePath = AppConfig.getInstance().getConfiguration().getString("specific.templatedir");
			String templateFileName = templatePath + "/GLF-logo-small.png";
			File image = new File(templateFileName);
			InputStream fStream = new FileInputStream(image);
			byte[] bytes = IOUtils.toByteArray(fStream);
			int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
			Picture picture = drawing.createPicture(anchor, pictureIdx);
			picture.resize();
			fStream.close();
				
			//Create Title On Header
			for (int j = 0; j <= 11; j++) {
				createCell(headerRow, j, "", 14, false, false, false, CellStyle.ALIGN_CENTER, false, BG_WHITE, FC_BLACK, false);
			}
				
			sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow + 2, 0, 11));
			iRow = iRow + 1;
			createCell(headerRow,0,"GLF Sale by Weekly in "+ DateUtils.getDateLabel(selectStartDate, "yyyy"), 20,
					false, false, false, CellStyle.ALIGN_CENTER, false, BG_WHITE,FC_BLACK, false);
			createCell(headerRow, 11, "", 14, false, false, false,CellStyle.ALIGN_LEFT, false, BG_WHITE, FC_BLACK, false);
		}
		
		iRow = iRow + 2;
		Row tmpRow = sheet.createRow(iRow++);
		int iCol = 0;
		createCell(tmpRow, iCol++, "SALES", 16, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, false);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, 0, 11));
		createCell(tmpRow, iCol++, "", 12, false, false, true,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, false);
		createCell(tmpRow, iCol++, "", 12, false, false, true,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, false);
		createCell(tmpRow, iCol++, "", 12, false, false, true,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, false);
		createCell(tmpRow, iCol++, "", 12, false, false, true,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, false);
		createCell(tmpRow, iCol++, "", 12, false, false, true,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, false);
		createCell(tmpRow, iCol++, "", 12, false, false, true,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, false);
		createCell(tmpRow, iCol++, "", 12, false, false, true,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, false);
		createCell(tmpRow, iCol++, "", 12, false, false, true,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, false);
		createCell(tmpRow, iCol++, "", 12, false, false, true,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, false);
		createCell(tmpRow, iCol++, "", 12, false, false, true,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, false);
		createCell(tmpRow, iCol++, "", 12, false, false, true,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, false);

		tmpRow = sheet.createRow(iRow++);
		iCol = 0;
		createCell(tmpRow, iCol++, "Month", 12, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow + 1, 0, 0));

		createCell(tmpRow, iCol++, "Weekly", 12, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow + 1, 1, 1));

		createCell(tmpRow, iCol++, "Date", 12, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow + 1, 2, 2));

		createCell(tmpRow, iCol++, "Honda Visitors", 12, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow + 1, 3, 3));

		createCell(tmpRow, iCol++, "GLF Visitors", 12, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow + 1, 4, 4));

		createCell(tmpRow, iCol++, "GLF Visitors / Honda Visitors", 12, false,
				true, false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
				true);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow + 1, 5, 5));

		createCell(tmpRow, iCol++, "Apply", 12, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow + 1, 6, 6));

		createCell(tmpRow, iCol++, "Apply / Honda Visitors", 12, false, true,
				false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow + 1, 7, 7));

		createCell(tmpRow, iCol++, "Apply / GLF Visitors", 12, false, true,
				false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow + 1, 8, 8));

		createCell(tmpRow, iCol++, "New Contracts", 12, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow + 1, 9, 9));

		createCell(tmpRow, iCol++, "New Contracts / GLF Visitors", 12, false, true,
				false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow + 1, 10, 10));

		createCell(tmpRow, iCol++, "New Contracts / Apply", 12, false, true,
				false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow + 1, 11, 11));

		tmpRow = sheet.createRow(iRow++);
		iCol = 0;
		createCell(tmpRow, iCol++, "", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
		createCell(tmpRow, iCol++, "", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
		createCell(tmpRow, iCol++, "", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
		createCell(tmpRow, iCol++, "", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
		createCell(tmpRow, iCol++, "", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
		createCell(tmpRow, iCol++, "", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
		createCell(tmpRow, iCol++, "", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
		createCell(tmpRow, iCol++, "", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
		createCell(tmpRow, iCol++, "", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
		createCell(tmpRow, iCol++, "", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
		createCell(tmpRow, iCol++, "", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
		createCell(tmpRow, iCol++, "", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);

		tmpRow = sheet.createRow(iRow++);
		iCol = 0;
		createCell(tmpRow, iCol++, "", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
		createCell(tmpRow, iCol++, "", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
		createCell(tmpRow, iCol++, "", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
		createCell(tmpRow, iCol++, "", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
		createCell(tmpRow, iCol++, "", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
		createCell(tmpRow, iCol++, "", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
		createCell(tmpRow, iCol++, "", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
		createCell(tmpRow, iCol++, "", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
		createCell(tmpRow, iCol++, "", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
		createCell(tmpRow, iCol++, "", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
		createCell(tmpRow, iCol++, "", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
		createCell(tmpRow, iCol++, "", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
	
		String month = DateUtils.formatDate(selectStartDate, "MM/yyyy");
		getWeeklyDataTable(sheet, iRow, month, selectStartDate, dealer, dealerType);

		return iRow;

	}

	/**
	 * 
	 * @param sheet
	 * @param iRow
	 * @param month
	 * @param selectStartDate
	 * @param dealer
	 * @param dealerType
	 */
	private void getWeeklyDataTable(final Sheet sheet, int iRow, String month,
			Date selectStartDate, Dealer dealer, EDealerType dealerType) {
		int iCol = 0;
		String[] strWeek = { "st", "nd", "rd", "th", "th", "th" };

		// initialize total to zero
		totalHondaVisitor = 0;
		totalGLFVisitor = 0;
		totalNumApply = 0;
		totalNumcontract = 0;

		// Per week calculation
		Date startOfWeek = selectStartDate;
		Date endOfMonth = DateUtils.getDateAtEndOfMonth(selectStartDate);
		//Date endOfWeek = DateUtils.addDaysDate(DateUtils.getDateAtEndOfWeek(selectStartDate), 1);
		Date endOfWeek = DateUtils.addDaysDate(startOfWeek, 6);
		long numDayInMonth = DateUtils.getDiffInDaysPlusOneDay(endOfMonth,startOfWeek);
		int iWeek = 0;
		while (numDayInMonth > 0) {
			numDayInMonth -= DateUtils.getDiffInDaysPlusOneDay(endOfWeek,startOfWeek);
			Row tmpRow = sheet.createRow(iRow++);
			iCol = 0;

			if (iWeek == 0) {
				createCell(tmpRow, iCol++, month, 12, false, true, false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, false);
			} else {
				createCell(tmpRow, iCol++, month, 12, false, true, false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, false);
			}
			createCell(tmpRow, iCol++, iWeek + 1 + strWeek[iWeek++] + " week", 12, false, true, false, CellStyle.ALIGN_RIGHT, true,
					BG_WHITE, FC_BLACK, false);
			createCell(tmpRow, iCol++, DateUtils.getDateLabel(startOfWeek, "dd") + " to " + DateUtils.getDateLabel(endOfWeek), 12, 
					false, true, false, CellStyle.ALIGN_RIGHT, true, BG_WHITE, FC_BLACK, false);
			
			startDate = DateUtils.getDateAtBeginningOfDay(startDate);
			endDate = DateUtils.getDateAtEndOfDay(endDate);
			if (startDate.compareTo(DateUtils.getDateAtBeginningOfDay(startOfWeek)) >= 0 
					&& startDate.compareTo(DateUtils.getDateAtEndOfDay(endOfWeek)) <= 0) {
				if (endDate.compareTo(DateUtils.getDateAtEndOfDay(endOfWeek)) <= 0) {
					createRow(tmpRow, iCol, startDate, endDate, dealer, dealerType);
				} else {
					createRow(tmpRow, iCol, startDate, endOfWeek, dealer, dealerType); 
					startDate = DateUtils.addDaysDate(endOfWeek, 1);
				}
			} else {				
				createRowZero(tmpRow, iCol, dealer, dealerType);	
			}
			
			startOfWeek = DateUtils.addDaysDate(endOfWeek, 1);
			if (numDayInMonth >= 7) {
				//endOfWeek = DateUtils.addDaysDate(DateUtils.getDateAtEndOfWeek(startOfWeek), 1);
				endOfWeek = DateUtils.addDaysDate(startOfWeek, 6);
			} else {
				endOfWeek = DateUtils.addDaysDate(startOfWeek, (int) numDayInMonth - 1);
			}
		}
		sheet.addMergedRegion(new CellRangeAddress(iRow - iWeek, iRow - 1, 0, 0));

		// create total row at the bottom
		sheet.addMergedRegion(new CellRangeAddress(iRow, iRow, 0, 2));
		createTotalRow(sheet.createRow(iRow++), iWeek - 1);
	}
	
	/**
	 * Display number zero 
	 * @param row
	 * @param iCol
	 * @param dealer
	 * @param dealerType
	 */
	private void createRowZero(Row row, int iCol, Dealer dealer, EDealerType dealerType) {
		// Put number zero into cell
		createCell(row, iCol++, 0, 14, false, true, false, CellStyle.ALIGN_RIGHT, true, BG_WHITE, FC_BLACK, false);
		createCell(row, iCol++, 0, 14, false, true, false, CellStyle.ALIGN_RIGHT, true, BG_WHITE, FC_BLACK, false);
		createCell(row, iCol++, 0d,14, false, true, false, CellStyle.ALIGN_RIGHT, true, BG_WHITE,FC_BLACK, false);
		createCell(row, iCol++, 0, 14, false, true, false, CellStyle.ALIGN_RIGHT, true, BG_WHITE, FC_BLACK, false);
		createCell(row, iCol++, 0d, 14, false, true, false, CellStyle.ALIGN_RIGHT, true, BG_WHITE, FC_BLACK, false);
		createCell(row, iCol++, 0d, 14, false, true, false, CellStyle.ALIGN_RIGHT, true, BG_WHITE, FC_BLACK, false);
		createCell(row, iCol++, 0, 14, false, true, false, CellStyle.ALIGN_RIGHT, true, BG_WHITE, FC_BLACK, false);
		createCell(row, iCol++, 0d, 14, false, true, false, CellStyle.ALIGN_RIGHT, true, BG_WHITE, FC_BLACK, false);
		createCell(row, iCol++, 0d, 14,false, true, false, CellStyle.ALIGN_RIGHT, true, BG_WHITE, FC_BLACK, false);
	}

	/**
	 * Create a row of data per week
	 * @param row
	 * @param iCol
	 * @param startDate
	 * @param endDate
	 * @param dealer
	 * @param dealerType
	 */
	private void createRow(Row row, int iCol, Date startDate, Date endDate, Dealer dealer, EDealerType dealerType) {
		long hondaVisitor = 0;
		long GLFVisitor = 0;
		long numApply = 0;
		long numContract = 0;
		List<Statistic3HoursVisitor> statistic3HoursVisitors = getListStatistic3HoursVisitors(
				startDate, endDate, dealer, dealerType);

		if (statistic3HoursVisitors != null) {
			for (Statistic3HoursVisitor statistic3HoursVisitor : statistic3HoursVisitors) {
				hondaVisitor += MyNumberUtils.getInteger(statistic3HoursVisitor
						.getNumberVisitorDealer11());
				hondaVisitor += MyNumberUtils.getInteger(statistic3HoursVisitor
						.getNumberVisitorDealer14());
				hondaVisitor += MyNumberUtils.getInteger(statistic3HoursVisitor
						.getNumberVisitorDealer17());
				GLFVisitor += MyNumberUtils.getInteger(statistic3HoursVisitor
						.getNumberVisitorCompany11());
				GLFVisitor += MyNumberUtils.getInteger(statistic3HoursVisitor
						.getNumberVisitorCompany14());
				GLFVisitor += MyNumberUtils.getInteger(statistic3HoursVisitor
						.getNumberVisitorCompany17());
			}
		}
		numApply = getNumQuotationTotalApply(startDate, endDate, dealer, dealerType);
		numContract = getNumNewContract(startDate, endDate, dealer, dealerType);

		// Add to total
		totalHondaVisitor += hondaVisitor;
		totalGLFVisitor += GLFVisitor;
		totalNumApply += numApply;
		totalNumcontract += numContract;

		// Put data into cell
		createCell(row, iCol++, hondaVisitor, 14, false, true, false,
				CellStyle.ALIGN_RIGHT, true, BG_WHITE, FC_BLACK, false);
		createCell(row, iCol++, GLFVisitor, 14, false, true, false,
				CellStyle.ALIGN_RIGHT, true, BG_WHITE, FC_BLACK, false);
		createCell(row, iCol++, calculatePercentage(GLFVisitor, hondaVisitor),
				14, false, true, false, CellStyle.ALIGN_RIGHT, true, BG_WHITE,
				FC_BLACK, false);
		createCell(row, iCol++, numApply, 14, false, true, false,
				CellStyle.ALIGN_RIGHT, true, BG_WHITE, FC_BLACK, false);
		createCell(row, iCol++, calculatePercentage(numApply, hondaVisitor),
				14, false, true, false, CellStyle.ALIGN_RIGHT, true, BG_WHITE,
				FC_BLACK, false);
		createCell(row, iCol++, calculatePercentage(numApply, GLFVisitor), 14,
				false, true, false, CellStyle.ALIGN_RIGHT, true, BG_WHITE,
				FC_BLACK, false);
		createCell(row, iCol++, numContract, 14, false, true, false,
				CellStyle.ALIGN_RIGHT, true, BG_WHITE, FC_BLACK, false);
		createCell(row, iCol++, calculatePercentage(numContract, GLFVisitor),
				14, false, true, false, CellStyle.ALIGN_RIGHT, true, BG_WHITE,
				FC_BLACK, false);
		createCell(row, iCol++, calculatePercentage(numContract, numApply), 14,
				false, true, false, CellStyle.ALIGN_RIGHT, true, BG_WHITE,
				FC_BLACK, false);
	}

	/**
	 *  Create a total row at the bottom
	 * @param row
	 * @param iWeek
	 */
	private void createTotalRow(Row row, int iWeek) {
		int iCol = 0;
		createCell(row, iCol++, "Total", 12, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, false);
		createCell(row, iCol++, "", 10, false, true, false,
				CellStyle.ALIGN_RIGHT, true, BG_WHITE, FC_BLACK, false);
		createCell(row, iCol++, "", 10, false, true, false,
				CellStyle.ALIGN_RIGHT, true, BG_WHITE, FC_BLACK, false);
		createCell(row, iCol++, totalHondaVisitor, 14, false, true, false,
				CellStyle.ALIGN_RIGHT, true, BG_WHITE, FC_BLACK, false);
		createCell(row, iCol++, totalGLFVisitor, 14, false, true, false,
				CellStyle.ALIGN_RIGHT, true, BG_WHITE, FC_BLACK, false);
		createCell(row, iCol++,
				calculatePercentage(totalGLFVisitor, totalHondaVisitor), 14,
				false, true, false, CellStyle.ALIGN_RIGHT, true, BG_WHITE,
				FC_BLACK, false);
		createCell(row, iCol++, totalNumApply, 14, false, true, false,
				CellStyle.ALIGN_RIGHT, true, BG_WHITE, FC_BLACK, false);
		createCell(row, iCol++,
				calculatePercentage(totalNumApply, totalHondaVisitor), 14,
				false, true, false, CellStyle.ALIGN_RIGHT, true, BG_WHITE,
				FC_BLACK, false);
		createCell(row, iCol++,
				calculatePercentage(totalNumApply, totalGLFVisitor), 14, false,
				true, false, CellStyle.ALIGN_RIGHT, true, BG_WHITE, FC_BLACK,
				false);
		createCell(row, iCol++, totalNumcontract, 14, false, true, false,
				CellStyle.ALIGN_RIGHT, true, BG_WHITE, FC_BLACK, false);
		createCell(row, iCol++,
				calculatePercentage(totalNumcontract, totalGLFVisitor), 14,
				false, true, false, CellStyle.ALIGN_RIGHT, true, BG_WHITE,
				FC_BLACK, false);
		createCell(row, iCol++,
				calculatePercentage(totalNumcontract, totalNumApply), 14,
				false, true, false, CellStyle.ALIGN_RIGHT, true, BG_WHITE,
				FC_BLACK, false);
	}
	
	/**
	 * Calculate the percentage
	 * @param value1
	 * @param value2
	 * @return
	 */
	private double calculatePercentage(long value1, long value2) {
		if (value2 == 0) {
			return 0d;
		}
		return ((double) value1 / value2);
	}

	/**
	 * Get a list of statistic3HoursVisitor
	 * @param startDate
	 * @param endDate
	 * @param dealer
	 * @param dealerType
	 * @return
	 */
	private List<Statistic3HoursVisitor> getListStatistic3HoursVisitors(
			Date startDate, Date endDate, Dealer dealer, EDealerType dealerType) {
		BaseRestrictions<Statistic3HoursVisitor> restrictions1 = new BaseRestrictions<>(
				Statistic3HoursVisitor.class);
		restrictions1.addAssociation("dealer", "stadeal", JoinType.INNER_JOIN);
		if (dealerType != null) {
			restrictions1.addCriterion(Restrictions.eq("stadeal.dealerType", dealerType));
		}
		if (dealer != null) {
			restrictions1.addCriterion(Restrictions.eq("stadeal.id", dealer.getId()));
		}
		restrictions1.addCriterion(Restrictions.ge("date", DateFilterUtil.getStartDate(startDate)));
		restrictions1.addCriterion(Restrictions.le("date", DateFilterUtil.getEndDate(endDate)));
		return quotationService.list(restrictions1);
	}

	/**
	 * Get the number of apply
	 * @param startDate
	 * @param endDate
	 * @param dealer
	 * @param dealerType
	 * @return
	 */
	private long getNumQuotationTotalApply(Date startDate, Date endDate, Dealer dealer, EDealerType dealerType) {
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<Quotation>(
				Quotation.class);
		restrictions.addAssociation("dealer", "quodeal", JoinType.INNER_JOIN);
		if (dealerType != null) {
			restrictions.addCriterion(Restrictions.eq("quodeal.dealerType", dealerType));
		}
		if (dealer != null) {
			restrictions.addCriterion(Restrictions.eq("quodeal.id", dealer.getId()));
		}
		restrictions.addCriterion(Restrictions.ge("firstSubmissionDate", DateFilterUtil.getStartDate(startDate)));
		restrictions.addCriterion(Restrictions.le("firstSubmissionDate", DateFilterUtil.getEndDate(endDate)));
		return quotationService.count(restrictions);
	}

	/**
	 * Get the number of new contract
	 * @param startDate
	 * @param endDate
	 * @param dealer
	 * @param dealerType
	 * @return
	 */
	private long getNumNewContract(Date startDate, Date endDate, Dealer dealer, EDealerType dealerType) {
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(Quotation.class);
		restrictions.addAssociation("dealer", "paydeal", JoinType.INNER_JOIN);
		if (dealerType != null) {
			restrictions.addCriterion(Restrictions.eq("paydeal.dealerType", dealerType));
		}
		if (dealer != null) {
			restrictions.addCriterion(Restrictions.eq("paydeal.id", dealer.getId()));
		}
		restrictions.addCriterion(Restrictions.eq(WKF_STATUS, QuotationWkfStatus.ACT));
		restrictions.addCriterion(Restrictions.ge("activationDate", DateFilterUtil.getStartDate(startDate)));
		restrictions.addCriterion(Restrictions.le("activationDate", DateFilterUtil.getEndDate(endDate)));
		return quotationService.count(restrictions);
	}

	/**
	 * 
	 * @param row
	 * @param iCol
	 * @param value
	 * @param fontsize
	 * @param isBold
	 * @param hasBorder
	 * @param leftRight
	 * @param alignment
	 * @param setBgColor
	 * @param bgColor
	 * @param fonCorlor
	 * @param wrapText
	 * @return
	 */
	protected Cell createCell(final Row row, final int iCol,
			final Object value, final int fontsize, final boolean isBold,
			final boolean hasBorder, final boolean leftRight,
			final short alignment, final boolean setBgColor,
			final short bgColor, final short fonCorlor, boolean wrapText) {

		final Cell cell = row.createCell(iCol);
		final Font itemFont = wb.createFont();
		itemFont.setFontHeightInPoints((short) fontsize);
		itemFont.setFontName("Arial");

		final CellStyle style = wb.createCellStyle();
		
		//format data to number in excel
		DataFormat format = wb.createDataFormat();
		
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
		if (isBold) {
			style.setBorderLeft(CellStyle.BORDER_THIN);
		}
		if (leftRight) {
			style.setBorderTop(CellStyle.BORDER_THIN);
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
		if (value instanceof Integer) {
			style.setDataFormat(format.getFormat("0"));
			cell.setCellValue(Integer.valueOf(value.toString()));
			cell.setCellStyle(style);
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		} else if (value instanceof Long) {
			style.setDataFormat(format.getFormat("0"));
			cell.setCellValue(Long.valueOf(value.toString()));
			cell.setCellStyle(style);
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		} else if (value instanceof Double) {
			style.setDataFormat(format.getFormat("##0.00%"));
			cell.setCellValue((Double) value);
			cell.setCellStyle(style);
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		} else {
			cell.setCellValue((value == null ? "" : value.toString()));
		}
		cell.setCellStyle(style);
		return cell;
	}

	/**
	 * 
	 * @return style
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
		itemFont.setFontHeightInPoints((short) 10);
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

		return styles;
	}

	/**
	 * @see com.nokor.efinance.tools.report.XLSAbstractReportExtractor#getCellStyle(java.lang.String)
	 */
	protected CellStyle getCellStyle(final String styleName) {
		return styles.get(styleName);
	}
}
