package com.nokor.efinance.gui.report.xls;

import java.util.Collections;
import java.util.Comparator;
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
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyMathUtils;
import org.seuksa.frmk.tools.MyNumberUtils;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.model.Employment;
import com.nokor.efinance.core.financial.service.FinanceCalculationService;
import com.nokor.efinance.core.quotation.QuotationService;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationDocument;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.efinance.tools.report.Report;
import com.nokor.efinance.tools.report.XLSAbstractReportExtractor;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.finance.services.shared.AmortizationSchedules;
import com.nokor.finance.services.shared.CalculationParameter;
import com.nokor.finance.services.shared.Schedule;
import com.nokor.finance.services.tools.LoanUtils;
import com.nokor.frmk.security.context.SecApplicationContextHolder;

/**
 * @author sok.vina
 */
public class CBCReportExtraction extends XLSAbstractReportExtractor implements Report, GLFApplicantFields, QuotationEntityField {

	private QuotationService quotationService = (QuotationService) SecApplicationContextHolder.getContext().getBean("quotationService");
	private FinanceCalculationService financeCalculationService = (FinanceCalculationService) SecApplicationContextHolder.getContext().getBean("financeCalculationService");
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

	public CBCReportExtraction() {

	}

	@Override
	public String generate(ReportParameter reportParameter) throws Exception {
		
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<Quotation>(Quotation.class);
		restrictions.addCriterion("quotationStatus", QuotationWkfStatus.ACT);
		//restrictions.addOrder(Order.asc("contractStartDate"));
		Long[] quotations = quotationService.getIds(restrictions);

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

//		final Row headerRow = sheet.createRow(2);
//		createCell(headerRow, 6, APPLICANT_TITTLE, 14, true, false, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK);

		int iRow = 0;

		iRow = dataTable(sheet, iRow, style, quotations);

		Row tmpRowEnd = sheet.createRow(iRow++ + 1);
		tmpRowEnd.setRowStyle(style);

		iRow = iRow + 2;

		String fileName = writeXLSData("CBC_Report_Extraction_" + DateUtils.getDateLabel(DateUtils.today(), "yyyyMMddHHmmssSSS") + ".xlsx");

		return fileName;
	}

