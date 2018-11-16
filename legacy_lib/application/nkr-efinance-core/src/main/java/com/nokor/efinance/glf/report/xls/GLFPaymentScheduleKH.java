package com.nokor.efinance.glf.report.xls;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.financial.service.FinanceCalculationService;
import com.nokor.efinance.tools.report.XLSAbstractReportExtractor;
import com.nokor.efinance.core.quotation.QuotationService;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.conf.AppConfig;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.ersys.core.finance.model.eref.ECurrency;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.finance.services.shared.AmortizationSchedules;
import com.nokor.finance.services.shared.CalculationParameter;
import com.nokor.finance.services.shared.Schedule;
import com.nokor.frmk.security.context.SecApplicationContextHolder;

/**
 * Monthly payment schedule report for template_1
 * @author ly.youhort
 */
public class GLFPaymentScheduleKH extends XLSAbstractReportExtractor implements GLFPaymentScheduleFields {

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
	
	protected QuotationService quotationService = (QuotationService) SecApplicationContextHolder.getContext().getBean("quotationService");
	protected FinanceCalculationService financeCalculationService = (FinanceCalculationService) SecApplicationContextHolder.getContext().getBean("financeCalculationService");
		
	/** */
	public GLFPaymentScheduleKH() {
		
	}
	
