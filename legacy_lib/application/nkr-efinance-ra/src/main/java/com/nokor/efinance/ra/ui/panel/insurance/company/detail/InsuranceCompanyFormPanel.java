package com.nokor.efinance.ra.ui.panel.insurance.company.detail;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.ra.ui.panel.organization.detail.BaseOrganizationFormPanel;

/**
 * 
 * @author prasnar
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InsuranceCompanyFormPanel extends BaseOrganizationFormPanel {
	/** */
	private static final long serialVersionUID = -946967635521571655L;

	private static final String TEMPLATE_NAME = "insurance/" + "companyDetail";

	/**
	 * Init
	 */
	@Override
	public void init() {
		super.init();
	}

	@Override
	protected String getTemplateName() {
		return TEMPLATE_NAME;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormTabPanel#saveEntity()
	 */
	@Override
	public void saveEntity() {
		boolean isUpdate = getEntityId() != null && getEntityId() > 0;
		super.saveEntity();
		if (!isUpdate) {
        	mainPanel.addSubsidiairySubTab(getEntity().getId());
        }
	}

}
