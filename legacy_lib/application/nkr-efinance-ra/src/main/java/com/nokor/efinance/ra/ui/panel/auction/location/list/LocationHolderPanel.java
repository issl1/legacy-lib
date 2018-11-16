package com.nokor.efinance.ra.ui.panel.auction.location.list;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.ra.ui.panel.organization.list.BaseOrganizationHolderPanel;
import com.nokor.ersys.core.hr.model.eref.ESubTypeOrganization;
import com.nokor.ersys.core.hr.model.organization.OrganizationTypes;

/**
 * Location Holder Panel
 * @author bunlong.taing
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(LocationHolderPanel.NAME)
public class LocationHolderPanel extends BaseOrganizationHolderPanel {
	/** */
	private static final long serialVersionUID = 9072427136454001419L;
	
	public static final String NAME = "locations";
	
	/**
	 * Location Holder Panel post constructor
	 */
	@PostConstruct
	public void PostConstruct() {
		setTypeOrganization(OrganizationTypes.LOCATION);
		setSubTypeOrganization(ESubTypeOrganization.getById(5l));// id 5 = Locations
		super.init();
	}

	/**
	 * @see com.nokor.efinance.ra.ui.panel.organization.list.BaseOrganizationHolderPanel#getOrgCaption()
	 */
	@Override
	protected String getOrgCaption() {
		return "locations";
	}

}
