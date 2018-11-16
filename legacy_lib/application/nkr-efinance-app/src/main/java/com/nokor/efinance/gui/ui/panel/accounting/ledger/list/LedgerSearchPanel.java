package com.nokor.efinance.gui.ui.panel.accounting.ledger.list;

import org.hibernate.criterion.Order;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.ersys.finance.accounting.model.AccountLedger;
import com.nokor.ersys.finance.accounting.service.AccountLedgerRestriction;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;

/**
 * Ledger Search Panel
 * @author bunlong.taing
 */
public class LedgerSearchPanel extends AbstractSearchPanel<AccountLedger> {
	/** */
	private static final long serialVersionUID = -260447104676825829L;
	
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;

	/**
	 * @param tablePanel
	 */
	public LedgerSearchPanel(AbstractTablePanel<AccountLedger> tablePanel) {
		super(I18N.message("search"), tablePanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		dfStartDate = ComponentFactory.getAutoDateField();
		dfEndDate = ComponentFactory.getAutoDateField();
		
		Label lblStartDate = ComponentFactory.getLabel("start.date");
		Label lblEndDate = ComponentFactory.getLabel("end.date");
		
		GridLayout content = new GridLayout(4, 1);
		content.setSpacing(true);
		content.addComponent(lblStartDate);
		content.addComponent(dfStartDate);
		content.addComponent(lblEndDate);
		content.addComponent(dfEndDate);
		
		content.setComponentAlignment(lblStartDate, Alignment.MIDDLE_LEFT);
		content.setComponentAlignment(lblEndDate, Alignment.MIDDLE_LEFT);
		
		return content;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<AccountLedger> getRestrictions() {
		AccountLedgerRestriction restriction = new AccountLedgerRestriction();
		restriction.setStartDate(dfStartDate.getValue());
		restriction.setEndDate(dfEndDate.getValue());
		restriction.addOrder(Order.desc(AccountLedger.WHEN));
		return restriction;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		dfStartDate.setValue(null);
		dfEndDate.setValue(null);
	}

}
