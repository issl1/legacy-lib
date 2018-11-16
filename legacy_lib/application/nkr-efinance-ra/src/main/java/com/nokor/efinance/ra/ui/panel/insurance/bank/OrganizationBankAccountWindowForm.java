package com.nokor.efinance.ra.ui.panel.insurance.bank;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Order;
import org.seuksa.frmk.i18n.I18N;

import com.gl.finwiz.share.domain.AP.AccountHolderDTO;
import com.gl.finwiz.share.domain.AP.BankAccountDTO;
import com.gl.finwiz.share.domain.AP.BankDTO;
import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.widget.BankBranchComboBox;
import com.nokor.efinance.core.widget.BankComboBox;
import com.nokor.efinance.core.widget.OrgAccountHolderComboBox;
import com.nokor.efinance.third.finwiz.client.ap.ClientAccountHolder;
import com.nokor.efinance.third.finwiz.client.ap.ClientBankAccount;
import com.nokor.efinance.third.finwiz.client.bank.ClientBank;
import com.nokor.ersys.core.hr.model.organization.OrgAccountHolder;
import com.nokor.ersys.core.hr.model.organization.OrgBankAccount;
import com.nokor.ersys.core.hr.model.organization.Organization;
import com.nokor.ersys.core.hr.service.OrgAccountHolderRestriction;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;


/**
 * 
 * @author uhout.cheng
 */
public class OrganizationBankAccountWindowForm extends Window implements FinServicesHelper {
	
	/** */
	private static final long serialVersionUID = 2317786252169588457L;
	
	private TextField txtAccountName;
	private TextField txtPayeeAccountName;
	private TextField txtSourceAccountName;
	private OrgAccountHolderComboBox cbxAccountHolder;
	private BankComboBox cbxBank;
	private BankBranchComboBox cbxBankBranch;
	private CheckBox cbActive;
	