	private int dataTable(final Sheet sheet, int iRow, final CellStyle style, Long[] quotations) throws Exception {
		/* Create total data header */

		int iCol = 0;
		Row tmpRow = sheet.createRow(iRow++);
		createCell(tmpRow, iCol++, "CustomerID", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "IDCode", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "IDNumber", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "IDExpiryDate", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "IDCode2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "IDNumber2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "IDExpiryDate2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "IDCode3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "IDNumber3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "IDExpiryDate3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "DateOfBirth", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "FamilyNameEng", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "FirstNameEng", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "SecondNameEng", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "ThirdNameEng", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "UnformattedNameEng", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "MotherNameEng", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "FamilyNameKhm", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "FirstNameKhm", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "SecondNameKhm", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "ThirdNameKhm", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "UnformattedNameKh", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "MotherNameKhm", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "GenderCode", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "MaritalStatusCode", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "NationalityCode", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "TaxpayerRegistrationNumber", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "ApplicantTypeCode", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "AddressTypeCode", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "Province", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "District", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "Commune", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "Village", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "AddressField1Eng", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "AddressField2Eng", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "AddressField1Khm", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "AddressField2Khm", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "CityEng", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "CityKhm", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "CountryCode", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "PostalCode", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "AddressTypeCode2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "Province2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "District2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "Commune2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "Village2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "AddressField1Eng2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "AddressField2Eng2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "AddressField1Khm2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "AddressField2Khm2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "CityEng2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "CityKhm2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "CountryCode2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "PostalCode2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "AddressTypeCode3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		createCell(tmpRow, iCol++, "Province3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "District3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "Commune3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "Village3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "AddressField1Eng3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "AddressField2Eng3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "AddressField1Khm3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "AddressField2Khm3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "CityEng3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "CityKhm3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "CountryCode3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "PostalCode3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EmailAddress", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "ContactNumberTypeCode", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "ContactNumberCountryCode", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "ContactNumberArea", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "ContactNumberNumber", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "ContactNumberExtension", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "ContactNumberTypeCode2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "ContactNumberCountryCode2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "ContactNumberArea2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "ContactNumberNumber2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "ContactNumberExtension2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "ContactNumberTypeCode3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "ContactNumberCountryCode3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "ContactNumberArea3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "ContactNumberNumber3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "ContactNumberExtension3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EmployerTypeCode", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "SelfEmployed", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EmployerNameEng", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EmployerNameKhm", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EconomicSector", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "BusinessType", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EmployerAddressEng", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EmployerAddressKhm", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EmployerProvince", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EmployerDistrict", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EmployerCommune", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EmployerVillage", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EmployerAddressCityEng", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EmployerAddressCityKhm", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EmployerAddressCountryCode", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EmployerAddressPostalCode", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "OccupationEng", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "OccupationKhm", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "DateOfEmployment", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "LengthOfService", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "ContractExpiryDate", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "IncomeCurrency", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "MonthlyBasicIncome", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "MonthlyTotalIncome", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EmployerTypeCode2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "SelfEmployed2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EmployerNameEng2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EmployerNameKhm2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EconomicSector2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "BusinessType2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EmployerAddressEng2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EmployerAddressKhm2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EmployerProvince2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EmployerDistrict2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EmployerCommune2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EmployerVillage2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EmployerAddressCityEng2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EmployerAddressCityKhm2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EmployerAddressCountryCode2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EmployerAddressPostalCode2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "OccupationEng2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "OccupationKhm2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "DateOfEmployment2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "LengthOfService2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "ContractExpiryDate2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "IncomeCurrency2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "MonthlyBasicIncome2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "MonthlyTotalIncome2", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EmployerTypeCode3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "SelfEmployed3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EmployerNameEng3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EmployerNameKhm3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EconomicSector3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "BusinessType3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EmployerAddressEng3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EmployerAddressKhm3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EmployerProvince3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EmployerDistrict3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EmployerCommune3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EmployerVillage3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EmployerAddressCityEng3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EmployerAddressCityKhm3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EmployerAddressCountryCode3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "EmployerAddressPostalCode3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "OccupationEng3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "OccupationKhm3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "DateOfEmployment3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "LengthOfService3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "ContractExpiryDate3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "IncomeCurrency3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "MonthlyBasicIncome3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "MonthlyTotalIncome3", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "CreditorID", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "AccountTypeCode", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "GroupAccountReference", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "AccountNumber", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "DateIssued", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "ProductTypeCode", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "Currency", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "LoanAmount", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "ProductExpiryDate", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "ProductStatusCode", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "RestructuredLoan", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "InstalmentAmount", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "PaymentFrequencyCode", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "Tenure", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "LastPaymentDate", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "LastAmountPaid", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "SecurityTypeCode", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "OutstandingBalance", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "PastDue", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "NextPaymentDate", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "PaymentStatusCode", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "AsOfDate", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "LossStatusCode", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "LossStatusDate", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "OriginalAmountAsAtLoadDate", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "OutstandingBalanceAsAtLoadDate", 14, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);	
		int nbRows = quotations.length;
		int customerId = 0;
		for (int i = 0; i < nbRows; i++) {
			customerId++;
			Quotation quotation = quotationService.getById(Quotation.class, quotations[i]);
			Applicant applicant = quotation.getApplicant();
			Applicant guarantor = quotation.getGuarantor();

			CalculationParameter calculationParameter = new CalculationParameter();
			calculationParameter.setInitialPrincipal(quotation.getTiFinanceAmount());
			calculationParameter.setNumberOfPeriods(LoanUtils.getNumberOfPeriods(quotation.getTerm(), quotation.getFrequency()));
			calculationParameter.setPeriodicInterestRate(quotation.getInterestRate() / 100d);
			calculationParameter.setFrequency(quotation.getFrequency());
			
			Date firstInstallmentDate = quotation.getFirstDueDate();
			Date contractStartDate = quotation.getContractStartDate();
			AmortizationSchedules amortizationSchedules = financeCalculationService.getAmortizationSchedules(contractStartDate, firstInstallmentDate, calculationParameter);
			List<Schedule> schedules = amortizationSchedules.getSchedules();
			
			List<QuotationDocument> documents = quotation.getQuotationDocuments(EApplicantType.C);			
			List<Employment> employments = applicant.getIndividual().getEmployments();
			Date dateIssued = quotation.getContractStartDate();
			
			String documentCode = "";
			String reference = "";
			Date expireDate = null;
			Date lastPaymentDate = null;
			Date nextPaymentDate = null;
			double applicantRevenus = 0d;
			double applicantAllowance = 0d;
			double applicantBusinessExpenses = 0d;
			double lastAmountPaid = 0d;
			double outstandingBalance = 0d;
			String productStatusCode = "N";
			String paymentStatusCode = "0";
			Date productExpire = schedules.get(schedules.size()-1).getInstallmentDate();
			for (int j = 0; j < schedules.size(); j++) {
				if(DateUtils.getMonth(DateUtils.today()) == DateUtils.getMonth(schedules.get(j).getInstallmentDate()) && 
				   DateUtils.getYear(DateUtils.today()) == DateUtils.getYear(schedules.get(j).getInstallmentDate())) {
					// dateA.after(DateB) = dateA > dateB
					if (DateUtils.today().after(schedules.get(j).getInstallmentDate())) {
						lastPaymentDate = schedules.get(j).getInstallmentDate();
						lastAmountPaid = schedules.get(j).getInstallmentPayment();
						if (j != schedules.size() - 1) {
							nextPaymentDate = schedules.get(j+1).getInstallmentDate();
						}	
					} else {
						if (j > 0) {
							lastPaymentDate = schedules.get(j-1).getInstallmentDate();
							lastAmountPaid = schedules.get(j-1).getInstallmentPayment();
						}
						nextPaymentDate = schedules.get(j).getInstallmentDate();
					}
				}
				if (schedules.get(j).getInstallmentDate().after(DateUtils.today())) {
					outstandingBalance += MyNumberUtils.getDouble(schedules.get(j).getPrincipalAmount());
				}
			}
			
			if (MyMathUtils.roundAmountTo(outstandingBalance) == 0d) {
				productStatusCode = "C";
				paymentStatusCode = "C";
			}
			
			if (lastPaymentDate == null && MyMathUtils.roundAmountTo(outstandingBalance) > 0) {
				paymentStatusCode = "N";
			}
			
			if (nextPaymentDate == null && schedules.get(0).getInstallmentDate().after(DateUtils.today())) {
				nextPaymentDate = schedules.get(0).getInstallmentDate();
			}
			
			if (documents != null && !documents.isEmpty()) {
				Collections.sort(documents, new DocumentComparatorBySortIndex());
				QuotationDocument quotationDocument = documents.get(0);
				documentCode = quotationDocument.getDocument().getCode();
				reference = quotationDocument.getReference();
				expireDate = quotationDocument.getExpireDate();
			}
			for (Employment employmentAmount : employments) {
				applicantRevenus += MyNumberUtils.getDouble(employmentAmount.getRevenue()) ;
				applicantAllowance += MyNumberUtils.getDouble(employmentAmount.getAllowance()) ;
				applicantBusinessExpenses += MyNumberUtils.getDouble(employmentAmount.getBusinessExpense());
			}
			Double monthlyBasicIncom = applicantRevenus + applicantAllowance 
					- applicantBusinessExpenses
					- MyNumberUtils.getDouble(applicant.getIndividual().getMonthlyFamilyExpenses())
					- MyNumberUtils.getDouble(applicant.getIndividual().getMonthlyPersonalExpenses());
			iCol = 0;
			iRow = generateRow(sheet, applicant, quotation, documentCode, reference,
					expireDate, "P", monthlyBasicIncom,
					productExpire, dateIssued, productStatusCode,
					lastPaymentDate, lastAmountPaid, outstandingBalance,
					nextPaymentDate, paymentStatusCode, iRow, customerId);
			
			if (guarantor != null) {
				customerId = customerId +1;
				iRow = generateRow(sheet, guarantor, quotation, documentCode,
						reference, expireDate, "G",
						monthlyBasicIncom, productExpire, dateIssued,
						productStatusCode, lastPaymentDate, lastAmountPaid,
						outstandingBalance, nextPaymentDate, paymentStatusCode, iRow, customerId);
			}
				
		}
		return iRow; 
	}
	
	
	private int generateRow(final Sheet sheet, Applicant applicant, Quotation quotation, String documentCode,
			String reference, Date expireDate, String applicantTypeCode, Double monthlyBasicIncom, 
			Date productExpire, Date dateIssued, String productStatusCode, Date lastPaymentDate, double lastAmountPaid,
			double outstandingBalance, Date nextPaymentDate, String paymentStatusCode, int iRow, int customerId) {
		
		Employment employment = applicant.getIndividual().getCurrentEmployment();
		Address address = applicant.getIndividual().getMainAddress();
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
		createNumericCell(tmpRow, iCol++, (customerId), true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, documentCode, true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, reference, true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, (expireDate != null ? DateUtils.formatDate(expireDate,"ddMMyyyy") : ""), true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, DateUtils.formatDate(applicant.getIndividual().getBirthDate(),"ddMMyyyy"), true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, applicant.getIndividual().getLastNameEn(), true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, applicant.getIndividual().getFirstNameEn(), true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, applicant.getIndividual().getLastName(), true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, applicant.getIndividual().getFirstName(), true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, applicant.getIndividual().getGender().toString(), true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, applicant.getIndividual().getMaritalStatus().toString(), true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, applicant.getIndividual().getNationality().getCode(), true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, applicantTypeCode, true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++,"RESID", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "#"+houseNo+", "+ str+", "+ address.getVillage().getDescEn()+", " +address.getCommune().getDescEn()+", " + address.getDistrict().getDescEn()+
				", "+ address.getProvince().getDescEn(), true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, address.getProvince().getDescEn(), true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "KHM", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);

		createNumericCell(tmpRow, iCol++, "M", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "855", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		// TODO YLY
		/*createNumericCell(tmpRow, iCol++, applicant.getMobilePhone(), true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);*/
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, employment.getPosition(), true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "USD", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, monthlyBasicIncom, true,
				CellStyle.ALIGN_RIGHT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, monthlyBasicIncom, true,
				CellStyle.ALIGN_RIGHT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "330", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "S", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, accountNumber, true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		
		createNumericCell(tmpRow, iCol++, (dateIssued != null ? DateUtils.formatDate(dateIssued,"ddMMyyyy") : ""), true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "MIS", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "USD", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, MyNumberUtils.getDouble(quotation.getTiFinanceAmount()), true,
				CellStyle.ALIGN_RIGHT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, DateUtils.formatDate(productExpire,"ddMMyyyy"), true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, productStatusCode, true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "N", true,
				CellStyle.ALIGN_CENTER, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, quotation.getTiInstallmentAmount(), true,
				CellStyle.ALIGN_RIGHT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "M", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, quotation.getTerm(), true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, (lastPaymentDate != null ? DateUtils.formatDate(lastPaymentDate,"ddMMyyyy") : ""), true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, lastAmountPaid, true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "MV", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, outstandingBalance, true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, 0, true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, (nextPaymentDate != null ? DateUtils.formatDate(nextPaymentDate,"ddMMyyyy") : ""), true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, paymentStatusCode, true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, DateUtils.formatDate(DateUtils.today(),"ddMMyyyy"), true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		createNumericCell(tmpRow, iCol++, "", true,
				CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		return iRow;
	}

	
	protected Cell createCell(final Row row, final int iCol,
			final String value, final int fontsize, final boolean isBold,
			final boolean hasBorder, final short alignment,
			final boolean setBgColor, final short bgColor, final short fonCorlor) {
		final Cell cell = row.createCell(iCol);
		
		cell.setCellValue((value == null ? "" : value));
		cell.setCellStyle(styles.get(BODY));
		return cell;
	}

	private Cell createNumericCell(final Row row, final int iCol,
			final Object value, final boolean hasBorder, final short alignment,
			final int fontsize, final boolean isBold, final boolean setBgColor,
			final short bgColor, final short fontColor) {

		final Cell cell = row.createCell(iCol);
		
		CellStyle style;
		DataFormat format = wb.createDataFormat();
		style = wb.createCellStyle();
		style.setDataFormat(format.getFormat("0"));
		style.setAlignment(CellStyle.ALIGN_CENTER);
		
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
	public String getDateLabel(final Date date) {
		return getDateLabel(date, DEFAULT_DATE_FORMAT);
	}
	protected CellStyle getCellStyle(final String styleName) {
		return styles.get(styleName);
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
}
