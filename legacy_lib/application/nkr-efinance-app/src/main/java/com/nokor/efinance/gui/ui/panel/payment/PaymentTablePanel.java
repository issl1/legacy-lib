package com.nokor.efinance.gui.ui.panel.payment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.collection.model.EPaymentChannel;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.payment.model.MPayment;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.payment.PaymentEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.RowRenderer;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.PrintClickListener;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.HeaderClickEvent;
import com.vaadin.ui.Table.HeaderClickListener;

/**
 * 
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PaymentTablePanel extends AbstractTablePanel<Payment> implements MPayment, PrintClickListener {
	
	private static final long serialVersionUID = -381729263394373499L;

	private List<Long> selectedIds = new ArrayList<>();
	private boolean selectAll = false;
	
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("payments"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("payments"));
		
		pagedTable.setColumnIcon(SELECT, FontAwesome.CHECK);
		pagedTable.addHeaderClickListener(new HeaderClickListener() {
			
			/** */
			private static final long serialVersionUID = 6354185744451187290L;

			/**
			 * @see com.vaadin.ui.Table.HeaderClickListener#headerClick(com.vaadin.ui.Table.HeaderClickEvent)
			 */
			@Override
			public void headerClick(HeaderClickEvent event) {
				if (event.getPropertyId() == SELECT) {
					selectAll = !selectAll;
					@SuppressWarnings("unchecked")
					Collection<Long> ids = (Collection<Long>) pagedTable.getItemIds();
					for (Long id : ids) {
						Item item = pagedTable.getItem(id);
						CheckBox cbSelect = (CheckBox) item.getItemProperty(SELECT).getValue();
						cbSelect.setImmediate(true);
						cbSelect.setValue(selectAll);
					}
				}
			}
		});
		
		NavigationPanel navigationPanel = addNavigationPanel();	
		navigationPanel.addEditClickListener(this);
		navigationPanel.addRefreshClickListener(this);
		navigationPanel.addPrintClickListener(this);
	}	
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createPagedDataProvider()
	 */
	@Override
	protected PagedDataProvider<Payment> createPagedDataProvider() {
		PagedDefinition<Payment> pagedDefinition = new PagedDefinition<Payment>(searchPanel.getRestrictions());
		pagedDefinition.setRowRenderer(new PaymentRowRenderer());
		pagedDefinition.addColumnDefinition(SELECT, StringUtils.EMPTY, CheckBox.class, Align.CENTER, 30);
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 50);
		pagedDefinition.addColumnDefinition(RECEIPTDATE, I18N.message("receipt.date"), Date.class, Align.LEFT, 80);
		pagedDefinition.addColumnDefinition(CHANNEL, I18N.message("channel"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(REFERENCE, I18N.message("contract.id"), String.class, Align.LEFT, 120);
		pagedDefinition.addColumnDefinition(FULLNAME, I18N.message("fullname"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(FMEntityField.BIRTH_DATE, I18N.message("dob"), Date.class, Align.LEFT, 70);
		pagedDefinition.addColumnDefinition(Payment.TIPAIDAMOUNT, I18N.message("amount"), Amount.class, Align.LEFT, 80);
		pagedDefinition.addColumnDefinition(Payment.VATPAIDAMOUNT, I18N.message("amount.vat"), Amount.class, Align.LEFT, 70);
		pagedDefinition.addColumnDefinition(RECEIPTID, I18N.message("receipt.id"), String.class, Align.LEFT, 80);
		pagedDefinition.addColumnDefinition(RECEIPTCODE, I18N.message("receipt.code"), String.class, Align.LEFT, 80);
		EntityPagedDataProvider<Payment> pagedDataProvider = new EntityPagedDataProvider<Payment>();//query data
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
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#deleteEntity(org.seuksa.frmk.model.entity.Entity)
	 */
	@Override
	protected void deleteEntity(Entity entity) {
		ENTITY_SRV.changeStatusRecord((Payment) entity, EStatusRecord.RECYC);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected PaymentSearchPanel createSearchPanel() {
		return new PaymentSearchPanel(this);		
	}
	
	/**
	 * 
	 * @author uhout.cheng
	 */
	private class PaymentRowRenderer implements RowRenderer, PaymentEntityField {

		@SuppressWarnings("unchecked")
		@Override
		public void renderer(Item item, Entity entity) {
			
			Payment payment = (Payment) entity;
			
			Contract contract = payment.getContract();
			Date dob = null;
			if (contract != null) {
				Applicant app = contract.getApplicant();
				Individual ind = app.getIndividual();
				dob = ind.getBirthDate();
			}
			
			String channel = StringUtils.EMPTY;
			List<Cashflow> cashflows = payment.getCashflows();
			if (cashflows != null & !cashflows.isEmpty()) {
				EPaymentChannel paymentChannel = cashflows.get(0).getPaymentChannel();
				if (paymentChannel != null) {
					channel = paymentChannel.getDescLocale();
				}
			}
			
			item.getItemProperty(SELECT).setValue(getRenderSelected(payment.getId()));			
			item.getItemProperty(ID).setValue(payment.getId());
			item.getItemProperty(RECEIPTDATE).setValue(payment.getPaymentDate());
			item.getItemProperty(CHANNEL).setValue(channel);
			item.getItemProperty(REFERENCE).setValue(contract != null ? contract.getReference() : "");
			item.getItemProperty(FULLNAME).setValue(payment.getApplicant() == null ? StringUtils.EMPTY : payment.getApplicant().getNameLocale());
			item.getItemProperty(FMEntityField.BIRTH_DATE).setValue(dob);
			item.getItemProperty(Payment.TIPAIDAMOUNT).setValue(AmountUtils.convertToAmount(MyNumberUtils.getDouble(payment.getTiPaidAmount())));
			item.getItemProperty(Payment.VATPAIDAMOUNT).setValue(AmountUtils.convertToAmount(MyNumberUtils.getDouble(payment.getVatPaidAmount())));
			item.getItemProperty(RECEIPTID).setValue(payment.getReference());
			item.getItemProperty(RECEIPTCODE).setValue(payment.getPaymentType().getCode());
		}
	}
	
	/**
	 * @param payId
	 * @return
	 */
	private CheckBox getRenderSelected(Long payId) {
		final CheckBox checkBox = new CheckBox();
		checkBox.setImmediate(true);
		checkBox.setData(payId);
		checkBox.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = -5783771282507156427L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				Long id = (Long) checkBox.getData();
				if (checkBox.getValue()) {
					selectedIds.add(id);
				} else {
					selectedIds.remove(id);
				}
			}
		});
		return checkBox;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.toolbar.event.PrintClickListener#printButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void printButtonClick(ClickEvent event) {
		
	}
}
