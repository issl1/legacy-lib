package com.nokor.efinance.ra.ui.panel.collections.locksplitrule;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.collection.model.LockSplitRule;
import com.nokor.efinance.core.shared.collection.CollectionEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Table.Align;

/**
 * Lock Split Rule Table Panel
 * @author bunlong.taing
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LockSplitRuleTablePanel extends AbstractTablePanel<LockSplitRule> implements CollectionEntityField {

	/** */
	private static final long serialVersionUID = 1655229358134676323L;
	
	@Autowired
	private EntityService entityService;
	
	/**
	 * Post Contructor
	 */
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("lock.split.rules"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		super.init(I18N.message("lock.split.rules"));
		addDefaultNavigation();
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createPagedDataProvider()
	 */
	@Override
	protected PagedDataProvider<LockSplitRule> createPagedDataProvider() {
		PagedDefinition<LockSplitRule> pagedDefinition = new PagedDefinition<LockSplitRule>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(LockSplitRule.ID, I18N.message("id"), Long.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(LockSplitRule.DESCEN, I18N.message("name.en"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(LockSplitRule.DESC, I18N.message("name"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(DEFAULT, I18N.message("is.default"), Boolean.class, Align.LEFT, 100);
		
		EntityPagedDataProvider<LockSplitRule> pagedDataProvider = new EntityPagedDataProvider<>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected LockSplitRule getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return entityService.getById(LockSplitRule.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected AbstractSearchPanel<LockSplitRule> createSearchPanel() {
		return new LockSplitRuleSearchPanel(this);
	}

}
