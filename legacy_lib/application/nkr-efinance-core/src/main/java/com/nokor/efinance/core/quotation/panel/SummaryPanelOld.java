package com.nokor.efinance.core.quotation.panel;

import java.io.IOException;
import java.io.InputStream;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;

/**
 * Summary panel
 * @author ly.youhort
 */
public class SummaryPanelOld extends AbstractControlPanel {
		
	private static final long serialVersionUID = -779945654418611299L;
	
	private TextField txtContractId;
	private TextField txtApplicantionId;
	private TextField txtCustomerId;
	private AutoDateField dfApprovalDate;
	private TextField txtNickname;
	private TextField txtFirstName;
	private TextField txtLastName;
	
	private TextField txtMarketingName;
	private TextField txtSeries;
	private TextField txtProductName;
	private TextField txtFinancialProduct;
	private TextField txtDealerShopName;
	private TextField txtPhoneNumber;
	private TextField txtRegistrationNo;
	
	private TextField txtTerms;
	private TextField txtInstallment;
	private AutoDateField dfFirstDueDate;
	private AutoDateField dfLastDueDate;
	private AutoDateField dfLastPaymentDate;
	private AutoDateField dfPenaltyCalculate;
	private TextField txtCalculate;
	
	private TextField txtOverdueTermsOrAmount;
	private TextField txtOverdueTermsOrAmount2;
	private TextField txtBalancePenalty;
	private TextField txtPaidTermsOrRemaining;
	private TextField txtPaidTermsOrRemaining2;
	private AutoDateField dfNextAppointmentDate;
	private TextField txtLatestStaff;
	private TextField txtCurrentStaff;
	private TextField txtPaymentFullBlock;
	
	private Button btnCalculate;

	/** */
	public SummaryPanelOld(/*ContractFormPanel delegate*/) {
		setMargin(true);
		setSpacing(true);
		setSizeFull();
		addComponent(createSummaryForm());
	}
	
