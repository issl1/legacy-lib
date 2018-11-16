package com.nokor.efinance.ra.ui.panel.organization.subsidiairy.list;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;

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
public class SubsidiairySearchPanel extends AbstractSearchPanel<Organization> {
	/** */
	private static final long serialVersionUID = -5407882067943869691L;

	private TextField txtName;
	private SubsidiairyTablePanel tablePanel;

	/**
	 * 
	 * @param tablePanel
	 */
	public SubsidiairySearchPanel(SubsidiairyTablePanel tablePanel) {
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
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtName.setValue("");
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public OrganizationRestriction getRestrictions() {
		OrganizationRestriction restrictions = OrganizationRestriction.buildRestrictions(OrganizationTypes.INSURANCE, null, null, tablePanel.getParentOrganizationId());
		
		if (StringUtils.isNotEmpty(txtName.getValue())) {
			restrictions.setName(txtName.getValue());
		}
		
		return restrictions;
	}

}
