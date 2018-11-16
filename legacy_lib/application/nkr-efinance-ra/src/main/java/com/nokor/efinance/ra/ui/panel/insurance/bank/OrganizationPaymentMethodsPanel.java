package com.nokor.efinance.ra.ui.panel.insurance.bank;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.dealer.model.DealerAccountHolder;
import com.nokor.efinance.core.dealer.model.DealerBankAccount;
import com.nokor.efinance.core.organization.model.OrgPaymentMethod;
import com.nokor.efinance.core.payment.model.EPaymentFlowType;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.core.widget.OrgAccountHolderComboBox;
import com.nokor.efinance.core.widget.OrgBankAccountComboBox;
import com.nokor.ersys.core.hr.model.organization.OrgAccountHolder;
import com.nokor.ersys.core.hr.model.organization.OrgBankAccount;
import com.nokor.ersys.core.hr.model.organization.Organization;
import com.nokor.ersys.core.hr.service.OrgAccountHolderRestriction;
import com.nokor.ersys.core.hr.service.OrgBankAccountRestriction;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author uhout.cheng
 */
public class OrganizationPaymentMethodsPanel extends AbstractTabPanel {
	
	/** */
	private static final long serialVersionUID = 101512578540413946L;

	private Organization org;
	
	private OrganizationPaymentMethodPanel lostInsurancePanel;
	private OrganizationPaymentMethodPanel aomInsurancePanel;
	
