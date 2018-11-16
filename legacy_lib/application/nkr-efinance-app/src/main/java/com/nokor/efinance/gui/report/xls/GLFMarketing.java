package com.nokor.efinance.gui.report.xls;

import java.awt.Paint;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.jCharts.axisChart.AxisChart;
import org.jCharts.chartData.AxisChartDataSet;
import org.jCharts.chartData.DataSeries;
import org.jCharts.encoders.PNGEncoder;
import org.jCharts.properties.AxisProperties;
import org.jCharts.properties.ChartProperties;
import org.jCharts.properties.ClusteredBarChartProperties;
import org.jCharts.properties.LegendProperties;
import org.jCharts.types.ChartType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.quotation.QuotationService;
import com.nokor.efinance.core.shared.conf.AppConfig;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.efinance.glf.statistic.model.EComplaintType;
import com.nokor.efinance.glf.statistic.model.Statistic3HoursVisitor;
import com.nokor.efinance.tools.report.Report;
import com.nokor.efinance.tools.report.XLSAbstractReportExtractor;
import com.nokor.frmk.security.context.SecApplicationContextHolder;

/**
 * @author sok.vina
 */
public class GLFMarketing extends XLSAbstractReportExtractor implements Report, GLFApplicantFields, QuotationEntityField {

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
	static short BG_AQUA = IndexedColors.AQUA.getIndex();

	/** Font color */
	static short FC_WHITE = IndexedColors.WHITE.getIndex();
	static short FC_BLACK = IndexedColors.BLACK.getIndex();
	static short FC_BLUE = 48;
	static short FC_GREY = IndexedColors.GREY_80_PERCENT.getIndex();
	static short FC_GREEN = IndexedColors.GREEN.getIndex();

	public GLFMarketing() {

	}

