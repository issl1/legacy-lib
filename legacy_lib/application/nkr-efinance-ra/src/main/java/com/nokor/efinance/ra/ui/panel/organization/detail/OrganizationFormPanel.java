package com.nokor.efinance.ra.ui.panel.organization.detail;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 
 * @author prasnar
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class OrganizationFormPanel extends BaseOrganizationFormPanel {
	/**  */
	private static final long serialVersionUID = 297656035482037631L;

	private static final String TEMPLATE_NAME = "organization/" + "companyDetail";

	/**
	 * Branch Form Panel post construct
	 */
	@PostConstruct
	public void PostConstruct() {
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
        	mainPanel.addBranchSubTab(getEntity().getId());
        }
	}

}
