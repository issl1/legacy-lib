package com.nokor.efinance.gui.ui.panel.contract;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.ersys.core.hr.model.eref.ECivility;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;


/**
 * 
 * @author uhout.cheng
 */
public class InsurancePanel extends AbstractTabPanel implements ValueChangeListener {

	/** */
	private static final long serialVersionUID = 2909358859022941627L;
	
	//lost insurance
	private TextField txtVehicleType;
	private TextField txtInsuranceCompany;
	private TextField txtInsuranceAgent;
	private TextField txtInsuranceLevel;
	private TextField txtInsurancePolicyNo;
	private TextField txtInsurancePeriodYear;
	private TextField txtPeriodInsuredFrom;
	private TextField txtPeriodInsuredTo;
	private AutoDateField dfPremiumPaymentDate;
	private TextField txtCapitalInsuranceYearOne;
	private TextField txtCapitalInsuranceYearTwo;
	private TextField txtCapitalInsuranceYearThree;
	private TextField txtSumInsuredDeath;
	private TextField txtNetPremium;
	private TextField txtStampTax;
	private TextField txtVAT;
	private TextField txtWithholdingTax;
	private TextField txtTotalPremium;
	private TextField txtReceiptPremiumAmount;
	
	private AutoDateField dfCompensationReceivedDate;
	private AutoDateField dfChequeDate;
	private TextField txtChequeNo;
	private TextField txtAmount;
	private TextField txtCompensationAmount;
	
	//AOM insurance
	private CheckBox cbfirst;
	private TextField txtCompanyName;
	private TextField txtPolicyNo;
	private TextField txtAOMPeriodInsuredFrom;
	private TextField txtAOMPeriodInsuredTo;
	private TextField txtPremium;
	private TextField txtSumOfInsurance;
	
	private CheckBox cbExtension;
	private TextField txtAOMVehicleType;
	private TextField txtAOMInsuranceCompany;
	private TextField txtAOMInsuranceAgent;
	private TextField txtAOMNo;
	private TextField txtAOMInsurancePeriodYear;
	private TextField txtAOMExtensionPeriodInsuredFrom;
	private TextField txtAOMExtensionPeriodInsuredTo;
	private TextField txtPremiumPaymentDate;
	private TextField txtAOMCapitalInsuranceYearOne;
	private TextField txtAOMCapitalInsuranceYearTwo;
	private TextField txtAOMCapitalInsuranceYearThree;
	private TextField txtAOMNetPremium;
	private TextField txtAOMStampTax;
	private TextField txtAOMVAT;
	private TextField txtAOMReceiptPremiumAmount;
	
	//Lost
	private TextField txtLostDate;
	private TextField txtLocation;
	private TextField txtCriminalCaseNo;
	private TextField txtDPRReceivedDate;
	private TextField txtDCRReceivedDate;
	private TextField txtNoOfKeys;
	private TextField txtLostCaseNo;
	private TextArea txtRemarks;
	
	//Claim Information
	private TextField txtTransactionDate;
	private TextField txtDPRNo;
	private TextField txtStaffCode;
	private TextField txtAccidentDate;
	private TextField txtTime;
	private TextField txtInsuranceType;
	private TextField txtPoliceDepartment;
	private TextField txtAccidentScene;
	private TextField txtAccidentType;
	private TextField txtAccidentDetail;
	
	//Document
	private CheckBox cbCompensationRequestForm;
	private CheckBox cbInsuranceCertificate;
	private CheckBox cbDPR;
	private CheckBox cbContract;
	private CheckBox cbOTF;
	private CheckBox cbWaivedAgreement;
	private CheckBox cbCopyofIDCard;
	private CheckBox cbCopyofHR;
	private CheckBox cbRegistrationBook;
	private CheckBox cbKey;
	
	
	private Button btnswitch;
	private ERefDataComboBox<ECivility> cbxClaimType;
	private Panel panel;
	
