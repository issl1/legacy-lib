package com.nokor.ersys.finance.accounting.ui.panel.main.list;

import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.ersys.finance.accounting.model.AccountLedger;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class AccountingSearchPanel extends AbstractSearchPanel<AccountLedger> {
	/** */
	private static final long serialVersionUID = -2897269362150507263L;

	/**
	 * @param tablePanel
	 */
	public AccountingSearchPanel(AccountingListPanel tablePanel) {
		super(I18N.message("search"), tablePanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		// TODO Auto-generated method stub
		return new VerticalLayout();
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<AccountLedger> getRestrictions() {
		// TODO Auto-generated method stub
		return new BaseRestrictions<AccountLedger>(AccountLedger.class);
	}

}
