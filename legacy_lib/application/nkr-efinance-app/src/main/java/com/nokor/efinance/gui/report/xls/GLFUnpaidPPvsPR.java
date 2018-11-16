package com.nokor.efinance.gui.report.xls;

import java.awt.Paint;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.jCharts.axisChart.AxisChart;
import org.jCharts.axisChart.customRenderers.axisValue.renderers.ValueLabelPosition;
import org.jCharts.axisChart.customRenderers.axisValue.renderers.ValueLabelRenderer;
import org.jCharts.chartData.AxisChartDataSet;
import org.jCharts.chartData.DataSeries;
import org.jCharts.encoders.PNGEncoder;
import org.jCharts.properties.AxisProperties;
import org.jCharts.properties.ChartProperties;
import org.jCharts.properties.ClusteredBarChartProperties;
import org.jCharts.properties.LegendProperties;
import org.jCharts.types.ChartType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.service.ContractService;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.shared.conf.AppConfig;
import com.nokor.efinance.core.shared.contract.PenaltyVO;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.efinance.tools.report.Report;
import com.nokor.efinance.tools.report.XLSAbstractReportExtractor;

/**
 * @author sok.vina
 */
public class GLFUnpaidPPvsPR extends XLSAbstractReportExtractor implements Report, GLFApplicantFields, CashflowEntityField {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private ContractService contractService = SpringUtils.getBean(ContractService.class);

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

	public GLFUnpaidPPvsPR() {

	}

	@Override
	public String generate(ReportParameter reportParameter) throws Exception {	
		Map<String, Object> parameters = reportParameter.getParameters();
		
		Dealer dealer = (Dealer) parameters.get("dealer");
		EDealerType dealerType = (EDealerType) parameters.get("dealer.type");
		Date endDate = DateUtils.getDateAtEndOfDay((Date) parameters.get("endDate"));
		
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
		sheet.setColumnWidth(0, 8000);
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
		createCell(headerRow, 0, "Unpaid 31 day+ Phnom Penh vs province", 16, true, false, false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,false);
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
		iRow = dataTable(sheet, iRow, style, dealerType, dealer, endDate);

		iRow = iRow + 1;

		String fileName = writeXLSData("Unpaid_PP_vs_PR_report" + DateUtils.getDateLabel(DateUtils.today(), "yyyyMMddHHmmssSSS") + ".xlsx");

		return fileName;
	}

