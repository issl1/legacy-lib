package com.nokor.efinance.core.applicant.panel;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.Employment;
import com.nokor.efinance.core.applicant.model.IndividualAddress;
import com.nokor.efinance.core.applicant.model.IndividualContactInfo;
import com.nokor.efinance.core.applicant.model.IndividualReferenceContactInfo;
import com.nokor.efinance.core.applicant.model.IndividualReferenceInfo;
import com.nokor.efinance.core.applicant.panel.contact.ContactInfoPanel;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.organization.ContactInfo;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Applicant panel
 * @author ly.youhort
 * @author buntha.chea (modify)
 */
public class ApplicantPanel extends AbstractTabPanel implements ClickListener, FrmkServicesHelper {
	
	private static final long serialVersionUID = 710425425958548975L;
	
	private PersonalInformationPanel personalInfomationPanel;
	private FinancialIncomeAddressPanel financialIncomeAddressPanel;
	private AddressInfoPanel addressInfoPanel;
	private ContactInfoPanel contactInfoPanel;
	private ReferenceInfoPanel referenceInfoPanel;
	
	private List<AddressInfoPanel> addressInfoPanels;
	private List<ContactInfoPanel> contactInfoPanels;
	private List<ReferenceInfoPanel> referenceInfoPanels;
	private List<FinancialIncomeAddressPanel> financialIncomeAddressPanels;
	private List<List<ContactInfoPanel>> listContainListOfContactInfoPanel;
	
	private Button btnAddAddress;
	private Button btnCancelAddAddressInfo;
	private Button btnAddContactInfo;
	private Button btnCancelAddContactInfo;
	private Button btnAddReferenceInfo;
	private Button btnCancelAddReferenceInfo;
	
	private Button btnAddFinancialInfo;
	private Button btnCancelAddFinancialInfo;
	
	private VerticalLayout addressLayout;
	private VerticalLayout newAddressLayout;
	private VerticalLayout contactInfoLayout;
	private VerticalLayout newContactInfoLayout;
	private VerticalLayout referenceLayout;
	private VerticalLayout newreferenceLayout;
	
	private VerticalLayout financialInfoLayout;
	private VerticalLayout newFinancialInfoLayout;
	private VerticalLayout netIncomeLayout;
	
	private TextField txtNetIncome;
	private TextField txtHouseholdExpenses;
	private TextField txtHouseholdIncome;
	
	private Button btnDeleteContactInfo;
	private Button btnDeleteAddressInfo;
	private Button btnDeleteFinancialInfo;
	private Button btnDeleteReferenceInfo;
	private Applicant applicant;
	private List<IndividualContactInfo> individualContactInfos;
	private List<IndividualAddress> individualAddresses;
	private List<Employment> employments;
	private List<IndividualReferenceInfo> individualReferenceInfos;
	
