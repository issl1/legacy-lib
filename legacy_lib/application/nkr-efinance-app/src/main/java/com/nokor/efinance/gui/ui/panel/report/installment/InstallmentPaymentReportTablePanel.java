package com.nokor.efinance.gui.ui.panel.report.installment;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.shared.payment.PaymentEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.RowRenderer;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Item;
import com.vaadin.ui.Table.Align;

/**
 * 
 * @author sok.vina
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InstallmentPaymentReportTablePanel extends AbstractTablePanel<Payment> implements PaymentEntityField {

	private static final long serialVersionUID = -3673659939697073593L;

	@PostConstruct
	public void PostConstruct() {
		
		setCaption(I18N.message("installment.payments"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("installment.payments"));
		
		NavigationPanel navigationPanel = addNavigationPanel();	
		navigationPanel.addRefreshClickListener(this);
	}	
	
	/**
	 * Get Paged definition
	 * @return
	 */
	@Override
	protected PagedDataProvider<Payment> createPagedDataProvider() {
		
		PagedDefinition<Payment> pagedDefinition = new PagedDefinition<Payment>(searchPanel.getRestrictions());
		pagedDefinition.setRowRenderer(new PaymentRowRenderer());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 60);
		pagedDefinition.addColumnDefinition("contract.date", I18N.message("contract.date"), Date.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("contract", I18N.message("contract"), String.class, Align.LEFT, 140);
		pagedDefinition.addColumnDefinition("installment.receipt", I18N.message("installment.receipt"), String.class, Align.LEFT, 140);
		pagedDefinition.addColumnDefinition(LAST_NAME_EN, I18N.message("lastname.en"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(FIRST_NAME_EN , I18N.message("firstname.en"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(DEALER + "." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 140);
		pagedDefinition.addColumnDefinition("motor.model", I18N.message("motor.model"), String.class, Align.LEFT, 100);		
		pagedDefinition.addColumnDefinition(PAYMENT_DATE, I18N.message("payment.date"), Date.class, Align.LEFT, 110);
		pagedDefinition.addColumnDefinition("receiver", I18N.message("receiver"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("installment.number", I18N.message("No"), Integer.class, Align.CENTER, 60);
		pagedDefinition.addColumnDefinition("installment.amount", I18N.message("installment.amount"), Amount.class, Align.RIGHT, 70);
		pagedDefinition.addColumnDefinition("principal.amount", I18N.message("principal.amount"), Amount.class, Align.RIGHT, 70);
		pagedDefinition.addColumnDefinition("interest.amount", I18N.message("interest.amount"), Amount.class, Align.RIGHT, 70);
		pagedDefinition.addColumnDefinition("insurance.fee", I18N.message("insurance.fee"), Amount.class, Align.RIGHT, 70);
		pagedDefinition.addColumnDefinition("servicing.fee", I18N.message("servicing.fee"), Amount.class, Align.RIGHT, 70);
		pagedDefinition.addColumnDefinition("no.penalty.days", I18N.message("no.penalty.days"), Integer.class, Align.LEFT, 40);
		pagedDefinition.addColumnDefinition("penalty.amount", I18N.message("penalty.amount"), Amount.class, Align.RIGHT, 70);
		pagedDefinition.addColumnDefinition("total.payment", I18N.message("total.payment"), Amount.class, Align.RIGHT, 70);
		
		EntityPagedDataProvider<Payment> pagedDataProvider = new EntityPagedDataProvider<Payment>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

		
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected Payment getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(Payment.class, id);
		}
		return null;
	}
	
	@Override
	protected void deleteEntity(Entity entity) {
		ENTITY_SRV.changeStatusRecord((Payment) entity, EStatusRecord.RECYC);
	}
	
	@Override
	protected InstallmentPaymentReportSearchPanel createSearchPanel() {
		return new InstallmentPaymentReportSearchPanel(this);		
	}
	
	
	private class PaymentRowRenderer implements RowRenderer, PaymentEntityField {

		@SuppressWarnings("unchecked")
		@Override
		public void renderer(Item item, Entity entity) {
			
			Payment payment = (Payment) entity;
			List<Cashflow> cashflows = payment.getCashflows();
			if (cashflows != null && !cashflows.isEmpty()) {
				double tiInstallmentAmountUsd = 0;
				double tiPrincipalAmountUsd = 0;
				double tiInterestAmountUsd = 0;
				double tiInsuranceFeeUsd = 0;
				double tiPenaltyAmountUsd = 0;
				double tiServicingFeeUsd = 0;
				int installmentNumber = 0;
				for (Cashflow cashflow : cashflows) {
					if (cashflow.getCashflowType().equals(ECashflowType.FEE)) {
						if ("INSFEE".equals(cashflow.getService().getCode())) {
							tiInsuranceFeeUsd += cashflow.getTiInstallmentAmount();
						} else if ("SERFEE".equals(cashflow.getService().getCode())) {
							tiServicingFeeUsd += cashflow.getTiInstallmentAmount();
						}
					} else if (cashflow.getCashflowType().equals(ECashflowType.CAP)) {
						tiPrincipalAmountUsd += cashflow.getTiInstallmentAmount();
						tiInstallmentAmountUsd += cashflow.getTiInstallmentAmount();
					} else if (cashflow.getCashflowType().equals(ECashflowType.IAP)) {
						tiInterestAmountUsd += cashflow.getTiInstallmentAmount();
						tiInstallmentAmountUsd += cashflow.getTiInstallmentAmount();
					} else if (cashflow.getCashflowType().equals(ECashflowType.PEN)) {
						tiPenaltyAmountUsd += cashflow.getTiInstallmentAmount();
					}
					installmentNumber = cashflow.getNumInstallment();
				}
				
				Contract contract = cashflows.get(0).getContract();
				double tiTotalPaymentUsd = tiInstallmentAmountUsd + tiInsuranceFeeUsd + tiServicingFeeUsd + tiPenaltyAmountUsd;
				item.getItemProperty(ID).setValue(payment.getId());
				item.getItemProperty("contract.date").setValue(contract.getSigatureDate());
				item.getItemProperty("contract").setValue(contract.getReference());
				item.getItemProperty("installment.receipt").setValue(payment.getReference());
				item.getItemProperty(LAST_NAME_EN).setValue(contract.getApplicant().getIndividual().getLastNameEn());
				item.getItemProperty(FIRST_NAME_EN).setValue(contract.getApplicant().getIndividual().getFirstNameEn());
				item.getItemProperty(DEALER + "." + NAME_EN).setValue(contract.getDealer().getNameEn());
				item.getItemProperty("motor.model").setValue(contract.getAsset().getDescEn());
				item.getItemProperty(PAYMENT_DATE).setValue(payment.getPaymentDate());
				item.getItemProperty("receiver").setValue(payment.getReceivedUser() != null ? payment.getReceivedUser().getDesc() : "");
				item.getItemProperty("installment.number").setValue(installmentNumber);
				item.getItemProperty("installment.amount").setValue(AmountUtils.convertToAmount(tiInstallmentAmountUsd));
				item.getItemProperty("principal.amount").setValue(AmountUtils.convertToAmount(tiPrincipalAmountUsd));
				item.getItemProperty("interest.amount").setValue(AmountUtils.convertToAmount(tiInterestAmountUsd));
				item.getItemProperty("insurance.fee").setValue(AmountUtils.convertToAmount(tiInsuranceFeeUsd));
				item.getItemProperty("servicing.fee").setValue(AmountUtils.convertToAmount(tiServicingFeeUsd));
				item.getItemProperty("no.penalty.days").setValue(payment.getNumPenaltyDays());
				item.getItemProperty("penalty.amount").setValue(AmountUtils.convertToAmount(tiPenaltyAmountUsd));
				item.getItemProperty("total.payment").setValue(AmountUtils.convertToAmount(tiTotalPaymentUsd));
			}
		}
	}
	
}