	/**
	 * 
	 */
	public OrganizationPaymentMethodsPanel() {
		super();
		setSizeFull();
		setMargin(true);
		setSpacing(true);

	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		NavigationPanel navigationPanel = new NavigationPanel();
		NativeButton btnSave = new NativeButton(I18N.message("save"));
		btnSave.setIcon(FontAwesome.SAVE);
		btnSave.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = -1101198510652450294L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				save();
			}
		});
			
		navigationPanel.addButton(btnSave);
				
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		
		lostInsurancePanel = new OrganizationPaymentMethodPanel(EPaymentFlowType.LOS);
		aomInsurancePanel = new OrganizationPaymentMethodPanel(EPaymentFlowType.AOM);
		
		addComponent(navigationPanel, 0);
		contentLayout.addComponent(lostInsurancePanel);
		contentLayout.addComponent(aomInsurancePanel);
		return contentLayout;
	}
	
	/**
	 * Save
	 */
	private void save() {
		removeErrorsPanel();
		errors.addAll(lostInsurancePanel.validate());
		errors.addAll(aomInsurancePanel.validate());
				
		if (errors != null && !errors.isEmpty()) {
			displayErrorsPanel();
		} else {
			OrgPaymentMethod lostPaymentMethod = lostInsurancePanel.getOrgPaymentMethod();
			lostPaymentMethod.setOrganization(org);
			ENTITY_SRV.saveOrUpdate(lostPaymentMethod);
			
			OrgPaymentMethod aomPaymentMethod = aomInsurancePanel.getOrgPaymentMethod();
			aomPaymentMethod.setOrganization(org);
			ENTITY_SRV.saveOrUpdate(aomPaymentMethod);
			
			ENTITY_SRV.refresh(org);
			displaySuccess();
		}
	}
	
	
	/**
	 * @param org
	 */
	public void assignValues(Organization org) {
		removeErrorsPanel();
		if (org != null) {
			this.org = org;
			lostInsurancePanel.assignValues(org, getOrgPaymentMethod(org, EPaymentFlowType.LOS));
			aomInsurancePanel.assignValues(org, getOrgPaymentMethod(org, EPaymentFlowType.COM));
		}
	}
	
	/**
	 * 
	 * @param organization
	 * @param type
	 * @return
	 */
	public OrgPaymentMethod getOrgPaymentMethod(Organization organization, EPaymentFlowType type) {
		BaseRestrictions<OrgPaymentMethod> restrictions = new BaseRestrictions<OrgPaymentMethod>(OrgPaymentMethod.class);
		restrictions.addCriterion(Restrictions.eq(OrgPaymentMethod.ORGANIZATION, organization));
		restrictions.addCriterion(Restrictions.eq(OrgPaymentMethod.TYPE, type));
		List<OrgPaymentMethod> orgPaymentMethods = ENTITY_SRV.list(restrictions);
		if (orgPaymentMethods != null && !orgPaymentMethods.isEmpty()) {
			for (OrgPaymentMethod dealerPaymentMethod : orgPaymentMethods) {
				if (type.equals(dealerPaymentMethod.getType())) {
					return dealerPaymentMethod;
				}
			}
		}
		return null;
	}

	/**
	 * @author youhort.ly
	 */
	private class OrganizationPaymentMethodPanel extends Panel {
		
		/**
		 */
		private static final long serialVersionUID = -5161659170410808045L;
		
		private EntityRefComboBox<EPaymentMethod> cbxPaymentMethod;
		private OrgBankAccountComboBox cbxOrgBankAccount;
		private OrgAccountHolderComboBox cbxOrgAccountHolder;
		
		private EPaymentFlowType type;
		
		private OrgPaymentMethod orgPaymentMethod;
		
		/**
		 * @param caption
		 */
		public OrganizationPaymentMethodPanel(EPaymentFlowType type) {			
			setCaption(type.getDescEn());
			this.type = type;
			
			cbxPaymentMethod = new EntityRefComboBox<>(I18N.message("payment.method"), EPaymentMethod.valuesForPaymentDealer());
			cbxPaymentMethod.addStyleName("mytextfield-caption");
			cbxPaymentMethod.setRequired(true);
			cbxPaymentMethod.setImmediate(true);
			cbxPaymentMethod.setWidth(150, Unit.PIXELS);
			
			cbxOrgAccountHolder = new OrgAccountHolderComboBox("payee.name", new ArrayList<OrgAccountHolder>());
			cbxOrgAccountHolder.setWidth(150, Unit.PIXELS);
			cbxOrgAccountHolder.setImmediate(true);
			
			cbxOrgBankAccount = new OrgBankAccountComboBox("bank.account", new ArrayList<OrgBankAccount>());
			cbxOrgBankAccount.setWidth(150, Unit.PIXELS);
			cbxOrgBankAccount.setImmediate(true);

			cbxPaymentMethod.addValueChangeListener(new ValueChangeListener() {
				
				/** */
				private static final long serialVersionUID = -8812447707984283105L;

				/**
				 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
				 */
				@Override
				public void valueChange(ValueChangeEvent event) {
					if (cbxPaymentMethod.getSelectedEntity() != null) {
						if (cbxPaymentMethod.getSelectedEntity().equals(EPaymentMethod.CHEQUE)) {
							cbxOrgBankAccount.setVisible(false);
							cbxOrgBankAccount.setSelectedEntity(null);
						} else if (cbxPaymentMethod.getSelectedEntity().equals(EPaymentMethod.TRANSFER)) {
							cbxOrgBankAccount.setVisible(true);
						}
					}
				}
			});
			
			FormLayout formLayout = new FormLayout();
			formLayout.setMargin(true);
			formLayout.setSpacing(true);
			formLayout.addStyleName("myform-align-left");
			formLayout.addComponent(cbxPaymentMethod);
			formLayout.addComponent(cbxOrgAccountHolder);
			formLayout.addComponent(cbxOrgBankAccount);
	
			VerticalLayout contentLayout = new VerticalLayout(); 
			contentLayout.setSpacing(true);
			contentLayout.addComponent(formLayout);
			setContent(contentLayout);
		}
		
		/**
		 * 
		 * @param organization
		 * @return
		 */
		private List<OrgAccountHolder> getOrgAccHolderRestrictions(Organization organization) {
			OrgAccountHolderRestriction restrictions = new OrgAccountHolderRestriction();
			restrictions.setOrganization(organization);
			return ENTITY_SRV.list(restrictions);
		}
		
		/**
		 * 
		 * @param accHolderId
		 * @return
		 */
		private OrgAccountHolder getOrgAccountHolder(Long accHolderId) {
			OrgAccountHolderRestriction restrictions = new OrgAccountHolderRestriction();
			restrictions.setAccountHolderId(accHolderId);
			restrictions.addOrder(Order.desc(DealerAccountHolder.ID));
			return ENTITY_SRV.list(restrictions).isEmpty() ? null : ENTITY_SRV.list(restrictions).get(0);
		}
		
		/**
		 * 
		 * @param organization
		 * @return
		 */
		private List<OrgBankAccount> getOrgBankAccountRestrictions(Organization organization) {
			OrgBankAccountRestriction restrictions = new OrgBankAccountRestriction();
			restrictions.setOrganization(organization);
			return ENTITY_SRV.list(restrictions);
		}
		
		/**
		 * 
		 * @param bankAccId
		 * @return
		 */
		private OrgBankAccount getOrgBankAccount(Long bankAccId) {
			OrgBankAccountRestriction restrictions = new OrgBankAccountRestriction();
			restrictions.setBankAccountId(bankAccId);
			restrictions.addOrder(Order.desc(DealerBankAccount.ID));
			return ENTITY_SRV.list(restrictions).isEmpty() ? null : ENTITY_SRV.list(restrictions).get(0);
		}
		
		/**
		 * 
		 * @param org
		 * @param orgPaymentMethod
		 */
		public void assignValues(Organization org, OrgPaymentMethod orgPaymentMethod) {
		
			cbxOrgAccountHolder.setValues(getOrgAccHolderRestrictions(org));
			cbxOrgBankAccount.setValues(getOrgBankAccountRestrictions(org));
			
			if (orgPaymentMethod != null) {
				this.orgPaymentMethod = orgPaymentMethod;
				cbxPaymentMethod.setSelectedEntity(orgPaymentMethod.getPaymentMethod());
				
				OrgAccountHolder accountHolder = orgPaymentMethod.getOrgAccountHolder();
				if (accountHolder != null && accountHolder.getAccountHolder() != null) {
					cbxOrgAccountHolder.setSelectedEntity(getOrgAccountHolder(accountHolder.getAccountHolder()));
				}
				
				if (EPaymentMethod.TRANSFER.equals(orgPaymentMethod.getPaymentMethod())) {
					OrgBankAccount bankAccount = orgPaymentMethod.getOrgBankAccount();
					if (bankAccount != null && bankAccount.getBankAccount() != null) {
						cbxOrgBankAccount.setSelectedEntity(getOrgBankAccount(bankAccount.getBankAccount()));
					}
				}
			} else {
				this.orgPaymentMethod = new OrgPaymentMethod();
				reset();
			}
		}
		
		/**
		 * Reset
		 */
		public void reset() {
			cbxPaymentMethod.setSelectedEntity(null);
			cbxOrgBankAccount.setSelectedEntity(null);
			cbxOrgAccountHolder.setSelectedEntity(null);
		}
		
		/**
		 * @return
		 */
		public OrgPaymentMethod getOrgPaymentMethod() {
			orgPaymentMethod.setPaymentMethod(cbxPaymentMethod.getSelectedEntity());
			orgPaymentMethod.setType(this.type);
			orgPaymentMethod.setOrgBankAccount(cbxOrgBankAccount.getSelectedEntity());
			orgPaymentMethod.setOrgAccountHolder(cbxOrgAccountHolder.getSelectedEntity());
			return orgPaymentMethod;
		}
		
		/**
		 * Validate the term form
		 * @return
		 */
		public List<String> validate() {
			List<String> errors = new ArrayList<>();
			if (cbxPaymentMethod.getSelectedEntity() == null) {
				errors.add(type.getDescEn() + " : " + I18N.message("field.required.1", new String[] { I18N.message("payment.method") }));
			} else {
				if (cbxOrgAccountHolder.getSelectedEntity() == null) {
					errors.add(type.getDescEn() + " : " + I18N.message("field.required.1", new String[] { I18N.message("payee.name") }));
				}
				if (cbxPaymentMethod.getSelectedEntity().equals(EPaymentMethod.TRANSFER)) {
					if (cbxOrgBankAccount.getSelectedEntity() == null) {
						errors.add(type.getDescEn() + " : " + I18N.message("field.required.1", new String[] { I18N.message("bank.account") }));
					}
				}
			}	
			return errors;
		}
	}
}