	/**
	 * 
	 * @return
	 */
	private CustomLayout createSummaryForm() {
		txtContractId = ComponentFactory.getTextField(false, 45, 130);
		txtApplicantionId = ComponentFactory.getTextField(false, 45, 130);
		txtCustomerId = ComponentFactory.getTextField(false, 45, 130);
		dfApprovalDate = ComponentFactory.getAutoDateField();
		txtNickname = ComponentFactory.getTextField(false, 45, 130);
		txtFirstName = ComponentFactory.getTextField(false, 45, 130);
		txtLastName = ComponentFactory.getTextField(false, 45, 130);
		
		txtMarketingName = ComponentFactory.getTextField(false, 45, 130);
		txtSeries = ComponentFactory.getTextField(false, 45, 130);
		txtProductName = ComponentFactory.getTextField(false, 45, 130);
		txtFinancialProduct = ComponentFactory.getTextField(false, 45, 130);
		txtDealerShopName = ComponentFactory.getTextField(false, 45, 130);
		txtPhoneNumber = ComponentFactory.getTextField(false, 45, 130);
		txtRegistrationNo = ComponentFactory.getTextField(false, 45, 130);
		
		txtTerms = ComponentFactory.getTextField(false, 45, 130);
		txtInstallment = ComponentFactory.getTextField(false, 45, 130);
		dfFirstDueDate = ComponentFactory.getAutoDateField();
		dfLastDueDate = ComponentFactory.getAutoDateField();
		dfLastPaymentDate = ComponentFactory.getAutoDateField();
		dfPenaltyCalculate = ComponentFactory.getAutoDateField();
		txtCalculate = ComponentFactory.getTextField(false, 45, 100);
		
		txtOverdueTermsOrAmount = ComponentFactory.getTextField(false, 45, 50);
		txtOverdueTermsOrAmount2 = ComponentFactory.getTextField(false, 45, 75);
		txtBalancePenalty = ComponentFactory.getTextField(false, 45, 130);
		txtPaidTermsOrRemaining = ComponentFactory.getTextField(false, 45, 30);
		txtPaidTermsOrRemaining2 = ComponentFactory.getTextField(false, 45, 95);
		dfNextAppointmentDate = ComponentFactory.getAutoDateField();
		txtLatestStaff = ComponentFactory.getTextField(false, 45, 130);
		txtCurrentStaff = ComponentFactory.getTextField(false, 45, 130);
		txtPaymentFullBlock = ComponentFactory.getTextField(false, 45, 130);
		
		btnCalculate = new Button(I18N.message("calculate"));
		
		setEnabledSummaryControls(false);
		
		String template = "summaryLayout";
		InputStream layoutFile = getClass().getResourceAsStream("/VAADIN/themes/efinance/layouts/" + template + ".html");
		CustomLayout customLayout = null;
		try {
			customLayout = new CustomLayout(layoutFile);
		} catch (IOException e) {
			Notification.show("Could not locate template " + template, e.getMessage(), Type.ERROR_MESSAGE);
		}
	
		customLayout.addComponent(new Label(I18N.message("contract.id")), "lblContractID");
		customLayout.addComponent(txtContractId, "txtContractID");
		customLayout.addComponent(new Label(I18N.message("marketing.name")), "lblMarketingName");
		customLayout.addComponent(txtMarketingName, "txtMarketingName");
		customLayout.addComponent(new Label(I18N.message("terms")), "lblTerms");
		customLayout.addComponent(txtTerms, "txtTerms");
		customLayout.addComponent(new Label(I18N.message("overdue.terms.or.amount")), "lblOverdueTerms");
		customLayout.addComponent(txtOverdueTermsOrAmount, "txtOverdueTerms1");
		customLayout.addComponent(txtOverdueTermsOrAmount2, "txtOverdueTerms2");
		customLayout.addComponent(new Label(I18N.message("application.id")), "lblApplicantionId");
		customLayout.addComponent(txtApplicantionId, "txtApplicantionId");
		customLayout.addComponent(new Label(I18N.message("series")), "lblSeries");
		customLayout.addComponent(txtSeries, "txtSeries");
		customLayout.addComponent(new Label(I18N.message("installments")), "lblInstallments");
		customLayout.addComponent(txtInstallment, "txtInstallments");
		customLayout.addComponent(new Label(I18N.message("balance.penalty")), "lblPenaltyBalance");
		customLayout.addComponent(txtBalancePenalty, "txtPenaltyBalance");
		customLayout.addComponent(new Label(I18N.message("customer.id")), "lblCustomerID");
		customLayout.addComponent(txtCustomerId, "txtCustomerID");
		customLayout.addComponent(new Label(I18N.message("products.name")), "lblProductName");
		customLayout.addComponent(txtProductName, "txtProductName");
		customLayout.addComponent(new Label(I18N.message("first.due.date")), "lblFirstDueDate");
		customLayout.addComponent(dfFirstDueDate, "dfFirstDueDate");
		customLayout.addComponent(new Label(I18N.message("paid.terms.or.remaining")), "lblPaidTerms");
		customLayout.addComponent(txtPaidTermsOrRemaining, "txtPaidTerms1");
		customLayout.addComponent(txtPaidTermsOrRemaining2, "txtPaidTerms2");
		customLayout.addComponent(new Label(I18N.message("approval.date")), "lblApprovalDate");
		customLayout.addComponent(dfApprovalDate, "dfApprovalDate");
		customLayout.addComponent(new Label(I18N.message("campaign.name")), "lblCampaignName");
		customLayout.addComponent(txtFinancialProduct, "txtCampaignName");
		customLayout.addComponent(new Label(I18N.message("last.due.date")), "lblLastDueDate");
		customLayout.addComponent(dfLastDueDate, "dfLastDueDate");
		customLayout.addComponent(new Label(I18N.message("next.appointment.date")), "lblNextAppointmentDate");
		customLayout.addComponent(dfNextAppointmentDate, "dfNextAppointmentDate");
		customLayout.addComponent(new Label(I18N.message("nickname")), "lblNickName");
		customLayout.addComponent(txtNickname, "txtNickName");
		customLayout.addComponent(new Label(I18N.message("dealer")), "lblDealer");
		customLayout.addComponent(txtDealerShopName, "txtDealer");
		customLayout.addComponent(new Label(I18N.message("last.payment.date")), "lblLastPaymentDate");
		customLayout.addComponent(dfLastPaymentDate, "dfLastPaymentDate");
		customLayout.addComponent(new Label(I18N.message("latest.staff")), "lblLastestStaff");
		customLayout.addComponent(txtLatestStaff, "txtLastestStaff");
		customLayout.addComponent(new Label(I18N.message("firstname.en")), "lblFirstName");
		customLayout.addComponent(txtFirstName, "txtFirstName");
		customLayout.addComponent(new Label(I18N.message("phone.number")), "lblPhoneNumber");
		customLayout.addComponent(txtPhoneNumber, "txtPhoneNumber");
		customLayout.addComponent(new Label(I18N.message("penalty.calculate")), "lblPenaltyCalculate");
		customLayout.addComponent(dfPenaltyCalculate, "txtPenaltyCalculate");
		customLayout.addComponent(new Label(I18N.message("current.staff")), "lblCurrentStaff");
		customLayout.addComponent(txtCurrentStaff, "txtCurrentStaff");
		customLayout.addComponent(new Label(I18N.message("lastname.en")), "lblLastName");
		customLayout.addComponent(txtLastName, "txtLastName");
		customLayout.addComponent(new Label(I18N.message("registration.no")), "lblRegistrationNo");
		customLayout.addComponent(txtRegistrationNo, "txtRegistrationNo");
		customLayout.addComponent(btnCalculate, "btnCalculate");
		customLayout.addComponent(txtCalculate, "txtCalculatedValue");
		customLayout.addComponent(new Label(I18N.message("bath")), "lblCurrency");
		customLayout.addComponent(new Label(I18N.message("payment.full.block")), "lblPaymentFullBlock");
		customLayout.addComponent(txtPaymentFullBlock, "txtPaymentFullBlock");
		
		return customLayout;
	}
	
