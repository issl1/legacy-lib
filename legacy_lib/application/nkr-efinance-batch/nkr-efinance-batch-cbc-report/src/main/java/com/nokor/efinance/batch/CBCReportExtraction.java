package com.nokor.efinance.batch;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.meta.NativeColumn;
import org.seuksa.frmk.model.meta.NativeRow;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyMathUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.exception.NativeQueryException;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.address.model.Address;
import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.model.Employment;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractHistory;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowCode;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.service.ContractService;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationApplicant;
import com.nokor.efinance.core.quotation.model.QuotationDocument;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.efinance.workflow.ContractHistoReason;
import com.nokor.efinance.workflow.ContractWkfStatus;
import com.nokor.efinance.workflow.model.history.EProcessType;
import com.nokor.efinance.workflow.model.history.HistoryProcess;

/**
 * CBC report extraction
 * @author sok.vina
 */
public class CBCReportExtraction implements GLFApplicantFields, QuotationEntityField {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private ContractService contractService = SpringUtils.getBean(ContractService.class);
	
	public static String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";
	
	List<String> AREA_CODE_FIX = Arrays.asList( 
			"023", "024", "025", "026", "032", "033", "034", "035", "036", "042", "043", "044",
			"052", "053", "054", "055", "062", "063", "064", "065", "072", "073", "074", "075");
	
	   
	/**
	 * 
	 */
	public CBCReportExtraction() {

	}

