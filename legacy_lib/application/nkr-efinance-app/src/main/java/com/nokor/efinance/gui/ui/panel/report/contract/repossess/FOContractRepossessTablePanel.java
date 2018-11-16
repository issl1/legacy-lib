package com.nokor.efinance.gui.ui.panel.report.contract.repossess;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.collection.model.ContractCollectionHistory;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.service.ContractService;
import com.nokor.efinance.core.contract.service.cashflow.CashflowService;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.RowRenderer;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.data.Item;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;

/**
 * Contract Overdue Table panel
 * @author bunlong.taing
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class FOContractRepossessTablePanel extends AbstractTablePanel<Contract> implements FMEntityField, CashflowEntityField {

	/** */
	private static final long serialVersionUID = 6941674894700099825L;
	
	@Autowired
	private ContractService contractService;
	@Autowired
	private CashflowService cashflowService;
	
	/** */
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("contract.repossesses"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		
		if(ProfileUtil.isManager())
			initialize(I18N.message("contract.repossesses"));
		else
			super.init(I18N.message("contract.repossesses"));
		
		getPagedTable().addStyleName("colortable");
		getPagedTable().setCellStyleGenerator(new Table.CellStyleGenerator() {
			private static final long serialVersionUID = 6242667432758981026L;
			@Override
			public String getStyle(Table source, Object itemId, Object propertyId) {
				if (propertyId == null) {
					Item item = source.getItem(itemId);
					Date endPeriodPromiseToPay = (Date) item.getItemProperty("end.period.promise.to.pay").getValue();
					if (endPeriodPromiseToPay != null){
						long timeCompare = getHour(DateUtils.todayH00M00S00().getTime() - endPeriodPromiseToPay.getTime());
						if (timeCompare >= 0){
							return "highligh-red";
						}else if(timeCompare >= -48){
							return "highligh";
						}
					}
				}
				return null;
			}
		});
	}
	
	public void initialize(String caption) {		
		searchPanel = createSearchPanel();
		if (searchPanel != null) {
			addComponent(searchPanel);
		}

		beforeTablePanel = createBeforeTablePanel();
		if (beforeTablePanel != null) {
			addComponent(beforeTablePanel);
		}
		
		pagedTable = createPagedTable(caption);
		
		addComponent(pagedTable);
		addComponent(pagedTable.createControls());
		
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createPagedDataProvider()
	 */
	@Override
	protected PagedDataProvider<Contract> createPagedDataProvider() {
		PagedDefinition<Contract> pagedDefinition = new PagedDefinition<Contract>(searchPanel.getRestrictions());
		pagedDefinition.setRowRenderer(new ContractOverdueRowRenderer());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 70, false);
		pagedDefinition.addColumnDefinition(UPDATE_DATE, I18N.message("last.update"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("contract", I18N.message("contract"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("collection.status", I18N.message("collection.status"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("customer.attribute", I18N.message("customer.attribute"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("amount.promise.to.pay", I18N.message("amount.promise.to.pay"), Amount.class, Align.RIGHT, 150);
		pagedDefinition.addColumnDefinition("start.period.promise.to.pay", I18N.message("start.period.promise.to.pay"), Date.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("end.period.promise.to.pay", I18N.message("end.period.promise.to.pay"), Date.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("lessee.mobile.phone.1", I18N.message("lessee.mobile.phone.1"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("lessee.mobile.phone.2", I18N.message("lessee.mobile.phone.2"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("guarantor.mobile.phone", I18N.message("guarantor.mobile.phone"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("lastname.en", I18N.message("lastname.en"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("firstname.en", I18N.message("firstname.en"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(DEALER + "." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 170);
		pagedDefinition.addColumnDefinition("credit.officer", I18N.message("credit.officer"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("term", I18N.message("term	"), Integer.class, Align.LEFT, 60);
		pagedDefinition.addColumnDefinition("collection.officer", I18N.message("collection.officer"), String.class, Align.LEFT, 120);		
		pagedDefinition.addColumnDefinition("task", I18N.message("task"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("group", I18N.message("group"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("advance.payment.percentage", I18N.message("advance.payment.percentage"), String.class, Align.RIGHT, 150);
		pagedDefinition.addColumnDefinition("num.overdue.in.days", I18N.message("num.overdue.in.days"), Integer.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("lastest.payment.date", I18N.message("lastest.payment.date"), Date.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("num.installment.in.overdue", I18N.message("num.installment.in.overdue"), Integer.class, Align.LEFT, 160);
		pagedDefinition.addColumnDefinition("latest.num.installment.paid", I18N.message("latest.num.installment.paid"), Integer.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("total.penalty.amount.in.overdue", I18N.message("total.penalty.amount.in.overdue"), Amount.class, Align.RIGHT, 160);
		pagedDefinition.addColumnDefinition("total.amount.in.overdue", I18N.message("total.amount.in.overdue"), Amount.class, Align.RIGHT, 150);
		pagedDefinition.addColumnDefinition("num.installment.paid", I18N.message("num.installment.paid"), Integer.class, Align.LEFT, 130);
		pagedDefinition.addColumnDefinition("last.paid.payment.method", I18N.message("last.paid.payment.method"), String.class, Align.LEFT, 160);		
		pagedDefinition.addColumnDefinition("contract.start.date", I18N.message("contract.start.date"), Date.class, Align.LEFT, 160);
		pagedDefinition.addColumnDefinition("remaining.principal.balance", I18N.message("outstanding.balance"), Amount.class, Align.RIGHT, 140);
		
		EntityPagedDataProvider<Contract> pagedDataProvider = new EntityPagedDataProvider<Contract>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected Contract getEntity() {
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected AbstractSearchPanel<Contract> createSearchPanel() {
		if (ProfileUtil.isCollectionController()) {
			return new ContractRepossessSearch2Panel(this);
		}
		return new ContractRepossessSearchPanel(this);
	}
	
	/**
	 * @author bunlong.taing
	 */
	private class ContractOverdueRowRenderer implements RowRenderer {

		/**
		 * @see com.nokor.frmk.vaadin.ui.widget.table.RowRenderer#renderer(com.vaadin.data.Item, org.seuksa.frmk.model.entity.Entity)
		 */
		@SuppressWarnings("unchecked")
		@Override
		public void renderer(Item item, Entity entity) {
			Contract contract = (Contract) entity;
			Quotation quotation = contract.getQuotation();
			Amount outstanding = contractService.getRealOutstanding(DateUtils.todayH00M00S00(), contract.getId());
			// TODO PYI
//			SecUser collectionofficer = contract.getOfficer();
//			
//			item.getItemProperty(ID).setValue(contract.getId());
//			CollectionHistory contractCollectionHistory = getLatestContractCollectionHistory(contract.getContractCollectionHistories());
//			
//			if (contractCollectionHistory != null) {
//				long processingTime = DateUtils.today().getTime() - contractCollectionHistory.getCreateDate().getTime();
//				item.getItemProperty(UPDATE_DATE).setValue(getTime(processingTime));
//			} else {
//				item.getItemProperty(UPDATE_DATE).setValue("");
//			}
//			item.getItemProperty("contract").setValue(contract.getReference());
//			item.getItemProperty("collection.status").setValue(contract.getCollectionStatus() != null ? contract.getCollectionStatus().getDescEn() : "");
//			
//			if (quotation.getApplicant() != null) {
//				Applicant applicant = quotation.getApplicant();
//				item.getItemProperty("lessee.mobile.phone.1").setValue(applicant.getMobilePhone());
//				item.getItemProperty("lessee.mobile.phone.2").setValue(applicant.getMobilePhone2());
//			}
//			if (quotation.getQuotationApplicant(EApplicantType.G) != null) {
//				QuotationApplicant quotationApplicant = quotation.getQuotationApplicant(EApplicantType.G);
//				Applicant applicant = quotationApplicant.getApplicant();
//				item.getItemProperty("guarantor.mobile.phone").setValue(applicant != null ? applicant.getMobilePhone() : "");
//			}
//			if (contract.getApplicant() != null) {
//				Applicant applicant = contract.getApplicant();
//				item.getItemProperty("lastname.en").setValue(applicant.getLastNameEn());
//				item.getItemProperty("firstname.en").setValue(applicant.getFirstNameEn());
//			}
//			item.getItemProperty(DEALER + "." + NAME_EN).setValue(contract.getDealer().getNameEn());
//			if (quotation.getFieldCheck() != null) {
//				item.getItemProperty("credit.officer").setValue(quotation.getFieldCheck().getDesc());
//			} else {
//				item.getItemProperty("credit.officer").setValue(contract.getCreditOfficer() != null ? contract.getCreditOfficer().getDesc() : "");
//			}
//			item.getItemProperty("collection.officer").setValue(collectionofficer != null ? collectionofficer.getDesc() : "");
//			item.getItemProperty("term").setValue(contract.getTerm());
//			item.getItemProperty("task").setValue(contract.getCollectionTask() != null ? contract.getCollectionTask().getDescEn() : "");
//			item.getItemProperty("group").setValue(contract.getGroup() != null ? contract.getGroup().getDescEn() : "");
//			if (contract.getContractOtherData() != null) {
//				Collection otherData = contract.getContractOtherData();
//				item.getItemProperty("num.installment.in.overdue").setValue(otherData.getNbInstallmentsInOverdue());
//				item.getItemProperty("num.overdue.in.days").setValue(otherData.getNbOverdueInDays());
//				item.getItemProperty("lastest.payment.date").setValue(otherData.getLatestPaymentDate());
//				item.getItemProperty("latest.num.installment.paid").setValue(otherData.getLatestNumInstallmentPaid());
//				item.getItemProperty("num.installment.paid").setValue(otherData.getNbInstallmentsPaid());
//				item.getItemProperty("total.amount.in.overdue").setValue(AmountUtils.convertToAmount(otherData.getTiTotalAmountInOverdueUsd()));
//				item.getItemProperty("last.paid.payment.method").setValue(otherData.getLastPaidPaymentMethod() != null ? otherData.getLastPaidPaymentMethod().getDescEn() : "");
//				item.getItemProperty("contract.start.date").setValue(contract.getStartDate());
//				item.getItemProperty("total.penalty.amount.in.overdue").setValue(AmountUtils.convertToAmount(otherData.getTiTotalPenaltyAmountInOverdueUsd()));
//			}
//			
//			if (contractCollectionHistory != null) {
//				item.getItemProperty("amount.promise.to.pay").setValue(AmountUtils.convertToAmount(contractCollectionHistory.getAmountPromiseToPayUsd()));
//				item.getItemProperty("start.period.promise.to.pay").setValue(contractCollectionHistory.getStartPeriodPromiseToPay());
//				item.getItemProperty("end.period.promise.to.pay").setValue(contractCollectionHistory.getEndPeriodPromiseToPay());
//				item.getItemProperty("customer.attribute").setValue(contractCollectionHistory.getCustomerAttribute() != null ? contractCollectionHistory.getCustomerAttribute().getDescEn() : "");
//			}
			item.getItemProperty("advance.payment.percentage").setValue(AmountUtils.format(contract.getAdvancePaymentPercentage()) + "%");
			item.getItemProperty("remaining.principal.balance").setValue(AmountUtils.convertToAmount(outstanding.getTiAmount()));
		}
		
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
	 * Get the latest ContractCollectionHistory
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
	private long getHour(Long millis) {
		if (millis != null) {
			long h =  (millis / (1000 * 60 * 60));
			return h;
		}
		return 0;
	}
}
