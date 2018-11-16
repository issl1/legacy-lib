package com.nokor.efinance.core.applicant.panel.contact;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.model.Employment;
import com.nokor.efinance.core.applicant.model.IndividualAddress;
import com.nokor.efinance.core.applicant.model.IndividualContactInfo;
import com.nokor.efinance.core.applicant.panel.AddressInfoPanel;
import com.nokor.efinance.core.applicant.service.ApplicantService;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractApplicant;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.organization.ContactInfo;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Full detail LESSEE, SPOUSE, GUARANTOR in contact tab
 * @author uhout.cheng
 */
public class XXXContactApplicantPanel extends AbstractTabPanel implements ClickListener, FrmkServicesHelper {

	/** */
	private static final long serialVersionUID = 6930983868488765132L;

	private PersonalInformationPanel personalInfomationPanel;
	private FinancialIncomeAddressPanelOld financialIncomeAddressPanel;
	private AddressInfoPanel addressInfoPanel;
	private ContactInfoPanel contactInfoPanel;
	
	private List<AddressInfoPanel> addressInfoPanels;
	private List<ContactInfoPanel> contactInfoPanels;
	private List<FinancialIncomeAddressPanelOld> financialIncomeAddressPanels;
	
	private Button btnAddAddress;
	private Button btnCancelAddAddressInfo;
	private Button btnAddContactInfo;
	private Button btnCancelAddContactInfo;
	
	private Button btnAddFinancialInfo;
	private Button btnCancelAddFinancialInfo;
	
	private VerticalLayout addressLayout;
	private VerticalLayout newAddressLayout;
	private VerticalLayout contactInfoLayout;
	private VerticalLayout newContactInfoLayout;
	
	private VerticalLayout financialInfoLayout;
	private VerticalLayout newFinancialInfoLayout;
	private VerticalLayout netIncomeLayout;
	
	private TextField txtNetIncome;
	private TextField txtHouseholdExpenses;
	private TextField txtHouseholdIncome;
	
	private Button btnDeleteContactInfo;
	private Button btnDeleteAddressInfo;
	private Button btnDeleteFinancialInfo;
	private Applicant applicant;
	private List<IndividualContactInfo> individualContactInfos;
	private List<IndividualAddress> individualAddresses;
	private List<Employment> employments;
	
	private Panel mainPanel;
	private Button btnSave;
	private Button btnBack;
	private VerticalLayout messagePanel;
	private TabSheet tabSheet;
	