	/**
	 * @see com.nokor.efinance.tools.report.Report#generate(com.nokor.efinance.core.shared.report.ReportParameter)
	 */
	@Override
	public String generate(ReportParameter reportParameter) throws Exception {
		
		String fileName = ""; 
				
		createWorkbook(null);
		
		Long quotaId = (Long) reportParameter.getParameters().get("quotaId");
		
		Quotation quotation = quotationService.getById(Quotation.class, quotaId);			
		CalculationParameter calculationParameter = new CalculationParameter();
		calculationParameter.setFrequency(quotation.getFrequency());
		calculationParameter.setInitialPrincipal(quotation.getTiFinanceAmount());
		calculationParameter.setNumberOfPeriods(quotation.getTerm());
		calculationParameter.setPeriodicInterestRate(quotation.getInterestRate() / 100);
		calculationParameter.setNumberOfPrincipalGracePeriods(MyNumberUtils.getInteger(quotation.getNumberOfPrincipalGracePeriods()));
		
		Date contractStartDate = quotation.getContractStartDate();
		if (contractStartDate == null) {
			contractStartDate = DateUtils.today();
		}
		
		Date firstPaymentDate = quotation.getFirstDueDate();
		if (firstPaymentDate == null) {
			firstPaymentDate = DateUtils.today();
		}
		
		AmortizationSchedules amortizationSchedules = financeCalculationService.getAmortizationSchedules(contractStartDate, firstPaymentDate, calculationParameter);
		Applicant applicant = quotation.getApplicant();
		Address applicantAddress = applicant.getIndividual().getMainAddress();
		Asset asset = quotation.getAsset();
		
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
		sheet.setColumnWidth(1, 4000);
		sheet.setColumnWidth(2, 5500);
		sheet.setColumnWidth(3, 4000);
		sheet.setColumnWidth(4, 2500);
		sheet.setColumnWidth(5, 6000);
		sheet.setColumnWidth(6, 4000);
		sheet.setColumnWidth(7, 4000);
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
        
		/*for (int j = 0; j < 256; j++) {
			Row rowLoop = sheet.createRow((short) +j);
			for (int i = 0; i < 256; i++) {
				Cell cell = rowLoop.createCell((short) +i);
				cell.setCellStyle(style);
			}
		}*/
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

		final Row headerRow = sheet.createRow(2);
		createCell(headerRow, 3, RSP_HEADER, 14, true, false, CellStyle.ALIGN_LEFT, false, BG_WHITE, FC_BLACK);
		
		int iRow = 6;
		int iCol = 0;
		final Row rspCusNameRow = sheet.createRow(iRow++);
		createCell(rspCusNameRow, iCol, RSP_CUS_NAME, 12, false, false, CellStyle.ALIGN_LEFT, false, BG_WHITE, FC_BLACK);
		iCol = iCol + 2;
		createCell(rspCusNameRow, iCol,  applicant.getIndividual().getLastName() + " " + applicant.getIndividual().getFirstName(), 11, false, false, CellStyle.ALIGN_LEFT, false, BG_WHITE, FC_BLUE);

		String address1 = "";
		String address2 = "";
		if (StringUtils.isNotEmpty(applicantAddress.getHouseNo())) {
			address1 += HOUSE_NO + " " + applicantAddress.getHouseNo();
		}
					
		if (StringUtils.isNotEmpty(applicantAddress.getStreet())) {
			address1 = concat(address1, STREET + " " + applicantAddress.getStreet());
		}
		
		if (applicantAddress.getVillage() != null) {
			address1 = concat(address1, VILLAGE + " " + applicantAddress.getVillage().getDesc());
		}
		
		if (applicantAddress.getCommune() != null) {
			address1 = concat(address1, SANGKAT + " " + applicantAddress.getCommune().getDesc());
		}
		
		if (applicantAddress.getDistrict() != null) {
			address2 = concat(address2, applicantAddress.getDistrict().getDesc());
		}
		
		if (applicantAddress.getProvince() != null) {
			address2 = concat(address2, applicantAddress.getProvince().getDesc());
		}
		
		iCol = 0;
		final Row rspCusAddress1Row = sheet.createRow(iRow++);
		createCell(rspCusAddress1Row, iCol, RSP_CUS_ADDRESS, 12, false, false, CellStyle.ALIGN_LEFT, false, BG_WHITE, FC_BLACK);
		iCol = iCol + 2;
		createCell(rspCusAddress1Row, iCol, address1, 11, false, false, CellStyle.ALIGN_LEFT, false, BG_WHITE, FC_BLUE);

		final Row rspCusAddress2Row = sheet.createRow(iRow++);
		iCol = 2;
		createCell(rspCusAddress2Row, iCol, address2, 11, false, false, CellStyle.ALIGN_LEFT, false, BG_WHITE, FC_BLUE);

		iCol = iCol + 3;
		createCell(rspCusAddress2Row, iCol, RSP_DEALER_NUMBER, 11, false, false, CellStyle.ALIGN_LEFT, false, BG_WHITE, FC_BLACK);
		iCol = iCol + 1;
		createNumericCell(rspCusAddress2Row, iCol, quotation.getDealer().getCode(), false, CellStyle.ALIGN_LEFT, 11, false, true, BG_WHITE, FC_BLUE);

		iCol = 0;
		final Row rspCusPhoneNumberRow = sheet.createRow(iRow++);
		createCell(rspCusPhoneNumberRow, iCol, RSP_CUS_PHONENUMBER, 12, false, false, CellStyle.ALIGN_LEFT, false, BG_WHITE, FC_BLACK);
		iCol = iCol + 2;
		// TODO YLY
		// createCell(rspCusPhoneNumberRow, iCol, applicant.getMobilePhone(), 11, false, false, CellStyle.ALIGN_LEFT, false, BG_WHITE, FC_BLUE);

		iCol = iCol + 3;
		createCell(rspCusPhoneNumberRow, iCol, RSP_LID_NUMBER, 11, false, false, CellStyle.ALIGN_LEFT, false, BG_WHITE, FC_BLACK);

		iCol = iCol + 1;
		createNumericCell(rspCusPhoneNumberRow, iCol, quotation.getReference(), false, CellStyle.ALIGN_LEFT, 11, false, true, BG_WHITE, FC_BLUE);

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
		
		double registrationFee = getServiceFee("REGFEE", quotation.getQuotationServices());
		double insuranceFee = getServiceFee("INSFEE", quotation.getQuotationServices()); 
		double servicingFee = getServiceFee("SERFEE", quotation.getQuotationServices());
		double totalFirstPayment = MyNumberUtils.getDouble(quotation.getTiAdvancePaymentAmount()) + insuranceFee + servicingFee + registrationFee;
		
		// 1
		Row rowModel = sheet.createRow(iRow++);
		iCol = 0;
		createCell(rowModel, iCol, "", getCellStyle(LEFT_BORDER));
		iCol = 2;
		createRichCell(rowModel, iCol, RSP_VHE_MOTORCYCLE_MODEL, 13, RSP_VHE_MOTORCYCLE_MODEL.length(), CellStyle.ALIGN_RIGHT, fontsize);
		iCol = 3;
		createCell(rowModel, iCol, asset.getDesc(), 11, false, true, CellStyle.ALIGN_CENTER, false, BG_WHITE, FC_BLUE);

		iCol = 6;
		createRichCell(rowModel, iCol, RSP_NUMBER_OF_PAYMENTS, 13, RSP_NUMBER_OF_PAYMENTS.length(), CellStyle.ALIGN_RIGHT, fontsize);
		iCol = 7;
		createNumericCell(rowModel, iCol, quotation.getTerm(), true, CellStyle.ALIGN_CENTER, 11, false, true, BG_WHITE, FC_BLUE);
		iCol = 8;
		createCell(rowModel, iCol, "", getCellStyle(RIGHT_BORDER));
		// 2
		Row rowYear = sheet.createRow(iRow++);
		iCol = 0;
		createCell(rowYear, iCol, "", getCellStyle(LEFT_BORDER));
		iCol = 2;
		createRichCell(rowYear, iCol, RSP_VHE_YEAR, 10, RSP_VHE_YEAR.length(), CellStyle.ALIGN_RIGHT, fontsize);
		iCol = 3;
		createNumericCell(rowYear, iCol, asset.getYear(), true, CellStyle.ALIGN_CENTER, 11, false, true, BG_WHITE, FC_BLUE);

		iCol = 6;
		createRichCell(rowYear, iCol, RSP_MONTHLY_INSTALMENT_AMOUNT, 23, RSP_MONTHLY_INSTALMENT_AMOUNT.length(), CellStyle.ALIGN_RIGHT, fontsize);
		iCol = 7;
		createPriceGeneral(rowYear, iCol, quotation.getTiInstallmentAmount(), true, false, true, CellStyle.ALIGN_CENTER, 11, false, 2, true, BG_WHITE, FC_GREEN);
		
		iCol = 8;
		createCell(rowYear, iCol, "", getCellStyle(RIGHT_BORDER));
		// 3
		Row rowCc = sheet.createRow(iRow++);
		iCol = 0;
		createCell(rowCc, iCol, "", getCellStyle(LEFT_BORDER));
		iCol = 2;
		createRichCell(rowCc, iCol, RSP_VHE_CC, 13, RSP_VHE_CC.length(), CellStyle.ALIGN_RIGHT, fontsize);
		iCol = 3;
		createNumericCell(rowCc, iCol, getDefaultString(asset.getEngine().getDesc(), asset.getEngine().getDescEn()), true, CellStyle.ALIGN_CENTER, 11, false, true, BG_WHITE, FC_BLUE);

		iCol = 6;
		createRichCell(rowCc, iCol, RSP_ADVANCE_PAYMENT_RATE, 18, RSP_ADVANCE_PAYMENT_RATE.length(), CellStyle.ALIGN_RIGHT, fontsize);
		iCol = 7;
		createPercentageValueCell(rowCc, iCol, quotation.getAdvancePaymentPercentage() / 100, false, true, CellStyle.ALIGN_CENTER, 11, 2, true, true, BG_WHITE, FC_BLUE);
		iCol = 8;
		createCell(rowCc, iCol, "", getCellStyle(RIGHT_BORDER));
		// 4
		Row rowVehCashPice = sheet.createRow(iRow++);
		iCol = 0;
		createCell(rowVehCashPice, iCol, "", getCellStyle(LEFT_BORDER));
		iCol = 2;
		createRichCell(rowVehCashPice, iCol, RSP_VHE_PRICE, 11, RSP_VHE_PRICE.length(), CellStyle.ALIGN_RIGHT, fontsize);
		iCol = 3;
		createPriceGeneral(rowVehCashPice, iCol, asset.getTiAssetPrice(), true, false, true, CellStyle.ALIGN_CENTER, 11, false, 2, true, BG_WHITE, FC_GREEN);
		
		iCol = 6;
		createRichCell(rowVehCashPice, iCol, RSP_ADVANCE_PAYMENT, 13, RSP_ADVANCE_PAYMENT.length(), CellStyle.ALIGN_RIGHT, fontsize);
		iCol = 7;
		createPriceGeneral(rowVehCashPice, iCol, MyNumberUtils.getDouble(quotation.getTiAdvancePaymentAmount()), true, false, true, CellStyle.ALIGN_CENTER, 11, false, 2, true, BG_WHITE, FC_GREEN);
		iCol = 8;
		createCell(rowVehCashPice, iCol, "", getCellStyle(RIGHT_BORDER));
		// 5
		Row rowLeaseAmount = sheet.createRow(iRow++);
		iCol = 0;
		createCell(rowLeaseAmount, iCol, "", getCellStyle(LEFT_BORDER));
		iCol = 2;
		createRichCell(rowLeaseAmount, iCol, RSP_VHE_LEASE_AMOUNT, 19,  RSP_VHE_LEASE_AMOUNT.length(), CellStyle.ALIGN_RIGHT, fontsize);
		iCol = 3;
		createPriceGeneral(rowLeaseAmount, iCol, quotation.getTiFinanceAmount(), true, false, true, CellStyle.ALIGN_CENTER, 11, false, 2, true, BG_WHITE, FC_GREEN);

		String registrationOradmistrationFee = RSP_REGISTRATION_FEE;
		if (quotation.getDealer().getCode().equals("KTH-01") || quotation.getDealer().getCode().equals("KTH-02")) {
			registrationOradmistrationFee = RSP_ADMINISTRATION_FEE;
		}
		iCol = 6;
		createRichCell(rowLeaseAmount, iCol, registrationOradmistrationFee, 17, registrationOradmistrationFee.length(), CellStyle.ALIGN_RIGHT, fontsize);
		iCol = 7;
		createPriceGeneral(rowLeaseAmount, iCol, registrationFee, true, false, true, CellStyle.ALIGN_CENTER, 11, false, 2, true, BG_WHITE, FC_GREEN);
		iCol = 8;
		createCell(rowLeaseAmount, iCol, "", getCellStyle(RIGHT_BORDER));

		// 6
		Row rowInterestRate = sheet.createRow(iRow++);
		iCol = 0;
		createCell(rowInterestRate, iCol, "", getCellStyle(LEFT_BORDER));
		iCol = 2;
		createRichCell(rowInterestRate, iCol, RSP_VHE_INTEREST_RATE, 14, RSP_VHE_INTEREST_RATE.length(), CellStyle.ALIGN_RIGHT, fontsize);
		iCol = 3;
		createPercentageValueCell(rowInterestRate, iCol, quotation.getInterestRate() / 100, false, true, CellStyle.ALIGN_CENTER, 11, 2, true, true, BG_WHITE, FC_BLUE);

		iCol = 6;
		createRichCell(rowInterestRate, iCol, RSP_SERVICING_FEE, 15, RSP_SERVICING_FEE.length(), CellStyle.ALIGN_RIGHT, fontsize);
		iCol = 7;
		createPriceGeneral(rowInterestRate, iCol, servicingFee, true, false, true, CellStyle.ALIGN_CENTER, 11, false, 2, true, BG_WHITE, FC_GREEN);
		iCol = 8;
		createCell(rowInterestRate, iCol, "", getCellStyle(RIGHT_BORDER));

		// 7
		Row rowContractDate = sheet.createRow(iRow++);
		iCol = 0;
		createCell(rowContractDate, iCol, "", getCellStyle(LEFT_BORDER));
		iCol = 2;
		createRichCell(rowContractDate, iCol, RSP_VHE_CONTRACT_DATE, 24, RSP_VHE_CONTRACT_DATE.length(), CellStyle.ALIGN_RIGHT, fontsize);
		iCol = 3;
		createDateCell(rowContractDate, iCol, contractStartDate, true, CellStyle.ALIGN_CENTER, 11, false, true, BG_WHITE, FC_BLUE);
		
		iCol = 6;
		createRichCell(rowContractDate, iCol, RSP_INSURANCE_FEE, 18, RSP_INSURANCE_FEE.length(), CellStyle.ALIGN_RIGHT, fontsize);
		iCol = 7;
		createPriceGeneral(rowContractDate, iCol, insuranceFee, true, false, true, CellStyle.ALIGN_CENTER, 11, false, 2, true, BG_WHITE, FC_GREEN);
		iCol = 8;
		createCell(rowContractDate, iCol, "", getCellStyle(RIGHT_BORDER));
		
		// 8
		Row rowFirstPaymentDate = sheet.createRow(iRow++);
		iCol = 0;
		createCell(rowFirstPaymentDate, iCol, "", getCellStyle(LEFT_BORDER));
		iCol = 2;
		createRichCell(rowFirstPaymentDate, iCol, RSP_VHE_FIRST_PAYMENT_DATE, 27, RSP_VHE_FIRST_PAYMENT_DATE.length(), CellStyle.ALIGN_RIGHT, fontsize);
		iCol = 3;
		createDateCell(rowFirstPaymentDate, iCol, firstPaymentDate, true, CellStyle.ALIGN_CENTER, 11, false, true, BG_WHITE, FC_BLUE);
		
		iCol = 6;
		createRichCell(rowFirstPaymentDate, iCol, RSP_TOTAL_FIRST_PAYMENT, 27, RSP_TOTAL_FIRST_PAYMENT.length(), CellStyle.ALIGN_RIGHT, fontsize);
		iCol = 7;
		createPriceGeneral(rowFirstPaymentDate, iCol, totalFirstPayment, true, false, true, CellStyle.ALIGN_CENTER, 11, false, 2, true, BG_WHITE, FC_GREEN);
		iCol = 8;
		createCell(rowFirstPaymentDate, iCol, "", getCellStyle(RIGHT_BORDER));

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
		
		int nbSchedulePerYear = quotation.getFrequency().getNbSchedulePerYear();
		double nbYear = amortizationSchedules.getSchedules().size() / (double) nbSchedulePerYear;
		
		List<Date> insuranceDates = new ArrayList<Date>();
		int indexValue = 0;
		for (int i = 0; i < nbYear; i++) {
			List<Schedule> schedules = new ArrayList<Schedule>();
			indexValue = i + 1;
			int lastIndex = indexValue * nbSchedulePerYear;
			if (lastIndex > amortizationSchedules.getSchedules().size()) {
				lastIndex = amortizationSchedules.getSchedules().size();
			}
			for (int j = (indexValue - 1) * nbSchedulePerYear; j < lastIndex; j++) {
				schedules.add(amortizationSchedules.getSchedules().get(j));
			}
			/*double remainingInterest = 0.0;
			for (int k = lastIndex; k < amortizationSchedules.getSchedules().size(); k++) {
				remainingInterest += amortizationSchedules.getSchedules().get(k).getInterestAmount();
			}*/
			
			//iRow = paymentTable(sheet, iRow, style, schedules, i, insuranceFee, remainingInterest, schedules.get(schedules.size() - 1).getBalanceAmount());
			iRow = paymentTable(sheet, iRow, style, schedules);
			if (indexValue > 1) {
				insuranceDates.add(schedules.get(0).getInstallmentDate());
			}
		}
		
		Row tmpRowEnd = sheet.createRow(iRow++);
		tmpRowEnd.setRowStyle(style);
		for (int r = 0; r <= 8; r++) {
			createCell(tmpRowEnd, r, "", getCellStyle(HEADER));
		}
		
		String symbol = "\\$";
		ECurrency currency = ECurrency.getDefault();
		if (currency != null && StringUtils.isNotEmpty(currency.getSymbol())) {
			symbol = "\\" + currency.getSymbol();
		}
		
		if (nbYear > 1) {
			iRow++;
			Row tmpRow = sheet.createRow(iRow++);
			createCell(tmpRow, 1, RSP_NOTICE, 12, true, false, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK);
			for (int i = 2; i <= nbYear; i++) {
				tmpRow = sheet.createRow(iRow++);
				createCell(tmpRow, 1, "" + (i - 1) + "  ", 12, false, false, CellStyle.ALIGN_RIGHT, true, BG_WHITE, FC_BLACK);
				createCell(tmpRow, 2, I18N.message(RSP_INSURANCE_YEARLY_FEE, "" + i, symbol + AmountUtils.format(insuranceFee), 
						DateUtils.date2StringDDMMYYYY_SLASH(insuranceDates.get(i - 2))), 
						12, false, false, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK);
			}
		}
		
		iRow = iRow + 2;
		iRow = footer(sheet, iRow, style, quotation);
		
		//lockAll(sheet, wb);
		
		fileName = writeXLSData("Payment_Schedule_" + quotation.getDealer().getCode() + "_" + DateUtils.getDateLabel(DateUtils.today(), "yyyyMMddHHmmssSSS") + ".xlsx");
								
		return fileName;
	}	
	
