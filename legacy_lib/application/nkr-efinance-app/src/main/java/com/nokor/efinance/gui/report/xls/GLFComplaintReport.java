package com.nokor.efinance.gui.report.xls;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.quotation.QuotationService;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.efinance.glf.statistic.model.EComplaintType;
import com.nokor.efinance.glf.statistic.model.StatisticVisitor;
import com.nokor.efinance.tools.report.Report;
import com.nokor.efinance.tools.report.XLSAbstractReportExtractor;
import com.nokor.frmk.security.context.SecApplicationContextHolder;

/**
 * @author buntha.chea
 * GLFComplaintReport for generate report excel about complaint from each dealer 
 */
public class GLFComplaintReport extends XLSAbstractReportExtractor implements Report, GLFApplicantFields, FMEntityField {

	protected EntityService entityService = (EntityService) SecApplicationContextHolder.getContext().getBean("entityService");
	protected QuotationService quotationService = (QuotationService) SecApplicationContextHolder.getContext().getBean("quotationService");

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
	static short BG_AQUA = IndexedColors.AQUA.getIndex();

	/** Font color */
	static short FC_WHITE = IndexedColors.WHITE.getIndex();
	static short FC_BLACK = IndexedColors.BLACK.getIndex();
	static short FC_BLUE = 48;
	static short FC_GREY = IndexedColors.GREY_80_PERCENT.getIndex();
	static short FC_GREEN = IndexedColors.GREEN.getIndex();

	/** */
	public GLFComplaintReport() {

	}

	/**
	 * for generate excel report
	 * @see com.nokor.efinance.tools.report.Report#generate(com.nokor.efinance.core.shared.report.ReportParameter)
	 */
	@Override
	public String generate(ReportParameter reportParameter) throws Exception {	
		Map<String, Object> parameters = reportParameter.getParameters();
		Date startDate = (Date) parameters.get("startDate");
		Date endDate = (Date) parameters.get("endDate");
		Dealer dealer = (Dealer) parameters.get("dealer");
		EDealerType dealerType = (EDealerType) parameters.get("dealer.type");
		
		List<EComplaintType> complaints	= EComplaintType.values();
		
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
		sheet.setColumnWidth(0, 12000);
		if (complaints != null && !complaints.isEmpty()) {
			for (int i = 1; i < complaints.size()+3; i++) {
				sheet.setColumnWidth(i, 9000);
			}
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
		int iRow = 0;
		int iCol = 0;
		iCol = iCol + 2;
		iCol = 0;
		iRow = iRow + 3;
		iRow = dataTableByDealer(sheet, iRow, style, complaints, startDate, endDate, dealerType, dealer, I18N.message("complaint.reports")); // Vistor Company
		String fileName = writeXLSData("Complaint_Report" + DateUtils.getDateLabel(DateUtils.today(), "yyyyMMddHHmmssSSS") + ".xlsx");
		return fileName;
	}

	/**
	 * @param sheet
	 * @param iRow
	 * @param startDate
	 * @param endDate
	 * @param dealer
	 * @param title
	 * @param complaints
	 * @return
	 * @throws Exception
	 */

	private int dataTableByDealer(final Sheet sheet, int iRow, final CellStyle style, List<EComplaintType> complaints, Date startDate, Date endDate, EDealerType dealerType, Dealer dealer, String title) throws Exception {

		int iCol = 0;
			
		Row titleRow =  sheet.createRow(0);
		sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, complaints.size()));
		createCell(titleRow, 0,title , 18, true, true, false, CellStyle.ALIGN_CENTER, true, BG_AQUA, FC_BLACK,false);
		
		

		Row tmpRow = sheet.createRow(iRow++);
		createCell(tmpRow, iCol++, "Dealer", 14, false, true, false,
				CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, false);
		
		if (complaints != null && !complaints.isEmpty()) {
			for (int i = 0; i < complaints.size(); i++) {
				createCell(tmpRow, iCol++, complaints.get(i).getDescEn(), 14, false, true, false,
						CellStyle.ALIGN_CENTER, true, BG_GREY, FC_BLACK, true);
			}
		}
				List<StatisticVisitor> statisticVisitors = null;
				//Query data from StatisticVisiors model with some condition 
				BaseRestrictions<StatisticVisitor> restrictions = new BaseRestrictions<StatisticVisitor>(StatisticVisitor.class);
				restrictions.addAssociation("dealer", "dea", JoinType.INNER_JOIN);
				if(startDate !=null){
					restrictions.addCriterion(Restrictions.ge("createDate", DateUtils.getDateAtBeginningOfDay(startDate)));
				}
				if(endDate !=null){
					restrictions.addCriterion(Restrictions.le("createDate", DateUtils.getDateAtEndOfDay(endDate)));
				}
				if(dealer !=null){
					restrictions.addCriterion(Restrictions.eq(DEALER +"."+ ID, dealer.getId()));
				}
				if (dealerType != null) {
					restrictions.addCriterion(Restrictions.eq("dea.dealerType", dealerType));
				}
				