	/**
	 * 
	 */
	public InsurancePanel() {
		addStyleName("overflow-layout-style");
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		//lost insurance 
		txtVehicleType = ComponentFactory.getTextField("vehicle.type", false, 60, 150);
		txtInsuranceCompany = ComponentFactory.getTextField("insurance.company", false, 60, 150);
		txtInsuranceAgent = ComponentFactory.getTextField("insurance.agent", false, 60, 150);
		txtInsuranceLevel = ComponentFactory.getTextField("insurance.level", false, 60, 150);
		txtInsurancePolicyNo = ComponentFactory.getTextField("insurance.policy.no", false, 60, 150);
		txtInsurancePeriodYear = ComponentFactory.getTextField("insurance.period.year", false, 60, 150);
		txtPeriodInsuredFrom = ComponentFactory.getTextField("period.insured.from", false, 60, 150);
		txtPeriodInsuredTo = ComponentFactory.getTextField("period.insured.to", false, 60, 150);
		dfPremiumPaymentDate = ComponentFactory.getAutoDateField("premium.payment.date", false);
		txtCapitalInsuranceYearOne = ComponentFactory.getTextField("capital.insurance.year.one", false, 60, 150);
		txtCapitalInsuranceYearTwo = ComponentFactory.getTextField("capital.insurance.year.two", false, 60, 150);
		txtCapitalInsuranceYearThree = ComponentFactory.getTextField("capital.insurance.year.three", false, 60, 150);
		txtSumInsuredDeath = ComponentFactory.getTextField("sum.insured.death", false, 60, 150);
		txtNetPremium = ComponentFactory.getTextField("net.premium", false, 60, 150);
		txtStampTax = ComponentFactory.getTextField("stamp.tax", false, 60, 150);
		txtVAT = ComponentFactory.getTextField("vat", false, 60, 150);
		txtWithholdingTax = ComponentFactory.getTextField("with.holding.tax", false, 60, 150);
		txtTotalPremium = ComponentFactory.getTextField("total.premium", false, 60, 150);
		txtReceiptPremiumAmount = ComponentFactory.getTextField("receipt.premium.amount", false, 60, 150);
		
		dfCompensationReceivedDate = ComponentFactory.getAutoDateField("compensation.received.date", false);
		dfCompensationReceivedDate.addStyleName("mytextfield-caption");
		dfChequeDate = ComponentFactory.getAutoDateField("cheque.date", false);
		txtChequeNo = ComponentFactory.getTextField("cheque.no", false, 60, 150);
		txtAmount = ComponentFactory.getTextField("amount", false, 60, 150);
		txtCompensationAmount = ComponentFactory.getTextField("compensation.amount", false, 60, 150);
	
		//AOM Insurance 
		cbfirst = new CheckBox(I18N.message("first"));
		cbfirst.addValueChangeListener(this);
		txtCompanyName = ComponentFactory.getTextField("company.name", false, 60, 150);
		txtPolicyNo = ComponentFactory.getTextField("policy.no", false, 60, 150);
		txtAOMPeriodInsuredFrom = ComponentFactory.getTextField("period.insured.from", false, 60, 150);
		txtAOMPeriodInsuredTo = ComponentFactory.getTextField("period.insured.to", false, 60, 150);
		txtPremium = ComponentFactory.getTextField("premium", false, 60, 150);
		txtSumOfInsurance = ComponentFactory.getTextField("sum.of.insurance", false, 60, 150);
		
		cbExtension = new CheckBox(I18N.message("extension"));
		cbExtension.addValueChangeListener(this);
		txtAOMVehicleType = ComponentFactory.getTextField("vehicle.type", false, 60, 150);
		txtAOMInsuranceCompany = ComponentFactory.getTextField("insurance.company", false, 60, 150);
		txtAOMInsuranceAgent = ComponentFactory.getTextField("insurance.agent", false, 60, 150);
		txtAOMNo = ComponentFactory.getTextField("no", false, 60, 150);
		txtAOMInsurancePeriodYear = ComponentFactory.getTextField("insurance.period.year", false, 60, 150);
		txtAOMExtensionPeriodInsuredFrom = ComponentFactory.getTextField("period.insured.from", false, 60, 150);
		txtAOMExtensionPeriodInsuredTo = ComponentFactory.getTextField("period.insured.to", false, 60, 150);
		txtPremiumPaymentDate = ComponentFactory.getTextField("premium.payment.date", false, 60, 150);
		txtAOMCapitalInsuranceYearOne = ComponentFactory.getTextField("capital.insurance.year.one", false, 60, 150);
		txtAOMCapitalInsuranceYearTwo = ComponentFactory.getTextField("capital.insurance.year.two", false, 60, 150);
		txtAOMCapitalInsuranceYearThree = ComponentFactory.getTextField("capital.insurance.year.three", false, 60, 150);
		txtAOMNetPremium = ComponentFactory.getTextField("net.premium", false, 60, 150);
		txtAOMStampTax = ComponentFactory.getTextField("stamp.tax", false, 60, 150);
		txtAOMVAT = ComponentFactory.getTextField("vat", false, 60, 150);
		txtAOMReceiptPremiumAmount = ComponentFactory.getTextField("receipt.premium.amount", false, 60, 150);
		
		//Lost
		txtLostDate = ComponentFactory.getTextField("lost.date", false, 50, 150);
		txtLocation = ComponentFactory.getTextField("location", false, 50, 150);
		txtCriminalCaseNo = ComponentFactory.getTextField("criminal.case.no", false, 50, 150);
		txtDPRReceivedDate = ComponentFactory.getTextField("dpr.received.date", false, 50, 150);
		txtDCRReceivedDate = ComponentFactory.getTextField("dcr.received.date", false, 50, 150);
		txtNoOfKeys = ComponentFactory.getTextField("no.of.keys", false, 50, 150);
		txtLostCaseNo = ComponentFactory.getTextField("lost.case.no", false, 50, 150);
		txtRemarks = ComponentFactory.getTextArea("remarks", false, 150, 60);
		
		//Claim Information
		txtTransactionDate = ComponentFactory.getTextField("transaction.date", false, 50, 150);
		txtTransactionDate.addStyleName("mytextfield-caption");
		txtDPRNo = ComponentFactory.getTextField("dpr.no", false, 50, 150);
		txtStaffCode = ComponentFactory.getTextField("staf.code", false, 50, 150);
		txtAccidentDate = ComponentFactory.getTextField("accident.date", false, 50, 150);
		txtTime = ComponentFactory.getTextField("time", false, 50, 150);
		txtInsuranceType = ComponentFactory.getTextField("insurance.type", false, 50, 150);
		txtPoliceDepartment = ComponentFactory.getTextField("police.department", false, 50, 150);
		txtAccidentScene = ComponentFactory.getTextField("accident.scene", false, 50, 150);
		txtAccidentType = ComponentFactory.getTextField("accident.type", false, 50, 150);
		txtAccidentDetail = ComponentFactory.getTextField("accident.detail", false, 50, 150);
		
		//Document 
		cbCompensationRequestForm = new CheckBox(I18N.message("compensation.request.form"));
		cbInsuranceCertificate = new CheckBox(I18N.message("insurance.certifiacate"));
		cbDPR = new CheckBox(I18N.message("dpr"));
		cbContract = new CheckBox(I18N.message("contract"));
		cbOTF = new CheckBox(I18N.message("otf"));
		
		cbWaivedAgreement = new CheckBox(I18N.message("waived.agreement"));
		cbCopyofIDCard = new CheckBox(I18N.message("copy.of.id.card"));
		cbCopyofHR = new CheckBox(I18N.message("copy.of.hr"));
		cbRegistrationBook = new CheckBox(I18N.message("registration.book"));
		cbKey = new CheckBox(I18N.message("key"));
		
		cbxClaimType = new ERefDataComboBox<>(ECivility.class);
		btnswitch =  new NativeButton("ON");
		btnswitch.setStyleName("btn btn-success");
		btnswitch.addClickListener(new ClickListener() {
			/** */
			private static final long serialVersionUID = 5369527148441857660L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				if (btnswitch.getCaption() == "ON") {
					btnswitch.setCaption("OFF");
					btnswitch.setStyleName("btn btn-danger");
					panel.setVisible(false);
				} else {
					btnswitch.setCaption("ON");
					btnswitch.setStyleName("btn btn-success");
					panel.setVisible(true);
				}
				
			}
		});
		
