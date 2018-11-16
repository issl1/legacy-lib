package com.nokor.efinance.gui.ui.panel.report.servicing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.shared.accounting.ServicingIncome;
import com.nokor.efinance.core.shared.contract.ContractEntityField;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.glf.accounting.service.GLFLeasingAccountingService;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
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
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import ru.xpoft.vaadin.VaadinView;

/**
 * @author mao.heng
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(ServicingIncomesReportAdjustment2Panel.NAME)
public class ServicingIncomesReportAdjustment2Panel extends AbstractTabPanel implements View, ContractEntityField, FrmkServicesHelper {
	
	private static final long serialVersionUID = 8019051395954081483L;

	public static final String NAME = "servicing.incomes.report";
	
	@Autowired
	private GLFLeasingAccountingService accountingService;
		
	private TabSheet tabSheet;
	
	private SimplePagedTable<ServicingIncome> pagedTable;
	private List<ColumnDefinition> columnDefinitions;
	private ERefDataComboBox<EDealerType> cbxDealerType;
	private DealerComboBox cbxDealer;
	private TextField txtContractReference;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	
	public ServicingIncomesReportAdjustment2Panel() {
		super();
		setSizeFull();
	}
	
	@SuppressWarnings("serial")
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
		btnSearch.setIcon(new ThemeResource("../nkr-default/icons/16/search.png"));
		btnSearch.addClickListener(new ClickListener() {		
			@Override
			public void buttonClick(ClickEvent event) {
				search();
			}
		});
		
		Button btnReset = new Button(I18N.message("reset"));
		btnReset.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				reset();
			}
		});
		buttonsLayout.setSpacing(true);
		buttonsLayout.addComponent(btnSearch);
		buttonsLayout.addComponent(btnReset);
		
		final GridLayout gridLayout = new GridLayout(8, 3);
		gridLayout.setSpacing(true);
		cbxDealer = new DealerComboBox(null, DataReference.getInstance().getDealers(), I18N.message("all"));
		cbxDealer.setSelectedEntity(null);
		cbxDealer.setWidth("220px");
		
		cbxDealerType = new ERefDataComboBox<EDealerType>(EDealerType.class);
		cbxDealerType.setImmediate(true);
		cbxDealerType.addValueChangeListener(new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = -6774641791917653706L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				BaseRestrictions<Dealer> restrictions = new BaseRestrictions<Dealer>(Dealer.class);
				restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
				if (cbxDealerType.getSelectedEntity() != null) {
					restrictions.addCriterion(Restrictions.eq("dealerType", cbxDealerType.getSelectedEntity()));
				}
				cbxDealer.setDealers(ENTITY_SRV.list(restrictions));
				cbxDealer.setSelectedEntity(null);
			}
		});
		
		txtContractReference = ComponentFactory.getTextField(false, 20, 150);
		dfStartDate = ComponentFactory.getAutoDateField("",false);
		dfStartDate.setValue(DateUtils.getDateAtBeginningOfMonth(DateUtils.todayH00M00S00()));
		
		dfEndDate = ComponentFactory.getAutoDateField("", false);
		dfEndDate.setValue(DateUtils.getDateAtEndOfMonth(DateUtils.todayH00M00S00()));

        int iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("contract.reference")), iCol++, 0);
        gridLayout.addComponent(txtContractReference, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("dealer.type")), iCol++, 0);
        gridLayout.addComponent(cbxDealerType, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("dealer")), iCol++, 0);
        gridLayout.addComponent(cbxDealer, iCol++, 0);
        
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("startdate")), iCol++, 1);
        gridLayout.addComponent(dfStartDate, iCol++, 1);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 1);
        gridLayout.addComponent(new Label(I18N.message("enddate")), iCol++, 1);
        gridLayout.addComponent(dfEndDate, iCol++, 1);
        
        gridLayoutPanel.addComponent(gridLayout);
        
        searchLayout.setMargin(true);
        searchLayout.setSpacing(true);
        searchLayout.addComponent(gridLayoutPanel);
        searchLayout.addComponent(buttonsLayout);
        
        Panel searchPanel = new Panel();
        searchPanel.setCaption(I18N.message("search"));
        searchPanel.setContent(searchLayout);
        
        this.columnDefinitions = createColumnDefinitions();
        pagedTable = new SimplePagedTable<>(this.columnDefinitions);
        pagedTable.setFooterVisible(true);
        pagedTable.setColumnFooter(FIRST_NAME_EN, I18N.message("total"));
        
        contentLayout.addComponent(searchPanel);
        contentLayout.addComponent(pagedTable);
        contentLayout.addComponent(pagedTable.createControls());
        
        tabSheet.addTab(contentLayout, I18N.message("service.incomes"));
        
        return tabSheet;
	}
	
	public void reset() {
		cbxDealerType.setSelectedEntity(null);
		cbxDealer.setSelectedEntity(null);
		txtContractReference.setValue("");
		dfStartDate.setValue(null);
		dfEndDate.setValue(null);
	}
	
	/**
	 * Search
	 */
	private void search() {
		if (isValid()) {
			List<ServicingIncome> servicingIncomes = accountingService
					.getServicingIncomes(cbxDealerType.getSelectedEntity(),
							cbxDealer.getSelectedEntity(),
							txtContractReference.getValue(),
							dfStartDate.getValue(), dfEndDate.getValue());
			setIndexedContainer(servicingIncomes);
		}
	}
	
	/**
	 * Get indexed container
	 * @return IndexedContainer
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<ServicingIncome> servicingIncomes) {
		Indexed indexedContainer = pagedTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		double totalRealServicingIncomeDistributed = 0d;
		double totalUnearnedServicingIncome = 0d;
		double totalAccountReceivable = 0d;
		double totalServicingIncome = 0d;
		
		double totalServicingIncomeInSuspend = 0d;
		double totalServicingIncomeInSuspendCumulated = 0d;
		
		for (ServicingIncome servicingIncome : servicingIncomes) {
			Item item = indexedContainer.addItem(servicingIncome.getId());
			item.getItemProperty(REFERENCE).setValue(servicingIncome.getReference());
			item.getItemProperty(START_DATE).setValue(servicingIncome.getContractStartDate());
			item.getItemProperty("firstInstallmentDate").setValue(servicingIncome.getFirstInstallmentDate());
			item.getItemProperty(LAST_NAME_EN).setValue(servicingIncome.getLastNameEn());
			item.getItemProperty(FIRST_NAME_EN).setValue(servicingIncome.getFirstNameEn());
			
			item.getItemProperty("servicingIncomeInSuspend").setValue(servicingIncome.getServicingIncomeInSuspend());
			item.getItemProperty("servicingIncomeInSuspendCumulated").setValue(servicingIncome.getServicingIncomeInSuspendCumulated());
			
			item.getItemProperty("realServicingIncomeDistributed").setValue(servicingIncome.getRealServicingIncomeDistributed());
			item.getItemProperty("unearnedServicingIncome").setValue(servicingIncome.getUnearnedServicingIncome());
			item.getItemProperty("incomeReceived").setValue(servicingIncome.getServicingIncomeReceived());
			item.getItemProperty("accountReceivable").setValue(servicingIncome.getAccountReceivable());
			
			totalRealServicingIncomeDistributed += MyNumberUtils.getDouble(servicingIncome.getRealServicingIncomeDistributed().getTiAmount());
			totalUnearnedServicingIncome += MyNumberUtils.getDouble(servicingIncome.getUnearnedServicingIncome().getTiAmount());
			totalAccountReceivable += MyNumberUtils.getDouble(servicingIncome.getCumulativeBalance().getTiAmount());
			totalServicingIncome += MyNumberUtils.getDouble(servicingIncome.getServicingIncomeReceived().getTiAmount());
			
			totalServicingIncomeInSuspend += MyNumberUtils.getDouble(servicingIncome.getServicingIncomeInSuspend().getTiAmount());
			totalServicingIncomeInSuspendCumulated += MyNumberUtils.getDouble(servicingIncome.getServicingIncomeInSuspendCumulated().getTiAmount());
		}
		
		pagedTable.setColumnFooter("realServicingIncomeDistributed", AmountUtils.format(totalRealServicingIncomeDistributed));
		pagedTable.setColumnFooter("unearnedServicingIncome", AmountUtils.format(totalUnearnedServicingIncome));
		pagedTable.setColumnFooter("accountReceivable", AmountUtils.format(totalAccountReceivable));
		pagedTable.setColumnFooter("incomeReceived", AmountUtils.format(totalServicingIncome));
		pagedTable.setColumnFooter("servicingIncomeInSuspend", AmountUtils.format(totalServicingIncomeInSuspend));
		pagedTable.setColumnFooter("servicingIncomeInSuspendCumulated", AmountUtils.format(totalServicingIncomeInSuspendCumulated));
		
		pagedTable.refreshContainerDataSource();
	}

	/**
	 * @return List of ColumnDefinition
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(REFERENCE, I18N.message("contract.reference"), String.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition(START_DATE, I18N.message("contract.start.date"), Date.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("firstInstallmentDate", I18N.message("first.payment.date"), Date.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(LAST_NAME_EN, I18N.message("lastname.en"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(FIRST_NAME_EN, I18N.message("firstname.en"), String.class, Align.LEFT, 100));
		
		columnDefinitions.add(new ColumnDefinition("servicingIncomeInSuspend", I18N.message("servicing.income.insuspend"), Amount.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition("servicingIncomeInSuspendCumulated", I18N.message("servicing.income.insuspend.cumulated"), Amount.class, Align.RIGHT, 100));
		
		columnDefinitions.add(new ColumnDefinition("realServicingIncomeDistributed", I18N.message("real.servicing.income.distributed"), Amount.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition("unearnedServicingIncome", I18N.message("unearned.servicing.income"), Amount.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition("incomeReceived", I18N.message("received.servicing.income"), Amount.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition("accountReceivable", I18N.message("account.receivable"), Amount.class, Align.RIGHT, 100));
		return columnDefinitions;
	}
	
	/**
	 * @return
	 */
	public boolean isValid() {
		removeErrorsPanel();
		checkMandatoryDateField(dfStartDate, "startdate");
		checkMandatoryDateField(dfEndDate, "enddate");
		if (!errors.isEmpty()) {
			displayErrorsPanel();
		}
		return errors.isEmpty();
	}
	
	/**
	 * Refresh
	 */
	public void refresh() {
		search();
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
	}
}
