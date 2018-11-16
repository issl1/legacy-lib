package com.nokor.efinance.gui.ui.panel.contract.activation;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyMathUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.asset.model.AssetRange;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractApplicant;
import com.nokor.efinance.core.contract.model.ContractFinService;
import com.nokor.efinance.core.contract.service.ContractUtils;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.ersys.core.hr.model.eref.EColor;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
/**
 * 
 * @author buntha.chea
 *
 */
public class UpdateDataPanelOld extends AbstractTabPanel implements ClickListener, FinServicesHelper {

	/** * */
	private static final long serialVersionUID = -6172772406412518562L;

	private Label lblApplicantionIdValue;
	private Label lblApplicantDateValue;
	private Label lblCheckerIdValue;
	private Label lblCheckerPhoneNumberValue;
	
	private Label lblContractIdValue;
	private Label lblContractDateValue;
	private Label lblContractStatusValue;
	private Label lblLoanTypeValue;
	
	private Label lblProductNameValue;
	private Label lblBorrowerFullNameValue;
	private Label lblGuarantor1FullNameValue;
	private Label lblGuarantor2FullNameValue;
	
	private Label lblLoanAmountValue;
	private Label lblFinanceAmountValue;
	private Label lblInterestValue;
	private Label lblVatValue;
	
	private Label lblDownPaymentValue;
	private Label lblInstallmentValue;	
	private Label lblTermsValue;
	private Label lblPrepaidTermsValue;
	
	private Label lblFlatRateValue;
	private Label lblEffactiveRateValue;
	private Label lblInsurancePremiumValue;
	private Label lblCommission2Value;
	
	private Label lblCommissionValue;
	private Label lblServiceFeeValue;
	private Label lblSubSidyValue;
	private Label lblNetFinanceAmountValue;
	
	private Label lblNetInterestAmountValue;
	private Label lblNetEffactiveRateValue;
	private Label lblLastDueDateValue;
	
	private Label lblBrandValue;
	private Label lblAssetValue;
	private Label lblCCValue;
	private Label lblModelValue;
	
	private Label lblSerieValue;
	private Label lblYearValue;
	private Label lblMileAgeValue;
	private Label lblDealerShopNameValue;
	
	private Label lblMotorbikePriceValue;
	
//	private AutoDateField dfFirstDueDate;
	private ComboBox cbxDay;
	private ComboBox cbxMonth;
	private ComboBox cbxYear;
	
	private TextField txtTaxInvoiceId;
	private TextField txtMotorbikePrice;
	
	private TextField txtEngineNo;
	private TextField txtChassisNo;
	private ERefDataComboBox<EColor> cbxColor;
	
	// private Button btnSubmit;
	private Button btnSave;
	private Button btnEdit;
	private Button btnModifyLoan;
	
	private Contract contract;
	
	// private SchedulePanel schedulePanel;
	
