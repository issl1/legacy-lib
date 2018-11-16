package com.nokor.efinance.gui.ui.panel.accounting.journal.entry.list;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.ersys.finance.accounting.model.TransactionEntry;
import com.nokor.ersys.finance.accounting.service.TransactionEntryRestriction;
import com.nokor.ersys.finance.accounting.tools.helper.ErsysAccountingAppServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * JournalEntry Search Panel
 * @author bunlong.taing
 */
public class TransactionEntrySearchPanel extends AbstractSearchPanel<TransactionEntry> implements ErsysAccountingAppServicesHelper {
	
	/**
	 */
	private static final long serialVersionUID = -8986758424937862053L;
		
	/**
	 * @param tablePanel
	 */
	public TransactionEntrySearchPanel(TransactionEntryTablePanel tablePanel) {
		super(I18N.message("search"), tablePanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		Label lblSearchText = ComponentFactory.getLabel("contract.no");
		Label lblPayeeName = ComponentFactory.getLabel("payee.name");
				
		// gridLayout.setComponentAlignment(lblSearchText, Alignment.MIDDLE_LEFT);
		// gridLayout.setComponentAlignment(lblPayeeName, Alignment.MIDDLE_LEFT);
		
		VerticalLayout content = new VerticalLayout();
		content.setSpacing(true);
		return content;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<TransactionEntry> getRestrictions() {
		TransactionEntryRestriction restrictions = new TransactionEntryRestriction();
		// restrictions.setContractNo(txtContractNo.getValue());
		// restrictions.setPayeeName(txtPayeeName.getValue());		
		return restrictions;
	}
	
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
	}
	
}
