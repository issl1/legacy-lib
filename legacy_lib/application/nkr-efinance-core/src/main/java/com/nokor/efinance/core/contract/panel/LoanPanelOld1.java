package com.nokor.efinance.core.contract.panel;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractApplicant;
import com.nokor.efinance.core.contract.model.ContractDocument;
import com.nokor.efinance.core.contract.model.ContractFinService;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.document.model.Document;
import com.nokor.efinance.core.document.model.EDocumentStatus;
import com.nokor.efinance.core.financial.model.Campaign;
import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.financial.model.FinProduct;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.quotation.panel.SummaryPanelOld1;
import com.nokor.frmk.config.model.SettingConfig;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.SaveClickListener;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
/**
 * 
 * @author buntha.chea
 */
public class LoanPanelOld1 extends AbstractTabPanel implements FinServicesHelper, SaveClickListener, ValueChangeListener {
	
	/** */
	private static final long serialVersionUID = -2456724092864285465L;
	
	private TextField txtApplicationId;
	private AutoDateField dfApplicationDate;
	private TextField txtApplicationTime;
	private AutoDateField dfApprovalDate;
	private TextField txtApprovalTime;
	private TextField txtDealerShopId;
	private TextField txtDealerShopName;
	
	private TextField txtContractId;
	private AutoDateField dfContractDate;
	private TextField txtProductId;
	private TextField txtProductName;
	private TextField txtCampaignId;
	private TextField txtCampaingName;
	private TextField txtFlatRate;
	private TextField txtInstallment;
	private TextField txtTerm;
	private AutoDateField dfFirstDueDate;
	private AutoDateField dfLastDueDate;
	private TextField txtFinanceAmount;
	private TextField txtInsterestAmount;
	private TextField txtVatAmount;
	private TextField txtTotalLoan;
	private TextField txtDownPayment;
	private TextField txtPrepaidTerms;
	private TextField txtServiceFee;
	private TextField txtInsuranceFee;
	private TextField txtGuarantor;
	private TextField txtFinanceAmountExc;
	private TextField txtFinanceAmountInc;
	private TextField txtInstallmentAmountExc;
	private TextField txtInstallmentAmountInc;
	private TextField txtNetFinanceAmountExc;
	private TextField txtNetFinanceAmountInc;
	private TextField txtEffectiveRate;
	private TextField txtNetEffectiveRate;
	private TextField txtTotalGrossInterestIncome;
	private TextField txtTotalNetInterestIncome;
	
	private Button btnApplicationDetailLink;
	
	private TextField txtServiceFeeExVat;
	private TextField txtServiceFeeIncVat;
	private TextField txtSubsidyExVat;
	private TextField txtSubsidyIncVat;
	private TextField txtDirectIncomeTotalExVat;
	private TextField txtDirectIncomeTotalIncVat;
	
	private TextField txtCommissionExVat;
	private TextField txtCommissionIncVat;
	private TextField txtInsurancePremiumExVat;
	private TextField txtInsurancePremiumIncVat;
	private TextField txtDirectCostTotalExVat;
	private TextField txtDirectCostTotalIncVat;
	
	private Contract contract;
	private SummaryPanelOld1 summaryPanel;
	private List<ContractDocument> documentsReceived;
	private HorizontalLayout documentLayout;
	private GridLayout gridLayout;
	
	/**
	 * 
	 * @param summaryPanel
	 */
	public LoanPanelOld1(SummaryPanelOld1 summaryPanel) {
		this.summaryPanel = summaryPanel;
	}
	
	/**
	 * @param caption
	 * @return
	 */
	private Button getButton(String caption) {
		Button btnButton = ComponentFactory.getButton(caption);
		btnButton.setStyleName(Reindeer.BUTTON_LINK);
		return btnButton;
	} 
	
	/**
	 * @return
	 */
	private FormLayout getFormLayout() {
		FormLayout formLayout = new FormLayout();
		formLayout.setStyleName("myform-align-left");
		formLayout.setCaptionAsHtml(true);
		formLayout.setMargin(true);
		formLayout.setSpacing(false);
		return formLayout;
	}
	
