package com.nokor.efinance.ra.ui.panel.agent.company.list;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.ra.ui.panel.organization.list.OrganizationTablePanel;
import com.nokor.ersys.core.hr.model.eref.ESubTypeOrganization;
import com.nokor.ersys.core.hr.model.organization.MOrganization;
import com.nokor.ersys.core.hr.model.organization.Organization;
import com.nokor.ersys.core.hr.model.organization.OrganizationTypes;
import com.nokor.ersys.core.hr.service.OrganizationRestriction;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

/**
 * Agent Company Search Panel
 * @author bunlong.taing
 */
public class AgentCompanySearchPanel extends AbstractSearchPanel<Organization> implements MOrganization {
	/** */
	private static final long serialVersionUID = -1606656724777873640L;
	
	private CheckBox cbRegistration;
	private CheckBox cbCollection;
	private CheckBox cbPrinting;
	
	private TextField txtName;

	public AgentCompanySearchPanel(OrganizationTablePanel tablePanel) {
		super(I18N.message("search"), tablePanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		txtName = ComponentFactory.getTextField(50, 200);
		
		GridLayout mainLayout = new GridLayout(3, 1);
		mainLayout.setSpacing(true);
		mainLayout.addComponent(ComponentFactory.getLabel("name"));
		mainLayout.addComponent(txtName);

		cbRegistration = new CheckBox(I18N.message("registration"));
		cbRegistration.setValue(true);
		cbCollection = new CheckBox(I18N.message("collection"));
		cbCollection.setValue(true);
		cbPrinting = new CheckBox(I18N.message("printing"));
		cbPrinting.setValue(true);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		horizontalLayout.addComponent(cbRegistration);
		horizontalLayout.addComponent(cbCollection);
		horizontalLayout.addComponent(cbPrinting);
		mainLayout.addComponent(horizontalLayout);
		
		return mainLayout;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<Organization> getRestrictions() {
		OrganizationRestriction restrictions = OrganizationRestriction.buildRestrictions(OrganizationTypes.AGENT, null, true, null);
		
		if (StringUtils.isNotEmpty(txtName.getValue())) { 
			restrictions.setName(txtName.getValue());
		}
		
		if (!cbRegistration.getValue() && !cbCollection.getValue() && !cbPrinting.getValue()) {
			cbRegistration.setValue(true);
			cbCollection.setValue(true);
			cbPrinting.setValue(true);
		}
		
		if (cbRegistration.getValue()) {
			restrictions.addSubTypeOrganization(ESubTypeOrganization.getById(1l));// id 1 = Registration
		}
		if (cbCollection.getValue()) {
			restrictions.addSubTypeOrganization(ESubTypeOrganization.getById(2l));// id 2 = Collection
		}
		if (cbPrinting.getValue()) {
			restrictions.addSubTypeOrganization(ESubTypeOrganization.getById(3l));// id 3 = Printing
		}
		
		return restrictions;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtName.setValue("");
		cbRegistration.setValue(true);
		cbCollection.setValue(true);
		cbPrinting.setValue(true);
	}

}
