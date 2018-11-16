package com.nokor.efinance.core.applicant.panel.applicant.individual;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.model.Employment;
import com.nokor.efinance.core.applicant.model.IndividualAddress;
import com.nokor.efinance.core.applicant.model.IndividualContactInfo;
import com.nokor.efinance.core.applicant.panel.AddressInfoPanel;
import com.nokor.efinance.core.applicant.panel.contact.ContactInfoPanel;
import com.nokor.efinance.core.applicant.panel.contact.FinancialIncomeAddressPanelOld;
import com.nokor.efinance.core.applicant.panel.contact.PersonalInformationPanel;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.organization.ContactInfo;
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
public class ApplicantPanel extends AbstractTabPanel implements ClickListener, FinServicesHelper {

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
	
	private VerticalLayout mainLayout;
	private Button btnSave;
	private Button btnBack;
	private VerticalLayout messagePanel;
	private TabSheet tabSheet;

	/** */
	public ApplicantPanel() {
		super.setMargin(false);
		setMargin(false);
		btnSave = new NativeButton(I18N.message("save"));
		btnSave.setIcon(FontAwesome.SAVE);
		btnSave.addClickListener(this);
		
		btnBack = new NativeButton(I18N.message("back"));
		btnBack.setIcon(FontAwesome.STEP_BACKWARD);	
		
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.setSizeFull();
		messagePanel.addStyleName("message");
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.setSizeFull();
		navigationPanel.addButton(btnBack);
		navigationPanel.addButton(btnSave);
		addComponent(navigationPanel, 0);
		addComponent(messagePanel, 1);
	}	
	
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
	
