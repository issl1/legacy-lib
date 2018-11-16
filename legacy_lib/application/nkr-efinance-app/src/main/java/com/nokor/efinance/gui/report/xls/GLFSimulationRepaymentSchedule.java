package com.nokor.efinance.gui.report.xls;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
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
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyMathUtils;
import org.seuksa.frmk.tools.MyNumberUtils;

import com.nokor.efinance.core.financial.service.FinanceCalculationService;
import com.nokor.efinance.core.quotation.QuotationService;
import com.nokor.efinance.core.shared.conf.AppConfig;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.efinance.glf.report.xls.GLFPaymentScheduleFields;
import com.nokor.efinance.tools.report.XLSAbstractReportExtractor;
import com.nokor.ersys.core.finance.model.eref.ECurrency;
import com.nokor.finance.services.shared.AmortizationSchedules;
import com.nokor.finance.services.shared.CalculationParameter;
import com.nokor.finance.services.shared.Schedule;
import com.nokor.finance.services.shared.system.EFrequency;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
/**
 * 
 * @author buntha.chea
 *
 */
public class GLFSimulationRepaymentSchedule extends XLSAbstractReportExtractor implements GLFPaymentScheduleFields {

	private Map<String, CellStyle> styles = null;
    private static String FORMAT_PERCENTAGE1 = "###,###,###,##0.0%";
    private static String FORMAT_PERCENTAGE2 = "###,###,##0.00%";
    private static String FORMAT_PERCENTAGE3 = "###,###,###,##0.000%";
    
    private static String FORMAT_DECIMAL1 = "###,###,###,##0.0";
    private static String FORMAT_DECIMAL2 = "###,###,###,##0.00";
    private static String FORMAT_DECIMAL3 = "###,###,###,##0.000";
    public static String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";
	/** Background color format */
    static short BG_WHITE =  IndexedColors.WHITE.getIndex();
    static short BG_GREEN = IndexedColors.GREEN.getIndex();

    /** Font color */
    static short FC_WHITE = IndexedColors.WHITE.getIndex();
    static short FC_BLACK = IndexedColors.BLACK.getIndex();
    static short FC_BLUE = 48;
    static short FC_GREY = IndexedColors.GREY_80_PERCENT.getIndex();
    static short FC_GREEN = IndexedColors.GREEN.getIndex();
    
    private String lesseeName = "";
	
	protected QuotationService quotationService = (QuotationService) SecApplicationContextHolder.getContext().getBean("quotationService");
	protected FinanceCalculationService financeCalculationService = (FinanceCalculationService) SecApplicationContextHolder.getContext().getBean("financeCalculationService");
	private int term;
	private double insuranceFee;
	private double serviceFee;
	private double insurancingFee;
	private double servicingFee;
	private EFrequency frequency;
	public GLFSimulationRepaymentSchedule() {
		
	}
	
	/**
	 * 
	 */
	@Override
	public String generate(ReportParameter reportParameter) throws Exception {
		
		String fileName = ""; 
				
		createWorkbook(null);
		
		
		Double advancePayment = Double.valueOf(reportParameter.getParameters().get("advancePayment").toString());
		Double advancePaymentPercentage = Double.valueOf(reportParameter.getParameters().get("advancePaymentPercentage").toString());
		int termInMonth = Integer.valueOf(reportParameter.getParameters().get("termInMonth").toString());
		Double assetPrice = Double.valueOf(reportParameter.getParameters().get("assetPrice").toString());
		Double lesseeAmount = Double.valueOf(reportParameter.getParameters().get("lesseeAmount").toString());
		Double periodicInterestRate = Double.valueOf(reportParameter.getParameters().get("periodicInterestRate").toString());
		Double installmentAmont = Double.valueOf(reportParameter.getParameters().get("installmentAmont").toString());
	    frequency = (EFrequency) reportParameter.getParameters().get("frequency");		
	    insuranceFee = Double.valueOf(reportParameter.getParameters().get("insuranceFee").toString());
		serviceFee = Double.valueOf(reportParameter.getParameters().get("serviceFee").toString());
	   	
		
		CalculationParameter calculationParameter = new CalculationParameter();
		calculationParameter.setFrequency(frequency);
		calculationParameter.setInitialPrincipal(lesseeAmount);
		calculationParameter.setNumberOfPeriods(termInMonth);
		calculationParameter.setPeriodicInterestRate(periodicInterestRate / 100);
		
		Date contractStartDate = DateUtils.today();
		Date firstPaymentDate = DateUtils.today();
		
	
		AmortizationSchedules amortizationSchedules = financeCalculationService.getAmortizationSchedules(contractStartDate, firstPaymentDate, calculationParameter);
		XSSFSheet sheet = wb.createSheet("Payment Schedule");
		sheet.protectSheet("abcd56abcd");
		sheet.lockDeleteColumns();
        sheet.lockDeleteRows();
        sheet.lockFormatCells();
        sheet.lockFormatColumns();
        sheet.lockFormatRows();
        sheet.lockInsertColumns();
        sheet.lockInsertRows();
        sheet.lockSelectLockedCells();
        sheet.lockSelectUnlockedCells();
		
		CellStyle style = wb.createCellStyle();
		styles = new HashMap<String, CellStyle>();
		createStyles();
		sheet.setColumnWidth(1, 3000);
		sheet.setColumnWidth(2, 4500);
		sheet.setColumnWidth(3, 4000);
		sheet.setColumnWidth(4, 4000);
		sheet.setColumnWidth(5, 4000);
		sheet.setColumnWidth(6, 3900);
		sheet.setColumnWidth(7, 5300);
		sheet.setColumnWidth(8, 1000);
		sheet.setZoom(7, 10);
        final PrintSetup printSetup = sheet.getPrintSetup();

        printSetup.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);

