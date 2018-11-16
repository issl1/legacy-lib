package com.nokor.efinance.gui.ui.panel.collection.oa;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.address.panel.AddressPanel;
import com.nokor.efinance.core.bank.BankAccountPanel;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.eref.ESubTypeOrganization;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;
import com.nokor.ersys.core.hr.model.eref.ETypeBankAccount;
import com.nokor.ersys.core.hr.model.organization.OrgAddress;
import com.nokor.ersys.core.hr.model.organization.OrgBankAccount;
import com.nokor.ersys.core.hr.model.organization.Organization;
import com.nokor.ersys.core.hr.model.organization.OrganizationSubTypes;
import com.nokor.ersys.core.hr.model.organization.OrganizationTypes;
import com.nokor.frmk.vaadin.ui.layout.LayoutHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;


/**
 * 
 * @author uhout.cheng
 */
public class OutsourceAgencyFormPanel extends AbstractFormPanel implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = -5696873583967689682L;
	
	private static final String ADDRESS_TEMPLATE = "organization/organizationAddress";
	private static final String TEMPLATE_NAME = "agent/" + "companyDetail";
	
	private OutsourceAgencyTablePanel mainPanel;
	
	private Long entityId;
	private TextField txtCode;
	private TextField txtName;
	private TextField txtNameEn;
	private TextField txtCommercialNo;
	private RichTextArea txtDescEn;
	
	private ERefDataComboBox<ESubTypeOrganization> cbxSubTypeOrganization;
	
	private AutoDateField dfStartDate;
	private AutoDateField dfDateAdded;
	
	private AddressPanel addressPanel;
	private AddressPanel mailAddressPanel;
	
	private BankAccountPanel lostInsuranceAccount;
	private BankAccountPanel aomInsuranceAccount;
	
	/**
	 * 
	 * @param mainPanel
	 */
   	public OutsourceAgencyFormPanel(OutsourceAgencyTablePanel mainPanel) {
   		this.mainPanel = mainPanel;
        super.init();
        setCaption(I18N.message("outsource.agent"));
        NavigationPanel navigationPanel = addNavigationPanel();
   		navigationPanel.addSaveClickListener(this);
   	}
	
	/**
	 * 
	 */
	@Override
	protected Entity getEntity() {
		Organization comp = null;
		boolean isUpdate = getEntityId() != null && getEntityId() > 0;
		if (isUpdate) {
			comp = ORG_SRV.getById(Organization.class, getEntityId());
		} else {
			comp = Organization.createInstance();
			comp.setTypeOrganization(OrganizationTypes.AGENT);
		}
		buildCompanyDetailsFromControls(comp);
		return comp;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormTabPanel#saveEntity()
	 */
	@Override
	public void saveEntity() {
		Organization comp = (Organization) getEntity();
		boolean isUpdate = comp.getId() != null && comp.getId() > 0;
		if (isUpdate) {
			ORG_SRV.updateProcess(comp);
        } else {
        	COL_SRV.createOrganizationWithDefaultUser(comp);        	
        	setEntityId(comp.getId());
        }
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormTabPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		addressPanel = new AddressPanel(true, ETypeAddress.MAIN, ADDRESS_TEMPLATE);
		mailAddressPanel = new AddressPanel(true, ETypeAddress.MAILADDRESS, ADDRESS_TEMPLATE);
		lostInsuranceAccount = new BankAccountPanel();
		aomInsuranceAccount = new BankAccountPanel();		
		
		// Address
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		horizontalLayout.setMargin(true);
		
		Panel panel = new Panel();
		panel.setCaption(I18N.message("address"));
		panel.setContent(addressPanel);
		horizontalLayout.addComponent(panel);
		
		panel = new Panel();
		panel.setCaption(I18N.message("mail.address"));
		panel.setContent(mailAddressPanel);		
		horizontalLayout.addComponent(panel);
		
		VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.setSpacing(true);
        verticalLayout.addComponent(createCompanyPanel());
        verticalLayout.addComponent(horizontalLayout);
        
        // Bank Account
 		if (mainPanel != null) {
 			HorizontalLayout bankAccountLayout = new HorizontalLayout();
 			bankAccountLayout.setSpacing(true);
 			bankAccountLayout.setMargin(true);
 			
 			panel = new Panel();
 			panel.setCaption(I18N.message("wage"));
 			panel.setContent(lostInsuranceAccount);
 			bankAccountLayout.addComponent(panel);
 			
 			panel = new Panel();
 			panel.setCaption(I18N.message("payment.details"));
 			panel.setContent(bankAccountLayout);
 			
 			HorizontalLayout paymentDetailLayout = new HorizontalLayout();
 			paymentDetailLayout.setMargin(true);
 			paymentDetailLayout.addComponent(panel);
 			
 			verticalLayout.addComponent(paymentDetailLayout);
 		}
        
        Panel mainPanel = ComponentFactory.getPanel();        
		mainPanel.setContent(verticalLayout);        
		return mainPanel;
	}
	
	/**
	 * Create Branch Panel
	 * @return
	 */
	private com.vaadin.ui.Component createCompanyPanel() {
		txtCode = ComponentFactory.getTextField(50, 180);
		txtName = ComponentFactory.getTextField35(false,50, 180);
		txtNameEn = ComponentFactory.getTextField(50, 180);
		txtCommercialNo = ComponentFactory.getTextField(50, 180);
		txtDescEn = new RichTextArea();
		
		dfStartDate = new AutoDateField();
		dfStartDate.setValue(DateUtils.today());
		dfDateAdded = new AutoDateField();
		dfDateAdded.setValue(DateUtils.today());
		
		cbxSubTypeOrganization = new ERefDataComboBox<ESubTypeOrganization>(ESubTypeOrganization.values(OrganizationTypes.AGENT));
		cbxSubTypeOrganization.setWidth(180, Unit.PIXELS);
		cbxSubTypeOrganization.setEnabled(!ProfileUtil.isColOASupervisor());
		if (ProfileUtil.isColOASupervisor()) {
			cbxSubTypeOrganization.setSelectedEntity(OrganizationSubTypes.OUTSOURCE_AGENT);
		}
		
		String templateName = TEMPLATE_NAME;
		CustomLayout formLayout = LayoutHelper.createCustomLayout(templateName);
		if (formLayout == null) {
			Notification.show("Could not locate template " + LayoutHelper.getTemplateFullPath(templateName), Type.ERROR_MESSAGE);
		}
		
		formLayout.setSizeFull();
		formLayout.addComponent(ComponentFactory.getLabel("code"), "lblCode");
		formLayout.addComponent(txtCode, "txtCode");
		
		formLayout.addComponent(ComponentFactory.getLabel("name.en"), "lblNameEn");
		formLayout.addComponent(txtNameEn, "txtNameEn");
		
		formLayout.addComponent(ComponentFactory.getLabel("name"), "lblName");
		formLayout.addComponent(txtName, "txtName");
		
		formLayout.addComponent(ComponentFactory.getLabel("opening.date"), "lblStartDate");
		formLayout.addComponent(dfStartDate, "dfStartDate");
		
		formLayout.addComponent(ComponentFactory.getLabel("date.added"), "lblDateAdded");
		formLayout.addComponent(dfDateAdded, "dfDateAdded");
		
		formLayout.addComponent(ComponentFactory.getLabel("commercial.no"), "lblCommercialNo");
		formLayout.addComponent(txtCommercialNo, "txtCommercialNo");
		
		formLayout.addComponent(ComponentFactory.getLabel("desc.en"), "lblDescEn");
		formLayout.addComponent(txtDescEn, "txtDescEn");
		
		formLayout.addComponent(ComponentFactory.getLabel("type"), "lblSubTypeOrganization");
		formLayout.addComponent(cbxSubTypeOrganization, "cbxSubTypeOrganization");
		
		return formLayout;
	}
	
	/**
	 * assign Values
	 * @param branch
	 */
	public void assignValues(Long entityId) {
		reset();
		if (entityId != null) {
			setEntityId(entityId);
			Organization comp = ENTITY_SRV.getById(Organization.class, entityId);
			txtCode.setValue(comp.getCode() != null ? comp.getCode() : "");
			txtName.setValue(comp.getName() != null ? comp.getName() : "");
			txtNameEn.setValue(comp.getNameEn() != null ? comp.getNameEn() : "");
			txtCommercialNo.setValue(comp.getLicenceNo() != null ? comp.getLicenceNo() : "");
			txtDescEn.setValue(comp.getDescEn() != null ? comp.getDescEn() : "");
			dfStartDate.setValue(comp.getStartDate());
			dfDateAdded.setValue(comp.getCreateDate());
			
			cbxSubTypeOrganization.setSelectedEntity(comp.getSubTypeOrganization());
			
			List<OrgAddress> orgAddresses = comp.getOrgAddresses();
			if (!orgAddresses.isEmpty()) {
				for (OrgAddress orgAddress : orgAddresses) {
					Address address = orgAddress.getAddress();
					if (ETypeAddress.MAIN.equals(address.getType())) {
						addressPanel.assignValues(address);
					} else if (ETypeAddress.MAILADDRESS.equals(address.getType())) {
						mailAddressPanel.assignValues(address);
					}
				}
			}
			
			List<OrgBankAccount> orgBankAccounts = comp.getOrgBankAccounts();
			if (!orgBankAccounts.isEmpty()) {
				for (OrgBankAccount orgBankAccount : orgBankAccounts) {
					if (ETypeBankAccount.MAIN.equals(orgBankAccount.getTypeBankAccount())) {
						lostInsuranceAccount.assignValues(orgBankAccount);
					} 
				}
			}
		}
	}
	
	/**
	 * Build Company Details From Controls
	 * @param org
	 */
	private void buildCompanyDetailsFromControls(Organization org) {
		org.setCode(txtCode.getValue());
		org.setName(txtName.getValue());
		org.setNameEn(txtNameEn.getValue());
		org.setLicenceNo(txtCommercialNo.getValue());
		org.setDescEn(txtDescEn.getValue());
		org.setStartDate(dfStartDate.getValue());
		org.setCreateDate(dfDateAdded.getValue());
		
		org.setSubTypeOrganization(cbxSubTypeOrganization.getSelectedEntity());
		
		List<OrgAddress> orgAddresses = org.getOrgAddresses();
		if (orgAddresses == null) {
			orgAddresses = new ArrayList<OrgAddress>();
			org.setOrgAddresses(orgAddresses);
		}
		OrgAddress address = getOrgAddress(org, ETypeAddress.MAIN);
		address.setAddress(addressPanel.getAddress(address.getAddress()));
		if (!(address.getId() != null && address.getId() > 0) && addressPanel.validateAddress()) {
			orgAddresses.add(address);
		}
		address.getAddress().setType(ETypeAddress.MAIN);
		
		address = getOrgAddress(org, ETypeAddress.MAILADDRESS);
		address.setAddress(mailAddressPanel.getAddress(address.getAddress()));
		if (!(address.getId() != null && address.getId() > 0) && mailAddressPanel.validateAddress()) {
			orgAddresses.add(address);
		}
		address.getAddress().setType(ETypeAddress.MAILADDRESS);
		
		List<OrgBankAccount> orgBankAccounts = org.getOrgBankAccounts();
		if (orgBankAccounts == null) {
			orgBankAccounts = new ArrayList<OrgBankAccount>();
			org.setOrgBankAccounts(orgBankAccounts);
		}
		OrgBankAccount orgBankAccount = getOrgBankAccount(org, ETypeBankAccount.MAIN);
		lostInsuranceAccount.getBankAccount(orgBankAccount);
		if (!(orgBankAccount.getId() != null && orgBankAccount.getId() > 0) && lostInsuranceAccount.validateBankAccount()) {
			orgBankAccounts.add(orgBankAccount);
		}
	}
	
	/**
	 * Get OrgAddress
	 * @param org
	 * @param typeAddress
	 * @return
	 */
	private OrgAddress getOrgAddress(Organization org, ETypeAddress typeAddress) {
		List<OrgAddress> orgAddresses = org.getOrgAddresses();
		OrgAddress address = null;
		if (orgAddresses != null && !orgAddresses.isEmpty()) {
			for (OrgAddress orgAddress : orgAddresses) {
				if (typeAddress.equals(orgAddress.getAddress().getType())) {
					address = orgAddress;
				}
			}
		}
		if (address == null) {
			address = new OrgAddress();
			Address addr = new Address();
			address.setOrganization(org);
			address.setAddress(addr);
		}
		return address;
	}
	
	/**
	 * Get Org Bank Account
	 * @param org
	 * @param typeBankAccount
	 * @return
	 */
	private OrgBankAccount getOrgBankAccount(Organization org, ETypeBankAccount typeBankAccount) {
		List<OrgBankAccount> orgBankAccounts = org.getOrgBankAccounts();
		OrgBankAccount orgBankAccount = null;
		if (orgBankAccounts != null) {
			for (OrgBankAccount bankAccount : orgBankAccounts) {
				if (typeBankAccount.equals(bankAccount.getTypeBankAccount())) {
					orgBankAccount = bankAccount;
				}
			}
		}
		if (orgBankAccount == null) {
			orgBankAccount = new OrgBankAccount();
			orgBankAccount.setTypeBankAccount(typeBankAccount);
			orgBankAccount.setOrganization(org);
		}
		return orgBankAccount;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormTabPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		setEntityId(null);
		txtCode.setValue("");
		txtName.setValue("");
		txtNameEn.setValue("");
		txtCommercialNo.setValue("");
		txtDescEn.setValue("");
		cbxSubTypeOrganization.setSelectedEntity(OrganizationSubTypes.OUTSOURCE_AGENT);
		dfStartDate.setValue(DateUtils.today());
		dfDateAdded.setValue(DateUtils.today());
		addressPanel.reset();
		mailAddressPanel.reset();
		lostInsuranceAccount.reset();
		aomInsuranceAccount.reset();
	}
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormTabPanel#validate()
	 */
	@Override
	protected boolean validate() {
		checkMandatoryField(txtCode, "code");
		checkMandatoryField(txtNameEn, "name.en");
		checkMandatoryField(txtName, "name");
		checkMandatorySelectField(cbxSubTypeOrganization, "sub.type.organization");
		errors.addAll(lostInsuranceAccount.validate());
		return errors.isEmpty();
	}

	/**
	 * @return the entityId
	 */
	private Long getEntityId() {
		return entityId;
	}

	/**
	 * @param entityId the entityId to set
	 */
	private void setEntityId(Long entityId) {
		this.entityId = entityId;
	}
	
}