	/**
	 * @return
	 */
	private VerticalLayout getVerticalLayout() {
		VerticalLayout verLayout = new VerticalLayout();
		verLayout.setCaptionAsHtml(true);
		verLayout.setSpacing(true);
		return verLayout;
	}
	
	/**
	 * @param caption
	 * @param maxLength
	 * @param width
	 * @return
	 */
	private TextField getTextField(String caption, int maxLength, float width) {
		TextField textField = ComponentFactory.getTextField(caption, false, maxLength, width);
		textField.setEnabled(false);
		return textField;
	}
	
	/**
	 * @param caption
	 * @return
	 */
	private Label getLabel(String caption) {
		Label label = new Label();
		label.setValue("<h2 style=\"border:1px solid #E3E3E3;padding:9px;border-radius:3px;margin:0;"
				+ "background-color:#F5F5F5;\" align=\"center\" >" + I18N.message(caption) + "</h2>");
		label.setContentMode(ContentMode.HTML);
		label.setWidth("100%");
		return label;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addSaveClickListener(this);
		addComponent(navigationPanel, 0);
		
		dfApplicationDate = ComponentFactory.getAutoDateField("application.date", false);
		dfApprovalDate = ComponentFactory.getAutoDateField("approval.date", false);
		txtApplicationId = getTextField("application.id", 60, 150);
		txtApplicationTime = getTextField("application.time", 60, 150);
		txtApprovalTime = getTextField("approval.time", 60, 150);
		txtDealerShopId = getTextField("dealer.shop.id", 60, 150);
		txtDealerShopName = getTextField("dealer.shop.name", 60, 150);
		txtApplicationId.setStyleName("mytextfield-caption");
		
		txtContractId = getTextField("contract.id", 60, 150);
		dfContractDate = ComponentFactory.getAutoDateField("contract.date", false);
		txtProductId = getTextField("product.id", 60, 150);
		txtProductName = getTextField("name.product", 60, 150);
		txtCampaignId = getTextField("campaign.id", 60, 150);
		txtCampaingName = getTextField("campaign.name", 60, 150);
		txtCampaingName.setStyleName("mytextfield-caption");
		txtFlatRate = getTextField("flat.rate", 60, 150);
		txtInstallment = getTextField("installment", 60, 150);
		txtTerm = getTextField("terms", 60, 150);
		dfFirstDueDate = ComponentFactory.getAutoDateField("first.due.date", true);
		dfFirstDueDate.addValueChangeListener(this);
		dfLastDueDate = ComponentFactory.getAutoDateField("last.due.date", false);
		txtFinanceAmount = getTextField("finance.amount", 60, 150);
		txtInsterestAmount = getTextField("insterest.amount", 60, 150);
		txtVatAmount = getTextField("vat.amount", 60, 150);
		txtTotalLoan = getTextField("total.loan", 60, 150);
		txtDownPayment = getTextField("down.payment", 60, 150);
		txtPrepaidTerms = getTextField("prepaid.terms", 60, 150);
		txtServiceFee = getTextField("service.fee", 60, 150);
		txtInsuranceFee = getTextField("insurance.fee", 60, 150);
		txtGuarantor = getTextField("guarantor", 60, 150);
		
		txtFinanceAmountExc = getTextField("finance.amount.ex.vat", 60, 150);
		txtFinanceAmountInc = getTextField("finance.amount.inc.vat", 60, 150);
		txtInstallmentAmountExc = getTextField("installment.ex.vat", 60, 150);
		txtInstallmentAmountInc = getTextField("installment.inc.vat", 60, 150);
		
		txtNetFinanceAmountExc = getTextField("net.finance.amount.ex.vat", 60, 150);
		txtNetFinanceAmountExc.setStyleName("mytextfield-caption-17");
		txtNetFinanceAmountInc = getTextField("net.finance.amount.inc.vat", 60, 150);
		txtEffectiveRate = getTextField("effective.rate.irr", 60, 150);
		txtNetEffectiveRate = getTextField("net.effective.rate.irr", 60, 150);
		
		txtTotalGrossInterestIncome = getTextField("total.gross.interest.income", 60, 150);
		txtTotalGrossInterestIncome.setStyleName("mytextfield-caption");
		txtTotalNetInterestIncome = getTextField("total.net.interest.income", 60, 150);
		
		txtServiceFeeExVat = getTextField("service.fee.ex.vat", 60, 150);
		txtServiceFeeIncVat = getTextField("service.fee.inc.vat", 60, 150);
		txtSubsidyExVat = getTextField("subsidy.ex.vat", 60, 150);
		txtSubsidyIncVat = getTextField("subsidy.inc.vat", 60, 150);
		txtDirectIncomeTotalExVat = getTextField("total.ex.vat", 60, 150);
		txtDirectIncomeTotalIncVat = getTextField("total.inc.vat", 60, 150);
		txtDirectIncomeTotalIncVat.setStyleName("mytextfield-caption");
		
		txtCommissionExVat = getTextField("commission.ex.vat", 60, 150);
		txtCommissionIncVat = getTextField("commission.inc.vat", 60, 150);
		txtInsurancePremiumExVat = getTextField("insurance.premiume.ex.vat", 60, 150);
		txtInsurancePremiumExVat.setStyleName("mytextfield-caption-17");
		txtInsurancePremiumIncVat = getTextField("insurance.premiume.inc.vat", 60, 150);
		txtDirectCostTotalExVat = getTextField("total.ex.vat", 60, 150);
		txtDirectCostTotalIncVat = getTextField("total.inc.vat", 60, 150);
		
		btnApplicationDetailLink = getButton("more.detail");
		
		gridLayout = new GridLayout();
		gridLayout.setColumns(2);
		gridLayout.setSpacing(true);
		
		documentLayout = new HorizontalLayout();
		documentLayout.setSpacing(true);
		
		setEnabledLoanControls(false);
		Label lblApplicationTitle = getLabel("application.detail");
		Label lblContractTitle = getLabel("contract.detail");
		VerticalLayout loanContent = getVerticalLayout();
		loanContent.addStyleName("overflow-layout-style");
		loanContent.addComponent(lblApplicationTitle);
		loanContent.addComponent(applicationDetailPanel());
		loanContent.addComponent(lblContractTitle);
		loanContent.addComponent(contractDetailPanel());
		return loanContent;
	}	
		
