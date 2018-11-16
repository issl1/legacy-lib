package com.nokor.efinance.gui.report.xls;

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
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.model.Employment;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.quotation.QuotationService;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.efinance.tools.report.Report;
import com.nokor.efinance.tools.report.XLSAbstractReportExtractor;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;
import com.nokor.frmk.security.context.SecApplicationContextHolder;

/**
 * @author ly.youhort
 */
public class GLFQuotations extends XLSAbstractReportExtractor implements Report, GLFApplicantFields, QuotationEntityField {

	protected QuotationService quotationService = (QuotationService) SecApplicationContextHolder.getContext().getBean("quotationService");

	private Map<String, CellStyle> styles = null;
	private static final String DEALER_TYPE = "dealer.type";
	
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

	public GLFQuotations() {

	}

	@Override
	public String generate(ReportParameter reportParameter) throws Exception {

		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<Quotation>(Quotation.class);
		
		Map<String, Object> parameters = reportParameter.getParameters();
		EDealerType dealerType = (EDealerType) parameters.get(DEALER_TYPE);
		Dealer dealer = (Dealer) parameters.get(DEALER);
		QuotationWkfStatus[] quotationStatus = (QuotationWkfStatus[]) parameters.get(WKF_STATUS);		
		Province province = (Province) parameters.get(PROVINCE);

		if (dealer != null) {
			restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));
		}
		restrictions.addAssociation("dealer", "dea", JoinType.INNER_JOIN);
		if (dealerType != null) {
			restrictions.addCriterion(Restrictions.eq("dea.dealerType", dealerType));
		}
		
		if (quotationStatus != null) {
			restrictions.addCriterion(Restrictions.in(WKF_STATUS, quotationStatus));
		}

		restrictions.addAssociation("quotationApplicants", "quoapp", JoinType.INNER_JOIN);
		restrictions.addAssociation("quoapp.applicant", "app", JoinType.INNER_JOIN);
		restrictions.addCriterion("quoapp.applicantType", EApplicantType.C);

		if (province != null) {
			restrictions.addAssociation("app.applicantAddresses", "appaddr", JoinType.INNER_JOIN);
			restrictions.addAssociation("appaddr.address", "addr", JoinType.INNER_JOIN);
			restrictions.addCriterion("appaddr.addressType", ETypeAddress.MAIN);
			restrictions.addCriterion("addr.province.id", province.getId());
		}
		restrictions.addOrder(Order.desc(QUOTATION_DATE));

		List<Quotation> quotations = quotationService.list(restrictions);
		
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
		sheet.setColumnWidth(0, 4000);
		sheet.setColumnWidth(1, 5000);
		sheet.setColumnWidth(2, 11000);
		sheet.setColumnWidth(3, 11000);
		sheet.setColumnWidth(4, 11000);
		sheet.setColumnWidth(5, 5000);
		sheet.setColumnWidth(6, 4000);
		sheet.setColumnWidth(7, 5000);
		sheet.setColumnWidth(8, 5000);
		sheet.setColumnWidth(9, 5000);
		sheet.setColumnWidth(10, 6000);
		sheet.setColumnWidth(11, 8000);
		sheet.setColumnWidth(12, 7000);
		sheet.setColumnWidth(13, 7000);
		sheet.setColumnWidth(14, 7000);
		sheet.setColumnWidth(15, 5000);
		sheet.setColumnWidth(16, 4500);
		sheet.setColumnWidth(17, 6000);
		sheet.setColumnWidth(18, 7000);
		sheet.setColumnWidth(19, 6000);
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

		final Row headerRow = sheet.createRow(2);
		createCell(headerRow, 6, APPLICANT_TITTLE, 14, true, false, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK);

		int iRow = 4;
		int iCol = 0;
		iCol = iCol + 2;
		iCol = 0;
		iRow = iRow + 1;

		iRow = paymentTable(sheet, iRow, style, quotations);

		Row tmpRowEnd = sheet.createRow(iRow++);
		tmpRowEnd.setRowStyle(style);

		iRow = iRow + 2;

		String fileName = writeXLSData("Applicant_" + DateUtils.getDateLabel(DateUtils.today(), "yyyyMMddHHmmssSSS") + ".xlsx");

		return fileName;
	}

	private int paymentTable(final Sheet sheet, int iRow, final CellStyle style, List<Quotation> quotations) throws Exception {
		/* Create total data header */

		int iCol = 0;
		Row tmpRow = sheet.createRow(iRow++);
		createCell(tmpRow, iCol++, NO, 12, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow, iCol - 1, iCol - 1));
		
		createCell(tmpRow, iCol++, JOBS_SENIORITY, 12, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow, iCol - 1, iCol - 1));
		
		createCell(tmpRow, iCol++, INDUSTRY, 12, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow, iCol - 1, iCol - 1));
		
		createCell(tmpRow, iCol++, POS, 12, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow, iCol - 1, iCol - 1));
		
		createCell(tmpRow, iCol++, OCCUPATION, 12, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow, iCol - 1, iCol - 1));
		
		createCell(tmpRow, iCol++, "Gender", 12, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow, iCol - 1, iCol - 1));
		
		createCell(tmpRow, iCol++, AGE, 12, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow, iCol - 1, iCol - 1));
		
		createCell(tmpRow, iCol++, HOUSE_HOLD, 12, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow, iCol - 1, iCol - 1));
		
		createCell(tmpRow, iCol++, GROSS_INCOME, 12, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow, iCol - 1, iCol - 1));
		
		createCell(tmpRow, iCol++, PERSONAL_EXPENSE, 12, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow, iCol - 1, iCol - 1));
		
		createCell(tmpRow, iCol++, FAMILY_EXPENSE, 12, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow, iCol - 1, iCol - 1));
		
		createCell(tmpRow, iCol++, "Address", 12, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow - 1, iCol - 1, iCol + 2));
		
		iCol = iCol + 3;
		createCell(tmpRow, iCol++, MARRIED, 12, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow, iCol - 1, iCol - 1));
		
		createCell(tmpRow, iCol++, HOUSING_STATUS, 12, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow, iCol - 1, iCol - 1));
		
		createCell(tmpRow, iCol++, GUARANTOR_STATUS, 12, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow, iCol - 1, iCol - 1));
		
		createCell(tmpRow, iCol++, HONDA_MODEL, 12, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow, iCol - 1, iCol - 1));
		
		createCell(tmpRow, iCol++, RESULT, 12, false, false,
				CellStyle.ALIGN_CENTER, true, BG_GREEN, FC_WHITE);
		sheet.addMergedRegion(new CellRangeAddress(iRow - 1, iRow, iCol - 1, iCol - 1));
		
		tmpRow = sheet.createRow(iRow++);
		iCol = 11;
		createCell(tmpRow, iCol++, VILLAGE_EN, 12, false, true,
				CellStyle.ALIGN_LEFT, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, SANGKAT_EN, 12, false, true,
				CellStyle.ALIGN_LEFT, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, KHAN, 12, false, true,
				CellStyle.ALIGN_LEFT, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, CITY, 12, false, true,
				CellStyle.ALIGN_LEFT, true, BG_GREEN, FC_WHITE);

		int nbRows = quotations.size();
		for (int i = 0; i < nbRows; i++) {
			tmpRow = sheet.createRow(iRow++);
			
			Quotation quotation = quotations.get(i);
			Applicant guarantor = quotation.getGuarantor();
			Asset asset = quotation.getAsset();
			Employment employment = null;
			Address applicantAddress = null;
			String result = quotation.getWkfStatus().getDesc();

			String dealer = "";
			Applicant applicant = quotation.getApplicant();
			Individual individual = applicant.getIndividual();
			if (quotation.getDealer() != null) {
				dealer = quotation.getDealer().getNameEn();
			}
			employment = individual.getCurrentEmployment();
			applicantAddress = individual.getMainAddress();

			String gender = individual.getGender() == null ? "" : individual.getGender().toString();
			int age = DateUtils.getAge(individual.getBirthDate());
			Double crossIncome = 0d;
			Double personalExpense = individual.getMonthlyPersonalExpenses();
			Double familyExpense = individual.getMonthlyFamilyExpenses();
			String married = individual.getMaritalStatus() == null ? "" : individual.getMaritalStatus().getDesc();
			int household = 1;

			String occupation = "";
			String jobSeniority = "";
			String industry = "";
			if (employment != null) {
				if (employment.getEmploymentStatus() != null) {
					occupation = employment.getPosition() + " " + employment.getEmploymentStatus().getDescEn();
				}
				if (employment.getSeniorityLevel() != null) {
					jobSeniority = employment.getSeniorityLevel().getDesc();
				}
				if (employment.getEmploymentIndustry() != null) {
					industry = employment.getEmploymentIndustry().getDescEn();
				}

			}
			String guarantorStatus = "";
			if (guarantor != null && quotation.getQuotationApplicant(EApplicantType.G).getRelationship() != null) {
				guarantorStatus = quotation.getQuotationApplicant(EApplicantType.G).getRelationship().getDescEn();
			} else {
				guarantorStatus = "N/A";
			}
			
			String assetModel = asset.getDescEn();
			String housingStatus = "";
			String village = "";
			String khan = "";
			String sangkat = "";
			String city = "";
			
			if (applicantAddress != null) {
				if (applicantAddress.getProperty() != null) {
					housingStatus = applicantAddress.getProperty().getDesc();
				}
				village = applicantAddress.getVillage() == null ? "" : applicantAddress.getVillage().getDescEn();
				khan = applicantAddress.getDistrict() == null ? "" : applicantAddress.getDistrict().getDescEn();
				sangkat = applicantAddress.getCommune() == null ? "" : applicantAddress.getCommune().getDescEn();
				city = applicantAddress.getProvince() == null ? "" : applicantAddress.getProvince().getDescEn();
			}

			iCol = 0;
			createNumericCell(tmpRow, iCol++, (i + 1), true,
					CellStyle.ALIGN_CENTER, 14, false, true, BG_WHITE, FC_BLACK);
			createNumericCell(tmpRow, iCol++, jobSeniority, true,
					CellStyle.ALIGN_CENTER, 14, false, true, BG_WHITE, FC_BLACK);
			createNumericCell(tmpRow, iCol++, industry, true,
					CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
			createNumericCell(tmpRow, iCol++, dealer, true,
					CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);

			createNumericCell(tmpRow, iCol++, occupation, true,
					CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
			createNumericCell(tmpRow, iCol++, gender, true,
					CellStyle.ALIGN_CENTER, 14, false, true, BG_WHITE, FC_BLACK);
			createNumericCell(tmpRow, iCol++, age, true,
					CellStyle.ALIGN_CENTER, 14, false, true, BG_WHITE, FC_BLACK);

			createNumericCell(tmpRow, iCol++, (household == 0 ? "N/A"
					: household), true, CellStyle.ALIGN_CENTER, 14, false, true, BG_WHITE, FC_BLACK);
			createNumericCell(tmpRow, iCol++, crossIncome, true,
					CellStyle.ALIGN_RIGHT, 14, false, true, BG_WHITE, FC_BLACK);
			createNumericCell(tmpRow, iCol++, personalExpense, true,
					CellStyle.ALIGN_RIGHT, 14, false, true, BG_WHITE, FC_BLACK);
			createNumericCell(tmpRow, iCol++, familyExpense, true,
					CellStyle.ALIGN_RIGHT, 14, false, true, BG_WHITE, FC_BLACK);

			createNumericCell(tmpRow, iCol++, village, true,
					CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
			createNumericCell(tmpRow, iCol++, sangkat, true,
					CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
			createNumericCell(tmpRow, iCol++, khan, true,
					CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
			createNumericCell(tmpRow, iCol++, city, true,
					CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);

			createNumericCell(tmpRow, iCol++, married, true,
					CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
			createNumericCell(tmpRow, iCol++, housingStatus, true,
					CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
			createNumericCell(tmpRow, iCol++, guarantorStatus, true,
					CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
			createNumericCell(tmpRow, iCol++, assetModel, true,
					CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
			createNumericCell(tmpRow, iCol++, result, true,
					CellStyle.ALIGN_LEFT, 14, false, true, BG_WHITE, FC_BLACK);
		}

		iRow = iRow + 1;
		return iRow;

	}
	
	protected Cell createCell(final Row row, final int iCol, final String value, final CellStyle style) {
		final Cell cell = row.createCell(iCol);
		cell.setCellValue((value == null ? "" : value));
		cell.setCellStyle(style);
		return cell;
	}

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
		} else if (value instanceof Double) {
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
}
