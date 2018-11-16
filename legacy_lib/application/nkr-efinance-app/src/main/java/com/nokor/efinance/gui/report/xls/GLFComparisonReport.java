package com.nokor.efinance.gui.report.xls;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;

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

/**
 * Comparison report
 * @author bunlong.taing
 */
public class GLFComparisonReport extends XLSAbstractReportExtractor implements Report, QuotationEntityField, GLFApplicantFields {
	
	protected QuotationService quotationService = SpringUtils.getBean(QuotationService.class);
	protected EntityService entityService = SpringUtils.getBean(EntityService.class);
	
	static short BG_WHITE = IndexedColors.WHITE.getIndex();
	
	private Dealer paramDealer;
	private EDealerType paramDealerType;

	/**
	 * @see com.nokor.efinance.tools.report.Report#generate(com.nokor.efinance.core.shared.report.ReportParameter)
	 */
	@Override
	public String generate(ReportParameter reportParameter) throws Exception {
		Map<String, Object> parameters = reportParameter.getParameters();
		Date date = (Date) parameters.get("date");
		paramDealer = (Dealer) parameters.get("dealer");
		paramDealerType = (EDealerType) parameters.get("dealerType");
		
		createWorkbook(null);
		XSSFSheet sheet = setUpWorkBook();
		if (date == null) {
			date = DateUtils.getDateAtEndOfLastMonth();
		}
		
		BaseRestrictions<Dealer> restrictions = new BaseRestrictions<>(Dealer.class);
		restrictions.addAssociation("dealerAddresses", "deaAddress", JoinType.INNER_JOIN);
		restrictions.addAssociation("deaAddress.address", "address", JoinType.INNER_JOIN);
		restrictions.addAssociation("address.province", "province", JoinType.INNER_JOIN);
		restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
		if (paramDealerType != null) {
			restrictions.addCriterion(Restrictions.eq("dealerType", paramDealerType));
		}
		if (paramDealer != null) {
			restrictions.addCriterion(Restrictions.eq("id", paramDealer.getId()));
		}
		restrictions.addOrder(Order.asc("province.descEn"));
		List<Dealer> dealers = entityService.list(restrictions);
		
		dataTable(sheet, date, dealers, 1);
		
		return writeXLSData("comparison_report" + DateUtils.getDateLabel(DateUtils.today(), "yyyyMMddHHmmssSSS") + ".xlsx");
	}
	
	/**
	 * Generate the data
	 * @param sheet
	 * @param date
	 * @param dealers
	 * @param iRow
	 * @throws Exception
	 */
	private void dataTable(XSSFSheet sheet, Date date, List<Dealer> dealers, int iRow) 
			throws Exception {
		date = DateUtils.getDateAtEndOfDay(date);
		Row headerRow = sheet.createRow(iRow);

		Drawing drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, iRow +1, iRow + 3, iRow + 1);
		String templatePath = AppConfig.getInstance().getConfiguration().getString("specific.templatedir");
		String templateFileName = templatePath + "/GLF-logo-small.png";
        File image = new File(templateFileName);
        InputStream fStream = new FileInputStream(image);
        byte[] bytes = IOUtils.toByteArray(fStream);
        int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
        Picture picture = drawing.createPicture(anchor, pictureIdx);
        picture.resize();
        fStream.close();
        
        // Title of the report
        sheet.addMergedRegion(new CellRangeAddress(iRow, iRow + 3, 0, 10));
		createCell(headerRow, 0, "Comparison Report", 20, false, false, false, CellStyle.ALIGN_CENTER, false, BG_WHITE, FC_BLACK, false);
		createCell(headerRow, 8, "", 14, false, false, false, CellStyle.ALIGN_LEFT, false, BG_WHITE, FC_BLACK, false);
		
