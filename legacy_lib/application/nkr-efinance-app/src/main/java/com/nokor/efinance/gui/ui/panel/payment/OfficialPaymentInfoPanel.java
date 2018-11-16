package com.nokor.efinance.gui.ui.panel.payment;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.widget.ManualPaymentMethodComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * @author heng.mao
 *
 */
public class OfficialPaymentInfoPanel extends VerticalLayout implements FMEntityField {

	private static final long serialVersionUID = 8593151485544267183L;
	
	private TextField txtFirstName;
	private TextField txtLastName;
	private AutoDateField dfPaymentDate;
	private TextField txtCode;
	private TextField txtExternalCode;
	private TextField txtPaymentAmount;
	private TextField txtNumberInstallment;
	
	private TextField txtInsuranceFee;
	private TextField txtRegistrationFee;
	private TextField txtServicingFee;
	private TextField txtOtherAmount;
	private TextField txtAdvancePayment;
	private TextField txtSecondPayment;
	private ManualPaymentMethodComboBox cbxPaymentMethod; 

	public OfficialPaymentInfoPanel() {
		setMargin(true);
		addComponent(createForm());
	}

	/**
	 * @return
	 */
	protected Component createForm() {
		final GridLayout gridLayout = new GridLayout(12, 5);
		gridLayout.setSpacing(true);
		
		txtFirstName = ComponentFactory.getTextField(false, 60, 150);
		txtFirstName.setEnabled(false);
		txtFirstName.setStyleName("v-disabled");

		txtLastName = ComponentFactory.getTextField(60, 150);
		txtLastName.setEnabled(false);
		txtLastName.setStyleName("v-disabled");
				
		dfPaymentDate = ComponentFactory.getAutoDateField("", false);
		dfPaymentDate.setEnabled(false);
		
		cbxPaymentMethod = new ManualPaymentMethodComboBox();
		cbxPaymentMethod.renderer();
		cbxPaymentMethod.setWidth("150px");
		cbxPaymentMethod.setEnabled(false);
		
		txtCode = ComponentFactory.getTextField(60, 150);
		txtCode.setEnabled(false);
		txtExternalCode = ComponentFactory.getTextField(60, 150);
		
		txtPaymentAmount = ComponentFactory.getTextField(60, 150);
		txtPaymentAmount.setEnabled(false);
		
		txtServicingFee = ComponentFactory.getTextField(60, 150);
		txtServicingFee.setEnabled(false);
		
		txtInsuranceFee = ComponentFactory.getTextField(60, 150);
		txtInsuranceFee.setEnabled(false);
		
		txtRegistrationFee = ComponentFactory.getTextField(60, 150);
		txtRegistrationFee.setEnabled(false);
		
		txtAdvancePayment = ComponentFactory.getTextField(60, 150);
		txtAdvancePayment.setEnabled(false);
		
		txtSecondPayment = ComponentFactory.getTextField(60, 150);
		txtSecondPayment.setEnabled(false);
		
		txtOtherAmount = ComponentFactory.getTextField(60, 150);
		txtOtherAmount.setEnabled(false);
		
		txtNumberInstallment = ComponentFactory.getTextField(60, 150);
		txtNumberInstallment.setEnabled(false);
		int iCol = 0;
		gridLayout.addComponent(new Label(I18N.message("official.payment.no")), iCol++, 0);
        gridLayout.addComponent(txtCode, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("num.installment")), iCol++, 0);
        gridLayout.addComponent(txtNumberInstallment, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("insurance.fee")), iCol++, 0);
        gridLayout.addComponent(txtInsuranceFee, iCol++, 0);
        
        iCol = 0;
		gridLayout.addComponent(new Label(I18N.message("lastname.en")), iCol++, 1);
        gridLayout.addComponent(txtLastName, iCol++, 1);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 1);
		gridLayout.addComponent(new Label(I18N.message("firstname.en")), iCol++, 1);
        gridLayout.addComponent(txtFirstName, iCol++, 1);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 1);
        gridLayout.addComponent(new Label(I18N.message("registration.fee")), iCol++, 1);
        gridLayout.addComponent(txtRegistrationFee, iCol++, 1);
        
		iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("payment.date")), iCol++, 2);
        gridLayout.addComponent(dfPaymentDate, iCol++, 2);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 2);        
		gridLayout.addComponent(new Label(I18N.message("payment.method")), iCol++, 2);
        gridLayout.addComponent(cbxPaymentMethod, iCol++, 2);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 2);
        gridLayout.addComponent(new Label(I18N.message("servicing.fee")), iCol++, 2);
        gridLayout.addComponent(txtServicingFee, iCol++, 2);
        
		iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("advance.payment")), iCol++, 3);
        gridLayout.addComponent(txtAdvancePayment, iCol++, 3);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 3);
        gridLayout.addComponent(new Label(I18N.message("other.amount")), iCol++, 3);
        gridLayout.addComponent(txtOtherAmount, iCol++, 3);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 3);
        gridLayout.addComponent(new Label(I18N.message("total.amount")), iCol++, 3);
        gridLayout.addComponent(txtPaymentAmount, iCol++, 3);

		return gridLayout;
	}
	
	
	/**
	 * @param contractId
	 */
	public void assignValues(Payment payment) {
		reset();
		Applicant applicant = payment.getApplicant();
		if (applicant != null) {
			txtFirstName.setValue(applicant.getIndividual().getFirstNameEn());
			txtLastName.setValue(applicant.getIndividual().getLastNameEn());
		}
		cbxPaymentMethod.setSelectedEntity(payment.getPaymentMethod());
		dfPaymentDate.setValue(payment.getPaymentDate());
		txtExternalCode.setValue(StringUtils.defaultString(payment.getExternalReference()));
		txtCode.setValue(StringUtils.defaultString(payment.getReference()));
		double tiAdvancePaymentUsd = 0d;
		double tiRegistrationFeeUsd = 0d;
		double tiServicingFeeUsd = 0d;
		double tiInsuranceFeeUsd = 0d;
		double tiOtherUsd = 0d;
		double financedAmountUsd = 0d;
		Integer numInstallment = null;
		if (payment.getCashflows() != null) {
			for (Cashflow cashflow : payment.getCashflows()) {
				tiAdvancePaymentUsd = cashflow.getContract().getTiAdvancePaymentAmount();
				numInstallment = cashflow.getNumInstallment();
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
		txtNumberInstallment.setValue("" + numInstallment);
		txtInsuranceFee.setValue(AmountUtils.format(tiInsuranceFeeUsd));
		txtRegistrationFee.setValue(AmountUtils.format(tiRegistrationFeeUsd));
		txtServicingFee.setValue(AmountUtils.format(tiServicingFeeUsd));
		txtOtherAmount.setValue(AmountUtils.format(tiOtherUsd));
		txtAdvancePayment.setValue(AmountUtils.format(tiAdvancePaymentUsd));
		txtPaymentAmount.setValue(AmountUtils.format(tiAdvancePaymentUsd + tiInsuranceFeeUsd + tiRegistrationFeeUsd + tiServicingFeeUsd + tiOtherUsd));
		txtSecondPayment.setValue(AmountUtils.format(financedAmountUsd + tiRegistrationFeeUsd + tiServicingFeeUsd + tiInsuranceFeeUsd + tiOtherUsd));
	}

	/**
	 */
	public void reset() {
		txtFirstName.setValue("");
		txtLastName.setValue("");
		dfPaymentDate.setValue(null);
		txtCode.setValue("");
		txtExternalCode.setValue("");
		txtNumberInstallment.setValue("");
		txtAdvancePayment.setValue("");
	}
}