		this.enable(false);
		
		VerticalLayout insuranceLeft = new VerticalLayout();
		insuranceLeft.setSpacing(true);
		insuranceLeft.addComponent(lostInsurancePanel());
		insuranceLeft.addComponent(switchButton());
		insuranceLeft.addComponent(lostAndClaimInformation());
		Panel insuranceLeftPanel = new Panel();
		insuranceLeftPanel.setContent(insuranceLeft);
		
		VerticalLayout insuranceRigth = new VerticalLayout();
		insuranceRigth.setSpacing(true);
		insuranceRigth.addComponent(aomInsurancePanel());
		
		HorizontalLayout insuranceLayout = new HorizontalLayout();
		insuranceLayout.addComponent(insuranceLeftPanel);
		insuranceLayout.addComponent(insuranceRigth);
		insuranceLayout.setExpandRatio(insuranceLeftPanel, 1);
		
		return insuranceLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private Panel lostInsurancePanel() {
		Panel lostInsurancePanel = ComponentFactory.getPanel("lost.insurace");
		FormLayout lostInsuranceFormLeft = getFormLayout();
		lostInsuranceFormLeft.addComponent(txtVehicleType);
		lostInsuranceFormLeft.addComponent(txtInsuranceCompany);
		lostInsuranceFormLeft.addComponent(txtInsuranceAgent);
		lostInsuranceFormLeft.addComponent(txtInsuranceLevel);
		lostInsuranceFormLeft.addComponent(txtInsurancePolicyNo);
		lostInsuranceFormLeft.addComponent(txtInsurancePeriodYear);
		lostInsuranceFormLeft.addComponent(txtPeriodInsuredFrom);
		lostInsuranceFormLeft.addComponent(txtPeriodInsuredTo);
		lostInsuranceFormLeft.addComponent(dfPremiumPaymentDate);
		lostInsuranceFormLeft.addComponent(txtCapitalInsuranceYearOne);
		lostInsuranceFormLeft.addComponent(txtCapitalInsuranceYearTwo);
		lostInsuranceFormLeft.addComponent(txtCapitalInsuranceYearThree);
		lostInsuranceFormLeft.addComponent(txtSumInsuredDeath);
		lostInsuranceFormLeft.addComponent(txtNetPremium);
		lostInsuranceFormLeft.addComponent(txtStampTax);
		lostInsuranceFormLeft.addComponent(txtVAT);
		lostInsuranceFormLeft.addComponent(txtWithholdingTax);
		lostInsuranceFormLeft.addComponent(txtTotalPremium);
		lostInsuranceFormLeft.addComponent(txtReceiptPremiumAmount);
		
		FormLayout lostInsuranceFormRight = getFormLayout();
		lostInsuranceFormRight.addComponent(dfCompensationReceivedDate);
		lostInsuranceFormRight.addComponent(dfChequeDate);
		lostInsuranceFormRight.addComponent(txtChequeNo);
		lostInsuranceFormRight.addComponent(txtAmount);
		lostInsuranceFormRight.addComponent(txtCompensationAmount);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout(lostInsuranceFormLeft, ComponentFactory.getSpaceLayout(15, Unit.PIXELS), lostInsuranceFormRight);
		lostInsurancePanel.setContent(horizontalLayout);
		lostInsurancePanel.setStyleName(Reindeer.PANEL_LIGHT);
		
		return lostInsurancePanel;
	}
	
