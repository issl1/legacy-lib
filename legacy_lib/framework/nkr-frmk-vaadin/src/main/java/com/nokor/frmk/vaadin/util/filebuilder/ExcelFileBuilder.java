package com.nokor.frmk.vaadin.util.filebuilder;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.amount.Amount;

import com.vaadin.data.Container;

/**
 * @author ky.nora
 */
public class ExcelFileBuilder extends FileBuilder {
    
	/**	 */
	private static final long serialVersionUID = -5351470393412965377L;
	
	public static String DEFAULT_DATE_FORMAT = DateUtils.FORMAT_YYYYMMDD_HHMMSS_SLASH;
    protected Workbook workbook;
    protected Sheet sheet;
    private int rowNr;
    private int colNr;
    private Row row;
    protected Cell cell;
    private CellStyle dateCellStyle;
    private CellStyle boldStyle;
    private Map<Integer, CellStyle> amountCellStyles;
    private CellStyle numberCellStyle;

    public ExcelFileBuilder(Container container) {
        super(container);
        setDateFormat(DEFAULT_DATE_FORMAT);
    }

    public void setDateCellStyle(String style) {
        CreationHelper createHelper = workbook.getCreationHelper();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat(
                style));
    }

    @Override
    public String getFileExtension() {
        return ".xls";
    }

    @Override
    protected void writeToFile() {
        try {
            workbook.write(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewRow() {
        row = sheet.createRow(rowNr);
        rowNr++;
        colNr = 0;
    }

    @Override
    protected void onNewCell() {
        cell = row.createCell(colNr);
        colNr++;
    }

    @Override
    protected void buildCell(Object value) {
        if (value == null) {
            cell.setCellType(Cell.CELL_TYPE_BLANK);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
            cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
            cell.setCellStyle(getDateCellStyle());
            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        } else if (value instanceof Calendar) {
        	Calendar calendar = (Calendar) value;
        	cell.setCellValue(calendar.getTime());
            cell.setCellType(Cell.CELL_TYPE_STRING);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
            cell.setCellStyle(getNumberCellStyle());
            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        } else if (value instanceof Integer) {
        	cell.setCellValue((Integer) value);
        	cell.setCellStyle(getNumberCellStyle());
        	cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        } else if (value instanceof Amount) {
        	Amount amountValue = (Amount) value;
        	if (amountValue != null && amountValue.getTiAmount() != null) {
        		cell.setCellValue(amountValue.getTiAmount());
        		cell.setCellStyle(getAmountCellStyle(amountValue.getNbDecimal()));
        	}
            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        } else {
            cell.setCellValue(value.toString());
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }
    }

    @Override
    protected void buildColumnHeaderCell(String header) {
        buildCell(header);
        cell.setCellStyle(getBoldStyle());
    }

    public CellStyle getDateCellStyle() {
        if (dateCellStyle == null) {
            CreationHelper createHelper = workbook.getCreationHelper();
            dateCellStyle = workbook.createCellStyle();
            dateCellStyle.setDataFormat(createHelper.createDataFormat()
                    .getFormat(getDateFormatString()));
        }
        return dateCellStyle;
    }

    private CellStyle getBoldStyle() {
        if (boldStyle == null) {
            Font bold = workbook.createFont();
            bold.setBoldweight(Font.BOLDWEIGHT_BOLD);

            boldStyle = workbook.createCellStyle();
            boldStyle.setFont(bold);
        }
        return boldStyle;
    }
    
    public CellStyle getAmountCellStyle(int nbDecimal) {
    	CellStyle amountCellStyle = amountCellStyles.get(nbDecimal);
    	if (amountCellStyle == null) {
    		String strFormat = "##0.";
    		for (int i = 0; i < nbDecimal; ++i) {
    			strFormat = strFormat + "0";
    		}
			amountCellStyle = workbook.createCellStyle();
			amountCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
	        DataFormat format = workbook.createDataFormat();
	        amountCellStyle.setDataFormat(format.getFormat(strFormat));
	        amountCellStyles.put(nbDecimal, amountCellStyle);
		}
        return amountCellStyle;
    }
    
    public CellStyle getNumberCellStyle() {
    	if (numberCellStyle == null) {
    		DataFormat format = workbook.createDataFormat();
    		numberCellStyle = workbook.createCellStyle();
    		numberCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
    		numberCellStyle.setDataFormat(format.getFormat("#,##0"));
    	}
    	return numberCellStyle;
    }
    
    @Override
    protected void buildHeader() {
        if (getHeader() == null) {
            return;
        }
        onNewRow();
        onNewCell();
        cell.setCellValue(getHeader());

        Font headerFont = workbook.createFont();
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        headerFont.setFontHeightInPoints((short) 15);
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cell.setCellStyle(headerStyle);

        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0,
                getNumberofColumns()));
        onNewRow();
    }
    
    @Override
    protected void buildDescription() {
        if (getDescriptions().isEmpty()) {
            return;
        }
        for (int i = 0; i < getDescriptions().size(); i++) {
	        onNewRow();
	        onNewCell();
	        cell.setCellValue(getDescriptions().get(i));
	        sheet.addMergedRegion(new CellRangeAddress(2 + i, 2 + i, 0,
	                getNumberofColumns()));
        }
        onNewRow();
    }

    @Override
    protected void buildFooter() {
        for (int i = 0; i < getNumberofColumns(); i++) {
            sheet.autoSizeColumn(i);
        }
    }

    @Override
    protected void resetContent() {
        workbook = new HSSFWorkbook();
        sheet = workbook.createSheet();
        colNr = 0;
        rowNr = 0;
        row = null;
        cell = null;
        dateCellStyle = null;
        boldStyle = null;
        amountCellStyles = new HashMap<Integer, CellStyle>();
    }
}
