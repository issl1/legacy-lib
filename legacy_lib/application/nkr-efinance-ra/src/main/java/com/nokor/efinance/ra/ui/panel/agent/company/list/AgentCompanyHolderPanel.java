package com.nokor.efinance.ra.ui.panel.agent.company.list;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.ra.ui.panel.organization.list.BaseOrganizationHolderPanel;
import com.nokor.ersys.core.hr.model.organization.OrganizationTypes;

/**
 * 
 * @author prasnar
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(AgentCompanyHolderPanel.NAME)
public class AgentCompanyHolderPanel extends BaseOrganizationHolderPanel {
	/** */
	private static final long serialVersionUID = -3311528210573056740L;

	public static final String NAME = "agent.companies";
	
	/**
	 * Agent Holder Panel post constructor
	 */
	@PostConstruct
	public void PostConstruct() {
		setTypeOrganization(OrganizationTypes.AGENT);
		super.init();
	}

	/**
	 * @see com.nokor.efinance.ra.ui.panel.organization.list.BaseOrganizationHolderPanel#getOrgCaption()
	 */
	@Override
	protected String getOrgCaption() {
		return "agent.companies";
	}
	
}
