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
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.quotation.QuotationService;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.efinance.core.shared.util.DateFilterUtil;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.efinance.glf.statistic.model.Statistic3HoursVisitor;
import com.nokor.efinance.glf.statistic.model.StatisticConfig;
import com.nokor.efinance.tools.report.Report;
import com.nokor.efinance.tools.report.XLSAbstractReportExtractor;

/**
 * @author ly.youhort
 */
public class GLFThreeHours extends XLSAbstractReportExtractor implements Report, GLFApplicantFields, QuotationEntityField {

	protected QuotationService quotationService = SpringUtils.getBean(QuotationService.class);

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

	public GLFThreeHours() {

	}

	@Override
	public String generate(ReportParameter reportParameter) throws Exception {	
		Map<String, Object> parameters = reportParameter.getParameters();
		Date date = (Date) parameters.get("dateValue");
		EDealerType dealerType = (EDealerType) parameters.get("dealerType");
		Dealer dealer = (Dealer) parameters.get("dealer");
		
		BaseRestrictions<Dealer> restrictions = new BaseRestrictions<>(Dealer.class);
		restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
		if (dealerType != null) {
			restrictions.addCriterion("dealerType", dealerType);
		}
		if (dealer != null) {
			restrictions.addCriterion("id", dealer.getId());
		}
		restrictions.addOrder(Order.asc("createDate"));		
		List<Dealer> dealers = quotationService.list(restrictions);
		
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
		sheet.setColumnWidth(0, 1200);
		sheet.setColumnWidth(1, 6400);
		sheet.setColumnWidth(2, 2200);
		sheet.setColumnWidth(3, 2200);
		sheet.setColumnWidth(4, 3550);
		sheet.setColumnWidth(5, 5600);
		sheet.setColumnWidth(6, 1400);
		sheet.setColumnWidth(7, 1400);
		sheet.setColumnWidth(8, 2100);
		sheet.setColumnWidth(9, 2100);
		sheet.setColumnWidth(10, 2100);
		sheet.setColumnWidth(11, 1900);
		sheet.setColumnWidth(12, 2000);
		sheet.setColumnWidth(13, 2100);
		sheet.setColumnWidth(14, 2100);
		sheet.setColumnWidth(15, 1800);
		sheet.setColumnWidth(16, 2200);
		sheet.setColumnWidth(17, 2100);
		sheet.setColumnWidth(18, 2100);
		sheet.setColumnWidth(19, 2000);
		
		sheet.setZoom(7, 10);
		final PrintSetup printSetup = sheet.getPrintSetup();

		printSetup.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
		printSetup.setLandscape(true);

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
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 18));
		createCell(headerRow, 0, "Three-Hours Report", 14, true, false, false, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK,false);
		headerRow = sheet.createRow(1);
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 18));
		createCell(headerRow, 0, "Date :"+ DateUtils.getDateLabel(date, "dd-MMM-yyyy"), 14, true, false, false, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK,false);
		headerRow = sheet.createRow(2);
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 18));
		createCell(headerRow, 0, "Time :" + DateUtils.formatDate(DateUtils.today(), "HH:mm a"), 14, true, false, false, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK,false);

		int iRow = 2;
		int iCol = 0;
		iCol = iCol + 2;
		iCol = 0;
		iRow = iRow + 1;
		if (date == null) {
			date = DateUtils.today();
		}
		iRow = dataTable(sheet, iRow, style, dealers, date);

