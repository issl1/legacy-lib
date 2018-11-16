package com.nokor.efinance.core.applicant.panel.contact.dealer;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Full detail dealer in contact tab
 * @author uhout.cheng
 */
public class DealerPanel extends AbstractTabPanel {

	/** */
	private static final long serialVersionUID = -7898485050087515488L;

	private static String OPEN_TABLE = "<table cellspacing=\"1\" cellpadding=\"1\" style=\"border:1px solid black; border-collapse:collapse;\" >";
	private static String OPEN_TR = "<tr>";
	private static String OPEN_TH = "<th class=\"align-center\" width=\"120px\" bgcolor=\"#e1e1e1\" "
			+ "style=\"border:1px solid black;\" >";
	private static String OPEN_TD = "<td class=\"align-center\" style=\"border:1px solid black;\" >";
	private static String CLOSE_TR = "</tr>";
	private static String CLOSE_TH = "</th>";
	private static String CLOSE_TD = "</td>";
	private static String CLOSE_TABLE = "</table>";
	
	private TextField txtReference;
	private TextField txtName;
	private TextField txtRefMotherCompany;
	private TextField txtNameMotherCompany;
	private TextField txtCommercialRegNo;
	private TextField txtServiceFee;
	private TextField txtAddress;
	private AutoDateField dfApprovalDate;
	private TextField txtManagerName;
	private TextField txtManagerAddress;
	private TextField txtPhoneNumber;
	private TextField txtMailAddress;
	
	private TextField txtMTBNamePayee;
	private TextField txtMTBIban;
	private TextField txtMTBSwift;
	private TextField txtMTBBankName;
	private TextField txtMTBBranch;
	private TextField txtMTBAddress;
	private TextField txtCMSNamePayee;
	private TextField txtCMSIban;
	private TextField txtCMSSwift;
	private TextField txtCMSBankName;
	private TextField txtCMSBranch;
	private TextField txtCMSAddress;
	private Button btnBack;
	
	/**
	 * @return the btnBack
	 */
	public Button getBtnBack() {
		return btnBack;
	}

	/**
	 * @param btnBack the btnBack to set
	 */
	public void setBtnBack(Button btnBack) {
		this.btnBack = btnBack;
	}

	/** */
	public DealerPanel() {
		super.setMargin(false);
		setMargin(false);
		btnBack = new NativeButton(I18N.message("back"));
		btnBack.setIcon(FontAwesome.STEP_BACKWARD);
		setBtnBack(btnBack);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.setSizeFull();
		navigationPanel.addButton(btnBack);
		addComponent(navigationPanel, 0);
	}
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private TextField getTextField(String caption) {
		TextField textField = ComponentFactory.getTextField(caption, false, 60, 200);
		return textField;
	}
	
	/** */
	private void initControls() {
		txtReference = getTextField("reference");
		txtName = getTextField("name.en");
		txtRefMotherCompany = getTextField("reference.mother.company");
		txtNameMotherCompany = getTextField("name.mother.company");
		txtCommercialRegNo = getTextField("commercial.registration.no");
		txtServiceFee = getTextField("service.fee");
		txtAddress = getTextField("address");
		dfApprovalDate = ComponentFactory.getAutoDateField("approval.date", false);
		txtManagerName = getTextField("manager.name");
		txtManagerAddress = getTextField("manager.address");
		txtPhoneNumber = getTextField("phone.number");
		txtMailAddress = getTextField("mail.address");
		txtMTBNamePayee = getTextField("name.payee.mtb");
		txtMTBIban = getTextField("iban.mtb");
		txtMTBSwift = getTextField("swift.mtb");
		txtMTBBankName = getTextField("bank.name.mtb");
		txtMTBBranch = getTextField("branch.mtb");
		txtMTBAddress = getTextField("address.mtb");
		txtCMSNamePayee = getTextField("name.payee.cms");
		txtCMSIban = getTextField("iban.cms");
		txtCMSSwift = getTextField("swift.cms");
		txtCMSBankName = getTextField("bank.name.cms");
		txtCMSBranch = getTextField("branch.cms");
		txtCMSAddress = getTextField("address.cms");
		txtRefMotherCompany.setStyleName("mytextfield-caption");
		txtCommercialRegNo.setStyleName("mytextfield-caption");
	}
	
