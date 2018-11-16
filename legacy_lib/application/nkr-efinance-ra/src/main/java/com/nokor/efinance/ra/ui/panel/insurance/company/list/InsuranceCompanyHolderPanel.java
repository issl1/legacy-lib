package com.nokor.efinance.ra.ui.panel.insurance.company.list;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.ra.ui.panel.insurance.bank.OrganizationBankAccountsPanel;
import com.nokor.efinance.ra.ui.panel.insurance.company.detail.price.InsurancePriceAOMHolderPanel;
import com.nokor.efinance.ra.ui.panel.insurance.company.detail.price.InsurancePriceLOSHolderPanel;
import com.nokor.efinance.ra.ui.panel.organization.list.BaseOrganizationHolderPanel;
import com.nokor.ersys.core.hr.model.organization.OrganizationTypes;

/**
 * 
 * @author prasnar
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(InsuranceCompanyHolderPanel.NAME)
public class InsuranceCompanyHolderPanel extends BaseOrganizationHolderPanel {
	/** */
	private static final long serialVersionUID = 4948133370956145047L;

	public static final String NAME = "insurance.companies";
	
//	@Autowired
//	private InsurancePriceDetailHolderPanel priceDetailPanel;
	@Autowired
	private InsurancePriceLOSHolderPanel priceLOSHolderPanel;
	@Autowired
	private InsurancePriceAOMHolderPanel priceAOMHolderPanel;
	
	private OrganizationBankAccountsPanel orgBankAccountsPanel;
	
	/**
	 * Insurance Holder Panel post constructor
	 */
	@PostConstruct
	public void PostConstruct() {
		setTypeOrganization(OrganizationTypes.INSURANCE);
		orgBankAccountsPanel = new OrganizationBankAccountsPanel();
		super.init();
	}

	/**
	 * @see com.nokor.efinance.ra.ui.panel.organization.list.BaseOrganizationHolderPanel#getOrgCaption()
	 */
	@Override
	protected String getOrgCaption() {
		return "insurance.companies";
	}
	
	/**
	 * @see com.nokor.efinance.ra.ui.panel.organization.list.BaseOrganizationHolderPanel#addSubsidiairySubTab(java.lang.Long)
	 */
	@Override
	public void addSubsidiairySubTab(Long selectedId) {
		orgBankAccountsPanel.assignValues(selectedId);
		priceLOSHolderPanel.assignValues(selectedId);
		priceAOMHolderPanel.assignValues(selectedId);
		getTabSheet().addFormPanel(orgBankAccountsPanel);
		getTabSheet().addFormPanel(priceLOSHolderPanel);
		getTabSheet().addFormPanel(priceAOMHolderPanel);
		getTabSheet().setSelectedTab(priceLOSHolderPanel);
		super.addSubsidiairySubTab(selectedId);
	}
	
}
