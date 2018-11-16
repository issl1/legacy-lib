package com.nokor.efinance.ra.ui.panel.organization.branch.list;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.ersys.core.hr.model.eref.EOrgLevel;
import com.nokor.ersys.core.hr.model.organization.OrgStructure;
import com.nokor.ersys.core.hr.service.OrgStructureRestriction;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;

/**
 * Branch or Department Search Panel
 * @author bunlong.taing
 */
public class BranchSearchPanel extends AbstractSearchPanel<OrgStructure> {

	/** */
	private static final long serialVersionUID = 5970155929365968762L;
	
	private TextField txtName;
	private Long organizationId;
	private EOrgLevel level;

	public BranchSearchPanel(BranchTablePanel tablePanel, EOrgLevel level) {
		super(I18N.message("search"), tablePanel);
		this.level = level;
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
	public BaseRestrictions<OrgStructure> getRestrictions() {
		OrgStructureRestriction restriction = new OrgStructureRestriction();
		if (EOrgLevel.BRANCH.equals(level) || EOrgLevel.HEAD_OFFICE.equals(level)) {
			restriction.addCriterion(Restrictions.or(Restrictions.eq("level", EOrgLevel.BRANCH), Restrictions.eq("level", EOrgLevel.HEAD_OFFICE)));
		} else if (EOrgLevel.DEPARTMENT.equals(level)) {
			restriction.addCriterion(Restrictions.eq("level", EOrgLevel.DEPARTMENT));
		}
		
		if (StringUtils.isNotEmpty(txtName.getValue())) {
			restriction.setOrganizationName(txtName.getValue());
		}
		if (organizationId != null) {
			restriction.setOrganizationId(organizationId);
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
