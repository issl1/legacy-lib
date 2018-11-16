package com.nokor.efinance.ra.ui.panel.collections.collectionconfig;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.collection.model.CollectionConfig;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Table.Align;

/**
 * 
 * @author buntha.chea
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CollectionConfigTablePanel extends AbstractTablePanel<CollectionConfig> implements FMEntityField {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1395563112612774433L;
	
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("collection.configs"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);		
		super.init(I18N.message("collection.configs"));
		addDefaultNavigation();
	}	

	@Override
	protected PagedDataProvider<CollectionConfig> createPagedDataProvider() {
		PagedDefinition<CollectionConfig> pagedDefinition = new PagedDefinition<CollectionConfig>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 50);
		pagedDefinition.addColumnDefinition(COL_TYPE + "." + DESC_EN, I18N.message("collection.type"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("collectionFee.descEn", I18N.message("collection.fee"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("reposessionFee.descEn", I18N.message("reposession.fee"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("extendInDay", I18N.message("extend.in.day"), Integer.class, Align.LEFT, 80);
		EntityPagedDataProvider<CollectionConfig> pagedDataProvider = new EntityPagedDataProvider<CollectionConfig>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

	@Override
	protected CollectionConfig getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(CollectionConfig.class, id);
		}
		return null;
	}
	
	@Override
	protected CollectionConfigSearchPanel createSearchPanel() {
		return new CollectionConfigSearchPanel(this);		
	}

}
