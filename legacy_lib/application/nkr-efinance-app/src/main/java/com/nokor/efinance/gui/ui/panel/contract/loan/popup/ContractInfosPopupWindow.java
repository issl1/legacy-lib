package com.nokor.efinance.gui.ui.panel.contract.loan.popup;

import java.util.Date;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.Company;
import com.nokor.efinance.core.applicant.model.EApplicantCategory;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractApplicant;
import com.nokor.efinance.core.contract.model.ContractFinService;
import com.nokor.efinance.core.financial.model.Campaign;
import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.financial.model.FinProduct;
import com.nokor.frmk.vaadin.ui.layout.LayoutHelper;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

/**
 * All informations for contract
 * @author uhout.cheng
 */
public class ContractInfosPopupWindow extends Window {

	/** */
	private static final long serialVersionUID = 7597234514325999286L;
	
	private static final String TEMPLATE = "loan/loanContractInfosLayout";
	
	private Contract contract;
	
	private String contractIDValue;
	private String loanAmountValue;
	private String contractDateValue;
	private String financeAmountValue;
	private String contractStatusValue;
	private String interestValue;
	private String productIDValue;
	private String vatValue;
	private String productNameValue;
	private String installmentValue;
	private String mktCampaignIDValue;
	private String termValue;
	private String mktCampaignNameValue;
	private String firstDueDateValue;
	private String isrCampaignIDValue;
	private String lastDueDateValue;
	private String isrCampaignNameValue;
	private String flatRateValue;
	private String guarantorsValue;
	private String downPaymentValue;
	private String borrowerFullNameValue;
	private String prepaidTermValue;
	private String guarantorFullNameValue;
	private String assetIDValue;
	private String assetNameValue;
	private String discountRateValue;
	private String insurancePremiumValue;
	private String commissionValue;
	private String effectiveRateValue;
	private String insuranceFeeValue;
	private String serviceFeeValue;
	private String subsidyValue;
	private String lastPaidDueDateValue;
	private String balancePricipleValue;
	private String nextDueDateValue;
	private String balancePenaltyValue;
	private String latestPaymentDateValue;
	private String remainingInstallmentValue;
	private String paidInstallmentValue;
	private String installmentPastDueValue;
	private String currentTermValue;
	private String amountPastDueValue;
	private String totalPaidValue;
	private String totalPaidOnlyInstallmentValue;
	private String dpdValue;
	private String mpdValue;
	
	/**
	 * 
	 * @param loanSummaryPanel
	 * @param contract
	 */
	public ContractInfosPopupWindow(Contract contract) {
		this.contract = contract;
		setCaption(I18N.message("contract.infos"));
		setModal(true);
		setResizable(false);
		init();
	}

	/**
	 * 
	 */
	private void init() {
		VerticalLayout contentLayout = new VerticalLayout(); 
	        
		Button btnPrint = new NativeButton(I18N.message("print"), new Button.ClickListener() {

			/** */
			private static final long serialVersionUID = 4827929570187685596L;

			public void buttonClick(ClickEvent event) {
				
			}
		});
		btnPrint.setIcon(FontAwesome.PRINT);
	     
		Button btnClose = new NativeButton(I18N.message("close"), new Button.ClickListener() {

			/** */
			private static final long serialVersionUID = -2946969306286231775L;

			public void buttonClick(ClickEvent event) {
				close();
			}
		});
		btnClose.setIcon(FontAwesome.TIMES);
			
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnPrint);
		navigationPanel.addButton(btnClose);

		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(getContractInfosPanel());
	    