	private Label lblLesseeContractID;
	private Label lblLesseeStatus;
	private Label lblLesseeType;
	private Label lblGuarantorContractID;
	private Label lblGuarantorStatus;
	private Label lblGuarantorType;

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
	public XXXContactApplicantPanel() {
		super();
		btnSave = new NativeButton(I18N.message("save"));
		btnSave.setIcon(FontAwesome.SAVE);
		btnSave.addClickListener(this);
		
		btnBack = new NativeButton(I18N.message("back"));
		btnBack.setIcon(FontAwesome.STEP_BACKWARD);
		setBtnBack(btnBack);
		
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.setSizeUndefined();
		messagePanel.addStyleName("message");
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.setSizeFull();
		navigationPanel.addButton(btnBack);
		navigationPanel.addButton(btnSave);
		addComponent(navigationPanel, 0);
		addComponent(messagePanel, 1);
	}	
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private Label getLabel(String caption) {
		Label label = new Label();
		label.setValue("<h2 style=\"border:1px solid #E3E3E3;padding:9px;border-radius:3px;"
				+ "background-color:#F5F5F5;\" align=\"center\" >" + I18N.message(caption) + "</h2>");
		label.setContentMode(ContentMode.HTML);
		label.addStyleName("label-width");
		return label;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {	
		lblLesseeContractID = new Label();
		lblLesseeStatus = new Label();
		lblLesseeType = new Label();
		lblGuarantorContractID = new Label();
		lblGuarantorStatus = new Label();
		lblGuarantorType = new Label();
		
		personalInfomationPanel = new PersonalInformationPanel();
		financialIncomeAddressPanel = new FinancialIncomeAddressPanelOld(null);
		addressInfoPanel = new AddressInfoPanel();
		contactInfoPanel = new ContactInfoPanel();
		
		addressInfoPanels = new ArrayList<>();
		contactInfoPanels = new ArrayList<>();
		financialIncomeAddressPanels = new ArrayList<>();
		
		btnAddAddress = new Button(I18N.message("add.address"));
		btnAddAddress.setWidth("350px");
		btnAddAddress.addClickListener(this);
		
		btnAddContactInfo = new Button(I18N.message("add.contact"));
		btnAddContactInfo.setWidth("450px");
		btnAddContactInfo.addClickListener(this);
		
		btnAddFinancialInfo = new Button(I18N.message("add.income"));
		btnAddFinancialInfo.setWidth("350px");
		btnAddFinancialInfo.addClickListener(this);
		
		addressLayout = new VerticalLayout();
		newAddressLayout = new VerticalLayout();
		addressLayout.addComponent(addressInfoPanel);
		
		contactInfoLayout = new VerticalLayout();
		newContactInfoLayout = new VerticalLayout();
		contactInfoLayout.addComponent(contactInfoPanel);
		
		financialInfoLayout = new VerticalLayout();
		newFinancialInfoLayout = new VerticalLayout();
		financialInfoLayout.addComponent(financialIncomeAddressPanel);
		
		txtNetIncome = ComponentFactory.getTextField("net.income", false, 20, 190);
		txtHouseholdIncome = ComponentFactory.getTextField("household.income", false, 20, 190);
		txtHouseholdExpenses = ComponentFactory.getTextField("household.expenses", false, 20, 190);
		
		netIncomeLayout = new VerticalLayout(); 
		netIncomeLayout.addComponent(new FormLayout(txtNetIncome));
		netIncomeLayout.addComponent(new FormLayout(txtHouseholdExpenses));
		netIncomeLayout.addComponent(new FormLayout(txtHouseholdIncome));
		
		Label lblFinanceTitle = getLabel("finance");
		VerticalLayout financialInfoWithButtonAdd = new VerticalLayout();
		financialInfoWithButtonAdd.addComponent(financialInfoLayout);
		financialInfoWithButtonAdd.addComponent(btnAddFinancialInfo);
		financialInfoWithButtonAdd.addComponent(netIncomeLayout);
		
		VerticalLayout adrLayout = new VerticalLayout();
		adrLayout.addComponent(addressLayout);
		adrLayout.addComponent(btnAddAddress);
		
		VerticalLayout conLayout = new VerticalLayout();
		conLayout.addComponent(contactInfoLayout);
		conLayout.addComponent(btnAddContactInfo);
		
		Label lblContactTitle = getLabel("contact");
		HorizontalLayout contactAddressLayout = new HorizontalLayout();
		contactAddressLayout.addComponent(adrLayout);
		contactAddressLayout.addComponent(conLayout);
		
		Label lblPersonalTitle = getLabel("personal");
		VerticalLayout personalLayout = new VerticalLayout();
		personalLayout.addComponent(personalInfomationPanel);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		verticalLayout.setSpacing(true);
		verticalLayout.addComponent(getTopTableInfo());
		verticalLayout.addComponent(lblContactTitle);
		verticalLayout.addComponent(contactAddressLayout);
		verticalLayout.addComponent(lblFinanceTitle);
		verticalLayout.addComponent(financialInfoWithButtonAdd);
		verticalLayout.addComponent(lblPersonalTitle);
		verticalLayout.addComponent(personalLayout);
		
		HorizontalLayout personeAndFinancialLayout = new HorizontalLayout();
		personeAndFinancialLayout.addComponent(verticalLayout);
		
		mainPanel = new Panel();
		mainPanel.setHeight(750, Unit.PIXELS);
		mainPanel.setContent(personeAndFinancialLayout);
		
		tabSheet = new TabSheet();
		
        return tabSheet;
    }

	/**
	 * display success 
	 */
	private void displaySuccessMsg() {
		Label messageLabel = new Label(I18N.message("msg.info.save.successfully"));
		messageLabel.addStyleName("success");
		Label iconLabel = new Label();
		iconLabel.setIcon(new ThemeResource("../nkr-default/icons/16/twitter.png"));
		iconLabel.addStyleName("success-icon");
		messagePanel.removeAllComponents();
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSpacing(true);
		layout.addComponent(iconLabel);
		layout.addComponent(messageLabel);
		messagePanel.addComponent(layout);
		messagePanel.setVisible(true);
	}
	
	/**
	 * 
	 * @return
	 */
	private HorizontalLayout getTopTableInfo(){
		String OPEN_TABLE = "<table cellspacing=\"3\" cellpadding=\"3\" style=\"border:1px solid black; border-collapse:collapse;\" >";
		String OPEN_TR = "<tr>";
		String OPEN_TH = "<th class=\"align-center\" width=\"120px\" bgcolor=\"#e1e1e1\" "
				+ "style=\"border:1px solid black;\" >";
		String OPEN_TD = "<td class=\"align-center\" style=\"border:1px solid black;\" >";
		String CLOSE_TR = "</tr>";
		String CLOSE_TH = "</th>";
		String CLOSE_TD = "</td>";
		String CLOSE_TABLE = "</table>";
		
		CustomLayout cusLayout = new CustomLayout("xxx");
		String template = OPEN_TABLE;
		template += OPEN_TR;
		template += OPEN_TH;
		template += "<div location =\"lblContractID\" />";
		template += CLOSE_TH;
		template += OPEN_TH;
		template += "<div location =\"lblStatus\" />";
		template += CLOSE_TH;
		template += OPEN_TH;
		template += "<div location =\"lblType\" />";
		template += CLOSE_TH;
		template += CLOSE_TR;
		template += OPEN_TR;
		template += OPEN_TD;
		template += "<div location =\"lblLesseeContractIDValue\" />";
		template += CLOSE_TD;
		template += OPEN_TD;
		template += "<div location =\"lblLesseeStatusValue\" />";
		template += CLOSE_TD;
		template += OPEN_TD;
		template += "<div location =\"lblLesseeTypeValue\" />";
		template += CLOSE_TD;
		template += CLOSE_TR;
		template += OPEN_TR;
		template += OPEN_TD;
		template += "<div location =\"lblGuarantorContractIDValue\" />";
		template += CLOSE_TD;
		template += OPEN_TD;
		template += "<div location =\"lblGuarantorStatusValue\" />";
		template += CLOSE_TD;
		template += OPEN_TD;
		template += "<div location =\"lblGuarantorTypeValue\" />";
		template += CLOSE_TD;
		template += CLOSE_TR;
		
		cusLayout.addComponent(new Label(I18N.message("contract.id")), "lblContractID");
		cusLayout.addComponent(new Label(I18N.message("status")), "lblStatus");
		cusLayout.addComponent(new Label(I18N.message("type")), "lblType");
		cusLayout.addComponent(lblLesseeContractID, "lblLesseeContractIDValue");
		cusLayout.addComponent(lblLesseeStatus, "lblLesseeStatusValue");
		cusLayout.addComponent(lblLesseeType, "lblLesseeTypeValue");
		cusLayout.addComponent(lblGuarantorContractID, "lblGuarantorContractIDValue");
		cusLayout.addComponent(lblGuarantorStatus, "lblGuarantorStatusValue");
		cusLayout.addComponent(lblGuarantorType, "lblGuarantorTypeValue");
		
		template += CLOSE_TABLE;
		cusLayout.setTemplateContents(template);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.addComponent(cusLayout);
		return horizontalLayout;
	}
	
	/**
	 * Get applicant for SaveorUpdate
	 * @param applicant
	 * @return
	 */
	public Applicant getApplicant(Applicant applicant) {
		personalInfomationPanel.getPersonalInformation(applicant);
		addressInfoPanel.getContactApplicant(addressInfoPanels, applicant);
		contactInfoPanel.getContactInfo(contactInfoPanels, applicant);
		financialIncomeAddressPanel.getEmploymentInfo(financialIncomeAddressPanels, applicant);
		if (applicant.getQuotations() != null && !applicant.getQuotations().isEmpty()) {
			applicant.getIndividual().setHouseholdIncome(getDouble(txtHouseholdIncome) != null ? getDouble(txtHouseholdIncome) : 0d);
			applicant.getIndividual().setHouseholdExpenses(getDouble(txtHouseholdExpenses) != null ? getDouble(txtHouseholdExpenses) : 0d);
		}
		return applicant;
	}
	
	/**
	 * @param contract
	 * @param applicant
	 * @param applicantType
	 */
	public void assignValues(Contract contract, Applicant applicant, EApplicantType applicantType) {
		if (applicantType.equals(EApplicantType.C)) {
			tabSheet.addTab(mainPanel, I18N.message("lessee"));
		} else if (applicantType.equals(EApplicantType.S)) {
			tabSheet.addTab(mainPanel, I18N.message("spouse"));
		} else if (applicantType.equals(EApplicantType.G)) {
			tabSheet.addTab(mainPanel, I18N.message("guarantor"));
		}
		if (applicant != null) {
			this.applicant = applicant;
			personalInfomationPanel.assignValues(applicant);
			this.assignValueToAddressInfomation(applicant);
			this.assignValueToContactInfo(applicant);
			assignValueToFinancialAddress(applicant);
			List<Quotation> quotations = applicant.getQuotations();
			//It show only LESSEE tab
			if (quotations != null && !quotations.isEmpty()) {
				netIncomeLayout.setVisible(true);
			} else {
				netIncomeLayout.setVisible(false);
			}
			if (contract != null) {
				lblLesseeContractID.setValue(getDefaultString(contract.getReference()));
				lblLesseeStatus.setValue(contract.getWkfStatus() != null ? contract.getWkfStatus().getDescEn() : "");
				lblLesseeType.setValue(I18N.message("lessee"));
				ContractApplicant guarantor = contract.getContractApplicant(EApplicantType.G);
				if (guarantor != null) {
					lblGuarantorContractID.setValue(getDefaultString(contract.getReference()));
					lblGuarantorStatus.setValue(contract.getWkfStatus() != null ? contract.getWkfStatus().getDescEn() : "");
					lblGuarantorType.setValue(I18N.message("guarantor"));
				}
			}
		}
	}
	
	/**
	 * @param enabled
	 */
	public void setApplicantEnabled(boolean enabled) {
		setApplicantEnabled(enabled, false);
	}
	
	/**
	 * 
	 * @param enabled
	 * @param all
	 */
	public void setApplicantEnabled(boolean enabled, boolean all) {
		
	}

	/**
	 * @return
	 */
	public boolean isValid() {
		super.removeErrorsPanel();
		personalInfomationPanel.isValid();
		addressInfoPanel.isValid();
		contactInfoPanel.isValid();
		for (FinancialIncomeAddressPanelOld financialIncomeAddressPanel : financialIncomeAddressPanels) {
			List<String> incomeAddressErrors = financialIncomeAddressPanel.fullValidate();
			if (incomeAddressErrors != null && !incomeAddressErrors.isEmpty()) {
				errors.addAll(incomeAddressErrors);
			}
		}
		if (!errors.isEmpty()) {
			super.displayErrorsPanel();
		}
		return errors.isEmpty();
	}

	/**
	 * Reset panel
	 */
	public void reset() {
		messagePanel.setVisible(false);
		personalInfomationPanel.reset();
		financialIncomeAddressPanel.reset();
		addressInfoPanel.reset();
		contactInfoPanel.reset();
		addressInfoPanels.clear();
		contactInfoPanels.clear();
		financialIncomeAddressPanels.clear();
		txtHouseholdExpenses.setValue("");
		txtNetIncome.setValue("");
		txtHouseholdIncome.setValue("");
	}
	
	/**
	 * Event to Click Add button Address,Contact,Reference
	 */
	@Override
	public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
		if (event.getButton() == btnAddAddress) {
			addressInfoPanel = new AddressInfoPanel();
			newAddressLayout = new VerticalLayout();
			
			btnCancelAddAddressInfo = new Button("X");
			btnCancelAddAddressInfo.setStyleName(Reindeer.BUTTON_LINK);
			btnCancelAddAddressInfo.addClickListener(new CancelAddAddressInfo(newAddressLayout));
			
			newAddressLayout.addComponent(btnCancelAddAddressInfo);
			newAddressLayout.setComponentAlignment(btnCancelAddAddressInfo, Alignment.MIDDLE_RIGHT);
			newAddressLayout.addComponent(addressInfoPanel);
			addressLayout.addComponent(newAddressLayout);
			addressInfoPanels.add(addressInfoPanel);
		} else if (event.getButton() == btnAddContactInfo) {
			contactInfoPanel = new ContactInfoPanel();
			newContactInfoLayout = new VerticalLayout();
			
			btnCancelAddContactInfo = new Button("X");
			btnCancelAddContactInfo.setStyleName(Reindeer.BUTTON_LINK);
			btnCancelAddContactInfo.addClickListener(new CancelAddContactInfo(newContactInfoLayout));
			
			newContactInfoLayout.addComponent(btnCancelAddContactInfo);	
			newContactInfoLayout.setComponentAlignment(btnCancelAddContactInfo, Alignment.MIDDLE_RIGHT);
			newContactInfoLayout.addComponent(contactInfoPanel);
			contactInfoLayout.addComponent(newContactInfoLayout);
			contactInfoPanels.add(contactInfoPanel);
		} else if (event.getButton() == btnAddFinancialInfo) {
			financialIncomeAddressPanel = new FinancialIncomeAddressPanelOld(null);
			newFinancialInfoLayout = new VerticalLayout();
			
			btnCancelAddFinancialInfo = new Button("X");
			btnCancelAddFinancialInfo.setStyleName(Reindeer.BUTTON_LINK);
			btnCancelAddFinancialInfo.addClickListener(new CancelAddFinancialInfo(newFinancialInfoLayout));
			
			newFinancialInfoLayout.addComponent(btnCancelAddFinancialInfo);
			newFinancialInfoLayout.setComponentAlignment(btnCancelAddFinancialInfo, Alignment.MIDDLE_RIGHT);
			newFinancialInfoLayout.addComponent(financialIncomeAddressPanel);
			financialInfoLayout.addComponent(newFinancialInfoLayout);
			financialIncomeAddressPanels.add(financialIncomeAddressPanel);
		} else if (event.getButton() == btnSave) {
			if (applicant != null && applicant.getId() != null) {
				ApplicantService applicantService= SpringUtils.getBean(ApplicantService.class);
				applicantService.saveOrUpdateApplicant(getApplicant(applicant));
				displaySuccessMsg();
			}
		}
	}
	
