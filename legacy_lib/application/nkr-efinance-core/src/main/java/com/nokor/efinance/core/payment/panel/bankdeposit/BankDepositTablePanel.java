package com.nokor.efinance.core.payment.panel.bankdeposit;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.payment.model.EPaymentType;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.workflow.PaymentWkfStatus;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Container;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * @author sok.vina
 *
 */
public class BankDepositTablePanel extends AbstractTabPanel implements View, CashflowEntityField, FrmkServicesHelper {
	
	private static final long serialVersionUID = -4618786633559261506L;
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	//private PaymentService paymentService= SpringUtils.getBean(PaymentService.class);
	
	private SimplePagedTable<Payment> pagedTable;
	private List<ColumnDefinition> columnDefinitions;
	private CheckBox cbxPayment;
	private List<CheckBox> listcbxPayments;
	private TextField txtTotalAmount;
	private Set<Long> selectedItemIds;
	private double valueTotalAmount = 0d;
	private Date date;
	private Dealer dealer;
	private Button btnSave;
	private BankDepositsPerDealerPerDayPanel bankDepositsPerDealerPerDayPanel;
	
	public BankDepositTablePanel(BankDepositsPerDealerPerDayPanel bankDepositsPerDealerPerDayPanel) {
		super();
		this.bankDepositsPerDealerPerDayPanel = bankDepositsPerDealerPerDayPanel;
		setSizeFull();
	}
	
	@Override
	protected com.vaadin.ui.Component createForm() {
		selectedItemIds = new HashSet<Long>();
		listcbxPayments = new ArrayList<CheckBox>();
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.setMargin(true);
        
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(true);
        btnSave = ComponentFactory.getButton("save");
        btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/save.png"));
        btnSave.addClickListener(getClickListener());		//Add the button listener
        txtTotalAmount = new TextField();
        txtTotalAmount.setEnabled(false);
        txtTotalAmount.setImmediate(true);
        horizontalLayout.addComponent(btnSave);
        horizontalLayout.addComponent(new Label(I18N.message("total.amount")));
        horizontalLayout.addComponent(txtTotalAmount);
        this.columnDefinitions = createColumnDefinitions();
        pagedTable = new SimplePagedTable<Payment>(this.columnDefinitions);
        contentLayout.addComponent(horizontalLayout);
        contentLayout.setComponentAlignment(horizontalLayout, Alignment.BOTTOM_RIGHT);
        contentLayout.addComponent(pagedTable);
        contentLayout.addComponent(pagedTable.createControls());
        return contentLayout;
	}
	
