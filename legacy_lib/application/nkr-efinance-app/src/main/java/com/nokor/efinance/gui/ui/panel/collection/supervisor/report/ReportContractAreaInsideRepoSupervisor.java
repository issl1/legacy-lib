package com.nokor.efinance.gui.ui.panel.collection.supervisor.report;

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
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.address.model.Area;
import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.efinance.gui.report.xls.GLFIncomingInstallFields;
import com.nokor.efinance.tools.report.XLSAbstractReportExtractor;

/**
 * 
 * @author buntha.chea
 *
 */
public class ReportContractAreaInsideRepoSupervisor extends XLSAbstractReportExtractor implements GLFIncomingInstallFields {
	
	private static Map<String, CellStyle> styles = null;
	
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
	
	private List<Contract> contracts;

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public String generate(ReportParameter reportParameter) throws Exception {
		
		Map<String, Object> parameters = reportParameter.getParameters();
		contracts = (List<Contract>) parameters.get("contracts");
		
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
		sheet.setColumnWidth(1, 3500);
		sheet.setColumnWidth(2, 3500);
		sheet.setColumnWidth(3, 3500);
		sheet.setColumnWidth(4, 3300);
		sheet.setColumnWidth(5, 2000);
		sheet.setColumnWidth(6, 2500);
		sheet.setColumnWidth(7, 3700);
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

		//final Row headerRow = sheet.createRow(2);
		

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
		
		Row tmpRow = sheet.createRow(iRow++);
		createCell(tmpRow, 0, I18N.message("leassee"), 16, true, false, CellStyle.ALIGN_CENTER, true, BG_WHITE, FC_BLACK);
		
		int iCol = 0;
		iRow++;
		tmpRow = sheet.createRow(iRow++);
		createCell(tmpRow, iCol++, I18N.message("contract.id"), 12, true, true, CellStyle.ALIGN_LEFT, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, I18N.message("province"), 12, true, true, CellStyle.ALIGN_LEFT, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, I18N.message("district"), 12, true, true, CellStyle.ALIGN_LEFT, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, I18N.message("sub.district"), 12, true, true, CellStyle.ALIGN_LEFT, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, I18N.message("brand"), 12, true, true, CellStyle.ALIGN_LEFT, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, I18N.message("model"), 12, true, true, CellStyle.ALIGN_LEFT, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, I18N.message("color"), 12, true, true, CellStyle.ALIGN_LEFT, true, BG_GREEN, FC_WHITE);
		createCell(tmpRow, iCol++, I18N.message("plate.number"), 12, true, true, CellStyle.ALIGN_LEFT, true, BG_GREEN, FC_WHITE);
		
		
		for (Contract contract : contracts) {
			iCol = 0;
			tmpRow = sheet.createRow(iRow++);
			Collection collection = contract.getCollection();
			Area area = collection.getArea();
			String province = "";
			String district = "";
			String subDistrict = "";
			if (area != null) {
				province = area.getProvince() != null ? area.getProvince().getDesc() : "";
				district = area.getDistrict() != null ? area.getDistrict().getDesc() : "";
				subDistrict = area.getCommune() != null ? area.getCommune().getDesc() : "";
			}
			
			Asset asset = contract.getAsset();
			String brand = "";
			String model = "";
			String color = "";
			String plateNumber = "";
			if (asset != null) {
				brand = asset.getBrandDescLocale();
				AssetModel assModel = asset.getModel();
				if (assModel != null) {
					model = assModel.getDescLocale();
				}
				plateNumber = asset.getPlateNumber();
				color = asset.getColor() == null ? StringUtils.EMPTY : asset.getColor().getDescLocale();
			}
			 
			createCell(tmpRow, iCol++, contract.getReference(), 12, false, true, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK);
			createCell(tmpRow, iCol++, province, 12, false, true, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK);
			createCell(tmpRow, iCol++, district, 12, false, true, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK);
			createCell(tmpRow, iCol++, subDistrict, 12, false, true, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK);
			createCell(tmpRow, iCol++, brand, 12, false, true, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK);
			createCell(tmpRow, iCol++, model, 12, false, true, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK);
			createCell(tmpRow, iCol++, color, 12, false, true, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK);
			createCell(tmpRow, iCol++, plateNumber, 12, false, true, CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK);
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
}