	/**
	 * 
	 * @param reportParameter
	 * @return
	 * @throws Exception
	 */
	public String generate(ReportParameter reportParameter) throws Exception {
		
		/*createWorkbook(null);
		XSSFSheet sheet = wb.createSheet();
		sheet.lockDeleteColumns();
		sheet.lockDeleteRows();
		sheet.lockFormatCells();
		sheet.lockFormatColumns();
		sheet.lockFormatRows();
		sheet.lockInsertColumns();
		sheet.lockInsertRows();*/
		
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet();

		CellStyle style = wb.createCellStyle();
		for (int i = 1; i < 162; i++) {
			sheet.setColumnWidth(i, 6000);	
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

		iRow = dataTable(sheet, iRow, style);

		Row tmpRowEnd = sheet.createRow(iRow++ + 1);
		tmpRowEnd.setRowStyle(style);

		iRow = iRow + 2;
		String fileName = "output/CBC_Report_Extraction_" + DateUtils.getDateLabel(DateUtils.today(), "yyyyMMddHHmmss") + ".xls";

        final FileOutputStream out = new FileOutputStream(fileName);
        wb.write(out);
        out.close();

		return fileName;
	}
	
	/**
	 * 
	 * @param sheet
	 * @param iRow
	 * @param style
	 * @return
	 * @throws Exception
	 */
	private int dataTable(final Sheet sheet, int iRow, final CellStyle style) throws Exception {
		/* Create total data header */

		int iCol = 0;
		Row tmpRow = sheet.createRow(iRow++);
		createCell(tmpRow, iCol++, "CustomerID");		
		createCell(tmpRow, iCol++, "IDCode");		
		createCell(tmpRow, iCol++, "IDNumber");		
		createCell(tmpRow, iCol++, "IDExpiryDate");		
		createCell(tmpRow, iCol++, "IDCode2");
		createCell(tmpRow, iCol++, "IDNumber2");
		createCell(tmpRow, iCol++, "IDExpiryDate2");		
		createCell(tmpRow, iCol++, "IDCode3");		
		createCell(tmpRow, iCol++, "IDNumber3");		
		createCell(tmpRow, iCol++, "IDExpiryDate3");		
		createCell(tmpRow, iCol++, "DateOfBirth");
		createCell(tmpRow, iCol++, "FamilyNameEng");		
		createCell(tmpRow, iCol++, "FirstNameEng");		
		createCell(tmpRow, iCol++, "SecondNameEng");		
		createCell(tmpRow, iCol++, "ThirdNameEng");		
		createCell(tmpRow, iCol++, "UnformattedNameEng");		
		createCell(tmpRow, iCol++, "MotherNameEng");
		createCell(tmpRow, iCol++, "FamilyNameKhm");		
		createCell(tmpRow, iCol++, "FirstNameKhm");		
		createCell(tmpRow, iCol++, "SecondNameKhm");		
		createCell(tmpRow, iCol++, "ThirdNameKhm");		
		createCell(tmpRow, iCol++, "UnformattedNameKh");
		createCell(tmpRow, iCol++, "MotherNameKhm");		
		createCell(tmpRow, iCol++, "GenderCode");		
		createCell(tmpRow, iCol++, "MaritalStatusCode");		
		createCell(tmpRow, iCol++, "NationalityCode");		
		createCell(tmpRow, iCol++, "TaxpayerRegistrationNumber");
		createCell(tmpRow, iCol++, "ApplicantTypeCode");
		createCell(tmpRow, iCol++, "AddressTypeCode");		
		createCell(tmpRow, iCol++, "Province");		
		createCell(tmpRow, iCol++, "District");		
		createCell(tmpRow, iCol++, "Commune");		
		createCell(tmpRow, iCol++, "Village");
		createCell(tmpRow, iCol++, "AddressField1Eng");		
		createCell(tmpRow, iCol++, "AddressField2Eng");		
		createCell(tmpRow, iCol++, "AddressField1Khm");		
		createCell(tmpRow, iCol++, "AddressField2Khm");		
		createCell(tmpRow, iCol++, "CityEng");		
		createCell(tmpRow, iCol++, "CityKhm");
		createCell(tmpRow, iCol++, "CountryCode");		
		createCell(tmpRow, iCol++, "PostalCode");		
		createCell(tmpRow, iCol++, "AddressTypeCode2");		
		createCell(tmpRow, iCol++, "Province2");		
		createCell(tmpRow, iCol++, "District2");
		createCell(tmpRow, iCol++, "Commune2");
		createCell(tmpRow, iCol++, "Village2");
		createCell(tmpRow, iCol++, "AddressField1Eng2");		
		createCell(tmpRow, iCol++, "AddressField2Eng2");		
		createCell(tmpRow, iCol++, "AddressField1Khm2");		
		createCell(tmpRow, iCol++, "AddressField2Khm2");		
		createCell(tmpRow, iCol++, "CityEng2");
		createCell(tmpRow, iCol++, "CityKhm2");		
		createCell(tmpRow, iCol++, "CountryCode2");		
		createCell(tmpRow, iCol++, "PostalCode2");		
		createCell(tmpRow, iCol++, "AddressTypeCode3");		
		createCell(tmpRow, iCol++, "Province3");
		createCell(tmpRow, iCol++, "District3");
		createCell(tmpRow, iCol++, "Commune3");
		createCell(tmpRow, iCol++, "Village3");
		createCell(tmpRow, iCol++, "AddressField1Eng3");
		createCell(tmpRow, iCol++, "AddressField2Eng3");
		createCell(tmpRow, iCol++, "AddressField1Khm3");
		createCell(tmpRow, iCol++, "AddressField2Khm3");
		createCell(tmpRow, iCol++, "CityEng3");
		createCell(tmpRow, iCol++, "CityKhm3");
		createCell(tmpRow, iCol++, "CountryCode3");
		createCell(tmpRow, iCol++, "PostalCode3");
		createCell(tmpRow, iCol++, "EmailAddress");
		createCell(tmpRow, iCol++, "ContactNumberTypeCode");
		createCell(tmpRow, iCol++, "ContactNumberCountryCode");
		createCell(tmpRow, iCol++, "ContactNumberArea");
		createCell(tmpRow, iCol++, "ContactNumberNumber");
		createCell(tmpRow, iCol++, "ContactNumberExtension");
		createCell(tmpRow, iCol++, "ContactNumberTypeCode2");
		createCell(tmpRow, iCol++, "ContactNumberCountryCode2");
		createCell(tmpRow, iCol++, "ContactNumberArea2");
		createCell(tmpRow, iCol++, "ContactNumberNumber2");
		createCell(tmpRow, iCol++, "ContactNumberExtension2");
		createCell(tmpRow, iCol++, "ContactNumberTypeCode3");
		createCell(tmpRow, iCol++, "ContactNumberCountryCode3");
		createCell(tmpRow, iCol++, "ContactNumberArea3");
		createCell(tmpRow, iCol++, "ContactNumberNumber3");
		createCell(tmpRow, iCol++, "ContactNumberExtension3");
		createCell(tmpRow, iCol++, "EmployerTypeCode");
		createCell(tmpRow, iCol++, "SelfEmployed");
		createCell(tmpRow, iCol++, "EmployerNameEng");
		createCell(tmpRow, iCol++, "EmployerNameKhm");
		createCell(tmpRow, iCol++, "EconomicSector");
		createCell(tmpRow, iCol++, "BusinessType");
		createCell(tmpRow, iCol++, "EmployerAddressEng");
		createCell(tmpRow, iCol++, "EmployerAddressKhm");
		createCell(tmpRow, iCol++, "EmployerProvince");
		createCell(tmpRow, iCol++, "EmployerDistrict");
		createCell(tmpRow, iCol++, "EmployerCommune");
		createCell(tmpRow, iCol++, "EmployerVillage");
		createCell(tmpRow, iCol++, "EmployerAddressCityEng");
		createCell(tmpRow, iCol++, "EmployerAddressCityKhm");
		createCell(tmpRow, iCol++, "EmployerAddressCountryCode");
		createCell(tmpRow, iCol++, "EmployerAddressPostalCode");
		createCell(tmpRow, iCol++, "OccupationEng");
		createCell(tmpRow, iCol++, "OccupationKhm");
		createCell(tmpRow, iCol++, "DateOfEmployment");
		createCell(tmpRow, iCol++, "LengthOfService");
		createCell(tmpRow, iCol++, "ContractExpiryDate");
		createCell(tmpRow, iCol++, "IncomeCurrency");
		createCell(tmpRow, iCol++, "MonthlyBasicIncome");
		createCell(tmpRow, iCol++, "MonthlyTotalIncome");
		createCell(tmpRow, iCol++, "EmployerTypeCode2");
		createCell(tmpRow, iCol++, "SelfEmployed2");
		createCell(tmpRow, iCol++, "EmployerNameEng2");
		createCell(tmpRow, iCol++, "EmployerNameKhm2");
		createCell(tmpRow, iCol++, "EconomicSector2");
		createCell(tmpRow, iCol++, "BusinessType2");
		createCell(tmpRow, iCol++, "EmployerAddressEng2");
		createCell(tmpRow, iCol++, "EmployerAddressKhm2");
		createCell(tmpRow, iCol++, "EmployerProvince2");
		createCell(tmpRow, iCol++, "EmployerDistrict2");
		createCell(tmpRow, iCol++, "EmployerCommune2");
		createCell(tmpRow, iCol++, "EmployerVillage2");
		createCell(tmpRow, iCol++, "EmployerAddressCityEng2");
		createCell(tmpRow, iCol++, "EmployerAddressCityKhm2");
		createCell(tmpRow, iCol++, "EmployerAddressCountryCode2");
		createCell(tmpRow, iCol++, "EmployerAddressPostalCode2");
		createCell(tmpRow, iCol++, "OccupationEng2");
		createCell(tmpRow, iCol++, "OccupationKhm2");
		createCell(tmpRow, iCol++, "DateOfEmployment2");
		createCell(tmpRow, iCol++, "LengthOfService2");
		createCell(tmpRow, iCol++, "ContractExpiryDate2");
		createCell(tmpRow, iCol++, "IncomeCurrency2");
		createCell(tmpRow, iCol++, "MonthlyBasicIncome2");
		createCell(tmpRow, iCol++, "MonthlyTotalIncome2");
		createCell(tmpRow, iCol++, "EmployerTypeCode3");
		createCell(tmpRow, iCol++, "SelfEmployed3");
		createCell(tmpRow, iCol++, "EmployerNameEng3");
		createCell(tmpRow, iCol++, "EmployerNameKhm3");
		createCell(tmpRow, iCol++, "EconomicSector3");
		createCell(tmpRow, iCol++, "BusinessType3");
		createCell(tmpRow, iCol++, "EmployerAddressEng3");
		createCell(tmpRow, iCol++, "EmployerAddressKhm3");
		createCell(tmpRow, iCol++, "EmployerProvince3");
		createCell(tmpRow, iCol++, "EmployerDistrict3");
		createCell(tmpRow, iCol++, "EmployerCommune3");
		createCell(tmpRow, iCol++, "EmployerVillage3");
		createCell(tmpRow, iCol++, "EmployerAddressCityEng3");
		createCell(tmpRow, iCol++, "EmployerAddressCityKhm3");
		createCell(tmpRow, iCol++, "EmployerAddressCountryCode3");
		createCell(tmpRow, iCol++, "EmployerAddressPostalCode3");
		createCell(tmpRow, iCol++, "OccupationEng3");
		createCell(tmpRow, iCol++, "OccupationKhm3");
		createCell(tmpRow, iCol++, "DateOfEmployment3");
		createCell(tmpRow, iCol++, "LengthOfService3");
		createCell(tmpRow, iCol++, "ContractExpiryDate3");
		createCell(tmpRow, iCol++, "IncomeCurrency3");
		createCell(tmpRow, iCol++, "MonthlyBasicIncome3");
		createCell(tmpRow, iCol++, "MonthlyTotalIncome3");
		createCell(tmpRow, iCol++, "CreditorID");
		createCell(tmpRow, iCol++, "AccountTypeCode");
		createCell(tmpRow, iCol++, "GroupAccountReference");
		createCell(tmpRow, iCol++, "AccountNumber");
		createCell(tmpRow, iCol++, "DateIssued");
		createCell(tmpRow, iCol++, "ProductTypeCode");
		createCell(tmpRow, iCol++, "Currency");
		createCell(tmpRow, iCol++, "LoanAmount");
		createCell(tmpRow, iCol++, "ProductExpiryDate");
		createCell(tmpRow, iCol++, "ProductStatusCode");
		createCell(tmpRow, iCol++, "RestructuredLoan");
		createCell(tmpRow, iCol++, "InstalmentAmount");
		createCell(tmpRow, iCol++, "PaymentFrequencyCode");
		createCell(tmpRow, iCol++, "Tenure");
		createCell(tmpRow, iCol++, "LastPaymentDate");
		createCell(tmpRow, iCol++, "LastAmountPaid");
		createCell(tmpRow, iCol++, "SecurityTypeCode");
		createCell(tmpRow, iCol++, "OutstandingBalance");
		createCell(tmpRow, iCol++, "PastDue");
		createCell(tmpRow, iCol++, "NextPaymentDate");
		createCell(tmpRow, iCol++, "PaymentStatusCode");
		createCell(tmpRow, iCol++, "AsOfDate");
		createCell(tmpRow, iCol++, "WriteOffStatusCode");
		createCell(tmpRow, iCol++, "WriteOffStatusDate");
		createCell(tmpRow, iCol++, "OriginalAmountAsAtLoadDate");
		createCell(tmpRow, iCol++, "OutstandingBalanceAsAtLoadDate");	
		
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<>(Contract.class);
		restrictions.addCriterion(Restrictions.ne(CONTRACT_STATUS, ContractWkfStatus.COM));		
		DetachedCriteria userSubCriteria = DetachedCriteria.forClass(HistoryProcess.class, "hispro");
		userSubCriteria.createAlias("hispro.history", "hist", JoinType.INNER_JOIN);
		userSubCriteria.add(Restrictions.eq("processType", EProcessType.CB));
		userSubCriteria.setProjection(Projections.projectionList().add(Projections.property("hist.contract.id")));
		restrictions.addCriterion(Property.forName("id").notIn(userSubCriteria) );
		// restrictions.addCriterion(Restrictions.eq(REFERENCE, "GLF-PNP-11-00001868"));
		
		List<Contract> contracts = contractService.list(restrictions);
		int nbRows = contracts.size();
		
		int customerId = 0;		
		for (int i = 0; i < nbRows; i++) {
			customerId++;
			Contract contract = contracts.get(i);
			Quotation quotation = contract.getQuotation();
			
			System.out.println("iter = " + i + "/" + nbRows + " (" + quotation.getReference() + ")");
			
			List<QuotationApplicant> quotationApplicants = quotation.getQuotationApplicants();
			Applicant applicant = getApplicant(quotationApplicants, EApplicantType.C);
			Applicant guarantor = getApplicant(quotationApplicants, EApplicantType.G);

			List<Employment> employments = applicant.getEmployments();
			Date dateIssued = quotation.getContractStartDate();
			
			double applicantRevenus = 0d;
			double applicantAllowance = 0d;
			double applicantBusinessExpenses = 0d;
			
			
			List<Cashflow> cashflows = getCashflowsNoCancel(contract.getId());
			Amount outstandingBalance = new Amount(0d, 0d, 0d);
			
			double lastAmountPaid = 0d;
			Date lastPaymentDate = null;
			Date nextPaymentDate = null;
			Amount pastDueAmount =  new Amount(0d, 0d, 0d);
			Date productExpire = DateUtils.addMonthsDate(quotation.getFirstPaymentDate(), quotation.getTerm());
			
			String productStatusCode = "N";
			String paymentStatusCode = "0";				
			
			Date today = DateUtils.todayH00M00S00();
			Date paymentStatusDate = getWkfStatusDate(cashflows);
			long numberOfDays = 0;
			if (paymentStatusDate != null) {
				numberOfDays = DateUtils.getDiffInDays(today, DateUtils.getDateAtBeginningOfDay(paymentStatusDate));
			}
			
			if (DateUtils.getDateAtBeginningOfDay(contract.getFirstInstallmentDate()).compareTo(today) > 0) {
				paymentStatusCode = "N";
			} else if (contract.getWkfStatus() == ContractWkfStatus.EAR ||					   
					   contract.getWkfStatus() == ContractWkfStatus.CLO ||
					   contract.getWkfStatus() == ContractWkfStatus.REP ||
					   contract.getWkfStatus() == ContractWkfStatus.THE ||
					   contract.getWkfStatus() == ContractWkfStatus.ACC ||
					   contract.getWkfStatus() == ContractWkfStatus.FRA ) {
				productStatusCode = "C";
				paymentStatusCode = "C";
				nextPaymentDate = null;
				HistoryProcess historyProcess = new HistoryProcess();
				historyProcess.setProcessType(EProcessType.CB);
				historyProcess.setProcessDate(DateUtils.today());
				// TODO PYI
//				historyProcess.setHistory(getHistoryByContractStatus(contract.getHistories(), contract.getWkfStatus()));
				contractService.saveOrUpdate(historyProcess);
			}else if(contract.getWkfStatus() == ContractWkfStatus.WRI){ 
				productStatusCode = "W";
				paymentStatusCode = "W";
				nextPaymentDate = null;
				HistoryProcess historyProcess = new HistoryProcess();
				historyProcess.setProcessType(EProcessType.CB);
				historyProcess.setProcessDate(DateUtils.today());
				// TODO PYI
//				historyProcess.setHistory(getHistoryByContractStatus(contract.getHistories(), contract.getWkfStatus()));
				contractService.saveOrUpdate(historyProcess);
			}else if(contract.getWkfStatus() == ContractWkfStatus.LOS){ 
				productStatusCode = "L";
				paymentStatusCode = "L";
				nextPaymentDate = null;
				HistoryProcess historyProcess = new HistoryProcess();
				historyProcess.setProcessType(EProcessType.CB);
				historyProcess.setProcessDate(DateUtils.today());
				// TODO PYI
//				historyProcess.setHistory(getHistoryByContractStatus(contract.getHistories(), contract.getWkfStatus()));
				contractService.saveOrUpdate(historyProcess);
			}else if (numberOfDays <= 0) {
				productStatusCode = "N";
				paymentStatusCode = "0";
			} else if (numberOfDays <= 29) {
				productStatusCode = "N";
				paymentStatusCode = "1";
			} else if (numberOfDays <= 59) {
				productStatusCode = "N";
				paymentStatusCode = "2";
			} else if (numberOfDays <= 89) {
				productStatusCode = "N";
				paymentStatusCode = "3";
			} else if (numberOfDays <= 119) {
				productStatusCode = "N";
				paymentStatusCode = "4";
			} else if (numberOfDays <= 149) {
				productStatusCode = "N";
				paymentStatusCode = "5";
			} else if (numberOfDays <= 179) {
				productStatusCode = "N";
				paymentStatusCode = "6";
			} else if (numberOfDays <= 209) {
				productStatusCode = "D";
				paymentStatusCode = "7";
			} else if (numberOfDays <= 239) {
				productStatusCode = "D";
				paymentStatusCode = "8";
			} else if (numberOfDays <= 269) {
				productStatusCode = "D";
				paymentStatusCode = "9";
			} else if (numberOfDays <= 299) {
				productStatusCode = "D";
				paymentStatusCode = "T";
			} else if (numberOfDays <= 329) {
				productStatusCode = "D";
				paymentStatusCode = "E";
			} else if (numberOfDays <= 359) {
				productStatusCode = "L";
				paymentStatusCode = "L";
				nextPaymentDate = null;
				HistoryProcess historyProcess = new HistoryProcess();
				historyProcess.setProcessType(EProcessType.CB);
				historyProcess.setProcessDate(DateUtils.today());
				// TODO PYI
//				historyProcess.setHistory(getHistoryByContractStatus(contract.getHistories(), contract.getWkfStatus()));
				contractService.saveOrUpdate(historyProcess);
			} 
			
			if ("N".equals(paymentStatusCode) || "R".equals(paymentStatusCode)
					 || "Q".equals(paymentStatusCode)  || "D".equals(paymentStatusCode) || "V".equals(paymentStatusCode)) {
				lastAmountPaid = 0d;
				lastPaymentDate = null;
			} else {
				Payment lastPayment = getLastPayment(cashflows);
				if (lastPayment != null) {
					lastAmountPaid = lastPayment.getTiPaidUsd() != null ? lastPayment.getTiPaidUsd() : 0d;
					lastPaymentDate = lastPayment.getPaymentDate();
				} else {
					lastPaymentDate = contract.getStartDate();
					// TODO PYI
//					lastAmountPaid = MyMathUtils.roundAmountTo(contract.getAsset().getTiCashPriceUsd() * contract.getAdvancePaymentPercentage() / 100);
				}
			}
			
			if ("N".equals(paymentStatusCode) || "R".equals(paymentStatusCode) || "C".equals(paymentStatusCode)) {
				outstandingBalance = new Amount(0d, 0d, 0d);
				pastDueAmount = new Amount(0d, 0d, 0d);
			} else {
				outstandingBalance = getPrincipalBalance(DateUtils.getDateAtEndOfDay(DateUtils.todayDate()), cashflows);
				pastDueAmount = getTotalAmountInOverdueUsd(DateUtils.todayH00M00S00(), cashflows);
				if (!"L".equals(paymentStatusCode)) {
					nextPaymentDate = getNextPaymentDate(cashflows);
					if (nextPaymentDate == null) {
						nextPaymentDate = DateUtils.addDaysDate(DateUtils.todayH00M00S00(), 1);
					}
				}
			}
			
			QuotationDocument applicantQuotationDocument = null;
			List<QuotationDocument> applicantDocuments = quotation.getQuotationDocuments(EApplicantType.C);
			if (applicantDocuments != null && !applicantDocuments.isEmpty()) {
				Collections.sort(applicantDocuments, new DocumentComparatorBySortIndex());
				for (QuotationDocument quotationDocument : applicantDocuments) {
					if (quotationDocument.getDocument().isSubmitCreditBureau()) {
						applicantQuotationDocument = quotationDocument;
						break;
					}
				}
			}
			for (Employment employmentAmount : employments) {
				applicantRevenus += MyNumberUtils.getDouble(employmentAmount.getRevenue()) ;
				applicantAllowance += MyNumberUtils.getDouble(employmentAmount.getAllowance()) ;
				applicantBusinessExpenses += MyNumberUtils.getDouble(employmentAmount.getBusinessExpense());
			}
			Double monthlyBasicIncom = applicantRevenus + applicantAllowance 
					- applicantBusinessExpenses
					- MyNumberUtils.getDouble(applicant.getMonthlyFamilyExpenses())
					- MyNumberUtils.getDouble(applicant.getMonthlyPersonalExpenses());
			
			if (applicantQuotationDocument != null) {
				iCol = 0;
				iRow = generateRow(sheet, applicant, quotation, applicantQuotationDocument, "P", 
						monthlyBasicIncom, productExpire, dateIssued,
						lastPaymentDate, lastAmountPaid, outstandingBalance.getTiAmountUsd(), pastDueAmount.getTiAmountUsd(), 
						nextPaymentDate, productStatusCode, paymentStatusCode, iRow, customerId);
				
				if (guarantor != null) {
					customerId = customerId +1;
					
					QuotationDocument guarantorQuotationDocument = null;
					List<QuotationDocument> guarantorDocuments = quotation.getQuotationDocuments(EApplicantType.G);
					if (guarantorDocuments != null && !guarantorDocuments.isEmpty()) {
						Collections.sort(guarantorDocuments, new DocumentComparatorBySortIndex());
						guarantorQuotationDocument = guarantorDocuments.get(0);
						for (QuotationDocument quotationDocument : guarantorDocuments) {
							if (quotationDocument.getDocument().isSubmitCreditBureau()) {
								guarantorQuotationDocument = quotationDocument;
								break;
							}
						}
					}
					
					iRow = generateRow(sheet, guarantor, quotation, guarantorQuotationDocument, "G",
							monthlyBasicIncom, productExpire, dateIssued,
							lastPaymentDate, lastAmountPaid,
							outstandingBalance.getTiAmountUsd(), pastDueAmount.getTiAmountUsd(), nextPaymentDate, productStatusCode, paymentStatusCode, iRow, customerId);
				}
			}
		}
		return iRow; 
	}
	
	/**
	 * 
	 * @param sheet
	 * @param applicant
	 * @param quotation
	 * @param documentCode
	 * @param reference
	 * @param expireDate
	 * @param applicantTypeCode
	 * @param monthlyBasicIncom
	 * @param productExpire
	 * @param dateIssued
	 * @param productStatusCode
	 * @param lastPaymentDate
	 * @param lastAmountPaid
	 * @param outstandingBalance
	 * @param nextPaymentDate
	 * @param paymentStatusCode
	 * @param iRow
	 * @param customerId
	 * @return
	 */
	private int generateRow(final Sheet sheet, Applicant applicant, Quotation quotation, QuotationDocument quotationDocument,
			String applicantTypeCode, Double monthlyBasicIncom, 
			Date productExpire, Date dateIssued, Date lastPaymentDate, double lastAmountPaid,
			double outstandingBalance, double pastDueAmount, Date nextPaymentDate, String productStatusCode, String paymentStatusCode, int iRow, int customerId) {
		
		Employment employment = applicant.getCurrentEmployment();
		Address address = applicant.getMainAddress();
		String accountNumber = quotation.getReference();
		String houseNo = ""; 
		String str = "";
		if (address != null) {
			if (address.getHouseNo() != null) {
				houseNo = address.getHouseNo();
			} 
			if(address.getStreet() != null){
				str = address.getStreet();
			} 
		}
		int iCol = 0;
		Row tmpRow = sheet.createRow(iRow++);
		createNumericCell(tmpRow, iCol++, customerId);
		createCell(tmpRow, iCol++, quotationDocument.getDocument().getCode());
		createCell(tmpRow, iCol++, quotationDocument.getReference());
		createCell(tmpRow, iCol++, (quotationDocument.getExpireDate() != null ? DateUtils.formatDate(quotationDocument.getExpireDate(),"ddMMyyyy") : ""));
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, DateUtils.formatDate(applicant.getBirthDate(),"ddMMyyyy"));
		createCell(tmpRow, iCol++, removeNonEngCharacters(applicant.getLastNameEn()));
		createCell(tmpRow, iCol++, removeNonEngCharacters(applicant.getFirstNameEn()));
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, applicant.getLastName());
		createCell(tmpRow, iCol++, applicant.getFirstName());
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, applicant.getGender().toString());
		createCell(tmpRow, iCol++, applicant.getMaritalStatus().toString());
		createCell(tmpRow, iCol++, applicant.getNationality().getCode());
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, applicantTypeCode);
		createCell(tmpRow, iCol++,"RESID");
		
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "#" + removeNonEngCharacters(houseNo) + ", " + removeNonEngCharacters(str) 
				+ ", "+ removeNonEngCharacters(address.getVillage().getDescEn())
				+ ", " + removeNonEngCharacters(address.getCommune().getDescEn())
				+ ", " + removeNonEngCharacters(address.getDistrict().getDescEn())
				+ ", "+ removeNonEngCharacters(address.getProvince().getDescEn()));
		
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, removeNonEngCharacters(address.getProvince().getDescEn()));
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "KHM");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");

		String phoneNumber = formatPhoneNumber(removeNonEngCharacters(applicant.getMobile()));
		String phoneNumberType = "";
		String phoneNumberCountryCode = "";
		String phoneNumberAreaCode = "";
		
		if (StringUtils.isNotEmpty(phoneNumber)) {
			phoneNumberCountryCode = "855";
			String areaCode = phoneNumber.substring(0, 3);
			if (AREA_CODE_FIX.contains(areaCode)) {
				phoneNumber = phoneNumber.substring(3);
				if (phoneNumber.length() == 6) {
					phoneNumberType = "O";
					phoneNumberAreaCode = areaCode;
				} else {
					phoneNumberCountryCode = "";
					areaCode = "";
					phoneNumber = "";
				}
			} else {
				phoneNumberType = "M";
			}
		}
		
		createCell(tmpRow, iCol++, phoneNumberType);
		createCell(tmpRow, iCol++, phoneNumberCountryCode);
		createCell(tmpRow, iCol++, phoneNumberAreaCode);
		createCell(tmpRow, iCol++, phoneNumber);
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, formatOccupationEng(removeNonEngCharacters(employment.getPosition())));
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "USD");
		createNumericCell(tmpRow, iCol++, MyMathUtils.roundAmountTo(monthlyBasicIncom));
		createNumericCell(tmpRow, iCol++, MyMathUtils.roundAmountTo(monthlyBasicIncom));
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, "330");
		createCell(tmpRow, iCol++, "S");
		createCell(tmpRow, iCol++, "");
		createCell(tmpRow, iCol++, accountNumber);
		
		createCell(tmpRow, iCol++, (dateIssued != null ? DateUtils.formatDate(dateIssued,"ddMMyyyy") : ""));
		createCell(tmpRow, iCol++, "MTL");
		createCell(tmpRow, iCol++, "USD");
		// TODO PYI		createNumericCell(tmpRow, iCol++, MyMathUtils.roundAmountTo(MyNumberUtils.getDouble(quotation.getTiPrincipalUsd())));
