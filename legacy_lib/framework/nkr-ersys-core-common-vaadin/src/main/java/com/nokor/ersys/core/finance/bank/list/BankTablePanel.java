package com.nokor.ersys.core.finance.bank.list;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.ersys.core.finance.model.Bank;
import com.nokor.ersys.core.finance.model.MBank;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Table.Align;

/**
 * BankInfo Table Panel
 * @author bunlong.taing
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class BankTablePanel extends AbstractTablePanel<Bank> implements MBank {

	/** */
	private static final long serialVersionUID = 1042647624741818122L;
	
	/**
	 * BankInfo Table Panel post constructor
	 */
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("banks"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		super.init(I18N.message("banks"));		
		addDefaultNavigation();
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createPagedDataProvider()
	 */
	@Override
	protected PagedDataProvider<Bank> createPagedDataProvider() {
		PagedDefinition<Bank> pagedDefinition = new PagedDefinition<Bank>(searchPanel.getRestrictions());
		
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(Bank.DESCEN, I18N.message("name.en"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(Bank.NAME, I18N.message("branch.name"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(Bank.CODE, I18N.message("branch.code"), String.class, Align.LEFT, 200);
		
		EntityPagedDataProvider<Bank> pagedDataProvider = new EntityPagedDataProvider<Bank>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected Bank getEntity() {
		if (selectedItem != null) {
			final Long id = (Long) selectedItem.getItemProperty(ID).getValue();
		    return ENTITY_SRV.getById(Bank.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected AbstractSearchPanel<Bank> createSearchPanel() {
		return new BankSearchPanel(this);
	}

}
