package com.nokor.efinance.core.quotation.panel;

import java.util.Date;

import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
/**
 * 
 * @author buntha.chea
 *
 */
public class SummaryPanelOld1 extends AbstractTabPanel{
	
	/** */
	private static final long serialVersionUID = -2157595497013222801L;
	
	private TextField txtContractStatus;
	private TextField txtMotorcycleStatus;
	private TextField txtContractID;
	private TextField txtCustomerID;
	private TextField txtFirstName;
	private TextField txtLastName;
	private TextField txtModel;
	private TextField txtProductName;
	private TextField txtPhoneNumber;
	private TextField txtInstallment;
	private TextField txtTerms;
	
	private AutoDateField dfFirstDueDate;
	private AutoDateField dfLastDueDate;
	private AutoDateField dfNextDueDate;
	private TextField txtPaidTerms;
	private TextField txtOverdueTerms;
	private TextField txtRemainningTerms;
	private TextField txtOverdueAmount;
	private TextField txtBalancePenalty;
	private TextField txtBalanceAR;
	private AutoDateField dfNextAppointmentDate;

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		txtContractStatus = ComponentFactory.getTextField("contract.status", false, 60, 150);
		txtMotorcycleStatus = ComponentFactory.getTextField("motorcycle.status", false, 60, 150);
		txtContractID = ComponentFactory.getTextField("contract.id", false, 60, 150);
		txtCustomerID = ComponentFactory.getTextField("customer.id", false, 60, 150);
		txtFirstName = ComponentFactory.getTextField("firstname.en", false, 60, 150);
		txtLastName = ComponentFactory.getTextField("lastname.en", false, 60, 150);
		txtModel = ComponentFactory.getTextField("model", false, 60, 150);
		txtProductName = ComponentFactory.getTextField("products.name", false, 60, 150);
		txtPhoneNumber = ComponentFactory.getTextField("phone.number", false, 60, 150);
		txtInstallment = ComponentFactory.getTextField("installment", false, 60, 150);
		txtTerms = ComponentFactory.getTextField("terms", false, 60, 150);
		
		dfFirstDueDate = ComponentFactory.getAutoDateField("first.due.date", false);
		dfLastDueDate = ComponentFactory.getAutoDateField("last.due.date", false);
		dfNextDueDate = ComponentFactory.getAutoDateField("next.due.date", false);
		txtPaidTerms = ComponentFactory.getTextField("paid.terms", false, 60, 150);
		txtOverdueTerms = ComponentFactory.getTextField("overdue.terms", false, 60, 150);
		txtRemainningTerms = ComponentFactory.getTextField("remainning.terms", false, 60, 150);
		txtOverdueAmount = ComponentFactory.getTextField("overdue.amount", false, 60, 150);
		txtBalancePenalty = ComponentFactory.getTextField("balance.penalty", false, 60, 150);
		txtBalanceAR = ComponentFactory.getTextField("balance.ar", false, 60, 150);
		dfNextAppointmentDate = ComponentFactory.getAutoDateField("next.appointment.date", false);
		
		this.setEnabledSummaryControls(false);
		
