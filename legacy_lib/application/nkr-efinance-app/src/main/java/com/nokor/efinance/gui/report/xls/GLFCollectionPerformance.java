package com.nokor.efinance.gui.report.xls;

import java.awt.Paint;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DateFormatUtils;
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
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.hibernate.criterion.Restrictions;
import org.jCharts.axisChart.AxisChart;
import org.jCharts.axisChart.customRenderers.axisValue.renderers.ValueLabelPosition;
import org.jCharts.axisChart.customRenderers.axisValue.renderers.ValueLabelRenderer;
import org.jCharts.chartData.AxisChartDataSet;
import org.jCharts.chartData.DataSeries;
import org.jCharts.encoders.PNGEncoder;
import org.jCharts.properties.AxisProperties;
import org.jCharts.properties.ChartProperties;
import org.jCharts.properties.LegendProperties;
import org.jCharts.properties.StackedBarChartProperties;
import org.jCharts.types.ChartType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.payment.model.EPenaltyCalculMethod;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.shared.conf.AppConfig;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.efinance.tools.report.Report;
import com.nokor.efinance.tools.report.XLSAbstractReportExtractor;
import com.nokor.frmk.security.context.SecApplicationContextHolder;

/**
 * @author sok.vina
 */
public class GLFCollectionPerformance extends XLSAbstractReportExtractor implements Report, GLFApplicantFields, CashflowEntityField {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
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

	public GLFCollectionPerformance() {

	}

	@Override
	public String generate(ReportParameter reportParameter) throws Exception {	
		Map<String, Object> parameters = reportParameter.getParameters();
		
		Date startDate = DateUtils.getDateAtBeginningOfMonth((Date) parameters.get("start.date"));
		Date endDate = DateUtils.getDateAtEndOfMonth((Date) parameters.get("end.date"));
		
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
		sheet.setColumnWidth(0, 10000);
		sheet.setColumnWidth(1, 6000);
		sheet.setColumnWidth(2, 6000);
		sheet.setColumnWidth(3, 7000);
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
		createCell(headerRow, 0, "Collection Performance Field &_Phone", 16, true, false, false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,false);
		createCell(headerRow, 1, " ", 16, true, false, false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,false);
		createCell(headerRow, 2, " ", 16, true, false, false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,false);
		createCell(headerRow, 3, " ", 16, true, false, true, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,false);

		int iRow = 0;
		int iCol = 0;
		iCol = iCol + 2;
		iCol = 0;
		iRow = iRow + 1;
		
		if (endDate == null) {
			endDate = DateUtils.getDateAtEndOfDay(DateUtils.today());
		}
		iRow = dataTable(sheet, iRow, style, startDate, endDate);

		iRow = iRow + 1;

		String fileName = writeXLSData("Collection_Performance_Field_and_Phone" + 
				DateUtils.getDateLabel(DateUtils.today(), "yyyyMMddHHmmssSSS") + ".xlsx");

		return fileName;
	}

	private int dataTable(final Sheet sheet, int iRow, final CellStyle style,
			Date startDate, Date endDate) throws Exception {
		/* Create total data header */
		// Format formatter = new SimpleDateFormat("MMM");
		int numMonth = DateUtils.getNumberMonthOfTwoDates(endDate, startDate);
		Format formatter = new SimpleDateFormat("MMMM"); 
		
		String collectionTeam = "Collection Team"; // formatter.format(DateUtils.today());	
		int iCol = 0;
		Row tmpRow = sheet.createRow(iRow++);

		String[] xAxisLabels = new String[numMonth];
		for (int i = 0; i < numMonth; i++) {
			Date date = DateUtils.addMonthsDate(startDate, i);
			String month = formatter.format(date);
			xAxisLabels[i] = month;
		}
		DataSeries dataSeries = new DataSeries(xAxisLabels, "Month",
				"Amount ($)", "Collection Performance Field & Phone");

		String[] legendLabels = new String[2];
		legendLabels[0] = "Phone";
		legendLabels[1] = "Field";
		double[][] datas = new double[2][numMonth];
		for (int i = 0; i < numMonth; i++) {
			Date date = DateUtils.addMonthsDate(startDate, i);
			double performanceTeamByPhone = CollegetCollectionPerformanceAmount(date, true);
			double performanceTeamByField = CollegetCollectionPerformanceAmount(date, false);
			//performance by Phone
			datas[0][i] = performanceTeamByPhone;
			//performance by Field
			datas[1][i] = performanceTeamByField;
		}
		String strPartImage = getImangeChart(dataSeries, datas, legendLabels, numMonth);
		if (!strPartImage.equals("")) {
			Drawing drawing = sheet.createDrawingPatriarch();
			ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, iRow + 3,
					7, iRow + 10);
			File barChart = new File(strPartImage);
			InputStream fStream = new FileInputStream(barChart);
			byte[] bytes = IOUtils.toByteArray(fStream);
			int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
			Picture picture = drawing.createPicture(anchor, pictureIdx);
			picture.resize();
			fStream.close();
			iRow++;
		}
		iRow = iRow + 20;
		iCol = 0;
		
