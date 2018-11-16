/*
 * Created on 29/05/2015.
 */
package com.nokor.ersys.vaadin.ui.referential.setting.list;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.common.app.tools.helper.AppServicesHelper;
import com.nokor.frmk.config.model.SettingConfig;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.DeleteClickListener;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.SearchClickListener;
import com.vaadin.ui.Table.Align;

/**
 * Setting Config Table
 * @author phirun.kong
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SettingConfigTablePanel extends AbstractTablePanel<SettingConfig> implements AppServicesHelper, 
DeleteClickListener, SearchClickListener{

	/**	 */
	private static final long serialVersionUID = -3764703307250108937L;
	
	@Override
	protected SettingConfigSearchPanel createSearchPanel() {
		return new SettingConfigSearchPanel(this);		
	}

	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("advanced.configurations"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("advanced.configurations"));
		
		NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addAddClickListener(this);
		navigationPanel.addEditClickListener(this);
		navigationPanel.addRefreshClickListener(this);

	}	
	
	@Override
	protected PagedDataProvider<SettingConfig> createPagedDataProvider() {
		PagedDefinition<SettingConfig> pagedDefinition = new PagedDefinition<SettingConfig>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition("id", I18N.message("id"), Long.class, Align.LEFT, 70);
		pagedDefinition.addColumnDefinition("code", I18N.message("code"), String.class, Align.LEFT, 250);
		pagedDefinition.addColumnDefinition("desc", I18N.message("desc"), String.class, Align.LEFT, 250);
		pagedDefinition.addColumnDefinition("descEn", I18N.message("desc.en"), String.class, Align.LEFT, 250);
		pagedDefinition.addColumnDefinition("value", I18N.message("value"), String.class, Align.LEFT, 250);
		
		EntityPagedDataProvider<SettingConfig> pagedDataProvider = new EntityPagedDataProvider<SettingConfig>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

	@Override
	protected SettingConfig getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		      return ENTITY_SRV.getById(SettingConfig.class, id);
		}
		return null;
	}

}
