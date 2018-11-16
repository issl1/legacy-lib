package com.nokor.efinance.ra.ui.panel.collections.warehouses.detail;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.ra.ui.panel.organization.detail.BaseOrganizationFormPanel;
import com.nokor.ersys.core.hr.model.eref.ESubTypeOrganization;
import com.nokor.ersys.core.hr.model.organization.Organization;

/**
 * Warehouse Form Panel
 * @author bunlong.taing
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class WarehouseFormPanel extends BaseOrganizationFormPanel {
	/** */
	private static final long serialVersionUID = -5596542965611620915L;
	
	private static final String TEMPLATE_NAME = "collection/warehouse/" + "companyDetail";
	
	/**
	 * Warehouse Form Panel post construct
	 */
	@PostConstruct
	public void PostConstruct() {
        super.init();
	}

	/**
	 * @see com.nokor.efinance.ra.ui.panel.organization.detail.BaseOrganizationFormPanel#getTemplateName()
	 */
	@Override
	protected String getTemplateName() {
		return TEMPLATE_NAME;
	}
	
	/**
	 * @see com.nokor.efinance.ra.ui.panel.organization.detail.BaseOrganizationFormPanel#getEntity()
	 */
	@Override
	protected Entity getEntity() {
		Organization comp = (Organization) super.getEntity();
		boolean isUpdate = getEntityId() != null && getEntityId() > 0;
		if (!isUpdate) {
			comp.setSubTypeOrganization(ESubTypeOrganization.getById(4l));// id 4 = Warehouse
		}
		return comp;
	}
	
	/**
	 * @see com.nokor.efinance.ra.ui.panel.organization.detail.BaseOrganizationFormPanel#saveEntity()
	 */
	@Override
	public void saveEntity() {
		boolean isUpdate = getEntityId() != null && getEntityId() > 0;
		super.saveEntity();
		if (!isUpdate) {
        	mainPanel.addAreaSubTab(getEntity().getId());
        }
	}

}