	private List<Payment> getListPayment(Dealer dealer, Date date) {
		BaseRestrictions<Payment> restrictions = new BaseRestrictions<Payment>(Payment.class);
		restrictions.addCriterion(Restrictions.eq(PAYMENT_TYPE, EPaymentType.IRC));
		restrictions.addCriterion(Restrictions.eq(PAYMENT_STATUS, PaymentWkfStatus.PAI));
		//restrictions.addCriterion(Restrictions.isNull("dealerPaymentDate"));
		restrictions.addCriterion(Restrictions.isNotNull("passToDealerPaymentDate"));
		
		restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));
		/*restrictions.addCriterion(Restrictions.ge(PAYMENT_DATE, DateUtils.getDateAtBeginningOfDay(date)));
		restrictions.addCriterion(Restrictions.le(PAYMENT_DATE, DateUtils.getDateAtEndOfDay(date)));*/
		restrictions.addCriterion(Restrictions.ge("passToDealerPaymentDate", DateUtils.getDateAtBeginningOfDay(date)));
		restrictions.addCriterion(Restrictions.le("passToDealerPaymentDate", DateUtils.getDateAtEndOfDay(date)));
		
		restrictions.addOrder(Order.desc("passToDealerPaymentDate"));
		return ENTITY_SRV.list(restrictions);	
	}
	
	/**
	 * 
	 * @param tab
	 * @param dealer
	 * @param date
	 */
	public void assignValues(Dealer dealer, Date date) {
		this.date = date;
		this.dealer = dealer;
		txtTotalAmount.setValue("");
		resetListCheckbox();
		setIndexedContainer(getListPayment(dealer, date));
		selectedItemIds.clear();
	}
	
	/**
	 * Get indexed container
	 * @return IndexedContainer
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<Payment> payments) {
		Indexed indexedContainer = pagedTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		int index = 0;
		double grandTotalAmount = 0d;
		for (Payment payment : payments) {
			List<Cashflow> cashflows = payment.getCashflows();
			double installmentAmount = 0d;
			double totalOtherAmount = 0d;
			double totalPenalty = 0d;
			double totalAmount = 0d;
			if (cashflows != null && !cashflows.isEmpty()) {
				for (Cashflow cashflow : cashflows) {
					/*if (cashflow.getCashflowType() == CashflowType.PER) {
						totalPenalty += cashflow.getTiInstallmentAmount();
					} else if(cashflow.getCashflowType() != CashflowType.CAP 
							&& cashflow.getCashflowType() != CashflowType.IAP
							&& cashflow.getCashflowType() != CashflowType.FEE) {
						totalOtherAmount += cashflow.getTiInstallmentAmount();
					}*/
					if (cashflow.getCashflowType().equals(ECashflowType.PEN)) {
						totalPenalty += cashflow.getTiInstallmentAmount();
					} else if (cashflow.getCashflowType().equals(ECashflowType.CAP) || cashflow.getCashflowType().equals(ECashflowType.IAP)) {
						installmentAmount += cashflow.getTiInstallmentAmount();
					} else {
						totalOtherAmount += cashflow.getTiInstallmentAmount();
					} 
				}
				//installmentAmount = payment.getTiPaidUsd();
				totalAmount = installmentAmount + totalOtherAmount + totalPenalty;
				Contract contract = cashflows.get(0).getContract();
				final Item item = indexedContainer.addItem(payment.getId());
				cbxPayment = new CheckBox();
				cbxPayment.setValue(true);
				cbxPayment.addValueChangeListener(new ValueChangeListener() {
					private static final long serialVersionUID = -2120119835501936565L;	
					@Override
					public void valueChange(ValueChangeEvent event) {						
						if (selectedItemIds.contains(Long.valueOf(item.getItemProperty(ID).getValue().toString()))) {
							Amount totalAmount = (Amount) item.getItemProperty("total.amount").getValue();
							valueTotalAmount += MyNumberUtils.getDouble(totalAmount.getTiAmount());
							selectedItemIds.remove(Long.valueOf(item.getItemProperty(ID).getValue().toString()));
			            } else {
			            	Amount totalAmount = (Amount) item.getItemProperty("total.amount").getValue();
			            	valueTotalAmount -= MyNumberUtils.getDouble(totalAmount.getTiAmount());
			            	selectedItemIds.add(Long.valueOf(item.getItemProperty(ID).getValue().toString()));
			            }
						txtTotalAmount.setValue(AmountUtils.format(+valueTotalAmount));
					}
				});
				item.getItemProperty("index").setValue(index);
				item.getItemProperty(ID).setValue(payment.getId());
				item.getItemProperty("cbxPayment").setValue(cbxPayment);
				item.getItemProperty("passToDealerDate").setValue(payment.getPassToDealerPaymentDate());
				listcbxPayments.add(cbxPayment);
				item.getItemProperty("installment.receipt").setValue(payment.getReference());
				item.getItemProperty(CONTRACT).setValue(contract.getReference());
				item.getItemProperty(LAST_NAME_EN).setValue(payment.getApplicant().getIndividual().getLastNameEn());
				item.getItemProperty(FIRST_NAME_EN).setValue(payment.getApplicant().getIndividual().getFirstNameEn());
				// TODO YLY
				// item.getItemProperty(MOBILEPHONE).setValue(payment.getApplicant().getMobilePhone());
				item.getItemProperty(DEALER + "." + NAME_EN).setValue(payment.getDealer() != null ? payment.getDealer().getNameEn() : "");
				item.getItemProperty(PAYMENT_DATE).setValue(payment.getPaymentDate());
				item.getItemProperty("no.overdue.days").setValue(payment.getNumPenaltyDays());
				item.getItemProperty("installment.amount").setValue(installmentAmount);
				item.getItemProperty("total.other.amount").setValue(totalOtherAmount);
				item.getItemProperty("total.penalty").setValue(totalPenalty);
				item.getItemProperty("total.amount").setValue(AmountUtils.convertToAmount(totalAmount));
				grandTotalAmount += totalAmount;
				index++;
			}
		} 
		txtTotalAmount.setValue(AmountUtils.format(grandTotalAmount));
		valueTotalAmount = grandTotalAmount;
		pagedTable.refreshContainerDataSource();
	}

	/**
	 * @return List of ColumnDefinition
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("index", I18N.message("index"), Integer.class, Align.LEFT, 30, false));
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 140, false));
		columnDefinitions.add(new ColumnDefinition("cbxPayment", I18N.message("check"), CheckBox.class, Align.LEFT, 40));
		columnDefinitions.add(new ColumnDefinition("passToDealerDate", I18N.message("pass.to.dealer.date"), Date.class, Align.LEFT, 115));
		columnDefinitions.add(new ColumnDefinition("installment.receipt", I18N.message("installment.receipt"), String.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition(CONTRACT, I18N.message("contract"), String.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition(LAST_NAME_EN, I18N.message("lastname.en"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(FIRST_NAME_EN , I18N.message("firstname.en"), String.class, Align.LEFT, 100));
		// TODO YLY
		// columnDefinitions.add(new ColumnDefinition(MOBILEPHONE, I18N.message("mobile.phone1"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(DEALER + "." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition(PAYMENT_DATE, I18N.message("payment.date"), Date.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("no.overdue.days", I18N.message("no.overdue.days"), Integer.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition("installment.amount", I18N.message("installment.amount"), Double.class, Align.RIGHT, 120));
		columnDefinitions.add(new ColumnDefinition("total.other.amount", I18N.message("other.amount"), Double.class, Align.RIGHT, 90));
		columnDefinitions.add(new ColumnDefinition("total.penalty", I18N.message("total.penalty"), Double.class, Align.RIGHT, 90));
		columnDefinitions.add(new ColumnDefinition("total.amount", I18N.message("total.amount"), Amount.class, Align.RIGHT, 90));
		return columnDefinitions;
	}
	
	public void resetListCheckbox() {
		if (listcbxPayments != null && !listcbxPayments.isEmpty()) {
			for (CheckBox checkbox : listcbxPayments) {
				checkbox.setValue(false);
			}	
		}
	}
	
	/**
	 * The event listener of the Save Button
	 * @return Vaadin Button Click Listener
	 */
	private ClickListener getClickListener() {
		return new ClickListener() {
			
			/** */
			private static final long serialVersionUID = -1318063236324263930L;

			@Override
			public void buttonClick(ClickEvent event) {
				//List<Long> ids = new ArrayList<>();
				Container container = pagedTable.getContainerDataSource();
				List<Payment> payments = getListPayment(dealer, date);
				if (payments != null && !payments.isEmpty()) {
					for (Payment payment : payments) {
						if (container.containsId(payment.getId())) {
							Item item = container.getItem(payment.getId());
							if (((CheckBox) item.getItemProperty("cbxPayment").getValue()).getValue() == false){
								//ids.add(payment.getId());
								payment.setPassToDealerPaymentDate(null);
								payment.setBankDeposit(null);
								ENTITY_SRV.saveOrUpdate(payment);
							}
						}
					}
				}
				//paymentService.cancelPayments(ids);
				switchTab();
			}
		};
	}
	
	/**
	 * Switch to bankDepositsPerDealerPerDayPanel Panel
	 */
	private void switchTab() {
		bankDepositsPerDealerPerDayPanel.getTabSheet().setSelectedTab(bankDepositsPerDealerPerDayPanel);
		bankDepositsPerDealerPerDayPanel.getTabSheet().removeComponent(this);
		bankDepositsPerDealerPerDayPanel.search();
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
	}	
}