		tmpRow = sheet.createRow(iRow++);
		createCell(tmpRow, iCol++, collectionTeam, 12, false, true, false,
				CellStyle.ALIGN_RIGHT, true, BG_GREY, FC_BLACK, false);
		for (int i = 0; i < numMonth; i++) {
			Date date = DateUtils.addMonthsDate(startDate, i);
			String month = formatter.format(date);
			
			createCell(tmpRow, iCol++, month, 12, false, true,
					true, CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		}

		tmpRow = sheet.createRow(iRow++);
		iCol = 0;
		createNumericCell(tmpRow, iCol++, "Phone Collection ($) ", true,
				CellStyle.ALIGN_RIGHT, 14, false, true, BG_WHITE, FC_BLACK);
		for (int i = 0; i < numMonth; i++) {	
			Date date = DateUtils.addMonthsDate(startDate, i);
			createNumericCell(tmpRow, iCol++, CollegetCollectionPerformanceAmount(date, true), true, CellStyle.ALIGN_CENTER,
					14, false, true, BG_WHITE, FC_BLACK);
		}


		tmpRow = sheet.createRow(iRow++);
		iCol = 0;
		createNumericCell(tmpRow, iCol++, "Field Collection ($) ", true,
				CellStyle.ALIGN_RIGHT, 14, false, true, BG_WHITE, FC_BLACK);
		for (int i = 0; i < numMonth; i++) {
			Date date = DateUtils.addMonthsDate(startDate, i);
			createNumericCell(tmpRow, iCol++, CollegetCollectionPerformanceAmount(date, false), true, CellStyle.ALIGN_CENTER,
					14, false, true, BG_WHITE, FC_BLACK);
		}

		tmpRow = sheet.createRow(iRow++);
		iCol = 0;
		createNumericCell(tmpRow, iCol++, "Number Of Phone Collection", true,
				CellStyle.ALIGN_RIGHT, 14, false, true, BG_WHITE, FC_BLACK);
		for (int i = 0; i < numMonth; i++) {
			Date date = DateUtils.addMonthsDate(startDate, i);
			createNumericCell(tmpRow, iCol++, CollegetCollectionPerformanceNumbert(date, true), true, CellStyle.ALIGN_CENTER,
					14, false, true, BG_WHITE, FC_BLACK);
		}

		tmpRow = sheet.createRow(iRow++);
		iCol = 0;
		createNumericCell(tmpRow, iCol++, "Number Of Field Collection", true,
				CellStyle.ALIGN_RIGHT, 14, false, true, BG_WHITE, FC_BLACK);
		for (int i = 0; i < numMonth; i++) {
			Date date = DateUtils.addMonthsDate(startDate, i);
			createNumericCell(tmpRow, iCol++, CollegetCollectionPerformanceNumbert(date, false), true, CellStyle.ALIGN_CENTER,
					14, false, true, BG_WHITE, FC_BLACK);
		}

		iRow = iRow + 1;
		return iRow;
	}
	
	/**
	 * 
	 * @param datee
	 * @param isCollectionPhone
	 * @return
	 */
	private Double CollegetCollectionPerformanceAmount(Date date, boolean isCollectionPhone) {
		
		Date beginningOfMonth = DateUtils.getDateAtBeginningOfMonth(date);
		Date endOfMonth = DateUtils.getDateAtEndOfMonth(date);
		
		double collectionAmount = 0d;
		
		BaseRestrictions<Payment> restrictions = new BaseRestrictions<Payment>(Payment.class);
		restrictions.addCriterion(Restrictions.ge(PAYMENT_DATE, DateUtils.getDateAtBeginningOfDay(beginningOfMonth)));
		restrictions.addCriterion(Restrictions.le(PAYMENT_DATE, DateUtils.getDateAtEndOfDay(endOfMonth)));
		if (isCollectionPhone) {
			restrictions.addCriterion(Restrictions.lt("numOverdueDays", 30));
		} else {
			restrictions.addCriterion(Restrictions.ge("numOverdueDays", 30));
		}
		List<Payment> payments = entityService.list(restrictions);
		if (payments != null && !payments.isEmpty()) {
			for (Payment payment : payments) {
				if (payment.getCashflows() != null
						&& !payment.getCashflows().isEmpty()) {
					Contract contract = entityService.getById(Contract.class, payment.getCashflows().get(0).getContract().getId());
					Quotation quotation = entityService.getByField(Quotation.class, "boReference", contract.getId());
					if (contract.getPenaltyRule().getPenaltyCalculMethod().equals(EPenaltyCalculMethod.FPD)) {
						collectionAmount += payment.getNumPenaltyDays() * contract.getPenaltyRule().getTiPenaltyAmounPerDaytUsd();
					} else if (contract.getPenaltyRule().getPenaltyCalculMethod().equals(EPenaltyCalculMethod.PIS)) {
						collectionAmount += payment.getNumPenaltyDays() * (contract.getPenaltyRule().getPenaltyRate() / 100) * quotation.getTiInstallmentAmount();
					}
				}
			}
		}
		return collectionAmount;
	}
	
