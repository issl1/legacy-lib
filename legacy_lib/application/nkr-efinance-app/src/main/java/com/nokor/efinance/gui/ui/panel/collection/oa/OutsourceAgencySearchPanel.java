package com.nokor.efinance.gui.ui.panel.collection.oa;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.ersys.core.hr.model.organization.MOrganization;
import com.nokor.ersys.core.hr.model.organization.Organization;
import com.nokor.ersys.core.hr.model.organization.OrganizationSubTypes;
import com.nokor.ersys.core.hr.model.organization.OrganizationTypes;
import com.nokor.ersys.core.hr.service.OrganizationRestriction;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

/**
 * 
 * @author uhout.cheng
 *
 */
public class OutsourceAgencySearchPanel extends AbstractSearchPanel<Organization> implements MOrganization {
	
	/** */
	private static final long serialVersionUID = -1455231046724250453L;
	
	private TextField txtName;

	public OutsourceAgencySearchPanel(OutsourceAgencyTablePanel tablePanel) {
		super(I18N.message("search"), tablePanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		txtName = ComponentFactory.getTextField(50, 200);
		
		Label lblName = ComponentFactory.getLabel("name");
		
		GridLayout mainLayout = new GridLayout(3, 1);
		mainLayout.setSpacing(true);
		mainLayout.addComponent(lblName);
		mainLayout.addComponent(txtName);
		
		mainLayout.setComponentAlignment(lblName, Alignment.MIDDLE_LEFT);
		
		return mainLayout;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<Organization> getRestrictions() {
		OrganizationRestriction restrictions = OrganizationRestriction.buildRestrictions(OrganizationTypes.AGENT, null, true, null);
		restrictions.addSubTypeOrganization(OrganizationSubTypes.OUTSOURCE_AGENT);
		
		if (StringUtils.isNotEmpty(txtName.getValue())) { 
			restrictions.setName(txtName.getValue());
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
