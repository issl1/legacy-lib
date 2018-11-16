package com.nokor.efinance.gui.ui.panel.payment;

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
import com.nokor.frmk.vaadin.ui.widget.component.Timer;
import com.nokor.frmk.vaadin.ui.widget.component.Timer.TimerListener;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.RowRenderer;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Table.Align;

/**
 * 
 * @author sok.vina
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PurchaseOrderTablePanel extends AbstractTablePanel<Payment> implements PaymentEntityField {

	private static final long serialVersionUID = -3673659939697073593L;
	
	private static final String DEALER_TYPE = "dealer.type";

	@PostConstruct
	public void PostConstruct() {
		
		setCaption(I18N.message("purchase.orders"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("purchase.orders"));
		
		NavigationPanel navigationPanel = addNavigationPanel();	
		navigationPanel.addEditClickListener(this);
		navigationPanel.addRefreshClickListener(this);
		addAutoRefresh(navigationPanel);
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
		pagedDefinition.addColumnDefinition("po.number", I18N.message("po.number"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(LAST_NAME_EN, I18N.message("lastname.en"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(FIRST_NAME_EN , I18N.message("firstname.en"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(DEALER_TYPE, I18N.message("dealer.type"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(DEALER + "." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 140);
		pagedDefinition.addColumnDefinition("motor.model", I18N.message("motor.model"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("year", I18N.message("year"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("color", I18N.message("color"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("motor.price", I18N.message("motor.price"), Amount.class, Align.RIGHT, 100);
		pagedDefinition.addColumnDefinition("advance.payment.percentage", I18N.message("advance.payment.percentage"), Amount.class, Align.RIGHT, 70);
		pagedDefinition.addColumnDefinition("advance.payment", I18N.message("advance.payment"), Amount.class, Align.RIGHT, 70);
		pagedDefinition.addColumnDefinition("insurance.fee", I18N.message("insurance.fee"), Amount.class, Align.RIGHT, 70);
		pagedDefinition.addColumnDefinition("servicing.fee", I18N.message("servicing.fee"), Amount.class, Align.RIGHT, 70);
		pagedDefinition.addColumnDefinition("registration.fee", I18N.message("registration.fee"), Amount.class, Align.RIGHT, 70);
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
	protected PurchaseOrderSearchPanel createSearchPanel() {
		return new PurchaseOrderSearchPanel(this);		
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
				} else if (!cashflow.getCashflowType().equals(ECashflowType.FIN)) {
					tiOtherUsd += cashflow.getTiInstallmentAmount();
				}
			}
			
			Contract contract = cashflows.get(0).getContract();
			double tiTotalPaymentUsd = MyNumberUtils.getDouble(contract.getTiAdvancePaymentAmount()) + tiInsuranceFeeUsd + tiServicingFeeUsd + tiRegistrationFeeUsd + tiOtherUsd;
			item.getItemProperty(ID).setValue(payment.getId());
			item.getItemProperty("po.number").setValue(payment.getReference().replaceAll("-OR", ""));
			item.getItemProperty(LAST_NAME_EN).setValue(contract.getApplicant().getIndividual().getLastNameEn());
			item.getItemProperty(FIRST_NAME_EN).setValue(contract.getApplicant().getIndividual().getFirstNameEn());
			item.getItemProperty(DEALER_TYPE).setValue(contract.getDealer() != null ? contract.getDealer().getDealerType().getDesc() : "");
			item.getItemProperty(DEALER + "." + NAME_EN).setValue(contract.getDealer().getNameEn());
			item.getItemProperty("motor.model").setValue(contract.getAsset().getDescEn());
			item.getItemProperty("year").setValue(contract.getAsset().getYear().toString());
			item.getItemProperty("color").setValue(contract.getAsset().getColor().getDescEn());
			item.getItemProperty("motor.price").setValue(AmountUtils.convertToAmount(contract.getAsset().getTiAssetPrice()));
			item.getItemProperty("advance.payment.percentage").setValue(AmountUtils.convertToAmount(contract.getAdvancePaymentPercentage()));
			item.getItemProperty("advance.payment").setValue(AmountUtils.convertToAmount(contract.getTiAdvancePaymentAmount()));
			item.getItemProperty("insurance.fee").setValue(AmountUtils.convertToAmount(tiInsuranceFeeUsd));
			item.getItemProperty("servicing.fee").setValue(AmountUtils.convertToAmount(tiServicingFeeUsd));
			item.getItemProperty("registration.fee").setValue(AmountUtils.convertToAmount(tiRegistrationFeeUsd));
			item.getItemProperty("total.payment").setValue(AmountUtils.convertToAmount(tiTotalPaymentUsd));
		}
	}
	
	@SuppressWarnings("serial")
	public class AutoRefreshListener implements TimerListener {
		public final static int DEFAULT_REFRESH_RATE = 30000;
		
		@Override
		public void onTimer() {
			refresh();
		}
	}
	
	/**
	 * @param navigationPanel
	 */
	@SuppressWarnings("serial")
	private void addAutoRefresh(NavigationPanel panel) {
		final Timer timer = new Timer();
		timer.addListener(new AutoRefreshListener());
		timer.start(true, AutoRefreshListener.DEFAULT_REFRESH_RATE);
		
		final CheckBox cbAutoRefresh = new CheckBox(I18N.message("autorefresh"));
		cbAutoRefresh.setStyleName("checkbox_label_white_color");
		final ComboBox refreshRate = new ComboBox();
		cbAutoRefresh.setValue(true);
		cbAutoRefresh.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbAutoRefresh.getValue() == Boolean.TRUE) {
					timer.start(true, Integer.parseInt(refreshRate.getValue().toString()));
				} else {
					timer.stop();
				}
			}
		});
		
		refreshRate.setNullSelectionAllowed(false);
		refreshRate.addItem(AutoRefreshListener.DEFAULT_REFRESH_RATE);
		refreshRate.setItemCaption(AutoRefreshListener.DEFAULT_REFRESH_RATE, I18N.message("second.30"));
		refreshRate.addItem(60000);
		refreshRate.setItemCaption(60000, I18N.message("minute.1"));
		refreshRate.addItem(120000);
		refreshRate.setItemCaption(120000, I18N.message("minute.2"));
		refreshRate.addItem(300000);
		refreshRate.setItemCaption(300000, I18N.message("minute.5"));
		refreshRate.addItem(600000);
		refreshRate.setItemCaption(600000, I18N.message("minute.10"));
		refreshRate.addItem(1800000);
		refreshRate.setItemCaption(1800000, I18N.message("minute.30"));
		refreshRate.select(AutoRefreshListener.DEFAULT_REFRESH_RATE);
		refreshRate.select(AutoRefreshListener.DEFAULT_REFRESH_RATE);		
		refreshRate.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbAutoRefresh.getValue() == Boolean.TRUE) {
					timer.start(true, Integer.parseInt(refreshRate.getValue().toString()));
				}
			}
		});
		
		panel.addComponent(timer);
		panel.addComponent(cbAutoRefresh);
		panel.addComponent(refreshRate);
		panel.setComponentAlignment(cbAutoRefresh, Alignment.MIDDLE_RIGHT);
		panel.setComponentAlignment(refreshRate, Alignment.MIDDLE_RIGHT);
	}
}