	/**
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		super.removeErrorsPanel();
		if (contract != null) {
			this.contract = contract;
			Dealer dealer = contract.getDealer();
			FinProduct finProduct = contract.getFinancialProduct();
			Campaign campaign = contract.getCampaign();
			
			txtApplicationId.setValue(getDefaultString(contract.getExternalReference()));
			dfApplicationDate.setValue(contract.getQuotationDate());
			if (contract.getQuotationDate() != null) {
				txtApplicationTime.setValue(DateUtils.date2String(contract.getQuotationDate(), "hh:mm"));
			}
			dfApprovalDate.setValue(contract.getApprovalDate());
			if (contract.getApprovalDate() != null) {
				txtApprovalTime.setValue(DateUtils.date2String(contract.getApprovalDate(), "hh:mm"));
			}

			txtDealerShopId.setValue(dealer != null ? getDefaultString(dealer.getCode()) : "");
			txtDealerShopName.setValue(dealer != null ? getDefaultString(dealer.getNameEn()) : "");
						
			dfContractDate.setValue(contract.getStartDate());
			txtContractId.setValue(getDefaultString(contract.getReference()));
			
			txtProductId.setValue(finProduct != null ? getDefaultString(finProduct.getCode()) : "");
			txtProductName.setValue(finProduct != null ? getDefaultString(finProduct.getDescEn()) : "");
			
			txtCampaignId.setValue(campaign != null ? campaign.getCode() : "");
			txtCampaingName.setValue(campaign != null ? campaign.getDescEn() : "");
			
			txtVatAmount.setValue(AmountUtils.format(contract.getVatFinancedAmount()));
			txtFlatRate.setValue(AmountUtils.format(contract.getInterestRate()));
			txtInstallment.setValue(AmountUtils.format(contract.getTiInstallmentAmount()));
			txtInstallmentAmountExc.setValue(AmountUtils.format(contract.getTeInstallmentAmount()));
			txtInstallmentAmountInc.setValue(AmountUtils.format(contract.getTiInstallmentAmount()));
			txtTerm.setValue(contract.getTerm() != null ? String.valueOf(contract.getTerm()) : "0");
			dfFirstDueDate.setValue(contract.getFirstDueDate());
			if (contract.getLastDueDate() != null) {
				dfLastDueDate.setValue(contract.getLastDueDate());
			} else {
				setLastDueDate(contract.getFirstDueDate());
			}
			txtFinanceAmount.setValue(AmountUtils.format(contract.getTiFinancedAmount()));
			txtFinanceAmountInc.setValue(AmountUtils.format(contract.getTiFinancedAmount()));
			txtFinanceAmountExc.setValue(AmountUtils.format(contract.getTeFinancedAmount()));
			
			txtEffectiveRate.setValue(AmountUtils.format(contract.getIrrRate()));
			txtNetFinanceAmountExc.setValue(AmountUtils.format(contract.getTeFinancedAmount()));
			txtNetFinanceAmountInc.setValue(AmountUtils.format(contract.getTiFinancedAmount()));
			txtNetEffectiveRate.setValue(AmountUtils.format(contract.getIrrRate()));
			txtTotalGrossInterestIncome.setValue("");
			txtTotalNetInterestIncome.setValue("");
			
			txtServiceFeeExVat.setValue("");
			txtServiceFeeIncVat.setValue("");
			txtSubsidyExVat.setValue("");
			txtSubsidyIncVat.setValue("");
			txtDirectIncomeTotalExVat.setValue("");
			txtDirectIncomeTotalIncVat.setValue("");
			
			txtCommissionExVat.setValue("");
			txtCommissionIncVat.setValue("");
			txtInsurancePremiumExVat.setValue("");
			txtInsurancePremiumIncVat.setValue("");
			txtDirectCostTotalExVat.setValue("");
			txtDirectCostTotalIncVat.setValue("");
			
			txtInsterestAmount.setValue(AmountUtils.format(contract.getGrossInterestAmount().getTeAmount()));
			txtTotalLoan.setValue(AmountUtils.format(MyNumberUtils.getDouble(contract.getLoanAmount().getTiAmount())));
			txtDownPayment.setValue(AmountUtils.format(MyNumberUtils.getDouble(contract.getTiAdvancePaymentAmount())));
			txtPrepaidTerms.setValue(getDefaultString(MyNumberUtils.getInteger(contract.getNumberPrepaidTerm())));
			ContractApplicant guarantorApplicant = contract.getContractApplicant(EApplicantType.G);
			Applicant guarantor = null;
			if (guarantorApplicant != null) {
				guarantor = guarantorApplicant.getApplicant();
			}
			txtGuarantor.setValue(guarantor == null ? "" : guarantor.getIndividual().getLastNameEn() + " " + guarantor.getIndividual().getFirstNameEn());
			List<ContractFinService> contractFinServices = contract.getContractFinServices();
			if (contractFinServices != null && !contractFinServices.isEmpty()) {
				for (ContractFinService contractFinService : contractFinServices) {
					if (contractFinService.getService() != null) {
						if (EServiceType.INSFEE.getCode().equals(contractFinService.getService().getCode())) {
							txtInsuranceFee.setValue(getDefaultString(contractFinService.getTiPrice()));	
						} else if (EServiceType.SRVFEE.getCode().equals(contractFinService.getService().getCode())) {
							txtServiceFee.setValue(getDefaultString(contractFinService.getTiPrice()));
						}
					}
				}
			}
			updateReceivedDocument(contract);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private Label getLabelIcon() {
		Label label = new Label();
		label.setIcon(FontAwesome.CHECK);
		return label;
	}
	
	/**
	 * 
	 * @param contract
	 */
	private void updateReceivedDocument(Contract contract) {
		gridLayout.removeAllComponents();
		documentsReceived = DOC_SRV.getDocumentsByContract(contract.getId());
		if (documentsReceived != null && !documentsReceived.isEmpty()) {
			int i = 1;
			gridLayout.setRows(documentsReceived.size() + 1);
        	gridLayout.addComponent(new Label("<h3 style=\"width:300px\">" + I18N.message("received.document") + "</h3>", ContentMode.HTML), 0, 0);
			for (ContractDocument contractDocument : documentsReceived) {
				CheckBox cbDocument = getDocumentCheckBox(contractDocument.getDocument(), contractDocument);
	    		gridLayout.addComponent(cbDocument, 0, i);
	    		if (EDocumentStatus.COMPLET.equals(contractDocument.getStatus())) {
	    			gridLayout.addComponent(getLabelIcon(), 1, i);
	    		}
	    		i++;
			}
			documentLayout.addComponent(gridLayout);
		}
	}
	
