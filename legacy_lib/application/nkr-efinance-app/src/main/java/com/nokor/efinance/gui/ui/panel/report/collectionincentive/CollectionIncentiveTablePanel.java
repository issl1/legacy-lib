package com.nokor.efinance.gui.ui.panel.report.collectionincentive;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.collection.model.CollectionIncentiveReport;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.service.ContractService;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.RowRenderer;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.data.Item;
import com.vaadin.ui.Table.Align;

/**
 * Collection Incentive Report
 * @author buntha.chea
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CollectionIncentiveTablePanel extends AbstractTablePanel<CollectionIncentiveReport> implements FMEntityField {

	/** */
	private static final long serialVersionUID = 6941674894700099825L;
	
	@Autowired
	private ContractService contractService;
	
	/** */
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("collection.incentives"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		super.init("");
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createPagedDataProvider()
	 */
	@Override
	protected PagedDataProvider<CollectionIncentiveReport> createPagedDataProvider() {
		PagedDefinition<CollectionIncentiveReport> pagedDefinition = new PagedDefinition<>(searchPanel.getRestrictions());
		pagedDefinition.setRowRenderer(new CollectionIncentionRowRenderer());
		pagedDefinition.addColumnDefinition(REFERENCE, I18N.message("reference"), String.class, Align.LEFT, 140, false);
		pagedDefinition.addColumnDefinition(PAYMENT_DATE, I18N.message("payment.date"), Date.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("collection.status", I18N.message("collection.status"), String.class, Align.LEFT, 130);
		pagedDefinition.addColumnDefinition("amount.promise.to.pay", I18N.message("amount.promise.to.pay"), String.class, Align.RIGHT, 100);
		pagedDefinition.addColumnDefinition("start.period.promise.to.pay", I18N.message("start.period.promise.to.pay"), Date.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("end.period.promise.to.pay", I18N.message("end.period.promise.to.pay"), Date.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("num.overdue.in.days", I18N.message("num.overdue.in.days"), Integer.class, Align.RIGHT, 150);
		pagedDefinition.addColumnDefinition(DEALER, I18N.message("dealer"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition("installment.amount", I18N.message("installment.amount"), String.class, Align.RIGHT, 100);
		pagedDefinition.addColumnDefinition("interest.amount", I18N.message("interest.amount"), String.class, Align.RIGHT, 100);
		pagedDefinition.addColumnDefinition("principal.amount", I18N.message("principal.amount"), String.class, Align.RIGHT, 100);
		pagedDefinition.addColumnDefinition("insurance.fee", I18N.message("insurance fee"), String.class, Align.RIGHT, 100);
		pagedDefinition.addColumnDefinition("servicing.fee", I18N.message("servicing.fee"), String.class, Align.RIGHT, 100);
		pagedDefinition.addColumnDefinition("commission", I18N.message("commission"), String.class, Align.RIGHT, 100);
		pagedDefinition.addColumnDefinition("penalty.amount", I18N.message("penalty.amount"), String.class, Align.RIGHT, 120);
		pagedDefinition.addColumnDefinition("payment.method", I18N.message("payment.method"), String.class, Align.LEFT, 120);
		pagedDefinition.addColumnDefinition("collection.officer", I18N.message("collection.officer"), String.class, Align.LEFT, 140);
		
		pagedDefinition.addColumnDefinition("collection.task", I18N.message("collection.task"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("collection.group", I18N.message("collection.group"), String.class, Align.LEFT, 150);
			
		EntityPagedDataProvider<CollectionIncentiveReport> pagedDataProvider = new EntityPagedDataProvider<>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected CollectionIncentiveReport getEntity() {
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected AbstractSearchPanel<CollectionIncentiveReport> createSearchPanel() {
		return new CollectionIncentiveSearchPanel(this);
	}
	
	/**
	 * @author buntha.chea
	 */
	private class CollectionIncentionRowRenderer implements RowRenderer {

		/**
		 * @see com.nokor.frmk.vaadin.ui.widget.table.RowRenderer#renderer(com.vaadin.data.Item, org.seuksa.frmk.model.entity.Entity)
		 */
		@SuppressWarnings("unchecked")
		@Override
		public void renderer(Item item, Entity entity) {
			CollectionIncentiveReport collectionIncentiveReport = (CollectionIncentiveReport) entity;
			Payment payment = collectionIncentiveReport.getPayment();
			List<Cashflow> cashflows = payment.getCashflows();
			Contract contract = null;
			if (cashflows != null && !cashflows.isEmpty()) {
				contract = cashflows.get(0).getContract();
			}
			double tiInstallmentAmountUsd = 0;
			double tiInsuranceFeeUsd = 0;
			double tiServicingFeeUsd = 0;
			double tiPenaltyUsd = 0;
			double tiPrincipal = 0;
			double tiInterest = 0;
			double commission = 0;
			for (Cashflow cashflow : cashflows) {
				if (cashflow.getCashflowType().equals(ECashflowType.FEE)) {
					if ("INSFEE".equals(cashflow.getService().getCode())) {
						tiInsuranceFeeUsd += cashflow.getTiInstallmentAmount();
					} else if ("SERFEE".equals(cashflow.getService().getCode())) {
						tiServicingFeeUsd += cashflow.getTiInstallmentAmount();
					} else if (Cashflow.WINFEE.equals(cashflow.getService().getCode()) || Cashflow.PAYGO.equals(cashflow.getService().getCode())) {
						commission += cashflow.getTiInstallmentAmount();
					}
				} else if (cashflow.getCashflowType().equals(ECashflowType.PEN)) {
					tiPenaltyUsd += cashflow.getTiInstallmentAmount();
				} else if (cashflow.getCashflowType().equals(ECashflowType.CAP) || cashflow.getCashflowType().equals(ECashflowType.IAP)) {
					tiInstallmentAmountUsd += cashflow.getTiInstallmentAmount();
					if (cashflow.getCashflowType().equals(ECashflowType.CAP)) {
						tiPrincipal += cashflow.getTiInstallmentAmount();
					} else{
						tiInterest += cashflow.getTiInstallmentAmount();
					}
				}
			}
			item.getItemProperty(REFERENCE).setValue(contract != null ? contract.getReference() : "");
			item.getItemProperty(PAYMENT_DATE).setValue(payment.getPaymentDate());
			item.getItemProperty(DEALER).setValue(payment.getDealer() != null ? payment.getDealer().getNameEn() : "");
			item.getItemProperty("installment.amount").setValue(AmountUtils.format(tiInstallmentAmountUsd));
			item.getItemProperty("insurance.fee").setValue(AmountUtils.format(tiInsuranceFeeUsd));
			item.getItemProperty("servicing.fee").setValue(AmountUtils.format(tiServicingFeeUsd));
			item.getItemProperty("commission").setValue(AmountUtils.format(commission));
			item.getItemProperty("penalty.amount").setValue(AmountUtils.format(tiPenaltyUsd));
			item.getItemProperty("payment.method").setValue(payment.getPaymentMethod() != null ? payment.getPaymentMethod().getDescEn() : "");
			item.getItemProperty("principal.amount").setValue(AmountUtils.format(tiPrincipal));
			item.getItemProperty("interest.amount").setValue(AmountUtils.format(tiInterest));
			item.getItemProperty("collection.status").setValue(collectionIncentiveReport.getCollectionStatus() != null ? collectionIncentiveReport.getCollectionStatus().getDescEn() : "");
			item.getItemProperty("amount.promise.to.pay").setValue(AmountUtils.format(collectionIncentiveReport.getAmountPromiseToPayUsd()));
			item.getItemProperty("start.period.promise.to.pay").setValue(collectionIncentiveReport.getStartPeriodPromiseToPay());
			item.getItemProperty("end.period.promise.to.pay").setValue(collectionIncentiveReport.getEndPeriodPromiseToPay());		
			SecUser collectionOfficer = collectionIncentiveReport.getAssignee();
			item.getItemProperty("num.overdue.in.days").setValue(collectionIncentiveReport.getNbOverdueInDay());
			item.getItemProperty("collection.officer").setValue(collectionOfficer != null ? collectionOfficer.getDesc() : "");
			item.getItemProperty("collection.task").setValue(collectionIncentiveReport.getCollectionTask() != null ? collectionIncentiveReport.getCollectionTask().getDescEn() : "");
			item.getItemProperty("collection.group").setValue(collectionIncentiveReport.getGroup() != null ? collectionIncentiveReport.getGroup().getDescEn() : "");
		}
		
	}
}
