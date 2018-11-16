package com.nokor.efinance.core.quotation.panel;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.exception.DaoException;

import com.nokor.efinance.core.quotation.QuotationService;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;

/**
 * Underwriter panel
 * @author ly.youhort
 */
public class HistoryStatusPanel extends AbstractTabPanel implements QuotationEntityField {

	private static final long serialVersionUID = -2123869961361097031L;

	private QuotationService quotationService = (QuotationService) SecApplicationContextHolder.getContext().getBean("quotationService");
		
	private List<ColumnDefinition> columnDefinitions;
	// TODO PYT
//	private SimplePagedTable<QuotationStatusHistory>  quotationStatusPagedTable;
		
	public HistoryStatusPanel() {
		super();
		setSizeFull();
	}
	
	
	@Override
	protected com.vaadin.ui.Component createForm() {
		VerticalLayout contentLayout = new VerticalLayout();		
		//contentLayout.setMargin(true);
		
		this.columnDefinitions = createColumnDefinitions();
		// TODO PYT
//		quotationStatusPagedTable = new SimplePagedTable<QuotationStatusHistory>(this.columnDefinitions);
//		contentLayout.addComponent(quotationStatusPagedTable);
//		contentLayout.addComponent(quotationStatusPagedTable.createControls());
		
		return contentLayout;
	}
	
	/**
	 * Set quotation
	 * @param quotation
	 */
	public void assignValues(Quotation quotation) {
		// TODO PYT
//		quotationStatusPagedTable.setContainerDataSource(getIndexedContainer(quotation));
	}
		
	
	/**
	 * Get indexed container
	 * @return
	 */
	@SuppressWarnings({ "unchecked"})
	private IndexedContainer getIndexedContainer(Quotation quotation) {
		IndexedContainer indexedContainer = new IndexedContainer();
		try {
			
			// TODO PYT
//			List<QuotationStatusHistory> quotationStatusHistories = quotationService.getWkfStatusHistories(quotation.getId(), Order.desc("updateDate"));
//			
//			for (ColumnDefinition column : this.columnDefinitions) {
//				indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
//			}
//
//			if(quotationStatusHistories != null && !quotationStatusHistories.isEmpty()){
//				long nbOfDate = 0;
//				long processingTime = 0;
//				long processingTimeByOneDay = 0;
//				long processingTimeEnd = 0;
//				long processingTimeStart = 0;
//				Date processingTimeDate;
//				for (int i = 0 ; i < quotationStatusHistories.size();i++){
//					if ((i + 1) == quotationStatusHistories.size()){
//						processingTimeDate = quotationStatusHistories.get(i).getQuotation().getStartCreationDate();
//					} else {
//						processingTimeDate = quotationStatusHistories.get(i + 1).getUpdateDate();
//					}
//					//Find the number of day from start to end 
//					nbOfDate = DateUtils.getDiffInDays(DateUtils.getDateAtBeginningOfDay(quotationStatusHistories.get(i).getUpdateDate()),DateUtils.getDateAtBeginningOfDay(processingTimeDate));
//					if(nbOfDate > 0){
//						for (int j = 0; j <= nbOfDate; j++) {
//							//Increase one day by loop
//							Date quotationStatusHistoryNextDate = DateUtils.addDaysDate(processingTimeDate, j);
//							if (j == 0) {
//								//Date before 17:30
//								if(quotationStatusHistoryNextDate.before(DateFilterUtil.getWorkingEndDate(quotationStatusHistoryNextDate))){
//									processingTimeByOneDay  = DateFilterUtil.getWorkingEndDate(quotationStatusHistoryNextDate).getTime() - quotationStatusHistoryNextDate.getTime();
//								}
//							}else if (j == nbOfDate){
//								//Date after 8:30
//								if(quotationStatusHistories.get(i).getUpdateDate().after(DateFilterUtil.getWorkingStartDate(quotationStatusHistoryNextDate))){
//									processingTimeByOneDay = processingTimeByOneDay + (quotationStatusHistories.get(i).getUpdateDate().getTime() - DateFilterUtil.getWorkingStartDate(quotationStatusHistoryNextDate).getTime());
//								}
//							}else{
//								processingTimeByOneDay = processingTimeByOneDay + (DateFilterUtil.getWorkingEndDate(quotationStatusHistoryNextDate).getTime() - DateFilterUtil.getWorkingStartDate(quotationStatusHistoryNextDate).getTime());
//							}
//						}
//						processingTime = processingTimeByOneDay;
//					}else{
//						if(processingTimeDate.after(DateFilterUtil.getWorkingStartDate(processingTimeDate))){
//							processingTimeStart = processingTimeDate.getTime();
//						}else{
//							processingTimeStart = DateFilterUtil.getWorkingStartDate(processingTimeDate).getTime();
//						}
//						
//						if(quotationStatusHistories.get(i).getUpdateDate().before(DateFilterUtil.getWorkingEndDate(quotationStatusHistories.get(i).getUpdateDate()))){
//							processingTimeEnd =  quotationStatusHistories.get(i).getUpdateDate().getTime();
//						}else{
//							processingTimeEnd = DateFilterUtil.getWorkingEndDate(quotationStatusHistories.get(i).getUpdateDate()).getTime();
//						}
//						processingTime = processingTimeEnd - processingTimeStart;
//					}
//					Item item = indexedContainer.addItem(quotationStatusHistories.get(i).getId());
//					item.getItemProperty("previousQuotationStatus.desc").setValue(quotationStatusHistories.get(i).getPreviousWkfStatus().getDesc());
//					item.getItemProperty("quotationStatus.desc").setValue(quotationStatusHistories.get(i).getWkfStatus().getDesc());
//					item.getItemProperty("updateDate").setValue(DateUtils.formatDate(quotationStatusHistories.get(i).getUpdateDate(), DateUtils.FORMAT_YYYYMMDD_HHMMSS_SLASH));
//					item.getItemProperty("user.desc").setValue(quotationStatusHistories.get(i).getUser().getDesc());
//					item.getItemProperty("profile").setValue(quotationStatusHistories.get(i).getUser().getDefaultProfile().getDescEn());
//					item.getItemProperty("processing.time").setValue(getTime(processingTime));
//				}
//			}
						
		} catch (DaoException e) {
			Notification.show(e.getMessage(), Type.ERROR_MESSAGE);
		}
		return indexedContainer;
	}
	
	/**
	 * @param millis
	 * @return
	 */
	private String getTime(Long millis) {
		if (millis != null) {
			String s = "" + (millis / 1000) % 60;
			String m = "" + (millis / (1000 * 60)) % 60;
			String h = "" + (millis / (1000 * 60 * 60)) % 24;
			String d = "" + (millis / (1000 * 60 * 60 * 24));
			return d + "d " + h + "h:" + m + "m:" + s + "s";
		}
		return "N/A";
	}
	
	/**
	 * Get Paged definition
	 * @return
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("previousQuotationStatus.desc", I18N.message("from"), String.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition("quotationStatus.desc", I18N.message("to"), String.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition("updateDate", I18N.message("date"), String.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition("user.desc", I18N.message("create.user"), String.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition("profile", I18N.message("profile"), String.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition("processing.time", I18N.message("processing.time"), String.class, Align.LEFT, 140));
		return columnDefinitions;
	}
	
}
