package com.nokor.efinance.ra.ui.panel.agent.company.detail;

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
public class AgentCompanyFormPanel extends BaseOrganizationFormPanel {
	/** */
	private static final long serialVersionUID = -7525721513484196049L;

	private static final String TEMPLATE_NAME = "agent/" + "companyDetail";

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

}