	/**
	 * 
	 * @param quotation
	 */
	public void assignValues(Quotation quotation) {	
		Applicant applicant = quotation.getApplicant();
		Asset asset = quotation.getAsset();
		
		txtContractId.setValue(getDefaultString(quotation.getReference()));
		dfFirstDueDate.setValue(quotation.getFirstDueDate());
		dfLastDueDate.setValue(quotation.getLastDueDate());
		
		if (applicant != null) {
			txtCustomerId.setValue(getDefaultString(applicant.getIndividual().getReference()));
			txtNickname.setValue(getDefaultString(applicant.getIndividual().getNickName()));
			txtFirstName.setValue(getDefaultString(applicant.getIndividual().getFirstNameEn()));
			txtLastName.setValue(getDefaultString(applicant.getIndividual().getLastNameEn()));
		}
		if (asset != null) {
			txtMarketingName.setValue(asset.getModel().getAssetRange().getDescEn());
			txtSeries.setValue(getDefaultString(asset.getDescEn()));
			txtRegistrationNo.setValue(getDefaultString(asset.getPlateNumber()));
		}
		txtApplicantionId.setValue(getDefaultString(quotation.getExternalReference()));
		dfApprovalDate.setValue(quotation.getAcceptationDate());
		
		if (quotation.getFinancialProduct() != null) {
			txtProductName.setValue(getDefaultString(quotation.getFinancialProduct().getDescEn()));
			txtFinancialProduct.setValue(getDefaultString(quotation.getFinancialProduct().getDescEn()));
		}

		txtDealerShopName.setValue(quotation.getDealer() != null ? quotation.getDealer().getNameEn() : "");
		txtPhoneNumber.setValue("");
		
		txtTerms.setValue(getDefaultString(quotation.getTerm()));
		txtInstallment.setValue(getDefaultString(quotation.getTiInstallmentAmount()));
		dfLastPaymentDate.setValue(null);
		dfPenaltyCalculate.setValue(null);
		txtCalculate.setValue("");
		
		txtOverdueTermsOrAmount.setValue("");
		txtOverdueTermsOrAmount2.setValue("");
		txtBalancePenalty.setValue("");
		txtPaidTermsOrRemaining.setValue("");
		txtPaidTermsOrRemaining2.setValue("");
		dfNextAppointmentDate.setValue(null);
		txtLatestStaff.setValue("");
		txtCurrentStaff.setValue("");
		txtPaymentFullBlock.setValue("");
	}
	
	/**
	 * @param enabled
	 */
	private void setEnabledSummaryControls(boolean enabled) {
		dfApprovalDate.setEnabled(enabled);
		txtNickname.setEnabled(enabled);
		txtLastName.setEnabled(enabled);
		txtFirstName.setEnabled(enabled);
		txtMarketingName.setEnabled(enabled);
		txtSeries.setEnabled(enabled);
		txtProductName.setEnabled(enabled);
		txtFinancialProduct.setEnabled(enabled);
		txtDealerShopName.setEnabled(enabled);
		txtPhoneNumber.setEnabled(enabled);
		txtRegistrationNo.setEnabled(enabled);
		txtTerms.setEnabled(enabled);
		txtInstallment.setEnabled(enabled);
		dfFirstDueDate.setEnabled(enabled);
		dfLastDueDate.setEnabled(enabled);
		dfLastPaymentDate.setEnabled(enabled);
		txtOverdueTermsOrAmount.setEnabled(enabled);
		txtOverdueTermsOrAmount2.setEnabled(enabled);
		txtBalancePenalty.setEnabled(enabled);
		txtPaidTermsOrRemaining.setEnabled(enabled);
		txtPaidTermsOrRemaining2.setEnabled(enabled);
	}
	
	/**
	 * Reset panel
	 */
	public void reset() {
		txtContractId.setValue("");
		txtApplicantionId.setValue("");
		txtCustomerId.setValue("");
		dfApprovalDate.setValue(null);
		txtNickname.setValue("");
		txtFirstName.setValue("");
		txtLastName.setValue("");
		txtMarketingName.setValue("");
		txtSeries.setValue("");
		txtProductName.setValue("");
		txtFinancialProduct.setValue("");
		txtDealerShopName.setValue("");
		txtPhoneNumber.setValue("");
		txtRegistrationNo.setValue("");	
		txtTerms.setValue("");
		txtInstallment.setValue("");
		dfFirstDueDate.setValue(null);
		dfLastDueDate.setValue(null);
		dfLastPaymentDate.setValue(null);
		dfPenaltyCalculate.setValue(null);
		txtCalculate.setValue("");
		txtOverdueTermsOrAmount.setValue("");
		txtOverdueTermsOrAmount2.setValue("");
		txtBalancePenalty.setValue("");
		txtPaidTermsOrRemaining.setValue("");
		txtPaidTermsOrRemaining2.setValue("");
		dfNextAppointmentDate.setValue(null);
		txtLatestStaff.setValue("");
		txtCurrentStaff.setValue("");
		txtPaymentFullBlock.setValue("");
	}
}
