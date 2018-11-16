package com.nokor.efinance.gui.ui.panel.report.outstanding;

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
import com.nokor.efinance.core.shared.accounting.LeaseTransaction;
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
import com.nokor.frmk.vaadin.ui.widget.table.PropertyColumnRenderer;
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
@VaadinView(NetLeasingsReportPanel.NAME)
public class NetLeasingsReportPanel extends AbstractTabPanel implements View, ContractEntityField, FrmkServicesHelper {
	
	private static final long serialVersionUID = 8019051395954081483L;

	public static final String NAME = "net.leasings.report";
	
	@Autowired
	private GLFLeasingAccountingService accountingService;
		
	private TabSheet tabSheet;
	
	private SimplePagedTable<LeaseTransaction> pagedTable;
	private List<ColumnDefinition> columnDefinitions;
	private ERefDataComboBox<EDealerType> cbxDealerType;
	private DealerComboBox cbxDealer;
	private TextField txtContractReference;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	
	public NetLeasingsReportPanel() {
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
        pagedTable = new SimplePagedTable<LeaseTransaction>(this.columnDefinitions);
        pagedTable.setFooterVisible(true);
        pagedTable.setColumnFooter("irr.rate", I18N.message("total"));
        
        contentLayout.addComponent(searchPanel);
        contentLayout.addComponent(pagedTable);
        contentLayout.addComponent(pagedTable.createControls());
        
        tabSheet.addTab(contentLayout, I18N.message("net.leasings"));
        
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
			List<LeaseTransaction> revenues = accountingService
					.getNetLeasings(cbxDealerType.getSelectedEntity(),
							cbxDealer.getSelectedEntity(),
							txtContractReference.getValue(),
							dfStartDate.getValue(), dfEndDate.getValue());
			setIndexedContainer(revenues);
		}
	}
	
	/**
	 * Get indexed container
	 * @return IndexedContainer
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<LeaseTransaction> leaseTransactions) {
		Indexed indexedContainer = pagedTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		double totalInterestRevenue = 0d;
		double totalInterestReceivable = 0d;
		double totalPenalty = 0d;
		double totalPrincipalRepayment = 0d;
		double totalPrincipalBalance = 0d;
		double totalInterestIncome = 0d;
		double totalUnearnedInterestBalance = 0d;
		double totalBalance = 0d;
		
		// double totalInterestInSuspend = 0d;
		// double totalInterestInSuspendCumulated = 0d;
		
		for (LeaseTransaction leaseTransaction : leaseTransactions) {
			Item item = indexedContainer.addItem(leaseTransaction.getId());
			item.getItemProperty(REFERENCE).setValue(leaseTransaction.getReference());
			item.getItemProperty(START_DATE).setValue(leaseTransaction.getContractStartDate());
			item.getItemProperty("firstInstallmentDate").setValue(leaseTransaction.getFirstInstallmentDate());
			// item.getItemProperty(LAST_NAME_EN).setValue(leaseTransaction.getLastNameEn());
			// item.getItemProperty(FIRST_NAME_EN).setValue(leaseTransaction.getFirstNameEn());
			item.getItemProperty(INTEREST_RATE).setValue(leaseTransaction.getInterestRate());
			item.getItemProperty("irr.rate").setValue(leaseTransaction.getIrrRate());
			// item.getItemProperty("interest.insuspend").setValue(leaseTransaction.getInterestInSuspend());
			// item.getItemProperty("interest.insuspend.cumulated").setValue(leaseTransaction.getInterestInSuspendCumulated());
			item.getItemProperty("interest.revenue").setValue(leaseTransaction.getInterestRevenue());
			item.getItemProperty("interest.receivable").setValue(leaseTransaction.getInterestReceivable());
			item.getItemProperty("penalty").setValue(leaseTransaction.getPenalty());
			item.getItemProperty("principal.repayment").setValue(leaseTransaction.getPrincipalRepayment());
			item.getItemProperty("principal.balance").setValue(leaseTransaction.getPrincipalBalance());
			item.getItemProperty("interest.income").setValue(leaseTransaction.getInterestIncome());
			item.getItemProperty("unearned.interest.balance").setValue(leaseTransaction.getUnearnedInterestBalance());
			item.getItemProperty("total.balance").setValue(leaseTransaction.getTotalBalance());
			totalInterestRevenue += MyNumberUtils.getDouble(leaseTransaction.getInterestRevenue().getTiAmount());
			totalInterestReceivable += MyNumberUtils.getDouble(leaseTransaction.getInterestReceivable().getTiAmount());
			totalPrincipalRepayment += MyNumberUtils.getDouble(leaseTransaction.getPrincipalRepayment().getTiAmount());
			totalInterestIncome += MyNumberUtils.getDouble(leaseTransaction.getInterestIncome().getTiAmount());
			totalPrincipalBalance += MyNumberUtils.getDouble(leaseTransaction.getPrincipalBalance().getTiAmount());
			totalUnearnedInterestBalance += MyNumberUtils.getDouble(leaseTransaction.getUnearnedInterestBalance().getTiAmount());
			totalBalance += MyNumberUtils.getDouble(leaseTransaction.getTotalBalance().getTiAmount());
			
			// totalInterestInSuspend += MyNumberUtils.getDouble(leaseTransaction.getInterestInSuspend().getTiAmountUsd());
			// totalInterestInSuspendCumulated += MyNumberUtils.getDouble(leaseTransaction.getInterestInSuspendCumulated().getTiAmountUsd());
			totalPenalty += MyNumberUtils.getDouble(leaseTransaction.getPenalty().getTiAmount());
		}
		
		pagedTable.setColumnFooter("interest.revenue", AmountUtils.format(totalInterestRevenue));
		pagedTable.setColumnFooter("interest.receivable", AmountUtils.format(totalInterestReceivable));
		pagedTable.setColumnFooter("penalty", AmountUtils.format(totalPenalty));
		pagedTable.setColumnFooter("principal.repayment", AmountUtils.format(totalPrincipalRepayment));
		pagedTable.setColumnFooter("principal.balance", AmountUtils.format(totalPrincipalBalance));
		pagedTable.setColumnFooter("interest.income", AmountUtils.format(totalInterestIncome));
		pagedTable.setColumnFooter("unearned.interest.balance", AmountUtils.format(totalUnearnedInterestBalance));
		pagedTable.setColumnFooter("total.balance", AmountUtils.format(totalBalance));
		
		// pagedTable.setColumnFooter("interest.insuspend", AmountUtils.format(totalInterestInSuspend));
		// pagedTable.setColumnFooter("interest.insuspend.cumulated", AmountUtils.format(totalInterestInSuspendCumulated));
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
		// columnDefinitions.add(new ColumnDefinition(LAST_NAME_EN, I18N.message("lastname.en"), String.class, Align.LEFT, 100));
		// columnDefinitions.add(new ColumnDefinition(FIRST_NAME_EN, I18N.message("firstname.en"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(INTEREST_RATE, I18N.message("interest.rate"), Double.class, Align.LEFT, 60));
		columnDefinitions.add(new ColumnDefinition("irr.rate", I18N.message("irr.rate"), Double.class, Align.LEFT, 60));
		// columnDefinitions.add(new ColumnDefinition("interest.insuspend" , I18N.message("interest.insuspend"), Amount.class, Align.RIGHT, 100));
		// columnDefinitions.add(new ColumnDefinition("interest.insuspend.cumulated" , I18N.message("interest.insuspend.cumulated"), Amount.class, Align.RIGHT, 100, new AmountColumnRenderer(2)));
		columnDefinitions.add(new ColumnDefinition("interest.revenue" , I18N.message("revenue.interest.income"), Amount.class, Align.RIGHT, 100, new AmountColumnRenderer(2)));
		columnDefinitions.add(new ColumnDefinition("interest.receivable" , I18N.message("interest.receivable"), Amount.class, Align.RIGHT, 100, new AmountColumnRenderer(2)));
		columnDefinitions.add(new ColumnDefinition("penalty" , I18N.message("penalty"), Amount.class, Align.RIGHT, 70));
		columnDefinitions.add(new ColumnDefinition("principal.repayment", I18N.message("principal.repayment"), Amount.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition("principal.balance", I18N.message("principal.balance"), Amount.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition("interest.income", I18N.message("interest.income"), Amount.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition("unearned.interest.balance", I18N.message("balance.unearned.income"), Amount.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition("total.balance", I18N.message("total.balance"), Amount.class, Align.RIGHT, 100));
		return columnDefinitions;
	}
	
	/**
	 * @return
	 */
	public boolean isValid() {
		removeErrorsPanel();
		checkMandatoryDateField(dfStartDate, "startdate");
		checkMandatoryDateField(dfEndDate, "enddate");
		/*if (dfEndDate.getValue() != null
				&& DateUtils.getDateAtBeginningOfDay(dfEndDate.getValue()).compareTo(DateUtils.today()) > 0) {
			errors.add(I18N.message("enddate.must.be.less.than.today"));
		}*/
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
	
	private class AmountColumnRenderer extends PropertyColumnRenderer {
		
		private int nbDecimalToDisplay;
		
		public AmountColumnRenderer(int nbDecimalToDisplay) {
			this.nbDecimalToDisplay = nbDecimalToDisplay;
		}
		
		@Override
		public Object getValue() {
			Amount amountValue = (Amount) getPropertyValue();
			if (amountValue != null) {
				return AmountUtils.format(amountValue.getTiAmount(), nbDecimalToDisplay);
			}
			return null;
		}
	}
}