	public UpdateDataPanelOld() {
		super();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		setSpacing(true);
		super.setMargin(false);
		
		// schedulePanel = new SchedulePanel();
		
		lblApplicantionIdValue = getLabelValue();
		lblApplicantDateValue = getLabelValue();
		lblCheckerIdValue = getLabelValue();
		lblCheckerPhoneNumberValue = getLabelValue();
		
		lblContractIdValue = getLabelValue();
		lblContractDateValue = getLabelValue();
		lblContractStatusValue = getLabelValue();
		lblLoanTypeValue = getLabelValue();
		
		lblProductNameValue = getLabelValue();
		lblBorrowerFullNameValue = getLabelValue();
		lblGuarantor1FullNameValue = getLabelValue();
		lblGuarantor2FullNameValue = getLabelValue();
		
		lblLoanAmountValue = getLabelValue();
		lblFinanceAmountValue = getLabelValue();
		lblInterestValue = getLabelValue();
		lblVatValue = getLabelValue();
		
		lblDownPaymentValue = getLabelValue();
		lblInstallmentValue = getLabelValue();
		lblTermsValue = getLabelValue();
		lblPrepaidTermsValue = getLabelValue();
		
		lblFlatRateValue = getLabelValue();
		lblEffactiveRateValue = getLabelValue();
		lblInsurancePremiumValue = getLabelValue();
		lblCommission2Value = getLabelValue();
		
		lblCommissionValue = getLabelValue();
		lblServiceFeeValue = getLabelValue();
		lblSubSidyValue = getLabelValue();
		lblNetFinanceAmountValue = getLabelValue();
		
		lblNetInterestAmountValue = getLabelValue();
		lblNetEffactiveRateValue = getLabelValue();
		lblLastDueDateValue = getLabelValue();
		
		lblBrandValue = getLabelValue();
		lblAssetValue = getLabelValue();
		lblCCValue = getLabelValue();
		lblModelValue = getLabelValue();
		
		lblSerieValue = getLabelValue();
		lblYearValue = getLabelValue();
		lblMileAgeValue = getLabelValue();
		lblDealerShopNameValue = getLabelValue();
		
		lblMotorbikePriceValue = getLabelValue();
		
		// First due date
		cbxDay = ComponentFactory.getComboBox();
		cbxDay.setCaption(I18N.message("day"));
		cbxDay.setWidth(50, Unit.PIXELS);
		cbxDay.setRequired(true);
		cbxMonth = ComponentFactory.getComboBox();
		cbxMonth.setCaption(I18N.message("month"));
		cbxMonth.setWidth(50, Unit.PIXELS);
		cbxMonth.setRequired(true);
		cbxYear = ComponentFactory.getComboBox();
		cbxYear.setCaption(I18N.message("year"));
		cbxYear.setWidth(65, Unit.PIXELS);
		cbxYear.setRequired(true);
		initFirstDueDateComboBox();
		
		cbxColor = new ERefDataComboBox<>(I18N.message("color"), EColor.class);
		cbxColor.setWidth(140, Unit.PIXELS);
		cbxColor.setRequired(true);
		txtChassisNo = ComponentFactory.getTextField("chassis.no", true, 30, 140);
		txtEngineNo = ComponentFactory.getTextField("engine.no", true, 30, 140);
		txtTaxInvoiceId = ComponentFactory.getTextField("tax.invoice.no", true, 30, 140);
		txtMotorbikePrice = ComponentFactory.getTextField("motobike.price", true, 30, 140);
		lblDownPaymentValue = getLabelValue();
		
		btnSave = ComponentLayoutFactory.getButtonStyle("save", FontAwesome.SAVE, 80, "btn btn-success button-small");
		btnSave.addClickListener(this);
		
		btnEdit = ComponentLayoutFactory.getButtonStyle("edit", FontAwesome.EDIT, 80, "btn btn-success button-small");
		btnEdit.addClickListener(this);
		
		// btnSubmit = ComponentLayoutFactory.getButtonStyle("submit", FontAwesome.ARROW_CIRCLE_RIGHT, 80, "btn btn-success button-small");
		// btnSubmit.addClickListener(this);
		btnModifyLoan = ComponentLayoutFactory.getButtonStyle("modify.loan", FontAwesome.EDIT, 80, "btn btn-success button-small");
		btnModifyLoan.setEnabled(false);
		btnModifyLoan.setWidth("95px");
		
		String template = "activationInfomation";
		InputStream layoutFile = getClass().getResourceAsStream("/VAADIN/themes/efinance/layouts/" + template + ".html");
		CustomLayout activationInfoLayout = null;
		try {
			activationInfoLayout = new CustomLayout(layoutFile);
		} catch (IOException e) {
			Notification.show("Could not locate template " + template, e.getMessage(), Type.ERROR_MESSAGE);
		}
		
		activationInfoLayout.addComponent(new Label(I18N.message("application.id")), "lblApplicationId");
		activationInfoLayout.addComponent(lblApplicantionIdValue, "lblApplicantionIdValue");
		activationInfoLayout.addComponent(new Label(I18N.message("application.date")), "lblApplicationDate");
		activationInfoLayout.addComponent(lblApplicantDateValue, "lblApplicationDateValue");
		activationInfoLayout.addComponent(new Label(I18N.message("checker.id")), "lblCheckerId");
		activationInfoLayout.addComponent(lblCheckerIdValue, "lblCheckerIdValue");
		activationInfoLayout.addComponent(new Label(I18N.message("checker.phone.number")), "lblCheckerPhoneNumber");
		activationInfoLayout.addComponent(lblCheckerPhoneNumberValue, "lblCheckerPhoneNumberValue");
		
		activationInfoLayout.addComponent(new Label(I18N.message("contract.id")), "lblContractId");
		activationInfoLayout.addComponent(lblContractIdValue, "lblContractIdValue");
		activationInfoLayout.addComponent(new Label(I18N.message("contract.date")), "lblContractDate");
		activationInfoLayout.addComponent(lblContractDateValue, "lblContractDateValue");
		activationInfoLayout.addComponent(new Label(I18N.message("contract.status")), "lblContractStatus");
		activationInfoLayout.addComponent(lblContractStatusValue, "lblContractStatusValue");
		activationInfoLayout.addComponent(new Label(I18N.message("loan.type")), "lblLoanType");
		activationInfoLayout.addComponent(lblLoanTypeValue, "lblLoanTypeValue");
		
		activationInfoLayout.addComponent(new Label(I18N.message("products.name")), "lblProductName");
		activationInfoLayout.addComponent(lblProductNameValue, "lblProductNameValue");
		activationInfoLayout.addComponent(new Label(I18N.message("borrower.fullname")), "lblBorrowerFullName");
		activationInfoLayout.addComponent(lblBorrowerFullNameValue, "lblBorrowerFullNameValue");
		activationInfoLayout.addComponent(new Label(I18N.message("guarantor.1.fullname")), "lblGuarantor1FullName");
		activationInfoLayout.addComponent(lblGuarantor1FullNameValue, "lblGuarantor1FullNameValue");
		activationInfoLayout.addComponent(new Label(I18N.message("guarantor.2.fullname")), "lblGuarantor2FullName");
		activationInfoLayout.addComponent(lblGuarantor2FullNameValue, "lblGuarantor2FullNameValue");
		

		activationInfoLayout.addComponent(new Label(I18N.message("motobike.price")), "lblMotobikePrice");
		activationInfoLayout.addComponent(lblMotorbikePriceValue, "lblMotorbikePriceValue");
		
		activationInfoLayout.addComponent(new Label(I18N.message("loan.amount.inc.vat")), "lblLoanAmount");
		activationInfoLayout.addComponent(lblLoanAmountValue, "lblLoanAmountValue");
		activationInfoLayout.addComponent(new Label(I18N.message("finance.amount.ex.vat")), "lblFinanceAmount");
		activationInfoLayout.addComponent(lblFinanceAmountValue, "lblFinanceAmountValue");
		activationInfoLayout.addComponent(new Label(I18N.message("interest.amount")), "lblInterest");
		activationInfoLayout.addComponent(lblInterestValue, "lblInterestValue");
		activationInfoLayout.addComponent(new Label(I18N.message("vat")), "lblVAT");
		activationInfoLayout.addComponent(lblVatValue, "lblVatValue");
		
		activationInfoLayout.addComponent(new Label(I18N.message("down.payment.inc.vat")), "lblDownPayment");
		activationInfoLayout.addComponent(lblDownPaymentValue, "lblDownPaymentValue");
		activationInfoLayout.addComponent(new Label(I18N.message("installment.inc.vat")), "lblInstallment");
		activationInfoLayout.addComponent(lblInstallmentValue, "lblInstallmentValue");
		activationInfoLayout.addComponent(new Label(I18N.message("terms")), "lblTerms");
		activationInfoLayout.addComponent(lblTermsValue, "lblTermsValue");
		activationInfoLayout.addComponent(new Label(I18N.message("prepaid.terms")), "lblPrepaidTerms");
		activationInfoLayout.addComponent(lblPrepaidTermsValue, "lblPrepaidTermsValue");
		
		activationInfoLayout.addComponent(new Label(I18N.message("flat.rate")), "lblFlatRate");
		activationInfoLayout.addComponent(lblFlatRateValue, "lblFlatRateValue");
		activationInfoLayout.addComponent(new Label(I18N.message("effective.rate")), "lblEffectiveRate");
		activationInfoLayout.addComponent(lblEffactiveRateValue, "lblEffactiveRateValue");
		activationInfoLayout.addComponent(new Label(I18N.message("insurance.premiume.inc.vat")), "lblInsurancePremium");
		activationInfoLayout.addComponent(lblInsurancePremiumValue, "lblInsurancePremiumValue");
		activationInfoLayout.addComponent(new Label(I18N.message("commission.2.inc.vat")), "lblCommission2");
		activationInfoLayout.addComponent(lblCommission2Value, "lblCommission2Value");
		
		activationInfoLayout.addComponent(new Label(I18N.message("commission.1.inc.vat")), "lblCommission");
		activationInfoLayout.addComponent(lblCommissionValue, "lblCommissionValue");
		activationInfoLayout.addComponent(new Label(I18N.message("service.fee.inc.vat")), "lblServiceFee");
		activationInfoLayout.addComponent(lblServiceFeeValue, "lblServiceFeeValue");
		activationInfoLayout.addComponent(new Label(I18N.message("subsidy.inc.vat")), "lblSubSidy");
		activationInfoLayout.addComponent(lblSubSidyValue, "lblSubSidyValue");
		activationInfoLayout.addComponent(new Label(I18N.message("net.finance.amount.ex.vat")), "lblNetFinanceAmount");
		activationInfoLayout.addComponent(lblNetFinanceAmountValue, "lblNetFinanceAmountValue");
		
		activationInfoLayout.addComponent(new Label(I18N.message("net.interest.amout")), "lblNetInterestAmount");
		activationInfoLayout.addComponent(lblNetInterestAmountValue, "lblNetInterestAmountValue");
		activationInfoLayout.addComponent(new Label(I18N.message("net.effactive.rate")), "lblNetEffectiveRate");
		activationInfoLayout.addComponent(lblNetEffactiveRateValue, "lblNetEffactiveRateValue");
		activationInfoLayout.addComponent(new Label(I18N.message("last.due.date")), "lblLastDueDate");
		activationInfoLayout.addComponent(lblLastDueDateValue, "lblLastDueDateValue");
		
		activationInfoLayout.addComponent(new Label(I18N.message("brand")), "lblBrand");
		activationInfoLayout.addComponent(lblBrandValue, "lblBrandValue");
		activationInfoLayout.addComponent(new Label(I18N.message("asset")), "lblAsset");
		activationInfoLayout.addComponent(lblAssetValue, "lblAssetValue");
		activationInfoLayout.addComponent(new Label(I18N.message("cc")), "lblCC");
		activationInfoLayout.addComponent(lblCCValue, "lblCCValue");
		activationInfoLayout.addComponent(new Label(I18N.message("model")), "lblModel");
		activationInfoLayout.addComponent(lblModelValue, "lblModelValue");
		
		activationInfoLayout.addComponent(new Label(I18N.message("serie")), "lblSerie");
		activationInfoLayout.addComponent(lblSerieValue, "lblSerieValue");
		activationInfoLayout.addComponent(new Label(I18N.message("year")), "lblYear");
		activationInfoLayout.addComponent(lblYearValue, "lblYearValue");
		activationInfoLayout.addComponent(new Label(I18N.message("mileage")), "lblMileage");
		activationInfoLayout.addComponent(lblMileAgeValue, "lblMileAgeValue");
		activationInfoLayout.addComponent(new Label(I18N.message("dealer.shop.name")), "lblDealerShopName");
		activationInfoLayout.addComponent(lblDealerShopNameValue, "lblDealerShopNameValue");
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		horizontalLayout.setCaption(I18N.message("first.due.date"));
		horizontalLayout.addComponent(cbxDay);
		horizontalLayout.addComponent(cbxMonth);
		horizontalLayout.addComponent(cbxYear);
		
		FormLayout editLeftFormLayout = ComponentLayoutFactory.getFormLayoutCaptionAlignLeft();
		editLeftFormLayout.addComponent(horizontalLayout);
		editLeftFormLayout.addComponent(txtMotorbikePrice);
		editLeftFormLayout.addComponent(txtChassisNo);
		
		FormLayout editRightFormLayout = ComponentLayoutFactory.getFormLayoutCaptionAlignLeft();
		editRightFormLayout.addComponent(txtTaxInvoiceId);
		editRightFormLayout.addComponent(txtEngineNo);
		editRightFormLayout.addComponent(cbxColor);
		
		HorizontalLayout buttonsLayout = new HorizontalLayout();
		buttonsLayout.setSpacing(true);
		buttonsLayout.addComponent(btnEdit);
		buttonsLayout.addComponent(btnSave);
		
		HorizontalLayout editLayout = new HorizontalLayout();
		editLayout.setMargin(new MarginInfo(true, true, false, true));	
		editLayout.setSpacing(true);
		editLayout.addComponent(editLeftFormLayout);
		editLayout.addComponent(editRightFormLayout);
		
		VerticalLayout inputLayout = new VerticalLayout();
		inputLayout.setMargin(new MarginInfo(false, true, true, false));
		inputLayout.addComponent(editLayout);
		inputLayout.addComponent(buttonsLayout);
		inputLayout.setComponentAlignment(buttonsLayout, Alignment.BOTTOM_RIGHT);
		
		Panel editPanel = new Panel();
		editPanel.setSizeUndefined();
		editPanel.setContent(inputLayout);
				
		VerticalLayout activationEditLayout = new VerticalLayout();
		activationEditLayout.setMargin(true);
		activationEditLayout.addComponent(activationInfoLayout);
		activationEditLayout.addComponent(editPanel);
		
		Panel customPanel = new Panel(activationEditLayout);
		customPanel.setStyleName(Reindeer.PANEL_LIGHT);

		// HorizontalLayout buttonLayout = new HorizontalLayout();
		// buttonLayout.setSpacing(true);
		// buttonLayout.addComponent(btnSubmit);
		// buttonLayout.addComponent(btnModifyLoan);
				
		VerticalLayout verticalLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
		verticalLayout.addComponent(customPanel);
		/*verticalLayout.addComponent(buttonLayout);
		verticalLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_RIGHT);*/
		
		Panel infoPanel = new Panel();
		infoPanel.setContent(verticalLayout);
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		mainLayout.addComponent(infoPanel);
		// mainLayout.addComponent(schedulePanel);
		return mainLayout;
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	private String getDateFormat(Date date) {
		String dateFormat = DateUtils.getDateLabel(date, DateUtils.FORMAT_DDMMYYYY_SLASH); 
		return dateFormat != null ? dateFormat : "";
	}
	
	/**
	 * Assign Value
	 * @param contract
	 */
	public void assignValue(Contract contract) {
		reset();
		this.contract = contract;
		Applicant applicant = contract.getApplicant();
		// schedulePanel.setVisible(ContractUtils.isValidate(contract) || ContractUtils.isHoldPayment(contract));
		// schedulePanel.createSchedule(contract);
		Asset asset = contract.getAsset();
		AssetModel assetModel = null;
		AssetRange assetRange = null;
		AssetMake assetMake = null;
		
		lblApplicantionIdValue.setValue(getDescription(contract.getExternalReference()));
		lblApplicantDateValue.setValue(getDescription(getDateFormat(contract.getQuotationDate())));
		lblContractIdValue.setValue(getDescription(contract.getReference()));
		lblContractDateValue.setValue(getDescription(getDateFormat(contract.getStartDate())));
		lblContractStatusValue.setValue(getDescription(getDefaultString(contract.getWkfStatus().getDescEn())));
		lblInterestValue.setValue(getDescription(AmountUtils.format(contract.getGrossInterestAmount().getTeAmount())));
		lblNetInterestAmountValue.setValue(getDescription(AmountUtils.format(contract.getNetInterestAmount())));
		lblVatValue.setValue(getDescription(getDefaultString(AmountUtils.format(contract.getVatFinancedAmount() + contract.getGrossInterestAmount().getVatAmount()))));
		lblFlatRateValue.setValue(getDescription(AmountUtils.format(contract.getInterestRate())));
		lblEffactiveRateValue.setValue(getDescription(AmountUtils.format(contract.getIrrRate())));
		lblNetEffactiveRateValue.setValue(getDescription(AmountUtils.format(contract.getNetIrrRate())));
		
		lblCheckerIdValue.setValue(getDescription(getDefaultString(contract.getCheckerID())));
		lblCheckerPhoneNumberValue.setValue(getDescription(getDefaultString(contract.getCheckerPhoneNumber())));
			
		lblLoanAmountValue.setValue(getDescription(AmountUtils.format(contract.getLoanAmount().getTiAmount())));
		lblFinanceAmountValue.setValue(getDescription(AmountUtils.format(contract.getTeFinancedAmount())));
		lblNetFinanceAmountValue.setValue(getDescription(AmountUtils.format(contract.getNetFinanceAmount().getTeAmount())));
		lblTermsValue.setValue(getDescription(getDefaultString(contract.getTerm())));
		lblPrepaidTermsValue.setValue(getDescription(getDefaultString(MyNumberUtils.getInteger(contract.getNumberPrepaidTerm()))));
		lblInstallmentValue.setValue(getDescription(AmountUtils.format(contract.getTiInstallmentAmount())));
		
		if (contract.getProductLine() != null) {
			lblLoanTypeValue.setValue(getDescription(contract.getProductLine().getDescEn()));
		}
		if (contract.getFinancialProduct() != null) {
			lblProductNameValue.setValue(getDescription(contract.getFinancialProduct().getDescEn()));
		}
		
		assignValuesAssetInfo(contract);
		
		if (asset != null) {
			lblAssetValue.setValue(getDescription(asset.getDescEn()));
			lblMileAgeValue.setValue(getDescription(getDefaultString(asset.getMileage())));
			
			assetModel = asset.getModel();
			if (assetModel != null) {
				lblCCValue.setValue(getDescription(assetModel.getEngine() != null ? assetModel.getEngine().getDescLocale() : null));
				lblSerieValue.setValue(getDescription(assetModel.getDescLocale()));
				assetRange = assetModel.getAssetRange();
				if (assetRange != null) {
					lblModelValue.setValue(getDescription(assetRange.getDescLocale()));
					assetMake = assetRange.getAssetMake();
					lblBrandValue.setValue(getDescription(assetMake != null ? assetMake.getDescLocale() : ""));
				}
				
			}
			lblYearValue.setValue(getDescription(String.valueOf(asset.getYear())));
		}
		
		Dealer dealer = contract.getDealer();
		if (dealer != null) {
			lblDealerShopNameValue.setValue(getDescription(dealer.getDescEn()));
		}
		lblBorrowerFullNameValue.setValue(getDescription(getDefaultString(applicant.getNameEn())));
		
		List<ContractApplicant> contractApplicants = contract.getContractApplicants();
		int index = 1;
		for (ContractApplicant contractApplicant : contractApplicants) {
			if (EApplicantType.G.equals(contractApplicant.getApplicantType())) {
				if (index == 1) {
					lblGuarantor1FullNameValue.setValue(getDescription(contractApplicant.getApplicant().getNameEn()));
				} else if (index == 2) {
					lblGuarantor2FullNameValue.setValue(getDescription(contractApplicant.getApplicant().getNameEn()));
				}
				index++;
			}
		}
		List<ContractFinService> contractFinServices = contract.getContractFinServices();
		if (contractFinServices != null && !contractFinServices.isEmpty()) {
			for (ContractFinService contractFinService : contractFinServices) {
				if (contractFinService.getService() != null) {
					if (EServiceType.SRVFEE.getCode().equals(contractFinService.getService().getCode())) {
						lblServiceFeeValue.setValue(getDescription(getDefaultString(MyNumberUtils.getDouble(contractFinService.getTiPrice()))));
					} else if (EServiceType.COMM.getCode().equals(contractFinService.getService().getCode())) {
						lblCommissionValue.setValue(getDescription(getDefaultString(MyNumberUtils.getDouble(contractFinService.getTiPrice()))));
					}
				}
			}
		}
		lblSubSidyValue.setValue(getDescription(I18N.message("unknow")));
		lblInsurancePremiumValue.setValue(getDescription(I18N.message("unknow")));
		lblCommission2Value.setValue(getDescription(I18N.message("unknow")));
		//lblNetFinanceAmountValue.setValue(getDescription(I18N.message("unknow")));
		//lblNetInterestAmountValue.setValue(getDescription(I18N.message("unknow")));
	}
	
	/**
	 * 
	 * @param contract
	 */
	private void assignValuesAssetInfo(Contract contract) {
		setFirstDueDateComboBox(contract.getFirstDueDate());
		lblLastDueDateValue.setValue(getDescription(getDateFormat(contract.getLastDueDate())));
		lblDownPaymentValue.setValue(getDescription(AmountUtils.format(contract.getTiAdvancePaymentAmount())));
		Asset asset = contract.getAsset();
		if (asset != null) {
			cbxColor.setSelectedEntity(asset.getColor());
			txtChassisNo.setValue(asset.getChassisNumber());
			txtEngineNo.setValue(asset.getEngineNumber());
			txtTaxInvoiceId.setValue(asset.getTaxInvoiceNumber());
			txtMotorbikePrice.setValue(getDefaultString(MyNumberUtils.getDouble(asset.getTiAssetPrice())));
			lblMotorbikePriceValue.setValue(getDescription(getDefaultString(MyNumberUtils.getDouble(asset.getTiAssetPrice()))));
		}
		setEnableControls(contract, false);
	}
	
	/**
	 * 
	 * @param contract
	 */
	private void refreshInformations(Contract contract) {
		assignValuesAssetInfo(contract);
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	private String getDescription(String value) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<b>");
		stringBuffer.append(value == null ? "" : value);
		stringBuffer.append("</b>");
		return stringBuffer.toString();
	}
	
	/**
	 * 
	 * @return
	 */
	private Label getLabelValue() {
		Label label = new Label("", ContentMode.HTML);
		return label;
	}

	/**
	 * Reset
	 */
	public void reset() {
		removeErrorsPanel();
		lblApplicantionIdValue.setValue("");
		lblApplicantDateValue.setValue("");
		lblContractIdValue.setValue("");
		lblContractDateValue.setValue("");
		lblContractStatusValue.setValue("");
		lblInterestValue.setValue("");
		lblNetInterestAmountValue.setValue("");
		lblVatValue.setValue("");
		lblFlatRateValue.setValue("");
		lblEffactiveRateValue.setValue("");
		lblNetEffactiveRateValue.setValue("");
		lblCheckerIdValue.setValue("");
		lblCheckerPhoneNumberValue.setValue("");
		lblLastDueDateValue.setValue("");
		lblLoanAmountValue.setValue("");
		lblFinanceAmountValue.setValue("");
		lblNetFinanceAmountValue.setValue("");
		lblTermsValue.setValue("");
		lblPrepaidTermsValue.setValue("");
		lblInstallmentValue.setValue("");
		lblLoanTypeValue.setValue("");
		lblProductNameValue.setValue("");
		lblAssetValue.setValue("");
		lblMileAgeValue.setValue("");
		lblCCValue.setValue("");
		lblSerieValue.setValue("");
		lblModelValue.setValue("");
		lblBrandValue.setValue("");
		lblYearValue.setValue("");
		setFirstDueDateComboBox(DateUtils.today());
		cbxColor.setSelectedEntity(null);
		txtChassisNo.setValue("");
		txtEngineNo.setValue("");
		txtTaxInvoiceId.setValue("");
		txtMotorbikePrice.setValue("");
	}
	
	/**
	 * 
	 * @param isEnable
	 */
	private void setEnableControls(Contract contract, boolean isEnable) {
		cbxDay.setEnabled(isEnable);
		cbxMonth.setEnabled(isEnable);
		cbxYear.setEnabled(isEnable);
		cbxColor.setEnabled(isEnable);
		txtChassisNo.setEnabled(isEnable);
		txtEngineNo.setEnabled(isEnable);
		txtTaxInvoiceId.setEnabled(isEnable);
		txtMotorbikePrice.setEnabled(isEnable);
		btnSave.setVisible(isEnable);
		btnEdit.setVisible(ContractUtils.isBeforeActive(contract) && !isEnable);
		// btnSubmit.setVisible(isEnable);
	}
	
	/**
	 * True if submit & false if activate
	 * @param displaySucessMessage
	 * @return
	 */
	public boolean doSave(boolean displaySucessMessage) {
		if (validation()) {
			if (displaySucessMessage) {
				try {
					updateContractAndAssetEntity();
					displaySuccess();
					// schedulePanel.createSchedule(contract);
				} catch (Exception ex) {
					logger.error(ex.getMessage(), ex);
					errors.add(I18N.message("msg.error.technical"));
					errors.add(ex.getMessage());
				}
				if (!errors.isEmpty()) {
					displayErrorsPanel();
				}
			}
			return true;
		} else {
			displayErrorsPanel();
			return false;
		}
	}
	
	/**
	 * display error
	 */
	private boolean validation() {
		removeErrorsPanel();
		
		if (errors.isEmpty()) {
			Date firstDueDate = getFirstDueDateValue();
			errors = CONT_ACTIVATION_SRV.validation(contract, firstDueDate, txtChassisNo.getValue(), txtEngineNo.getValue(), txtTaxInvoiceId.getValue());
		}
		
		return errors.isEmpty();
	}
	
	/**
	 * 
	 * @return
	 */
	private List<String> checkValidFields() {
		removeErrorsPanel();
		checkMandatorySelectField(cbxColor, "color");
		checkMandatoryField(txtChassisNo, "chassis.no");
		checkMandatoryField(txtEngineNo, "engine.no");
		checkMandatoryField(txtTaxInvoiceId, "tax.invoice.no");
		checkMandatoryField(txtMotorbikePrice, "motorbike.price");
		checkMandatorySelectField(cbxDay, "day");
		checkMandatorySelectField(cbxMonth, "month");
		checkMandatorySelectField(cbxYear, "year");
		return errors;
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnEdit) {
			setEnableControls(contract, true);
		/*} else if (event.getButton() == btnSubmit) {
			doSave(true);*/
		} else if (event.getButton() == btnSave) {
			removeErrorsPanel();
			Asset asset = contract.getAsset();
			if (asset != null) {
				if (checkValidFields().isEmpty()) {
					try {
						updateContractAndAssetEntity();
						displaySuccess();
						setEnableControls(contract, false);
					} catch (Exception ex) {
						errors.add(I18N.message("msg.error.technical"));
						errors.add(ex.getMessage());
					}
					if (!errors.isEmpty()) {
						displayErrorsPanel();
					}
				} else {
					displayErrorsPanel();
				}
			}
		}
	}	

