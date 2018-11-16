package com.nokor.efinance.gui.ui.panel.report.installment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.payment.service.PaymentService;
import com.nokor.efinance.core.shared.payment.PaymentEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.component.NumberField;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Cash installment payment table panel 
 * @author sok.vina
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CashInstallmentPaymentReportTablePanel extends AbstractTablePanel<Payment> implements PaymentEntityField {
	
	/** */
	private static final long serialVersionUID = -3673659939697073593L;
	
	private List<PaymentDetail> lstPaymentDetail;
	private Button btnSave;
	private NumberField txtTotalAmount;
	private boolean needRefresh = false;

	/**
	 * 
	 */
	@PostConstruct
	public void PostConstruct() {
		
		setCaption(I18N.message("installment.payments"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("installment.payments"));
		
		needRefresh = false;
		
		NavigationPanel navigationPanel = addNavigationPanel();	
		navigationPanel.addRefreshClickListener(this);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createBeforeTablePanel()
	 */
	@Override
	protected Panel createBeforeTablePanel() {
		Panel panel = new Panel();
		panel.setStyleName(Reindeer.PANEL_LIGHT);
		VerticalLayout verticalLayout = new VerticalLayout();
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		btnSave = ComponentFactory.getButton("save");
		btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/save.png"));
		btnSave.addClickListener(getButtonClickListener());
		horizontalLayout.addComponent(btnSave);
		horizontalLayout.addComponent(ComponentFactory.getLabel("total.amount"));
		txtTotalAmount = ComponentFactory.getNumberField("", false, 100, 100);
		txtTotalAmount.setEnabled(false);
		horizontalLayout.addComponent(txtTotalAmount);
		
		verticalLayout.addComponent(horizontalLayout);
		verticalLayout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_RIGHT);
		panel.setContent(verticalLayout);
		
		return panel;
	}
	
	/**
	 * 
	 * @return
	 */
	private ClickListener getButtonClickListener () {
		return new ClickListener() {
			/** */
			private static final long serialVersionUID = 7782893276902343244L;
			@Override
			public void buttonClick(ClickEvent event) {
				PaymentService paymentService = SpringUtils.getBean(PaymentService.class);
				if (lstPaymentDetail != null && !lstPaymentDetail.isEmpty()) {
					for (PaymentDetail paymentDetail : lstPaymentDetail) {
						if (!paymentDetail.getChechBox().getValue()) {
							Payment payment = paymentDetail.getPayment();
							paymentService.cancelPayment(payment);
						}
					}
					MessageBox mb = new MessageBox(UI.getCurrent(), "400px", "160px", I18N.message("information"),
							MessageBox.Icon.INFO, I18N.message("save.successfully"), Alignment.MIDDLE_RIGHT,
							new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
					mb.show();
					refresh();
				}
			}
		};
	}
	
	/**
	 * 
	 * @return
	 */
	private ValueChangeListener getValueChangeListener () {
		return new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = -5936412985901433234L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				calculateTotalAmount();
			}
		};
	}
	
	/**
	 * Calculate the total amount
	 */
	private void calculateTotalAmount () {
		Double total = Double.valueOf(0);
		if (lstPaymentDetail != null) {
			for (PaymentDetail value : lstPaymentDetail) {
				if (value.getChechBox().getValue()) {
					total += value.getTiTotalPaymentUsd() + value.getCommission();
				}
			}
		}
		DecimalFormat df = new DecimalFormat("##0.00");
		txtTotalAmount.setValue(df.format(total));
	}
	
	/**
	 * Get Paged definition
	 * @return
	 */
	@Override
	protected PagedDataProvider<Payment> createPagedDataProvider() {
		
		PagedDefinition<Payment> pagedDefinition = new PagedDefinition<Payment>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition("check", I18N.message("check"), CheckBox.class, Align.CENTER, 45);
		//pagedDefinition.addColumnDefinition("process.date", I18N.message("process.date"), Date.class, Align.LEFT, 80);
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 40);
		pagedDefinition.addColumnDefinition("contract.date", I18N.message("contract.date"), Date.class, Align.LEFT, 90);
		pagedDefinition.addColumnDefinition("contract", I18N.message("contract"), String.class, Align.LEFT, 130);
		pagedDefinition.addColumnDefinition("installment.receipt", I18N.message("installment.receipt"), String.class, Align.LEFT, 130);
		pagedDefinition.addColumnDefinition(LAST_NAME_EN, I18N.message("lastname.en"), String.class, Align.LEFT, 90);
		pagedDefinition.addColumnDefinition(FIRST_NAME_EN , I18N.message("firstname.en"), String.class, Align.LEFT, 90);
		pagedDefinition.addColumnDefinition(DEALER + "." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 140);
		pagedDefinition.addColumnDefinition("motor.model", I18N.message("motor.model"), String.class, Align.LEFT, 90);
		pagedDefinition.addColumnDefinition(PAYMENT_DATE, I18N.message("payment.date"), Date.class, Align.LEFT, 80);
		pagedDefinition.addColumnDefinition("receiver", I18N.message("receiver"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("installment.number", I18N.message("No"), Integer.class, Align.CENTER, 45);
		pagedDefinition.addColumnDefinition("installment.amount", I18N.message("installment.amount"), Amount.class, Align.RIGHT, 110);
		pagedDefinition.addColumnDefinition("principal.amount", I18N.message("principal.amount"), Amount.class, Align.RIGHT, 100);
		pagedDefinition.addColumnDefinition("interest.amount", I18N.message("interest.amount"), Amount.class, Align.RIGHT, 100);
		pagedDefinition.addColumnDefinition("insurance.fee", I18N.message("insurance.fee"), Amount.class, Align.RIGHT, 90);
		pagedDefinition.addColumnDefinition("servicing.fee", I18N.message("servicing.fee"), Amount.class, Align.RIGHT, 90);
		pagedDefinition.addColumnDefinition("no.penalty.days", I18N.message("no.penalty.days"), Integer.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("penalty.amount", I18N.message("penalty.amount"), Amount.class, Align.RIGHT, 100);
		pagedDefinition.addColumnDefinition("total.payment", I18N.message("total.payment"), Amount.class, Align.RIGHT, 90);
		pagedDefinition.addColumnDefinition("commission", I18N.message("commission"), Amount.class, Align.RIGHT, 70);
		pagedDefinition.addColumnDefinition("total.amount", I18N.message("total.amount"), Amount.class, Align.RIGHT, 90);
		pagedDefinition.addColumnDefinition("payment.method", I18N.message("payment.method"), String.class, Align.RIGHT, 100);
		pagedDefinition.addColumnDefinition("id.wing", I18N.message("id.wing"), String.class, Align.RIGHT, 140);
		
		PaymentPagedDataProvider<Payment> pagedDataProvider = new PaymentPagedDataProvider<Payment>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
	
	/**
	 * @author bunlong.taing
	 * @param <T>
	 */
	private class PaymentPagedDataProvider<T> extends EntityPagedDataProvider<Payment> {
		/** */
		private static final long serialVersionUID = -5515736145662778914L;
		private IndexedContainer indexedContainer;

		/**
		 * @see com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider#getIndexedContainer(java.lang.Integer, java.lang.Integer)
		 */
		@Override
		public IndexedContainer getIndexedContainer(Integer firstResult, Integer maxResults) {
			indexedContainer = new IndexedContainer();

			// Fetch data 
			if (needRefresh) {
				List<Payment> payments = fetchPayments();
				lstPaymentDetail = createListPaymentDetail(payments);
				needRefresh = false;
			}
			
			// Build columns names
			for (ColumnDefinition columnDefinition : getPagedDefinition().getColumnDefinitions()) {
				indexedContainer.addContainerProperty(columnDefinition.getPropertyId(), columnDefinition.getPropertyType(), null);
			}
			
			if (lstPaymentDetail == null || lstPaymentDetail.isEmpty()) {
				return indexedContainer;
			}
			
			// Build rows data
			int index = firstResult;
			while (index < lstPaymentDetail.size()
					&& index < firstResult + maxResults) {
				
				createRow(indexedContainer, lstPaymentDetail.get(index));
				index++;
			}
			
			return indexedContainer;
		}
		
		/**
		 * Fetch payments
		 */
		private List<Payment> fetchPayments() {
			try {
				BaseRestrictions<Payment> baseRestrictions = getPagedDefinition().getRestrictions();
				List<Payment> payments = ENTITY_SRV.list(baseRestrictions);
				return payments;
			} catch (Exception e) {
				logger.error("Error at FecthEntities", e);
				return new ArrayList<>();
			}
		}
		
		/**
		 * @see com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider#getTotalRecords()
		 */
		@Override
		public long getTotalRecords() {
			BaseRestrictions<Payment> baseRestrictions = getPagedDefinition().getRestrictions();
			List<Payment> entities = ENTITY_SRV.list(baseRestrictions);
			if (entities != null && !entities.isEmpty()) {
				return entities.size();
			}
			return 0;
		}
	}
	
	/**
	 * Create a list of paymentDetail
	 * @param payments
	 * @return
	 */
	private List<PaymentDetail> createListPaymentDetail(List<Payment> payments) {
		List<PaymentDetail> paymentDetails = new ArrayList<>();
		if (payments == null) {
			return paymentDetails;
		}
		
		for (Payment payment : payments) {
			PaymentDetail payDetail = createPaymentDetail(payment);
			if (payDetail == null) {
				continue;
			}
			paymentDetails.add(payDetail);
		}
		
		return paymentDetails;
	}
	
	/**
	 * Create a PaymentDetail
	 * @param payment
	 * @return
	 */
	private PaymentDetail createPaymentDetail (Payment payment) {
		double tiInstallmentAmountUsd = 0;
		double tiPrincipalAmountUsd = 0;
		double tiInterestAmountUsd = 0;
		double tiInsuranceFeeUsd = 0;
		double tiPenaltyAmountUsd = 0;
		double tiServicingFeeUsd = 0;
		double commission = 0;
		int installmentNumber = 0;
		
		List<Cashflow> cashflows = payment.getCashflows();
		if (cashflows == null || cashflows.isEmpty()) {
			return null;
		}
		
		PaymentDetail paymentDetail = new PaymentDetail();
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
		Contract contract = cashflows.get(0).getContract();
		
		CheckBox checkBox = new CheckBox();
		checkBox.addValueChangeListener(getValueChangeListener());
		checkBox.setValue(true);
		
		paymentDetail.setChechBox(checkBox);
		paymentDetail.setPayment(payment);
		paymentDetail.setDealerName(payment.getDealer().getNameEn());
		paymentDetail.setSigatureDate(contract.getSigatureDate());
		paymentDetail.setReference(contract.getReference());
		paymentDetail.setFirstName(contract.getApplicant().getIndividual().getFirstNameEn());
		paymentDetail.setLastName(contract.getApplicant().getIndividual().getLastNameEn());
		paymentDetail.setModel(contract.getAsset().getDescEn());
		paymentDetail.setReceiver(payment.getReceivedUser() != null ? payment.getReceivedUser().getDesc() : "");
		
		paymentDetail.setTiInstallmentAmountUsd(tiInstallmentAmountUsd);
		paymentDetail.setTiInsuranceFeeUsd(tiInsuranceFeeUsd);
		paymentDetail.setTiServicingFeeUsd(tiServicingFeeUsd);
		paymentDetail.setTiPenaltyAmountUsd(tiPenaltyAmountUsd);
		paymentDetail.setCommission(commission);
		paymentDetail.setTiPrincipalAmountUsd(tiPrincipalAmountUsd);
		paymentDetail.setTiInterestAmountUsd(tiInterestAmountUsd);
		paymentDetail.setInstallmentNumber(installmentNumber);
		paymentDetail.setPaymentMethodDesc(payment.getPaymentMethod().getDescEn());
		
		return paymentDetail;
	}
	
	/**
	 * Create a row in table
	 * @param indexedContainer
	 * @param paymentDetail
	 */
	@SuppressWarnings("unchecked")
	private void createRow(IndexedContainer indexedContainer, PaymentDetail paymentDetail) {
		Item item = indexedContainer.addItem(paymentDetail.getPayment().getId());
		
		item.getItemProperty("check").setValue(paymentDetail.getChechBox());
		//item.getItemProperty("process.date").setValue(paymentDetail.getDate());
		item.getItemProperty(ID).setValue(paymentDetail.getPayment().getId());
		item.getItemProperty("contract.date").setValue(paymentDetail.getSigatureDate());
		item.getItemProperty("contract").setValue(paymentDetail.getReference());
		item.getItemProperty("installment.receipt").setValue(paymentDetail.getPayment().getReference());
		item.getItemProperty(LAST_NAME_EN).setValue(paymentDetail.getLastName());
		item.getItemProperty(FIRST_NAME_EN).setValue(paymentDetail.getFirstName());
		item.getItemProperty(DEALER + "." + NAME_EN).setValue(paymentDetail.getDealerName() != null ? paymentDetail.getDealerName() : "");
		item.getItemProperty("motor.model").setValue(paymentDetail.getModel());
		item.getItemProperty(PAYMENT_DATE).setValue(paymentDetail.getPayment().getPaymentDate());
		item.getItemProperty("receiver").setValue(paymentDetail.getReceiver());
		item.getItemProperty("installment.number").setValue(paymentDetail.getInstallmentNumber());
		item.getItemProperty("installment.amount").setValue(AmountUtils.convertToAmount(paymentDetail.getTiInstallmentAmountUsd()));
		item.getItemProperty("principal.amount").setValue(AmountUtils.convertToAmount(paymentDetail.getTiPrincipalAmountUsd()));
		item.getItemProperty("interest.amount").setValue(AmountUtils.convertToAmount(paymentDetail.getTiInterestAmountUsd()));
		item.getItemProperty("insurance.fee").setValue(AmountUtils.convertToAmount(paymentDetail.getTiInsuranceFeeUsd()));
		item.getItemProperty("servicing.fee").setValue(AmountUtils.convertToAmount(paymentDetail.getTiServicingFeeUsd()));
		item.getItemProperty("no.penalty.days").setValue(paymentDetail.getPayment().getNumPenaltyDays());
		item.getItemProperty("penalty.amount").setValue(AmountUtils.convertToAmount(paymentDetail.getTiPenaltyAmountUsd()));
		item.getItemProperty("total.payment").setValue(AmountUtils.convertToAmount(paymentDetail.getTiTotalPaymentUsd()));
		item.getItemProperty("commission").setValue(AmountUtils.convertToAmount(paymentDetail.getCommission()));
		item.getItemProperty("payment.method").setValue(paymentDetail.getPaymentMethodDesc());
		item.getItemProperty("total.amount").setValue(AmountUtils.convertToAmount(paymentDetail.getTiTotalPaymentUsd() + paymentDetail.getCommission()));
		item.getItemProperty("id.wing").setValue(paymentDetail.getPayment().getExternalReference());
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#refresh()
	 */
	@Override
	public void refresh() {
		lstPaymentDetail = null;
		needRefresh = true;
		super.refresh();
		calculateTotalAmount();
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
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#deleteEntity(org.seuksa.frmk.Entity.model.entity.Entity)
	 */
	@Override
	protected void deleteEntity(Entity entity) {
		ENTITY_SRV.changeStatusRecord((Payment) entity, EStatusRecord.RECYC);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected CashInstallmentPaymentReportSearchPanel createSearchPanel() {
		return new CashInstallmentPaymentReportSearchPanel(this);		
	}
	
	/**
	 * @author bunlong.taing
	 */
	private class PaymentDetail {
		
		private Payment payment;
		private String dealerName;
		private CheckBox chechBox;
		private Date sigatureDate;
		private String reference;
		private String lastName;
		private String firstName;
		private String model;
		private String receiver;
		
		private double tiInstallmentAmountUsd = 0;
		private double tiPrincipalAmountUsd = 0;
		private double tiInterestAmountUsd = 0;
		private double tiInsuranceFeeUsd = 0;
		private double tiServicingFeeUsd = 0;
		private double commission = 0;
		private double tiPenaltyAmountUsd = 0;
		private int installmentNumber = 0;
		private String paymentMethodDesc;
		
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
		
		/**
		 * @return the dealerName
		 */
		public String getDealerName() {
			return dealerName;
		}
		
		/**
		 * @param dealerName the dealerName to set
		 */
		public void setDealerName(String dealerName) {
			this.dealerName = dealerName;
		}
		
		/**
		 * @return the chechBox
		 */
		public CheckBox getChechBox() {
			return chechBox;
		}
		
		/**
		 * @param chechBox the chechBox to set
		 */
		public void setChechBox(CheckBox chechBox) {
			this.chechBox = chechBox;
		}
		
		/**
		 * @return the tiInstallmentAmountUsd
		 */
		public double getTiInstallmentAmountUsd() {
			return tiInstallmentAmountUsd;
		}
		
		/**
		 * @param tiInstallmentAmountUsd the tiInstallmentAmountUsd to set
		 */
		public void setTiInstallmentAmountUsd(double tiInstallmentAmountUsd) {
			this.tiInstallmentAmountUsd = tiInstallmentAmountUsd;
		}
		
		/**
		 * @return the tiInsuranceFeeUsd
		 */
		public double getTiInsuranceFeeUsd() {
			return tiInsuranceFeeUsd;
		}
		
		/**
		 * @param tiInsuranceFeeUsd the tiInsuranceFeeUsd to set
		 */
		public void setTiInsuranceFeeUsd(double tiInsuranceFeeUsd) {
			this.tiInsuranceFeeUsd = tiInsuranceFeeUsd;
		}
		
		/**
		 * @return the tiServicingFeeUsd
		 */
		public double getTiServicingFeeUsd() {
			return tiServicingFeeUsd;
		}
		
		/**
		 * @param tiServicingFeeUsd the tiServicingFeeUsd to set
		 */
		public void setTiServicingFeeUsd(double tiServicingFeeUsd) {
			this.tiServicingFeeUsd = tiServicingFeeUsd;
		}
		
		/**
		 * @return the commission
		 */
		public double getCommission() {
			return commission;
		}
		
		/**
		 * @param commission the commission to set
		 */
		public void setCommission(double commission) {
			this.commission = commission;
		}
		
		/**
		 * @return the tiPenaltyAmountUsd
		 */
		public double getTiPenaltyAmountUsd() {
			return tiPenaltyAmountUsd;
		}
		
		/**
		 * @param tiPenaltyAmountUsd the tiPenaltyAmountUsd to set
		 */
		public void setTiPenaltyAmountUsd(double tiPenaltyAmountUsd) {
			this.tiPenaltyAmountUsd = tiPenaltyAmountUsd;
		}
		
		/**
		 * @return the tiPrincipalAmountUsd
		 */
		public double getTiPrincipalAmountUsd() {
			return tiPrincipalAmountUsd;
		}
		
		/**
		 * @param tiPrincipalAmountUsd the tiPrincipalAmountUsd to set
		 */
		public void setTiPrincipalAmountUsd(double tiPrincipalAmountUsd) {
			this.tiPrincipalAmountUsd = tiPrincipalAmountUsd;
		}
		
		/**
		 * @return the tiInterestAmountUsd
		 */
		public double getTiInterestAmountUsd() {
			return tiInterestAmountUsd;
		}
		
		/**
		 * @param tiInterestAmountUsd the tiInterestAmountUsd to set
		 */
		public void setTiInterestAmountUsd(double tiInterestAmountUsd) {
			this.tiInterestAmountUsd = tiInterestAmountUsd;
		}
		
		/**
		 * @return the installmentNumber
		 */
		public int getInstallmentNumber() {
			return installmentNumber;
		}
		
		/**
		 * @param installmentNumber the installmentNumber to set
		 */
		public void setInstallmentNumber(int installmentNumber) {
			this.installmentNumber = installmentNumber;
		}
		
		/**
		 * @return
		 */
		public double getTiTotalPaymentUsd () {
			return getTiInstallmentAmountUsd() +
					getTiInsuranceFeeUsd() + 
					getTiServicingFeeUsd() +
					getTiPenaltyAmountUsd();
		}
		
		/**
		 * @return the sigatureDate
		 */
		public Date getSigatureDate() {
			return sigatureDate;
		}
		
		/**
		 * @param sigatureDate the sigatureDate to set
		 */
		public void setSigatureDate(Date sigatureDate) {
			this.sigatureDate = sigatureDate;
		}
		
		/**
		 * @return the reference
		 */
		public String getReference() {
			return reference;
		}
		
		/**
		 * @param reference the reference to set
		 */
		public void setReference(String reference) {
			this.reference = reference;
		}
		
		/**
		 * @return the lastName
		 */
		public String getLastName() {
			return lastName;
		}
		
		/**
		 * @param lastName the lastName to set
		 */
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
		
		/**
		 * @return the firstName
		 */
		public String getFirstName() {
			return firstName;
		}
		
		/**
		 * @param firstName the firstName to set
		 */
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		
		/**
		 * @return the model
		 */
		public String getModel() {
			return model;
		}
		
		/**
		 * @param model the model to set
		 */
		public void setModel(String model) {
			this.model = model;
		}
		
		/**
		 * @return the receiver
		 */
		public String getReceiver() {
			return receiver;
		}
		
		/**
		 * @param receiver the receiver to set
		 */
		public void setReceiver(String receiver) {
			this.receiver = receiver;
		}
		
		/**
		 * @return the paymentMethodDesc
		 */
		public String getPaymentMethodDesc() {
			return paymentMethodDesc;
		}
		
		/**
		 * @param paymentMethodDesc the paymentMethodDesc to set
		 */
		public void setPaymentMethodDesc(String paymentMethodDesc) {
			this.paymentMethodDesc = paymentMethodDesc;
		}		
	}
}