        printSetup.setScale((short) 75);

        //Setup the Page margins - Left, Right, Top and Bottom
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

		InputStream logoInputStream = null;
		try {
			String templatePath = AppConfig.getInstance().getConfiguration().getString("specific.templatedir");
			logoInputStream = new FileInputStream(templatePath + "/GLF-logo.png");
			byte[] logoBytes = IOUtils.toByteArray(logoInputStream);
			int logoIdx = wb.addPicture(logoBytes, Workbook.PICTURE_TYPE_PNG);
			ClientAnchor clientAnchor = wb.getCreationHelper().createClientAnchor();
			clientAnchor.setAnchorType(ClientAnchor.MOVE_DONT_RESIZE);
			clientAnchor.setCol1(0);
			clientAnchor.setCol2(3);
			clientAnchor.setRow1(1);
			clientAnchor.setRow2(5);
			Drawing drawing = sheet.createDrawingPatriarch();
			Picture logo = drawing.createPicture(clientAnchor, logoIdx);
			logo.resize();
		} finally {
			logoInputStream.close();
		}
		
		lesseeName = "Empty";

		final Row headerRow = sheet.createRow(2);
		createCell(headerRow, 3, RSP_HEADER, 14, true, false, CellStyle.ALIGN_LEFT, false, BG_WHITE, FC_BLACK);
		
		int iRow = 6;
		int iCol = 0;
		final Row rspCusNameRow = sheet.createRow(iRow++);
		createCell(rspCusNameRow, iCol, RSP_CUS_NAME, 12, false, false, CellStyle.ALIGN_LEFT, false, BG_WHITE, FC_BLACK);
		iCol = iCol + 2;
		createCell(rspCusNameRow, iCol,lesseeName , 11, false, false, CellStyle.ALIGN_LEFT, false, BG_WHITE, FC_BLUE);
		iCol = 0;
		final Row rspCusAddress1Row = sheet.createRow(iRow++);
		createCell(rspCusAddress1Row, iCol, RSP_CUS_ADDRESS, 12, false, false, CellStyle.ALIGN_LEFT, false, BG_WHITE, FC_BLACK);
		iCol = iCol + 2;
		createCell(rspCusAddress1Row, iCol, "Empty", 11, false, false, CellStyle.ALIGN_LEFT, false, BG_WHITE, FC_BLUE);

		final Row rspCusAddress2Row = sheet.createRow(iRow++);
		iCol = 2;
		createCell(rspCusAddress2Row, iCol, "Empty", 11, false, false, CellStyle.ALIGN_LEFT, false, BG_WHITE, FC_BLUE);

		iCol = iCol + 3;
		createCell(rspCusAddress2Row, iCol, RSP_DEALER_NUMBER, 11, false, false, CellStyle.ALIGN_LEFT, false, BG_WHITE, FC_BLACK);
		iCol = iCol + 1;
		createNumericCell(rspCusAddress2Row, iCol, "Empty", false, CellStyle.ALIGN_LEFT, 11, false, true, BG_WHITE, FC_BLUE);

		iCol = 0;
		final Row rspCusPhoneNumberRow = sheet.createRow(iRow++);
		createCell(rspCusPhoneNumberRow, iCol, RSP_CUS_PHONENUMBER, 12, false, false, CellStyle.ALIGN_LEFT, false, BG_WHITE, FC_BLACK);
		iCol = iCol + 2;
		createCell(rspCusPhoneNumberRow, iCol, "Empty", 11, false, false, CellStyle.ALIGN_LEFT, false, BG_WHITE, FC_BLUE);

		iCol = iCol + 3;
		createCell(rspCusPhoneNumberRow, iCol, RSP_LID_NUMBER, 11, false, false, CellStyle.ALIGN_LEFT, false, BG_WHITE, FC_BLACK);

		iCol = iCol + 1;
		createNumericCell(rspCusPhoneNumberRow, iCol, "Empty", false, CellStyle.ALIGN_LEFT, 11, false, true, BG_WHITE, FC_BLUE);

