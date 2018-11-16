package com.nokor.efinance.core.bank;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.nokor.ersys.core.finance.model.Bank;
import com.nokor.ersys.core.hr.model.organization.OrgBankAccount;
import com.nokor.frmk.vaadin.ui.layout.LayoutHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;

/**
 * Bank Account Panel
 * @author bunlong.taing
 */
public class BankAccountPanel extends AbstractControlPanel {
	/** */
	private static final long serialVersionUID = -5120368391530408221L;
	
	private static final String TEMPLATE_NAME = "organization/" + "bankAccount";
	
	private TextField txtNamePayee;
	private TextField txtAccountNo;
	private EntityComboBox<Bank> cbxBankName;
	private TextField txtBranchName;
	
	/**
	 * 
	 */
	public BankAccountPanel() {
		createForm();
	}

	/**
	 * Create Form
	 */
	private void createForm() {
		txtNamePayee = ComponentFactory.getTextField(100, 200);
		txtAccountNo = ComponentFactory.getTextField(100, 200);
		txtBranchName = ComponentFactory.getTextField(100, 200);
		txtBranchName.setEnabled(false);
		cbxBankName = new EntityComboBox<Bank>(Bank.class, Bank.DESCEN);
		cbxBankName.setWidth(200, Unit.PIXELS);
		cbxBankName.renderer();
		cbxBankName.addValueChangeListener(new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = -1780077794925153786L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				Bank bank = cbxBankName.getSelectedEntity();
				if (bank != null && bank.getAgencyCode() != null) {
					txtBranchName.setValue(bank.getAgencyCode());
				} else {
					txtBranchName.setValue("");
				}
			}
		});
		
		CustomLayout formLayout = LayoutHelper.createCustomLayout(TEMPLATE_NAME);
		if (formLayout == null) {
			Notification.show("Could not locate template " + LayoutHelper.getTemplateFullPath(TEMPLATE_NAME), Type.ERROR_MESSAGE);
		}
		
		formLayout.addComponent(ComponentFactory.getLabel("name.payee"), "lblNamePayee");
		formLayout.addComponent(txtNamePayee, "txtNamePayee");
		
		formLayout.addComponent(ComponentFactory.getLabel("account.no"), "lblAccountNo");
		formLayout.addComponent(txtAccountNo, "txtAccountNo");
		
		formLayout.addComponent(ComponentFactory.getLabel("bank.name"), "lblBankName");
		formLayout.addComponent(cbxBankName, "cbxBankName");
		
		formLayout.addComponent(ComponentFactory.getLabel("branch.name"), "lblBranchName");
		formLayout.addComponent(txtBranchName, "txtBranchName");
		
		addComponent(formLayout);
	}
	
	/**
	 * Reset the form
	 */
	public void reset() {
		txtNamePayee.setValue("");
		txtAccountNo.setValue("");
		txtBranchName.setValue("");
		cbxBankName.setSelectedEntity(null);
	}
	
	/**
	 * Get Bank Account
	 * @param bankAccount
	 * @return
	 */
	public OrgBankAccount getBankAccount(OrgBankAccount bankAccount) {
		bankAccount.setAccountHolder(txtNamePayee.getValue());
		bankAccount.setAccountNumber(txtAccountNo.getValue());
		bankAccount.setBank(cbxBankName.getSelectedEntity());
		return bankAccount;
	}
	
	/**
	 * Assign values to form
	 * @param bankAccount
	 */
	public void assignValues(OrgBankAccount bankAccount) {
		txtNamePayee.setValue(getDefaultString(bankAccount.getAccountHolder()));
		txtAccountNo.setValue(getDefaultString(bankAccount.getAccountNumber()));
		cbxBankName.setSelectedEntity(bankAccount.getBank());
	}
	
	/**
	 * Validate Bank Account
	 * @return
	 */
	public boolean validateBankAccount() {
		boolean isValid = false;
		
		if (StringUtils.isNotEmpty(txtNamePayee.getValue())
				|| StringUtils.isNotEmpty(txtAccountNo.getValue())
				|| cbxBankName.getSelectedEntity() != null) {
			isValid = true;
		}
		
		return isValid;
	}
	
	/**
	 * Validate
	 * @return
	 */
	public List<String> validate() {
		super.reset();
		if (StringUtils.isNotEmpty(txtNamePayee.getValue())
				|| StringUtils.isNotEmpty(txtAccountNo.getValue())) {
			checkMandatorySelectField(cbxBankName, "bank.name");
		}
		return errors;
	}

}