	/**
	 * 
	 * @author buntha.chea
	 *
	 */
	private class CancelAddAddressInfo implements ClickListener {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 7534827815805759534L;
		private VerticalLayout addressInfoLayouts;
		
		public CancelAddAddressInfo(VerticalLayout addressInfoLayout) {
			this.addressInfoLayouts = addressInfoLayout;
		}

		/**
		 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
		 */
		@Override
		public void buttonClick(ClickEvent event) {
			addressLayout.removeComponent(this.addressInfoLayouts);
			addressInfoPanels.remove(addressInfoPanel);
		}
	}
	
	/**
	 * 
	 * @author buntha.chea
	 *
	 */
	private class CancelAddContactInfo implements ClickListener {

		private static final long serialVersionUID = 8294004035427042606L;
		private VerticalLayout contactInfoLayouts;
		
		public CancelAddContactInfo(VerticalLayout contractInfoLayout) {
			this.contactInfoLayouts = contractInfoLayout;
		}

		/**
		 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
		 */
		@Override
		public void buttonClick(ClickEvent event) {
			contactInfoLayout.removeComponent(this.contactInfoLayouts);
			contactInfoPanels.remove(contactInfoPanel);
		}	
	}
	
	/**
	 * 
	 * @author buntha.chea
	 *
	 */
	private class CancelAddFinancialInfo implements ClickListener {

