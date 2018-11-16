package com.nokor.efinance.gui.report.xls;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.entity.MainEntity;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.efinance.glf.statistic.model.StatisticVisitor;
import com.nokor.efinance.tools.report.XLSAbstractReportExtractor;
import com.nokor.ersys.core.hr.model.eref.EMediaPromoting;
import com.nokor.frmk.security.context.SecApplicationContextHolder;

/**
 * Generate the report to survey the number of Way of Knowing for Visitors and Visitors Apply in each month for dealers
 * @author sok.vina
 * @author sotheara.leang (Modified)
 *   
 */
public class GLFTBrandKnowledge extends XLSAbstractReportExtractor {

	protected EntityService entityService = (EntityService) SecApplicationContextHolder.getContext().getBean("entityService");

	private static String MM_yyyyy = "MM/yyyy";
	
	private static String STYLE_DATE = "STYLE_DATE";
	private static String STYLE_NUMERIC = "STYLE_NUMBERIC";
	private static String STYLE_TITLE = "STYLE_TITLE";
	private static String STYLE_TEXT = "STYLE_SIMPLE_TEXT";
	private static String STYLE_HEADER = "STYLE_HEADER";
	private static String STYLE_CENTER = "STYLE_CENTER";
	private static String STYLE_RIGHT = "STYLE_RIGHT";
	private static String STYLE_PERCENTAGE = "STYLE_PERCENTAGE";
	private static String STYLE_FOOTER = "STYLE_FOOTER";
	
	private Map<String, CellStyle> styles = null;
	
	private Date startDate;
	private Date endDate;
	private Dealer dealer;
	private EDealerType dealerType;
	
	private List<EMediaPromoting> wayOfKnowings;
	private int iRow;

	@Override
	public String generate(ReportParameter reportParameter) throws Exception {	
		Map<String, Object> parameters = reportParameter.getParameters();
		startDate = (Date) parameters.get("startDate");
		endDate = (Date) parameters.get("endDate");
		dealer = (Dealer) parameters.get("dealer");
		dealerType = (EDealerType) parameters.get("dealer.type");
		
		wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet();
		
		initStyle();
		initSheet(sheet);
		
		// Initialize the columns size
		int colIndex = 0;
		wayOfKnowings = EMediaPromoting.values();
		for (int i=0; i< 2 * wayOfKnowings.size() + 8; i++) {
			if (i == 0 || i == wayOfKnowings.size() + 4) {
				sheet.setColumnWidth(colIndex++, 6000);
			} else {
				sheet.setColumnWidth(colIndex++, 5000);
			}
		}
		
		// Generate the Brand Knowledge Tables
		iRow = 1;
		createBrandKnowledgeTable(sheet, iRow, "Applications per dealer value", false, true);
		iRow += 3;
		createBrandKnowledgeTable(sheet, iRow, "Applications per dealer percent", false, false);
		iRow += 3;
		createBrandKnowledgeTable(sheet, iRow, "GLF Visitor per dealer value", true, true);
		iRow += 3;
		createBrandKnowledgeTable(sheet, iRow, "GLF Visitor per dealer percent", true, false);
		
		createBrandKnowledgeVisiterByDealerTables(sheet, iRow);
		
		String fileName = writeXLSData("Brand_Knowledge" + DateUtils.getDateLabel(DateUtils.today(), "yyyyMMddHHmmssSSS") + ".xlsx");
		return fileName;
	}
	
	/**
	 * Generate the title of Brand Knowledge table
	 * @param sheet
	 * @param iRow
	 * @param title
	 */
	private void createBrandKnowledgeTableTitle(XSSFSheet sheet, int iRow, String title) {
		Row titleRow = sheet.createRow(iRow);
		sheet.addMergedRegion(new CellRangeAddress(iRow, iRow, 0, 1));
		Cell cell = titleRow.createCell(0);
		cell.setCellStyle(styles.get(STYLE_TITLE));
		cell.setCellValue(title);
	}
	