		/* vehicle */
		iCol = 0;
		iRow = iRow + 2;
		final Row borderLeftTop = sheet.createRow(iRow++);
		borderLeftTop.setRowStyle(style);
		createCell(borderLeftTop, iCol, "", getCellStyle(TOP_LEFT_BORDER));
		for (int i = 1; i < 8; i++) {
			createCell(borderLeftTop, i, "", getCellStyle(TOP_BORDER));
		}
		iCol = 8;
		createCell(borderLeftTop, iCol, "", getCellStyle(TOP_RIGHT_BORDER));
		int fontsize = 10;
		term = termInMonth;
		insurancingFee = getinsuranceFee();
		servicingFee = getServiceFee();	 
		
		
		
		
		// 1
		Row rowModel = sheet.createRow(iRow++);
		iCol = 0;
		createCell(rowModel, iCol, "", getCellStyle(LEFT_BORDER));
		iCol = 2;
		createRichCell(rowModel, iCol, RSP_VHE_MOTORCYCLE_MODEL, 13, RSP_VHE_MOTORCYCLE_MODEL.length(), CellStyle.ALIGN_RIGHT, fontsize);
		iCol = 3;
		createCell(rowModel, iCol, "Empty", 11, false, true, CellStyle.ALIGN_CENTER, false, BG_WHITE, FC_BLUE);

		iCol = 6;
		createRichCell(rowModel, iCol, RSP_VHE_LEASE_AMOUNT, 13, RSP_VHE_LEASE_AMOUNT.length(), CellStyle.ALIGN_RIGHT, fontsize);
		iCol = 7;
		createPriceGeneral(rowModel, iCol, lesseeAmount, true, false, true, CellStyle.ALIGN_CENTER, 11, false, 2, true, BG_WHITE, FC_GREEN);
		iCol = 8;
		createCell(rowModel, iCol, "", getCellStyle(RIGHT_BORDER));
		// 2
		Row rowYear = sheet.createRow(iRow++);
		iCol = 0;
		createCell(rowYear, iCol, "", getCellStyle(LEFT_BORDER));
		iCol = 2;
		createRichCell(rowYear, iCol, RSP_VHE_YEAR, 10, RSP_VHE_YEAR.length(), CellStyle.ALIGN_RIGHT, fontsize);
		iCol = 3;
		createCell(rowYear, iCol, "Empty", 11, false, true, CellStyle.ALIGN_CENTER, false, BG_WHITE, FC_BLUE);

		iCol = 6;
		createRichCell(rowYear, iCol, RSP_VHE_INTEREST_RATE, 23, RSP_VHE_INTEREST_RATE.length(), CellStyle.ALIGN_RIGHT, fontsize);
		iCol = 7;
		createPercentageValueCell(rowYear, iCol, periodicInterestRate / 100, false, true, CellStyle.ALIGN_CENTER, 11, 2, true, true, BG_WHITE, FC_BLUE);
		iCol = 8;
		createCell(rowYear, iCol, "", getCellStyle(RIGHT_BORDER));
		// 3
		Row rowCc = sheet.createRow(iRow++);
		iCol = 0;
		createCell(rowCc, iCol, "", getCellStyle(LEFT_BORDER));
		iCol = 2;
		createRichCell(rowCc, iCol, RSP_VHE_CC, 13, RSP_VHE_CC.length(), CellStyle.ALIGN_RIGHT, fontsize);
		iCol = 3;
		createCell(rowCc, iCol, "Empty", 11, false, true, CellStyle.ALIGN_CENTER, false, BG_WHITE, FC_BLUE);

		
		iCol = 6;
		createRichCell(rowCc, iCol, RSP_NUMBER_OF_PAYMENTS, 18, RSP_NUMBER_OF_PAYMENTS.length(), CellStyle.ALIGN_RIGHT, fontsize);
		iCol = 7;
		createNumericCell(rowCc, iCol, termInMonth, true, CellStyle.ALIGN_CENTER, 11, false, true, BG_WHITE, FC_BLUE);
		iCol = 8;
		createCell(rowCc, iCol, "", getCellStyle(RIGHT_BORDER));
		// 4
		Row rowVehCashPice = sheet.createRow(iRow++);
		iCol = 0;
		createCell(rowVehCashPice, iCol, "", getCellStyle(LEFT_BORDER));
		iCol = 2;
		createRichCell(rowVehCashPice, iCol, RSP_VHE_PRICE, 11, RSP_VHE_PRICE.length(), CellStyle.ALIGN_RIGHT, fontsize);
		iCol = 3;
		createPriceGeneral(rowVehCashPice, iCol, assetPrice, true, false, true, CellStyle.ALIGN_CENTER, 11, false, 2, true, BG_WHITE, FC_GREEN);
		
		iCol = 6;
		createRichCell(rowVehCashPice, iCol, RSP_MONTHLY_INSTALMENT_AMOUNT, 13, RSP_MONTHLY_INSTALMENT_AMOUNT.length(), CellStyle.ALIGN_RIGHT, fontsize);
		iCol = 7;
		createPriceGeneral(rowVehCashPice, iCol, installmentAmont, true, false, true, CellStyle.ALIGN_CENTER, 11, false, 2, true, BG_WHITE, FC_GREEN);
		iCol = 8;
		createCell(rowVehCashPice, iCol, "", getCellStyle(RIGHT_BORDER));
		// 5
	