		private static final long serialVersionUID = 4152994615445066995L;
		private VerticalLayout financialInfos;
		
		public CancelAddFinancialInfo(VerticalLayout financialInfoLayout) {
			this.financialInfos = financialInfoLayout;
		}

		/**
		 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
		 */
		@Override
		public void buttonClick(ClickEvent event) {
			financialInfoLayout.removeComponent(this.financialInfos);
			financialIncomeAddressPanels.remove(financialIncomeAddressPanel);
		}
	}
	

	/**
	 * 
	 * @param applicant
	 */
	private void assignValueToFinancialAddress(Applicant applicant) {
		if (applicant != null) {
			employments = applicant.getIndividual().getEmployments();
			financialInfoLayout.removeAllComponents();
			newFinancialInfoLayout.removeAllComponents();
			financialIncomeAddressPanels.clear();
			double totalNetIncomeAmount = 0d;
			if (!employments.isEmpty()) {
				for (Employment employment : employments) {
					if (employment != null) {
						btnDeleteFinancialInfo = new Button("X");
						btnDeleteFinancialInfo.setStyleName(Reindeer.BUTTON_LINK);
						btnDeleteFinancialInfo.addClickListener(new ButtonDeleteFinancialInfoPanelListener(employment));
						
						financialIncomeAddressPanel = new FinancialIncomeAddressPanelOld(null);
						financialIncomeAddressPanel.assignValue(employment);
						financialInfoLayout.addComponent(btnDeleteFinancialInfo);
						financialInfoLayout.addComponent(financialIncomeAddressPanel);
						financialInfoLayout.setComponentAlignment(btnDeleteFinancialInfo, Alignment.MIDDLE_RIGHT);
						
						financialIncomeAddressPanels.add(financialIncomeAddressPanel);
						if (applicant.getQuotations() != null && !applicant.getQuotations().isEmpty()) {
							double incomeAmount = employment.getRevenue() != null ? employment.getRevenue() : 0d;
							totalNetIncomeAmount += incomeAmount;
						}
					}
				}
			} else {
				financialIncomeAddressPanel = new FinancialIncomeAddressPanelOld(null);
				financialInfoLayout.addComponent(financialIncomeAddressPanel);
				financialIncomeAddressPanels.add(financialIncomeAddressPanel);
			}
			if (applicant.getQuotations() != null && !applicant.getQuotations().isEmpty()) {
				txtNetIncome.setValue(AmountUtils.format(totalNetIncomeAmount));
				txtHouseholdExpenses.setValue(AmountUtils.format(applicant.getIndividual().getHouseholdExpenses()));
				txtHouseholdIncome.setValue(AmountUtils.format(applicant.getIndividual().getHouseholdIncome()));
			}
		}
	}
	/**
	 * set listener to button delete financial info panel
	 * @author buntha.chea
	 *
	 */
	private class ButtonDeleteFinancialInfoPanelListener implements ClickListener {

