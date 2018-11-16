package com.nokor.efinance.gui.ui.panel.contract.expenses;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;


/**
 * 
 * @author uhout.cheng
 */
public class ExpensesPanel extends AbstractControlPanel implements SelectedTabChangeListener {
	
	/** */
	private static final long serialVersionUID = 9033001255531196275L;
	
	private TabSheet tabSheet;
	
	private ExpensesTablePanel payableTablePanel;
	private ExpensesTablePanel refundsTablePanel;
	
	private Contract contract;
	
	/**
	 * 
	 */
	public ExpensesPanel() {
		payableTablePanel = new ExpensesTablePanel();
		refundsTablePanel = new ExpensesTablePanel();
		
		tabSheet = ComponentFactory.getTabSheet();
		tabSheet.addSelectedTabChangeListener(this);
		tabSheet.addTab(payableTablePanel, I18N.message("payable"));
		tabSheet.addTab(refundsTablePanel, I18N.message("refunds"));
	
		setMargin(true);
		addComponent(tabSheet);
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		this.contract = contract;
		payableTablePanel.assignValues(contract);
	}
	
	/**
	 * @see com.vaadin.ui.TabSheet.SelectedTabChangeListener#selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent)
	 */
	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		if (tabSheet.getSelectedTab().equals(refundsTablePanel)) {
			refundsTablePanel.assignValues(contract);
		}
	}
	
}
