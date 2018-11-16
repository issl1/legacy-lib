package com.nokor.efinance.ra.ui.panel.collections.warehouses.list;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.ra.ui.panel.organization.list.BaseOrganizationHolderPanel;
import com.nokor.ersys.core.hr.model.eref.ESubTypeOrganization;
import com.nokor.ersys.core.hr.model.organization.OrganizationTypes;

/**
 * Warehouse Holder Panel
 * @author bunlong.taing
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(WarehouseHolderPanel.NAME)
public class WarehouseHolderPanel extends BaseOrganizationHolderPanel {
	/** */
	private static final long serialVersionUID = 4002435116717181361L;
	
	public static final String NAME = "warehouses";
	
	/**
	 * Warehouse Holder Panel post constructor
	 */
	@PostConstruct
	public void PostConstruct() {
		setTypeOrganization(OrganizationTypes.LOCATION);
		setSubTypeOrganization(ESubTypeOrganization.getById(4l));// id 4 = Warehouse
		super.init();
	}

	/**
	 * @see com.nokor.efinance.ra.ui.panel.organization.list.BaseOrganizationHolderPanel#getOrgCaption()
	 */
	@Override
	protected String getOrgCaption() {
		return "warehouses";
	}

}