		private static final long serialVersionUID = 7814464240541588468L;
		private Employment employment;
		public ButtonDeleteFinancialInfoPanelListener(Employment employment) {
			this.employment = employment;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			confirmDeleteFinancial(employment);
			
		}
	}
	
	/**
	 * Assign value to panel Address Info	
	 * @param applicant
	 */
	private void assignValueToAddressInfomation(Applicant applicant) {
		if (applicant != null && applicant.getIndividual() != null) {
			individualAddresses = applicant.getIndividual().getIndividualAddresses();
			addressLayout.removeAllComponents();
			newAddressLayout.removeAllComponents();
			addressInfoPanels.clear();
			if (!individualAddresses.isEmpty()) {
				for (IndividualAddress individualAddresse : individualAddresses) {
					Address appAddress = individualAddresse.getAddress();
					if (appAddress != null) {
						btnDeleteAddressInfo = new Button("X");
						btnDeleteAddressInfo.setStyleName(Reindeer.BUTTON_LINK);
						btnDeleteAddressInfo.addClickListener(new ButtonDeleteAddressInfoPanelListener(individualAddresse));
						
						addressInfoPanel = new AddressInfoPanel();
						addressInfoPanel.assignValue(appAddress, applicant);
						addressLayout.addComponent(btnDeleteAddressInfo);
						addressLayout.addComponent(addressInfoPanel);
						addressLayout.setComponentAlignment(btnDeleteAddressInfo, Alignment.MIDDLE_RIGHT);
						addressInfoPanels.add(addressInfoPanel);
					}
				}
			} else {
				addressInfoPanel = new AddressInfoPanel();
				addressLayout.addComponent(addressInfoPanel);
				addressInfoPanels.add(addressInfoPanel);
			}
		}
	}
	/**
	 * set listener to button delete addressinfo panel
	 * @author buntha.chea
	 *
	 */
	private class ButtonDeleteAddressInfoPanelListener implements ClickListener{