	/**
	 * 
	 * @return
	 */
	private Panel aomInsurancePanel() {
		Panel aomInsurancePanel = ComponentFactory.getPanel("aom.insurance");
		FormLayout aomInsuranceForm = getFormLayout();
		aomInsuranceForm.addComponent(cbfirst);
		aomInsuranceForm.addComponent(txtCompanyName);
		aomInsuranceForm.addComponent(txtPolicyNo);
		aomInsuranceForm.addComponent(txtAOMPeriodInsuredFrom);
		aomInsuranceForm.addComponent(txtAOMPeriodInsuredTo);
		aomInsuranceForm.addComponent(txtPremium);
		aomInsuranceForm.addComponent(txtSumOfInsurance);
		
		aomInsuranceForm.addComponent(cbExtension);
		aomInsuranceForm.addComponent(txtAOMVehicleType);
		aomInsuranceForm.addComponent(txtAOMInsuranceCompany);
		aomInsuranceForm.addComponent(txtAOMInsuranceAgent);
		aomInsuranceForm.addComponent(txtAOMNo);
		aomInsuranceForm.addComponent(txtAOMInsurancePeriodYear);
		aomInsuranceForm.addComponent(txtAOMExtensionPeriodInsuredFrom);
		aomInsuranceForm.addComponent(txtAOMExtensionPeriodInsuredTo);
		aomInsuranceForm.addComponent(txtPremiumPaymentDate);
		aomInsuranceForm.addComponent(txtAOMCapitalInsuranceYearOne);
		aomInsuranceForm.addComponent(txtAOMCapitalInsuranceYearTwo);
		aomInsuranceForm.addComponent(txtAOMCapitalInsuranceYearThree);
		aomInsuranceForm.addComponent(txtAOMNetPremium);
		aomInsuranceForm.addComponent(txtAOMStampTax);
		aomInsuranceForm.addComponent(txtAOMVAT);
		aomInsuranceForm.addComponent(txtAOMReceiptPremiumAmount);
		
		aomInsurancePanel.setContent(aomInsuranceForm);
		aomInsurancePanel.setStyleName(Reindeer.PANEL_LIGHT);
		
		return aomInsurancePanel;
	}
	
