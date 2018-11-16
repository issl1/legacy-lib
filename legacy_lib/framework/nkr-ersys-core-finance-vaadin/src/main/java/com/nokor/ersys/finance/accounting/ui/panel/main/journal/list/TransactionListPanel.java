package com.nokor.ersys.finance.accounting.ui.panel.main.journal.list;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.ersys.core.finance.model.eref.ECurrency;
import com.nokor.ersys.finance.accounting.model.JournalEntry;
import com.nokor.ersys.finance.accounting.tools.helper.ErsysAccountingAppServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Table.Align;

/**
 * Transaction List Panel
 * @author bunlong.taing
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TransactionListPanel extends AbstractTablePanel<JournalEntry> implements ErsysAccountingAppServicesHelper {
	/** */
	private static final long serialVersionUID = -5349489490774042925L;
	
	/**
	 * 
	 */
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("transactions"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		super.init(I18N.message("transactions"));		
		addDefaultNavigation();
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createPagedDataProvider()
	 */
	@Override
	protected PagedDataProvider<JournalEntry> createPagedDataProvider() {
		PagedDefinition<JournalEntry> pagedDefinition = new PagedDefinition<JournalEntry>(searchPanel.getRestrictions());
		
		pagedDefinition.addColumnDefinition(JournalEntry.ID, I18N.message("id"), Long.class, Align.LEFT, 50);
		pagedDefinition.addColumnDefinition(JournalEntry.DESC, I18N.message("desc"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(JournalEntry.REFERENCE, I18N.message("reference"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(JournalEntry.AMOUNT, I18N.message("amount"), BigDecimal.class, Align.RIGHT, 100);
		pagedDefinition.addColumnDefinition(JournalEntry.CURRENCY + "." + ECurrency.CODE, I18N.message("currency"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(JournalEntry.WHEN, I18N.message("when"), Date.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(JournalEntry.INFO, I18N.message("information"), String.class, Align.LEFT, 200);
		
		EntityPagedDataProvider<JournalEntry> pagedDataProvider = new EntityPagedDataProvider<JournalEntry>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected JournalEntry getEntity() {
		if (selectedItem != null) {
			final Long id = (Long) selectedItem.getItemProperty(JournalEntry.ID).getValue();
		    return ENTITY_SRV.getById(JournalEntry.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected TransactionSearchPanel createSearchPanel() {
		return new TransactionSearchPanel(this);
	}

}