	/**
	 * Generate the header of Brand Knowledge table
	 * @param sheet
	 * @param iRow
	 */
	private void createBrandKnowledgeTableHeader(XSSFSheet sheet, int iRow) {
		int iCol = 1;
		Row headerRow = sheet.createRow(iRow);
		Cell cell = headerRow.createCell(0);
		cell.setCellStyle(styles.get(STYLE_HEADER));
		cell.setCellValue("Month");
		
		for (EMediaPromoting way : wayOfKnowings) {
			cell = headerRow.createCell(iCol++);
			cell.setCellStyle(styles.get(STYLE_HEADER));
			cell.setCellValue(way.getDescEn());
		}
		
		cell = headerRow.createCell(wayOfKnowings.size() + 1);
		cell.setCellStyle(styles.get(STYLE_HEADER));
		cell.setCellValue("Total");
	}
	
	/**
	 * @param date
	 * @return List of Statistic Visitor
	 */
	private List<StatisticVisitor> getStatisticVisitors(Date date, Dealer dealer) {
		BaseRestrictions<StatisticVisitor> restrictions = new BaseRestrictions<>(StatisticVisitor.class);
		if (date != endDate) {
			restrictions.addCriterion(Restrictions.between(MainEntity.CREATE_DATE_PROPERTY, DateUtils.getDateAtBeginningOfDay(date), DateUtils.getDateAtEndOfMonth(date)));
		} else {
			restrictions.addCriterion(Restrictions.between(MainEntity.CREATE_DATE_PROPERTY, DateUtils.getDateAtBeginningOfMonth(date), DateUtils.getDateAtEndOfDay(date)));
		}
		restrictions.addCriterion(Restrictions.isNotNull("wayOfKnowing"));
		restrictions.addAssociation("dealer", "dea", JoinType.INNER_JOIN);
		
		if (dealer != null) {
			restrictions.addCriterion(Restrictions.eq("dealer.id", dealer.getId()));
		}
		if (dealerType != null) {
			restrictions.addCriterion(Restrictions.eq("dea.dealerType", dealerType));
		}

		return entityService.list(restrictions);
	}
	
	/**
	 * Get the Statistic Visitor for dealer in month of the given date
	 * @param date
	 * @return
	 */
	private HashMap<Long, Long> getStatisticVisitorsInMonth(Date date, Dealer dealer) {
		Long wayOfKnowing = null;
		HashMap<Long, Long> statistic = new HashMap<Long, Long>();
		
		for (EMediaPromoting way : wayOfKnowings) {
			statistic.put(way.getId(), Long.valueOf(0));
		}
		
		List<StatisticVisitor> list = getStatisticVisitors(date, dealer);
		for (StatisticVisitor st : list) {
			wayOfKnowing = statistic.get(st.getWayOfKnowing().getId());
			statistic.put(st.getWayOfKnowing().getId(), wayOfKnowing + 1);
		}
		return statistic;
	}
	
	/**
	 * Get the Quotations of dealer with the given date
	 * @param date
	 * @return
	 */
	private List<Quotation> getQuoations(Date date, Dealer dealer) {
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(Quotation.class);
		if (date != endDate) {
			restrictions.addCriterion(Restrictions.between(MainEntity.CREATE_DATE_PROPERTY, DateUtils.getDateAtBeginningOfDay(date), DateUtils.getDateAtEndOfMonth(date)));
		} else {
			restrictions.addCriterion(Restrictions.between(MainEntity.CREATE_DATE_PROPERTY, DateUtils.getDateAtBeginningOfMonth(date), DateUtils.getDateAtEndOfDay(date)));
		}
		restrictions.addCriterion(Restrictions.isNotNull("wayOfKnowing"));
		restrictions.addAssociation("dealer", "dea", JoinType.INNER_JOIN);
		
		if (dealer != null) {
			restrictions.addCriterion(Restrictions.eq("dealer.id", dealer.getId()));
		}
		if (dealerType != null) {
			restrictions.addCriterion(Restrictions.eq("dea.dealerType", dealerType));
		}
		
		return entityService.list(restrictions);
	}
	
