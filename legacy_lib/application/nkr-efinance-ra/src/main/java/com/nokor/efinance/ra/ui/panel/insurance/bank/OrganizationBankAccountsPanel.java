package com.nokor.efinance.ra.ui.panel.insurance.bank;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.ersys.core.hr.model.organization.Organization;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author uhout.cheng
 */
public class OrganizationBankAccountsPanel extends AbstractTabPanel implements FinServicesHelper, FMEntityField {
	
	/** */
	private static final long serialVersionUID = -1855253127811714960L;
	
	private OrganizationBankAccountTabPanel orgBankAccountTabPanel;
	
	private OrganizationPaymentMethodsPanel orgPaymentMethodsPanel;
	
	/**
	 */
	public OrganizationBankAccountsPanel() {
		super();
		setCaption(I18N.message("payment"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);			
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		orgBankAccountTabPanel = new OrganizationBankAccountTabPanel(this);
		orgPaymentMethodsPanel = new OrganizationPaymentMethodsPanel();
		
		TabSheet paymentMethodTab = new TabSheet();
		paymentMethodTab.addTab(orgPaymentMethodsPanel, I18N.message("payment.methods"));
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		
		contentLayout.addComponent(orgBankAccountTabPanel);
		contentLayout.addComponent(paymentMethodTab);
		return contentLayout;
	}
	
	/**
	 * 
	 * @param orgId
	 */
	public void assignValues(Long orgId) {
		if (orgId != null) {
			Organization org = ORG_SRV.getById(Organization.class, orgId);
			orgBankAccountTabPanel.assignValue(org);
			refreshPaymentMethod(org);
		}
	}
	
	/**
	 * 
	 * @param organization
	 */
	protected void refreshPaymentMethod(Organization organization) {
		orgPaymentMethodsPanel.assignValues(organization);
	}
	
}