	private int dataTable(final Sheet sheet, int iRow, final CellStyle style,
			EDealerType dt, Dealer dealer, Date endDate) throws Exception {
		/* Create total data header */
		// Format formatter = new SimpleDateFormat("MMM");
		
		long numlesseeAtProvince = getNumLesseeByLocation(dealer, dt, true);
		long numlesseeAtPP = getNumLesseeByLocation(dealer, dt, false);
		
		List<Cashflow> cashflowsAtProvince = getListCashflowByLocation(dealer, dt, endDate, true);
		List<Cashflow> cashflowsAtPP = getListCashflowByLocation(dealer, dt, endDate, false);
		
		String month = "Number Overdue"; // formatter.format(DateUtils.today());
		
		int numOverdueDays31PlusAtProvince = 0;
		int numOverdueDays31PlusAtPP = 0;
		if (cashflowsAtProvince != null && !cashflowsAtProvince.isEmpty()) {
			List<CashflowPayment> cashflowPayments = getCashflowPayments(cashflowsAtProvince);
			if (cashflowPayments != null && !cashflowPayments.isEmpty()) {
				for (CashflowPayment cashflowPayment : cashflowPayments) {
					Contract contract = cashflowPayment.getContract();
					PenaltyVO penaltyVo = contractService.calculatePenalty(contract, cashflowPayment.getInstallmentDate(), DateUtils.todayH00M00S00(), 
							MyNumberUtils.getDouble(cashflowPayment.getTiInstallmentAmount()));
					Integer numPenaltyInstallment = penaltyVo.getNumPenaltyDays();
					if (numPenaltyInstallment != null) {
						if (numPenaltyInstallment >= 31) {
							numOverdueDays31PlusAtProvince++;
						}
					}
				}

			}
		}
		
		if (cashflowsAtPP != null && !cashflowsAtPP.isEmpty()) {
			List<CashflowPayment> cashflowPayments = getCashflowPayments(cashflowsAtPP);
			if (cashflowPayments != null && !cashflowPayments.isEmpty()) {
				for (CashflowPayment cashflowPayment : cashflowPayments) {
					Contract contract = cashflowPayment.getContract();
					PenaltyVO penaltyVo = contractService.calculatePenalty(contract, cashflowPayment.getInstallmentDate(), DateUtils.todayH00M00S00(), 
							MyNumberUtils.getDouble(cashflowPayment.getTiInstallmentAmount()));
					Integer numOverdueInstallment = penaltyVo.getNumPenaltyDays();
					if (numOverdueInstallment != null) {

						if (numOverdueInstallment >= 31) {
							numOverdueDays31PlusAtPP++;
						}
					}

				}

			}
		}		
		int iCol = 0;
		Row tmpRow = sheet.createRow(iRow++);

		String[] xAxisLabels = new String[4];
		xAxisLabels[0] = "Unpaid 31 Days+ P.P";
		xAxisLabels[1] = "Active Lessee P.P";
		xAxisLabels[2] = "Unpaid 31 Days+ Province";
		xAxisLabels[3] = "Active Lessee Province";
		DataSeries dataSeries = new DataSeries(xAxisLabels, "Unpaid31days+",
				"Number", "Unpaid31days+ & Active Lessee");

		String[] legendLabels = new String[1];
		legendLabels[0] = month;
		double[][] datas = new double[1][4];
		datas[0][0] = numOverdueDays31PlusAtPP;
		datas[0][1] = numlesseeAtPP;
		datas[0][2] = numOverdueDays31PlusAtProvince;
		datas[0][3] = numlesseeAtProvince;
		String strPartImage = getImangeChart(dataSeries, datas, legendLabels);
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
		createCell(tmpRow, iCol++, "", 12, false, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		createCell(tmpRow, iCol++, "", 12, false, false, true,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		createCell(tmpRow, iCol++, month, 12, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		createCell(tmpRow, iCol++, "Ratio on Active Lessee", 12, false, true,
				true, CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);

		tmpRow = sheet.createRow(iRow++);
		iCol = 0;
		createNumericCell(tmpRow, iCol++, "Unpaid 31 Days+", true,
				CellStyle.ALIGN_CENTER, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "P.P", true, CellStyle.ALIGN_CENTER,
				14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, numOverdueDays31PlusAtPP, true, CellStyle.ALIGN_CENTER, 14,
				false, true, BG_WHITE, FC_BLACK);
		DecimalFormat formatDouble = new DecimalFormat("##0.00");
		createNumericCell(tmpRow, iCol++,(numlesseeAtPP != 0 ? formatDouble.format(numOverdueDays31PlusAtPP * 100 / (double)numlesseeAtPP) : 0.00)+"%", true, CellStyle.ALIGN_CENTER, 14,
				false, true, BG_WHITE, FC_BLACK);

		tmpRow = sheet.createRow(iRow++);
		iCol = 0;
		createNumericCell(tmpRow, iCol++, "Active Lessee", true,
				CellStyle.ALIGN_CENTER, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "P.P", true, CellStyle.ALIGN_CENTER,
				14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, numlesseeAtPP, true, CellStyle.ALIGN_CENTER, 14,
				false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true, CellStyle.ALIGN_CENTER, 14,
				false, true, BG_WHITE, FC_BLACK);

		tmpRow = sheet.createRow(iRow++);
		iCol = 0;
		createNumericCell(tmpRow, iCol++, "Unpaid 31days+", true,
				CellStyle.ALIGN_CENTER, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "Province", true,
				CellStyle.ALIGN_CENTER, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, numOverdueDays31PlusAtProvince, true, CellStyle.ALIGN_CENTER, 14,
				false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, (numlesseeAtProvince != 0 ? formatDouble.format(numOverdueDays31PlusAtProvince * 100 / (double)numlesseeAtProvince) : 0.00)+"%", true, CellStyle.ALIGN_CENTER, 14,
				false, true, BG_WHITE, FC_BLACK);

		tmpRow = sheet.createRow(iRow++);
		iCol = 0;
		createNumericCell(tmpRow, iCol++, "Active Lessee", true,
				CellStyle.ALIGN_CENTER, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "Province", true,
				CellStyle.ALIGN_CENTER, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, numlesseeAtProvince, true, CellStyle.ALIGN_CENTER, 14,
				false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true, CellStyle.ALIGN_CENTER, 14,
				false, true, BG_WHITE, FC_BLACK);

		iRow = iRow + 1;
		return iRow;
	}
	/**
	 * 
	 * @param dataSeries
	 * @param data
	 * @param legendLabels
	 * @return
	 */
	private String getImangeChart(DataSeries dataSeries, double[][] datas, String[] legendLabels) {
		try {
	       ClusteredBarChartProperties clusteredBarChartProperties = new ClusteredBarChartProperties();
	       // set value in bar chart label ValueLabelRenderer(boolean isCurrency, boolean isPercent, boolean showGrouping, int roundingPowerOfTen);
	       ValueLabelRenderer valueLabelRenderer = new ValueLabelRenderer( false, false, false, 0 );
	       valueLabelRenderer.setValueLabelPosition( ValueLabelPosition.ON_TOP );
	       valueLabelRenderer.useVerticalLabels( false );
	       clusteredBarChartProperties.addPostRenderEventListener(valueLabelRenderer);
	       clusteredBarChartProperties.setWidthPercentage((float)0.4);
	       
	       Paint[] paints= DataGenerator.getPaints(1);
	       AxisChartDataSet axisChartDataSet= new AxisChartDataSet( datas, legendLabels, paints, ChartType.BAR_CLUSTERED, clusteredBarChartProperties );
	       dataSeries.addIAxisPlotDataSet(axisChartDataSet);
	       
	       ChartProperties chartProperties= new ChartProperties();
	       AxisProperties axisProperties= new AxisProperties();
	       LegendProperties legendProperties = new LegendProperties();
	       AxisChart axisChart= new AxisChart(dataSeries, chartProperties, axisProperties, legendProperties, 800, 300);
	       
	   	   String tmpDir = AppConfig.getInstance().getConfiguration().getString("specific.tmpdir");
	   	   String tmpPath = "Unpaid_PP_vs_PR" + new Date().getTime() + ".png";
	       OutputStream outputChart = new FileOutputStream(tmpDir + "/" + tmpPath);
	       PNGEncoder.encode(axisChart, outputChart);
	       outputChart.close();
	       return tmpDir + "/" + tmpPath;
		} catch (Exception e) {
			logger.error("Exception", e);
		}
		 return "";
	}
	/**
	 * 
	 * @param dealer
	 * @param endDate
	 * @param isProvince
	 * @return
	 */
	public List<Cashflow> getListCashflowByLocation(Dealer dealer, EDealerType dt, Date endDate, boolean isProvince) {
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<>(Cashflow.class);
		restrictions.addAssociation("contract", "cont", JoinType.INNER_JOIN);
		restrictions.addAssociation("cont.penaltyRule", "penalty", JoinType.INNER_JOIN);
		restrictions.addAssociation("cont.dealer", "dea", JoinType.INNER_JOIN);
		restrictions.addAssociation("dea.dealerAddresses", "deaAddres", JoinType.INNER_JOIN);
		restrictions.addAssociation("deaAddres.address", "addr", JoinType.INNER_JOIN);
		
		restrictions.addCriterion(Restrictions.eq(PAID, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.eq(CANCEL, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.ne(CASHFLOW_TYPE, ECashflowType.FIN));
		restrictions.addCriterion(Restrictions.ge(NUM_INSTALLMENT, 0));
		restrictions.addCriterion(Restrictions.le(INSTALLMENT_DATE, endDate));
		if (isProvince) {
			restrictions.addCriterion(Restrictions.ne("addr.province.id", new Long(12)));
		} else {
			restrictions.addCriterion(Restrictions.eq("addr.province.id", new Long(12))); // 12 phnom penh id in table province
		}
		
		if (dealer != null) {
			restrictions.addCriterion(Restrictions.eq("dea." + ID, dealer.getId()));
		}
		if (dt != null) {
			restrictions.addCriterion(Restrictions.eq("dea.dealerType", dt));
		}
		restrictions.addOrder(Order.asc(NUM_INSTALLMENT));
		
		return contractService.list(restrictions);

	}
	/**
	 * 
	 * @return numActiveLessee
	 */
	private long getNumLesseeByLocation(Dealer dealer, EDealerType dt, boolean isProvince) {
		 
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<>(Contract.class);
		restrictions.addAssociation("penaltyRule", "penalty", JoinType.INNER_JOIN);
		restrictions.addAssociation("dealer", "dea", JoinType.INNER_JOIN);
		restrictions.addAssociation("dea.dealerAddresses", "deaAddres", JoinType.INNER_JOIN);
		restrictions.addAssociation("deaAddres.address", "addr", JoinType.INNER_JOIN);
		if (dealer != null) {
			restrictions.addCriterion(Restrictions.eq("dea." + ID, dealer.getId()));
		}
		if (dt != null) {
			restrictions.addCriterion(Restrictions.eq("dea.dealerType", dt));
		}
		
		if (isProvince) {
			restrictions.addCriterion(Restrictions.ne("addr.province.id", new Long(12)));
		} else {
			restrictions.addCriterion(Restrictions.eq("addr.province.id", new Long(12))); // 12 phnom penh id in table province
		}
		
		return contractService.count(restrictions);
	}
	
	/**
	 * 
	 * @param cashflows
	 * @return
	 */
	private List<CashflowPayment> getCashflowPayments(List<Cashflow> cashflows) {

		List<CashflowPayment> cashflowPayments = new ArrayList<CashflowPayment>();
		for (Cashflow cashflow : cashflows) {
			CashflowPayment cashflowPayment = getCashflowPayment(cashflowPayments, cashflow);
			if (cashflowPayment != null) {
				if (cashflow.getCashflowType().equals(ECashflowType.CAP) || cashflow.getCashflowType().equals(ECashflowType.IAP)) {
					cashflowPayment.setTiInstallmentAmount(MyNumberUtils.getDouble(cashflowPayment.getTiInstallmentAmount()) + cashflow.getTiInstallmentAmount());
				} else {
					cashflowPayment.setTiOtherInstallmentAmount(MyNumberUtils.getDouble(cashflowPayment.getTiOtherInstallmentAmount()) + cashflow.getTiInstallmentAmount());
				}
			} else {
				cashflowPayment = new CashflowPayment();
				cashflowPayment.setContract(cashflow.getContract());
				cashflowPayment.setInstallmentDate(cashflow.getInstallmentDate());
				if (cashflow.getCashflowType().equals(ECashflowType.CAP) || cashflow.getCashflowType().equals(ECashflowType.IAP)) {
					cashflowPayment.setTiInstallmentAmount(cashflow.getTiInstallmentAmount());
				} else {
					cashflowPayment.setTiOtherInstallmentAmount(cashflow.getTiInstallmentAmount());
				}
				cashflowPayments.add(cashflowPayment);
			}
		}
		return cashflowPayments;
	}
	/**
	 * @param cashflowPayments
	 * @param cashflow
	 * @return
	 */
	private CashflowPayment getCashflowPayment(List<CashflowPayment> cashflowPayments, Cashflow cashflow) {
		for (CashflowPayment cashflowPayment : cashflowPayments) {
			if (cashflowPayment.getContract().getReference().equals(cashflow.getContract().getReference())
					&& DateUtils.getDateWithoutTime(cashflowPayment.getInstallmentDate())
						.compareTo(DateUtils.getDateWithoutTime(cashflow.getInstallmentDate())) == 0) {
				return cashflowPayment;
			}
		}
		return null;
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
		
		CellStyle style = wb.createCellStyle();
		DataFormat format = wb.createDataFormat();
	    style.setDataFormat(format.getFormat("0"));
	    
		if (value == null) {
			cell.setCellValue("");
		} else if (value instanceof Integer) {
			cell.setCellValue(Integer.valueOf(value.toString()));
			cell.setCellStyle(style);
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		} else if (value instanceof Long) {
			cell.setCellValue(Long.valueOf(value.toString()));
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellStyle(style);
		} else if (value instanceof String) {
			cell.setCellValue(value.toString());
		}
		if (value instanceof Double) {
            cell.setCellValue((Double) value);
            cell.setCellStyle(styles.get(AMOUNT));
            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        }
		//cell.setCellStyle(styles.get("ALIGN_CENTER"));	
		style.setAlignment(CellStyle.ALIGN_CENTER);
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
	/**
	 * @author ly.youhort
	 */
	private class CashflowPayment implements Serializable, Entity {
		private static final long serialVersionUID = 3112339520304252300L;
		private Contract contract;
		private Date installmentDate;
		private Double tiInstallmentUsd;
		private Double tiOtherInstallmentUsd;
		
		/**
		 * @return the contract
		 */
		public Contract getContract() {
			return contract;
		}
		/**
		 * @param contract the contract to set
		 */
		public void setContract(Contract contract) {
			this.contract = contract;
		}
		/**
		 * @return the installmentDate
		 */
		public Date getInstallmentDate() {
			return installmentDate;
		}
		/**
		 * @param installmentDate the installmentDate to set
		 */
		public void setInstallmentDate(Date installmentDate) {
			this.installmentDate = installmentDate;
		}
		/**
		 * @return the tiInstallmentUsd
		 */
		public Double getTiInstallmentAmount() {
			return tiInstallmentUsd;
		}
		/**
		 * @param tiInstallmentUsd the tiInstallmentUsd to set
		 */
		public void setTiInstallmentAmount(Double tiInstallmentUsd) {
			this.tiInstallmentUsd = tiInstallmentUsd;
		}
		
		/**
		 * @return the tiOtherInstallmentUsd
		 */
		public Double getTiOtherInstallmentAmount() {
			return tiOtherInstallmentUsd;
		}
		/**
		 * @param tiOtherInstallmentUsd the tiOtherInstallmentUsd to set
		 */
		public void setTiOtherInstallmentAmount(Double tiOtherInstallmentUsd) {
			this.tiOtherInstallmentUsd = tiOtherInstallmentUsd;
		}

		@Override
		public Long getId() {
			return null;
		}
	}
}

