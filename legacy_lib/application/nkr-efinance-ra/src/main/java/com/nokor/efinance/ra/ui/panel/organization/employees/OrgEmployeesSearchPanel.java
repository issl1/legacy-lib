package com.nokor.efinance.ra.ui.panel.organization.employees;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.ersys.core.hr.model.eref.EGender;
import com.nokor.ersys.core.hr.model.eref.EJobPosition;
import com.nokor.ersys.core.hr.model.organization.Employee;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

/**
 * 
 * @author buntha.chea
 *
 */
public class OrgEmployeesSearchPanel extends AbstractSearchPanel<Employee> {

	private static final long serialVersionUID = 5451713054421122638L;
	
	private Long organizationId;
	
	private TextField txtFirstName;
	private TextField txtLastName;
	private ERefDataComboBox<EGender> cbxGender;
	private ERefDataComboBox<EJobPosition> cbxPosition;
	private TextField txtReference;

	public OrgEmployeesSearchPanel(String caption, AbstractTablePanel<Employee> tablePanel) {
		super(caption, tablePanel);
	}

	@Override
	protected void reset() {
		txtFirstName.setValue("");
		txtLastName.setValue("");
		txtReference.setValue("");
		cbxGender.setSelectedEntity(null);
		cbxPosition.setSelectedEntity(null);
	}

	@Override
	protected Component createForm() {
		
		txtFirstName = ComponentFactory.getTextField(false, 50, 200);
		txtLastName = ComponentFactory.getTextField(false, 50, 200);
		txtReference = ComponentFactory.getTextField(false, 50, 250);
		cbxGender = new ERefDataComboBox<>(EGender.values());
		cbxPosition = new ERefDataComboBox<>(EJobPosition.values());
		
		Label lblFirstName = ComponentFactory.getLabel("firstname");
		Label lblLastName = ComponentFactory.getLabel("lastname");
		Label lblReference = ComponentFactory.getLabel("reference");
		Label lblGender = ComponentFactory.getLabel("gender");
		Label lblPosition = ComponentFactory.getLabel("position");
		
		GridLayout gridLayout = new GridLayout(6, 2);
		gridLayout.setSpacing(true);
		int iCol = 0;
		gridLayout.addComponent(lblFirstName, iCol++, 0);
		gridLayout.addComponent(txtFirstName, iCol++, 0);
		gridLayout.addComponent(lblLastName, iCol++, 0);
		gridLayout.addComponent(txtLastName, iCol++, 0);
		gridLayout.addComponent(lblReference, iCol++, 0);
		gridLayout.addComponent(txtReference, iCol++, 0);
		iCol = 0;
		gridLayout.addComponent(lblGender, iCol++, 1);
		gridLayout.addComponent(cbxGender, iCol++, 1);
		gridLayout.addComponent(lblPosition, iCol++, 1);
		gridLayout.addComponent(cbxPosition, iCol++, 1);
		
		gridLayout.setComponentAlignment(lblFirstName, Alignment.MIDDLE_CENTER);
		gridLayout.setComponentAlignment(lblLastName, Alignment.MIDDLE_CENTER);
		gridLayout.setComponentAlignment(lblReference, Alignment.MIDDLE_CENTER);
		gridLayout.setComponentAlignment(lblGender, Alignment.MIDDLE_CENTER);
		gridLayout.setComponentAlignment(lblPosition, Alignment.MIDDLE_CENTER);
		
		return gridLayout;
	}

	@Override
	public BaseRestrictions<Employee> getRestrictions() {
		BaseRestrictions<Employee> restriction = new BaseRestrictions<>(Employee.class);
		
		if (organizationId != null) {
			restriction.addCriterion(Restrictions.eq("organization.id", organizationId));
		}
		
		if (StringUtils.isNotEmpty(txtFirstName.getValue())) {
			restriction.addCriterion(Restrictions.ilike("firstName", txtFirstName.getValue(), MatchMode.ANYWHERE));
		}
		
		if (StringUtils.isNotEmpty(txtLastName.getValue())) {
			restriction.addCriterion(Restrictions.ilike("lastName", txtLastName.getValue(), MatchMode.ANYWHERE));
		}
		
		if (cbxGender.getSelectedEntity() != null) {
			restriction.addCriterion(Restrictions.eq("gender", cbxGender.getSelectedEntity()));
		}
		
		if (cbxPosition.getSelectedEntity() != null) {
			restriction.addCriterion(Restrictions.eq("jobPosition", cbxPosition.getSelectedEntity()));
		}
		
		return restriction;
	}

	/**
	 * @return the organizationId
	 */
	public Long getOrganizationId() {
		return organizationId;
	}

	/**
	 * @param organizationId the organizationId to set
	 */
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
}
