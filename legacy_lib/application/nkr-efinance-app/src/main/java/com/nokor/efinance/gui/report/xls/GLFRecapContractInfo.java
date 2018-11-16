package com.nokor.efinance.gui.report.xls;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
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
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.IndividualAddress;
import com.nokor.efinance.core.applicant.model.IndividualContactInfo;
import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractSimulation;
import com.nokor.efinance.core.contract.service.ContractRestriction;
import com.nokor.efinance.core.contract.service.ContractUtils;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.efinance.tools.report.Report;
import com.nokor.efinance.tools.report.XLSAbstractReportExtractor;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;
import com.nokor.ersys.core.hr.model.eref.ETypeContactInfo;
import com.nokor.ersys.core.hr.model.organization.ContactInfo;

/**
 * @author uhout.cheng
 */
public class GLFRecapContractInfo extends XLSAbstractReportExtractor implements Report, FinServicesHelper, GLFRecapContractInfoFields {

	private Map<String, CellStyle> styles = null;
	
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

	public GLFRecapContractInfo() {

	}

	/**
	 * @see com.nokor.efinance.tools.report.Report#generate(com.nokor.efinance.core.shared.report.ReportParameter)
	 */
	@Override
	public String generate(ReportParameter reportParameter) throws Exception {

		ContractRestriction restrictions = new ContractRestriction();
		
		Map<String, Object> parameters = reportParameter.getParameters();
		@SuppressWarnings("unchecked")
		List<Long> conIds = (List<Long>) parameters.get(Contract.ID);
		restrictions.addCriterion(Restrictions.in(Contract.ID, conIds));

		List<Contract> contracts = CONT_SRV.list(restrictions);
		
		createWorkbook(null);
		
		if (contracts != null && !contracts.isEmpty()) {
			for (Contract contract : contracts) {
				XSSFSheet sheet = getSheet(contract.getReference());
				CellStyle style = wb.createCellStyle();
				style.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
				style.setFillPattern(HSSFCellStyle.BIG_SPOTS);
				style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
				style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				
				contractInfoTable(sheet, 0, style, contract);
			}
		}

		String fileName = writeXLSData("RecapContractInfo_" + DateUtils.getDateLabel(DateUtils.today(), "yyyyMMddHHmmssSSS") + ".xlsx");

		return fileName;
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	private XSSFSheet getSheet(String name) {
		XSSFSheet sheet = wb.createSheet(name);
		/*sheet.lockDeleteColumns();
		sheet.lockDeleteRows();
		sheet.lockFormatCells();
		sheet.lockFormatColumns();
		sheet.lockFormatRows();
		sheet.lockInsertColumns();
		sheet.lockInsertRows();*/
		sheet.setDisplayGridlines(false);
	
		styles = new HashMap<String, CellStyle>();
		createStyles();
		sheet.setColumnWidth(0, 5600);
		sheet.setColumnWidth(1, 11000);
		sheet.setColumnWidth(2, 5900);
		sheet.setColumnWidth(3, 8000);
		sheet.setColumnWidth(4, 6200);
		sheet.setZoom(7, 10);
		final PrintSetup printSetup = sheet.getPrintSetup();

		printSetup.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);

		printSetup.setScale((short) 60);
		sheet.setAutobreaks(true);
		sheet.setFitToPage(true);
		printSetup.setFitWidth((short) 1);
		printSetup.setFitHeight((short) 0);

		// Setup the Page margins - Left, Right, Top and Bottom
		sheet.setMargin(Sheet.LeftMargin, 0.25);
		sheet.setMargin(Sheet.RightMargin, 0.25);
		sheet.setMargin(Sheet.TopMargin, 0.75);
		sheet.setMargin(Sheet.BottomMargin, 0.75);
		sheet.setMargin(Sheet.HeaderMargin, 0.25);
		sheet.setMargin(Sheet.FooterMargin, 0.25);
		return sheet;
	}
	
