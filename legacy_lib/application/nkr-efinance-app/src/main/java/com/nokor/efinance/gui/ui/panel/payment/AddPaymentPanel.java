package com.nokor.efinance.gui.ui.panel.payment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.service.cashflow.CashflowService;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.payment.service.PaymentService;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.workflow.PaymentWkfStatus;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author heng.mao
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(AddPaymentPanel.NAME)
public class AddPaymentPanel extends AbstractControlPanel implements View, CashflowEntityField {
	
	private static final long serialVersionUID = -243663694664990703L;

	public static final String NAME = "add.payment";
	
	private PaymentService paymentService= SpringUtils.getBean(PaymentService.class);

	private PaymentInfoPanel paymentInfoPanel;
	private CashflowSearchPanel cashflowSearchPanel;
	private TextField txtTotalAmount;
	
	private Payment payment;
	private Contract contract;
	
	@PostConstruct
	public void PostConstruct() {

		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSizeFull();
		contentLayout.setSpacing(true);
		contentLayout.setMargin(true);
		
		Panel paymentPanel = new Panel(I18N.message("payment"));
		paymentInfoPanel = new PaymentInfoPanel();
		paymentPanel.setContent(paymentInfoPanel);
		
		Panel cashflowPanel = new Panel(I18N.message("search.cashflows"));
		cashflowSearchPanel = new CashflowSearchPanel(this);
		cashflowPanel.setContent(cashflowSearchPanel);

		HorizontalLayout horizontalLayout = new HorizontalLayout();
		txtTotalAmount = ComponentFactory.getTextField(false, 20, 150);
		txtTotalAmount.setValue("0");
		txtTotalAmount.setEnabled(false);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		NativeButton btnAllocate = new NativeButton(I18N.message("allocate"));
		navigationPanel.addButton(btnAllocate);
		navigationPanel.setSizeUndefined();
		horizontalLayout.addComponent(navigationPanel);
		horizontalLayout.addComponent(txtTotalAmount);
		
		btnAllocate.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -6661162238224070578L;
			@Override
			public void buttonClick(ClickEvent event) {
				Set<Long> selectedItemIds = cashflowSearchPanel.getSelectedItemIds();
				List<Cashflow> cashflows = new ArrayList<Cashflow>();
				final CashflowService cashflowService = SpringUtils.getBean(CashflowService.class);
				for (Long itemId : selectedItemIds) {
					//Update cash flow for payment amount
					Cashflow cashflow = cashflowService.getById(Cashflow.class, itemId);
					cashflows.add(cashflow);
		        }
				
				if (!cashflows.isEmpty() && paymentInfoPanel.getPayementDate() != null && paymentInfoPanel.getPaymentMethod() != null) {
					SecUser receivedUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					payment.setPaymentDate(paymentInfoPanel.getPayementDate());
					payment.setPaymentMethod(paymentInfoPanel.getPaymentMethod());
					payment.setExternalReference(paymentInfoPanel.getExternalCode());
					payment.setTiPaidAmount(getDouble(txtTotalAmount));
					payment.setWkfStatus(PaymentWkfStatus.PAI);
					payment.setConfirm(payment.getPaymentMethod().isAutoConfirm());
					payment.setReceivedUser(receivedUser);
					payment.setCashflows(cashflows);
					paymentService.createPayment(payment);
					reset();
				}
			}
		});
		
		contentLayout.addComponent(paymentPanel);
		contentLayout.addComponent(cashflowPanel);
		contentLayout.addComponent(horizontalLayout);
		contentLayout.setComponentAlignment(horizontalLayout, Alignment.BOTTOM_RIGHT);
		
		addComponent(contentLayout);
		setExpandRatio(contentLayout, 1f);
	}
	
	/**
	 * Reset components value 
	 */
	public void reset() {
		txtTotalAmount.setValue("");
		paymentInfoPanel.setTxtPaymentAmount("");
		paymentInfoPanel.setTxtInstallmentAmount("");
		paymentInfoPanel.setTxtOtherAmount("");
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		String cotraId = event.getParameters();
		if (StringUtils.isNotEmpty(cotraId)) {
			assignValues(new Long(cotraId));
		}
	}
	
	/**
	 * @param cotraId
	 */
	public void assignValues(Long cotraId) {
		if (cotraId != null) {
			contract = paymentService.getById(Contract.class, new Long(cotraId));
			payment = new Payment();
			payment.setApplicant(contract.getApplicant());
			payment.setContract(contract);
			paymentInfoPanel.assignValues(payment);
			cashflowSearchPanel.setSearchCriteriaValues(contract.getReference());
			cashflowSearchPanel.refreshCashflowTable();
		}
	}
	
	/**
	 * @param totalAmount
	 */
	public void setTotalAmount(Double totalAmount) {
		paymentInfoPanel.setTxtPaymentAmount(AmountUtils.format(totalAmount));
		txtTotalAmount.setValue(AmountUtils.format(totalAmount));
	}
	
	public void setAmount(Double installmentAmount, Double otherAmount) {
		paymentInfoPanel.setTxtInstallmentAmount(AmountUtils.format(installmentAmount));
		paymentInfoPanel.setTxtOtherAmount(AmountUtils.format(otherAmount));
	}

}