	/**
	 * 
	 * @param document
	 * @param contractDocument
	 * @return
	 */
	private CheckBox getDocumentCheckBox(Document document, ContractDocument contractDocument) {
		CheckBox cbDocument = new CheckBox();
		cbDocument.setCaption(document != null ? document.getDescEn() : "");
		cbDocument.setData(contractDocument);
		cbDocument.setValue(true);
		cbDocument.setEnabled(false);
		return cbDocument;
	}
	
	/**
	 * Reset controls
	 */
	public void reset() {
		removeErrorsPanel();
		contract = Contract.createInstance();
		documentLayout.removeAllComponents();
		txtApplicationId.setValue("");
		dfApplicationDate.setValue(null);
		txtApplicationTime.setValue("");
		txtDealerShopId.setValue("");
		txtDealerShopName.setValue("");
		txtContractId.setValue("");
		dfContractDate.setValue(null);
		txtProductId.setValue("");
		txtProductName.setValue("");
		txtCampaignId.setValue("");
		txtCampaingName.setValue("");
		txtFlatRate.setValue("");
		txtInstallment.setValue("");
		txtTerm.setValue("");
		dfFirstDueDate.setValue(null);
		dfLastDueDate.setValue(null);
		txtFinanceAmount.setValue("");
		txtInsterestAmount.setValue("");
		txtVatAmount.setValue("");
		txtTotalLoan.setValue("");
		txtDownPayment.setValue("");
		txtPrepaidTerms.setValue("");
		txtServiceFee.setValue("");
		txtInsuranceFee.setValue("");
		txtGuarantor.setValue("");
		txtInstallmentAmountExc.setValue("");
		txtInstallmentAmountInc.setValue("");
		txtFinanceAmountInc.setValue("");
		txtFinanceAmountExc.setValue("");
		txtEffectiveRate.setValue("");
		txtNetFinanceAmountExc.setValue("");
		txtNetFinanceAmountInc.setValue("");
		txtNetEffectiveRate.setValue("");
		txtTotalGrossInterestIncome.setValue("");
		txtTotalNetInterestIncome.setValue("");
		txtServiceFeeExVat.setValue("");
		txtServiceFeeIncVat.setValue("");
		txtSubsidyExVat.setValue("");
		txtSubsidyIncVat.setValue("");
		txtDirectIncomeTotalExVat.setValue("");
		txtDirectIncomeTotalIncVat.setValue("");
		txtCommissionExVat.setValue("");
		txtCommissionIncVat.setValue("");
		txtInsurancePremiumExVat.setValue("");
		txtInsurancePremiumIncVat.setValue("");
		txtDirectCostTotalExVat.setValue("");
		txtDirectCostTotalIncVat.setValue("");
	}
	
