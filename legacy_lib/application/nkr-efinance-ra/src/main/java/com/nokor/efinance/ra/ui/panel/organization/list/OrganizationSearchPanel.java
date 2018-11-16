package com.nokor.efinance.ra.ui.panel.organization.list;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.ersys.core.hr.model.organization.MOrganization;
import com.nokor.ersys.core.hr.model.organization.Organization;
import com.nokor.ersys.core.hr.model.organization.OrganizationTypes;
import com.nokor.ersys.core.hr.service.OrganizationRestriction;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;

/**
 * 
 * @author prasnar
 *
 */
public class OrganizationSearchPanel extends AbstractSearchPanel<Organization> implements FinServicesHelper, MOrganization {
	/** */
	private static final long serialVersionUID = -1352392310896604465L;
	
	private TextField txtName;
	private OrganizationTablePanel tablePanel;

	/**
	 * 
	 * @param tablePanel
	 */
	public OrganizationSearchPanel(OrganizationTablePanel tablePanel) {
		super(I18N.message("search"), tablePanel);
		this.tablePanel = tablePanel;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		txtName = ComponentFactory.getTextField(50, 200);
		
		GridLayout mainLayout = new GridLayout(2, 1);
		mainLayout.setSpacing(true);
		mainLayout.addComponent(ComponentFactory.getLabel("name"));
		mainLayout.addComponent(txtName);
		
		return mainLayout;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public OrganizationRestriction getRestrictions() {
		OrganizationRestriction restrictions = OrganizationRestriction.buildRestrictions(tablePanel.getTypeOrganization(), null, true, null);
		
		if (StringUtils.isNotEmpty(txtName.getValue())) { 
			restrictions.setName(txtName.getValue());
		}
		if (OrganizationTypes.LOCATION.equals(tablePanel.getTypeOrganization())) {
			restrictions.addSubTypeOrganization(tablePanel.getSubTypeOrganization());
		}
		
		return restrictions;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtName.setValue("");
	}

}