	/**
	 * 
	 * @return
	 */
	private FormLayout getFormLayout() {
		FormLayout layout = new FormLayout();
		layout.setStyleName("myform-align-left");
		layout.setSpacing(false);
		return layout;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		initControls();
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		
		FormLayout formLayout = getFormLayout();
		formLayout.addComponent(txtReference);
		formLayout.addComponent(txtName);
		formLayout.addComponent(txtRefMotherCompany);
		formLayout.addComponent(txtNameMotherCompany);
		formLayout.addComponent(txtCommercialRegNo);
		formLayout.addComponent(txtServiceFee);
		formLayout.addComponent(txtAddress);
		formLayout.addComponent(dfApprovalDate);
		formLayout.addComponent(txtManagerName);
		formLayout.addComponent(txtManagerAddress);
		formLayout.addComponent(txtPhoneNumber);
		formLayout.addComponent(txtMailAddress);
		horizontalLayout.addComponent(formLayout);
		horizontalLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS));
		
		formLayout = getFormLayout();
		formLayout.addComponent(txtMTBNamePayee);
		formLayout.addComponent(txtMTBIban);
		formLayout.addComponent(txtMTBSwift);
		formLayout.addComponent(txtMTBBankName);
		formLayout.addComponent(txtMTBBranch);
		formLayout.addComponent(txtMTBAddress);
		formLayout.addComponent(txtCMSNamePayee);
		formLayout.addComponent(txtCMSIban);
		formLayout.addComponent(txtCMSSwift);
		formLayout.addComponent(txtCMSBankName);
		formLayout.addComponent(txtCMSBranch);
		formLayout.addComponent(txtCMSAddress);
		horizontalLayout.addComponent(formLayout);
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		mainLayout.addComponent(getCampaignTableLayout());
		mainLayout.addComponent(getAssetTableLayout());
		mainLayout.addComponent(horizontalLayout);
		
		TabSheet tabDealer = new TabSheet();
		tabDealer.addTab(mainLayout, I18N.message("dealer.shop"));
		
		return tabDealer;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		if (contract != null) {
			Dealer dealer = contract.getDealer();
			txtReference.setValue(getDefaultString(dealer.getCode()));
			txtName.setValue(getDefaultString(dealer.getNameEn()));
			txtRefMotherCompany.setValue("");
			txtNameMotherCompany.setValue("");
			txtCommercialRegNo.setValue("");
			txtServiceFee.setValue("");
			txtAddress.setValue("");
			dfApprovalDate.setValue(contract.getApprovalDate());
			txtManagerName.setValue("");
			txtManagerAddress.setValue("");
			txtPhoneNumber.setValue("");
			txtMailAddress.setValue("");
			txtMTBNamePayee.setValue("");
			txtMTBIban.setValue("");
			txtMTBSwift.setValue("");
			txtMTBBankName.setValue("");
			txtMTBBranch.setValue("");
			txtMTBAddress.setValue("");
			txtCMSNamePayee.setValue("");
			txtCMSIban.setValue("");
			txtCMSSwift.setValue("");
			txtCMSBankName.setValue("");
			txtCMSBranch.setValue("");
			txtCMSAddress.setValue("");
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private HorizontalLayout getCampaignTableLayout(){
		CustomLayout cusLayout = new CustomLayout("xxx");
		String template = OPEN_TABLE;
		template += OPEN_TR;
		template += OPEN_TH;
		template += "<div location =\"lblCampaignCode\" />";
		template += CLOSE_TH;
		template += OPEN_TH;
		template += "<div location =\"lblCampaignName\" />";
		template += CLOSE_TH;
		template += OPEN_TH;
		template += "<div location =\"lblCampaignStatus\" />";
		template += CLOSE_TH;
		template += CLOSE_TR;
		template += OPEN_TR;
		template += OPEN_TD;
		template += "<div location =\"lblCampaignCodeValue\" />";
		template += CLOSE_TD;
		template += OPEN_TD;
		template += "<div location =\"lblCampaignNameValue\" />";
		template += CLOSE_TD;
		template += OPEN_TD;
		template += "<div location =\"lblCampaignStatusValue\" />";
		template += CLOSE_TD;
		template += CLOSE_TR;
		
		Label lblCampaignCode = new Label();
		Label lblCampaignName = new Label();
		Label lblCampaignStatus = new Label();
		cusLayout.addComponent(new Label(I18N.message("campaign.code")), "lblCampaignCode");
		cusLayout.addComponent(new Label(I18N.message("campaign.name")), "lblCampaignName");
		cusLayout.addComponent(new Label(I18N.message("campaign.status")), "lblCampaignStatus");
		cusLayout.addComponent(lblCampaignCode, "lblCampaignCodeValue");
		cusLayout.addComponent(lblCampaignName, "lblCampaignNameValue");
		cusLayout.addComponent(lblCampaignStatus, "lblCampaignStatusValue");
		
		template += CLOSE_TABLE;
		cusLayout.setTemplateContents(template);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.addComponent(cusLayout);
		return horizontalLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private HorizontalLayout getAssetTableLayout(){
		CustomLayout cusLayout = new CustomLayout("xxx");
		String template = OPEN_TABLE;
		template += OPEN_TR;
		template += OPEN_TH;
		template += "<div location =\"lblBranch\" />";
		template += CLOSE_TH;
		template += OPEN_TH;
		template += "<div location =\"lblModel\" />";
		template += CLOSE_TH;
		template += OPEN_TH;
		template += "<div location =\"lblSeries\" />";
		template += CLOSE_TH;
		template += OPEN_TH;
		template += "<div location =\"lblColor\" />";
		template += CLOSE_TH;
		template += CLOSE_TR;
		template += OPEN_TR;
		template += OPEN_TD;
		template += "<div location =\"lblBranchValue\" />";
		template += CLOSE_TD;
		template += OPEN_TD;
		template += "<div location =\"lblModelValue\" />";
		template += CLOSE_TD;
		template += OPEN_TD;
		template += "<div location =\"lblSeriesvalue\" />";
		template += CLOSE_TD;
		template += OPEN_TD;
		template += "<div location =\"lblColorValue\" />";
		template += CLOSE_TD;
		template += CLOSE_TR;
		
		Label lblBranch = new Label();
		Label lblModel = new Label();
		Label lblSeries = new Label();
		Label lblColor = new Label();
		cusLayout.addComponent(new Label(I18N.message("branch")), "lblBranch");
		cusLayout.addComponent(new Label(I18N.message("model")), "lblModel");
		cusLayout.addComponent(new Label(I18N.message("series")), "lblSeries");
		cusLayout.addComponent(new Label(I18N.message("color")), "lblColor");
		cusLayout.addComponent(lblBranch, "lblBranchValue");
		cusLayout.addComponent(lblModel, "lblModelValue");
		cusLayout.addComponent(lblSeries, "lblSeriesValue");
		cusLayout.addComponent(lblColor, "lblColorValue");
		
		template += CLOSE_TABLE;
		cusLayout.setTemplateContents(template);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.addComponent(cusLayout);
		return horizontalLayout;
	}
	
	/**
	 * Reset controls
	 */
	public void reset() {
		txtReference.setValue("");
		txtName.setValue("");
		txtRefMotherCompany.setValue("");
		txtNameMotherCompany.setValue("");
		txtCommercialRegNo.setValue("");
		txtServiceFee.setValue("");
		txtAddress.setValue("");
		dfApprovalDate.setValue(null);
		txtManagerName.setValue("");
		txtManagerAddress.setValue("");
		txtPhoneNumber.setValue("");
		txtMailAddress.setValue("");
		txtMTBNamePayee.setValue("");
		txtMTBIban.setValue("");
		txtMTBSwift.setValue("");
		txtMTBBankName.setValue("");
		txtMTBBranch.setValue("");
		txtMTBAddress.setValue("");
		txtCMSNamePayee.setValue("");
		txtCMSIban.setValue("");
		txtCMSSwift.setValue("");
		txtCMSBankName.setValue("");
		txtCMSBranch.setValue("");
		txtCMSAddress.setValue("");
	}
	
	/**
	 * @return
	 */
	public boolean isValid() {		
		return errors.isEmpty();
	}
}
