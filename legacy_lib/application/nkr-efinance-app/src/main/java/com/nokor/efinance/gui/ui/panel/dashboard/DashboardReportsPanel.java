package com.nokor.efinance.gui.ui.panel.dashboard;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.amount.Amount;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.collection.model.ContractCollectionHistory;
import com.nokor.efinance.core.common.security.model.SecUserDetail;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.SelectedItem;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;

/**
 * @author sok.vina
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DashboardReportsPanel extends AbstractTabPanel implements View, CashflowEntityField, ItemClickListener, SelectedItem {
	
	private static final long serialVersionUID = -4618786633559261506L;
	
	private SimplePagedTable<Contract> pagedTable;
	private List<ColumnDefinition> columnDefinitions;
	private VerticalLayout contentLayout;
	private Item selectedItem;
	private AbstractTabsheetPanel mainPanel;
	private ERefDataComboBox<EWkfStatus> cbxCollectStatus;
	private ERefDataComboBox<EWkfStatus> cbxCollectStatus2;
	private ERefDataComboBox<EWkfStatus> cbxCollectStatus3;
	private ERefDataComboBox<EWkfStatus> cbxCollectStatus4;
	private Button btnSearch;
	
	public DashboardReportsPanel() {
		super();
		setSizeFull();
	}
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		
		/*
		 * Additional mod.: David VA
		 */
		
		contentLayout = new VerticalLayout();	
        cbxCollectStatus = new ERefDataComboBox<EWkfStatus>(EWkfStatus.class);
		cbxCollectStatus.setImmediate(true);
		cbxCollectStatus.setWidth("220px");
		
		cbxCollectStatus2 = new ERefDataComboBox<EWkfStatus>(EWkfStatus.class);
		cbxCollectStatus2.setImmediate(true);
		cbxCollectStatus2.setWidth("220px");
		
		cbxCollectStatus3 = new ERefDataComboBox<EWkfStatus>(EWkfStatus.class);
		cbxCollectStatus3.setImmediate(true);
		cbxCollectStatus3.setWidth("220px");
		
		cbxCollectStatus4 = new ERefDataComboBox<EWkfStatus>(EWkfStatus.class);
		cbxCollectStatus4.setImmediate(true);
		cbxCollectStatus4.setWidth("220px");
		//contentLayout.addComponent(cbxCollectStatus);
		
		btnSearch = ComponentFactory.getButton("search");
		btnSearch.setClickShortcut(KeyCode.ENTER, null);
		btnSearch.setIcon(new ThemeResource("../nkr-default/icons/16/search.png"));
		btnSearch.addClickListener(getButtonClickListener());	// add button click listener
		//contentLayout.addComponent(btnSearch);
	
		final GridLayout gridLayout = new GridLayout(12, 2);
		gridLayout.setSpacing(true);
		
		int iCol = 0;
		gridLayout.addComponent(new Label(I18N.message("collection.status")), iCol++, 0);
        gridLayout.addComponent(cbxCollectStatus, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(1, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("collection.status")), iCol++, 0);
        gridLayout.addComponent(cbxCollectStatus2, iCol++, 0);
        
        iCol = 0;
        
        gridLayout.addComponent(new Label(I18N.message("collection.status")), iCol++, 1);
        gridLayout.addComponent(cbxCollectStatus3, iCol++, 1);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 1);
        gridLayout.addComponent(new Label(I18N.message("collection.status")), iCol++, 1);
        gridLayout.addComponent(cbxCollectStatus4, iCol++, 1);
        		
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(gridLayout);
		formLayout.addComponent(btnSearch);		
		contentLayout.addComponent(formLayout);
		
		pagedTable = new SimplePagedTable<Contract>(createColumnDefinitions());
		contentLayout.setSpacing(true);
		contentLayout.addComponent(new Label(I18N.message("overdue.contracts")));
		contentLayout.addComponent(pagedTable);
		contentLayout.addComponent(pagedTable.createControls());
		//contentLayout.setCaption(I18N.message("overdue.contracts"));
		pagedTable.addItemClickListener(this);
        return contentLayout;
	} //end of David VA's mod. 
	
	/**
	 * Search
	 */
	public void search(Dealer dealer) {
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<Contract>(Contract.class);
		
		restrictions.addCriterion(Restrictions.eq("overdue", Boolean.TRUE));
		if (dealer != null) {
			restrictions.addCriterion(Restrictions.eq("dealer", dealer));
		}
		restrictions.addOrder(Order.asc(START_DATE));
		
		setIndexedContainer(ENTITY_SRV.list(restrictions));
	}
	
			
	/**
	 * BaseRestrcitions
	 * @return BaseRestrcitions
	 * Additional mod.: David VA
	 */
	public BaseRestrictions<Contract> getRestrictions() {
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<Contract>(Contract.class);
	
		restrictions.addCriterion(Restrictions.eq("overdue", Boolean.TRUE));
		
		if (cbxCollectStatus.getSelectedEntity() != null
				|| cbxCollectStatus2.getSelectedEntity() != null
				|| cbxCollectStatus3.getSelectedEntity() != null
				|| cbxCollectStatus4.getSelectedEntity() != null) {
			
			Disjunction orJunction = Restrictions.or();
			
			if(cbxCollectStatus.getSelectedEntity() != null)
			orJunction.add(Restrictions.eq("collectionStatus.id", cbxCollectStatus.getSelectedEntity().getId()));
			
			if(cbxCollectStatus2.getSelectedEntity() != null)
			orJunction.add(Restrictions.eq("collectionStatus.id", cbxCollectStatus2.getSelectedEntity().getId()));
			
			if(cbxCollectStatus3.getSelectedEntity() != null)
			orJunction.add(Restrictions.eq("collectionStatus.id", cbxCollectStatus3.getSelectedEntity().getId()));
			
			if(cbxCollectStatus4.getSelectedEntity() != null)
			orJunction.add(Restrictions.eq("collectionStatus.id", cbxCollectStatus4.getSelectedEntity().getId()));
			
			restrictions.addCriterion(orJunction);
		}
		if(getSecUserDetail().getDealer()!=null){
			restrictions.addCriterion(Restrictions.eq("dealer",getSecUserDetail().getDealer()));
		}
		
		restrictions.addOrder(Order.asc(START_DATE));
		return restrictions;
		
	} //end of David VA's mod.
	
	/**
	 * Get indexed container
	 * @return IndexedContainer
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<Contract> contracts) {
		Indexed indexedContainer =  pagedTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		
		if (contracts != null && !contracts.isEmpty()) {
			for (Contract contract : contracts) {
//				Item item = indexedContainer.addItem(contract.getId());
//				
//				Quotation quotation = contract.getQuotation();
//				Amount outstanding = contractService.getRealOutstanding(DateUtils.todayH00M00S00(), contract.getId());
//				// TODO PYI
//				SecUser collectionofficer = null;//contract.getOfficer();
//				
//				item.getItemProperty(ID).setValue(contract.getId());
//				CollectionHistory contractCollectionHistory = getLatestContractCollectionHistory(contract.getContractCollectionHistories());
//				
//				if (contractCollectionHistory != null) {
//					long processingTime = DateUtils.today().getTime() - contractCollectionHistory.getCreateDate().getTime();
//					item.getItemProperty(UPDATE_DATE).setValue(getTime(processingTime));
//				} else {
//					item.getItemProperty(UPDATE_DATE).setValue("");
//				}
//				item.getItemProperty("contract").setValue(contract.getReference());
//				item.getItemProperty("collection.status").setValue(contract.getCollectionStatus() != null ? contract.getCollectionStatus().getDescEn() : "");
//				
//				if (quotation.getApplicant() != null) {
//					Applicant applicant = quotation.getApplicant();
//					item.getItemProperty("lessee.mobile.phone.1").setValue(applicant.getMobilePhone());
//					item.getItemProperty("lessee.mobile.phone.2").setValue(applicant.getMobilePhone2());
//				}
//				if (quotation.getQuotationApplicant(EApplicantType.G) != null) {
//					QuotationApplicant quotationApplicant = quotation.getQuotationApplicant(EApplicantType.G);
//					Applicant applicant = quotationApplicant.getApplicant();
//					item.getItemProperty("guarantor.mobile.phone").setValue(applicant != null ? applicant.getMobilePhone() : "");
//				}
//				if (contract.getApplicant() != null) {
//					Applicant applicant = contract.getApplicant();
//					item.getItemProperty("lastname.en").setValue(applicant.getLastNameEn());
//					item.getItemProperty("firstname.en").setValue(applicant.getFirstNameEn());
//				}
//				item.getItemProperty(DEALER + "." + NAME_EN).setValue(contract.getDealer().getNameEn());
//				if (quotation.getFieldCheck() != null) {
//					item.getItemProperty("credit.officer").setValue(quotation.getFieldCheck().getDesc());
//				} else {
//					item.getItemProperty("credit.officer").setValue(contract.getCreditOfficer() != null ? contract.getCreditOfficer().getDesc() : "");
//				}
//				item.getItemProperty("collection.officer").setValue(collectionofficer != null ? collectionofficer.getDesc() : "");
//				item.getItemProperty("term").setValue(contract.getTerm());
//				item.getItemProperty("task").setValue(contract.getCollectionTask() != null ? contract.getCollectionTask().getDescEn() : "");
//				item.getItemProperty("group").setValue(contract.getGroup() != null ? contract.getGroup().getDescEn() : "");
//				if (contract.getContractOtherData() != null) {
//					Collection otherData = contract.getContractOtherData();
//					item.getItemProperty("num.installment.in.overdue").setValue(otherData.getNbInstallmentsInOverdue());
//					item.getItemProperty("num.overdue.in.days").setValue(otherData.getNbOverdueInDays());
//					item.getItemProperty("lastest.payment.date").setValue(otherData.getLatestPaymentDate());
//					item.getItemProperty("latest.num.installment.paid").setValue(otherData.getLatestNumInstallmentPaid());
//					item.getItemProperty("num.installment.paid").setValue(otherData.getNbInstallmentsPaid());
//					item.getItemProperty("total.amount.in.overdue").setValue(AmountUtils.convertToAmount(otherData.getTiTotalAmountInOverdueUsd()));
//					item.getItemProperty("last.paid.payment.method").setValue(otherData.getLastPaidPaymentMethod() != null ? otherData.getLastPaidPaymentMethod().getDescEn() : "");
//					item.getItemProperty("contract.start.date").setValue(contract.getStartDate());
//					item.getItemProperty("total.penalty.amount.in.overdue").setValue(AmountUtils.convertToAmount(otherData.getTiTotalPenaltyAmountInOverdueUsd()));
//				}
//				
//				if (contractCollectionHistory != null) {
//					item.getItemProperty("amount.promise.to.pay").setValue(AmountUtils.convertToAmount(contractCollectionHistory.getAmountPromiseToPayUsd()));
//					item.getItemProperty("start.period.promise.to.pay").setValue(contractCollectionHistory.getStartPeriodPromiseToPay());
//					item.getItemProperty("end.period.promise.to.pay").setValue(contractCollectionHistory.getEndPeriodPromiseToPay());
//					item.getItemProperty("customer.attribute").setValue(contractCollectionHistory.getCustomerAttribute() != null ? contractCollectionHistory.getCustomerAttribute().getDescEn() : "");
//				}
//				item.getItemProperty("advance.payment.percentage").setValue(AmountUtils.format(contract.getAdvancePaymentPercentage()) + "%");
//				item.getItemProperty("remaining.principal.balance").setValue(AmountUtils.convertToAmount(outstanding.getTiAmountUsd()));
			}
		}
		
		pagedTable.refreshContainerDataSource();
	}

	/**
	 * @return List of ColumnDefinition
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 70, true));
		columnDefinitions.add(new ColumnDefinition(UPDATE_DATE, I18N.message("last.update"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("contract", I18N.message("contract"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("collection.status", I18N.message("collection.status"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("customer.attribute", I18N.message("customer.attribute"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("amount.promise.to.pay", I18N.message("amount.promise.to.pay"), Amount.class, Align.RIGHT, 150));
		columnDefinitions.add(new ColumnDefinition("start.period.promise.to.pay", I18N.message("start.period.promise.to.pay"), Date.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("end.period.promise.to.pay", I18N.message("end.period.promise.to.pay"), Date.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("lessee.mobile.phone.1", I18N.message("lessee.mobile.phone.1"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("lessee.mobile.phone.2", I18N.message("lessee.mobile.phone.2"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("guarantor.mobile.phone", I18N.message("guarantor.mobile.phone"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("lastname.en", I18N.message("lastname.en"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("firstname.en", I18N.message("firstname.en"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(DEALER + "." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 170));
		columnDefinitions.add(new ColumnDefinition("credit.officer", I18N.message("credit.officer"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("term", I18N.message("term	"), Integer.class, Align.LEFT, 60));
		columnDefinitions.add(new ColumnDefinition("collection.officer", I18N.message("collection.officer"), String.class, Align.LEFT, 120));		
		columnDefinitions.add(new ColumnDefinition("task", I18N.message("task"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("group", I18N.message("group"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("advance.payment.percentage", I18N.message("advance.payment.percentage"), String.class, Align.RIGHT, 150));
		columnDefinitions.add(new ColumnDefinition("num.overdue.in.days", I18N.message("num.overdue.in.days"), Integer.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("lastest.payment.date", I18N.message("lastest.payment.date"), Date.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("num.installment.in.overdue", I18N.message("num.installment.in.overdue"), Integer.class, Align.LEFT, 160));
		columnDefinitions.add(new ColumnDefinition("latest.num.installment.paid", I18N.message("latest.num.installment.paid"), Integer.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("total.penalty.amount.in.overdue", I18N.message("total.penalty.amount.in.overdue"), Amount.class, Align.RIGHT, 160));
		columnDefinitions.add(new ColumnDefinition("total.amount.in.overdue", I18N.message("total.amount.in.overdue"), Amount.class, Align.RIGHT, 150));
		columnDefinitions.add(new ColumnDefinition("num.installment.paid", I18N.message("num.installment.paid"), Integer.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition("last.paid.payment.method", I18N.message("last.paid.payment.method"), String.class, Align.LEFT, 160));		
		columnDefinitions.add(new ColumnDefinition("contract.start.date", I18N.message("contract.start.date"), Date.class, Align.LEFT, 160));
		columnDefinitions.add(new ColumnDefinition("remaining.principal.balance", I18N.message("outstanding.balance"), Amount.class, Align.RIGHT, 140));
		
		return columnDefinitions;
	}
	
	/**
	 * Get latest CotnractCollectionHistory
	 * @param histories
	 * @return
	 */
	private ContractCollectionHistory getLatestContractCollectionHistory (List<ContractCollectionHistory> histories) {
		if (histories == null || histories.isEmpty()) {
			return null;
		}
		ContractCollectionHistory latestHistory = histories.get(0);
		for (int i = 1; i < histories.size(); i++) {
			if (histories.get(i).getCreateDate().after(latestHistory.getCreateDate())) {
				latestHistory = histories.get(i);
			}
		}
		return latestHistory;
	}
	
	/**
	 * Get Time
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
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
	}

	/**
	 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
	 */
	@Override
	public void itemClick(ItemClickEvent event) {
		this.selectedItem = event.getItem();
		if (event.isDoubleClick() || SecApplicationContextHolder.getContext().clientDeviceIsMobileOrTablet()) {
			showFormPanel();
		}
	}
	
	/**
	 * Set Main Panel
	 * @param mainPanel
	 */
	public void setMainPanel (AbstractTabsheetPanel mainPanel) {
		this.mainPanel = mainPanel;
	}
	
	/**
	 * Show Form Panel
	 */
	public void showFormPanel () {
		this.mainPanel.getTabSheet().setAdd(false);
		this.mainPanel.onEditEventClick();
	}
	
	/**
	 * 
	 * @return
	 */
	public Long getItemSelectedId() {
		if (this.selectedItem != null) {
			return ((Long) this.selectedItem.getItemProperty("id").getValue());
		}
		return null;
	}

	@Override
	public Item getSelectedItem() {
		return this.selectedItem;
	}
	private ClickListener getButtonClickListener() {
		return new ClickListener() {
			/** */
			private static final long serialVersionUID = -774154222891913537L;
			@Override
			public void buttonClick(ClickEvent event) {
				if (event.getButton() == btnSearch) { // Button Search is clicked
					onSearch();
				}
			}
		};
	}
		private void onSearch() {
			List<Contract> contracts = ENTITY_SRV.list(getRestrictions());
			setIndexedContainer(contracts);
		}
		
		private SecUserDetail getSecUserDetail() {
			SecUser secUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			BaseRestrictions<SecUserDetail> restrictions = new BaseRestrictions<SecUserDetail>(SecUserDetail.class);
			restrictions.addCriterion(Restrictions.eq("secUser.id", secUser.getId()));
			List<SecUserDetail> usrDetails = ENTITY_SRV.list(restrictions);
			
			if (!usrDetails.isEmpty()) {
				return usrDetails.get(0);
			} 
		
			SecUserDetail usrDetail = new SecUserDetail();
			usrDetail.setSecUser(secUser);
			return usrDetail;
		}	
}