		private static final long serialVersionUID = 7717916219953348352L;
		private IndividualAddress individualAddress;
		public ButtonDeleteAddressInfoPanelListener(IndividualAddress individualAddress) {
			this.individualAddress = individualAddress;
		}
		@Override
		public void buttonClick(ClickEvent event) {
			confirmDeleteAddressInfo(individualAddress);
			
		}
	}
	/**
	 * 	//Assign value to panel Contact info	
	 * @param applicant
	 */
	public void assignValueToContactInfo(Applicant applicant) {
		if (applicant != null && applicant.getIndividual() != null) {
			individualContactInfos = applicant.getIndividual().getIndividualContactInfos();
			contactInfoLayout.removeAllComponents();
			newContactInfoLayout.removeAllComponents();
			contactInfoPanels.clear();
			if (!individualContactInfos.isEmpty()) {
				for (IndividualContactInfo individualContactInfo : individualContactInfos) {
					ContactInfo contactInfo = individualContactInfo.getContactInfo();
					if (contactInfo != null) {
						btnDeleteContactInfo = new Button("X");
						btnDeleteContactInfo.setStyleName(Reindeer.BUTTON_LINK);
						btnDeleteContactInfo.addClickListener(new ButtonDeleteContactInfoPanleListener(individualContactInfo));
						
						contactInfoPanel = new ContactInfoPanel();
						contactInfoPanel.assignValue(contactInfo);
						contactInfoLayout.addComponent(btnDeleteContactInfo);
						contactInfoLayout.addComponent(contactInfoPanel);
						contactInfoLayout.setComponentAlignment(btnDeleteContactInfo, Alignment.MIDDLE_RIGHT);
						contactInfoPanels.add(contactInfoPanel);
					}
				}
			}else {
				contactInfoPanel = new ContactInfoPanel();
				contactInfoLayout.addComponent(contactInfoPanel);
				contactInfoPanels.add(contactInfoPanel);
			}
		}
	}
	/**
	 * set listener to button delete contactinfo panel
	 * @author buntha.chea
	 *
	 */
	private class ButtonDeleteContactInfoPanleListener implements ClickListener {

