package com.nokor.ersys.collab.project.list;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.ersys.collab.project.model.EProjectCategory;
import com.nokor.ersys.collab.project.model.EProjectType;
import com.nokor.ersys.collab.project.model.Project;
import com.nokor.ersys.collab.tools.helper.ErsysCollabAppServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Table.Align;

/**
 * Assignment Table Panel
 * @author bunlong.taing
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ProjectTablePanel extends AbstractTablePanel<Project> implements ErsysCollabAppServicesHelper {

	/**  */
	private static final long serialVersionUID = -2078570869931084627L;
	
	/**
	 * Assignment Table Panel post constructor
	 */
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("projects"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		super.init(I18N.message("projects"));		
		addDefaultNavigation();
	}
	
	/**
	 * Get Item Selected Id
	 */
	public Long getItemSelectedId() {
		if (selectedItem != null) {
			return (Long) selectedItem.getItemProperty(Project.ID).getValue();
		}
		return null;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createPagedDataProvider()
	 */
	@Override
	protected PagedDataProvider<Project> createPagedDataProvider() {
		PagedDefinition<Project> pagedDefinition = new PagedDefinition<Project>(searchPanel.getRestrictions());
		
		pagedDefinition.addColumnDefinition(Project.ID, I18N.message("id"), Long.class, Align.LEFT, 50);
		pagedDefinition.addColumnDefinition(Project.CODE, I18N.message("code"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(Project.DESC, I18N.message("desc"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(Project.DESCEN, I18N.message("desc.en"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(Project.TYPE, I18N.message("project.type"), EProjectType.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(Project.CATEGORY, I18N.message("category"), EProjectCategory.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(Project.STARTDATE, I18N.message("date.start"), Date.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(Project.ENDDATE, I18N.message("date.end"), Date.class, Align.LEFT, 100);
		
		EntityPagedDataProvider<Project> pagedDataProvider = new EntityPagedDataProvider<Project>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected Project getEntity() {
		if (selectedItem != null) {
			final Long id = (Long) selectedItem.getItemProperty(Project.ID).getValue();
		    return PROJECT_SRV.getById(Project.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected AbstractSearchPanel<Project> createSearchPanel() {
		return new ProjectSearchPanel(this);
	}

}