	/**
	 * 
	 * @param sheet
	 * @param iRow
	 * @param style
	 * @param contra
	 * @throws Exception
	 */
	private void contractInfoTable(final Sheet sheet, int iRow, final CellStyle style, Contract contra) throws Exception {
		Asset asset = contra.getAsset();
		String serie = StringUtils.EMPTY;
		if (asset != null) {
			AssetModel assModel = asset.getModel();
			if (assModel != null) {
				serie = assModel.getSerie();
			}
		}

		String dealer = StringUtils.EMPTY;
		if (contra.getDealer() != null) {
			dealer = contra.getDealer().getNameLocale();
		}
		
		String applicationId = ContractUtils.getApplicationID(contra);
		String gender = StringUtils.EMPTY;
		String customerId = StringUtils.EMPTY;
		String appType = StringUtils.EMPTY;
		String firstName = StringUtils.EMPTY;
		String lastName = StringUtils.EMPTY;
		Applicant applicant = null;
		if (ContractUtils.isPendingTransfer(contra)) {
			ContractSimulation simulation = ContractUtils.getLastContractSimulation(contra.getId());
			if (simulation != null) {
				applicant = simulation.getApplicant();
			}
		} else {
			applicant = contra.getApplicant();
		}
		Individual individual = null;
		if (applicant != null) {
			individual = applicant.getIndividual();
		}
		if (individual != null) {
			appType = EApplicantType.C.getDescLocale();
			gender = individual.getGender() == null ? StringUtils.EMPTY : individual.getGender().getDescLocale();
			customerId = individual.getReference();
			firstName = individual.getFirstNameLocale();
			lastName = individual.getLastNameLocale();
		}

		// Contract Part
		Row tmpRow = sheet.createRow(iRow++);
		int iCol = 0;
		createCell(tmpRow, iCol++, I18N.message("contract"), 12, true, true, false, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, false, true);
		createCell(tmpRow, iCol++, StringUtils.EMPTY, 12, true, false, false, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, false, true);
		createCell(tmpRow, iCol++, StringUtils.EMPTY, 12, true, false, false, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, false, true);
		createCell(tmpRow, iCol++, StringUtils.EMPTY, 12, true, false, false, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, false, true);
		createCell(tmpRow, iCol++, StringUtils.EMPTY, 12, true, false, true, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, false, true);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, 0, 4));
		
		tmpRow = sheet.createRow(iRow++);
		iCol = 0;
		createCell(tmpRow, iCol++, CONTRACTID, 12, true, true, true, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, true, true);
		createCell(tmpRow, iCol++, APPLICATIONID, 12, true, true, true, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, true, true);
		createCell(tmpRow, iCol++, CONTRACTSTATUS, 12, true, true, true, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, true, true);
		createCell(tmpRow, iCol++, DEALER, 12, true, true, true, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, true, true);
		createCell(tmpRow, iCol++, SERIE, 12, true, true, true, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, true, true);
		
		tmpRow = sheet.createRow(iRow++);
		iCol = 0;
		createCell(tmpRow, iCol++, contra.getReference(), 12, true, true, true, true, CellStyle.ALIGN_LEFT, false, BG_GREY, FC_BLACK, true, false);
		createCell(tmpRow, iCol++, applicationId, 12, true, true, true, true, CellStyle.ALIGN_LEFT, false, BG_GREY, FC_BLACK, true, false);
		createCell(tmpRow, iCol++, contra.getWkfStatus().getDescLocale(), 12, true, true, true, true, CellStyle.ALIGN_LEFT, false, BG_GREY, FC_BLACK, true, false);
		createCell(tmpRow, iCol++, dealer, 12, true, true, true, true, CellStyle.ALIGN_LEFT, false, BG_WHITE, FC_BLACK, true, false);
		createCell(tmpRow, iCol++, serie, 12, true, true, true, true, CellStyle.ALIGN_LEFT, false, BG_WHITE, FC_BLACK, true, false);
		
		// Customer Part
		iRow = iRow + 1;
		tmpRow = sheet.createRow(iRow++);
		iCol = 0;
		createCell(tmpRow, iCol++, I18N.message("customer"), 12, true, true, false, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, false, true);
		createCell(tmpRow, iCol++, StringUtils.EMPTY, 12, true, false, false, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, false, true);
		createCell(tmpRow, iCol++, StringUtils.EMPTY, 12, true, false, false, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, false, true);
		createCell(tmpRow, iCol++, StringUtils.EMPTY, 12, true, false, false, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, false, true);
		createCell(tmpRow, iCol++, StringUtils.EMPTY, 12, true, false, true, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, false, true);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, 0, 4));
		
		tmpRow = sheet.createRow(iRow++);
		iCol = 0;
		createCell(tmpRow, iCol++, CUSTOMERID, 12, true, true, true, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, true, true);
		createCell(tmpRow, iCol++, TYPE, 12, true, true, true, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, true, true);
		createCell(tmpRow, iCol++, FIRSTNAME, 12, true, true, true, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, true, true);
		createCell(tmpRow, iCol++, LASTNAME, 12, true, true, true, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, true, true);
		createCell(tmpRow, iCol++, GENDER, 12, true, true, true, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, true, true);
		tmpRow = sheet.createRow(iRow++);
		iCol = 0;
		createCell(tmpRow, iCol++, customerId, 12, true, true, true, true, CellStyle.ALIGN_LEFT, false, BG_GREY, FC_BLACK, true, false);
		createCell(tmpRow, iCol++, appType, 12, true, true, true, true, CellStyle.ALIGN_LEFT, false, BG_GREY, FC_BLACK, true, false);
		createCell(tmpRow, iCol++, firstName, 12, true, true, true, true, CellStyle.ALIGN_LEFT, false, BG_GREY, FC_BLACK, true, false);
		createCell(tmpRow, iCol++, lastName, 12, true, true, true, true, CellStyle.ALIGN_LEFT, false, BG_WHITE, FC_BLACK, true, false);
		createCell(tmpRow, iCol++, gender, 12, true, true, true, true, CellStyle.ALIGN_LEFT, false, BG_WHITE, FC_BLACK, true, false);
		
		// Guarantor Part
		/*iRow = iRow + 1;
		tmpRow = sheet.createRow(iRow++);
		iCol = 0;
		createCell(tmpRow, iCol++, I18N.message("guarantor"), 12, true, true, false, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, false, true);
		createCell(tmpRow, iCol++, StringUtils.EMPTY, 12, true, false, false, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, false, true);
		createCell(tmpRow, iCol++, StringUtils.EMPTY, 12, true, false, false, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, false, true);
		createCell(tmpRow, iCol++, StringUtils.EMPTY, 12, true, false, false, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, false, true);
		createCell(tmpRow, iCol++, StringUtils.EMPTY, 12, true, false, true, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, false, true);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, 0, 4));
		
		tmpRow = sheet.createRow(iRow++);
		iCol = 0;
		createCell(tmpRow, iCol++, CUSTOMERID, 12, true, true, true, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, true, true);
		createCell(tmpRow, iCol++, TYPE, 12, true, true, true, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, true, true);
		createCell(tmpRow, iCol++, FIRSTNAME, 12, true, true, true, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, true, true);
		createCell(tmpRow, iCol++, LASTNAME, 12, true, true, true, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, true, true);
		createCell(tmpRow, iCol++, GENDER, 12, true, true, true, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, true, true);
		
		List<ContractApplicant> contractApplicants = contra.getContractApplicants();
		if (contractApplicants != null && !contractApplicants.isEmpty()) {
			for (int j = 0; j < contractApplicants.size(); j++) {
				if (EApplicantType.G.equals(contractApplicants.get(j).getApplicantType())) {
					Applicant guarantor = contractApplicants.get(j).getApplicant();
					if (guarantor != null) {
						Individual guaInd = guarantor.getIndividual();
						String guaCusId = StringUtils.EMPTY;
						String guaType = StringUtils.EMPTY;
						String guaFirstName = StringUtils.EMPTY;
						String guaLastName = StringUtils.EMPTY;
						String guaGender = StringUtils.EMPTY;
						if (guaInd != null) {
							guaCusId = guaInd.getReference();
							guaType = EApplicantType.G.getDescLocale();
							guaFirstName = guaInd.getFirstNameLocale();
							guaLastName = guaInd.getLastNameLocale();
							guaGender = guaInd.getGender() == null ? StringUtils.EMPTY : guaInd.getGender().getDescLocale();
						}
						tmpRow = sheet.createRow(iRow++);
						iCol = 0;
						createCell(tmpRow, iCol++, guaCusId, 12, true, true, true, true, CellStyle.ALIGN_LEFT, false, BG_GREY, FC_BLACK, true, false);
						createCell(tmpRow, iCol++, guaType, 12, true, true, true, true, CellStyle.ALIGN_LEFT, false, BG_GREY, FC_BLACK, true, false);
						createCell(tmpRow, iCol++, guaFirstName, 12, true, true, true, true, CellStyle.ALIGN_LEFT, false, BG_GREY, FC_BLACK, true, false);
						createCell(tmpRow, iCol++, guaLastName, 12, true, true, true, true, CellStyle.ALIGN_LEFT, false, BG_WHITE, FC_BLACK, true, false);
						createCell(tmpRow, iCol++, guaGender, 12, true, true, true, true, CellStyle.ALIGN_LEFT, false, BG_WHITE, FC_BLACK, true, false);
						iRow = iRow + 1;
					}
				}
			}
		}*/
		
		// Contact Part
		iRow = iRow + 1;
		iRow = getContactTable(sheet, tmpRow, iRow, iCol, style, individual);
		
		// Address Part
		iRow = getAddressTable(sheet, tmpRow, iRow, iCol, style, individual);
		
		iRow = iRow + 1;
	}
	
	/**
	 * 
	 * @param sheet
	 * @param tmpRow
	 * @param iRow
	 * @param iCol
	 * @param style
	 * @param individual
	 * @return
	 */
	private int getContactTable(final Sheet sheet, Row tmpRow, int iRow, int iCol, final CellStyle style, Individual individual) {
		List<IndividualContactInfo> indContactInfos = individual.getIndividualContactInfos();
		Map<ETypeContactInfo, List<ContactInfo>> mapContactTypes = new HashMap<ETypeContactInfo, List<ContactInfo>>();
		List<ContactInfo> contactInfos = null;
		if (indContactInfos != null && !indContactInfos.isEmpty()) {
			for (IndividualContactInfo individualContactInfo : indContactInfos) {
				ContactInfo conInfo = individualContactInfo.getContactInfo();
				if (!mapContactTypes.containsKey(conInfo.getTypeInfo())) {
					contactInfos = new ArrayList<ContactInfo>();
					contactInfos.add(conInfo);
					mapContactTypes.put(conInfo.getTypeInfo(), contactInfos);
				} else {
					mapContactTypes.get(conInfo.getTypeInfo()).add(conInfo);
				}
			}
		}
		
		tmpRow = sheet.createRow(iRow++);
		iCol = 0;
		createCell(tmpRow, iCol++, I18N.message("contacts"), 12, true, true, true, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, false, true);
		createCell(tmpRow, iCol++, StringUtils.EMPTY, 12, true, false, false, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, false, true);
		createCell(tmpRow, iCol++, StringUtils.EMPTY, 12, true, false, false, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, false, true);
		createCell(tmpRow, iCol++, StringUtils.EMPTY, 12, true, false, true, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, false, true);
		createCell(tmpRow, iCol++, StringUtils.EMPTY, 12, true, false, true, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, false, true);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, 0, 4));
		
		tmpRow = sheet.createRow(iRow++);
		iCol = 0;
		createCell(tmpRow, iCol++, CONTACT_TYPE, 12, true, true, true, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, true, true);
		createCell(tmpRow, iCol++, ADDRESS_TYPE, 12, true, true, true, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, true, true);
		createCell(tmpRow, iCol++, VALUE, 12, true, true, true, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, true, true);
		createCell(tmpRow, iCol++, REMARK, 12, true, true, true, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, true, true);
		createCell(tmpRow, iCol++, PRIMARY, 12, true, true, true, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, true, true);
		
		if (mapContactTypes != null && !mapContactTypes.isEmpty()) {
			for(ETypeContactInfo key : mapContactTypes.keySet()) {
				List<ContactInfo> contactValues = mapContactTypes.get(key);
				if (contactValues != null && !contactValues.isEmpty()) {
					tmpRow = sheet.createRow(iRow++);
					iCol = 0;
					createCell(tmpRow, iCol++, key == null ? StringUtils.EMPTY : key.getDescLocale(), 12, true, true, true, true, CellStyle.ALIGN_LEFT, false, BG_GREY, FC_BLACK, true, false);
					
					if (contactValues.size() > 1) {
						sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow + (contactValues.size() - 2), 0, 0));
					}
					int index = 0;
					for (ContactInfo contactInfo : contactValues) {
						String addrType = contactInfo.getTypeAddress() != null ? 
								getDefaultString(contactInfo.getTypeAddress().getDescLocale()) : StringUtils.EMPTY;
								
						if (index > 0) {
							tmpRow = sheet.createRow(iRow++);
							iCol = 0;
							createCell(tmpRow, iCol++, StringUtils.EMPTY, 12, true, true, true, true, CellStyle.ALIGN_LEFT, false, BG_GREY, FC_BLACK, true, false);
						}
						
						String primary = contactInfo.isPrimary() ? I18N.message("yes") : StringUtils.EMPTY;
						
						createCell(tmpRow, iCol++, addrType, 12, true, true, true, true, CellStyle.ALIGN_LEFT, false, BG_GREY, FC_BLACK, true, false);
						createCell(tmpRow, iCol++, getDefaultString(contactInfo.getValue()), 12, true, true, true, true, CellStyle.ALIGN_LEFT, false, BG_GREY, FC_BLACK, true, false);
						createCell(tmpRow, iCol++, getDefaultString(contactInfo.getRemark()), 12, true, true, true, true, CellStyle.ALIGN_LEFT, false, BG_GREY, FC_BLACK, true, false);
						createCell(tmpRow, iCol++, primary, 12, true, true, true, true, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, true, false);
						index++;
					}
				}
			}
			iRow = iRow + 1;
		}
		return iRow;
	}
	
	/**
	 * 
	 * @param sheet
	 * @param tmpRow
	 * @param iRow
	 * @param iCol
	 * @param style
	 * @param individual
	 * @return
	 */
	private int getAddressTable(final Sheet sheet, Row tmpRow, int iRow, int iCol, final CellStyle style, Individual individual) {
		List<IndividualAddress> indAddresses = individual.getIndividualAddresses();
		Map<ETypeAddress, List<Address>> mapAddressTypes = new HashMap<ETypeAddress, List<Address>>();
		List<Address> addresses = null;
		if (indAddresses != null && !indAddresses.isEmpty()) {
			for (IndividualAddress individualAddress : indAddresses) {
				Address address = individualAddress.getAddress();
				if (!mapAddressTypes.containsKey(address.getType())) {
					addresses = new ArrayList<Address>();
					addresses.add(address);
					mapAddressTypes.put(address.getType(), addresses);
				} else {
					mapAddressTypes.get(address.getType()).add(address);
				}
			}
		}
	
		tmpRow = sheet.createRow(iRow++);
		iCol = 0;
		createCell(tmpRow, iCol++, I18N.message("addresses"), 12, true, true, false, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, false, true);
		createCell(tmpRow, iCol++, StringUtils.EMPTY, 12, true, false, false, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, false, true);
		createCell(tmpRow, iCol++, StringUtils.EMPTY, 12, true, false, false, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, false, true);
		createCell(tmpRow, iCol++, StringUtils.EMPTY, 12, true, false, false, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, false, true);
		createCell(tmpRow, iCol++, StringUtils.EMPTY, 12, true, false, true, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, false, true);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, 0, 4));
		
		tmpRow = sheet.createRow(iRow++);
		iCol = 0;
		createCell(tmpRow, iCol++, ADDRESS_TYPE, 12, true, true, true, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, true, true);
		createCell(tmpRow, iCol++, NO_BUILDING_MOO_SOI_FLOOR_ROOM_STREET, 12, true, true, true, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, true, true);
		createCell(tmpRow, iCol++, PROVINCE, 12, true, true, true, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, true, true);
		createCell(tmpRow, iCol++, DISTRICT, 12, true, true, true, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, true, true);
		createCell(tmpRow, iCol++, SUB_DISTRICT_POSTAL_CODE, 12, true, true, true, false, CellStyle.ALIGN_CENTER, false, BG_GREY, FC_BLACK, true, true);
		
		if (mapAddressTypes != null && !mapAddressTypes.isEmpty()) {
			for(ETypeAddress key : mapAddressTypes.keySet()) {
				List<Address> addressValues = mapAddressTypes.get(key);
				if (addressValues != null && !addressValues.isEmpty()) {
					tmpRow = sheet.createRow(iRow++);
					iCol = 0;
					createCell(tmpRow, iCol++, key == null ? StringUtils.EMPTY : key.getDescLocale(), 12, true, true, true, true, CellStyle.ALIGN_LEFT, false, BG_GREY, FC_BLACK, true, false);
					
					if (addressValues.size() > 1) {
						sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow + (addressValues.size() - 2), 0, 0));
					}
					int index = 0;
					for (Address address : addressValues) {
						if (index > 0) {
							tmpRow = sheet.createRow(iRow++);
							iCol = 0;
							createCell(tmpRow, iCol++, StringUtils.EMPTY, 12, true, true, true, true, CellStyle.ALIGN_LEFT, false, BG_GREY, FC_BLACK, true, false);
						}
						
						String noBuildingMooSoiFloorRoomStreet = StringUtils.EMPTY;
						String province = StringUtils.EMPTY;
						String district = StringUtils.EMPTY;
						String subDistrictPostalCode = StringUtils.EMPTY;
						
						if (address != null) {
							noBuildingMooSoiFloorRoomStreet = getDefaultString(address.getHouseNo()) 
									+ " / " + getDefaultString(address.getBuildingName())
									+ " / " + getDefaultString(address.getFloor()) 
									+ " / " + getDefaultString(address.getRoomNumber())
									+ " / " + getDefaultString(address.getLine1()) 
									+ " / " + getDefaultString(address.getLine2())
									+ " / " + getDefaultString(address.getStreet());
							
							province = address.getProvince() == null ? StringUtils.EMPTY : address.getProvince().getDescLocale();
							district = address.getDistrict() == null ? StringUtils.EMPTY : address.getDistrict().getDescLocale();
							subDistrictPostalCode = address.getCommune() == null ? StringUtils.EMPTY : getDefaultString(address.getCommune().getDescLocale())
									+ " / " +  getDefaultString(address.getPostalCode());
						}
						
						createCell(tmpRow, iCol++, noBuildingMooSoiFloorRoomStreet, 12, true, true, true, true, CellStyle.ALIGN_LEFT, false, BG_GREY, FC_BLACK, true, false);
						createCell(tmpRow, iCol++, province, 12, true, true, true, true, CellStyle.ALIGN_LEFT, false, BG_GREY, FC_BLACK, true, false);
						createCell(tmpRow, iCol++, district, 12, true, true, true, true, CellStyle.ALIGN_LEFT, false, BG_GREY, FC_BLACK, true, false);
						createCell(tmpRow, iCol++, subDistrictPostalCode, 12, true, true, true, true, CellStyle.ALIGN_LEFT, false, BG_GREY, FC_BLACK, true, false);
						index++;
					}
				}
			}
			iRow = iRow + 1;
		}
		return iRow;
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
	 * @param value
	 * @return
	 */
	private String getDefaultString(String value) {
		if (value == null) {
			return StringUtils.EMPTY;
		}
		return value;
	}

	/**
	 * 
	 * @param row
	 * @param iCol
	 * @param value
	 * @param fontsize
	 * @param top
	 * @param left
	 * @param right
	 * @param bottom
	 * @param alignment
	 * @param setBgColor
	 * @param bgColor
	 * @param fonCorlor
	 * @param wrapText
	 * @param isBold
	 * @return
	 */
	private Cell createCell(final Row row, final int iCol,
			final Object value, final int fontsize, final boolean top, final boolean left, final boolean right, final boolean bottom, final short alignment,
			final boolean setBgColor, final short bgColor, final short fonCorlor, boolean wrapText, boolean isBold) {
		
		final Cell cell = row.createCell(iCol);
		final Font itemFont = wb.createFont();
		itemFont.setFontHeightInPoints((short) fontsize);
		itemFont.setFontName("Arial");
		itemFont.setBold(isBold);

		final CellStyle style = wb.createCellStyle();
		
		DataFormat format = wb.createDataFormat();
		style.setDataFormat(format.getFormat("0"));
		
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
		style.setBottomBorderColor(FC_BLACK);
		if (top) {
			style.setBorderTop(CellStyle.BORDER_THIN);
		}
		if (left) {
			style.setBorderLeft(CellStyle.BORDER_THIN);
		}
		if (right) {
			style.setBorderRight(CellStyle.BORDER_THIN);
		}
		if (bottom) {
			style.setBorderBottom(CellStyle.BORDER_THIN);
		} 
		if (setBgColor) {
			style.setFillForegroundColor(bgColor);
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		}
		if (wrapText) {
			style.setWrapText(wrapText);
		}
		if (value instanceof Integer) {
			cell.setCellValue(Integer.valueOf(value.toString()));
			cell.setCellStyle(style);
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			
		}else if(value instanceof Double){
			cell.setCellValue(Double.valueOf(value.toString()));
			cell.setCellStyle(style);
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		}else if(value instanceof Long){
			cell.setCellValue(Long.valueOf(value.toString()));
			cell.setCellStyle(style);
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		}else {
			cell.setCellValue((value == null ? "" : value.toString()));
		}
		cell.setCellStyle(style);
		return cell;
	}

	/**
	 * 
	 * @return
	 */
	private Map<String, CellStyle> createStyles() {
		return styles;
	}
}
