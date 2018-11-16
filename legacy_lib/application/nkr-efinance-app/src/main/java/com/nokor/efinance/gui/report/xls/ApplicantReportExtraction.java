package com.nokor.efinance.gui.report.xls;

import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.model.Employment;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.financial.model.FinProduct;
import com.nokor.efinance.core.financial.model.FinService;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationApplicant;
import com.nokor.efinance.core.quotation.model.QuotationDocument;
import com.nokor.efinance.core.quotation.model.QuotationService;
import com.nokor.efinance.core.shared.conf.AppConfig;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.efinance.tools.report.Report;
import com.nokor.ersys.core.hr.model.address.Address;

/**
 * @author sok.vina
 * @author buntha.chea (modified)
 */
public class ApplicantReportExtraction implements Report, GLFApplicantFields, QuotationEntityField {

	// private QuotationService quotationService = SpringUtils.getBean(QuotationService.class);
	
	@Autowired
    private EntityDao quotationService;
	
	private Map<String, CellStyle> styles = null;
	private static final String DATE_FORMAT = "dateFormat";
	private CellStyle style;
	private Workbook wb;
		
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

	/**
	 * 
	 */
	public ApplicantReportExtraction() {

	}

	/**
	 * @see com.nokor.efinance.tools.report.Report#generate(com.nokor.efinance.core.shared.report.ReportParameter)
	 */
	public String generate(ReportParameter reportParameter) throws Exception {
		
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(Quotation.class);
		restrictions.addCriterion("quotationStatus", QuotationWkfStatus.ACT);
		restrictions.addAssociation("dealer", "dea", JoinType.INNER_JOIN);
		
		Map<String, Object> parameters = reportParameter.getParameters();
		Dealer dealer = (Dealer) parameters.get(DEALER);
		EDealerType dealerType = (EDealerType) parameters.get("dealer.type");
		FinProduct financialProduct = (FinProduct) parameters.get("financial.product");

		if (dealer != null) {
			restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));
		}
		
		if (financialProduct != null) {
			restrictions.addCriterion(Restrictions.eq("financialProduct" + "." + ID, financialProduct.getId()));
		}
		
		if (dealerType != null) {
			restrictions.addCriterion(Restrictions.eq("dea.dealerType", dealerType));
		}
		
		restrictions.addOrder(Order.desc("contractStartDate"));
		
		List<Quotation> quotations = quotationService.list(restrictions);

		//createWorkbook(null);
		/*XSSFSheet sheet = wb.createSheet();
		sheet.lockDeleteColumns();
		sheet.lockDeleteRows();
		sheet.lockFormatCells();
		sheet.lockFormatColumns();
		sheet.lockFormatRows();
		sheet.lockInsertColumns();
		sheet.lockInsertRows();*/
	   
		wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("new sheet");

		style = wb.createCellStyle();
		styles = new HashMap<String, CellStyle>();
		createStyles();
		for (int i = 0; i < 89; i++) {
			sheet.setColumnWidth(i, 10000);	
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

//		final Row headerRow = sheet.createRow(2);
//		createCell(headerRow, 6, APPLICANT_TITTLE, 14, true, false, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK);

		int iRow = 0;

		iRow = dataTable(sheet, iRow, style, quotations);

		Row tmpRowEnd = sheet.createRow(iRow++ + 1);
		tmpRowEnd.setRowStyle(style);

		iRow = iRow + 2;

		String fileName = "Applicant_Report_Extraction_" + DateUtils.getDateLabel(DateUtils.today(), "yyyyMMddHHmmssSSS") + ".xls";

        final FileOutputStream out = new FileOutputStream(AppConfig.getInstance().getConfiguration().getString("specific.tmpdir") + "/" + fileName);
        wb.write(out);
        out.close();
		return fileName;
	}

	/**
	 * 
	 * @param sheet
	 * @param iRow
	 * @param style
	 * @param quotations
	 * @return
	 * @throws Exception
	 */
	private int dataTable(final Sheet sheet, int iRow, final CellStyle style, List<Quotation> quotations) throws Exception {
		/* Create total data header */

		int iCol = 0;
		Row tmpRow = sheet.createRow(iRow++);
		createCell(tmpRow, iCol++, "Applicant");
		sheet.addMergedRegion(new CellRangeAddress(iRow-1, iRow-1, iCol-1, iCol + 34));
		createCell(tmpRow, 36, "Guarantor");
		sheet.addMergedRegion(new CellRangeAddress(iRow-1, iRow-1, 36, 67));
		createCell(tmpRow, 68, "Asset");
		sheet.addMergedRegion(new CellRangeAddress(iRow-1, iRow-1, 68, 73));
		
		iCol = 0;
		tmpRow = sheet.createRow(iRow++);
		createCellHeader(tmpRow, iCol++, "Date Issued");
		createCellHeader(tmpRow, iCol++, "Family Name");
		createCellHeader(tmpRow, iCol++, "First Name");
		createCellHeader(tmpRow, iCol++, "GLF account number");
		createCellHeader(tmpRow, iCol++, "Gender");
		createCellHeader(tmpRow, iCol++, "Age");
		createCellHeader(tmpRow, iCol++, "Phone number 1");
		createCellHeader(tmpRow, iCol++, "Phone number 2");
		createCellHeader(tmpRow, iCol++, "Phone number Guarantor");
		createCellHeader(tmpRow, iCol++, "Marital Status");
		createCellHeader(tmpRow, iCol++, "# Children");	
		createCellHeader(tmpRow, iCol++, "Housing");		
		createCellHeader(tmpRow, iCol++, "District");
		createCellHeader(tmpRow, iCol++, "Province");
		createCellHeader(tmpRow, iCol++, "Employment Status");
		createCellHeader(tmpRow, iCol++, "Employment Industry");
		createCellHeader(tmpRow, iCol++, "Seniority Level");
		createCellHeader(tmpRow, iCol++, "Position");
		createCellHeader(tmpRow, iCol++, "Work Place Name");
		createCellHeader(tmpRow, iCol++, "Way Of Knowing");
		createCellHeader(tmpRow, iCol++, "Basic Salary");
		createCellHeader(tmpRow, iCol++, "Allowances");
		createCellHeader(tmpRow, iCol++, "Business Expenses");
		
		createCellHeader(tmpRow, iCol++, "Family Book");
		createCellHeader(tmpRow, iCol++, "Passport");
		createCellHeader(tmpRow, iCol++, "Resident Book");
		createCellHeader(tmpRow, iCol++, "Birth Certificate");
		createCellHeader(tmpRow, iCol++, "Village Certified Letter");
		createCellHeader(tmpRow, iCol++, "Voter Registration Card");
		createCellHeader(tmpRow, iCol++, "Driving Licence");
		createCellHeader(tmpRow, iCol++, "Utilities Bill");
		createCellHeader(tmpRow, iCol++, "Rent Contract");
		createCellHeader(tmpRow, iCol++, "Salary Slip");
		createCellHeader(tmpRow, iCol++, "Working Contract");
		createCellHeader(tmpRow, iCol++, "Bank Book");
		createCellHeader(tmpRow, iCol++, "Land Title");
		createCellHeader(tmpRow, iCol++, "Patent");
		createCellHeader(tmpRow, iCol++, "Other");
		createCellHeader(tmpRow, iCol++, "Ratio");
		
		
		createCellHeader(tmpRow, iCol++, "Family Name");
		createCellHeader(tmpRow, iCol++, "First Name");
		createCellHeader(tmpRow, iCol++, "Gender");
		createCellHeader(tmpRow, iCol++, "Age");
		createCellHeader(tmpRow, iCol++, "Marital Status");
		createCellHeader(tmpRow, iCol++, "District");
		createCellHeader(tmpRow, iCol++, "Province");
		createCellHeader(tmpRow, iCol++, "Relationship with applicant");
		createCellHeader(tmpRow, iCol++, "Employment Status");
		createCellHeader(tmpRow, iCol++, "Employment Industry");
		createCellHeader(tmpRow, iCol++, "Seniority Level");
		createCellHeader(tmpRow, iCol++, "Position");
		createCellHeader(tmpRow, iCol++, "Work Place Name");
		createCellHeader(tmpRow, iCol++, "Basic Salary");
		createCellHeader(tmpRow, iCol++, "Allowances");
		createCellHeader(tmpRow, iCol++, "Business Expenses");
		createCellHeader(tmpRow, iCol++, "Live with applicant");
		createCellHeader(tmpRow, iCol++, "Family Book");
		createCellHeader(tmpRow, iCol++, "Passport");
		createCellHeader(tmpRow, iCol++, "Resident Book");
		createCellHeader(tmpRow, iCol++, "Birth Certificate");
		createCellHeader(tmpRow, iCol++, "Village Certified Letter");
		createCellHeader(tmpRow, iCol++, "Voter Registration Card");
		createCellHeader(tmpRow, iCol++, "Driving Licence");
		createCellHeader(tmpRow, iCol++, "Utilities Bill");
		createCellHeader(tmpRow, iCol++, "Rent Contract");
		createCellHeader(tmpRow, iCol++, "Salary Slip");
		createCellHeader(tmpRow, iCol++, "Working Contract");
		createCellHeader(tmpRow, iCol++, "Bank Book");
		createCellHeader(tmpRow, iCol++, "Land Title");
		createCellHeader(tmpRow, iCol++, "Patent");
		createCellHeader(tmpRow, iCol++, "Other");
		createCellHeader(tmpRow, iCol++, "Asset Range");
		createCellHeader(tmpRow, iCol++, "Advance Payment");
		createCellHeader(tmpRow, iCol++, "%Advance Payment");
		createCellHeader(tmpRow, iCol++, "Term");
		createCellHeader(tmpRow, iCol++, "Insurance");
		createCellHeader(tmpRow, iCol++, "Closed");
		createCellHeader(tmpRow, iCol++, "Asset Price");
		createCellHeader(tmpRow, iCol++, "Lease Amount");
		createCellHeader(tmpRow, iCol++, "Base Salary");
		createCellHeader(tmpRow, iCol++, "Allowance");
		createCellHeader(tmpRow, iCol++, "Business Expense");
		createCellHeader(tmpRow, iCol++, "Net Income");
		createCellHeader(tmpRow, iCol++, "Personal Expense");
		createCellHeader(tmpRow, iCol++, "Family Expense");
		createCellHeader(tmpRow, iCol++, "Liability");
		createCellHeader(tmpRow, iCol++, "Disposable Income");
		createCellHeader(tmpRow, iCol++, "GLF Installment");
		int nbRows = quotations.size();
		FinService service = quotationService.getByField(FinService.class, CODE, "INSFEE");
		for (int i = 0; i < nbRows; i++) {
			System.out.println("=============== " + i + " / " + nbRows);
			Quotation quotation = quotations.get(i);
			iRow = generateRow(sheet, quotation, iRow, service);
		}
		return iRow; 
	}
	
	
	/**
	 * 
	 * @param sheet
	 * @param quotation
	 * @param iRow
	 * @param service
	 * @return
	 */
	private int generateRow(final Sheet sheet, Quotation quotation, int iRow, FinService service) {
		Applicant applicant = quotation.getApplicant();
		Individual individual = applicant.getIndividual();
		Address appAddress = individual.getMainAddress();
		Applicant guarantorApplicant = quotation.getGuarantor();
		Employment employment = individual.getCurrentEmployment();
		String statusContract = "N";
		if (quotation.getContract().getId() != null) {
			Contract contract = quotationService.getById(Contract.class, quotation.getContract().getId());
			if (contract.getWkfStatus().equals(ContractWkfStatus.CLO)
					|| contract.getWkfStatus().equals(ContractWkfStatus.EAR)) {
				statusContract = "Y";
			}
		}
		String assetRangeDesc = "";
		if(quotation.getAsset() != null){
			assetRangeDesc = quotation.getAsset().getDescEn();
		}
		String appfamilyBook = "";
		String appPassport = "";
		String appResidentBook = "";
		String appBirthCertificate = "";
		String appVillageCertifiedLetter = "";
		String appVoterRegistrationCard = "";
		String appDrivingLicence = "";
		String appUtilitiesBill = "";
		String appRentContract = "";
		String appSalarySlip = "";
		String appWorkingContract = "";
		String appBankBook = "";
		String appLandTitle = "";
		String appPatent = "";
		String appOther = "";
		
		List<QuotationDocument> documents = quotation.getQuotationDocuments(EApplicantType.C);
		if (documents != null && !documents.isEmpty()) {
			for (QuotationDocument quotationDocument : documents) {
				String documentCode = quotationDocument.getDocument().getCode();
				
				if (documentCode.equals("F")) {
					appfamilyBook = "Yes";
				}
				if (documentCode.equals("P")) {
					appPassport = "Yes";
				}
				if (documentCode.equals("R")) {
					appResidentBook = "Yes";
				}
				if (documentCode.equals("B")) {
					appBirthCertificate = "Yes";
				}
				if (documentCode.equals("G")) {
					appVillageCertifiedLetter = "Yes";
				}
				if (documentCode.equals("V")) {
					appVoterRegistrationCard = "Yes";
				}
				if (documentCode.equals("D")) {
					appDrivingLicence = "Yes";
				}
				if (documentCode.equals("BIL")) {
					appUtilitiesBill = "Yes";
				}
				if (documentCode.equals("REN")) {
					appRentContract = "Yes";
				}
				if (documentCode.equals("SAL")) {
					appSalarySlip = "Yes";
				}
				if (documentCode.equals("WOR")) {
					appWorkingContract = "Yes";
				}
				if (documentCode.equals("BAN")) {
					appBankBook = "Yes";
				}
				if (documentCode.equals("LAN")) {
					appLandTitle = "Yes";
				}
				if (documentCode.equals("PAT")) {
					appPatent = "Yes";
				}
				if (documentCode.equals("O")) {
					appOther = "Yes";
				}				
			}
		}
		String seniorityLevel = ""; 
		if (employment.getSeniorityLevel() != null) {
			seniorityLevel = employment.getSeniorityLevel().getDescEn();
		}
		
		int iCol = 0;
		Row tmpRow = sheet.createRow(iRow++);
		createNumericCell(tmpRow, iCol++, (applicant.getCreateDate() !=null ? applicant.getCreateDate() : "" ));
	
		createCell(tmpRow, iCol++, individual.getLastNameEn());
		createCell(tmpRow, iCol++, individual.getFirstNameEn());
		createCell(tmpRow, iCol++, getDefaultString(quotation.getReference()));
		createCell(tmpRow, iCol++, individual.getGender().getDesc());
		createNumericCell(tmpRow, iCol++, DateUtils.getAge(individual.getBirthDate()));
		// TODO YLY
		// createCell(tmpRow, iCol++, applicant.getMobilePhone());
		// createCell(tmpRow, iCol++, applicant.getMobilePhone2());
		// if (guarantor != null) {
		// 	createCell(tmpRow, iCol++, guarantor.getMobilePhone());
		// } else {
		// 	createCell(tmpRow, iCol++, "");
		// }
		createCell(tmpRow, iCol++, individual.getMaritalStatus().getDesc());
		createNumericCell(tmpRow, iCol++, individual.getNumberOfChildren());
		createCell(tmpRow, iCol++, appAddress.getProperty().getDesc());
		createCell(tmpRow, iCol++, appAddress.getDistrict().getDescEn());
		createCell(tmpRow, iCol++, appAddress.getProvince().getDescEn());
		createCell(tmpRow, iCol++, getDefaultString(employment.getEmploymentStatus().getDescEn()));
		createCell(tmpRow, iCol++, getDefaultString(employment.getEmploymentIndustry().getDescEn()));
		createCell(tmpRow, iCol++, getDefaultString(seniorityLevel));
		createCell(tmpRow, iCol++, getDefaultString(employment.getPosition()));
		createCell(tmpRow, iCol++, getDefaultString(employment.getEmployerName()));
		createCell(tmpRow, iCol++, getDefaultString(quotation.getWayOfKnowing().getDescEn()));
		createNumericCell(tmpRow, iCol++, employment.getRevenue());
		createNumericCell(tmpRow, iCol++, employment.getAllowance());
		createNumericCell(tmpRow, iCol++, employment.getBusinessExpense());
		
		createCell(tmpRow, iCol++, appfamilyBook);
		createCell(tmpRow, iCol++, appPassport);
		createCell(tmpRow, iCol++, appResidentBook);
		createCell(tmpRow, iCol++, appBirthCertificate);
		createCell(tmpRow, iCol++, appVillageCertifiedLetter);
		createCell(tmpRow, iCol++, appVoterRegistrationCard);
		createCell(tmpRow, iCol++, appDrivingLicence);
		createCell(tmpRow, iCol++, appUtilitiesBill);
		createCell(tmpRow, iCol++, appRentContract);
		createCell(tmpRow, iCol++, appSalarySlip);
		createCell(tmpRow, iCol++, appWorkingContract);
		createCell(tmpRow, iCol++, appBankBook);
		createCell(tmpRow, iCol++, appLandTitle);
		createCell(tmpRow, iCol++, appPatent);
		createCell(tmpRow, iCol++, appOther);
		createNumericCell(tmpRow, iCol++,MyNumberUtils.getDouble(getApplicantUWRatio(quotation)));
		//Guarantor
		if (guarantorApplicant != null) {
			Individual guarantor = guarantorApplicant.getIndividual();
			Address guaAddress = guarantor.getMainAddress();
			Employment guaEmployment = guarantor.getCurrentEmployment();
			String relationshipWithApp = "";
			if (guarantor.getQuotationApplicants() != null || !guarantor.getQuotationApplicants().isEmpty()) {
				QuotationApplicant quotationApplicant = guarantor.getQuotationApplicants().get(0);
				if (quotationApplicant.getRelationship() != null) {
					relationshipWithApp = quotationApplicant.getRelationship().getDescEn();
				}
			}
			String guaSeniorityLevel = ""; 
			if(guaEmployment.getSeniorityLevel() != null){
				guaSeniorityLevel = guaEmployment.getSeniorityLevel().getDescEn();
			}
			String liveWithApplicant = "No";
			if (guaEmployment.isSameApplicantAddress()) {
				liveWithApplicant = "Yes";
			}
			String guafamilyBook = "";
			String guaPassport = "";
			String guaResidentBook = "";
			String guaBirthCertificate = "";
			String guaVillageCertifiedLetter = "";
			String guaVoterRegistrationCard = "";
			String guaDrivingLicence = "";
			String guaUtilitiesBill = "";
			String guaRentContract = "";
			String guaSalarySlip = "";
			String guaWorkingContract = "";
			String guaBankBook = "";
			String guaLandTitle = "";
			String guaPatent = "";
			String guaOther = "";
			List<QuotationDocument> guaDocuments = quotation.getQuotationDocuments(EApplicantType.G);
			if (guaDocuments != null && !guaDocuments.isEmpty()) {
				for (QuotationDocument quotationDocument : guaDocuments) {
					String documentCode = quotationDocument.getDocument().getCode();
					if (documentCode.equals("F")) {
						guafamilyBook = "Yes";
					}
					if (documentCode.equals("P")) {
						guaPassport = "Yes";
					}
					if (documentCode.equals("R")) {
						guaResidentBook = "Yes";
					}
					if (documentCode.equals("B")) {
						guaBirthCertificate = "Yes";
					}
					if (documentCode.equals("G")) {
						guaVillageCertifiedLetter = "Yes";
					}
					if (documentCode.equals("V")) {
						guaVoterRegistrationCard = "Yes";
					}
					if (documentCode.equals("D")) {
						guaDrivingLicence = "Yes";
					}
					if (documentCode.equals("BIL")) {
						guaUtilitiesBill = "Yes";
					}
					if (documentCode.equals("REN")) {
						guaRentContract = "Yes";
					}
					if (documentCode.equals("SAL")) {
						guaSalarySlip = "Yes";
					}
					if (documentCode.equals("WOR")) {
						guaWorkingContract = "Yes";
					}
					if (documentCode.equals("BAN")) {
						guaBankBook = "Yes";
					}
					if (documentCode.equals("LAN")) {
						guaLandTitle = "Yes";
					}
					if (documentCode.equals("PAT")) {
						guaPatent = "Yes";
					}
					if (documentCode.equals("O")) {
						guaOther = "Yes";
					}
				}				
			}
			
			createCell(tmpRow, iCol++, guarantor.getLastNameEn());
			createCell(tmpRow, iCol++, guarantor.getFirstNameEn());
			createCell(tmpRow, iCol++, guarantor.getGender().getDesc());
			createNumericCell(tmpRow, iCol++, MyNumberUtils.getInteger(DateUtils.getAge(guarantor.getBirthDate())));
			createCell(tmpRow, iCol++, guarantor.getMaritalStatus().getDesc());
			createCell(tmpRow, iCol++, guaAddress.getDistrict().getDescEn());
			createCell(tmpRow, iCol++, guaAddress.getProvince().getDescEn());
			createCell(tmpRow, iCol++, relationshipWithApp);
			createCell(tmpRow, iCol++, getDefaultString(guaEmployment.getEmploymentStatus().getDescEn()));
			createCell(tmpRow, iCol++, getDefaultString(guaEmployment.getEmploymentIndustry().getDescEn()));
			createCell(tmpRow, iCol++, guaSeniorityLevel);
			createCell(tmpRow, iCol++, guaEmployment.getPosition());
			createCell(tmpRow, iCol++, guaEmployment.getEmployerName());
			createNumericCell(tmpRow, iCol++, guaEmployment.getRevenue());
			createNumericCell(tmpRow, iCol++, guaEmployment.getAllowance());
			createNumericCell(tmpRow, iCol++, guaEmployment.getBusinessExpense());
			createCell(tmpRow, iCol++, liveWithApplicant);
			
			createCell(tmpRow, iCol++, guafamilyBook);
			createCell(tmpRow, iCol++, guaPassport);
			createCell(tmpRow, iCol++, guaResidentBook);
			createCell(tmpRow, iCol++, guaBirthCertificate);
			createCell(tmpRow, iCol++, guaVillageCertifiedLetter);
			createCell(tmpRow, iCol++, guaVoterRegistrationCard);
			createCell(tmpRow, iCol++, guaDrivingLicence);
			createCell(tmpRow, iCol++, guaUtilitiesBill);
			createCell(tmpRow, iCol++, guaRentContract);
			createCell(tmpRow, iCol++, guaSalarySlip);
			createCell(tmpRow, iCol++, guaWorkingContract);
			createCell(tmpRow, iCol++, guaBankBook);
			createCell(tmpRow, iCol++, guaLandTitle);
			createCell(tmpRow, iCol++, guaPatent);
			createCell(tmpRow, iCol++, guaOther);
		} else {
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
		}
		//asset
		createCell(tmpRow, iCol++, assetRangeDesc);
		createNumericCell(tmpRow, iCol++, quotation.getTiAdvancePaymentAmount());
		createNumericCell(tmpRow, iCol++, quotation.getAdvancePaymentPercentage());
		createNumericCell(tmpRow, iCol++, MyNumberUtils.getInteger(quotation.getTerm()));
		double insuranceFee = 0d;
		QuotationService insService = quotation.getQuotationService(service.getId());
		if (service != null && insService != null) {
			insuranceFee = insService.getTiPrice();
		}
		
		double totalBaseSalary = 0d;
		double assetPrice = 0d;
		double leaseAmount = 0d;
		double totalAllowance = 0d;
		double totalBusinessExpense = 0d;
		double netIncome = 0d;
	
		List<Employment> employments = individual.getEmployments();
		for (Employment employment1 : employments) {
			totalBaseSalary += MyNumberUtils.getDouble(employment1.getRevenue());
			totalAllowance += MyNumberUtils.getDouble(employment1.getAllowance());
			totalBusinessExpense += MyNumberUtils.getDouble(employment1.getBusinessExpense());
		}
		/**
		 * Calculate the filed
		 * assetPrice,leaseAmount,baseSalary,Allowance,,netIncome,personalExpense,
		 * familyExpense,liability,disposableIncome,glfInstallment
		 */
		assetPrice = MyNumberUtils.getDouble(quotation.getAsset().getTiAssetPrice());
		leaseAmount = MyNumberUtils.getDouble(quotation.getTiFinanceAmount());
		netIncome = totalBaseSalary + totalAllowance - totalBusinessExpense;
		double personalExpense = MyNumberUtils.getDouble(individual.getMonthlyPersonalExpenses());
		double familyExpense = MyNumberUtils.getDouble(individual.getMonthlyFamilyExpenses());
		double liability = MyNumberUtils.getDouble(individual.getTotalDebtInstallment());
		double totalDebtInstallment = MyNumberUtils.getDouble(individual.getTotalDebtInstallment());
		double totalExpenses = MyNumberUtils.getDouble(individual.getMonthlyPersonalExpenses())
				+ MyNumberUtils.getDouble(individual.getMonthlyFamilyExpenses())
				+ totalDebtInstallment;
		double disposableIncome = netIncome - totalExpenses;
		double glfInstallment = MyNumberUtils.getDouble(quotation.getTiInstallmentAmount());
		
		createNumericCell(tmpRow, iCol++, insuranceFee);
		createNumericCell(tmpRow, iCol++, statusContract);
		
		
		createNumericCell(tmpRow, iCol++, assetPrice);
		createNumericCell(tmpRow, iCol++, leaseAmount);
		createNumericCell(tmpRow, iCol++, totalBaseSalary);
		createNumericCell(tmpRow, iCol++, totalAllowance);
		createNumericCell(tmpRow, iCol++, totalBusinessExpense);
		createNumericCell(tmpRow, iCol++, netIncome);
		createNumericCell(tmpRow, iCol++, personalExpense);
		createNumericCell(tmpRow, iCol++, familyExpense);
		createNumericCell(tmpRow, iCol++, liability);
		createNumericCell(tmpRow, iCol++, disposableIncome);
		createNumericCell(tmpRow, iCol++, glfInstallment);
		
		
		return iRow;
	}
	/**
	 * 
	 * @param quotation
	 * @return applicantUwRatio
	 */
	private double getApplicantUWRatio(Quotation quotation) {
		double applicantInstallment = quotation.getTiInstallmentAmount();
		double applicantUwNetIncome = MyNumberUtils.getDouble(quotation.getUwNetIncomeEstimation());
		double applicantUwPersonalExpenses = MyNumberUtils.getDouble(quotation.getUwPersonalExpensesEstimation());
		double applicantUwFamilyExpenses = MyNumberUtils.getDouble(quotation.getUwFamilyExpensesEstimation());
		double applicantUwDebtInstallment = MyNumberUtils.getDouble(quotation.getUwLiabilityEstimation());
		double applicantUwDisposableIncome = applicantUwNetIncome - applicantUwPersonalExpenses - applicantUwFamilyExpenses - applicantUwDebtInstallment;
		double applicantUwRatio = applicantUwDisposableIncome / applicantInstallment;
		return applicantUwRatio;
	}
	
	/**
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
	 * @param wrapText
	 * @return
	 */
	protected Cell createCellHeader(final Row row, final int iCol,final String value) {
		final Cell cell = row.createCell(iCol);	
		cell.setCellValue((value == null ? "" : value));
		cell.setCellStyle(styles.get(HEADER));
		return cell;
	}
	
	/**
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
	 * @param wrapText
	 * @return
	 */
	protected Cell createCell(final Row row, final int iCol,final String value) {
		final Cell cell = row.createCell(iCol);	
		cell.setCellValue((value == null ? "" : value));
		cell.setCellStyle(styles.get(BODY));
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
	private Cell createNumericCell(final Row row, final int iCol,
			final Object value) {
		final Cell cell = row.createCell(iCol);
		if (value == null) {
			cell.setCellValue("");
		} else if (value instanceof Integer) {
			cell.setCellValue(Integer.parseInt(value.toString()));
			cell.setCellStyle(styles.get("INTEGER"));
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
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
            cell.setCellStyle(styles.get(DATE_FORMAT));
        } else {
        	//cell.setCellStyle(styles.get(BODY));	
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
		Font itemFontBold = wb.createFont();
		itemFontBold.setFontHeightInPoints((short) 12);
		itemFontBold.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(itemFontBold);
		styles.put(HEADER, style);
				
		Font itemFont = wb.createFont();
		itemFont.setFontHeightInPoints((short) 12);
		itemFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		style = wb.createCellStyle();
		style.setFont(itemFont);
		styles.put(BODY, style);
		
		DataFormat format = wb.createDataFormat();
    	style = wb.createCellStyle();
    	style.setAlignment(CellStyle.ALIGN_RIGHT);
    	style.setDataFormat(format.getFormat("#,##0.00"));
    	style.setFont(itemFont);
    	styles.put(AMOUNT, style);
    	
    	format = wb.createDataFormat();
    	style = wb.createCellStyle();
       	style.setDataFormat(format.getFormat("0"));
       	style.setFont(itemFont);
    	styles.put("INTEGER", style);
    	
    	style = wb.createCellStyle();
    	style.setDataFormat(format.getFormat(DEFAULT_DATE_FORMAT));
    	style.setAlignment(CellStyle.ALIGN_LEFT);
    	style.setFont(itemFont);
    	styles.put(DATE_FORMAT, style);

		return styles;
	}
	
	public String getDateLabel(final Date date) {
		return getDateLabel(date, DEFAULT_DATE_FORMAT);
	}
	
	/**
	 * 
	 */
	protected CellStyle getCellStyle(final String styleName) {
		return styles.get(styleName);
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private String getDefaultString(String value) {
		String strValue = "";
		if (StringUtils.isNotEmpty(value)) {
			strValue = value;
		}
		return strValue; 
	}
}
