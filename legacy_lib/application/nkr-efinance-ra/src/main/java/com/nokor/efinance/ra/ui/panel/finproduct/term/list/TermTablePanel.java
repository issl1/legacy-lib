package com.nokor.efinance.ra.ui.panel.finproduct.term.list;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.financial.model.Term;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Table.Align;

/**
 * Term Table Panel
 * @author bunlong.taing
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TermTablePanel extends AbstractTablePanel<Term> {
	/** */
	private static final long serialVersionUID = -3283052377224830578L;
	
	/** */
	@PostConstruct
	public void PostConstruct() {
		super.init(I18N.message("terms"));
		setCaption(I18N.message("terms"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		addDefaultNavigation();
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createPagedDataProvider()
	 */
	@Override
	protected PagedDataProvider<Term> createPagedDataProvider() {
		PagedDefinition<Term> pagedDefinition = new PagedDefinition<>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(Term.ID, I18N.message("id"), Long.class, Align.LEFT, 70);
		pagedDefinition.addColumnDefinition(Term.VALUE, I18N.message("value"), Integer.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(Term.DESCEN, I18N.message("desc.en"), String.class, Align.LEFT, 200);
		
		EntityPagedDataProvider<Term> pagedDataProvider = new EntityPagedDataProvider<>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected Term getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(Term.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected TermSearchPanel createSearchPanel() {
		return new TermSearchPanel(this);
	}

}
