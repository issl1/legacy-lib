package com.nokor.efinance.gui.ui.panel.report.directcost;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.shared.payment.PaymentEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.RowRenderer;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.data.Item;
import com.vaadin.ui.Table.Align;

/**
 * Direct Cost payment table panel 
 * @author buntha.chea
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DirectCostReportTablePanel extends AbstractTablePanel<Payment> implements PaymentEntityField {
	
	/** */
	private static final long serialVersionUID = -3673659939697073593L;
	@PostConstruct
	public void PostConstruct() {		
		setCaption(I18N.message("direct.costs"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);				
		super.init(I18N.message("direct.costs"));
	}
	
	/**
	 * Get Paged definition
	 * @return
	 */
	@Override
	protected PagedDataProvider<Payment> createPagedDataProvider() {
		    PagedDefinition<Payment> pagedDefinition = new PagedDefinition<>(searchPanel.getRestrictions()); 
		    pagedDefinition.setRowRenderer(new PaymentRowRenderer());
			pagedDefinition.addColumnDefinition("contract", I18N.message("contract"), String.class, Align.LEFT, 140);
			pagedDefinition.addColumnDefinition(PAYMENT_DATE, I18N.message("payment.date"), Date.class, Align.LEFT, 100);
			pagedDefinition.addColumnDefinition("direct.cost", I18N.message("direct.cost"), String.class, Align.LEFT, 150);
			pagedDefinition.addColumnDefinition("amountExclVat", I18N.message("amount.excl.vat"), Amount.class, Align.RIGHT, 100);
			pagedDefinition.addColumnDefinition("vatAmount", I18N.message("vat.amount"), Amount.class, Align.RIGHT, 100);
			pagedDefinition.addColumnDefinition("amountInclVat", I18N.message("amount.incl.vat"), Amount.class, Align.RIGHT, 100);
		
			EntityPagedDataProvider<Payment> pagedDataProvider = new EntityPagedDataProvider<>();
			pagedDataProvider.setPagedDefinition(pagedDefinition);
			return pagedDataProvider;
	}
	
	/**
	 * @author youhort.ly
	 *
	 */
	private class PaymentRowRenderer implements RowRenderer {
		@SuppressWarnings("unchecked")
		@Override
		public void renderer(Item item, Entity entity) {
			Payment payment = (Payment) entity;
			if (payment != null) {
				List<Cashflow> cashflows = payment.getCashflows();
				if (cashflows != null) {
					for (Cashflow cashflow : cashflows) {
						if (EServiceType.listDirectCosts().contains(cashflow.getService().getServiceType())) {
							item.getItemProperty("contract").setValue(cashflow.getContract().getReference());	
							item.getItemProperty(PAYMENT_DATE).setValue(payment.getPaymentDate());
							item.getItemProperty("direct.cost").setValue(cashflow.getService().getDescEn());
							item.getItemProperty("amountExclVat").setValue(AmountUtils.convertToAmount(Math.abs(cashflow.getTiInstallmentAmount())));
							item.getItemProperty("vatAmount").setValue(AmountUtils.convertToAmount(Math.abs(cashflow.getVatInstallmentAmount())));
							item.getItemProperty("amountInclVat").setValue(AmountUtils.convertToAmount(Math.abs(cashflow.getTiInstallmentAmount())));
						}
					}
				}
			}
		}
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
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected DirectCostReportSearchPanel createSearchPanel() {
		return new DirectCostReportSearchPanel(this);		
	}
}