	/**
	 * Get the Statistic Visitor Apply for dealer in month of the given date
	 * @param date
	 * @return
	 */
	private HashMap<Long, Long> getStatisticVisitorsApplyInMonth(Date date, Dealer dealer) {
		Long wayOfKnowing = null;
		HashMap<Long, Long> statistic = new HashMap<Long, Long>();
		
		for (EMediaPromoting way : wayOfKnowings) {
			statistic.put(way.getId(), Long.valueOf(0));
		}
		
		List<Quotation> list = getQuoations(date, dealer);
		for (Quotation quotation : list) {
			wayOfKnowing = statistic.get(quotation.getWayOfKnowing().getId());
			statistic.put(quotation.getWayOfKnowing().getId(), wayOfKnowing + 1);
		}
		return statistic;
	}
	
	/**
	 * Sum the total of Way of Knowing
	 * @param statistic
	 * @return total number of Way of Knowing
	 */
	private long getTotalWayOfKnowings(HashMap<Long, Long> statistic) {
		long number = 0;
		for (EMediaPromoting way : wayOfKnowings) {
			number += statistic.get(way.getId());
		}
		return number;
	}
	
	/**
	 * Generate the table of Visitor or Visitor Apply
	 * @param sheet
	 * @param rowIndex
	 * @param title
	 * @param visitor  : true -> visitor, false -> visitor apply
	 * @param value    : true -> display in value, false -> display in percentage
	 */
	
	private void createBrandKnowledgeTable(XSSFSheet sheet, int rowIndex, String title, boolean visitor, boolean value) {
		int columnIndex;
		int nbMonth = DateUtils.getNumberMonthOfTwoDates(endDate, startDate);
		long[] totals = new long[wayOfKnowings.size() + 1];
		
		createBrandKnowledgeTableTitle(sheet, rowIndex++, title);
		createBrandKnowledgeTableHeader(sheet, rowIndex++);
		
		for (int i=0; i<nbMonth; i++) {
			columnIndex = 1;
			
			Date month;
			if (i == 0) {
				month = startDate;
			} else if (i < nbMonth - 1) {
				month = DateUtils.getDateAtBeginningOfMonth(DateUtils.addMonthsDate(startDate, i));
			} else {
				month = endDate;
			}
			
			HashMap<Long, Long> statistic;
			Long totalWayOfKnowings;
			if (visitor) {
				statistic = getStatisticVisitorsInMonth(month, dealer);
				totalWayOfKnowings = getTotalWayOfKnowings(statistic);
			} else {
				statistic = getStatisticVisitorsApplyInMonth(month, dealer);
				totalWayOfKnowings = getTotalWayOfKnowings(statistic);
			}
			
			// Month column
			Row row = sheet.createRow(rowIndex++);
			Cell cell = row.createCell(0);
			cell.setCellStyle(styles.get(STYLE_DATE));
			cell.setCellValue(month);
			
			// Way of Knowing columns
			for (int j=0; j<wayOfKnowings.size(); j++) {
				EMediaPromoting way = wayOfKnowings.get(j);
				cell = row.createCell(columnIndex++);
				totals[j] += statistic.get(way.getId());
				
				if (value) {
					cell.setCellStyle(styles.get(STYLE_NUMERIC));
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue(statistic.get(way.getId()));
				} else {
					cell.setCellStyle(styles.get(STYLE_PERCENTAGE));
					cell.setCellValue(totalWayOfKnowings == 0 ? 0 : statistic.get(way.getId()) / (double) totalWayOfKnowings);
				}
			}
			
			cell = row.createCell(wayOfKnowings.size() + 1);
			totals[wayOfKnowings.size()] += totalWayOfKnowings;
			
			// Total column
			if (value) {
				cell.setCellStyle(styles.get(STYLE_NUMERIC));
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(totalWayOfKnowings);
			} else {
				cell.setCellStyle(styles.get(STYLE_PERCENTAGE));
				cell.setCellValue(totalWayOfKnowings == 0 ? 0 : 1);
			}
		}
		
		// Total row
		columnIndex = 1;
		Row row = sheet.createRow(rowIndex++);
		Cell cell = row.createCell(0);
		cell.setCellStyle(styles.get(STYLE_FOOTER));
		cell.setCellValue("Total");
		
		for (int i=0; i<wayOfKnowings.size(); i++) {
			cell = row.createCell(columnIndex++);
			if (value) {
				cell.setCellStyle(styles.get(STYLE_NUMERIC));
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(totals[i]);
			} else {
				cell.setCellStyle(styles.get(STYLE_PERCENTAGE));
				cell.setCellValue(totals[i] == 0 ? 0 : totals[i] / (double) totals[wayOfKnowings.size()]);
			}
		}
		
		cell = row.createCell(columnIndex);
		if (value) {
			cell.setCellStyle(styles.get(STYLE_NUMERIC));
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellValue(totals[wayOfKnowings.size()]);
		} else {
			cell.setCellStyle(styles.get(STYLE_PERCENTAGE));
			cell.setCellValue(totals[wayOfKnowings.size()] == 0 ? 0 : 1);
		}
		
		iRow = rowIndex;
	}
	