	/**
	 * 
	 * @param firstDueDate
	 */
	private Date getLastDueDate(Date firstDueDate) {
		Integer term = MyNumberUtils.getInteger(contract.getTerm());
		Date lastDueDate = null;
		if (firstDueDate != null) {
			lastDueDate = DateUtils.addMonthsDate(DateUtils.addMonthsDate(firstDueDate, term), -1);
		} 
		return lastDueDate;
	}
	
	/**
	 * Save Entity
	 */
	private void updateContractAndAssetEntity() {
		// saveOrUpdate asset info & advance payment amount
		CONT_SRV.updateContractAndAsset(getContract());
		refreshInformations(contract);
	}
		
	/**
	 * 
	 * @return
	 */
	private Contract getContract() {
		// Update First Due Date
		contract.setFirstDueDate(getFirstDueDateValue());
		contract.setLastDueDate(getLastDueDate(getFirstDueDateValue()));
		// Update asset price 
		Asset asset = contract.getAsset();
		double assetPriceUsd = MyNumberUtils.getDouble(getDouble(txtMotorbikePrice));
		asset.setTiAssetPrice(assetPriceUsd);
		asset.setTeAssetPrice(assetPriceUsd);
		asset.setVatAssetPrice(0d);
		asset.setChassisNumber(txtChassisNo.getValue());
		asset.setEngineNumber(txtEngineNo.getValue());
		asset.setTaxInvoiceNumber(txtTaxInvoiceId.getValue());
		asset.setColor(cbxColor.getSelectedEntity());
		// Update advance payment amount & advance payment percentage
		double advancePaymentAmount = assetPriceUsd - MyNumberUtils.getDouble(contract.getTiFinancedAmount());
		double advancePaymentPercentage = getAdvancePaymentPercentage(advancePaymentAmount, assetPriceUsd);
		contract.setTiAdvancePaymentAmount(advancePaymentAmount);
		contract.setTeAdvancePaymentAmount(advancePaymentAmount);
		contract.setVatAdvancePaymentAmount(0d);
		contract.setAdvancePaymentPercentage(advancePaymentPercentage);
		return contract;
	}
	
