package com.nokor.efinance.ra.ui.panel.collections.locksplittype;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.collection.model.ELockSplitType;
import com.nokor.efinance.core.shared.collection.CollectionEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Table.Align;

/**
 * Lock Split Type Table Panel
 * @author bunlong.taing
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LockSplitTypeTablePanel extends AbstractTablePanel<ELockSplitType> implements CollectionEntityField {

	/** */
	private static final long serialVersionUID = -8434098572544534933L;
	
	/**
	 * Post Construct
	 */
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("lock.split.types"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		super.init(I18N.message("lock.split.types"));
		addDefaultNavigation();
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createPagedDataProvider()
	 */
	@Override
	protected PagedDataProvider<ELockSplitType> createPagedDataProvider() {
		PagedDefinition<ELockSplitType> pagedDefinition = new PagedDefinition<ELockSplitType>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(CODE, I18N.message("code"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(DESC, I18N.message("desc"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(DESC_EN, I18N.message("desc.en"), String.class, Align.LEFT, 200);
		
		EntityPagedDataProvider<ELockSplitType> pagedDataProvider = new EntityPagedDataProvider<ELockSplitType>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected ELockSplitType getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(ELockSplitType.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected LockSplitTypeSearchPanel createSearchPanel() {
		return new LockSplitTypeSearchPanel(this);
	}

}
