package com.nokor.efinance.tools.report;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.shared.conf.AppConfig;

/**
 * 
 * @author ly.youhort
 *
 */
public abstract class XLSAbstractReportExtractor implements Report {

	private static final String TITLE_STYLE = "TITLE_STYLE";
	private static final String TITLE_STYLE_BOLD = "TITLE_STYLE_BOLD";
	private static final String TITLE_CENTER_STYLE_BOLD = "TITLE_CENTER_STYLE_BOLD";
	private static final String COL_TITLE_STYLE = "COL_TITLE_STYLE";

	private static final String COL_LEFT_STYLE = "COL_LEFT_STYLE";
	private static final String COL_LEFT_STYLE_BOLD = "COL_LEFT_STYLE_BOLD";
	private static final String COL_LEFT_NO_BORDER_STYLE = "COL_LEFT_NO_BORDER_STYLE";
	private static final String COL_LEFT_BOLD_NO_BORDER_STYLE = "COL_LEFT_BOLD_NO_BORDER_STYLE";

	private static final String COL_RIGHT_STYLE = "COL_RIGHT_STYLE";
	private static final String COL_RIGHT_STYLE_BOLD = "COL_RIGHT_STYLE_BOLD";
    private static final String COL_RIGHT_STYLE_NO_BORDER_STYLE = "COL_RIGHT_STYLE_NO_BORDER_STYLE";

    private static final String COL_CENTER_STYLE = "COL_CENTER_STYLE";
    private static final String COL_CENTER_NO_BORDER_STYLE = "COL_CENTER_NO_BORDER_STYLE";
    private static final String COL_TITLE_CENTER_STYLE = "COL_TITLE_CENTER_STYLE";
	
    protected XSSFWorkbook wb = null;
    private Map<String, CellStyle> styles = null;
    private static String FORMAT_USD = "$ ###,###,##0.00";
    private static String FORMAT_KHR = "R ###,###,###,##0";
    private static String FORMAT_DATE = "dd\\-mmm\\-yyyy;@";
    private static String FORMAT_PERCENTAGE = "0.00%";

    /** Background color format */
    protected short BG_BLUE = IndexedColors.DARK_BLUE.getIndex();
    protected short BG_RED = IndexedColors.RED.getIndex();
    protected short BG_CYAN = IndexedColors.LIGHT_TURQUOISE.getIndex();
    protected short BG_YELLOW = IndexedColors.LIGHT_YELLOW.getIndex();
    protected short BG_GREY = IndexedColors.GREY_25_PERCENT.getIndex();

    /** Font color */
    protected short FC_WHITE = IndexedColors.WHITE.getIndex();
    protected short FC_BLACK = IndexedColors.BLACK.getIndex();

    public XLSAbstractReportExtractor() {
    }

    protected void createWorkbook(final String template) throws IOException {
        InputStream stream = null;
        if (StringUtils.isNotEmpty(template)) {
            stream = this.getClass().getClassLoader().getResourceAsStream(template);
        }
        if (stream != null) {
            wb = new XSSFWorkbook(stream);
        }
        else {
            wb = new XSSFWorkbook();
        }
        createStyles();
    }
    
    /**
     * @param s
     * @param workbookx
     */
    protected void lockAll(Sheet s, XSSFWorkbook workbookx) {
        String password= "9B7D2C34A366BF890C730641E6CECF6F";
        byte[] pwdBytes = DatatypeConverter.parseHexBinary(password);
       
        XSSFSheet sheet = ((XSSFSheet) s);
        sheet.lockDeleteColumns();
        sheet.lockDeleteRows();
        sheet.lockFormatCells();
        sheet.lockFormatColumns();
        sheet.lockFormatRows();
        sheet.lockInsertColumns();
        sheet.lockInsertRows();
        sheet.lockSelectLockedCells();
        sheet.lockSelectUnlockedCells();
        sheet.getCTWorksheet().getSheetProtection().setPassword(pwdBytes);
        sheet.enableLocking();
        workbookx.lockStructure();
    }

