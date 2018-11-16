package com.nokor.ersys.collab.project.list;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.ersys.collab.project.model.EProjectCategory;
import com.nokor.ersys.collab.project.model.EProjectType;
import com.nokor.ersys.collab.project.model.Project;
import com.nokor.ersys.collab.project.service.ProjectRestriction;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;

/**
 * Project Search Panel
 * @author bunlong.taing
 *
 */
public class ProjectSearchPanel extends AbstractSearchPanel<Project> {

	/**	 */
	private static final long serialVersionUID = -1931715105869713259L;
	
	private TextField txtText;
	private ERefDataComboBox<EProjectType> cbxType;
	private ERefDataComboBox<EProjectCategory> cbxCategory;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;

	/**
	 * Assignment Search Panel constructor
	 * @param tablePanel
	 */
	public ProjectSearchPanel(ProjectTablePanel tablePanel) {
		super(I18N.message("search"), tablePanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		txtText = ComponentFactory.getTextField(100, 200);
		
		cbxType = new ERefDataComboBox<EProjectType>(EProjectType.values());
		cbxType.setWidth(200, Unit.PIXELS);
		cbxCategory = new ERefDataComboBox<EProjectCategory>(EProjectCategory.values());
		cbxCategory.setWidth(200, Unit.PIXELS);
		dfStartDate = ComponentFactory.getAutoDateField();
		dfStartDate.setWidth(95, Unit.PIXELS);
		dfEndDate = ComponentFactory.getAutoDateField();
		dfEndDate.setWidth(95, Unit.PIXELS);
		
		GridLayout gridLayout = new GridLayout(6, 2);
		gridLayout.setSpacing(true);
		gridLayout.addComponent(ComponentFactory.getLabel("search.text"));
		gridLayout.addComponent(txtText);
		gridLayout.addComponent(ComponentFactory.getLabel("project.type"));
		gridLayout.addComponent(cbxType);
		gridLayout.addComponent(ComponentFactory.getLabel("category"));
		gridLayout.addComponent(cbxCategory);
		gridLayout.addComponent(ComponentFactory.getLabel("date.start"));
		gridLayout.addComponent(dfStartDate);
		gridLayout.addComponent(ComponentFactory.getLabel("date.end"));
		gridLayout.addComponent(dfEndDate);
		
		return gridLayout;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<Project> getRestrictions() {
		ProjectRestriction restrictions = new ProjectRestriction();
		
		if (StringUtils.isNotEmpty(txtText.getValue())) {
			restrictions.setText(txtText.getValue());
		}
		
		if (cbxType.getSelectedEntity() != null) {
			restrictions.setType(cbxType.getSelectedEntity());
		}
		
		if (cbxCategory.getSelectedEntity() != null) {
			restrictions.setCategory(cbxCategory.getSelectedEntity());
		}
		
		if (dfStartDate.getValue() != null) {
			restrictions.setStartDate(dfStartDate.getValue());
		}
		
		if (dfEndDate.getValue() != null) {
			restrictions.setEndDate(dfEndDate.getValue());
		}
		
		return restrictions;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtText.setValue("");
		cbxType.setSelectedEntity(null);
		cbxCategory.setSelectedEntity(null);
		dfStartDate.setValue(null);
		dfEndDate.setValue(null);
	}

}
