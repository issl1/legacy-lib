package com.nokor.efinance.ra.ui.panel.organization.subsidiairy.detail;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.ra.ui.panel.organization.detail.BaseOrganizationFormPanel;
import com.nokor.ersys.core.hr.model.organization.Organization;

/**
 * Subsidiary Form Panel
 * @author bunlong.taing
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SubsidiaryFormPanel extends BaseOrganizationFormPanel implements FinServicesHelper {
	/** */
	private static final long serialVersionUID = -5491514336559177392L;
	
	private static final String TEMPLATE_NAME = "insurance/" + "companyDetail";
	
	private Long parentOrganizationId;
	
	/**
	 * Init()
	 */
	@Override
	public void init() {
		super.init();
        setCaption(I18N.message("subsidiary"));
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
			Organization parent = ORG_SRV.getById(Organization.class, getParentOrganizationId());
			comp.setParent(parent);
		}
		return comp;
	}

	/**
	 * @return the parentOrganizationId
	 */
	public Long getParentOrganizationId() {
		return parentOrganizationId;
	}

	/**
	 * @param parentOrganizationId the parentOrganizationId to set
	 */
	public void setParentOrganizationId(Long parentOrganizationId) {
		this.parentOrganizationId = parentOrganizationId;
	}

}