package com.nokor.efinance.gui.ui.panel.report.uwperformance;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

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

import com.nokor.frmk.vaadin.util.filebuilder.FileBuilder;
import com.vaadin.data.Container;

/**
 * @author ky.nora
 */
@SuppressWarnings("serial")
public class UWExcelFileBuilder extends FileBuilder {
    
	public static String DEFAULT_DATE_FORMAT = DateUtils.FORMAT_YYYYMMDD_HHMMSS_SLASH;
    private Workbook workbook;
    private Sheet sheet;
    private int rowNr;
    private int colNr;
    private Row row;
    private Cell cell;
    private CellStyle dateCellStyle;
    private CellStyle boldStyle;
    private CellStyle amountCellStyle;
    private CellStyle numericCellStyle;
    private CellStyle intCellStyle;
    private DataFormat dataFormat = null;
    
    
    public UWExcelFileBuilder(Container container) {
        super(container);
    }

    public void setNumericCellStyle(String style){
    	if(numericCellStyle == null)
    		numericCellStyle = workbook.createCellStyle();
    	if(dataFormat == null)
    		dataFormat = workbook.createDataFormat();
    	numericCellStyle.setDataFormat(dataFormat.getFormat(style));
    	
    	if(cell != null)
    		cell.setCellStyle(numericCellStyle);
    }
    
    public void setIntCellStyle(String style){
    	if(intCellStyle == null)
    		intCellStyle = workbook.createCellStyle();
    	if(dataFormat == null)
    		dataFormat = workbook.createDataFormat();
    	intCellStyle.setDataFormat(dataFormat.getFormat(style));
    	
    	if(cell != null)
    		cell.setCellStyle(intCellStyle);
    }
    
    public void setDateCellStyle(String style) {
        if(dateCellStyle == null)
        	dateCellStyle = workbook.createCellStyle();
        
    	CreationHelper createHelper = workbook.getCreationHelper();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat(style));
        if(cell != null)
        	cell.setCellStyle(dateCellStyle);
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
            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        	// cell.setCellValue(formatDate((Date) value));
        	// cell.setCellType(Cell.CELL_TYPE_STRING);
        	//DataFormat format = workbook.createDataFormat();
            //cell.getCellStyle().setDataFormat(format.getFormat(DEFAULT_DATE_FORMAT));
        	//CellStyle dateStyle = workbook.createCellStyle();
        	//cell.setCellStyle(dateStyle);        	
        	setDateCellStyle(DEFAULT_DATE_FORMAT);
        } else if (value instanceof Calendar) {
        	Calendar calendar = (Calendar) value;
        	cell.setCellValue(calendar.getTime());
            cell.setCellType(Cell.CELL_TYPE_STRING);
        } else if (value instanceof Double) {            
        	cell.setCellValue((Double) value);
            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        	//DataFormat fmt = workbook.createDataFormat();
            //CellStyle doubleStyle = workbook.createCellStyle();
            //doubleStyle.setDataFormat(fmt.getFormat("###0.00"));
            //cell.setCellStyle(doubleStyle);            
            setNumericCellStyle("###0.00");
        }else if (value instanceof Float) {            
        	cell.setCellValue((Float) value);
            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        	//DataFormat fmt = workbook.createDataFormat();
            //CellStyle floatStyle = workbook.createCellStyle();
            //floatStyle.setDataFormat(fmt.getFormat("###0.00"));
            //cell.setCellStyle(floatStyle);            
            setNumericCellStyle("###0.00");
        }else if (value instanceof Integer) {            
        	cell.setCellValue((Integer) value);
            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        	//DataFormat fmt = workbook.createDataFormat();
            //CellStyle intStyle = workbook.createCellStyle();
            //intStyle.setDataFormat(fmt.getFormat("00"));
            //cell.setCellStyle(intStyle);            
        	setIntCellStyle("00");
        } else if (value instanceof Amount) {
        	Amount amountValue = (Amount) value;
        	if (amountValue != null && amountValue.getTiAmount() != null) {
        		cell.setCellValue(amountValue.getTiAmount());
        	}
            cell.setCellStyle(getAmountCellStyle());
            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
            DataFormat fmt = workbook.createDataFormat();
            cell.getCellStyle().setDataFormat(fmt.getFormat("###0.000"));
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
    
    public CellStyle getAmountCellStyle() {
        if (amountCellStyle == null) {
        	DataFormat format = workbook.createDataFormat();
            amountCellStyle = workbook.createCellStyle();
            amountCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
            amountCellStyle.setDataFormat(format.getFormat("#,##0.00"));
        }
        return amountCellStyle;
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
                getNumberofColumns() - 1));
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
        amountCellStyle = null;
        numericCellStyle = null;
        intCellStyle = null;
    }
}