	/** */
	public ApplicantPanel() {
		super();
		setSizeFull();
	}	
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {	
		personalInfomationPanel = new PersonalInformationPanel();
		financialIncomeAddressPanel = new FinancialIncomeAddressPanel();
		addressInfoPanel = new AddressInfoPanel();
		contactInfoPanel = new ContactInfoPanel();
		referenceInfoPanel = new ReferenceInfoPanel();
		
		addressInfoPanels = new ArrayList<>();
		contactInfoPanels = new ArrayList<>();
		referenceInfoPanels = new ArrayList<>();
		financialIncomeAddressPanels = new ArrayList<>();
		
		btnAddAddress = new Button(I18N.message("add.address"));
		btnAddAddress.setWidth("350px");
		btnAddAddress.addClickListener(this);
		
		btnAddContactInfo = new Button(I18N.message("add.contact"));
		btnAddContactInfo.setWidth("350px");
		btnAddContactInfo.addClickListener(this);
		
		btnAddReferenceInfo = new Button(I18N.message("add.reference"));
		btnAddReferenceInfo.setWidth("350px");
		btnAddReferenceInfo.addClickListener(this);
		
		btnAddFinancialInfo = new Button(I18N.message("add.income"));
		btnAddFinancialInfo.setWidth("350px");
		btnAddFinancialInfo.addClickListener(this);
		
		addressLayout = new VerticalLayout();
		newAddressLayout = new VerticalLayout();
		addressLayout.addComponent(addressInfoPanel);
		
		contactInfoLayout = new VerticalLayout();
		newContactInfoLayout = new VerticalLayout();
		contactInfoLayout.addComponent(contactInfoPanel);
		
		referenceLayout = new VerticalLayout();
		newreferenceLayout = new VerticalLayout();
		referenceLayout.addComponent(referenceInfoPanel);
		
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
		
		VerticalLayout financialInfoWithButtonAdd = new VerticalLayout();
		financialInfoWithButtonAdd.addComponent(financialInfoLayout);
		financialInfoWithButtonAdd.addComponent(btnAddFinancialInfo);
		financialInfoWithButtonAdd.addComponent(netIncomeLayout);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.addComponent(personalInfomationPanel);
		verticalLayout.addComponent(addressLayout);
		verticalLayout.addComponent(btnAddAddress);
		verticalLayout.addComponent(contactInfoLayout);
		verticalLayout.addComponent(btnAddContactInfo);
		verticalLayout.addComponent(referenceLayout);
		verticalLayout.addComponent(btnAddReferenceInfo);
		
		HorizontalLayout personeAndFinancialLayout = new HorizontalLayout();
		personeAndFinancialLayout.addComponent(verticalLayout);
		personeAndFinancialLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS));
		personeAndFinancialLayout.addComponent(financialInfoWithButtonAdd);
		
		Panel panel = new Panel();
		panel.setContent(personeAndFinancialLayout);
        return panel;
    }
	
	/**
	 * Get applicant for SaveorUpdate
	 * @param applicant
	 * @return
	 */
	public Applicant getApplicant(Applicant applicant) {
		this.applicant = applicant;
		personalInfomationPanel.getPersonalInformation(applicant);
		addressInfoPanel.getContactApplicant(addressInfoPanels, applicant);
		contactInfoPanel.getContactInfo(contactInfoPanels, applicant);
		financialIncomeAddressPanel.getEmploymentInfo(financialIncomeAddressPanels, applicant);
		if (applicant.getQuotations() != null && !applicant.getQuotations().isEmpty()) {
			referenceInfoPanel.getReferenceInfo(referenceInfoPanels, applicant, listContainListOfContactInfoPanel);
			applicant.getIndividual().setHouseholdIncome(getDouble(txtHouseholdIncome) != null ? getDouble(txtHouseholdIncome) : 0d);
			applicant.getIndividual().setHouseholdExpenses(getDouble(txtHouseholdExpenses) != null ? getDouble(txtHouseholdExpenses) : 0d);
		}
		return applicant;
	}
	
	/**
	 * Set applicant
	 * @param applicant
	 */
	public void assignValues(Applicant applicant) {
		personalInfomationPanel.assignValuePersonalInformaion(applicant);
		this.assignValueToAddressInfomation(applicant);
		this.assignValueToContactInfo(applicant);
		assignValueToFinancialAddress(applicant);
		List<Quotation> quotations = applicant.getQuotations();
		//It show only lessee tab
		if (quotations != null && !quotations.isEmpty()) {
			netIncomeLayout.setVisible(true);
			referenceLayout.setVisible(true);
			btnAddReferenceInfo.setVisible(true);
			this.assignReferenceInfoValue(applicant);
		} else {
			netIncomeLayout.setVisible(false);
			referenceLayout.setVisible(false);
			btnAddReferenceInfo.setVisible(false);
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
		referenceInfoPanel.isValid();
		for (FinancialIncomeAddressPanel financialIncomeAddressPanel : financialIncomeAddressPanels) {
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
		super.removeErrorsPanel();
		personalInfomationPanel.reset();
		financialIncomeAddressPanel.reset();
		addressInfoPanel.reset();
		contactInfoPanel.reset();
		referenceInfoPanel.reset();
		addressInfoPanels.clear();
		contactInfoPanels.clear();
		referenceInfoPanels.clear();
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
		} else if (event.getButton() == btnAddReferenceInfo) {
			referenceInfoPanel = new ReferenceInfoPanel();
			newreferenceLayout = new VerticalLayout();
			
			btnCancelAddReferenceInfo = new Button("X");
			btnCancelAddReferenceInfo.setStyleName(Reindeer.BUTTON_LINK);
			btnCancelAddReferenceInfo.addClickListener(new CancelAddReferenceInfo(newreferenceLayout));
			
			newreferenceLayout.addComponent(btnCancelAddReferenceInfo);
			newreferenceLayout.setComponentAlignment(btnCancelAddReferenceInfo, Alignment.MIDDLE_RIGHT);
			newreferenceLayout.addComponent(referenceInfoPanel);
			referenceLayout.addComponent(newreferenceLayout);
			referenceInfoPanels.add(referenceInfoPanel);
		} else if (event.getButton() == btnAddFinancialInfo) {
			financialIncomeAddressPanel = new FinancialIncomeAddressPanel();
			newFinancialInfoLayout = new VerticalLayout();
			
			btnCancelAddFinancialInfo = new Button("X");
			btnCancelAddFinancialInfo.setStyleName(Reindeer.BUTTON_LINK);
			btnCancelAddFinancialInfo.addClickListener(new CancelAddFinancialInfo(newFinancialInfoLayout));
			
			newFinancialInfoLayout.addComponent(btnCancelAddFinancialInfo);
			newFinancialInfoLayout.setComponentAlignment(btnCancelAddFinancialInfo, Alignment.MIDDLE_RIGHT);
			newFinancialInfoLayout.addComponent(financialIncomeAddressPanel);
			financialInfoLayout.addComponent(newFinancialInfoLayout);
			financialIncomeAddressPanels.add(financialIncomeAddressPanel);
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
	private class CancelAddReferenceInfo implements ClickListener {

		private static final long serialVersionUID = -1610455095911229423L;
		private VerticalLayout referenceLayouts;
		
		public CancelAddReferenceInfo(VerticalLayout referenceInfoLayout) {
			this.referenceLayouts = referenceInfoLayout;
		}

		/**
		 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
		 */
		@Override
		public void buttonClick(ClickEvent event) {
			referenceLayout.removeComponent(this.referenceLayouts);
			referenceInfoPanels.remove(referenceInfoPanel);
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
						
						financialIncomeAddressPanel = new FinancialIncomeAddressPanel();
						financialIncomeAddressPanel.assignValue(employment);
						financialInfoLayout.addComponent(btnDeleteFinancialInfo);
						financialInfoLayout.addComponent(financialIncomeAddressPanel);
						financialInfoLayout.setComponentAlignment(btnDeleteFinancialInfo, Alignment.MIDDLE_RIGHT);
						
						financialIncomeAddressPanels.add(financialIncomeAddressPanel);
						/*if (employment.getApplicant().getQuotations() != null && !employment.getApplicant().getQuotations().isEmpty()) {
							double incomeAmount = employment.getRevenue() != null ? employment.getRevenue() : 0d;
							totalNetIncomeAmount += incomeAmount;
						}*/
					}
				}
			} else {
				financialIncomeAddressPanel = new FinancialIncomeAddressPanel();
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
	 * set listener to button delete financialinfo panel
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
	 * 	Assign value to panel Address Info	
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
		if (applicant != null) {
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
	 * assign value to Reference Panel
	 * @param applicant
	 */
	public void assignReferenceInfoValue(Applicant applicant) {
		listContainListOfContactInfoPanel = new ArrayList<>();
		individualReferenceInfos = applicant.getIndividual().getIndividualReferenceInfos();
		referenceLayout.removeAllComponents();
		newreferenceLayout.removeAllComponents();
		referenceInfoPanels.clear();
		if (!individualReferenceInfos.isEmpty()) {
			for (IndividualReferenceInfo individualReferenceInfo : individualReferenceInfos) {
				btnDeleteReferenceInfo = new Button("X");
				btnDeleteReferenceInfo.setStyleName(Reindeer.BUTTON_LINK);
				btnDeleteReferenceInfo.addClickListener(new ButtonDeleteReferenceInfoPanelListener(individualReferenceInfo));
				
				referenceInfoPanel = new ReferenceInfoPanel();
				List<ContactInfoPanel> contactInfoPanels = referenceInfoPanel.assignValue(individualReferenceInfo);
				listContainListOfContactInfoPanel.add(contactInfoPanels);
				referenceLayout.addComponent(btnDeleteReferenceInfo);
				referenceLayout.addComponent(referenceInfoPanel);
				referenceLayout.setComponentAlignment(btnDeleteReferenceInfo, Alignment.MIDDLE_RIGHT);
				referenceInfoPanels.add(referenceInfoPanel);
				
			}
		} else {
			referenceInfoPanel = new ReferenceInfoPanel();
			referenceLayout.addComponent(referenceInfoPanel);
			referenceInfoPanels.add(referenceInfoPanel);
		}
	}
	/**
	 * set listener to button delete referenceinfo panel
	 * @author buntha.chea
	 *
	 */
	private class ButtonDeleteReferenceInfoPanelListener implements ClickListener{
		

		private static final long serialVersionUID = -2366823219939383496L;
		private IndividualReferenceInfo individualReferenceInfo;
		
		public ButtonDeleteReferenceInfoPanelListener(IndividualReferenceInfo individualReferenceInfo) {
			this.individualReferenceInfo = individualReferenceInfo;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			confirmDeleteReferenceInfo(individualReferenceInfo);
			
		}
		
	}
	/**
	 * 
	 * @param contactInfo
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
	 * @param address
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
		final DeleteInformationInApplicant deleteIndividual = new DeleteInformationInApplicant();
		deleteIndividual.setEmployment(employment);
		ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("are.you.sure.delete.financialinfo"),
		        new ConfirmDialog.Listener() {
					private static final long serialVersionUID = 2380193173874927880L;
					public void onClose(ConfirmDialog dialog) {
		                if (dialog.isConfirmed()) {
		                	Employment employment = deleteIndividual.getEmployment();
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
	 * @param individualReferenceInfo
	 */
	private void confirmDeleteReferenceInfo(IndividualReferenceInfo individualReferenceInfo) {
		final DeleteInformationInApplicant deleteInformationInApplicant = new DeleteInformationInApplicant();
		deleteInformationInApplicant.setIndividualReferenceInfo(individualReferenceInfo);
		ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("are.you.sure.delete.referenceinfo"),
		        new ConfirmDialog.Listener() {
					private static final long serialVersionUID = 2380193173874927880L;
					public void onClose(ConfirmDialog dialog) {
		                if (dialog.isConfirmed()) {
		                	IndividualReferenceInfo individualReferenceInfo = deleteInformationInApplicant.getIndividualReferenceInfo();
		                	List<IndividualReferenceContactInfo> individualReferenceContactInfos = individualReferenceInfo.getIndividualReferenceContactInfos();
		                	for (IndividualReferenceContactInfo individualReferenceContactInfo : individualReferenceContactInfos) {
								ContactInfo contactInfo = individualReferenceContactInfo.getContactInfo();
								ENTITY_SRV.delete(IndividualReferenceContactInfo.class, individualReferenceContactInfo.getId());
								ENTITY_SRV.delete(ContactInfo.class, contactInfo.getId());
							}
		                	ENTITY_SRV.delete(IndividualReferenceInfo.class, individualReferenceInfo.getId());
		                	individualReferenceInfos.remove(individualReferenceInfo);
		                	assignReferenceInfoValue(applicant);
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
		private IndividualReferenceInfo individualReferenceInfo;

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

		/**
		 * @return the individualReferenceInfo
		 */
		public IndividualReferenceInfo getIndividualReferenceInfo() {
			return individualReferenceInfo;
		}

		/**
		 * @param individualReferenceInfo the individualReferenceInfo to set
		 */
		public void setIndividualReferenceInfo(IndividualReferenceInfo individualReferenceInfo) {
			this.individualReferenceInfo = individualReferenceInfo;
		}
	}
}