	/**
	 * Get the dealers 
	 * @return
	 */
	private List<Dealer> getDealers() {
		BaseRestrictions<Dealer> restrictions = new BaseRestrictions<>(Dealer.class);
		restrictions.addOrder(Order.asc("nameEn"));
		
		if (dealer != null) {
			restrictions.addCriterion(Restrictions.eq("id", dealer.getId()));
		}
		if (dealerType != null) {
			restrictions.addCriterion(Restrictions.eq("dealerType", dealerType));
		}
		return entityService.list(restrictions);
	}
	
	/**
	 * Generate the title of Brand Knowledge table by dealer
	 * @param sheet
	 * @param iRow
	 * @param iCol
	 * @param title
	 * @param month
	 */
	private void createBrandKnowledgeByDealerTableTitle(XSSFSheet sheet, int iRow, int iCol, String title, Date month) {
		Row titleRow = sheet.getRow(iRow);
		if (titleRow == null) {
			titleRow = sheet.createRow(iRow);
		}
		sheet.addMergedRegion(new CellRangeAddress(iRow, iRow, iCol, iCol + 1));
		Cell cell = titleRow.createCell(iCol);
		cell.setCellStyle(styles.get(STYLE_TITLE));
		cell.setCellValue(title);
		
		iCol += 2;
		
		sheet.addMergedRegion(new CellRangeAddress(iRow, iRow, iCol, iCol + wayOfKnowings.size()));
		cell = titleRow.createCell(iCol);
		cell.setCellStyle(styles.get(STYLE_TEXT));
		cell.setCellValue("From " + DateUtils.getDateLabel(month, "dd/MM/yyyy") + " to " + DateUtils.getDateLabel(DateUtils.getDateAtEndOfMonth(month), "dd/MM/yyyy"));
	}
	
	/**
	 * Generate the header of Brand Knowledge table by dealer
	 * @param sheet
	 * @param iRow
	 * @param iCol
	 */
	private void createBrandKnowledgeByDealerTableHeader(XSSFSheet sheet, int iRow, int iCol) {
		Row headerRow = sheet.getRow(iRow);
		if (headerRow == null) {
			headerRow = sheet.createRow(iRow);
		}
		Cell cell = headerRow.createCell(iCol++);
		cell.setCellStyle(styles.get(STYLE_HEADER));
		cell.setCellValue("Province");
		
		cell = headerRow.createCell(iCol++);
		cell.setCellStyle(styles.get(STYLE_HEADER));
		cell.setCellValue("Dearler");
		
		for (EMediaPromoting way : wayOfKnowings) {
			cell = headerRow.createCell(iCol++);
			cell.setCellStyle(styles.get(STYLE_HEADER));
			cell.setCellValue(way.getDescEn());
		}
		
		cell = headerRow.createCell(iCol);
		cell.setCellStyle(styles.get(STYLE_HEADER));
		cell.setCellValue("Total");
	}
	
