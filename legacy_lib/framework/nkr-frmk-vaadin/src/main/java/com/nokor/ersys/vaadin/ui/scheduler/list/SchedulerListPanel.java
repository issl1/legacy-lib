package com.nokor.ersys.vaadin.ui.scheduler.list;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.model.entity.EntityStatusRecordAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.common.app.scheduler.SchedulerManager;
import com.nokor.common.app.scheduler.model.ScTriggerTask;

import com.nokor.ersys.vaadin.ui.scheduler.SchedulerConstants;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.util.VaadinServicesHelper;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Table.Align;

/**
 * 
 * @author phirun.kong
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SchedulerListPanel extends AbstractTablePanel<ScTriggerTask> implements SchedulerConstants, VaadinServicesHelper {

	/**	 */
	private static final long serialVersionUID = -3202584736343734906L;

	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("scheduler.list"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("scheduler.list2"));
		
		addDefaultNavigation();
	}
	@Override
	protected NavigationPanel addDefaultNavigation() {
		NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addAddClickListener(this);
		navigationPanel.addEditClickListener(this);
		navigationPanel.addRefreshClickListener(this);
		return navigationPanel;
	}
	
	/**
	 * Get item selected id
	 * @return
	 */
	@Override
	public Long getItemSelectedId() {
		if (selectedItem != null) {
			return (Long) selectedItem.getItemProperty(ID).getValue();
		}
		return null;
	}	
	
	/**
	 * Get Paged definition
	 * @return
	 */
	@Override
	protected PagedDataProvider<ScTriggerTask> createPagedDataProvider() {
		PagedDefinition<ScTriggerTask> pagedDefinition = new PagedDefinition<ScTriggerTask>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(DESC, I18N.message("desc"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("comment", I18N.message("comment"), String.class, Align.LEFT, 200);
		
		EntityPagedDataProvider<ScTriggerTask> pagedDataProvider = new EntityPagedDataProvider<ScTriggerTask>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

		
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected ScTriggerTask getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(ScTriggerTask.class, id);
		}
		return null;
	}
	
	@Override
	public void addButtonClick(ClickEvent event) {
		try {
			SchedulerManager.getSchedulerManager().restart();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	@Override
	protected void deleteEntity(Entity entity) {
		ENTITY_SRV.throwIntoRecycledBin( (EntityStatusRecordAware) entity);
	}
	
	@Override
	protected SchedulerSearchPanel createSearchPanel() {
		return new SchedulerSearchPanel(this);		
	}
}
