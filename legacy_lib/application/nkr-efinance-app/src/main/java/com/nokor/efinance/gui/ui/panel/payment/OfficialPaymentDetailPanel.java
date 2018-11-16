package com.nokor.efinance.gui.ui.panel.payment;

import java.util.Date;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.service.cashflow.CashflowService;
import com.nokor.efinance.core.payment.model.EPaymentType;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.payment.service.PaymentService;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.widget.ManualPaymentMethodComboBox;
import com.nokor.efinance.core.workflow.PaymentWkfStatus;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.ToolbarButtonsPanel;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * @author sok.vina
 */
public class OfficialPaymentDetailPanel extends AbstractTabPanel implements CashflowEntityField, FrmkServicesHelper {
	
	private static final long serialVersionUID = -4618786633559261506L;

	private CashflowService cashflowService = SpringUtils.getBean(CashflowService.class);
	private PaymentService paymentService= SpringUtils.getBean(PaymentService.class);
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	private AutoDateField dfPaymentDate;
	private AutoDateField dfInstallmentDate;
	private ManualPaymentMethodComboBox cbxPaymentMethod;
	private TextField txtPaymentAmount;
	private TextField txtDealer;
	private TextField txtApplicant;
	private TextField txtCode;
	private TextField txtRegistrationFee;
	private TextField txtAdvancePayment;
	private TextField txtServicingFee;
	private TextField txtInsuranceFee;
	private TextField txtOthers;
	private NativeButton btnAllocate;
	private List<Cashflow> cashflows;
	private double totalAmountUsd = 0d;
	private Long cotraId;
	private ClickListener allocateListener;
	private Payment payment;

	public OfficialPaymentDetailPanel() {
		super();
		setMargin(false);
		btnAllocate = new NativeButton(I18N.message("receive"));
		btnAllocate.setIcon(new ThemeResource("../nkr-default/icons/16/add.png"));
		btnAllocate.addClickListener(new ClickListener() {		
			private static final long serialVersionUID = -94612598626779199L;
			@Override
			public void buttonClick(ClickEvent event) {
				if (isValid()) {
					ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("confirm.receive.down.payment", txtApplicant.getValue()),
					        new ConfirmDialog.Listener() {
								private static final long serialVersionUID = 2380193173874927880L;
								public void onClose(ConfirmDialog dialog) {
					                if (dialog.isConfirmed()) { 
										if (cashflows != null && !cashflows.isEmpty()) {
											SecUser receivedUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
											Contract contract = cashflowService.getById(Contract.class, new Long(cotraId));
											Payment payment = new Payment();
											payment.setApplicant(contract.getApplicant());
											payment.setContract(contract);
											payment.setPaymentDate(dfPaymentDate.getValue());
											payment.setPaymentMethod(cbxPaymentMethod.getSelectedEntity());
											payment.setTiPaidAmount(totalAmountUsd);
											payment.setWkfStatus(PaymentWkfStatus.RVA);
											payment.setConfirm(payment.getPaymentMethod().isAutoConfirm());
											payment.setReceivedUser(receivedUser);
											payment.setPaymentType(EPaymentType.ORC);
											payment.setCashflows(cashflows);
											paymentService.createPayment(payment);
											setPayment(payment);
										}
					                	allocateListener.buttonClick(null);
					                	//Page.getCurrent().setUriFragment("!" + PurchaseOrdersPanel.NAME);
				                	}
				            	}
				    	});
						confirmDialog.setWidth("400px");
						confirmDialog.setHeight("150px");						
				}
			}
		});

