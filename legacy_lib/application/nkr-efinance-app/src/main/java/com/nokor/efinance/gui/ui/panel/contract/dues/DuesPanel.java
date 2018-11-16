package com.nokor.efinance.gui.ui.panel.contract.dues;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.gui.ui.panel.contract.dues.loan.ChargesTabPanel;
import com.nokor.efinance.gui.ui.panel.contract.dues.loan.LoanInstallmentTablePanel;
import com.nokor.efinance.gui.ui.panel.contract.dues.summary.SummaryDuePanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.VerticalLayout;


/**
 * 
 * @author uhout.cheng
 */
public class DuesPanel extends AbstractControlPanel implements SelectedTabChangeListener, FinServicesHelper {
	
	/** */
	private static final long serialVersionUID = -5748064129061718548L;

	private TabSheet tabSheet;
	
	private SummaryDuePanel summaryDuePanel;
	private ChargesTabPanel chargesTabPanel;
	
	private LoanInstallmentTablePanel scheduleInstallmentTablePanel;
	
	private Contract contract;
	private VerticalLayout scheduleLayout;
	
	/**
	 * 
	 */
	public DuesPanel() {
		summaryDuePanel = new SummaryDuePanel();
		chargesTabPanel = new ChargesTabPanel();
		scheduleInstallmentTablePanel = new LoanInstallmentTablePanel();
		
		scheduleLayout = new VerticalLayout();
		scheduleLayout.setMargin(true);
		scheduleLayout.addComponent(scheduleInstallmentTablePanel);
		
		tabSheet = ComponentFactory.getTabSheet();
		tabSheet.addSelectedTabChangeListener(this);
		
		tabSheet.addTab(summaryDuePanel, I18N.message("summary"));
		tabSheet.addTab(chargesTabPanel, I18N.message("charges"));
		tabSheet.addTab(scheduleLayout, I18N.message("schedule"));
		
		addComponent(tabSheet);
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		this.contract = contract;
		tabSheet.setSelectedTab(summaryDuePanel);
		summaryDuePanel.assignValues(contract);
	}
	
	/**
	 * @see com.vaadin.ui.TabSheet.SelectedTabChangeListener#selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent)
	 */
	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		if (tabSheet.getSelectedTab().equals(chargesTabPanel)) {
			chargesTabPanel.assignValues(this.contract);
		} else if (tabSheet.getSelectedTab().equals(scheduleLayout)) {
			if (contract != null) {
				scheduleInstallmentTablePanel.assignValues(CASHFLOW_SRV.getDueTransactions(contract.getId(), null), contract);
			}
		}
	}
	
}
