package com.nokor.efinance.gui.ui.panel.payment;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.MyNumberUtils;
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
public class SecondPaymentReportTablePanel extends AbstractTablePanel<Payment> implements PaymentEntityField {

	private static final long serialVersionUID = -3673659939697073593L;

	@PostConstruct
	public void PostConstruct() {
		
		setCaption(I18N.message("second.payments"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("second.payments"));
		
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
		pagedDefinition.addColumnDefinition("official.payment.no", I18N.message("official.payment.no"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(LAST_NAME_EN, I18N.message("lastname.en"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(FIRST_NAME_EN , I18N.message("firstname.en"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(DEALER + "." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 140);
		pagedDefinition.addColumnDefinition("bank.name", I18N.message("bank.name"), String.class, Align.LEFT, 140);
		pagedDefinition.addColumnDefinition("account.number", I18N.message("account.number"), String.class, Align.LEFT, 140);
		pagedDefinition.addColumnDefinition("account.name", I18N.message("account.name"), String.class, Align.LEFT, 140);
		pagedDefinition.addColumnDefinition("motor.model", I18N.message("motor.model"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("motor.price", I18N.message("motor.price"), Amount.class, Align.RIGHT, 100);
		pagedDefinition.addColumnDefinition("second.payment.date", I18N.message("second.payment.date"), Date.class, Align.RIGHT, 100);
		pagedDefinition.addColumnDefinition("advance.payment.percentage", I18N.message("advance.payment.percentage"), Amount.class, Align.RIGHT, 70);
		pagedDefinition.addColumnDefinition("advance.payment", I18N.message("advance.payment"), Amount.class, Align.RIGHT, 70);
		pagedDefinition.addColumnDefinition("insurance.fee", I18N.message("insurance.fee"), Amount.class, Align.RIGHT, 70);
		pagedDefinition.addColumnDefinition("servicing.fee", I18N.message("servicing.fee"), Amount.class, Align.RIGHT, 70);
		pagedDefinition.addColumnDefinition("registration.fee", I18N.message("registration.fee"), Amount.class, Align.RIGHT, 70);
		pagedDefinition.addColumnDefinition("total.payment", I18N.message("total.payment"), Amount.class, Align.RIGHT, 70);
		pagedDefinition.addColumnDefinition("second.payment", I18N.message("second.payment"), Amount.class, Align.RIGHT, 70);
		
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
	protected SecondPaymentReportSearchPanel createSearchPanel() {
		return new SecondPaymentReportSearchPanel(this);		
	}
	
	
	private class PaymentRowRenderer implements RowRenderer, PaymentEntityField {

		@SuppressWarnings("unchecked")
		@Override
		public void renderer(Item item, Entity entity) {
			
			Payment payment = (Payment) entity;
			List<Cashflow> cashflows = payment.getCashflows();
			
			double tiRegistrationFeeUsd = 0;
			double tiInsuranceFeeUsd = 0;
			double tiServicingFeeUsd = 0;
			double tiOtherUsd = 0;
			for (Cashflow cashflow : cashflows) {
				if (cashflow.getCashflowType().equals(ECashflowType.FEE)) {
					if ("REGFEE".equals(cashflow.getService().getCode())) {
						tiRegistrationFeeUsd += cashflow.getTiInstallmentAmount();
					} else if ("INSFEE".equals(cashflow.getService().getCode())) {
						tiInsuranceFeeUsd += cashflow.getTiInstallmentAmount();
					} else if ("SERFEE".equals(cashflow.getService().getCode())) {
						tiServicingFeeUsd += cashflow.getTiInstallmentAmount();
					}
				} else if (cashflow.getCashflowType() != ECashflowType.FIN) {
					tiOtherUsd += cashflow.getTiInstallmentAmount();
				}
			}
			
			Contract contract = cashflows.get(0).getContract();
			double tiTotalPaymentUsd = MyNumberUtils.getDouble(contract.getTiAdvancePaymentAmount()) + tiInsuranceFeeUsd + tiServicingFeeUsd + tiRegistrationFeeUsd + tiOtherUsd;
			item.getItemProperty(ID).setValue(payment.getId());
			item.getItemProperty("official.payment.no").setValue(payment.getReference().replaceAll("-OR", ""));
			item.getItemProperty(LAST_NAME_EN).setValue(contract.getApplicant().getIndividual().getLastNameEn());
			item.getItemProperty(FIRST_NAME_EN).setValue(contract.getApplicant().getIndividual().getFirstNameEn());
//			item.getItemProperty(DEALER + "." + NAME_EN).setValue(contract.getDealer().getNameEn());			
//			item.getItemProperty("bank.name").setValue(payment.getDealerBankAccount() != null ? payment.getDealerBankAccount().getBank().getName() : "");
//			item.getItemProperty("account.number").setValue(payment.getDealerBankAccount() != null ? payment.getDealerBankAccount().getAccountNumber() : "");
//			item.getItemProperty("account.name").setValue(payment.getDealerBankAccount() != null ? payment.getDealerBankAccount().getAccountHolder() : "");
			item.getItemProperty("motor.model").setValue(contract.getAsset().getDescEn());
			item.getItemProperty("motor.price").setValue(AmountUtils.convertToAmount(contract.getAsset().getTiAssetPrice()));
			item.getItemProperty("second.payment.date").setValue(payment.getPaymentDate());
			item.getItemProperty("advance.payment.percentage").setValue(AmountUtils.convertToAmount(contract.getAdvancePaymentPercentage()));
			item.getItemProperty("advance.payment").setValue(AmountUtils.convertToAmount(contract.getTiAdvancePaymentAmount()));
			item.getItemProperty("insurance.fee").setValue(AmountUtils.convertToAmount(tiInsuranceFeeUsd));
			item.getItemProperty("servicing.fee").setValue(AmountUtils.convertToAmount(tiServicingFeeUsd));
			item.getItemProperty("registration.fee").setValue(AmountUtils.convertToAmount(tiRegistrationFeeUsd));
			item.getItemProperty("total.payment").setValue(AmountUtils.convertToAmount(tiTotalPaymentUsd));
			item.getItemProperty("second.payment").setValue(AmountUtils.convertToAmount(-1 * payment.getTiPaidAmount()));
		}
	}
	
}