		ToolbarButtonsPanel tblButtonsPanel = new ToolbarButtonsPanel();
		tblButtonsPanel.addButton(btnAllocate);
		addComponent(tblButtonsPanel, 0);
	}
	
	/**
	 * @param cotraId
	 */
	public void assignValues(Long cotraId,Date installmentDate) {
		if (cotraId != null) {
			Contract contract = ENTITY_SRV.getById(Contract.class, cotraId);
			this.cotraId = cotraId;
			this.cashflows = cashflowService.getOfficialCashflowsToPaid(cotraId);
			double tiRegistrationFeeUsd = 0d;
			double tiServicingFeeUsd =  0d;
			double tiInsuranceFeeUsd = 0d;
			double tiOtherUsd = 0d;
			double financedAmountUsd = 0d;
			if (cashflows != null && !cashflows.isEmpty()) {
				dfPaymentDate.setValue(DateUtils.today());
				for (Cashflow cashflow : cashflows) {
					if (cashflow.getCashflowType().equals(ECashflowType.FIN)) {
						financedAmountUsd += cashflow.getTiInstallmentAmount();
					} else if (cashflow.getCashflowType().equals(ECashflowType.FEE)) {
						if ("REGFEE".equals(cashflow.getService().getCode())) {
							tiRegistrationFeeUsd += cashflow.getTiInstallmentAmount();
						} else if ("INSFEE".equals(cashflow.getService().getCode())) {
							tiInsuranceFeeUsd += cashflow.getTiInstallmentAmount();
						} else if ("SERFEE".equals(cashflow.getService().getCode())) {
							tiServicingFeeUsd += cashflow.getTiInstallmentAmount();
						}
					} else {
						tiOtherUsd += cashflow.getTiInstallmentAmount();
					}
				}
			}
			txtCode.setValue(contract.getReference());
			dfInstallmentDate.setValue(installmentDate);
			txtDealer.setValue(contract.getDealer().getNameEn());
			txtApplicant.setValue(contract.getApplicant().getIndividual().getLastNameEn() + "  " + contract.getApplicant().getIndividual().getFirstNameEn());
			txtRegistrationFee.setValue(AmountUtils.format(tiRegistrationFeeUsd));
			txtAdvancePayment.setValue(AmountUtils.format(contract.getTiAdvancePaymentAmount()));
			txtServicingFee.setValue(AmountUtils.format(tiServicingFeeUsd));
			txtInsuranceFee.setValue(AmountUtils.format(tiInsuranceFeeUsd));
			txtOthers.setValue(AmountUtils.format(tiOtherUsd));
			txtPaymentAmount.setValue(AmountUtils.format(tiRegistrationFeeUsd + contract.getTiAdvancePaymentAmount() + tiServicingFeeUsd + tiInsuranceFeeUsd + tiOtherUsd));
			totalAmountUsd =  financedAmountUsd + tiRegistrationFeeUsd + tiServicingFeeUsd + tiInsuranceFeeUsd + tiOtherUsd;
		
		}
	}
	
	@Override
	protected com.vaadin.ui.Component createForm() {
		VerticalLayout contentLayout = new VerticalLayout();
		
		final GridLayout gridLayout = new GridLayout(8, 6);
		gridLayout.setMargin(true);
		gridLayout.setSpacing(true);
		dfPaymentDate = ComponentFactory.getAutoDateField("", false);
		dfPaymentDate.setValue(new Date());
		dfPaymentDate.setRequired(true);
		
		dfInstallmentDate = ComponentFactory.getAutoDateField("", false);
		dfInstallmentDate.setEnabled(false);
		
		cbxPaymentMethod = new ManualPaymentMethodComboBox();
		cbxPaymentMethod.renderer();
		cbxPaymentMethod.setRequired(true);
		cbxPaymentMethod.setWidth("150px");
        
		txtPaymentAmount = ComponentFactory.getTextField(30, 150);
		txtPaymentAmount.setEnabled(false);
		
		txtDealer = ComponentFactory.getTextField(100, 200);
		txtDealer.setEnabled(false);
		txtApplicant = ComponentFactory.getTextField(100, 150);
		txtApplicant.setEnabled(false);
		
		txtRegistrationFee = ComponentFactory.getTextField(30, 150);
		txtRegistrationFee.setEnabled(false);
		
		txtCode = ComponentFactory.getTextField(30, 150);
		txtCode.setEnabled(false);
		
		txtAdvancePayment = ComponentFactory.getTextField(30, 150);
		txtAdvancePayment.setEnabled(false);
		txtServicingFee = ComponentFactory.getTextField(30, 150);
		txtServicingFee.setEnabled(false);
		txtInsuranceFee = ComponentFactory.getTextField(30, 150);
		txtInsuranceFee.setEnabled(false);
		
		txtOthers = ComponentFactory.getTextField(30, 150);
		txtOthers.setEnabled(false);
		
        int iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("applicant")), iCol++, 0);
        gridLayout.addComponent(txtApplicant, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("dealer")), iCol++, 0);
        gridLayout.addComponent(txtDealer, iCol++, 0);

        
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("down.payment.date")), iCol++, 1);
        gridLayout.addComponent(dfInstallmentDate, iCol++, 1);
        
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("advance.payment")), iCol++, 2);
        gridLayout.addComponent(txtAdvancePayment, iCol++, 2);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 2);
        gridLayout.addComponent(new Label(I18N.message("servicing.fee")), iCol++, 2);
        gridLayout.addComponent(txtServicingFee, iCol++, 2);
		
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("registration.fee")), iCol++, 3);
        gridLayout.addComponent(txtRegistrationFee, iCol++, 3);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 3);
        gridLayout.addComponent(new Label(I18N.message("insurance.fee")), iCol++, 3);
        gridLayout.addComponent(txtInsuranceFee, iCol++, 3);
        
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("other.amount")), iCol++, 4);
        gridLayout.addComponent(txtOthers, iCol++, 4);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 4);
        gridLayout.addComponent(new Label(I18N.message("total.amount")), iCol++, 4);
        gridLayout.addComponent(txtPaymentAmount, iCol++, 4);
        
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("payment.date")), iCol++, 5);
        gridLayout.addComponent(dfPaymentDate, iCol++, 5);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 5);
        gridLayout.addComponent(new Label(I18N.message("payment.method")), iCol++, 5);
        gridLayout.addComponent(cbxPaymentMethod, iCol++, 5);
		
        contentLayout.setSpacing(true);
        contentLayout.addComponent(gridLayout);

        return contentLayout;
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
	private void setPayment(Payment payment) {
		this.payment = payment;
	}
	
	/**
	 * @param allocateListener the allocateListener to set
	 */
	public void setAllocateListener(ClickListener allocateListener) {
		this.allocateListener = allocateListener;
	}
	
	/**
	 * @return
	 */
	public boolean isValid() {
		reset();
		checkMandatoryDateField(dfPaymentDate, "payment.date");
		checkMandatorySelectField(cbxPaymentMethod, "payment.method");
		if (!errors.isEmpty()) {
			super.displayErrorsPanel();
		}
		return errors.isEmpty();
	}
}