		Row rowAdvancePaymentRate = sheet.createRow(iRow++);
		iCol = 0;
		createCell(rowAdvancePaymentRate, iCol, "", getCellStyle(LEFT_BORDER));
		iCol = 2;
		createRichCell(rowAdvancePaymentRate, iCol, RSP_ADVANCE_PAYMENT_RATE, 19,  RSP_ADVANCE_PAYMENT_RATE.length(), CellStyle.ALIGN_RIGHT, fontsize);
		iCol = 3;
		createPercentageValueCell(rowAdvancePaymentRate, iCol,advancePaymentPercentage / 100, false, true, CellStyle.ALIGN_CENTER, 11, 2, true, true, BG_WHITE, FC_BLUE);
		
		iCol = 6;
		createRichCell(rowAdvancePaymentRate, iCol, RSP_VHE_CONTRACT_DATE, 17, RSP_VHE_CONTRACT_DATE.length(), CellStyle.ALIGN_RIGHT, fontsize);
		iCol = 7;
		createNumericCell(rowAdvancePaymentRate, iCol, "Empty", true, CellStyle.ALIGN_CENTER, 11, false, true, BG_WHITE, FC_BLUE);		
        iCol = 8;
		createCell(rowAdvancePaymentRate, iCol, "", getCellStyle(RIGHT_BORDER));

		// 6
		
		Row rowAdvancePayment = sheet.createRow(iRow++);
		iCol = 0;
		createCell(rowAdvancePayment, iCol, "", getCellStyle(LEFT_BORDER));
		iCol = 2;
		createRichCell(rowAdvancePayment, iCol, RSP_ADVANCE_PAYMENT, 14, RSP_ADVANCE_PAYMENT.length(), CellStyle.ALIGN_RIGHT, fontsize);
		iCol = 3;
		createPriceGeneral(rowAdvancePayment, iCol, MyNumberUtils.getDouble(advancePayment), true, false, true, CellStyle.ALIGN_CENTER, 11, false, 2, true, BG_WHITE, FC_GREEN);
		
		iCol = 6;
		createRichCell(rowAdvancePayment, iCol, RSP_VHE_FIRST_PAYMENT_DATE, 15, RSP_VHE_FIRST_PAYMENT_DATE.length(), CellStyle.ALIGN_RIGHT, fontsize);
		iCol = 7;
		createNumericCell(rowAdvancePayment, iCol, "Empty", true, CellStyle.ALIGN_CENTER, 11, false, true, BG_WHITE, FC_BLUE);		
		iCol = 8;		
        createCell(rowAdvancePayment, iCol, "", getCellStyle(RIGHT_BORDER));

		final Row buttom_border = sheet.createRow(iRow++);
		buttom_border.setRowStyle(style);
		iCol = 0;
		createCell(buttom_border, iCol, "", getCellStyle(BUTTOM_LEFT_BORDER));

		for (int i = 1; i < 8; i++) {
			createCell(buttom_border, i, "", getCellStyle(BUTTOM_BORDER));
		}
		iCol = 8;
		createCell(buttom_border, iCol, "", getCellStyle(BUTTOM_RIGHT_BORDER));
		/* end vehicle */
		iRow = iRow + 1;
		
		int nbSchedulePerYear = frequency.getNbSchedulePerYear();
		int nbYear = amortizationSchedules.getSchedules().size() / nbSchedulePerYear;
		if (amortizationSchedules.getSchedules().size() % nbSchedulePerYear != 0) {
			nbYear = 1;
		}			
		
		List<Date> insuranceDates = new ArrayList<Date>();
		for (int i = 1; i <= nbYear; i++) {
			List<Schedule> schedules = new ArrayList<Schedule>();
			int lastIndex = i * nbSchedulePerYear;
			if (lastIndex > amortizationSchedules.getSchedules().size()) {
				lastIndex = amortizationSchedules.getSchedules().size();
			}
			for (int j = (i - 1) * nbSchedulePerYear; j < lastIndex; j++) {
				schedules.add(amortizationSchedules.getSchedules().get(j));
			}
			
			iRow = paymentTable(sheet, iRow, style, schedules);
			if (i > 1) {
				insuranceDates.add(schedules.get(0).getInstallmentDate());
			}
		}
		
		Row tmpRowEnd = sheet.createRow(iRow++);
		tmpRowEnd.setRowStyle(style);
		for (int r = 0; r <= 8; r++) {
			createCell(tmpRowEnd, r, "", getCellStyle(HEADER));
		}
		
		iRow = iRow + 2;
		iRow = footer(sheet, iRow, style);
		
		//lockAll(sheet, wb);
		String uuid = UUID.randomUUID().toString().replace("-", "");
		fileName = writeXLSData("RePayment_Schedule_"+ uuid + DateUtils.getDateLabel(DateUtils.today(), "yyyyMMddHHmmssSSS") + ".xlsx");
								