		private static final long serialVersionUID = 4152994615445066995L;
		private IndividualContactInfo individualContactInfo;
		/**
		 * 
		 * @param contactInfo
		 */
		public ButtonDeleteContactInfoPanleListener(IndividualContactInfo individualContactInfo) {
			this.individualContactInfo = individualContactInfo;
		}
		/**
		 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
		 */
		@Override
		public void buttonClick(ClickEvent event) {
			confirmDeleteContactInfo(this.individualContactInfo);
		}
	}
	
	/**
	 * 
	 * @param individualContactInfo
	 */
	private void confirmDeleteContactInfo(IndividualContactInfo individualContactInfo) {
		final DeleteInformationInApplicant deleteIndividual = new DeleteInformationInApplicant();
		deleteIndividual.setIndividualContactInfo(individualContactInfo);
		ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("are.you.sure.delete.contactinfo"),
		        new ConfirmDialog.Listener() {
					private static final long serialVersionUID = 2380193173874927880L;
					public void onClose(ConfirmDialog dialog) {
		                if (dialog.isConfirmed()) {
		                	IndividualContactInfo individualContactInfo = deleteIndividual.getIndividualContactInfo();
		                	ENTITY_SRV.delete(IndividualContactInfo.class, individualContactInfo.getId());
		                	ENTITY_SRV.delete(ContactInfo.class, individualContactInfo.getContactInfo().getId());
		                	individualContactInfos.remove(individualContactInfo);
		                	assignValueToContactInfo(applicant);
		                	Notification.show("",I18N.message("delete.success"),Notification.Type.HUMANIZED_MESSAGE);
		                }
		            }
			});
			confirmDialog.setWidth("400px");
			confirmDialog.setHeight("150px");
	}
	/**
	 * 
	 * @param individualAddress
	 */
	private void confirmDeleteAddressInfo(IndividualAddress individualAddress) {
		final DeleteInformationInApplicant deleteIndividual = new DeleteInformationInApplicant();
		deleteIndividual.setIndividualAddress(individualAddress);
		ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("are.you.sure.delete.addressinfo"),
		        new ConfirmDialog.Listener() {
					private static final long serialVersionUID = 2380193173874927880L;
					public void onClose(ConfirmDialog dialog) {
		                if (dialog.isConfirmed()) {
		                	IndividualAddress individualAddress = deleteIndividual.getIndividualAddress();
		                	ENTITY_SRV.delete(IndividualAddress.class, individualAddress.getId());
		                	ENTITY_SRV.delete(Address.class, individualAddress.getAddress().getId());
		                	individualAddresses.remove(individualAddress);
		                	assignValueToAddressInfomation(applicant);
		                	Notification.show("",I18N.message("delete.success"),Notification.Type.HUMANIZED_MESSAGE);
		                }
		            }
			});
			confirmDialog.setWidth("400px");
			confirmDialog.setHeight("150px");
	}
	/**
	 * 
	 * @param employment
	 */
	private void confirmDeleteFinancial(Employment employment) {
		final DeleteInformationInApplicant deleteApplicant = new DeleteInformationInApplicant();
		deleteApplicant.setEmployment(employment);
		ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("are.you.sure.delete.financialinfo"),
		        new ConfirmDialog.Listener() {
					private static final long serialVersionUID = 2380193173874927880L;
					public void onClose(ConfirmDialog dialog) {
		                if (dialog.isConfirmed()) {
		                	Employment employment = deleteApplicant.getEmployment();
		                	ENTITY_SRV.delete(Employment.class, employment.getId());
		                	ENTITY_SRV.delete(Address.class, employment.getAddress().getId());
		                	employments.remove(employment);
		                	assignValueToFinancialAddress(applicant);
		                	Notification.show("",I18N.message("delete.success"),Notification.Type.HUMANIZED_MESSAGE);
		                }
		            }
			});
			confirmDialog.setWidth("400px");
			confirmDialog.setHeight("150px");
	}
	
	/**
	 * 
	 * @author buntha.chea
	 *
	 */
	private class DeleteInformationInApplicant {
		private IndividualContactInfo individualContactInfo;
		private IndividualAddress individualAddress;
		private Employment employment;

		/**
		 * @return the employment
		 */
		public Employment getEmployment() {
			return employment;
		}

		/**
		 * @param employment the employment to set
		 */
		public void setEmployment(Employment employment) {
			this.employment = employment;
		}

		/**
		 * @return the individualAddress
		 */
		public IndividualAddress getIndividualAddress() {
			return individualAddress;
		}

		/**
		 * @param individualAddress the individualAddress to set
		 */
		public void setIndividualAddress(IndividualAddress individualAddress) {
			this.individualAddress = individualAddress;
		}

		/**
		 * @return the individualContactInfo
		 */
		public IndividualContactInfo getIndividualContactInfo() {
			return individualContactInfo;
		}

		/**
		 * @param individualContactInfo the individualContactInfo to set
		 */
		public void setIndividualContactInfo(IndividualContactInfo individualContactInfo) {
			this.individualContactInfo = individualContactInfo;
		}
	}
}