	/**
	 * 
	 * @return
	 */
	private Panel lostAndClaimInformation() {
		Panel lostPanel = new Panel(I18N.message("lost"));
		lostPanel.setStyleName(Reindeer.PANEL_LIGHT);
		FormLayout lostForm = getFormLayout();
		lostForm.addComponent(txtLostDate);
		lostForm.addComponent(txtLocation);
		lostForm.addComponent(txtCriminalCaseNo);
		lostForm.addComponent(txtDPRReceivedDate);
		lostForm.addComponent(txtDCRReceivedDate);
		lostForm.addComponent(txtNoOfKeys);
		lostForm.addComponent(txtLostCaseNo);
		lostForm.addComponent(txtRemarks);
		lostPanel.setContent(lostForm);
		
		Panel claimInformationPanel = new Panel(I18N.message("claim.information"));
		claimInformationPanel.setStyleName(Reindeer.PANEL_LIGHT);
		FormLayout claimInformationForm = getFormLayout();
		claimInformationForm.addComponent(txtTransactionDate);
		claimInformationForm.addComponent(txtDPRNo);
		claimInformationForm.addComponent(txtStaffCode);
		claimInformationForm.addComponent(txtAccidentDate);
		claimInformationForm.addComponent(txtTime);
		claimInformationForm.addComponent(txtInsuranceType);
		claimInformationForm.addComponent(txtPoliceDepartment);
		claimInformationForm.addComponent(txtAccidentScene);
		claimInformationForm.addComponent(txtAccidentType);
		claimInformationForm.addComponent(txtAccidentDetail);
		claimInformationPanel.setContent(claimInformationForm);
		
		VerticalLayout documentLeft = new VerticalLayout();
		documentLeft.addComponent(cbCompensationRequestForm);
		documentLeft.addComponent(cbInsuranceCertificate);
		documentLeft.addComponent(cbDPR);
		documentLeft.addComponent(cbContract);
		documentLeft.addComponent(cbOTF);
		
		VerticalLayout documentRight = new VerticalLayout();
		documentRight.addComponent(cbWaivedAgreement);
		documentRight.addComponent(cbCopyofIDCard);
		documentRight.addComponent(cbCopyofHR);
		documentRight.addComponent(cbRegistrationBook);
		documentRight.addComponent(cbKey);
		
		HorizontalLayout document = new HorizontalLayout();
		document.addComponent(documentLeft);
		document.addComponent(documentRight);
		
		Panel docuementPanel = new Panel(I18N.message("document"));
		docuementPanel.setStyleName(Reindeer.PANEL_LIGHT);
		docuementPanel.setContent(document);
		
		VerticalLayout verticalLayoutLeft = new VerticalLayout();
		verticalLayoutLeft.addComponent(lostPanel);
		verticalLayoutLeft.addComponent(docuementPanel);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.addComponent(verticalLayoutLeft);
		horizontalLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS));
		horizontalLayout.addComponent(claimInformationPanel);
		
		panel = new Panel();
		panel.setContent(horizontalLayout);
		panel.setStyleName(Reindeer.PANEL_LIGHT);
		
		return panel;
	}
	
	/**
	 * 
	 * @param enable
	 */
	private void enable(boolean enable) {
		txtVehicleType.setEnabled(enable);
		txtInsuranceCompany.setEnabled(enable);
		txtInsuranceAgent.setEnabled(enable);
		txtInsuranceLevel.setEnabled(enable);
		txtInsurancePolicyNo.setEnabled(enable);
		txtInsurancePeriodYear.setEnabled(enable);
		txtPeriodInsuredFrom.setEnabled(enable);
		txtPeriodInsuredTo.setEnabled(enable);
		dfPremiumPaymentDate.setEnabled(enable);
		txtCapitalInsuranceYearOne.setEnabled(enable);
		txtCapitalInsuranceYearTwo.setEnabled(enable);
		txtCapitalInsuranceYearThree.setEnabled(enable);
		txtSumInsuredDeath.setEnabled(enable);
		txtNetPremium.setEnabled(enable);
		txtStampTax.setEnabled(enable);
		txtVAT.setEnabled(enable);
		txtWithholdingTax.setEnabled(enable);
		txtTotalPremium.setEnabled(enable);
		txtReceiptPremiumAmount.setEnabled(enable);
		
		dfCompensationReceivedDate.setEnabled(enable);
		dfChequeDate.setEnabled(enable);
		txtChequeNo.setEnabled(enable);
		txtAmount.setEnabled(enable);
		txtCompensationAmount.setEnabled(enable);
		
		cbfirst.setEnabled(enable);
		txtCompanyName.setEnabled(enable);
		txtPolicyNo.setEnabled(enable);
		txtAOMPeriodInsuredFrom.setEnabled(enable);
		txtAOMPeriodInsuredTo.setEnabled(enable);
		txtPremium.setEnabled(enable);
		txtSumOfInsurance.setEnabled(enable);
		
		cbExtension.setEnabled(enable);
		txtSumOfInsurance.setEnabled(enable);
		txtAOMVehicleType.setEnabled(enable);
		txtAOMInsuranceCompany.setEnabled(enable);
		txtAOMInsuranceAgent.setEnabled(enable);
		txtAOMNo.setEnabled(enable);
		txtAOMInsurancePeriodYear.setEnabled(enable);
		txtAOMExtensionPeriodInsuredFrom.setEnabled(enable);
		txtAOMExtensionPeriodInsuredTo.setEnabled(enable);
		txtPremiumPaymentDate.setEnabled(enable);
		txtAOMCapitalInsuranceYearOne.setEnabled(enable);
		txtAOMCapitalInsuranceYearTwo.setEnabled(enable);
		txtAOMCapitalInsuranceYearThree.setEnabled(enable);
		txtAOMNetPremium.setEnabled(enable);
		txtAOMStampTax.setEnabled(enable);
		txtAOMVAT.setEnabled(enable);
		txtAOMReceiptPremiumAmount.setEnabled(enable);
		
		txtLostDate.setEnabled(enable);
		txtLocation.setEnabled(enable);
		txtCriminalCaseNo.setEnabled(enable);
		txtDPRReceivedDate.setEnabled(enable);
		txtDCRReceivedDate.setEnabled(enable);
		txtNoOfKeys.setEnabled(enable);
		txtLostCaseNo.setEnabled(enable);
		txtRemarks.setEnabled(enable);
		
		txtTransactionDate.setEnabled(enable);
		txtDPRNo.setEnabled(enable);
		txtStaffCode.setEnabled(enable);
		txtAccidentDate.setEnabled(enable);
		txtTime.setEnabled(enable);
		txtInsuranceType.setEnabled(enable);
		txtPoliceDepartment.setEnabled(enable);
		txtAccidentScene.setEnabled(enable);
		txtAccidentType.setEnabled(enable);
		txtAccidentDetail.setEnabled(enable);
		
		cbCompensationRequestForm.setEnabled(enable);
		cbInsuranceCertificate.setEnabled(enable);
		cbDPR.setEnabled(enable);
		cbContract.setEnabled(enable);
		cbOTF.setEnabled(enable);
		cbWaivedAgreement.setEnabled(enable);
		cbCopyofIDCard.setEnabled(enable);
		cbCopyofHR.setEnabled(enable);
		cbRegistrationBook.setEnabled(enable);
		cbKey.setEnabled(enable);
	}
	
	/**
	 * 
	 * @return
	 */
	private FormLayout getFormLayout() {
		FormLayout formLayout = new FormLayout();
		formLayout.setStyleName("myform-align-left");
		formLayout.setMargin(true);
		formLayout.setSpacing(false);
		return formLayout;
	}
	
	/**
	 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
	 */
	@Override
	public void valueChange(ValueChangeEvent event) {
		if (event.getProperty() == cbfirst) {
			if (cbfirst.getValue()) {
				cbExtension.setValue(false);
			}
		} else if (event.getProperty() == cbExtension) {
			if (cbExtension.getValue()) {
				cbfirst.setValue(false);
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public CustomLayout switchButton() {
		String OPEN_TABLE = "<table width=\"100%\" height=\"100%\" cellspacing=\"0\" cellpadding=\"5\""+"\">";
	   	String OPEN_TR = "<tr>";
		String OPEN_TD = "<td class=\"align-left\">";
		String CLOSE_TD = "</td>";
		String CLOSE_TR = "</tr>";
		String CLOSE_TABLE = "</table>";
		String switchButtonTemplate = "";
		
		CustomLayout switchButtonLayout = new CustomLayout("xxx");
		switchButtonTemplate = OPEN_TABLE;
		switchButtonTemplate += OPEN_TR;
		switchButtonTemplate += OPEN_TD;
		switchButtonTemplate += "<div location =\"lblswitchButton\" />";
		switchButtonTemplate += CLOSE_TD;
		switchButtonTemplate += OPEN_TD;
		switchButtonTemplate += "<div location =\"btnSwitchButton\" />";
		switchButtonTemplate += CLOSE_TD;
		switchButtonTemplate += OPEN_TD;
		switchButtonTemplate += "<div location =\"lblClaimType\" />";
		switchButtonTemplate += CLOSE_TD;
		switchButtonTemplate += OPEN_TD;
		switchButtonTemplate += "<div location =\"cbxClaimType\" />";
		switchButtonTemplate += CLOSE_TD;
		switchButtonTemplate += CLOSE_TR;
		
		switchButtonLayout.addComponent(new Label(I18N.message("to.bo.claimed")), "lblswitchButton");
		switchButtonLayout.addComponent(btnswitch, "btnSwitchButton");
		switchButtonLayout.addComponent(new Label(I18N.message("claim.type")), "lblClaimType");
		switchButtonLayout.addComponent(cbxClaimType, "cbxClaimType");
		switchButtonTemplate += CLOSE_TABLE;
		switchButtonLayout.setTemplateContents(switchButtonTemplate);
		return switchButtonLayout;
	}
	
	/**
	 * Assing Value to Insurance Panel 
	 * @param contract
	 */
//	public void assignValue(Contract contract) {
//		if (contract != null) {
//			try {
//				Response response = ClientInsurance.getInsurance(contract.getId());
//
//				logger.info("Response Status: " + response.getStatus());
//				if (response.getStatus() == Status.OK.getStatusCode()) {
//					InsuranceDTO insuranceDTO = response.readEntity(InsuranceDTO.class);
//					txtPolicyNo.setValue(getDefaultString(insuranceDTO.getPolicy()));
//				} else {
//					String errMsg = response.readEntity(String.class);
//					logger.error("Error: " + errMsg);
//				}
//			} catch (Exception e) {
//				logger.error(e.getMessage(), e);
//			}
//			
//		}
//	}
}
