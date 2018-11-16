package com.nokor.ersys.finance.accounting.ui.panel.main.journal.list;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.ersys.finance.accounting.model.JournalEntry;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.vaadin.ui.Component;

/**
 * Transaction Search Panel
 * @author bunlong.taing
 */
public class TransactionSearchPanel extends AbstractSearchPanel<JournalEntry> {
	/** */
	private static final long serialVersionUID = -7936701394063236678L;

	public TransactionSearchPanel(TransactionListPanel tablePanel) {
		super(I18N.message("search"), tablePanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<JournalEntry> getRestrictions() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		// TODO Auto-generated method stub
	}

}
