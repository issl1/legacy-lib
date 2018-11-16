package com.nokor.efinance.gui.ui.panel.report.dailycontracts;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.core.workflow.PaymentWkfStatus;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
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
 * @author sok.vina
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(DailyContractReportsPanel.NAME)
public class DailyContractReportsPanel extends AbstractTabPanel implements View, FrmkServicesHelper {
	
	private static final long serialVersionUID = -4618786633559261506L;
	public static final String NAME = "daily.contract.report";
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private TabSheet tabSheet;
	
	private SimplePagedTable<Contract> pagedTable;
	private List<ColumnDefinition> columnDefinitions;
	private DealerComboBox cbxDealer;
	private AutoDateField dfCurrentDate;
	public DailyContractReportsPanel() {
		super();
		setSizeFull();
	}
	
	@Override
	protected com.vaadin.ui.Component createForm() {
		tabSheet = new TabSheet();
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.setMargin(true);
		
		VerticalLayout gridLayoutPanel = new VerticalLayout();
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
		
		final GridLayout gridLayout = new GridLayout(10, 2);
		gridLayout.setSpacing(true);
		cbxDealer = new DealerComboBox(null, DataReference.getInstance().getDealers(), I18N.message("all"));
		cbxDealer.setSelectedEntity(null);
		cbxDealer.setWidth("220px");
		
		dfCurrentDate = ComponentFactory.getAutoDateField("", false);
		dfCurrentDate.setValue(DateUtils.today());

        int iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("dealer")), iCol++, 0);
        gridLayout.addComponent(cbxDealer, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("current.date")), iCol++, 0);
        gridLayout.addComponent(dfCurrentDate, iCol++, 0);
        
        gridLayoutPanel.addComponent(gridLayout);
        searchLayout.setMargin(true);
        searchLayout.setSpacing(true);
        searchLayout.addComponent(gridLayoutPanel);
        searchLayout.addComponent(buttonsLayout);
        
        Panel searchPanel = new Panel();
        searchPanel.setCaption(I18N.message("search"));
        searchPanel.setContent(searchLayout);
        
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(true);
        this.columnDefinitions = createColumnDefinitions();
        pagedTable = new SimplePagedTable<Contract>(this.columnDefinitions);
        contentLayout.addComponent(searchPanel);
        contentLayout.addComponent(horizontalLayout);
        contentLayout.setComponentAlignment(horizontalLayout, Alignment.BOTTOM_RIGHT);
        contentLayout.addComponent(pagedTable);
        contentLayout.addComponent(pagedTable.createControls());
        tabSheet.addTab(contentLayout, I18N.message("daily.contracts"));
        return tabSheet;
	}
	
	/**
	 * @return BaseRestrictions
	 */
	public BaseRestrictions<Quotation> getRestrictions() {
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<Quotation>(Quotation.class);				
		if (cbxDealer.getSelectedEntity() != null) { 
			restrictions.addCriterion(Restrictions.eq("dealer.id", cbxDealer.getSelectedEntity().getId()));
		}
		if (dfCurrentDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.ge("startCreationDate", DateUtils.getDateAtBeginningOfDay(dfCurrentDate.getValue())));
			restrictions.addCriterion(Restrictions.le("startCreationDate", DateUtils.getDateAtEndOfDay(dfCurrentDate.getValue())));
		}
		return restrictions;
	
	}

	public void reset() {
		cbxDealer.setSelectedEntity(null);
		dfCurrentDate.setValue(DateUtils.today());
	}
	
	/**
	 * Search
	 */
	private void search() {
		setIndexedContainer(ENTITY_SRV.list(getRestrictions()));
	}
	
	/**
	 * Get indexed container
	 * @return IndexedContainer
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<Quotation> quotations) {
		Indexed indexedContainer = pagedTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		int numberContract = 0;
		int numPendingAdvancePayment = 0;
		int numReceivedAdvancePayment = 0;
		if (quotations != null & !quotations.isEmpty()) {
			for (int i = 0; i < quotations.size() ; i++) {
				Quotation quotation = quotations.get(i);
				List<Cashflow> cashflows = null;
				Contract contract = ENTITY_SRV.getById(Contract.class, quotation.getContract().getId());
				if (contract != null) {
					numberContract++;
					BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<Cashflow>(Cashflow.class);
					restrictions.addCriterion(Restrictions.eq("cancel", Boolean.FALSE));
					restrictions.addCriterion(Restrictions.eq("numInstallment", 0));
					restrictions.addCriterion(Restrictions.eq("contract.id", contract.getId()));
					cashflows = ENTITY_SRV.list(restrictions);
				}

				if(quotation.getWkfStatus().equals(QuotationWkfStatus.APV) && 
						((cashflows == null || cashflows.isEmpty()) || cashflows.get(0).getPayment().getWkfStatus().equals(PaymentWkfStatus.RVA))) {
					numPendingAdvancePayment++;
				} else if (quotation.getWkfStatus().equals(QuotationWkfStatus.APV) 
						&& cashflows != null && !cashflows.isEmpty()
						&& (cashflows.get(0).getPayment().getWkfStatus().equals(PaymentWkfStatus.VAL) 
						|| cashflows.get(0).getPayment().getWkfStatus().equals(PaymentWkfStatus.PAI))) {
					numReceivedAdvancePayment++;
				}
			} 	
		}
		final Item item = indexedContainer.addItem(1);
		item.getItemProperty("date").setValue(dfCurrentDate.getValue() == null ? DateUtils.today() : dfCurrentDate.getValue());
		item.getItemProperty("num.contract").setValue(numberContract);	
		item.getItemProperty("advance.payment.number").setValue(numPendingAdvancePayment + numReceivedAdvancePayment);	
		item.getItemProperty("Pending.advance.payment.number").setValue(numPendingAdvancePayment);	
		item.getItemProperty("Received.advance.payment.number").setValue(numReceivedAdvancePayment);	
		pagedTable.refreshContainerDataSource();
	}

	/**
	 * @return List of ColumnDefinition
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("date", I18N.message("date"), Date.class, Align.CENTER, 120));
		columnDefinitions.add(new ColumnDefinition("num.contract", I18N.message("num.contract"), Integer.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("advance.payment.number", I18N.message("advance.payment.number"), Integer.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("Pending.advance.payment.number", I18N.message("Pending.advance.payment.number"), Integer.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("Received.advance.payment.number", I18N.message("Received.advance.payment.number"), Integer.class, Align.LEFT, 120));
		return columnDefinitions;
	}
	
	/**
	 * Refresh
	 */
	public void refresh() {
		search();
	}

	@Override
	public void enter(ViewChangeEvent event) {
		search();
	}	
}
