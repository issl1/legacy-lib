package com.nokor.efinance.gui.ui.panel.inbox.collection.ar;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.gui.ui.panel.inbox.collection.ar.batch.BatchTabPanel;
import com.nokor.efinance.gui.ui.panel.inbox.collection.ar.cashier.CashierTabPanel;
import com.nokor.efinance.gui.ui.panel.inbox.collection.ar.history.HistoryTabPanel;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(ARStaffPanel.NAME)
public class ARStaffPanel extends VerticalLayout implements View, SelectedTabChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2530991097528606026L;
	
	public static final String NAME = "ar.staff.dashboards";
	
	private TabSheet tabSheet;
	
	private CashierTabPanel cashierTabPanel;
	private BatchTabPanel batchTabPanel;
	private HistoryTabPanel historyTabPanel;
	
	public ARStaffPanel() {
		tabSheet = new TabSheet();
		tabSheet.addSelectedTabChangeListener(this);
		
		cashierTabPanel = new CashierTabPanel();
		batchTabPanel = new BatchTabPanel();
		historyTabPanel = new HistoryTabPanel();
		
		tabSheet.addTab(cashierTabPanel, I18N.message("cashier"));
		tabSheet.addTab(batchTabPanel, I18N.message("batch"));
		tabSheet.addTab(historyTabPanel, I18N.message("history"));
		
		addComponent(tabSheet);
	}

	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		if (tabSheet.getSelectedTab() == historyTabPanel) {
			historyTabPanel.assignValue();
		}
		
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