		return summaryPanel();
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValue(Contract contract) {
		if (contract != null) {
			if (contract != null) {
				txtContractStatus.setValue(contract.getWkfStatus() != null ? getDefaultString(contract.getWkfStatus().getDescEn()) : "");
				txtContractID.setValue(getDefaultString(contract.getReference()));
				Collection collection = contract.getCollection();
				if (collection != null) {
					dfNextDueDate.setValue(collection.getNextDueDate());
					txtPaidTerms.setValue(getDefaultString(collection.getNbInstallmentsPaid()));
					txtOverdueTerms.setValue(getDefaultString(collection.getNbInstallmentsInOverdue()));
					int currentTerm = MyNumberUtils.getInteger(collection.getCurrentTerm());
					if (currentTerm > 1) {
						txtRemainningTerms.setValue(getDefaultString((contract.getTerm() - (currentTerm - 1))));
					} else if (currentTerm == 1) {
						txtRemainningTerms.setValue(getDefaultString(contract.getTerm()));
					} 
					txtOverdueAmount.setValue(AmountUtils.format(collection.getTiTotalAmountInOverdue()));
					txtBalancePenalty.setValue(AmountUtils.format(collection.getTiPenaltyAmount()));
					txtBalanceAR.setValue(AmountUtils.format(MyNumberUtils.getDouble(collection.getTiBalanceCapital()) + MyNumberUtils.getDouble(collection.getTiBalanceInterest())));
				}
			}
			txtTerms.setValue(getDefaultString(contract.getTerm()));
			dfFirstDueDate.setValue(contract.getFirstDueDate());
			if (contract.getLastDueDate() != null) {
				dfLastDueDate.setValue(contract.getLastDueDate());
			} else {
				setLastDueDate(contract.getFirstDueDate());
			}
			Applicant applicant = contract.getApplicant();
			if (applicant != null && applicant.getIndividual() != null) {
				txtCustomerID.setValue(getDefaultString(applicant.getIndividual().getReference()));
				txtFirstName.setValue(getDefaultString(applicant.getIndividual().getFirstNameEn()));
				txtLastName.setValue(getDefaultString(applicant.getIndividual().getLastNameEn()));
			}
			txtModel.setValue(contract.getAsset().getModel().getDescEn());
			txtProductName.setValue(contract.getFinancialProduct().getDescEn());
			txtInstallment.setValue(AmountUtils.format(contract.getTiInstallmentAmount()));
			dfNextAppointmentDate.setValue(null);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private FormLayout getFormLayout() {
		FormLayout formLayout = new FormLayout();
		formLayout.setSpacing(false);
		formLayout.setStyleName("myform-align-left");
		formLayout.setSizeUndefined();
		return formLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private HorizontalLayout summaryPanel() {
		FormLayout summaryFormLeft = getFormLayout();
		summaryFormLeft.addComponent(txtContractStatus);
		summaryFormLeft.addComponent(txtMotorcycleStatus);
		summaryFormLeft.addComponent(txtContractID);
		summaryFormLeft.addComponent(txtCustomerID);
		summaryFormLeft.addComponent(txtFirstName);
		summaryFormLeft.addComponent(txtLastName);
		summaryFormLeft.addComponent(txtModel);
		
		FormLayout summaryFormMiddle = getFormLayout();
		summaryFormMiddle.addComponent(txtProductName);
		summaryFormMiddle.addComponent(txtPhoneNumber);
		summaryFormMiddle.addComponent(txtInstallment);
		summaryFormMiddle.addComponent(txtTerms);
		summaryFormMiddle.addComponent(dfFirstDueDate);
		summaryFormMiddle.addComponent(dfLastDueDate);
		summaryFormMiddle.addComponent(dfNextDueDate);
		
		FormLayout summaryFormRight = getFormLayout();
		summaryFormRight.addComponent(txtPaidTerms);
		summaryFormRight.addComponent(txtOverdueTerms);
		summaryFormRight.addComponent(txtRemainningTerms);
		summaryFormRight.addComponent(txtOverdueAmount);
		summaryFormRight.addComponent(txtBalancePenalty);
		summaryFormRight.addComponent(txtBalanceAR);
		summaryFormRight.addComponent(dfNextAppointmentDate);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		horizontalLayout.addComponent(summaryFormLeft);
		horizontalLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS));
		horizontalLayout.addComponent(summaryFormMiddle);
		horizontalLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS));
		horizontalLayout.addComponent(summaryFormRight);

		return horizontalLayout;
	}
	
	/**
	 * 
	 */
	public void reset() {
		txtContractStatus.setValue("");
		txtMotorcycleStatus.setValue("");
		txtContractID.setValue("");
		txtCustomerID.setValue("");
		txtFirstName.setValue("");
		txtLastName.setValue("");
		txtModel.setValue("");
		txtProductName.setValue("");
		txtPhoneNumber.setValue("");
		txtInstallment.setValue("");
		txtTerms.setValue("");
		
		dfFirstDueDate.setValue(null);
		dfLastDueDate.setValue(null);
		dfNextDueDate.setValue(null);
		txtPaidTerms.setValue("");
		txtOverdueTerms.setValue("");
		txtRemainningTerms.setValue("");
		txtOverdueAmount.setValue("");
		txtBalancePenalty.setValue("");
		txtBalanceAR.setValue("");
		dfNextAppointmentDate.setValue(null);
	}
	
	/**
	 * 
	 * @param firstDueDate
	 */
	private void setLastDueDate(Date firstDueDate) {
		Integer term = getInteger(txtTerms);
		if (firstDueDate != null) {
			dfLastDueDate.setValue(DateUtils.addMonthsDate(DateUtils.addMonthsDate(firstDueDate, term), -1));
		} else {
			dfLastDueDate.setValue(null);
		}
	}
	
	/**
	 * 
	 * @param enabled
	 */
	public void setEnabledSummaryControls(boolean enabled) {
		txtContractStatus.setEnabled(enabled);
		txtMotorcycleStatus.setEnabled(enabled);
		txtContractID.setEnabled(enabled);
		txtCustomerID.setEnabled(enabled);
		txtFirstName.setEnabled(enabled);
		txtLastName.setEnabled(enabled);
		txtModel.setEnabled(enabled);
		txtProductName.setEnabled(enabled);
		txtPhoneNumber.setEnabled(enabled);
		txtInstallment.setEnabled(enabled);
		txtTerms.setEnabled(enabled);
		
		dfFirstDueDate.setEnabled(enabled);
		dfLastDueDate.setEnabled(enabled);
		dfNextDueDate.setEnabled(enabled);
		txtPaidTerms.setEnabled(enabled);
		txtOverdueTerms.setEnabled(enabled);
		txtRemainningTerms.setEnabled(enabled);
		txtOverdueAmount.setEnabled(enabled);
		txtBalancePenalty.setEnabled(enabled);
		txtBalanceAR.setEnabled(enabled);
		dfNextAppointmentDate.setEnabled(enabled);
	}
}
