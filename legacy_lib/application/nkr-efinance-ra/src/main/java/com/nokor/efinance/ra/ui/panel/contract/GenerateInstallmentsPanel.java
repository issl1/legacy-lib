package com.nokor.efinance.ra.ui.panel.contract;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.vaadin.dialogs.ConfirmDialog;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.service.cashflow.CashflowService;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.core.payment.model.EPaymentType;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.payment.service.PaymentService;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.workflow.PaymentWkfStatus;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * @author sok.vina
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(GenerateInstallmentsPanel.NAME)
public class GenerateInstallmentsPanel extends Panel implements View, CashflowEntityField {
	
	private static final long serialVersionUID = 6227740006388204118L;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	public static final String NAME = "generate.installment";
	
	@Autowired
	private PaymentService paymentService; 
	
	@Autowired
	private CashflowService cashflowService;
	
	private AutoDateField dfInstallment;
	
	private EntityService entityService;
	
	@PostConstruct
	public void PostConstruct() {
		entityService = SpringUtils.getBean(EntityService.class);
		
		setCaption(I18N.message("generate.installment"));
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		
		dfInstallment = ComponentFactory.getAutoDateField();
		Button btnRfreshData = new Button(I18N.message("ok"));
		btnRfreshData.addClickListener(new ClickListener() {
			
			private static final long serialVersionUID = 7761470482429822091L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (dfInstallment.getValue() != null) {
					ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), "Please confirm your action !!!",
					        new ConfirmDialog.Listener() {
								private static final long serialVersionUID = 2380193173874927880L;
								public void onClose(ConfirmDialog dialog) {
					                if (dialog.isConfirmed()) {	
					                	generate(dfInstallment.getValue());
					                }
					            }
					});
					confirmDialog.setWidth("400px");
					confirmDialog.setHeight("150px");	
				}
			}
		});
		
        final GridLayout gridLayout = new GridLayout(5, 2);
		gridLayout.setMargin(true);
		gridLayout.setSpacing(true);
        int iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("date")), iCol++, 0);
        gridLayout.addComponent(dfInstallment, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(btnRfreshData, iCol++, 0);
        verticalLayout.addComponent(gridLayout);
		setContent(verticalLayout);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	private void generate(Date installmentDate) {
		
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<Cashflow>(Cashflow.class);
		restrictions.addCriterion(Restrictions.eq(PAID, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.eq(CANCEL, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.ne(CASHFLOW_TYPE, ECashflowType.FIN));
		restrictions.addCriterion(Restrictions.gt(NUM_INSTALLMENT, 0));
		restrictions.addCriterion(Restrictions.lt(INSTALLMENT_DATE, DateUtils.getDate("28022014", "ddMMyyyy")));
		restrictions.addOrder(Order.asc(INSTALLMENT_DATE));
	
		SecUser processByUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		List<Cashflow> cashflows = cashflowService.getListCashflow(restrictions);
		List<CashflowPayment> cashflowPayments = new ArrayList<CashflowPayment>();
		for (Cashflow cashflow : cashflows) {
			CashflowPayment cashflowPayment = getCashflowPayment(cashflowPayments, cashflow);
			if (cashflowPayment == null) {
				cashflowPayment = new CashflowPayment();
				cashflowPayment.setContract(cashflow.getContract());
				cashflowPayment.setInstallmentDate(cashflow.getInstallmentDate());
				cashflowPayments.add(cashflowPayment);
			}
			cashflowPayment.setTiPaymentAmountUsd(MyNumberUtils.getDouble(cashflowPayment.getTiPaymentAmountUsd()) + cashflow.getTiInstallmentAmount());
			cashflowPayment.addCashflow(cashflow);
		}
		int i = 0;
		for (CashflowPayment cashflowPayment : cashflowPayments) {
			i++;
			System.out.println("============================= Process " + i + "/" + cashflowPayments.size());
			Payment payment = new Payment();
			Contract contract = cashflowPayment.getContract();
			payment.setApplicant(contract.getApplicant());
			payment.setContract(contract);
			payment.setExternalReference("");
			payment.setPaymentDate(cashflowPayment.getInstallmentDate());
			payment.setPaymentMethod(entityService.getByCode(EPaymentMethod.class, "CASH"));
			payment.setTiPaidAmount(cashflowPayment.getTiPaymentAmountUsd());
			payment.setWkfStatus(PaymentWkfStatus.PAI);
			payment.setConfirm(payment.getPaymentMethod().isAutoConfirm());
			payment.setReceivedUser(processByUser);
			payment.setPaymentType(EPaymentType.IRC);
			payment.setDealer(contract.getDealer());
			payment.setCashflows(cashflowPayment.getCashflows());
			paymentService.createPayment(payment);
		}
	}
	
	/**
	 * @param cashflowPayments
	 * @param cashflow
	 * @return
	 */
	private CashflowPayment getCashflowPayment(List<CashflowPayment> cashflowPayments, Cashflow cashflow) {
		for (CashflowPayment cashflowPayment : cashflowPayments) {
			if (cashflowPayment.getContract().getReference().equals(cashflow.getContract().getReference())
					&& DateUtils.getDateWithoutTime(cashflowPayment.getInstallmentDate())
						.compareTo(DateUtils.getDateWithoutTime(cashflow.getInstallmentDate())) == 0) {
				return cashflowPayment;
			}
		}
		return null;
	}
	
	/**
	 * @author ly.youhort
	 */
	private class CashflowPayment implements Serializable {
		private static final long serialVersionUID = 3112339520304252300L;
		private Contract contract;
		private Date installmentDate;
		private Double tiPaymentAmountUsd;
		
		private List<Cashflow> cashflows = new ArrayList<Cashflow>();
			
		/**
		 * @return the contract
		 */
		public Contract getContract() {
			return contract;
		}
		/**
		 * @param contract the contract to set
		 */
		public void setContract(Contract contract) {
			this.contract = contract;
		}
		/**
		 * @return the installmentDate
		 */
		public Date getInstallmentDate() {
			return installmentDate;
		}
		/**
		 * @param installmentDate the installmentDate to set
		 */
		public void setInstallmentDate(Date installmentDate) {
			this.installmentDate = installmentDate;
		}
		/**
		 * @return the tiPaymentAmountUsd
		 */
		public Double getTiPaymentAmountUsd() {
			return tiPaymentAmountUsd;
		}
		/**
		 * @param tiPaymentAmountUsd the tiPaymentAmountUsd to set
		 */
		public void setTiPaymentAmountUsd(Double tiPaymentAmountUsd) {
			this.tiPaymentAmountUsd = tiPaymentAmountUsd;
		}
		public void addCashflow(Cashflow cashflow) {
			cashflows.add(cashflow);
		}
		/**
		 * @return the cashflows
		 */
		public List<Cashflow> getCashflows() {
			return cashflows;
		}
		
	}
}