		// Table header
		iRow = iRow + 4;
		int iCol = 0;
		Row row = sheet.createRow(iRow++);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, 0, 2));
		createCell(row, iCol++, "POS PERFORMANCE COMPARATIVE\nMTD " + DateUtils.getDateLabel(date), 12, true, true, true, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
		createCell(row, iCol++, "", 12, true, true, true, CellStyle.ALIGN_LEFT, true, BG_GREY, FC_BLACK, true);
		createCell(row, iCol++, "", 12, true, true, true, CellStyle.ALIGN_LEFT, true, BG_GREY, FC_BLACK, true);
		createCell(row, iCol++, "Honda Visitors", 12, true, true, true, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
		createCell(row, iCol++, "GLF Visitors", 12, true, true, true, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
		createCell(row, iCol++, "Apply", 12, true, true, true, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
		createCell(row, iCol++, "New Contracts", 12, true, true, true, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
		createCell(row, iCol++, "GLF Visitors / Honda Visitors", 12, true, true, true, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
		createCell(row, iCol++, "Apply / GLF Visitors", 12, true, true, true, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
		createCell(row, iCol++, "New Contracts / GLF Visitors", 12, true, true, true, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
		createCell(row, iCol++, "New Contracts / Apply", 12, true, true, true, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
		
		// Detail the data of the table
		builtTableData(sheet, iRow, date, dealers);
	}
	
	/**
	 * Build the table data
	 * @param sheet
	 * @param iRow
	 * @param date
	 * @param dealers
	 */
	private void builtTableData(XSSFSheet sheet, int iRow, Date date, List<Dealer> dealers) {
		date = DateUtils.getDateAtEndOfDay(date);
		int nRow = iRow + 2;
	
		int totalHondaVisitor = 0;
		int totalGLFVisitor = 0;
		int totalApply = 0;
		int totalContract = 0;
		
		// Detail row
		int dealerID = 0;
		for (Dealer dealer : dealers) {
			int hondaVisitor = 0;
			int GLFVisitor = 0;
			int apply = 0;
			int contract = 0;
			dealerID++;
			List<Statistic3HoursVisitor> statistic3HoursVisitors = getStatistic3HoursVisitor(dealer.getDealerType(), dealer, date);
			
			if (statistic3HoursVisitors != null && !statistic3HoursVisitors.isEmpty()) {
				for (Statistic3HoursVisitor statisticVisitor : statistic3HoursVisitors) {
					hondaVisitor += MyNumberUtils.getInteger(statisticVisitor.getNumberVisitorDealer11());
					hondaVisitor += MyNumberUtils.getInteger(statisticVisitor.getNumberVisitorDealer14());
					hondaVisitor += MyNumberUtils.getInteger(statisticVisitor.getNumberVisitorDealer17());
					GLFVisitor += MyNumberUtils.getInteger(statisticVisitor.getNumberVisitorCompany11());
					GLFVisitor += MyNumberUtils.getInteger(statisticVisitor.getNumberVisitorCompany14());
					GLFVisitor += MyNumberUtils.getInteger(statisticVisitor.getNumberVisitorCompany17());
				}
			}
			totalHondaVisitor += hondaVisitor;
			totalGLFVisitor += GLFVisitor;
			
			apply += getNumQuotationApplyToday(dealer.getDealerType(), dealer, date);
			contract += getNumNewContractInMonth(dealer.getDealerType(), dealer, date);
			totalApply += apply;
			totalContract += contract;
			
			Row row = sheet.createRow(nRow++);
			int iCol = 0;
			createCell(row, iCol++, dealerID, 14, true, true, true, CellStyle.ALIGN_LEFT, false, BG_WHITE, FC_BLACK, false);
			createCell(row, iCol++, dealer.getDealerAddresses().get(0).getAddress().getProvince().getDescEn(), 12, true, true, 
					true, CellStyle.ALIGN_LEFT, false, BG_WHITE, FC_BLACK, false);
			createCell(row, iCol++, dealer.getNameEn(), 12, true, true, true, CellStyle.ALIGN_LEFT, false, BG_WHITE, FC_BLACK, false);
			createCell(row, iCol++, hondaVisitor, 14, true, true, true, CellStyle.ALIGN_RIGHT, true, BG_WHITE, FC_BLACK, false);
			createCell(row, iCol++, GLFVisitor, 14, true, true, true, CellStyle.ALIGN_RIGHT, true, BG_WHITE, FC_BLACK, false);
			createCell(row, iCol++, apply, 14, true, true, true, CellStyle.ALIGN_RIGHT, true, BG_WHITE, FC_BLACK, false);
			createCell(row, iCol++, contract, 14, true, true, true, CellStyle.ALIGN_RIGHT, true, BG_WHITE, FC_BLACK, false);
			createCell(row, iCol++, calculatePercentage(GLFVisitor, hondaVisitor), 14, true, true, true, CellStyle.ALIGN_RIGHT, false, BG_WHITE, FC_BLACK, false);
			createCell(row, iCol++, calculatePercentage(apply, GLFVisitor), 14, true, true, true, CellStyle.ALIGN_RIGHT, false, BG_WHITE, FC_BLACK, false);
			createCell(row, iCol++, calculatePercentage(contract, GLFVisitor), 14, true, true, true, CellStyle.ALIGN_RIGHT, false, BG_WHITE, FC_BLACK, false);
			createCell(row, iCol++, calculatePercentage(contract, apply), 14, true, true, true, CellStyle.ALIGN_RIGHT, false, BG_WHITE, FC_BLACK, false);
		}
		// Current month
		Row row = sheet.createRow(iRow + 1);
		int iCol = 0;
		createCell(row, iCol++, "ID", 12, true, true, true, CellStyle.ALIGN_LEFT, true, BG_CYAN, FC_BLACK, false);
		createCell(row, iCol++, "Province", 12, true, true, true, CellStyle.ALIGN_LEFT, true, BG_CYAN, FC_BLACK, false);
		createCell(row, iCol++, "Total (Current Month)", 12, true, true, true, CellStyle.ALIGN_LEFT, true, BG_CYAN, FC_BLACK, false);
		createCell(row, iCol++, totalHondaVisitor, 14, true, true, true, CellStyle.ALIGN_RIGHT, true, BG_CYAN, FC_BLACK, false);
		createCell(row, iCol++, totalGLFVisitor, 14, true, true, true, CellStyle.ALIGN_RIGHT, true, BG_CYAN, FC_BLACK, false);
		createCell(row, iCol++, totalApply, 14, true, true, true, CellStyle.ALIGN_RIGHT, true, BG_CYAN, FC_BLACK, false);
		createCell(row, iCol++, totalContract, 14, true, true, true, CellStyle.ALIGN_RIGHT, true, BG_CYAN, FC_BLACK, false);
		createCell(row, iCol++, calculatePercentage(totalGLFVisitor, totalHondaVisitor), 14, true, true, true, CellStyle.ALIGN_RIGHT, true, BG_CYAN, FC_BLACK, false);
		createCell(row, iCol++, calculatePercentage(totalApply, totalGLFVisitor), 14, true, true, true, CellStyle.ALIGN_RIGHT, true, BG_CYAN, FC_BLACK, false);
		createCell(row, iCol++, calculatePercentage(totalContract, totalGLFVisitor), 14, true, true, true, CellStyle.ALIGN_RIGHT, true, BG_CYAN, FC_BLACK, false);
		createCell(row, iCol++, calculatePercentage(totalContract, totalApply), 14, true, true, true, CellStyle.ALIGN_RIGHT, true, BG_CYAN, FC_BLACK, false);
		
		// previous month
		totalHondaVisitor = 0;
		totalGLFVisitor = 0;
		totalApply = 0;
		totalContract = 0;
		date = DateUtils.getDateAtEndOfDay(DateUtils.addMonthsDate(date, -1));
		for (Dealer dealer2 : dealers) {
			List<Statistic3HoursVisitor> statistic3HoursVisitors = getStatistic3HoursVisitor(dealer2.getDealerType(), dealer2, date);
			
			if (statistic3HoursVisitors != null && !statistic3HoursVisitors.isEmpty()) {
				for (Statistic3HoursVisitor statisticVisitor : statistic3HoursVisitors) {
					totalHondaVisitor += MyNumberUtils.getInteger(statisticVisitor.getNumberVisitorDealer11());
					totalHondaVisitor += MyNumberUtils.getInteger(statisticVisitor.getNumberVisitorDealer14());
					totalHondaVisitor += MyNumberUtils.getInteger(statisticVisitor.getNumberVisitorDealer17());
					totalGLFVisitor += MyNumberUtils.getInteger(statisticVisitor.getNumberVisitorCompany11());
					totalGLFVisitor += MyNumberUtils.getInteger(statisticVisitor.getNumberVisitorCompany14());
					totalGLFVisitor += MyNumberUtils.getInteger(statisticVisitor.getNumberVisitorCompany17());
				}
			}
			totalApply += getNumQuotationApplyToday(dealer2.getDealerType(), dealer2, date);
			totalContract += getNumNewContractInMonth(dealer2.getDealerType(), dealer2, date);
		}
		
		row = sheet.createRow(iRow);
		sheet.addMergedRegion(new CellRangeAddress(iRow, iRow, 0, 2));
		iCol = 0;
		createCell(row, iCol++, "Previous Month", 12, true, true, true, CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		createCell(row, iCol++, "", 12, true, true, true, CellStyle.ALIGN_LEFT, true, BG_GREY, FC_BLACK, false);
		createCell(row, iCol++, "", 12, true, true, true, CellStyle.ALIGN_LEFT, true, BG_GREY, FC_BLACK, false);
		createCell(row, iCol++, totalHondaVisitor, 14, true, true, true, CellStyle.ALIGN_RIGHT, true, BG_GREY, FC_BLACK, false);
		createCell(row, iCol++, totalGLFVisitor, 14, true, true, true, CellStyle.ALIGN_RIGHT, true, BG_GREY, FC_BLACK, false);
		createCell(row, iCol++, totalApply, 14, true, true, true, CellStyle.ALIGN_RIGHT, true, BG_GREY, FC_BLACK, false);
		createCell(row, iCol++, totalContract, 14, true, true, true, CellStyle.ALIGN_RIGHT, true, BG_GREY, FC_BLACK, false);
		createCell(row, iCol++, calculatePercentage(totalGLFVisitor, totalHondaVisitor), 14, true, true, true, CellStyle.ALIGN_RIGHT, true, BG_GREY, FC_BLACK, false);
		createCell(row, iCol++, calculatePercentage(totalApply, totalGLFVisitor), 14, true, true, true, CellStyle.ALIGN_RIGHT, true, BG_GREY, FC_BLACK, false);
		createCell(row, iCol++, calculatePercentage(totalContract, totalGLFVisitor), 14, true, true, true, CellStyle.ALIGN_RIGHT, true, BG_GREY, FC_BLACK, false);
		createCell(row, iCol++, calculatePercentage(totalContract, totalApply), 14, true, true, true, CellStyle.ALIGN_RIGHT, true, BG_GREY, FC_BLACK, false);
	}
	
	/**
	 * Get the number of apply
	 * @param dealerType
	 * @param dealer
	 * @param date
	 * @return
	 */
	private int getNumQuotationApplyToday(EDealerType dealerType, Dealer dealer, Date date) {
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<Quotation>(Quotation.class);
		restrictions.addCriterion(Restrictions.ge("firstSubmissionDate", DateFilterUtil.getStartDate(DateUtils.getDateAtBeginningOfMonth(date))));
		restrictions.addCriterion(Restrictions.le("firstSubmissionDate", DateFilterUtil.getEndDate(date)));
		restrictions.addAssociation("dealer", "dea", JoinType.INNER_JOIN);
		if (dealerType != null) {
			restrictions.addCriterion(Restrictions.eq("dea.dealerType", dealerType));
		}
		if (dealer != null) {
			restrictions.addCriterion(Restrictions.eq("dea." + ID, dealer.getId()));
		}
		return (int) quotationService.count(restrictions);
	}
	
	/**
	 * Get a list of Statistic3HoursVisitor
	 * @param dealerType
	 * @param dealer
	 * @param date
	 * @return
	 */
	private List<Statistic3HoursVisitor> getStatistic3HoursVisitor(EDealerType dealerType, Dealer dealer, Date date) {
		BaseRestrictions<Statistic3HoursVisitor> restrictions = new BaseRestrictions<>(Statistic3HoursVisitor.class);
		restrictions.addCriterion(Restrictions.ge("date", DateFilterUtil.getStartDate(DateUtils.getDateAtBeginningOfMonth(date))));
		restrictions.addCriterion(Restrictions.le("date", DateFilterUtil.getEndDate(date)));
		restrictions.addAssociation("dealer", "dea", JoinType.INNER_JOIN);
		if (dealerType != null) {
			restrictions.addCriterion(Restrictions.eq("dea.dealerType", dealerType));
		}
		if (dealer != null) {
			restrictions.addCriterion(Restrictions.eq("dea." + ID, dealer.getId()));
		}
		return quotationService.list(restrictions);
	}
	
	/**
	 * Get the number of contract
	 * @param dealerType
	 * @param dealer
	 * @param date
	 * @return
	 */
	private long getNumNewContractInMonth(EDealerType dealerType, Dealer dealer, Date date) {
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(Quotation.class);
		restrictions.addCriterion(Restrictions.eq(WKF_STATUS, QuotationWkfStatus.ACT));
		restrictions.addCriterion(Restrictions.ge("activationDate", DateFilterUtil.getStartDate(DateUtils.getDateAtBeginningOfMonth(date))));
		restrictions.addCriterion(Restrictions.le("activationDate", DateFilterUtil.getEndDate(date)));
		restrictions.addAssociation("dealer", "dea", JoinType.INNER_JOIN);
		if (dealerType != null) {
			restrictions.addCriterion(Restrictions.eq("dea.dealerType", dealerType));
		}
		if (dealer != null) {
			restrictions.addCriterion(Restrictions.eq("dea." + ID, dealer.getId()));
		}
		return quotationService.count(restrictions);
	}
	
	/**
	 * Calculate the percentage
	 * @param value1
	 * @param value2
	 * @return
	 */
	private double calculatePercentage(int value1, int value2) {
		if (value2 == 0) {
			return 0d;
		}
		return ((double) value1 / value2);
	}
	
	/**
	 * Set up the work book
	 * @return
	 */
	private XSSFSheet setUpWorkBook () {
		
		XSSFSheet sheet = wb.createSheet();
		sheet.lockDeleteColumns();
		sheet.lockDeleteRows();
		sheet.lockFormatCells();
		sheet.lockFormatColumns();
		sheet.lockFormatRows();
		sheet.lockInsertColumns();
		sheet.lockInsertRows();
		CellStyle style = wb.createCellStyle();
		sheet.setColumnWidth(0, 2000);
		sheet.setColumnWidth(1, 5500);
		sheet.setColumnWidth(2, 8000);
		sheet.setColumnWidth(3, 3000);
		sheet.setColumnWidth(4, 3000);
		sheet.setColumnWidth(5, 2500);
		sheet.setColumnWidth(6, 3200);
		sheet.setColumnWidth(7, 4300);
		sheet.setColumnWidth(8, 4000);
		sheet.setColumnWidth(9, 4500);
		sheet.setColumnWidth(10, 4500);
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
		
		return sheet;
	}
	
	/**
	 * Create cell
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
			final boolean hasBorder, final boolean leftRight, final short alignment,
			final boolean setBgColor, final short bgColor, final short fonCorlor, boolean wrapText) {
		
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
		
		if (value == null) {
			cell.setCellValue("");
		} else if (value instanceof Integer) {
			style.setDataFormat(format.getFormat("0"));
			cell.setCellValue(Integer.valueOf(value.toString()));
			cell.setCellStyle(style);
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		} else if (value instanceof Long) {
			style.setDataFormat(format.getFormat("0"));
			cell.setCellValue(Long.valueOf(value.toString()));
			cell.setCellStyle(style);
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		} else if(value instanceof Double){
			style.setDataFormat(format.getFormat("##0.00%"));
			cell.setCellValue((Double) value);
			cell.setCellStyle(style);
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		} else if (value instanceof String) {
			cell.setCellValue(value.toString());
		} 
		
		cell.setCellStyle(style);
		return cell;
	}
}