	/**
	 * 
	 * @param width
	 * @param unit
	 * @return
	 */
	private VerticalLayout getVerticalLayout(float width, Unit unit) {
		VerticalLayout verLayout = new VerticalLayout();
		verLayout.setWidth(width, unit);
		return verLayout;
	}
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private Label getLabel(String caption) {
		Label label = new Label();
		label.setValue("<h2 style=\"border:1px solid #E3E3E3;padding:9px;border-radius:3px;margin:0;"
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
		
		personalInfomationPanel = new PersonalInformationPanel();
		financialIncomeAddressPanel = new FinancialIncomeAddressPanelOld(this);
		addressInfoPanel = new AddressInfoPanel();
		contactInfoPanel = new ContactInfoPanel();
		
		addressInfoPanels = new ArrayList<>();
		contactInfoPanels = new ArrayList<>();
		financialIncomeAddressPanels = new ArrayList<>();
		
		btnAddAddress = new Button(I18N.message("add.address"));
		btnAddAddress.setWidth(350, Unit.PIXELS);
		btnAddAddress.addClickListener(this);
		
		btnAddContactInfo = new Button(I18N.message("add.contact"));
		btnAddContactInfo.setWidth(470, Unit.PIXELS);
		btnAddContactInfo.addClickListener(this);
		
		btnAddFinancialInfo = new Button(I18N.message("add.income"));
		btnAddFinancialInfo.setWidth(50, Unit.PERCENTAGE);
		btnAddFinancialInfo.addClickListener(this);
		
		addressLayout = getVerticalLayout(350, Unit.PIXELS);
		newAddressLayout = getVerticalLayout(350, Unit.PIXELS);
		addressLayout.addComponent(addressInfoPanel);
		
		contactInfoLayout = getVerticalLayout(470, Unit.PIXELS);
		newContactInfoLayout = getVerticalLayout(470, Unit.PIXELS);
		contactInfoLayout.addComponent(contactInfoPanel);
		
		financialInfoLayout = new VerticalLayout();
		newFinancialInfoLayout = new VerticalLayout();
		financialInfoLayout.addComponent(financialIncomeAddressPanel);
		
		txtNetIncome = ComponentFactory.getTextField(20, 190);
		txtHouseholdIncome = ComponentFactory.getTextField(20, 190);
		txtHouseholdExpenses = ComponentFactory.getTextField(false, 20, 190);
		
		netIncomeLayout = new VerticalLayout(); 
		netIncomeLayout.addComponent(getNetIncomeLayout());
		
		Label lblFinanceTitle = getLabel("finance");
		VerticalLayout financialInfoWithButtonAdd = new VerticalLayout();
		financialInfoWithButtonAdd.setMargin(true);
		financialInfoWithButtonAdd.setSpacing(true);
		financialInfoWithButtonAdd.addComponent(financialInfoLayout);
		financialInfoWithButtonAdd.addComponent(btnAddFinancialInfo);
		financialInfoWithButtonAdd.setComponentAlignment(btnAddFinancialInfo, Alignment.MIDDLE_CENTER);
		financialInfoWithButtonAdd.addComponent(netIncomeLayout);
		
		Panel financePanel = new Panel();
		financePanel.setCaption(I18N.message("financial.information"));
		financePanel.setContent(financialInfoWithButtonAdd);
		
		VerticalLayout adrLayout = new VerticalLayout();
		adrLayout.setSpacing(true);
		adrLayout.addComponent(addressLayout);
		adrLayout.addComponent(btnAddAddress);
		
		VerticalLayout conLayout = new VerticalLayout();
		conLayout.setSpacing(true);
		conLayout.addComponent(contactInfoLayout);
		conLayout.addComponent(btnAddContactInfo);
		
		Label lblContactTitle = getLabel("contact");
		HorizontalLayout contactAddressLayout = new HorizontalLayout();
		contactAddressLayout.setSpacing(true);
		contactAddressLayout.setMargin(true);
		contactAddressLayout.addComponent(adrLayout);
		contactAddressLayout.addComponent(conLayout);
		
		Panel contactPanel = new Panel();
		contactPanel.setCaption(I18N.message("contact.information"));
		contactPanel.setContent(contactAddressLayout);
		
		Label lblPersonalTitle = getLabel("personal");
		Panel personPanel = new Panel();
		personPanel.setCaption(I18N.message("personal.information"));
		personPanel.setContent(personalInfomationPanel);
		
		VerticalLayout verLayout = new VerticalLayout();
		verLayout.setMargin(true);
		verLayout.setSpacing(true);
		verLayout.addComponent(lblContactTitle);
		verLayout.addComponent(contactPanel);
		verLayout.addComponent(lblFinanceTitle);
		verLayout.addComponent(financePanel);
		verLayout.addComponent(lblPersonalTitle);
		verLayout.addComponent(personPanel);
		
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.setSizeFull();
		horLayout.addComponent(verLayout);
		
		mainLayout = new VerticalLayout();
		mainLayout.addStyleName("overflow-layout-style");
		mainLayout.addComponent(horLayout);
		
		tabSheet = new TabSheet();
		
        return tabSheet;
    }

	/**
	 * 
	 * @return
	 */
	private CustomLayout getNetIncomeLayout() {
		CustomLayout cusLayout = new CustomLayout("xxx");
		String template = "<table cellspacing=\"2\" cellpadding=\"2\" border=\"0\" >";
		template += "<tr>";
		template += "<td align=\"left\" width=\"120\"><div location=\"lblNetIncome\"></td>";
		template += "<td><div location =\"txtNetIncome\"/></td>";
		template += "</tr>";
		template += "<tr>";
		template += "<td><div location=\"lblHouseHoldIncome\"></td>";
		template += "<td><div location =\"txtHouseHoldIncome\"/></td>";
		template += "</tr>";
		template += "<tr>";
		template += "<td><div location=\"lblHouseHoldExpenses\"></td>";
		template += "<td><div location =\"txtHouseHoldExpenses\"/></td>";
		template += "</tr>";
		template += "</table>";
		
		cusLayout.addComponent(new Label(I18N.message("net.income")), "lblNetIncome");
		cusLayout.addComponent(new Label(I18N.message("household.income")), "lblHouseHoldIncome");
		cusLayout.addComponent(new Label(I18N.message("household.expenses")), "lblHouseHoldExpenses");
		cusLayout.addComponent(txtNetIncome, "txtNetIncome");
		cusLayout.addComponent(txtHouseholdIncome, "txtHouseHoldIncome");
		cusLayout.addComponent(txtHouseholdExpenses, "txtHouseHoldExpenses");
		cusLayout.setTemplateContents(template);

		return cusLayout;
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
	 * Get applicant for SaveorUpdate
	 * @param applicant
	 * @return
	 */
	public Applicant getApplicant(Applicant applicant) {
		personalInfomationPanel.getPersonalInformation(applicant);
		addressInfoPanel.getContactApplicant(addressInfoPanels, applicant);
		contactInfoPanel.getContactInfo(contactInfoPanels, applicant);
		financialIncomeAddressPanel.getEmploymentInfo(financialIncomeAddressPanels, applicant);
		if (applicant.getContracts() != null && !applicant.getContracts().isEmpty()) {
			applicant.getIndividual().setHouseholdIncome(getDouble(txtHouseholdIncome) != null ? getDouble(txtHouseholdIncome) : 0d);
			applicant.getIndividual().setHouseholdExpenses(getDouble(txtHouseholdExpenses) != null ? getDouble(txtHouseholdExpenses) : 0d);
		}
		return applicant;
	}
	
	/**
	 * 
	 * @param applicant
	 * @param applicantType
	 */
	public void assignValues(Applicant applicant, EApplicantType applicantType) {
		removeMessagePanel();
		if (applicantType.equals(EApplicantType.C)) {
			tabSheet.addTab(mainLayout, I18N.message("lessee"));
		} else if (applicantType.equals(EApplicantType.S)) {
			tabSheet.addTab(mainLayout, I18N.message("spouse"));
		} else if (applicantType.equals(EApplicantType.G)) {
			tabSheet.addTab(mainLayout, I18N.message("guarantor"));
		}
		if (applicant != null) {
			this.applicant = applicant;
			personalInfomationPanel.assignValues(applicant);
			this.assignValueToAddressInfomation(applicant);
			this.assignValueToContactInfo(applicant);
			assignValueToFinancialAddress(applicant);
			List<Contract> contracts = applicant.getContracts();
			//It show only LESSEE tab
			if (contracts != null && !contracts.isEmpty()) {
				netIncomeLayout.setVisible(true);
			} else {
				netIncomeLayout.setVisible(false);
			}
		}
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
	 * Reset
	 */
	public void removeMessagePanel() {
		messagePanel.removeAllComponents();
		messagePanel.setVisible(false);
		errors.clear();
	}

	/**
	 * Reset panel
	 */
	public void reset() {
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
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
		if (event.getButton() == btnAddAddress) {
			addressInfoPanel = new AddressInfoPanel();
			newAddressLayout = getVerticalLayout(350, Unit.PIXELS);
			
			btnCancelAddAddressInfo = new Button("X");
			btnCancelAddAddressInfo.setStyleName(Reindeer.BUTTON_LINK);
			btnCancelAddAddressInfo.addClickListener(new CancelAddAddressInfoListener(newAddressLayout, addressInfoPanel));
			
			VerticalLayout verLayout = new VerticalLayout();
			verLayout.addComponent(btnCancelAddAddressInfo);
			verLayout.setComponentAlignment(btnCancelAddAddressInfo, Alignment.MIDDLE_RIGHT);
			verLayout.addComponent(addressInfoPanel);
			newAddressLayout.addComponent(new Panel(verLayout));
			
			addressLayout.addComponent(newAddressLayout);
			addressInfoPanels.add(addressInfoPanel);
		} else if (event.getButton() == btnAddContactInfo) {
			contactInfoPanel = new ContactInfoPanel();
			newContactInfoLayout = getVerticalLayout(470, Unit.PIXELS);
			
			btnCancelAddContactInfo = new Button("X");
			btnCancelAddContactInfo.setStyleName(Reindeer.BUTTON_LINK);
			btnCancelAddContactInfo.addClickListener(new CancelAddContactInfoListener(newContactInfoLayout, contactInfoPanel));
			
			VerticalLayout verLayout = new VerticalLayout();
			verLayout.addComponent(btnCancelAddContactInfo);	
			verLayout.setComponentAlignment(btnCancelAddContactInfo, Alignment.MIDDLE_RIGHT);
			verLayout.addComponent(contactInfoPanel);
			newContactInfoLayout.addComponent(new Panel(verLayout));
			
			contactInfoLayout.addComponent(newContactInfoLayout);
			contactInfoPanels.add(contactInfoPanel);
		} else if (event.getButton() == btnAddFinancialInfo) {
			financialIncomeAddressPanel = new FinancialIncomeAddressPanelOld(this);
			newFinancialInfoLayout = new VerticalLayout();
			
			btnCancelAddFinancialInfo = new Button("X");
			btnCancelAddFinancialInfo.setStyleName(Reindeer.BUTTON_LINK);
			btnCancelAddFinancialInfo.addClickListener(new CancelAddFinancialInfoListener(newFinancialInfoLayout, financialIncomeAddressPanel));
			
			VerticalLayout verLayout = new VerticalLayout();
			verLayout.addComponent(btnCancelAddFinancialInfo);
			verLayout.setComponentAlignment(btnCancelAddFinancialInfo, Alignment.MIDDLE_RIGHT);
			verLayout.addComponent(financialIncomeAddressPanel);
			newFinancialInfoLayout.addComponent(new Panel(verLayout));
			
			financialInfoLayout.addComponent(newFinancialInfoLayout);
			financialIncomeAddressPanels.add(financialIncomeAddressPanel);
		} else if (event.getButton() == btnSave) {
			if (applicant != null && applicant.getId() != null) {
				APP_SRV.saveOrUpdateApplicant(getApplicant(applicant));
				displaySuccessMsg();
			}
		}
	}
	
	/**
	 * 
	 * @author buntha.chea
	 *
	 */
	private class CancelAddAddressInfoListener implements ClickListener {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 7534827815805759534L;
		private VerticalLayout addressInfoLayouts;
		private AddressInfoPanel addressInfoPanel;
		
		/**
		 * 
		 * @param addressInfoLayout
		 * @param addressInfoPanel
		 */
		public CancelAddAddressInfoListener(VerticalLayout addressInfoLayout, AddressInfoPanel addressInfoPanel) {
			this.addressInfoLayouts = addressInfoLayout;
			this.addressInfoPanel = addressInfoPanel;
		}

		/**
		 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
		 */
		@Override
		public void buttonClick(ClickEvent event) {
			addressLayout.removeComponent(this.addressInfoLayouts);
			addressInfoPanels.remove(this.addressInfoPanel);
		}
	}
	
	/**
	 * 
	 * @author buntha.chea
	 *
	 */
	private class CancelAddContactInfoListener implements ClickListener {

		private static final long serialVersionUID = 8294004035427042606L;
		private VerticalLayout contactInfoLayouts;
		private ContactInfoPanel contactInfoPanel;
		
		/**
		 * 
		 * @param contractInfoLayout
		 * @param contactInfoPanel
		 */
		public CancelAddContactInfoListener(VerticalLayout contractInfoLayouts, ContactInfoPanel contactInfoPanel) {
			this.contactInfoLayouts = contractInfoLayouts;
			this.contactInfoPanel = contactInfoPanel;
		}

		/**
		 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
		 */
		@Override
		public void buttonClick(ClickEvent event) {
			contactInfoLayout.removeComponent(this.contactInfoLayouts);
			contactInfoPanels.remove(this.contactInfoPanel);
		}	
	}
	
	/**
	 * 
	 * @author buntha.chea
	 *
	 */
	private class CancelAddFinancialInfoListener implements ClickListener {

		private static final long serialVersionUID = 4152994615445066995L;
		private VerticalLayout financialInfos;
		private FinancialIncomeAddressPanelOld finIncomeAddressPanel;
		
		/**
		 * 
		 * @param financialInfoLayout
		 * @param finIncomeAddressPanel
		 */
		public CancelAddFinancialInfoListener(VerticalLayout financialInfoLayout, FinancialIncomeAddressPanelOld finIncomeAddressPanel) {
			this.financialInfos = financialInfoLayout;
			this.finIncomeAddressPanel = finIncomeAddressPanel;
		}

		/**
		 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
		 */
		@Override
		public void buttonClick(ClickEvent event) {
			financialInfoLayout.removeComponent(this.financialInfos);
			financialIncomeAddressPanels.remove(this.finIncomeAddressPanel);
			updateNetIncome();
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
						btnDeleteFinancialInfo.addClickListener(new DeleteFinancialInfoListener(employment));
						
						financialIncomeAddressPanel = new FinancialIncomeAddressPanelOld(this);
						financialIncomeAddressPanel.assignValue(employment);
						
						VerticalLayout verLayout = new VerticalLayout();
						verLayout.addComponent(btnDeleteFinancialInfo);
						verLayout.addComponent(financialIncomeAddressPanel);
						verLayout.setComponentAlignment(btnDeleteFinancialInfo, Alignment.MIDDLE_RIGHT);
						financialInfoLayout.addComponent(new Panel(verLayout));
						
						financialIncomeAddressPanels.add(financialIncomeAddressPanel);
						if (applicant.getContracts() != null && !applicant.getContracts().isEmpty()) {
							double incomeAmount = employment.getRevenue() != null ? employment.getRevenue() : 0d;
							totalNetIncomeAmount += incomeAmount;
						}
					}
				}
			} else {
				financialIncomeAddressPanel = new FinancialIncomeAddressPanelOld(this);
				financialInfoLayout.addComponent(financialIncomeAddressPanel);
				financialIncomeAddressPanels.add(financialIncomeAddressPanel);
			}
			if (applicant.getContracts() != null && !applicant.getContracts().isEmpty()) {
				txtNetIncome.setValue(AmountUtils.format(totalNetIncomeAmount));
				txtHouseholdExpenses.setValue(AmountUtils.format(applicant.getIndividual().getHouseholdExpenses()));
				txtHouseholdIncome.setValue(AmountUtils.format(applicant.getIndividual().getHouseholdIncome()));
			}
		}
	}
	
