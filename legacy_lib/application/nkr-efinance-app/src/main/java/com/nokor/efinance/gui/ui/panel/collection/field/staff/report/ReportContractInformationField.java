package com.nokor.efinance.gui.ui.panel.collection.field.staff.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.IndividualAddress;
import com.nokor.efinance.core.applicant.model.IndividualContactInfo;
import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractApplicant;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.EPaymentType;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.efinance.gui.report.xls.GLFIncomingInstallFields;
import com.nokor.efinance.tools.report.XLSAbstractReportExtractor;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;

/**
 * 
 * @author buntha.chea
 *
 */
public class ReportContractInformationField extends XLSAbstractReportExtractor implements GLFIncomingInstallFields, FinServicesHelper {
	
	private static Map<String, CellStyle> styles = null;
	private Contract contract;
	
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
	
	private String guarantorCurrentAddress;
	private String guarantorWorkAddress;
	private String guarantorHrAddress;

	@SuppressWarnings("deprecation")
	@Override
	public String generate(ReportParameter reportParameter) throws Exception {
	
		Map<String, Object> parameters = reportParameter.getParameters();
		contract = (Contract) parameters.get("contract");
		
		
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
		sheet.setColumnWidth(0, 3700);
		sheet.setColumnWidth(1, 6000);
		sheet.setColumnWidth(2, 6000);
		sheet.setColumnWidth(3, 6000);
		sheet.setColumnWidth(4, 6000);
		sheet.setZoom(7, 10);
		style.setWrapText(true);

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

		final Row headerRow = sheet.createRow(2);
		createCell(headerRow, 2, contract.getReference(), 16, true, false, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK);

		int iRow = 4;
		int iCol = 0;
		iCol = iCol + 2;
		iCol = 0;

		iRow = contractInfoTable(sheet, iRow, style);

		Row tmpRowEnd = sheet.createRow(iRow++);
		tmpRowEnd.setRowStyle(style);

		iRow = iRow + 1;

		String fileName = writeXLSData("ContractInformationField"
				+ DateUtils.getDateLabel(DateUtils.today(), "yyyyMMddHHmmss")
				+ ".xlsx");	
		
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
	private int contractInfoTable(final Sheet sheet, int iRow,
			final CellStyle style) throws Exception {
		/* Create total data header */
		int iCol = 0;
		Row tmpRow = sheet.createRow(iRow++);
		
		String ContractDetailt = DateUtils.getDateLabel(contract.getFirstDueDate()) + " @ " + contract.getTiInstallmentAmount() + " X " + contract.getTerm(); 
		String lesseeFullName = contract.getApplicant().getNameLocale();
		String currentAddressPhone = "";
		
		Individual individual = contract.getApplicant().getIndividual();
		if (individual.getMainAddress() != null) {
			currentAddressPhone = displayDetailAddress(individual.getMainAddress());
			currentAddressPhone += " Tel." + individual.getIndividualPrimaryContactInfo();
		}
		
		String workAddressPhone = "";
		if (individual.getIndividualAddress(ETypeAddress.WORK) != null) {
			workAddressPhone = displayDetailAddress(individual.getIndividualAddress(ETypeAddress.WORK).getAddress());
			workAddressPhone += " Tel." + INDIVI_SRV.getIndividualContactInfoByAddressType(individual.getId(), ETypeAddress.WORK).getContactInfo().getValue();
		}
		
		String hrAddress = "";
		if (individual.getIndividualAddress(ETypeAddress.HRADDRESS) != null) {
			hrAddress = displayDetailAddress(individual.getIndividualAddress(ETypeAddress.HRADDRESS).getAddress());
		}
		
		String detailMotoCycle = "";
		Asset asset = contract.getAsset();
		if (asset != null) {
			detailMotoCycle += "รถจักรยานยนต์ " + asset.getBrandDescLocale();
			AssetModel assModel = asset.getModel();
			if (assModel != null) {
				detailMotoCycle +=", " + assModel.getDescLocale();
				detailMotoCycle += ", " + assModel.getEngine() != null ? assModel.getEngine().getDescLocale() : null;
			}
			detailMotoCycle += ", เลขโครง " + asset.getChassisNumber();
			detailMotoCycle += ", ทะเบียน " + asset.getPlateNumber();
			detailMotoCycle += ", สี "+ asset.getColor().getDescLocale();
		}
		Dealer dealer = contract.getDealer();
		detailMotoCycle += ", ร้าน " + dealer.getNameLocale();
		
		String guarantorFullName = "";
		ContractApplicant contractApplicant = contract.getContractApplicant(EApplicantType.G);
		Applicant applicant = null;
		getAddressesGuarantor();
		if (contractApplicant != null) {
			applicant = contractApplicant.getApplicant();
			guarantorFullName = applicant.getNameLocale();
			
			IndividualContactInfo individualContactInfoCurrent = INDIVI_SRV.getIndividualContactInfoByAddressType(applicant.getIndividual().getId(), ETypeAddress.MAIN);
			if (individualContactInfoCurrent != null) {
				if (individualContactInfoCurrent.getContactInfo().isPrimary()) {
					guarantorCurrentAddress = " Tel." + individualContactInfoCurrent.getContactInfo().getValue();
				}
			}
			
			IndividualContactInfo individualContactInfoWork = INDIVI_SRV.getIndividualContactInfoByAddressType(applicant.getIndividual().getId(), ETypeAddress.WORK);
			if (individualContactInfoWork != null) {
				guarantorWorkAddress += " Tel." + individualContactInfoWork.getContactInfo().getValue();
			}
			
			IndividualContactInfo individualContactInfoHR = INDIVI_SRV.getIndividualContactInfoByAddressType(applicant.getIndividual().getId(), ETypeAddress.HRADDRESS);
			if (individualContactInfoHR != null) {
				guarantorHrAddress += " Tel." + individualContactInfoHR.getContactInfo().getValue();
			}
		}
		
		Collection collection = contract.getCollection();
		double collectionFee = 0d;
		List<Cashflow> cashflowsCollectionFee = CASHFLOW_SRV.getCashflowCollectionFee(contract);
		for (Cashflow cashflow : cashflowsCollectionFee) {
			if (cashflow.getService() != null) {
				collectionFee += cashflow.getService().getTiPrice();
			}
		}
		String detailAmount = "ค่างวดค้างชำระ " + AmountUtils.format(collection.getTeTotalAmountInOverdue()) + 
							  ", ค่าปรับ " + AmountUtils.format(collection.getTiPenaltyAmount()) +", "+ AmountUtils.format(collectionFee);
		
		iCol = 0;
		sheet.addMergedRegion(new CellRangeAddress(4, 4, 1, 5));
		createCell(tmpRow, iCol++, I18N.message("ค่างวดเริ่ม"), 12, false, false, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK);
		createCell(tmpRow, iCol++, ContractDetailt, 12, false, false, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK);
		
		iCol = 0;
		tmpRow = sheet.createRow(iRow++);
		sheet.addMergedRegion(new CellRangeAddress(5, 5, 1, 5));
		createCell(tmpRow, iCol++, "ชื่อผู้เช่าซื้อ", 12, false, false, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK);
		createCell(tmpRow, iCol++, lesseeFullName, 12, false, false, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK);
		
		iCol = 0;
		tmpRow = sheet.createRow(iRow++);
		sheet.addMergedRegion(new CellRangeAddress(6, 6, 1, 5));
		createCell(tmpRow, iCol++, "ที่อยู่ปัจจุบัน", 12, false, false, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK);
		createCell(tmpRow, iCol++, currentAddressPhone, 12, false, false, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK);
		
		iCol = 0;
		tmpRow = sheet.createRow(iRow++);
		sheet.addMergedRegion(new CellRangeAddress(7, 7, 1, 5));
		createCell(tmpRow, iCol++, "ที่ทำงาน", 12, false, false, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK);
		createCell(tmpRow, iCol++, workAddressPhone, 12, false, false, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK);
		
		iCol = 0;
		tmpRow = sheet.createRow(iRow++);
		sheet.addMergedRegion(new CellRangeAddress(8, 8, 1, 5));
		createCell(tmpRow, iCol++, "ที่อยู่ ทร.14", 12, false, false, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK);
		createCell(tmpRow, iCol++, hrAddress, 12, false, false, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK);
		
		iCol = 0;
		tmpRow = sheet.createRow(iRow++);
		sheet.addMergedRegion(new CellRangeAddress(9, 9, 1, 5));
		createCell(tmpRow, iCol++, "เช่าซื้อ", 12, false, false, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK);
		createCell(tmpRow, iCol++, detailMotoCycle, 12, false, false, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK);
		
		iCol = 0;
		tmpRow = sheet.createRow(iRow++);
		sheet.addMergedRegion(new CellRangeAddress(10, 10, 1, 5));
		createCell(tmpRow, iCol++, "ค้ำ 1", 12, false, false, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK);
		createCell(tmpRow, iCol++, guarantorFullName, 12, false, false, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK);
		
		
		iCol = 0;
		tmpRow = sheet.createRow(iRow++);
		sheet.addMergedRegion(new CellRangeAddress(11, 11, 1, 5));
		createCell(tmpRow, iCol++, "ที่อยู่ปัจจุบัน", 12, false, false, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK);
		createCell(tmpRow, iCol++, guarantorCurrentAddress, 12, false, false, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK);
		
		iCol = 0;
		tmpRow = sheet.createRow(iRow++);
		sheet.addMergedRegion(new CellRangeAddress(12, 12, 1, 5));
		createCell(tmpRow, iCol++, "ทำงาน", 12, false, false, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK);
		createCell(tmpRow, iCol++, guarantorWorkAddress, 12, false, false, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK);
		
		iCol = 0;
		tmpRow = sheet.createRow(iRow++);
		sheet.addMergedRegion(new CellRangeAddress(12, 12, 1, 5));
		createCell(tmpRow, iCol++, "ที่อยู่ ทร.14", 12, false, false, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK);
		createCell(tmpRow, iCol++, guarantorHrAddress, 12, false, false, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK);
		
		iCol = 0;
		tmpRow = sheet.createRow(iRow++);
		sheet.addMergedRegion(new CellRangeAddress(12, 12, 1, 5));
		createCell(tmpRow, iCol++, I18N.message("จำนวนเงิน"), 12, false, false, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK);
		createCell(tmpRow, iCol++, detailAmount, 12, false, false, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK);
		
		iRow += 3;
		tmpRow = sheet.createRow(iRow++);
		createCell(tmpRow, 3, I18N.message("customer.payment.history"), 16, true, false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK);
		
		iCol = 1;
		iRow++;
		tmpRow = sheet.createRow(iRow++);
		createCell(tmpRow, iCol++, "วันที่", 12, false, true, CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "เดือน", 12, false, true, CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "งวดที่", 12, false, true, CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, "จำนวนเงิน", 12, false, true, CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		
		List<Payment> payments = PAYMENT_SRV.getListPaymentsByContractID(contract.getId(), new EPaymentType[] {EPaymentType.IRC});
		for (Payment payment : payments) {
			iCol = 1;
			tmpRow = sheet.createRow(iRow++);
			List<Cashflow> cashflows = payment.getCashflows();
			createCell(tmpRow, iCol++, DateUtils.getDateLabel(payment.getPaymentDate()), 12, false, true, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK);
			createCell(tmpRow, iCol++, cashflows.get(0).getNumInstallment() + "", 12, false, true, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK);
			createCell(tmpRow, iCol++, "", 12, false, true, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK);
			createCell(tmpRow, iCol++, AmountUtils.format(payment.getTiPaidAmount()), 12, false, true, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK);
		}
		
		return iRow;

	}
	
	/**
	 * 
	 */
	protected Cell createCell(final Row row, final int iCol,
			final String value, final CellStyle style) {
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
	protected Cell createCell(final Row row, final int iCol,
			final String value, final int fontsize, final boolean isBold,
			final boolean hasBorder, final short alignment,
			final boolean setBgColor, final short bgColor, final short fonCorlor) {
		final Cell cell = row.createCell(iCol);
		final Font itemFont = wb.createFont();
		itemFont.setFontHeightInPoints((short) fontsize);
		if (isBold) {
			itemFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		}
		itemFont.setFontName("Times New Roman");

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
		}
		if (setBgColor) {
			style.setFillForegroundColor(bgColor);
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		}
		cell.setCellValue((value == null ? "" : value));
		cell.setCellStyle(style);
		return cell;
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
	
	public String displayDetailAddress(Address address) {
		List<String> descriptions = new ArrayList<>();
		descriptions.add("no." + address.getHouseNo());
		if (StringUtils.isNotEmpty(address.getLine1())) {
			descriptions.add(" moo." + address.getLine1());
		}
		if (StringUtils.isNotEmpty(address.getLine2())) {
			descriptions.add(" soi." + address.getLine2());
		}
		if (StringUtils.isNotEmpty(address.getLine3())) {
			descriptions.add(address.getLine3());
		}
		descriptions.add(" street." + address.getStreet());
		descriptions.add(address.getCommune() != null ? " sub district." + address.getCommune().getDesc() : StringUtils.EMPTY);
		descriptions.add(address.getDistrict() != null ? " district." + address.getDistrict().getDesc() : StringUtils.EMPTY);
		descriptions.add(address.getProvince() != null ? " province." + address.getProvince().getDesc() : StringUtils.EMPTY);
		descriptions.add(address.getPostalCode());
		
		return StringUtils.join(descriptions, ",");
	}

	/**
	 * getAddressesGuarantor
	 */
	private void getAddressesGuarantor() {
		guarantorCurrentAddress = "";
		guarantorWorkAddress = "";
		Map<EApplicantType, Applicant> applicants = new HashMap<>();
		applicants.put(EApplicantType.C, contract.getApplicant());
		ContractApplicant conApp = contract.getContractApplicant(EApplicantType.G);
		if (conApp != null) {
			applicants.put(EApplicantType.G, conApp.getApplicant());
		}
		
		Map<EApplicantType, List<Address>> addresses = new HashMap<>();
		if (!applicants.isEmpty()) {
			for (EApplicantType appType : applicants.keySet()) {
				Applicant applicant = applicants.get(appType);
				Individual individual = applicant.getIndividual();
				
				if (individual != null) {
					List<IndividualAddress> indAddrs = individual.getIndividualAddresses();
					if (indAddrs != null && !indAddrs.isEmpty()) {
						List<Address> addrs = new ArrayList<Address>();
						for (IndividualAddress indaddr : indAddrs) {
							addrs.add(indaddr.getAddress());
						}
						addresses.put(appType, addrs);
					}
					if (EApplicantType.C.equals(appType)) {
						for (Address address : addresses.get(EApplicantType.C)) {
							if (ETypeAddress.MAIN.equals(address.getType())) {
								guarantorCurrentAddress = ADDRESS_SRV.getDetailAddress(address);
							} else if (ETypeAddress.WORK.equals(address.getType())) {
								guarantorWorkAddress = ADDRESS_SRV.getDetailAddress(address);
							} else if (ETypeAddress.HRADDRESS.equals(address.getType())) {
								guarantorHrAddress = ADDRESS_SRV.getDetailAddress(address);
							}
						}
					} 
				}
			}
		}
	}
}