	private int CollegetCollectionPerformanceNumbert(Date date, boolean isCollectionPhone) {
		
		Date beginningOfMonth = DateUtils.getDateAtBeginningOfMonth(date);
		Date endOfMonth = DateUtils.getDateAtEndOfMonth(date);
		
		int numCollection = 0;
		
		BaseRestrictions<Payment> restrictions = new BaseRestrictions<Payment>(Payment.class);
		restrictions.addCriterion(Restrictions.ge(PAYMENT_DATE, DateUtils.getDateAtBeginningOfDay(beginningOfMonth)));
		restrictions.addCriterion(Restrictions.le(PAYMENT_DATE, DateUtils.getDateAtEndOfDay(endOfMonth)));
		if (isCollectionPhone) {
			restrictions.addCriterion(Restrictions.lt("numOverdueDays", 30));
		} else {
			restrictions.addCriterion(Restrictions.ge("numOverdueDays", 30));
		}
		List<Payment> payments = entityService.list(restrictions);
		if (payments != null && !payments.isEmpty()) {
			for (Payment payment : payments) {
				if (payment.getCashflows() != null
						&& !payment.getCashflows().isEmpty()) {
					numCollection++;
				}
			}
		}
		return numCollection;
	}
	/**
	 * 
	 * @param dataSeries
	 * @param data
	 * @param legendLabels
	 * @return
	 */
	private String getImangeChart(DataSeries dataSeries, double[][] datas, String[] legendLabels, int numMonth) {
		try {
			StackedBarChartProperties stackedBarChartProperties = new StackedBarChartProperties();
			if (numMonth < 4) {
				stackedBarChartProperties.setWidthPercentage((float) ((0.1 * numMonth) + 0.1));
			} else {
				stackedBarChartProperties.setWidthPercentage((float) 0.4);
			}
	       // set value in bar chart label ValueLabelRenderer(boolean isCurrency, boolean isPercent, boolean showGrouping, int roundingPowerOfTen);
	       ValueLabelRenderer valueLabelRenderer = new ValueLabelRenderer(false,true,true, -2);
	       valueLabelRenderer.setValueLabelPosition( ValueLabelPosition.ON_TOP );
	       valueLabelRenderer.useVerticalLabels( false );
	       stackedBarChartProperties.addPostRenderEventListener(valueLabelRenderer);
	       
	       Paint[] paints= DataGenerator.getPaints(2);
	       AxisChartDataSet axisChartDataSet= new AxisChartDataSet( datas, legendLabels, paints, ChartType.BAR_STACKED, stackedBarChartProperties );
	       dataSeries.addIAxisPlotDataSet(axisChartDataSet);
	       
	       ChartProperties chartProperties= new ChartProperties();
	       AxisProperties axisProperties= new AxisProperties();
	       LegendProperties legendProperties = new LegendProperties();
	       int imageWidth = 0;
			if (numMonth <= 1) {
				imageWidth = 220 * (numMonth+1);
			} else {
				imageWidth = 220 * (numMonth+1) + 25 * numMonth;
			}
	       AxisChart axisChart= new AxisChart(dataSeries, chartProperties, axisProperties, legendProperties, imageWidth, 300);
	       
	   	   String tmpDir = AppConfig.getInstance().getConfiguration().getString("specific.tmpdir");
	   	   String tmpPath = "collectio_perform_phone&field" + new Date().getTime() + ".png";
	       OutputStream outputChart = new FileOutputStream(tmpDir + "/" + tmpPath);
	       PNGEncoder.encode(axisChart, outputChart);
	       outputChart.close();
	       return tmpDir + "/" + tmpPath;
		} catch (Exception e) {
			logger.error("Exception", e);
		}
		 return "";
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
		if (isBold) {
			itemFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
			style.setBorderBottom(CellStyle.BORDER_THIN);
		}
		if (hasBorder) {
			style.setBorderTop(CellStyle.BORDER_THIN);
			style.setBorderLeft(CellStyle.BORDER_THIN);
			style.setBorderRight(CellStyle.BORDER_THIN);
			style.setBorderBottom(CellStyle.BORDER_THIN);
			style.setBottomBorderColor(FC_BLACK);
		}
		if (leftRight) {
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