	/**
	 * 
	 * @return
	 */
	private VerticalLayout applicationDetailPanel() {
		FormLayout formLayout = getFormLayout();
		formLayout.addComponent(txtApplicationId);
		formLayout.addComponent(dfApplicationDate);
		formLayout.addComponent(txtApplicationTime);
		formLayout.addComponent(dfApprovalDate);
		formLayout.addComponent(txtApprovalTime);
		formLayout.addComponent(txtDealerShopId);
		formLayout.addComponent(txtDealerShopName);
		
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.setSpacing(true);
		horLayout.addComponent(formLayout);
		horLayout.addComponent(ComponentFactory.getSpaceLayout(150, Unit.PIXELS));
		horLayout.addComponent(documentLayout);
		
		VerticalLayout verLayout = getVerticalLayout();
		verLayout.addComponent(horLayout);
		
		return verLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private VerticalLayout contractDetailPanel() {
		FormLayout formLayoutLeft = getFormLayout();
		formLayoutLeft.addComponent(txtContractId);
		formLayoutLeft.addComponent(dfContractDate);
		formLayoutLeft.addComponent(txtProductId);
		formLayoutLeft.addComponent(txtProductName);
		formLayoutLeft.addComponent(txtCampaignId);
		formLayoutLeft.addComponent(txtCampaingName);
		formLayoutLeft.addComponent(txtFlatRate);
		formLayoutLeft.addComponent(txtInstallment);
		formLayoutLeft.addComponent(txtTerm);
		formLayoutLeft.addComponent(dfFirstDueDate);
		formLayoutLeft.addComponent(dfLastDueDate);
		formLayoutLeft.addComponent(txtFinanceAmount);
		formLayoutLeft.addComponent(txtInsterestAmount);
		formLayoutLeft.addComponent(txtVatAmount);
		formLayoutLeft.addComponent(txtTotalLoan);
		formLayoutLeft.addComponent(txtDownPayment);
		formLayoutLeft.addComponent(txtPrepaidTerms);
		
		FormLayout formLayoutRight = getFormLayout();
		formLayoutRight.addComponent(txtServiceFee);
		formLayoutRight.addComponent(txtInsuranceFee);
		formLayoutRight.addComponent(txtGuarantor);
		formLayoutRight.addComponent(txtFinanceAmountExc);
		formLayoutRight.addComponent(txtFinanceAmountInc);
		formLayoutRight.addComponent(txtInstallmentAmountExc);
		formLayoutRight.addComponent(txtInstallmentAmountInc);
		formLayoutRight.addComponent(txtNetFinanceAmountExc);
		formLayoutRight.addComponent(txtNetFinanceAmountInc);
		formLayoutRight.addComponent(txtEffectiveRate);
		formLayoutRight.addComponent(txtNetEffectiveRate);
		formLayoutRight.addComponent(txtTotalGrossInterestIncome);
		formLayoutRight.addComponent(txtTotalNetInterestIncome);
		
		HorizontalLayout horTopLayout = new HorizontalLayout(formLayoutLeft, ComponentFactory.getSpaceLayout(100, Unit.PIXELS),
				formLayoutRight);
		
		FormLayout directIncomeForm = getFormLayout();
		directIncomeForm.setCaption("<h3>" + I18N.message("direct.income") + "</h3>");
		directIncomeForm.addComponent(txtServiceFeeExVat);
		directIncomeForm.addComponent(txtServiceFeeIncVat);
		directIncomeForm.addComponent(txtSubsidyExVat);
		directIncomeForm.addComponent(txtSubsidyIncVat);
		directIncomeForm.addComponent(txtDirectIncomeTotalExVat);
		directIncomeForm.addComponent(txtDirectIncomeTotalIncVat);
		
		FormLayout directCostForm = getFormLayout();
		directCostForm.setCaption("<h3>" + I18N.message("direct.cost") + "</h3>");
		directCostForm.addComponent(txtCommissionExVat);
		directCostForm.addComponent(txtCommissionIncVat);
		directCostForm.addComponent(txtInsurancePremiumExVat);
		directCostForm.addComponent(txtInsurancePremiumIncVat);
		directCostForm.addComponent(txtDirectCostTotalExVat);
		directCostForm.addComponent(txtDirectCostTotalIncVat);
		
		final HorizontalLayout horBottomLayout = new HorizontalLayout(directIncomeForm, ComponentFactory.getSpaceLayout(100, Unit.PIXELS), directCostForm);
		horBottomLayout.setVisible(false);
		
		
		VerticalLayout mainVerLayout = getVerticalLayout();
		mainVerLayout.addComponent(horTopLayout);
		mainVerLayout.addComponent(btnApplicationDetailLink);
		mainVerLayout.setComponentAlignment(btnApplicationDetailLink, Alignment.MIDDLE_RIGHT);
		mainVerLayout.addComponent(ComponentFactory.getSpaceHeight(10, Unit.PIXELS));
		mainVerLayout.addComponent(horBottomLayout);
		
		btnApplicationDetailLink.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = -6841795960748572362L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				if (horBottomLayout.isVisible()) {
					horBottomLayout.setVisible(false);
				} else {
					horBottomLayout.setVisible(true);
				}
			}
		});
		
		return mainVerLayout;
	}
	
	/**
	 * 
	 * @param enabled
	 */
	private void setEnabledLoanControls(boolean enabled) {
		dfApplicationDate.setEnabled(enabled);
		dfApprovalDate.setEnabled(enabled);
		dfContractDate.setEnabled(enabled);
		dfLastDueDate.setEnabled(enabled);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.toolbar.event.SaveClickListener#saveButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void saveButtonClick(ClickEvent event) {
		errors.clear();
		if (validate()) {
			SettingConfig settingConfig = ENTITY_SRV.getByCode(SettingConfig.class, "max.first.due.date.fixation");
			Integer maxFirstDueDay = 45;
			if (settingConfig != null && StringUtils.isNotEmpty(settingConfig.getValue())) {
				maxFirstDueDay = Integer.parseInt(settingConfig.getValue().toString());
			}

			Date firstDueDate = DateUtils.getDateAtBeginningOfDay(dfFirstDueDate.getValue());
			Date maxFirstDueDate = DateUtils.addDaysDate(DateUtils.getDateAtBeginningOfDay(dfContractDate.getValue()), maxFirstDueDay);

			if (DateUtils.getDateAtBeginningOfDay(firstDueDate).compareTo(DateUtils.getDateAtBeginningOfDay(maxFirstDueDate)) < 0) {
				if (DateUtils.getDateAtBeginningOfDay(dfContractDate.getValue()).compareTo(DateUtils.getDateAtBeginningOfDay(dfFirstDueDate.getValue())) > 0) {							
					MessageBox mb = new MessageBox(UI.getCurrent(), "400px", "160px", I18N.message("information"),
							MessageBox.Icon.ERROR, I18N.message("first.due.date.should.be.greater.than.or.equals.contract.start.date"),
							Alignment.MIDDLE_RIGHT, new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
					mb.show();
				} else {
					try {
						saveEntity();
						displaySuccess();
						summaryPanel.assignValue(contract);
					} catch (Exception ex) {
						logger.error(ex.getMessage(), ex);
						errors.add(I18N.message("msg.error.technical"));
						errors.add(ex.getMessage());
					}
					if (!errors.isEmpty()) {
						displayErrorsPanel();
					}
				}
			} else {
				MessageBox mb = new MessageBox(UI.getCurrent(), "400px", "160px", I18N.message("information"),
					MessageBox.Icon.ERROR, I18N.message(
						"first.due.date.should.be.less.than.max.first.data.payment", "" + 
						DateUtils.getDateLabel(maxFirstDueDate, DateUtils.DEFAULT_DATE_FORMAT)), 
						Alignment.MIDDLE_RIGHT, new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
				mb.show();
			}
		} else {
			displayErrorsPanel();
		}
	}
	
	/**
	 * Save Entity
	 */
	public void saveEntity() {
		contract.setFirstDueDate(dfFirstDueDate.getValue());
		contract.setLastDueDate(dfLastDueDate.getValue());
		CONT_SRV.saveOrUpdate(contract);
	}
	
	/**
	 * Validate
	 * @return
	 */
	private boolean validate() {
		checkMandatoryDateField(dfFirstDueDate, "first.due.date");
		if (contract == null) {
			errors.add(I18N.message("contract.cannot.be.null"));
		}
		return errors.isEmpty();
	}
	
	/**
	 * Validate for activated
	 * @return
	 */
	public List<String> validateForActivated() {
		if (contract != null) {
			if (contract.getFirstDueDate() == null) {
				errors.add(I18N.message("field.required.1", I18N.message("first.due.date")));
			}
		}
		return errors;
	}
	
	/**
	 * 
	 * @param firstDueDate
	 */
	private void setLastDueDate(Date firstDueDate) {
		Integer term = getInteger(txtTerm);
		if (firstDueDate != null) {
			dfLastDueDate.setValue(DateUtils.addMonthsDate(DateUtils.addMonthsDate(firstDueDate, term), -1));
		} else {
			dfLastDueDate.setValue(null);
		}
	}

	/**
	 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
	 */
	@Override
	public void valueChange(ValueChangeEvent event) {
		setLastDueDate(dfFirstDueDate.getValue());
	}
	
}