	/**
	 * Generate the Brand Knowledge Visitor tables by dealer (Value and Percentage)
	 * @param sheet
	 * @param rowIndex
	 * @param colIndex
	 * @param title
	 * @param month
	 * @param visitor : true -> visitor, false -> visitor apply
	 */
	private void createBrandKnowledgeTableByDealer(XSSFSheet sheet, int rowIndex, int colIndex, String title, Date month, boolean visitor) {
		createBrandKnowledgeByDealerTableTitle(sheet, rowIndex, colIndex, title, month);
		createBrandKnowledgeByDealerTableTitle(sheet, rowIndex++, colIndex + wayOfKnowings.size() + 4, title, month);
		createBrandKnowledgeByDealerTableHeader(sheet, rowIndex, colIndex);
		createBrandKnowledgeByDealerTableHeader(sheet, rowIndex++, colIndex + wayOfKnowings.size() + 4);
		
		int iColValue;
		int iColPercentage;
		long[] totals = new long[wayOfKnowings.size() + 1];
		List<Dealer> dealers = getDealers();
		for (Dealer dealer : dealers) {
			iColValue = colIndex;
			iColPercentage = colIndex + wayOfKnowings.size() + 4;
			
			Row row = sheet.createRow(rowIndex++);
			
			// Value table
			Cell cell = row.createCell(iColValue++);
			cell.setCellStyle(styles.get(STYLE_RIGHT));
			try {
				cell.setCellValue(dealer.getMainAddress().getProvince().getDescEn());
			} catch (Exception e) {}
			
			cell = row.createCell(iColValue++);
			cell.setCellStyle(styles.get(STYLE_RIGHT));
			cell.setCellValue(dealer.getNameEn());
			
			// Percentage table
			cell = row.createCell(iColPercentage++);
			cell.setCellStyle(styles.get(STYLE_RIGHT));
			try {
				cell.setCellValue(dealer.getMainAddress().getProvince().getDescEn());
			} catch (Exception e) {}
			
			cell = row.createCell(iColPercentage++);
			cell.setCellStyle(styles.get(STYLE_RIGHT));
			cell.setCellValue(dealer.getNameEn());
			
			HashMap<Long, Long> statistic;
			if (visitor) {
				statistic = getStatisticVisitorsInMonth(month ,dealer);
			} else {
				statistic = getStatisticVisitorsApplyInMonth(month, dealer);
			}
			Long totalWayOfKnowings = getTotalWayOfKnowings(statistic);
			
			for (int i=0; i<wayOfKnowings.size(); i++) {
				EMediaPromoting way = wayOfKnowings.get(i);
				totals[i] += statistic.get(way.getId());
				
				// Value Table
				cell = row.createCell(iColValue++);
				cell.setCellStyle(styles.get(STYLE_NUMERIC));
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(statistic.get(way.getId()));
				
				// Percentage table
				cell = row.createCell(iColPercentage++);
				cell.setCellStyle(styles.get(STYLE_PERCENTAGE));
				cell.setCellValue(totalWayOfKnowings == 0 ? 0 : statistic.get(way.getId()) / (double) totalWayOfKnowings);
			}
			
			totals[wayOfKnowings.size()] += totalWayOfKnowings;
			
			// Total column - Value table
			cell = row.createCell(iColValue);
			cell.setCellStyle(styles.get(STYLE_NUMERIC));
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellValue(totalWayOfKnowings);
			
			// Total column - Percentage table
			cell = row.createCell(iColPercentage);
			cell.setCellStyle(styles.get(STYLE_NUMERIC));
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellValue(totalWayOfKnowings);
		}
		
		// Total row 
		iColValue = colIndex;
		iColPercentage = colIndex + wayOfKnowings.size() + 4;;
		Row row = sheet.createRow(rowIndex++);
		
		// Value table
		Cell cell = row.createCell(iColValue++);
		cell.setCellStyle(styles.get(STYLE_FOOTER));
		cell.setCellValue("Total");
		
		cell = row.createCell(iColValue++);
		cell.setCellStyle(styles.get(STYLE_FOOTER));
		cell.setCellValue("All POS");
		
		// Percentage table
		cell = row.createCell(iColPercentage++);
		cell.setCellStyle(styles.get(STYLE_FOOTER));
		cell.setCellValue("Total");
		
		cell = row.createCell(iColPercentage++);
		cell.setCellStyle(styles.get(STYLE_FOOTER));
		cell.setCellValue("All POS");
		
		// Way of Knowing fields
		for (int i=0; i<wayOfKnowings.size(); i++) {
			// Value table
			cell = row.createCell(iColValue++);
			cell.setCellStyle(styles.get(STYLE_NUMERIC));
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellValue(totals[i]);
			
			// Percentage table
			cell = row.createCell(iColPercentage++);
			cell.setCellStyle(styles.get(STYLE_PERCENTAGE));
			cell.setCellValue(totals[i] == 0 ? 0 : totals[i] / (double) totals[wayOfKnowings.size()]);
		}
		
		// Total column - Value table
		cell = row.createCell(iColValue);
		cell.setCellStyle(styles.get(STYLE_NUMERIC));
		cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		cell.setCellValue(totals[wayOfKnowings.size()]);
		
		// Total column - Percentage table
		cell = row.createCell(iColPercentage);
		cell.setCellStyle(styles.get(STYLE_NUMERIC));
		cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		cell.setCellValue(totals[wayOfKnowings.size()]);
		
		iRow = rowIndex;
	}
	