		return fileName;
	}	
	

	private int paymentTable(final Sheet sheet, int iRow, final CellStyle style, List<Schedule> schedules) throws Exception {
		/* Create total data header */

		int iCol = 0;

		Row tmpRow = sheet.createRow(iRow++);
		createCell(tmpRow, iCol++, RSP_INDEX, 12, false, false, CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, RSP_DATE_PAYMENT, 12, false, false, CellStyle.ALIGN_RIGHT, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, RSP_SCHEDULE_PAYMENT, 12, false, false, CellStyle.ALIGN_RIGHT, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, SERVICING_FEE, 12, false, false, CellStyle.ALIGN_RIGHT, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, INSURANCE_FEE, 12, false, false, CellStyle.ALIGN_RIGHT, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, RSP_PRICIPAL_PAYMENT, 12, false, false, CellStyle.ALIGN_RIGHT, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, RSP_INTEREST_PAYMENT, 12, false, false, CellStyle.ALIGN_RIGHT, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol, RSP_BALANCE_PAYMENT, 12, false, false, CellStyle.ALIGN_RIGHT, true, BG_GREEN, FC_WHITE);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, iCol, iCol + 1));
		iCol = iCol + 1;
		createCell(tmpRow, iCol++, "", getCellStyle(HEADER));
		tmpRow.setRowStyle(style);
				
		int nbRows = schedules.size() > 9 ? schedules.size() : 9;
				
		for (int i = 0; i < nbRows; i++) {
			tmpRow = sheet.createRow(iRow++);
			if (i < schedules.size()) {
				Schedule schedule = schedules.get(i);
				iCol = 0;
				createNumericCell(tmpRow, iCol++, schedule.getN(), false, CellStyle.ALIGN_CENTER, 12, false, true, BG_WHITE, FC_BLACK);
				createDateCell(tmpRow, iCol++, schedule.getInstallmentDate(), false, CellStyle.ALIGN_RIGHT, 12, false, true, BG_WHITE, FC_BLACK);
				createPriceGeneral(tmpRow, iCol++, schedule.getInstallmentPayment() + servicingFee + insurancingFee , true, false, false, CellStyle.ALIGN_RIGHT, 12, false, 2, true, BG_WHITE, FC_BLACK);
				createPriceGeneral(tmpRow, iCol++, servicingFee, true, false, false, CellStyle.ALIGN_RIGHT, 12, false, 2, true, BG_WHITE, FC_BLACK);
				createPriceGeneral(tmpRow, iCol++, insurancingFee, true, false, false, CellStyle.ALIGN_RIGHT, 12, false, 2, true, BG_WHITE, FC_BLACK);
				createPriceGeneral(tmpRow, iCol++, schedule.getPrincipalAmount(), true, false, false, CellStyle.ALIGN_RIGHT, 12, false, 2, true, BG_WHITE, FC_BLACK);
				createPriceGeneral(tmpRow, iCol++, schedule.getInterestAmount(), true, false, false, CellStyle.ALIGN_RIGHT, 12, false, 2, true, BG_WHITE, FC_BLACK);
				createPriceGeneral(tmpRow, iCol, schedule.getBalanceAmount(), true, false, false, CellStyle.ALIGN_RIGHT, 12, false, 2, true, BG_WHITE, FC_BLACK);
				sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, iCol, iCol + 1));
			}
			tmpRow.setRowStyle(style);
		}
		
		iRow = iRow + 1;
		return iRow;

	}
	
	protected int paymentTable(final Sheet sheet, int iRow, final CellStyle style, List<Schedule> schedules, int numberYear, double insuranceFee, double remainingInterest, double remainingBalance) throws Exception {
		/* Create total data header */

		int iCol = 0;

		Row tmpRow = sheet.createRow(iRow++);
		createCell(tmpRow, iCol++, RSP_INDEX, 12, false, false, CellStyle.ALIGN_LEFT, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, RSP_DATE_PAYMENT, 12, false, false, CellStyle.ALIGN_LEFT, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, RSP_SCHEDULE_PAYMENT, 12, false, false, CellStyle.ALIGN_LEFT, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "", getCellStyle(HEADER));
		createCell(tmpRow, iCol++, "", getCellStyle(HEADER));
		createCell(tmpRow, iCol++, "", getCellStyle(HEADER));
		createCell(tmpRow, iCol++, "", getCellStyle(HEADER));
		createCell(tmpRow, iCol++, "", getCellStyle(HEADER));
		createCell(tmpRow, iCol++, "", getCellStyle(HEADER));
		createCell(tmpRow, iCol++, "", getCellStyle(HEADER));
		createCell(tmpRow, iCol++, "", getCellStyle(HEADER));
		tmpRow.setRowStyle(style);
				
		int nbRows = schedules.size() > 9 ? schedules.size() : 9;
				
		for (int i = 0; i < nbRows; i++) {
			tmpRow = sheet.createRow(iRow++);
			if (i < schedules.size()) {
				Schedule schedule = schedules.get(i);
				createNumericCell(tmpRow, 0, schedule.getN(), false, CellStyle.ALIGN_CENTER, 12, false, true, BG_WHITE, FC_BLACK);
				iCol = 1;
				createDateCell(tmpRow, 1, schedule.getInstallmentDate(), false, CellStyle.ALIGN_RIGHT, 12, false, true, BG_WHITE, FC_BLACK);
				createPriceGeneral(tmpRow, 2, schedule.getInstallmentPayment(), true, false, false, CellStyle.ALIGN_RIGHT, 12, false, 2, true, BG_WHITE, FC_BLACK);
			}
			
			if (numberYear > 1 && (i == nbRows - 8)) {
				sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow, 4, 7));
				createCell(tmpRow, 4, I18N.message(RSP_INSURANCE_YEARLY_FEE, "" + insuranceFee, DateUtils.date2StringDDMMYYYY_SLASH(schedules.get(0).getInstallmentDate())) 
						, 12, false, false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK);
			}
			
			if (i == nbRows - 6) {
				sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow, 4, 7));
				createCell(tmpRow, 4, RSP_YEAR + numberYear + " (YEAR +" + numberYear + ")", 12, false, false, CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
			}

			if (i == nbRows - 4) {
				sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow, 4, 6));
				sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow, 7, 7));
				createCell(tmpRow, 4, RSP_REMAINING_INTEREST_BALANCE_ROW, 12, false, false, CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
				createPriceGeneral(tmpRow, 7, remainingInterest, true, false, false, CellStyle.ALIGN_CENTER, 12, false, 2, true, BG_GREEN, FC_WHITE);
			}
			if (i == nbRows - 2) {
				sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow, 4, 6));
				sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow, 7, 7));
				createCell(tmpRow, 4, RSP_REMAINING_PRINCIPAL_ROW, 12, false, false, CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
				createPriceGeneral(tmpRow, 7, remainingBalance, true, false, false, CellStyle.ALIGN_CENTER, 12, false, 2, true, BG_GREEN, FC_WHITE);
			}
			tmpRow.setRowStyle(style);
		}
		
		iRow = iRow + 1;
		Row tmpRowEnd = sheet.createRow(iRow++);
		tmpRowEnd.setRowStyle(style);
		for (int r = 0; r <= 8; r++) {
			createCell(tmpRowEnd, r, "", getCellStyle(HEADER));
		}
		iRow++;
		
		return iRow;

	}

	public int footer(final Sheet sheet, int iRow, final CellStyle style) throws Exception {
		/* Create total data header */
		int setFontsize = 12;
		int iCol = 1;
		Row tmpRow1 = sheet.createRow(iRow++);
		createRichCell(tmpRow1, iCol, RSP_FOOTER_GL_FINANCE_PLC, 14, RSP_FOOTER_GL_FINANCE_PLC.length(), CellStyle.ALIGN_LEFT, setFontsize);
		iCol = 6;
		createRichCell(tmpRow1, iCol, RSP_FOOTER_CUS_LESSEE, 15, RSP_FOOTER_CUS_LESSEE.length(), CellStyle.ALIGN_LEFT, setFontsize);
		iRow = iRow + 6;
		iCol = 1;
		Row tmpRow2 = sheet.createRow(iRow++);
		createRichCell(tmpRow2, iCol, RSP_FOOTER_GLF_NAME , 5, RSP_FOOTER_GLF_NAME.length(), CellStyle.ALIGN_LEFT, setFontsize);
		iCol = 6;
		createRichCell(tmpRow2, iCol, RSP_FOOTER_CUS_NAME , 5, RSP_FOOTER_CUS_NAME.length(), CellStyle.ALIGN_LEFT, setFontsize);
		iCol = 7;
		createCell(tmpRow2, iCol,lesseeName, 11, true, false, CellStyle.ALIGN_LEFT, false, BG_WHITE, FC_BLACK);
		
		iCol = 1;
		Row tmpRow3 = sheet.createRow(iRow++);
		createRichCell(tmpRow3, iCol, RSP_FOOTER_GLF_DATE, 11, RSP_FOOTER_GLF_DATE.length(), CellStyle.ALIGN_LEFT, setFontsize);
		iCol = 6;
		createRichCell(tmpRow3, iCol, RSP_FOOTER_CUS_DATE, 11, RSP_FOOTER_CUS_DATE.length(), CellStyle.ALIGN_LEFT, setFontsize);

		return iRow;

	}

	public int remainingBalanceNotice(Row tmpRow, final CellStyle style) throws Exception {
		/* Create total data header */
		int iCol = 4;
		createCell(tmpRow, iCol++, "", getCellStyle(HEADER));
		createCell(tmpRow, iCol++, "", getCellStyle(HEADER));
		createCell(tmpRow, iCol++, RSP_INDEX, 12, false, false, CellStyle.ALIGN_LEFT, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "", getCellStyle(HEADER));
		createCell(tmpRow, iCol++, "", getCellStyle(HEADER));
		return iCol;

	}

	protected Cell createRichCell(final Row richStringRow, final int iCol, final String value, int start, int end, final short alignment, final int fontsize) {
		final CellStyle style = wb.createCellStyle();
		style.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.BIG_SPOTS);
		style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setLocked(true);
		style.setAlignment(alignment);

		Font font1 = wb.createFont();
		font1.setFontHeightInPoints((short) fontsize);

		font1.setFontName("Khmer OS Battambang");
		font1.setColor(FC_BLACK);

		Font font2 = wb.createFont();
		font2.setFontHeightInPoints((short) fontsize);
		font2.setFontName("Khmer OS Battambang");
		font2.setColor(FC_GREY);

		final Cell cell = richStringRow.createCell(iCol);
		RichTextString richString = new XSSFRichTextString(value);
		richString.applyFont(0, start, font1);
		richString.applyFont(start, end, font2);
		cell.setCellValue(richString);
		style.setFont(font1);
		style.setFont(font2);
		cell.setCellStyle(style);

		richStringRow.setRowStyle(style);

		return cell;
	}

	protected Cell createCell(final Row row, final int iCol, final String value, final CellStyle style) {
		final Cell cell = row.createCell(iCol);
		cell.setCellValue((value == null ? "" : value));
		cell.setCellStyle(style);
		return cell;
	}

	protected Cell createCell(final Row row, final int iCol, final String value, final int fontsize, final boolean isBold, final boolean hasBorder, 
			final short alignment, final boolean setBgColor, final short bgColor, final short fonCorlor) {
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
			style.setBorderTop(CellStyle.BORDER_DOTTED);
			style.setBorderLeft(CellStyle.BORDER_DOTTED);
			style.setBorderRight(CellStyle.BORDER_DOTTED);
			style.setBorderBottom(CellStyle.BORDER_DOTTED);
		}
		if (setBgColor) {
			style.setFillForegroundColor(bgColor);
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		}
		cell.setCellValue((value == null ? "" : value));
		cell.setCellStyle(style);
		row.setRowStyle(style);
		return cell;
	}

	protected Cell createPriceGeneral(final Row row, final int iCol,
			final Double price, final boolean setCurrentcy,
			final boolean isKhr, final boolean hasBorder,
			final short alignment, final int fontsize, final boolean isBold,
			final int decimalFormatNumber, final boolean setBgColor,
			final short bgColor, final short fontColor) {

		final Cell cell = row.createCell(iCol);
		final DataFormat df = wb.createDataFormat();
		final Font itemRightFont = wb.createFont();
		itemRightFont.setFontHeightInPoints((short) fontsize);
		itemRightFont.setFontName("Khmer OS Battambang");
		itemRightFont.setColor(fontColor);
		if (isBold) {
			itemRightFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		}
		final CellStyle style = wb.createCellStyle();
		style.setFont(itemRightFont);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setLocked(true);
		
		if (setBgColor) {
			style.setFillForegroundColor(bgColor);
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		}
		if (hasBorder) {
			style.setBorderTop(CellStyle.BORDER_DOTTED);
			style.setBorderLeft(CellStyle.BORDER_DOTTED);
			style.setBorderRight(CellStyle.BORDER_DOTTED);
			style.setBorderBottom(CellStyle.BORDER_DOTTED);
		}
		if (setCurrentcy && price != 0) {
			if (!isKhr) {
				ECurrency currency = ECurrency.getDefault();
				if (currency != null && StringUtils.isNotEmpty(currency.getSymbol())) {
					style.setDataFormat(df.getFormat(currency.getSymbol() + FORMAT_DECIMAL2));
				} else {
					style.setDataFormat(df.getFormat("$" + FORMAT_DECIMAL2));
				}
			}
		}
		if (price != 0 && !setCurrentcy) {
			if (decimalFormatNumber == 1) {
				style.setDataFormat(df.getFormat(FORMAT_DECIMAL1));
			} else if (decimalFormatNumber == 2) {
				style.setDataFormat(df.getFormat(FORMAT_DECIMAL2));
			} else if (decimalFormatNumber == 3) {
				style.setDataFormat(df.getFormat(FORMAT_DECIMAL3));
			}
		}
		style.setAlignment(alignment);
		if (price == 0 || price == null) {
			cell.setCellValue("-");
		} else {
			cell.setCellValue((price == null ? 0 : price));
		}

		cell.setCellStyle(style);
		return cell;
	}

	protected Cell createNumericCell(final Row row, final int iCol,
			final Object value, final boolean hasBorder, final short alignment,
			final int fontsize, final boolean isBold, final boolean setBgColor,
			final short bgColor, final short fontColor) {

		final Cell cell = row.createCell(iCol);
		final Font itemRightFont = wb.createFont();
		itemRightFont.setFontHeightInPoints((short) fontsize);
		itemRightFont.setFontName("Khmer OS Battambang");
		itemRightFont.setColor(fontColor);
		if (isBold) {
			itemRightFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		}
		final CellStyle style = wb.createCellStyle();
		style.setFont(itemRightFont);
		style.setLocked(true);
		
		if (setBgColor) {
			style.setFillForegroundColor(bgColor);
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		}
		if (hasBorder) {
			style.setBorderTop(CellStyle.BORDER_DOTTED);
			style.setBorderLeft(CellStyle.BORDER_DOTTED);
			style.setBorderRight(CellStyle.BORDER_DOTTED);
			style.setBorderBottom(CellStyle.BORDER_DOTTED);
		}
		//format data to number in excel
	        DataFormat format = wb.createDataFormat();
			style.setDataFormat(format.getFormat("0"));

		style.setAlignment(alignment);
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
		}
		cell.setCellStyle(style);
		return cell;
	}

	protected Cell createPercentageValueCell(final Row row,
			final int iCol, final Double value, final boolean isBold,
			final boolean hasBorder, final short alignment, final int fontsize,
			final int decimalFormatNumber, final boolean percentageSymbol,
			final boolean setBgColor, final short bgColor, final short fontColor) {
		final Cell cell = row.createCell(iCol);
		final DataFormat df = wb.createDataFormat();

		final Font itemRightFont = wb.createFont();
		itemRightFont.setFontHeightInPoints((short) fontsize);
		itemRightFont.setFontName("Khmer OS Battambang");
		itemRightFont.setColor(fontColor);

		final CellStyle style = wb.createCellStyle();
		style.setFont(itemRightFont);
		style.setLocked(true);
		
		if (isBold) {
			itemRightFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		}
		if (setBgColor) {
			style.setFillForegroundColor(bgColor);
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		}
		if (hasBorder) {
			style.setBorderTop(CellStyle.BORDER_DOTTED);
			style.setBorderLeft(CellStyle.BORDER_DOTTED);
			style.setBorderRight(CellStyle.BORDER_DOTTED);
			style.setBorderBottom(CellStyle.BORDER_DOTTED);
		}

		if (percentageSymbol) {
			if (decimalFormatNumber == 1) {
				style.setDataFormat(df.getFormat(FORMAT_PERCENTAGE1));
			} else if (decimalFormatNumber == 2) {
				style.setDataFormat(df.getFormat(FORMAT_PERCENTAGE2));
			} else if (decimalFormatNumber == 3) {
				style.setDataFormat(df.getFormat(FORMAT_PERCENTAGE3));
			}
		} else {
			if (decimalFormatNumber == 1) {
				style.setDataFormat(df.getFormat(FORMAT_DECIMAL1));
			} else if (decimalFormatNumber == 2) {
				style.setDataFormat(df.getFormat(FORMAT_DECIMAL2));
			} else if (decimalFormatNumber == 3) {
				style.setDataFormat(df.getFormat(FORMAT_DECIMAL3));
			}
		}
		style.setAlignment(alignment);
		cell.setCellValue((value == null ? 0 : value));
		cell.setCellStyle(style);
		return cell;
	}

	protected Cell createDateCell(final Row row, final int iCol,
			final Date value, final boolean hasBorder, final short alignment,
			final int fontsize, final boolean isBold, final boolean setBgColor,
			final short bgColor, final short fontColor) {

		final Cell cell = row.createCell(iCol);

		final Font itemRightFont = wb.createFont();
		itemRightFont.setFontHeightInPoints((short) fontsize);
		itemRightFont.setFontName("Khmer OS Battambang");
		itemRightFont.setColor(fontColor);
		if (isBold) {
			itemRightFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		}
		final CellStyle style = wb.createCellStyle();
		style.setFont(itemRightFont);
		style.setLocked(true);
		
		if (setBgColor) {
			style.setFillForegroundColor(bgColor);
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		}
		if (hasBorder) {
			style.setBorderTop(CellStyle.BORDER_DOTTED);
			style.setBorderLeft(CellStyle.BORDER_DOTTED);
			style.setBorderRight(CellStyle.BORDER_DOTTED);
			style.setBorderBottom(CellStyle.BORDER_DOTTED);
		}
		style.setAlignment(alignment);
		cell.setCellValue((value == null ? "" : getDateLabel(value)));
		cell.setCellStyle(style);
		return cell;
	}

	public String getDateLabel(final Date date,
			final String formatPattern) {
		if (date != null && formatPattern != null) {
			return DateFormatUtils.format(date, formatPattern);
		}
		return null;
	}

	public String getDateLabel(final Date date) {
		return getDateLabel(date, DEFAULT_DATE_FORMAT);
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

		return styles;
	}

	protected CellStyle getCellStyle(final String styleName) {
		return styles.get(styleName);
	}
	/**
	 * @param serviceCode
	 * @param services
	 * @return
	 */	int nbPaidsPerYear;
	private Double getServiceFee() {
		return MyMathUtils.roundAmountTo(serviceFee / term);
	}
	private Double getinsuranceFee() {
		if(frequency.getCode() == "annually"){
			nbPaidsPerYear = 1;
		}else if(frequency.getCode() == "half.year"){
			nbPaidsPerYear = 2;
		}else if(frequency.getCode() == "monthly"){
			nbPaidsPerYear = 12;
		}else {
			nbPaidsPerYear = 4;
		}
		return MyMathUtils.roundAmountTo(((term / nbPaidsPerYear) * insuranceFee) / term);
	}
}