//		Row tmpRowEnd = sheet.createRow(iRow++);
//		tmpRowEnd.setRowStyle(style);

		iRow = iRow + 1;

		String fileName = writeXLSData("Three-Hours_" + DateUtils.getDateLabel(DateUtils.today(), "yyyyMMddHHmmssSSS") + ".xlsx");

		return fileName;
	}

	private int dataTable(final Sheet sheet, int iRow, final CellStyle style, List<Dealer> dealers, Date date) throws Exception {
		/* Create total data header */
		Format formatter = new SimpleDateFormat("MMMM");  
		String month = formatter.format(date);
		int iCol = 0;
		Row tmpRow = sheet.createRow(iRow++);
		createCell(tmpRow, iCol++, "NO.", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow, iCol - 1, iCol - 1));
		
		createCell(tmpRow, iCol++, "BRANCH", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow, iCol - 1, iCol - 1));
		
		createCell(tmpRow, iCol++, "TODAY APPLY", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow, iCol - 1, iCol - 1));
		
		createCell(tmpRow, iCol++, "TOTAL APPLIED", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow, iCol - 1, iCol - 1));
		
		createCell(tmpRow, iCol++, "NEW CONTRACTS TODAY", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow, iCol - 1, iCol - 1));
		
		createCell(tmpRow, iCol++, "NEW CONTRACTS IN " + month.toUpperCase(), 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow, iCol - 1, iCol - 1));
		
		createCell(tmpRow, 6, "TARGET", 10, false, true, true,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, 6, 7));
		createCell(tmpRow, 7, "", 10, false, true, true,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
		
		createCell(tmpRow, 8, "HONDA VISITOR", 10, false, true, true,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, 8, 11));
		createCell(tmpRow, 9, "", 10, false, true, true,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
		createCell(tmpRow, 10, "", 10, false, true, true,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
		createCell(tmpRow, 11, "", 10, false, true, true,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
		
		createCell(tmpRow, 12, "GLF VISITOR", 10, false, true, true,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, 12, 15));
		createCell(tmpRow, 13, "", 10, false, true, true,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		createCell(tmpRow, 14, "", 10, false, true, true,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		createCell(tmpRow, 15, "", 10, false, true, true,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		
		createCell(tmpRow, 16, "Applied", 10, false, true, true,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, 16, 19));
		createCell(tmpRow, 17, "", 10, false, true, true,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		createCell(tmpRow, 18, "", 10, false, true, true,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		createCell(tmpRow, 19, "", 10, false, true, true,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		
		iCol = 6;
		tmpRow = sheet.createRow(iRow++);
		int iColBorder = 0;
		createCellBorderBottomAndRight(tmpRow, iColBorder++, "", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		createCellBorderBottomAndRight(tmpRow, iColBorder++, "", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		createCellBorderBottomAndRight(tmpRow, iColBorder++, "", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		createCellBorderBottomAndRight(tmpRow, iColBorder++, "", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		createCellBorderBottomAndRight(tmpRow, iColBorder++, "", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		createCellBorderBottomAndRight(tmpRow, iColBorder++, "", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		
		createCell(tmpRow, iCol++, "LOW", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		createCell(tmpRow, iCol++, "HIGH", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		createCell(tmpRow, iCol++, "11:00 AM", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		createCell(tmpRow, iCol++, "14:00 PM", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		createCell(tmpRow, iCol++, "17:00 PM", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		createCell(tmpRow, iCol++, "TOTAL", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		createCell(tmpRow, iCol++, "11:00 AM", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		createCell(tmpRow, iCol++, "14:00 PM", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		createCell(tmpRow, iCol++, "17:00 PM", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		createCell(tmpRow, iCol++, "TOTAL", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		
		createCell(tmpRow, iCol++, "11:00 AM", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		createCell(tmpRow, iCol++, "14:00 PM", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		createCell(tmpRow, iCol++, "17:00 PM", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		createCell(tmpRow, iCol++, "TOTAL", 10, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		
		if (dealers != null && !dealers.isEmpty()) {
			int nbRows = dealers.size();
			int totalNumQuotationApplyToday = 0;
			int totalNumQuotationTotalApply = 0;
			int totalNumNewContractInMonth = 0;
			int totalNumNewContractToday = 0;
			
			int totalNumDealerVisitor11 = 0;
	    	int totalNumCompanyVisitor11 = 0;
	    	int totalNumApplied11 = 0;
			
	    	int totalNumDealerVisitor14 = 0;
	    	int totalNumCompanyVisitor14 = 0;
	    	int totalNumApplied14 = 0;
	    	
	    	int totalNumDealerVisitor17 = 0;
	    	int totalNumCompanyVisitor17 = 0;
	    	int totalNumApplied17 = 0;
	    	
			for (int i = 0; i < nbRows; i++) {
				Dealer dealer = dealers.get(i);
				BaseRestrictions<StatisticConfig> restrictions = new BaseRestrictions<>(StatisticConfig.class);		
				restrictions.addCriterion(Restrictions.eq("dealer.id", dealer.getId()));
				restrictions.addCriterion(Restrictions.eq("startDate", DateUtils.getDateAtBeginningOfMonth(date)));
				List<StatisticConfig> statisticConfigs = quotationService.list(restrictions);
				int targetLow = 0;
				int targetHigh = 0;
				if (statisticConfigs != null && !statisticConfigs.isEmpty()) {
					targetLow = statisticConfigs.get(0).getTargetLow(); 
					targetHigh = statisticConfigs.get(0).getTargetHigh();
					
				}
				tmpRow = sheet.createRow(iRow++);
				iCol = 0;
				
				int numApplied11 = 0;
		    	int numApplied14 = 0;
		    	int numApplied17 = 0;
		    	
				List<Quotation> applied3Hours = getNumApplied(dealer, date, date);
				if(applied3Hours != null && !applied3Hours.isEmpty()){
					for (Quotation applied3Hour : applied3Hours) {

				    	Calendar calendar11 = Calendar.getInstance();
				    	calendar11.setTime(applied3Hour.getFirstSubmissionDate());
						calendar11.set(Calendar.HOUR_OF_DAY, 11);
						calendar11.set(Calendar.MINUTE, 0);
						
						Calendar calendar14 = Calendar.getInstance();
						calendar14.setTime(applied3Hour.getFirstSubmissionDate());
						calendar14.set(Calendar.HOUR_OF_DAY, 14);
						calendar14.set(Calendar.MINUTE, 0);
						
						if(applied3Hour.getFirstSubmissionDate().before(calendar11.getTime())){
							numApplied11 += 1;
						}else if(applied3Hour.getFirstSubmissionDate().before(calendar14.getTime())){
							numApplied14 += 1;
						}else{
							numApplied17 +=1;
						}
					}
					totalNumApplied11 += numApplied11;
					totalNumApplied14 += numApplied14;
					totalNumApplied17 += numApplied17;
				}
				
				long numQuotationApplyToday = getNumQuotationApplyToday(dealer, date, date);
				totalNumQuotationApplyToday += numQuotationApplyToday;
				
				long numQuotationTotalApply = getNumQuotationTotalApply(dealer, date);
				totalNumQuotationTotalApply += numQuotationTotalApply;
				
				long numNewContractInMonth = getNumNewContractInMonth(dealer, date);
				totalNumNewContractInMonth += numNewContractInMonth;
				
				long numNewContractToday = getNumNewContractToday(dealer, date);
				totalNumNewContractToday += numNewContractToday;
				
				createNumericCell(tmpRow, iCol++, (i + 1), true,
						CellStyle.ALIGN_CENTER, 10, false, true, BG_WHITE, FC_BLACK);
				createNumericCell(tmpRow, iCol++,getDefaultString(dealer.getNameEn()), true,
						CellStyle.ALIGN_CENTER, 10, false, true, BG_WHITE, FC_BLACK);
				createNumericCell(tmpRow, iCol++, numQuotationApplyToday, true,
						CellStyle.ALIGN_CENTER, 10, false, true, BG_WHITE, FC_BLACK);
				createNumericCell(tmpRow, iCol++, numQuotationTotalApply, true,
						CellStyle.ALIGN_CENTER, 10, false, true, BG_WHITE, FC_BLACK);
				createNumericCell(tmpRow, iCol++, numNewContractToday, true,
						CellStyle.ALIGN_CENTER, 10, false, true, BG_WHITE, FC_BLACK);
				createNumericCell(tmpRow, iCol++, numNewContractInMonth, true,
						CellStyle.ALIGN_CENTER, 10, false, true, BG_WHITE, FC_BLACK);
				createNumericCell(tmpRow, iCol++, (targetLow == 0 ? "" : targetLow), true,
						CellStyle.ALIGN_CENTER, 10, false, true, BG_WHITE, FC_BLACK);
				createNumericCell(tmpRow, iCol++, (targetHigh == 0 ? "" : targetHigh), true,
						CellStyle.ALIGN_CENTER, 10, false, true, BG_WHITE, FC_BLACK);
							
				BaseRestrictions<Statistic3HoursVisitor> restrictions1 = new BaseRestrictions<>(Statistic3HoursVisitor.class);
				restrictions1.addCriterion(Restrictions.ge("date", DateUtils.getDateAtBeginningOfDay(date)));
				restrictions1.addCriterion(Restrictions.le("date", DateUtils.getDateAtEndOfDay(date)));
				restrictions1.addCriterion(Restrictions.eq("dealer.id", dealer.getId()));
				List<Statistic3HoursVisitor> statisticVisitors = quotationService.list(restrictions1);
		    	int numDealervisitor11 = 0;
		    	int numDealervisitor14 = 0;
		    	int numDealervisitor17 = 0;
		    	int numCompanyVisitor11 = 0;
		    	int numCompanyVisitor14 = 0;
		    	int numCompanyVisitor17 = 0;
		    	
				if (statisticVisitors != null && !statisticVisitors.isEmpty()) {
					for (Statistic3HoursVisitor statisticVisitor : statisticVisitors) {
						totalNumDealerVisitor11 += MyNumberUtils.getInteger(statisticVisitor.getNumberVisitorDealer11());
						totalNumDealerVisitor14 += MyNumberUtils.getInteger(statisticVisitor.getNumberVisitorDealer14());
						totalNumDealerVisitor17 += MyNumberUtils.getInteger(statisticVisitor.getNumberVisitorDealer17());
						totalNumCompanyVisitor11 += MyNumberUtils.getInteger(statisticVisitor.getNumberVisitorCompany11());
						totalNumCompanyVisitor14 += MyNumberUtils.getInteger(statisticVisitor.getNumberVisitorCompany14());
						totalNumCompanyVisitor17 += MyNumberUtils.getInteger(statisticVisitor.getNumberVisitorCompany17());
						
						numDealervisitor11 += MyNumberUtils.getInteger(statisticVisitor.getNumberVisitorDealer11());
						numDealervisitor14 += MyNumberUtils.getInteger(statisticVisitor.getNumberVisitorDealer14());
						numDealervisitor17 += MyNumberUtils.getInteger(statisticVisitor.getNumberVisitorDealer17());
						numCompanyVisitor11 += MyNumberUtils.getInteger(statisticVisitor.getNumberVisitorCompany11());
						numCompanyVisitor14 += MyNumberUtils.getInteger(statisticVisitor.getNumberVisitorCompany14());
						numCompanyVisitor17 += MyNumberUtils.getInteger(statisticVisitor.getNumberVisitorCompany17());
					}
				}
		    	
				createNumericCell(tmpRow, iCol++, numDealervisitor11, true,
						CellStyle.ALIGN_CENTER, 10, false, true, BG_WHITE, FC_BLACK);
				createNumericCell(tmpRow, iCol++, numDealervisitor14, true,
						CellStyle.ALIGN_CENTER, 10, false, true, BG_WHITE, FC_BLACK);
				createNumericCell(tmpRow, iCol++, numDealervisitor17, true,
						CellStyle.ALIGN_CENTER, 10, false, true, BG_WHITE, FC_BLACK);
				createNumericCell(tmpRow, iCol++, numDealervisitor11 + numDealervisitor14 + numDealervisitor17, true,
						CellStyle.ALIGN_CENTER, 10, false, true, BG_WHITE, FC_BLACK);
				
				createNumericCell(tmpRow, iCol++, numCompanyVisitor11, true,
						CellStyle.ALIGN_CENTER, 10, false, true, BG_WHITE, FC_BLACK);
				createNumericCell(tmpRow, iCol++, numCompanyVisitor14, true,
						CellStyle.ALIGN_CENTER, 10, false, true, BG_WHITE, FC_BLACK);
				createNumericCell(tmpRow, iCol++, numCompanyVisitor17, true,
						CellStyle.ALIGN_CENTER, 10, false, true, BG_WHITE, FC_BLACK);
				createNumericCell(tmpRow, iCol++, numCompanyVisitor11 + numCompanyVisitor14 + numCompanyVisitor17, true,
						CellStyle.ALIGN_CENTER, 10, false, true, BG_WHITE, FC_BLACK);
				
				createNumericCell(tmpRow, iCol++, numApplied11, true,
						CellStyle.ALIGN_CENTER, 10, false, true, BG_WHITE, FC_BLACK);
				createNumericCell(tmpRow, iCol++, numApplied14, true,
						CellStyle.ALIGN_CENTER, 10, false, true, BG_WHITE, FC_BLACK);
				createNumericCell(tmpRow, iCol++, numApplied17, true,
						CellStyle.ALIGN_CENTER, 10, false, true, BG_WHITE, FC_BLACK);
				createNumericCell(tmpRow, iCol++, numApplied11 + numApplied14 + numApplied17, true,
						CellStyle.ALIGN_CENTER, 10, false, true, BG_WHITE, FC_BLACK);
			}

			tmpRow = sheet.createRow(iRow++);
			iCol = 0;
			createNumericCell(tmpRow, iCol++, "", true,
					CellStyle.ALIGN_CENTER, 10, false, true, BG_WHITE, FC_BLACK);
			createNumericCell(tmpRow, iCol++, "Total", true,
					CellStyle.ALIGN_CENTER, 10, false, true, BG_WHITE, FC_BLACK);
			createNumericCell(tmpRow, iCol++, totalNumQuotationApplyToday, true,
					CellStyle.ALIGN_CENTER, 10, false, true, BG_WHITE, FC_BLACK);
			createNumericCell(tmpRow, iCol++, totalNumQuotationTotalApply, true,
					CellStyle.ALIGN_CENTER, 10, false, true, BG_WHITE, FC_BLACK);
			createNumericCell(tmpRow, iCol++, totalNumNewContractToday, true,
					CellStyle.ALIGN_CENTER, 10, false, true, BG_WHITE, FC_BLACK);
			createNumericCell(tmpRow, iCol++, totalNumNewContractInMonth, true,
					CellStyle.ALIGN_CENTER, 10, false, true, BG_WHITE, FC_BLACK);
			createNumericCell(tmpRow, iCol++, "", true,
					CellStyle.ALIGN_CENTER, 10, false, true, BG_WHITE, FC_BLACK);
			createNumericCell(tmpRow, iCol++, "", true,
					CellStyle.ALIGN_CENTER, 10, false, true, BG_WHITE, FC_BLACK);
			
			createNumericCell(tmpRow, iCol++, totalNumDealerVisitor11, true,
					CellStyle.ALIGN_CENTER, 10, false, true, BG_WHITE, FC_BLACK);
			createNumericCell(tmpRow, iCol++, totalNumDealerVisitor14, true,
					CellStyle.ALIGN_CENTER, 10, false, true, BG_WHITE, FC_BLACK);
			createNumericCell(tmpRow, iCol++, totalNumDealerVisitor17, true,
					CellStyle.ALIGN_CENTER, 10, false, true, BG_WHITE, FC_BLACK);
			createNumericCell(tmpRow, iCol++, totalNumDealerVisitor11 + totalNumDealerVisitor14 + totalNumDealerVisitor17, true,
					CellStyle.ALIGN_CENTER, 10, false, true, BG_WHITE, FC_BLACK);
			
			createNumericCell(tmpRow, iCol++, totalNumCompanyVisitor11, true,
					CellStyle.ALIGN_CENTER, 10, false, true, BG_WHITE, FC_BLACK);
			createNumericCell(tmpRow, iCol++, totalNumCompanyVisitor14, true,
					CellStyle.ALIGN_CENTER, 10, false, true, BG_WHITE, FC_BLACK);
			createNumericCell(tmpRow, iCol++, totalNumCompanyVisitor17, true,
					CellStyle.ALIGN_CENTER, 10, false, true, BG_WHITE, FC_BLACK);
			createNumericCell(tmpRow, iCol++, totalNumCompanyVisitor11 + totalNumCompanyVisitor14 + totalNumCompanyVisitor17, true,
					CellStyle.ALIGN_CENTER, 10, false, true, BG_WHITE, FC_BLACK);
			
			createNumericCell(tmpRow, iCol++, totalNumApplied11, true,
					CellStyle.ALIGN_CENTER, 10, false, true, BG_WHITE, FC_BLACK);
			createNumericCell(tmpRow, iCol++, totalNumApplied14, true,
					CellStyle.ALIGN_CENTER, 10, false, true, BG_WHITE, FC_BLACK);
			createNumericCell(tmpRow, iCol++, totalNumApplied17, true,
					CellStyle.ALIGN_CENTER, 10, false, true, BG_WHITE, FC_BLACK);
			createNumericCell(tmpRow, iCol++, totalNumApplied11 + totalNumApplied14 + totalNumApplied17, true,
					CellStyle.ALIGN_CENTER, 10, false, true, BG_WHITE, FC_BLACK);

			iRow = iRow + 1;
		}

		return iRow;

	}

	/**
	 * 
	 * @param dealer
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private int getNumQuotationApplyToday(Dealer dealer, Date startDate, Date endDate) {
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<Quotation>(Quotation.class);
		restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));
		restrictions.addCriterion(Restrictions.ge("firstSubmissionDate", DateFilterUtil.getStartDate(startDate)));
		restrictions.addCriterion(Restrictions.le("firstSubmissionDate", DateFilterUtil.getEndDate(startDate)));
		return (int) quotationService.count(restrictions);
	}
	
	/**
	 * 
	 * @param dealer
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private List<Quotation> getNumApplied(Dealer dealer, Date startDate, Date endDate) {
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<Quotation>(Quotation.class);
		restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));
		restrictions.addCriterion(Restrictions.ge("firstSubmissionDate", DateFilterUtil.getStartDate(startDate)));
		restrictions.addCriterion(Restrictions.le("firstSubmissionDate", DateFilterUtil.getEndDate(startDate)));
		return quotationService.list(restrictions);
	}
	
	
	
	/**
	 * @param dealer
	 * @param date
	 * @return
	 */
	private long getNumQuotationTotalApply(Dealer dealer, Date date) {
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<Quotation>(Quotation.class);
		restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));
		restrictions.addCriterion(Restrictions.ge("firstSubmissionDate", DateFilterUtil.getStartDate(DateUtils.getDateAtBeginningOfMonth(date))));
		restrictions.addCriterion(Restrictions.le("firstSubmissionDate", DateFilterUtil.getEndDate(date)));
		return quotationService.count(restrictions);
	}
	
	/**
	 * @param dealer
	 * @param date
	 * @return
	 */
	private long getNumNewContractInMonth(Dealer dealer, Date date) {
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(Quotation.class);
		restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));
		restrictions.addCriterion(Restrictions.eq(WKF_STATUS, QuotationWkfStatus.ACT));
		restrictions.addCriterion(Restrictions.ge("activationDate", DateFilterUtil.getStartDate(DateUtils.getDateAtBeginningOfMonth(date))));
		restrictions.addCriterion(Restrictions.le("activationDate", DateFilterUtil.getEndDate(date)));
		return quotationService.count(restrictions);
	}
	
	/**
	 * @param dealer
	 * @param date
	 * @return
	 */
	private long getNumNewContractToday(Dealer dealer, Date date) {
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(Quotation.class);
		restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));
		restrictions.addCriterion(Restrictions.eq(WKF_STATUS, QuotationWkfStatus.ACT));
		restrictions.addCriterion(Restrictions.ge("activationDate", DateFilterUtil.getStartDate(date)));
		restrictions.addCriterion(Restrictions.le("activationDate", DateFilterUtil.getEndDate(date)));
		return quotationService.count(restrictions);
	}
	/**
	 * 
	 * @param strValue
	 * @return
	 */
	private String getDefaultString(String strValue) {
		if (strValue == null) {
			return "";
		} else {
			return strValue;
		}
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
		CellStyle style;
		final Font itemFont = wb.createFont();
		DataFormat format = wb.createDataFormat();
		style = wb.createCellStyle();
		style.setDataFormat(format.getFormat("0"));
		itemFont.setFontHeightInPoints((short) fontsize);
		
		if (value == null) {
			cell.setCellValue("");
		} else if (value instanceof Integer) {
			cell.setCellValue(Integer.valueOf(value.toString()));
			cell.setCellStyle(style);
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		} else if (value instanceof Long) {
			cell.setCellValue(Long.valueOf(value.toString()));
			cell.setCellStyle(style);
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		} else if (value instanceof String) {
			cell.setCellValue(value.toString());
		}else if (value instanceof Double) {
            cell.setCellValue((Double) value);
            cell.setCellStyle(styles.get(AMOUNT));
            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        } else {
        	cell.setCellStyle(styles.get(BODY));	
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
    	style.setAlignment(CellStyle.ALIGN_RIGHT);
    	style.setFont(itemFont);
    	style.setDataFormat(format.getFormat("#,##0.00"));
    	styles.put(AMOUNT, style);

		return styles;
	}

	protected CellStyle getCellStyle(final String styleName) {
		return styles.get(styleName);
	}
    
//    private class Visitor {
//    	
//    	private int numDealerVisitor;
//    	private int numCompanyVisitor;
//    	private int numVisitorApply;
//	
//	 	/**
//		 * @return the numDealerVisitor
//		 */
//		public int getNumDealerVisitor() {
//			return numDealerVisitor;
//		}
//		
//		/**
//		 * @param numDealerVisitor the numDealerVisitor to set
//		 */
//		public void setNumDealerVisitor(int numDealerVisitor) {
//			this.numDealerVisitor = numDealerVisitor;
//		}
//		
//		/**
//		 * @return the numCompanyVisitor
//		 */
//		public int getNumCompanyVisitor() {
//			return numCompanyVisitor;
//		}
//		
//		/**
//		 * @param numCompanyVisitor the numCompanyVisitor to set
//		 */
//		public void setNumCompanyVisitor(int numCompanyVisitor) {
//			this.numCompanyVisitor = numCompanyVisitor;
//		}
//		
//		/**
//		 * @return the numVisitorApply
//		 */
//		public int getNumVisitorApply() {
//			return numVisitorApply;
//		}
//		
//		/**
//		 * @param numVisitorApply the numVisitorApply to set
//		 */
//		public void setNumVisitorApply(int numVisitorApply) {
//			this.numVisitorApply = numVisitorApply;
//		}
//
//		/**
//		 * 
//		 * @param dealer
//		 * @param startDate
//		 * @param endDate
//		 * @return
//		 */
//		private Visitor getNumVisitor(Dealer dealer,Date startDate, Date endDate) {
//			Visitor visitor = new Visitor();
//			int dealerVisitor = 0;
//			int companyVisitor = 0;
//			
//			BaseRestrictions<StatisticVisitor> restrictions = new BaseRestrictions<>(StatisticVisitor.class);
//			restrictions.addCriterion(Restrictions.ge("createDate", DateUtils.getDateAtBeginningOfDay(startDate)));
//			restrictions.addCriterion(Restrictions.le("createDate", DateUtils.getDateAtEndOfDay(endDate)));
//			restrictions.addCriterion(Restrictions.eq("dealer.id", dealer.getId()));
//			
//			List<StatisticVisitor> statisticVisitors = entityService.list(restrictions);
//			if (statisticVisitors != null && !statisticVisitors.isEmpty()) {
//				for (StatisticVisitor staticVisitor : statisticVisitors) {
//					dealerVisitor += staticVisitor.getNumberVisitorDealer11();
//					dealerVisitor += staticVisitor.getNumberVisitorDealer14();
//					dealerVisitor += staticVisitor.getNumberVisitorDealer17();
//					companyVisitor += staticVisitor.getNumberVisitorCompany11();
//					companyVisitor += staticVisitor.getNumberVisitorCompany14();
//					companyVisitor += staticVisitor.getNumberVisitorCompany17();
//				}
//			}
//			visitor.setNumDealerVisitor(dealerVisitor);
//			visitor.setNumCompanyVisitor(companyVisitor);
//			visitor.setNumVisitorApply(getNumApply(dealer, startDate, endDate));
//			return visitor;
//		}
//		/**
//		 * 
//		 * @param date
//		 * @param dealer
//		 * @return number visitor apply
//		 */

//    }
}