	/**
	 * Generate the tables of Brand Knowledge Visitor and Visitor Apply 
	 * @param sheet
	 * @param rowIndex
	 */
	private void createBrandKnowledgeVisiterByDealerTables(XSSFSheet sheet, int rowIndex) {
		int nbMonth = DateUtils.getNumberMonthOfTwoDates(endDate, startDate);
		for (int i=0; i<nbMonth; i++) {
			Date month;
			if (i == 0) {
				month = startDate;
			} else if (i < nbMonth - 1) {
				month = DateUtils.getDateAtBeginningOfMonth(DateUtils.addMonthsDate(startDate, i));
			} else {
				month = endDate;
			}
			createBrandKnowledgeTableByDealer(sheet, iRow += 3, 0, "GLF Visitor per dealer", month, true);
		}
		for (int i=0; i<nbMonth; i++) {
			Date month;
			if (i == 0) {
				month = startDate;
			} else if (i < nbMonth - 1) {
				month = DateUtils.getDateAtBeginningOfMonth(DateUtils.addMonthsDate(startDate, i));
			} else {
				month = endDate;
			}
			createBrandKnowledgeTableByDealer(sheet, iRow += 3, 0, "Applications per dealer", month, false);
		}
	}
	
	/**
	 * Initialize the sheet properties (margin, scale...)
	 * @param sheet
	 */
	private void initSheet(XSSFSheet sheet) {
		sheet.lockInsertColumns();
		sheet.lockInsertRows();
		sheet.lockDeleteColumns();
		sheet.lockDeleteRows();
		sheet.lockFormatCells();
		sheet.lockFormatColumns();
		sheet.lockFormatRows();
		
		PrintSetup printSetup = sheet.getPrintSetup();
		printSetup.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
		printSetup.setScale((short) 75);

		sheet.setMargin(Sheet.LeftMargin, 0.25);
		sheet.setMargin(Sheet.RightMargin, 0.25);
		sheet.setMargin(Sheet.TopMargin, 0.25);
		sheet.setMargin(Sheet.BottomMargin, 0.25);
		sheet.setMargin(Sheet.HeaderMargin, 0.25);
		sheet.setMargin(Sheet.FooterMargin, 0.25);
		
		sheet.setZoom(7, 10);
	} 
	