		setContent(contentLayout);
	}
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private Label getLabel(String caption) {
		return ComponentFactory.getLabel(caption);
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private Label getLabelValue(String value) {
		return ComponentFactory.getLabel("<b>" + I18N.message(value) + "</b>", ContentMode.HTML);
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	private String getDateFormat(Date date) {
		return DateUtils.getDateLabel(date, DateUtils.FORMAT_DDMMYYYY_SLASH);
	}
	
	/**
	 * 
	 * @return
	 */
	private Panel getContractInfosPanel() {
		CustomLayout customLayout = LayoutHelper.createCustomLayout(TEMPLATE);
		if (customLayout == null) {
			Notification.show("Could not locate template " + LayoutHelper.getTemplateFullPath(TEMPLATE), Type.ERROR_MESSAGE);
		}
		customLayout.addComponent(getLabel("contract.id"), "lblContractID");
		customLayout.addComponent(getLabel("loan.amount"), "lblLoanAmount");
		customLayout.addComponent(getLabel("contract.date"), "lblContractDate");
		customLayout.addComponent(getLabel("finance.amount"), "lblFinanceAmount");
		customLayout.addComponent(getLabel("contract.status"), "lblContractStatus");
		customLayout.addComponent(getLabel("interest"), "lblInterest");
		customLayout.addComponent(getLabel("product.id"), "lblProductID");
		customLayout.addComponent(getLabel("vat"), "lblVat");
		customLayout.addComponent(getLabel("fin.product.name"), "lblProductName");
		customLayout.addComponent(getLabel("installment"), "lblInstallment");
		customLayout.addComponent(getLabel("mkt.campaign.id"), "lblMKTCampaignID");
		customLayout.addComponent(getLabel("term"), "lblTerm");
		customLayout.addComponent(getLabel("mkt.campaign.name"), "lblMKTCampaignName");
		customLayout.addComponent(getLabel("first.due.date"), "lblFirstDueDate");
		customLayout.addComponent(getLabel("isr.campaign.id"), "lblISRCampaignID");
		customLayout.addComponent(getLabel("last.due.date"), "lblLastDueDate");
		customLayout.addComponent(getLabel("isr.campaign.name"), "lblISRCampaignName");
		customLayout.addComponent(getLabel("flat.rate"), "lblFlatRate");
		customLayout.addComponent(getLabel("guarantors"), "lblGuarantors");
		customLayout.addComponent(getLabel("down.payment"), "lblDownPayment");
		customLayout.addComponent(getLabel("borrower.fullname"), "lblBorrowerFullName");
		customLayout.addComponent(getLabel("prepaid.term"), "lblPrepaidTerm");
		customLayout.addComponent(getLabel("guarantor.fullname"), "lblGuarantorFullName");
		customLayout.addComponent(getLabel("asset.id"), "lblAssetID");
		customLayout.addComponent(getLabel("asset.name"), "lblAssetName");
		customLayout.addComponent(getLabel("discount.rate"), "lblDiscountRate");
		customLayout.addComponent(getLabel("insurance.premium"), "lblInsurancePremium");
		customLayout.addComponent(getLabel("net.finance.amount"), "lblNetFinanceAmount");
		customLayout.addComponent(getLabel("commission"), "lblCommission");
		customLayout.addComponent(getLabel("effective.rate"), "lblEffectiveRate");
		customLayout.addComponent(getLabel("insurance.fee"), "lblInsuranceFee");
		customLayout.addComponent(getLabel("net.effective.rate"), "lblNetEffectiveRate");
		customLayout.addComponent(getLabel("service.fee"), "lblServiceFee");
		customLayout.addComponent(getLabel("net.interest"), "lblTotalNetInterest");
		customLayout.addComponent(getLabel("subsidy"), "lblSubsidy");
		customLayout.addComponent(getLabel("balance.loan"), "lblBalanceLoan");
		customLayout.addComponent(getLabel("last.paid.due.date"), "lblLastPaidDueDate");
		customLayout.addComponent(getLabel("balance.principle"), "lblBalancePriciple");
		customLayout.addComponent(getLabel("next.due.date"), "lblNextDueDate");
		customLayout.addComponent(getLabel("balance.vat"), "lblBalanceVat");
		customLayout.addComponent(getLabel("balance.penalty"), "lblBalancePenalty");
		customLayout.addComponent(getLabel("latest.payment.date"), "lblLatestPaymentDate");
		customLayout.addComponent(getLabel("remaining.installment"), "lblRemainingInstallment");
		customLayout.addComponent(getLabel("paid.installment"), "lblPaidInstallment");
		customLayout.addComponent(getLabel("installment.past.due"), "lblInstallmentPastDue");
		customLayout.addComponent(getLabel("current.term"), "lblCurrentTerm");
		customLayout.addComponent(getLabel("amount.past.due"), "lblAmountPastDue");
		customLayout.addComponent(getLabel("dpd"), "lblDPD");
		customLayout.addComponent(getLabel("mpd"), "lblMPD");
		customLayout.addComponent(getLabel("total.paid"), "lblTotalPaid");
		customLayout.addComponent(getLabel("total.paid.installment"), "lblTotalPaidOnlyInstallment");
		
		assigValues(contract);
		
		customLayout.addComponent(getLabelValue(contractIDValue), "lblContractIDValue");
		customLayout.addComponent(getLabelValue(loanAmountValue), "lblLoanAmountValue");
		customLayout.addComponent(getLabelValue(contractDateValue), "lblContractDateValue");
		customLayout.addComponent(getLabelValue(financeAmountValue), "lblFinanceAmountValue");
		customLayout.addComponent(getLabelValue(contractStatusValue), "lblContractStatusValue");
		customLayout.addComponent(getLabelValue(interestValue), "lblInterestValue");
		customLayout.addComponent(getLabelValue(productIDValue), "lblProductIDValue");
		customLayout.addComponent(getLabelValue(vatValue), "lblVatValue");
		customLayout.addComponent(getLabelValue(productNameValue), "lblProductNameValue");
		customLayout.addComponent(getLabelValue(installmentValue), "lblInstallmentValue");
		customLayout.addComponent(getLabelValue(mktCampaignIDValue), "lblMKTCampaignIDValue");
		customLayout.addComponent(getLabelValue(termValue), "lblTermValue");
		customLayout.addComponent(getLabelValue(mktCampaignNameValue), "lblMKTCampaignNameValue");
		customLayout.addComponent(getLabelValue(firstDueDateValue), "lblFirstDueDateValue");
		customLayout.addComponent(getLabelValue(isrCampaignIDValue), "lblISRCampaignIDValue");
		customLayout.addComponent(getLabelValue(lastDueDateValue), "lblLastDueDateValue");
		customLayout.addComponent(getLabelValue(isrCampaignNameValue), "lblISRCampaignNameValue");
		customLayout.addComponent(getLabelValue(flatRateValue), "lblFlatRateValue");
		customLayout.addComponent(getLabelValue(guarantorsValue), "lblGuarantorsValue");
		customLayout.addComponent(getLabelValue(downPaymentValue), "lblDownPaymentValue");
		customLayout.addComponent(getLabelValue(borrowerFullNameValue), "lblBorrowerFullNameValue");
		customLayout.addComponent(getLabelValue(prepaidTermValue), "lblPrepaidTermValue");
		customLayout.addComponent(getLabelValue(guarantorFullNameValue), "lblGuarantorFullNameValue");
		customLayout.addComponent(getLabelValue(assetIDValue), "lblAssetIDValue");
		customLayout.addComponent(getLabelValue(discountRateValue), "lblDiscountRateValue");
		customLayout.addComponent(getLabelValue(assetNameValue), "lblAssetNameValue");
		customLayout.addComponent(getLabelValue(insurancePremiumValue), "lblInsurancePremiumValue");
		customLayout.addComponent(getLabelValue(financeAmountValue), "lblNetFinanceAmountValue"); // Verify again
		customLayout.addComponent(getLabelValue(commissionValue), "lblCommissionValue");
		customLayout.addComponent(getLabelValue(effectiveRateValue), "lblEffectiveRateValue");
		customLayout.addComponent(getLabelValue(insuranceFeeValue), "lblInsuranceFeeValue");
		customLayout.addComponent(getLabelValue(effectiveRateValue), "lblNetEffectiveRateValue"); // Verify again
		customLayout.addComponent(getLabelValue(serviceFeeValue), "lblServiceFeeValue");
		customLayout.addComponent(getLabelValue(interestValue), "lblTotalNetInterestValue"); // Verify again
		customLayout.addComponent(getLabelValue(subsidyValue), "lblSubsidyValue");
		customLayout.addComponent(getLabelValue(loanAmountValue), "lblBalanceLoanValue"); // Verify again
		customLayout.addComponent(getLabelValue(lastPaidDueDateValue), "lblLastPaidDueDateValue");
		customLayout.addComponent(getLabelValue(balancePricipleValue), "lblBalancePricipleValue");
		customLayout.addComponent(getLabelValue(nextDueDateValue), "lblNextDueDateValue");
		customLayout.addComponent(getLabelValue(vatValue), "lblBalanceVatValue"); // Verify again
		customLayout.addComponent(getLabelValue(balancePenaltyValue), "lblBalancePenaltyValue");
		customLayout.addComponent(getLabelValue(latestPaymentDateValue), "lblLatestPaymentDateValue");
		customLayout.addComponent(getLabelValue(remainingInstallmentValue), "lblRemainingInstallmentValue");
		customLayout.addComponent(getLabelValue(paidInstallmentValue), "lblPaidInstallmentValue");
		customLayout.addComponent(getLabelValue(installmentPastDueValue), "lblInstallmentPastDueValue");
		customLayout.addComponent(getLabelValue(currentTermValue), "lblCurrentTermValue");
		customLayout.addComponent(getLabelValue(amountPastDueValue), "lblAmountPastDueValue");
		customLayout.addComponent(getLabelValue(dpdValue), "lblDPDValue");
		customLayout.addComponent(getLabelValue(mpdValue), "lblMPDValue");
		customLayout.addComponent(getLabelValue(totalPaidValue), "lblTotalPaidValue");
		customLayout.addComponent(getLabelValue(totalPaidOnlyInstallmentValue), "lblTotalPaidOnlyInstallmentValue");
		
		VerticalLayout verLayout = new VerticalLayout();
		verLayout.setMargin(true);
		verLayout.addComponent(customLayout);
		
		Panel panel = new Panel(verLayout);
		panel.setStyleName(Reindeer.PANEL_LIGHT);
		return panel;
	}
	
	/**
	 * 
	 * @param contract
	 */
	private void assigValues(Contract contract) {
		FinProduct finProduct = contract.getFinancialProduct();
		Collection collection = contract.getCollection();
		Asset asset = contract.getAsset();
		Applicant applicant = contract.getApplicant();
		ContractApplicant contractGuarantor = contract.getContractApplicant(EApplicantType.G);
		Campaign campaign = contract.getCampaign();
		
		contractIDValue = contract.getReference();
		productIDValue = finProduct != null ? finProduct.getCode() : "";
		productNameValue = finProduct != null ? finProduct.getDescLocale() : "";
		termValue = contract.getTerm() != null ? contract.getTerm().toString() : "0";
		guarantorsValue = contract.getNumberGuarantors() != null ? contract.getNumberGuarantors().toString() : "0";
		loanAmountValue = AmountUtils.format(MyNumberUtils.getDouble(contract.getLoanAmount().getTiAmount()));
		vatValue = AmountUtils.format(MyNumberUtils.getDouble(contract.getVatFinancedAmount()));
		financeAmountValue = AmountUtils.format(MyNumberUtils.getDouble(contract.getTiFinancedAmount()));
		downPaymentValue = AmountUtils.format(MyNumberUtils.getDouble(contract.getTiAdvancePaymentAmount()));
		interestValue = AmountUtils.format(MyNumberUtils.getDouble(contract.getGrossInterestAmount().getTeAmount()));
		flatRateValue = AmountUtils.format(MyNumberUtils.getDouble(contract.getInterestRate()));
		effectiveRateValue = AmountUtils.format(MyNumberUtils.getDouble(contract.getIrrRate()));
		installmentValue = AmountUtils.format(MyNumberUtils.getDouble(contract.getTiInstallmentAmount()));
		prepaidTermValue = contract.getNumberPrepaidTerm() != null ? contract.getNumberPrepaidTerm().toString() : "0";
		guarantorFullNameValue = ""; 
		insuranceFeeValue = "0.00";
		serviceFeeValue = "0.00";
		commissionValue = "0.00";
		currentTermValue = "0";
		latestPaymentDateValue = "";
		nextDueDateValue = "";
		balancePricipleValue = "0.00";
		balancePenaltyValue = "0.00";
		dpdValue = "0";
		mktCampaignIDValue = "";
		mktCampaignNameValue = "";
		borrowerFullNameValue = "";
		contractDateValue = getDateFormat(contract.getStartDate());
		contractStatusValue = contract.getWkfStatus().getDescLocale();
		firstDueDateValue = getDateFormat(contract.getFirstDueDate());
		lastPaidDueDateValue = getDateFormat(contract.getLastDueDate());
		assetIDValue = asset != null ? asset.getCode() : "";
		assetNameValue = asset != null ? asset.getDesc() : "";
		isrCampaignIDValue = "N/A";
		isrCampaignNameValue = "N/A";
		discountRateValue = "N/A";
		insurancePremiumValue = "N/A";
		subsidyValue = "N/A";
		lastPaidDueDateValue = "N/A";
		remainingInstallmentValue = "N/A";
		paidInstallmentValue = "N/A";
		installmentPastDueValue = "N/A";
		amountPastDueValue = "N/A";
		mpdValue = "N/A";
		totalPaidValue = "N/A";
		totalPaidOnlyInstallmentValue = "N/A";
		if (applicant != null) {
			if (EApplicantCategory.INDIVIDUAL.equals(applicant.getApplicantCategory())) {
				Individual individual = applicant.getIndividual();
				if (individual != null) {
					borrowerFullNameValue = individual.getLastNameFirstNameLocale();
				}
			} else if (EApplicantCategory.COMPANY.equals(applicant.getApplicantCategory())) {
				Company company = applicant.getCompany();
				if (company != null) {
					borrowerFullNameValue = company.getNameLocale();
				}
			}
		}
		if (campaign != null) {
			mktCampaignIDValue = campaign.getCode();
			mktCampaignNameValue = campaign.getDescEn();
		}
		if (collection != null) {
			currentTermValue = collection.getCurrentTerm() != null ? collection.getCurrentTerm().toString() : "0";
			latestPaymentDateValue = getDateFormat(collection.getLastPaymentDate());
			balancePricipleValue = AmountUtils.format(MyNumberUtils.getDouble(collection.getTiBalanceCapital()));
			nextDueDateValue = getDateFormat(collection.getNextDueDate());
			balancePenaltyValue = AmountUtils.format(MyNumberUtils.getDouble(collection.getTiPenaltyAmount()));
			dpdValue = String.valueOf(MyNumberUtils.getInteger(collection.getNbOverdueInDays()));
		}
		if (contractGuarantor != null) {
			Applicant guarantor = contractGuarantor.getApplicant();
			if (guarantor != null) {
				if (EApplicantCategory.INDIVIDUAL.equals(guarantor.getApplicantCategory())) {
					Individual individual = guarantor.getIndividual();
					if (individual != null) {
						guarantorFullNameValue = individual.getLastNameFirstNameLocale();
					} else if (EApplicantCategory.COMPANY.equals(guarantor.getApplicantCategory())) {
						Company company = guarantor.getCompany();
						if (company != null) {
							guarantorFullNameValue = company.getNameLocale();
						}
					}
				}
			}
		}
		List<ContractFinService> contractFinServices = contract.getContractFinServices();
		if (contractFinServices != null && !contractFinServices.isEmpty()) {
			for (ContractFinService contractFinService : contractFinServices) {
				if (contractFinService.getService() != null) {
					if (EServiceType.INSFEE.equals(contractFinService.getService())) {
						insuranceFeeValue = AmountUtils.format(MyNumberUtils.getDouble(contractFinService.getTiPrice()));	
					} else if (EServiceType.SRVFEE.equals(contractFinService.getService())) {
						serviceFeeValue = AmountUtils.format(MyNumberUtils.getDouble(contractFinService.getTiPrice()));
					} else if (EServiceType.COMM.equals(contractFinService.getService())) {
						commissionValue = AmountUtils.format(MyNumberUtils.getDouble(contractFinService.getTiPrice()));
					}
				}
			}
		}
	}
	
}