				statisticVisitors = entityService.list(restrictions);
				
				EntityService entityService = SpringUtils.getBean(EntityService.class);
				List<Dealer> dealers = new ArrayList<>();
				BaseRestrictions<Dealer> dealerRestrictions = getDealerRestriction();
				if (dealerType != null) {
					dealerRestrictions.addCriterion(Restrictions.eq("dealerType", dealerType));
				}
				if (dealer != null) {
					dealers.add(dealer);
				} else {
					dealers = entityService.list(dealerRestrictions);
				}
				for (Dealer d : dealers) {
					tmpRow = sheet.createRow(iRow++);
					iCol = 0;
					createCell(tmpRow, iCol++, d.getNameEn(), 12, false, true, false,
							CellStyle.ALIGN_LEFT, true, BG_WHITE, FC_BLACK, false);
					if (complaints != null && !complaints.isEmpty()) {
						for (EComplaintType complaint : complaints) {
							//insert number of complaint to each field in excel
							createNumericCell(tmpRow, iCol++, getStatisticVisitorByDealer(statisticVisitors, complaint.getId(),d), false,
									CellStyle.ALIGN_CENTER, 14, false, true, BG_WHITE, FC_BLACK);							
						}
					}
				}
				tmpRow = sheet.createRow(iRow++);
		return iRow;
	}
	
	/**
	 * @return
	 */
	private BaseRestrictions<Dealer> getDealerRestriction () {
		BaseRestrictions<Dealer> restrictions = new BaseRestrictions<Dealer>(Dealer.class);
		restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
		return restrictions;
	}
	
	/**
	 * @param statisticVisitors
	 * @param dealer
	 * @return
	 */
	private int getStatisticVisitorByDealer(List<StatisticVisitor> statisticVisitors, long complaintId, Dealer dealer) {
		int numComplaint = 0;
		try{
			if (statisticVisitors != null && !statisticVisitors.isEmpty()) {
				for (StatisticVisitor statisticVisitor : statisticVisitors) {
					if (statisticVisitor.getComplaint() != null 
							&& statisticVisitor.getComplaint().getId() == complaintId
							&& statisticVisitor.getDealer().getId().equals(dealer.getId())) {
							//count number of complaint by for loop
							numComplaint += statisticVisitor.getNumberVisitorCompany() == null ? 0 : statisticVisitor.getNumberVisitorCompany();
						
						}						
					}
				}
			
		}catch(Exception e){
			e.getStackTrace();
		}
		return numComplaint;
	}
	
	protected Cell createCell(final Row row, final int iCol, final String value, final CellStyle style) {
		final Cell cell = row.createCell(iCol);
		cell.setCellValue((value == null ? "" : value));
		cell.setCellStyle(style);
		return cell;
	}

	protected Cell createCell(final Row row, final int iCol,
			final String value, final int fontsize, final boolean isBold,
			final boolean hasBorder, final boolean leftRight, final short alignment,
			final boolean setBgColor, final short bgColor, final short fonCorlor, boolean wrapText) {
	
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
			style.setBorderTop(CellStyle.BORDER_THIN);
			style.setBorderLeft(CellStyle.BORDER_THIN);
			style.setBorderRight(CellStyle.BORDER_THIN);
			style.setBorderBottom(CellStyle.BORDER_THIN);
			style.setBottomBorderColor(FC_BLACK);
		}
		if (leftRight) {
			style.setBorderLeft(CellStyle.BORDER_THIN);
			style.setBorderRight(CellStyle.BORDER_THIN);
		}
		if (setBgColor) {
			style.setFillForegroundColor(bgColor);
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		}
		if (wrapText) {
			style.setWrapText(wrapText);
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
		
		cell.setCellStyle(styles.get("ALIGN_CENTER"));
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
		}
		if (value instanceof Double) {
            cell.setCellValue((Double) value);
            cell.setCellStyle(styles.get(AMOUNT));
        }
		return cell;
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
    	//style.setAlignment(CellStyle.ALIGN_RIGHT);
    	style.setFont(itemFont);
    	style.setDataFormat(format.getFormat("#,##0.00"));
    	styles.put(AMOUNT, style);
    	
    	style = wb.createCellStyle();
		itemFont.setFontHeightInPoints((short) 12);
		itemFont.setFontName("Khmer OS Battambang");
		style = wb.createCellStyle();
		style.setFont(itemFont);
    	style.setAlignment(CellStyle.ALIGN_CENTER);
    	styles.put("ALIGN_CENTER", style);

		return styles;
	}

	protected CellStyle getCellStyle(final String styleName) {
		return styles.get(styleName);
	}
}

