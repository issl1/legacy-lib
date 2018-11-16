package com.nokor.efinance.gui.report.xls;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.efinance.core.stock.model.EStockReason;
import com.nokor.efinance.core.stock.model.ProductStock;
import com.nokor.efinance.core.stock.model.ProductStockInventory;
import com.nokor.efinance.tools.report.Report;
import com.nokor.efinance.tools.report.XLSAbstractReportExtractor;
import com.nokor.frmk.security.context.SecApplicationContextHolder;

/**
 * @author uhout.cheng
 */
public class GLFLeafletReport extends XLSAbstractReportExtractor implements Report, GLFApplicantFields, QuotationEntityField {

	protected EntityService entityService = (EntityService) SecApplicationContextHolder.getContext().getBean("entityService");

	private Map<String, CellStyle> styles = null;
	
	public static String DEFAULT_DATE_FORMAT = "d/MM/yyyy";
	/** Background color format */
	static short BG_WHITE = IndexedColors.WHITE.getIndex();
	static short BG_BROWN = IndexedColors.BROWN.getIndex();

	/** Font color */
	static short FC_WHITE = IndexedColors.WHITE.getIndex();
	static short FC_BLACK = IndexedColors.BLACK.getIndex();
	
	private Long numberDay;
	private Date selectStartDate;
	private Date selectEndDate;
	private static String TSHIRT = "TSHIRT";
	private static String LEAFL = "LEAFL";
	
	/**
	 * default constructor
	 */
	public GLFLeafletReport() {

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
		styles = new HashMap<String, CellStyle>();
		createStyles();
		int iCol = 0;
		sheet.setColumnWidth(iCol++, 5200);
		sheet.setColumnWidth(iCol++, 10000);
		sheet.setColumnWidth(iCol++, 3700);
		sheet.setColumnWidth(iCol++, 3700);
		sheet.setColumnWidth(iCol++, 3700);
		sheet.setColumnWidth(iCol++, 4700);
		sheet.setColumnWidth(iCol++, 4300);
		sheet.setColumnWidth(iCol++, 4300);
		sheet.setColumnWidth(iCol++, 4300);
		sheet.setColumnWidth(iCol++, 4300);
		sheet.setColumnWidth(iCol++, 4300);
		sheet.setColumnWidth(iCol++, 1500);
		for (int i = 1; i <= numberDay; i++) {
			sheet.setColumnWidth(iCol++, 2500);
			sheet.setColumnWidth(iCol++, 2500);
			sheet.setColumnWidth(iCol++, 2500);
			sheet.setColumnWidth(iCol++, 2500);
		}
		sheet.setZoom(7, 10);
		final PrintSetup printSetup = sheet.getPrintSetup();

		printSetup.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
		printSetup.setLandscape(true);

		printSetup.setScale((short) 65);

		// Setup the Page margins - Left, Right, Top and Bottom
		sheet.setMargin(Sheet.LeftMargin, 0.25);
		sheet.setMargin(Sheet.RightMargin, 0.25);
		sheet.setMargin(Sheet.TopMargin, 0.25);
		sheet.setMargin(Sheet.BottomMargin, 0.25);
		sheet.setMargin(Sheet.HeaderMargin, 0.25);
		sheet.setMargin(Sheet.FooterMargin, 0.25);
		
		return sheet;
	}

	/**
	 * @see com.nokor.efinance.tools.report.Report#generate(com.nokor.efinance.core.shared.report.ReportParameter)
	 */
	@Override
	public String generate(ReportParameter reportParameter) throws Exception {	
		Map<String, Object> parameters = reportParameter.getParameters();
		selectStartDate = (Date) parameters.get("dateStartValue");
		selectEndDate = (Date) parameters.get("dateEndValue");	
		
		BaseRestrictions<Dealer> restrictions = new BaseRestrictions<>(Dealer.class);		
		restrictions.addAssociation("dealerAddresses", "deaAddress", JoinType.INNER_JOIN);
		restrictions.addAssociation("deaAddress.address", "address", JoinType.INNER_JOIN);
		restrictions.addAssociation("address.province", "province", JoinType.INNER_JOIN);
		restrictions.addOrder(Order.desc("province.descEn"));
		List<Dealer> dealers = entityService.list(restrictions);
		
		numberDay = DateUtils.getDiffInDaysPlusOneDay(selectEndDate, selectStartDate);
		
		createWorkbook(null);
		XSSFSheet sheet = setUpWorkBook();
		
		int iRow = 1;
		iRow = iRow + 1;
		if (selectStartDate == null) {
			selectStartDate = DateUtils.today();
		} else {
			selectStartDate = DateUtils.getDateAtBeginningOfDay(selectStartDate);
		}
		if (selectEndDate == null) {
			selectEndDate = DateUtils.today();
		} else {
			selectEndDate = DateUtils.getDateAtEndOfDay(selectEndDate);
		}
		iRow = dataTable(sheet, iRow, dealers);

		String fileName = writeXLSData("Leaflet_Report" + DateUtils.getDateLabel(DateUtils.today(), "yyyyMMddHHmmssSSS") + ".xlsx");

		return fileName;
	}