//		createCell(tmpRow, iCol++, DateUtils.formatDate(productExpire,"ddMMyyyy"));
//		createCell(tmpRow, iCol++, productStatusCode);
//		createCell(tmpRow, iCol++, "N");
//		createNumericCell(tmpRow, iCol++, MyMathUtils.roundAmountTo(quotation.getTiInstallmentUsd()));
		createCell(tmpRow, iCol++, "M");
		createNumericCell(tmpRow, iCol++, quotation.getTerm());
		createCell(tmpRow, iCol++, (lastPaymentDate != null ? DateUtils.formatDate(lastPaymentDate,"ddMMyyyy") : ""));
		createNumericCell(tmpRow, iCol++, MyMathUtils.roundAmountTo(lastAmountPaid));
		createCell(tmpRow, iCol++, "MV");
		createNumericCell(tmpRow, iCol++, MyMathUtils.roundAmountTo(outstandingBalance));
		createNumericCell(tmpRow, iCol++, MyMathUtils.roundAmountTo(pastDueAmount));
		createCell(tmpRow, iCol++, (nextPaymentDate != null ? DateUtils.formatDate(nextPaymentDate,"ddMMyyyy") : ""));
		createCell(tmpRow, iCol++, paymentStatusCode);
		createCell(tmpRow, iCol++, DateUtils.formatDate(DateUtils.today(),"ddMMyyyy"));
		
		String writeOffStatus = "";
		String writeOffStatusDate = "";
		String writeOffOriginalAmount = "";
		String writeOffOustandingBalance = "";
		if ("W".equals(paymentStatusCode)) {
			writeOffStatus = "OS";
			writeOffStatusDate = DateUtils.formatDate(DateUtils.today(),"ddMMyyyy");
			writeOffOriginalAmount = "" + MyMathUtils.roundAmountTo(outstandingBalance);
			writeOffOustandingBalance = "" + MyMathUtils.roundAmountTo(outstandingBalance);
		}
		
		createCell(tmpRow, iCol++, writeOffStatus);
		createCell(tmpRow, iCol++, writeOffStatusDate);
		createCell(tmpRow, iCol++, writeOffOriginalAmount);
		createCell(tmpRow, iCol++, writeOffOustandingBalance);
		return iRow;
	}

	/**
	 * 
	 * @param row
	 * @param iCol
	 * @param value
	 * @return
	 */
	protected Cell createCell(final Row row, final int iCol, final String value) {
		final Cell cell = row.createCell(iCol);		
		cell.setCellValue((value == null ? "" : value));
		return cell;
	}

	/**
	 * 
	 * @param row
	 * @param iCol
	 * @param value
	 * @return
	 */
	private Cell createNumericCell(final Row row, final int iCol, final Object value) {
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
            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        } else {
        }
		return cell;
	}

	/**
	 * 
	 * @param date
	 * @param formatPattern
	 * @return
	 */
	public String getDateLabel(final Date date, final String formatPattern) {
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
	 * @param quotationApplicants
	 * @param applicantType
	 * @return
	 */
	private Applicant getApplicant(List<QuotationApplicant> quotationApplicants, EApplicantType applicantType) {
		if (quotationApplicants != null && !quotationApplicants.isEmpty()) {
			for (QuotationApplicant quotationApplicant : quotationApplicants) {
				if (applicantType == quotationApplicant.getApplicantType()) {
					return quotationApplicant.getApplicant();
				}
			}
		}
		return null;
	}
	
	/**
	 * @author ly.youhort
	 */
	private class DocumentComparatorBySortIndex implements Comparator<Object> {
		public int compare(Object o1, Object o2) {
			QuotationDocument c1 = (QuotationDocument) o1;
			QuotationDocument c2 = (QuotationDocument) o2;
			if (c1 == null || c1.getDocument().getSortIndex() == null) {
				if (c2 == null || c2.getDocument().getSortIndex() == null) {
					return 0;
				}
				return 1;
			}
			if (c2 == null || c2.getDocument().getSortIndex() == null) {
				return -1;
			}
			return c1.getDocument().getSortIndex().compareTo(c2.getDocument().getSortIndex());
		}
	}
	
	/**
	 * @param cotraId
	 * @return
	 */
	private List<Cashflow> getCashflowsNoCancel(Long cotraId) {
		/*BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<>(Cashflow.class);
		restrictions.addCriterion(Restrictions.eq(CANCEL, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.eq(CONTRACT + "." + ID, cotraId));
		restrictions.addOrder(Order.asc(NUM_INSTALLMENT));
		*/
		List<Cashflow> cashflows = new ArrayList<>();
		
		String query = 
				"SELECT "
				+ " c.caflw_id, "
				+ " c.catyp_code, "
				+ " c.caflw_dt_installment, "
				+ " c.caflw_dt_period_start, "
				+ " c.caflw_dt_period_end, "
				+ " c.caflw_bl_cancel, "
				+ " c.caflw_bl_paid, "
				+ " c.caflw_bl_unpaid, "
				+ " c.caflw_am_te_installment_usd, "
				+ " c.caflw_am_vat_installment_usd, "
				+ " c.caflw_am_ti_installment_usd, "
				+ " c.caflw_nu_num_installment, "
				+ " c.cacode_code, "
				+ " c.paymn_id, "
				+ " p.paymn_dt_installment, "
				+ " p.paymn_am_ti_paid_usd, "
				+ " p.paymn_dt_payment"
				+ " FROM td_cashflow c"
				+ " left join td_payment p on p.paymn_id = c.paymn_id" 
				+ " WHERE c.cotra_id = " + cotraId			
				+ " AND (c.caflw_bl_cancel is false or cacode_code is not null)"
				+ " AND catyp_code in('CAP', 'IAP', 'PER')"
				+ " ORDER BY c.caflw_nu_num_installment asc";
		
		try {
			List<NativeRow> cashflowRows = contractService.executeSQLNativeQuery(query);
			for (NativeRow row : cashflowRows) {
		      	List<NativeColumn> columns = row.getColumns();
		      	int i = 0;
		      	Cashflow cashflow = new Cashflow();
		      	cashflow.setId((Long) columns.get(i++).getValue());
		      	cashflow.setCashflowType(ECashflowType.getByCode(columns.get(i++).getValue().toString()));
		      	cashflow.setInstallmentDate((Date) columns.get(i++).getValue());
		      	cashflow.setPeriodStartDate((Date) columns.get(i++).getValue());
		      	cashflow.setPeriodEndDate((Date) columns.get(i++).getValue());
		      	cashflow.setCancel((Boolean) columns.get(i++).getValue());
		      	cashflow.setPaid((Boolean) columns.get(i++).getValue());
		      	cashflow.setUnpaid((Boolean) columns.get(i++).getValue());
		      	cashflow.setTeInstallmentUsd((Double) columns.get(i++).getValue());
		      	cashflow.setVatInstallmentUsd((Double) columns.get(i++).getValue());
		      	cashflow.setTiInstallmentUsd((Double) columns.get(i++).getValue());
		      	cashflow.setNumInstallment((Integer) columns.get(i++).getValue());
		      	String cashflowCode = (String) columns.get(i++).getValue();
		      	if (StringUtils.isNotEmpty(cashflowCode)) {
		      		cashflow.setCashflowCode(ECashflowCode.getByCode(cashflowCode));
		      	}
		      	
		      	Long paymnId = (Long) columns.get(i).getValue();
		      	if (paymnId != null && paymnId.longValue() > 0) {
		      		Payment payment = new Payment();
		      		payment.setId((Long) columns.get(i++).getValue());
		      		payment.setInstallmentDate((Date) columns.get(i++).getValue());
		      		payment.setTiPaidUsd((Double) columns.get(i++).getValue());
		      		payment.setPaymentDate((Date) columns.get(i++).getValue());
		      		cashflow.setPayment(payment);
		      	}
		      	cashflows.add(cashflow);
		    }
		} catch (NativeQueryException e) {
			logger.error(e.getMessage(), e);
		}
		
		return cashflows;
	}
	
	/**
	 * Get principal balance
	 * @param calculDate
	 * @param cashflows
	 * @return
	 */
	private Amount getPrincipalBalance(Date calculDate, List<Cashflow> cashflows) {
		Amount principalBalance = new Amount(0d, 0d, 0d);		
		for (Cashflow cashflow : cashflows) {
			if (cashflow.getCashflowType() == ECashflowType.CAP
					&& !cashflow.isCancel()
					&& (!cashflow.isPaid() || (cashflow.getPayment() != null && cashflow.getPayment().getPaymentDate().compareTo(calculDate) > 0))
					&& !cashflow.isUnpaid()) {
				principalBalance.plusTiAmountUsd(cashflow.getTiInstallmentUsd());
				principalBalance.plusTeAmountUsd(cashflow.getTeInstallmentUsd());
				principalBalance.plusVatAmountUsd(cashflow.getVatInstallmentUsd());
			}
		}
		return principalBalance;
	}
	
	/**
	 * @param calDate
	 * @param cashflows
	 * @param gracePeriod
	 * @return
	 */
	private Amount getTotalAmountInOverdueUsd(Date calDate, List<Cashflow> cashflows) {
		Amount totalAmountInOverdueUsd = new Amount(0d, 0d,0d);
		for (Cashflow cashflow : cashflows) {
			if (cashflow != null 
					&& !cashflow.isCancel()
					&& !cashflow.isUnpaid()
					&& !cashflow.isPaid()) {
				long nbOverdueInDays = DateUtils.getDiffInDaysPlusOneDay(calDate, DateUtils.getDateAtBeginningOfDay(cashflow.getInstallmentDate()));
				if (nbOverdueInDays > 0) {
					totalAmountInOverdueUsd.plus(new Amount(cashflow.getTeInstallmentUsd(), cashflow.getVatInstallmentUsd(), cashflow.getTiInstallmentUsd()));
				}
			}
		}
		return totalAmountInOverdueUsd;
	}
	
	/**
	 * @param cashflows
	 * @return
	 */
	private Payment getLastPayment(List<Cashflow> cashflows) {
		Payment payment = null;
		for (Cashflow cashflow : cashflows) {
			if (cashflow.getCashflowType() == ECashflowType.IAP
					&& !cashflow.isCancel()
					&& cashflow.isPaid()
					&& (cashflow.getPayment() != null)
					&& !cashflow.isUnpaid()) {
				if (payment == null || payment.getPaymentDate().compareTo(cashflow.getPayment().getPaymentDate()) < 0) {
					payment = cashflow.getPayment();
				} 
			}
		}
		return payment;
	}
	
	/**
	 * @param cashflows
	 * @return
	 */
	private Date getNextPaymentDate(List<Cashflow> cashflows) {
		Cashflow cashflowValue = null;
		for (Cashflow cashflow : cashflows) {
			if (cashflow != null 
					&& cashflow.getCashflowType() == ECashflowType.CAP
					&& !cashflow.isCancel()
					&& !cashflow.isUnpaid()
					&& !cashflow.isPaid()
					&& DateUtils.getDateAtBeginningOfDay(cashflow.getInstallmentDate()).compareTo(DateUtils.todayH00M00S00()) > 0) {
				if (cashflowValue == null || DateUtils.getDateAtBeginningOfDay(cashflowValue.getInstallmentDate()).compareTo(
						DateUtils.getDateAtBeginningOfDay(cashflow.getInstallmentDate())) > 0) {
					cashflowValue = cashflow;
				} 
			}
		}
		return (cashflowValue != null ? DateUtils.getDateAtBeginningOfDay(cashflowValue.getInstallmentDate()) : null);
	}
	
	/**
	 * @param cashflows
	 * @return
	 */
	private Date getWkfStatusDate(List<Cashflow> cashflows) {
		Cashflow cashflowValue = null;
		for (Cashflow cashflow : cashflows) {
			if (cashflow != null 
					&& cashflow.getCashflowType() == ECashflowType.IAP
					&& !cashflow.isCancel()
					&& !cashflow.isUnpaid()
					&& !cashflow.isPaid()) {
				if (cashflowValue == null || DateUtils.getDateAtBeginningOfDay(cashflowValue.getInstallmentDate()).compareTo(
						DateUtils.getDateAtBeginningOfDay(cashflow.getInstallmentDate())) > 0) {
					cashflowValue = cashflow;
				} 
			}
		}
		return (cashflowValue != null ? DateUtils.getDateAtBeginningOfDay(cashflowValue.getInstallmentDate()) : null);
	}
	
	/**
	 * 
	 * @param histories
	 * @param contractStatus
	 * @return
	 */
	private ContractHistory getHistoryByContractStatus(List<ContractHistory> histories, EWkfStatus contractStatus) {
		ContractHistoReason hisReason = null;
		// TODO PYI
//		if (contractStatus == WkfContractStatus.EAR) {
//			hisReason = EhisReason.CONTRACT_0003;
//		} else if (contractStatus == WkfContractStatus.CLO) {
//			hisReason = EhisReason.CONTRACT_0002;
//		} else if (contractStatus == WkfContractStatus.LOS) {
//			hisReason = EhisReason.CONTRACT_LOSS;
//		} else if (contractStatus == WkfContractStatus.REP) {
//			hisReason = EhisReason.CONTRACT_REP;
//		} else if (contractStatus == WkfContractStatus.THE) {
//			hisReason = EhisReason.CONTRACT_THE;
//		} else if (contractStatus == WkfContractStatus.ACC) {
//			hisReason = EhisReason.CONTRACT_ACC;
//		} else if (contractStatus == WkfContractStatus.FRA) {
//			hisReason = EhisReason.CONTRACT_FRA;
//		} else if (contractStatus == WkfContractStatus.WRI) {
//			hisReason = EhisReason.CONTRACT_WRI;
//		}
//
//		for (HistoryContract history : histories) {
//			if (history.gethisReason() == hisReason) {
//				return history;
//			}
//		}
		return null;
	}
	
	/**
	 * @param phoneNumber
	 * @return
	 */
	private String formatPhoneNumber(String phoneNumber) {
		String phoneNumberFormatted = "";
		if (phoneNumber != null) {
			phoneNumber = phoneNumber.replaceAll(" ", "");
			if (phoneNumber.length() > 8) {
				phoneNumberFormatted = phoneNumber;
				if (!phoneNumberFormatted.startsWith("0")) {
					phoneNumberFormatted = "0" + phoneNumberFormatted;
				}
				if (phoneNumberFormatted.length() > 10) {
					phoneNumberFormatted = "";
				} else if (phoneNumberFormatted.startsWith("012") && phoneNumberFormatted.length() >= 10) {
					phoneNumberFormatted = "";
				}
				
			}
		}
		return phoneNumberFormatted;
	}
	
	/**
	 * 
	 * @param occupation
	 * @return
	 */
	private String formatOccupationEng(String occupation) {
		if (occupation != null) {
			occupation = occupation.replaceAll("[/&]", " ");
		}
		return occupation;
	}
	
	/**
	 * @param value
	 * @return
	 */
	private String removeNonEngCharacters(String value) {
		if (StringUtils.isNotEmpty(value)) {
			return value.replaceAll("[^\\p{ASCII}]", "");
		}
		return "";
	}
}
