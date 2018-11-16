package com.nokor.efinance.ra.ui.panel.collections.storagelocation;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.collection.model.StorageLocation;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Table.Align;

/**
 * The Group Table Panel
 * @author buntha.chea
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StorageLocationTablePanel extends AbstractTablePanel<StorageLocation> implements FMEntityField {

	/** */
	private static final long serialVersionUID = -5491073837907908246L;
	
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("storage.location"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("storage.location"));
		
		addDefaultNavigation();
	}
	
	

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createPagedDataProvider()
	 */
	@Override
	protected PagedDataProvider<StorageLocation> createPagedDataProvider() {
		PagedDefinition<StorageLocation> pagedDefinition = new PagedDefinition<>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 80);
		pagedDefinition.addColumnDefinition(DESC_EN, I18N.message("desc.en"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(DESC, I18N.message("desc"), String.class, Align.LEFT, 200);
		
		EntityPagedDataProvider<StorageLocation> pagedDataProvider = new EntityPagedDataProvider<>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected StorageLocation getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(StorageLocation.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected AbstractSearchPanel<StorageLocation> createSearchPanel() {
		return new StorageLocationSearchPanel(this);
	}

}