	/**
	 * 
	 * @param tmpRow
	 * @param iCol
	 * @param hasValue
	 * @return
	 */
	private int createEmptyRow11Column(Row tmpRow, int iCol, boolean hasValue) {
		for (int i = 0; i <= 10; i++) {
			if (i == 0 && hasValue) {
				createCell(tmpRow, iCol++, DateUtils.getDateLabel(selectStartDate, DEFAULT_DATE_FORMAT) + " to " + 
						DateUtils.getDateLabel(selectEndDate, DEFAULT_DATE_FORMAT), 14, false, true, CellStyle.ALIGN_CENTER, 
						false, BG_GREY, FC_BLACK, false);
			} else {
				createCell(tmpRow, iCol++, "", 12, false, true, CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
			}	
		}
		createCell(tmpRow, iCol++, "", 12, false, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, false);
		return iCol;
	}
	
	/**
	 * 
	 * @param tmpRow
	 * @param iCol
	 * @return
	 */
	private int createEmptyRow3Column(Row tmpRow, int iCol) {
		for (int index = 0; index <= 2; index++) {
			createCell(tmpRow, iCol++, "", 12, false, true, CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		}
		return iCol;
	}
	
	/**
	 * 
	 * @param sheet
	 * @param iRow
	 * @param dealers
	 * @return
	 * @throws Exception
	 */
	private int dataTable(final Sheet sheet, int iRow, List<Dealer> dealers) throws Exception {
		iRow = iRow - 1;
		Row tmpRow = sheet.createRow(iRow++);
		int iCol = 0;
		int i = 0;
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow, 0, 10));
		iCol = createEmptyRow11Column(tmpRow, iCol, true);
		
		for (i = 0; i < numberDay; i++) {
			createCell(tmpRow, iCol, DateUtils.getDateLabel(DateUtils.addDaysDate(selectStartDate, i), "d-MMM"), 
					14, false, true, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, false);
			sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow, iCol, iCol + 3));
			iCol++;
			iCol = createEmptyRow3Column(tmpRow, iCol);
		}
		
		tmpRow = sheet.createRow(iRow++);
		iCol = 0;
		iCol = createEmptyRow11Column(tmpRow, iCol, false);
		
		for (i = 0; i < numberDay; i++) {
			createCell(tmpRow, iCol++, "", 12, false, true, CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
			iCol = createEmptyRow3Column(tmpRow, iCol);
		}
		
		tmpRow = sheet.createRow(iRow++);
		iCol = 0;
		createCell(tmpRow, iCol++, "Province", 10, true, false, CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
		sheet.addMergedRegion(new CellRangeAddress(iRow-1, iRow+1, 0, 0));
		
		createCell(tmpRow, iCol++, "Dealer", 10, true, false, CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
		sheet.addMergedRegion(new CellRangeAddress(iRow-1, iRow+1, 1, 1));
		
		createCell(tmpRow, iCol++, "Stock of T-Shirt", 10, true, false, CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
		sheet.addMergedRegion(new CellRangeAddress(iRow-1, iRow+1, 2, 2));
		
		createCell(tmpRow, iCol++, "Stock of Old Leaflet", 10, true, false, CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
		sheet.addMergedRegion(new CellRangeAddress(iRow-1, iRow+1, 3, 3));
		
		createCell(tmpRow, iCol++, "Stock of New Leaflet", 10, true, false, CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
		sheet.addMergedRegion(new CellRangeAddress(iRow-1, iRow+1, 4, 4));
		
		createCell(tmpRow, iCol++, "Number of leaflet delivery during the month", 10, true, false, CellStyle.ALIGN_CENTER, 
				true, BG_GREY, FC_BLACK, true);
		sheet.addMergedRegion(new CellRangeAddress(iRow-1, iRow+1, 5, 5));
		
		createCell(tmpRow, iCol++, "Total leaflet distributed this month", 10, true, false, CellStyle.ALIGN_CENTER, true, 
				BG_GREY, FC_BLACK, true);
		sheet.addMergedRegion(new CellRangeAddress(iRow-1, iRow+1, 6, 6));
		
		createCell(tmpRow, iCol++, "Total distributed inside dealer", 10, true, false, CellStyle.ALIGN_CENTER, true, 
				BG_GREY, FC_BLACK, true);
		sheet.addMergedRegion(new CellRangeAddress(iRow-1, iRow+1, 7, 7));
		
		createCell(tmpRow, iCol++, "Total distributed in the street", 10, true, false, CellStyle.ALIGN_CENTER, true, 
				BG_GREY, FC_BLACK, true);
		sheet.addMergedRegion(new CellRangeAddress(iRow-1, iRow+1, 8, 8));
		
		createCell(tmpRow, iCol++, "Total distributed in field check", 10, true, false, CellStyle.ALIGN_CENTER, true, 
				BG_GREY, FC_BLACK, true);
		sheet.addMergedRegion(new CellRangeAddress(iRow-1, iRow+1, 9, 9));
		
		createCell(tmpRow, iCol++, "Total distributed in special event", 10, true, false, CellStyle.ALIGN_CENTER, true, 
				BG_GREY, FC_BLACK, true);
		sheet.addMergedRegion(new CellRangeAddress(iRow-1, iRow+1, 10, 10));
		
		createCell(tmpRow, iCol++, "", 10, false, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, false);
		sheet.addMergedRegion(new CellRangeAddress(iRow-1, iRow+1, 11, 11));
		
		for (i = 0; i < numberDay; i++) {
			createCell(tmpRow, iCol, "Inside dealer", 10, false, true, CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
			sheet.addMergedRegion(new CellRangeAddress(iRow-1, iRow+1, iCol, iCol));
			iCol++;
			createCell(tmpRow, iCol, "In the street", 10, false, true, CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
			sheet.addMergedRegion(new CellRangeAddress(iRow-1, iRow+1, iCol, iCol));
			iCol++;
			createCell(tmpRow, iCol, "In field check", 10, false, true, CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
			sheet.addMergedRegion(new CellRangeAddress(iRow-1, iRow+1, iCol, iCol));
			iCol++;
			createCell(tmpRow, iCol, "In special event", 10, false, true, CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
			sheet.addMergedRegion(new CellRangeAddress(iRow-1, iRow+1, iCol, iCol));
			iCol++;
		}
		
		tmpRow = sheet.createRow(iRow++);
		iCol = 0;
		iCol = createEmptyRow11Column(tmpRow, iCol, false);
		
		for (i = 0; i < numberDay; i++) {
			createCell(tmpRow, iCol++, "", 10, true, false, CellStyle.ALIGN_RIGHT, false, BG_GREY, FC_BLACK, true);
			iCol = createEmptyRow3Column(tmpRow, iCol);
		}
	
		tmpRow = sheet.createRow(iRow++);
		iCol = 0;
		iCol = createEmptyRow11Column(tmpRow, iCol, false);
		
		for (i = 0; i < numberDay; i++) {
			createCell(tmpRow, iCol++, "", 10, true, false, CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
			iCol = createEmptyRow3Column(tmpRow, iCol);
		}
		
		if (dealers != null && !dealers.isEmpty()) {
			int nbRows = dealers.size();
			
			for (int index = 0; index < nbRows; index++) {
				tmpRow = sheet.createRow(iRow++);
				Dealer dealer = dealers.get(index);
				
				int numStockOfTShirtPerMonth = 0;
				int numStockOfOldLeafletPerMonth = 0;
				int numStockOfNewLeafletPerMonth = 0;
				int numStockInsideDealerPerMonth = getProductStockInventoryPerMonth(
						dealer, selectStartDate, selectEndDate, EStockReason.INSIDE_DEALER);
				int numStockInTheStreetPerMonth = getProductStockInventoryPerMonth(
						dealer, selectStartDate, selectEndDate, EStockReason.IN_THE_STREET);
				int numStockInFieldCheckPerMonth = getProductStockInventoryPerMonth(
						dealer, selectStartDate, selectEndDate, EStockReason.IN_FIELD_CHECK);
				int numStockInSpecialEventPerMonth = getProductStockInventoryPerMonth(
						dealer, selectStartDate, selectEndDate, EStockReason.IN_SPECIAL_EVENT);
				int numTotal = numStockInsideDealerPerMonth
						+ numStockInTheStreetPerMonth
						+ numStockInFieldCheckPerMonth
						+ numStockInSpecialEventPerMonth;
				List<ProductStock> productStockOfTShirts = getStockOfTShirtPerMonth(dealer, selectStartDate, selectEndDate);
				if (productStockOfTShirts != null && !productStockOfTShirts.isEmpty()) {
					for (ProductStock productStockOfTShirt : productStockOfTShirts) {
						numStockOfTShirtPerMonth += productStockOfTShirt.getQty();
					}
				}
				
				List<ProductStock> productStockOfLeaflets = getStockOfLeafletPerMonth(dealer, selectStartDate, selectEndDate);
				if (productStockOfLeaflets != null && !productStockOfLeaflets.isEmpty()) {
					for (ProductStock productStockOfLeaflet : productStockOfLeaflets) {
						numStockOfOldLeafletPerMonth += productStockOfLeaflet.getInitialQty();
						numStockOfNewLeafletPerMonth += productStockOfLeaflet.getQty();
					}
				}
				
				String address = dealer.getDealerAddresses().get(0).getAddress().getProvince().getDescEn();
				iCol = 0;
				createCell(tmpRow, iCol++, getDefaultString(address), 10, true, false, CellStyle.ALIGN_RIGHT, false, 
						BG_GREY, FC_BLACK, true);
				createCell(tmpRow, iCol++, getDefaultString(dealer.getNameEn()), 10, true, false, CellStyle.ALIGN_RIGHT, false, 
						BG_GREY, FC_BLACK, true);
				createCell(tmpRow, iCol++, numStockOfTShirtPerMonth, 10, true, false, CellStyle.ALIGN_CENTER, false, 
						BG_GREY, FC_BLACK, false);
				createCell(tmpRow, iCol++, numStockOfOldLeafletPerMonth, 10, true, false, CellStyle.ALIGN_CENTER, false, 
						BG_GREY, FC_BLACK, false);
				createCell(tmpRow, iCol++, numStockOfNewLeafletPerMonth, 10, true, false, CellStyle.ALIGN_CENTER, false, 
						BG_GREY, FC_BLACK, false);
				createCell(tmpRow, iCol++, getProductStockInventoryPerMonth(dealer, selectStartDate, selectEndDate, 
						EStockReason.DELIVERY), 10, true, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, false);
				createCell(tmpRow, iCol++, numTotal, 10, true, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, false);
				createCell(tmpRow, iCol++, numStockInsideDealerPerMonth, 10, true, false, CellStyle.ALIGN_CENTER, false, 
						BG_GREY, FC_BLACK, false);
				createCell(tmpRow, iCol++, numStockInTheStreetPerMonth, 10, true, false, CellStyle.ALIGN_CENTER, false, 
						BG_GREY, FC_BLACK, false);
				createCell(tmpRow, iCol++, numStockInFieldCheckPerMonth, 10, true, false, CellStyle.ALIGN_CENTER, false, 
						BG_GREY, FC_BLACK, false);
				createCell(tmpRow, iCol++, numStockInSpecialEventPerMonth, 10, true, false, CellStyle.ALIGN_CENTER, false, 
						BG_GREY, FC_BLACK, false);
				createCell(tmpRow, iCol++, "", 10, false, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, false);
					
				for (i = 0; i < numberDay; i++) {
					Date startDate = DateUtils.addDaysDate(selectStartDate, i);
					createCell(tmpRow, iCol++, getProductStockInventoryPerDay(dealer, startDate, EStockReason.INSIDE_DEALER), 10, 
							true, false, CellStyle.ALIGN_RIGHT, false, BG_GREY, FC_BLACK, false);
					createCell(tmpRow, iCol++, getProductStockInventoryPerDay(dealer, startDate, EStockReason.IN_THE_STREET), 10, 
							true, false, CellStyle.ALIGN_RIGHT, false, BG_GREY, FC_BLACK, false);
					createCell(tmpRow, iCol++, getProductStockInventoryPerDay(dealer, startDate, EStockReason.IN_FIELD_CHECK), 10, 
							true, false, CellStyle.ALIGN_RIGHT, false, BG_GREY, FC_BLACK, false);
					createCell(tmpRow, iCol++, getProductStockInventoryPerDay(dealer, startDate, EStockReason.IN_SPECIAL_EVENT), 10, 
							true, false, CellStyle.ALIGN_RIGHT, false, BG_GREY, FC_BLACK, false);
				}
			}
		}
		return iRow;
	}
	
	/**
	 * stock of t-shirt per month
	 * @param dealer
	 * @param startDate
	 * @param endDate
	 * @return list
	 */
	private List<ProductStock> getStockOfTShirtPerMonth(Dealer dealer, Date startDate, Date endDate) {
		BaseRestrictions<ProductStock> restrictions = new BaseRestrictions<>(ProductStock.class);
		restrictions.addAssociation("product", "produ", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));
		restrictions.addCriterion(Restrictions.ge("updateDate", DateUtils.getDateAtBeginningOfDay(startDate)));
		restrictions.addCriterion(Restrictions.le("updateDate", DateUtils.getDateAtEndOfDay(endDate)));
		restrictions.addCriterion(Restrictions.eq("produ." + CODE , TSHIRT));
		return entityService.list(restrictions);
	}
	
	/**
	 * stock of leaflet per month
	 * @param dealer
	 * @param startDate
	 * @param endDate
	 * @return list
	 */
	private List<ProductStock> getStockOfLeafletPerMonth(Dealer dealer, Date startDate, Date endDate) {
		BaseRestrictions<ProductStock> restrictions = new BaseRestrictions<>(ProductStock.class);
		restrictions.addAssociation("product", "produ", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));
		restrictions.addCriterion(Restrictions.ge("updateDate", DateUtils.getDateAtBeginningOfDay(startDate)));
		restrictions.addCriterion(Restrictions.le("updateDate", DateUtils.getDateAtEndOfDay(endDate)));
		restrictions.addCriterion(Restrictions.eq("produ." + CODE , LEAFL));
		return entityService.list(restrictions);
	}
	
	/**
	 * stock inventory per month
	 * @param dealer
	 * @param startDate
	 * @param endDate
	 * @param stockReason
	 * @return int
	 */
	private int getProductStockInventoryPerMonth(Dealer dealer, Date startDate, Date endDate, EStockReason stockReason) {
		BaseRestrictions<ProductStockInventory> restrictions = new BaseRestrictions<>(ProductStockInventory.class);
		restrictions.addAssociation("product", "produ", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));
		restrictions.addCriterion(Restrictions.ge("date", DateUtils.getDateAtBeginningOfDay(startDate)));
		restrictions.addCriterion(Restrictions.le("date", DateUtils.getDateAtEndOfDay(endDate)));
		restrictions.addCriterion(Restrictions.eq("produ." + CODE , LEAFL));
		restrictions.addCriterion(Restrictions.eq("stockReason", stockReason));
		
		int total = 0;
		List<ProductStockInventory> productStockInventoryPerMonths = entityService.list(restrictions);
		if (productStockInventoryPerMonths != null && !productStockInventoryPerMonths.isEmpty()) {
			for (ProductStockInventory productStockInventory : productStockInventoryPerMonths) {
				total += productStockInventory.getQty();
			}
		}
		return Math.abs(total);
	}
	
	/**
	 * stock inventory per day
	 * @param dealer
	 * @param startDate
	 * @param stockReason
	 * @return int
	 */
	private int getProductStockInventoryPerDay(Dealer dealer, Date startDate, EStockReason stockReason) {
		return getProductStockInventoryPerMonth(dealer, startDate, startDate, stockReason);
	}
	
	/**
	 * 
	 * @param strValue
	 * @return string
	 */
	private String getDefaultString(String strValue) {
		if (strValue == null) {
			return "";
		} else {
			return strValue;
		}
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
			final Object value, final int fontsize,
			final boolean hasBorder, final boolean leftRight, final short alignment,
			final boolean setBgColor, final short bgColor, final short fonCorlor, boolean wrapText) {
		
		final Cell cell = row.createCell(iCol);
		final Font itemFont = wb.createFont();
		itemFont.setFontHeightInPoints((short) fontsize);
		itemFont.setFontName("Arial");

		final CellStyle style = wb.createCellStyle();
		
		DataFormat format = wb.createDataFormat();
		style.setDataFormat(format.getFormat("0"));
		
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
			cell.setCellValue(Integer.valueOf(value.toString()));
			cell.setCellStyle(style);
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			
		}else if(value instanceof Double){
			cell.setCellValue(Double.valueOf(value.toString()));
			cell.setCellStyle(style);
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		}else if(value instanceof Long){
			cell.setCellValue(Long.valueOf(value.toString()));
			cell.setCellStyle(style);
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		}else {
			cell.setCellValue((value == null ? "" : value.toString()));
		}
		cell.setCellStyle(style);
		return cell;
	}
	
	/**
	 * 
	 * @return
	 */
	private Map<String, CellStyle> createStyles() {
		return styles;
	}
	
}