	/**
	 * Set the border style to the cellStyle
	 * @param cellStyle
	 */
	private void setBorder(CellStyle cellStyle) {
		cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyle.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle.setBorderTop(CellStyle.BORDER_THIN);
	}
	
	/**
	 * Initialize the styles for the report
	 */
	private void initStyle() {
		DataFormat dataFormat = wb.createDataFormat();
		styles = new HashMap<String, CellStyle>();
		
		// STYLE TITLE
		Font titleFont = wb.createFont();
		titleFont.setFontHeightInPoints((short) 14);
		titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);

		CellStyle titleStyle = wb.createCellStyle();
		titleStyle.setAlignment(CellStyle.ALIGN_LEFT);
		titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		titleStyle.setWrapText(true);
		titleStyle.setFont(titleFont);
		styles.put(STYLE_TITLE, titleStyle);
		
		// STYLE HEADER
		Font headerFont = wb.createFont();
		headerFont.setFontHeightInPoints((short) 12);
		
		CellStyle headerStyle = wb.createCellStyle();
		headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
		headerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		headerStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
		headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		headerStyle.setFont(headerFont);
		headerStyle.setWrapText(true);
		setBorder(headerStyle);
		styles.put(STYLE_HEADER, headerStyle);
		
		// STYLE CENTER
		Font font = wb.createFont();
		font.setFontHeightInPoints((short) 10);
		CellStyle centerStyle = wb.createCellStyle();
		
		centerStyle.setAlignment(CellStyle.ALIGN_CENTER);
		centerStyle.setFont(font);
		setBorder(centerStyle);
		styles.put(STYLE_CENTER, centerStyle);
		
		//	STYLE TEXT
		CellStyle text = wb.createCellStyle();
		text.setAlignment(CellStyle.ALIGN_CENTER);
		text.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		text.setFont(font);
		styles.put(STYLE_TEXT, text);
		
		// STYLE PERCENTAGE
		CellStyle stylePercentage = wb.createCellStyle();
		stylePercentage.setAlignment(CellStyle.ALIGN_CENTER);
		stylePercentage.setDataFormat(dataFormat.getFormat("##0.00%"));
		stylePercentage.setFont(font);
		setBorder(stylePercentage);
		styles.put(STYLE_PERCENTAGE, stylePercentage);
		
		// STYLE RIGHT
		CellStyle rightStyle = wb.createCellStyle();
		rightStyle.setAlignment(CellStyle.ALIGN_RIGHT);
		rightStyle.setFont(font);
		setBorder(rightStyle);
		styles.put(STYLE_RIGHT, rightStyle);
		
		// STYLE DATE
		CellStyle dateStyle = wb.createCellStyle();
		dateStyle.setAlignment(CellStyle.ALIGN_RIGHT);
		dateStyle.setDataFormat(dataFormat.getFormat(MM_yyyyy));
		dateStyle.setFont(font);
		setBorder(dateStyle);
		styles.put(STYLE_DATE, dateStyle);
		
		// STYLE NUMBERIC
		CellStyle numbericStyle = wb.createCellStyle();
		numbericStyle.setDataFormat(dataFormat.getFormat("0"));
		numbericStyle.setAlignment(CellStyle.ALIGN_CENTER);
		setBorder(numbericStyle);
		styles.put(STYLE_NUMERIC, numbericStyle);	
		
		// STYLE FOOTER
		CellStyle footerStyle = wb.createCellStyle();
		footerStyle.setDataFormat(dataFormat.getFormat("0"));
		footerStyle.setAlignment(CellStyle.ALIGN_RIGHT);
		footerStyle.setFont(headerFont);
		setBorder(footerStyle);
		styles.put(STYLE_FOOTER, footerStyle);
	}
}