	@Override
	public String generate(ReportParameter reportParameter) throws Exception {	
		Map<String, Object> parameters = reportParameter.getParameters();
		Date startDate = (Date) parameters.get("startDate");
		Date endDate = (Date) parameters.get("endDate");
		List<EComplaintType> complaints = EComplaintType.values();
		
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
		sheet.setColumnWidth(1, 4000);
		if (complaints != null && !complaints.isEmpty()) {
			for (int i = 2; i < complaints.size()+2; i++) {
				sheet.setColumnWidth(i, 5000);
			}
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
		int iRow = 0;
		int iCol = 0;
		iCol = iCol + 2;
		iCol = 0;
		iRow = iRow + 1;
		iRow = DataTable(sheet, iRow, style, complaints, startDate, endDate);

//		Row tmpRowEnd = sheet.createRow(iRow++);
//		tmpRowEnd.setRowStyle(style);

		iRow = iRow + 1;
		String fileName = writeXLSData("Marketing_" + DateUtils.getDateLabel(DateUtils.today(), "yyyyMMddHHmmssSSS") + ".xlsx");
		return fileName;
	}

	private int DataTable(final Sheet sheet, int iRow, final CellStyle style, List<EComplaintType> complaints, Date startDate, Date endDate) throws Exception {
		/* Create total data header */
//		Format formatter = new SimpleDateFormat("MMMM");  
//		String month = formatter.format(date);
		int totalNumStatisticVisitor = 0;
		String[] xAxisLabels = new String[complaints.size()];
		String xAxisTitle= "Complaints";
		String yAxisTitle= "Pecentage(%)";
		String title= "";
		int numOfMonth = DateUtils.getNumberMonthOfTwoDates(endDate, startDate);
		List<MonthAndComplaint> monthAndComplaints = new ArrayList<MonthAndComplaint>();
		int iCol = 0;
		Row tmpRow = sheet.createRow(iRow++);
		createCell(tmpRow, iCol++, "Month", 14, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_AQUA, FC_BLACK, false);
		createCell(tmpRow, iCol++, "Description", 14, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_AQUA, FC_BLACK, false);
		
		if (complaints != null && !complaints.isEmpty()) {
			for (int i = 0; i < complaints.size(); i++) {
				createCell(tmpRow, iCol++, complaints.get(i).getDescEn(), 14, false, true, false,
						CellStyle.ALIGN_CENTER, true, BG_AQUA, FC_BLACK, true);
			}
		}
		
		if (numOfMonth > 0) {
			int nbRows = numOfMonth;
			for (int i = 0; i < nbRows; i++) {
				totalNumStatisticVisitor = 0;
				Format formatter = new SimpleDateFormat("MMM");  
				Date dateOfMonth = DateUtils.addMonthsDate(startDate, i);
				String month = formatter.format(dateOfMonth);
				List<Statistic3HoursVisitor> statisticVisitors = getStatisticVisitors(DateUtils.getDateAtBeginningOfMonth(dateOfMonth), DateUtils.getDateAtEndOfMonth(dateOfMonth));
				
				tmpRow = sheet.createRow(iRow++);
				iCol = 0;
				createCell(tmpRow, iCol++, month, 14, false, true, false,
						CellStyle.ALIGN_CENTER, true, BG_AQUA, FC_BLACK, false);
				sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow, 0, 0));
				
				createCell(tmpRow, iCol++, "Amount", 14, false, true, false,
						CellStyle.ALIGN_CENTER, true, BG_AQUA, FC_BLACK, false);
				if (complaints != null && !complaints.isEmpty()) {
					for (EComplaintType complaint : complaints) {
						totalNumStatisticVisitor += getStatisticVisitorComplaint(statisticVisitors,complaint.getId());
						createNumericCell(tmpRow, iCol++, getStatisticVisitorComplaint(statisticVisitors,complaint.getId()), false,
								CellStyle.ALIGN_CENTER, 14, false, true, BG_WHITE, FC_BLACK);
						
					}
				}
				tmpRow = sheet.createRow(iRow++);
				
				iCol = 1;
				createCell(tmpRow, iCol++, "%", 14, false, true, false,
						CellStyle.ALIGN_CENTER, true, BG_AQUA, FC_BLACK, false);
				List<Double> percentages = new ArrayList<Double>();
				if (complaints != null && !complaints.isEmpty()) {
					for (int j = 0; j < complaints.size(); j++) {
						EComplaintType complaint = complaints.get(j);
						double percenttageValue = 0d;
						if (totalNumStatisticVisitor > 0) {
							percenttageValue = (getStatisticVisitorComplaint(statisticVisitors,complaint.getId())*100)/totalNumStatisticVisitor;
							xAxisLabels[j] = complaint.getDescEn();
							percentages.add(percenttageValue);
							if (j == complaints.size() - 1) {
								MonthAndComplaint monthAndComplaint = new MonthAndComplaint();
								monthAndComplaint.setMonth(month);
								monthAndComplaint.setPercentageComplaints(percentages);
								monthAndComplaints.add(monthAndComplaint);
							}
						}
						createNumericCell(tmpRow, iCol++, percenttageValue+"%", false,
								CellStyle.ALIGN_CENTER, 14, false, true, BG_WHITE, FC_BLACK);
					}
				}
				title = "GLF's Visitor Feedback = " + totalNumStatisticVisitor;
			}
			if (monthAndComplaints != null && !monthAndComplaints.isEmpty()) {
				String[] legendLabels = new String[monthAndComplaints.size()];
				double[][] datas = new double[monthAndComplaints.size()][complaints.size()];
				for (int i = 0; i < monthAndComplaints.size(); i++) {
					MonthAndComplaint monthAndComplaint = monthAndComplaints.get(i);
					legendLabels[i] = monthAndComplaint.getMonth();
					List<Double> percentages = monthAndComplaint.getPercentageComplaints();
					for (int j = 0; j < percentages.size(); j++) {
						datas[i][j] = percentages.get(j);
					}
				}
				DataSeries dataSeries = new DataSeries(xAxisLabels, xAxisTitle, yAxisTitle, title );
				String strPartImage = getImangeChart(dataSeries,datas,legendLabels);
				if (!strPartImage.equals("")) {
				        Drawing drawing = sheet.createDrawingPatriarch();
				        ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, iRow + 5, complaints.size(), iRow + 15);
			            File barChart = new File(strPartImage);
			            InputStream fStream = new FileInputStream(barChart);
				        byte[] bytes = IOUtils.toByteArray(fStream);
				        int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
			            Picture picture = drawing.createPicture(anchor, pictureIdx);
			            picture.resize();
			            fStream.close();
					}	
			}

		}

		return iRow;

	}

	public String getImangeChart(DataSeries dataSeries, double[][] data, String[] legendLabels) {
		try {
	       //Paint[] paints= TestDataGenerator.getRandomPaints(legendLabels.length+1);
			Paint[] paints= DataGenerator.getPaints(legendLabels.length);
	       ClusteredBarChartProperties clusteredBarChartProperties= new ClusteredBarChartProperties();
			if (legendLabels.length <= 2) {
			       clusteredBarChartProperties.setWidthPercentage((float)0.4);
			}
	       AxisChartDataSet axisChartDataSet= new AxisChartDataSet( data, legendLabels, paints, ChartType.BAR_CLUSTERED, clusteredBarChartProperties );
	       dataSeries.addIAxisPlotDataSet( axisChartDataSet );
	       ChartProperties chartProperties= new ChartProperties();
	       AxisProperties axis_Properties= new AxisProperties();
	       LegendProperties legend_Properties= new LegendProperties(); 
	       AxisChart my_output_chart= new AxisChart( dataSeries, chartProperties, axis_Properties, legend_Properties, 2500, 500 );
	   	   String tmpDir = AppConfig.getInstance().getConfiguration().getString("specific.tmpdir");
	   	   String tmpPath = "marketing_chart_" + (new Date().getTime())+".png";
	       OutputStream outputChart = new FileOutputStream(tmpDir + "/" + tmpPath);
	       PNGEncoder.encode(my_output_chart,outputChart);
	       outputChart.close();
	       return tmpDir + "/" + tmpPath;
		} catch (Exception e) {
			 System.out.println(e);
		}
		 return "";
	}
	/**
	 * 
	 * @param statisticVisitors
	 * @param complaintId
	 * @return
	 */
	private int getStatisticVisitorComplaint(List<Statistic3HoursVisitor> statisticVisitors, long complaintId) {
		int numComplaint = 0;
		if (statisticVisitors != null && !statisticVisitors.isEmpty()) {
			for (Statistic3HoursVisitor statisticVisitor : statisticVisitors) {
				
//				if (statisticVisitor.getComplaint1() != null) {
//					if (statisticVisitor.getComplaint1().getId() == complaintId) {
//						numComplaint++;
//					}
//				}
//				if (statisticVisitor.getComplaint2() != null) {
//					if (statisticVisitor.getComplaint2().getId() == complaintId) {
//						numComplaint++;
//					}
//				}
//				if (statisticVisitor.getComplaint3() != null) {
//					if (statisticVisitor.getComplaint3().getId() == complaintId) {
//						numComplaint++;
//					}
//				}
			}
		}

		return numComplaint;
	}
	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private List<Statistic3HoursVisitor> getStatisticVisitors(Date startDate, Date endDate) {
		BaseRestrictions<Statistic3HoursVisitor> restrictions = new BaseRestrictions<Statistic3HoursVisitor>(Statistic3HoursVisitor.class);
		restrictions.addCriterion(Restrictions.ge("createDate", startDate));
		restrictions.addCriterion(Restrictions.le("createDate", endDate));
		return entityService.list(restrictions);
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

	private Cell createNumericCell(final Row row, final int iCol,
			final Object value, final boolean hasBorder, final short alignment,
			final int fontsize, final boolean isBold, final boolean setBgColor,
			final short bgColor, final short fontColor) {

		final Cell cell = row.createCell(iCol);
		cell.setCellStyle(styles.get("ALIGN_CENTER"));
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
            cell.setCellStyle(styles.get(AMOUNT));
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
    	//style.setAlignment(CellStyle.ALIGN_RIGHT);
    	style.setFont(itemFont);
    	style.setDataFormat(format.getFormat("#,##0.00"));
    	styles.put(AMOUNT, style);
    	
    	style = wb.createCellStyle();
		itemFont.setFontHeightInPoints((short) 12);
		itemFont.setFontName("Khmer OS Battambang");
		style = wb.createCellStyle();
		style.setFont(itemFont);
    	style.setAlignment(CellStyle.ALIGN_CENTER);
    	styles.put("ALIGN_CENTER", style);

		return styles;
	}

	protected CellStyle getCellStyle(final String styleName) {
		return styles.get(styleName);
	}

	private class MonthAndComplaint {
		private String month;
		private List<Double> percentageComplaints;
		/**
		 * @return the month
		 */
		public String getMonth() {
			return month;
		}
		/**
		 * @param month the month to set
		 */
		public void setMonth(String month) {
			this.month = month;
		}
		/**
		 * @return the percentageComplaints
		 */
		public List<Double> getPercentageComplaints() {
			return percentageComplaints;
		}
		/**
		 * @param percentageComplaints the percentageComplaints to set
		 */
		public void setPercentageComplaints(List<Double> percentageComplaints) {
			this.percentageComplaints = percentageComplaints;
		}
		
	}
}

