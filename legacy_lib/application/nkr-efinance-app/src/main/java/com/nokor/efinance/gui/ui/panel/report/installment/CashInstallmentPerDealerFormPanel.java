package com.nokor.efinance.gui.ui.panel.report.installment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.payment.model.EPaymentType;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.payment.service.PaymentService;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.workflow.PaymentWkfStatus;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Cash installment payment per dealer form panel
 * @author bunlong.taing
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CashInstallmentPerDealerFormPanel extends AbstractFormPanel implements CashflowEntityField, ClickListener {
	
	/** */
	private static final long serialVersionUID = 7599131636782748854L;
	
	private Button btnSave;
	private TextField txtTotalAmount;
	private CheckBox cbCheck;
	
	//private AutoDateField dfProcessDate;
	
	private SimplePagedTable<Payment> pagedTable;
	private List<MyCheckBox> lsMyCheckBox;
	
	private PaymentService paymentService = SpringUtils.getBean(PaymentService.class);
	
	private Dealer dealer;
	private Date date;
	
	/**
	 * 
	 */
	@PostConstruct
	public void PostConstruct() {
        super.init();
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		btnSave = ComponentFactory.getButton("save");
		btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/save.png"));
		btnSave.addClickListener(this);
		txtTotalAmount = ComponentFactory.getTextField("total.amount", false, 100, 100);
		txtTotalAmount.setEnabled(false);
		pagedTable = new SimplePagedTable<Payment>(createColumnDefinition());
		
		VerticalLayout content = new VerticalLayout();
		content.setSpacing(true);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		horizontalLayout.addComponent(new FormLayout(btnSave));
		horizontalLayout.addComponent(new FormLayout(txtTotalAmount));
		
		content.addComponent(horizontalLayout);
		content.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_RIGHT);
		content.addComponent(pagedTable);
		content.addComponent(pagedTable.createControls());
		
		return content;
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (lsMyCheckBox != null && !lsMyCheckBox.isEmpty()) {
			for (MyCheckBox myCheckBox : lsMyCheckBox) {
				if (!myCheckBox.getCheckBox().getValue()) {
					Payment payment = myCheckBox.getPayment();
					paymentService.cancelPayment(payment);
				}
			}
			MessageBox mb = new MessageBox(UI.getCurrent(), "400px", "160px", I18N.message("information"),
					MessageBox.Icon.INFO, I18N.message("save.successfully"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
			assignValue(dealer, date);
		}
	}
	
	/**
	 * Create the ColumnDefinition
	 * @return List of ColumnDefinition
	 */
	private List<ColumnDefinition> createColumnDefinition () {
		List<ColumnDefinition> columnDefinitions = new ArrayList<>();
		
		columnDefinitions.add(new ColumnDefinition("check", I18N.message("check"), CheckBox.class, Align.LEFT, 30));
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 40));
		columnDefinitions.add(new ColumnDefinition("contract.date", I18N.message("contract.date"), Date.class, Align.LEFT, 90));
		columnDefinitions.add(new ColumnDefinition("contract", I18N.message("contract"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("installment.receipt", I18N.message("installment.receipt"), String.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition(LAST_NAME_EN, I18N.message("lastname.en"), String.class, Align.LEFT, 90));
		columnDefinitions.add(new ColumnDefinition(FIRST_NAME_EN , I18N.message("firstname.en"), String.class, Align.LEFT, 90));
		columnDefinitions.add(new ColumnDefinition(DEALER + "." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition("motor.model", I18N.message("motor.model"), String.class, Align.LEFT, 85));
		columnDefinitions.add(new ColumnDefinition(PAYMENT_DATE, I18N.message("payment.date"), Date.class, Align.LEFT, 90));
		columnDefinitions.add(new ColumnDefinition("receiver", I18N.message("receiver"), String.class, Align.LEFT, 90));
		columnDefinitions.add(new ColumnDefinition("installment.number", I18N.message("No"), Integer.class, Align.CENTER, 30));
		columnDefinitions.add(new ColumnDefinition("installment.amount", I18N.message("installment.amount"), Amount.class, Align.RIGHT, 115));
		columnDefinitions.add(new ColumnDefinition("principal.amount", I18N.message("principal.amount"), Amount.class, Align.RIGHT, 105));
		columnDefinitions.add(new ColumnDefinition("interest.amount", I18N.message("interest.amount"), Amount.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition("insurance.fee", I18N.message("insurance.fee"), Amount.class, Align.RIGHT, 80));
		columnDefinitions.add(new ColumnDefinition("servicing.fee", I18N.message("servicing.fee"), Amount.class, Align.RIGHT, 80));
		columnDefinitions.add(new ColumnDefinition("no.penalty.days", I18N.message("no.penalty.days"), Integer.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("penalty.amount", I18N.message("penalty.amount"), Amount.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition("total.payment", I18N.message("total.payment"), Amount.class, Align.RIGHT, 100));
		columnDefinitions.add(new ColumnDefinition("commission", I18N.message("commission"), Amount.class, Align.RIGHT, 80));
		columnDefinitions.add(new ColumnDefinition("total.amount", I18N.message("total.amount"), Amount.class, Align.RIGHT, 90));
		columnDefinitions.add(new ColumnDefinition("payment.method", I18N.message("payment.method"), String.class, Align.RIGHT, 120));
		columnDefinitions.add(new ColumnDefinition("id.wing", I18N.message("id.wing"), String.class, Align.RIGHT, 140));
		
		return columnDefinitions;
	}
	
	/**
	 * Get the BaseRestriction
	 * @param dealer
	 * @param date
	 * @return
	 */
	private BaseRestrictions<Payment> getRestrictions (Dealer dealer, Date date) {
		BaseRestrictions<Payment> restrictions = new BaseRestrictions<Payment>(Payment.class);
		
		restrictions.addCriterion(Restrictions.eq(PAYMENT_TYPE, EPaymentType.IRC));
		restrictions.addCriterion(Restrictions.eq(PAYMENT_STATUS, PaymentWkfStatus.PAI));
		restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));
		restrictions.addCriterion(Restrictions.ge(PAYMENT_DATE, DateUtils.getDateAtBeginningOfDay(date)));
		restrictions.addCriterion(Restrictions.le(PAYMENT_DATE, DateUtils.getDateAtEndOfDay(date)));
		
		return restrictions;
	}
	
	/**
	 * Set the table index container
	 * @param payments
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<Payment> payments) {
		Indexed indexedContainer = pagedTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		lsMyCheckBox = new ArrayList<>();
		if (payments != null) {
			for (Payment payment : payments) {
				List<Cashflow> cashflows = payment.getCashflows();
				if (cashflows == null || cashflows.isEmpty()) {
					continue;
				}
				
				double tiInstallmentAmountUsd = 0;
				double tiPrincipalAmountUsd = 0;
				double tiInterestAmountUsd = 0;
				double tiInsuranceFeeUsd = 0;
				double tiPenaltyAmountUsd = 0;
				double tiServicingFeeUsd = 0;
				double commission = 0;
				int installmentNumber = 0;
				for (Cashflow cashflow : cashflows) {
					if (cashflow.getCashflowType().equals(ECashflowType.FEE)) {
						if (Cashflow.INSFEE.equals(cashflow.getService().getCode())) {
							tiInsuranceFeeUsd += cashflow.getTiInstallmentAmount();
						} else if (Cashflow.SERFEE.equals(cashflow.getService().getCode())) {
							tiServicingFeeUsd += cashflow.getTiInstallmentAmount();
						} else if (Cashflow.WINFEE.equals(cashflow.getService().getCode()) || Cashflow.PAYGO.equals(cashflow.getService().getCode())) {
							commission += cashflow.getTiInstallmentAmount();
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
				
				cbCheck = new CheckBox();
				cbCheck.setValue(true);
				cbCheck.addValueChangeListener(getValueChangeListener());
				//dfProcessDate = ComponentFactory.getAutoDateField();
				//dfProcessDate.setValue(DateUtils.today());
				
				Contract contract = cashflows.get(0).getContract();
				double tiTotalPaymentUsd = tiInstallmentAmountUsd + tiInsuranceFeeUsd + tiServicingFeeUsd + tiPenaltyAmountUsd;
				Item item = indexedContainer.addItem(payment.getId());
				item.getItemProperty(ID).setValue(payment.getId());
				item.getItemProperty("check").setValue(cbCheck);
				//item.getItemProperty("process.date").setValue(dfProcessDate);
				item.getItemProperty("contract.date").setValue(contract.getSigatureDate());
				item.getItemProperty("contract").setValue(contract.getReference());
				item.getItemProperty("installment.receipt").setValue(payment.getReference());
				item.getItemProperty(LAST_NAME_EN).setValue(contract.getApplicant().getIndividual().getLastNameEn());
				item.getItemProperty(FIRST_NAME_EN).setValue(contract.getApplicant().getIndividual().getFirstNameEn());
				item.getItemProperty(DEALER + "." + NAME_EN).setValue(payment.getDealer() != null ? payment.getDealer().getNameEn() : "");
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
				item.getItemProperty("commission").setValue(AmountUtils.convertToAmount(commission));
				item.getItemProperty("total.amount").setValue(AmountUtils.convertToAmount(tiTotalPaymentUsd + commission));
				item.getItemProperty("payment.method").setValue(payment.getPaymentMethod().getDescEn());
				item.getItemProperty("id.wing").setValue(payment.getExternalReference());
				
				MyCheckBox myCheckBox = new MyCheckBox(cbCheck, payment, tiTotalPaymentUsd);
				lsMyCheckBox.add(myCheckBox);
			}
		}
		pagedTable.refreshContainerDataSource();
	}
	
	/**
	 * Value change listener of CheckBox
	 * @return
	 */
	private ValueChangeListener getValueChangeListener() {
		return new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = -1879281291554762673L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				findTotalAmount();
			}
		};
	}
	
	/**
	 * Find the total amount
	 */
	private void findTotalAmount () {
		double total = 0d;
		if (lsMyCheckBox != null) {
			for (MyCheckBox myCheckBox : lsMyCheckBox) {
				if (myCheckBox.getCheckBox().getValue()){
					total += myCheckBox.getTotalAmount();
				}
			}
		}
		DecimalFormat df = new DecimalFormat("0.00");
		txtTotalAmount.setValue(df.format(total));
	}
	
	/**
	 * Assign the value to the form
	 * @param dealer
	 * @param date
	 */
	public void assignValue (Dealer dealer, Date date) {
		this.dealer = dealer;
		this.date = date;
		setIndexedContainer(ENTITY_SRV.list(getRestrictions(dealer, date)));
		findTotalAmount();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
	 */
	@Override
	protected Entity getEntity() {
		return null;
	}
	
	/**
	 * @author bunlong.taing
	 */
	private class MyCheckBox {
		
		private double totalAmount;
		private CheckBox checkBox;
		private Payment payment;
		//private AutoDateField processDate;
		
		/**
		 * 
		 * @param ch
		 * @param pm
		 * @param amount
		 */
		public MyCheckBox(CheckBox ch, Payment pm, double amount) {
			setCheckBox(ch);
			setPayment(pm);
			setTotalAmount(amount);
		}
		
		/**
		 * @return the checkBox
		 */
		public CheckBox getCheckBox() {
			return checkBox;
		}
		
		/**
		 * @param checkBox the checkBox to set
		 */
		public void setCheckBox(CheckBox checkBox) {
			this.checkBox = checkBox;
		}
		
		/**
		 * @return the totalAmount
		 */
		public double getTotalAmount() {
			return totalAmount;
		}
		/**
		 * @param totalAmount the totalAmount to set
		 */
		public void setTotalAmount(double totalAmount) {
			this.totalAmount = totalAmount;
		}
		
		/**
		 * @return the payment
		 */
		public Payment getPayment() {
			return payment;
		}
		
		/**
		 * @param payment the payment to set
		 */
		public void setPayment(Payment payment) {
			this.payment = payment;
		}
	}
}
