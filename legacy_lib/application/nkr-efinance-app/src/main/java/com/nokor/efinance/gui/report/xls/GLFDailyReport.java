package com.nokor.efinance.gui.report.xls;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyMathUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.conf.AppConfig;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.efinance.core.shared.util.DateFilterUtil;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.efinance.glf.report.model.DailyReport;
import com.nokor.efinance.glf.report.service.DailyReportService;
import com.nokor.efinance.glf.statistic.model.Statistic3HoursVisitor;
import com.nokor.efinance.glf.statistic.model.StatisticConfig;
import com.nokor.efinance.tools.report.Report;
import com.nokor.efinance.tools.report.XLSAbstractReportExtractor;

/**
 * @author sok.vina
 * @author buntha.chea (modified)
 */
public class GLFDailyReport extends XLSAbstractReportExtractor implements
		Report, GLFApplicantFields, QuotationEntityField {

	protected DailyReportService dailyReportService = SpringUtils.getBean(DailyReportService.class);
	protected Logger logger = LoggerFactory.getLogger(getClass());
	private EntityService entityService = SpringUtils.getBean(EntityService.class);

	private Map<String, CellStyle> styles = null;

	private static String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";

	private static String LEFT_BORDER_ALIGN_LEFT = "LEFT_BORDER_ALIGN_LEFT";
	private static String RIGHT_BORDER = "RIGHT_BORDER";
	private static String TOP_LEFT_BOTTOM_BORDER_ALIGN_LEFT = "TOP_LEFT_BOTTOM_BORDER_ALIGN_LEFT";
	private static String TOP_RIGHT_BOTTOM_BORDER_ALIGN_RIGHT = "TOP_RIGHT_BOTTOM_BORDER_ALIGN_RIGHT";
	private static String TOP_BOTTOM_BORDER_ALIGN_RIGHT_PERCENTAGE = "TOP_BOTTOM_BORDER_ALIGN_RIGHT_PERCENTAGE";
	private static String TOP_BOTTOM_BORDER_ALIGN_RIGHT = "TOP_BOTTOM_BORDER_ALIGN_RIGHT";
	private static String NONE_BORDER_ALIGN_RIGHT_PERCENTAGE = "NONE_BORDER_ALIGN_RIGHT_PERCENTAGE";
	private static String NONE_BORDER_ALIGN_RIGHT = "NONE_BORDER_ALIGN_RIGHT";

	/** Background color format */
	static short BG_LIGHT_BLUE = IndexedColors.LIGHT_BLUE.getIndex();
	static short BG_WHITE = IndexedColors.WHITE.getIndex();
	static short BG_GREEN = IndexedColors.GREEN.getIndex();

	/** Font color */
	static short FC_WHITE = IndexedColors.WHITE.getIndex();
	static short FC_BLACK = IndexedColors.BLACK.getIndex();
	static short FC_BLUE = 48;
	static short FC_GREEN = IndexedColors.GREEN.getIndex();
	static short FC_ORANGE = IndexedColors.ORANGE.getIndex();
	static short FC_RED = IndexedColors.RED.getIndex();
	static short FC_DARK_BLUE = IndexedColors.DARK_BLUE.getIndex();

	private Long numberDay;
	private Dealer paramDealer;
	private EDealerType paramDealerType;

	/** */
	public GLFDailyReport() {

	}

	/**
	 * @see com.nokor.efinance.tools.report.Report#generate(com.nokor.efinance.core.shared.report.ReportParameter)
	 */
	@Override
	public String generate(ReportParameter reportParameter) throws Exception {
		Map<String, Object> parameters = reportParameter.getParameters();
		Date selectDate = (Date) parameters.get("dateValue");
		paramDealer = (Dealer) parameters.get("dealer");
		paramDealerType = (EDealerType) parameters.get("dealerType");

		numberDay = DateUtils.getDiffInDaysPlusOneDay(selectDate,
				DateUtils.getDateAtBeginningOfMonth(selectDate));

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
		sheet.setColumnWidth(0, 6000);
		sheet.setColumnWidth(1, 5500);
		sheet.setColumnWidth(2, 5500);
		sheet.setColumnWidth(3, 1700);
		sheet.setColumnWidth(4, 4300);
		sheet.setColumnWidth(5, 5000);
		sheet.setColumnWidth(6, 3900);
		sheet.setColumnWidth(7, 3900);
		sheet.setColumnWidth(8, 5000);
		sheet.setColumnWidth(9, 5000);
		sheet.setColumnWidth(10, 5000);
		sheet.setColumnWidth(11, 3850);
		sheet.setColumnWidth(12, 4000);
		sheet.setColumnWidth(13, 6500);
		sheet.setColumnWidth(14, 5600);
		sheet.setColumnWidth(15, 3700);
		sheet.setColumnWidth(16, 3300);
		sheet.setColumnWidth(17, 3300);
		sheet.setColumnWidth(18, 2000);
		sheet.setColumnWidth(19, 2500);
		sheet.setColumnWidth(20, 3500);
		sheet.setZoom(9, 10);
		final PrintSetup printSetup = sheet.getPrintSetup();

		printSetup.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
		printSetup.setLandscape(true);

		printSetup.setScale((short) 60);
		sheet.setAutobreaks(true);
		sheet.setFitToPage(true);
		printSetup.setFitWidth((short) 1);
		printSetup.setFitHeight((short) 0);

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
		int iRow = 1;
		iRow = iRow + 1;
		if (selectDate == null) {
			selectDate = DateUtils.todayH00M00S00();
		} else {
			selectDate = DateUtils.getDateAtBeginningOfDay(selectDate);
		}

		BaseRestrictions<Dealer> restrictions = new BaseRestrictions<>(
				Dealer.class);
		restrictions.addAssociation("dealerAddresses", "deaAddress",
				JoinType.INNER_JOIN);
		restrictions.addAssociation("deaAddress.address", "address",
				JoinType.INNER_JOIN);
		restrictions.addAssociation("address.province", "province",
				JoinType.INNER_JOIN);
		if (paramDealer != null) {
			restrictions
					.addCriterion(Restrictions.eq("id", paramDealer.getId()));
		}
		if (paramDealerType != null) {
			restrictions.addCriterion(Restrictions.eq("dealerType",
					paramDealerType));
		}
		restrictions.addOrder(Order.asc("province.descEn"));

		List<Dealer> dealers = dailyReportService.list(restrictions);
		List<DealerReport> dealerReports = new ArrayList<>();

		if (dealers != null && !dealers.isEmpty()) {
			for (Dealer dealer : dealers) {
				dailyReportService.calculateDailyReportByDealer(dealer,
						selectDate);
				boolean includeInDailyReport = true;
				if (includeInDailyReport) {
					DealerReport dealerReport = new DealerReport();
					dealerReport.setDealer(dealer);
					BaseRestrictions<StatisticConfig> statRestrictions = new BaseRestrictions<>(
							StatisticConfig.class);
					statRestrictions.addCriterion(Restrictions.eq("dealer.id",
							dealer.getId()));
					statRestrictions.addCriterion(Restrictions.eq("startDate",
							DateUtils.getDateAtBeginningOfMonth(selectDate)));
					List<StatisticConfig> statisticConfigs = dailyReportService
							.list(statRestrictions);
					int lowTarget = 0;
					int highTarget = 0;
					if (statisticConfigs != null && !statisticConfigs.isEmpty()) {
						lowTarget = statisticConfigs.get(0).getTargetLow();
						highTarget = statisticConfigs.get(0).getTargetHigh();
					}
					dealerReport.setLowTarget(lowTarget);
					dealerReport.setHighTarget(highTarget);

					BaseRestrictions<DailyReport> dailyRestrictions = new BaseRestrictions<>(
							DailyReport.class);
					dailyRestrictions.addCriterion(Restrictions.eq("dealer.id",
							dealer.getId()));
					dailyRestrictions.addCriterion(Restrictions.ge("date",
							DateUtils.getDateAtBeginningOfMonth(selectDate)));
					dailyRestrictions.addCriterion(Restrictions.le("date",
							DateUtils.getDateAtEndOfMonth(selectDate)));

					restrictions.addOrder(Order.asc("date"));
					List<DailyReport> dailyReports = dailyReportService
							.list(dailyRestrictions);

					for (DailyReport dailyReport : dailyReports) {
						long[] numVisitors = getNumVisitor(
								dailyReport.getDealer(), dailyReport.getDate());
						dailyReport.setDealerVisitor(numVisitors[0]);
						dailyReport.setCompanyVisitor(numVisitors[1]);
						dailyReport.setCompanyApply(numVisitors[2]);
					}

					dealerReport.setDailyReports(dailyReports);

					dealerReports.add(dealerReport);
				}
			}

		}

		// Display overview table
		iRow = overviewTable(sheet, iRow, dealerReports, selectDate);

		// add space three rows
		iRow = iRow + 3;

		if (dealerReports != null && !dealerReports.isEmpty()) {

			// Display Total table
			sheet.setRowBreak(iRow);
			iRow = dataTable(sheet, iRow, getTotalDealerReport(dealerReports, selectDate), selectDate);
			// add space one row
			iRow = iRow + 1;

			// Display one by one Dealer table
			for (DealerReport dealerReport : dealerReports) {
				sheet.setRowBreak(iRow);
				iRow = dataTable(sheet, iRow, dealerReport, selectDate);
				// add space one row
				iRow = iRow + 1;

			}
		}

		String fileName = writeXLSData("Daily_Report"
				+ DateUtils.getDateLabel(DateUtils.today(), "yyyyMMddHHmmssSSS") + ".xlsx");

		return fileName;
	}

	/**
	 * @param dealerReports
	 * @return
	 */
	private DealerReport getTotalDealerReport(List<DealerReport> dealerReports,
			Date selectDate) {
		DealerReport totalDealerReport = new DealerReport();
		List<DailyReport> dailyReports = new ArrayList<>();
		for (DealerReport dealerReport : dealerReports) {
			totalDealerReport.setLowTarget(MyNumberUtils
					.getInteger(totalDealerReport.getLowTarget())
					+ MyNumberUtils.getInteger(dealerReport.getLowTarget()));
			totalDealerReport.setHighTarget(MyNumberUtils
					.getInteger(totalDealerReport.getHighTarget())
					+ MyNumberUtils.getInteger(dealerReport.getHighTarget()));
			for (DailyReport dest : dealerReport.getDailyReports()) {
				DailyReport dailyReport = getDailyReport(dailyReports, dest);
				if (dailyReport == null) {
					dailyReport = new DailyReport();
					dailyReport.setDate(dest.getDate());
					dailyReports.add(dailyReport);
				}
				dailyReport.plus(dest);
				if (DateUtils.isSameDay(selectDate, dest.getDate())) {
					dailyReport.setDealerAccumulateNewContracts(MyNumberUtils
							.getLong(dailyReport
									.getDealerAccumulateNewContracts())
							+ MyNumberUtils.getLong(dest
									.getDealerAccumulateNewContracts()));
					dailyReport.setNbDealerContractsUntilLastMonth(MyNumberUtils
							.getLong(dailyReport
									.getNbDealerContractsUntilLastMonth())
							+ MyNumberUtils.getLong(dest
									.getNbDealerContractsUntilLastMonth()));

					dailyReport
							.setNbDealerApplicationsFromBeginOfMonth(MyNumberUtils.getLong(dailyReport
									.getNbDealerApplicationsFromBeginOfMonth())
									+ MyNumberUtils.getLong(dest
											.getNbDealerApplicationsFromBeginOfMonth()));
					dailyReport
							.setNbDealerApplicationsLastMonthFromBeginToDate(MyNumberUtils.getLong(dailyReport
									.getNbDealerApplicationsLastMonthFromBeginToDate())
									+ MyNumberUtils.getLong(dest
											.getNbDealerApplicationsLastMonthFromBeginToDate()));
					dailyReport.setNbDealerContractsFromBeginOfMonth(MyNumberUtils
							.getLong(dailyReport
									.getNbDealerContractsFromBeginOfMonth())
							+ MyNumberUtils.getLong(dest
									.getNbDealerContractsFromBeginOfMonth()));
					dailyReport
							.setNbDealerContractsLastMonthFromBeginToDate(MyNumberUtils.getLong(dailyReport
									.getNbDealerContractsLastMonthFromBeginToDate())
									+ MyNumberUtils.getLong(dest
											.getNbDealerContractsLastMonthFromBeginToDate()));
					dailyReport
							.setNbDealerFieldChecksFromBeginOfMonth(MyNumberUtils.getLong(dailyReport
									.getNbDealerFieldChecksFromBeginOfMonth())
									+ MyNumberUtils.getLong(dest
											.getNbDealerFieldChecksFromBeginOfMonth()));
					dailyReport
							.setNbDealerFieldChecksLastMonthFromBeginToDate(MyNumberUtils.getLong(dailyReport
									.getNbDealerFieldChecksLastMonthFromBeginToDate())
									+ MyNumberUtils.getLong(dest
											.getNbDealerFieldChecksLastMonthFromBeginToDate()));
					dailyReport.setNbDealerVisitorsFromBeginOfMonth(MyNumberUtils
							.getLong(dailyReport
									.getNbDealerVisitorsFromBeginOfMonth())
							+ MyNumberUtils.getLong(dest
									.getNbDealerVisitorsFromBeginOfMonth()));
					dailyReport
							.setNbDealerVisitorsLastMonthFromBeginToDate(MyNumberUtils.getLong(dailyReport
									.getNbDealerVisitorsLastMonthFromBeginToDate())
									+ MyNumberUtils.getLong(dest
											.getNbDealerVisitorsLastMonthFromBeginToDate()));
				}
			}
		}
		Collections.sort(dailyReports, new DailyReportComparator());
		totalDealerReport.setDailyReports(dailyReports);
		return totalDealerReport;
	}

	/**
	 * @param dailyReports
	 * @param dest
	 * @return
	 */
	private DailyReport getDailyReport(List<DailyReport> dailyReports,
			DailyReport dest) {
		for (DailyReport dailyReport : dailyReports) {
			if (DateUtils.isSameDay(dailyReport.getDate(), dest.getDate())) {
				return dailyReport;
			}
		}
		return null;
	}

	/**
	 * @param reasonCode
	 * @param selectDate
	 * @param dealer
	 * @param dealerType
	 * @return
	 */
	private int getNbRejectByReasonCode(String reasonCode, Date selectDate,
			Dealer dealer, EDealerType dealerType) {
		BaseRestrictions<Quotation> criteria = new BaseRestrictions<>(
				Quotation.class);
		criteria.addAssociation("quotationSupportDecisions", "qsup",
				JoinType.INNER_JOIN);
		criteria.addAssociation("qsup.supportDecision", "sup",
				JoinType.INNER_JOIN);
		criteria.addAssociation("dealer", "quodeal", JoinType.INNER_JOIN);
		criteria.addCriterion(Restrictions.eq("quotationStatus",
				QuotationWkfStatus.REJ));
		criteria.addCriterion(Restrictions.ge("rejectDate", DateFilterUtil
				.getStartDate(DateUtils.getDateAtBeginningOfMonth(selectDate))));
		criteria.addCriterion(Restrictions.le("rejectDate",
				DateFilterUtil.getEndDate(selectDate)));
		criteria.addCriterion(Restrictions.eq("sup.code", reasonCode));
		if (dealer != null) {
			criteria.addCriterion(Restrictions.eq("quodeal.id", dealer.getId()));
		}
		if (dealerType != null) {
			criteria.addCriterion(Restrictions.eq("quodeal.dealerType",
					dealerType));
		}
		return (int) dailyReportService.count(criteria);
	}

	/**
	 * @param reasonCode
	 * @param selectDate
	 * @return
	 */
	private int getNbDeclineByReasonCode(String reasonCode, Date selectDate,
			Dealer dealer, EDealerType dealerType) {
		BaseRestrictions<Quotation> criteria = new BaseRestrictions<>(
				Quotation.class);
		criteria.addAssociation("quotationSupportDecisions", "qsup",
				JoinType.INNER_JOIN);
		criteria.addAssociation("qsup.supportDecision", "sup",
				JoinType.INNER_JOIN);
		criteria.addAssociation("dealer", "quodeal", JoinType.INNER_JOIN);
		criteria.addCriterion(Restrictions.eq("quotationStatus",
				QuotationWkfStatus.DEC));
		criteria.addCriterion(Restrictions.ge("changeStatusDate",
				DateFilterUtil.getStartDate(DateUtils
						.getDateAtBeginningOfMonth(selectDate))));
		criteria.addCriterion(Restrictions.le("changeStatusDate",
				DateFilterUtil.getEndDate(selectDate)));
		criteria.addCriterion(Restrictions.eq("sup.code", reasonCode));
		if (dealer != null) {
			criteria.addCriterion(Restrictions.eq("quodeal.id", dealer.getId()));
		}
		if (dealerType != null) {
			criteria.addCriterion(Restrictions.eq("quodeal.dealerType",
					dealerType));
		}
		return (int) dailyReportService.count(criteria);
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	private long getNbFieldChecksFromBeginOfMonthForDealerType(Date date, boolean isDealer) {
		Date dateBeginning = DateUtils.getDateAtBeginningOfMonth(date);
		// TODO PYI
//		BaseRestrictions<QuotationStatusHistory> restrictions = new BaseRestrictions<>(QuotationStatusHistory.class);
//		restrictions.addAssociation("quotation", "quo", JoinType.INNER_JOIN);
//		restrictions.addAssociation("quo.dealer", "quodeal", JoinType.INNER_JOIN);
//		restrictions.addCriterion(Restrictions.eq("quotationStatus", WkfQuotationStatus.RFC));
//		restrictions.addCriterion(Restrictions.ge("createDate", DateFilterUtil.getStartDate(dateBeginning)));
//		restrictions.addCriterion(Restrictions.le("createDate", DateFilterUtil.getEndDate(date)));
//		if (paramDealer != null) {
//			restrictions.addCriterion(Restrictions.eq("quodeal." + ID, paramDealer.getId()));
//		}
//		if (isDealer) {
//			restrictions.addCriterion(Restrictions.eq("quodeal.dealerType", EDealerType.DEA));
//		} else {
//			restrictions.addCriterion(Restrictions.eq("quodeal.dealerType", EDealerType.SDE));
//		}
		Map<Long, Long> results = new HashMap<>();
		// TODO PYI
//		List<QuotationStatusHistory> quotationStatusHistories = entityService.list(restrictions);
//		for (QuotationStatusHistory quotationStatusHistory : quotationStatusHistories) {
//			if (!results.containsKey(quotationStatusHistory.getQuotation().getId())) {
//				results.put(quotationStatusHistory.getQuotation().getId(), quotationStatusHistory.getId());
//			}
//		}		
		return results.size();
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	private long getNbRejectsFromBeginOfMonth(Date date) {
		Date dateBeginningOfMonth = DateUtils.getDateAtBeginningOfMonth(date);
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(Quotation.class);
		restrictions.addAssociation("dealer", "quodeal", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.eq("quotationStatus", QuotationWkfStatus.REJ));
		restrictions.addCriterion(Restrictions.ge("rejectDate", DateFilterUtil.getStartDate(dateBeginningOfMonth)));
		restrictions.addCriterion(Restrictions.le("rejectDate", DateFilterUtil.getEndDate(date)));
		if (paramDealer != null) {
			restrictions.addCriterion(Restrictions.eq("quodeal.id", paramDealer.getId()));
		}
		if (paramDealerType != null) {
			restrictions.addCriterion(Restrictions.eq("quodeal.dealerType", paramDealerType));
		}
		return dailyReportService.count(restrictions);
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	private long getNbDeclinesFromBeginOfMonth(Date date) {
		Date dateBeginningOfMonth = DateUtils.getDateAtBeginningOfMonth(date);
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(Quotation.class);
		restrictions.addAssociation("dealer", "quodeal", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.eq("quotationStatus", QuotationWkfStatus.DEC));
		restrictions.addCriterion(Restrictions.ge("changeStatusDate", DateFilterUtil.getStartDate(dateBeginningOfMonth)));
		restrictions.addCriterion(Restrictions.le("changeStatusDate", DateFilterUtil.getEndDate(date)));
		if (paramDealer != null) {
			restrictions.addCriterion(Restrictions.eq("quodeal.id", paramDealer.getId()));
		}
		if (paramDealerType != null) {
			restrictions.addCriterion(Restrictions.eq("quodeal.dealerType", paramDealerType));
		}
		return dailyReportService.count(restrictions);
	}
	
	/**
	 * 
	 * @param date
	 * @param isBefore
	 * @return
	 */
	private long getNbDeclinesBeforeAndAfterApproval(Date date, boolean isBefore) {
		Date dateBeginningOfMonth = DateUtils.getDateAtBeginningOfMonth(date);
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(Quotation.class);
		restrictions.addAssociation("dealer", "quodeal", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.eq("quotationStatus", QuotationWkfStatus.DEC));
		if (isBefore) {
			restrictions.addCriterion(Restrictions.isNull(ACCEPTATION_DATE));
		} else {
			restrictions.addCriterion(Restrictions.isNotNull(ACCEPTATION_DATE));
		}
		restrictions.addCriterion(Restrictions.ge("changeStatusDate", DateFilterUtil.getStartDate(dateBeginningOfMonth)));
		restrictions.addCriterion(Restrictions.le("changeStatusDate", DateFilterUtil.getEndDate(date)));
		if (paramDealer != null) {
			restrictions.addCriterion(Restrictions.eq("quodeal.id", paramDealer.getId()));
		}
		if (paramDealerType != null) {
			restrictions.addCriterion(Restrictions.eq("quodeal.dealerType", paramDealerType));
		}
		return dailyReportService.count(restrictions);
	}

	/**
	 * @param sheet
	 * @param iRow
	 * @param style
	 * @param dealerReport
	 * @param selectDate
	 * @param isTotal
	 * @return
	 * @throws Exception
	 */
	private int dataTable(final Sheet sheet, int iRow,
			DealerReport dealerReport, Date selectDate) throws Exception {
		String month = DateUtils.getDateLabel(selectDate, "MMMM");
		Row headerRow = sheet.createRow(iRow + 1);

		Dealer dealer = dealerReport.getDealer();
		iRow++;
		Drawing drawing = sheet.createDrawingPatriarch();
		ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, iRow,
				iRow + 2, iRow);
		String templatePath = AppConfig.getInstance().getConfiguration()
				.getString("specific.templatedir");
		String templateFileName = templatePath + "/GLF-logo-small.png";
		File image = new File(templateFileName);
		InputStream fStream = new FileInputStream(image);
		byte[] bytes = IOUtils.toByteArray(fStream);
		int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
		Picture picture = drawing.createPicture(anchor, pictureIdx);
		picture.resize();
		fStream.close();

		DailyReport selectDailyReport = dealerReport
				.getDailyReportSelectDate(selectDate);

		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, 0, 20));
		for (int j = 0; j <= 19; j++) {
			createCell(headerRow, j, "", 14, false, false, true,
					CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, false);
		}
		createCell(headerRow, 20, "", 14, true, false, true,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, false);

		headerRow = sheet.createRow(iRow);
		sheet.addMergedRegion(new CellRangeAddress(iRow, iRow, 0, 20));
		iRow = iRow + 1;
		createCell(headerRow, 0,
				(dealer == null ? "Total" : dealer.getNameEn()), 18, false,
				false, false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
				false);
		createCell(headerRow, 20, "", 14, false, false, false,
				CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK, false);

		headerRow = sheet.createRow(iRow);
		sheet.addMergedRegion(new CellRangeAddress(iRow, iRow, 0, 20));
		iRow = iRow + 1;
		createCell(
				headerRow,
				0,
				"DAILY REPORT " + month + " "
						+ DateUtils.getDateLabel(selectDate, "yyyy"), 16,
				false, false, false, CellStyle.ALIGN_CENTER, true, BG_WHITE,
				FC_BLACK, false);
		createCell(headerRow, 20, "", 14, false, false, false,
				CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK, false);

		headerRow = sheet.createRow(iRow);
		sheet.addMergedRegion(new CellRangeAddress(iRow, iRow, 0, 20));
		iRow = iRow + 1;
		createCell(
				headerRow,
				0,
				"LOW TARGET  : "
						+ (dealerReport.getLowTarget() == 0 ? "" : dealerReport
								.getLowTarget()), 15, false, false, false,
				CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK, false);
		createCell(headerRow, 20, "", 14, false, false, false,
				CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK, false);

		headerRow = sheet.createRow(iRow);
		sheet.addMergedRegion(new CellRangeAddress(iRow, iRow, 0, 18));
		iRow = iRow + 1;
		createCell(headerRow, 0,
				"HIGH TARGET : "
						+ (dealerReport.getHighTarget() == 0 ? ""
								: dealerReport.getHighTarget()), 15, false,
				false, false, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK,
				false);
		createCell(headerRow, 20, "", 14, false, false, false,
				CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK, false);

		headerRow = sheet.createRow(iRow);
		headerRow.setHeight((short) 550);

		sheet.addMergedRegion(new CellRangeAddress(iRow, iRow, 0, 1));
		createCell(headerRow, 0, "Honda Visitors ", 14, false, false, false,
				CellStyle.ALIGN_RIGHT, true, BG_GREEN, FC_WHITE, false);
		createCell(
				headerRow,
				2,
				roundDouble(selectDailyReport.getDealerVisitorsPercentage()),
				15, false, false, false, CellStyle.ALIGN_CENTER, true,
				BG_GREEN, FC_WHITE, false);
		createCell(headerRow, 3, "", 10, false, false, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_WHITE, false);

		sheet.addMergedRegion(new CellRangeAddress(iRow, iRow, 4, 5));
		createCell(headerRow, 4, "Applications", 14, false, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE, false);
		createCell(headerRow, 6,
				roundDouble(selectDailyReport.getApplicationsPercentage()),
				15, false, false, false, CellStyle.ALIGN_CENTER, true,
				BG_GREEN, FC_WHITE, false);
		createCell(headerRow, 7, "", 10, false, false, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_WHITE, false);

		sheet.addMergedRegion(new CellRangeAddress(iRow, iRow, 8, 9));
		createCell(headerRow, 8, "Field Check", 14, false, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE, false);
		createCell(headerRow, 10,
				roundDouble(selectDailyReport.getFieldChecksPercentage()),
				15, false, false, false, CellStyle.ALIGN_CENTER, true,
				BG_GREEN, FC_WHITE, false);
		createCell(headerRow, 11, "", 10, false, false, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_WHITE, false);

		sheet.addMergedRegion(new CellRangeAddress(iRow, iRow, 12, 13));
		createCell(headerRow, 12, "Contracts ", 14, false, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE, false);
		createCell(headerRow, 14,
				roundDouble(selectDailyReport.getContractsPercentage()), 15,
				false, false, false, CellStyle.ALIGN_CENTER, true, BG_GREEN,
				FC_WHITE, false);
		createCell(headerRow, 15, "", 10, false, false, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_WHITE, false);

		sheet.addMergedRegion(new CellRangeAddress(iRow, iRow, 16, 19));
		createCell(headerRow, 16, "Accumulated New Contracts ", 15, false,
				false, false, CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE,
				true);
		createCell(headerRow, 17, "", 10, false, false, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_WHITE, false);
		createCell(headerRow, 18, "", 10, false, false, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_WHITE, false);
		createCell(headerRow, 19, "", 10, false, false, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_WHITE, false);
		createCell(headerRow, 20,
				selectDailyReport.getDealerAccumulateNewContracts(), 15, false,
				false, false, CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE,
				false);

		iRow = iRow + 1;
		headerRow = sheet.createRow(iRow);
		sheet.addMergedRegion(new CellRangeAddress(iRow, iRow, 0, 7));
		sheet.addMergedRegion(new CellRangeAddress(iRow, iRow, 8, 9));
		createCell(headerRow, 8, "Number of Field Check", 14, false, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE, false);
		createCell(headerRow, 10, selectDailyReport.getNbDealerFieldChecksFromBeginOfMonth(),
				15, false, false, false, CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE, false);
		sheet.addMergedRegion(new CellRangeAddress(iRow, iRow, 11, 15));
		createCell(headerRow, 15, "", 14, false, false, false,
				CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK, false);
		sheet.addMergedRegion(new CellRangeAddress(iRow, iRow, 16, 19));
		createCell(headerRow, 16, "End of previous month ", 15, false, false,
				false, CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE, true);
		createCell(headerRow, 17, "", 14, false, false, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_WHITE, false);
		createCell(headerRow, 18, "", 14, false, false, false,
				CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK, false);
		createCell(headerRow, 19, "", 14, false, false, false,
				CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK, false);
		createCell(headerRow, 20,
				selectDailyReport.getNbDealerContractsUntilLastMonth(), 15,
				false, false, false, CellStyle.ALIGN_CENTER, true, BG_GREEN,
				FC_WHITE, false);
		iRow = iRow + 1;

		int iCol = 0;
		Row tmpRow = sheet.createRow(iRow++);
		createCell(tmpRow, iCol++, DateUtils.getDateLabel(selectDate, "yyyy"),
				16, false, false, false, CellStyle.ALIGN_CENTER, true,
				BG_WHITE, FC_BLACK, false);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow, iCol - 1,
				iCol - 1));

		createCell(tmpRow, 1, "SALES", 14, false, true, true,
				CellStyle.ALIGN_CENTER, true, BG_LIGHT_BLUE, FC_WHITE, false);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, 1, 7));
		createCell(tmpRow, 2, "", 10, false, true, true,
				CellStyle.ALIGN_CENTER, true, BG_LIGHT_BLUE, FC_BLACK, false);
		createCell(tmpRow, 3, "", 10, false, true, true,
				CellStyle.ALIGN_CENTER, true, BG_LIGHT_BLUE, FC_BLACK, false);
		createCell(tmpRow, 4, "", 10, false, true, true,
				CellStyle.ALIGN_CENTER, true, BG_LIGHT_BLUE, FC_BLACK, false);
		createCell(tmpRow, 5, "", 10, false, true, true,
				CellStyle.ALIGN_CENTER, true, BG_LIGHT_BLUE, FC_BLACK, false);
		createCell(tmpRow, 6, "", 10, false, true, true,
				CellStyle.ALIGN_CENTER, true, BG_LIGHT_BLUE, FC_BLACK, false);
		createCell(tmpRow, 7, "", 10, false, true, true,
				CellStyle.ALIGN_CENTER, true, BG_LIGHT_BLUE, FC_BLACK, false);

		createCell(tmpRow, 8, "UNDERWRITING", 14, false, true, true,
				CellStyle.ALIGN_CENTER, true, BG_BLUE, FC_WHITE, false);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, 8, 13));
		createCell(tmpRow, 9, "", 10, false, true, true,
				CellStyle.ALIGN_CENTER, true, BG_BLUE, FC_BLACK, false);
		createCell(tmpRow, 10, "", 10, false, true, true,
				CellStyle.ALIGN_CENTER, true, BG_BLUE, FC_BLACK, false);
		createCell(tmpRow, 11, "", 10, false, true, true,
				CellStyle.ALIGN_CENTER, true, BG_BLUE, FC_BLACK, false);
		createCell(tmpRow, 12, "", 10, false, true, true,
				CellStyle.ALIGN_CENTER, true, BG_BLUE, FC_BLACK, false);
		createCell(tmpRow, 13, "", 10, false, true, true,
				CellStyle.ALIGN_CENTER, true, BG_BLUE, FC_BLACK, false);

		createCell(tmpRow, 14, "OPERATION", 14, false, true, true,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE, false);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, 14, 20));
		createCell(tmpRow, 15, "", 10, false, true, true,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_BLACK, false);
		createCell(tmpRow, 16, "", 10, false, true, true,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_BLACK, false);
		createCell(tmpRow, 17, "", 10, false, true, true,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_BLACK, false);
		createCell(tmpRow, 18, "", 10, false, true, true,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_BLACK, false);
		createCell(tmpRow, 19, "", 10, false, true, true,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_BLACK, false);
		createCell(tmpRow, 20, "", 10, false, true, true,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_BLACK, false);

		iCol = 1;
		tmpRow = sheet.createRow(iRow++);
		int iColBorder = 0;
		createCellBorderBottomAndRight(tmpRow, iColBorder++, "");

		createCell(tmpRow, iCol++, "Honda Visitors", 14, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
		createCell(tmpRow, iCol++, "GLF Visitors", 14, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);

		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, 3, 4));
		createCell(tmpRow, iCol++, "GLF Visitors / Honda Visitors", 14, false,
				true, false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
				true);
		createCell(tmpRow, iCol++, "", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);

		createCell(tmpRow, iCol++, "Apply", 14, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);

		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, 6, 7));
		createCell(tmpRow, iCol++, "Apply / GLF Visitors", 14, false, true,
				false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
		createCell(tmpRow, iCol++, "", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);

		createCell(tmpRow, iCol++, "In Process at PoS", 14, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
		createCell(tmpRow, iCol++, "In Process at UW", 14, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
		createCell(tmpRow, iCol++, "Reject", 12, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);

		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, 11, 12));
		createCell(tmpRow, iCol++, "Reject / Application", 14, false, true,
				false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
		createCell(tmpRow, iCol++, "", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);

		createCell(tmpRow, iCol++, "Approve", 14, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);

		createCell(tmpRow, iCol++, "Pending New Contract", 14, false, true,
				false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
		createCell(tmpRow, iCol++, "Pending PO", 14, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);

		createCell(tmpRow, iCol++, "Decline", 14, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);

		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, 17, 18));
		createCell(tmpRow, iCol++, "Decline / Approve", 14, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
		createCell(tmpRow, iCol++, "", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, 19, 20));
		createCell(tmpRow, iCol++, "New Contract", 14, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
		createCell(tmpRow, iCol++, "", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);

		List<DailyReport> dailyReports = dealerReport.getDailyReports();
		dailyReports.add(dealerReport.getTotalDailyReport());
		logger.info("----------One dealer table report part--------------------");
		if (dailyReports != null && !dailyReports.isEmpty()) {
			DailyReport totalDailyReport = getDefaultDailyReport();
			for (int index = 0; index < numberDay; index++) {
				int nbDay = index;
				nbDay++;
				logger.info("Day : " + nbDay + " / Total : " + numberDay);
				Date date = (DateUtils.addDaysDate(
						DateUtils.getDateAtBeginningOfMonth(selectDate), index));
				DailyReport dailyReport = getDailyReport(dailyReports, date);

				mergedCellRow(sheet, iRow);
				tmpRow = sheet.createRow(iRow++);
				iCol = 0;

				if (dailyReport != null) {
					if (dailyReport.getDate() != null) {
						/*
						 * createNumericCellLeftBorderAlignLeft(tmpRow, iCol++,
						 * DateUtils.formatDate(dailyReport.getDate(),
						 * DEFAULT_DATE_FORMAT));
						 */
						createNumericCell(tmpRow, iCol++, DateUtils.formatDate(
								dailyReport.getDate(), DEFAULT_DATE_FORMAT),
								LEFT_BORDER_ALIGN_LEFT, false);
						
						createRowDataPerDay(tmpRow, iCol++, dailyReport, false);
						totalDailyReport.plus(dailyReport);
					}
				} else {
					// out put zero
					rowOutputZero(tmpRow, iCol++, date);
				}
			}
			logger.info("--------End One dealer table report part-----------------------");
			mergedCellRow(sheet, iRow);
			tmpRow = sheet.createRow(iRow++);
			iCol = 0;
			createNumericCell(tmpRow, iCol++, "Total-Average",TOP_LEFT_BOTTOM_BORDER_ALIGN_LEFT, false);
			Long paddingNewContract = totalDailyReport.getPendingNewContract() == null ? 0 : totalDailyReport.getPendingNewContract();
			Long pendingPurchaseOrder = totalDailyReport.getPendingPurchaseOrder() == null ? 0 :  totalDailyReport.getPendingPurchaseOrder();
			
			totalDailyReport.setInProcessAtPoS(roundLong((double) totalDailyReport.getInProcessAtPoS() / numberDay));
			totalDailyReport.setInProcessAtUW(roundLong((double) totalDailyReport.getInProcessAtUW() / numberDay));
			totalDailyReport.setPendingNewContract(roundLong((double) paddingNewContract / numberDay));
			totalDailyReport.setPendingPurchaseOrder(roundLong((double) pendingPurchaseOrder / numberDay));
			
			createRowDataPerDay(tmpRow, iCol++, totalDailyReport, true);
		}

		iRow = iRow + 4;
		return iRow;

	}

	/**
	 * 
	 * @param tmpRow
	 * @param iCol
	 * @param date
	 */
	private void rowOutputZero(final Row tmpRow, int iCol, Date date) {
		createNumericCell(tmpRow, iCol++,DateUtils.formatDate(date, DEFAULT_DATE_FORMAT),LEFT_BORDER_ALIGN_LEFT, false);
		createNumericCell(tmpRow, iCol++, 0, NONE_BORDER_ALIGN_RIGHT, false);
		createNumericCell(tmpRow, iCol++, 0, NONE_BORDER_ALIGN_RIGHT, false);
		createNumericCell(tmpRow, iCol++, 0, NONE_BORDER_ALIGN_RIGHT_PERCENTAGE, false);
		createNumericCell(tmpRow, iCol++, "", NONE_BORDER_ALIGN_RIGHT_PERCENTAGE, false);
		createNumericCell(tmpRow, iCol++, 0, NONE_BORDER_ALIGN_RIGHT, false);

		createNumericCell(tmpRow, iCol++, 0, NONE_BORDER_ALIGN_RIGHT_PERCENTAGE, false);
		createNumericCell(tmpRow, iCol++, "", NONE_BORDER_ALIGN_RIGHT_PERCENTAGE, false);
		createNumericCell(tmpRow, iCol++, 0, NONE_BORDER_ALIGN_RIGHT, false);
		createNumericCell(tmpRow, iCol++, 0, NONE_BORDER_ALIGN_RIGHT, false);
		createNumericCell(tmpRow, iCol++, 0, NONE_BORDER_ALIGN_RIGHT, false);
		createNumericCell(tmpRow, iCol++, 0, NONE_BORDER_ALIGN_RIGHT_PERCENTAGE, false);
		createNumericCell(tmpRow, iCol++, "", NONE_BORDER_ALIGN_RIGHT_PERCENTAGE, false);
		createNumericCell(tmpRow, iCol++, 0, NONE_BORDER_ALIGN_RIGHT, false);

		createNumericCell(tmpRow, iCol++, 0, NONE_BORDER_ALIGN_RIGHT, false);
		createNumericCell(tmpRow, iCol++, 0, NONE_BORDER_ALIGN_RIGHT, false);
		createNumericCell(tmpRow, iCol++, 0, NONE_BORDER_ALIGN_RIGHT, false);

		createNumericCell(tmpRow, iCol++, 0, NONE_BORDER_ALIGN_RIGHT_PERCENTAGE, false);
		createNumericCell(tmpRow, iCol++, "", NONE_BORDER_ALIGN_RIGHT_PERCENTAGE, false);
		createNumericCell(tmpRow, iCol++, 0, NONE_BORDER_ALIGN_RIGHT, false);
		createNumericCell(tmpRow, iCol++, "", RIGHT_BORDER, false);
	}

	/**
	 * 
	 * @param sheet
	 * @param iRow
	 */
	private void mergedCellRow(final Sheet sheet, int iRow) {
		sheet.addMergedRegion(new CellRangeAddress(iRow, iRow, 3, 4));
		sheet.addMergedRegion(new CellRangeAddress(iRow, iRow, 6, 7));
		sheet.addMergedRegion(new CellRangeAddress(iRow, iRow, 11, 12));
		sheet.addMergedRegion(new CellRangeAddress(iRow, iRow, 17, 18));
		sheet.addMergedRegion(new CellRangeAddress(iRow, iRow, 19, 20));
	}

	/**
	 * 
	 * @return
	 */
	private DailyReport getDefaultDailyReport() {
		DailyReport d = new DailyReport();
		d.setDealerVisitor(0l);
		d.setCompanyVisitor(0l);
		d.setApply(0l);
		d.setInProcessAtPoS(0l);
		d.setInProcessAtUW(0l);
		d.setReject(0l);
		d.setApprove(0l);
		d.setPendingNewContract(0l);
		d.setPendingPurchaseOrder(0l);
		d.setDecline(0l);
		d.setNewContract(0l);
		return d;
	}

	/**
	 * 
	 * @param dailyReports
	 * @param date
	 * @return
	 */
	private DailyReport getDailyReport(List<DailyReport> dailyReports, Date date) {
		if (dailyReports != null && !dailyReports.isEmpty()) {
			for (DailyReport dailyReport : dailyReports) {
				if (dailyReport.getDate() != null
						&& DateUtils.isSameDay(
								DateUtils.getDateAtBeginningOfDay(dailyReport
										.getDate()), date)) {
					return dailyReport;
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param tmpRow
	 * @param iCol
	 * @param dailyReport
	 */
	private void createRowDataPerDay(Row tmpRow, int iCol,
			DailyReport dailyReport, boolean isTotalRow) {
		createNumericCell(tmpRow, iCol++,MyNumberUtils.getLong(dailyReport.getDealerVisitor()),NONE_BORDER_ALIGN_RIGHT, isTotalRow);
		createNumericCell(tmpRow, iCol++,MyNumberUtils.getLong(dailyReport.getCompanyVisitor()),NONE_BORDER_ALIGN_RIGHT, isTotalRow);
		double percentageCompany = 0;
		if (MyNumberUtils.getLong(dailyReport.getDealerVisitor()) > 0) {
			percentageCompany = MyNumberUtils.getLong(dailyReport.getCompanyVisitor())* 100/ (double) MyNumberUtils.getLong(dailyReport.getDealerVisitor());
		}
		createNumericCell(tmpRow, iCol++, roundDouble(percentageCompany),NONE_BORDER_ALIGN_RIGHT_PERCENTAGE, isTotalRow);
		createNumericCell(tmpRow, iCol++, "", NONE_BORDER_ALIGN_RIGHT_PERCENTAGE,isTotalRow);
		createNumericCell(tmpRow, iCol++,MyNumberUtils.getLong(dailyReport.getApply()),NONE_BORDER_ALIGN_RIGHT, isTotalRow);
		double percentageApply = 0;
		if (MyNumberUtils.getLong(dailyReport.getCompanyVisitor()) > 0) {
			percentageApply = MyNumberUtils.getLong(dailyReport.getApply())* 100/ (double) MyNumberUtils.getLong(dailyReport.getCompanyVisitor());
		}
		createNumericCell(tmpRow, iCol++, roundDouble(percentageApply),NONE_BORDER_ALIGN_RIGHT_PERCENTAGE, isTotalRow);
		createNumericCell(tmpRow, iCol++, "", NONE_BORDER_ALIGN_RIGHT_PERCENTAGE,isTotalRow);
		createNumericCell(tmpRow, iCol++,MyNumberUtils.getLong(dailyReport.getInProcessAtPoS()),NONE_BORDER_ALIGN_RIGHT, isTotalRow);
		createNumericCell(tmpRow, iCol++,MyNumberUtils.getLong(dailyReport.getInProcessAtUW()),NONE_BORDER_ALIGN_RIGHT, isTotalRow);
		createNumericCell(tmpRow, iCol++,MyNumberUtils.getLong(dailyReport.getReject()),NONE_BORDER_ALIGN_RIGHT, isTotalRow);
		double percentageReject = 0;
		if (MyNumberUtils.getLong(dailyReport.getApply()) > 0) {
			percentageReject = MyNumberUtils.getLong(dailyReport.getReject()) * 100/ (double) MyNumberUtils.getLong(dailyReport.getApply());
		}
		createNumericCell(tmpRow, iCol++, roundDouble(percentageReject),NONE_BORDER_ALIGN_RIGHT_PERCENTAGE, isTotalRow);
		createNumericCell(tmpRow, iCol++, "", NONE_BORDER_ALIGN_RIGHT_PERCENTAGE,isTotalRow);
		createNumericCell(tmpRow, iCol++,MyNumberUtils.getLong(dailyReport.getApprove()),NONE_BORDER_ALIGN_RIGHT, isTotalRow);
		createNumericCell(tmpRow, iCol++,MyNumberUtils.getLong(dailyReport.getPendingNewContract()),NONE_BORDER_ALIGN_RIGHT, isTotalRow);
		createNumericCell(tmpRow, iCol++,MyNumberUtils.getLong(dailyReport.getPendingPurchaseOrder()), NONE_BORDER_ALIGN_RIGHT,isTotalRow);
		createNumericCell(tmpRow, iCol++,MyNumberUtils.getLong(dailyReport.getDecline()),NONE_BORDER_ALIGN_RIGHT, isTotalRow);
		double percentageApprov = 0;
		if (MyNumberUtils.getLong(dailyReport.getApprove()) > 0) {
			percentageApprov = MyNumberUtils.getLong(dailyReport.getDecline())* 100/ (double) MyNumberUtils.getLong(dailyReport.getApprove());
		}
		createNumericCell(tmpRow, iCol++, roundDouble(percentageApprov),NONE_BORDER_ALIGN_RIGHT_PERCENTAGE, isTotalRow);
		createNumericCell(tmpRow, iCol++, "", NONE_BORDER_ALIGN_RIGHT_PERCENTAGE,isTotalRow);
		createNumericCell(tmpRow, iCol++,MyNumberUtils.getLong(dailyReport.getNewContract()),NONE_BORDER_ALIGN_RIGHT, isTotalRow);
		createNumericCell(tmpRow, iCol++, "", RIGHT_BORDER, isTotalRow);
	}

	/**
	 * @param sheet
	 * @param iRow
	 * @param style
	 * @param dealers
	 * @param selectDate
	 * @return
	 * @throws Exception
	 */
	protected int overviewTable(final Sheet sheet, int iRow,
			List<DealerReport> dealerReports, Date selectDate) throws Exception {
		/* Create total data header */
		String month = DateUtils.getDateLabel(selectDate, "MMMM");
		Row headerRow = sheet.createRow(iRow - 1);

		Drawing drawing = sheet.createDrawingPatriarch();
		ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, iRow,iRow + 2, iRow);
		String templatePath = AppConfig.getInstance().getConfiguration().getString("specific.templatedir");
		String templateFileName = templatePath + "/GLF-logo-small.png";
		File image = new File(templateFileName);
		InputStream fStream = new FileInputStream(image);
		byte[] bytes = IOUtils.toByteArray(fStream);
		int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
		Picture picture = drawing.createPicture(anchor, pictureIdx);
		picture.resize();
		fStream.close();

		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, 0, 15));
		for (int j = 0; j <= 14; j++) {
			createCell(headerRow, j, "", 14, false, false, false,CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, false);
		}
		createCell(headerRow, 15, "", 14, false, false, false,CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, false);

		headerRow = sheet.createRow(iRow);
		sheet.addMergedRegion(new CellRangeAddress(iRow, iRow, 0, 15));
		iRow = iRow + 1;
		createCell(headerRow, 0, "OVERVIEW", 18, false, false, false,CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, false);
		createCell(headerRow, 15, "", 14, false, false, false,CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK, false);

		headerRow = sheet.createRow(iRow);
		sheet.addMergedRegion(new CellRangeAddress(iRow, iRow, 0, 15));
		iRow = iRow + 1;
		createCell(headerRow,0,"DAILY REPORT " + month + " "+ DateUtils.getDateLabel(selectDate, "yyyy"), 16,false, false, false, 
				CellStyle.ALIGN_CENTER, true, BG_WHITE,FC_BLACK, false);
		createCell(headerRow, 15, "", 14, false, false, false,
				CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK, false);

		iRow = iRow + 1;
		headerRow = sheet.createRow(iRow - 1);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, 0, 15));
		createCell(headerRow, 15, "", 14, false, false, false,
				CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK, false);

		int iCol = 0;
		Row tmpRow = sheet.createRow(iRow++);
		createCell(tmpRow, iCol++, "", 9, false, false, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_WHITE, true);
		createCell(tmpRow, iCol++, "", 9, false, false, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_WHITE, true);
		createCell(tmpRow, iCol++, "", 9, false, false, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_WHITE, true);
		createCell(tmpRow, iCol++, "", 9, false, false, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_WHITE, true);
		createCell(tmpRow, iCol++, "Applications.", 14, false, false, false,
				CellStyle.ALIGN_CENTER, true, BG_BLUE, FC_WHITE, true);
		createCell(tmpRow, iCol++, "Contracts", 14, false, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE, true);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, iCol,
				iCol + 1));
		createCell(tmpRow, iCol++, " Contracts / Applications.", 14, false,
				false, false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
				true);
		createCell(tmpRow, iCol++, "", 9, false, false, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_WHITE, true);
		createCell(tmpRow, iCol++, "%Low Target", 14, false, false, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
		createCell(tmpRow, iCol++, "%High Target", 14, false, false, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
		createCell(tmpRow, iCol++, "Low Target", 14, false, false, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
		createCell(tmpRow, iCol++, "High Target", 14, false, false, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
		createCell(tmpRow, iCol++, "", 9, false, false, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_WHITE, true);
		createCell(tmpRow, iCol++, "", 9, false, false, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_WHITE, true);
		createCell(tmpRow, iCol++, "", 9, false, false, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_WHITE, true);
		createCell(tmpRow, iCol++, "", 9, false, false, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_WHITE, true);

		int[][] numApplicationsByTermAndPercentage = getNumApplicationsByTerm(selectDate, paramDealer, paramDealerType);
		int[] numApplicationsByTerm = numApplicationsByTermAndPercentage[0];
		int[] numApplicationsByPercentage = numApplicationsByTermAndPercentage[1];

		int[][] numApplicationsOfMonthByTermAndPercentage = getDetailNumApplicationsOfMonthByTerm(selectDate, paramDealer, paramDealerType);
		int[] numApplicationsOfMonthByTerm = numApplicationsOfMonthByTermAndPercentage[0];
		int[] numApplicationsOfMonthByPercentage = numApplicationsOfMonthByTermAndPercentage[1];
				
		double advance10Percentage = 0d;
		double advance20Percentage = 0d;
		double advance30Percentage = 0d;
		double advance40Percentage = 0d;
		double advance50Percentage = 0d;
		double advance60Percentage = 0d;
		double advance70Percentage = 0d;
		double totalOfAdvanPaymentPercentage = 0d;
		int totalOfAdvancePayment = 0;
		
		double term12Percentage = 0d;
		double term24Percentage = 0d;
		double term36Percentage = 0d;
		double term48Percentage = 0d;
		double term60Percentage = 0d;
		double totalOfTermPercentage = 0d;
		int totalOfTerm = 0;
		
		int numOfMonthByTermAndAdvance = numApplicationsOfMonthByTermAndPercentage[2][0];
		
		double[] advancePaymentPercentage = {advance10Percentage, advance20Percentage, advance30Percentage, advance40Percentage,
											 advance50Percentage, advance60Percentage, advance70Percentage};
		double[] termPercentage = {term12Percentage, term24Percentage, term36Percentage, term48Percentage, term60Percentage};
		
		for (int i = 0; i <= 6; i++) {
			advancePaymentPercentage[i] = calculatePercentageForAdvanceAndTerm(numApplicationsOfMonthByPercentage[i], 
					numOfMonthByTermAndAdvance);
			totalOfAdvancePayment += numApplicationsOfMonthByPercentage[i];
		}
		totalOfAdvanPaymentPercentage = calculatePercentageForAdvanceAndTerm(totalOfAdvancePayment, numOfMonthByTermAndAdvance);
		for (int i = 0; i <= 4; i++) {
			termPercentage[i] = calculatePercentageForAdvanceAndTerm(numApplicationsOfMonthByTerm[i], numOfMonthByTermAndAdvance);
			totalOfTerm += numApplicationsOfMonthByTerm[i];
		}
		totalOfTermPercentage = calculatePercentageForAdvanceAndTerm(totalOfTerm, numOfMonthByTermAndAdvance);
				
		iCol = 0;
		if (dealerReports != null && !dealerReports.isEmpty()) {
			//int nbRows = dealerReports.size() > 50 ? dealerReports.size() : 51;
			int nbRows = dealerReports.size() > 55 ? dealerReports.size() : 56;
			int totaltargetLow = 0;
			int totaltargetHigh = 0;
			int totalNbApplications = 0;
			int totalNbContracts = 0;

			double totalRejects = getNbRejectsFromBeginOfMonth(selectDate);
			double totalDeclines = getNbDeclinesFromBeginOfMonth(selectDate);
			long totalNumRFCForTypeDealer = getNbFieldChecksFromBeginOfMonthForDealerType(selectDate, true);
			long totalNumRFCForTypeSubDealer = getNbFieldChecksFromBeginOfMonthForDealerType(selectDate, false);
			
			long totalDeclineBeforeApproval = getNbDeclinesBeforeAndAfterApproval(selectDate, true);
			long totalDeclineAfterApproval = getNbDeclinesBeforeAndAfterApproval(selectDate, false);
		
			int totalIRow = iRow + 1;
			Row totalRow = null;
			int accumlateIRow = 0;
			Row accumlateRow = null;
			logger.info("----------------Overview table report part--------------------");
			logger.info("Total dealer : " + dealerReports.size());
			for (int i = 0; i < nbRows; i++) {			
				tmpRow = sheet.createRow(iRow++);
				iCol = 0;
				if (dealerReports.size() < 56 && i == dealerReports.size()) {
					totalRow = tmpRow;
				}
				if (i < dealerReports.size()) {
					DealerReport dealerReport = dealerReports.get(i);
					Dealer dealer = dealerReport.getDealer();
					int targetLow = dealerReport.getLowTarget();
					int targetHigh = dealerReport.getHighTarget();
					totaltargetLow += targetLow;
					totaltargetHigh += targetHigh;
					DailyReport selectDailyReport = dealerReport.getSelectDailyReport(selectDate);
					long nbContracts = selectDailyReport.getNewContract() == null ? 0 : selectDailyReport.getNewContract();
					long nbApplications = selectDailyReport.getApply() == null ? 0 : selectDailyReport.getApply();
					totalNbApplications += nbApplications;
					totalNbContracts += nbContracts;

					createCell(tmpRow, iCol++,dealer.getDealerAddresses().get(0).getAddress().getProvince().getDescEn(), 13, false,false, false, 
							CellStyle.ALIGN_RIGHT, true,BG_WHITE, FC_BLACK, false);
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,iRow - 1, iCol, iCol + 2));
					createCell(tmpRow, iCol++, dealer.getNameEn(), 14, false,false, false, CellStyle.ALIGN_LEFT, true, BG_WHITE,FC_BLACK, false);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,true);
					createCell(tmpRow, iCol++, nbApplications, 15, false,false, false, 
							CellStyle.ALIGN_CENTER, true,BG_WHITE, FC_BLACK, true);
					createCell(tmpRow, iCol++, nbContracts, 15, false, false,false, 
							CellStyle.ALIGN_CENTER, true, BG_WHITE,FC_BLACK, true);
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,iRow - 1, iCol, iCol + 1));
					createCell(tmpRow, iCol++,(nbApplications > 0 ? roundDouble((100 * nbContracts)/ nbApplications) : 0d), 15, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,true);
					createCell(tmpRow, iCol++,(targetLow > 0 ? roundDouble((100 * nbContracts) / targetLow) : 0d), 15, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,true);
					createCell(tmpRow, iCol++,(targetHigh > 0 ? roundDouble((100 * nbContracts) / targetHigh) : 0d), 15, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,true);
					createCell(tmpRow, iCol++, targetLow, 15, false, false,false, 
							CellStyle.ALIGN_CENTER, true, BG_WHITE,FC_BLACK, true);
					createCell(tmpRow, iCol++, targetHigh, 15, false, false,false, 
							CellStyle.ALIGN_CENTER, true, BG_WHITE,FC_BLACK, true);
				}
				iCol = 12;
				if (i == 0) {
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,iRow - 1, 12, 14));
					createCell(tmpRow, iCol++, "APPLICATIONS", 14, false,false, false, 
							CellStyle.ALIGN_RIGHT, false,BG_WHITE, FC_BLUE, true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLUE,true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLUE,true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_GREEN,true);
				} else if (i == 1) {
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,iRow - 1, 12, 14));
					createCell(tmpRow, iCol++, "AVERAGE ALL POS", 14, false,false, false, 
							CellStyle.ALIGN_RIGHT, true, BG_GREY,FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,true);
					createCell(tmpRow, iCol++,getAverageApplications(selectDate, dealerReports),15, false, true, false, 
							CellStyle.ALIGN_CENTER,true, BG_WHITE, FC_BLACK, true);
				} else if (i == 2) {
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,iRow - 1, 12, 14));
					createCell(tmpRow, iCol++, "BEST PERFORMING", 14, false,false, false, 
							CellStyle.ALIGN_RIGHT, true, BG_GREY,FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,
							true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,true);
					long bestPerformingApplications = getBestPerformingApplications(selectDate, dealerReports);
					createCell(tmpRow, iCol++, bestPerformingApplications, 15,false, true, false, 
							CellStyle.ALIGN_CENTER, true,BG_WHITE, FC_BLACK, true);

					createCell(tmpRow, iCol++, getBestPerformingApplicationsPOS(bestPerformingApplications, selectDate,dealerReports), 14, false, true, false, 
							CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK, false);
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,iRow - 1, 16, 18));
					createCell(tmpRow, iCol++, "", 9, false, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,true);
					createCell(tmpRow, iCol++, "", 9, false, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,true);
				} else if (i == 3) {
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,iRow - 1, 12, 14));
					createCell(tmpRow, iCol++, "WORSE PERFORMING", 14, false,false, false, 
							CellStyle.ALIGN_RIGHT, true, BG_GREY,FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,true);
					long worsePerformingApplications = getWorsePerformingApplications(
							selectDate, dealerReports);
					createCell(tmpRow, iCol++, worsePerformingApplications, 15,false, true, false, 
							CellStyle.ALIGN_CENTER, true,BG_WHITE, FC_BLACK, true);
					
					createCell(tmpRow, iCol++, getWorsePerformingApplicationsPOS(worsePerformingApplications, selectDate,dealerReports), 14, false, true, false, 
							CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK, false);
					
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,iRow - 1, 16, 18));
					createCell(tmpRow, iCol++, "", 9, false, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,true);
					createCell(tmpRow, iCol++, "", 9, false, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,true);
				} else if (i == 4) {
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,iRow - 1, 12, 14));
					createCell(tmpRow, iCol++, "CONTRACTS", 14, false, false,false, 
							CellStyle.ALIGN_RIGHT, false, BG_WHITE,FC_GREEN, true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, false, BG_WHITE, FC_BLACK,true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, false, BG_WHITE, FC_BLACK,true);
					createCell(tmpRow, iCol++, "", 9, true, false, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_GREEN,true);
				} else if (i == 5) {
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,iRow - 1, 12, 14));
					createCell(tmpRow, iCol++, "AVERAGE ALL POS", 14, false,false, false, 
							CellStyle.ALIGN_RIGHT, true, BG_GREY,FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,true);
					createCell(tmpRow, iCol++,getAverageContracts(selectDate, dealerReports), 15,false, true, false, 
							CellStyle.ALIGN_CENTER, true,BG_WHITE, FC_BLACK, true);
				} else if (i == 6) {
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,
							iRow - 1, 12, 14));
					createCell(tmpRow, iCol++, "BEST PERFORMING", 14, false,false, false,
							CellStyle.ALIGN_RIGHT, true, BG_GREY,FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,true);
					long bestPerformingContracts = getBestPerformingContracts(
							selectDate, dealerReports);
					createCell(tmpRow, iCol++, bestPerformingContracts, 15,false, true, false, 
							CellStyle.ALIGN_CENTER, true,BG_WHITE, FC_BLACK, true);
					createCell(tmpRow, iCol++, getBestPerformingContractsPOS(bestPerformingContracts, selectDate,dealerReports), 14, false, true, false, 
							CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK, false);
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,iRow - 1, 16, 18));
					createCell(tmpRow, iCol++, "", 9, false, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,true);
					createCell(tmpRow, iCol++, "", 9, false, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,true);
				} else if (i == 7) {
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,iRow - 1, 12, 14));
					createCell(tmpRow, iCol++, "WORSE PERFORMING", 14, false,false, false, 
							CellStyle.ALIGN_RIGHT, true, BG_GREY,FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,true);
					long worsePerformingContracts = getWorsePerformingContracts(
							selectDate, dealerReports);
					createCell(tmpRow, iCol++, worsePerformingContracts, 15,false, true, false, 
							CellStyle.ALIGN_CENTER, true,BG_WHITE, FC_BLACK, true);
					createCell(tmpRow, iCol++, getWorsePerformingContractsPOS(worsePerformingContracts, selectDate,dealerReports), 14, false, true, false, 
							CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK, false);
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,iRow - 1, 16, 18));
					createCell(tmpRow, iCol++, "", 9, false, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,true);
					createCell(tmpRow, iCol++, "", 9, false, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,true);
				} else if (i == 8) {
					cellSpaceBorderRight(tmpRow, iCol++);
				} else if (i == 9) {
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,iRow - 1, 12, 14));
					createCell(tmpRow, iCol++, "REJECT REASONS", 14, false,false, false, 
							CellStyle.ALIGN_RIGHT, false,BG_WHITE, FC_DARK_BLUE, true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, false, BG_WHITE, FC_BLACK,true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, false, BG_WHITE, FC_BLACK,true);
					createCell(tmpRow, iCol++, "", 9, true, false, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_GREEN,true);
				} else if (i == 10) {
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,iRow - 1, 12, 14));
					createCell(tmpRow, iCol++, "Out of policy", 14, false,false, false, 
							CellStyle.ALIGN_RIGHT, true, BG_GREY,FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,
							true);
					createCell(tmpRow,iCol++,totalRejects == 0d ? 0d : roundDouble((getNbRejectByReasonCode("01RR",selectDate, paramDealer,paramDealerType) 
							/ totalRejects) * 100), 15, false, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,true);
				} else if (i == 11) {
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,iRow - 1, 12, 14));
					createCell(tmpRow, iCol++,"No repayment capacity after CBC", 14, false,false, false, 
							CellStyle.ALIGN_RIGHT, true, BG_GREY,FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,true);
					createCell(tmpRow,iCol++,totalRejects == 0d ? 0d : roundDouble((getNbRejectByReasonCode("02RR",selectDate, paramDealer,
							paramDealerType) / totalRejects) * 100), 15, false, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,true);
				} else if (i == 12) {
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,iRow - 1, 12, 14));
					createCell(tmpRow, iCol++, "Bad History in CBC", 14, false,false, false, 
							CellStyle.ALIGN_RIGHT, true, BG_GREY,FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,true);
					createCell(tmpRow,iCol++,totalRejects == 0d ? 0d : roundDouble((getNbRejectByReasonCode("03RR",selectDate, paramDealer,
							paramDealerType) / totalRejects) * 100), 15, false, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,true);
				} else if (i == 13) {
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,iRow - 1, 12, 14));
					createCell(tmpRow, iCol++, "No Repayment Capacity", 14,
							false, false, false, CellStyle.ALIGN_RIGHT, true,BG_GREY, FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,true);
					createCell(tmpRow,iCol++,totalRejects == 0d ? 0d : roundDouble((getNbRejectByReasonCode("04RR",selectDate, paramDealer,
							paramDealerType) / totalRejects) * 100), 15, false, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,true);
				} else if (i == 14) {
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,iRow - 1, 12, 14));
					createCell(tmpRow, iCol++, "Untrustworthy applicant", 14,false, false, false, 
							CellStyle.ALIGN_RIGHT, true,BG_GREY, FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,true);
					createCell(tmpRow,iCol++,totalRejects == 0d ? 0d : roundDouble((getNbRejectByReasonCode("05RR",selectDate, paramDealer,
							paramDealerType) / totalRejects) * 100), 15, false, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,true);
				} else if (i == 15) {
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,iRow - 1, 12, 14));
					createCell(tmpRow, iCol++, "Impossible to assess income",14, false, false, false, CellStyle.ALIGN_RIGHT,true, BG_GREY, FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,true);
					createCell(tmpRow,iCol++,totalRejects == 0d ? 0d : roundDouble((getNbRejectByReasonCode("06RR",selectDate, paramDealer,
							paramDealerType) / totalRejects) * 100), 15, false, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,true);
				} else if (i == 16) {
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,iRow - 1, 12, 14));
					createCell(tmpRow, iCol++,"Automatic reject after 15 days", 14, false, false,false, 
							CellStyle.ALIGN_RIGHT, true, BG_GREY,FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,true);
					createCell(tmpRow,iCol++,totalRejects == 0d ? 0d : roundDouble((getNbRejectByReasonCode("07RR",selectDate, paramDealer,
							paramDealerType) / totalRejects) * 100), 15, false, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
				} else if (i == 17) {
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,iRow - 1, 12, 14));
					createCell(tmpRow, iCol++, "Other", 14, false, false,false, 
							CellStyle.ALIGN_RIGHT, true, BG_GREY,FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,true);
					createCell(tmpRow,iCol++,totalRejects == 0d ? 0d : roundDouble((getNbRejectByReasonCode("08RR",selectDate, paramDealer,
							paramDealerType) / totalRejects) * 100), 15, false, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,true);
				} else if (i == 18) {
					cellSpaceBorderRight(tmpRow, iCol++);
					// decline
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow, 16, 16));
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow, 17, 18));
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow, 19, 20));
					//
					createCell(tmpRow, 16, "", 13, false, true, false, CellStyle.ALIGN_CENTER,
							true, BG_WHITE, FC_BLACK, true);
					createCell(tmpRow, 17, "Decline before approval", 13, false, true, false, CellStyle.ALIGN_CENTER, 
							true, BG_WHITE, FC_BLACK, true);
					createCell(tmpRow, 18, "", 9, false, true, false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
					createCell(tmpRow, 19, "Decline after approval", 13, false, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
					createCell(tmpRow, 20, "", 9, false, true, false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
				} else if (i == 19) {
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, 12, 14));
					createCell(tmpRow, iCol++, "DECLINE REASONS", 14, false,false, false, 
							CellStyle.ALIGN_RIGHT, false,BG_WHITE, FC_ORANGE, true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, false, BG_WHITE, FC_BLACK,true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, false, BG_WHITE, FC_BLACK,true);
					createCell(tmpRow, iCol++, "", 9, true, false, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_GREEN,true);
					createCell(tmpRow, 16, "", 9, true, false, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_GREEN,true);
					createCell(tmpRow, 18, "", 9, true, false, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_GREEN,true);
					createCell(tmpRow, 20, "", 9, true, false, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_GREEN,true);
				} else if (i == 20) {
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,iRow - 1, 12, 14));
					createCell(tmpRow, iCol++, "Don't buy motor for now", 14,false, false, false, 
							CellStyle.ALIGN_RIGHT, true,BG_GREY, FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,true);
					createCell(tmpRow,iCol++,totalDeclines == 0d ? 0d : roundDouble((getNbDeclineByReasonCode("01DR", selectDate, paramDealer,
							paramDealerType) / totalDeclines) * 100), 15, false, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,true);
					// total decline
					createCell(tmpRow, 16, "Total", 13, false, true, false, CellStyle.ALIGN_CENTER,
							true, BG_WHITE, FC_BLACK, true);
					// decline before approval
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, 17, 18));
					createCell(tmpRow, 17, totalDeclineBeforeApproval, 15, false, true, false, 
						CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
					createCell(tmpRow, 18, "", 9, false, true, false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
					// decline after approval
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, 19, 20));
					createCell(tmpRow, 19, totalDeclineAfterApproval, 15, false, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
					createCell(tmpRow, 20, "", 9, false, true, false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
				} else if (i == 21) {
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,iRow - 1, 12, 14));
					createCell(tmpRow, iCol++, "Buy motor with own cash", 14,
							false, false, false, CellStyle.ALIGN_RIGHT, true,BG_GREY, FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,true);
					createCell(tmpRow,iCol++,totalDeclines == 0d ? 0d : roundDouble((getNbDeclineByReasonCode("02DR", selectDate, paramDealer,
							paramDealerType) / totalDeclines) * 100), 15, false, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,true);
				} else if (i == 22) {
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,iRow - 1, 12, 14));
					createCell(tmpRow,iCol++,"Buy motor with financial support from other person",12, false, false, false, 
							CellStyle.ALIGN_RIGHT,true, BG_GREY, FC_BLACK, false);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,true);
					createCell(tmpRow,iCol++,totalDeclines == 0d ? 0d : roundDouble((getNbDeclineByReasonCode("03DR", selectDate, paramDealer,
							paramDealerType) / totalDeclines) * 100), 15, false, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,true);
				} else if (i == 23) {
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,iRow - 1, 12, 14));
					createCell(tmpRow, iCol++,"Buy motor with other institution's service", 12,false, false, false, 
							CellStyle.ALIGN_RIGHT, true,BG_GREY, FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,true);
					createCell(tmpRow,iCol++,totalDeclines == 0d ? 0d : roundDouble((getNbDeclineByReasonCode("04DR", selectDate, paramDealer,
							paramDealerType) / totalDeclines) * 100), 15, false, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,true);
				} else if (i == 24) {
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,iRow - 1, 12, 14));
					createCell(tmpRow, iCol++, "Preferred motor out of stock",14, false, false, false, 
							CellStyle.ALIGN_RIGHT,true, BG_GREY, FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,true);
					createCell(tmpRow,iCol++,totalDeclines == 0d ? 0d : roundDouble((getNbDeclineByReasonCode("05DR", selectDate, paramDealer,
							paramDealerType) / totalDeclines) * 100), 15, false, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,true);
				} else if (i == 25) {
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,iRow - 1, 12, 14));
					createCell(tmpRow, iCol++, "Can not meet GLF requirement",
							14, false, false, false, CellStyle.ALIGN_RIGHT,
							true, BG_GREY, FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,
							true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,
							true);
					createCell(
							tmpRow,
							iCol++,
							totalDeclines == 0d ? 0d
									: roundDouble((getNbDeclineByReasonCode(
											"06DR", selectDate, paramDealer,
											paramDealerType) / totalDeclines) * 100), 15, false, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
				} else if (i == 26) {
					cellSpaceBorderRight(tmpRow, iCol++);
				} else if (i == 27) {
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,
							iRow - 1, 12, 14));
					createCell(tmpRow, iCol++, "OVERDUE IN % OF PORTFOLIO", 14,
							false, false, false, CellStyle.ALIGN_RIGHT, false,
							BG_WHITE, FC_RED, true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, false, BG_WHITE, FC_BLACK,
							true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, false, BG_WHITE, FC_BLACK,
							true);
					createCell(tmpRow, iCol++, "", 9, true, false, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_GREEN,
							true);
				} else if (i == 28) {
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,
							iRow - 1, 12, 14));
					createCell(tmpRow, iCol++, "PAR > 30 days", 14, false,
							false, false, CellStyle.ALIGN_RIGHT, true, BG_GREY,
							FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,
							true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,
							true);
					// PAR > 30 days
					createCell(tmpRow, iCol++, "", 15, false, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
				} else if (i == 29) {
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,
							iRow - 1, 12, 14));
					createCell(tmpRow, iCol++, "PAR > 90 days", 14, false,
							false, false, CellStyle.ALIGN_RIGHT, true, BG_GREY,
							FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,
							true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,
							true);
					// PAR > 90 days
					createCell(tmpRow, iCol++, "", 15, false, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
				} else if (i == 30) {
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,
							iRow - 1, 12, 14));
					createCell(tmpRow, iCol++, "PAR > 180 days", 14, false,
							false, false, CellStyle.ALIGN_RIGHT, true, BG_GREY,
							FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,
							true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,
							true);
					// PAR > 180 days
					createCell(tmpRow, iCol++, "", 15, false, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
				} else if (i == 31) {
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,
							iRow - 1, 12, 14));
					createCell(tmpRow, iCol++, "Total Overdue", 14, false,
							false, false, CellStyle.ALIGN_RIGHT, true, BG_GREY,
							FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,
							true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK,
							true);
					createCell(tmpRow, iCol++, "", 15, false, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
				} else if (i == 32) {
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,
							iRow - 1, 12, 14));
					createCell(tmpRow, iCol++, "APPLICATIONS TODAY", 14, false,
							true, false, CellStyle.ALIGN_RIGHT, true, BG_WHITE,
							FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, false, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
					createCell(tmpRow, iCol++, "", 9, false, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
					createCell(tmpRow, iCol++,
							getNbApplicationsToday(selectDate, dealerReports),
							15, false, true, false, CellStyle.ALIGN_CENTER,
							true, BG_WHITE, FC_BLACK, true);
				} else if (i == 33) {
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,
							iRow - 1, 12, 14));
					createCell(tmpRow, iCol++, "CONTRACTS TODAY", 14, false,
							true, false, CellStyle.ALIGN_RIGHT, true, BG_WHITE,
							FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, false, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
					createCell(tmpRow, iCol++, "", 9, false, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
					createCell(tmpRow,iCol++,getNbContractsSelectDate(selectDate, dealerReports),15, false, 
							true, false, CellStyle.ALIGN_CENTER,true, BG_WHITE, FC_BLACK, true);
				} else if (i == 34) {
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,
							iRow - 1, 12, 14));
					createCell(tmpRow, iCol++, "TOTAL CONTRACTS THIS MONTH",
							14, false, true, false, CellStyle.ALIGN_RIGHT,
							true, BG_WHITE, FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, false, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
					createCell(tmpRow, iCol++, "", 9, false, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
					createCell(tmpRow, iCol++,
							getNbContractsOfMonth(selectDate, dealerReports),
							15, false, true, false, CellStyle.ALIGN_CENTER,
							true, BG_WHITE, FC_BLACK, true);
				} else if (i == 35) {
					cellSpaceBorderRight(tmpRow, iCol++);
				} else if (i == 36) {
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,
							iRow - 1, 12, 15));
					createCell(tmpRow, iCol++, "Detail of application today",
							13, false, true, false, CellStyle.ALIGN_CENTER,
							true, BG_WHITE, FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, false, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
					createCell(tmpRow, iCol++, "", 9, false, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
					createCell(tmpRow, iCol++, "", 9, true, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
					// contract in term
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,
							iRow - 1, 16, 20));
					createCell(tmpRow, iCol++, "Detail of application today",
							13, false, true, false, CellStyle.ALIGN_CENTER,
							true, BG_WHITE, FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, false, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
					createCell(tmpRow, iCol++, "", 9, false, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
					createCell(tmpRow, iCol++, "", 9, true, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
					createCell(tmpRow, iCol++, "", 9, true, true, true,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
				} else if (i == 37) {
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,
							iRow - 1, 12, 13));
					createCell(tmpRow, iCol++, "Advance payment", 13, false,
							true, false, CellStyle.ALIGN_CENTER, true,
							BG_WHITE, FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, true, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,
							iRow - 1, 14, 15));
					createCell(tmpRow, iCol++, "Accumulated for the month", 13,
							false, true, false, CellStyle.ALIGN_CENTER, true,
							BG_WHITE, FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, true, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
					// contract in term
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,
							iRow - 1, 16, 17));
					createCell(tmpRow, iCol++, "Contract term", 13, false,
							true, false, CellStyle.ALIGN_CENTER, true,
							BG_WHITE, FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, true, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,
							iRow - 1, 18, 20));
					createCell(tmpRow, iCol++, "Accumulated for the month", 13,
							false, true, false, CellStyle.ALIGN_CENTER, true,
							BG_WHITE, FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, true, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
					createCell(tmpRow, iCol++, "", 9, true, true, true,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
				} else if (i == 38) {
					createCell(tmpRow, iCol++, "Today", 13, true, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
					createCell(tmpRow, iCol++,
							numApplicationsByTermAndPercentage[2][0], 15, true,
							true, false, CellStyle.ALIGN_CENTER, true,
							BG_WHITE, FC_BLACK, true);
					createCell(tmpRow, iCol++,
							numApplicationsOfMonthByTermAndPercentage[2][0],
							15, true, true, false, CellStyle.ALIGN_CENTER,
							true, BG_WHITE, FC_BLACK, true);
					// total of advance payment by percentage
					createCell(tmpRow, iCol++, roundDouble(totalOfAdvanPaymentPercentage), 15, true, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
					// contract in term
					createCell(tmpRow, iCol++, "Today", 14, true, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
					createCell(tmpRow, iCol++,
							numApplicationsByTermAndPercentage[2][0], 15, true,
							true, false, CellStyle.ALIGN_CENTER, true,
							BG_WHITE, FC_BLACK, true);
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,
							iRow - 1, 18, 19));
					createCell(tmpRow, iCol++,
							numApplicationsOfMonthByTermAndPercentage[2][0],
							15, true, true, false, CellStyle.ALIGN_CENTER,
							true, BG_WHITE, FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, true, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
					// total of term by percentage
					createCell(tmpRow, iCol++, roundDouble(totalOfTermPercentage), 15, true, true, true,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
				} else if (i == 39) {
					createCell(tmpRow, iCol++, "10%", 15, true, true, false,
							CellStyle.ALIGN_RIGHT, true, BG_WHITE, FC_BLACK,
							true);
					createCell(tmpRow, iCol++, numApplicationsByPercentage[0],
							15, true, true, false, CellStyle.ALIGN_CENTER,
							true, BG_WHITE, FC_BLACK, true);
					createCell(tmpRow, iCol++,
							numApplicationsOfMonthByPercentage[0], 14, true,
							true, false, CellStyle.ALIGN_CENTER, true,
							BG_WHITE, FC_BLACK, true);
					// advance payment 10% by percentage
					createCell(tmpRow, iCol++, roundDouble(advancePaymentPercentage[0]), 15, true, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
					// contract in term
					createCell(tmpRow, iCol++, "12", 15, true, true, false,
							CellStyle.ALIGN_RIGHT, true, BG_WHITE, FC_BLACK,
							true);
					createCell(tmpRow, iCol++, numApplicationsByTerm[0], 15,
							true, true, false, CellStyle.ALIGN_CENTER, true,
							BG_WHITE, FC_BLACK, true);
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,
							iRow - 1, 18, 19));
					//
					createCell(tmpRow, iCol++, numApplicationsOfMonthByTerm[0],
							15, true, true, false, CellStyle.ALIGN_CENTER,
							true, BG_WHITE, FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, true, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
					// 12 term by percentage
					createCell(tmpRow, iCol++, roundDouble(termPercentage[0]), 15, true, true, true,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
				} else if (i == 40) {
					createCell(tmpRow, iCol++, "20%", 15, true, true, false,
							CellStyle.ALIGN_RIGHT, true, BG_WHITE, FC_BLACK,
							true);
					createCell(tmpRow, iCol++, numApplicationsByPercentage[1],
							15, true, true, false, CellStyle.ALIGN_CENTER,
							true, BG_WHITE, FC_BLACK, true);
					createCell(tmpRow, iCol++,
							numApplicationsOfMonthByPercentage[1], 15, true,
							true, false, CellStyle.ALIGN_CENTER, true,
							BG_WHITE, FC_BLACK, true);
					// advance payment 20% by percentage
					createCell(tmpRow, iCol++, roundDouble(advancePaymentPercentage[1]), 15, true, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
					// contract in term
					createCell(tmpRow, iCol++, "24", 15, true, true, false,
							CellStyle.ALIGN_RIGHT, true, BG_WHITE, FC_BLACK,
							true);
					createCell(tmpRow, iCol++, numApplicationsByTerm[1], 15,
							true, true, false, CellStyle.ALIGN_CENTER, true,
							BG_WHITE, FC_BLACK, true);
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,
							iRow - 1, 18, 19));
					//
					createCell(tmpRow, iCol++, numApplicationsOfMonthByTerm[1],
							15, true, true, false, CellStyle.ALIGN_CENTER,
							true, BG_WHITE, FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, true, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
					// 24 term by percentage
					createCell(tmpRow, iCol++, roundDouble(termPercentage[1]), 15, true, true, true,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
				} else if (i == 41) {
					createCell(tmpRow, iCol++, "30%", 15, true, true, false,
							CellStyle.ALIGN_RIGHT, true, BG_WHITE, FC_BLACK,
							true);
					createCell(tmpRow, iCol++, numApplicationsByPercentage[2],
							15, true, true, false, CellStyle.ALIGN_CENTER,
							true, BG_WHITE, FC_BLACK, true);
					createCell(tmpRow, iCol++,
							numApplicationsOfMonthByPercentage[2], 15, true,
							true, false, CellStyle.ALIGN_CENTER, true,
							BG_WHITE, FC_BLACK, true);
					// advance payment 30% by percentage
					createCell(tmpRow, iCol++, roundDouble(advancePaymentPercentage[2]), 15, true, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
					// contract in term
					createCell(tmpRow, iCol++, "36", 15, true, true, false,
							CellStyle.ALIGN_RIGHT, true, BG_WHITE, FC_BLACK,
							true);
					createCell(tmpRow, iCol++, numApplicationsByTerm[2], 15,
							true, true, false, CellStyle.ALIGN_CENTER, true,
							BG_WHITE, FC_BLACK, true);
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,
							iRow - 1, 18, 19));
					//
					createCell(tmpRow, iCol++, numApplicationsOfMonthByTerm[2],
							15, true, true, false, CellStyle.ALIGN_CENTER,
							true, BG_WHITE, FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, true, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
					// 36 term by percentage
					createCell(tmpRow, iCol++, roundDouble(termPercentage[2]), 15, true, true, true,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
				} else if (i == 42) {
					createCell(tmpRow, iCol++, "40%", 15, true, true, false,
							CellStyle.ALIGN_RIGHT, true, BG_WHITE, FC_BLACK,
							true);
					createCell(tmpRow, iCol++, numApplicationsByPercentage[3],
							15, true, true, false, CellStyle.ALIGN_CENTER,
							true, BG_WHITE, FC_BLACK, true);
					createCell(tmpRow, iCol++,
							numApplicationsOfMonthByPercentage[3], 15, true,
							true, false, CellStyle.ALIGN_CENTER, true,
							BG_WHITE, FC_BLACK, true);
					// advance payment 40% by percentage
					createCell(tmpRow, iCol++, roundDouble(advancePaymentPercentage[3]), 15, true, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
					// contract in term
					createCell(tmpRow, iCol++, "48", 15, true, true, false,
							CellStyle.ALIGN_RIGHT, true, BG_WHITE, FC_BLACK,
							true);
					createCell(tmpRow, iCol++, numApplicationsByTerm[3], 15,
							true, true, false, CellStyle.ALIGN_CENTER, true,
							BG_WHITE, FC_BLACK, true);
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,
							iRow - 1, 18, 19));
					//
					createCell(tmpRow, iCol++, numApplicationsOfMonthByTerm[3],
							15, true, true, false, CellStyle.ALIGN_CENTER,
							true, BG_WHITE, FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, true, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
					// 48 term by percentage
					createCell(tmpRow, iCol++, roundDouble(termPercentage[3]), 15, true, true, true,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
				} else if (i == 43) {
					createCell(tmpRow, iCol++, "50%", 15, true, true, false,
							CellStyle.ALIGN_RIGHT, true, BG_WHITE, FC_BLACK,
							true);
					createCell(tmpRow, iCol++, numApplicationsByPercentage[4],
							15, true, true, false, CellStyle.ALIGN_CENTER,
							true, BG_WHITE, FC_BLACK, true);
					createCell(tmpRow, iCol++,
							numApplicationsOfMonthByPercentage[4], 15, true,
							true, false, CellStyle.ALIGN_CENTER, true,
							BG_WHITE, FC_BLACK, true);
					// advance payment 50% by percentage
					createCell(tmpRow, iCol++, roundDouble(advancePaymentPercentage[4]), 15, true, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
					// contract in term
					createCell(tmpRow, iCol++, "60", 15, true, true, false,
							CellStyle.ALIGN_RIGHT, true, BG_WHITE, FC_BLACK,
							true);
					createCell(tmpRow, iCol++, numApplicationsByTerm[4], 15,
							true, true, false, CellStyle.ALIGN_CENTER, true,
							BG_WHITE, FC_BLACK, true);
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,
							iRow - 1, 18, 19));
					//
					createCell(tmpRow, iCol++, numApplicationsOfMonthByTerm[4],
							15, true, true, false, CellStyle.ALIGN_CENTER,
							true, BG_WHITE, FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, true, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
					// 60 term by percentage
					createCell(tmpRow, iCol++, roundDouble(termPercentage[4]), 15, true, true, true,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
				} else if (i == 44) {
					createCell(tmpRow, iCol++, "60%", 15, true, true, false,
							CellStyle.ALIGN_RIGHT, true, BG_WHITE, FC_BLACK,
							true);
					createCell(tmpRow, iCol++, numApplicationsByPercentage[5],
							15, true, true, false, CellStyle.ALIGN_CENTER,
							true, BG_WHITE, FC_BLACK, true);
					createCell(tmpRow, iCol++,
							numApplicationsOfMonthByPercentage[5], 15, true,
							true, false, CellStyle.ALIGN_CENTER, true,
							BG_WHITE, FC_BLACK, true);
					// advance payment 60% by percentage
					createCell(tmpRow, iCol++, roundDouble(advancePaymentPercentage[5]), 15, true, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
				} else if (i == 45) {
					createCell(tmpRow, iCol++, "70%", 15, true, true, false,
							CellStyle.ALIGN_RIGHT, true, BG_WHITE, FC_BLACK,
							true);
					createCell(tmpRow, iCol++, numApplicationsByPercentage[6],
							15, true, true, false, CellStyle.ALIGN_CENTER,
							true, BG_WHITE, FC_BLACK, true);
					createCell(tmpRow, iCol++,
							numApplicationsOfMonthByPercentage[6], 15, true,
							true, false, CellStyle.ALIGN_CENTER, true,
							BG_WHITE, FC_BLACK, true);
					// advance payment 70% by percentage
					createCell(tmpRow, iCol++, roundDouble(advancePaymentPercentage[6]), 15, true, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
				} else if (i == 46) {
					cellSpaceBorderRight(tmpRow, iCol++);
				} else if (i == 47) {
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,
							iRow - 1, 12, 15));
					createCell(tmpRow, iCol++, "Detail of application today",
							15, false, true, false, CellStyle.ALIGN_CENTER,
							true, BG_WHITE, FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, false, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
					createCell(tmpRow, iCol++, "", 9, false, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
					createCell(tmpRow, iCol++, "", 9, true, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
				} else if (i == 48) {
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,
							iRow - 1, 12, 13));
					createCell(tmpRow, iCol++, "Pay later product", 13, false,
							true, false, CellStyle.ALIGN_CENTER, true,
							BG_WHITE, FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, true, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,
							iRow - 1, 14, 15));
					createCell(tmpRow, iCol++, "Accumulated for the month", 13,
							false, true, false, CellStyle.ALIGN_CENTER, true,
							BG_WHITE, FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, true, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
				} else if (i == 49) {
					createCell(tmpRow, iCol++, "Today", 14, true, true, false,
							CellStyle.ALIGN_RIGHT, true, BG_WHITE, FC_BLACK,
							true);
					createCell(tmpRow, iCol++,
							numApplicationsByTermAndPercentage[2][1], 15, true,
							true, false, CellStyle.ALIGN_CENTER, true,
							BG_WHITE, FC_BLACK, true);
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,
							iRow - 1, 14, 15));
					createCell(tmpRow, iCol++,
							numApplicationsOfMonthByTermAndPercentage[2][1],
							15, true, true, false, CellStyle.ALIGN_CENTER,
							true, BG_WHITE, FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, true, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
				} else if (i == 50) {
					cellSpaceBorderRight(tmpRow, iCol++);	
				} else if (i == 51) {
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1,
							iRow - 1, 12, 13));
					createCell(tmpRow, iCol++, "Accumulated contract for the month", 13, false,
							true, false, CellStyle.ALIGN_CENTER, true,
							BG_WHITE, FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 9, true, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
					createCell(tmpRow, iCol++, "Low target", 13,
							false, true, false, CellStyle.ALIGN_CENTER, true,
							BG_WHITE, FC_BLACK, true);
					createCell(tmpRow, iCol++, "High target", 13, true, true, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
				} else if (i == 52) {
					accumlateRow = tmpRow;
					accumlateIRow = iRow;
				} else if (i == 53) {
					cellSpaceBorderRight(tmpRow, iCol++);
				} else if (i == 54) {
					sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow, 12, 13));
					createCell(tmpRow, iCol++, "Total number of field check", 13, false, true, false, CellStyle.ALIGN_CENTER, true,
							BG_WHITE, FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 13, false, true, false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
					createCell(tmpRow, iCol++, "Dealer", 13, false, true, false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, 
							true);
					if (paramDealerType != null) {
						if (paramDealerType == EDealerType.HEAD) {
							createCell(tmpRow, iCol++, totalNumRFCForTypeDealer > 0 ? totalNumRFCForTypeDealer : 0, 15, 
									true, true, false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
						} else {
							createCell(tmpRow, iCol++, 0, 15, true, true, false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
						}	
					} else {
						createCell(tmpRow, iCol++, totalNumRFCForTypeDealer > 0 ? totalNumRFCForTypeDealer : 0, 15, 
								true, true, false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
					}
				} else if (i == 55) {
					createCell(tmpRow, iCol++, "", 13, false, true, false, CellStyle.ALIGN_CENTER, true,
							BG_WHITE, FC_BLACK, true);
					createCell(tmpRow, iCol++, "", 13, false, true, false, CellStyle.ALIGN_CENTER, true,
							BG_WHITE, FC_BLACK, true);
					createCell(tmpRow, iCol++, "Sub Dealer", 13, false, true, false, CellStyle.ALIGN_CENTER, true, BG_WHITE, 
							FC_BLACK, true);
					if (paramDealerType != null) {
						if (paramDealerType == EDealerType.BRANCH) {
							createCell(tmpRow, iCol++, totalNumRFCForTypeSubDealer > 0 ? totalNumRFCForTypeSubDealer : 0, 15, 
									true, true, false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
						} else {
							createCell(tmpRow, iCol++, 0, 15, true, true, false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
						}
					} else {
						createCell(tmpRow, iCol++, totalNumRFCForTypeSubDealer > 0 ? totalNumRFCForTypeSubDealer : 0, 15, 
								true, true, false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
					}	
				} else {
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
					createCell(tmpRow, iCol++, "", 9, false, false, false,
							CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK,
							true);
				}
			}
			
			sheet.addMergedRegion(new CellRangeAddress(accumlateIRow - 1, accumlateIRow - 1, 12, 13));
			createCell(accumlateRow, 12, totalNbContracts, 15, false, true, false, CellStyle.ALIGN_CENTER, true,
					BG_WHITE, FC_BLACK, true);
			createCell(accumlateRow, 13, "", 15, true, true, false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
			createCell(accumlateRow, 14, (totaltargetLow > 0 ? roundDouble((100 * totalNbContracts) / totaltargetLow) : 0d)
					, 15, false, true, false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
			createCell(accumlateRow, 15, (totaltargetHigh > 0 ? roundDouble((100 * totalNbContracts) / totaltargetHigh) : 0d)
					, 15, true, true, false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
			
			tmpRow = totalRow == null ? sheet.createRow(iRow++) : totalRow;
			totalIRow = totalRow != null ? totalIRow + dealerReports.size()
					: iRow;
			iCol = 0;
			createCell(tmpRow, iCol++, "Total", 14, false, false, false,
					CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_GREEN, true);
			createCell(tmpRow, iCol++, "All POS", 14, false, false, false,
					CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_GREEN, true);
			createCell(tmpRow, iCol++, "", 9, false, false, false,
					CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_GREEN, true);
			createCell(tmpRow, iCol++, "", 9, false, false, false,
					CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_GREEN, true);
			createCell(tmpRow, iCol++, totalNbApplications, 15, false, false,
					false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_GREEN,
					true);
			createCell(tmpRow, iCol++, totalNbContracts, 15, false, false,
					false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_GREEN,
					true);
			sheet.addMergedRegion(new CellRangeAddress(totalIRow - 1,
					totalIRow - 1, iCol, iCol + 1));
			createCell(tmpRow, iCol++, (totalNbApplications > 0 ? roundDouble((100 * totalNbContracts) / totalNbApplications) : 0d), 
					15, false, false, false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_GREEN, true);
			createCell(tmpRow, iCol++, "", 9, false, false, false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_GREEN, true);
			createCell(tmpRow, iCol++, (totaltargetLow > 0 ? roundDouble((100 * totalNbContracts) / totaltargetLow) : 0d), 15, 
					false, false, false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_GREEN, true);
			createCell(tmpRow, iCol++, (totaltargetHigh > 0 ? roundDouble((100 * totalNbContracts) / totaltargetHigh) : 0d), 15, 
					false, false, false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_GREEN, true);
			createCell(tmpRow, iCol++, totaltargetLow, 15, false, false, false,
					CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_GREEN, true);
			createCell(tmpRow, iCol++, totaltargetHigh, 15, false, false,
					false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_GREEN, true);
		}
		logger.info("----------------End overview table report part----------------");
		return iRow;
	}

	/**
	 * 
	 * @param tmpRow
	 * @param iCol
	 */
	private void cellSpaceBorderRight(Row tmpRow, int iCol) {
		createCell(tmpRow, iCol++, "", 9, false, false, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
		createCell(tmpRow, iCol++, "", 9, false, false, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
		createCell(tmpRow, iCol++, "", 9, false, false, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
		createCell(tmpRow, iCol++, "", 9, true, false, false,
				CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK, true);
	}

	/**
	 * 
	 * @param dealer
	 * @param selectDate
	 * @return
	 */
	private long[] getNumVisitor(Dealer dealer, Date selectDate) {

		long dealerVisitor = 0;
		long companyVisitor = 0;
		long companyApply = 0;
		BaseRestrictions<Statistic3HoursVisitor> restrictions = new BaseRestrictions<>(
				Statistic3HoursVisitor.class);
		restrictions.addCriterion(Restrictions.ge("date",
				DateUtils.getDateAtBeginningOfDay(selectDate)));
		restrictions.addCriterion(Restrictions.le("date",
				DateUtils.getDateAtEndOfDay(selectDate)));
		restrictions.addCriterion(Restrictions.eq("dealer.id", dealer.getId()));

		List<Statistic3HoursVisitor> statisticVisitors = dailyReportService
				.list(restrictions);
		if (statisticVisitors != null && !statisticVisitors.isEmpty()) {
			for (Statistic3HoursVisitor staticVisitor : statisticVisitors) {
				dealerVisitor += MyNumberUtils.getInteger(staticVisitor
						.getNumberVisitorDealer11());
				dealerVisitor += MyNumberUtils.getInteger(staticVisitor
						.getNumberVisitorDealer14());
				dealerVisitor += MyNumberUtils.getInteger(staticVisitor
						.getNumberVisitorDealer17());
				companyVisitor += MyNumberUtils.getInteger(staticVisitor
						.getNumberVisitorCompany11());
				companyVisitor += MyNumberUtils.getInteger(staticVisitor
						.getNumberVisitorCompany14());
				companyVisitor += MyNumberUtils.getInteger(staticVisitor
						.getNumberVisitorCompany17());
				companyApply += MyNumberUtils.getInteger(staticVisitor
						.getNumberVisitorApplied11());
				companyApply += MyNumberUtils.getInteger(staticVisitor
						.getNumberVisitorApplied14());
				companyApply += MyNumberUtils.getInteger(staticVisitor
						.getNumberVisitorApplied17());
			}
		}
		return new long[] { dealerVisitor, companyVisitor, companyApply };
	}

	/**
	 * 
	 * @param value1
	 * @param value2
	 * @return
	 */
	private double calculatePercentageForAdvanceAndTerm(int value1, int value2) {
		if (value2 > 0) {
			return (100 * value1) / (double) value2;
		}
		return 0d;
	}
	
	/**
	 * Detail of accumulated application per advance payment and contract term for 
	 * month
	 * @param selectDate
	 * @return
	 */
	private int[][] getDetailNumApplicationsOfMonthByTerm(Date selectDate,
			Dealer dealer, EDealerType dealerType) {
		Date startDate = DateUtils.getDateAtBeginningOfMonth(selectDate);
		int numApplicationsTerm12 = 0;
		int numApplicationsTerm24 = 0;
		int numApplicationsTerm36 = 0;
		int numApplicationsTerm48 = 0;
		int numApplicationsTerm60 = 0;
		int numAdvancePayment10Percentage = 0;
		int numAdvancePayment20Percentage = 0;
		int numAdvancePayment30Percentage = 0;
		int numAdvancePayment40Percentage = 0;
		int numAdvancePayment50Percentage = 0;
		int numAdvancePayment60Percentage = 0;
		int numAdvancePayment70Percentage = 0;

		int totalApplicationsOfMonth = 0;
		int totalApplicationsPayLateProductOfMonth = 0;
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(
				Quotation.class);
		restrictions.addAssociation("dealer", "quodeal", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.ge("firstSubmissionDate",
				DateFilterUtil.getStartDate(startDate)));
		restrictions.addCriterion(Restrictions.le("firstSubmissionDate",
				DateFilterUtil.getEndDate(selectDate)));
		if (dealer != null) {
			restrictions.addCriterion(Restrictions.eq("quodeal.id",
					dealer.getId()));
		}
		if (dealerType != null) {
			restrictions.addCriterion(Restrictions.eq("quodeal.dealerType",
					dealerType));
		}
		List<Quotation> quotations = entityService.list(restrictions);
		if (quotations != null && !quotations.isEmpty()) {
			totalApplicationsOfMonth = quotations.size();
			for (Quotation quotation : quotations) {
				if (quotation.getTerm() != null) {
					if (quotation.getTerm() <= 12) {
						numApplicationsTerm12++;
					} else if (quotation.getTerm() <= 24) {
						numApplicationsTerm24++;
					} else if (quotation.getTerm() <= 36) {
						numApplicationsTerm36++;
					} else if (quotation.getTerm() <= 48) {
						numApplicationsTerm48++;
					} else if (quotation.getTerm() <= 60) {
						numApplicationsTerm60++;
					}

					if (quotation.getAdvancePaymentPercentage() <= 10) {
						numAdvancePayment10Percentage++;
					} else if (quotation.getAdvancePaymentPercentage() <= 20) {
						numAdvancePayment20Percentage++;
					} else if (quotation.getAdvancePaymentPercentage() <= 30) {
						numAdvancePayment30Percentage++;
					} else if (quotation.getAdvancePaymentPercentage() <= 40) {
						numAdvancePayment40Percentage++;
					} else if (quotation.getAdvancePaymentPercentage() <= 50) {
						numAdvancePayment50Percentage++;
					} else if (quotation.getAdvancePaymentPercentage() <= 60) {
						numAdvancePayment60Percentage++;
					} else if (quotation.getAdvancePaymentPercentage() > 60) {
						numAdvancePayment70Percentage++;
					}

					if (quotation.getFinancialProduct() != null) {
						if ("GLF06".equals(quotation.getFinancialProduct()
								.getCode())
								|| "GLF07".equals(quotation
										.getFinancialProduct().getCode())) {
							totalApplicationsPayLateProductOfMonth++;
						}
					}
				}

			}
		}
		return new int[][] {
				{ numApplicationsTerm12, numApplicationsTerm24,
						numApplicationsTerm36, numApplicationsTerm48,
						numApplicationsTerm60 },
				{ numAdvancePayment10Percentage, numAdvancePayment20Percentage,
						numAdvancePayment30Percentage,
						numAdvancePayment40Percentage,
						numAdvancePayment50Percentage,
						numAdvancePayment60Percentage,
						numAdvancePayment70Percentage },
				{ totalApplicationsOfMonth,
						totalApplicationsPayLateProductOfMonth }};
	}

	/**
	 * Detail of accumulated application per advance payment and contract term for
	 * today
	 * @param selectDate
	 * @return
	 */
	private int[][] getNumApplicationsByTerm(Date selectDate, Dealer dealer,
			EDealerType dealerType) {
		int numApplicationsTerm12 = 0;
		int numApplicationsTerm24 = 0;
		int numApplicationsTerm36 = 0;
		int numApplicationsTerm48 = 0;
		int numApplicationsTerm60 = 0;
		int numAdvancePayment10Percentage = 0;
		int numAdvancePayment20Percentage = 0;
		int numAdvancePayment30Percentage = 0;
		int numAdvancePayment40Percentage = 0;
		int numAdvancePayment50Percentage = 0;
		int numAdvancePayment60Percentage = 0;
		int numAdvancePayment70Percentage = 0;

		int totalApplications = 0;
		int totalApplicationsPayLateProduct = 0;

		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(
				Quotation.class);
		restrictions.addAssociation("dealer", "quodeal", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.ge("firstSubmissionDate",
				DateFilterUtil.getStartDate(selectDate)));
		restrictions.addCriterion(Restrictions.le("firstSubmissionDate",
				DateFilterUtil.getEndDate(selectDate)));
		if (dealer != null) {
			restrictions.addCriterion(Restrictions.eq("quodeal.id",
					dealer.getId()));
		}
		if (dealerType != null) {
			restrictions.addCriterion(Restrictions.eq("quodeal.dealerType",
					dealerType));
		}
		List<Quotation> quotations = entityService.list(restrictions);
		if (quotations != null && !quotations.isEmpty()) {
			totalApplications = quotations.size();
			for (Quotation quotation : quotations) {
				if (quotation.getTerm() != null) {
					if (quotation.getTerm() <= 12) {
						numApplicationsTerm12++;
					} else if (quotation.getTerm() <= 24) {
						numApplicationsTerm24++;
					} else if (quotation.getTerm() <= 36) {
						numApplicationsTerm36++;
					} else if (quotation.getTerm() <= 48) {
						numApplicationsTerm48++;
					} else if (quotation.getTerm() <= 60) {
						numApplicationsTerm60++;
					}

					if (quotation.getAdvancePaymentPercentage() <= 10) {
						numAdvancePayment10Percentage++;
					} else if (quotation.getAdvancePaymentPercentage() <= 20) {
						numAdvancePayment20Percentage++;
					} else if (quotation.getAdvancePaymentPercentage() <= 30) {
						numAdvancePayment30Percentage++;
					} else if (quotation.getAdvancePaymentPercentage() <= 40) {
						numAdvancePayment40Percentage++;
					} else if (quotation.getAdvancePaymentPercentage() <= 50) {
						numAdvancePayment50Percentage++;
					} else if (quotation.getAdvancePaymentPercentage() <= 60) {
						numAdvancePayment60Percentage++;
					} else if (quotation.getAdvancePaymentPercentage() > 60) {
						numAdvancePayment70Percentage++;
					}

					if (quotation.getFinancialProduct() != null) {
						if ("GLF06".equals(quotation.getFinancialProduct()
								.getCode())
								|| "GLF07".equals(quotation
										.getFinancialProduct().getCode())) {
							totalApplicationsPayLateProduct++;
						}
					}
				}

			}
		}
		return new int[][] {
				{ numApplicationsTerm12, numApplicationsTerm24,
						numApplicationsTerm36, numApplicationsTerm48,
						numApplicationsTerm60 },
				{ numAdvancePayment10Percentage, numAdvancePayment20Percentage,
						numAdvancePayment30Percentage,
						numAdvancePayment40Percentage,
						numAdvancePayment50Percentage,
						numAdvancePayment60Percentage,
						numAdvancePayment70Percentage },
				{ totalApplications, totalApplicationsPayLateProduct } };
	}
	
	/**
	 * 
	 * @param selectDate
	 * @param dealerReports
	 * @return
	 */
	private long getAverageApplications(Date selectDate,
			List<DealerReport> dealerReports) {
		long averageApplications = 0;
		for (DealerReport dealerReport : dealerReports) {
			DailyReport selectDailyReport = dealerReport
					.getSelectDailyReport(selectDate);
			long nbApplications = selectDailyReport.getApply() == null ? 0
					: selectDailyReport.getApply();
			averageApplications += nbApplications;
		}
		return roundInt((double) averageApplications / dealerReports.size());
	}

	/**
	 * 
	 * @param selectDate
	 * @param dealerReports
	 * @return
	 */
	private long getBestPerformingApplications(Date selectDate,
			List<DealerReport> dealerReports) {
		long bestPerformingApplications = 0;
		for (DealerReport dealerReport : dealerReports) {
			DailyReport selectDailyReport = dealerReport
					.getSelectDailyReport(selectDate);
			long nbApplications = selectDailyReport.getApply() == null ? 0
					: selectDailyReport.getApply();
			if (bestPerformingApplications < nbApplications) {
				bestPerformingApplications = nbApplications;
			}
		}
		return bestPerformingApplications;
	}
	
	/**
	 * 
	 * @param selectDate
	 * @param dealerReports
	 * @return
	 */
	private long getWorsePerformingApplications(Date selectDate,
			List<DealerReport> dealerReports) {
		long worsePerformingApplications = 0;
		int index = 0;
		for (DealerReport dealerReport : dealerReports) {
			DailyReport selectDailyReport = dealerReport
					.getSelectDailyReport(selectDate);
			long nbApplications = selectDailyReport.getApply() == null ? 0
					: selectDailyReport.getApply();
			if (index == 0 || worsePerformingApplications > nbApplications) {
				worsePerformingApplications = nbApplications;
			}
			index++;
		}
		return worsePerformingApplications;
	}

	/**
	 * 
	 * @param selectDate
	 * @param dealerReports
	 * @return
	 */
	private long getAverageContracts(Date selectDate,
			List<DealerReport> dealerReports) {
		long averageContracts = 0;
		for (DealerReport dealerReport : dealerReports) {
			DailyReport selectDailyReport = dealerReport
					.getSelectDailyReport(selectDate);
			long nbContracts = selectDailyReport.getNewContract() == null ? 0
					: selectDailyReport.getNewContract();
			averageContracts += nbContracts;
		}
		return roundInt((double) averageContracts / dealerReports.size());
	}

	/**
	 * 
	 * @param selectDate
	 * @param dealerReports
	 * @return
	 */
	private long getBestPerformingContracts(Date selectDate,
			List<DealerReport> dealerReports) {
		long bestPerformingContracts = 0;
		for (DealerReport dealerReport : dealerReports) {
			DailyReport selectDailyReport = dealerReport
					.getSelectDailyReport(selectDate);
			long nbContracts = selectDailyReport.getNewContract() == null ? 0
					: selectDailyReport.getNewContract();
			if (bestPerformingContracts < nbContracts) {
				bestPerformingContracts = nbContracts;
			}
		}
		return bestPerformingContracts;
	}

	/**
	 * 
	 * @param selectDate
	 * @param dealerReports
	 * @return
	 */
	private long getWorsePerformingContracts(Date selectDate,
			List<DealerReport> dealerReports) {
		long worsePerformingContracts = 0;
		int index = 0;
		for (DealerReport dealerReport : dealerReports) {
			DailyReport selectDailyReport = dealerReport
					.getSelectDailyReport(selectDate);
			long nbContracts = selectDailyReport.getNewContract() == null ? 0
					: selectDailyReport.getNewContract();
			if (index == 0 || worsePerformingContracts > nbContracts) {
				worsePerformingContracts = nbContracts;
			}
			index++;
		}
		return worsePerformingContracts;
	}

	/**
	 * 
	 * @param bestPerformingContracts
	 * @param selectDate
	 * @param dealerReports
	 * @return
	 */
	private String getBestPerformingContractsPOS(long bestPerformingContracts, Date selectDate, List<DealerReport> dealerReports) {
		String noBestPerformancePOS = "No dealer";
		String bestPerformancePOS = "";
		for (DealerReport dealerReport : dealerReports) {
			DailyReport selectDailyReport = dealerReport.getSelectDailyReport(selectDate);
			long nbContracts = selectDailyReport.getNewContract() == null ? 0 : selectDailyReport.getNewContract();
			if (bestPerformingContracts == nbContracts) {
				bestPerformancePOS = dealerReport.getDealer().getNameEn();
			}
		}
		return (bestPerformancePOS.length() == 0) ? noBestPerformancePOS : bestPerformancePOS.toString();
	}

	/**
	 * 
	 * @param worsePerformingContracts
	 * @param selectDate
	 * @param dealerReports
	 * @return
	 */
	private String getWorsePerformingContractsPOS(long worsePerformingContracts, Date selectDate,
			List<DealerReport> dealerReports) {
		String noWorstPerformancePOS = "No dealer";
		String worstPerformancePOS = "";
		for (DealerReport dealerReport : dealerReports) {
			DailyReport selectDailyReport = dealerReport.getSelectDailyReport(selectDate);
			long nbContracts = selectDailyReport.getNewContract() == null ? 0 : selectDailyReport.getNewContract();
			if (worsePerformingContracts == nbContracts) {
				worstPerformancePOS = dealerReport.getDealer().getNameEn();
			}
		}
		return (worstPerformancePOS.length() == 0) ? noWorstPerformancePOS : worstPerformancePOS.toString();
	}

	/**
	 * 
	 * @param worsePerformingApplications
	 * @param selectDate
	 * @param dealerReports
	 * @return
	 */
	private String getWorsePerformingApplicationsPOS(long worsePerformingApplications, Date selectDate,
			List<DealerReport> dealerReports) {
		String noWorstPerformancePOS = "No dealer";
		String worstPerformancePOS = "";
		for (DealerReport dealerReport : dealerReports) {
			DailyReport selectDailyReport = dealerReport.getSelectDailyReport(selectDate);
			long nbApplications = selectDailyReport.getApply() == null ? 0 : selectDailyReport.getApply();
			if (worsePerformingApplications == nbApplications) {
				worstPerformancePOS = dealerReport.getDealer().getNameEn();
			}
		}
		return (worstPerformancePOS.length() == 0) ? noWorstPerformancePOS : worstPerformancePOS.toString();
	}

	/**
	 * 
	 * @param bestPerformingApplications
	 * @param selectDate
	 * @param dealerReports
	 * @return
	 */
	private String getBestPerformingApplicationsPOS(long bestPerformingApplications, Date selectDate,
			List<DealerReport> dealerReports) {
		String noBestPerformancePOS = "No dealer";
		String bestPerformancePOS = "";
		for (DealerReport dealerReport : dealerReports) {
			DailyReport selectDailyReport = dealerReport.getSelectDailyReport(selectDate);
			long nbApplications = selectDailyReport.getApply() == null ? 0 : selectDailyReport.getApply();
			if (bestPerformingApplications == nbApplications) {
				bestPerformancePOS = dealerReport.getDealer().getNameEn();
			}
		}
		return (bestPerformancePOS.length() == 0) ? noBestPerformancePOS : bestPerformancePOS.toString();
	}

	/**
	 * @param ref
	 * @return
	 */
	private int roundInt(double ref) {
		return (int) MyMathUtils.roundTo(ref, 0);
	}
	
	/**
	 * @param ref
	 * @return
	 */
	private long roundLong(double ref) {
		return (long) MyMathUtils.roundTo(ref, 0);
	}
	
	/**
	 * @param ref
	 * @return
	 */
	private double roundDouble(double ref) {
		return (double) MyMathUtils.roundTo(ref, 0) / 100;
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
		itemFont.setColor(fonCorlor);

		CellStyle style = wb.createCellStyle();
		DataFormat format = wb.createDataFormat();
		style.setAlignment(alignment);
		style.setFont(itemFont);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		cell.setCellStyle(styles.get(HEADER));

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
			style.setDataFormat(format.getFormat("##0%"));
			cell.setCellValue((Double) value);
			cell.setCellStyle(style);
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		} else if (value instanceof String) {
			cell.setCellValue(value.toString());
		} 

		if (hasBorder) {
			style.setBorderTop(CellStyle.BORDER_THIN);
			style.setBorderLeft(CellStyle.BORDER_THIN);
			style.setBorderRight(CellStyle.BORDER_THIN);
			style.setBorderBottom(CellStyle.BORDER_THIN);
			style.setBottomBorderColor(FC_BLACK);
		}
		if (isBold) {
			style.setBorderRight(CellStyle.BORDER_THIN);
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
		cell.setCellStyle(style);
		return cell;
	}

	/**
	 * 
	 * @param row
	 * @param iCol
	 * @param value
	 * @return
	 */
	private Cell createCellBorderBottomAndRight(final Row row, final int iCol,
			final Object value) {
		final Cell cell = row.createCell(iCol);

		if (value instanceof Integer) {
			cell.setCellValue(Integer.valueOf(value.toString()));
		} else {
			cell.setCellValue((value == null ? "" : value.toString()));
		}

		cell.setCellStyle(styles.get(HEADER));
		return cell;
	}

	/**
	 * 
	 * @param row
	 * @param iCol
	 * @param value
	 * @param hasBorder
	 * @param isTotalRow
	 * @return
	 */
	private Cell createNumericCell(final Row row, final int iCol,
			final Object value, String hasBorder, boolean isTotalRow) {
		final Cell cell = row.createCell(iCol);
		// convert from general to number in excel for all number (integer,
		// double, long)
		if (value == null) {
			cell.setCellValue("");
		} else if (value instanceof Integer) {
			cell.setCellValue(Integer.valueOf(value.toString()));
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		} else if (value instanceof Long) {
			cell.setCellValue(Long.valueOf(value.toString()));
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		} else if (value instanceof String) {
			cell.setCellValue(value.toString());
		}
		if (value instanceof Double) {
			cell.setCellValue((Double) value);
			cell.setCellStyle(styles.get(AMOUNT));
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		} else {
			cell.setCellStyle(styles.get(BODY));
		}

		if (hasBorder.equals(LEFT_BORDER_ALIGN_LEFT)) {

			cell.setCellStyle(styles.get(LEFT_BORDER_ALIGN_LEFT));

		} else if (hasBorder.equals(RIGHT_BORDER) && !isTotalRow) {

			cell.setCellStyle(styles.get(RIGHT_BORDER));

		} else if (hasBorder.equals(RIGHT_BORDER) && isTotalRow) {

			cell.setCellStyle(styles.get(TOP_RIGHT_BOTTOM_BORDER_ALIGN_RIGHT));

		} else if (hasBorder.equals(TOP_LEFT_BOTTOM_BORDER_ALIGN_LEFT)) {

			cell.setCellStyle(styles.get(TOP_LEFT_BOTTOM_BORDER_ALIGN_LEFT));

		} else if (hasBorder.equals(NONE_BORDER_ALIGN_RIGHT_PERCENTAGE) && !isTotalRow) {

			cell.setCellStyle(styles.get(NONE_BORDER_ALIGN_RIGHT_PERCENTAGE));

		} else if (hasBorder.equals(NONE_BORDER_ALIGN_RIGHT_PERCENTAGE) && isTotalRow) {

			cell.setCellStyle(styles.get(TOP_BOTTOM_BORDER_ALIGN_RIGHT_PERCENTAGE));

		} else if (hasBorder.equals(NONE_BORDER_ALIGN_RIGHT) && !isTotalRow) {

			cell.setCellStyle(styles.get(NONE_BORDER_ALIGN_RIGHT));
		} else if (hasBorder.equals(NONE_BORDER_ALIGN_RIGHT) && isTotalRow) {

			cell.setCellStyle(styles.get(TOP_BOTTOM_BORDER_ALIGN_RIGHT));

		}

		return cell;
	}

	/**
	 * 
	 * @return
	 */
	private Map<String, CellStyle> createStyles() {
		final Font itemFont = wb.createFont();
		itemFont.setFontHeightInPoints((short) 15);
		itemFont.setFontName("Arial");

		CellStyle style;
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
		style.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.BORDER_DOUBLE);
		style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setLocked(true);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(itemFont);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(FC_BLACK);
		styles.put(HEADER, style);

		style = wb.createCellStyle();
		cellStyleBorder(style, itemFont, LEFT_BORDER_ALIGN_LEFT);
		cellStyleBorder(style, itemFont, RIGHT_BORDER);
		cellStyleBorder(style, itemFont, TOP_LEFT_BOTTOM_BORDER_ALIGN_LEFT);
		cellStyleBorder(style, itemFont, TOP_RIGHT_BOTTOM_BORDER_ALIGN_RIGHT);
		cellStyleBorder(style, itemFont, TOP_BOTTOM_BORDER_ALIGN_RIGHT_PERCENTAGE);
		cellStyleBorder(style, itemFont, TOP_BOTTOM_BORDER_ALIGN_RIGHT);
		cellStyleBorder(style, itemFont, NONE_BORDER_ALIGN_RIGHT_PERCENTAGE);
		cellStyleBorder(style, itemFont, NONE_BORDER_ALIGN_RIGHT);

		return styles;
	}

	/**
	 * 
	 * @param style
	 * @param itemFont
	 * @param hasBorder
	 */
	private void cellStyleBorder(CellStyle style, Font itemFont, String hasBorder) {
		DataFormat format = wb.createDataFormat();
		style = wb.createCellStyle();
		style.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.BIG_SPOTS);
		style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setLocked(true);
		style.setFont(itemFont);
		if (hasBorder.equals(LEFT_BORDER_ALIGN_LEFT)) {

			style.setAlignment(CellStyle.ALIGN_LEFT);
			style.setBorderLeft(CellStyle.BORDER_THIN);
			style.setDataFormat(format.getFormat(DEFAULT_DATE_FORMAT));

		} else if (hasBorder.equals(RIGHT_BORDER)) {

			style.setAlignment(CellStyle.ALIGN_RIGHT);
			style.setBorderRight(CellStyle.BORDER_THIN);

		} else if (hasBorder.equals(TOP_LEFT_BOTTOM_BORDER_ALIGN_LEFT)) {

			style.setAlignment(CellStyle.ALIGN_LEFT);
			style.setBorderTop(CellStyle.BORDER_THIN);
			style.setBorderLeft(CellStyle.BORDER_THIN);
			style.setBorderBottom(CellStyle.BORDER_THIN);
			style.setBottomBorderColor(FC_BLACK);

		} else if (hasBorder.equals(TOP_RIGHT_BOTTOM_BORDER_ALIGN_RIGHT)) {

			style.setAlignment(CellStyle.ALIGN_RIGHT);
			style.setBorderTop(CellStyle.BORDER_THIN);
			style.setBorderRight(CellStyle.BORDER_THIN);
			style.setBorderBottom(CellStyle.BORDER_THIN);
			style.setBottomBorderColor(FC_BLACK);

		} else if (hasBorder.endsWith(TOP_BOTTOM_BORDER_ALIGN_RIGHT_PERCENTAGE)) {

			style.setAlignment(CellStyle.ALIGN_RIGHT);
			style.setBorderTop(CellStyle.BORDER_THIN);
			style.setBorderBottom(CellStyle.BORDER_THIN);
			style.setBottomBorderColor(FC_BLACK);
			style.setDataFormat(format.getFormat("##0%"));

		} else if (hasBorder.equals(TOP_BOTTOM_BORDER_ALIGN_RIGHT)) {

			style.setAlignment(CellStyle.ALIGN_RIGHT);
			style.setBorderTop(CellStyle.BORDER_THIN);
			style.setBorderBottom(CellStyle.BORDER_THIN);
			style.setBottomBorderColor(FC_BLACK);
			style.setDataFormat(format.getFormat("0"));

		} else if (hasBorder.equals(NONE_BORDER_ALIGN_RIGHT_PERCENTAGE)) {

			style.setAlignment(CellStyle.ALIGN_RIGHT);
			style.setDataFormat(format.getFormat("##0%"));

		} else if (hasBorder.equals(NONE_BORDER_ALIGN_RIGHT)) {

			style.setAlignment(CellStyle.ALIGN_RIGHT);
			style.setDataFormat(format.getFormat("0"));
		}
		styles.put(hasBorder, style);
	}

	/**
	 * @param selectDate
	 * @param dealerReports
	 * @return
	 */
	private long getNbApplicationsToday(Date selectDate,
			List<DealerReport> dealerReports) {
		long nbNbApplicationsToday = 0;
		for (DealerReport dealerReport : dealerReports) {
			nbNbApplicationsToday += MyNumberUtils.getLong(dealerReport
					.getDailyReportSelectDate(selectDate).getApply());
		}
		return nbNbApplicationsToday;
	}

	/**
	 * @param selectDate
	 * @param dealerReports
	 * @return
	 */
	private long getNbContractsSelectDate(Date selectDate,
			List<DealerReport> dealerReports) {
		long nbContractsToday = 0;
		for (DealerReport dealerReport : dealerReports) {
			nbContractsToday += MyNumberUtils.getLong(dealerReport
					.getDailyReportSelectDate(selectDate).getNewContract());
		}
		return nbContractsToday;
	}

	/**
	 * @param selectDate
	 * @param dealerReports
	 * @return
	 */
	private long getNbContractsOfMonth(Date selectDate,
			List<DealerReport> dealerReports) {
		long nbContractsToday = 0;
		for (DealerReport dealerReport : dealerReports) {
			nbContractsToday += MyNumberUtils.getLong(dealerReport
					.getSelectDailyReport(selectDate).getNewContract());
		}
		return nbContractsToday;
	}

	/**
	 * @see com.nokor.efinance.tools.report.XLSAbstractReportExtractor#getCellStyle(java.lang.String)
	 */
	protected CellStyle getCellStyle(final String styleName) {
		return styles.get(styleName);
	}

	/**
	 * @author ly.youhort
	 */
	private class DealerReport {

		private Dealer dealer;
		private int lowTarget;
		private int highTarget;
		private List<DailyReport> dailyReports;

		/**
		 * @return the dealer
		 */
		public Dealer getDealer() {
			return dealer;
		}

		/**
		 * @param dealer
		 *            the dealer to set
		 */
		public void setDealer(Dealer dealer) {
			this.dealer = dealer;
		}

		/**
		 * @return the lowTarget
		 */
		public int getLowTarget() {
			return lowTarget;
		}

		/**
		 * @param lowTarget
		 *            the lowTarget to set
		 */
		public void setLowTarget(int lowTarget) {
			this.lowTarget = lowTarget;
		}

		/**
		 * @return the highTarget
		 */
		public int getHighTarget() {
			return highTarget;
		}

		/**
		 * @param highTarget
		 *            the highTarget to set
		 */
		public void setHighTarget(int highTarget) {
			this.highTarget = highTarget;
		}

		/**
		 * @return the dailyReports
		 */
		public List<DailyReport> getDailyReports() {
			return dailyReports;
		}

		/**
		 * @param dailyReports
		 *            the dailyReports to set
		 */
		public void setDailyReports(List<DailyReport> dailyReports) {
			this.dailyReports = dailyReports;
		}

		/**
		 * @return
		 */
		public DailyReport getTotalDailyReport() {
			DailyReport totalDailyReport = new DailyReport();
			for (DailyReport dailyReport : dailyReports) {
				totalDailyReport = totalDailyReport.plus(dailyReport);
			}
			return totalDailyReport;
		}

		/**
		 * @return
		 */
		public DailyReport getSelectDailyReport(Date selectDate) {
			DailyReport selectDailyReport = getDefaultDailyReport();
			Date startDate = DateUtils.getDateAtBeginningOfMonth(selectDate);
			for (DailyReport dailyReport : dailyReports) {
				/*
				 * if (DateUtils.isAfterDay(dailyReport.getDate(), startDate) &&
				 * DateUtils.isBeforeDay(dailyReport.getDate(), selectDate)) {
				 * selectDailyReport.plus(dailyReport); }
				 */
				if (DateUtils.getDateWithoutTime(dailyReport.getDate())
						.compareTo(DateUtils.getDateWithoutTime(startDate)) >= 0
						&& DateUtils
								.getDateWithoutTime(dailyReport.getDate())
								.compareTo(
										DateUtils
												.getDateWithoutTime(selectDate)) <= 0) {
					selectDailyReport.plus(dailyReport);
				}
			}
			return selectDailyReport;
		}

		/**
		 * 
		 * @param selectDate
		 * @return
		 */
		public DailyReport getDailyReportSelectDate(Date selectDate) {
			DailyReport selectDailyReport = getDefaultDailyReport();
			for (DailyReport dailyReport : dailyReports) {
				if (DateUtils.isSameDay(selectDate, dailyReport.getDate())) {
					selectDailyReport = dailyReport;
					break;
				}
			}
			return selectDailyReport;
		}
	}

	/**
	 * @author youhort.ly
	 */
	private class DailyReportComparator implements Comparator<DailyReport> {
		@Override
		public int compare(DailyReport o1, DailyReport o2) {

			if (o1.getDate() == null && o2.getDate() != null) {
				return -1;
			}
			if (o1.getDate() != null && o2.getDate() == null) {
				return 1;
			}
			if (o1.getDate() == null && o2.getDate() == null) {
				return 0;
			}
			return o1.getDate().compareTo(o2.getDate());
		}
	}
}
