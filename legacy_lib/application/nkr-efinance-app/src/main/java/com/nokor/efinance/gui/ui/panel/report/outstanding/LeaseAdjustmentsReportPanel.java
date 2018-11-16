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

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.shared.accounting.LeaseAdjustment;
import com.nokor.efinance.core.shared.contract.ContractEntityField;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
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
@VaadinView(LeaseAdjustmentsReportPanel.NAME)
public class LeaseAdjustmentsReportPanel extends AbstractTabPanel implements View, ContractEntityField, FrmkServicesHelper {
	
	private static final long serialVersionUID = 8019051395954081483L;

	public static final String NAME = "lease.adjustments.report";
	
	@Autowired
	private GLFLeasingAccountingService accountingService;
		
	private TabSheet tabSheet;
	
	private SimplePagedTable<LeaseAdjustment> pagedTable;
	private List<ColumnDefinition> columnDefinitions;
	private ERefDataComboBox<EDealerType> cbxDealerType;
	private ERefDataComboBox<EWkfStatus> cbxContractStatus;
	private DealerComboBox cbxDealer;
	private TextField txtContractReference;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	
	public LeaseAdjustmentsReportPanel() {
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
		
		//-----remove fraud status
		List<EWkfStatus> listContractStatus = ContractWkfStatus.listAfterSales();
		listContractStatus.remove(ContractWkfStatus.FRA);
		cbxContractStatus = new ERefDataComboBox<EWkfStatus>(listContractStatus);	
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
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 1);
        gridLayout.addComponent(new Label(I18N.message("contract.status")), iCol++, 1);
        gridLayout.addComponent(cbxContractStatus, iCol++, 1);
        
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
        
        tabSheet.addTab(contentLayout, I18N.message("lease.adjustments"));
        
        return tabSheet;
	}
	
	public void reset() {
		cbxDealer.setSelectedEntity(null);
		cbxDealerType.setSelectedEntity(null);
		txtContractReference.setValue("");
		dfStartDate.setValue(null);
		dfEndDate.setValue(null);
		cbxContractStatus.setSelectedEntity(null);
	}
	
	/**
	 * Search
	 */
	private void search() {
		if (isValid()) {
			List<LeaseAdjustment> leaseAdjustments = accountingService
					.getLeaseAdjustments(cbxDealerType.getSelectedEntity(),
							cbxDealer.getSelectedEntity(),
							cbxContractStatus.getSelectedEntity(),
							txtContractReference.getValue(),
							dfStartDate.getValue(), dfEndDate.getValue());
			if(leaseAdjustments != null){
				setIndexedContainer(leaseAdjustments);
			}
		}
	}
	
	/**
	 * Get indexed container
	 * @return IndexedContainer
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<LeaseAdjustment> leaseAdjustments) {
		Indexed indexedContainer = pagedTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		// double totalBalanceInterestInSuspend = 0d;
		double totalUnpaidAccruedInterestReceivable = 0d;
		double totalUnpaidPrincipalBalance = 0d;
		double totalUnpaidInterestBalance = 0d;
		double totalUnpaidTotalBalance = 0d;
		for (LeaseAdjustment leaseAdjustment : leaseAdjustments) {
			Item item = indexedContainer.addItem(leaseAdjustment.getId());
			item.getItemProperty(REFERENCE).setValue(leaseAdjustment.getReference());
			item.getItemProperty(CONTRACT_STATUS).setValue(leaseAdjustment.getWkfStatus().getDesc());
			item.getItemProperty(START_DATE).setValue(leaseAdjustment.getContractStartDate());
			item.getItemProperty("firstInstallmentDate").setValue(leaseAdjustment.getFirstInstallmentDate());
			item.getItemProperty("changeStatusDate").setValue(leaseAdjustment.getChangeStatusDate());
			// item.getItemProperty(LAST_NAME_EN).setValue(leaseAdjustment.getLastNameEn());
			// item.getItemProperty(FIRST_NAME_EN).setValue(leaseAdjustment.getFirstNameEn());
			// item.getItemProperty("balanceInterestInSuspend").setValue(leaseAdjustment.getBalanceInterestInSuspend());
			item.getItemProperty("unpaidAccruedInterestReceivable").setValue(leaseAdjustment.getUnpaidAccruedInterestReceivable());
			item.getItemProperty("unpaidPrincipalBalance").setValue(leaseAdjustment.getUnpaidPrincipalBalance());
			item.getItemProperty("unpaidInterestBalance").setValue(leaseAdjustment.getUnpaidInterestBalance());
			item.getItemProperty("unpaidTotalBalance").setValue(leaseAdjustment.getUnpaidTotalBalance());
			// totalBalanceInterestInSuspend += MyNumberUtils.getDouble(leaseAdjustment.getBalanceInterestInSuspend().getTiAmountUsd());
			totalUnpaidAccruedInterestReceivable += MyNumberUtils.getDouble(leaseAdjustment.getUnpaidAccruedInterestReceivable().getTiAmount());
			totalUnpaidPrincipalBalance += MyNumberUtils.getDouble(leaseAdjustment.getUnpaidPrincipalBalance().getTiAmount());
			totalUnpaidInterestBalance += MyNumberUtils.getDouble(leaseAdjustment.getUnpaidInterestBalance().getTiAmount());
			totalUnpaidTotalBalance += MyNumberUtils.getDouble(leaseAdjustment.getUnpaidTotalBalance().getTiAmount());
		}
		// pagedTable.setColumnFooter("balanceInterestInSuspend", AmountUtils.format(totalBalanceInterestInSuspend));
		pagedTable.setColumnFooter("unpaidAccruedInterestReceivable", AmountUtils.format(totalUnpaidAccruedInterestReceivable));
		pagedTable.setColumnFooter("unpaidPrincipalBalance", AmountUtils.format(totalUnpaidPrincipalBalance));
		pagedTable.setColumnFooter("unpaidInterestBalance", AmountUtils.format(totalUnpaidInterestBalance));
		pagedTable.setColumnFooter("unpaidTotalBalance", AmountUtils.format(totalUnpaidTotalBalance));
		pagedTable.refreshContainerDataSource();
	}

	/**
	 * @return List of ColumnDefinition
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(REFERENCE, I18N.message("contract.reference"), String.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition(CONTRACT_STATUS, I18N.message("status"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(START_DATE, I18N.message("contract.start.date"), Date.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("firstInstallmentDate", I18N.message("first.payment.date"), Date.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("changeStatusDate", I18N.message("change.status.date"), Date.class, Align.LEFT, 100));
		// columnDefinitions.add(new ColumnDefinition(LAST_NAME_EN, I18N.message("lastname.en"), String.class, Align.LEFT, 100));
		// columnDefinitions.add(new ColumnDefinition(FIRST_NAME_EN, I18N.message("firstname.en"), String.class, Align.LEFT, 100));
		// columnDefinitions.add(new ColumnDefinition("balanceInterestInSuspend", I18N.message("balance.interest.in.suspend"), Amount.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition("unpaidAccruedInterestReceivable", I18N.message("unpaid.accrued.interest.receivable"), Amount.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition("unpaidPrincipalBalance", I18N.message("unpaid.principal.balance"), Amount.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition("unpaidInterestBalance", I18N.message("unpaid.interest.balance"), Amount.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition("unpaidTotalBalance", I18N.message("unpaid.total.balance"), Amount.class, Align.RIGHT, 100));
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