	/**
	 * 
	 * @param sheet
	 * @param iRow
	 * @param style
	 * @param schedules
	 * @return
	 * @throws Exception
	 */
	private int paymentTable(final Sheet sheet, int iRow, final CellStyle style, List<Schedule> schedules) throws Exception {
		/* Create total data header */

		int iCol = 0;

		Row tmpRow = sheet.createRow(iRow++);
		createCell(tmpRow, iCol++, RSP_INDEX, 12, false, false, CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, RSP_DATE_PAYMENT, 12, false, false, CellStyle.ALIGN_RIGHT, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, RSP_SCHEDULE_PAYMENT, 12, false, false, CellStyle.ALIGN_RIGHT, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol, RSP_PRICIPAL_PAYMENT, 12, false, false, CellStyle.ALIGN_RIGHT, true, BG_GREEN, FC_WHITE);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, iCol, iCol + 1));
		iCol = iCol + 2;
		createCell(tmpRow, iCol++, RSP_INTEREST_PAYMENT, 12, false, false, CellStyle.ALIGN_RIGHT, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol, RSP_BALANCE_PAYMENT, 12, false, false, CellStyle.ALIGN_RIGHT, true, BG_GREEN, FC_WHITE);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, iCol, iCol + 1));
		iCol = iCol + 2;
		createCell(tmpRow, iCol++, "", getCellStyle(HEADER));
		tmpRow.setRowStyle(style);
				
		int nbRows = schedules.size() > 9 ? schedules.size() : 6;
				
		for (int i = 0; i < nbRows; i++) {
			tmpRow = sheet.createRow(iRow++);
			if (i < schedules.size()) {
				Schedule schedule = schedules.get(i);
				iCol = 0;
				createNumericCell(tmpRow, iCol++, schedule.getN(), false, CellStyle.ALIGN_CENTER, 12, false, true, BG_WHITE, FC_BLACK);
				createDateCell(tmpRow, iCol++, schedule.getInstallmentDate(), false, CellStyle.ALIGN_RIGHT, 12, false, true, BG_WHITE, FC_BLACK);
				createPriceGeneral(tmpRow, iCol++, schedule.getInstallmentPayment(), true, false, false, CellStyle.ALIGN_RIGHT, 12, false, 2, true, BG_WHITE, FC_BLACK);
				createPriceGeneral(tmpRow, iCol, schedule.getPrincipalAmount(), true, false, false, CellStyle.ALIGN_RIGHT, 12, false, 2, true, BG_WHITE, FC_BLACK);
				sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, iCol, iCol + 1));
				iCol = iCol + 2;
				createPriceGeneral(tmpRow, iCol++, schedule.getInterestAmount(), true, false, false, CellStyle.ALIGN_RIGHT, 12, false, 2, true, BG_WHITE, FC_BLACK);
				createPriceGeneral(tmpRow, iCol, schedule.getBalanceAmount(), true, false, false, CellStyle.ALIGN_RIGHT, 12, false, 2, true, BG_WHITE, FC_BLACK);
				sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, iCol, iCol + 1));
			}
			tmpRow.setRowStyle(style);
		}
		
		iRow = iRow + 1;
		return iRow;

	}
	
	/**
	 * 
	 * @param sheet
	 * @param iRow
	 * @param style
	 * @param schedules
	 * @param numberYear
	 * @param insuranceFee
	 * @param remainingInterest
	 * @param remainingBalance
	 * @return
	 * @throws Exception
	 */
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

	/**
	 * 
	 * @param sheet
	 * @param iRow
	 * @param style
	 * @return
	 * @throws Exception
	 */
	public int footer(final Sheet sheet, int iRow, final CellStyle style, Quotation quotation) throws Exception {
		Applicant applicant = quotation.getApplicant();
		String lesseeName = applicant.getIndividual().getLastName() + " " + applicant.getIndividual().getFirstName();
		
		Date contractStartDate = quotation.getContractStartDate();
		if (contractStartDate == null) {
			contractStartDate = DateUtils.today();
		}
		
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
		createRichCell(tmpRow2, iCol, RSP_FOOTER_GLF_NAME, 5, RSP_FOOTER_GLF_NAME.length(), CellStyle.ALIGN_LEFT, setFontsize);
		iCol = 6;
		createRichCell(tmpRow2, iCol, I18N.message(RSP_FOOTER_CUS_NAME, lesseeName), 5, I18N.message(RSP_FOOTER_CUS_NAME, lesseeName).length(), CellStyle.ALIGN_LEFT, setFontsize);

		iCol = 1;
		Row tmpRow3 = sheet.createRow(iRow++);
		createRichCell(tmpRow3, iCol, I18N.message(RSP_FOOTER_GLF_DATE, DateUtils.date2StringDDMMYYYY_SLASH(contractStartDate)), 11, I18N.message(RSP_FOOTER_GLF_DATE, DateUtils.date2StringDDMMYYYY_SLASH(contractStartDate)).length(), CellStyle.ALIGN_LEFT, setFontsize);
		iCol = 6;
		createRichCell(tmpRow3, iCol, I18N.message(RSP_FOOTER_CUS_DATE, DateUtils.date2StringDDMMYYYY_SLASH(contractStartDate)), 11, I18N.message(RSP_FOOTER_CUS_DATE, DateUtils.date2StringDDMMYYYY_SLASH(contractStartDate)).length(), CellStyle.ALIGN_LEFT, setFontsize);

		return iRow;

	}

	/**
	 * 
	 * @param tmpRow
	 * @param style
	 * @return
	 * @throws Exception
	 */
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

	/**
	 * 
	 * @param richStringRow
	 * @param iCol
	 * @param value
	 * @param start
	 * @param end
	 * @param alignment
	 * @param fontsize
	 * @return
	 */
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

	/**
	 * @see com.nokor.efinance.tools.report.XLSAbstractReportExtractor#createCell(org.apache.poi.ss.usermodel.Row, int, java.lang.String, org.apache.poi.ss.usermodel.CellStyle)
	 */
	protected Cell createCell(final Row row, final int iCol, final String value, final CellStyle style) {
		final Cell cell = row.createCell(iCol);
		cell.setCellValue((value == null ? "" : value));
		cell.setCellStyle(style);
		return cell;
	}

	/**
	 * 
	 * @param row
	 * @param iCol
	 * @param value
	 * @param fontsize
	 * @param isBold
	 * @param hasBorder
	 * @param alignment
	 * @param setBgColor
	 * @param bgColor
	 * @param fonCorlor
	 * @return
	 */
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

	/**
	 * 
	 * @param row
	 * @param iCol
	 * @param price
	 * @param setCurrentcy
	 * @param isKhr
	 * @param hasBorder
	 * @param alignment
	 * @param fontsize
	 * @param isBold
	 * @param decimalFormatNumber
	 * @param setBgColor
	 * @param bgColor
	 * @param fontColor
	 * @return
	 */
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
					style.setDataFormat(df.getFormat(currency.getSymbol() + " " + FORMAT_DECIMAL2));
				} else {
					style.setDataFormat(df.getFormat("$ " + FORMAT_DECIMAL2));
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

	/**
	 * 
	 * @param row
	 * @param iCol
	 * @param value
	 * @param hasBorder
	 * @param alignment
	 * @param fontsize
	 * @param isBold
	 * @param setBgColor
	 * @param bgColor
	 * @param fontColor
	 * @return
	 */
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

		style.setAlignment(alignment);
		if (value == null) {
			cell.setCellValue("");
		} else if (value instanceof Integer) {
			cell.setCellValue(Integer.valueOf(value.toString()));
		} else if (value instanceof Long) {
			cell.setCellValue(Long.valueOf(value.toString()));
		} else if (value instanceof String) {
			cell.setCellValue(value.toString());
		}
		cell.setCellStyle(style);
		return cell;
	}

	/**
	 * 
	 * @param row
	 * @param iCol
	 * @param value
	 * @param isBold
	 * @param hasBorder
	 * @param alignment
	 * @param fontsize
	 * @param decimalFormatNumber
	 * @param percentageSymbol
	 * @param setBgColor
	 * @param bgColor
	 * @param fontColor
	 * @return
	 */
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

	/**
	 * 
	 * @param row
	 * @param iCol
	 * @param value
	 * @param hasBorder
	 * @param alignment
	 * @param fontsize
	 * @param isBold
	 * @param setBgColor
	 * @param bgColor
	 * @param fontColor
	 * @return
	 */
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

	/**
	 * 
	 * @param date
	 * @param formatPattern
	 * @return
	 */
	public String getDateLabel(final Date date,
			final String formatPattern) {
		if (date != null && formatPattern != null) {
			return DateFormatUtils.format(date, formatPattern);
		}
		return null;
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public String getDateLabel(final Date date) {
		return getDateLabel(date, DEFAULT_DATE_FORMAT);
	}

	/**
	 * 
	 * @return
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

		return styles;
	}

	/**
	 * @see com.nokor.efinance.tools.report.XLSAbstractReportExtractor#getCellStyle(java.lang.String)
	 */
	protected CellStyle getCellStyle(final String styleName) {
		return styles.get(styleName);
	}
	
	/**
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	private String getDefaultString(String value, String defaultValue) {
		if (StringUtils.isNotEmpty(value)) {
			return value;
		} else {
			return StringUtils.defaultString(defaultValue);
		}
	}
	
	/**
	 * @param serviceCode
	 * @param services
	 * @return
	 */
	private Double getServiceFee(String serviceCode, List<com.nokor.efinance.core.quotation.model.QuotationService> services) {
		for (com.nokor.efinance.core.quotation.model.QuotationService service : services) {
			if (service.getService().getCode().equals(serviceCode)) {
				return service.getTiPrice();
			}
		}
		return 0.0;
	}
	
	/**
	 * @param resumeAddress
	 * @param value
	 * @return
	 */
	private String concat(String resumeAddress, String value) {
		if (StringUtils.isNotEmpty(value)) {
			if (StringUtils.isNotEmpty(resumeAddress)) {
				resumeAddress += ", ";
			}
			resumeAddress += value;
		}
		return resumeAddress;
	}
}