    /**
     * @return
     */
    private Map<String, CellStyle> createStyles() {
    	
    	styles = new HashMap<String, CellStyle>();
    	
        CellStyle style;

        final Font titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short) 10);
        titleFont.setFontName("Trebuchet MS");
        style = wb.createCellStyle();
        style.setFont(titleFont);
        styles.put(TITLE_STYLE, style);

        final Font titleBoldFont = wb.createFont();
        titleBoldFont.setFontHeightInPoints((short) 10);
        titleBoldFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        titleBoldFont.setFontName("Trebuchet MS");
        style = wb.createCellStyle();
        style.setFont(titleBoldFont);
        styles.put(TITLE_STYLE_BOLD, style);

        final Font titleBoldCenterFont = wb.createFont();
        titleBoldCenterFont.setFontHeightInPoints((short) 10);
        titleBoldCenterFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        titleBoldCenterFont.setFontName("Trebuchet MS");
        style = wb.createCellStyle();
        style.setFont(titleBoldCenterFont);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        styles.put(TITLE_CENTER_STYLE_BOLD, style);

        final Font colTitleNoBackgroundFont = wb.createFont();
        colTitleNoBackgroundFont.setFontHeightInPoints((short) 9);
        colTitleNoBackgroundFont.setFontName("Trebuchet MS");
        colTitleNoBackgroundFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style = wb.createCellStyle();
        style.setFont(colTitleNoBackgroundFont);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.ALIGN_CENTER);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setWrapText(true);
        styles.put(COL_TITLE_STYLE, style);

        final Font itemLeftFont = wb.createFont();
        itemLeftFont.setFontHeightInPoints((short) 9);
        itemLeftFont.setFontName("Trebuchet MS");
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setFont(itemLeftFont);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        styles.put(COL_LEFT_STYLE, style);

        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setFont(itemLeftFont);
        styles.put(COL_LEFT_NO_BORDER_STYLE, style);

        final Font itemRightFont = wb.createFont();
        itemRightFont.setFontHeightInPoints((short) 9);
        itemRightFont.setFontName("Trebuchet MS");
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setFont(itemRightFont);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        styles.put(COL_RIGHT_STYLE, style);

        final Font itemCenterFont = wb.createFont();
        itemCenterFont.setFontHeightInPoints((short) 9);
        itemCenterFont.setFontName("Trebuchet MS");
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFont(itemCenterFont);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        styles.put(COL_CENTER_STYLE, style);

        final Font itemCenterNoBorderFont = wb.createFont();
        itemCenterNoBorderFont.setFontHeightInPoints((short) 9);
        itemCenterNoBorderFont.setFontName("Trebuchet MS");
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFont(itemCenterNoBorderFont);
        styles.put(COL_CENTER_NO_BORDER_STYLE, style);

        final Font itemHeaderCenterFont = wb.createFont();
        itemHeaderCenterFont.setFontHeightInPoints((short) 12);
        itemHeaderCenterFont.setFontName("Trebuchet MS");
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFont(itemHeaderCenterFont);
        style.setWrapText(true);
        styles.put(COL_TITLE_CENTER_STYLE, style);

        final Font itemLeftBoldFont = wb.createFont();
        itemLeftBoldFont.setFontHeightInPoints((short) 9);
        itemLeftBoldFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        itemLeftBoldFont.setFontName("Trebuchet MS");
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setFont(itemLeftBoldFont);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        styles.put(COL_LEFT_STYLE_BOLD, style);

        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setFont(itemLeftBoldFont);
        styles.put(COL_LEFT_BOLD_NO_BORDER_STYLE, style);

        final Font itemRightBoldFont = wb.createFont();
        itemRightBoldFont.setFontHeightInPoints((short) 9);
        itemRightBoldFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        itemRightBoldFont.setFontName("Trebuchet MS");
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setFont(itemRightBoldFont);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        styles.put(COL_RIGHT_STYLE_BOLD, style);

        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setFont(itemRightBoldFont);
        styles.put(COL_RIGHT_STYLE_NO_BORDER_STYLE, style);

        return styles;
    }

    /**
     * @param styleName
     * @return
     */
    protected CellStyle getCellStyle(final String styleName) {
        return styles.get(styleName);
    }

    protected Cell createCell(final Row row, final int iCol, final int value) {
        return createCell(row, iCol, String.valueOf(value));
    }

    protected Cell createCell(final Row row, final int iCol, final float value) {
        return createCell(row, iCol, String.valueOf(value));
    }

    /**
     * 
     * @param isBold
     * @param hasBorder
     * @param alignment
     * @return
     */
    private CellStyle createCellStyle(final boolean isBold, final boolean hasBorder, final short alignment) {
        final Font itemRightFont = wb.createFont();
        itemRightFont.setFontHeightInPoints((short) 9);
        itemRightFont.setFontName("Trebuchet MS");

        final CellStyle style = wb.createCellStyle();
        style.setAlignment(alignment);
        style.setFont(itemRightFont);
        if (isBold) {
            itemRightFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        }
        if (hasBorder) {
            style.setBorderTop(CellStyle.BORDER_THIN);
            style.setBorderLeft(CellStyle.BORDER_THIN);
            style.setBorderRight(CellStyle.BORDER_THIN);
            style.setBorderBottom(CellStyle.BORDER_THIN);
        }
        return style;
    }

    /**
     * 
     * @param row
     * @param iCol
     * @param priceKHR
     * @param priceUSD
     * @param hasBorder
     * @return
     */
    protected Cell createPriceCell(final Row row, final int iCol, final String priceKHR, final String priceUSD, final boolean hasBorder) {
        final Cell cell = row.createCell(iCol);
        Double value = null;
        /**
         * If we use style = getCellStyle(COL_RIGHT_STYLE) it's going to format to other cell that has value = null too
         */

        final DataFormat df = wb.createDataFormat();
        final CellStyle style = createCellStyle(false, hasBorder, CellStyle.ALIGN_RIGHT);
        if (priceKHR != null) {
            value = Double.parseDouble(priceKHR);
            style.setDataFormat(df.getFormat(FORMAT_KHR));
        }
        else if (priceUSD != null) {
            value = Double.parseDouble(priceUSD);
            style.setDataFormat(df.getFormat(FORMAT_USD));
        }

        cell.setCellValue(value == null ? 0 : value);
        cell.setCellStyle(style);
        return cell;
    }

    protected Cell createPriceCell(final Row row, final int iCol, final Double price, final boolean isUsd, final boolean hasBorder,
            final boolean isBold) {
        final Cell cell = row.createCell(iCol);
        final DataFormat df = wb.createDataFormat();

        //If we use style = getCellStyle(COL_RIGHT_STYLE) it's going to format to other cell that has value = null too.
        final CellStyle style = createCellStyle(isBold, hasBorder, CellStyle.ALIGN_RIGHT);
        if (isUsd) {
            style.setDataFormat(df.getFormat(FORMAT_USD));
        }
        else {
        	style.setDataFormat(df.getFormat(FORMAT_KHR));
        }

        cell.setCellValue(price == null ? 0 : price);
        cell.setCellStyle(style);
        return cell;
    }

    protected Cell createPriceCellColor(final Row row, final int iCol, final Double price, final boolean isUsd, final boolean hasBorder,
            final boolean isBold, final short bgColor) {
        final Cell cell = row.createCell(iCol);
        final DataFormat df = wb.createDataFormat();
        final CellStyle style = createCellStyle(isBold, hasBorder, CellStyle.ALIGN_RIGHT);
        style.setFillForegroundColor(bgColor);
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        if (isUsd) {
            style.setDataFormat(df.getFormat(FORMAT_USD));
        }
        else {
        	style.setDataFormat(df.getFormat(FORMAT_KHR));
        }

        cell.setCellValue(price == null ? 0 : price);
        cell.setCellStyle(style);
        return cell;
    }

    protected Cell createCellDate(final Row row, final int iCol, final Date value, final boolean hasBorder, final short alignment) {
        final Cell cell = row.createCell(iCol);
        final DataFormat df = wb.createDataFormat();
        // If we use style = getCellStyle(COL_RIGHT_STYLE) it's going to format to other cell that has value = null too.
        final CellStyle style = createCellStyle(false, hasBorder, alignment);
        style.setDataFormat(df.getFormat(FORMAT_DATE));
        if (value == null) {
            cell.setCellValue("");
        }
        else {
            cell.setCellValue(value);
        }

        cell.setCellStyle(style);
        return cell;
    }

    protected Cell createCellCode(final Row row, final int iCol, final String value) {
        final Cell cell = row.createCell(iCol);
        if (value != null && !value.isEmpty() && StringUtils.isNumeric(value)) {
            cell.setCellValue(Integer.parseInt(value));
        }
        else {
            cell.setCellValue(value == null ? "" : value);
        }
        cell.setCellStyle(getCellStyle(COL_LEFT_STYLE));
        return cell;
    }

    protected Cell createCellNumeric(final Row row, final int iCol, final Object value) {
        final Cell cell = row.createCell(iCol);
        if (value == null) {
            cell.setCellValue("");
        }
        else if (value instanceof Integer) {
            cell.setCellValue(Integer.valueOf(value.toString()));
        }
        else if (value instanceof Long) {
            cell.setCellValue(Long.valueOf(value.toString()));
        }
        else if (value instanceof String) {
            cell.setCellValue(value.toString());
        }
        cell.setCellStyle(getCellStyle(COL_LEFT_STYLE));
        return cell;
    }

    protected Cell createCellNumeric(final Row row, final int iCol, final Object value, final CellStyle style) {
        final Cell cell = row.createCell(iCol);
        if (value == null) {
            cell.setCellValue("");
        }
        else if (value instanceof Integer) {
            cell.setCellValue(Integer.valueOf(value.toString()));
        }
        else if (value instanceof Long) {
            cell.setCellValue(Long.valueOf(value.toString()));
        }
        else if (value instanceof String) {
            cell.setCellValue(value.toString());
        }
        cell.setCellStyle(style);
        return cell;
    }

    protected Cell createCell(final Row row, final int iCol, final String value) {
        final Cell cell = row.createCell(iCol);
        cell.setCellValue((value == null ? "" : value));
        cell.setCellStyle(getCellStyle(COL_LEFT_STYLE));
        return cell;
    }

    protected Cell createCell(final Row row, final int iCol, final String value, final CellStyle style) {
        final Cell cell = row.createCell(iCol);
        cell.setCellValue((value == null ? "" : value));
        cell.setCellStyle(style);
        return cell;
    }

    protected Cell createCellColor(final Row row, final int iCol, final String value, final short bgColor, final short fontColor) {
        final Cell cell = row.createCell(iCol);
        final Font itemRightFont = wb.createFont();
        itemRightFont.setFontHeightInPoints((short) 9);
        itemRightFont.setFontName("Trebuchet MS");
        itemRightFont.setColor(fontColor);

        final CellStyle style = wb.createCellStyle();
        style.setFont(itemRightFont);
        style.setFillForegroundColor(bgColor);
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderBottom(CellStyle.BORDER_THIN);

        cell.setCellValue((value == null ? "" : value));
        cell.setCellStyle(style);

        return cell;
    }

    protected Cell createPercentageCellColor(final Row row, final int iCol, final Double rate, final boolean isBold, final boolean hasBorder,
            final short alignment, final short bgColor) {
        final Cell cell = row.createCell(iCol);
        final DataFormat df = wb.createDataFormat();
        final CellStyle style = createCellStyle(isBold, hasBorder, alignment);
        style.setDataFormat(df.getFormat(FORMAT_PERCENTAGE));
        style.setFillForegroundColor(bgColor);
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);

        final Double percentage = rate / 100;
        cell.setCellValue(percentage == null ? 0 : percentage);
        cell.setCellStyle(style);

        return cell;
    }

    protected Cell createPercentageCell(final Row row, final int iCol, final Double rate, final boolean isBold, final boolean hasBorder,
            final short alignment) {
        final Cell cell = row.createCell(iCol);
        final DataFormat df = wb.createDataFormat();
        final CellStyle style = createCellStyle(isBold, hasBorder, alignment);
        style.setDataFormat(df.getFormat(FORMAT_PERCENTAGE));
        /*
         * After format as % it will multiple(*) by 100, so need to divide(/) by 100 in order to get the original value.
         * Ex: rate = 0.22% --> (0.22 * 100)/100 = 0.22 %
         */
        final Double percentage = rate / 100;
        cell.setCellValue(percentage == null ? 0 : percentage);
        cell.setCellStyle(style);
        return cell;
    }

    protected Cell createCell(final Row row, final int iCol, final String value, final boolean isBold, final boolean hasBorder) {
        final Cell cell = row.createCell(iCol);
        final CellStyle style = createCellStyle(isBold, hasBorder, CellStyle.ALIGN_RIGHT);
        cell.setCellStyle(style);
        cell.setCellValue((value == null ? "" : value));

        return cell;
    }

    protected Cell createDateCell(final Row row, final int iCol, final Date value) {
        final Cell cell = row.createCell(iCol);
        cell.setCellValue((value == null ? "" : DateUtils.getDateLabel(value)));
        cell.setCellStyle(getCellStyle(COL_LEFT_STYLE));
        return cell;
    }

    /**
     * @param reportName
     * @return
     * @throws IOException
     */
    protected String writeXLSData(final String fileName) throws IOException {
        final FileOutputStream out = new FileOutputStream(AppConfig.getInstance().getConfiguration().getString("specific.tmpdir") + "/" + fileName);
        wb.write(out);
        out.close();
        return fileName;
    }
}
