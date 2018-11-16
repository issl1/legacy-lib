package com.nokor.ersys.core.finance.bank.detail;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.model.entity.CrudAction;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.nokor.common.app.eref.ECountry;
import com.nokor.ersys.core.finance.model.Bank;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.util.VaadinServicesHelper;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Bank Form Panel
 * @author bunlong.taing
 *
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class BankFormPanel extends AbstractFormPanel implements VaadinServicesHelper {
	/** */
	private static final long serialVersionUID = -1549061479114390958L;
	
	private Long entityId;
	private TextField txtDesc;
	private TextField txtDescEn;
	private TextField txtBranchName;
	private TextField txtBranchCode;
	private TextField txtAgencyCode;
	private TextField txtSwift;
	private TextField txtTel;
	private TextField txtEMail;
	private TextArea txtComment;
	
	// Address
	private TextField txtLine1;
	private TextField txtLine2;
	private TextField txtPostalCode;
	private TextField txtCity;
	private ERefDataComboBox<ECountry> cbxCountry;
	
	/**
	 * 
	 */
    @PostConstruct
	public void PostConstruct() {
        super.init();
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}
    
	/**
	 * 
	 */
	@Override
	protected Entity getEntity() {
		Bank bank = null;
		boolean isUpdate = getEntityId() != null && getEntityId() > 0;
		if (isUpdate) {
			bank = BANK_SRV.getById(Bank.class, getEntityId());
		} else {
			bank = Bank.createInstance();
		}
		buildBankDetailsFromControls(bank);
		buildAddressFromControls(bank);
		return bank;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormTabPanel#saveEntity()
	 */
	@Override
	public void saveEntity() {
		Bank bank = (Bank) getEntity();
		boolean isUpdate = bank.getId() != null && bank.getId() > 0;
		if (isUpdate) {
			setCascadeAtUpdate(bank);
			BANK_SRV.updateProcess(bank);
        } else {
        	BANK_SRV.createProcess(bank);
        	setEntityId(bank.getId());
        }
	}
    
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		Panel bankPanel = ComponentFactory.getPanel("bank.info");
		bankPanel.setStyleName("group-panel");
		bankPanel.setContent(createBankPanel());
		
		Panel addressPanel = ComponentFactory.getPanel("address");
		addressPanel.setStyleName("group-panel");
		addressPanel.setContent(createAddressPanel());
		
		Panel additionalPanel = ComponentFactory.getPanel("comment");
		additionalPanel.setStyleName("group-panel");
		additionalPanel.setContent(createAdditionalPanel());
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.setMargin(true);
		contentLayout.addComponent(bankPanel);
		contentLayout.addComponent(addressPanel);
		contentLayout.addComponent(additionalPanel);
		
		Panel panel = new Panel();
		panel.setSizeFull();
		panel.setContent(contentLayout);
		
		return panel;
	}
	
	/**
	 * create Bank Panel
	 * @return
	 */
	private Component createBankPanel() {
		txtDesc = ComponentFactory.getTextField35(false,100, 180);
		txtDescEn = ComponentFactory.getTextField( 100, 180);
		txtBranchName = ComponentFactory.getTextField(100, 180);
		txtBranchCode = ComponentFactory.getTextField(50, 180);
		txtAgencyCode = ComponentFactory.getTextField(50, 180);
		txtSwift = ComponentFactory.getTextField(50, 180);
		txtTel = ComponentFactory.getTextField(15, 180);
		txtEMail = ComponentFactory.getTextField(50, 180);
		
		String template = "bankInfo";
		InputStream layoutFile = getClass().getResourceAsStream("/VAADIN/themes/ersys/core/finance/bank/" + template + ".html");
		CustomLayout formLayout = null;
		try {
			formLayout = new CustomLayout(layoutFile);
		} catch (IOException e) {
			Notification.show("Could not locate template " + template, e.getMessage(), Type.ERROR_MESSAGE);
		}
		
		formLayout.setSizeFull();
		formLayout.addComponent(ComponentFactory.getLabel("name.en"), "lblDescEn");
		formLayout.addComponent(txtDescEn, "txtDescEn");
		
		formLayout.addComponent(ComponentFactory.getLabel("name"), "lblDesc");
		formLayout.addComponent(txtDesc, "txtDesc");
		
		formLayout.addComponent(ComponentFactory.getLabel("branch.name"), "lblBranchName");
		formLayout.addComponent(txtBranchName, "txtBranchName");
		
		formLayout.addComponent(ComponentFactory.getLabel("branch.code"), "lblBranchCode");
		formLayout.addComponent(txtBranchCode, "txtBranchCode");
		
		formLayout.addComponent(ComponentFactory.getLabel("agency.code"), "lblAgencyCode");
		formLayout.addComponent(txtAgencyCode, "txtAgencyCode");
		
		formLayout.addComponent(ComponentFactory.getLabel("swift"), "lblSwift");
		formLayout.addComponent(txtSwift, "txtSwift");
		
		formLayout.addComponent(ComponentFactory.getLabel("tel"), "lblTel");
		formLayout.addComponent(txtTel, "txtTel");
		
		formLayout.addComponent(ComponentFactory.getLabel("email"), "lblEMail");
		formLayout.addComponent(txtEMail, "txtEMail");
		
		return formLayout;
	}
	
	/**
	 * create Address Panel
	 * @return
	 */
	private Component createAddressPanel() {
		txtLine1 = ComponentFactory.getTextField(50, 280);
		txtLine2 = ComponentFactory.getTextField( 50, 280);
		txtPostalCode = ComponentFactory.getTextField( 50, 180);
		txtCity = ComponentFactory.getTextField( 50, 180);

		cbxCountry = new ERefDataComboBox<ECountry>(ECountry.values());
		cbxCountry.setWidth(180, Unit.PIXELS);
		
		String template = "bankAddress";
		InputStream layoutFile = getClass().getResourceAsStream("/VAADIN/themes/ersys/core/finance/bank/" + template + ".html");
		CustomLayout formLayout = null;
		try {
			formLayout = new CustomLayout(layoutFile);
		} catch (IOException e) {
			Notification.show("Could not locate template " + template, e.getMessage(), Type.ERROR_MESSAGE);
		}
		
		formLayout.setSizeFull();
		formLayout.addComponent(ComponentFactory.getLabel("line1"), "lblLine1");
		formLayout.addComponent(txtLine1, "txtLine1");
		
		formLayout.addComponent(ComponentFactory.getLabel("line2"), "lblLine2");
		formLayout.addComponent(txtLine2, "txtLine2");
		
		formLayout.addComponent(ComponentFactory.getLabel("postal.code"), "lblPostalCode");
		formLayout.addComponent(txtPostalCode, "txtPostalCode");
		
		formLayout.addComponent(ComponentFactory.getLabel("city"), "lblCity");
		formLayout.addComponent(txtCity, "txtCity");
		
		formLayout.addComponent(ComponentFactory.getLabel("country"), "lblCountry");
		formLayout.addComponent(cbxCountry, "cbxCountry");

		return formLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private Component createAdditionalPanel() {
		txtComment = ComponentFactory.getTextArea(false,300, 100);
		
		String template = "bankAdditionalInfo";
		InputStream layoutFile = getClass().getResourceAsStream("/VAADIN/themes/ersys/core/finance/bank/" + template + ".html");
		CustomLayout formLayout = null;
		try {
			formLayout = new CustomLayout(layoutFile);
		} catch (IOException e) {
			Notification.show("Could not locate template " + template, e.getMessage(), Type.ERROR_MESSAGE);
		}
		
		formLayout.setSizeFull();
		formLayout.addComponent(ComponentFactory.getLabel("comment"), "lblComment");
		formLayout.addComponent(txtComment, "txtComment");
		
		return formLayout;
		
	}
	
	/**
	 * @param banInfId
	 */
	public void assignValues(Long banInfId) {
		if (banInfId != null) {
			reset();
			setEntityId(banInfId);
			Bank bank = BANK_SRV.getById(Bank.class, banInfId);
			// Bank Info
			txtDesc.setValue(bank.getDesc());
			txtDescEn.setValue(bank.getDescEn());
			txtBranchName.setValue(bank.getName());
			txtBranchCode.setValue(bank.getCode());
			txtAgencyCode.setValue(bank.getAgencyCode());
			txtSwift.setValue(bank.getSwift());
			txtTel.setValue(bank.getTel());
			txtEMail.setValue(bank.getEmail());
			txtComment.setValue(bank.getComment());
			// address
			Address address = bank.getAddress();
			if (address != null) {
				txtLine1.setValue(address.getLine1());
				txtLine2.setValue(address.getLine2());
				txtPostalCode.setValue(address.getPostalCode());
				txtCity.setValue(address.getCity());
				cbxCountry.setSelectedEntity(address.getCountry());
			}
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormTabPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		setEntityId(null);
		// Bank Info
		txtDesc.setValue("");
		txtDescEn.setValue("");
		txtBranchName.setValue("");
		txtBranchCode.setValue("");
		txtAgencyCode.setValue("");
		txtSwift.setValue("");
		txtTel.setValue("");
		txtEMail.setValue("");
		txtComment.setValue("");
		// Address
		txtLine1.setValue("");
		txtLine2.setValue("");
		txtPostalCode.setValue("");
		txtCity.setValue("");
		cbxCountry.setSelectedEntity(ECountry.KHM);
	}
	
	/**
	 * Set Cascade At Update
	 * @param employee
	 */
	private void setCascadeAtUpdate(Bank bank) {
		if (bank.getAddress().getId() != null) {
			bank.addSubEntityToCascade(bank.getAddress(), CrudAction.UPDATE);
		}
    }
	
	/**
	 * Build Bank Details From Controls
	 * @param bank
	 */
	private void buildBankDetailsFromControls(Bank bank) {
		bank.setDesc(txtDesc.getValue());
		bank.setDescEn(txtDescEn.getValue());
		bank.setName(txtBranchName.getValue());
		bank.setCode(txtBranchCode.getValue());
		bank.setAgencyCode(txtAgencyCode.getValue());
		bank.setSwift(txtSwift.getValue());
		bank.setTel(txtTel.getValue());
		bank.setEmail(txtEMail.getValue());
		bank.setComment(txtComment.getValue());
	}
	
	/**
	 * Build Address From Controls
	 * @param bank
	 */
	private void buildAddressFromControls(Bank bank) {
		Address address = bank.getAddress();
		if (address == null) {
			address = Address.createInstance();
			bank.setAddress(address);
			bank.addCascadeAtCreation(address);
		}
		address.setLine1(StringUtils.trim(txtLine1.getValue()));
		address.setLine2(StringUtils.trim(txtLine2.getValue()));
		address.setPostalCode(StringUtils.trim(txtPostalCode.getValue()));
		address.setCity(StringUtils.trim(txtCity.getValue()));
		address.setCountry(cbxCountry.getSelectedEntity());	
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormTabPanel#validate()
	 */
	@Override
	protected boolean validate() {
		checkMandatoryField(txtDescEn, "desc.en");
		checkMandatoryField(txtDesc, "desc");
		checkMandatoryField(txtLine1, "line1");
		checkMandatoryField(txtPostalCode, "postal.code");
		checkMandatorySelectField(cbxCountry, "country");
		
		return errors.isEmpty();
	}

	/**
	 * @return the entityId
	 */
	public Long getEntityId() {
		return entityId;
	}

	/**
	 * @param entityId the entityId to set
	 */
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}
	
}
