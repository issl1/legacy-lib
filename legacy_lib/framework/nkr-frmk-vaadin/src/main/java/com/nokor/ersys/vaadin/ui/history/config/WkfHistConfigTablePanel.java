package com.nokor.ersys.vaadin.ui.history.config;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.common.app.workflow.model.WkfHistoConfig;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Table.Align;

/**
 * Wkf Hist Config Table Panel
 * @author bunlong.taing
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class WkfHistConfigTablePanel extends AbstractTablePanel<WkfHistoConfig> {
	/** */
	private static final long serialVersionUID = -2493614654388824258L;
	
	/**
	 */
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("hist.configs"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(getCaption());
		addDefaultNavigation();
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected WkfHistoConfig getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(WkfHistoConfig.class, id);
		}
		return null;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createPagedDataProvider()
	 */
	@Override
	protected PagedDataProvider<WkfHistoConfig> createPagedDataProvider() {
		PagedDefinition<WkfHistoConfig> pagedDefinition = new PagedDefinition<WkfHistoConfig>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(WkfHistoConfig.ID, I18N.message("id"), Long.class, Align.LEFT, 50);
		pagedDefinition.addColumnDefinition(WkfHistoConfig.HISTCLASSNAME, I18N.message("hist.class.name"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(WkfHistoConfig.HISTITEMCLASSNAME, I18N.message("hist.item.class.name"), String.class, Align.LEFT, 250);
		pagedDefinition.addColumnDefinition(WkfHistoConfig.HISTAUDITPROPERTIES, I18N.message("hist.audit.properties"), String.class, Align.LEFT, 400);
		
		EntityPagedDataProvider<WkfHistoConfig> pagedDataProvider = new EntityPagedDataProvider<WkfHistoConfig>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected WkfHistConfigSearchPanel createSearchPanel() {
		return new WkfHistConfigSearchPanel(this);
	}

}