	/**
	 * 
	 */
	public void updateNetIncome() {
		if (netIncomeLayout.isVisible() && !financialIncomeAddressPanels.isEmpty()) {
			double totalNetIncome =0d;
			for (FinancialIncomeAddressPanelOld incomeAddressPanel : financialIncomeAddressPanels) {
				double incomeAmount = MyNumberUtils.getDouble(getDouble(incomeAddressPanel.getTxtIncome()));
				totalNetIncome += incomeAmount;
			}
			txtNetIncome.setValue(AmountUtils.format(totalNetIncome));
		}
	}
	
	/**
	 * set listener to button delete financial info panel
	 * @author buntha.chea
	 *
	 */
	private class DeleteFinancialInfoListener implements ClickListener {

		private static final long serialVersionUID = 7814464240541588468L;
		private Employment employment;
		public DeleteFinancialInfoListener(Employment employment) {
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
						btnDeleteAddressInfo.addClickListener(new DeleteAddressInfoListener(individualAddresse));
						
						addressInfoPanel = new AddressInfoPanel();
						addressInfoPanel.assignValue(appAddress, applicant);
						
						VerticalLayout verLayout = new VerticalLayout();
						verLayout.addComponent(btnDeleteAddressInfo);
						verLayout.addComponent(addressInfoPanel);
						verLayout.setComponentAlignment(btnDeleteAddressInfo, Alignment.MIDDLE_RIGHT);
						
						addressLayout.addComponent(new Panel(verLayout));
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
	 * set listener to button delete address info panel
	 * @author buntha.chea
	 */
	private class DeleteAddressInfoListener implements ClickListener{

		private static final long serialVersionUID = 7717916219953348352L;
		private IndividualAddress individualAddress;
		public DeleteAddressInfoListener(IndividualAddress individualAddress) {
			this.individualAddress = individualAddress;
		}
		@Override
		public void buttonClick(ClickEvent event) {
			confirmDeleteAddressInfo(individualAddress);
		}
	}
	
	/**
	 * Assign value to panel Contact info	
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
						btnDeleteContactInfo.addClickListener(new DeleteContactInfoListener(individualContactInfo));
						
						contactInfoPanel = new ContactInfoPanel();
						contactInfoPanel.assignValue(contactInfo);
						
						VerticalLayout verLayout = new VerticalLayout();
						verLayout.addComponent(btnDeleteContactInfo);
						verLayout.addComponent(contactInfoPanel);
						verLayout.setComponentAlignment(btnDeleteContactInfo, Alignment.MIDDLE_RIGHT);
						
						contactInfoLayout.addComponent(new Panel(verLayout));
						contactInfoPanels.add(contactInfoPanel);
					}
				}
			}else {
				contactInfoPanel = new ContactInfoPanel();
				contactInfoLayout.addComponent(new Panel(contactInfoPanel));
				contactInfoPanels.add(contactInfoPanel);
			}
		}
	}
	
	/**
	 * Set listener to button delete contact info panel
	 * @author buntha.chea
	 *
	 */
	private class DeleteContactInfoListener implements ClickListener {

		private static final long serialVersionUID = 4152994615445066995L;
		private IndividualContactInfo individualContactInfo;
		/**
		 * 
		 * @param contactInfo
		 */
		public DeleteContactInfoListener(IndividualContactInfo individualContactInfo) {
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
