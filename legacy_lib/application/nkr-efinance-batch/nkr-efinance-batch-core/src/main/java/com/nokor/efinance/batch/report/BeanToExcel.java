package com.nokor.efinance.batch.report;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFCellUtil;
import org.apache.poi.ss.util.CellUtil;

/**
 * @author ky.nora
 *
 */
public class BeanToExcel {
    private HSSFWorkbook workbook;
    private HSSFFont boldFont;
    private HSSFDataFormat format;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    
    /**
     * 
     */
    public BeanToExcel() {
        workbook = new HSSFWorkbook();
        boldFont = workbook.createFont();
        boldFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        format = workbook.createDataFormat();
    }
 
    /**
     * @param data
     * @param columns
     * @param sheetName
     */
    public void addSheet(List<?> data, ReportColumn[] columns, String sheetName) {
 
        HSSFSheet sheet = workbook.createSheet(sheetName);
        int numCols = columns.length;
        int currentRow = 0;
        HSSFRow row;
 
        try {
 
            // Create the report header at row 0
            row = sheet.createRow(currentRow);
            for (int i = 0; i < numCols; i++) {
                writeCell(row, i, columns[i].getHeader(), FormatType.TEXT,
                        null, this.boldFont);
            }
 
            currentRow++;
 
            // Write report rows
            for (int i = 0; i < data.size(); i++) {
                // create a row in the spreadsheet
                row = sheet.createRow(currentRow++);
                // get the bean for the current row
                Object bean = data.get(i);
                // For each column object, create a column on the current row
                for (int y = 0; y < numCols; y++) {
                    Object value = PropertyUtils.getProperty(bean,
                            columns[y].getMethod());
                    writeCell(row, y, value, columns[y].getType(),
                            columns[y].getColor(), columns[y].getFont());
                }
            }
 
            for (int i = 0; i < numCols; i++) {
                sheet.autoSizeColumn((short) i);
            }
 
        } catch (Exception e) {
            System.err.println("Caught Generate Error exception: "
                    + e.getMessage());
        }
 
    }
 
    /**
     * @return
     */
    public HSSFFont boldFont() {
        return boldFont;
    }
 
    /**
     * @param outputStream
     * @throws Exception
     */
    public void write(OutputStream outputStream) throws Exception {
        workbook.write(outputStream);
    }
 
    /**
     * @param row
     * @param col
     * @param value
     * @param formatType
     * @param bgColor
     * @param font
     * @throws Exception
     */
    private void writeCell(HSSFRow row, int col, Object value,
            FormatType formatType, Short bgColor, HSSFFont font)
            throws Exception {
 
        HSSFCell cell = HSSFCellUtil.createCell(row, col, null);
 
        if (value == null) {
            return;
        }
 
        if (font != null) {
            HSSFCellStyle style = workbook.createCellStyle();
            style.setFont(font);
            cell.setCellStyle(style);
        }
 
        switch (formatType) {
        case TEXT:
            cell.setCellValue(value.toString());
            break;
        case NUMBER:
            cell.setCellValue(((Number) value).intValue());
            HSSFCellUtil.setCellStyleProperty(cell, workbook,
                    CellUtil.DATA_FORMAT,
                    HSSFDataFormat.getBuiltinFormat(("#,##0")));
            break;
        case AMOUNT:
            cell.setCellValue(((Number) value).doubleValue());
            HSSFCellUtil.setCellStyleProperty(cell, workbook,
                    CellUtil.DATA_FORMAT,
                    HSSFDataFormat.getBuiltinFormat(("#,##0.00")));
            break;
        case DATE:
            cell.setCellValue(dateFormat.format((Date) value));
            break;
        case PERCENTAGE:
            cell.setCellValue(((Number) value).doubleValue());
            HSSFCellUtil.setCellStyleProperty(cell, workbook,
                    CellUtil.DATA_FORMAT,
                    HSSFDataFormat.getBuiltinFormat("#,##0.00%"));
        }
 
        if (bgColor != null) {
            HSSFCellUtil.setCellStyleProperty(cell, workbook,
                    CellUtil.FILL_FOREGROUND_COLOR, bgColor);
            HSSFCellUtil.setCellStyleProperty(cell, workbook,
                    CellUtil.FILL_PATTERN, HSSFCellStyle.SOLID_FOREGROUND);
        }
 
    }
 
	/**
	 * @param dateFormat
	 */
	public void setDateFormat(String dateFormat) {
		this.dateFormat = new SimpleDateFormat(dateFormat);
	}

	public enum FormatType {
        TEXT, NUMBER, AMOUNT, DATE, PERCENTAGE
    }
}