	/**
	 * 
	 * @param org
	 * @param bankAccId
	 * @param deleget
	 */
	public OrganizationBankAccountWindowForm(Organization org, Long bankAccId, OrganizationBankAccountTable deleget) {
		setCaption(I18N.message("bank.accounts"));
		setModal(true);
		setResizable(false);
						
		txtAccountName = ComponentFactory.getTextField(180, 180);
		txtPayeeAccountName = ComponentFactory.getTextField(180, 180);
		txtSourceAccountName = ComponentFactory.getTextField(180, 180);
		cbActive = new CheckBox(I18N.message("active"));
		
		cbxAccountHolder = new OrgAccountHolderComboBox(ORG_SRV.list(getRestrictions(org)));
		cbxAccountHolder.setWidth(180, Unit.PIXELS);
		cbxAccountHolder.setImmediate(true);

		BankAccountDTO bankAccountDTO = null;
		if (bankAccId != null) {
			bankAccountDTO = ClientBankAccount.getBankAccountById(bankAccId);
		}
		
		List<BankDTO> banks = ClientBank.getBankDTOs();
		cbxBank = new BankComboBox(banks);
		cbxBank.setWidth(180, Unit.PIXELS);
		
		cbxBankBranch = new BankBranchComboBox();
		cbxBankBranch.setWidth(180, Unit.PIXELS);
		
		cbxBank.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = -2497943553010152386L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxBank.getSelectedEntity() != null) {
					cbxBankBranch.setBankBranches(ClientBank.getBankBranchsByBankId(cbxBank.getSelectedEntity().getId()));
				} else {
					cbxBankBranch.setBankBranches(ClientBank.getBankBranchDTOs());
				}
			}
		});
		
		SecUser secUser = UserSessionManager.getCurrentUser();
		
		if (bankAccountDTO != null) {
			txtAccountName.setValue(bankAccountDTO.getAccountName());
			txtPayeeAccountName.setValue(bankAccountDTO.getPayeeAccountNumber());
			txtSourceAccountName.setValue(bankAccountDTO.getSourceAccountNumber());
			if (bankAccountDTO.getAccountHolder() != null && bankAccountDTO.getAccountHolder().getId() != null) {
				cbxAccountHolder.setSelectedEntity(getOrgAccountHolder(bankAccountDTO.getAccountHolder().getId()));
			}
			cbActive.setValue(bankAccountDTO.getIsActive());
			if (bankAccountDTO.getBankBranch() != null && bankAccountDTO.getBankBranch().getBank() != null) {
				cbxBank.setSelectedEntity(bankAccountDTO.getBankBranch().getBank());
				cbxBankBranch.setSelectedEntity(bankAccountDTO.getBankBranch());
			} else {
				cbxBank.setSelectedEntity(banks != null && !banks.isEmpty() ? banks.get(0) : null);
				cbxBankBranch.setSelectedEntity(null);
			}
		} else {
			cbxBank.setSelectedEntity(banks != null && !banks.isEmpty() ? banks.get(0) : null);
		}
      
        Button btnSave = new NativeButton(I18N.message("save"), new Button.ClickListener() {

			/** */
			private static final long serialVersionUID = 4041938195486050785L;

			/**
        	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
        	 */
        	@Override
			public void buttonClick(ClickEvent event) {
				if (getErrors().isEmpty()) {
					BankAccountDTO bankAccountDTO = null;
					if (bankAccId == null) {
						bankAccountDTO = new BankAccountDTO();
						bankAccountDTO.setCreatedBy(secUser.getLogin());
						bankAccountDTO = getBankAccountDTO(bankAccountDTO);
						try {
							BankAccountDTO newBankAcc = ClientBankAccount.getBankAccountCreate(bankAccountDTO);
							try {
								OrgBankAccount orgBankAccount = OrgBankAccount.createInstance();
								orgBankAccount.setOrganization(org);
								orgBankAccount.setBankAccount(newBankAcc.getId());
								DEA_SRV.create(orgBankAccount);
							} catch (Exception e) {
								ComponentLayoutFactory.displayErrorMsg(e.getMessage());
							}
						} catch (Exception e) {
							e.printStackTrace();
							ComponentLayoutFactory.displayErrorMsg("Error while create new bank account. [" + txtPayeeAccountName.getValue() + "]");
						}
					} else {
						bankAccountDTO = ClientBankAccount.getBankAccountById(bankAccId);
						bankAccountDTO.setUpdatedBy(secUser.getLogin());
						bankAccountDTO = getBankAccountDTO(bankAccountDTO);
						try {
							ClientBankAccount.setBankAccountUpdate(bankAccId, bankAccountDTO);
						} catch (Exception e) {
							e.printStackTrace();
							ComponentLayoutFactory.displayErrorMsg("Error while update bank account. [" + txtPayeeAccountName.getValue() + "]");
						}
					}
					close();
					deleget.assignValues(org);
				} else {
					ComponentLayoutFactory.displayErrorMsg(getErrors().get(0).toString());
				}
            }
        });
		btnSave.setIcon(FontAwesome.SAVE);
		
		Button btnCancel = new NativeButton(I18N.message("close"), new Button.ClickListener() {

			/** */
			private static final long serialVersionUID = -4184040875080497208L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
            	close();
            }
        });
		btnCancel.setIcon(FontAwesome.BAN);
		
		VerticalLayout contentLayout = new VerticalLayout(); 
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		
		GridLayout gridLayout = new GridLayout(2, 7);
		gridLayout.setSpacing(true);
		gridLayout.setMargin(true);
		
		Label lblAccountName = ComponentFactory.getHtmlLabel(I18N.message("account.name") + StringUtils.SPACE + "<b style=\"color: red\">*</b>");
		Label lblPayeeAccountName = ComponentFactory.getHtmlLabel(I18N.message("payee.account.number") + StringUtils.SPACE + "<b style=\"color: red\">*</b>");
		Label lblSourceAccountName = ComponentFactory.getLabel("source.account.number");
		Label lblAccountHolder = ComponentFactory.getHtmlLabel(I18N.message("account.holder") + StringUtils.SPACE + "<b style=\"color: red\">*</b>");
		Label lblBank = ComponentFactory.getLabel("bank");
		Label lblBankBranch = ComponentFactory.getHtmlLabel(I18N.message("bank.branch") + StringUtils.SPACE + "<b style=\"color: red\">*</b>");
		gridLayout.addComponent(lblAccountName);
		gridLayout.addComponent(txtAccountName);
		gridLayout.addComponent(lblPayeeAccountName);
		gridLayout.addComponent(txtPayeeAccountName);
		gridLayout.addComponent(lblSourceAccountName);
		gridLayout.addComponent(txtSourceAccountName);
		gridLayout.addComponent(lblAccountHolder);
		gridLayout.addComponent(cbxAccountHolder);
		gridLayout.addComponent(lblBank);
		gridLayout.addComponent(cbxBank);
		gridLayout.addComponent(lblBankBranch);
		gridLayout.addComponent(cbxBankBranch);
		gridLayout.addComponent(cbActive, 1, 6);
		
		gridLayout.setComponentAlignment(lblAccountName, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblPayeeAccountName, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblSourceAccountName, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblAccountHolder, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblBank, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblBankBranch, Alignment.MIDDLE_LEFT);
		
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(gridLayout);
        
        setContent(contentLayout);
	}
	
	/**
	 * 
	 * @param bankAccountDTO
	 * @return
	 */
	private BankAccountDTO getBankAccountDTO(BankAccountDTO bankAccountDTO) {
		bankAccountDTO.setAccountName(txtAccountName.getValue());
		bankAccountDTO.setPayeeAccountNumber(txtPayeeAccountName.getValue());
		bankAccountDTO.setSourceAccountNumber(txtSourceAccountName.getValue());
		bankAccountDTO.setIsActive(cbActive.getValue());
		AccountHolderDTO accountHolderDTO = null;
		if (cbxAccountHolder.getSelectedEntity() != null) {
			accountHolderDTO = ClientAccountHolder.getAccountHolderById(cbxAccountHolder.getSelectedEntity().getAccountHolder());
		}
		bankAccountDTO.setAccountHolder(accountHolderDTO);
		bankAccountDTO.setBankBranch(cbxBankBranch.getSelectedEntity());
		return bankAccountDTO;
	}
	
	/**
	 * 
	 * @param org
	 * @return
	 */
	private OrgAccountHolderRestriction getRestrictions(Organization org) {
		OrgAccountHolderRestriction restrictions = new OrgAccountHolderRestriction();
		restrictions.setOrganization(org);
		return restrictions;
	}
	
	/**
	 * 
	 * @param accHolderId
	 * @return
	 */
	private OrgAccountHolder getOrgAccountHolder(Long accHolderId) {
		OrgAccountHolderRestriction restrictions = new OrgAccountHolderRestriction();
		restrictions.setAccountHolderId(accHolderId);
		restrictions.addOrder(Order.desc(OrgAccountHolder.ID));
		return ENTITY_SRV.list(restrictions).isEmpty() ? null : ENTITY_SRV.list(restrictions).get(0);
	}
	
	/**
	 * 
	 * @return
	 */
	private List<String> getErrors() {
		List<String> errors = new ArrayList<String>();
		if (StringUtils.isEmpty(txtAccountName.getValue())) {
			txtAccountName.focus();
			errors.add(I18N.message("field.required.1", new String[] { I18N.message("account.name") }));
		} else if (StringUtils.isEmpty(txtPayeeAccountName.getValue())) {
			txtPayeeAccountName.focus();
			errors.add(I18N.message("field.required.1", new String[] { I18N.message("payee.account.number") }));
		} else if (cbxAccountHolder.getSelectedEntity() == null) {
			cbxAccountHolder.focus();
			errors.add(I18N.message("field.required.1", new String[] { I18N.message("account.holder") }));
		} else if (cbxBankBranch.getSelectedEntity() == null) {
			cbxBankBranch.focus();
			errors.add(I18N.message("field.required.1", new String[] { I18N.message("bank.branch") }));
		}
		return errors;
	}
	
}
