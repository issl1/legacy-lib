package com.nokor.efinance.ra.ui.panel.organization.list;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.ersys.core.hr.model.organization.OrganizationTypes;

/**
 * 
 * @author prasnar
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(OrganizationHolderPanel.NAME)
public class OrganizationHolderPanel extends BaseOrganizationHolderPanel {
	/** */
	private static final long serialVersionUID = 2229864306030972621L;

	public static final String NAME = "main.organizations";
	
	/**
	 * Organization Holder Panel post constructor
	 */
	@PostConstruct
	public void PostConstruct() {
		setTypeOrganization(OrganizationTypes.MAIN);
		super.init();
	}

	/**
	 * @see com.nokor.efinance.ra.ui.panel.organization.list.BaseOrganizationHolderPanel#getOrgCaption()
	 */
	@Override
	protected String getOrgCaption() {
		return "companies";
	}

}
