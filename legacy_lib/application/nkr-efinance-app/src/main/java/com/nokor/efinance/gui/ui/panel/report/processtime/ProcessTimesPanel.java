package com.nokor.efinance.gui.ui.panel.report.processtime;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.quotation.ProcessTimeService;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationStatusHistory;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;

import ru.xpoft.vaadin.VaadinView;

/**
 * @author buntha.chea
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(ProcessTimesPanel.NAME)
public class ProcessTimesPanel extends AbstractTabPanel implements View, CashflowEntityField, FrmkServicesHelper {
	
	private static final long serialVersionUID = -4618786633559261506L;

	public static final String NAME = "process.times";
	
	private final ProcessTimeService processTimeService = SpringUtils.getBean(ProcessTimeService.class);
		
	private TabSheet tabSheet;
	// TODO PYI
	private SimpleTable<?> pagedTable;// QuotationStatusHistory
	private List<ColumnDefinition> columnDefinitions;
	
	private ERefDataComboBox<EDealerType> cbxDealerType;
	private DealerComboBox cbxDealer;
	private ERefDataComboBox<EWkfStatus> cbxQuotationStatusFrom;
	private ERefDataComboBox<EWkfStatus> cbxQuotationStatusTo;	
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	private ValueChangeListener valueChangeListener;
	
	public ProcessTimesPanel() {
		super();
		setSizeFull();
	}
	
	@Override
	protected com.vaadin.ui.Component createForm() {
		tabSheet = new TabSheet();
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.setMargin(true);
		
		VerticalLayout searchLayout = new VerticalLayout();
		HorizontalLayout buttonsLayout = new HorizontalLayout();
		Button btnSearch = new Button(I18N.message("search"));
		btnSearch.setClickShortcut(KeyCode.ENTER, null); // null it means we don't modify key of shortcut Enter(default = 13)
		btnSearch.setIcon(new ThemeResource("../nkr-default/icons/16/search.png"));
		btnSearch.addClickListener(new ClickListener() {		
			private static final long serialVersionUID = -3403059921454308342L;
			@Override
			public void buttonClick(ClickEvent event) {
				search();
			}
		});
		
		Button btnReset = new Button(I18N.message("reset"));
		btnReset.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -7165734546798826698L;
			@Override
			public void buttonClick(ClickEvent event) {
				reset();
			}
		});
		buttonsLayout.setSpacing(true);
		buttonsLayout.addComponent(btnSearch);
		buttonsLayout.addComponent(btnReset);
		
		final GridLayout gridLayout = new GridLayout(12, 3);
		gridLayout.setSpacing(true);

		cbxDealer = new DealerComboBox(ENTITY_SRV.list(getDealerRestriction()));
		cbxDealer.setWidth("220px");
		dfStartDate = ComponentFactory.getAutoDateField(I18N.message("startdate"), false);

		List<EDealerType> dealerTypes = EDealerType.values();
		cbxDealerType = new ERefDataComboBox<EDealerType>(dealerTypes);
		cbxDealerType.setImmediate(true);
		cbxDealerType.setWidth("220px");
		valueChangeListener = new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = -581526660007092292L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				BaseRestrictions<Dealer> restrictions = getDealerRestriction();
				if (cbxDealerType.getSelectedEntity() != null) {
					restrictions.addCriterion(Restrictions.eq("dealerType", cbxDealerType.getSelectedEntity()));
				}
				cbxDealer.setDealers(ENTITY_SRV.list(restrictions));
				cbxDealer.setSelectedEntity(null);
			}
		};
		cbxDealerType.addValueChangeListener(valueChangeListener);
		
		dfStartDate = ComponentFactory.getAutoDateField("", false);
		dfStartDate.setValue(DateUtils.today());
		dfStartDate.setWidth("95px");
		
		dfEndDate = ComponentFactory.getAutoDateField("", false);      
		dfEndDate.setValue(DateUtils.today());
		dfEndDate.setWidth("95px");
		
		cbxQuotationStatusFrom =  new ERefDataComboBox<EWkfStatus>(QuotationWkfStatus.values());
		cbxQuotationStatusTo = new ERefDataComboBox<EWkfStatus>(QuotationWkfStatus.values());
		
		int iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("dealer.type")), iCol++, 0);
        gridLayout.addComponent(cbxDealerType, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("enddate")),iCol++,0);
        gridLayout.addComponent(dfStartDate, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("startdate")),iCol++,0);
        gridLayout.addComponent(dfEndDate, iCol++, 0);
        
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("dealer")), iCol++, 1);
        gridLayout.addComponent(cbxDealer, iCol++, 1);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 1);
        gridLayout.addComponent(new Label(I18N.message("status.from")), iCol++, 1);
        gridLayout.addComponent(cbxQuotationStatusFrom, iCol++, 1);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 1);
        gridLayout.addComponent(new Label(I18N.message("status.to")), iCol++, 1);
        gridLayout.addComponent(cbxQuotationStatusTo, iCol++, 1);
		
        searchLayout.setMargin(true);
        searchLayout.setSpacing(true);
        searchLayout.addComponent(gridLayout);
        searchLayout.addComponent(buttonsLayout);
        
        Panel searchPanel = new Panel();
        searchPanel.setCaption(I18N.message("search"));
        searchPanel.setContent(searchLayout);
        
        this.columnDefinitions = createColumnDefinitions();
		// TODO PYI
//        pagedTable = new SimpleTable<>(this.columnDefinitions);
        contentLayout.addComponent(searchPanel);
        contentLayout.addComponent(pagedTable);
        
        tabSheet.addTab(contentLayout, I18N.message("process.time.report"));
        return tabSheet;
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
	 * @return BaseRestrictions
	 */
	public BaseRestrictions<Quotation> getRestrictions() {
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(Quotation.class);
		restrictions.addCriterion(Restrictions.eq("quotationStatus", QuotationWkfStatus.ACT));
		restrictions.addAssociation("dealer", "dea", JoinType.INNER_JOIN);
		if (dfStartDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.ge("contractStartDate", DateUtils.getDateAtBeginningOfDay(dfStartDate.getValue())));
		}
		
		if (dfEndDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.le("contractStartDate", DateUtils.getDateAtEndOfDay(dfEndDate.getValue())));
		}
		
		if (cbxDealer.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, cbxDealer.getSelectedEntity().getId()));	
		}
		if (cbxDealerType.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("dea.dealerType", cbxDealerType.getSelectedEntity()));
		}
		return restrictions;
	}
	

	public void reset() {
		cbxDealerType.setSelectedEntity(null);
		cbxDealer.setSelectedEntity(null);
		cbxQuotationStatusFrom.setValue(null);
		cbxQuotationStatusTo.setValue(null);
		dfStartDate.setValue(DateUtils.today());
		dfEndDate.setValue(DateUtils.today());
	}
	
	/**
	 * Search
	 */
	public void search() {	
		setIndexedContainer(processTimeService.getConsolidateQuotationStatusHistories(
				cbxQuotationStatusFrom.getSelectedEntity(), 
				cbxQuotationStatusTo.getSelectedEntity(), 
				getRestrictions()));
	}
	
	/**
	 * Get indexed container
	 * @return IndexedContainer
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<QuotationStatusHistory> quotationStatusHistories) {
		// TODO PYI
//		Container indexedContainer = pagedTable.getContainerDataSource();
//		indexedContainer.removeAllItems();
//		for (QuotationStatusHistory quotationStatusHistory : quotationStatusHistories) {
//			Item item = indexedContainer.addItem(quotationStatusHistory.getId());
//			item.getItemProperty(ID).setValue(quotationStatusHistory.getId());
//			item.getItemProperty("status.from").setValue(quotationStatusHistory.getPreviousWkfStatus().getDesc());
//			item.getItemProperty("status.to").setValue(quotationStatusHistory.getWkfStatus().getDesc());
//			item.getItemProperty("average.process.time").setValue(getTime(quotationStatusHistory.getProcessTime()));
//		}
	}

	/**
	 * @return List of ColumnDefinition
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		columnDefinitions = new ArrayList<>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 140, false));
		columnDefinitions.add(new ColumnDefinition("status.from", I18N.message("status.from"), String.class, Align.LEFT, 140, true));
		columnDefinitions.add(new ColumnDefinition("status.to", I18N.message("status.to"), String.class, Align.LEFT, 140, true));
		columnDefinitions.add(new ColumnDefinition("average.process.time", I18N.message("average.process.time"), String.class, Align.LEFT, 170, true));		
		return columnDefinitions;
	}
	
	/**
	 * @param millis
	 * @return
	 */
	private String getTime(Long millis) {
		if (millis != null) {
			String m = "" + (millis / (1000 * 60)) % 60;
			String h = "" + (millis / (1000 * 60 * 60));
			return h + "h" + m + "m";
		}
		return "N/A";
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		
	}
}