	/**
	 * 
	 * @param advancePaymentAmt
	 * @param assetPrice
	 * @return
	 */
	private Double getAdvancePaymentPercentage(double advancePaymentAmt, double assetPrice) {
		if (advancePaymentAmt > 0) {
			double total = (advancePaymentAmt / assetPrice);
			return MyMathUtils.roundTo(total * 100, 2);
		}
		return 0.00d;
	}
	
	/**
	 * Init ComboBox of First due date
	 */
	private void initFirstDueDateComboBox() {
		for (int i = 2015; i < 2031; i++) {
			cbxYear.addItem(i);
			cbxYear.setItemCaption(i, String.valueOf(i));
		}
		for (int i = 1; i < 13; i++) {
			cbxMonth.addItem(i);
			cbxYear.setItemCaption(i, String.valueOf(i));
		}
		for (int i : new int[] {1, 5, 10, 15, 20}) {
			cbxDay.addItem(i);
			cbxYear.setItemCaption(i, String.valueOf(i));
		}
	}
	
	/**
	 * Set First due date ComboBox
	 * @param firstDueDate
	 */
	private void setFirstDueDateComboBox(Date firstDueDate) {
		if (firstDueDate != null) {
			int day = DateUtils.getDay(firstDueDate);
			int month = DateUtils.getMonth(firstDueDate);
			int year = DateUtils.getYear(firstDueDate);
			cbxDay.setValue(day);
			cbxMonth.setValue(month);
			cbxYear.setValue(year);
		}
	}
	
	/**
	 * Get First due date from combobox
	 * @return
	 */
	private Date getFirstDueDateValue() {
		if (cbxDay.getValue() != null && cbxMonth.getValue() != null && cbxYear.getValue() != null) {
			int day = (int) cbxDay.getValue();
			int month = (int) cbxMonth.getValue();
			int year = (int) cbxYear.getValue();
			NumberFormat nf = new DecimalFormat("00");
			String dateLabel = nf.format(day) + nf.format(month) + String.valueOf(year);
			return DateUtils.getDate(dateLabel, DateUtils.FORMAT_STR_DDMMYYYY_NOSEP);
		}
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean validateSaved() {
		if (btnSave.isVisible()) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "320px", "160px", I18N.message("information"),
					MessageBox.Icon.ERROR, I18N.message("please.save.data.before.activate"), 
					Alignment.MIDDLE_RIGHT, new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
			return false;
		} 
		return true;
	}
}
